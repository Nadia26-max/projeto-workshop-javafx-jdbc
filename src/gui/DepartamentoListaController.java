package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.entities.Departamento;

public class DepartamentoListaController implements Initializable{
	
	@FXML
	private TableView<Departamento> tableViewDepartamento;
	
	@FXML
	private TableColumn<Departamento,Integer> tableColunaId;//Referencia o Id 
	@FXML
	private TableColumn<Departamento,String> tableColunaNome;//Referencia o Nome
	
	@FXML
	private Button btNovo;
	
	//Tratamento de eventos do clique do botao
	public void onBtNovoAction() {
		System.out.println("Testando botao novo");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		initializeNodes();//Iniciar algum componente na tela
	}

	private void initializeNodes() {

		//Iniciando o comportamento das colunas - Padrao
		tableColunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColunaNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();//Referencia para a janela com o Stage em partentes pois o Main é superclasse
		
		//Deixar a parte debaixo na subjanela de Departamento acompanhando a parte inferior da tela
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}
}
