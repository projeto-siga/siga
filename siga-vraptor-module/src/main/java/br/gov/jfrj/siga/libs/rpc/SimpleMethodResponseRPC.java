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
 * 		É uma resposta RPC simples no formato chave->valor<br/>
 * <br/> Exemplo de uso: 
 * <br/>    // cria e prepara os parâmetros de retorno <String-nome,String-valor>
 * <br/>	HashMap<String, String> parRet = new HashMap<String, String>();
 * <br/>    parRet.put("param1", "valor1");
 * <br/>	parRet.put("param2", "valor2");
 * <br/>	...	
 * <br/>    // Cria a resposta
 * <br/>	SimpleMethodResponseRPC resposta = new SimpleMethodResponseRPC();
 * <br/>	resposta.setMembersFrom(parRet);
 * <br/>    // atribui a resposta no formato XML ao retorno
 * <br/>	setRespostaXMLStringRPC(resposta.toXMLString());
 * <br/>  
 * <br/>	Em caso de erro, responder com FaultMethodResponseRPC
 * <br/>  
 * @see	MethodResponseRPC,FaultMethodResponseRPC
 * @author aym
 * 
 */
public class SimpleMethodResponseRPC extends MethodResponseRPC {
	private StructRPC value;
	public SimpleMethodResponseRPC() {
		value = new StructRPC();
	}
	public void setMembersFrom(HashMap<String,String> p_hmpMembers) {
		value.setMembersFrom(p_hmpMembers);
	}
	public String toXMLString() {
		return "<?xml version='1.0' encoding='UTF-8'?>" 
			 + "<methodResponse>"
			 + "<params>"
			 + "<param>"
			 + "<value>"
			 + value.toXMLString() 
			 + "</value>"
			 + "</param>"
			 + "</params>"
			 + "</methodResponse>";
	}
	/**
	 * @return the value
	 */
	public StructRPC getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(StructRPC value) {
		this.value = value;
	}
	
}
