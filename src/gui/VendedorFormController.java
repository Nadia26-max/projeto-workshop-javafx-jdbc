package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Vendedor;
import model.exceptions.ValidacaoExcecao;
import model.services.VendedorService;

public class VendedorFormController implements Initializable{

	//Iniciando a população dos formularios
	private Vendedor entidade;
	
	//Dependência
	private VendedorService servico;
	
	private List<DadoChangeListener> dadoCListener =  new ArrayList<>();
	
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
	private Label labelErroNome;//Mensagem de erro caso tenha alguma coisa errada no preenchimento do nome
	
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
	
	//Metodo set do Vendedor
	//O controlador terá uma instancia do Vendedor
	public void setVendedor(Vendedor entidade) {
		this.entidade = entidade;
	}	
	
	public void setVendedorService(VendedorService servico) {
		this.servico = servico;
	}	
	
	//Vai escrever/adicionar esse listener na lista
	public void subscribeDadoChangeListener(DadoChangeListener listener) {
		dadoCListener.add(listener);
	}
	
	@FXML
	public void onBtSalvarAction(ActionEvent evento) {//Salvando departamento no bd
		
		//Caso a dependencia nao seja injetada
		if(entidade == null) {
			throw new IllegalStateException("O objeto entidade está nulo");
		}
		
		//Caso a dependencia nao seja injetada
		if(servico == null) {
			throw new IllegalStateException("O objeto serviço está nulo");
		}
		
		try {
			//Pega os dados que estao na caixinha do formulario e instanciar um departamento
			entidade = getFormDado();//Pego os dados e jogo na variavel entidade
			servico.salvaOrAtualiza(entidade);
			Uteis.currentStage(evento).close();//Pega a referencia da janela e depois fecha
			notificaDadoChangeListeners();
		}
		catch (ValidacaoExcecao e) {
			setErroMensagens(e.getErros());//Coleção de erros
		}
		catch (DbExcecao e) {
			Alertas.showAlert("Erro ao salvar o objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notificaDadoChangeListeners() {
		for(DadoChangeListener listener: dadoCListener) {
			listener.onDataChanged();
		}
	}

	private Vendedor getFormDado() {
		Vendedor obj = new Vendedor();
		
		ValidacaoExcecao excecao = new ValidacaoExcecao("Erro de validação");
		
		obj.setId(Uteis.tryParseToInt(txtId.getText()));//Se o que tiver aqui nao for numero inteiro, será nulo
		
		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {//Se o nome for nulo ou o espaço vazio for igual ao string vazio, significa que a caixinha está vazia 
			excecao.addErro("nome", "O campo não pode ser vazio");
		}
		obj.setNome(txtNome.getText());
		
		if(excecao.getErros().size() > 0) {//Se tem pelo menos um erro, lanço a exceção
			throw excecao;
		}
		
		return obj;
	}
	
	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Uteis.currentStage(evento).close();//Pega a referencia da janela e depois fecha
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		inicializandoNodes();//Para iniciar este metodo
	}
	
	private void inicializandoNodes() {
		Constraints.setTextFieldInteger(txtId);//Só aceitará numero inteiro
		Constraints.setTextFieldMaxLength(txtNome, 50);//Só aceitará até 30 caracteres
		Constraints.setTextFieldMaxLength(txtEmail, 30);
		Uteis.formatDatePicker(dpNascimento, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(txtBaseSalario);
	}
	
	//Populando as caixas do formulario de fato com os dados que estao no objeto entidade - Pega os os objetos e joga na caixinha do formulario
	public void atualizaFormDados() {
		
		if(entidade == null) {//Verificando se a entidade está nula
			throw new IllegalStateException("O objeto entidade está nulo");
		}
		txtId.setText(String.valueOf(entidade.getId()));//Convertendo id da entidade (inteiro) para String
		txtNome.setText(entidade.getNome());
		txtEmail.setText(entidade.getEmail());
		
		if(entidade.getNascimento() != null) {
		//Criando uma data local
		dpNascimento.setValue(LocalDate.ofInstant(entidade.getNascimento().toInstant(), ZoneId.systemDefault()));//Pega o horario da maquina de usuario
		}
		
		Locale.setDefault(Locale.UK);
		txtBaseSalario.setText(String.format("%.2f", entidade.getBaseSalario()));
	}
	
	private void setErroMensagens(Map<String , String> erros) {//Carrega os erros e preenche esses erros na caixinha de texto
		Set<String> fields = erros.keySet();//A partir dos nomes dos campos, percorro o conjunto
	
		if(fields.contains("nome")) {//Se nesse conjunto existe a chave nome
			labelErroNome.setText(erros.get("nome"));//Pega a mensagem no campo nome e seta no label
		}
	}
}
