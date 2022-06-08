package model.services;

import java.util.List;
import model.dao.DaoFabrica;
import model.dao.VendedorDao;
import model.entities.Vendedor;

public class VendedorService {
	
	private VendedorDao dao = DaoFabrica.createVendedorDao();
	
	public List<Vendedor> findAll(){//Adicionando dados via bd
		return dao.findAll();
	}
	
	public void salvaOrAtualiza(Vendedor obj) {//Insere um dado no bd ou atualiza
		if(obj.getId() == null) {//Insere novo Vendedor
			dao.insert(obj);
		}
		else {
			dao.update(obj);//Atualiza o Vendedor existente
		}
	}
	
	public void remove(Vendedor obj) {//Remove algum dado do bd
		dao.deleteById(obj.getId());
	}
}
