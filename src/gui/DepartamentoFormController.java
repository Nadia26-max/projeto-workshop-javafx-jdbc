package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;
import model.exceptions.ValidacaoExcecao;
import model.services.DepartamentoService;

public class DepartamentoFormController implements Initializable{

	//Iniciando a população dos formularios
	private Departamento entidade;
	
	//Dependência
	private DepartamentoService servico;
	
	private List<DadoChangeListener> dadoCListener =  new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private Label labelErroNome;//Mensagem de erro caso tenha alguma coisa errada no preenchimento do nome
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	//Metodo set do Departamento
	//O controlador terá uma instancia do Departamento
	public void setDepartamento(Departamento entidade) {
		this.entidade = entidade;
	}	
	
	public void setDepartamentoService(DepartamentoService servico) {
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

	private Departamento getFormDado() {
		Departamento obj = new Departamento();
		
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
		Constraints.setTextFieldMaxLength(txtNome, 30);//Só aceitará até 30 caracteres
	}
	
	//Populando as caixas do formulario de fato com os dados que estao no objeto entidade
	public void atualizaFormDados() {
		
		if(entidade == null) {//Verificando se a entidade está nula
			throw new IllegalStateException("O objeto entidade está nulo");
		}
		txtId.setText(String.valueOf(entidade.getId()));//Convertendo id da entidade (inteiro) para String
		txtNome.setText(entidade.getNome());
	}
	
	private void setErroMensagens(Map<String , String> erros) {//Carrega os erros e preenche esses erros na caixinha de texto
		Set<String> fields = erros.keySet();//A partir dos nomes dos campos, percorro o conjunto
	
		if(fields.contains("nome")) {//Se nesse conjunto existe a chave nome
			labelErroNome.setText(erros.get("nome"));//Pega a mensagem no campo nome e seta no label
		}
	}
}
