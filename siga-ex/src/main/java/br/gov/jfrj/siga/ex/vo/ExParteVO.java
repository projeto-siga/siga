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
package br.gov.jfrj.siga.ex.vo;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class ExParteVO {
	String sigla;
	String siglaAmpliada;
	String descricao;
	String descricaoAmpliada;
	String iniciais;
	String siglaOrgao;

	public String getSigla() {
		return sigla;
	}

	public String getSiglaAmpliada() {
		return siglaAmpliada;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getDescricaoAmpliada() {
		return descricaoAmpliada;
	}

	public String getIniciais() {
		return iniciais;
	}
	
	public String getSiglaOrgao() {
		return siglaOrgao;
	}

	public String getNomeAbreviado() {
		if (descricao == null)
			return "";
		String a[] = descricao.split(" ");

		String nomeAbreviado = "";
		for (String n : a) {
			if (nomeAbreviado.length() == 0)
				nomeAbreviado = n.substring(0, 1).toUpperCase()
						+ n.substring(1).toLowerCase();
			// else if (!"|DA|DE|DO|DAS|DOS|E|".contains("|" + n + "|"))
			// nomeAbreviado += " " + n.substring(0, 1) + ".";
		}
		return nomeAbreviado;
	}

	public ExParteVO(DpLotacao lota) {
		sigla = lota.getSigla();
		siglaAmpliada = lota.getSiglaAmpliada();
		descricao = lota.getDescricao();
		descricaoAmpliada = lota.getDescricaoAmpliada();
		iniciais = lota.getIniciais();
		siglaOrgao = lota.getOrgaoUsuario().getSigla();
	}

	public ExParteVO(DpPessoa pess) {
		sigla = pess.getSigla();
		siglaAmpliada = pess.getSigla();
		descricao = pess.getDescricao();
		descricaoAmpliada = pess.getDescricao();
		iniciais = pess.getIniciais();
		siglaOrgao = pess.getOrgaoUsuario().getSigla();
	}
}
