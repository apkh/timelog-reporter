package com.vranec.timesheet.generator;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportableTask {
    private String key;
    private String summary;
    private Integer minutes;
    private String resource;

    @Override
    public String toString() {
        return String.format("%s, %s, %2.1f, %s", key, 
        		getQuotedSUmmary(), 
            	((float)(int)minutes)/60, resource);
    }

	private String getQuotedSUmmary() {
		return summary.indexOf(',') >= 0 ? '"' + summary + '"' : summary;
	}
}
