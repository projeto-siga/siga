package br.gov.jfrj.siga.hibernate.ext;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;

public class MontadorQueryNative implements IMontadorQuery {
	
	public String montaQueryConsultaporFiltro(final IExMobilDaoFiltro flt, boolean apenasCount) {

		StringBuffer sbf = new StringBuffer();

		if (apenasCount) {
			sbf.append("SELECT COUNT(1) "
					+ "	FROM CORPORATIVO.CP_MARCA MAR "
					+ " INNER JOIN CORPORATIVO.CP_MARCADOR MARCADOR ON MAR.ID_MARCADOR = MARCADOR.ID_MARCADOR "
					+ " INNER JOIN SIGA.EX_MOBIL MOB ON MAR.ID_REF = MOB.ID_MOBIL "
					+ " INNER JOIN SIGA.EX_DOCUMENTO DOC ON MOB.ID_DOC = DOC.ID_DOC ");
		} else {
			sbf.append("SELECT MAR.ID_MARCA "
					+ "	FROM CORPORATIVO.CP_MARCA MAR "
					+ " INNER JOIN CORPORATIVO.CP_MARCADOR MARCADOR ON MAR.ID_MARCADOR = MARCADOR.ID_MARCADOR "
					+ " INNER JOIN SIGA.EX_MOBIL MOB ON MAR.ID_REF = MOB.ID_MOBIL "
					+ " INNER JOIN SIGA.EX_DOCUMENTO DOC ON MOB.ID_DOC = DOC.ID_DOC ");
		}
		
		if (flt.getIdMod() != null && flt.getIdMod() != 0) {
			sbf.append(" INNER JOIN SIGA.EX_MODELO MODELO ON DOC.ID_MOD = MODELO.ID_MOD ");
		}
		
		//Analisar com Newton
		if (flt.getIdTipoFormaDoc() != null && flt.getIdTipoFormaDoc() != 0) {
			sbf.append(" INNER JOIN SIGA.EX_FORMA_DOCUMENTO FRM ON DOC.ID_FORMA_DOC = FRM.ID_FORMA_DOC ");
		}
		
		if (flt.getClassificacaoSelId() != null
				&& flt.getClassificacaoSelId() != 0) {
			sbf.append(" INNER JOIN SIGA.EX_CLASSIFICACAO CLASSIFICACAO "
					+ "			ON DOC.ID_CLASSIFICACAO = CLASSIFICACAO.ID_CLASSIFICACAO ");
		}
		
		sbf.append(" WHERE ");

		if (flt.getUltMovIdEstadoDoc() != null	&& flt.getUltMovIdEstadoDoc() != 0) {
			sbf.append(" AND MARCADOR.HIS_ID_INI = :idMarcadorIni ");
			sbf.append(" AND ( MAR.DT_INI_MARCA IS NULL OR MAR.DT_INI_MARCA < :dbDatetime ) ");
			sbf.append(" AND ( MAR.DT_FIM_MARCA IS NULL OR MAR.DT_FIM_MARCA > :dbDatetime ) ");
		} else {
			sbf.append(" AND MARCADOR.LISTAVEL_PESQUISA_DEFAULT = 1 ");
		}

		
		
		if (flt.getUltMovRespSelId() != null && flt.getUltMovRespSelId() != 0) {
			sbf.append(" AND MAR.ID_PESSOA_INI = :ultMovRespSelId");
		}

		if (flt.getUltMovLotaRespSelId() != null
				&& flt.getUltMovLotaRespSelId() != 0) {
			sbf.append(" AND MAR.ID_LOTACAO_INI = = :ultMovLotaRespSelId");
		}

		
		
		if (flt.getIdTipoMobil() != null && flt.getIdTipoMobil() != 0) {
			sbf.append(" AND MOB.ID_TIPO_MOBIL = :idTipoMobil");
		}

		if (flt.getNumSequencia() != null && flt.getNumSequencia() != 0) {
			sbf.append(" AND MOB.NUM_SEQUENCIA = :numSequencia ");
		}

		
		
		if (flt.getIdOrgaoUsu() != null && flt.getIdOrgaoUsu() != 0) {
			sbf.append(" AND DOC.ID_ORGAO_USU = :idOrgaoUsu");
		}

		if (flt.getAnoEmissao() != null && flt.getAnoEmissao() != 0) {
			sbf.append(" AND DOC.ANO_EMISSAO = :anoEmissao");
		}

		if (flt.getNumExpediente() != null && flt.getNumExpediente() != 0) {
			sbf.append(" AND DOC.NUM_EXPEDIENTE = :numExpediente");
		}

		if (flt.getIdTpDoc() != null && flt.getIdTpDoc() != 0) {
			sbf.append(" AND DOC.ID_TP_DOC = :idTpDoc");
		}

		if (flt.getIdFormaDoc() != null && flt.getIdFormaDoc() != 0) {
			sbf.append(" AND DOC.ID_FORMA_DOC = :idFormaDoc");
		}
		
		if (flt.getDescrDocumento() != null
				&& !flt.getDescrDocumento().trim().equals("")) {
			sbf.append(" AND DOC.DESCR_DOCUMENTO_AI like :descrDocumento");
		}
		
		if (flt.getDtDoc() != null) {
			if (((Long)CpMarcadorEnum.EM_ELABORACAO.getId()).equals(flt.getUltMovIdEstadoDoc())) { 
				sbf.append(" AND DOC.DT_REG_DOC >= ");
			} else {
				sbf.append(" AND DOC.DT_DOC >= ");
			}
			sbf.append(":dtDoc");
		}
		
		if (flt.getDtDocFinal() != null) {
			if (((Long)CpMarcadorEnum.EM_ELABORACAO.getId()).equals(flt.getUltMovIdEstadoDoc())) { 
				sbf.append(" AND DOC.DT_REG_DOC <= ");
			} else {
				sbf.append(" AND DOC.DT_DOC <= ");
			}
			sbf.append(":dtDocFinal");
		}
		
		if (flt.getNumAntigoDoc() != null
				&& !flt.getNumAntigoDoc().trim().equals("")) {
			sbf.append(" AND UPPER(DOC.NUM_ANTIGO_DOC) like :numAntigoDoc");
		}
		
		if (flt.getDestinatarioSelId() != null
				&& flt.getDestinatarioSelId() != 0) {
			sbf.append(" AND DOC.ID_DESTINATARIO = :destinatarioSelId");
		}
		
		if (flt.getLotacaoDestinatarioSelId() != null
				&& flt.getLotacaoDestinatarioSelId() != 0) {
			sbf.append(" AND DOC.ID_LOTA_DESTINATARIO = :lotacaoDestinatarioSelId");
		}
		
		if (flt.getNmDestinatario() != null
				&& !flt.getNmDestinatario().trim().equals("")) {
			sbf.append(" AND UPPER(DOC.NM_DESTINATARIO) like :nmDestinatario");
		}
		
		if (flt.getOrgaoExternoDestinatarioSelId() != null
				&& flt.getOrgaoExternoDestinatarioSelId() != 0) {
			sbf.append(" AND DOC.ID_ORGAO_DESTINATARIO = :orgaoExternoDestinatarioSelId");
		}
		
		if (flt.getCadastranteSelId() != null && flt.getCadastranteSelId() != 0) {
			sbf.append(" AND DOC.ID_CADASTRANTE = :cadastranteSelId");
		}
		
		if (flt.getLotaCadastranteSelId() != null
				&& flt.getLotaCadastranteSelId() != 0) {
			sbf.append(" AND DOC.ID_LOTA_CADASTRANTE = :lotaCadastranteSelId");
 		}
		
		if (flt.getSubscritorSelId() != null && flt.getSubscritorSelId() != 0) {
			sbf.append(" AND DOC.ID_SUBSCRITOR = :subscritorSelId");
		}
		
		if (flt.getNmSubscritorExt() != null
				&& !flt.getNmSubscritorExt().trim().equals("")) {
			sbf.append(" AND UPPER(DOC.NM_SUBSCRITOR_EXT) like :nmSubscritorExt");
		}
		
		if (flt.getOrgaoExternoSelId() != null
				&& flt.getOrgaoExternoSelId() != 0) {
			sbf.append(" AND DOC.ID_ORGAO = :orgaoExternoSelId");
		}
		
		if (flt.getNumExtDoc() != null && !flt.getNumExtDoc().trim().equals("")) {
			sbf.append(" AND UPPER(DOC.NUM_EXT_DOC) like :numExtDoc");
		}
		
		

		if (flt.getIdTipoFormaDoc() != null && flt.getIdTipoFormaDoc() != 0) {
			sbf.append(" AND FRM.ID_FORMA_DOC= :idTipoFormaDoc");
		}

		if (flt.getClassificacaoSelId() != null
				&& flt.getClassificacaoSelId() != 0) {
			sbf.append(" AND CLASSIFICACAO.HIS_ID_INI = :classificacaoSelId");
		}

		if (flt.getIdMod() != null && flt.getIdMod() != 0) {
			sbf.append(" AND MODELO.HIS_ID_INI = :hisIdIni");
		}

		if (!apenasCount) {
			if (flt.getOrdem() == null || flt.getOrdem() == 0)
				sbf.append(" ORDER BY DOC.DT_DOC DESC, DOC.ID_DOC DESC"); 
			else if (flt.getOrdem() == 1)
				sbf.append(" ORDER BY MAR.DT_INI_MARCA DESC, DOC.ID_DOC DESC");
			else if (flt.getOrdem() == 2)
				sbf.append(" ORDER BY DOC.ANO_EMISSAO DESC, DOC.NUM_EXPEDIENTE DESC, MOB.NUM_SEQUENCIA, DOC.ID_DOC DESC");
			else if (flt.getOrdem() == 3)
				sbf.append(" ORDER BY DOC.DT_FINALIZACAO DESC, DOC.ID_DOC DESC");
			else if (flt.getOrdem() == 4)
				sbf.append(" ORDER BY DOC.ID_DOC DESC");
		}
		
		String s = sbf.toString();
		s = s.replace("WHERE  AND", "WHERE");

		return s;
	}
	
	public void setMontadorPrincipal(IMontadorQuery montadorQueryPrincipal) {
		// Este médodo não faz nada. É utilizado apenas para a extensão da busca
		// textual do SIGA.
	}

}
