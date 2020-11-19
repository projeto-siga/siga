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
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.dp;

import java.io.Serializable;

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

import br.gov.jfrj.siga.model.Objeto;

//Essa anotação é necessária por causa do mappedBy em CpOcorrenciaFeriado que aponta pra cá 
@MappedSuperclass
@NamedQueries({ @NamedQuery(name = "listarAplicacoesFeriado", query = "from CpAplicacaoFeriado apl where apl.cpOcorrenciaFeriado.idOcorrencia = :cpOcorrenciaFeriado") })
public abstract class AbstractCpAplicacaoFeriado extends Objeto implements
		Serializable {

	@Id
	@SequenceGenerator(sequenceName = "CORPORATIVO.CP_APLICACAO_FERIADO_SEQ", name = "CORPORATIVO.CP_APLICACAO_FERIADO_SEQ")
	@GeneratedValue(generator = "CORPORATIVO.CP_APLICACAO_FERIADO_SEQ")	
	@Column(name = "ID_APLICACAO", unique = true, nullable = false)
	private Long idAplicacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTACAO")
	private DpLotacao dpLotacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOCALIDADE")
	private CpLocalidade localidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO_USU")
	private CpOrgaoUsuario orgaoUsu;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_OCORRENCIA_FERIADO")
	private CpOcorrenciaFeriado cpOcorrenciaFeriado;

	@Column(name = "FERIADO", nullable = false, length = 1)
	private String fgFeriado;

	public String getFgFeriado() {
		return fgFeriado;
	}

	public void setFgFeriado(String feriado) {
		this.fgFeriado = feriado;
	}

	public Long getIdAplicacao() {
		return idAplicacao;
	}

	public void setIdAplicacao(Long idAplicacao) {
		this.idAplicacao = idAplicacao;
	}

	public DpLotacao getDpLotacao() {
		return dpLotacao;
	}

	public void setDpLotacao(DpLotacao dpLotacao) {
		this.dpLotacao = dpLotacao;
	}

	public CpLocalidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(CpLocalidade localicade) {
		this.localidade = localicade;
	}

	public CpOrgaoUsuario getOrgaoUsu() {
		return orgaoUsu;
	}

	public void setOrgaoUsu(CpOrgaoUsuario orgaoUsu) {
		this.orgaoUsu = orgaoUsu;
	}

	public CpOcorrenciaFeriado getCpOcorrenciaFeriado() {
		return cpOcorrenciaFeriado;
	}

	public void setCpOcorrenciaFeriado(CpOcorrenciaFeriado cpOcorrenciaFeriado) {
		this.cpOcorrenciaFeriado = cpOcorrenciaFeriado;
	}

}
