package com.walhay.lab.gui;

import com.walhay.lab.interfaces.*;
import com.walhay.lab.primitives.Point;
import com.walhay.lab.utils.Vector2D;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.util.converter.DoubleStringConverter;

public class ViewController implements Initializable, IView {
	private ResizableCanvas canvas;
	private Model model;

	@FXML
	private Pane canvasPane;

	@FXML
	private TextField scale;

	@FXML
	private TextField moveStep;

	@FXML
	private TextField rotationStep;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		canvas = new ResizableCanvas();
		canvasPane.widthProperty().addListener(change -> canvas.setWidth(canvasPane.getWidth()));
		canvasPane.heightProperty().addListener(change -> canvas.setHeight(canvasPane.getHeight()));
		canvasPane.getChildren().add(canvas);

		scale.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
		moveStep.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
		rotationStep.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));

		scale.setText("1.0f");
		moveStep.setText("10f");
		rotationStep.setText("10f");
	}

	public void initModel(Model model) {
		this.model = model;
		canvas.setKirby(model.getKirby());
		model.setView(this);
	}

	@Override
	public void updateView() {
		canvas.update();
	}

	@FXML
	private void onMoveLeftUp() {
		Double step = Double.parseDouble(moveStep.getText());

		model.move(new Vector2D(-step, -step));
	}

	@FXML
	private void onMoveLeft() {
		Double step = Double.parseDouble(moveStep.getText());

		model.move(new Vector2D(-step, 0));
	}

	@FXML
	private void onMoveLeftDown() {
		Double step = Double.parseDouble(moveStep.getText());

		model.move(new Vector2D(-step, step));
	}

	@FXML
	private void onMoveUp() {
		Double step = Double.parseDouble(moveStep.getText());

		model.move(new Vector2D(0, -step));
	}

	@FXML
	private void onMoveDown() {
		Double step = Double.parseDouble(moveStep.getText());

		model.move(new Vector2D(0, step));
	}

	@FXML
	private void onMoveRightUp() {
		Double step = Double.parseDouble(moveStep.getText());

		model.move(new Vector2D(step, -step));
	}

	@FXML
	private void onMoveRight() {
		Double step = Double.parseDouble(moveStep.getText());

		model.move(new Vector2D(step, 0));
	}

	@FXML
	private void onMoveRightDown() {
		Double step = Double.parseDouble(moveStep.getText());

		model.move(new Vector2D(step, step));
	}

	@FXML
	private void onRotateCW() {
		double angle = Double.parseDouble(rotationStep.getText());

		model.changeRotation(angle);
	}

	@FXML
	private void onRotateCCW() {
		double angle = Double.parseDouble(rotationStep.getText());

		model.changeRotation(-angle);
	}

	@FXML
	private void onScaleChange() {
		double newScale = Double.parseDouble(scale.getText());

		model.changeScale(newScale);
	}
}
