package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbExcecao;
import gui.listeners.DadoChangeListener;
import gui.util.Alertas;
import gui.util.Constraints;
import gui.util.Uteis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Departamento;
import model.entities.Vendedor;
import model.exceptions.ValidacaoExcecao;
import model.services.DepartamentoService;
import model.services.VendedorService;

public class VendedorFormController implements Initializable {

	// Iniciando a população dos formularios
	private Vendedor entidade;

	// Dependência
	private VendedorService servico;

	private DepartamentoService dservico;

	private List<DadoChangeListener> dadoCListener = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpNascimento;

	@FXML
	private TextField txtBaseSalario;

	@FXML
	private ComboBox<Departamento> comboBoxDepartamento;

	@FXML
	private Label labelErroNome;// Mensagem de erro caso tenha alguma coisa errada no preenchimento do nome

	@FXML
	private Label labelErroEmail;

	@FXML
	private Label labelErroNascimento;

	@FXML
	private Label labelErroBaseSalario;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	private ObservableList<Departamento> obsLista;

	// Metodo set do Vendedor
	// O controlador terá uma instancia do Vendedor
	public void setVendedor(Vendedor entidade) {
		this.entidade = entidade;
	}

	public void setServices(VendedorService servico, DepartamentoService dservico) {// Vai injetar dois serviços de uma
																					// vez
		this.servico = servico;
		this.dservico = dservico;
	}

	// Vai escrever/adicionar esse listener na lista
	public void subscribeDadoChangeListener(DadoChangeListener listener) {
		dadoCListener.add(listener);
	}

