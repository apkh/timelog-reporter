package com.vranec.jira.gateway;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.vranec.jpa.model.IssueModel;
import com.vranec.jpa.model.TimeLog;
import com.vranec.jpa.repository.IssuesRepository;
import com.vranec.jpa.repository.TimeLogRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.vranec.timesheet.generator.Configuration;
import com.vranec.timesheet.generator.TaskSource;

import lombok.extern.slf4j.Slf4j;
import net.rcarz.jiraclient.ICredentials;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.WorkLog;

@Slf4j
public class CustomJiraClient extends JiraClient implements TaskSource {

    private Date startDate;
    private Date endDate;

    @Autowired
    private Configuration configuration;
    @Autowired
    private TimeLogRepository timeLogRepo;
    @Autowired
    private IssuesRepository issuesRepo;

    CustomJiraClient(String uri, ICredentials creds) throws JiraException {
        super(uri, creds);
    }

    private Issue.SearchResult searchIssues(String jql, Integer maxResults, String expand) {
        try {
            Issue.SearchResult sr = new Issue.SearchResult(getRestClient(), jql,
                    "summary,comment,assignee,status,updated", expand, maxResults, 0 );

            return sr;
        } catch (JiraException e) {
            throw new IllegalStateException("JSON payload is malformed");
        }
    }

    public void getTasks(LocalDate localStartDate, LocalDate localEndDate) {
        startDate = Date.from(localStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        endDate = Date.from(localEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//        String qlAssignee = String.join(", ", configuration.getResources());
        String qlProject = String.join(", ", configuration.getProjects());
        String jql = "project in (" + qlProject + ")" +
        		" AND updated >= '" + DateTimeFormatter.ofPattern("yyyy-M-d").format(localStartDate) + "'";
        		//" AND updated >= '" + DateTimeFormatter.ofPattern("yyyy-M-d").format(localEndDate) +
        		//"' and assignee in (" + qlAssignee + ")";

        log.info("Searching for issues by JQL: " + jql + "...");
        Issue.SearchResult result = searchIssues(jql, 1000, "changelog");
        processIssues(result);
    }

    /*
     * We need to create a map <Date, Map<User, Pair<TaskKey, Float>>> 
     */
    private void processIssues(Issue.SearchResult result) {
        if (result == null) {
            return;
        }
        timeLogRepo.deleteAll();
        issuesRepo.deleteAll();

        for (Issue issue : result.issues) {
            IssueModel persistingIssue = IssueModel.builder()
                    .issueId(issue.getKey())
                    .assignee(issue.getAssignee() == null
                            ? ""
                            : issue.getAssignee().getName())
                    .updateDate(issue.getUpdatedDate())
                    .summary(issue.getSummary())
                    .status(issue.getStatus().getName())
                    .build();
            issuesRepo.save(persistingIssue);
            HashMap<String, Integer> timeMap = new HashMap<>();
            try {
                List<WorkLog> allWorkLogs = issue.getAllWorkLogs();
                log.info("Issue {}", issue.getKey());
                for (WorkLog wl : allWorkLogs) {
                    if (!wl.getStarted().before(startDate) &&
                    		!wl.getStarted().after(endDate)) {
                        int minutes = wl.getTimeSpentSeconds() / 60;

                        if (minutes >= configuration.getMinimumWlTime()) {
                            String userName = getUserName(issue, wl);

                            log.info("* time: {} -- {}", wl.getAuthor(), (minutes > 60) ? ((minutes / 60) + " h " + (minutes % 60) + " m") : (minutes + " m"));
                            timeLogRepo.save(TimeLog.builder()
                                    .reportTime(wl.getTimeSpentSeconds() / 60)
                                    .date(wl.getStarted())
                                    .resource(userName)
                                    .issue(persistingIssue)
                                    .build());
                        }
                    }
                }
            } catch (JiraException e) {
                log.error("No worklog at {} ", issue.getKey());
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

 }
