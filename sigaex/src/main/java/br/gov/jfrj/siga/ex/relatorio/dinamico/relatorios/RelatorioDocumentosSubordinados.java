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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import net.sf.jasperreports.engine.JRException;

public class RelatorioDocumentosSubordinados extends RelatorioTemplate {

	public RelatorioDocumentosSubordinados(Map parametros)
			throws Exception {
		super(parametros);
		if (Utils.empty((String) parametros.get("secaoUsuario"))) {
			throw new AplicacaoException(
					"Parâmetro secaoUsuario não informado!");
		}
		if (Utils.empty((String) parametros.get("lotacao"))) {
			throw new AplicacaoException(
					"Parâmetro lotação não informado!");
		}
		if (Utils.empty((String) parametros.get("link_siga"))) {
			throw new AplicacaoException("Parâmetro link_siga não informado!");
		}

	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException {

		this.setTitle("Relatório de Documentos em Setores Subordinados");
		this.addColuna("Setor", 0, RelatorioRapido.ESQUERDA, true);
		this.addColuna("Documento", 30, RelatorioRapido.ESQUERDA,  true, true);
		this.addColuna("Descrição", 50, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Responsável", 45, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Situação", 40, RelatorioRapido.ESQUERDA, false);
		return this;

	}
	
	public Collection processarDados() throws Exception {

		Query qryLotacaoTitular = ContextoPersistencia.em().createQuery(
				"from DpLotacao lot " + "where lot.dataFimLotacao is null "
						+ "and lot.orgaoUsuario = "
						+ parametros.get("orgaoUsuario")
						+ " and lot.siglaLotacao = '"
						+ parametros.get("lotacaoTitular") + "'");
		DpLotacao lotaTitular = (DpLotacao) qryLotacaoTitular.getSingleResult();

		DpPessoa titular = ExDao.getInstance().consultar(
				new Long((String) parametros.get("idTit")), DpPessoa.class,
				false);
		
		// Obtém uma formaDoc a partir da sigla passada e monta trecho da query
		// para a forma
		Query qryTipoForma = ContextoPersistencia.em().createQuery(
				"from ExTipoFormaDoc tf where " + "tf.descTipoFormaDoc = '"
						+ parametros.get("tipoFormaDoc") + "'");
		
		ExTipoFormaDoc tipoFormaDoc = null;
		try {
			tipoFormaDoc = (ExTipoFormaDoc) qryTipoForma.getSingleResult();
		} catch (Exception e) {
		}
		
		String trechoQryTipoForma = tipoFormaDoc == null ? ""
				: " and tipoForma.idTipoFormaDoc = "
						+ tipoFormaDoc.getIdTipoFormaDoc();

			
		// Obtém a lotação com o id passado...
		Query qrySetor = ContextoPersistencia.em().createQuery(
				"from DpLotacao lot where lot.idLotacao = " + parametros.get("lotacao"));
			
		Set<DpLotacao> lotacaoSet = new HashSet<DpLotacao>();
		DpLotacao lotacao = (DpLotacao)qrySetor.getSingleResult();
		lotacaoSet.add(lotacao);
		
	

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
		/*String trechoQryCodigoDoc = " orgao.siglaOrgaoUsu "
				+ "|| '-' || " + "forma.siglaFormaDoc "
				+ "|| '-' || " + "doc.anoEmissao " + "|| '/' || "
				+ "doc.numExpediente " + "|| "
				+ "(case when (tipoMob.idTipoMobil = 4) "
				+ "then ('-V' || marca.exMobil.numSequencia) " +
						"else ('-' || chr(marca.exMobil.numSequencia+64)) end)";*/

		// Monta query definitiva
		String listaMarcadoresRelevantes = "2, 3, 5, 7, 14, 15"; // Ativos
		if (parametros.get("tipoRel").equals("2")) {
			listaMarcadoresRelevantes = "27"; // Como gestor
		} else if (parametros.get("tipoRel").equals("3")) {
			listaMarcadoresRelevantes = "28"; // Como interessado
		}
		
		
		// bruno.lacerda@avantiprima.com.br
		// int timeout = 1;
		Query qryMarcas = ContextoPersistencia.em()
				.createQuery(
						"select " + "	marca.dpLotacaoIni.nomeLotacao, "
								+ "doc.idDoc, orgao.siglaOrgaoUsu, orgao.acronimoOrgaoUsu, forma.siglaFormaDoc, doc.anoEmissao, doc.numExpediente, doc.numSequencia, tipoMob.idTipoMobil, mob.numSequencia,  "
								+ "docPai.idDoc, orgaoDocPai.siglaOrgaoUsu, orgaoDocPai.acronimoOrgaoUsu, formaDocPai.siglaFormaDoc, docPai.anoEmissao, docPai.numExpediente, docPai.numSequencia, tipoMobPai.idTipoMobil, mobPai.numSequencia, "
								+ " '"
								+ parametros.get("link_siga")
								+ " '"
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
								+ "left outer join doc.exMobilPai as mobPai "
								+ "left outer join mobPai.exDocumento docPai "
								+ "left outer join docPai.orgaoUsuario orgaoDocPai "
								+ "left outer join docPai.exFormaDocumento formaDocPai "
								+ "left outer join mobPai.exTipoMobil tipoMobPai "
								+ "where lot.idLotacao in ("
								+ listaLotacoes
								+ ") "
								+ "and marcador.idMarcador in ("
								+ listaMarcadoresRelevantes
								+ ")"
								+ trechoQryTipoForma
								+ " order by lot.siglaLotacao, doc.idDoc"/*, timeout*/);

		// Retorna
		List<Object[]> lista = qryMarcas.getResultList();

		List<String> listaFinal = new ArrayList<String>();
		for (Object[] array : lista) {
			
				
			String nomeLotacao = (String)array[0]; 
			
			
			Long idDoc = (Long)array[1];
			
			ExDocumento documento = ExDao.getInstance().consultarExDocumentoPorId(idDoc);
			if (Ex.getInstance().getBL().exibirQuemTemAcessoDocumentosLimitados(
					documento, titular, 
							lotaTitular)) {
					
				String siglaOrgaoUsu = (String)array[2];
				String acronimoOrgaoUsu = (String)array[3];
				String siglaFormaDoc = (String)array[4];
				Long anoEmissao = (Long)array[5];
				Long numExpediente = (Long)array[6];
				Integer docNumSequencia = (Integer)array[7];
				Long idTipoMobil = (Long)array[8];
				Integer mobilNumSequencia = (Integer)array[9];
				Long pai_idDoc = (Long)array[10];
				String pai_siglaOrgaoUsu = (String)array[11];
				String pai_acronimoOrgaoUsu = (String)array[12];
				String pai_siglaFormaDoc = (String)array[13];
				Long pai_anoEmissao = (Long)array[14];
				Long pai_numExpediente = (Long)array[15];
				Integer pai_numSequencia = (Integer)array[16];
				Long pai_idTipoMobil = (Long)array[17];
				Integer pai_mobilNumSequencia = (Integer)array[18];
				
				String codigoDocumento = ExDocumento.getCodigo(idDoc, siglaOrgaoUsu, acronimoOrgaoUsu, siglaFormaDoc, anoEmissao, numExpediente, docNumSequencia, idTipoMobil, mobilNumSequencia, 
						pai_idDoc, pai_siglaOrgaoUsu, pai_acronimoOrgaoUsu, pai_siglaFormaDoc, pai_anoEmissao, pai_numExpediente, pai_numSequencia, pai_idTipoMobil, pai_mobilNumSequencia);
				
				String codigoMobil = ExMobil.getSigla(codigoDocumento, mobilNumSequencia, idTipoMobil);
				
				String url = ((String)array[19]).trim() + codigoMobil;
				
				String descricao = (String)array[20];
				
				String nomePessoa = (String)array[21];
				
				String descrMarcador =  (String)array[22];
				
				listaFinal.add(nomeLotacao);
				listaFinal.add(codigoMobil);
				listaFinal.add(url);
				listaFinal.add(descricao);
				listaFinal.add(nomePessoa);
				listaFinal.add(descrMarcador);
			}
		}

		return listaFinal;
	}

	public Collection processarDadosAnterior() throws Exception {

		// Obtém uma formaDoc a partir da sigla passada e monta trecho da query
		// para a forma
		Query qryTipoForma = ContextoPersistencia.em().createQuery(
				"from ExTipoFormaDoc tf where " + "tf.descTipoFormaDoc = '"
						+ parametros.get("tipoFormaDoc") + "'");
		
		ExTipoFormaDoc tipoFormaDoc = null;
		List resultList = qryTipoForma.getResultList();
		if (resultList.size() > 0) {
			tipoFormaDoc = (ExTipoFormaDoc) resultList.get(0);
		}
		
		String trechoQryTipoForma = tipoFormaDoc == null ? ""
				: " and tipoForma.idTipoFormaDoc = "
						+ tipoFormaDoc.getIdTipoFormaDoc();

			
		// Obtém a lotação com o id passado...
		Query qrySetor = ContextoPersistencia.em().createQuery(
				"from DpLotacao lot where lot.idLotacao = " + parametros.get("lotacao"));
			
		Set<DpLotacao> lotacaoSet = new HashSet<DpLotacao>();
		DpLotacao lotacao = (DpLotacao)qrySetor.getResultList().get(0);
		lotacaoSet.add(lotacao);
		
	

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
		
		// bruno.lacerda@avantiprima.com.br
		// int timeout = 1;
		Query qryMarcas = ContextoPersistencia.em()
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
								+ " order by lot.siglaLotacao, doc.idDoc"/*, timeout*/);

		// Retorna
		List<Object[]> lista = qryMarcas.getResultList();

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
		Query qryTipoForma = ContextoPersistencia.em().createQuery(
				"from ExTipoFormaDoc tf where " + "tf.descTipoFormaDoc = '"
						+ parametros.get("tipoFormaDoc") + "'");

		ExTipoFormaDoc tipoFormaDoc = null;
		List resultList = qryTipoForma.getResultList();
		if (resultList.size() > 0) {
			tipoFormaDoc = (ExTipoFormaDoc) resultList.get(0);
		}

		Query qrySetor = ContextoPersistencia.em().createQuery(
				"from DpLotacao lot where " + "lot.dataFimLotacao is null "
						+ "and lot.orgaoUsuario = "
						+ parametros.get("orgaoUsuario") + " "
						+ "and lot.siglaLotacao = '"
						+ parametros.get("lotacao") + "'");

		Set<DpLotacao> lotacaoSet = new HashSet<DpLotacao>();
		for (DpLotacao lot : (List<DpLotacao>) qrySetor.getResultList()) {
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
			qryMovimentacao = ContextoPersistencia.em()
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
			qryMovimentacao = ContextoPersistencia.em()
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
				.getResultList();

		Query qryLotacaoTitular = ContextoPersistencia.em().createQuery(
				"from DpLotacao lot " + "where lot.dataFimLotacao is null "
						+ "and lot.orgaoUsuario = "
						+ parametros.get("orgaoUsuario")
						+ " and lot.siglaLotacao = '"
						+ parametros.get("lotacaoTitular") + "'");

		DpLotacao lotaTitular = (DpLotacao) qryLotacaoTitular.getSingleResult();

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
//				System.out.println(indice);
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