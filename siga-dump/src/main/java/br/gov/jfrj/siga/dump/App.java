package br.gov.jfrj.siga.dump;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class App {

	private final static List<String> _tabs=new ArrayList<String>();
	private static String _arq = null;
	
	
	private List<String> tabelas;
	private String arquivoSaida;
	private Connection connection;
	
	private List<String> inserts = new ArrayList<String>();
	private List<String> updates = new ArrayList<String>();
	
	public App(List<String> tabelas, String arquivoSaida) {
		this.tabelas = tabelas;
		this.arquivoSaida = arquivoSaida;
	}

	public static void main(String[] args) throws SQLException, IOException {
		processarArgumentos(args);
		App app = new App(_tabs,_arq);
		app.dump();
	}

	/**
	 * Executa o dump
	 * @throws SQLException 
	 * @throws IOException 
	 */
	private void dump() throws SQLException, IOException {
		 DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		 connection = DriverManager.getConnection( "jdbc:oracle:thin:@192.168.56.101:1521:xe", "siga", "siga" );
		 
		 List<String> resultado = null;
		 for (String t:tabelas) {
			 resultado = dump(t);
		}
		
		enviarParaSaida(resultado);
	}

	private void enviarParaSaida(List<String> resultado) throws IOException {
		File file = new File(arquivoSaida);
		FileWriter fw = new FileWriter(file);
		for (String linha : resultado) {
			fw.write(linha + "\n");
		}
		fw.close();
	}

	private List<String> dump(String nomeTabela) throws SQLException {
		inserts.add("\n\n-- INSERTS " + nomeTabela + "\n\n");
		updates.add("\n\n-- UPDATES " + nomeTabela+ "\n\n");
		PreparedStatement stm = connection.prepareStatement("select * from " + nomeTabela + " ORDER BY ID_MOD");
		ResultSet rs = stm.executeQuery();
		
		List<String> colunas = new ArrayList<String>();
		for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
			colunas.add(rs.getMetaData().getColumnName(i));
		}
		
		List<Object> valores = new ArrayList<Object>();
		
		while(rs.next()){
			
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				if (rs.getMetaData().getColumnType(i) == Types.BLOB){
					valores.add(getAspas(blobToString(rs.getBytes(i))));
				}else if (rs.getMetaData().getColumnType(i) == Types.VARCHAR){
					valores.add(getAspas(rs.getString(i)));
				}else {
					valores.add(rs.getObject(i));	
				}
				
			}
			
			String pk = getPrimaryKey(nomeTabela) ;
			inserts.add(getInsertCmd(nomeTabela,colunas,valores));
			updates.add(getUpdateCmd(nomeTabela,pk,colunas,valores));
			
			valores = new ArrayList<Object>();
		}
		
		List<String> result = new ArrayList<String>();
		result.addAll(inserts);
		result.addAll(updates);

		return result;
	}

	private String getAspas(String str) {
		return "'" + str + "'";
	}

	private String getPrimaryKey(String nomeTabela) throws SQLException {
		ResultSet rsPK = connection.getMetaData().getPrimaryKeys(null, null, nomeTabela);
		rsPK.next();
		return rsPK.getString(4);
	}

	private String blobToString(byte[] bytes) {
		if (bytes!=null){
			return new String(bytes);	
		}
		return null;
	}

	private String getInsertCmd(String nomeTabela,List<String> colunas, List<Object> valores) {
		String lstColunas = colunas.toString().replaceAll("\\[|\\]", "");
		String lstValores = valores.toString().replaceAll("\\[|\\]", "");
		return "INSERT INTO " + nomeTabela + " (" + lstColunas + ") VALUES (" + lstValores + ");";
	}

	private String getUpdateCmd(String nomeTabela,String pk, List<String> colunas, List<Object> valores) {
		List<String> lstSet = new ArrayList<String>();
		String valorPk = null;
		for (int i = 0; i < colunas.size(); i++) {
			lstSet.add(colunas.get(i) + "=" + valores.get(i));
			if (colunas.get(i).equals(pk)){
				valorPk = valores.get(i).toString();
			}
		}
		
		String updt =  "UPDATE " + nomeTabela + " SET " + lstSet + " WHERE " + pk + " = " + valorPk +";";
		return updt.replaceAll("\\[|\\]", "");
	}

	/**
	 * Extrai os arqumentos passados na linha de comando
	 * @param pars
	 */
	private static void processarArgumentos(String[] pars) {
		if (pars.length < 2) {
			System.err.println("Número de Parametros inválidos!");
		}

		for (String param : Arrays.asList(pars)) {
			if (param.startsWith("-tabelas=")) {
				String t = param.split("=")[1];
				_tabs.addAll(Arrays.asList(t.split(",")));
			}
			if (param.startsWith("-arquivoSaida=")) {
				String a = param.split("=")[1];
				_arq=a;
			}
			
		}

	}
	
}
