package com.vranec.csv.exporter;

import java.io.IOException;

public interface GridExporter {

	int STYLE_HEADER = 0;
	int STYLE_HIGHLIGHT = 1;
	int STYLE_BODY = 1;

	void close() throws IOException;

	void print(String str);

	void println();

	void createSheet(String string);

	void setStyle(int styleHeader);

	void printNumber(double value);
}
