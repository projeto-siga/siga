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
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import net.sf.jasperreports.engine.JRException;

public class RelMovimentacaoDocSubordinados extends RelatorioTemplate {

	public RelMovimentacaoDocSubordinados(Map<String, String> parametros)
			throws Exception {
		super(parametros);
		if (Utils.empty(parametros.get("secaoUsuario"))) {
			throw new AplicacaoException(
 					"Parâmetro secaoUsuario não informado!");
 		}
		if (Utils.empty(parametros.get("lotacao"))) {
			throw new AplicacaoException(
					"Parâmetro lotacao não informado!");
		}
		if (Utils.empty(parametros.get("link_siga"))) {
			throw new AplicacaoException("Parâmetro link_siga não informado!");
 		}
	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException {

		this.setTitle("Relatório de Movimentação de Documentos em Setores Subordinados");
		this.addColuna("Setor", 0, RelatorioRapido.ESQUERDA, true);
		this.addColuna("Documento", 25, RelatorioRapido.CENTRO, false);
		this.addColuna("Data", 20, RelatorioRapido.CENTRO, false);
		this.addColuna("Lotação", 20, RelatorioRapido.CENTRO, false);
		this.addColuna("Situação", 20, RelatorioRapido.CENTRO, false);
		this.addColuna("Descrição", 20, RelatorioRapido.CENTRO, false);
		this.addColuna("Anotação", 20, RelatorioRapido.CENTRO, false);
		this.addColuna("Duração (em dias)", 15, RelatorioRapido.CENTRO, false);
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
		} catch (Exception ex) {}
		
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
								+ "mov, mob, " 
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
								+ "inner join mob.exMovimentacaoSet as mov "
								+ "inner join mov.lotaResp as dplot "
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
								+ " and mov.dtMov = (select max(movim.dtMov) from ExMovimentacao movim where movim.exMobil = mov.exMobil) "
								+ " order by lot.siglaLotacao, doc.idDoc, mov.dtMov"/*, timeout*/);

		// Retorna
		List<Object[]> lista = qryMarcas.getResultList();
		long datatual = 0;
		long dataant = 0;
		long durentrelots = 0;
		List<String> listaFinal = new ArrayList<String>();
		
