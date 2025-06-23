package com.walhay.lab.widgets;

import java.awt.Color;
import lombok.Data;

@Data
public class AlgoEntry<T> {
	private final T type;

	private Color color = Color.BLACK;
	private boolean active = false;
	private int points = 100;

	public AlgoEntry(T type) {
		this.type = type;
	}

	public AlgoEntry(T type, Color color, boolean active) {
		this.type = type;
		this.color = color;
		this.active = active;
	}
};
