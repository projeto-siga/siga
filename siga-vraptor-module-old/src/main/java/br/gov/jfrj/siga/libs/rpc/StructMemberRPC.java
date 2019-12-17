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
/**
 * É a representação de um valor de um membro de Struct RPC
 * @author aym
 *
 */
public class StructMemberRPC {
	private String name;
	private ValueRPC value;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the value
	 */
	public ValueRPC getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(ValueRPC value) {
		this.value = value;
	}
	public String toXMLString() {
		return "<member>" 
			+ "<name>" + name + "</name>" 
			+ "<value>" + value.toXMLString() + "</value>" 
			+  "</member>" ;
	}
}
