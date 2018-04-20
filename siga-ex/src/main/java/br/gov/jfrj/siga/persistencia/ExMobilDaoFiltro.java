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

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.hibernate.ext.IExMobilDaoFiltro;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

public class ExMobilDaoFiltro extends DaoFiltroSelecionavel implements
		IExMobilDaoFiltro {
	@Override
	public void setSigla(String nome) {
		// Se receber valor nulo, zerar todos os campos
		if (nome == null || nome.equals("")) {
			setIdFormaDoc(null);
			setAnoEmissao(null);
			setNumExpediente(null);
			setNumSequencia(null);
			setIdTipoMobil(null);
			return;
		}
		final ExMobil mob = new ExMobil();
		if (idOrgaoUsu != null && idOrgaoUsu != 0) {
			final ExDocumento doc = new ExDocumento();
			doc.setOrgaoUsuario(ExDao.getInstance().consultar(idOrgaoUsu,
					CpOrgaoUsuario.class, false));
			mob.setExDocumento(doc);
		}
		mob.setSigla(nome);
		if (mob.getExDocumento() != null) {
			if (mob.getExDocumento().getExFormaDocumento() != null)
				setIdFormaDoc(mob.getExDocumento().getExFormaDocumento()
						.getIdFormaDoc());
			setIdDoc(mob.getExDocumento().getIdDoc());
			setAnoEmissao(mob.getExDocumento().getAnoEmissao());
			setNumExpediente(mob.getExDocumento().getNumExpediente());
			setNumSequencia(mob.getNumSequencia());
			if (mob.getExTipoMobil() != null)
				setIdTipoMobil(mob.getExTipoMobil().getIdTipoMobil());
			if (mob.getExDocumento().getOrgaoUsuario() != null){
				setIdOrgaoUsu(mob.getExDocumento().getOrgaoUsuario()
						.getIdOrgaoUsu());
			}
			if (mob.getExTipoMobil() != null)
				setIdTipoMobil(mob.getExTipoMobil().getIdTipoMobil());
		}
	}

	private Long idOrgaoUsu;

	private Long idDoc;

	private Long anoEmissao;

	private Long classificacaoSelId;

	private String descrDocumento;

	private String fullText;

	private Long destinatarioSelId;

	public Date dtDoc;

	public Date dtDocFinal;

	private Long idTipoFormaDoc;

	private Long idMod;

	private Long idFormaDoc;

	private Integer idTpDoc;

	private Long lotacaoDestinatarioSelId;

	private Long lotacaoCadastranteAtualId;

	private String nmDestinatario;

	private String nmSubscritorExt;

	private Long numExpediente;

	private String numExtDoc;

	private String numAntigoDoc;

	private Long idTipoMobil;

	private Integer numSequencia;

	private Long orgaoExternoDestinatarioSelId;

	private Long orgaoExternoSelId;

	private Long subscritorSelId;

	private Long cadastranteSelId;

	private Long lotaCadastranteSelId;

	private Long ultMovIdEstadoDoc;

	private Long ultMovLotaRespSelId;

	private Long ultMovRespSelId;

	private Integer ordem;

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

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

	public Long getLotacaoDestinatarioSelId() {
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

	public void setAnoEmissao(final Long anoEmissao) {
		this.anoEmissao = anoEmissao;
	}

	public void setClassificacaoSelId(final Long classificacaoSelId) {
		this.classificacaoSelId = classificacaoSelId;
	}

	public void setDescrDocumento(final String descrDocumento) {
		this.descrDocumento = descrDocumento;
	}

	public String getFullText() {
		return fullText;
	}

	public void setFullText(String fullText) {
		this.fullText = fullText;
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

	public void setLotacaoDestinatarioSelId(final Long lotacaoDestinatarioSelId) {
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

	public Long getId() {
		return getId();
	}

	public Integer getNumSequencia() {
		return numSequencia;
	}

	public void setNumSequencia(final Integer numSequencia) {
		this.numSequencia = numSequencia;
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

	public String getNumAntigoDoc() {
		return numAntigoDoc;
	}

	public void setNumAntigoDoc(String numAntigoDoc) {
		this.numAntigoDoc = numAntigoDoc;
	}

	public Long getLotacaoCadastranteAtualId() {
		return lotacaoCadastranteAtualId;
	}

	public void setLotacaoCadastranteAtualId(Long lotacaoCadastranteAtualId) {
		this.lotacaoCadastranteAtualId = lotacaoCadastranteAtualId;
	}

	public Long getIdOrgaoUsu() {
		return idOrgaoUsu;
	}

	public void setIdOrgaoUsu(Long idOrgaoUsu) {
		this.idOrgaoUsu = idOrgaoUsu;
	}

	public Long getCadastranteSelId() {
		return cadastranteSelId;
	}

	public void setCadastranteSelId(Long cadastranteSelId) {
		this.cadastranteSelId = cadastranteSelId;
	}

	public Long getLotaCadastranteSelId() {
		return lotaCadastranteSelId;
	}

	public void setLotaCadastranteSelId(Long lotaCadastranteSelId) {
		this.lotaCadastranteSelId = lotaCadastranteSelId;
	}

	public Date getDtDocFinal() {
		return dtDocFinal;
	}

	public void setDtDocFinal(Date dtDocFinal) {
		this.dtDocFinal = dtDocFinal;
	}

	public Long getIdTipoMobil() {
		return idTipoMobil;
	}

	public void setIdTipoMobil(Long idTipoMobil) {
		this.idTipoMobil = idTipoMobil;
	}

	public Long getIdTipoFormaDoc() {
		return idTipoFormaDoc;
	}

	public void setIdTipoFormaDoc(Long idTipoFormaDoc) {
		this.idTipoFormaDoc = idTipoFormaDoc;
	}

	public Long getIdMod() {
		return idMod;
	}

	public void setIdMod(Long idMod) {
		this.idMod = idMod;
	}

	public boolean buscarPorCamposMarca() {
		return (getUltMovIdEstadoDoc() != null && getUltMovIdEstadoDoc() != 0)
				|| (getUltMovLotaRespSelId() != null && getUltMovLotaRespSelId() != 0)
				|| (getUltMovRespSelId() != null && ultMovRespSelId != 0);
	}

	public boolean buscarPorCamposDoc() {
		return (getDescrDocumento() != null && !getDescrDocumento().trim()
				.equals(""))
				|| (getFullText() != null && !getFullText().trim().equals(""))
				|| (getDtDoc() != null)
				|| (getDtDocFinal() != null)
				|| (getIdTpDoc() != null && getIdTpDoc() != 0)
				|| (getIdFormaDoc() != null && getIdFormaDoc() != 0)
				|| (getIdTipoFormaDoc() != null && getIdTipoFormaDoc() != 0)
				|| (getClassificacaoSelId() != null && getClassificacaoSelId() != 0)
				|| (getNumAntigoDoc() != null && !getNumAntigoDoc().trim()
						.equals(""))
				|| (getDestinatarioSelId() != null && getDestinatarioSelId() != 0)
				|| (getLotacaoDestinatarioSelId() != null && getLotacaoDestinatarioSelId() != 0)
				|| (getNmDestinatario() != null && !getNmDestinatario().trim()
						.equals(""))
				|| (getCadastranteSelId() != null && getCadastranteSelId() != 0)
				|| (getLotaCadastranteSelId() != null && getLotaCadastranteSelId() != 0)
				|| (getSubscritorSelId() != null && getSubscritorSelId() != 0)
				|| (getNmSubscritorExt() != null && !getNmSubscritorExt()
						.equals(""))
				|| (getOrgaoExternoSelId() != null && getOrgaoExternoSelId() != 0)
				|| (getNumExtDoc() != null && !getNumExtDoc().trim().equals(""))
				|| (getIdMod() != null && getIdMod() != 0)
				|| (getIdOrgaoUsu() != null && getIdOrgaoUsu() != 0)
				|| (getAnoEmissao() != null && getAnoEmissao() != 0)
				|| (getNumExpediente() != null && getNumExpediente() != 0);
	}
}
