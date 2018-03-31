package com.vranec.jira.gateway;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.vranec.timesheet.generator.Configuration;
import com.vranec.timesheet.generator.ReportableTask;
import com.vranec.timesheet.generator.TaskSource;

import lombok.Getter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import net.rcarz.jiraclient.ICredentials;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.Resource;
import net.rcarz.jiraclient.WorkLog;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

@Slf4j
public class CustomJiraClient extends JiraClient implements TaskSource {

    private Collection<ReportableTask> reportableTasks;
    private HashMap<String, Integer> statistics;
    private Date startDate;
    private Date endDate;
    // Date -> User -> {task, time}*
    @Autowired
    private WorkloadMap workloadMap; 
    
    @Autowired
    private Configuration configuration;

    CustomJiraClient(String uri, ICredentials creds) throws JiraException {
        super(uri, creds);
        statistics = new HashMap<String, Integer>();

    }

    public Map<String, Integer> getStatistics() {
        return statistics;
    }

    private Issue.SearchResult searchIssues(String jql, Integer maxResults, String expand) {
        final String j = jql;
        JSON result;

        try {
            Map<String, String> queryParams = new HashMap<String, String>() {
                {
                    put("jql", j);
                }
            };
            if (maxResults != null) {
                queryParams.put("maxResults", String.valueOf(maxResults));
            }
            queryParams.put("fields", "summary,comment,assignee");
            if (expand != null) {
                queryParams.put("expand", expand);
            }

//            URI searchUri = getRestClient().buildURI(Resource.getBaseUri() + "search", queryParams);
//            result = getRestClient().get(searchUri);
//        } catch (IOException ex) {
//            log.error("Cannot connect to JIRA server {}", ex.getMessage());
//            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }

/*
        if (!(result instanceof JSONObject)) {
            throw new IllegalStateException("JSON payload is malformed");
        }
log.info("JSON: {}", result);
*/

        try {
            Issue.SearchResult sr = new Issue.SearchResult(getRestClient(), jql, "summary,comment,assignee", expand, maxResults, 0 );
/*
            Map map = (Map) result;

            sr.start = Field.getInteger(map.get("startAt"));
            sr.max = Field.getInteger(map.get("maxResults"));
            sr.total = Field.getInteger(map.get("total"));
            sr.issues = Field.getResourceArray(Issue.class, map.get("issues"), getRestClient());
*/

            return sr;
        } catch (JiraException e) {
            throw new IllegalStateException("JSON payload is malformed");
        }
    }

    public Iterable<ReportableTask> getTasks(LocalDate localStartDate, LocalDate localEndDate) {
        startDate = Date.from(localStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        endDate = Date.from(localEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        String qlAssignee = String.join(", ", configuration.getResources());
        String qlProject = String.join(", ", configuration.getProjects());
        String jql = "project in (" + qlProject + ")" +
        		" AND updated >= '" + DateTimeFormatter.ofPattern("yyyy-M-d").format(localStartDate) + "'";
        		//" AND updated >= '" + DateTimeFormatter.ofPattern("yyyy-M-d").format(localEndDate) +
        		//"' and assignee in (" + qlAssignee + ")";
        log.info("Searching for issues by JQL: " + jql + "...");
        Issue.SearchResult result = searchIssues(jql, 1000, "changelog");
        processIssues(result);
        return reportableTasks;
    }

    /*
     * We need to create a map <Date, Map<User, Pair<TaskKey, Float>>> 
     */
    private void processIssues(Issue.SearchResult result) {
        reportableTasks = new ArrayList<>();
        statistics.clear();

        if (result == null) {
            return;
        }

        for (Issue issue : result.issues) {
            List<WorkLog> allWorkLogs = null;
            HashMap<String, Integer> timeMap = new HashMap<>();
            try {
                allWorkLogs = issue.getAllWorkLogs();
                log.info("Issue {}", issue.getKey());
                for (WorkLog wl : allWorkLogs) {
                    if (!wl.getStarted().before(startDate) &&
                    		!wl.getStarted().after(endDate)) {
                        int min = wl.getTimeSpentSeconds() / 60;

                        if (min >= configuration.getMinimumWlTime()) {
                            String userName = getUserName(issue, wl);
                            addTimeToIndex(timeMap, userName, min);
                            workloadMap.addWl(wl.getStarted(), issue.getKey(), issue.getSummary(), userName, min);
                            log.info("* time: {} -- {}", wl.getAuthor(), (min > 60) ? ((min / 60) + " h " + (min % 60) + " m") : (min + " m"));
                        }
                    }
                }
            } catch (JiraException e) {
                log.error("No worklog at {} ", issue.getKey());
            }
            for (String author : timeMap.keySet()) {
                reportableTasks.add(processIssues(issue, author, timeMap.get(author)));
                addTimeToIndex(statistics, author, timeMap.get(author));
            }
        }
    }

    private String getUserName(Issue issue, WorkLog wl) {
        String issueUserName = issue.getAssignee() == null ? null : issue.getAssignee().getName();
        String wlUserName = wl.getAuthor().getName();
        // Belongs supervisor iff both WL and Issue assigned to them or
        // WL -> resource; Issue -> * ==>> resource
        // WL -> *; Issue -> resource
        // WL != SV || Issue == null ==> User = wl.user
        // WL == SV && Issue != null ==> User = issue.user

        return wlUserName.equals(configuration.getSupervisorName()) && !issueUserName.equals(null)
                ? issueUserName
                : wlUserName;

    }

    private static void addTimeToIndex(HashMap<String, Integer> map, String index, Integer integer) {
        if (map.containsKey(index)) {
            Integer time = map.get(index);
            map.put(index, time + integer);
        } else {
            map.put(index, integer);
        }
    }

    private ReportableTask processIssues(Issue issue, String author, int time) {
        return ReportableTask.builder()
                .key(issue.getKey())
                .summary(issue.getSummary())
                .resource(author)
                .minutes(time)
                .build();
    }
}
