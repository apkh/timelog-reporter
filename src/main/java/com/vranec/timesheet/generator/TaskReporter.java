package com.vranec.timesheet.generator;

import java.util.List;
import java.util.Map;

public interface TaskReporter {
    void report(List<ReportableTask> timesheet, Map<String, Integer> statistics);
}
