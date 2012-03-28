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
package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;

import org.hibernate.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class RelatorioDocumentosSubordinados extends RelatorioTemplate {

	public RelatorioDocumentosSubordinados(Map parametros)
			throws DJBuilderException {
		super(parametros);
		if (parametros.get("secaoUsuario") == null) {
			throw new DJBuilderException(
					"Parâmetro secaoUsuario não informado!");
		}
		if (parametros.get("link_siga") == null) {
			throw new DJBuilderException("Parâmetro link_siga não informado!");
		}

	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException {

		this.setTitle("Relatório de Documentos em Setores Subordinados");
		this.addColuna("Setor", 0, RelatorioRapido.ESQUERDA, true);
		this.addColuna("Documento", 25, RelatorioRapido.ESQUERDA, false, true);
		this.addColuna("Descrição", 50, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Responsável", 30, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Situação", 30, RelatorioRapido.ESQUERDA, false);
		return this;

	}

	public Collection processarDados() throws Exception {

		// Obtém uma formaDoc a partir da sigla passada e monta trecho da query
		// para a forma
		Query qryTipoForma = HibernateUtil.getSessao().createQuery(
				"from ExTipoFormaDoc tf where " + "tf.descTipoFormaDoc = '"
						+ parametros.get("tipoFormaDoc") + "'");
		ExTipoFormaDoc tipoFormaDoc = null;
		if (qryTipoForma.list().size() > 0) {
			tipoFormaDoc = (ExTipoFormaDoc) qryTipoForma.uniqueResult();
		}
		String trechoQryTipoForma = tipoFormaDoc == null ? ""
				: " and tipoForma.idTipoFormaDoc = "
						+ tipoFormaDoc.getIdTipoFormaDoc();

		// Obtém todas as lotações do órgão com a sigla passada...
		Query qrySetor = HibernateUtil.getSessao().createQuery(
				"from DpLotacao lot where " + "lot.dataFimLotacao is null "
						+ "and lot.orgaoUsuario = "
						+ parametros.get("orgaoUsuario") + " "
						+ "and lot.siglaLotacao = '"
						+ parametros.get("lotacao") + "'");
		Set<DpLotacao> lotacaoSet = new HashSet<DpLotacao>();
		for (Iterator iterator = qrySetor.list().iterator(); iterator.hasNext();) {
			DpLotacao lot = (DpLotacao) iterator.next();
			lotacaoSet.add(lot);
		}

		// ... e monta trecho da query para as lotações
		String listaLotacoes = "";
		Set<DpLotacao> todasLotas;
		if (parametros.get("incluirSubordinados") != null)
			todasLotas = getSetoresSubordinados(lotacaoSet);
		else
			todasLotas = lotacaoSet;
		for (DpLotacao lot : todasLotas) {
			if (listaLotacoes != "")
				listaLotacoes += ",";
			listaLotacoes += lot.getIdInicial().toString();
		}

		// Monta trecho da query para ocultar seletivamente a descrição do
		// documento
		String trechoQryDescrDocumento = "(case when ("
				+ "	nivel.idNivelAcesso <> 1 "
				+ "	and nivel.idNivelAcesso <> 6"
				+ ") then 'CONFIDENCIAL' else doc.descrDocumento end)";

		// Monta trecho da query para retornar o código do documento
		String trechoQryCodigoDoc = " orgao.siglaOrgaoUsu "
				+ "|| '-' || " + "forma.siglaFormaDoc "
				+ "|| '-' || " + "doc.anoEmissao " + "|| '/' || "
				+ "doc.numExpediente " + "|| "
				+ "(case when (tipoMob.idTipoMobil = 4) "
				+ "then ('-V' || marca.exMobil.numSequencia) " +
						"else ('-' || chr(marca.exMobil.numSequencia+64)) end)";

		// Monta query definitiva
		String listaMarcadoresRelevantes = "2, 3, 5, 7, 14, 15"; // Ativos
		if (parametros.get("tipoRel").equals("2")) {
			listaMarcadoresRelevantes = "27"; // Como gestor
		} else if (parametros.get("tipoRel").equals("3")) {
			listaMarcadoresRelevantes = "28"; // Como interessado
		}
		Query qryMarcas = HibernateUtil
				.getSessao()
				.createQuery(
						"select " + "	marca.dpLotacaoIni.nomeLotacao, "
								+ trechoQryCodigoDoc
								+ ","
								+ " '"
								+ parametros.get("link_siga")
								+ "' ||"
								+ trechoQryCodigoDoc
								+ ","
								+ trechoQryDescrDocumento
								+ ","
								+ "	pes.nomePessoa,"
								+ "	marca.cpMarcador.descrMarcador "
								+ "from ExMarca marca "
								+ "inner join marca.exMobil as mob "
								+ "inner join mob.exTipoMobil as tipoMob "
								+ "inner join mob.exDocumento as doc "
								+ "inner join doc.exNivelAcesso as nivel "
								+ "inner join doc.orgaoUsuario as orgao "
								+ "inner join doc.exFormaDocumento as forma "
								+ "inner join forma.exTipoFormaDoc as tipoForma "
								+ "inner join marca.dpLotacaoIni as lot "
								+ "inner join marca.cpMarcador as marcador "
								+ "left outer join marca.dpPessoaIni as pes "
								+ "where lot.idLotacao in ("
								+ listaLotacoes
								+ ") "
								+ "and marcador.idMarcador in ("
								+ listaMarcadoresRelevantes
								+ ")"
								+ trechoQryTipoForma
								+ " order by lot.siglaLotacao, doc.idDoc");

		// Retorna
		List<Object[]> lista = qryMarcas.list();

		List<String> listaFinal = new ArrayList<String>();
		for (Object[] array : lista) {
			for (Object value : array)
				listaFinal.add((String) value);
		}

		return listaFinal;

	}

	public Collection processarDadosLento() throws Exception {
		List<String> d = new LinkedList<String>();
		String lotacoes = "";

		String consulta = null;
		Query qryTipoForma = HibernateUtil.getSessao().createQuery(
				"from ExTipoFormaDoc tf where " + "tf.descTipoFormaDoc = '"
						+ parametros.get("tipoFormaDoc") + "'");

		ExTipoFormaDoc tipoFormaDoc = null;
		if (qryTipoForma.list().size() > 0) {
			tipoFormaDoc = (ExTipoFormaDoc) qryTipoForma.uniqueResult();
		}

		Query qrySetor = HibernateUtil.getSessao().createQuery(
				"from DpLotacao lot where " + "lot.dataFimLotacao is null "
						+ "and lot.orgaoUsuario = "
						+ parametros.get("orgaoUsuario") + " "
						+ "and lot.siglaLotacao = '"
						+ parametros.get("lotacao") + "'");

		Set<DpLotacao> lotacaoSet = new HashSet<DpLotacao>();
		for (Iterator iterator = qrySetor.list().iterator(); iterator.hasNext();) {
			DpLotacao lot = (DpLotacao) iterator.next();
			lotacaoSet.add(lot);
		}

		if (parametros.get("incluirSubordinados") != null) {

			Set<DpLotacao> todosSubordinados = getSetoresSubordinados(lotacaoSet);

			for (DpLotacao lot : todosSubordinados) {
				if (lotacoes != "") {
					lotacoes += " or lot.id=" + lot.getIdInicial().toString();
				} else {
					lotacoes = "and (lot.id=" + lot.getIdInicial().toString();
				}
			}
			lotacoes += ")";
		} else {
			for (DpLotacao lot : lotacaoSet) {
				if (lotacoes != "") {
					lotacoes += " or lot.id=" + lot.getIdInicial().toString();
				} else {
					lotacoes = "and (lot.id=" + lot.getIdInicial().toString();
				}
			}
			lotacoes += ")";
		}

		Query qryMovimentacao = null;

		String marcadoresRelevantes = "2, 3, 5, 7, 14, 15"; // Ativos
		if (parametros.get("tipoRel").equals("2")) {
			marcadoresRelevantes = "27"; // Como gestor
		} else if (parametros.get("tipoRel").equals("3")) {
			marcadoresRelevantes = "28"; // Como interessado
		}

		if (tipoFormaDoc != null) {
			qryMovimentacao = HibernateUtil
					.getSessao()
					.createQuery(
							"select mc from ExMarca mc "
									+ "inner join fetch mc.exMobil mob "
									+ "inner join mc.dpLotacaoIni lot "
									+ "inner join fetch mob.exDocumento doc where "
									+ "mc.cpMarcador.idMarcador in ("
									+ marcadoresRelevantes
									+ ") and "
									+ "doc.exFormaDocumento.exTipoFormaDoc.idTipoFormaDoc = "
									+ tipoFormaDoc.getIdTipoFormaDoc()
									+ lotacoes
									+ " order by lot.siglaLotacao,doc.idDoc");
		} else {
			qryMovimentacao = HibernateUtil
					.getSessao()
					.createQuery(
							"select mc from ExMarca mc "
									+ "inner join fetch mc.exMobil mob "
									+ "inner join mc.dpLotacaoIni lot "
									+ "inner join fetch mob.exDocumento doc where "
									+ "mc.cpMarcador.idMarcador in ("
									+ marcadoresRelevantes + ") " + lotacoes
									+ " order by lot.siglaLotacao,doc.idDoc");
		}
		int indice = 0;
		qryMovimentacao.setFirstResult(indice);
		java.util.List<ExMarca> listaMarcas = (List<ExMarca>) qryMovimentacao
				.list();

		Query qryLotacaoTitular = HibernateUtil.getSessao().createQuery(
				"from DpLotacao lot " + "where lot.dataFimLotacao is null "
						+ "and lot.orgaoUsuario = "
						+ parametros.get("orgaoUsuario")
						+ " and lot.siglaLotacao = '"
						+ parametros.get("lotacaoTitular") + "'");

		DpLotacao lotaTitular = (DpLotacao) qryLotacaoTitular.uniqueResult();

		DpPessoa titular = ExDao.getInstance().consultar(
				new Long((String) parametros.get("idTit")), DpPessoa.class,
				false);

		String resp = "";
		while (listaMarcas.size() > 0) {
			for (ExMarca mc : listaMarcas) {

				d.add(mc.getDpLotacaoIni().getNomeLotacao());
				d.add(mc.getExMobil().getSigla());
				d.add((String) parametros.get("link_siga")
						+ mc.getExMobil().getSigla());
				d.add(Ex.getInstance()
						.getBL()
						.descricaoConfidencialDoDocumento(mc.getExMobil(),
								titular, lotaTitular));
				// d.add("");
				// d.add(getResponsavel(mc.getExMobil().getExDocumento()).getNomePessoa());
				if (mc.getDpPessoaIni() != null) {
					resp = mc.getDpPessoaIni().getNomePessoa();
				} else {
					resp = "";
				}

				d.add(resp);
				d.add(mc.getCpMarcador().getDescrMarcador());
				indice++;
				System.out.println(indice);
			}

			/*
			 * if (indice < MAX_RESULTS) { break; } else {
			 * System.out.println("Tamanho do resultado:" + d.size());
			 * qryMovimentacao.setFirstResult(indice);
			 * HibernateUtil.getSessao().clear(); listaMarcas =
			 * qryMovimentacao.list(); }
			 */
		}

		return d;

	}

	private Set<DpLotacao> getSetoresSubordinados(Set<DpLotacao> lista) {
		Set<DpLotacao> todosSubordinados = new HashSet<DpLotacao>();
		Set<DpLotacao> subordinadosDiretos;

		for (DpLotacao pai : lista) {
			if (pai.getDpLotacaoSubordinadosSet().size() <= 0) {
				todosSubordinados.add(pai);
				continue;
			} else {
				todosSubordinados.add(pai);
				todosSubordinados.addAll(getSetoresSubordinados(pai
						.getDpLotacaoSubordinadosSet()));
			}
		}

		return todosSubordinados;

	}

	private DpPessoa getResponsavel(ExDocumento doc) {
		if (doc.getTitular() != null) {
			return doc.getTitular();
		}

		if (doc.getSubscritor() != null) {
			return doc.getSubscritor();
		}

		return doc.getCadastrante();
	}
}
