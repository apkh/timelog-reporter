package com.vranec.csv.exporter;

import java.io.IOException;

public interface GridExporter {

	int STYLE_HEADER = 0;
	int STYLE_HIGHLIGHT = 1;
	int STYLE_BODY = 1;

	void close() throws IOException;

	void print(Object obj) throws IOException;

	void println() throws IOException;

	void createSheet(String string);

	void setStyle(int styleHeader);

}
