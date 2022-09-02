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
import br.gov.jfrj.siga.hibernate.query.ext.IExMobilDaoFiltro;
import br.gov.jfrj.siga.hibernate.query.ext.IMontadorQuery;

public class MontadorQueryNative implements IMontadorQuery {

	public String montaQueryConsultaporFiltro(final IExMobilDaoFiltro flt, boolean apenasCount) {

		StringBuffer sbf = new StringBuffer();
		sbf.append("select ");
		sbf.append(" /*+ first_rows leading(doc mob label marcador) index (doc, IACS_EX_DOCUMENTO_00002) */ ");
		sbf.append((apenasCount  ? " count(1) " : " label.id_marca ") + " from corporativo.cp_marca label ");
		sbf.append("inner join corporativo.cp_marcador marcador on marcador.id_marcador = label.id_marcador ");
		sbf.append("inner join siga.ex_mobil mob on mob.id_mobil = label.id_ref inner join siga.ex_documento doc on doc.id_doc =  mob.id_doc ");


		/****** Tabelas Adicionais ******/
		if (flt.getIdTipoFormaDoc() != null && flt.getIdTipoFormaDoc() != 0) {
			sbf.append(" inner join siga.ex_forma_documento forma on forma.id_forma_doc = doc.id_forma_doc ");
		}
		
		if (flt.getClassificacaoSelId() != null && flt.getClassificacaoSelId() != 0) {
			sbf.append(" inner join siga.ex_classificacao classificacao on classificacao.id_classificacao = doc.id_classificacao ");
		}
		
		if (flt.getDestinatarioSelId() != null && flt.getDestinatarioSelId() != 0) {
			sbf.append(" inner join corporativo.dp_pessoa destinatario on destinatario.id_pessoa = doc.id_destinatario ");
		}
		
		if (flt.getLotacaoDestinatarioSelId() != null && flt.getLotacaoDestinatarioSelId() != 0) {
			sbf.append(" inner join corporativo.dp_lotacao lotaDestinatario on lotaDestinatario.id_lotacao = doc.id_lota_destinatario ");
		}
		
		if (flt.getCadastranteSelId() != null && flt.getCadastranteSelId() != 0) {
			sbf.append(" inner join corporativo.dp_pessoa cadastrante on cadastrante.id_pessoa = doc.id_cadastrante ");
		}
		
		if (flt.getLotaCadastranteSelId() != null && flt.getLotaCadastranteSelId() != 0) {
			sbf.append(" inner join corporativo.dp_lotacao lotaCadastrante on lotaCadastrante.id_lotacao = doc.id_lota_cadastrante ");
 		}
		
		if (flt.getSubscritorSelId() != null && flt.getSubscritorSelId() != 0) {
			sbf.append(" inner join corporativo.dp_pessoa subscritor on subscritor.id_pessoa = doc.id_subscritor ");
		}
		
		if (flt.getUltMovRespSelId() != null && flt.getUltMovRespSelId() != 0) {
			sbf.append(" inner join corporativo.dp_pessoa labelPessoa on labelPessoa.id_pessoa = label.id_pessoa_ini ");
		}
		
		if (flt.getUltMovLotaRespSelId() != null && flt.getUltMovLotaRespSelId() != 0) {
			sbf.append(" inner join corporativo.dp_lotacao labelLotacao on labelLotacao.id_lotacao = label.id_lotacao_ini");
		}
		
		if (flt.getIdMod() != null && flt.getIdMod() != 0) {
			sbf.append(" inner join siga.ex_modelo exMod on exMod.id_mod = doc.id_mod ");
		}
		
		sbf.append(" where");

		if (flt.getUltMovIdEstadoDoc() != null	&& flt.getUltMovIdEstadoDoc() != 0) {
			sbf.append(" and marcador.his_id_ini = :idMarcadorIni");
			sbf.append(" and (label.dt_ini_marca is null or label.dt_ini_marca < :dbDatetime)");
			sbf.append(" and (label.dt_fim_marca is null or label.dt_fim_marca > :dbDatetime)");
		} else {
			sbf.append(" and marcador.listavel_pesquisa_default = 1");
		}

		if (flt.getUltMovRespSelId() != null && flt.getUltMovRespSelId() != 0) {
			sbf.append(" and labelPessoa.id_pessoa = :ultMovRespSelId");
		}

		if (flt.getUltMovLotaRespSelId() != null && flt.getUltMovLotaRespSelId() != 0) {
			sbf.append(" and labelLotacao.id_lotacao = :ultMovLotaRespSelId");
		}

		if (flt.getIdTipoMobil() != null && flt.getIdTipoMobil() != 0) {
			sbf.append(" and mob.id_tipo_mobil = :idTipoMobil");
		}

		if (flt.getNumSequencia() != null && flt.getNumSequencia() != 0) {
			sbf.append(" and mob.num_sequencia = :numSequencia ");
		}

		if (flt.getIdOrgaoUsu() != null && flt.getIdOrgaoUsu() != 0) {
			sbf.append(" and doc.id_orgao_usu = :idOrgaoUsu");
		}

		if (flt.getAnoEmissao() != null && flt.getAnoEmissao() != 0) {
			sbf.append(" and doc.ano_emissao = :anoEmissao");
		}

		if (flt.getNumExpediente() != null && flt.getNumExpediente() != 0) {
			sbf.append(" and doc.num_expediente = :numExpediente");
		}

		if (flt.getIdTpDoc() != null && flt.getIdTpDoc() != 0) {
			sbf.append(" and doc.id_tp_doc = :idTpDoc");
		}

		if (flt.getIdFormaDoc() != null && flt.getIdFormaDoc() != 0) {
			sbf.append(" and doc.id_forma_doc = :idFormaDoc");
		}

		if (flt.getIdTipoFormaDoc() != null && flt.getIdTipoFormaDoc() != 0) {
			sbf.append(" and forma.id_tipo_forma_doc = :idTipoFormaDoc");
		}

		if (flt.getClassificacaoSelId() != null && flt.getClassificacaoSelId() != 0) {
			sbf.append(" and classificacao.his_id_ini = :classificacaoSelId");
		}

		if (flt.getDescrDocumento() != null && !flt.getDescrDocumento().trim().equals("") && flt.getListaIdDoc() == null) {
			sbf.append(" and doc.descr_documento_ai like :descrDocumento");
		}

		if (flt.getDtDoc() != null) {
			if (((Long)CpMarcadorEnum.EM_ELABORACAO.getId()).equals(flt.getUltMovIdEstadoDoc())) { 
				sbf.append(" and doc.dt_reg_doc >= ");
			} else {
				sbf.append(" and doc.dt_doc >= ");
			}
			sbf.append(":dtDoc");
		}

		if (flt.getDtDocFinal() != null) {
			if (((Long)CpMarcadorEnum.EM_ELABORACAO.getId()).equals(flt.getUltMovIdEstadoDoc())) { 
				sbf.append(" and doc.dt_reg_doc <= ");
			} else {
				sbf.append(" and doc.dt_doc <= ");
			}
			sbf.append(":dtDocFinal");
		}

		if (flt.getNumAntigoDoc() != null && !flt.getNumAntigoDoc().trim().equals("")) {
			sbf.append(" and upper(doc.num_antigo_doc) like :numAntigoDoc");
		}

		if (flt.getDestinatarioSelId() != null && flt.getDestinatarioSelId() != 0) {
			sbf.append(" and destinatario.id_pessoa_inicial = :destinatarioSelId");
		}
		
		if (flt.getLotacaoDestinatarioSelId() != null && flt.getLotacaoDestinatarioSelId() != 0) {
			sbf.append(" and lotaDestinatario.id_lotacao_ini = :lotacaoDestinatarioSelId");
		}

		if (flt.getNmDestinatario() != null && !flt.getNmDestinatario().trim().equals("")) {
			sbf.append(" and upper(doc.nm_destinatario) like :nmDestinatario");
		}
		
		if (flt.getOrgaoExternoDestinatarioSelId() != null && flt.getOrgaoExternoDestinatarioSelId() != 0) {
			sbf.append(" and doc.id_orgao_destinatario = :orgaoExternoDestinatarioSelId");
		}

		if (flt.getCadastranteSelId() != null && flt.getCadastranteSelId() != 0) {
			sbf.append(" and cadastrante.id_pessoa_inicial = :cadastranteSelId");
		}

		if (flt.getLotaCadastranteSelId() != null && flt.getLotaCadastranteSelId() != 0) {
			sbf.append(" and lotaCadastrante.id_lotacao_ini = :lotaCadastranteSelId");
 		}

		if (flt.getSubscritorSelId() != null && flt.getSubscritorSelId() != 0) {
			sbf.append(" and subscritor.id_pessoa_inicial = :subscritorSelId");
		}

		if (flt.getNmSubscritorExt() != null && !flt.getNmSubscritorExt().trim().equals("")) {
			sbf.append(" and upper(doc.nm_subscritor_ext) like :nmSubscritorExt");
		}

		if (flt.getOrgaoExternoSelId() != null && flt.getOrgaoExternoSelId() != 0) {
			sbf.append(" and doc.id_orgao = :orgaoExternoSelId");
		}

		if (flt.getNumExtDoc() != null && !flt.getNumExtDoc().trim().equals("")) {
			sbf.append(" and upper(doc.num_ext_doc) like :numExtDoc");
		}

		if (flt.getIdMod() != null && flt.getIdMod() != 0) {
			sbf.append(" and exMod.his_id_ini = :hisIdIni");
		}
		
		if(flt.getListaIdDoc() != null && !flt.getListaIdDoc().isEmpty()) {
			sbf.append(" and (");
			
			for(int i=0; i <= flt.getListaIdDoc().size()/1000; i++)
				sbf.append(" doc.id_doc IN (:listaIdDoc" + i + ") or");
			
			sbf.delete(sbf.length()-3, sbf.length()).append(")");
		}

		if (!apenasCount) {
			if (flt.getOrdem() == null || flt.getOrdem() == 0)
				sbf.append(" order by doc.dt_doc desc, doc.id_doc desc");
			else if (flt.getOrdem() == 3)
				sbf.append(" order by doc.dt_finalizacao desc, doc.id_doc desc");
			else if (flt.getOrdem() == 4)
				sbf.append(" order by doc.id_doc desc");
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