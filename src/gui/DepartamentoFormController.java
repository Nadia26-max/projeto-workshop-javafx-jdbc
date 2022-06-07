package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;

public class DepartamentoFormController implements Initializable{

	//Iniciando a popula��o dos formularios
	private Departamento entidade;
	
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
	//O controlador ter� uma instancia do Departamento
	public void setDepartamento(Departamento entidade) {
		this.entidade = entidade;
	}	
	
	@FXML
	public void onBtSalvarAction() {
		System.out.println("Testando onBtSalvarAction");
	}
	
	@FXML
	public void onBtCancelarAction() {
		System.out.println("Testando onBtCancelarAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		inicializandoNodes();//Para iniciar este metodo
	}
	
	private void inicializandoNodes() {
		Constraints.setTextFieldInteger(txtId);//S� aceitar� numero inteiro
		Constraints.setTextFieldMaxLength(txtNome, 30);//S� aceitar� at� 30 caracteres
	}
	
	//Populando as caixas do formulario de fato com os dados que estao no objeto entidade
	public void atualizaFormDados() {
		
		if(entidade == null) {//Verificando se a entidade est� nula
			throw new IllegalStateException("O objeto entidade est� nulo");
		}
		txtId.setText(String.valueOf(entidade.getId()));//Convertendo id da entidade (inteiro) para String
		txtNome.setText(entidade.getNome());
	}
}
