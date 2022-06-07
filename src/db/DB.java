package db;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {//Métodos estáticos para conectar e desconectar com o bd
	
	//Para conectar com o banco de dados
	private static Connection con = null;
	
	//Método para a conexao
	public static Connection getConexao() {
		if(con == null) {
			try {
				Properties props = carregaPropriedades();//Propriedades do bd utilizando o método instanciado
				//url do bd
				String url = props.getProperty("dburl");//O que está definido em dburl no db.properties
				con = DriverManager.getConnection(url,props);//Para haver a conexao
			}
			catch (SQLException e) {
				throw new DbExcecao(e.getMessage());
			}
		}
		return con;
	}
	
	//Para fechar a conexao
	public static void fechaConexao() {
		if(con == null) {
			try {
				con.close();
			}
			catch (Exception e) {
				throw new DbExcecao(e.getMessage());
			}
		}
	}
	
	//Métodos para carregar as propriedades que estao salvas no arquivo (dados de conexao)
	private static Properties carregaPropriedades() {
		try (FileInputStream fs = new FileInputStream("db.properties")){
			Properties props = new Properties();
			props.load(fs);//Faz a leitura (load) do props apontado para o fs e guarda os dados no props
		return props;
		}
		catch (Exception e) {
			throw new DbExcecao(e.getMessage());//Passa os dados de acordo com o tratamento que criei
		}
	}
	//Evitar exceções na hora de fechar as conexoes
	public static void fechaStatement(Statement st) {
		if(st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbExcecao(e.getMessage());
			}
		}
	}
	
	public static void fechaResultSet(ResultSet rt) {
		if(rt != null) {
			try {
				rt.close();
			} catch (SQLException e) {
				throw new DbExcecao(e.getMessage());
			}
		}
	}
}
