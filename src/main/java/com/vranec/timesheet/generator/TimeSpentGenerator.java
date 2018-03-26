package com.vranec.timesheet.generator;

import static java.time.LocalDate.now;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class TimeSpentGenerator {
    private final Configuration configuration;
    private final TaskSource taskSource;
    private final TaskReporter reporter;

    public void generateTimesheet() throws Exception {
    	try {
	    	// TODO Rework to specified date
	        val startDate = now().minusDays(configuration.getMonthDetectionSubtractDays()).withDayOfMonth(1);
	        val endDate = startDate.plusDays(startDate.lengthOfMonth());
	        log.info("START TIMESHEET GENERATION SINCE {} till {}", startDate, endDate);
	        Iterable<ReportableTask> tasks = taskSource.getTasks(startDate, endDate);
	        val timesheet = filterTasksByResourceNames(tasks, configuration.getResources());
	        reporter.report(timesheet, taskSource.getStatistics());
	        //exporter.export(timesheet);
	        log.info("TIMESHEET GENERATED SUCCESSFULLY");
    	} catch (Exception e) {
    		log.error(e.getMessage());
    	}
    }


    private List<ReportableTask> filterTasksByResourceNames(Iterable<ReportableTask> tasks, Collection<String> resources) {
        List<ReportableTask> result = new ArrayList<>();

        tasks.forEach(task -> {
            log.info("Parsing {}", task);
            if (resources.contains(task.getResource())) {
                result.add(task);
            }
        });

        return result;
    }

    /*private List<TasksForDay> parseTimesheetFrom(LocalDate startDate) {
        Iterable<Task> tasks = taskSource.getTasks(startDate);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        if (endDate.isAfter(now())) {
            endDate = now();
        }
        List<TasksForDay> tasksForDays = new ArrayList<>();
        ConcurrentMap<LocalDate, TasksForDay> timesheet = new ConcurrentHashMap<>();
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            TasksForDay tasksForDay = new TasksForDay(date);
            tasksForDays.add(tasksForDay);
            timesheet.putIfAbsent(date, tasksForDay);
        }

        tasks.forEach(task -> {
            log.info("Parsing {}", task.getName());
            if (task.getAuthor().equals(configuration.getJiraUsername().split("@")[0])) {
                if (timesheet.containsKey(task.getDate())) {
                    timesheet.get(task.getDate()).addTask(task.getName());
                }
            }
        });
        return tasksForDays;
    }*/
}
