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

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

@MappedSuperclass
public abstract class AbstractDpLotacao extends DpResponsavel implements
		Serializable {

	@SequenceGenerator(name = "generator", sequenceName = "DP_LOTACAO_SEQ")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "ID_LOTACAO", nullable = false)
	@Desconsiderar
	private Long idLotacao;
	@Column(name = "ID_LOTACAO_INI")
	@Desconsiderar
	private Long idLotacaoIni;
	@Column(name = "NOME_LOTACAO", nullable = false)
	private String nomeLotacao;
	@Column(name = "SIGLA_LOTACAO")
	private String siglaLotacao;
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_FIM_LOT")
	@Desconsiderar
	private Date dataFimLotacao;
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_INI_LOT")
	@Desconsiderar
	private Date dataInicioLotacao;
	@Column(name = "IDE_LOTACAO")
	@Desconsiderar
	private String ideLotacao;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTACAO_INI", insertable = false, updatable = false)
	@Desconsiderar
	private DpLotacao lotacaoInicial;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTACAO_PAI")
	private DpLotacao lotacaoPai;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lotacaoInicial")
	@Desconsiderar
	private Set<DpLotacao> lotacoesPosteriores = new TreeSet<DpLotacao>();
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO_USU")
	@Desconsiderar
	private CpOrgaoUsuario orgaoUsuario;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lotacaoPai")
	@Desconsiderar
	private Set<DpLotacao> dpLotacaoSubordinadosSet = new TreeSet<DpLotacao>();
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lotacao")
    @Desconsiderar
    private Set<DpPessoa> dpPessoaLotadosSet  = new TreeSet<DpPessoa>();
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_LOTACAO")
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

	public Set<DpPessoa> getDpPessoaLotadosSet() {
		return dpPessoaLotadosSet;
	}

	public void setDpPessoaLotadosSet(Set<DpPessoa> dpPessoaLotadosSet) {
		this.dpPessoaLotadosSet = dpPessoaLotadosSet;
	}

}
