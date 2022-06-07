package model.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DBIntegridadeExcecao;
import db.DbExcecao;
import model.dao.DepartamentoDao;
import model.entities.Departamento;

public class DepartamentoDaoJDBC implements DepartamentoDao {

	private Connection con;

	public DepartamentoDaoJDBC(Connection con) {
		this.con = con;
	}

	@Override
	public void insert(Departamento dep) {

		PreparedStatement st = null;
		
		try {
			st = con.prepareStatement(
				"INSERT INTO departamento (Name) VALUES (?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, dep.getNome());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					dep.setId(id);
				}
			}
			else {
				throw new DbExcecao("Erro inesperado! Nenhuma linha afetada!");
			}
		}
		catch (SQLException e) {
			throw new DbExcecao(e.getMessage());
		} 
		finally {
			DB.fechaStatement(st);
		}
	}

	@Override
	public void update(Departamento dep) {
		
		PreparedStatement st = null;
		try {
			st = con.prepareStatement(
				"UPDATE departamento SET Name = ? WHERE Id = ?");

			st.setString(1, dep.getNome());
			st.setInt(2, dep.getId());

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
			st = con.prepareStatement(
				"DELETE FROM departamento WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DBIntegridadeExcecao(e.getMessage());
		} 
		finally {
			DB.fechaStatement(st);
		}
	}

	@Override
	public Departamento findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement(
				"SELECT * FROM departamento WHERE Id = ?");
			
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if (rs.next()) {
				Departamento dep = new Departamento();
				dep.setId(rs.getInt("Id"));
				dep.setNome(rs.getString("Name"));
				return dep;
			}
			
			return null;
		}
		catch (SQLException e) {
			throw new DbExcecao(e.getMessage());
		}
		
		finally {
			DB.fechaStatement(st);
			DB.fechaResultSet(rs);
		}
	}

	@Override
	public List<Departamento> findAll() {

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = con.prepareStatement(
					
				"SELECT * FROM departamento ORDER BY Name");
			
			rs = st.executeQuery();

			List<Departamento> lista = new ArrayList<>();

			while (rs.next()) {
				Departamento ven = new Departamento();
				ven.setId(rs.getInt("Id"));
				ven.setNome(rs.getString("Name"));
				lista.add(ven);
			}
			return lista;
		}
		catch (SQLException e) {
			throw new DbExcecao(e.getMessage());
		}
		finally {
			DB.fechaStatement(st);
			DB.fechaResultSet(rs);
		}
	}
}
