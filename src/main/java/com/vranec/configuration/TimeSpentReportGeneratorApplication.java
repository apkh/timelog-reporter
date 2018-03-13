package com.vranec.configuration;

import com.vranec.timesheet.generator.TimeSpentGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * Entry point of the application. Starts {@link TimeSpentGenerator} automatically (using @{@link javax.annotation.PostConstruct}.
 */
@SpringBootApplication
public class TimeSpentReportGeneratorApplication {
    @Autowired
    private TimeSpentGenerator timeSpentGenerator;

    public static void main(String[] args) {
        SpringApplication.run(TimeSpentReportGeneratorApplication.class, args);
    }

    @PostConstruct
    public void main() throws Exception {
        timeSpentGenerator.generateTimesheet();
    }
}
