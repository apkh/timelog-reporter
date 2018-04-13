package com.vranec.configuration;

import com.vranec.controller.WebController;
import com.vranec.csv.exporter.CsvGridReporter;
import com.vranec.jira.gateway.CustomJiraClient;
import com.vranec.timesheet.generator.TimeSpentGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {TimeSpentGenerator.class,
        ConfigurationFromPropertiesFile.class,
        CustomJiraClient.class,
        CsvGridReporter.class,
        WebController.class})
public class SpringContextConfiguration {
}
