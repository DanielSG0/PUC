package br.pucminas.ti2;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DAO {
	private Connection conexao;
	
	public DAO() {
		conexao = null;
	}
	
	public boolean conectar() {
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream("config.properties")){
			props.load(fis);
		} catch (IOException e) {
			System.err.println("Erro: Não foi possível ler o arquivo de configuração 'config.properties");
			e.printStackTrace();
			return false;
		}
		
		String driverName = "org.postgresql.Driver";    
		String url = props.getProperty("db.url");
		String username = props.getProperty("db.user");
		String password = props.getProperty("db.password");
		boolean status = false;
		
		try {
			Class.forName(driverName);
			conexao = DriverManager.getConnection(url, username, password);
			status = (conexao != null);
			System.out.println("Conexão efetuada com postgres!");
		} catch (ClassNotFoundException e) {
			System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
		}
		
		return status;
	}
	
	public boolean close() {
		boolean status = false;
		
		try {
			conexao.close();
			status = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return status;
	}
	
	public boolean inserirVeiculo(Concessionaria veiculo) {
		boolean status = false;
		try {
			Statement st = conexao.createStatement();
			//Criei uma variavel que armazena a solicitação pra ficar mais legivel
			String sql = "INSERT INTO veiculo (marca, ano, cor) VALUES ('"
					+ veiculo.getMarca() + "', " 
					+ veiculo.getAno() + ", '"
					+ veiculo.getCor() + "');";
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		
		return status;
	}
	
	public boolean atualizarVeiculo(Concessionaria veiculo) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			String sql = "UPDATE veiculo SET marca = '" + veiculo.getMarca()
						+ "', ano = " + veiculo.getAno() 
						+ ", cor = '" + veiculo.getCor() + "'"
						+ "WHERE id_veiculo = " + veiculo.getId(); 
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	public boolean excluirVeiculo(int id_veiculo) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			String sql = "DELETE FROM veiculo WHERE id_veiculo = " + id_veiculo;
			
			//Capturo a quantidade de linhas afetadas pra evitar o falso delete, retorna 0 ou 1
			int linhasAfetadas = st.executeUpdate(sql); 
			
			if(linhasAfetadas > 0) {
				status = true;				
			}
			
			st.close();
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	public Concessionaria[] getVeiculos() {
		Concessionaria[] veiculos = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM veiculo");		
	         if(rs.next()){
	             rs.last();
	             veiculos = new Concessionaria[rs.getRow()];
	             rs.beforeFirst();

	             for(int i = 0; rs.next(); i++) {
	                veiculos[i] = new Concessionaria(rs.getInt("id_veiculo"), rs.getString("marca"), 
	                		                  rs.getInt("ano"), rs.getString("cor"));
	             }
	          }
	          st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return veiculos;
	}
	
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
}
