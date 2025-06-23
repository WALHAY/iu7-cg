package com.walhay.lab.interfaces;

import com.walhay.lab.utils.*;

public interface IAffineTransform {
	void rotate(double angle);

	void scale(double scale);

	void transpose(Vector2D move);
}
