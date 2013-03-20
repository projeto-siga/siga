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

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class RelOrgao extends RelatorioTemplate {

	public RelOrgao(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("secaoUsuario") == null) {
			throw new DJBuilderException(
					"Parâmetro secaoUsuario não informado!");
		}
		if (parametros.get("lotacaoTitular") == null) {
			throw new DJBuilderException("Parâmetro lotação não informado!");
		}
		if (parametros.get("orgao") == null) {
			throw new DJBuilderException("Parâmetro órgão não informado!");
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

		this.setTitle("Relatório de Despachos e Transferências");
		this.addColuna("Lotação", 20, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Expedientes recebidos", 20, RelatorioRapido.ESQUERDA,
				false);
		this.addColuna("Expedientes transferidos", 20,
				RelatorioRapido.ESQUERDA, false);
		this.addColuna("Processos recebidos", 20, RelatorioRapido.ESQUERDA,
				false);
		this.addColuna("Processos transferidos", 20, RelatorioRapido.ESQUERDA,
				false);
		return this;

	}

	@Override
	public Collection processarDados() throws Exception {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		List<String> d = new ArrayList<String>();

		Query query = HibernateUtil
				.getSessao()
				.createQuery(
						"select mov.lotaCadastrante.siglaLotacao, doc.exFormaDocumento.exTipoFormaDoc.descTipoFormaDoc, "
								+ "mov.exTipoMovimentacao.descrTipoMovimentacao, count(distinct doc.idDoc) "
								+ "from ExMovimentacao mov inner join mov.exMobil mob "
								+ "inner join mob.exDocumento doc "
								+ "where doc.orgaoUsuario = :orgaoUsu "
								+ "and mov.exTipoMovimentacao in (3,4,6) "
								+ "and mov.exMovimentacaoCanceladora is null "
								+ "and doc.exFormaDocumento in (1,2) "
								+ "and mov.dtIniMov between :dtini and :dtfim "
								+ "group by mov.lotaCadastrante.siglaLotacao, "
								+ "doc.exFormaDocumento.exTipoFormaDoc.descTipoFormaDoc, mov.exTipoMovimentacao.descrTipoMovimentacao");

		Long orgaoUsu = Long.valueOf((String) parametros.get("orgao"));
		query.setLong("orgaoUsu", orgaoUsu);
		Date dtini = formatter.parse((String) parametros.get("dataInicial"));
		query.setDate("dtini", dtini);
		Date dtfim = formatter.parse((String) parametros.get("dataFinal"));
		query.setDate("dtfim", dtfim);

		SortedSet<String> set = new TreeSet<String>();
		Map<String, Long> map = new TreeMap<String, Long>();

		Iterator it = query.list().iterator();
		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			String lotacao = (String) obj[0];
			String tipodoc = (String) obj[1];
			String tipomov = (String) obj[2];
			Long totaldesp = Long.valueOf(obj[3].toString());
			set.add(lotacao);
			map.put(chave(lotacao, tipodoc, tipomov), totaldesp);
		}
		for (String s : set) {
			d.add(s);
			acrescentarColuna(d, map, s, "Expediente", "Recebimento");
			acrescentarColuna(d, map, s, "Expediente", "Transferência"); 
			acrescentarColuna(d, map, s, "Processo Administrativo",
			"Recebimento");
			acrescentarColuna(d, map, s, "Processo Administrativo",
					"Transferência");
		}
		return d;
	}

	private void acrescentarColuna(List<String> d, Map<String, Long> map,
			String s, String tipodoc, String tipomov) {
		Long l = 0L;
		String key = chave(s, tipodoc, tipomov);
		if (map.containsKey(key))
			l += map.get(key);

		if (tipomov.equals("Transferência")) {
			key = chave(s, tipodoc, "Despacho com Transferência");
			if (map.containsKey(key))
				l += map.get(key);
		}
		if (l > 0)
			d.add(l.toString());
		else
			d.add("");
	}

	private String chave(String lotacao, String tipodoc, String tipomov) {
		return lotacao + tipodoc + tipomov;
	}
}
