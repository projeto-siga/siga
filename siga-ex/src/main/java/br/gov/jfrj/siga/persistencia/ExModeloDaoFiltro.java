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

import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

public class ExModeloDaoFiltro extends DaoFiltroSelecionavel {
	private String nome;
	private String descricao;
	private ExFormaDocumento exFormaDocumento;
	private ExNivelAcesso exNivelAcesso;
	private ExClassificacao exClassificacao;
	private Boolean ativos;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ExFormaDocumento getExFormaDocumento() {
		return exFormaDocumento;
	}

	public void setExFormaDocumento(ExFormaDocumento exFormaDocumento) {
		this.exFormaDocumento = exFormaDocumento;
	}

	public ExNivelAcesso getExNivelAcesso() {
		return exNivelAcesso;
	}

	public void setExNivelAcesso(ExNivelAcesso exNivelAcesso) {
		this.exNivelAcesso = exNivelAcesso;
	}

	public ExClassificacao getExClassificacao() {
		return exClassificacao;
	}

	public void setExClassificacao(ExClassificacao exClassificacao) {
		this.exClassificacao = exClassificacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getAtivos() {
		return ativos;
	}

	public void setAtivo(Boolean ativos) {
		this.ativos = ativos;
	}
	
}