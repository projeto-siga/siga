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
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

import org.hibernate.search.annotations.DocumentId;

import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

/**
 * A class that represents a row in the EX_MOVIMENTACAO table. You can customize
 * the behavior of this class by editing the class, {@link ExMovimentacao()}.
 */
public abstract class AbstractExMovimentacao extends ExArquivo implements
		Serializable {
	private DpPessoa cadastrante;

	private Blob conteudoBlobMov;

	private String conteudoTpMov;

	private java.util.Set<ExMovimentacao> exMovimentacaoReferenciadoraSet;

	private String descrMov;

	private DpPessoa destinoFinal;

	
	private Date dtFimMov;

	private Date dtIniMov;

	private Date dtDispPublicacao;

	private Date dtEfetivaDispPublicacao;

	private Date dtEfetivaPublicacao;

	private Long numTRFPublicacao;

	private String pagPublicacao;

	private Date dtMov;

	private String obsOrgao;

	private String nmFuncaoSubscritor;

	// private ExDocumento exDocumento;

	private ExNivelAcesso exNivelAcesso;

	private CpOrgao orgaoExterno;

	private ExMobil exMobilRef;

	// private ExEstadoDoc exEstadoDoc;

	private ExMovimentacao exMovimentacaoCanceladora;

	private ExMovimentacao exMovimentacaoRef;

	private ExTipoDespacho exTipoDespacho;
	
	private ExClassificacao exClassificacao;

	private ExTipoMovimentacao exTipoMovimentacao;

	@DocumentId
	private Long idMov;
	
	private Long idTpMov;

	private DpLotacao lotaCadastrante;

	private DpLotacao lotaDestinoFinal;

	private DpLotacao lotaSubscritor;

	private DpLotacao lotaResp;

	private String nmArqMov;

	// private Integer numVia;

	// private Integer numViaDocPai;

	// private Integer numViaDocRef;

	private DpPessoa resp;

	private DpPessoa subscritor;

	private DpPessoa titular;

	private DpLotacao lotaTitular;

	private String cadernoPublicacaoDje;

	private ExMobil exMobil;

	private Integer numPaginasOri;
	
	private ExPapel exPapel;

	private CpMarcador marcador;

	public void setNumPaginasOri(Integer numPaginasOri) {
		this.numPaginasOri = numPaginasOri;
	}

	public Integer getNumPaginasOri() {
		return numPaginasOri;
	}

	public DpPessoa getTitular() {
		return titular;
	}

	public void setTitular(DpPessoa titular) {
		this.titular = titular;
	}

	public AbstractExMovimentacao() {
	}

	public AbstractExMovimentacao(final Long idMov) {
		setIdMov(idMov);
	}

	public DpPessoa getCadastrante() {
		return cadastrante;
	}

	public Blob getConteudoBlobMov() {
		return conteudoBlobMov;
	}

	public String getConteudoTpMov() {
		return conteudoTpMov;
	}

	public String getDescrMov() {
		return descrMov;
	}

	public DpPessoa getDestinoFinal() {
		return destinoFinal;
	}

	public Date getDtFimMov() {
		return dtFimMov;
	}

	public Date getDtIniMov() {
		return dtIniMov;
	}

	public Date getDtMov() {
		return dtMov;
	}

	public ExMovimentacao getExMovimentacaoCanceladora() {
		return exMovimentacaoCanceladora;
	}

	public ExTipoDespacho getExTipoDespacho() {
		return exTipoDespacho;
	}
	
	public ExClassificacao getExClassificacao() {
		return exClassificacao;
	}

	public ExTipoMovimentacao getExTipoMovimentacao() {
		return exTipoMovimentacao;
	}

	public Long getIdMov() {
		return idMov;
	}

	public DpLotacao getLotaCadastrante() {
		return lotaCadastrante;
	}

	public DpLotacao getLotaResp() {
		return lotaResp;
	}

	public String getNmArqMov() {
		return nmArqMov;
	}

	public DpPessoa getResp() {
		return resp;
	}

	public DpPessoa getSubscritor() {
		return subscritor;
	}

	public void setIdTpMov(final Long idTpMov){
		this.idTpMov = idTpMov;
	}
	
	public void setCadastrante(final DpPessoa cadastrante) {
		this.cadastrante = cadastrante;
	}

	public void setConteudoBlobMov(Blob conteudoBlobMov) {
		this.conteudoBlobMov = conteudoBlobMov;
	}

	public void setConteudoTpMov(final String conteudoTpMov) {
		this.conteudoTpMov = conteudoTpMov;
	}

	public void setDescrMov(final String descrMov) {
		this.descrMov = descrMov;
	}

	public void setDestinoFinal(final DpPessoa destinoFinal) {
		this.destinoFinal = destinoFinal;
	}

	public void setDtFimMov(final Date dtFimMov) {
		 this.dtFimMov = dtFimMov;
	}

	public void setDtIniMov(final Date dtIniMov) {
		this.dtIniMov = dtIniMov;
	}

	public void setDtMov(final Date dtMov) {
		this.dtMov = dtMov;
	}

	public void setExMovimentacaoCanceladora(
			final ExMovimentacao exMovimentacaoCanceladora) {
		this.exMovimentacaoCanceladora = exMovimentacaoCanceladora;
	}

	public void setExTipoDespacho(final ExTipoDespacho exTipoDespacho) {
		this.exTipoDespacho = exTipoDespacho;
	}
	
	public void setExClassificacao(ExClassificacao exClassificacao) {
		this.exClassificacao = exClassificacao;
	}

	public void setExTipoMovimentacao(
			final ExTipoMovimentacao exTipoMovimentacao) {
		this.exTipoMovimentacao = exTipoMovimentacao;
	}

	public void setIdMov(final Long idMov) {
		this.idMov = idMov;
	}

	public void setLotaCadastrante(final DpLotacao lotaCadastrante) {
		this.lotaCadastrante = lotaCadastrante;
	}

	public void setLotaResp(final DpLotacao lotaResp) {
		this.lotaResp = lotaResp;
	}

	public void setNmArqMov(final String nmArqMov) {
		this.nmArqMov = nmArqMov;
	}

	public void setResp(final DpPessoa resp) {
		this.resp = resp;
	}

	public void setSubscritor(final DpPessoa subscritor) {
		this.subscritor = subscritor;
	}

	public DpLotacao getLotaDestinoFinal() {
		return lotaDestinoFinal;
	}

	public void setLotaDestinoFinal(final DpLotacao lotaDestinoFinal) {
		this.lotaDestinoFinal = lotaDestinoFinal;
	}

	public ExMobil getExMobilRef() {
		return exMobilRef;
	}

	public void setExMobilRef(final ExMobil exMobilRef) {
		this.exMobilRef = exMobilRef;
	}

	public CpOrgao getOrgaoExterno() {
		return orgaoExterno;
	}

	public void setOrgaoExterno(final CpOrgao orgaoExterno) {
		this.orgaoExterno = orgaoExterno;
	}

	public String getObsOrgao() {
		return obsOrgao;
	}

	public void setObsOrgao(final String obsOrgao) {
		this.obsOrgao = obsOrgao;
	}

	public ExMovimentacao getExMovimentacaoRef() {
		return exMovimentacaoRef;
	}

	public void setExMovimentacaoRef(ExMovimentacao exMovRef) {
		this.exMovimentacaoRef = exMovRef;
	}

	public java.util.Set<ExMovimentacao> getExMovimentacaoReferenciadoraSet() {
		return exMovimentacaoReferenciadoraSet;
	}

	public void setExMovimentacaoReferenciadoraSet(
			java.util.Set<ExMovimentacao> exMovimentacaoReferenciadoraSet) {
		this.exMovimentacaoReferenciadoraSet = exMovimentacaoReferenciadoraSet;
	}

	public DpLotacao getLotaSubscritor() {
		return lotaSubscritor;
	}

	public void setLotaSubscritor(DpLotacao lotaSubscritor) {
		this.lotaSubscritor = lotaSubscritor;
	}

	public DpLotacao getLotaTitular() {
		return lotaTitular;
	}

	public void setLotaTitular(DpLotacao lotaTitular) {
		this.lotaTitular = lotaTitular;
	}

	public String getNmFuncaoSubscritor() {
		return nmFuncaoSubscritor;
	}

	public void setNmFuncaoSubscritor(String nmFuncaoSubscritor) {
		this.nmFuncaoSubscritor = nmFuncaoSubscritor;
	}

	public Date getDtDispPublicacao() {
		return dtDispPublicacao;
	}

	public void setDtDispPublicacao(Date dtDispPublicacao) {
		this.dtDispPublicacao = dtDispPublicacao;
	}

	public ExNivelAcesso getExNivelAcesso() {
		return exNivelAcesso;
	}

	public void setExNivelAcesso(ExNivelAcesso exNivelAcesso) {
		this.exNivelAcesso = exNivelAcesso;
	}

	public Date getDtEfetivaDispPublicacao() {
		return dtEfetivaDispPublicacao;
	}

	public void setDtEfetivaDispPublicacao(Date dtEfetivaDispPublicacao) {
		this.dtEfetivaDispPublicacao = dtEfetivaDispPublicacao;
	}

	public Date getDtEfetivaPublicacao() {
		return dtEfetivaPublicacao;
	}

	public void setDtEfetivaPublicacao(Date dtEfetivaPublicacao) {
		this.dtEfetivaPublicacao = dtEfetivaPublicacao;
	}

	public Long getNumTRFPublicacao() {
		return numTRFPublicacao;
	}

	public void setNumTRFPublicacao(Long numTRFPublicacao) {
		this.numTRFPublicacao = numTRFPublicacao;
	}

	public String getPagPublicacao() {
		return pagPublicacao;
	}

	public void setPagPublicacao(String pagPublicacao) {
		this.pagPublicacao = pagPublicacao;
	}

	public String getCadernoPublicacaoDje() {
		return cadernoPublicacaoDje;
	}

	public void setCadernoPublicacaoDje(String cadernoPublicacaoDje) {
		this.cadernoPublicacaoDje = cadernoPublicacaoDje;
	}

	public ExMobil getExMobil() {
		return exMobil;
	}

	public void setExMobil(ExMobil exMobil) {
		this.exMobil = exMobil;
	}

	public ExPapel getExPapel() {
		return exPapel;
	}

	public void setExPapel(ExPapel exPapel) {
		this.exPapel = exPapel;
	}

	public CpMarcador getMarcador() {
		return marcador;
	}

	public void setMarcador(CpMarcador marcador) {
		this.marcador = marcador;
	}
}