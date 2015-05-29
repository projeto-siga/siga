package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sf.jasperreports.engine.JRException;

import org.hibernate.Query;

import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DJCrosstab;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.CrosstabBuilder;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class RelTipoDoc extends RelatorioTemplate {

	public RelTipoDoc(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("secaoUsuario") == null) {
			throw new DJBuilderException(
					"Parâmetro secaoUsuario não informado!");
		}
		if (parametros.get("lotacaoTitular") == null) {
			throw new DJBuilderException("Parâmetro lotação não informado!");
		}
		/*
		 * if (parametros.get("orgao") == null) { throw new
		 * DJBuilderException("Parâmetro órgão não informado!"); }
		 */
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

		this.setTitle("Relação de Documentos Criados");
		this.addColuna("Tipo de Documento", 35, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Forma do Documento", 40, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Total", 25, RelatorioRapido.CENTRO, false);
		return this;

	}

	@Override
	public Collection processarDados() throws Exception {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		List<String> d = new ArrayList<String>();

		Query query = HibernateUtil
				.getSessao()
				.createQuery(
						"select doc.exFormaDocumento.exTipoFormaDoc.descTipoFormaDoc, "
						+ "doc.exFormaDocumento.descrFormaDoc, "
						+ "count(distinct doc.idDoc) "
						+ "from ExMovimentacao mov inner join mov.exMobil mob "
						+ "inner join mob.exDocumento doc "
						+ "where mov.dtIniMov between :dtini and :dtfim "
						+ "and mov.lotaResp.idLotacao = :id "
						+ "group by doc.exFormaDocumento.exTipoFormaDoc.descTipoFormaDoc, "
						+ "doc.exFormaDocumento.descrFormaDoc");
		
		query.setParameter("id",
				Long.valueOf((String) parametros.get("lotacao")));
		/*
		 * Long orgaoUsu = Long.valueOf((String) parametros.get("orgao"));
		 * query.setLong("orgaoUsu", orgaoUsu);
		 */
		Date dtini = formatter.parse((String) parametros.get("dataInicial"));
		query.setDate("dtini", dtini);
		Date dtfim = formatter.parse((String) parametros.get("dataFinal"));
		query.setDate("dtfim", dtfim);

		SortedSet<String> set = new TreeSet<String>();
		SortedSet<String> lista = new TreeSet<String>();
		Map<String, Long> map = new TreeMap<String, Long>();

		Iterator it = query.list().iterator();
		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			String tipodoc = (String) obj[0];
			String formadoc = (String) obj[1];
			Long total = Long.valueOf(obj[2].toString());
			set.add(tipodoc);
			lista.add(formadoc);
			map.put(chave(tipodoc, formadoc), total);
		}
		for (String s : set) {
			for (String lis : lista) {
				d.add(s);
				d.add(lis);
				acrescentarColuna(d, map, s, lis);
			}
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
