package db;

public class DBIntegridadeExcecao extends RuntimeException{

	//Para deletar dados no banco
	private static final long serialVersionUID = 1L;//Id padrao
	
	public DBIntegridadeExcecao(String msg) {
		super(msg);
	}

}
