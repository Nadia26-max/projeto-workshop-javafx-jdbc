package gui.util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Alertas {

		public static void showAlert(String titulo, String cabecalho, String content, AlertType tipo) {
			
			Alert alert = new Alert(tipo);
			alert.setTitle(titulo);
			alert.setHeaderText(cabecalho);
			alert.setContentText(content);
			alert.show();
		}
		
		//Referente ao botao de excluir
		public static Optional<ButtonType> showConfirmation(String titulo, String content) {
			 Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle(titulo);
			alert.setHeaderText(null);
			alert.setContentText(content);
			return alert.showAndWait();
			} 

	}