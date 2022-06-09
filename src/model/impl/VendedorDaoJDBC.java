package model.impl;//Pacote onde estará a implementação do Dao

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbExcecao;
import model.dao.VendedorDao;
import model.entities.Departamento;
import model.entities.Vendedor;

public class VendedorDaoJDBC implements VendedorDao {

	// Instanciado antes da operações e assim, usarei em minhas conexoes abaixo
	private Connection con;

	public VendedorDaoJDBC(Connection con) {
		this.con = con;
	}

	@Override
	public void insert(Vendedor ven) {
		PreparedStatement st = null;

		try {
			st = con.prepareStatement("INSERT INTO vendedor (Name, Email, BirthDate, BaseSalary, DepartmentId)"
					+ "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, ven.getNome());
			st.setString(2, ven.getEmail());
			st.setDate(3, new java.sql.Date(ven.getNascimento().getTime()));
			st.setDouble(4, ven.getBaseSalario());
			st.setInt(5, ven.getDepartament().getId());

			int linhasAfetadas = st.executeUpdate();

			if (linhasAfetadas > 0) {// Se Inserir
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) { // Se Inserir - 1 dado
					int id = rs.getInt(1);// Primeira posição das chaves

					ven.setId(id);// ven ja recebe o valor da variavel id
				}

				DB.fechaResultSet(rs);
			} else {// Caso nenhuma linha seja alterada
				throw new DbExcecao("Erro inesperado!Nenhuma linha afetada");
			}
		} catch (SQLException e) {
			throw new DbExcecao(e.getMessage());
		} finally {
			DB.fechaStatement(st);
		}
	}

