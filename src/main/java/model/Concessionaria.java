package model;

public class Concessionaria {
	private int id_veiculo;
	private String marca;
	private int ano;
	private String cor;
	
	public Concessionaria(){
		this.id_veiculo = -1;
		this.marca = "";
		this.ano = 0;
		this.cor = "";
	}
	
	public Concessionaria(int id_veiculo, String marca, int ano, String cor) {
		this.id_veiculo = id_veiculo;
		this.marca = marca;
		this.ano = ano;
		this.cor = cor;
	}
	
	public Concessionaria(String marca, int ano, String cor) {
		this.id_veiculo = 0;
		this.marca = marca;
		this.ano = ano;
		this.cor = cor;
	}
	
	public int getId() { return id_veiculo; }
	
	public void setId(int novoId) { this.id_veiculo = novoId; }
	
	public String getMarca() { return marca; }
	
	public void setMarca( String novaMarca) { this.marca = novaMarca; }
	
	public int getAno() { return ano; }
	
	public void setAno(int novoAno) { this.ano = novoAno; }
	
	public String getCor() { return cor; }
	
	public void setCor(String novaCor) { this.cor = novaCor; }
	
	@Override
	public String toString() {
		return "-- Dados do veiculo[ID: "+ id_veiculo + ", Marca: "+ marca + ", Ano: "+ ano + ", Cor: " + cor +"] --";
	}
}
