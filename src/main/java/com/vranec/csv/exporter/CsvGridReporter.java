package com.vranec.csv.exporter;

import com.vranec.jira.gateway.WorkloadMap;
import com.vranec.jpa.repository.IssuesRepository;
import com.vranec.jpa.repository.TimeLogRepository;
import com.vranec.timesheet.generator.Configuration;
import com.vranec.timesheet.generator.ReportableTask;
import com.vranec.timesheet.generator.TaskReporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Primary
public class CsvGridReporter implements TaskReporter {
    @Autowired
    WorkloadMap wlMap;
    
    @Autowired
    private Configuration configuration;

    @Autowired
    private TimeLogRepository timeLogRepo;
    @Autowired
    private IssuesRepository issuesRepo;


    private GridExporter exporter;

     private void saveToCsv(List<ReportableTask> timesheet, Map<String, Integer> statistics) {
        try {
            exporter = new ExcelGridExporter();
            writeCsv(timesheet);
            writeStatCsv(statistics);
        } catch (IOException e) {
            log.error("FATAL ERROR: {} {}", e.getMessage());
        } finally {
            try {
				exporter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }

    private void writeStatCsv(Map<String, Integer> statistics) throws IOException {
    	exporter.createSheet("Statistics");
    	exporter.setStyle(GridExporter.STYLE_HEADER);
    	exporter.print("User");
    	exporter.print("Time");
    	exporter.setStyle(GridExporter.STYLE_BODY);
        exporter.println();
        for (String author: statistics.keySet()) {
            log.info("STAT: {} --> {}", author, statistics.get(author));
            exporter.print(author);
            double value = ((float) statistics.get(author)) / 60;
            printDouble(value);
            exporter.println();
        }

    }

    private void printDouble(double value) throws IOException {
        exporter.printNumber(value);
    }

    /**
     *
     * @param timesheet
     * @throws IOException
     */
    private void writeCsv(List<ReportableTask> timesheet) throws IOException {
    	log.info("Saving to CSV...");
        printCsvHeader();
        for (Integer date: timeLogRepo.allDays()) {
        	exporter.setStyle(GridExporter.STYLE_HIGHLIGHT);
        	exporter.print(date.toString());
        	exporter.setStyle(GridExporter.STYLE_BODY);

            for (String user: configuration.getResources()) {
                Integer workload = timeLogRepo.getWorkload(user, date);
                Map<String, Float> tasks = wlMap.getTasks(date, user);
            	float sum = 0;
            	for (String task: tasks.keySet()) {
            		sum += tasks.get(task);
            	}
            	printDouble(sum);
            	exporter.print(String.join(",", tasks.keySet()));
            }
            exporter.println();
        }
       	writeTasks(timesheet);
    }
    private void writeTasks(List<ReportableTask> timesheet) throws IOException { 
    	exporter.createSheet("Tasks");
    	exporter.setStyle(GridExporter.STYLE_HEADER);
    	exporter.print("Task");
    	exporter.setStyle(GridExporter.STYLE_BODY);
        exporter.println();

        for (String user: configuration.getResources()) {
        	for (ReportableTask task : timesheet) {
        		if (user.equals(task.getResource())) {
        			writeTask(task);
        		}
        	}
        }
    }

    private void writeTask(ReportableTask task) throws IOException {
        exporter.print(task.getKey());
        exporter.print(task.getSummary());
        printDouble((float)task.getMinutes() / 60.0);
        exporter.print(task.getResource());
        exporter.print(task.getStatus());
        exporter.println();
    }

    private void printCsvHeader() throws IOException {
    	exporter.createSheet("Timesheet");
    	exporter.setStyle(GridExporter.STYLE_HEADER);
    	exporter.print("Date");
        for (String user: configuration.getResources()) {
        	exporter.print(user);
        	exporter.print("");
        }
        exporter.println();
    	exporter.print("");
    	for (@SuppressWarnings("unused") String user: configuration.getResources()) {
        	exporter.print("Hours");
        	exporter.print("Tasks");
        }
        exporter.println();
    	exporter.setStyle(GridExporter.STYLE_BODY);

    }



    @Override
    public void report(List<ReportableTask> timesheet, Map<String, Integer> statistics) {
        saveToCsv(timesheet, statistics);
        log.info("timesheet: {}", timesheet);
    }


}
