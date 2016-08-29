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

import java.util.Date;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

public class ExDocumentoDaoFiltro extends DaoFiltroSelecionavel {
	@Override
	public void setSigla(final String nome) {
		// Se receber valor nulo, zerar todos os campos
		if (nome == null || nome.equals("")) {
			setIdFormaDoc(null);
			setAnoEmissao(null);
			setNumExpediente(null);
			setNumVia(null);
			return;
		}
		final ExMobil docVia = new ExMobil();
		docVia.setSigla(nome);
		if (docVia.getExDocumento() != null) {
			if (docVia.getExDocumento().getExFormaDocumento() != null)
				setIdFormaDoc(docVia.getExDocumento().getExFormaDocumento()
						.getIdFormaDoc());
			setIdDoc(docVia.getExDocumento().getIdDoc());
			setAnoEmissao(docVia.getExDocumento().getAnoEmissao());
			setNumExpediente(docVia.getExDocumento().getNumExpediente());
			setNumVia(docVia.getNumSequencia());
			if (docVia.getExDocumento ().getOrgaoUsuario () != null )
				setIdOrgaoUsu ( docVia.getExDocumento ().getOrgaoUsuario ().getIdOrgaoUsu () );
		}
	}

	private Long idOrgaoUsu;

	private Long idDoc;

	private Long anoEmissao;

	private Long classificacaoSelId;

	private String descrDocumento;

	private Long destinatarioSelId;

	public Date dtDoc;

	private Long idFormaDoc;

	private Integer idTpDoc;

	private Integer lotacaoDestinatarioSelId;

	private Integer lotacaoCadastranteAtualId;

	private String nmDestinatario;

	private String nmSubscritorExt;

	private Long numExpediente;

	private String numExtDoc;

	private String numAntigoDoc;

	private Integer numVia;

	private Long orgaoExternoDestinatarioSelId;

	private Long orgaoExternoSelId;

	private Long subscritorSelId;

	private Long ultMovIdEstadoDoc;

	private Long ultMovLotaRespSelId;

	private Long ultMovRespSelId;

	private Long ultMovCadastranteSelId;

	private Long ultMovLotaCadastranteSelId;

	private Long ultMovSubscritorSelId;

	private Long ultMovLotaSubscritorSelId;

	public Long getAnoEmissao() {
		return anoEmissao;
	}

	public Long getClassificacaoSelId() {
		return classificacaoSelId;
	}

	public String getDescrDocumento() {
		return descrDocumento;
	}

	public Long getDestinatarioSelId() {
		return destinatarioSelId;
	}

	public Long getIdFormaDoc() {
		return idFormaDoc;
	}

	public Integer getIdTpDoc() {
		return idTpDoc;
	}

	public Integer getLotacaoDestinatarioSelId() {
		return lotacaoDestinatarioSelId;
	}

	public String getNmDestinatario() {
		return nmDestinatario;
	}

	public String getNmSubscritorExt() {
		return nmSubscritorExt;
	}

	public Long getNumExpediente() {
		return numExpediente;
	}

	public String getNumExtDoc() {
		return numExtDoc;
	}

	public Long getOrgaoExternoDestinatarioSelId() {
		return orgaoExternoDestinatarioSelId;
	}

	public Long getSubscritorSelId() {
		return subscritorSelId;
	}

	public Long getUltMovIdEstadoDoc() {
		return ultMovIdEstadoDoc;
	}

	public Long getUltMovLotaRespSelId() {
		return ultMovLotaRespSelId;
	}

	public Long getUltMovRespSelId() {
		return ultMovRespSelId;
	}

	public Long getUltMovSubscritorSelId() {
		return ultMovSubscritorSelId;
	}

	public void setAnoEmissao(final Long anoEmissao) {
		this.anoEmissao = anoEmissao;
	}

	public void setClassificacaoSelId(final Long classificacaoSelId) {
		this.classificacaoSelId = classificacaoSelId;
	}

	public void setDescrDocumento(final String descrDocumento) {
		this.descrDocumento = descrDocumento;
	}

	public void setDestinatarioSelId(final Long destinatarioSelId) {
		this.destinatarioSelId = destinatarioSelId;
	}

	public void setIdFormaDoc(final Long idFormaDoc) {
		this.idFormaDoc = idFormaDoc;
	}

