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
package br.gov.jfrj.siga;

import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

/*
 * Esta classe precisa ser realocada
 * 
 */
public class AxisClient {

	private Call call;

	public Object call(Object[] par) throws Exception {
		try {
			return call.invoke(par);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public AxisClient(String endpoint, String namespace, String methodName,
			String soapAction) throws Exception {
		Service service = new Service();
		call = (Call) service.createCall();
		call.setTargetEndpointAddress(new URL(endpoint));
		call.setOperationName(new QName(namespace, methodName));
		if (soapAction != null && !soapAction.trim().equals(""))
			call.setProperty(Call.SOAPACTION_USE_PROPERTY, Boolean.TRUE);
		call.setProperty(Call.SOAPACTION_URI_PROPERTY, soapAction);

	}

}
