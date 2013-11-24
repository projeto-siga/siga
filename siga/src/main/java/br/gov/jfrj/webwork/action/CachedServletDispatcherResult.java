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
package br.gov.jfrj.webwork.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.webwork.dispatcher.ServletDispatcherResult;
import com.opensymphony.webwork.interceptor.ServletRequestAware;
import com.opensymphony.webwork.interceptor.ServletResponseAware;
import com.opensymphony.xwork.ActionInvocation;


public class CachedServletDispatcherResult extends ServletDispatcherResult implements ServletResponseAware {
	
	HttpServletResponse resp = null;
	
	@Override
	public void doExecute(String arg0, ActionInvocation arg1) throws Exception {
		resp.addHeader("Cache-Control", "max-age=3600");
		super.doExecute(arg0, arg1);
	}

	public void setServletResponse(HttpServletResponse arg0) {
		this.resp = arg0;
	}
}
