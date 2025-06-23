package com.walhay.lab.gui;

import com.walhay.lab.interfaces.IView;
import com.walhay.lab.utils.ColorUtils;
import com.walhay.lab.widgets.ResizableCanvas;
import java.awt.Point;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.converter.IntegerStringConverter;

public class ViewController implements Initializable, IView {
	private Model model;

	private ResizableCanvas canvas = new ResizableCanvas();

	@FXML
	Pane canvasPane;

	@FXML
	ColorPicker lineColor;

	@FXML
	ColorPicker cutColor;

	@FXML
	ColorPicker rectColor;

	@FXML
	TextField x1;

	@FXML
	TextField x2;

	@FXML
	TextField y1;

	@FXML
	TextField y2;

	@FXML
	TextField cutx;

	@FXML
	TextField cuty;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		canvasPane.getChildren().add(canvas);

		canvas.setWidth(1080);
		canvas.setHeight(720);
		canvas.setOnMouseClicked(this::onMouseClicked);

		for (TextField field : new TextField[] {x1, x2, y1, y2, cutx, cuty}) {
			field.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
			field.setText("0");
		}
	}

	private Point getPointFromMouseEvent(MouseEvent event) {
		return new Point((int) event.getX(), (int) event.getY());
	}

	public void onMouseClicked(MouseEvent event) {
		Point point = getPointFromMouseEvent(event);
		if (event.getButton() == MouseButton.SECONDARY) {
			this.model.addCutterPoint(point);
		} else if (event.getButton() == MouseButton.PRIMARY) {
			this.model.addLinePoint(point);
		}
	}

	public void initModel(Model model) {
		this.model = model;
		if (this.model == null) {
			return;
		}

		updatePalette();
		this.canvas.setImage(model.getImage());
		this.model.setView(this);
		this.model.draw();
	}

	@FXML
	public void addLine() {
		try {
			int lineX1 = Integer.parseInt(x1.getText());
			int lineX2 = Integer.parseInt(x2.getText());
			int lineY1 = Integer.parseInt(y1.getText());
			int lineY2 = Integer.parseInt(y2.getText());

			this.model.addLine(new Point(lineX1, lineY1), new Point(lineX2, lineY2));
		} catch (Exception e) {
			System.out.print("Wrong value inside TextField");
		}
	}

	@FXML
	public void addCutterPoint() {
		try {
			int cutX = Integer.parseInt(cutx.getText());
			int cutY = Integer.parseInt(cuty.getText());

			this.model.addCutterPoint(new Point(cutX, cutY));
		} catch (Exception e) {
			System.out.print("Wrong value inside TextField");
		}
	}

	@FXML
	public void actionCut() {
		if (model.checkCutterConvex()) {
			model.actionCut();
		} else {
			showError("Cutter is not convex", "");
		}
	}

	@Override
	public void updateView() {
		if (canvas != null) {
			canvas.update();
		}
	}

	@FXML
	public void clearScreen() {
		this.model.clearScreen();
	}

	@FXML
	public void updatePalette() {
		model.setCutColor(ColorUtils.toAwtColor(cutColor.getValue()));
		model.setRectColor(ColorUtils.toAwtColor(rectColor.getValue()));
		model.setLineColor(ColorUtils.toAwtColor(lineColor.getValue()));
		model.draw();
	}

	private void showError(String title, String msg) {
		Alert alert = new Alert(AlertType.ERROR);

		alert.setHeaderText(String.format(title));
		alert.setContentText(String.format(msg));
		alert.showAndWait();
	}
}
