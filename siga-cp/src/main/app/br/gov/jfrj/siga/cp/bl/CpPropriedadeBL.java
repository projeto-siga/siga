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
package br.gov.jfrj.siga.cp.bl;

import java.util.List;

import br.gov.jfrj.siga.model.prop.ModeloPropriedade;

public class CpPropriedadeBL extends ModeloPropriedade {
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
	@Override
	public String getPrefixoModulo() {
		return "siga.cp";
	}
	public void setCacheUseSecondLevelCache(Boolean b) throws Exception {
		this.setPropriedade("cache.use_second_level_cache",b.toString());
	}
	public void setCacheUseQueryCache(Boolean b) throws Exception {
		this.setPropriedade("cache.use_query_cache",b.toString());
	}
	
	public Integer getJWTTokenTTL(String modulo) {
		int defaultTTL = 3600;
		String ttl = System.getProperty("idp.jwt.token.ttl." + modulo,String.valueOf(defaultTTL));
		return Integer.valueOf(ttl); 
		 
	}
	public String getJWTModuloPwd(String modulo) throws Exception {
		return System.getProperty("idp.jwt.modulo.pwd." + modulo);
	}
	
	public String getModoAutenticacao(String orgao) throws Exception {
		return this.obterPropriedade("autenticacao." + orgao.toLowerCase() + ".modo");
	}
}