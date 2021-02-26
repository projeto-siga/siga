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
 */
package br.gov.jfrj.siga.wf.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;

/**
 * A class that represents a row in the EX_MOVIMENTACAO table. You can customize
 * the behavior of this class by editing the class, {@link ExMovimentacao()}.
 */
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@DiscriminatorColumn(name = "MOVI_TP")
@Table(name = "sigawf.wf_movimentacao")
public abstract class WfMov extends HistoricoAuditavelSuporte implements Serializable, Comparable<WfMov> {
	@Id
	@GeneratedValue
	@Column(name = "MOVI_ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "MOVI_DS", length = 400)
	private String descr;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROC_ID")
	private WfProcedimento procedimento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PESS_ID_TITULAR")
	private DpPessoa titular;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOTA_ID_TITULAR")
	private DpLotacao lotaTitular;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "MOVI_ID_CANCELADORA")
//	private WfMov movimentacaoCanceladora;

//	@BatchSize(size = 1)
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "movimentacaoRef")
//	private java.util.Set<WfMov> movimentacaoReferenciadoraSet;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "MOVI_ID_REF")
//	private WfMov movimentacaoRef;

	// Solução para não precisar criar HIS_ATIVO em todas as tabelas que herdam de
	// HistoricoSuporte.
	//
	@Column(name = "HIS_ATIVO")
	private Integer hisAtivo;

	public WfMov() {
		super();
	}

	public WfMov(WfProcedimento procedimento, String descr, Date dtIni, DpPessoa titular, DpLotacao lotaTitular,
			CpIdentidade idcIni) {
		super();
		this.setProcedimento(procedimento);
		this.setDescr(descr);
		this.titular = titular;
		this.lotaTitular = lotaTitular;
		this.setHisIdcIni(idcIni);
		this.setHisDtIni(dtIni);
		this.hisAtivo = 1;
	}

	@Override
	public Integer getHisAtivo() {
		this.hisAtivo = super.getHisAtivo();
		return this.hisAtivo;
	}

	@Override
	public void setHisAtivo(Integer hisAtivo) {
		super.setHisAtivo(hisAtivo);
		this.hisAtivo = getHisAtivo();
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return obj == this;
	}

	public DpPessoa getTitular() {
		return titular;
	}

	public void setTitular(DpPessoa titular) {
		this.titular = titular;
	}

	public DpLotacao getLotaTitular() {
		return lotaTitular;
	}

	public void setLotaTitular(DpLotacao lotaTitular) {
		this.lotaTitular = lotaTitular;
	}

	public WfProcedimento getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(WfProcedimento procedimento) {
		this.procedimento = procedimento;
	}

	public String getClasseVO() {
		return this.getClass().getSimpleName() + " " + (this.getHisAtivo() == 1 ? "" : "disabled");
	}

	public String getDtIniVO() {
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		if (getHisDtIni() != null) {
			return df.format(getHisDtIni());
		}
		return "";
	}

	public String getTempoRelativoVO() {
		if (getHisDtIni() != null) {
			return Data.calcularTempoRelativo(getHisDtIni());
		}
		return "";
	}

	public abstract String getEvento();

	public abstract String getDescricaoEvento();

	public List<AcaoVO> getAcoes() {
		List<AcaoVO> set = new ArrayList<>();
		return set;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@Override
	public int compareTo(WfMov o) {
		int i = this.getHisDtIni().compareTo(o.getHisDtIni());
		if (i != 0)
			return i;
		return this.getId().compareTo(o.getId());
	}
}