	@Override
	public void update(Vendedor ven) {
		PreparedStatement st = null;

		try {
			st = con.prepareStatement("UPDATE vendedor "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " + "WHERE Id = ?");

			st.setString(1, ven.getNome());
			st.setString(2, ven.getEmail());
			st.setDate(3, new java.sql.Date(ven.getNascimento().getTime()));
			st.setDouble(4, ven.getBaseSalario());
			st.setInt(5, ven.getDepartament().getId());
			st.setInt(6, ven.getId());// id do vendedor - Por causa do where no upadate

			st.executeUpdate();
		}

		catch (SQLException e) {
			throw new DbExcecao(e.getMessage());
		}

		finally {
			DB.fechaStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {

		PreparedStatement st = null;
		try {
			st = con.prepareStatement("DELETE FROM vendedor WHERE Id = ?");

			st.setInt(1, id);// Por causa do where
				
			st.executeUpdate();
			
			/* Para exibir uma exceção caso id nao exista
			 
			int linhasAfetadas = st.executeUpdate();
			
			if(linhasAfetadas == 0) {
				throw new DbExcecao("Nenhum dado foi alterado/excluído");
				//System.out.println("Nenhum dado foi alterado/excluído");
			}*/
			
		} catch (SQLException e) {
			throw new DbExcecao(e.getMessage());
		}

		finally {
			DB.fechaStatement(st);
		}
	}

	@Override
	public Vendedor findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = con.prepareStatement(
					"SELECT vendedor.*,departamento.Name as DepName " 
		           + "FROM vendedor "
				   + "INNER JOIN departamento ON vendedor.DepartmentId = departamento.Id " 
		           + "WHERE vendedor.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();

			// Verifica se tem um vendedor pelo id, senao retorna nulo
			if (rs.next()) {
				Departamento dep = instanciandoDepartamento(rs);

				Vendedor ven = instanciandoVendedor(rs, dep);
				return ven;
			}
			return null;
		} catch (SQLException e) {
			throw new DbExcecao(e.getMessage());
		}

		finally {
			DB.fechaStatement(st);
			DB.fechaResultSet(rs);
		}
	}

	private Vendedor instanciandoVendedor(ResultSet rs, Departamento dep) throws SQLException {
		Vendedor ven = new Vendedor();
		ven.setId(rs.getInt("Id"));// Nome da coluna no bd
		ven.setNome(rs.getString("Name"));
		ven.setEmail(rs.getString("Email"));
		ven.setNascimento(new java.util.Date(rs.getTimestamp("BirthDate").getTime()));
		ven.setBaseSalario(rs.getDouble("BaseSalary"));
		ven.setDepartament(dep);// Referenciando o Departmamento
		return ven;
	}

	private Departamento instanciandoDepartamento(ResultSet rs) throws SQLException {
		Departamento dep = new Departamento();
		dep.setId(rs.getInt("DepartmentId"));// Nome da coluna no bd
		dep.setNome(rs.getString("DepName"));// Nome da coluna no bd
		return dep;
	}

	@Override
	public List<Vendedor> findAll() {// Vai buscar todos os vendedores, ordenar mas nao tem restrição (where)

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = con.prepareStatement(
					"SELECT vendedor.*,departamento.Name as DepName " 
			      + "FROM vendedor "
				  + "INNER JOIN departamento ON vendedor.DepartmentId = departamento.Id " 
			      + "ORDER BY Name");

			rs = st.executeQuery();

			List<Vendedor> lista = new ArrayList<Vendedor>();

			// Para nao repetir o Departamento no while (os vendedores que apontam para o
			// departamento e nao o contrario - dois para um)
			Map<Integer, Departamento> mapa = new HashMap<>();
			// Criei um mapa vazio e posso guardar qualquer departamento que eu instanciar

			// Portanto, cada vez que passar no while, verifico se ja existe aquele
			// Departamento
			while (rs.next()) {
				// Busco dentro do mapa se ja existe alguma id 2, por exemplo.
				Departamento dep = mapa.get(rs.getInt("DepartmentId"));

				// Se nao existir, retorna nulo e instancia o departamento
				if (dep == null) {
					dep = instanciandoDepartamento(rs);

					// Guardando o Departamento no map - chave, departamento que vou salvar
					mapa.put(rs.getInt("DepartmentId"), dep);
				}
				// Apontando para o departamento existente ou o novo que instanciei
				Vendedor ven = instanciandoVendedor(rs, dep);
				lista.add(ven);
			}
			return lista;
		} catch (SQLException e) {
			throw new DbExcecao(e.getMessage());
		}

		finally {
			DB.fechaStatement(st);
			DB.fechaResultSet(rs);
		}
	}

	@Override
	public List<Vendedor> findByDepartamento(Departamento departamento) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = con.prepareStatement(
					"SELECT vendedor.*,departamento.Name as DepName " 
			      + "FROM vendedor "
				  + "INNER JOIN departamento ON vendedor.DepartmentId = departamento.Id "
				  + "WHERE DepartmentId = ? "
				  + "ORDER BY Name");

			st.setInt(1, departamento.getId());
			rs = st.executeQuery();

			List<Vendedor> lista = new ArrayList<Vendedor>();

			// Para nao repetir o Departamento no while (os vendedores que apontam para o
			// departamento e nao o contrario - dois para um)
			Map<Integer, Departamento> mapa = new HashMap<>();
			// Criei um mapa vazio e posso guardar qualquer departamento que eu instanciar

			// Portanto, cada vez que passar no while, verifico se ja existe aquele
			// Departamento
			while (rs.next()) {
				// Busco dentro do mapa se ja existe alguma id 2, por exemplo.
				Departamento dep = mapa.get(rs.getInt("DepartmentId"));

				// Se nao existir, retorna nulo e instancia o departamento
				if (dep == null) {
					dep = instanciandoDepartamento(rs);

					// Guardando o Departamento no map - chave, departamento que vou salvar
					mapa.put(rs.getInt("DepartmentId"), dep);
				}
				// Apontando para o departamento existente ou o novo que instanciei
				Vendedor ven = instanciandoVendedor(rs, dep);
				lista.add(ven);
			}
			return lista;
		} catch (SQLException e) {
			throw new DbExcecao(e.getMessage());
		}

		finally {
			DB.fechaStatement(st);
			DB.fechaResultSet(rs);
		}
	}
}
