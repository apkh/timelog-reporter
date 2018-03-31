package com.vranec.configuration;

import com.vranec.timesheet.generator.Configuration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Getter
@Component
@Order(1)
@PropertySource(value = "file:timesheet-generator.properties", ignoreResourceNotFound = true)
public class ConfigurationFromPropertiesFile implements Configuration {
    @Value("${jira.url:}")
    private String jiraUrl;
    @Value("${jira.username:}")
    private String jiraUsername;
    @Value("${jira.password:}")
    private String jiraPassword;
    @Value("${jira.resources:}")
    private String jiraResources;
    @Value("${jira.projects:}")
    private String jiraProjects;
    @Value("${jira.supervisor:}")
    private String supervisorName;
    @Value("${jira.minimumWlTime:5}")
    private int minimumWlTime;
    @Value("${month.detection.subtract.days:15}")
    private int monthDetectionSubtractDays;
    @Value("${ignore.invalid.server.certificate:false}")
    private boolean ignoreInvalidServerCertificate;
    @Value("${proxy.url:}")
    private String proxyUrl;
    @Value("${proxy.port:0}")
    private int proxyPort;
    @Value("${proxy.username:}")
    private String proxyUsername;
    @Value("${proxy.password:}")
    private String proxyPassword;

    @PostConstruct
    public void createConfigurationFileIfNotExists() throws IOException {
        File configurationFile = new File("timesheet-generator.properties");
        if (!configurationFile.exists()) {
            Files.copy(getClass().getResourceAsStream("/default.properties"), configurationFile.toPath());
            log.error("Please edit the generated timesheet-generator.properties");
        }
    }
    
    public List getResources() {
    	return new ArrayList<String>(Arrays.asList(jiraResources.split(",")));

    }    
    public List getProjects() {
    	return new ArrayList<String>(Arrays.asList(jiraProjects.split(",")));

    }
}
