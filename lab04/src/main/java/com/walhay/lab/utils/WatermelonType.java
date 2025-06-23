package com.walhay.lab.utils;

public enum WatermelonType {
	VERTICAL("Vertical"),
	HORIZONTAL("Horizontal");

	private final String name;

 	WatermelonType(String name)
	{
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
