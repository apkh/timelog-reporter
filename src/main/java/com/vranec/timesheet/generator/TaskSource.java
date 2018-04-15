package com.vranec.timesheet.generator;

import java.time.LocalDate;
import java.util.Map;

public interface TaskSource {
    void getTasks(LocalDate startDate, LocalDate endDate);
}
