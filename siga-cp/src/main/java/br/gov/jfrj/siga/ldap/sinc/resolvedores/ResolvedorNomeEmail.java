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
package br.gov.jfrj.siga.ldap.sinc.resolvedores;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.base.util.Texto;

public class ResolvedorNomeEmail {

	private String nome;
	private String matricula;
	private String[] tokens;

	public void setNome(String nome) {
		this.nome = nome;
		tokens = padronizarTokens(ajustaTexto(nome).split(" "));
	}

	private String ajustaTexto(String nome) {
		String textoAjustado = nome.replaceAll("\\\\", "").replaceAll("\\/", "").replaceAll("\\[", "")
				.replaceAll("\\]", "").replaceAll("\\:", "").replaceAll("\\|", "").replaceAll("\\<", "")
				.replaceAll("\\>", "").replaceAll("\\.", "").replaceAll("\\=", "").replaceAll("\\;", "")
				.replaceAll("\\?", "").replaceAll("\\,", "").replaceAll("\\*", "")

				.replaceAll("\\`", "").replaceAll("\\´", "").replaceAll("\\'", "").replaceAll("\\\"", "");
		return Texto.removeAcento(textoAjustado).toLowerCase();
	}

	private String[] padronizarTokens(String[] nomes) {
		List<String> listaNomesValidos = new ArrayList<String>();
		for (String s : nomes) {
			if (s.length() > 3) { // desconsidera possíveis preposições ou abreviações
				listaNomesValidos.add(s);
			}
		}

		String[] token = new String[listaNomesValidos.size()];
		for (int i = 0; i < listaNomesValidos.size(); i++) {
			token[i] = listaNomesValidos.get(i);
		}
		return token;
	}

	public String getNome() {
		return nome;
	}

	public String[] getNomesResolvidos() {
		String[] nomesResolvidos = new String[6];
		nomesResolvidos[0] = getRegraNomeUltimoSobrenome();
		nomesResolvidos[1] = getRegraNomePrimeiroSobrenome(); /* PENÚLTIMO SOBRENOME???? */
		nomesResolvidos[2] = getRegraNome();
		nomesResolvidos[3] = getRegraUltimoSobrenome();
		nomesResolvidos[4] = getRegraPrimeiroSobrenome();
		nomesResolvidos[5] = getRegraMatricula();
		return nomesResolvidos;
	}

	private String getRegraMatricula() {
		return matricula;
	}

	/**
	 * nome.ultimosobrenome@jfrj.gov.br
	 * 
	 * @return
	 */
	private String getRegraNomeUltimoSobrenome() {
		int idx = tokens.length - 1;
		return tokens[0] + "." + tokens[idx >= 0 ? idx : 0];
	}

	/**
	 * nome.primeirosobrenome@jfrj.gov.br
	 * 
	 * @return
	 */
	private String getRegraNomePrimeiroSobrenome() {
		if (tokens.length == 2) {
			return getRegraNomeUltimoSobrenome();
		}

		int idx = tokens.length - 2;
		return tokens[0] + "." + tokens[idx >= 0 ? idx : 0];
	}

	/**
	 * nome@jfrj.gov.br
	 * 
	 * @return
	 */
	private String getRegraNome() {
		return tokens[0];
	}

	/**
	 * ultimosobrenome@jfrj.gov.br
	 * 
	 * @return
	 */
	private String getRegraUltimoSobrenome() {
		int idx = tokens.length - 1;
		return tokens[idx >= 0 ? idx : 0];
	}

	/**
	 * primeirosobrenome@jfrj.gov.br
	 * 
	 * @return
	 */
	private String getRegraPrimeiroSobrenome() {
		if (tokens.length == 2) {
			return getRegraUltimoSobrenome();
		}
		int idx = tokens.length - 2;
		return tokens[idx >= 0 ? idx : 0];
	}

	public void setMatricula(String m) {
		this.matricula = m;
	}

}
