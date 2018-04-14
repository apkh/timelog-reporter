package com.vranec.configuration;

import com.vranec.controller.WebController;
import com.vranec.csv.exporter.CsvGridReporter;
import com.vranec.jira.gateway.CustomJiraClient;
import com.vranec.timesheet.generator.TimeSpentGenerator;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.vranec.jpa.repository")
@ComponentScan(basePackageClasses = {TimeSpentGenerator.class,
        ConfigurationFromPropertiesFile.class,
        CustomJiraClient.class,
        CsvGridReporter.class,
        WebController.class})
@EntityScan("com.vranec.jpa.model")
public class SpringContextConfiguration {
}
