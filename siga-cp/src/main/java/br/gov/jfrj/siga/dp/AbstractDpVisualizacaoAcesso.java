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
/*
 * Criado em  21/12/2005
 *
 */
package br.gov.jfrj.siga.dp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.jfrj.siga.model.Objeto;

@MappedSuperclass
public abstract class AbstractDpVisualizacaoAcesso extends Objeto implements
		Serializable {

	@Id
	@SequenceGenerator(name = "DP_VISUALIZACAO_ACESSO_SEQ", sequenceName = "CORPORATIVO.DP_VISUALIZACAO_ACESSO_SEQ")
	@GeneratedValue(generator = "DP_VISUALIZACAO_ACESSO_SEQ")
	@Column(name = "ID_VISUALIZACAO_ACESSO", unique = true, nullable = false)
	private Long idVisualizacaoAcesso;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_ACESSO", length = 19)
	private Date dtAcesso;

	@Column(name = "ID_DELEG")
	private Long idDelegado;

	@Column(name = "ID_TITULAR")
	private Long idTitular;

	@Column(name = "ID_DOC")
	private Long idDoc;
	
	@Column(name = "ID_VISUALIZACAO")
	private Long idVisualizacao;

	public Long getIdVisualizacaoAcesso() {
		return idVisualizacaoAcesso;
	}

	public void setIdVisualizacaoAcesso(Long idVisualizacaoAcesso) {
		this.idVisualizacaoAcesso = idVisualizacaoAcesso;
	}

	public Date getDtAcesso() {
		return dtAcesso;
	}

	public void setDtAcesso(Date dtAcesso) {
		this.dtAcesso = dtAcesso;
	}

	public Long getIdDelegado() {
		return idDelegado;
	}

	public void setIdDelegado(Long idDelegado) {
		this.idDelegado = idDelegado;
	}

	public Long getIdTitular() {
		return idTitular;
	}

	public void setIdTitular(Long idTitular) {
		this.idTitular = idTitular;
	}

	public Long getIdDoc() {
		return idDoc;
	}

	public void setIdDoc(Long idDoc) {
		this.idDoc = idDoc;
	}

	public Long getIdVisualizacao() {
		return idVisualizacao;
	}

	public void setIdVisualizacao(Long idVisualizacao) {
		this.idVisualizacao = idVisualizacao;
	}
}