package dao;

import model.Concessionaria;
//import dao.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.Timestamp;
//import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class ConcessionariaDAO extends DAO {	
	public ConcessionariaDAO() {
		super();
		conectar();
	}
	
	
	public void finalize() {
		close();
	}
	
	
	/*public boolean insert(Concessionaria concessionaria) {
		boolean status = false;
		try {
			String sql = "INSERT INTO concessionaria (descricao, preco, quantidade, datafabricacao, datavalidade) "
		               + "VALUES ('" + concessionaria.getDescricao() + "', "
		               + concessionaria.getPreco() + ", " + concessionaria.getQuantidade() + ", ?, ?);";
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(concessionaria.getDataFabricacao()));
			st.setDate(2, Date.valueOf(concessionaria.getDataValidade()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}*/
	
	public boolean inserirVeiculo(Concessionaria veiculo) {
		boolean status = false;
		try {
			
			String sql = "INSERT INTO veiculo (marca, ano, cor) VALUES (?,?,?)";
			PreparedStatement st = conexao.prepareStatement(sql);
			
			st.setString(1, veiculo.getMarca());
			st.setInt(2, veiculo.getAno());
			st.setString(3, veiculo.getCor());
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		
		return status;
	}

	
	/*public Concessionaria get(int id) {
		Concessionaria concessionaria = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM concessionaria WHERE id="+id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	 concessionaria = new Concessionaria(rs.getInt("id"), rs.getString("descricao"), (float)rs.getDouble("preco"), 
	                				   rs.getInt("quantidade"), 
	        			               rs.getTimestamp("datafabricacao").toLocalDateTime(),
	        			               rs.getDate("datavalidade").toLocalDate());
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return concessionaria;
	}*/
	
	public Concessionaria getVeiculoId(int id) {
		Concessionaria veiculo = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM veiculo WHERE id_veiculo = " + id;
			ResultSet rs = st.executeQuery(sql);		
			if(rs.next()){
				veiculo = new Concessionaria(rs.getInt("id_veiculo"), rs.getString("marca"), 
						rs.getInt("ano"), rs.getString("cor"));
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return veiculo;
	}
	
	public List<Concessionaria> get() {
		return get("");
	}

	
	public List<Concessionaria> getOrderByID() {
		return get("id_veiculo");		
	}
	
	
	public List<Concessionaria> getOrderByMarca() {
		return get("marca");		
	}
	
	
	public List<Concessionaria> getOrderByAno() {
		return get("ano");		
	}
	
	
	//Verificar segurança pra evitar SQL INJECTION
	private List<Concessionaria> get(String orderBy) {
		List<Concessionaria> veiculos = new ArrayList<Concessionaria>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM veiculo" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Concessionaria p = new Concessionaria(rs.getInt("id_veiculo"), rs.getString("marca"), rs.getInt("ano") ,rs.getString("cor"));
	            veiculos.add(p);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return veiculos;
	}
	
	
	/*public boolean update(Concessionaria concessionaria) {
		boolean status = false;
		try {  
			String sql = "UPDATE concessionaria SET descricao = '" + concessionaria.getDescricao() + "', "
					   + "preco = " + concessionaria.getPreco() + ", " 
					   + "quantidade = " + concessionaria.getQuantidade() + ","
					   + "datafabricacao = ?, " 
					   + "datavalidade = ? WHERE id = " + concessionaria.getID();
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(concessionaria.getDataFabricacao()));
			st.setDate(2, Date.valueOf(concessionaria.getDataValidade()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}*/
	
	public boolean atualizarVeiculo(Concessionaria veiculo) {
		boolean status = false;
		try {  
			String sql = "UPDATE veiculo SET marca = ?, ano = ?, cor = ? WHERE id_veiculo = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setString(1, veiculo.getMarca());
			st.setInt(2, veiculo.getAno());
			st.setString(3, veiculo.getCor());
			st.setInt(4, veiculo.getId());
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	
	/*public boolean delete(int id) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM concessionaria WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}*/
	
	public boolean excluirVeiculo(int id_veiculo) {
		boolean status = false;
		try {  
			String sql = "DELETE FROM veiculo WHERE id_veiculo = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, id_veiculo);
			
			//Capturo a quantidade de linhas afetadas pra evitar o falso delete, retorna 0 ou 1
			int linhasAfetadas = st.executeUpdate(); 
			
			if(linhasAfetadas > 0) {
				status = true;				
			}
			
			st.close();
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
}