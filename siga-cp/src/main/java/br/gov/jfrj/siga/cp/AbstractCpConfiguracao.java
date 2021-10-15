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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.jfrj.siga.cp.converter.ITipoDeConfiguracaoConverter;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
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
@SuppressWarnings("serial")
@MappedSuperclass
@NamedQueries({
		@NamedQuery(name = "consultarDataUltimaAtualizacao", query = ""
				+ "select max(cpcfg.hisDtIni), max(cpcfg.hisDtFim) "
				+ "from CpConfiguracao cpcfg"),
		@NamedQuery(name = "consultarCpConfiguracoes", query = "from "
				+ "CpConfiguracao cpcfg where (:idTpConfiguracao is null or "
				+ "cpcfg.cpTipoConfiguracao = :idTpConfiguracao)"),
		@NamedQuery(name = "consultarCpConfiguracoesPorLotacaoPessoaServicoTipo", query = "from CpConfiguracao cpcfg"
				+ "	where (cpcfg.dpPessoa.idPessoa = :idPessoa) "
				+ "	and (cpcfg.cpTipoConfiguracao = :idTpConfiguracao) "
				+ "	and (cpcfg.cpServico.siglaServico = :siglaServico)"
				+ "	and (cpcfg.cpSituacaoConfiguracao = :idSitConfiguracao)"
				+ "	and hisDtFim is null"),
		@NamedQuery(name = "consultarCpConfiguracoesPorServico", query = "from CpConfiguracao cpcfg where  (cpcfg.cpServico.idServico = :idServico) and hisDtFim is null"),
		@NamedQuery(name = "consultarCpConfiguracoesPorPessoa", query = "from CpConfiguracao cpcfg where (cpcfg.dpPessoa.idPessoa = :idPessoa) and hisDtFim is null"),
		@NamedQuery(name = "consultarCpConfiguracoesHistoricasPorPessoa", query = "from CpConfiguracao cpcfg where (cpcfg.dpPessoa.idPessoa = :idPessoa) order by cpcfg.cpServico.idServico, cpcfg.hisDtIni"),
		@NamedQuery(name = "consultarCpConfiguracoesPorPeriodo", query = "from CpConfiguracao cpcfg"
				+ "	where (cpcfg.hisDtIni >= :dtInicioVigenciaIni)"
				+ "	and (cpcfg.hisDtIni <= :dtInicioVigenciaFim) "
				+ "	order by cpcfg.hisDtIni"),
		@NamedQuery(name = "consultarCpConfiguracoesPorTipoLotacao", query = "from CpConfiguracao cpcfg where (cpcfg.cpTipoLotacao = :idTpLotacao) and hisDtFim is null"),
		@NamedQuery(name = "consultarCacheDeConfiguracoesAtivas", query = " from "
				+ "CpConfiguracaoCache cpcfg where cpTipoConfiguracao in :tipos and hisDtFim is null")})
