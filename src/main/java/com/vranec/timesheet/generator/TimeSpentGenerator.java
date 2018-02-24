package com.vranec.timesheet.generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.time.LocalDate.now;

@Slf4j
@RequiredArgsConstructor
@Component
public class TimeSpentGenerator {
    private final Configuration configuration;
    private final Exporter exporter;
    private final TaskSource taskSource;
    private final TaskReporter reporter;

    static HashSet resourcesSet = new HashSet(Arrays.asList(new String[]{"p.bogatyrev", "f.frolov", "d.sychugov"}));

    public void generateTimesheet() throws Exception {
        val startDate = now().minusDays(configuration.getMonthDetectionSubtractDays()).withDayOfMonth(1);
        log.info("START TIMESHEET GENERATION SINCE {}", startDate);
        Iterable<ReportableTask> tasks = taskSource.getTasks(startDate);
        val timesheet = parseTasks(tasks, resourcesSet);
        reporter.report(timesheet, taskSource.getStatistics());
        //exporter.export(timesheet);
        log.info("TIMESHEET GENERATED SUCCESSFULLY");
    }


    private List<ReportableTask> parseTasks(Iterable<ReportableTask> tasks, Set<String> resources) {
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
