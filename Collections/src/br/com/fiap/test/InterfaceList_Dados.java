package br.com.fiap.test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InterfaceList_Dados {
	
	
	public static void main(String[] args) {
		
		//Dependendo da vers�o vc coloca o tipo de dado na implementa��o
		//List<String> cargos = new ArrayList<>();
		List<String> cargos = new ArrayList<String>();
		
		//Aceitando dados duplicados
		
		cargos.add("DBA");
		cargos.add("Estagiario");
		cargos.add("Desenvolvedor");
		cargos.add("Desenvolvedor");
		
		System.out.println(cargos);
		Collections.sort(cargos);
		System.out.println(cargos);
	}
	

}
