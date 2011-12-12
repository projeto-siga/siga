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
import java.util.Set;

import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

public abstract class AbstractDpLotacao extends DpResponsavel implements
		Serializable {

	@Desconsiderar
	private Date dataFimLotacao;

	@Desconsiderar
	private Date dataInicioLotacao;

	@Desconsiderar
	private Long idLotacao;

	@Desconsiderar
	private Long idLotacaoIni;

	private String ideLotacao;

	private DpLotacao lotacaoPai;

	private String nomeLotacao;

	private String siglaLotacao;

	@Desconsiderar
	private CpOrgaoUsuario orgaoUsuario;

	@Desconsiderar
	private DpLotacao lotacaoInicial;

	@Desconsiderar
	private Set<DpLotacao> lotacoesPosteriores;

	@Desconsiderar
	private Set<DpLotacao> dpLotacaoSubordinadosSet;

	private CpTipoLotacao cpTipoLotacao;
	/**
	 * @return the cpTipoLotacao
	 */
	public CpTipoLotacao getCpTipoLotacao() {
		return cpTipoLotacao;
	}

	/**
	 * @param cpTipoLotacao the cpTipoLotacao to set
	 */
	public void setCpTipoLotacao(CpTipoLotacao cpTipoLotacao) {
		this.cpTipoLotacao = cpTipoLotacao;
	}

	public String getIdeLotacao() {
		return ideLotacao;
	}

	public void setIdeLotacao(String ideLotacao) {
		this.ideLotacao = ideLotacao;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof DpLotacao))
			return false;
		final DpLotacao that = (DpLotacao) rhs;

		if ((this.getIdLotacao() == null ? that.getIdLotacao() == null : this
				.getIdLotacao().equals(that.getIdLotacao()))) {
			if ((this.getNomeLotacao() == null ? that.getNomeLotacao() == null
					: this.getNomeLotacao().equals(that.getNomeLotacao())))
				return true;

		}
		return false;

	}

	/**
	 * @return Retorna o atributo dataFimLotacao.
	 */
	public Date getDataFimLotacao() {
		return dataFimLotacao;
	}

	/**
	 * @return Retorna o atributo dataInicioLotacao.
	 */
	public Date getDataInicioLotacao() {
		return dataInicioLotacao;
	}

	/**
	 * @return Retorna o atributo idLotacao.
	 */
	public Long getIdLotacao() {
		return idLotacao;
	}

	public Long getIdLotacaoIni() {
		return idLotacaoIni;
	}

	/**
	 * @return Retorna o atributo lotacaoPai.
	 */
	public DpLotacao getLotacaoPai() {
		return lotacaoPai;
	}

	/**
	 * @return Retorna o atributo nomeLotacao.
	 */
	public String getNomeLotacao() {
		return nomeLotacao;
	}

	public String getSiglaLotacao() {
		return siglaLotacao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
		int idValue = this.getIdLotacao() == null ? 0 : this.getIdLotacao()
				.hashCode();
		result = result * 37 + idValue;
		idValue = this.getNomeLotacao() == null ? 0 : this.getNomeLotacao()
				.hashCode();
		result = result * 37 + idValue;

		return result;
	}

	/**
	 * @param dataFimLotacao
	 *            Atribui a dataFimLotacao o valor.
	 */
	public void setDataFimLotacao(final Date dataFimLotacao) {
		this.dataFimLotacao = dataFimLotacao;
	}

	/**
	 * @param dataInicioLotacao
	 *            Atribui a dataInicioLotacao o valor.
	 */
	public void setDataInicioLotacao(final Date dataInicioLotacao) {
		this.dataInicioLotacao = dataInicioLotacao;
	}

	/**
	 * @param idLotacao
	 *            Atribui a idLotacao o valor.
	 */
	public void setIdLotacao(final Long idLotacao) {
		this.idLotacao = idLotacao;
	}

	public void setIdLotacaoIni(Long idLotacaoIni) {
		this.idLotacaoIni = idLotacaoIni;
	}

	/**
	 * @param lotacaoPai
	 *            Atribui a lotacaoPai o valor.
	 */
	public void setLotacaoPai(final DpLotacao lotacaoPai) {
		this.lotacaoPai = lotacaoPai;
	}

	/**
	 * @param nomeLotacao
	 *            Atribui a nomeLotacao o valor.
	 */
	public void setNomeLotacao(final String nomeLotacao) {
		this.nomeLotacao = nomeLotacao;
	}

	public void setSiglaLotacao(final String siglaLotacao) {
		this.siglaLotacao = siglaLotacao;
	}

	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

	public Set<DpLotacao> getLotacoesPosteriores() {
		return lotacoesPosteriores;
	}

	public void setLotacoesPosteriores(Set<DpLotacao> lotacoesPosteriores) {
		this.lotacoesPosteriores = lotacoesPosteriores;
	}

	public DpLotacao getLotacaoInicial() {
		return lotacaoInicial;
	}

	public void setLotacaoInicial(DpLotacao lotacaoInicial) {
		this.lotacaoInicial = lotacaoInicial;
	}

	public Set<DpLotacao> getDpLotacaoSubordinadosSet() {
		return dpLotacaoSubordinadosSet;
	}

	public void setDpLotacaoSubordinadosSet(
			Set<DpLotacao> dpLotacaoSubordinadosSet) {
		this.dpLotacaoSubordinadosSet = dpLotacaoSubordinadosSet;
	}

}
