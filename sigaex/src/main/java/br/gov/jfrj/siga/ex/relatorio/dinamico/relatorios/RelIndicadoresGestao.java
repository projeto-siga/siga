package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import net.sf.jasperreports.engine.JRException;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class RelIndicadoresGestao extends RelatorioTemplate {

	public List<String> listColunas;
	public List<List<String>> listLinhas;
	public Long totalDocumentos;
	public Long totalPaginas;
	public Long totalTramitados;

	public RelIndicadoresGestao(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("dataInicial") == null) {
			throw new DJBuilderException("Parâmetro dataInicial não informado!");
		}
		if (parametros.get("dataFinal") == null) {
			throw new DJBuilderException("Parâmetro dataFinal não informado!");
		}
		listColunas = new ArrayList<String>();
		listLinhas = new ArrayList<List<String>>();
		
	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException {

		estiloTituloColuna.setBackgroundColor(Color.white);
//		estiloTituloColuna.setBorderBottom(Border.NO_BORDER());
//		estiloTituloGrupo.setBorderTop(Border.NO_BORDER());
		estiloTituloGrupo.setPaddingTop(10);
		estiloTituloGrupo.setHorizontalAlign(HorizontalAlign.CENTER);
//		estiloTituloGrupo.setBackgroundColor(Color.gray);
//		estiloTituloGrupo.setTextColor(Color.white);
		this.addColuna("", 140, RelatorioRapido.CENTRO, true);
		this.addColuna("", 90, RelatorioRapido.ESQUERDA, false);
		this.addColuna("", 50, RelatorioRapido.ESQUERDA, false);
		
		return this;
	}

	@Override
	public Collection processarDados() throws Exception {

		List<String> d = new ArrayList<String>();
		
		final RelDocumentosProduzidos rel = new RelDocumentosProduzidos(
				parametros);
		rel.gerar();
		rel.processarDadosTramitados();
		
		d.add("Indicadores de Produção");
		d.add("Total de Documentos Produzidos");
		d.add(rel.totalDocumentos.toString());
		
		d.add("Indicadores de Produção");
		d.add("Total de Páginas Geradas");
		d.add( rel.totalPaginas.toString());
		
		d.add("Indicadores de Produção");
		d.add("Total de Documentos Tramitados");
		d.add(rel.totalTramitados.toString());
		parametros.get("orgao");
		String id = parametros.get("orgao").toString();
		String nome = parametros.get("orgaoUsuario").toString();
		parametros.put("orgaoUsuario", id);
		final RelVolumeTramitacao relVol = new RelVolumeTramitacao(
				parametros);
		
		relVol.gerar();
		parametros.put("orgaoUsuario", nome);
		
		for (int i = 0; i < relVol.listColunas.size(); i++) {
			
			d.add("Documentos Por Volume de Tramitação (Top 5)");
			d.add(relVol.listColunas.get(i));
			d.add(relVol.listDados.get(i));
			
		}
		
		return d;
	}
	
	public void processarDadosTramitados() throws Exception {
		List<String> d = new ArrayList<String>();

		// Total de documentos tramitadas pelo menos uma vez (por lotação ou
		// usuário)
		String addSelect = "count(distinct doc.idDoc) ";
		String addWhere = "and (mov.exTipoMovimentacao.idTpMov = "
				+ Long.valueOf(ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA) 
 				+ " or mov.exTipoMovimentacao.idTpMov = " 
				+ Long.valueOf(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA) 
				+ " or mov.exTipoMovimentacao.idTpMov = "  
				+ Long.valueOf(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA) 
				+ " or mov.exTipoMovimentacao.idTpMov = "  
				+ Long.valueOf(ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA) 
				+ ") and mov.exMovimentacaoCanceladora is null ";
		Iterator it = obtemDados(d, addSelect, addWhere);

		if (it.hasNext()) {
			totalTramitados = (Long) it.next();
		} else {
			totalTramitados = 0L;
		}
	}

	
	private Iterator obtemDados(List<String> d, String addSelect,
			String addWhere) throws ParseException {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		String queryOrgao = "";
		if (parametros.get("orgao") != null && !"".equals(parametros.get("orgao"))) {
			queryOrgao = "and doc.orgaoUsuario.idOrgaoUsu = :orgao ";
		}

		String queryLotacao = "";
		if (parametros.get("lotacao") != null
				&& !"".equals(parametros.get("lotacao"))) {
			queryLotacao = "and doc.lotaCadastrante.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :lotacao) ";
		}

		String queryUsuario = "";
		if (parametros.get("usuario") != null
				&& !"".equals(parametros.get("usuario"))) {
			queryUsuario = "and doc.cadastrante.idPessoa in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :usuario) ";
		}

		Query query = ContextoPersistencia.em().createQuery(
				"select " + addSelect
						+ "from ExMovimentacao mov inner join mov.exMobil mob "
						+ "inner join mob.exDocumento doc "
						+ "where doc.dtRegDoc >= :dtini and doc.dtRegDoc < :dtfim "
						+ queryOrgao + queryLotacao + queryUsuario + addWhere);

		if (parametros.get("orgao") != null && !"".equals(parametros.get("orgao"))) {
			query.setParameter("orgao",
					Long.valueOf((String) parametros.get("orgao")));
		}

		if (parametros.get("lotacao") != null
				&& !"".equals(parametros.get("lotacao"))) {
			Query qryLota = ContextoPersistencia.em().createQuery(
					"from DpLotacao lot where lot.idLotacao = "
							+ parametros.get("lotacao"));

			Set<DpLotacao> lotacaoSet = new HashSet<DpLotacao>();
			DpLotacao lotacao = (DpLotacao) qryLota.getResultList().get(0);
			lotacaoSet.add(lotacao);

			query.setParameter("lotacao", lotacao.getIdInicial());
		}

		if (parametros.get("usuario") != null
				&& !"".equals(parametros.get("usuario"))) {
			Query qryPes = ContextoPersistencia.em().createQuery(
					"from DpPessoa pes where pes.idPessoa = "
							+ parametros.get("usuario"));

			Set<DpPessoa> pessoaSet = new HashSet<DpPessoa>();
			DpPessoa pessoa = (DpPessoa) qryPes.getResultList().get(0);
			pessoaSet.add(pessoa);

			query.setParameter("usuario", pessoa.getIdPessoaIni());
		}

		Date dtini = formatter.parse((String) parametros.get("dataInicial"));
		query.setParameter("dtini", dtini);
		Date dtfim = formatter.parse((String) parametros.get("dataFinal"));
		Date dtfimMaisUm = new Date( dtfim.getTime() + 86400000L );
		query.setParameter("dtfim", dtfimMaisUm);
		
		Iterator it = query.getResultList().iterator();
		return it;
	}

}