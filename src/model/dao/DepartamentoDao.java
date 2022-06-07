package model.dao;

import java.util.List;

import model.entities.Departamento;

public interface DepartamentoDao {
	
	//Inserindo, atualizando e excluíndo o objeto obj no bd
	void insert(Departamento obj);
	void update(Departamento obj);
	void deleteById(Integer id);
	
	//Retornando o Departamento e recebendo o id como argumento
	//Responsavel por consultar no bd um objeto com esse id (se existir, retorna. Senao, retorna nulo
	Departamento findById(Integer id);
	
	//Retornando todos os Departamentos
	List<Departamento> findAll();
}
