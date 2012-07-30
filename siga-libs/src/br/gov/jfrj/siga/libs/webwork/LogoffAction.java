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
package br.gov.jfrj.siga.libs.webwork;

import java.util.Map;

import com.opensymphony.xwork.Action;

public class LogoffAction extends SigaActionSupport {
	
	private String uri;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 9195445738817081791L;
	@SuppressWarnings("unchecked")
	private Map session;

	@SuppressWarnings("unchecked")
	public Map getSession() {
		return session;
	}

	@SuppressWarnings("unchecked")
	public void setSession(final Map sessao) {
		this.session = sessao;
	}

	@Override
	public String execute() {
		getRequest().getSession().invalidate();
		invalidar();
		if (isClientCertAuth()) {
			return "CLIENT_CERT_AUTH_LOGOFF";
		} else {
			return Action.SUCCESS;
		}
	}

	public String redirect() {
		//System.out.println(getRequest().getParameter("uri"));
		//getRequest().setAttribute("uri", getRequest().getParameter("uri"));
		return Action.SUCCESS;
	}

}