	public void setIdTpDoc(final Integer idTpDoc) {
		this.idTpDoc = idTpDoc;
	}

	public void setLotacaoDestinatarioSelId(
			final Integer lotacaoDestinatarioSelId) {
		this.lotacaoDestinatarioSelId = lotacaoDestinatarioSelId;
	}

	public void setNmDestinatario(final String nmDestinatario) {
		this.nmDestinatario = nmDestinatario;
	}

	public void setNmSubscritorExt(final String nmSubscritorExt) {
		this.nmSubscritorExt = nmSubscritorExt;
	}

	public void setNumExpediente(final Long numExpediente) {
		this.numExpediente = numExpediente;
	}

	public void setNumExtDoc(final String numExtDoc) {
		this.numExtDoc = numExtDoc;
	}

	public void setOrgaoExternoDestinatarioSelId(
			final Long orgaoExternoDestinatarioSelId) {
		this.orgaoExternoDestinatarioSelId = orgaoExternoDestinatarioSelId;
	}

	public void setSubscritorSelId(final Long subscritorSelId) {
		this.subscritorSelId = subscritorSelId;
	}

	public void setUltMovIdEstadoDoc(final Long ultMovIdEstadoDoc) {
		this.ultMovIdEstadoDoc = ultMovIdEstadoDoc;
	}

	public void setUltMovLotaRespSelId(final Long ultMovLotaRespSelId) {
		this.ultMovLotaRespSelId = ultMovLotaRespSelId;
	}

	public void setUltMovRespSelId(final Long ultMovRespSelId) {
		this.ultMovRespSelId = ultMovRespSelId;
	}

	public void setUltMovSubscritorSelId(final Long ultMovSubscritorSelId) {
		this.ultMovSubscritorSelId = ultMovSubscritorSelId;
	}

	public Long getUltMovLotaSubscritorSelId() {
		return ultMovLotaSubscritorSelId;
	}

	public void setUltMovLotaSubscritorSelId(
			final Long ultMovLotaSubscritorSelId) {
		this.ultMovLotaSubscritorSelId = ultMovLotaSubscritorSelId;
	}

	public Long getId() {
		return getId();
	}

	public Integer getNumVia() {
		return numVia;
	}

	public void setNumVia(final Integer numVia) {
		this.numVia = numVia;
	}

	public Long getIdDoc() {
		return idDoc;
	}

	public void setIdDoc(final Long idDoc) {
		this.idDoc = idDoc;
	}

	public Long getOrgaoExternoSelId() {
		return orgaoExternoSelId;
	}

	public void setOrgaoExternoSelId(final Long orgaoExternoSelId) {
		this.orgaoExternoSelId = orgaoExternoSelId;
	}

	public Date getDtDoc() {
		return dtDoc;
	}

	public void setDtDoc(final Date dtDoc) {
		this.dtDoc = dtDoc;
	}

	public Long getUltMovCadastranteSelId() {
		return ultMovCadastranteSelId;
	}

	public void setUltMovCadastranteSelId(Long ultMovCadastranteSelId) {
		this.ultMovCadastranteSelId = ultMovCadastranteSelId;
	}

	public Long getUltMovLotaCadastranteSelId() {
		return ultMovLotaCadastranteSelId;
	}

	public void setUltMovLotaCadastranteSelId(Long ultMovLotaCadastranteSelId) {
		this.ultMovLotaCadastranteSelId = ultMovLotaCadastranteSelId;
	}

	public String getNumAntigoDoc() {
		return numAntigoDoc;
	}

	public void setNumAntigoDoc(String numAntigoDoc) {
		this.numAntigoDoc = numAntigoDoc;
	}

	public Integer getLotacaoCadastranteAtualId() {
		return lotacaoCadastranteAtualId;
	}

	public void setLotacaoCadastranteAtualId(Integer lotacaoCadastranteAtualId) {
		this.lotacaoCadastranteAtualId = lotacaoCadastranteAtualId;
	}

	public Long getIdOrgaoUsu() {
		return idOrgaoUsu;
	}

	public void setIdOrgaoUsu(Long idOrgaoUsu) {
		this.idOrgaoUsu = idOrgaoUsu;
	}
}
