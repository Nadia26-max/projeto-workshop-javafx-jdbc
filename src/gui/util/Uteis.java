package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Uteis {

	//Acesso o Stage onde o controle que recebeu o evento est� (Clicando no botao e pegando o evento daquele botao
	public static Stage currentStage(ActionEvent evento) {//currentStage -> palco atual
		return (Stage) ((Node) evento.getSource()).getScene().getWindow();//Stage � superclasse e precisar estar declarada
	}
	
	public static Integer tryParseToInt(String str) {//Convertendo o valor da caixinha para inteiro
		try {
			return Integer.parseInt(str);
		}
		catch (NumberFormatException e) {//Na hora da conversao
			return null;
		}
	}
}
