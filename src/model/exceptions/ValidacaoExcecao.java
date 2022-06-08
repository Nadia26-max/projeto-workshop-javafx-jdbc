package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidacaoExcecao extends RuntimeException {//Valida um formulario

	private static final long serialVersionUID = 1L;

	//Carregando erros na exceção
	//* Map - Coleção de chave,valor (o primeiro indica o nome do campo, o segundo vai indicar a mensagem de erro
	private Map<String , String> erros =  new HashMap<String, String>();
	
	//Instanciando a exceção
	public ValidacaoExcecao(String msg) {
		super(msg);
	}
	
	public Map<String , String> getErros(){
		return erros;
	}
	
	//Permite adicionar um elemento na coleção - * 
	public void addErro(String fieldNome, String erroMensagem) {//Nome do campo, mensagem de erro
		erros.put(fieldNome, erroMensagem);//Inserindo no nome e mostrando a mensagem
	}
}
