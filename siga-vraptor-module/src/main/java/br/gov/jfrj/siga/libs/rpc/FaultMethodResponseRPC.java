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

import java.util.HashMap;
/**
 * 		É uma resposta RPC a ser enviada ao cliente em caso de erro<br/>
 * <br/> Exemplo de uso: 
 *           
 * <br/>    // Cria a resposta
 * <br/>	FaultMethodResponseRPC erro = new FaultMethodResponseRPC();
 * <br/>	erro.set(0, "isto é uma mensagem de erro" )
 * <br/>    // atribui a resposta no formato XML ao retorno
 * <br/>	setRespostaXMLStringRPC(erro.toXMLString());
 * <br/>  
 * @see	MethodResponseRPC, SimpleMethodResponseRPC
 * @author aym
 * 
 */
public class FaultMethodResponseRPC extends MethodResponseRPC {
	private StructRPC value;
	public FaultMethodResponseRPC() {
		value = new StructRPC();
	}
	public void set(Integer p_intFaultCode, String p_strFaultString ) {
		value = new StructRPC();
		HashMap<String,String> t_hmpMapa = new HashMap<String,String> ();
		t_hmpMapa.put("faultCode", String.valueOf(p_intFaultCode));
		t_hmpMapa.put("faultString",p_strFaultString );
		value.setMembersFrom(t_hmpMapa);
	}
	public String toXMLString() {
	return "<?xml version='1.0' encoding='UTF-8'?>" 
			 + "<methodResponse>"
			 + "<fault>"
			 + "<value>"
			 + value.toXMLString() 
			 + "</value>"
			 + "</fault>"
			 + "</methodResponse>";
	}
}
