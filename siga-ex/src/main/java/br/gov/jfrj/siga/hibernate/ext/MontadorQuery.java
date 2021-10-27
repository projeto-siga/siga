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

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.dp.CpMarcador;

public class MontadorQuery implements IMontadorQuery {

	public String montaQueryConsultaporFiltro(final IExMobilDaoFiltro flt, boolean apenasCount) {

		StringBuffer sbf = new StringBuffer();

		if (apenasCount)
			sbf.append("select count(1) from ExMarca label  inner join label.cpMarcador marcador inner join label.exMobil mob inner join mob.exDocumento doc");
		else
			sbf.append("select label.idMarca from ExMarca label inner join label.cpMarcador marcador inner join label.exMobil mob inner join mob.exDocumento doc");

		//Nato: desabilitei este where pois causava muito impacto na velocidade da consulta. Precisamos criar uma variavel denormalizada mais a frente para resolver esse problema.
		//sbf.append(" where not exists (from ExMovimentacao where exTipoMovimentacao.idTpMov = 10 and (exMobil.idMobil = mob.idMobil ");
		//sbf.append("    or exMobil.idMobil = (from ExMobil where exTipoMobil.idTipoMobil = 1 and exDocumento.idDoc = mob.exDocumento.idDoc)))");
		if (flt.getIdMod() != null && flt.getIdMod() != 0) {
			sbf.append(" INNER JOIN doc.exModelo exMod ");
		}
		
		sbf.append(" where");

		if (flt.getUltMovIdEstadoDoc() != null	&& flt.getUltMovIdEstadoDoc() != 0) {
			sbf.append(" and marcador.hisIdIni = :idMarcadorIni");
			sbf.append(" and (dt_ini_marca is null or dt_ini_marca < :dbDatetime)");
			sbf.append(" and (dt_fim_marca is null or dt_fim_marca > :dbDatetime)");
		} else {
			sbf.append(" and marcador.listavelPesquisaDefault = 1");
		}

		if (flt.getUltMovRespSelId() != null && flt.getUltMovRespSelId() != 0) {
			sbf.append(" and label.dpPessoaIni.idPessoa = :ultMovRespSelId");
		}

		if (flt.getUltMovLotaRespSelId() != null
				&& flt.getUltMovLotaRespSelId() != 0) {
			sbf.append(" and label.dpLotacaoIni.idLotacao = :ultMovLotaRespSelId");
		}

		if (flt.getIdTipoMobil() != null && flt.getIdTipoMobil() != 0) {
			sbf.append(" and mob.exTipoMobil.idTipoMobil = :idTipoMobil");
		}

		if (flt.getNumSequencia() != null && flt.getNumSequencia() != 0) {
			sbf.append(" and mob.numSequencia = :numSequencia ");
		}

		if (flt.getIdOrgaoUsu() != null && flt.getIdOrgaoUsu() != 0) {
			sbf.append(" and doc.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu");
		}

		if (flt.getAnoEmissao() != null && flt.getAnoEmissao() != 0) {
			sbf.append(" and doc.anoEmissao = :anoEmissao");
		}

		if (flt.getNumExpediente() != null && flt.getNumExpediente() != 0) {
			sbf.append(" and doc.numExpediente = :numExpediente");
		}

		if (flt.getIdTpDoc() != null && flt.getIdTpDoc() != 0) {
			sbf.append(" and doc.exTipoDocumento.idTpDoc = :idTpDoc");
		}

		if (flt.getIdFormaDoc() != null && flt.getIdFormaDoc() != 0) {
			sbf.append(" and doc.exFormaDocumento.idFormaDoc = :idFormaDoc");
		}

		if (flt.getIdTipoFormaDoc() != null && flt.getIdTipoFormaDoc() != 0) {
			sbf.append(" and doc.exFormaDocumento.exTipoFormaDoc.idTipoFormaDoc = :idTipoFormaDoc");
		}

		if (flt.getClassificacaoSelId() != null
				&& flt.getClassificacaoSelId() != 0) {
			sbf.append(" and doc.exClassificacao.hisIdIni = :classificacaoSelId");
		}

		if (flt.getDescrDocumento() != null
				&& !flt.getDescrDocumento().trim().equals("")) {
			sbf.append(" and doc.descrDocumentoAI like :descrDocumento");
		}

		// if (flt.getFullText() != null &&
		// !flt.getFullText().trim().equals("")) {
		// String s = flt.getFullText();
		// while (s.contains("  "))
		// s = s.replace("  ", " ");
		// s = s.replaceAll(" ", " AND ");
		// sbf.append(" and CONTAINS(conteudo_blob_doc, '");
		// sbf.append(s);
		// sbf.append("', 1) > 0");
		// }

		if (flt.getDtDoc() != null) {
			if (((Long)CpMarcadorEnum.EM_ELABORACAO.getId()).equals(flt.getUltMovIdEstadoDoc())) { 
				sbf.append(" and doc.dtRegDoc >= ");
			} else {
				sbf.append(" and doc.dtDoc >= ");
			}
			sbf.append(":dtDoc");
		}

		if (flt.getDtDocFinal() != null) {
			if (((Long)CpMarcadorEnum.EM_ELABORACAO.getId()).equals(flt.getUltMovIdEstadoDoc())) { 
				sbf.append(" and doc.dtRegDoc <= ");
			} else {
				sbf.append(" and doc.dtDoc <= ");
			}
			sbf.append(":dtDocFinal");
		}

		if (flt.getNumAntigoDoc() != null
				&& !flt.getNumAntigoDoc().trim().equals("")) {
			sbf.append(" and upper(doc.numAntigoDoc) like :numAntigoDoc");
		}

		if (flt.getDestinatarioSelId() != null
				&& flt.getDestinatarioSelId() != 0) {
			sbf.append(" and doc.destinatario.idPessoaIni = :destinatarioSelId");
		}

		if (flt.getLotacaoDestinatarioSelId() != null
				&& flt.getLotacaoDestinatarioSelId() != 0) {
			sbf.append(" and doc.lotaDestinatario.idLotacaoIni = :lotacaoDestinatarioSelId");
		}

		if (flt.getNmDestinatario() != null
				&& !flt.getNmDestinatario().trim().equals("")) {
			sbf.append(" and upper(doc.nmDestinatario) like :nmDestinatario");
		}
		
		if (flt.getOrgaoExternoDestinatarioSelId() != null
				&& flt.getOrgaoExternoDestinatarioSelId() != 0) {
			sbf.append(" and doc.orgaoExternoDestinatario.idOrgao = :orgaoExternoDestinatarioSelId");
		}

		if (flt.getCadastranteSelId() != null && flt.getCadastranteSelId() != 0) {
			sbf.append(" and doc.cadastrante.idPessoaIni = :cadastranteSelId");
		}

		if (flt.getLotaCadastranteSelId() != null
				&& flt.getLotaCadastranteSelId() != 0) {
			sbf.append(" and doc.lotaCadastrante.idLotacaoIni = :lotaCadastranteSelId");
 		}

		if (flt.getSubscritorSelId() != null && flt.getSubscritorSelId() != 0) {
			sbf.append(" and doc.subscritor.idPessoaIni = :subscritorSelId");
		}

		if (flt.getNmSubscritorExt() != null
				&& !flt.getNmSubscritorExt().trim().equals("")) {
			sbf.append(" and upper(doc.nmSubscritorExt) like :nmSubscritorExt");
		}

		if (flt.getOrgaoExternoSelId() != null
				&& flt.getOrgaoExternoSelId() != 0) {
			sbf.append(" and doc.orgaoExterno.idOrgao = :orgaoExternoSelId");
		}

		if (flt.getNumExtDoc() != null && !flt.getNumExtDoc().trim().equals("")) {
			sbf.append(" and upper(doc.numExtDoc) like :numExtDoc");
		}

		if (flt.getIdMod() != null && flt.getIdMod() != 0) {
			sbf.append(" and exMod.hisIdIni = :hisIdIni");
		}

		if (!apenasCount) {
			if (flt.getOrdem() == null || flt.getOrdem() == 0)
				sbf.append(" order by doc.dtDoc desc, doc.idDoc desc");
			else if (flt.getOrdem() == 1)
				sbf.append(" order by label.dtIniMarca desc, doc.idDoc desc");
			else if (flt.getOrdem() == 2)
				sbf.append(" order by doc.anoEmissao desc, doc.numExpediente desc, mob.numSequencia, doc.idDoc desc");
			else if (flt.getOrdem() == 3)
				sbf.append(" order by doc.dtFinalizacao desc, doc.idDoc desc");
			else if (flt.getOrdem() == 4)
				sbf.append(" order by doc.idDoc desc");
		}
		
		String s = sbf.toString();
		s = s.replace("where and", "where");

		return s;

	}
	
	public String montaNativeQueryConsultaporFiltro(final IExMobilDaoFiltro flt, boolean apenasCount) {

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
		s = s.replace("where and", "where");

		return s;

	}

	public void setMontadorPrincipal(IMontadorQuery montadorQueryPrincipal) {
		// Este médodo não faz nada. É utilizado apenas para a extensão da busca
		// textual do SIGA.
	}
}