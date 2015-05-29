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

import br.gov.jfrj.siga.libs.rpc.MethodResponseRPC;
/**
 * 		É uma resposta RPC no formato de tabela
 * <br/> 
 * <br/> Exemplo de uso: 
 * <br/>    // Cria a resposta
 * <br/>	TableMethodResponseRPC tabela = new TableMethodResponseRPC();
 * <br/>    // linha 1
 * <br/>	HashMap<String, String> linha1 = new HashMap<String, String>();
 * <br/>    linha1.put("coluna1", "valor1-1");
 * <br/>	linha1.put("coluna2", "valor1-2");
 * <br/>	...	
 * <br/>	tabela.addLine(linha1) ; 
 * <br/>	// linha 2	
 * <br/>	HashMap<String, String> linha2 = new HashMap<String, String>();
 * <br/>    linha2.put("coluna1", "valor2-1");
 * <br/>	linha2.put("coluna2", "valor2-2");
 * <br/>	tabela.addLine(linha2) ; 
 * <br/>	...	
 * <br/>	resposta.setMembersFrom(parRet);
 * <br/>    // atribui a resposta no formato XML ao retorno
 * <br/>	setRespostaXMLStringRPC(tabela.toXMLString());  
 * <br/>
 * <br/>	Em caso de erro, responder ao cliente com FaultMethodResponseRPC
 * <br/> 
 * @see	MethodResponseRPC,FaultMethodResponseRPC
 * @author aym
 * 
 */
public class TableMethodResponseRPC extends MethodResponseRPC {
	private ArrayRPC data;
	public TableMethodResponseRPC() {
		data = new ArrayRPC();
	}
	public void addLine( StructRPC line) {
		data.add(line);
	}
	public void addLine (HashMap<String,String> line) {
		StructRPC t_srpConteudo = new StructRPC();
		t_srpConteudo.setMembersFrom(line);
		data.add(t_srpConteudo);
	}
	public String toXMLString() {
		return "<?xml version='1.0' encoding='UTF-8'?>" 
			 + "<methodResponse>"
			 + "<params>"
			 + "<param>"
			 + "<value>"
			 + data.toXMLString() 
			 + "</value>"
			 + "</param>"
			 + "</params>"
			 + "</methodResponse>";
	}
}
