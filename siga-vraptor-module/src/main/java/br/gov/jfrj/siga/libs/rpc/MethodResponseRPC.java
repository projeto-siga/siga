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
 * 			Formata uma estrutura em formato XML objetivando uma resposta RPC - (vide subclasses)
 * <br/>   
 * <br/>    a forma de preenchimento da resposta depende da subclasse.
 * <br/>    pode ser uma resposta simples, em estrutura de tabela ou erro (fault) 
 * <br/>  	o método toXMLString() retorna uma forma simplificada de XML-RPC.
 * <br/>    uso em cliente javascript: vide siga-libs/lib/js/Ajax.js
 * <br/>    uso no webwork:
 * <br/>	   A action deve preencher a variável respostaXMLStringRPC
 * <br/>       página jsp de resposta padrão: /sigalibs/rpc_retorno.jsp
 * <br/>       <%@page contentType="text/xml; charset=UTF-8" %>${respostaXMLStringRPC}
 * <br/>	  
 * 
 * @see	Subclasses, xwork.xml, rpc_retorno.jsp...
 * @author aym
 *
 */
public abstract class MethodResponseRPC {
	public abstract String toXMLString();
}
