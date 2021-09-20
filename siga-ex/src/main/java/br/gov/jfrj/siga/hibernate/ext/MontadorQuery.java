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

	public void setMontadorPrincipal(IMontadorQuery montadorQueryPrincipal) {
		// Este médodo não faz nada. É utilizado apenas para a extensão da busca
		// textual do SIGA.
	}
}