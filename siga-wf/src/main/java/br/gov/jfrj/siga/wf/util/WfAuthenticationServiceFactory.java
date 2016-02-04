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

import org.jbpm.security.authentication.DefaultAuthenticationServiceFactory;
import org.jbpm.svc.Service;

/**
 * Classe que representa uma fábrica de serviços de autenticação.
 * @author kpf
 *
 */
public class WfAuthenticationServiceFactory extends
		DefaultAuthenticationServiceFactory {

	WfAuthenticationService as = null;

	/**
	 * Fecha a fábrica.
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
		super.close();
	}

	/**
	 * Retorna um serviço de autenticação WfAuthenticationService.
	 */
	@Override
	public Service openService() {
		as = new WfAuthenticationService();
		return as;
	}

}
