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
package br.gov.jfrj.siga.gi.integracao;

import br.gov.jfrj.ldap.conf.LdapProperties;
import br.gov.jfrj.siga.base.AplicacaoException;

public class IntegracaoLdapProperties extends LdapProperties {
	

	public boolean sincronizarSenhaLdap() {
		try {
			return this.obterPropriedade("integracao").equals("true");
		} catch (Exception e) {
			return false;
		}

	}
	
	public String getEnderecoTrocaSenhaWebServiceLdap() {
		try {
			return this.obterPropriedade("ws.endereco.trocaSenha");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter o endereco do Web Service do LDAP (trocaSenha)", 9, e);
		}
	}

	@Override
	public String getPrefixoModulo() {
		return "siga.integracao";
	}

	public String getEnderecoAutenticacaoWebServiceLdap() {
		try {
			return this.obterPropriedade("ws.endereco.autenticacao");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter o endereco do Web Service do LDAP (autenticacao)", 9, e);
		}
	}

	public String getEnderecoBuscaWebServiceLdap() {
		try {
			return this.obterPropriedade("ws.endereco.busca");
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao obter o endereco do Web Service do LDAP (busca)", 9, e);
		}
	}

}