	@FXML
	public void onBtSalvarAction(ActionEvent evento) {// Salvando departamento no bd

		// Caso a dependencia nao seja injetada
		if (entidade == null) {
			throw new IllegalStateException("O objeto entidade está nulo");
		}

		// Caso a dependencia nao seja injetada
		if (servico == null) {
			throw new IllegalStateException("O objeto serviço está nulo");
		}

		try {
			// Pega os dados que estao na caixinha do formulario e instanciar um
			// departamento
			entidade = getFormDado();// Pego os dados e jogo na variavel entidade
			servico.salvaOrAtualiza(entidade);
			Uteis.currentStage(evento).close();// Pega a referencia da janela e depois fecha
			notificaDadoChangeListeners();
		} catch (ValidacaoExcecao e) {
			setErroMensagens(e.getErros());// Coleção de erros
		} catch (DbExcecao e) {
			Alertas.showAlert("Erro ao salvar o objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notificaDadoChangeListeners() {
		for (DadoChangeListener listener : dadoCListener) {
			listener.onDataChanged();
		}
	}

	private Vendedor getFormDado() {// Pega os dados que foram carregados no formulario e carregam os dados com este
									// objeto
		Vendedor obj = new Vendedor();

		ValidacaoExcecao excecao = new ValidacaoExcecao("Erro de validação");

		obj.setId(Uteis.tryParseToInt(txtId.getText()));// Se o que tiver aqui nao for numero inteiro, será nulo
		/*------------------------------------------------------------*/
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {// Se o nome for nulo ou o espaço vazio
																				// for igual ao string vazio, significa
																				// que a caixinha está vazia
			excecao.addErro("nome", "O campo não pode ser vazio");
		}
		obj.setNome(txtNome.getText());
		/*------------------------------------------------------------*/
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			excecao.addErro("email", "O campo não pode ser vazio");
		}
		obj.setEmail(txtEmail.getText());
		/*------------------------------------------------------------*/
		if(dpNascimento.getValue() == null) {
			excecao.addErro("nascimento", "O campo não pode ser vazio");
		}
		else {
		//Converte a data da maquina de usuario, para o Instant(do tipo DatePicker) - independente da localidade
		Instant instante = Instant.from(dpNascimento.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setNascimento(Date.from(instante));//Espera um Date, entao é preciso escrever dessa forma
		}
		/*------------------------------------------------------------*/
		if (txtBaseSalario.getText() == null || txtBaseSalario.getText().trim().equals("")) {
			excecao.addErro("baseSalario", "O campo não pode ser vazio");
		}
		obj.setBaseSalario(Uteis.tryParseToDouble(txtBaseSalario.getText()));
		/*------------------------------------------------------------*/
		obj.setDepartament(comboBoxDepartamento.getValue());
		/*------------------------------------------------------------*/
		
		if (excecao.getErros().size() > 0) {// Se tem pelo menos um erro, lanço a exceção
			throw excecao;
		}
		return obj;
	}

	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Uteis.currentStage(evento).close();// Pega a referencia da janela e depois fecha
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		inicializandoNodes();// Para iniciar este metodo
	}

	private void inicializandoNodes() {
		Constraints.setTextFieldInteger(txtId);// Só aceitará numero inteiro
		Constraints.setTextFieldMaxLength(txtNome, 50);// Só aceitará até 30 caracteres
		Constraints.setTextFieldMaxLength(txtEmail, 30);
		Uteis.formatDatePicker(dpNascimento, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(txtBaseSalario);

		inicializandoComboBoxDepartamento();
	}

	// Populando as caixas do formulario de fato com os dados que estao no objeto
	// entidade - Pega os os objetos e joga na caixinha do formulario
	public void atualizaFormDados() {

		if (entidade == null) {// Verificando se a entidade está nula
			throw new IllegalStateException("O objeto entidade está nulo");
		}
		txtId.setText(String.valueOf(entidade.getId()));// Convertendo id da entidade (inteiro) para String
		txtNome.setText(entidade.getNome());
		txtEmail.setText(entidade.getEmail());

		if (entidade.getNascimento() != null) {
			// Criando uma data local
			dpNascimento.setValue(LocalDate.ofInstant(entidade.getNascimento().toInstant(), ZoneId.systemDefault()));// Pega
																													// usuario
		}

		Locale.setDefault(Locale.UK);
		txtBaseSalario.setText(String.format("%.2f", entidade.getBaseSalario()));

		if (entidade.getDepartament() == null) {// Para novo departamento
			comboBoxDepartamento.getSelectionModel().selectFirst();// Pegando o primeiro elemento do combobox
		} else {// Se ja tiver departamento
			comboBoxDepartamento.setValue(entidade.getDepartament());// O departamento que estiver associado ao vendedor
																		// // irá para o combobox (se nao for nulo)
		}
	}

	public void carregandoObjetosAssociados() {// carregandos os dados do bd
		if (dservico == null) {
			throw new IllegalStateException("O Departamento está nulo");
		}
		List<Departamento> lista = dservico.findAll(); // buscando os objetos do bd
		obsLista = FXCollections.observableArrayList(lista);
		comboBoxDepartamento.setItems(obsLista);
		;
	}

	private void setErroMensagens(Map<String, String> erros) {// Carrega os erros e preenche esses erros na caixinha de
																// texto
		Set<String> fields = erros.keySet();// A partir dos nomes dos campos, percorro o conjunto

		//Sem operador ternario (se somente assim, implementa todos os campos desta forma
		if (fields.contains("nome")) {// Se nesse conjunto existe a chave nome
			labelErroNome.setText(erros.get("nome"));// Pega a mensagem no campo nome e seta no label
		}
		else {
			labelErroNome.setText("");//Se colocou dado, apaga a mensagem de erro
		}
		
		//Com operador ternario
		labelErroEmail.setText((fields.contains("email")? erros.get("email"): ""));
		
		//Com operador ternario
		labelErroNascimento.setText((fields.contains("nascimento")? erros.get("nascimento"): ""));
				
		//Com operador ternario
		labelErroBaseSalario.setText((fields.contains("baseSalario")? erros.get("baseSalario"): ""));
	}

	private void inicializandoComboBoxDepartamento() {
		Callback<ListView<Departamento>, ListCell<Departamento>> factory = lv -> new ListCell<Departamento>() {
			@Override
			protected void updateItem(Departamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxDepartamento.setCellFactory(factory);
		comboBoxDepartamento.setButtonCell(factory.call(null));
	}
}
