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
package br.gov.jfrj.siga.util;

import java.util.List;


public class CpSincPropriedade extends ModeloPropriedade {
	/*
	 *  Propriedades de Localidade
	 */
	@SuppressWarnings("unchecked")
	public List obterMunicipios() throws  Exception {
		return this.obterPropriedadeLista("siga.cp.municipio");
	}
	/*
	 *  Propriedades de conexão
	 */
	public String urlConexao() throws Exception {
		return this.obterPropriedade("hibernate.connection.url");
	}
	public String driverConexao( ) throws Exception {
		return this.obterPropriedade("hibernate.connection.driver_class");
	}
	public String usuario ( ) throws Exception {
		return this.obterPropriedade("hibernate.connection.username");
	}
	public String senha ( ) throws Exception {
		return this.obterPropriedade("hibernate.connection.password");
	}
	public String c3poMinSize  ( ) throws Exception {
		return this.obterPropriedade("c3p0.min_size");
	}
	public String c3poMaxSize  ( ) throws Exception {
		return this.obterPropriedade("c3p0.max_size");
	}
	public String c3poTimeout  ( ) throws Exception {
		return this.obterPropriedade("c3p0.timeout");
	}
	public String c3poMaxStatements  ( ) throws Exception {
		return this.obterPropriedade("c3p0.max_statements");
	}
	public String cacheUseSecondLevelCache  ( ) throws Exception {
		String s = this.obterPropriedade("cache.use_second_level_cache");
		return s == null ? "true" : s;
	}
	public String cacheUseQueryCache  ( ) throws Exception {
		String s = this.obterPropriedade("cache.use_query_cache");
		return s == null ? "true" : s;
	}
	public String gsaUrl  ( ) throws Exception {
		return this.obterPropriedade("gsa.url");
	}
	public String xjusUrl  ( ) throws Exception {
		return this.obterPropriedade("xjus.url");
	}
	public String xjusJwtSecret  ( ) throws Exception {
		return this.obterPropriedade("xjus.jwt.secret");
	}
	public String xjusPermalinkUrl  ( ) throws Exception {
		return this.obterPropriedade("xjus.permalink.url");
	}
	public String xjusPassword  ( ) throws Exception {
		return this.obterPropriedade("xjus.password");
	}
	public String timestampUrl  ( ) throws Exception {
		return this.obterPropriedade("timestamp.url");
	}
	public String timestampSystem  ( ) throws Exception {
		return this.obterPropriedade("timestamp.system");
	}

	public String emailRemetente  ( ) throws Exception {
		return this.obterPropriedade("siga.cp.sinc.xml.servidor.usuario.remetente");
	}
	
	public String listaDestinatarios  ( ) throws Exception {
		return this.obterPropriedade("siga.cp.sinc.xml.lista.destinatario");
	}
	
	public String servidorSmtp  ( ) throws Exception {
		return this.obterPropriedade("servidor.smtp");
	}
	
	public String servidorSmtpPorta  ( ) throws Exception {
		return this.obterPropriedade("servidor.smtp.porta");
	}
	
	public String servidorSmtpAuth  ( ) throws Exception {
		return this.obterPropriedade("servidor.smtp.auth");
	}
	
	public String servidorSmtpAuthUsuario  ( ) throws Exception {
		return this.obterPropriedade("servidor.smtp.auth.usuario");
	}
	
	public String servidorSmtpAuthSenha  ( ) throws Exception {
		return this.obterPropriedade("servidor.smtp.auth.senha");
	}
	
	public String servidorSmtpStarttlsEnable  ( ) throws Exception {
		return this.obterPropriedade("servidor.smtp.starttls.enable");
	}
	
	public String getPrefixoModulo() {
		return "siga.cp";
	}
	
	public void setCacheUseSecondLevelCache(Boolean b) throws Exception {
		this.setPropriedade("cache.use_second_level_cache",b.toString());
	}
	public void setCacheUseQueryCache(Boolean b) throws Exception {
		this.setPropriedade("cache.use_query_cache",b.toString());
	}
	public String servidorSmtpDebug() {
		// TODO Auto-generated method stub
		try {
			return this.obterPropriedade("servidor.smtp.debug");
		} catch (Exception e) {
			return "false";
		}
	}

}