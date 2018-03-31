package com.vranec.csv.exporter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExcelGridExporterTest {

    @Test
    public void formatDoubleTest() {
        assertEquals("12.3", ExcelGridExporter.formatDouble(12.32));
        assertEquals("12.4", ExcelGridExporter.formatDouble(12.38));
        assertEquals("0.4", ExcelGridExporter.formatDouble(0.43));
    }
}