public abstract class AbstractCpConfiguracao extends HistoricoAuditavelSuporte
		implements Serializable, CpConvertableEntity {

	@Id
	@SequenceGenerator(name = "CP_CONFIGURACAO_SEQ", sequenceName = "CORPORATIVO.CP_CONFIGURACAO_SEQ")
	@GeneratedValue(generator = "CP_CONFIGURACAO_SEQ")
	@Column(name = "ID_CONFIGURACAO", unique = true, nullable = false)
	@Desconsiderar
	private Long idConfiguracao;

	@Column(name = "DESCR_CONFIGURACAO")
	private String descrConfiguracao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO_USU")
	@NaoRecursivo
	private CpOrgaoUsuario orgaoUsuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTACAO")
	@NaoRecursivo
	private DpLotacao lotacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_COMPLEXO")
	@NaoRecursivo
	private CpComplexo complexo;

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

	@Column(name = "ID_SIT_CONFIGURACAO")
	@NaoRecursivo
	private CpSituacaoDeConfiguracaoEnum cpSituacaoConfiguracao;

	@Convert(converter = ITipoDeConfiguracaoConverter.class)
	@Column(name = "ID_TP_CONFIGURACAO")
	@NaoRecursivo
	private ITipoDeConfiguracao cpTipoConfiguracao;

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

	@Column(name = "NM_EMAIL", length = 50)
	@NaoRecursivo
	private String nmEmail;

	@Column(name = "DESC_FORMULA", length = 1024)
	@NaoRecursivo
	private String dscFormula;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_LOTACAO")
	@NaoRecursivo
	private CpTipoLotacao cpTipoLotacao;

	// @Temporal(TemporalType.TIMESTAMP)
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_INI_VIG_CONFIGURACAO", length = 19)
	private Date dtIniVigConfiguracao;

	// @Temporal(TemporalType.TIMESTAMP)
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_FIM_VIG_CONFIGURACAO", length = 19)
	private Date dtFimVigConfiguracao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	private CpConfiguracao configuracaoInicial;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "configuracaoInicial")
	@Desconsiderar
	private Set<CpConfiguracao> configuracoesPosteriores = new HashSet<CpConfiguracao>(
			0);

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO_OBJETO")
	@NaoRecursivo
	private CpOrgaoUsuario orgaoObjeto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTACAO_OBJETO")
	@NaoRecursivo
	private DpLotacao lotacaoObjeto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_COMPLEXO_OBJETO")
	@NaoRecursivo
	private CpComplexo complexoObjeto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CARGO_OBJETO")
	@NaoRecursivo
	private DpCargo cargoObjeto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FUNCAO_CONFIANCA_OBJETO")
	@NaoRecursivo
	private DpFuncaoConfianca funcaoConfiancaObjeto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PESSOA_OBJETO")
	@NaoRecursivo
	private DpPessoa pessoaObjeto;

	public Set<CpConfiguracao> getConfiguracoesPosteriores() {
		return configuracoesPosteriores;
	}

	public void setConfiguracoesPosteriores(
			Set<CpConfiguracao> configuracoesPosteriores) {
		this.configuracoesPosteriores = configuracoesPosteriores;
	}

	public CpConfiguracao getConfiguracaoInicial() {
		return configuracaoInicial;
	}

	public void setConfiguracaoInicial(CpConfiguracao configuracaoInicial) {
		this.configuracaoInicial = configuracaoInicial;
	}

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

	public String getDescrConfiguracao() {
		return descrConfiguracao;
	}

	public void setDescrConfiguracao(String descrConfiguracao) {
		this.descrConfiguracao = descrConfiguracao;
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

	public CpComplexo getComplexo() {
		return complexo;
	}

	public void setComplexo(CpComplexo complexo) {
		this.complexo = complexo;
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

	public CpSituacaoDeConfiguracaoEnum getCpSituacaoConfiguracao() {
		return cpSituacaoConfiguracao;
	}

	public void setCpSituacaoConfiguracao(
			CpSituacaoDeConfiguracaoEnum cpSituacaoConfiguracao) {
		this.cpSituacaoConfiguracao = cpSituacaoConfiguracao;
	}

	public ITipoDeConfiguracao getCpTipoConfiguracao() {
		return cpTipoConfiguracao;
	}

	public void setCpTipoConfiguracao(ITipoDeConfiguracao cpTipoConfiguracao) {
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

	/**
	 * @param cpTipoLotacao
	 *            the cpTipoLotacao to set
	 */
	public void setCpTipoLotacao(CpTipoLotacao cpTipoLotacao) {
		this.cpTipoLotacao = cpTipoLotacao;
	}

	public CpOrgaoUsuario getOrgaoObjeto() {
		return orgaoObjeto;
	}

	public void setOrgaoObjeto(CpOrgaoUsuario orgaoObjeto) {
		this.orgaoObjeto = orgaoObjeto;
	}

	public DpLotacao getLotacaoObjeto() {
		return lotacaoObjeto;
	}

	public void setLotacaoObjeto(DpLotacao lotacaoObjeto) {
		this.lotacaoObjeto = lotacaoObjeto;
	}

	public CpComplexo getComplexoObjeto() {
		return complexoObjeto;
	}

	public void setComplexoObjeto(CpComplexo complexoObjeto) {
		this.complexoObjeto = complexoObjeto;
	}

	public DpCargo getCargoObjeto() {
		return cargoObjeto;
	}

	public void setCargoObjeto(DpCargo cargoObjeto) {
		this.cargoObjeto = cargoObjeto;
	}

	public DpFuncaoConfianca getFuncaoConfiancaObjeto() {
		return funcaoConfiancaObjeto;
	}

	public void setFuncaoConfiancaObjeto(DpFuncaoConfianca funcaoConfiancaObjeto) {
		this.funcaoConfiancaObjeto = funcaoConfiancaObjeto;
	}

	public DpPessoa getPessoaObjeto() {
		return pessoaObjeto;
	}

	public void setPessoaObjeto(DpPessoa pessoaObjeto) {
		this.pessoaObjeto = pessoaObjeto;
	}

}
