package com.vranec.csv.exporter;

import com.vranec.jira.gateway.WorkloadMap;
import com.vranec.timesheet.generator.Configuration;
import com.vranec.timesheet.generator.ReportableTask;
import com.vranec.timesheet.generator.TaskReporter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
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

     private void saveToCsv(List<ReportableTask> timesheet, Map<String, Integer> statistics) {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream("export.csv"), "UTF-8");//new FileWriter("export.csv");
            writeCsv(timesheet, writer);
            writeStatCsv(statistics, writer);
            writer.close();
        } catch (IOException e) {
            log.error("FATAL ERROR: {} {}", e.getMessage());
        } finally {
            closeWriter(writer);
        }
    }

    private void writeStatCsv(Map<String, Integer> statistics, Writer writer) throws IOException {
        CSVPrinter csv = createCsvPrinter(writer);
        for (String author: statistics.keySet()) {
            log.info("STAT: {} --> {}", author, statistics.get(author));
            csv.print(author);
            csv.print(((float)statistics.get(author))/60);
            csv.println();
        }
        csv.close();

    }
/**
 * Prints
 * Date,User1,,User2,,User3
 * 1.01.2001,8,LK-102;LK-103;
 * 2.02.....
 * @param timesheet
 * @param writer
 * @throws IOException
 */
    private void writeCsv(List<ReportableTask> timesheet, Writer writer) throws IOException {
    	log.info("Saving to CSV...");
        CSVPrinter csv = createCsvPrinter(writer);
        printCsvHeader(csv);
        for (Integer date: wlMap.getDates()) {
        	csv.print(date);

            for (String user: configuration.getResources()) {
            	Map<String, Float> tasks = wlMap.getTasks(date, user);
            	float sum = 0;
            	for (String task: tasks.keySet()) {
            		sum += tasks.get(task);
            	}
            	csv.print(sum == 0.0 ? "" : sum);
            	csv.print(String.join(",", tasks.keySet()));
            }
            csv.println();
        }
       	writeTasks(csv, timesheet);
    }
    private void writeTasks(CSVPrinter csv, List<ReportableTask> timesheet) throws IOException {
        for (String user: configuration.getResources()) {
        	for (ReportableTask task : timesheet) {
        		if (user.equals(task.getResource())) {
        			writeTask(csv, task);
        		}
        	}
        }
    }

    private void writeTask(CSVPrinter csv, ReportableTask task) throws IOException {
        csv.print(task);
        csv.println();
    }

    private void printCsvHeader(CSVPrinter csv) throws IOException {
        csv.print("Date");
        for (String user: configuration.getResources()) {
        	csv.print(user);
        	csv.print("");
        }
        csv.print("");
        for (String user: configuration.getResources()) {
        	csv.print("Hours");
        	csv.print("Tasks");
        }
        csv.println();
    }

    private CSVPrinter createCsvPrinter(Writer writer) throws IOException {
        return new CSVPrinter(writer, CSVFormat.EXCEL.withDelimiter(','));
    }

    private void closeWriter(Writer writer) {
        if (writer == null) {
            return;
        }
        try {
            writer.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void report(List<ReportableTask> timesheet, Map<String, Integer> statistics) {
        saveToCsv(timesheet, statistics);
        log.info("timesheet: {}", timesheet);
    }
}
