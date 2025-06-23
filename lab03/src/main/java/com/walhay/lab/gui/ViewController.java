package com.walhay.lab.gui;

import com.walhay.lab.interfaces.IView;
import com.walhay.lab.utils.*;
import com.walhay.lab.widgets.AlgoEntry;
import com.walhay.lab.widgets.AlgoListCell;
import com.walhay.lab.widgets.ResizableCanvas;
import java.awt.Color;
import java.awt.Point;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;

public class ViewController implements Initializable, IView {
	private Model model;

	private ResizableCanvas canvas = new ResizableCanvas();

	@FXML
	Pane canvasPane;

	@FXML
	ListView<AlgoEntry> algoList;

	@FXML
	Text reportText;

	@FXML
	TextField startX;

	@FXML
	TextField startY;

	@FXML
	TextField endX;

	@FXML
	TextField endY;

	@FXML
	TextField circlePoints;

	@FXML
	ChoiceBox<LineType> circleAlgo;

	@FXML
	ColorPicker circleColor;

	@FXML
	ColorPicker bgColor;

	private void populateAlgoList() {
		if (algoList == null)
			return;

		algoList.getItems().addAll(Arrays.stream(LineType.values()).map(AlgoEntry::new).toList());
		algoList.setCellFactory(e -> new AlgoListCell());
	}

	private void populateAlgoTypeBox() {
		if (circleAlgo == null)
			return;

		circleAlgo.getItems().addAll(LineType.values());
		circleAlgo.getSelectionModel().select(LineType.LIB);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateAlgoList();
		populateAlgoTypeBox();

		canvasPane.widthProperty().addListener(change -> canvas.setWidth(canvasPane.getWidth()));
		canvasPane.heightProperty().addListener(change -> canvas.setHeight(canvasPane.getHeight()));
		canvasPane.getChildren().add(canvas);

		startX.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
		startY.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));

		endX.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
		endY.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));

		circlePoints.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
	}

	public void initModel(Model model) {
		this.model = model;
		this.model.setView(this);
		this.canvas.setImage(model.getImage());
	}

	@Override
	public void updateView() {
		canvas.update();
		String report = model.getDrawReport();

		if (report != null && !report.isEmpty())
			reportText.setText(report);
	}

	private Point getStart() {
		int x = Integer.valueOf(startX.getText());
		int y = Integer.valueOf(startY.getText());
		return new Point(x, y);
	}

	private Point getEnd() {
		int x = Integer.valueOf(endX.getText());
		int y = Integer.valueOf(endY.getText());
		return new Point(x, y);
	}

	@FXML
	void drawLines() {
		model.drawLines(algoList.getItems().stream().filter(alg -> alg != null && alg.isActive()).iterator(),
						getStart(), getEnd());
	}

	@FXML
	void clearCanvas() {
		model.clearCanvas(getBackgroundColor());
	}

	private LineType getCircleLineType() {
		return circleAlgo.getValue();
	}

	private int getCirclePoints() {
		return Integer.parseInt(circlePoints.getText());
	}

	private Color getCircleColor() {
		return ColorUtils.toAwtColor(circleColor.getValue());
	}

	private Color getBackgroundColor() {
		return ColorUtils.toAwtColor(bgColor.getValue());
	}

	@FXML
	void drawCircle() {
		model.drawCircle(getCircleLineType(), getCircleColor(), getCirclePoints());
	}
}
