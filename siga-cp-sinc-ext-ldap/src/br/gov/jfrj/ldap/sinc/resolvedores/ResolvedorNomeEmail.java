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
package br.gov.jfrj.ldap.sinc.resolvedores;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.base.Texto;


public class ResolvedorNomeEmail {

	private String nome;
	private String[] tokens;
	
	public void setNome(String nome) {
		this.nome= nome;
		tokens = padronizarTokens(Texto.removeAcento(nome).toLowerCase().split(" "));
	}
	
	private String[] padronizarTokens(String[] nomes) {
		List<String> listaNomesValidos = new ArrayList<String>();
		for(String s: nomes){
			if (s.length() >2){	//desconsidera possíveis preposições ou abreviações
				listaNomesValidos.add(s);
			}
		}
		
		
		String[] token = new String[listaNomesValidos.size()];
		for (int i =0; i < listaNomesValidos.size();i++) {
			token[i] = listaNomesValidos.get(i);
		}
		return token;
	}

	public String getNome(){
		return nome;
	}

	public String[] getNomesResolvidos() {
		String[] nomesResolvidos = new String[5];
		nomesResolvidos[0] = getRegraNomeUltimoSobrenome();
		nomesResolvidos[1] = getRegraNomePrimeiroSobrenome(); /*PENÚLTIMO SOBRENOME????*/
		nomesResolvidos[2] = getRegraNome();
		nomesResolvidos[3] = getRegraUltimoSobrenome();
		nomesResolvidos[4] = getRegraPrimeiroSobrenome();
		return nomesResolvidos;
	}
	
	/**
	 * nome.ultimosobrenome@jfrj.gov.br 
	 * @return
	 */
	private String getRegraNomeUltimoSobrenome() {
		return tokens[0] + "." + tokens[tokens.length - 1];
	}

	/**
	 * nome.primeirosobrenome@jfrj.gov.br 
	 * @return
	 */
	private String getRegraNomePrimeiroSobrenome() {
		if (tokens.length == 2){
			return getRegraNomeUltimoSobrenome();
		}
		return tokens[0] + "." + tokens[tokens.length - 2];
	}
	
	/**
	 * nome@jfrj.gov.br 
	 * @return
	 */
	private String getRegraNome() {
		return tokens[0];
	}
	
	/**
	 * ultimosobrenome@jfrj.gov.br 
	 * @return
	 */
	private String getRegraUltimoSobrenome() {
		return tokens[tokens.length - 1];
	}

	/**
	 * primeirosobrenome@jfrj.gov.br
	 * @return
	 */
	private String getRegraPrimeiroSobrenome() {
		if (tokens.length == 2){
			return getRegraUltimoSobrenome();
		}
		return tokens[tokens.length - 2];
	}






	
	

}
