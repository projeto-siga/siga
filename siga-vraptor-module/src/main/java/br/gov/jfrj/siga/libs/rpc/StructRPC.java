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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/**
 * É a representação de uma Struct RPC
 * @author aym
 *
 */
public class StructRPC extends ValueRPC {
	private ArrayList<StructMemberRPC> members;
	public StructRPC() {
		members = new ArrayList<StructMemberRPC>();
	}
	@SuppressWarnings("unchecked")
	public void setMembersFrom(HashMap<String,String> p_hmpMembers) {
		Set t_setEntradas = p_hmpMembers.entrySet();
    		Iterator t_itrIterador = t_setEntradas.iterator();
	   	 while (t_itrIterador.hasNext()) {
	      	Map.Entry t_entEntrada = (Map.Entry) t_itrIterador.next();
			if (t_entEntrada != null) {
				StructMemberRPC t_stmMembro = new StructMemberRPC();
				t_stmMembro.setName((String) t_entEntrada.getKey() ); 
				StringRPC t_srpValor = new StringRPC();
				t_srpValor.setContent((String) t_entEntrada.getValue());
				t_stmMembro.setValue(t_srpValor); 
				members.add(t_stmMembro);
			}
    	 } 
	}
	public String toXMLString() {
		StringBuffer t_stbXML = new StringBuffer();
		t_stbXML.append("<struct>");
		for (StructMemberRPC t_srmMember : members) {
			t_stbXML.append(t_srmMember.toXMLString());
		} 
		t_stbXML.append("</struct>");
		return t_stbXML.toString();
	}
	@SuppressWarnings("unused")
	private void setMembers(ArrayList<StructMemberRPC> p_arlMembers) {
		members = p_arlMembers;
	}
	@SuppressWarnings("unused")
	private ArrayList<StructMemberRPC> getMembers() {
		return members;
	}

}
