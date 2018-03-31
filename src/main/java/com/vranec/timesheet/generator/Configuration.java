package com.vranec.timesheet.generator;

import java.util.List;

public interface Configuration {
    int getMonthDetectionSubtractDays();

    String getJiraUsername();
    List<String> getResources();
    List<String> getProjects();

    String getSupervisorName();

    int getMinimumWlTime();
}
