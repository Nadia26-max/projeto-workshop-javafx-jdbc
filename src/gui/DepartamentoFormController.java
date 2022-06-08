package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbExcecao;
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
import model.services.DepartamentoService;

public class DepartamentoFormController implements Initializable{

	//Iniciando a população dos formularios
	private Departamento entidade;
	
	//Dependência
	private DepartamentoService servico;
	
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
		}
		catch (DbExcecao e) {
			Alertas.showAlert("Erro ao salvar o objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private Departamento getFormDado() {
		Departamento obj = new Departamento();
		
		obj.setId(Uteis.tryParseToInt(txtId.getText()));//Se o que tiver aqui nao for numero inteiro, será nulo
		obj.setNome(txtNome.getText());
		
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
}
