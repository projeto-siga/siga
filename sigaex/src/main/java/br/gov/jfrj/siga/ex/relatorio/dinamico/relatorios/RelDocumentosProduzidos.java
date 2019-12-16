package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

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

import net.sf.jasperreports.engine.JRException;

import org.hibernate.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class RelDocumentosProduzidos extends RelatorioTemplate {

	public List<String> listColunas;
	public List<String> listDados;
	public Long totalDocumentos;
	public Long totalPaginas;
	public Long totalTramitados;

	public RelDocumentosProduzidos(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("dataInicial") == null) {
			throw new DJBuilderException("Parâmetro dataInicial não informado!");
		}
		if (parametros.get("dataFinal") == null) {
			throw new DJBuilderException("Parâmetro dataFinal não informado!");
		}
		listColunas = new ArrayList<String>();
		listDados = new ArrayList<String>();
	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException {

		this.setTitle("Indicadores de Produção");
		this.listColunas.add("Unidade");
		this.listColunas.add("Nome do Documento");
		this.listColunas.add("Número do Documento");

		this.addColuna("Unidade", 25, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Nome do Documento", 45, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Número do Documento", 25, RelatorioRapido.ESQUERDA,
				false);
		return this;

	}

	@Override
	public Collection processarDados() throws Exception {

		List<String> d = new ArrayList<String>();

		// Total de documentos criados (por orgao, lotação ou usuário)
		String addSelect = "doc.lotaCadastrante.siglaLotacao, "
				+ "doc.exModelo.nmMod, " + "count(doc.idDoc) as qtd, "
				+ "sum(doc.numPaginas) ";
		String addWhere = "group by doc.lotaCadastrante.siglaLotacao,"
				+ "doc.exModelo.nmMod" + " order by 1, 3";
		Iterator it = obtemDados(d, addSelect, addWhere,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO);
		totalPaginas = 0L;
		totalDocumentos = 0L;

		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			String lotaDoc = (String) obj[0];
			String modeloDoc = (String) obj[1];
			listDados.add(lotaDoc);
			listDados.add(modeloDoc);
			listDados.add(obj[2].toString());
			d.add(lotaDoc);
			d.add(modeloDoc);
			d.add(obj[2].toString());
			Long docs = Long.valueOf(obj[2].toString());
			Long pags = Long.valueOf(obj[3].toString());
			totalDocumentos = totalDocumentos + docs;
			totalPaginas = totalPaginas + pags;

		}
		if (d.size() == 0) {
			throw new Exception(
					"Não foram encontrados documentos para os dados informados.");
		}

		return d;
	}

	public void gerarDetalhes() throws Exception {
		relatorio = configurarRelatorioDetalhes();
		Collection dados = processarDadosDetalhes();
		if (dados != null && dados.size() > 0) {
			relatorio.setDados(dados);
		} else {
			throw new Exception("Não há dados para gerar o relatório!");
		}

	}

	private AbstractRelatorioBaseBuilder configurarRelatorioDetalhes()
			throws DJBuilderException, JRException {
		
		this.listColunas.add("Unidade");
		this.listColunas.add("Nome do Documento");
		this.listColunas.add("Número do Documento");

		this.addColuna("Unidade", 20, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Nome do Documento", 60, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Número do Documento", 30, RelatorioRapido.ESQUERDA,
				false);
		return this;

	}

	private Collection processarDadosDetalhes() throws Exception {

		List<String> d = new ArrayList<String>();
		ExDao dao = ExDao.getInstance();
		ExMobil mob = null;
		String siglaDoc = "";

		String addSelect = "distinct doc.lotaCadastrante.siglaLotacao, "
				+ "doc.exModelo.nmMod, " + "mob.idMobil, " + "doc.numPaginas ";

		String addWhere = "order by doc.lotaCadastrante.siglaLotacao, "
				+ "doc.exModelo.nmMod, " + "mob.idMobil ";

		Iterator it = obtemDados(d, addSelect, addWhere,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO);
		totalPaginas = 0L;
		totalDocumentos = 0L;

		String lotacaoAnterior = "";
		
		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();

			String lotaDoc = (String) obj[0];
			String modeloDoc = (String) obj[1];
			BigDecimal idMobil = new BigDecimal(obj[2].toString());
			if (idMobil != null) {
				mob = dao.consultar(new Long(idMobil.longValue()),
						ExMobil.class, false);
				siglaDoc = mob.getSigla();
			}

			Long pags = (long) 0;

			if (obj[3] != null) {
				pags = Long.valueOf(obj[3].toString());
			}

			if(!lotacaoAnterior.equals(lotaDoc)){
				listDados.add(lotaDoc);
				d.add(lotaDoc);
				lotacaoAnterior = lotaDoc;
			} else {
				listDados.add(" ");
				d.add(" ");
			}
			listDados.add(modeloDoc);
			listDados.add(siglaDoc);
			
			d.add(modeloDoc);
			d.add(siglaDoc);

			totalDocumentos = totalDocumentos + 1;
			totalPaginas = totalPaginas + pags;
		}
		if (d.size() == 0) {
			throw new Exception(
					"Não foram encontrados documentos para os dados informados.");
		}

		return d;
	}

	public void processarDadosTramitados() throws Exception {
		List<String> d = new ArrayList<String>();

		// Total de documentos tramitadas pelo menos uma vez (por lotação ou
		// usuário)
		String addSelect = "count(doc.idDoc) ";
		String addWhere = "";
		Iterator it = obtemDados(d, addSelect, addWhere,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA);

		if (it.hasNext()) {
			totalTramitados = (Long) it.next();
		} else {
			totalTramitados = 0L;
		}
	}

	private Iterator obtemDados(List<String> d, String addSelect,
			String addWhere, Long idTpMov) throws ParseException {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		String queryOrgao = "";
		if (parametros.get("orgao") != null && parametros.get("orgao") != "") {
			queryOrgao = "and doc.orgaoUsuario.idOrgaoUsu = :orgao ";
		}

		String queryLotacao = "";
		if (parametros.get("lotacao") != null
				&& parametros.get("lotacao") != "") {
			queryLotacao = "and mov.lotaCadastrante.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :lotacao) ";
		}

		String queryUsuario = "";
		if (parametros.get("usuario") != null
				&& parametros.get("usuario") != "") {
			queryUsuario = "and mov.cadastrante.idPessoaIni in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :usuario) ";
		}

		Query query = HibernateUtil.getSessao().createQuery(
				"select " + addSelect
						+ "from ExMovimentacao mov inner join mov.exMobil mob "
						+ "inner join mob.exDocumento doc "
						+ "where mov.dtIniMov between :dtini and :dtfim "
						+ "and mov.exTipoMovimentacao.idTpMov = :idTpMov "
						+ queryOrgao + queryLotacao + queryUsuario + addWhere);

		query.setParameter("idTpMov", idTpMov);

		if (parametros.get("orgao") != null && parametros.get("orgao") != "") {
			query.setLong("orgao",
					Long.valueOf((String) parametros.get("orgao")));
		}

		if (parametros.get("lotacao") != null
				&& parametros.get("lotacao") != "") {
			Query qryLota = HibernateUtil.getSessao().createQuery(
					"from DpLotacao lot where lot.idLotacao = "
							+ parametros.get("lotacao"));

			Set<DpLotacao> lotacaoSet = new HashSet<DpLotacao>();
			DpLotacao lotacao = (DpLotacao) qryLota.list().get(0);
			lotacaoSet.add(lotacao);

			query.setParameter("lotacao", lotacao.getIdInicial());
		}

		if (parametros.get("usuario") != null
				&& parametros.get("usuario") != "") {
			Query qryPes = HibernateUtil.getSessao().createQuery(
					"from DpPessoa pes where pes.idPessoa = "
							+ parametros.get("usuario"));

			Set<DpPessoa> pessoaSet = new HashSet<DpPessoa>();
			DpPessoa pessoa = (DpPessoa) qryPes.list().get(0);
			pessoaSet.add(pessoa);

			query.setParameter("usuario", pessoa.getIdPessoaIni());
		}

		Date dtini = formatter.parse((String) parametros.get("dataInicial"));
		query.setDate("dtini", dtini);
		Date dtfim = formatter.parse((String) parametros.get("dataFinal"));
		query.setDate("dtfim", dtfim);

		Iterator it = query.list().iterator();
		return it;
	}

}