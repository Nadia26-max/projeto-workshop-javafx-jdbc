package model.services;

import java.util.ArrayList;
import java.util.List;

import modelo.entities.Departamento;

public class DepartamentoService {
	
	public List<Departamento> findAll(){
		List<Departamento> lista = new ArrayList<>();
		
		//Instanciando alguns departamentos
		lista.add(new Departamento(1,"Livros"));
		lista.add(new Departamento(2,"Computadores"));
		lista.add(new Departamento(3,"Eletrônicos"));

		return lista;
	}
}
