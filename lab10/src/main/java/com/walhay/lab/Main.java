package com.walhay.lab;

import com.walhay.lab.gui.Model;
import com.walhay.lab.gui.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public static Stage stage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Main.stage = stage;
		Model model = new Model();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/View.fxml"));
		Parent parent = loader.load();

		ViewController controller = loader.getController();
		controller.initModel(model);
		model.setView(controller);

		Scene scene = new Scene(parent);
		stage.setScene(scene);

		stage.setHeight(720);
		stage.setWidth(1280);
		stage.setResizable(false);

		stage.show();
	}
}
