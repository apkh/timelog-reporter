package com.vranec.timesheet.generator;

import static java.time.LocalDate.now;

import java.util.*;

import com.vranec.jpa.model.TimeLog;
import com.vranec.jpa.repository.TimeLogRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Component
public class TimeSpentGenerator {
    private final Configuration configuration;
    private final TaskSource taskSource;
    private final TaskReporter reporter;
    @Autowired
    private TimeLogRepository timeLogRepo;

    public void generateTimesheet() {
    	try {
	    	// TODO Rework to specified date
	        val startDate = now().minusDays(configuration.getMonthDetectionSubtractDays()).withDayOfMonth(1);
	        val endDate = startDate.plusDays(startDate.lengthOfMonth());
	        log.info("START TIMESHEET GENERATION SINCE {} till {}", startDate, endDate);
	        taskSource.getTasks(startDate, endDate);

	        reporter.report();
	        //exporter.export(timesheet);
	        log.info("TIMESHEET GENERATED SUCCESSFULLY");
    	} catch (Exception e) {
    		log.error("Unexpected exception caught {}", e);
    		throw new IllegalStateException(e);
    	}
    }

}
