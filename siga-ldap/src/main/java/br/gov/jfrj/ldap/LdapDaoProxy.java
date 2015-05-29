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
package br.gov.jfrj.ldap;

import java.util.logging.Logger;

import javax.naming.directory.Attributes;

import br.gov.jfrj.siga.base.AplicacaoException;

public class LdapDaoProxy extends LdapDaoImpl {

	Logger log =  Logger.getLogger("br.gov.jfrj.log.sinc.ldap.proxy");
	private String MSG_LOG_SOMENTE_LEITURA = "<<<<SOMENTE LEITURA>>>>";

	protected LdapDaoProxy(boolean somenteLeitura) {
		super(somenteLeitura);
	}

	@Override
	public void alterar(String dn, Attributes atributos)
			throws AplicacaoException {

		if (!isSomenteLeitura()) {
			super.alterar(dn, atributos);
		} else {
			log.info(MSG_LOG_SOMENTE_LEITURA + " Alterando: " + dn);
		}

	}

	@Override
	public void alterarAtributo(String dn, String nomeAtributo,
			Object valorAtributo) throws AplicacaoException {

		if (!isSomenteLeitura()) {
			super.alterarAtributo(dn, nomeAtributo, valorAtributo);
		} else {
			log.info(MSG_LOG_SOMENTE_LEITURA + " Alterando \nObjeto: " + dn
					+ "\nAtributo: " + nomeAtributo);
		}

	}

	@Override
	public void ativarUsuario(String dnUsuario) throws AplicacaoException {
		if (!isSomenteLeitura()) {
			super.ativarUsuario(dnUsuario);
		} else {
			log.info(MSG_LOG_SOMENTE_LEITURA + " Ativando usuário: "
					+ dnUsuario);
		}
	}

	@Override
	public void definirSenha(String dnUsuario, String senhaNova)
			throws AplicacaoException {
		if (!isSomenteLeitura()) {
			super.definirSenha(dnUsuario, senhaNova);
		} else {
			log.info(MSG_LOG_SOMENTE_LEITURA + " Definindo senha usuário: "
					+ dnUsuario);
		}
	}

	@Override
	public void desativarUsuario(String dnUsuario) throws AplicacaoException {
		if (!isSomenteLeitura()) {
			super.desativarUsuario(dnUsuario);
		} else {
			log.info(MSG_LOG_SOMENTE_LEITURA + " Desativando usuário: "
					+ dnUsuario);
		}
	}

	@Override
	public void excluir(String dn) throws AplicacaoException {
		if (!isSomenteLeitura()) {
			super.excluir(dn);
		} else {
			log.info(MSG_LOG_SOMENTE_LEITURA + " Excluindo: " + dn);
		}
	}

	@Override
	public void incluir(String dn, Attributes atributos)
			throws AplicacaoException {
		if (!isSomenteLeitura()) {
			super.incluir(dn, atributos);
		} else {
			log.info(MSG_LOG_SOMENTE_LEITURA + " Incluindo: " + dn);
		}

	}

	@Override
	public void inserirValorAtributoMultivalorado(String dn,
			String nomeAtributo, Object valorAtributo)
			throws AplicacaoException {
		if (!isSomenteLeitura()) {
			super.inserirValorAtributoMultivalorado(dn, nomeAtributo,
					valorAtributo);
		} else {
			log.info(MSG_LOG_SOMENTE_LEITURA + " Inserindo atributo \nObjeto: "
					+ dn + "\nAtributo: " + nomeAtributo + "\nValor:" + valorAtributo);
		}
	}

	@Override
	public void mover(String dn, String novoDN) throws AplicacaoException {
		if (!isSomenteLeitura()) {
			super.mover(dn, novoDN);
		} else {
			log.info(MSG_LOG_SOMENTE_LEITURA + " Movendo...\nOrigem:" + dn
					+ "\nDestino:" + novoDN);
		}
	}

	@Override
	public void removerValorAtributoMultivalorado(String dn,
			String nomeAtributo, Object valorAtributo)
			throws AplicacaoException {
		if (!isSomenteLeitura()) {
			super.removerValorAtributoMultivalorado(dn, nomeAtributo,
					valorAtributo);
		} else {
			log.info(MSG_LOG_SOMENTE_LEITURA + " Removendo valor do \nObjeto: "
					+ dn + "\nAtributo: " + nomeAtributo + "\nValor: " + valorAtributo);
		}
	}

	@Override
	public void excluirAtributo(String dn, String nomeAtributo)
			throws AplicacaoException {
		if (!isSomenteLeitura()) {
			super.excluirAtributo(dn, nomeAtributo);
		} else {
			log.info(MSG_LOG_SOMENTE_LEITURA + " Removendo atributo \nObjeto: "
					+ dn + "\nAtributo: " + nomeAtributo);
		}

	}

}
