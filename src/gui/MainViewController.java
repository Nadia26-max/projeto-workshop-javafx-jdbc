package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
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
import model.services.DepartamentoService;

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
		carregaView("/gui/DepartamentoLista.fxml",(DepartamentoListaController controller) -> {
			
			//Injetando dependencia no DepartamentoController
			controller.setDepartamentoService(new DepartamentoService());
			controller.atualizaTableView();//ja posso atualizar os dados na tela 
		});
	}

	@FXML
	public void onMenuItemItemSobreAction() {
		carregaView("/gui/Sobre.fxml", x -> {});//Não tem nada para fazer, entao as chaves sao vazias
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {

	}
				//Funçao carrega view do tipo T
	private synchronized <T> void carregaView(String absolutoNome,Consumer<T> inicializandoAcao) {//Caminho completo (pasta até o arquivo)
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
			
			//Ativando função que passar em Consumer<T> inicializandoAcao
			T controller =  loader.getController();//O controlador vai retornar o controle do tipo que eu chamar no metodo onMenuItemDepartamentoAction
		
			//Chamando a função inicializandoAcao
			inicializandoAcao.accept(controller);//Executará o que será passado na função onMenuItemDepartamentoAction
		}
		catch (IOException e) {
			Alertas.showAlert("IoException", "Erro ao carregar a página", e.getMessage(), AlertType.ERROR);
		}
	}
	/*
	private synchronized void carregaView2(String absolutoNome) {//Caminho completo (pasta até o arquivo)
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutoNome));
		
			VBox novoVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();//Pega o primeiro elemento da minha view (ScrollPane), seguindo elemento content
			
			Node mainMenu = mainVBox.getChildren().get(0);//Primeiro filho na posição zero
			
			mainVBox.getChildren().clear();//Limpando todos os filhos do mainVBox
			
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(novoVBox.getChildren());
			
			DepartamentoListaController controller = loader.getController();//Acessando o controller (se precisar, tambem poderei carregar a view 
		
			//Injetando dependencia no DepartamentoController
			controller.setDepartamentoService(new DepartamentoService());
			controller.atualizaTableView();//ja posso atualizar os dados na tela 
		}
		catch (IOException e) {
			Alertas.showAlert("IoException", "Erro ao carregar a página", e.getMessage(), AlertType.ERROR);
		}
	}*/
}
