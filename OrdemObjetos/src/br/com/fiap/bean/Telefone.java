package br.com.fiap.bean;

public class Telefone {
	
	private int ddd;
	private String numero;
	private String operadora;
	
	public Telefone(){
		
	}
	
	public Telefone(int ddd, String numero, String operadora) {
		
		setDdd(ddd);
		setNumero(numero);
		setOperadora(operadora);
	}
	
	public int getDdd() {
		return ddd;
	}
	public void setDdd(int ddd) {
		this.ddd = ddd;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getOperadora() {
		return operadora;
	}
	public void setOperadora(String operadora) {
		this.operadora = operadora;
	}
	
	
	
}