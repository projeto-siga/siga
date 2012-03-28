package br.gov.jfrj.siga.hibernate.ext;

import java.util.Date;

public interface IExMobilDaoFiltro {

	public abstract void setSigla(final String nome);

	public abstract Long getAnoEmissao();

	public abstract Long getClassificacaoSelId();

	public abstract String getDescrDocumento();

	public abstract Long getDestinatarioSelId();

	public abstract Integer getIdFormaDoc();

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

	public abstract void setIdFormaDoc(final Integer idFormaDoc);

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

	public abstract boolean buscarPorCamposMarca();

	public abstract boolean buscarPorCamposDoc();

}