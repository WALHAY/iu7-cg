package com.walhay.lab.gui;

import com.walhay.lab.interfaces.IView;
import com.walhay.lab.utils.ColorUtils;
import com.walhay.lab.widgets.ResizableCanvas;
import java.awt.Point;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
	TextField top;

	@FXML
	TextField left;

	@FXML
	TextField right;

	@FXML
	TextField bottom;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		canvasPane.getChildren().add(canvas);

		canvas.setWidth(1080);
		canvas.setHeight(720);
		canvas.setOnMouseClicked(this::onMouseClicked);

		for (TextField field : new TextField[] {x1, x2, y1, y2, top, bottom, left, right}) {
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
			this.model.addRectPoint(point);
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
	public void setCutter() {
		try {
			int rectBottom = Integer.parseInt(bottom.getText());
			int rectTop = Integer.parseInt(top.getText());
			int rectLeft = Integer.parseInt(left.getText());
			int rectRight = Integer.parseInt(right.getText());

			this.model.setRect(new Point(rectLeft, rectTop), new Point(rectRight, rectBottom));
		} catch (Exception e) {
			System.out.print("Wrong value inside TextField");
		}
	}

	@FXML
	public void actionCut() {
		this.model.actionCut();
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
}
