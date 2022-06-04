package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scroll = loader.load();//Para aumentar a linha de scroll com os menus
			
			//Ajustando à janela
			scroll.setFitToHeight(true);
			scroll.setFitToWidth(true);
			
			Scene mainScene = new Scene(scroll);
			
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Aplicação JavaFX de Modelo");
			primaryStage.show();
		} 
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
