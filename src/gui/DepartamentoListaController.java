package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import application.Main;
import gui.util.Alertas;
import gui.util.Uteis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.services.DepartamentoService;
import model.entities.Departamento;

public class DepartamentoListaController implements Initializable {

	// Fazendo referencia/inje��o de depend�ncia � classe de servi�o
	private DepartamentoService depservice;// N�o precisa for�ar o new DepartamentoService, crio um m�todo

	@FXML
	private TableView<Departamento> tableViewDepartamento;

	@FXML
	private TableColumn<Departamento, Integer> tableColunaId;// Referencia o Id
	@FXML
	private TableColumn<Departamento, String> tableColunaNome;// Referencia o Nome

	@FXML
	private Button btNovo;

	private ObservableList<Departamento> obsList;// Carrego os departamentos na obsList

	// Tratamento de eventos do clique do botao
	public void onBtNovoAction(ActionEvent evento) {//Referencia para o controle que recebeu o evento. E a partir desse evento, posso acessar o Stage
		Stage parentStage = Uteis.currentStage(evento);
		Departamento obj = new Departamento();//Iniciou vazio pois � o botao de novo
		criaFormDialogo(obj,"/gui/DepartamentoForm.fxml", parentStage);//A� � criada a janela de formulario
	}

	// Setando depend�ncia (classe de servi�o) pelo m�todo
	public void setDepartamentoService(DepartamentoService depservice) {
		this.depservice = depservice;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		initializeNodes();// Iniciar algum componente na tela
	}

	private void initializeNodes() {

		// Iniciando o comportamento das colunas - Padrao
		tableColunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColunaNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));

		Stage stage = (Stage) Main.getMainScene().getWindow();// Referencia para a janela com o Stage em partentes pois
																// o Main � superclasse

		// Deixar a parte debaixo na subjanela de Departamento acompanhando a parte
		// inferior da tela
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}

	// Responsavel por acessar o servi�o, carregar os departamentos e jogar os
	// departamentos na ObservableList. Assim, associo com o tableView e a� os
	// departamentos aparecerao na tela
	public void atualizaTableView() {

		// Teste para evitar que ocorra algum problema
		if (depservice == null) {// Se o programador esqueceu de setar esse servi�o, terei que lan�ar uma excecao
			throw new IllegalStateException("O servi�o estava nulo");
		}
		// Passando desse teste de excecao
		List<Departamento> lista = depservice.findAll();// recupera os servi�os do findAll

		// Carregando a lista dentro do ObservableList
		obsList = FXCollections.observableArrayList(lista);// Instancia o obsList da lista
		tableViewDepartamento.setItems(obsList);// Mostrando, na tela, os dados instanciados na obsList
	}

		private void criaFormDialogo(Departamento obj,String absolutoNome, Stage parentStage) {//Informando quem criou essa janela de dialogo
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutoNome));
				Pane painel = loader.load();
				
				//Refer�ncia para a tela de formulario no Controller
				DepartamentoFormController dfc = loader.getController();
				dfc.setDepartamento(obj);
				dfc.setDepartamentoService(new DepartamentoService());//Inje��o de depencia para funcionar o servico de salvar os dados no bd com o botao
				dfc.atualizaFormDados();
				
				//Janela do formulario para preencher um novo Departamento
				//Colocando outro Stage - Um palco na frente de outro
				Stage dialogoStage = new Stage();
				dialogoStage.setTitle("Entre com os dados de Departamento:");//T�tulo para a janela
				dialogoStage.setScene(new Scene(painel));//Novo Stage, nova cena
				dialogoStage.setResizable(false);//Informa se a janela pode ou nao ser redimensionada (no caso, nao)
				dialogoStage.initOwner(parentStage);//Entra o pai dessa janela 
				dialogoStage.initModality(Modality.WINDOW_MODAL);//Se a janela ser� modal ou ter� outro comportamento - Modal (tem que fechar esta para acessar a anterior)
				dialogoStage.showAndWait();
			}
			catch (IOException e) {
				Alertas.showAlert("IOException", "Erro ao carregar a view", e.getMessage(), AlertType.ERROR);
			}
		}
}
