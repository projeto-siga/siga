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

public class RelDocumentosDevolucaoProgramada extends RelatorioTemplate {

	public List<String> listColunas;
	public List<String> listDados;
	public Long totalDocumentos;

	public RelDocumentosDevolucaoProgramada(Map parametros) throws DJBuilderException {
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
		this.listColunas.add("Vencido");
		this.listColunas.add("Qtde de documentos");

		this.addColuna("Unidade", 25, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Nome do Documento", 45, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Vencido", 25, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Qtde de documentos", 25, RelatorioRapido.ESQUERDA,
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
			listDados.add(formato.format(obj[2]));
			listDados.add(obj[3].toString());
			d.add(lotaDoc);
			d.add(modeloDoc);
			d.add(formato.format(obj[2]));
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
			queryLotacao = " AND SIGA.EX_MOVIMENTACAO.ID_LOTA_CADASTRANTE IN ( SELECT CORPORATIVO.DP_LOTACAO.ID_LOTACAO FROM CORPORATIVO.DP_LOTACAO WHERE CORPORATIVO.DP_LOTACAO.ID_LOTACAO_INI = :lotacao ) ";
		}

		String queryUsuario = "";
		if (parametros.get("usuario") != null
				&& parametros.get("usuario") != "") {
			queryUsuario = " AND CORPORATIVO.DP_PESSOA.ID_PESSOA_INICIAL IN ( SELECT CORPORATIVO.DP_PESSOA.ID_PESSOA FROM CORPORATIVO.DP_PESSOA WHERE CORPORATIVO.DP_PESSOA.ID_PESSOA_INICIAL = :usuario ) ";
		}

		Query query = HibernateUtil
				.getSessao()
				.createSQLQuery(
						"SELECT CORPORATIVO.DP_LOTACAO.SIGLA_LOTACAO,"
								+ " SIGA.EX_MODELO.NM_MOD,"
								+ " TABELA_TEMP3.DT_FIM_MOV,"
								+ " COUNT(SIGA.EX_DOCUMENTO.ID_DOC)"
								+ " FROM ( SELECT SIGA.EX_MOVIMENTACAO.ID_MOBIL,"
								+ " SIGA.EX_MOVIMENTACAO.ID_MOV,"
								+ " SIGA.EX_MOVIMENTACAO.DT_FIM_MOV,"
								+ " SIGA.EX_MOVIMENTACAO.ID_CADASTRANTE"
								+ " FROM SIGA.EX_MOVIMENTACAO"
								+ " INNER JOIN ( SELECT TABELA_TEMP1.ID_MOBIL, "
								+ " MAX(TABELA_TEMP1.ID_MOV) ID_MOV"
								+ " FROM ( SELECT SIGA.EX_MOVIMENTACAO.ID_MOBIL,"
								+ " SIGA.EX_MOVIMENTACAO.ID_MOV"
								+ " FROM SIGA.EX_MOVIMENTACAO"
								+ " INNER JOIN CORPORATIVO.DP_PESSOA"
								+ " ON CORPORATIVO.DP_PESSOA.ID_PESSOA = SIGA.EX_MOVIMENTACAO.ID_CADASTRANTE"
								+ " WHERE SIGA.EX_MOVIMENTACAO.ID_TP_MOV = 3"
								+ queryLotacao
								+ queryUsuario
								+ " AND SIGA.EX_MOVIMENTACAO.ID_MOBIL IN ( SELECT SIGA.EX_MOVIMENTACAO.ID_MOBIL"
								+ " FROM SIGA.EX_MOVIMENTACAO"
								+ " INNER JOIN CORPORATIVO.DP_PESSOA"
								+ " ON CORPORATIVO.DP_PESSOA.ID_PESSOA = SIGA.EX_MOVIMENTACAO.ID_CADASTRANTE"
								+ " WHERE SIGA.EX_MOVIMENTACAO.DT_FIM_MOV >= :dtini "
								+ " AND SIGA.EX_MOVIMENTACAO.DT_FIM_MOV < :dtfim "
								+ " AND SIGA.EX_MOVIMENTACAO.ID_TP_MOV = 3"
								+ queryLotacao
								+ queryUsuario
								+ " GROUP BY SIGA.EX_MOVIMENTACAO.ID_MOBIL  ) ) TABELA_TEMP1"
								+ " GROUP BY TABELA_TEMP1.ID_MOBIL ) TABELA_TEMP2"
								+ " ON SIGA.EX_MOVIMENTACAO.ID_MOBIL = TABELA_TEMP2.ID_MOBIL"
								+ " AND SIGA.EX_MOVIMENTACAO.ID_MOV = TABELA_TEMP2.ID_MOV"
								+ " WHERE SIGA.EX_MOVIMENTACAO.DT_FIM_MOV IS NOT NULL ) TABELA_TEMP3"
								+ " INNER JOIN SIGA.EX_MOBIL"
								+ " ON SIGA.EX_MOBIL.ID_MOBIL = TABELA_TEMP3.ID_MOBIL"
								+ " INNER JOIN SIGA.EX_DOCUMENTO"
								+ " ON SIGA.EX_DOCUMENTO.ID_DOC = SIGA.EX_MOBIL.ID_DOC"
								+ " INNER JOIN SIGA.EX_MODELO"
								+ " ON SIGA.EX_MODELO.ID_MOD = SIGA.EX_DOCUMENTO.ID_MOD"
								+ " INNER JOIN CORPORATIVO.DP_LOTACAO"
								+ " ON CORPORATIVO.DP_LOTACAO.ID_LOTACAO = SIGA.EX_DOCUMENTO.ID_LOTA_CADASTRANTE"
								+ " INNER JOIN CORPORATIVO.DP_PESSOA"
								+ " ON CORPORATIVO.DP_PESSOA.ID_PESSOA = TABELA_TEMP3.ID_CADASTRANTE"
								+ " WHERE SIGA.EX_DOCUMENTO.ID_ORGAO_USU = :orgao "
								+ " AND DT_FIM_MOV >= :dtini "
								+ " AND DT_FIM_MOV < :dtfim "
								+ " GROUP BY CORPORATIVO.DP_LOTACAO.SIGLA_LOTACAO,"
								+ " SIGA.EX_MODELO.NM_MOD,"
								+ " TABELA_TEMP3.DT_FIM_MOV"
								+ " ORDER BY 1 ASC, 2 ASC, 3 ASC, 4 ASC");

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

		Date dtini = formatter.parse((String) parametros.get("dataInicial"));
		query.setDate("dtini", dtini);
		Date dtfim = formatter.parse((String) parametros.get("dataFinal"));
		query.setDate("dtfim", dtfim);

		Iterator it = query.list().iterator();
		return it;
	}
}