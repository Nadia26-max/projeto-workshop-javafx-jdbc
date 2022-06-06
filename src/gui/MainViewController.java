package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alertas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

	// Itens de Menus da tela
	@FXML
	private MenuItem menuItemVendedor;

	@FXML
	private MenuItem menuItemDepartamento;

	@FXML
	private MenuItem menuItemSobre;

	// Eventos
	@FXML
	public void onMenuItemVendedorAction() {
		System.out.println("Menu de Vendedor");
	}

	@FXML
	public void onMenuItemDepartamentoAction() {
		System.out.println("Menu de Departamento");
	}

	@FXML
	public void onMenuItemItemSobreAction() {
		carregaView("/gui/Sobre.fxml");
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {

	}

	private synchronized void carregaView(String absolutoNome) {//Caminho completo (pasta até o arquivo)
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutoNome));
		
			VBox novoVBox = loader.load();
			
			//Mostrar a view dentro da janela principal - Pego a referencia da cena
			Scene mainScene = Main.getMainScene();
			
			//Referenciando o VBox da Sobre.fxml na janela principal - MainView.fxml
			//Conteudo do ScrollPane - ScrollPane referenciado para o VBox da janela principal
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();//Pega o primeiro elemento da minha view (ScrollPane), seguindo elemento content
			
			//Guardando referencia para o menu
			Node mainMenu = mainVBox.getChildren().get(0);//Primeiro filho na posição zero
			
			mainVBox.getChildren().clear();//Limpando todos os filhos do mainVBox
			
			//Adicionando no mainVBox o mainMenu e os filhos do novoVBox
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(novoVBox.getChildren());
		}
		catch (IOException e) {
			Alertas.showAlert("IoException", "Erro ao carregar a página", e.getMessage(), AlertType.ERROR);
		}
	}
}
