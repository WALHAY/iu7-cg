package com.walhay.lab.utils;

import com.walhay.lab.graphics.Algorithms;
import lombok.Getter;

public enum FunctionEnum {
	FIRST("First", Functions.first),
	SECOND("Second", Functions.second),
	THIRD("Third", Functions.third);

	@Getter
	private final String name;

	@Getter
	private final Algorithms function;

	FunctionEnum(String name, Algorithms function) {
		this.name = name;
		this.function = function;
	}

	@Override
	public String toString() {
		return name;
	}
}
