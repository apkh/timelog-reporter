package com.vranec.timesheet.generator;

import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReportableTaskTest {

    @Test
    public void toStringTest() {
        val task = ReportableTask.builder()
                        .key("123")
                        .minutes(42)
                        .resource("John Dow")
                        .summary("#sum#").build();
        assertEquals("123, #sum#, 0.7, John Dow", task.toString());
    }
}