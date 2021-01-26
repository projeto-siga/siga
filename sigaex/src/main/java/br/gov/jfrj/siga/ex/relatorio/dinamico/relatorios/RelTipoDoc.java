package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import net.sf.jasperreports.engine.JRException;

public class RelTipoDoc extends RelatorioTemplate {

	private DpLotacao lotacao;
	
	public RelTipoDoc(Map<String, String> parametros) throws Exception {
		super(parametros);
		if (Utils.empty(parametros.get("secaoUsuario"))) {
			throw new AplicacaoException(
					"Parâmetro secaoUsuario não informado!");
		}
		if (Utils.empty(parametros.get("lotacao"))) {
			throw new AplicacaoException("Parâmetro lotação não informado!");
		}
		/*
		 * if (Utils.empty(parametros.get("orgao"))) { throw new
		 * AplicacaoException("Parâmetro órgão não informado!"); }
		 */
		if (Utils.empty(parametros.get("dataInicial"))) {
			throw new AplicacaoException("Parâmetro dataInicial não informado!");
		}
		if (Utils.empty(parametros.get("dataFinal"))) {
			throw new AplicacaoException("Parâmetro dataFinal não informado!");
		}
		if (Utils.empty(parametros.get("link_siga"))) {
			throw new AplicacaoException("Parâmetro link_siga não informado!");
		}
		this.lotacao = buscarLotacaoPor(Long.valueOf(parametros.get("lotacao")));
	}

	private DpLotacao buscarLotacaoPor(Long id) {
		CpDao dao = CpDao.getInstance();
		DpLotacao lotacao = dao.consultar(id, DpLotacao.class, false);
		return lotacao;
	}
	
	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException {

		String titulo = "Relação de Documentos Criados de " + parametros.get("dataInicial").toString() + " a " + parametros.get("dataFinal").toString();
		String subtitulo = "Do(a) " + lotacao.getNomeLotacao();
		
		this.setTitle(titulo);
		this.setSubtitle(subtitulo);
		
		this.addColuna("Tipo de Documento", 35, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Forma do Documento", 40, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Total", 25, RelatorioRapido.CENTRO, false);
		return this;

	}

	@Override
	public Collection processarDados() throws Exception {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		List<String> d = new ArrayList<String>();

		Query query = ContextoPersistencia.em()
				.createQuery(
						"select doc.exFormaDocumento.exTipoFormaDoc.descTipoFormaDoc, "
						+ "doc.exFormaDocumento.descrFormaDoc, "
						+ "count(distinct doc.idDoc) "
						+ "from ExMovimentacao mov inner join mov.exMobil mob "
						+ "inner join mob.exDocumento doc "
						+ "where mov.dtIniMov between :dtini and :dtfim "
						+ "and mov.lotaResp.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :id) "
						+ "and mov.exTipoMovimentacao.idTpMov =  1 "
						+ "group by doc.exFormaDocumento.exTipoFormaDoc.descTipoFormaDoc, "
						+ "doc.exFormaDocumento.descrFormaDoc");
		
		// Obtém a lotação com o id passado...
		Query qrySetor = ContextoPersistencia.em().createQuery(
				"from DpLotacao lot where lot.idLotacao = " + parametros.get("lotacao"));
					
		Set<DpLotacao> lotacaoSet = new HashSet<DpLotacao>();
		DpLotacao lotacao = (DpLotacao)qrySetor.getResultList().get(0);
		lotacaoSet.add(lotacao);
		
		
		query.setParameter("id",
				lotacao.getIdInicial());
		/*
		 * Long orgaoUsu = Long.valueOf((String) parametros.get("orgao"));
		 * query.setLong("orgaoUsu", orgaoUsu);
		 */
		Date dtini = formatter.parse((String) parametros.get("dataInicial"));
		query.setParameter("dtini", dtini);
		Date dtfim = formatter.parse((String) parametros.get("dataFinal"));
		query.setParameter("dtfim", dtfim);

		Map<String, Long> map = new TreeMap<String, Long>();

		Iterator it = query.getResultList().iterator();
		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			String tipodoc = (String) obj[0];
			String formadoc = (String) obj[1];
			Long total = Long.valueOf(obj[2].toString());
			map.put(chave(tipodoc, formadoc), total);
			d.add(tipodoc);
			d.add(formadoc);
			acrescentarColuna(d, map, tipodoc, formadoc);
		}
		
		return d;
	}

	private void acrescentarColuna(List<String> d, Map<String, Long> map,
			String s, String lis) {
		Long l = 0L;
		String key = chave(s, lis);
		if (map.containsKey(key))
			l += map.get(key);
		if (l > 0)
			d.add(l.toString());
		else
			d.add("0");
	}

	private String chave(String tipodoc, String formadoc) {
		return tipodoc + formadoc;
	}
}