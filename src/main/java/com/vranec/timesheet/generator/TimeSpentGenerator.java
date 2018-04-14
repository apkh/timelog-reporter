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
    		log.error("Unexpected exception caught {}", e);
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

}
