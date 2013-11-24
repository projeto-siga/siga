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
package br.gov.jfrj.siga.persistencia;

import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

public class ExClassificacaoDaoFiltro extends DaoFiltroSelecionavel {
//	private String assuntoPrincipal;

//	private String assuntoSecundario;

	private String atividade;

	private String classe;

	private String descricao;

	private String subclasse;
	
//	private String ultimoNivel;
	
	private String assunto;

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

//	public String getAssuntoPrincipal() {
//		return assuntoPrincipal;
//	}

//	public void setAssuntoPrincipal(final String assuntoPrincipal) {
//		this.assuntoPrincipal = assuntoPrincipal;
//	}

//	public String getAssuntoSecundario() {
//		return assuntoSecundario;
//	}

//	public void setAssuntoSecundario(final String assuntoSecundario) {
//		this.assuntoSecundario = assuntoSecundario;
//	}

	public String getAtividade() {
		return atividade;
	}

	public void setAtividade(final String atividade) {
		this.atividade = atividade;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(final String classe) {
		this.classe = classe;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	public String getSubclasse() {
		return subclasse;
	}

	public void setSubclasse(final String subclasse) {
		this.subclasse = subclasse;
	}

//	public String getUltimoNivel() {
//		return ultimoNivel;
//	}

//	public void setUltimoNivel(String ultimoNivel) {
//		this.ultimoNivel = ultimoNivel;
//	}
}
