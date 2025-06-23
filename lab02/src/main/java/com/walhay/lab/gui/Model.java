package com.walhay.lab.gui;

import com.walhay.lab.image.*;
import com.walhay.lab.interfaces.*;
import com.walhay.lab.utils.*;

public class Model {
	NewKirby kirby = new NewKirby();

	IView view;

	public void setView(IView view) {
		this.view = view;
	}

	public void changeScale(double scale) {
		kirby.scale(scale);
		view.updateView();
	}

	public NewKirby getKirby() {
		return kirby;
	}

	public void move(Vector2D move) {
		if (move == null)
			return;
		kirby.transpose(move);
		view.updateView();
	}

	public void changeRotation(double angle) {
		kirby.rotate(angle);
		view.updateView();
	}
}
