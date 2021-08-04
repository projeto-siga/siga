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

import javax.crypto.Cipher;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.ldap.ILdapDao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.gi.integracao.IntegracaoLdapProperties;
import br.gov.jfrj.siga.integracao.ldap.IntegracaoLdap;

@Path("/app/admin/ldap")
@Controller
public class AdminController extends SigaController {
	

	/**
	 * @deprecated CDI eyes only
	 */
	public AdminController() {
		super();
	}

	@Inject
	public AdminController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
		assertAcesso("FE;LDAP_ADMIN:Administrar Integracao LDAP");
	}
	
	@Get("/administrar")
	public void administrarLdap() throws Exception {
	}
	
	@Get("/testar")
	public void testarLDAP(String sesbPessoa) throws Exception {

		IntegracaoLdap integracaoLdap = IntegracaoLdap.getInstancia();
		IntegracaoLdapProperties prop = new IntegracaoLdapProperties(sesbPessoa);
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
	public void trocarSenhaLDAP(String sesbPessoa, String dn, String senha) throws Exception {
		try{
			IntegracaoLdap integracaoLdap = IntegracaoLdap.getInstancia();
			IntegracaoLdapProperties prop = new IntegracaoLdapProperties(sesbPessoa);
			ILdapDao ldap = integracaoLdap.conectarLDAP(prop);
			ldap.definirSenha(dn, senha);
			result.include("status", "ok");
		}catch(Exception e){
			resultErro(e);
		}
	}



}

