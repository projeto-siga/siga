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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.view.JasperViewer;

import org.hibernate.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class RelConsultaDocEntreDatas extends RelatorioTemplate {

	public RelConsultaDocEntreDatas(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("secaoUsuario") == null) {
			throw new DJBuilderException(
					"Parâmetro secaoUsuario não informado!");
		}
		if (parametros.get("lotacao") == null) {
			throw new DJBuilderException("Parâmetro lotacao não informado!");
		}
		if (parametros.get("dataInicial") == null) {
			throw new DJBuilderException("Parâmetro dataInicial não informado!");
		}
		if (parametros.get("dataFinal") == null) {
			throw new DJBuilderException("Parâmetro dataFinal não informado!");
		}
		if (parametros.get("link_siga") == null) {
			throw new DJBuilderException("Parâmetro link_siga não informado!");
		}
	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException {
		// TODO Auto-generated method stub
		this.setTitle("Relação de Documentos entre Datas");
		this.addColuna("Código do Documento", 20, RelatorioRapido.ESQUERDA,
				 true, true);
		this.addColuna("Descrição do documento", 100, RelatorioRapido.ESQUERDA,
				false);
		this.addColuna("Lotação do Cadastrante", 40, RelatorioRapido.ESQUERDA,
				false);

		return this;
	}

	public Collection processarDados() throws Exception {

		ExDao dao = ExDao.getInstance();
		final Query query = dao.getSessao().getNamedQuery(
				"consultarMobilNoPeriodo");

		//DpLotacao lot = new DpLotacao();
		//lot.setSigla((String) parametros.get("lotacao"));
		//List<DpLotacao> listaLotacao = dao.consultar(lot, null);
		
		Long idLotacao = Long.valueOf((String)parametros.get("lotacao"));
		query.setLong("idLotacao", idLotacao);
		query.setString("dataInicial", (String) parametros.get("dataInicial"));
		query.setString("dataFinal", (String) parametros.get("dataFinal"));

		query.setString("URL", (String) parametros.get("link_siga"));

		List<Object[]> provResultList = query.list();

		List<String> dados = new ArrayList<String>();

		for (Object[] array : provResultList) {
			for (Object value : array)
				dados.add((String) value);
		}

		return dados;
	}

	public Collection processarDadosLento() throws Exception {
		List<String> dados = new ArrayList<String>();
		// HibernateUtil.configurarHibernate("/br/gov/jfrj/siga/hibernate/hibernate.cfg.xml");

		ExDao dao = ExDao.getInstance();
		final Query query = dao.getSessao().getNamedQuery(
				"consultarMobilNoPeriodo");

		DpLotacao lot = new DpLotacao();
		lot.setSigla((String) parametros.get("lotacao"));
		List<DpLotacao> listaLotacao = dao.consultar(lot, null);
		query.setLong("idLotacao", new Long(listaLotacao.get(0).getId()));
		query.setString("dataInicial", (String) parametros.get("dataInicial"));
		query.setString("dataFinal", (String) parametros.get("dataFinal"));

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

		ExMobil mob = null;

		List provResultList = query.list();

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
