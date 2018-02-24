package com.vranec.timesheet.generator;

import java.time.LocalDate;
import java.util.Map;

public interface TaskSource {
    Iterable<ReportableTask> getTasks(LocalDate startDate);
    Map<String, Integer> getStatistics();
}
