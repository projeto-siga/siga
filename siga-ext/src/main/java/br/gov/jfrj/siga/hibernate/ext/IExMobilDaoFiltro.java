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
package br.gov.jfrj.siga.hibernate.ext;

import java.util.Date;

public interface IExMobilDaoFiltro {

	public abstract void setSigla(final String nome);

	public abstract Long getAnoEmissao();

	public abstract Long getClassificacaoSelId();

	public abstract String getDescrDocumento();

	public abstract Long getDestinatarioSelId();

	public abstract Long getIdFormaDoc();

	public abstract Integer getIdTpDoc();

	public abstract Long getLotacaoDestinatarioSelId();

	public abstract String getNmDestinatario();

	public abstract String getNmSubscritorExt();

	public abstract Long getNumExpediente();

	public abstract String getNumExtDoc();

	public abstract Long getOrgaoExternoDestinatarioSelId();

	public abstract Long getSubscritorSelId();

	public abstract Long getUltMovIdEstadoDoc();

	public abstract Long getUltMovLotaRespSelId();

	public abstract Long getUltMovRespSelId();

	public abstract void setAnoEmissao(final Long anoEmissao);

	public abstract void setClassificacaoSelId(final Long classificacaoSelId);

	public abstract void setDescrDocumento(final String descrDocumento);

	public abstract String getFullText();

	public abstract void setFullText(String fullText);

	public abstract void setDestinatarioSelId(final Long destinatarioSelId);

	public abstract void setIdFormaDoc(final Long idFormaDoc);

	public abstract void setIdTpDoc(final Integer idTpDoc);

	public abstract void setLotacaoDestinatarioSelId(
			final Long lotacaoDestinatarioSelId);

	public abstract void setNmDestinatario(final String nmDestinatario);

	public abstract void setNmSubscritorExt(final String nmSubscritorExt);

	public abstract void setNumExpediente(final Long numExpediente);

	public abstract void setNumExtDoc(final String numExtDoc);

	public abstract void setOrgaoExternoDestinatarioSelId(
			final Long orgaoExternoDestinatarioSelId);

	public abstract void setSubscritorSelId(final Long subscritorSelId);

	public abstract void setUltMovIdEstadoDoc(final Long ultMovIdEstadoDoc);

	public abstract void setUltMovLotaRespSelId(final Long ultMovLotaRespSelId);

	public abstract void setUltMovRespSelId(final Long ultMovRespSelId);

	public abstract Long getId();

	public abstract Integer getNumSequencia();

	public abstract void setNumSequencia(final Integer numSequencia);

	public abstract Long getIdDoc();

	public abstract void setIdDoc(final Long idDoc);

	public abstract Long getOrgaoExternoSelId();

	public abstract void setOrgaoExternoSelId(final Long orgaoExternoSelId);

	public abstract Date getDtDoc();

	public abstract void setDtDoc(final Date dtDoc);

	public abstract String getNumAntigoDoc();

	public abstract void setNumAntigoDoc(String numAntigoDoc);

	public abstract Long getLotacaoCadastranteAtualId();

	public abstract void setLotacaoCadastranteAtualId(
			Long lotacaoCadastranteAtualId);

	public abstract Long getIdOrgaoUsu();

	public abstract void setIdOrgaoUsu(Long idOrgaoUsu);

	public abstract Long getCadastranteSelId();

	public abstract void setCadastranteSelId(Long cadastranteSelId);

	public abstract Long getLotaCadastranteSelId();

	public abstract void setLotaCadastranteSelId(Long lotaCadastranteSelId);

	public abstract Date getDtDocFinal();

	public abstract void setDtDocFinal(Date dtDocFinal);

	public abstract Long getIdTipoMobil();

	public abstract void setIdTipoMobil(Long idTipoMobil);

	public abstract Long getIdTipoFormaDoc();

	public abstract void setIdTipoFormaDoc(Long idTipoFormaDoc);

	public abstract Long getIdMod();

	public abstract void setIdMod(Long idMod);

	public abstract void setOrdem(Integer ordem);

	public abstract Integer getOrdem();
	
	public abstract boolean buscarPorCamposMarca();

	public abstract boolean buscarPorCamposDoc();
}