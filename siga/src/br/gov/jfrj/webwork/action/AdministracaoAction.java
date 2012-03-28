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
 * Criado em  23/11/2005
 *
 */
package br.gov.jfrj.webwork.action;

import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.libs.webwork.SigaActionSupport;

import com.opensymphony.xwork.Action;

public class AdministracaoAction extends SigaActionSupport {

	private static final long serialVersionUID = 1630775520737927455L;

	@Override
	public String execute() throws Exception {

		try {
			String ds[] = getRequest().getParameterValues("destinatarios");
			// envia em formato text/plain
			Correio
					.enviar(
							ds[0],
							"(text/plain)Assunto Teste Email Ação!",
							"Teste ação! Atenção: esta é uma mensagem automática. Por favor, não responda. Mensagem Enviada de "
									+ getOrigem());

			// envia em formato text/html
			Correio.enviar(SigaBaseProperties.getString("servidor.smtp.usuario.remetente"), ds,
					"(text/html)Assunto Teste Email Ação!",
					"Teste ação! Atenção: esta é uma mensagem automática. Por favor, não responda."
							+ "Mensagem Enviada de " + getOrigem(),
					"Teste ação! Atenção: esta é uma mensagem automática. Por favor, não responda."
							+ "Mensagem Enviada de " + getOrigem());

		} catch (Exception e) {

		}

		return Action.SUCCESS;
	}

	private String getOrigem() {
		return "RemoteAddr:" + getRequest().getRemoteAddr()  
				+ ",RemoteHost:" + getRequest().getRemoteHost() 
				+ ",RequestedSessionId:" + getRequest().getRequestedSessionId()
				+ ",UserPrincipal:" + getRequest().getUserPrincipal()
				+ ",LocalAddr:" + getRequest().getLocalAddr()
				+ ",getRequestURL:" + getRequest().getRequestURL();
	}
}
