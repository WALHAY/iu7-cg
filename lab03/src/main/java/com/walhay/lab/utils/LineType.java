package com.walhay.lab.utils;

public enum LineType {
	LIB("Library"),
	DDA("Digital Differential Analyzer"),
	BRS_F("Bresenham Real"),
	BRS_INT("Bresenham Int"),
	BRS_AA("Bresenham Antialiasing"),
	WU("Wu");

	private final String name;

	LineType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