		for (Object[] array : lista) {
			String nomeLotacao = (String)array[0]; 
			ExMovimentacao mov = (ExMovimentacao) array[1];
			ExMovimentacao movimentacao = (ExMovimentacao) array[1];
			ExMobil mob = (ExMobil)array[2];
			Long idDoc = (Long)array[3];
			
			ExDocumento documento = ExDao.getInstance().consultarExDocumentoPorId(idDoc);
			if (Ex.getInstance().getBL().exibirQuemTemAcessoDocumentosLimitados(
					documento, titular, 
							lotaTitular)) {
				String siglaOrgaoUsu = (String)array[4];
				String acronimoOrgaoUsu = (String)array[5];
				String siglaFormaDoc = (String)array[6];
				Long anoEmissao = (Long)array[7];
				Long numExpediente = (Long)array[8];
				Integer docNumSequencia = (Integer)array[9];
				Long idTipoMobil = (Long)array[10];
				Integer mobilNumSequencia = (Integer)array[11];
				Long pai_idDoc = (Long)array[12];
				String pai_siglaOrgaoUsu = (String)array[13];
				String pai_acronimoOrgaoUsu = (String)array[14];
				String pai_siglaFormaDoc = (String)array[15];
				Long pai_anoEmissao = (Long)array[16];
				Long pai_numExpediente = (Long)array[17];
				Integer pai_numSequencia = (Integer)array[18];
				Long pai_idTipoMobil = (Long)array[19];
				Integer pai_mobilNumSequencia = (Integer)array[20];
				
				String codigoDocumento = ExDocumento.getCodigo(idDoc, siglaOrgaoUsu, acronimoOrgaoUsu, siglaFormaDoc, anoEmissao, numExpediente, docNumSequencia, idTipoMobil, mobilNumSequencia, 
						pai_idDoc, pai_siglaOrgaoUsu, pai_acronimoOrgaoUsu, pai_siglaFormaDoc, pai_anoEmissao, pai_numExpediente, pai_numSequencia, pai_idTipoMobil, pai_mobilNumSequencia);
				
				String codigoMobil = ExMobil.getSigla(codigoDocumento, mobilNumSequencia, idTipoMobil);
				
				String url = ((String)array[21]).trim() + codigoMobil;
				
				String descricao = (String)array[22];
				
				String nomePessoa = (String)array[23];
				
				String descrMarcador =  (String)array[24];
				
				long identificador = movimentacao.getExMobil().getId();
				datatual = System.currentTimeMillis();
				movimentacao.getExMobil().getId();
				if ((mob.getUltimaMovimentacao(3) == null) || (mob.getUltimaMovimentacao(8) == null)) {
					dataant = mob.getUltimaMovimentacao(1).getData().getTime();
					durentrelots = (((datatual - dataant)/86400000L)+1);
				}
				else
				{
					if (mob.getUltimaMovimentacao(3).getData().getTime() > mob.getUltimaMovimentacao(8).getData().getTime()){
						dataant = mob.getUltimaMovimentacao(3).getData().getTime();
						durentrelots = (((datatual - dataant)/86400000L)+1);
					}
					else{
						dataant = mob.getUltimaMovimentacao(8).getData().getTime();
						durentrelots = (((datatual - dataant)/86400000L)+1);
					}
						
				}
				mob.getUltimaMovimentacao(28);
				listaFinal.add(nomeLotacao);
				listaFinal.add(codigoMobil);
				listaFinal.add(mov.getDtRegMovDDMMYY().toString());
				listaFinal.add(mov.getLotaResp().getSigla().toString());
				listaFinal.add(descrMarcador);
				listaFinal.add(descricao);
				if (mob.getUltimaMovimentacao(28)  != null) {
					listaFinal.add(mob.getUltimaMovimentacao(28).toString());
				} else {
					listaFinal.add("");
				}
				listaFinal.add(String.valueOf(durentrelots));
			}
		
		}
		return listaFinal;
	}

	private Long String(Long idDoc) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection processarDadosAnterior() throws Exception {

		// Obtém uma formaDoc a partir da sigla passada e monta trecho da query
		// para a forma
		Query qryTipoForma = ContextoPersistencia.em().createQuery(
				"from ExTipoFormaDoc tf where " + "tf.descTipoFormaDoc = '"
						+ parametros.get("tipoFormaDoc") + "'");
		
		ExTipoFormaDoc tipoFormaDoc = null;
		try {
			tipoFormaDoc = (ExTipoFormaDoc) qryTipoForma.getSingleResult();
		} catch (Exception ex) {}
		
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
								+ " mov.dtMov, mov.descrMov, dplot.siglaLotacao, "
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
								+ "inner join mob.exMovimentacaoSet as mov "
								+ "inner join mov.exMovimentacao as movimentacao"
								+ "inner join mov.lotaResp as dplot "
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
								+ " and mov.dtMov = (select max(movim.dtMov) from ExMovimentacao movim where movim.exMobil = mov.exMobil) "
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
		try {
			tipoFormaDoc = (ExTipoFormaDoc) qryTipoForma.getSingleResult();
		} catch (Exception ex) {}

		Query qrySetor = ContextoPersistencia.em().createQuery(
				"from DpLotacao lot where " + "lot.dataFimLotacao is null "
						+ "and lot.orgaoUsuario = "
						+ parametros.get("orgaoUsuario") + " "
						+ "and lot.siglaLotacao = '"
						+ parametros.get("lotacao") + "'");

		Set<DpLotacao> lotacaoSet = new HashSet<DpLotacao>();
		for (Iterator iterator = qrySetor.getResultList().iterator(); iterator.hasNext();) {
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
