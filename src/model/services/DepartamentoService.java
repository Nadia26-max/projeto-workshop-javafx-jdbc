package model.services;

import java.util.List;
import model.dao.DaoFabrica;
import model.dao.DepartamentoDao;
import model.entities.Departamento;

public class DepartamentoService {
	
	private DepartamentoDao dao = DaoFabrica.createDepartamentoDao();
	
	public List<Departamento> findAll(){//Adicionando dados via bd
		return dao.findAll();
	}
}
