package com.walhay.lab.primitives;

import com.walhay.lab.interfaces.*;
import com.walhay.lab.utils.*;
import java.util.List;

public abstract class AbstractPrimitive implements IDrawable {
	abstract public List<IDrawable> getDrawables();

	abstract protected void recalculatePoints();

	@Override
	public void changeBasis(Basis basis) {
		for (IDrawable drawable : getDrawables()) drawable.changeBasis(basis);
		recalculatePoints();
	}
};
