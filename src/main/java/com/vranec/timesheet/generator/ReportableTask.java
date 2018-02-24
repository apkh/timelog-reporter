package com.vranec.timesheet.generator;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportableTask {
    private String key;
    private String summary;
    private Integer hours;
    private String resource;

    @Override
    public String toString() {
        return String.format("%s, %s, %d, %s", key, summary, hours, resource);
    }
}
