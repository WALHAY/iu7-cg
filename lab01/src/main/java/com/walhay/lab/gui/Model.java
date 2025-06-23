package com.walhay.lab.gui;

import com.walhay.lab.interfaces.IModel;
import com.walhay.lab.interfaces.IView;
import com.walhay.lab.utils.DataLoader;
import com.walhay.lab.utils.Geometry;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

public class Model implements IModel {
	private IView view;
	private final Geometry geometry = new Geometry();

	private final ObservableSet<Geometry.Point> pointObservableList;
	private ObservableList<Geometry.Triangle> triangleObservableList;

	public Model() {
		this.pointObservableList = FXCollections.observableSet(geometry.getPoints());
		this.triangleObservableList = null;
	}

	public void addPoint(double x, double y) {
		geometry.addPoint(x, y);

		updateResult();
		if (view != null)
			view.onModelUpdate();
	}

	public void removePoint(double x, double y) {
		geometry.removePoint(x, y);

		updateResult();
		if (view != null)
			view.onModelUpdate();
	}

	public void removePoint(Geometry.Point point) {
		geometry.removePoint(point);

		updateResult();
		if (view != null)
			view.onModelUpdate();
	}

	public ObservableSet<Geometry.Point> getObservablePoints() {
		return pointObservableList;
	}

	public ObservableList<Geometry.Triangle> getObservableTriangles() {
		return triangleObservableList;
	}

	private void updateResult() {
		if (geometry.getPoints().size() >= 6) {
			Set<Geometry.Triangle> triangles = geometry.getAllTriangles();
			if (triangles != null)
				this.triangleObservableList = FXCollections.observableList(geometry.findTriangles(triangles));
		} else
			this.triangleObservableList = null;
	}

	public void importDataFromFile(File file) throws IOException, NumberFormatException {
		var points = DataLoader.importPointsFromFile(file.toPath());

		if (points != null && !points.isEmpty()) {
			geometry.getPoints().clear();
			geometry.getPoints().addAll(points);
		}

		updateResult();
		if (view != null)
			view.onModelUpdate();
	}

	public void exportDataToFile(File file) throws IOException {
		DataLoader.exportPointsToFile(file.toPath(), geometry.getPoints());
	}

	@Override
	public void setView(IView view) {
		this.view = view;
	}

	@Override
	public IView getView() {
		return view;
	}
}
