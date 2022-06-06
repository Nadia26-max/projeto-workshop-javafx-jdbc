package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.services.DepartamentoService;
import modelo.entities.Departamento;

public class DepartamentoListaController implements Initializable{
	
	//Fazendo referencia/inje��o de depend�ncia � classe de servi�o
	private DepartamentoService depservice;//N�o precisa for�ar o new DepartamentoService, crio um m�todo
	
	@FXML
	private TableView<Departamento> tableViewDepartamento;
	
	@FXML
	private TableColumn<Departamento,Integer> tableColunaId;//Referencia o Id 
	@FXML
	private TableColumn<Departamento,String> tableColunaNome;//Referencia o Nome
	
	@FXML
	private Button btNovo;
	
	private ObservableList<Departamento> obsList;//Carrego os departamentos na obsList
	
	//Tratamento de eventos do clique do botao
	public void onBtNovoAction() {
		System.out.println("Testando botao novo");
	}
	
	//Setando depend�ncia (classe de servi�o) pelo m�todo
	public void setDepartamentoService(DepartamentoService depservice) {
		this.depservice = depservice;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		initializeNodes();//Iniciar algum componente na tela
	}

	private void initializeNodes() {

		//Iniciando o comportamento das colunas - Padrao
		tableColunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColunaNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();//Referencia para a janela com o Stage em partentes pois o Main � superclasse
		
		//Deixar a parte debaixo na subjanela de Departamento acompanhando a parte inferior da tela
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}
		
		//Responsavel por acessar o servi�o, carregar os departamentos e jogar os departamentos na ObservableList. Assim, associo com o tableView e a� os departamentos aparecerao na tela 
		public void atualizaTableView() {
			
		//Teste para evitar que ocorra algum problema
		if(depservice == null) {//Se o programador esqueceu de setar esse servi�o, terei que lan�ar uma excecao
			throw new IllegalStateException("O servi�o estava nulo");
		}
		//Passando desse teste de excecao
		List<Departamento> lista = depservice.findAll();//recupera os servi�os do findAll
		
		//Carregando a lista dentro do ObservableList
		obsList = FXCollections.observableArrayList(lista);//Instancia o obsList da lista
		tableViewDepartamento.setItems(obsList);//Mostrando, na tela, os dados instanciados na obsList
	}
}
