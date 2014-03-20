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
	
	private List<String> inicioScript = new ArrayList<String>();
	private List<String> inserts = new ArrayList<String>();
	private List<String> updates = new ArrayList<String>();
	private List<String> fimScript = new ArrayList<String>();
	
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
		 
		 inicioScript.add(getInicioScript());
		 List<String> resultadoDump = null;
		 for (String t:tabelas) {
			 resultadoDump = dump(t);
		}
		 fimScript.add(getFimScript());
		
		 
		enviarParaSaida(resultadoDump);
	}

	private void enviarParaSaida(List<String> resultadoDump) throws IOException {
		File file = new File(arquivoSaida);
		FileWriter fw = new FileWriter(file);
		
		for (String linha : inicioScript) {
			fw.write(linha + "\n");
		}
		for (String linha : resultadoDump) {
			fw.write(linha + "\n");
		}
		for (String linha : fimScript) {
			fw.write(linha + "\n");
		}
		
		fw.close();
	}

	private List<String> dump(String nomeTabela) throws SQLException {
		inserts.add("\n\n-- INSERTS " + nomeTabela + "\n\n");
		updates.add("\n\n-- UPDATES " + nomeTabela+ "\n\n");
		
		String pk = getPrimaryKey(nomeTabela) ;
		
		PreparedStatement stm = connection.prepareStatement("select * from " + nomeTabela + " ORDER BY " + pk);
		ResultSet rs = stm.executeQuery();
		
		List<String> colunas = new ArrayList<String>();
		for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
			colunas.add(rs.getMetaData().getColumnName(i));
		}
		
		List<Object> valores = new ArrayList<Object>();
		
		while(rs.next()){
			
			String pkValue=null;
			
			boolean contemBlob = false;
			String campoBlob = null;
			String blobValue =null;
			
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				if(rs.getMetaData().getColumnName(i).equals(pk)){
					pkValue = rs.getObject(i).toString();
				}
				
				if (rs.getMetaData().getColumnType(i) == Types.BLOB){
					contemBlob=true;
					campoBlob=rs.getMetaData().getColumnName(i);
					valores.add(null);
					blobValue = rs.getBytes(i)==null?null:blobToString(rs.getBytes(i));
				}else if(rs.getMetaData().getColumnType(i) == Types.TIMESTAMP){
					valores.add(rs.getString(i)==null?null:toDate(rs.getString(i)));
					
				}else if (rs.getMetaData().getColumnType(i) == Types.VARCHAR){
					valores.add(rs.getString(i)==null?null:getAspas(rs.getString(i)));
					
				}else {
					valores.add(rs.getObject(i));	
				}
				
			}
			
			inserts.add(getInsertCmd(nomeTabela,colunas,valores));
			if(contemBlob  && blobValue !=null){
				inserts.add(getUpdateBlobCmd(nomeTabela,campoBlob,blobValue,pk,pkValue));
			}
			updates.add(getUpdateCmd(nomeTabela,pk,colunas,valores));
			
			valores = new ArrayList<Object>();
		}
		
		List<String> result = new ArrayList<String>();
		result.addAll(inserts);
		result.addAll(updates);

		return result;
	}

	private String getUpdateBlobCmd(String nomeTabela, String campoBlob, String blobValue, String pk, String pkValue) {
		StringBuffer sb = new StringBuffer();
		getInicioScript();
		sb.append("\tupdate "	+ nomeTabela + " set " + campoBlob + " = utl_raw.cast_to_raw(' ') where " + pk + " = " + pkValue + ";\n");
		sb.append("\tselect "	+ campoBlob	+ " into dest_blob from " + nomeTabela + " where " + pk + " = " + pkValue + " for update;\n");
		sb.append("\tsrc_blob := utl_raw.cast_to_raw(convert('");
		sb.append(blobValue);
		sb.append("','AL32UTF8'));\n");
		sb.append("\tdbms_lob.append(dest_blob, src_blob);\n");
		getFimScript();
		return sb.toString();
	}

	private String getFimScript() {
		StringBuffer sb = new StringBuffer();
		sb.append("END;\n");
		sb.append("/\n");
		return sb.toString();
	}

	private String getInicioScript() {
		StringBuffer sb = new StringBuffer();
		sb.append("DECLARE\n");
		sb.append("\tdest_blob BLOB;\n");
		sb.append("\tsrc_blob BLOB;\n");
		sb.append("BEGIN\n");
		return sb.toString();
	}

	private String toDate(String str) {
		return "to_date('" + str + "','yyyy-mm-dd HH24:mi:ss')";
	}

	private String getAspas(String str) {
		return "'" + str.replaceAll("'", " || chr(39) || ") + "'";
	}

	private String getPrimaryKey(String nomeTabela) throws SQLException {
		ResultSet rsPK = connection.getMetaData().getPrimaryKeys(null, null, nomeTabela);
		rsPK.next();
		return rsPK.getString(4);
	}

	private String blobToString(byte[] bytes) {
		return new String(bytes).replaceAll("'", " || chr(39) || ");

	}

	private String getInsertCmd(String nomeTabela,List<String> colunas, List<Object> valores) {
		String lstColunas = colunas.toString().replaceAll("^\\[|\\]$", "");
		String lstValores = valores.toString().replaceAll("^\\[|\\]$", "");
		return "INSERT INTO " + nomeTabela + " (" + lstColunas + ") VALUES (" + lstValores + ");";
	}

	private String getUpdateCmd(String nomeTabela,String pk, List<String> colunas, List<Object> valores) {
		List<String> lstSet = new ArrayList<String>();
		String valorPk = null;
		for (int i = 0; i < colunas.size(); i++) {
			lstSet.add(colunas.get(i) + "=" + String.valueOf(valores.get(i)));
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
