package com.walhay.lab;

import com.walhay.lab.gui.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppGUI extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		Model model = new Model();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/View.fxml"));
		Parent parent = loader.load();

		ViewController controller = loader.getController();
		controller.initModel(model);
		model.setView(controller);

		Scene scene = new Scene(parent);
		stage.setScene(scene);

		stage.setMinHeight(480);
		stage.setMinWidth(640);

		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
