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

import java.text.SimpleDateFormat;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class MontadorQuery implements IMontadorQuery {

	public String montaQueryConsultaporFiltro(final IExMobilDaoFiltro flt,
			DpPessoa titular, DpLotacao lotaTitular, boolean apenasCount) {

		StringBuffer sbf = new StringBuffer();

		if (apenasCount)
			sbf
					.append("select count(doc) from ExMarca label inner join label.exMobil mob inner join label.exMobil.exDocumento doc");
		else
			sbf
					.append("select doc, mob, label from ExMarca label inner join label.exMobil mob inner join label.exMobil.exDocumento doc");

		sbf.append(" where 1 = 1");

		if (flt.getUltMovIdEstadoDoc() != null
				&& flt.getUltMovIdEstadoDoc() != 0) {
			sbf.append(" and label.cpMarcador.idMarcador = ");
			sbf.append(flt.getUltMovIdEstadoDoc());
		} else {
			sbf.append(" and not (label.cpMarcador.idMarcador in (3, 14, 25))");
		}

		if (flt.getUltMovRespSelId() != null && flt.getUltMovRespSelId() != 0) {
			sbf.append(" and label.dpPessoaIni.idPessoa = ");
			sbf.append(flt.getUltMovRespSelId());
		}

		if (flt.getUltMovLotaRespSelId() != null
				&& flt.getUltMovLotaRespSelId() != 0) {
			sbf.append(" and label.dpLotacaoIni.idLotacao = ");
			sbf.append(flt.getUltMovLotaRespSelId());
		}

		if (flt.getIdTipoMobil() != null && flt.getIdTipoMobil() != 0) {
			sbf.append(" and mob.exTipoMobil.idTipoMobil = ");
			sbf.append(flt.getIdTipoMobil());
		}

		if (flt.getNumSequencia() != null && flt.getNumSequencia() != 0) {
			sbf.append(" and mob.numSequencia = ");
			sbf.append(flt.getNumSequencia());
		}

		if (flt.getIdOrgaoUsu() != null && flt.getIdOrgaoUsu() != 0) {
			sbf.append(" and doc.orgaoUsuario.idOrgaoUsu = ");
			sbf.append(flt.getIdOrgaoUsu());
		}

		if (flt.getAnoEmissao() != null && flt.getAnoEmissao() != 0) {
			sbf.append(" and doc.anoEmissao = ");
			sbf.append(flt.getAnoEmissao());
		}

		if (flt.getNumExpediente() != null && flt.getNumExpediente() != 0) {
			sbf.append(" and doc.numExpediente = ");
			sbf.append(flt.getNumExpediente());
		}

		if (flt.getIdTpDoc() != null && flt.getIdTpDoc() != 0) {
			sbf.append(" and doc.exTipoDocumento.idTpDoc = ");
			sbf.append(flt.getIdTpDoc());
		}

		if (flt.getIdFormaDoc() != null && flt.getIdFormaDoc() != 0) {
			sbf.append(" and doc.exFormaDocumento.idFormaDoc = ");
			sbf.append(flt.getIdFormaDoc());
		}

		if (flt.getIdTipoFormaDoc() != null && flt.getIdTipoFormaDoc() != 0) {
			sbf
					.append(" and doc.exFormaDocumento.exTipoFormaDoc.idTipoFormaDoc = ");
			sbf.append(flt.getIdTipoFormaDoc());
		}

		if (flt.getClassificacaoSelId() != null
				&& flt.getClassificacaoSelId() != 0) {
			sbf.append(" and doc.exClassificacao.idClassificacao = ");
			sbf.append(flt.getClassificacaoSelId());
		}

		if (flt.getDescrDocumento() != null
				&& !flt.getDescrDocumento().trim().equals("")) {
			sbf.append(" and upper(doc.descrDocumentoAI) like ");
			sbf.append("'%" + flt.getDescrDocumento().toUpperCase() + "%'");
		}

//		if (flt.getFullText() != null && !flt.getFullText().trim().equals("")) {
//			String s = flt.getFullText();
//			while (s.contains("  "))
//				s = s.replace("  ", " ");
//			s = s.replaceAll(" ", " AND ");
//			sbf.append(" and CONTAINS(conteudo_blob_doc, '");
//			sbf.append(s);
//			sbf.append("', 1) > 0");
//		}

		if (flt.getDtDoc() != null) {
			sbf.append(" and doc.dtDoc >= to_date('");
			sbf.append(new SimpleDateFormat("dd/MM/yyyy")
					.format(flt.getDtDoc()));
			sbf.append("', 'dd/mm/yyyy')");
		}

		if (flt.getDtDocFinal() != null) {
			sbf.append(" and doc.dtDoc <= to_date('");
			sbf.append(new SimpleDateFormat("dd/MM/yyyy").format(flt
					.getDtDocFinal()));
			sbf.append("', 'dd/mm/yyyy')");
		}

		if (flt.getNumAntigoDoc() != null
				&& !flt.getNumAntigoDoc().trim().equals("")) {
			sbf.append(" and upper(doc.numAntigoDoc) like ");
			sbf.append("'%" + flt.getNumAntigoDoc().toUpperCase() + "%'");
		}

		if (flt.getDestinatarioSelId() != null
				&& flt.getDestinatarioSelId() != 0) {
			sbf.append(" and doc.destinatario.idPessoaIni = ");
			sbf.append(flt.getDestinatarioSelId());
		}

		if (flt.getLotacaoDestinatarioSelId() != null
				&& flt.getLotacaoDestinatarioSelId() != 0) {
			sbf.append(" and doc.lotaDestinatario.idLotacaoIni = ");
			sbf.append(flt.getLotacaoDestinatarioSelId());
		}

		if (flt.getNmDestinatario() != null
				&& !flt.getNmDestinatario().trim().equals("")) {
			sbf.append(" and upper(doc.nmDestinatario) like ");
			sbf.append("'%" + flt.getNmDestinatario().toUpperCase() + "%'");
		}

		if (flt.getCadastranteSelId() != null && flt.getCadastranteSelId() != 0) {
			sbf.append(" and doc.cadastrante.idPessoaIni =  ");
			sbf.append(flt.getCadastranteSelId());
		}

		if (flt.getLotaCadastranteSelId() != null
				&& flt.getLotaCadastranteSelId() != 0) {
			sbf.append(" and doc.lotaCadastrante.idLotacaoIni = ");
			sbf.append(flt.getLotaCadastranteSelId());
		}

		if (flt.getSubscritorSelId() != null && flt.getSubscritorSelId() != 0) {
			sbf.append(" and doc.subscritor.idPessoaIni = ");
			sbf.append(flt.getSubscritorSelId());
		}

		if (flt.getNmSubscritorExt() != null
				&& !flt.getNmSubscritorExt().trim().equals("")) {
			sbf.append(" and upper(doc.nmSubscritorExt) like ");
			sbf.append("'%" + flt.getNmSubscritorExt().toUpperCase() + "%'");
		}

		if (flt.getOrgaoExternoSelId() != null
				&& flt.getOrgaoExternoSelId() != 0) {
			sbf.append(" and doc.orgaoExterno.idOrgao = ");
			sbf.append(flt.getOrgaoExternoSelId());
		}

		if (flt.getNumExtDoc() != null && !flt.getNumExtDoc().trim().equals("")) {
			sbf.append(" and upper(doc.numExtDoc) like ");
			sbf.append("'%" + flt.getNumExtDoc() + "%'");
		}

		if (flt.getIdMod() != null && flt.getIdMod() != 0) {
			sbf.append(" and doc.exModelo.idMod = ");
			sbf.append(flt.getIdMod());
		}

		if (!apenasCount)
			sbf.append(" order by doc.idDoc desc");

		return sbf.toString();

	}

	public void setMontadorPrincipal(IMontadorQuery montadorQueryPrincipal) {
		//Este médodo não faz nada. É utilizado apenas para a extensão da busca textual do SIGA.
	}

}
