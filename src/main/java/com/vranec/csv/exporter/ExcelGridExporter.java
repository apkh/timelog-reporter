package com.vranec.csv.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
        		+ d.getDay() + "-"
        		+ d.getMonth() + "-"
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
	public void print(Object obj) throws IOException {
        Cell cell = currentRow.createCell(columnIndex++);
        cell.setCellValue(obj == null ? "" : obj.toString());
        cell.setCellStyle(currentStyle);
		
	}
	
	@Override
	public void println() throws IOException {
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

        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
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
    
    public void writeExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook();

        try {
            Sheet sheet = workbook.createSheet("Persons");
            sheet.setColumnWidth(0, 6000);
            sheet.setColumnWidth(1, 4000);

            Row header = sheet.createRow(0);

            CellStyle headerStyle = workbook.createCellStyle();

            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short) 16);
            font.setBold(true);
            headerStyle.setFont(font);

            Cell headerCell = header.createCell(0);
            headerCell.setCellValue("Name");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(1);
            headerCell.setCellValue("Age");
            headerCell.setCellStyle(headerStyle);

            CellStyle style = workbook.createCellStyle();
            style.setWrapText(true);

            Row row = sheet.createRow(2);
            Cell cell = row.createCell(0);
            cell.setCellValue("John Smith");
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(20);
            cell.setCellStyle(style);

            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";
            System.out.println("Generate " + fileLocation);
            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
        } finally {
            if (workbook != null) {
               
                    workbook.close();
               
            }
        }
    }
}
