package com.vranec.csv.exporter;

import com.vranec.jpa.model.IssueModel;
import com.vranec.jpa.repository.IssuesRepository;
import com.vranec.jpa.repository.TimeLogRepository;
import com.vranec.timesheet.generator.Configuration;
import com.vranec.timesheet.generator.TaskReporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Slf4j
@Component
@Primary
public class CsvGridReporter implements TaskReporter {

    @Autowired
    private Configuration configuration;

    @Autowired
    private TimeLogRepository timeLogRepo;
    @Autowired
    private IssuesRepository issuesRepo;


    private GridExporter exporter;

     private void saveToCsv() {
        try {
            exporter = new ExcelGridExporter();
            writeCsv();
            writeStatCsv();
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

    private void writeStatCsv() throws IOException {
    	exporter.createSheet("Statistics");
    	exporter.setStyle(GridExporter.STYLE_HEADER);
    	exporter.print("User");
    	exporter.print("Time");
    	exporter.setStyle(GridExporter.STYLE_BODY);
        exporter.println();

        timeLogRepo.getStatisticsByResource().forEach(stat -> printStat(stat));
    }

    private void printStat(Object[] stat) {
        String author = (String)stat[0];
        Long time = (Long)stat[1];
        log.info("STAT: {} --> {}", author, time);
        exporter.print(author);
        double value = ((float) time) / 60;
        printDouble(value);
        exporter.println();
    }

    private void printDouble(double value) {
        exporter.printNumber(value);
    }

    /**
     *
     * @throws IOException
     */
    private void writeCsv() throws IOException {
    	log.info("Saving to CSV...");
        printCsvHeader();
        for (Integer date: timeLogRepo.allDays()) {
        	exporter.setStyle(GridExporter.STYLE_HIGHLIGHT);
        	exporter.print(date.toString());
        	exporter.setStyle(GridExporter.STYLE_BODY);

            for (String user: configuration.getResources()) {
                Integer workload = timeLogRepo.getWorkload(user, date);
                List<String> issues = timeLogRepo.getIssues(user, date);
            	printDouble(((workload == null) ? 0 : workload) / 60.0);
            	exporter.print(String.join(",", issues));
            }
            exporter.println();
        }
       	writeTasks();
    }
    private void writeTasks() throws IOException {
    	exporter.createSheet("Tasks");
    	exporter.setStyle(GridExporter.STYLE_HEADER);
    	exporter.print("Task");
    	exporter.setStyle(GridExporter.STYLE_BODY);
        exporter.println();

        timeLogRepo.getTasksWithWorkload().forEach(task -> writeTask(task));

    }

    private void writeTask(Object[] task) {
        if (task.length != 2 || task[0] == null || task[0].getClass() != IssueModel.class
                || task[1] == null || task[1].getClass() != Long.class) {
            StringBuilder sb  = new StringBuilder();
            sb.append("Wrong task definition:\n");
            if (task == null) {
                sb.append("-task == null");
            } else if (task.length != 2) {
                sb.append("-length == " + task.length);
            } else {
                sb.append("[0].class=" + task[0] == null ? null : task[0].getClass());
                sb.append("[1].class=" + task[1] == null ? null : task[1].getClass());
            }
            throw new IllegalStateException(sb.toString());
        }
        IssueModel issue = (IssueModel) (task[0]);
        Long minutes = (Long) (task[1]);
        exporter.print(issue.getIssueId());
        exporter.print(issue.getSummary());
        printDouble(minutes.doubleValue() / 60.0);
        exporter.print(issue.getAssignee());
        exporter.print(issue.getStatus());
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
    public void report() {
        saveToCsv();
    }


}
