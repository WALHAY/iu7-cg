package com.walhay.lab.gui;

import com.walhay.lab.Constants;
import com.walhay.lab.graphics.Algorithms;
import com.walhay.lab.interfaces.IView;
import com.walhay.lab.utils.FunctionEnum;
import com.walhay.lab.widgets.ResizableCanvas;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.util.converter.DoubleStringConverter;

public class ViewController implements Initializable, IView {

	private Model model;

	private ResizableCanvas canvas = new ResizableCanvas();

	@FXML
	Pane canvasPane;

	@FXML
	ChoiceBox<FunctionEnum> function;

	@FXML
	TextField scale;

	@FXML
	TextField xMin;

	@FXML
	TextField zMin;

	@FXML
	TextField xMax;

	@FXML
	TextField zMax;

	@FXML
	TextField xStep;

	@FXML
	TextField zStep;

	@FXML
	TextField xAngle;

	@FXML
	TextField yAngle;

	@FXML
	TextField zAngle;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		canvasPane.getChildren().add(canvas);

		canvas.setWidth(Constants.WIDTH);
		canvas.setHeight(Constants.HEIGHT);

		function.getItems().setAll(FunctionEnum.values());
		function.getSelectionModel().select(0);

		List<TextField> fieldList = List.of(xMin, xMax, xStep, zMin, zMax, zStep, xAngle, yAngle, zAngle, scale);

		for (final TextField field : fieldList) {
			field.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
			field.setText("0");
		}
		scale.setText("1.0");

		function.getSelectionModel().selectedItemProperty().addListener(e -> {
			var selected = function.getSelectionModel().getSelectedItem();

			this.model.setFunction(selected.getFunction());

			setXYFromFunction(selected.getFunction());
		});
	}

	private void setXYFromFunction(Algorithms algo) {
		xMin.setText(String.valueOf(algo.getXMin()));
		zMin.setText(String.valueOf(algo.getZMin()));
		xMax.setText(String.valueOf(algo.getXMax()));
		zMax.setText(String.valueOf(algo.getZMax()));
		xStep.setText(String.valueOf(algo.getXStep()));
		zStep.setText(String.valueOf(algo.getZStep()));
		scale.setText(String.valueOf(algo.getScale()));
	}

	public void initModel(Model model) {
		this.model = model;

		if (this.model == null) {
			return;
		}

		this.canvas.setImage(model.getImage());
		var func = function.getSelectionModel().getSelectedItem().getFunction();
		this.model.setFunction(func);
		setXYFromFunction(func);
		this.model.setView(this);
		this.model.draw();
	}

	@Override
	public void updateView() {
		if (canvas != null) {
			canvas.update();
		}
	}

	@FXML
	void xChange() {
		try {
			double xMinD = Double.valueOf(xMin.getText());
			double xMaxD = Double.valueOf(xMax.getText());
			double xStepD = Double.valueOf(xStep.getText());

			model.setX(xMinD, xMaxD, xStepD);
		} catch (Exception e) {
			System.out.print("Failed to parse double");
		}
	}

	@FXML
	void zChange() {
		try {
			double zMinD = Double.valueOf(zMin.getText());
			double zMaxD = Double.valueOf(zMax.getText());
			double zStepD = Double.valueOf(zStep.getText());

			model.setZ(zMinD, zMaxD, zStepD);
		} catch (Exception e) {
			System.out.print("Failed to parse double");
		}
	}

	@FXML
	void rotate() {
		try {
			double xAngleD = Double.valueOf(xAngle.getText());
			double yAngleD = Double.valueOf(yAngle.getText());
			double zAngleD = Double.valueOf(zAngle.getText());

			model.addAngle(xAngleD, yAngleD, zAngleD);
		} catch (Exception e) {
			System.out.print("Failed to parse double");
		}
	}

	@FXML
	void scaleChange() {
		try {
			double scaleD = Double.valueOf(scale.getText());

			model.setScale(scaleD);
		} catch (Exception e) {
			System.out.print("Failed to parse double");
		}
	}
}
