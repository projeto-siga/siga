/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * Criado em  16/06/2011
 *
 *  To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.ex;
/*
import java.util.MissingResourceException;
import java.util.ResourceBundle;
*/
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.gov.jfrj.siga.model.prop.ext.ModeloPropriedade;

public class SigaExProperties extends ModeloPropriedade {
	/*
	 * 
	 *  Antes a classe se chamava Mensagens (foi renomeada - refactor)
	 *  
	 */
	//private static final String BUNDLE_NAME = "resource.application";
	/*private static final String BUNDLE_NAME = "globalMessages";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(Mensagens.BUNDLE_NAME);

	public static String getString(final String key) {

		try {
			return Mensagens.RESOURCE_BUNDLE.getString(key);
		} catch (final MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	*/

	protected SigaExProperties() {
		// construtor privado
	}
	
	private static SigaExProperties instance = new SigaExProperties();
	
	public static String getString(final String key) {
		try {
			return instance.obterPropriedade(key);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String getAmbiente() {
		return System.getProperty("ambiente");
	}
	
	public static String getStringComAmbiente(String key){
		String ambiente = System.getProperty("ambiente");
		if (ambiente != null && ambiente.length() > 0)
			return getString(ambiente+"."+key).trim();
		return "";
	}
	
	public static String getServidorDJE(){
		return getStringComAmbiente("dje.servidor");
	}
	
	public static String getConversorHTMLFactory(){
		return getString("conversor.html.factory");
	}

	public static String getMontadorQuery() {
		return getString("montador.query");
	}
	
	public static boolean isAmbienteProducao(){
		if (getAmbiente() != null && getAmbiente().equals("prod"))
			return true;
		
		return false;
	}

	@Override
	public String getPrefixoModulo() {
		return "siga.ex";
	}

	public static String getExtensaoConversorHTML() {
		return getString("conversor.html.ext");
	}
	
	public static String getAssinaturaDecodificador(){
		return getString("siga.ex.assinatura.decodificador");
	}

	public static Object getAssinaturaCodebasePath() {
		return getString("siga.ex.assinatura.codeBasePath");
	}

	public static Object getAssinaturaMessagesURLPath() {
		return getString("siga.ex.assinatura.messagesUrlPath");
	}

	public static Object getAssinaturaPorlicyUrlPath() {
		return getString("siga.ex.assinatura.policyUrlPath");
	}

	public static String getEnderecoAutenticidadeDocs() {
		return getString("siga.ex.enderecoAutenticidadeDocs");
	}
	
	public static Long getIdModInternoImportado() throws Exception{
		String s = getStringComAmbiente("modelos.interno_importado");
		if (s == null)
			throw new Exception("Propriedade para o identificador do modelo Interno Importado não encontrada");
		try{
			return Long.valueOf(s);
		} catch (NumberFormatException nfe){
			throw new Exception("Erro ao obter propriedade para o identificador do modelo Interno Importado");
		}
	}
	
	public static Long getIdModPA() throws Exception{
		String s = getStringComAmbiente("modelos.processo_administrativo");
		if (s == null)
			throw new Exception("Propriedade para o identificador do modelo Processo Administrativo não encontrada");
		try{
			return Long.valueOf(s);
		} catch (NumberFormatException nfe){
			throw new Exception("Erro ao obter propriedade para o identificador do modelo Processo Administrativo");
		}
	}

	public static Long getAnoInicioAcronimoNoCodigoDoDocumento() throws Exception{
		String s = getString("siga.ex.anoInicioAcronimoNoCodigoDoDocumento");
		if (s == null)
			return 9999L;
		try{
			return Long.valueOf(s);
		} catch (NumberFormatException nfe){
			throw new Exception("Erro ao obter propriedade para o ano inicial para a utilização do acrônimo no código do documento");
		}
	}
	
	public static Date getDataInicioObrigacaoDeAssinarAnexoEDespacho() throws Exception{
		String s = getString("siga.ex.dataInicioObrigacaoDeAssinarAnexoEDespacho");
		
		DateFormat formatter = new SimpleDateFormat("MM/dd/yy");  
				
		if (s == null)
			return (Date)formatter.parse("31/12/2099");
		try{
			return (Date)formatter.parse(s);
		} catch (NumberFormatException nfe){
			throw new Exception("Erro ao obter propriedade para o data inicial de obrigação de assinatura de anexos e despachos em documentos eletrônicos");
		}
	}
	

}
