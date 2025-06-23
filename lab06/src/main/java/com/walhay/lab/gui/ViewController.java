package com.walhay.lab.gui;

import com.walhay.lab.*;
import com.walhay.lab.interfaces.IView;
import com.walhay.lab.utils.ColorUtils;
import com.walhay.lab.utils.UserMode;
import com.walhay.lab.widgets.ResizableCanvas;
import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.converter.IntegerStringConverter;

public class ViewController implements Initializable, IView {
	private Model model;

	private ResizableCanvas canvas = new ResizableCanvas();

	@FXML
	Pane canvasPane;

	@FXML
	GridPane controlGrid;

	@FXML
	ToggleButton modeButton;

	@FXML
	CheckBox withDelay;

	@FXML
	TextField delayTime;

	@FXML
	ColorPicker fillColor;

	@FXML
	ColorPicker holeBorderColor;

	@FXML
	ColorPicker polygonBorderColor;

	@FXML
	TextArea reportText;

	@FXML
	Button fileSelectBtn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		canvasPane.getChildren().add(canvas);

		canvas.setWidth(1080);
		canvas.setHeight(720);
		canvas.setOnMouseClicked(this::onMouseClicked);

		delayTime.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
		delayTime.setText("5");
	}

	@FXML
	void setPolygonMode() {
		this.model.setUserMode(UserMode.POLYGON);
	}

	@FXML
	void setHoleMode() {
		this.model.setUserMode(UserMode.HOLE);
	}

	@FXML
	void setFillMode() {
		this.model.setUserMode(UserMode.FILL);
	}

	private Point getPointFromMouseEvent(MouseEvent event) {
		return new Point((int) event.getX(), (int) event.getY());
	}

	public void onMouseClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			this.model.onClick(getPointFromMouseEvent(event));
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

	@Override
	public void updateView() {
		if (canvas != null) {
			canvas.update();
		}
	}

	@Override
	public void addReport(long time) {
		if (reportText != null) {
			String previous = reportText.getText();
			reportText.setText(String.format(
							"[%s] Draw time: %f ms\n",
							LocalTime.now().truncatedTo(ChronoUnit.SECONDS), (double) time / 1e6)
					+ previous);
		}
	}

	@FXML
	public void endHole() {
		model.endHole();
	}

	public Color getColor() {
		if (fillColor != null) {
			return ColorUtils.toAwtColor(fillColor.getValue());
		}
		return Color.BLACK;
	}

	public int getDelay() {
		if (delayTime != null) {
			try {
				delayTime.setStyle("");
				return Integer.valueOf(delayTime.getText());
			} catch (Exception e) {
				delayTime.setStyle("-fx-background-color: rgb(200, 20, 30);");
			}
		}
		return 5;
	}

	public boolean isDelayed() {
		return withDelay != null && withDelay.isSelected();
	}

	@FXML
	public void fillFigure() {
		this.model.floodFill(isDelayed(), getDelay());
	}

	@FXML
	public void clearFill() {
		this.model.clearFill();
	}

	@FXML
	public void addCircle() {
		this.model.addCircle();
	}

	@FXML
	public void updatePalette() {
		model.setPolygonBorderColor(ColorUtils.toAwtColor(polygonBorderColor.getValue()));
		model.setHoleBorderColor(ColorUtils.toAwtColor(holeBorderColor.getValue()));
		model.setFillColor(ColorUtils.toAwtColor(fillColor.getValue()));
	}

	public File getFile(boolean open) {
		FileChooser fileSelector = new FileChooser();
		if (open) {
			return fileSelector.showOpenDialog(AppGUI.stage);
		} else {
			return fileSelector.showSaveDialog(AppGUI.stage);
		}
	}

	@FXML
	public void importFile() {
		File importFile = getFile(true);

		try {
			model.loadData(importFile);
		} catch (IOException e) {
			showIOErrMsg(true, importFile, e);
		}
	}

	@FXML
	public void exportFile() {
		File exportFile = getFile(false);

		try {
			model.saveData(exportFile);
		} catch (IOException e) {
			showIOErrMsg(false, exportFile, e);
		}
	}

	private void showIOErrMsg(boolean open, File file, IOException exception) {
		Alert alert = new Alert(AlertType.ERROR);

		String filename = file == null ? "Null" : file.getName();
		String action = open ? "open" : "save";

		alert.setHeaderText(String.format("Failed to %s file %s", action, filename));
		alert.setContentText(String.format("Error message: %s", exception.getLocalizedMessage()));
		alert.showAndWait();
	}

	@Override
	public void updatePalette(Color[] colors) {
		polygonBorderColor.setValue(ColorUtils.toFXColor(colors[0]));
		holeBorderColor.setValue(ColorUtils.toFXColor(colors[1]));
		fillColor.setValue(ColorUtils.toFXColor(colors[2]));
	}
}
