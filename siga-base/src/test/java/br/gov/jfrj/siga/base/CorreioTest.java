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
package br.gov.jfrj.siga.base;


public class CorreioTest {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String[] destinatatios = args;
		//envia em formato text/plain
		Correio.enviar(destinatatios[0], "(text/plain)Assunto Teste Email Ação!", "Teste ação! Atenção: esta é uma mensagem automática. Por favor, não responda.");
		
		//envia em formato text/html
		Correio.enviar(Prop.get("/siga.smtp.usuario.remetente"),destinatatios,"(text/html)Assunto Teste Email Ação!", "Teste ação! Atenção: esta é uma mensagem automática. Por favor, não responda.", "Teste ação! Atenção: esta é uma mensagem automática. Por favor, não responda.");
	}
}
