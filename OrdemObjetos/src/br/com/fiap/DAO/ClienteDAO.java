package br.com.fiap.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.bean.Cliente;
import br.com.fiap.bean.Telefone;
import br.com.fiap.conexao.ConexaoFactory;

public class ClienteDAO {
	
	private Connection con;
	
	//Construtor Gera a conex�o , pois todos tem uma dependencia
	
	public ClienteDAO()throws Exception{
		
		con = new ConexaoFactory().conectar();
	}
	
	public String fecharConexao()throws Exception{
		con.close();
		return "Conexao Off";
	}

	
	//Fazer a verificacao antes de mandar 
	//gravar no banco , caso tenha chave primaria contendo numeros iguais
	
	
	
	/**
	 * <i> 
	 * Este metodo esta relacionado com Bean Cliente , 
	 * com o ClienteBO , no Banco Tabela cliente.
	 * Este metodo est� inserindo valores na tabela cliente
	 * </i>
	 * 
	 * @author Lucas Rodrigues do Nascimento
	 * @param Cliente
	 * @see ClienteBO;
	 * @see Cliente;
	 * @return String
	 * @version 1.0
	 * 
	 */
	
	
	public String Inserir(Cliente c)throws Exception{
		
		PreparedStatement estrutura = null;
		estrutura = con.prepareStatement("INSERT INTO TB_DDD_CLIENTE (NR_CLIENTE , NM_CLIENTE , QT_ESTRELA) VALUES (?,?,?)");
		
		estrutura.setInt(1, c.getNumero());
		estrutura.setString(2, c.getNome());
		estrutura.setInt(3, c.getQtdEstrelas());
		
		//SO utiliza quando estamos usando Select 
		estrutura.execute();
		
		//Percorrer a Lista para Adicionar 
		for(Telefone t : c.getFone()){
			new TelefoneDAO().inserir(t, c.getNumero());
		}
		
		estrutura.close();
		return "Inserido com Sucesso";
		
	}
	
	//----------------------------------------------------------------------------------------------
	
	public Cliente retornandoUM_CLIENTE(int numero)throws Exception{
		
		PreparedStatement estrutura = null;
		Cliente c = new Cliente();
		estrutura = con.prepareStatement("SELECT * FROM TB_DDD_CLIENTE WHERE NR_CLIENTE=?");
		
		estrutura.setInt(1, numero);
		ResultSet resultado = estrutura.executeQuery();
		if(resultado.next()){
			c.setNome(resultado.getString("NM_CLIENTE"));
			c.setNumero(resultado.getInt("NR_CLIENTE"));
			c.setQtdEstrelas(resultado.getInt("QT_ESTRELA"));
			
			//Instanciando obj Telefone , Pq necessita de uma lista de telefone
		
			TelefoneDAO Tdao = new TelefoneDAO();
			c.setFone(Tdao.consultarPorNumero(numero));
		}
		
		resultado.close();
		estrutura.close();
		
		return c;
		
	}
	
	//ALTERANDO ESTRELA PARA AUMENTAR O NIVEL DE ESTRELA
	
	public String AUMENTANDO_ESTRELA(int numero)throws Exception{
		
		PreparedStatement estrutura = con.prepareStatement("UPDATE TB_DDD_CLIENTE set QT_ESTRELA = QT_ESTRELA+1 WHERE NR_CLIENTE = ?");
		estrutura.setInt(1, numero);
		
		//Mostrando o numero de linhas
		int x = estrutura.executeUpdate();
		estrutura.close();
		return x + "linha(s) foi(ram) alterada(s)!";
	}
	
	//Listando por NIVEL COM ARRAYLIST
	//CONSULTANDO
	
	public List<Cliente> ListarPorNivel(int estrela)throws Exception{
		List<Cliente> lista = new ArrayList<>();
		
		Cliente objCliente = new Cliente();
		
		PreparedStatement estrutura = null;
		estrutura = con.prepareStatement("SELECT * FROM TB_DDD_CLIENTE WHERE QT_ESTRELA=?");
		
		estrutura.setInt(1, estrela);
		ResultSet resultado = estrutura.executeQuery();
		while(resultado.next()){
			
			//Zerando o Objeto para Carregar outro endere�o de memoria
			objCliente = new Cliente();
			
			objCliente.setNome(resultado.getString("NM_CLIENTE"));
			objCliente.setNumero(resultado.getInt("NR_CLIENTE"));
			objCliente.setQtdEstrelas(resultado.getInt("QT_ESTRELA"));
			
			//Recuperando uma lista de telefone
			TelefoneDAO tDAO = new TelefoneDAO();
			//List<Telefone> listaPhone = tDAO.consultarPorNumero(resultado.getInt("NR_CLIENTE"));
			
			tDAO.fechar();
			//objCliente.setFone(listaPhone);
			
			//FIM
			lista.add(objCliente);
		}
		
		resultado.close();
		estrutura.close();
		
		
		return lista;
	}
	
	
	//Por nome
	
	//Adicionar a lista de telefone para remover os telefones e depois excluir o cliente
	
	public int DELETANDO_NAME(String pNome)throws Exception{
		
		//Entrando no DAO do telefone para excluir os nome dos clientes
		TelefoneDAO tDAO = new TelefoneDAO();
		
		//Realizando uma pesquisa
		PreparedStatement stmt = con.prepareStatement
				("SELECT * FROM TB_DDD_CLIENTE WHERE NM_CLIENTE LIKE ?");
		stmt.setString(1, pNome + "%");
									//Colocar o % para concatenar
		
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			tDAO.excluirNumero(rs.getInt("NR_CLIENTE"));
		}
		tDAO.fechar();
		
		//Agora pode excluir o cliente
		// 1� Primeiro pesquisar os clientes com nome e depois excluir
		
		PreparedStatement estrutura = con.prepareStatement("DELETE FROM TB_DDD_CLIENTE WHERE NM_CLIENTE LIKE ?");
		estrutura.setString(1, "%"+ pNome+"%");
		int x = estrutura.executeUpdate();
		estrutura.close();
		return x;
	}
	
	
	//Por numero
	public String deletandoUm_Cliente(Cliente c)throws Exception{
		
		PreparedStatement estrutura = null;
		
		estrutura = con.prepareStatement("DELETE FROM TB_DDD_CLIENTE WHERE NR_CLIENTE = ?");
		estrutura.setInt(1, c.getNumero());
		estrutura.execute();
		estrutura.close();
		
		return "CLIENTE DELETADO";
	}
	
}
