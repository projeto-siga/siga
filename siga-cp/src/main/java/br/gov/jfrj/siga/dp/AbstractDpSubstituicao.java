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
@NamedQueries({
		@NamedQuery(name = "consultarSubstituicoesPermitidas", query = "from DpSubstituicao dps "
				+ " where (dps.dtIniSubst < :dbDatetime or dps.dtIniSubst = null) "
				+ " and (dps.dtFimSubst > :dbDatetime or dps.dtFimSubst = null) "
				+ " and ((dps.substituto = null and dps.lotaSubstituto.idLotacao in (select lot.idLotacao from DpLotacao as lot where lot.idLotacaoIni = :idLotaSubstitutoIni)) or "
				+ " dps.substituto.idPessoa in (select pes.idPessoa from DpPessoa as pes where pes.idPessoaIni = :idSubstitutoIni)) "
				+ " and dps.dtFimRegistro = null"),
		@NamedQuery(name = "consultarOrdemData", query = "from DpSubstituicao as dps "
				+ " where ((dps.titular = null and dps.lotaTitular.idLotacao in (select lot.idLotacao from DpLotacao as lot where lot.idLotacaoIni = :idLotaTitularIni)) or "
				+ " dps.titular.idPessoa in (select pes.idPessoa from DpPessoa as pes where pes.idPessoaIni = :idTitularIni)) "
				+ " and dps.dtFimRegistro = null "
				+ " order by dps.dtIniSubst, dps.dtFimSubst "),
		@NamedQuery(name = "qtdeSubstituicoesAtivasPorTitular", query = "select count(1) from DpSubstituicao as dps "
				+ " where (dps.dtIniSubst < :dbDatetime or dps.dtIniSubst = null) " 
				+ " and (dps.dtFimSubst > :dbDatetime or dps.dtFimSubst = null)"
				+ " and ((dps.titular = null and dps.lotaTitular.idLotacao in (select lot.idLotacao from DpLotacao as lot where lot.idLotacaoIni = :idLotaTitularIni)) or "
				+ " dps.titular.idPessoa in (select pes.idPessoa from DpPessoa as pes where pes.idPessoaIni = :idTitularIni)) "
				+ " and dps.dtFimRegistro = null ")
	})
public abstract class AbstractDpSubstituicao extends Objeto implements
		Serializable {

	@Id
	@SequenceGenerator(name = "DP_SUBSTITUICAO_SEQ", sequenceName = "CORPORATIVO.DP_SUBSTITUICAO_SEQ")
	@GeneratedValue(generator = "DP_SUBSTITUICAO_SEQ")
	@Column(name = "ID_SUBSTITUICAO", unique = true, nullable = false)
	private Long idSubstituicao;

	@Column(name = "DT_FIM_SUBST")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtFimSubst;

	@Column(name = "DT_INI_SUBST")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtIniSubst;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTA_SUBSTITUTO", nullable = false)
	private DpLotacao lotaSubstituto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTA_TITULAR", nullable = false)
	private DpLotacao lotaTitular;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SUBSTITUTO")
	private DpPessoa substituto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TITULAR")
	private DpPessoa titular;

	@Column(name = "DT_FIM_REG", length = 19)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtFimRegistro;

	@Column(name = "DT_INI_REG", nullable = false, length = 19)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtIniRegistro;

	@Column(name = "ID_REG_INI")
	private Long idRegistroInicial;

	/*
	 * (non-Javadoc)
	 */
	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof DpSubstituicao))
			return false;
		final DpSubstituicao that = (DpSubstituicao) rhs;

		if ((this.getIdSubstituicao() == null ? that.getIdSubstituicao() == null
				: this.getIdSubstituicao().equals(that.getIdSubstituicao())))
			return true;
		return false;
	}

	public Date getDtFimSubst() {
		return dtFimSubst;
	}

	public void setDtFimSubst(Date dtFimSubst) {
		this.dtFimSubst = dtFimSubst;
	}

	public Date getDtIniSubst() {
		return dtIniSubst;
	}

	public void setDtIniSubst(Date dtIniSubst) {
		this.dtIniSubst = dtIniSubst;
	}

	public Long getIdSubstituicao() {
		return idSubstituicao;
	}

	public void setIdSubstituicao(Long idSubstituicao) {
		this.idSubstituicao = idSubstituicao;
	}

	public DpLotacao getLotaSubstituto() {
		return lotaSubstituto;
	}

	public void setLotaSubstituto(DpLotacao lotaSubstituto) {
		this.lotaSubstituto = lotaSubstituto;
	}

	public DpLotacao getLotaTitular() {
		return lotaTitular;
	}

	public void setLotaTitular(DpLotacao lotaTitular) {
		this.lotaTitular = lotaTitular;
	}

	public DpPessoa getSubstituto() {
		return substituto;
	}

	public void setSubstituto(DpPessoa substituto) {
		this.substituto = substituto;
	}

	public DpPessoa getTitular() {
		return titular;
	}

	public void setTitular(DpPessoa titular) {
		this.titular = titular;
	}

	public Long getIdRegistroInicial() {
		return idRegistroInicial;
	}

	public void setIdRegistroInicial(Long idRegistroInicial) {
		this.idRegistroInicial = idRegistroInicial;
	}

	public Date getDtFimRegistro() {
		return dtFimRegistro;
	}

	public void setDtFimRegistro(Date dtFimRegistro) {
		this.dtFimRegistro = dtFimRegistro;
	}

	public Date getDtIniRegistro() {
		return dtIniRegistro;
	}

	public void setDtIniRegistro(Date dtIniRegistro) {
		this.dtIniRegistro = dtIniRegistro;
	}
}
