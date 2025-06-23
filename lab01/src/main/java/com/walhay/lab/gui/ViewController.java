package com.walhay.lab.gui;

import com.walhay.lab.AppGUI;
import com.walhay.lab.interfaces.IView;
import com.walhay.lab.utils.Geometry;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.converter.DoubleStringConverter;

public class ViewController implements Initializable, IView {
	private Model model;
	private ResizableCanvas plot;

	@FXML
	private Pane canvasPane;

	@FXML
	private ListView<Geometry.Point> pointList;

	@FXML
	private TextField inputX;

	@FXML
	private TextField inputY;

	@FXML
	private CheckBox checkFocus;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		plot = new ResizableCanvas();

		canvasPane.widthProperty().addListener(change -> plot.setWidth(canvasPane.getWidth()));
		canvasPane.heightProperty().addListener(change -> plot.setHeight(canvasPane.getHeight()));
		canvasPane.getChildren().add(plot);

		pointList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		pointList.getSelectionModel().selectedItemProperty().addListener(e -> {
			var values = pointList.getSelectionModel()
							 .getSelectedIndices()
							 .stream()
							 .map(pointList.getItems()::get)
							 .collect(Collectors.toList());
			plot.setSelection(values);
		});

		inputX.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
		inputY.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
	}

	public void initModel(Model model) {
		if (this.model != null)
			throw new IllegalStateException("Controller cant get more than 1 Model");

		this.model = model;

		plot.setPoints(model.getObservablePoints());
	}

	private void warningWrongPoint() {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setHeaderText("Wrong Value");
		alert.setContentText("Invalid value inside coordinate input field");
		alert.showAndWait();
	}

	@FXML
	private void actionAddPoint() {
		try {
			double x = Double.parseDouble(inputX.getText());
			double y = Double.parseDouble(inputY.getText());

			model.addPoint(x, y);
		} catch (Exception e) {
			warningWrongPoint();
		}
	}

	private void listRemoveSelected() {
		var values =
			pointList.getSelectionModel().getSelectedIndices().stream().map(pointList.getItems()::get).toList();
		values.forEach(model::removePoint);
	}

	@FXML
	public void actionRemovePoint() {
		listRemoveSelected();
	}

	@FXML
	public void actionImportData() {
		File file = getImportPath();
		if (file == null)
			return;

		try {
			model.importDataFromFile(file);
		} catch (Exception e) {
			errorFileAction(true, file);
		}
	}

	@FXML
	public void actionExportData() {
		File file = getExportPath();
		if (file == null)
			return;

		try {
			model.exportDataToFile(file);
		} catch (Exception e) {
			errorFileAction(false, file);
		}
	}

	private void errorFileAction(boolean onImport, File file) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		String action = onImport ? "import" : "export";
		alert.setHeaderText(String.format("Failed to %s file", action));
		alert.setContentText(String.format("Unable to %s %s data. Data may be corrupted", action,
										   file == null ? "null" : file.getName()));
		alert.showAndWait();
	}

	private File getImportPath() {
		FileChooser filePicker = new FileChooser();
		filePicker.setTitle("File to import");
		return filePicker.showOpenDialog(AppGUI.stage);
	}

	private File getExportPath() {
		FileChooser filePicker = new FileChooser();
		filePicker.setTitle("File to export");
		return filePicker.showSaveDialog(AppGUI.stage);
	}

	@FXML
	public void listActionKeyPressed(KeyEvent event) {
		if (event.getCode().equals(KeyCode.BACK_SPACE) || event.getCode().equals(KeyCode.DELETE))
			listRemoveSelected();
	}

	@FXML
	public void actionChangeFocus() {
		boolean newFocus = checkFocus.isSelected();

		plot.setPointFocus(newFocus);
	}

	@Override
	public void onModelUpdate() {
		pointList.setItems(FXCollections.observableArrayList(model.getObservablePoints()));
		plot.setTriangleSelection(model.getObservableTriangles());
		plot.forceUpdate();
	}
}
