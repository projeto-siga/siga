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
 * Criado em  13/09/2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.vraptor;

import java.util.Arrays;
import java.util.Map;

import javax.crypto.Cipher;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.ldap.ILdapDao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.gi.integracao.IntegracaoLdapProperties;
import br.gov.jfrj.siga.integracao.ldap.IntegracaoLdap;

@Path("/app/admin/ldap")
@Resource
public class AdminController extends SigaController {
	
	public AdminController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
		assertAcesso("FE;LDAP_ADMIN:Administrar Integracao LDAP");
	}
	
	@Get("/administrar")
	public void administrarLdap() throws Exception {
	}
	
	@Get("/testar")
	public void testarLDAP(String localidade) throws Exception {

		IntegracaoLdap integracaoLdap = IntegracaoLdap.getInstancia();
		IntegracaoLdapProperties prop = integracaoLdap.configurarProperties(localidade);
		try{
			integracaoLdap.conectarLDAP(prop);
			result.include("status", "ok");
		}catch(Exception e){
			resultErro(e);
		}
	}

	private void resultErro(Exception e) {
		result.include("status", "erro");
		result.include("err",e);
		result.include("message",e.getMessage());
		result.include("cause",e.getCause());
		result.include("stacktrace",Arrays.deepToString(e.getStackTrace()));
	}

	@Get("/testarjce")
	public void testarJCE(String localidade) throws Exception {
		try{
			int max = Cipher.getMaxAllowedKeyLength("AES");
			result.include("status", "ok");
			result.include("javaHome", System.getProperty("java.home"));
			result.include("jceStrength", max);
			result.include("jceStrengthLimit", max != Integer.MAX_VALUE?"LIMITED":"UNLIMITED");
		}catch(Exception e){
			resultErro(e);
		}
	}

	@Post("/trocarsenha")
	public void trocarSenhaLDAP(String localidade, String dn, String senha) throws Exception {
		try{
			IntegracaoLdap integracaoLdap = IntegracaoLdap.getInstancia();
			IntegracaoLdapProperties prop = integracaoLdap.configurarProperties(localidade);
			ILdapDao ldap = integracaoLdap.conectarLDAP(prop);
			ldap.definirSenha(dn, senha);
			result.include("status", "ok");
		}catch(Exception e){
			resultErro(e);
		}
	}

	@Get("/propriedades")
	public void listarPropriedadesLDAP(String localidade) throws Exception {
		IntegracaoLdap integracaoLdap = IntegracaoLdap.getInstancia();
		IntegracaoLdapProperties prop = integracaoLdap.configurarProperties(localidade);
		
		
		Map<String,String> ambiente = prop.obterDefinicaoPropriedade("ambiente");
		Map<String,String> dnUsuarios = prop.obterDefinicaoPropriedade("dnUsuarios");
		Map<String,String> keystore = prop.obterDefinicaoPropriedade("keystore");
		Map<String,String> sslPorta = prop.obterDefinicaoPropriedade("ssl.porta");
		Map<String,String> senha = prop.obterDefinicaoPropriedade("senha");
		Map<String,String> servidor = prop.obterDefinicaoPropriedade("servidor");
		Map<String,String> usuario = prop.obterDefinicaoPropriedade("usuario");
		
		
		try{
			
			result.include("prefixo", prop.getPrefixo()!=null?prop.getPrefixo():"não definido");			
			result.include("localidade", localidade!=null?localidade:"não definida");
			result.include("ambiente", ambiente!=null?ambiente:"não definida");
			
			result.include("dnUsuarios", dnUsuarios!=null?dnUsuarios:"não definida");
			result.include("keystore", keystore!=null?keystore:"não definida");
			result.include("sslPorta", sslPorta!=null?sslPorta:"não definida");
			result.include("senha", senha!=null?"definida":"não definida");
			result.include("servidor", servidor!=null?servidor:"não definida");
			result.include("usuario", usuario!=null?"definido":"não definido");
		}catch(Exception e){
			resultErro(e);
		}
	}


}

