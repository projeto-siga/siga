/*******************************************************************************
] * Copyright (c) 2006 - 2011 SJRJ.
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
 * Criado em  12/12/2005
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.cp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoLotacao;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.NaoRecursivo;

/**
 * A class that represents a row in the CP_CLASSIFICACAO table. You can
 * customize the behavior of this class by editing the class, {@link
 * CpClassificacao()}.
 */
@MappedSuperclass
public abstract class AbstractCpConfiguracao extends HistoricoAuditavelSuporte
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4514355304185987860L;

	@Id
	//Ver comentário em sigasr.controllers.Util.nextVal() 
	//@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	//@SequenceGenerator(name = "generator", sequenceName = "CP_CONFIGURACAO_SEQ")
	@Column(name = "ID_CONFIGURACAO", nullable = false)
	@Desconsiderar
	private Long idConfiguracao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO_USU")
	@NaoRecursivo
	private CpOrgaoUsuario orgaoUsuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTACAO")
	@NaoRecursivo
	private DpLotacao lotacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CARGO")
	@NaoRecursivo
	private DpCargo cargo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FUNCAO_CONFIANCA")
	@NaoRecursivo
	private DpFuncaoConfianca funcaoConfianca;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PESSOA")
	@NaoRecursivo
	private DpPessoa dpPessoa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SIT_CONFIGURACAO")
	@NaoRecursivo
	private CpSituacaoConfiguracao cpSituacaoConfiguracao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_CONFIGURACAO")
	@NaoRecursivo
	private CpTipoConfiguracao cpTipoConfiguracao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SERVICO")
	@NaoRecursivo
	private CpServico cpServico;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_IDENTIDADE")
	@NaoRecursivo
	private CpIdentidade cpIdentidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_GRUPO")
	@NaoRecursivo
	private CpGrupo cpGrupo;

	@Column(name = "NM_EMAIL")
	@NaoRecursivo
	private String nmEmail;

	@Column(name = "DESC_FORMULA")
	@NaoRecursivo
	private String dscFormula;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_LOTACAO")
	@NaoRecursivo
	private CpTipoLotacao cpTipoLotacao;

	@Column(name = "DT_INI_VIG_CONFIGURACAO")
	@Temporal(TemporalType.DATE)
	private Date dtIniVigConfiguracao;

	@Column(name = "DT_FIM_VIG_CONFIGURACAO")
	@Temporal(TemporalType.DATE)
	private Date dtFimVigConfiguracao;
	
	@Desconsiderar
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="HIS_IDC_INI")
	private CpIdentidade hisIdcIni;

	@Desconsiderar
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="HIS_IDC_FIM")
	private CpIdentidade hisIdcFim;
	
	@Column(name = "HIS_ID_INI")
	@Desconsiderar
	private Long hisIdIni;

	@Column(name = "DT_INI_REG")
	@Desconsiderar
	private Date hisDtIni;

	@Column(name = "DT_FIM_REG")
	@Desconsiderar
	private Date hisDtFim;

	public CpIdentidade getCpIdentidade() {
		return cpIdentidade;
	}

	public void setCpIdentidade(CpIdentidade cpIdentidade) {
		this.cpIdentidade = cpIdentidade;
	}

	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof CpConfiguracao))
			return false;
		final CpConfiguracao that = (CpConfiguracao) rhs;

		if ((this.getIdConfiguracao() == null ? that.getIdConfiguracao() == null
				: this.getIdConfiguracao().equals(that.getIdConfiguracao())))
			return true;
		return false;

	}

	public Long getIdConfiguracao() {
		return idConfiguracao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
		final int idValue = this.getIdConfiguracao() == null ? 0 : this
				.getIdConfiguracao().hashCode();
		result = result * 37 + idValue;

		return result;

	}

	public void setIdConfiguracao(final Long idConfiguracao) {
		this.idConfiguracao = idConfiguracao;
	}

	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

	public DpLotacao getLotacao() {
		return lotacao;
	}

	public void setLotacao(DpLotacao lotacao) {
		this.lotacao = lotacao;
	}

	public DpCargo getCargo() {
		return cargo;
	}

	public void setCargo(DpCargo cargo) {
		this.cargo = cargo;
	}

	public DpFuncaoConfianca getFuncaoConfianca() {
		return funcaoConfianca;
	}

	public void setFuncaoConfianca(DpFuncaoConfianca funcaoConfianca) {
		this.funcaoConfianca = funcaoConfianca;
	}

	public DpPessoa getDpPessoa() {
		return dpPessoa;
	}

	public void setDpPessoa(DpPessoa dpPessoa) {
		this.dpPessoa = dpPessoa;
	}

	public CpSituacaoConfiguracao getCpSituacaoConfiguracao() {
		return cpSituacaoConfiguracao;
	}

	public void setCpSituacaoConfiguracao(
			CpSituacaoConfiguracao cpSituacaoConfiguracao) {
		this.cpSituacaoConfiguracao = cpSituacaoConfiguracao;
	}

	public CpTipoConfiguracao getCpTipoConfiguracao() {
		return cpTipoConfiguracao;
	}

	public void setCpTipoConfiguracao(CpTipoConfiguracao cpTipoConfiguracao) {
		this.cpTipoConfiguracao = cpTipoConfiguracao;
	}

	public CpServico getCpServico() {
		return cpServico;
	}

	public void setCpServico(CpServico cpServico) {
		this.cpServico = cpServico;
	}

	/**
	 * @return the cpGrupo
	 */
	public CpGrupo getCpGrupo() {
		return cpGrupo;
	}

	/**
	 * @param cpGrupo
	 *            the cpGrupo to set
	 */
	public void setCpGrupo(CpGrupo cpGrupo) {
		this.cpGrupo = cpGrupo;
	}

	/**
	 * @return the nmEmail
	 */
	public String getNmEmail() {
		return nmEmail;
	}

	/**
	 * @param nmEmail
	 *            the nmEmail to set
	 */
	public void setNmEmail(String nmEmail) {
		this.nmEmail = nmEmail;
	}

	/**
	 * @return the dscFormula
	 */
	public String getDscFormula() {
		return dscFormula;
	}

	/**
	 * @param dscFormula
	 *            the dscFormula to set
	 */
	public void setDscFormula(String dscFormula) {
		this.dscFormula = dscFormula;
	}

	/**
	 * @return the cpTipoLotacao
	 */
	public CpTipoLotacao getCpTipoLotacao() {
		return cpTipoLotacao;
	}

	public Date getDtIniVigConfiguracao() {
		return dtIniVigConfiguracao;
	}

	public void setDtIniVigConfiguracao(Date dtIniVigConfiguracao) {
		this.dtIniVigConfiguracao = dtIniVigConfiguracao;
	}

	public Date getDtFimVigConfiguracao() {
		return dtFimVigConfiguracao;
	}

	public void setDtFimVigConfiguracao(Date dtFimVigConfiguracao) {
		this.dtFimVigConfiguracao = dtFimVigConfiguracao;
	}

	public CpIdentidade getHisIdcIni() {
		return hisIdcIni;
	}

	public void setHisIdcIni(CpIdentidade hisIdcIni) {
		this.hisIdcIni = hisIdcIni;
	}

	public CpIdentidade getHisIdcFim() {
		return hisIdcFim;
	}

	public void setHisIdcFim(CpIdentidade hisIdcFim) {
		this.hisIdcFim = hisIdcFim;
	}

	public Long getHisIdIni() {
		return hisIdIni;
	}

	public void setHisIdIni(Long hisIdIni) {
		this.hisIdIni = hisIdIni;
	}

	public Date getHisDtIni() {
		return hisDtIni;
	}

	public void setHisDtIni(Date hisDtIni) {
		this.hisDtIni = hisDtIni;
	}

	public Date getHisDtFim() {
		return hisDtFim;
	}

	public void setHisDtFim(Date hisDtFim) {
		this.hisDtFim = hisDtFim;
	}

	/**
	 * @param cpTipoLotacao
	 *            the cpTipoLotacao to set
	 */
	public void setCpTipoLotacao(CpTipoLotacao cpTipoLotacao) {
		this.cpTipoLotacao = cpTipoLotacao;
	}

}
