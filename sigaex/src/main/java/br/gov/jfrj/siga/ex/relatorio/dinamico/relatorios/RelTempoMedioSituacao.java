package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

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
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class RelTempoMedioSituacao extends RelatorioTemplate {

	public List<String> listColunas;
	public List<String> listDados;
	public Long totalDocumentos;

	public RelTempoMedioSituacao(Map parametros) throws DJBuilderException {
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

		this.setTitle("Tempo Médio por Situação");
		this.listColunas.add("Unidade");
		this.listColunas.add("Nome do Documento");
		this.listColunas.add("Situação");
		this.listColunas.add("Tempo médio em dias");

		this.addColuna("Unidade", 25, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Nome do Documento", 45, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Situação", 25, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Tempo médio em dias", 25, RelatorioRapido.ESQUERDA,
				false);
		return this;

	}

	@Override
	public Collection processarDados() throws Exception {

		List<String> d = new ArrayList<String>();

		Iterator it = obtemDados(d);
		totalDocumentos = 0L;
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			String lotaDoc = (String) obj[0];
			String modeloDoc = (String) obj[1];
			listDados.add(lotaDoc);
			listDados.add(modeloDoc);
			listDados.add(obj[2].toString());
			listDados.add(obj[3].toString());
			d.add(lotaDoc);
			d.add(modeloDoc);
			d.add(obj[2].toString());
			d.add(obj[3].toString());
			Long docs = Long.valueOf(obj[3].toString());
			totalDocumentos = totalDocumentos + docs;

		}
		if (d.size() == 0) {
			throw new Exception(
					"Não foram encontrados documentos para os dados informados.");
		}

		return d;
	}

	private Iterator obtemDados(List<String> d) throws ParseException {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		String queryLotacao = "";
		if (parametros.get("lotacao") != null
				&& parametros.get("lotacao") != "") {
			queryLotacao = " AND INICIO_MARCACAO.ID_LOTA_CADASTRANTE IN ( SELECT CORPORATIVO.DP_LOTACAO.ID_LOTACAO FROM CORPORATIVO.DP_LOTACAO WHERE CORPORATIVO.DP_LOTACAO.ID_LOTACAO_INI = :lotacao ) ";
		}

		String queryUsuario = "";
		if (parametros.get("usuario") != null
				&& parametros.get("usuario") != "") {
			queryUsuario = " AND CORPORATIVO.DP_PESSOA.ID_PESSOA_INICIAL IN ( SELECT CORPORATIVO.DP_PESSOA.ID_PESSOA FROM CORPORATIVO.DP_PESSOA WHERE CORPORATIVO.DP_PESSOA.ID_PESSOA_INICIAL = :usuario ) ";
		}

		Query query = HibernateUtil
				.getSessao()
				.createSQLQuery(
						"SELECT SIGLA_LOTACAO, "
								+ " NM_MOD, "
								+ " SITUACAO, "
								+ " CEIL(SUM(MEDIA) / COUNT(*)) MEDIA "
								+ " FROM ( "
								+ " SELECT SIGLA_LOTACAO, "
								+ " ID_DOC, "
								+ " NM_MOD, "
								+ " 'URGENTE' SITUACAO, "
								+ " SUM(DIAS) TOTAL_DIAS, "
								+ " (COUNT(*) * 2) QTD_MOVIMENTACOES, "
								+ " CEIL(SUM(DIAS) / (COUNT(*) * 2)) MEDIA "
								+ " FROM ( SELECT (CORPORATIVO.DP_LOTACAO.SIGLA_LOTACAO || ' - ' || CORPORATIVO.DP_LOTACAO.NOME_LOTACAO) SIGLA_LOTACAO, "
								+ " SIGA.EX_DOCUMENTO.ID_DOC, "
								+ " SIGA.EX_MODELO.NM_MOD, "
								+ " (trunc(FIM_MARCACAO.DT_MOV) - trunc(INICIO_MARCACAO.DT_MOV)) dias "
								+ " FROM SIGA.EX_MOVIMENTACAO INICIO_MARCACAO "
								+ " INNER JOIN SIGA.EX_MOVIMENTACAO FIM_MARCACAO "
								+ " ON INICIO_MARCACAO.ID_MOV_CANCELADORA = FIM_MARCACAO.ID_MOV "
								+ " AND INICIO_MARCACAO.ID_MOV = FIM_MARCACAO.ID_MOV_REF "
								+ " INNER JOIN CORPORATIVO.DP_PESSOA "
								+ " ON CORPORATIVO.DP_PESSOA.ID_PESSOA = INICIO_MARCACAO.ID_CADASTRANTE "
								+ " INNER JOIN SIGA.EX_MOBIL "
								+ " ON SIGA.EX_MOBIL.ID_MOBIL = INICIO_MARCACAO.ID_MOBIL "
								+ " INNER JOIN SIGA.EX_DOCUMENTO "
								+ " ON SIGA.EX_DOCUMENTO.ID_DOC = SIGA.EX_MOBIL.ID_DOC "
								+ " INNER JOIN SIGA.EX_MODELO "
								+ " ON SIGA.EX_MODELO.ID_MOD = SIGA.EX_DOCUMENTO.ID_MOD "
								+ " INNER JOIN CORPORATIVO.DP_LOTACAO "
								+ " ON CORPORATIVO.DP_LOTACAO.ID_LOTACAO = SIGA.EX_DOCUMENTO.ID_LOTA_CADASTRANTE "
								+ " WHERE INICIO_MARCACAO.ID_TP_MOV = 62 "
								+ " AND FIM_MARCACAO.ID_TP_MOV = 14 "
								+ " AND INICIO_MARCACAO.ID_MARCADOR = 1000 "
								+ " AND (trunc(FIM_MARCACAO.DT_MOV) - trunc(INICIO_MARCACAO.DT_MOV)) > 0 "
								+ " AND SIGA.EX_DOCUMENTO.ID_ORGAO_USU = :orgao "
								+ " AND SIGA.EX_DOCUMENTO.DT_DOC >= :dtini "
								+ " AND SIGA.EX_DOCUMENTO.DT_DOC <= :dtfim	"
								+ queryLotacao
								+ queryUsuario
								+ " ) "
								+ " GROUP BY SIGLA_LOTACAO, "
								+ " ID_DOC, "
								+ " NM_MOD "
								+ " UNION "
								+ " SELECT SIGLA_LOTACAO, "
								+ " ID_DOC, "
								+ " NM_MOD, "
								+ " 'CAIXA DE ENTRADA' SITUACAO, "
								+ " SUM(DIAS) TOTAL_DIAS, "
								+ " (COUNT(*) * 2) QTD_MOVIMENTACOES, "
								+ " CEIL(SUM(DIAS) / (COUNT(*) * 2)) MEDIA "
								+ " FROM (SELECT (CORPORATIVO.DP_LOTACAO.SIGLA_LOTACAO || ' - ' || CORPORATIVO.DP_LOTACAO.NOME_LOTACAO) SIGLA_LOTACAO,"
								+ " SIGA.EX_DOCUMENTO.ID_DOC, "
								+ " SIGA.EX_MODELO.NM_MOD, "
								+ " (trunc(FIM_MARCACAO.DT_MOV) - trunc(INICIO_MARCACAO.DT_MOV)) dias "
								+ " FROM SIGA.EX_MOVIMENTACAO INICIO_MARCACAO "
								+ " INNER JOIN SIGA.EX_MOVIMENTACAO FIM_MARCACAO "
								+ " ON INICIO_MARCACAO.ID_MOBIL = FIM_MARCACAO.ID_MOBIL "
								+ " INNER JOIN CORPORATIVO.DP_PESSOA "
								+ " ON CORPORATIVO.DP_PESSOA.ID_PESSOA = INICIO_MARCACAO.ID_CADASTRANTE "
								+ " INNER JOIN SIGA.EX_MOBIL "
								+ " ON SIGA.EX_MOBIL.ID_MOBIL = INICIO_MARCACAO.ID_MOBIL "
								+ " INNER JOIN SIGA.EX_DOCUMENTO "
								+ " ON SIGA.EX_DOCUMENTO.ID_DOC = SIGA.EX_MOBIL.ID_DOC "
								+ " INNER JOIN SIGA.EX_MODELO "
								+ " ON SIGA.EX_MODELO.ID_MOD = SIGA.EX_DOCUMENTO.ID_MOD "
								+ " INNER JOIN CORPORATIVO.DP_LOTACAO "
								+ " ON CORPORATIVO.DP_LOTACAO.ID_LOTACAO = SIGA.EX_DOCUMENTO.ID_LOTA_CADASTRANTE "
								+ " WHERE INICIO_MARCACAO.ID_TP_MOV = 3 "
								+ " AND FIM_MARCACAO.ID_TP_MOV = 4 "
								+ " AND (trunc(FIM_MARCACAO.DT_MOV) - trunc(INICIO_MARCACAO.DT_MOV)) > 0 "
								+ " AND SIGA.EX_DOCUMENTO.ID_ORGAO_USU = :orgao "
								+ " AND SIGA.EX_DOCUMENTO.DT_DOC >= :dtini "
								+ " AND SIGA.EX_DOCUMENTO.DT_DOC <= :dtfim	"
								+ queryLotacao
								+ queryUsuario
								+ " ) "
								+ " GROUP BY SIGLA_LOTACAO, "
								+ " ID_DOC, "
								+ " NM_MOD "
								+ " ) "
								+ " GROUP BY SIGLA_LOTACAO, "
								+ " NM_MOD, "
								+ " SITUACAO ORDER BY 4 DESC " );

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

		query.setLong("orgao", Long.valueOf((String) parametros.get("orgao")));

		/*
		Date dtini = formatter.parse((String) parametros.get("dataInicial"));
		query.setDate("dtini", dtini);
		Date dtfim = formatter.parse((String) parametros.get("dataFinal"));
		query.setDate("dtfim", dtfim);
*/
		
		query.setString("dtini", (String) parametros.get("dataInicial"));
		query.setString("dtfim", (String) parametros.get("dataFinal"));
		
		Iterator it = query.list().iterator();
		return it;
	}
}