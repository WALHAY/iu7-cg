package com.walhay.lab.utils;

public enum EllipseType {
	GENERAL("General"),
	PARAM("Parametric");

	private final String name;

	EllipseType(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
