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
	
	public void salvaOrAtualiza(Departamento obj) {//Insere um dado no bd ou atualiza
		if(obj.getId() == null) {//Insere novo departamento
			dao.insert(obj);
		}
		else {
			dao.update(obj);//Atualiza o departamento existente
		}
	}
	
	public void remove(Departamento obj) {//Remove algum dado do bd
		dao.deleteById(obj.getId());
	}
}
