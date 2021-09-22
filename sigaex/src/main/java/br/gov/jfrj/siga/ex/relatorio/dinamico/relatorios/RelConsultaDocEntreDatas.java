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

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.view.JasperViewer;

public class RelConsultaDocEntreDatas extends RelatorioTemplate {

	private Long tipoDoDocumento;
	private DpLotacao lotacao;
	private Date dataInicial;
	private Date dataFinal;
	private String link;

	public RelConsultaDocEntreDatas(Map<String, String> parametros) throws Exception {
		super(parametros);
		
		if (Utils.empty(parametros.get("secaoUsuario"))) {
			throw new AplicacaoException("Parâmetro secaoUsuario não informado!");
		}
		if (Utils.empty(parametros.get("lotacao"))) {
			throw new AplicacaoException("Parâmetro lotação não informado!");
		}
		if (Utils.empty(parametros.get("dataInicial"))) {
			throw new AplicacaoException("Parâmetro data inicial não informado!");
		}
		if (Utils.empty(parametros.get("dataFinal"))) {
			throw new AplicacaoException("Parâmetro data final não informado!");
		}
		if (Utils.empty(parametros.get("link_siga"))) {
			throw new AplicacaoException("Parâmetro link_siga não informado!");
		}
		
		preencherAtributosCom(parametros);
		
		if (dataFinal.getTime() - dataInicial.getTime() > 31536000000L) 
			throw new AplicacaoException("O relatório retornará muitos resultados. Favor reduzir o intervalo entre as datas.");
	}
		
	private void preencherAtributosCom(Map<String, String> parametros) throws ParseException {
		this.tipoDoDocumento = Long.valueOf(parametros.get("origem"));
		this.lotacao = buscarLotacaoPor(Long.valueOf(parametros.get("lotacao")));
		this.dataInicial = stringToDate(parametros.get("dataInicial") + " 00:00:00");
		this.dataFinal = stringToDate(parametros.get("dataFinal") + " 23:59:59");	
		this.link = parametros.get("link_siga"); 
	}
	
	private DpLotacao buscarLotacaoPor(Long id) {
		CpDao dao = CpDao.getInstance();
		DpLotacao lotacao = dao.consultar(id, DpLotacao.class, false);
		return lotacao;
	}
	
	private Date stringToDate(String texto) throws ParseException {
		DateFormat formatador = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		return formatador.parse(texto);
	}
	
	private String dateToString(Date data) {
		DateFormat formatador = new SimpleDateFormat("dd/MM/yy");
		return formatador.format(data);
	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException {

		String origemtext = "";
 		
		switch (tipoDoDocumento.intValue()) {
		case 1:
			origemtext = "Interno Produzido ";
			break;
		case 2:
			origemtext = "Interno Folha de Rosto ";
			break;
		case 3:
			origemtext = "Externo Folha de Rosto ";
			break;
		case 4:
			origemtext = "Externo Capturado ";
			break;
		case 5:
			origemtext = "Interno Capturado ";
			break;
		}
		String titulo = "Relação de Documento(s) " + origemtext + " de " + dateToString(dataInicial) + " a " + dateToString(dataFinal);
		String subtitulo = "Do(a) " + lotacao.getNomeLotacao();
		this.setSubtitle(subtitulo);	
		this.setTitle(titulo);
		this.addColuna("Código do Documento", 140, RelatorioRapido.ESQUERDA, true, true);
		this.addColuna("Descrição do documento", 140, RelatorioRapido.ESQUERDA, false);

		return this;
	}

	@SuppressWarnings("unchecked")
	public Collection processarDados() throws Exception {
		List<String> dados = new ArrayList<String>();
		
		try {
			ExDao dao = ExDao.getInstance();
			Query query = ContextoPersistencia.em().createNamedQuery("consultarDocumentosFinalizadosEntreDatas");
			
			query.setParameter("idTipoDocumento", tipoDoDocumento);
			query.setParameter("idLotacaoInicial", lotacao.getIdInicial());
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
			
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
			
			List<ExDocumento> listaDocumentos = query.getResultList();

			for (ExDocumento documento : listaDocumentos) {
				if (Ex.getInstance().getBL().exibirQuemTemAcessoDocumentosLimitados(
						documento, titular, 
								lotaTitular)) {
					dados.add(documento.getCodigo());
					dados.add(link.concat(documento.getCodigo()));
					dados.add(Ex
							.getInstance()
							.getBL()
							.descricaoConfidencialDoDocumento(documento.getMobilGeral(), titular,
									lotaTitular));
				}

			}
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao gerar o relatorio");
		}
		
		return dados;
	}

	public Collection processarDadosLento() throws Exception {
		List<String> dados = new ArrayList<String>();
		// HibernateUtil.configurarHibernate("/br/gov/jfrj/siga/hibernate/hibernate.cfg.xml");

		ExDao dao = ExDao.getInstance();
		final Query query = ContextoPersistencia.em().createNamedQuery(
				"consultarMobilNoPeriodo");

		DpLotacao lot = new DpLotacao();
		lot.setSigla((String) parametros.get("lotacao"));
		DpLotacao otacao = dao.consultarPorSigla(lot);
		query.setParameter("idLotacao", new Long(lotacao.getId()));
		query.setParameter("dataInicial", (String) parametros.get("dataInicial"));
		query.setParameter("dataFinal", (String) parametros.get("dataFinal"));

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

		ExMobil mob = null;

		List provResultList = query.getResultList();

		for (Iterator iterator = provResultList.iterator(); iterator.hasNext();) {
			BigDecimal idMobil = (BigDecimal) iterator.next();
			if (idMobil != null) {
				mob = dao.consultar(new Long(idMobil.longValue()),
						ExMobil.class, false);
				dados.add(mob.getSigla());
				dados.add((String) parametros.get("link_siga") + mob.getSigla());
				// dados.add(mob.getExDocumento().getDescrDocumento());
				dados.add(Ex
						.getInstance()
						.getBL()
						.descricaoConfidencialDoDocumento(mob, titular,
								lotaTitular));
				dados.add(mob.getExDocumento().getLotaCadastrante().getSigla());
			}
		}
		return dados;
	}

	public static void main(String args[]) throws Exception {
		Map<String, String> listaParametros = new HashMap<String, String>();
		listaParametros.put("secaoUsuario", "SJRJ");
		listaParametros.put("lotacao", "13183");
		listaParametros.put("dataInicial", "01/07/2009");
		listaParametros.put("dataFinal", "01/08/2009");
		RelConsultaDocEntreDatas r = new RelConsultaDocEntreDatas(
				listaParametros);
		r.gerar();
		JasperViewer.viewReport(r.getRelatorioJasperPrint());
	}

}
