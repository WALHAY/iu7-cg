package com.walhay.lab.gui;

import com.walhay.lab.interfaces.IView;
import com.walhay.lab.utils.*;
import com.walhay.lab.widgets.AlgoEntry;
import com.walhay.lab.widgets.AlgoListCell;
import com.walhay.lab.widgets.ResizableCanvas;
import java.awt.Color;
import java.net.URL;
import java.util.EnumSet;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;

public class ViewController implements Initializable, IView {
	private Model model;

	private ResizableCanvas canvas = new ResizableCanvas();

	@FXML
	Pane canvasPane;

	@FXML
	ListView<AlgoEntry<EllipseType>> ellipseList;

	@FXML
	Text reportText;

	@FXML
	TextField ellipseA;

	@FXML
	TextField ellipseB;

	@FXML
	TextField watermelonParts;

	@FXML
	ChoiceBox<WatermelonType> watermelonAlignment;

	@FXML
	ColorPicker bgColor;

	private <E extends Enum<E>> void populateList(ListView<AlgoEntry<E>> view, Class<E> enumType) {
		if (view == null || enumType == null)
			return;

		view.getItems().addAll(EnumSet.allOf(enumType).stream().map(v -> new AlgoEntry<E>(v)).toList());
		view.setCellFactory(e -> new AlgoListCell<>());

		view.prefHeightProperty().bind(Bindings.size(view.getItems()).multiply(AlgoListCell.CELL_HEIGHT).add(5));

		view.setFocusTraversable(false);
	}

	private void populateAlignment() {
		if (watermelonAlignment == null)
			return;

		watermelonAlignment.getItems().addAll(WatermelonType.values());
		watermelonAlignment.getSelectionModel().select(WatermelonType.HORIZONTAL);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateList(ellipseList, EllipseType.class);
		populateAlignment();

		canvasPane.widthProperty().addListener(change -> canvas.setWidth(canvasPane.getWidth()));
		canvasPane.heightProperty().addListener(change -> canvas.setHeight(canvasPane.getHeight()));
		canvasPane.getChildren().add(canvas);

		ellipseA.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
		ellipseB.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
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

	private Pair<Integer, Integer> getRadiuses() {
		try {
			int ra = Integer.valueOf(ellipseA.getText());
			int rb = Integer.valueOf(ellipseB.getText());
			return new Pair<>(ra, rb);
		} catch (Exception e) {
			return null;
		}
	}

	@FXML
	void drawEllipses() {
		var rad = getRadiuses();
		if (rad != null)
			model.drawEllipses(ellipseList.getItems().stream().filter(alg -> alg != null && alg.isActive()).iterator(),
							   rad);
	}

	private Color getBackgroundColor() {
		return ColorUtils.toAwtColor(bgColor.getValue());
	}

	@FXML
	void clearCanvas() {
		model.clearCanvas(getBackgroundColor());
	}

	private WatermelonType getWatermelonType() {
		return watermelonAlignment.getValue();
	}

	private int getWatermelonParts() {
		try {
			return Integer.valueOf(watermelonParts.getText());
		} catch (Exception e) {
			return 0;
		}
	}

	@FXML
	void drawWatermelon() {
		var rad = getRadiuses();
		var parts = getWatermelonParts();
		if (rad != null)
			model.drawWatermelons(
				ellipseList.getItems().stream().filter(alg -> alg != null && alg.isActive()).iterator(), rad,
				getWatermelonType(), parts);
	}
}
