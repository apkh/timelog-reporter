package com.vranec.configuration;

import com.vranec.timesheet.generator.TimeSpentGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;

/**
 * Entry point of the application. Starts {@link TimeSpentGenerator} automatically (using @{@link javax.annotation.PostConstruct}.
 */
@SpringBootApplication
public class TimeSpentReportGeneratorApplication extends SpringBootServletInitializer {
    @Autowired
    private TimeSpentGenerator timeSpentGenerator;

    public static void main(String[] args) {
        SpringApplication.run(TimeSpentReportGeneratorApplication.class, args);
    }

    @PostConstruct
    @Async
    public void main() throws Exception {
        timeSpentGenerator.generateTimesheet();
    }
}
