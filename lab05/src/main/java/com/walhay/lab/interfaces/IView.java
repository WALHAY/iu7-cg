package com.walhay.lab.interfaces;

import java.awt.Color;

public interface IView {
	void updateView();

	void addReport(long time);

	void updateColor(Color color);
}
