package com.walhay.lab.interfaces;

import java.awt.Color;

public interface IView {
	void updateView();

	void updatePalette(Color[] colors);

	void addReport(long time);
}
