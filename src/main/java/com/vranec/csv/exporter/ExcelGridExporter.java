package com.vranec.csv.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelGridExporter implements GridExporter {

	private Workbook workbook;
	private Sheet sheet;
	private CellStyle[] styles = new CellStyle[3];
	private int rowIndex;
	private Row currentRow;
	private CellStyle currentStyle;
	private int columnIndex;
	
	public ExcelGridExporter() throws IOException {
        workbook = new XSSFWorkbook();
        initStyles();
	}


	@Override
	public void close() throws IOException {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        Date d = new Date();
        String fileLocation = path.substring(0, path.length() - 1) 
        		+ d.getDate() + "-"
        		+ (d.getMonth() + 1) + "-"
        		+ d.getHours() + "-"
        		+ d.getMinutes() 
        		+ "_temp.xlsx";
        System.out.println("Generate " + fileLocation);
        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        try {
        	workbook.write(outputStream);
        } finally {
        	if (workbook != null) {
                workbook.close();

        	}
        }
	}

    @Override
    public void print(String str) {
        Cell cell = currentRow.createCell(columnIndex++);
        cell.setCellValue(str == null ? "" : str);
        cell.setCellStyle(currentStyle);
    }

    @Override
    public void printNumber(double value) {
        Cell cell = currentRow.createCell(columnIndex++);
        if (value != 0.0) {
            cell.setCellValue(formatDouble(value));
        }
        cell.setCellStyle(currentStyle);
//        cell.setCellType(CellType.NUMERIC);

    }

    static String formatDouble(double value) {
        return String.format("%.1f", (float)value);
    }


    @Override
	public void println() {
		rowIndex++;
		createRow();
	}


	@Override
	public void createSheet(String name) {
        sheet = workbook.createSheet(name);
        rowIndex = 0;
        createRow();
	}


	private void createRow() {
        currentRow = sheet.createRow(rowIndex);
        columnIndex = 0;
	}


	@Override
	public void setStyle(int styleIndex) {
		currentStyle = styles[styleIndex];
	}

	
	private void initStyles() {
        CellStyle headerStyle = workbook.createCellStyle();

        headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        CellStyle hStyle = workbook.createCellStyle();
        hStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());

        styles [STYLE_HEADER] = headerStyle;
        styles[STYLE_BODY] = style;
        styles[STYLE_HIGHLIGHT] = hStyle;
        
	}
    
 }
