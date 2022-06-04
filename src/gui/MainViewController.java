package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable{
	
	//Itens de Menus da tela
	@FXML
	private MenuItem menuItemVendedor;
	
	@FXML
	private MenuItem menuItemDepartamento;
	
	@FXML
	private MenuItem menuItemSobre;
	
	//Eventos
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
		System.out.println("Menu Sobre");
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
}
