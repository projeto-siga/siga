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
package br.gov.jfrj.siga.wf.util;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.mail.AddressResolver;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.dao.WfDao;

/**
 * Classe utilizada para descobrir o endereço de e-mail baseado na sigla de uma
 * pessoa. Esta classe é definida em siga-wf/src/jbpm.cfg.xml.
 * 
 * @author kpf
 * 
 */
public class WfAddressResolver implements AddressResolver {

	/**
	 * Retorna o e-mail do ator, caso exista.
	 */
	// Retornar uma string, string[] ou list<string>. Ou null se o usuário não
	// existir
	public Object resolveAddress(String actorId) {
		if (actorId == null || actorId.trim().length() == 0)
			return null;

		DpPessoa ator = WfDao.getInstance().getPessoaFromSigla(actorId);
		if (ator != null) {
			return ator.getEmailPessoaAtual();
		}

		DpLotacao lota = WfDao.getInstance().getLotacaoFromSigla(actorId);
		if (lota != null) {
			List<DpPessoa> l = null;
			try {
				l = WfDao.getInstance().pessoasPorLotacao(lota.getId(), false,true);
			} catch (AplicacaoException e) {
				e.printStackTrace();
				return null;
			}
			List<String> emails = new ArrayList<String>();
			for (DpPessoa pessoa : l) {
				emails.add(pessoa.getEmailPessoaAtual());
			}
			return emails;
		}
		return null;
	}
}
