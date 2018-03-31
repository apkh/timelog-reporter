package com.vranec.csv.exporter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class CVSGridExporter implements GridExporter {

	private Writer writer;
	private CSVPrinter cvsPrinter;
	
	public CVSGridExporter() throws IOException {
		super();
        writer = new OutputStreamWriter(new FileOutputStream("export.csv"), "UTF-8");//new FileWriter("export.csv");
        cvsPrinter = new CSVPrinter(writer, CSVFormat.EXCEL.withDelimiter(','));
	}


	@Override
	public void close() throws IOException {
		cvsPrinter.close();
		writer.close();
	}
	
	@Override
	public void print(String obj) throws IOException {
		cvsPrinter.print(obj);
		
	}
	
	@Override
	public void println() throws IOException {
		cvsPrinter.println();
	}


	@Override
	public void createSheet(String string) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setStyle(int styleHeader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printNumber(double value) {
		try {
			print(String.format("%.1f", (float)value));
		} catch (IOException e) {
		}
	}

}
