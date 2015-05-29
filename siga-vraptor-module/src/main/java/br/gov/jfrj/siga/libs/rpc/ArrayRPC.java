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
package br.gov.jfrj.siga.libs.rpc;

import java.util.ArrayList;
/**
 * É a representação de uma estrutura de Array RPC
 * @author aym
 *
 */
public class ArrayRPC extends ValueRPC {
	private ArrayList<ValueRPC> data;
	public ArrayRPC() {
		data = new ArrayList<ValueRPC>();
	}
	public void add(ValueRPC p_vrpValor) {
		data.add(p_vrpValor);
	}
	public String toXMLString() {
		StringBuffer t_stbXML = new StringBuffer();
		t_stbXML.append("<array>");
		t_stbXML.append("<data>");
		for (ValueRPC t_arlVal : data) {
			t_stbXML.append(t_arlVal.toXMLString());
		} 
		t_stbXML.append("</data>");
		t_stbXML.append("</array>");
		return t_stbXML.toString();
	}
	/**
	 * @return the data
	 */
	public ArrayList<ValueRPC> getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(ArrayList<ValueRPC> data) {
		this.data = data;
	}
	
}
