package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DBIntegridadeExcecao;
import gui.listeners.DadoChangeListener;
import gui.util.Alertas;
import gui.util.Uteis;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Vendedor;
import model.services.VendedorService;

public class VendedorListaController implements Initializable, DadoChangeListener {

	// Fazendo referencia/injeção de dependência à classe de serviço
	private VendedorService depservice;// Não precisa forçar o new VendedorService, crio um método

	@FXML
	private TableView<Vendedor> tableViewVendedor;

	@FXML
	private TableColumn<Vendedor, Integer> tableColunaId;// Referencia o Id

	@FXML
	private TableColumn<Vendedor, String> tableColunaNome;// Referencia o Nome

	@FXML
	private TableColumn<Vendedor, String> tableColunaEmail;// Referencia o Nome
	
	@FXML
	private TableColumn<Vendedor, Date> tableColunaDataNascimento;// Referencia o Nome
	
	@FXML
	private TableColumn<Vendedor, Double> tableColunaBaseSalario;// Referencia o Nome
	
	@FXML
	private TableColumn<Vendedor, Vendedor> tableColunaEDIT;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColunaREMOVE;

	@FXML
	private Button btNovo;

	private ObservableList<Vendedor> obsList;// Carrego os Vendedors na obsList

	// Tratamento de eventos do clique do botao
	public void onBtNovoAction(ActionEvent evento) {// Referencia para o controle que recebeu o evento. E a partir desse
													// evento, posso acessar o Stage
		Stage parentStage = Uteis.currentStage(evento);
		Vendedor obj = new Vendedor();// Iniciou vazio pois é o botao de novo
		criaFormDialogo(obj, "/gui/VendedorForm.fxml", parentStage);// Aí é criada a janela de formulario
	}

	// Setando dependência (classe de serviço) pelo método
	public void setVendedorService(VendedorService depservice) {
		this.depservice = depservice;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		initializeNodes();// Iniciar algum componente na tela
	}

	private void initializeNodes() {

		// Iniciando o comportamento das colunas - Padrao
		tableColunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		
		tableColunaDataNascimento.setCellValueFactory(new PropertyValueFactory<>("nascimento"));
		Uteis.formatTableColumnDate(tableColunaDataNascimento,"dd/MM/yyyy");
		
		tableColunaBaseSalario.setCellValueFactory(new PropertyValueFactory<>("baseSalario"));
		Uteis.formatTableColumnDouble(tableColunaBaseSalario, 2);//Duas casas decimais
		
		Stage stage = (Stage) Main.getMainScene().getWindow();// Referencia para a janela com o Stage em partentes pois
																// o Main é superclasse
		// Deixar a parte debaixo na subjanela de Vendedor acompanhando a parte
		// inferior da tela
		tableViewVendedor.prefHeightProperty().bind(stage.heightProperty());
	}

	// Responsavel por acessar o serviço, carregar os Vendedors e jogar os
	// Vendedors na ObservableList. Assim, associo com o tableView e aí os
	// Vendedors aparecerao na tela
	public void atualizaTableView() {

		// Teste para evitar que ocorra algum problema
		if (depservice == null) {// Se o programador esqueceu de setar esse serviço, terei que lançar uma excecao
			throw new IllegalStateException("O serviço estava nulo");
		}
		// Passando desse teste de excecao
		List<Vendedor> lista = depservice.findAll();// recupera os serviços do findAll

		// Carregando a lista dentro do ObservableList
		obsList = FXCollections.observableArrayList(lista);// Instancia o obsList da lista
		tableViewVendedor.setItems(obsList);// Mostrando, na tela, os dados instanciados na obsList

		//Chama os metodos
		iniciaEdicaoButtons();// Acrescenta um botao de edição em cada linha da tabela e vai abrir o formulario de edição usando o criaFormDialogo
		iniciaRemoveButtons();
	}

	private void criaFormDialogo(Vendedor obj, String absolutoNome, Stage parentStage) {// Informando quem criou
																							// essa janela de dialogo
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutoNome));
			Pane painel = loader.load();

			// Referência para a tela de formulario no Controller
			VendedorFormController dfc = loader.getController();
			dfc.setVendedor(obj);
			dfc.setVendedorService(new VendedorService());// Injeção de depencia para funcionar o servico de
																	// salvar os dados no bd com o botao
			dfc.subscribeDadoChangeListener(this);// executa o objeto do VendedorListController, dispara o evento e
													// executa o metodo onDataChanged
			dfc.atualizaFormDados();

			// Janela do formulario para preencher um novo Vendedor
			// Colocando outro Stage - Um palco na frente de outro
			Stage dialogoStage = new Stage();
			dialogoStage.setTitle("Entre com os dados de Vendedor:");// Título para a janela
			dialogoStage.setScene(new Scene(painel));// Novo Stage, nova cena
			dialogoStage.setResizable(false);// Informa se a janela pode ou nao ser redimensionada (no caso, nao)
			dialogoStage.initOwner(parentStage);// Entra o pai dessa janela
			dialogoStage.initModality(Modality.WINDOW_MODAL);// Se a janela será modal ou terá outro comportamento -
																// Modal (tem que fechar esta para acessar a anterior)
			dialogoStage.showAndWait();
		} catch (IOException e) {
			Alertas.showAlert("IOException", "Erro ao carregar a view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		atualizaTableView();// Chama o metodo que atualiza
	}

	private void iniciaEdicaoButtons() {
		tableColunaEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColunaEDIT.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {

			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criaFormDialogo(obj, "/gui/VendedorForm.fxml", Uteis.currentStage(event)));
			}
		});
	}

	private void iniciaRemoveButtons() {
		tableColunaREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColunaREMOVE.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("excluir");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntidade(obj));
			}

		});
	}

	private void removeEntidade(Vendedor obj) {
		Optional<ButtonType> resultado = Alertas.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
	
		//get - funciona para acessar o objeto que está dentro do get
		if(resultado.get() == ButtonType.OK) {//Ok confirma a exclusao
			if(depservice ==  null) {
				throw new IllegalStateException("O objeto serviço está nulo");//Pois pode gerar uma exceção
			}
			try {
			depservice.remove(obj);
			atualizaTableView();
			}
			catch (DBIntegridadeExcecao e) {
				Alertas.showAlert("Erro ao excluir o objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
