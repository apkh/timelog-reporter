package com.vranec.csv.exporter;

import com.vranec.timesheet.generator.ReportableTask;
import com.vranec.timesheet.generator.TaskReporter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CsvReporter implements TaskReporter {
     private void saveToCsv(List<ReportableTask> timesheet, Map<String, Integer> statistics) {
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream("export.csv"), "UTF-8");//new FileWriter("export.csv");
            writeCsv(timesheet, writer);
            writeStatCsv(statistics, writer);
            writer.close();
        } catch (IOException e) {
            log.error("FATAL ERROR: {} {}", e.getMessage(), e);
        } finally {
            closeWriter(writer);
        }
    }

    private void writeStatCsv(Map<String, Integer> statistics, Writer writer) throws IOException {
        CSVPrinter csv = createCsvPrinter(writer);
        for (String author: statistics.keySet()) {
            log.info("STAT: {} --> {}", author, statistics.get(author));
            csv.print(author);
            csv.print(statistics.get(author));
            csv.println();
        }
        csv.close();

    }

    private void writeCsv(List<ReportableTask> timesheet, Writer writer) throws IOException {
        log.info("Saving to CSV...");
        CSVPrinter csv = createCsvPrinter(writer);
        printCsvHeader(csv);
        writeTasks(csv, timesheet);
    }

    private void writeTasks(CSVPrinter csv, List<ReportableTask> timesheet) throws IOException {
        for (ReportableTask tasks : timesheet) {
            writeTask(csv, tasks);
        }
    }

    private void writeTask(CSVPrinter csv, ReportableTask tasks) throws IOException {
        csv.print(tasks);
        csv.println();
    }

    private void printCsvHeader(CSVPrinter csv) throws IOException {
        csv.printRecord("Date", "Tasks");
    }

    private CSVPrinter createCsvPrinter(Writer writer) throws IOException {
        return new CSVPrinter(writer, CSVFormat.EXCEL.withDelimiter(';'));
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
