package com.walhay.lab.widgets;

import com.walhay.lab.utils.LineType;
import java.awt.Color;
import lombok.Data;

@Data
public class AlgoEntry {
	private final LineType type;

	private Color color = Color.BLACK;
	private boolean active = false;

	public AlgoEntry(LineType type) {
		this.type = type;
	}

	public AlgoEntry(LineType type, Color color, boolean active) {
		this.type = type;
		this.color = color;
		this.active = active;
	}
};
