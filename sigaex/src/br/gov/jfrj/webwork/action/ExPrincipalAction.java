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
 * Criado em 23/11/2005
 */

package br.gov.jfrj.webwork.action;

import br.gov.jfrj.siga.libs.webwork.SigaActionSupport;

import com.opensymphony.xwork.Action;

public class ExPrincipalAction extends SigaActionSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1630775520737927455L;
	
	@ Override
	public String execute () throws Exception
	{
		return Action.SUCCESS;
	}
}
