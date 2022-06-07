package model.dao;

import java.util.List;
import model.entities.Departamento;
import model.entities.Vendedor;

public interface VendedorDao {

	//Inserindo, atualizando e excluíndo o objeto obj no bd
	void insert(Vendedor obj);
	void update(Vendedor obj);
	void deleteById(Integer id);
	
	//Retornando o Vendedor e recebendo o id como argumento
	//Responsavel por consultar no bd um objeto com esse id (se existir, retorna. Senao, retorna nulo
	Vendedor findById(Integer id);
	
	//Retornando todos os Vendedores
	List<Vendedor> findAll();
	
	//Buscando por Departamento
	List<Vendedor> findByDepartamento(Departamento departamento);

}
