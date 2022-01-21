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

import javax.persistence.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeExibirQuemTemAcessoAoDocumento;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import net.sf.jasperreports.engine.JRException;

public class RelDocumentosForaPrazo extends RelatorioTemplate {

	public List<String> listColunas;
	public List<List<String>> listDocs;
	public List<List<String>> listModelos;
	public Long totalDocs;

	public RelDocumentosForaPrazo(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("dataInicial") == null) {
			throw new DJBuilderException("Parâmetro dataInicial não informado!");
		}
		if (parametros.get("dataFinal") == null) {
			throw new DJBuilderException("Parâmetro dataFinal não informado!");
		}
		listColunas = new ArrayList<String>();
		listModelos = new ArrayList<List<String>>();
		listDocs = new ArrayList<List<String>>();
	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException {

		this.listColunas.add("Unidade");
		this.listColunas.add("Nome do Documento");
		this.listColunas.add(parametros.get("nomeColuna") == null ? "Data Vencida" : parametros.get("nomeColuna").toString());
		this.listColunas.add("Qtde de documentos");
		
		this.estiloColuna.setVerticalAlign(VerticalAlign.MIDDLE);

		this.addColuna("Unidade", 25, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Nome do Documento", 50, RelatorioRapido.ESQUERDA, false);
		this.addColuna(parametros.get("nomeColuna") == null ? "Data Vencida" : parametros.get("nomeColuna").toString(),
				15, RelatorioRapido.CENTRO, false);
		this.addColuna("Qtde de documentos", 20, RelatorioRapido.DIREITA, false);
		return this;

	}

	@Override
	public Collection processarDados() throws Exception {

		List<String> d = new ArrayList<String>();
		
		Query qryLotacaoTitular = ContextoPersistencia.em().createQuery(
				"from DpLotacao lot " + "where lot.dataFimLotacao is null "
						+ "and lot.orgaoUsuario = "
						+ parametros.get("orgao")
						+ " and lot.siglaLotacao = '"
						+ parametros.get("lotacaoTitular") + "'");
		DpLotacao lotaTitular = (DpLotacao) qryLotacaoTitular.getSingleResult();

		DpPessoa titular = ExDao.getInstance().consultar(
				new Long((String) parametros.get("idTit")), DpPessoa.class,
				false);

		String querySelect = " LOTA.SIGLA_LOTACAO,"
				+ " LOTA.NOME_LOTACAO,"
				+ " LOTA.ID_LOTACAO,"
				+ " DOC.ID_MOD,"
				+ " MD.NM_MOD,"
				+ " TABELA_TEMP3.DT_FIM_MOV,"
				+ " COUNT(DOC.ID_DOC)";
	
		String queryGroupBy = " GROUP BY LOTA.SIGLA_LOTACAO,"
				+ " LOTA.NOME_LOTACAO,"
				+ " LOTA.ID_LOTACAO,"
				+ " DOC.ID_MOD,"
				+ " MD.NM_MOD,"
				+ " TABELA_TEMP3.DT_FIM_MOV"
				+ " ORDER BY 1 ASC, 2 ASC, 3 ASC, 4 ASC, 5 ASC";
		
		Iterator it = obtemDados(d, querySelect, queryGroupBy);
		totalDocs = 0L;
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String lotaAnt = "";

		Object[] obj = null;
		String lotaDoc = "";
		String lotaNomeDoc = "";
		String idLotaResp = "";
		String idMod = "";
		ExModelo exModelo = null;
		String modeloDoc = "";
		StringBuffer linkModeloDoc = new StringBuffer();
		List<String> listDados = new ArrayList();
		Long docs = null;
		
		while (it.hasNext()) {
			obj = (Object[]) it.next();
			lotaDoc = (String) obj[0];
			lotaNomeDoc = (String) obj[1];
			idLotaResp = new BigDecimal(obj[2].toString()).toString();
			idMod = new BigDecimal(obj[3].toString()).toString();
			exModelo = new ExModelo();
			exModelo.setIdMod(Long.valueOf(idMod));
			if (Ex.getInstance().getBL().getComp().pode(ExPodeExibirQuemTemAcessoAoDocumento.class, 
					 titular, lotaTitular ,ExDao.getInstance().consultar(exModelo.getIdMod(),ExModelo.class, false)
							)) {
				modeloDoc = (String) obj[4];
				linkModeloDoc = new StringBuffer();
				if(parametros.get("link_siga") != "") {
					linkModeloDoc.append("<a href='#' class='text-primary' onclick=\"javascript:abreDetalhe('");
					linkModeloDoc.append(parametros.get("link_siga"));
					linkModeloDoc.append( "','");
					linkModeloDoc.append(modeloDoc);
					linkModeloDoc.append("','" );
					linkModeloDoc.append(lotaDoc);
					linkModeloDoc.append(" / ");
					linkModeloDoc.append(lotaNomeDoc);
					linkModeloDoc.append("','");
					linkModeloDoc.append(formato.format(obj[5]));
					linkModeloDoc.append("','");
					linkModeloDoc.append(idMod);
					linkModeloDoc.append("','");
					linkModeloDoc.append(idLotaResp);
					linkModeloDoc.append("');\">");
					linkModeloDoc.append(modeloDoc);
					linkModeloDoc.append("</a>");
				} else {
					linkModeloDoc.append(modeloDoc);
				}
				
				listDados = new ArrayList();
				if (!lotaDoc.equals(lotaAnt)) {
					listDados.add(lotaDoc + " / " + lotaNomeDoc);
					lotaAnt = lotaDoc;
				} else {
					listDados.add("");
				}
				listDados.add(linkModeloDoc.toString());
				listDados.add(formato.format(obj[5]));
				listDados.add(obj[6].toString());
				listModelos.add(listDados);
				docs = Long.valueOf(obj[6].toString());
				totalDocs = totalDocs + docs;
			}

		}
		for (List<String> lin : listModelos) {
			for (String dado : lin) {
				d.add(dado);
			}
		}
		if (d.size() == 0) {
			throw new Exception(
					"Não foram encontrados documentos para os dados informados.");
		}

		return d;
	}

	public void processarDadosDetalhe() throws Exception {

		List<String> d = new ArrayList<String>();

		String querySelect = " DOC.ID_DOC, " 
				+ " ORG.SIGLA_ORGAO_USU, " 
				+ " ORG.ACRONIMO_ORGAO_USU, " 
				+ " FRM.SIGLA_FORMA_DOC, " 
				+ " DOC.ANO_EMISSAO, "  
				+ " DOC.NUM_EXPEDIENTE, "  
				+ " MOB.NUM_SEQUENCIA, "  
				+ " MOB.ID_TIPO_MOBIL, "  
				+ " LOTARESP.SIGLA_LOTACAO || ' / ' || RESP.SESB_PESSOA || RESP.MATRICULA ";
	
		String queryGroupBy = " ORDER BY "
				+ " ORG.ACRONIMO_ORGAO_USU, " 
				+ " FRM.SIGLA_FORMA_DOC, " 
				+ " DOC.ANO_EMISSAO, "  
				+ " DOC.NUM_EXPEDIENTE, "  
				+ " MOB.NUM_SEQUENCIA "  
				;
		
		Iterator it = obtemDados(d, querySelect, queryGroupBy);
		totalDocs = 0L;
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			String siglaDoc = ExDocumento.getCodigo(Long.valueOf(obj[0].toString()), obj[1].toString(),
					obj[2].toString(), obj[3].toString(), Long.valueOf(obj[4].toString()),
					Long.valueOf(obj[5].toString()), Integer.valueOf(obj[6].toString()), Long.valueOf(obj[7].toString()), null, null,  
					null, null, null, null, null, null, null, null);
			String siglaMob = ExMobil.getSigla(siglaDoc, Integer.valueOf(obj[6].toString()), Long.valueOf(obj[7].toString()));
			List<String> listDados = new ArrayList();
			listDados.add(siglaMob);
			listDados.add(obj[8] != null ? obj[8].toString() : "");
			listDocs.add(listDados);
			totalDocs = totalDocs + 1;
		}
		if (listDocs.size() == 0) {
			throw new Exception(
					"Não foram encontrados documentos para os dados informados.");
		}
	}

	private Iterator obtemDados(List<String> d, String querySelect, String queryGroupBy) throws ParseException {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String queryLotacao = "";
		if (parametros.get("lotacao") != null
				&& !"".equals(parametros.get("lotacao"))) {
			queryLotacao = " AND DOC.ID_LOTA_CADASTRANTE IN ( SELECT LOTA.ID_LOTACAO FROM corporativo.dp_lotacao LOTA WHERE LOTA.ID_LOTACAO_INI = :lotacao ) ";
		}

		String queryUsuario = "";
		if (parametros.get("usuario") != null
				&& !"".equals(parametros.get("usuario"))) {
			queryUsuario = " AND DOC.ID_CADASTRANTE IN ( SELECT PES.ID_PESSOA FROM corporativo.dp_pessoa PES WHERE PES.ID_PESSOA_INICIAL = :usuario ) ";
		}

		String queryModelo = "";
		String queryJoin = "";
		if (parametros.get("idMod") != null && !"".equals(parametros.get("idMod"))) {
			queryModelo = " AND DOC.ID_MOD = :idMod "
					+ " AND DOC.ID_LOTA_CADASTRANTE = :idLotaResp ";
			queryJoin = " LEFT OUTER JOIN siga.ex_forma_documento FRM "
					+ " ON FRM.ID_FORMA_DOC = DOC.ID_FORMA_DOC "
					+ " LEFT OUTER JOIN corporativo.dp_pessoa RESP"
					+ " ON RESP.ID_PESSOA = TABELA_TEMP3.ID_RESP"
					+ " INNER JOIN corporativo.cp_orgao_usuario ORG "
					+ " ON ORG.ID_ORGAO_USU = DOC.ID_ORGAO_USU ";
		}

		Query query = ContextoPersistencia.em().createNativeQuery(
						"SELECT "
								+ querySelect
								+ " FROM ( SELECT MOV.ID_MOBIL,"
								+ " 	MOV.ID_MOV,"
								+ " 	MOV.DT_FIM_MOV,"
								+ " 	MOV.ID_CADASTRANTE,"
								+ " 	MOV.ID_LOTA_RESP,"
								+ " 	MOV.ID_RESP"
								+ " 	FROM siga.ex_movimentacao MOV"
								+ " 	INNER JOIN ( SELECT TABELA_TEMP1.ID_MOBIL, "
								+ " 		MAX(TABELA_TEMP1.ID_MOV) ID_MOV"
								+ " 		FROM ( SELECT MOV.ID_MOBIL,"
								+ " 			MOV.ID_MOV"
								+ " 			FROM siga.ex_movimentacao MOV"
								+ " 			WHERE (MOV.ID_TP_MOV = :idTpMov1"
								+ " 					OR MOV.ID_TP_MOV = :idTpMov2"
								+ " 					OR MOV.ID_TP_MOV = :idTpMov3"
								+ " 					OR MOV.ID_TP_MOV = :idTpMov4)"
								+ " 			AND MOV.ID_MOV_CANCELADORA IS NULL"
								+ " 			AND MOV.ID_MOBIL IN ( SELECT MOV.ID_MOBIL"
								+ " 				FROM siga.ex_movimentacao MOV"
								+ " 				INNER JOIN siga.ex_mobil MOB"
								+ " 				ON MOB.ID_MOBIL = MOV.ID_MOBIL"
								+ " 				INNER JOIN siga.ex_documento DOC"
								+ " 				ON DOC.ID_DOC = MOB.ID_DOC"
								+ " 				WHERE MOV.DT_FIM_MOV >= :dtini "
								+ " 				AND MOV.DT_FIM_MOV < :dtfim "
								+ " 				AND (MOV.ID_TP_MOV = :idTpMov1"
								+ " 					OR MOV.ID_TP_MOV = :idTpMov2"
								+ " 					OR MOV.ID_TP_MOV = :idTpMov3"
								+ " 					OR MOV.ID_TP_MOV = :idTpMov4)"
								+ " 				AND MOV.ID_MOV_CANCELADORA IS NULL"
								+ queryLotacao
								+ queryUsuario
								+ " 				GROUP BY MOV.ID_MOBIL  ) ) TABELA_TEMP1"
								+ " 			GROUP BY TABELA_TEMP1.ID_MOBIL ) TABELA_TEMP2"
								+ " 	ON MOV.ID_MOBIL = TABELA_TEMP2.ID_MOBIL"
								+ " 	AND MOV.ID_MOV = TABELA_TEMP2.ID_MOV"
								+ " 	WHERE MOV.DT_FIM_MOV IS NOT NULL ) TABELA_TEMP3"
								+ " INNER JOIN siga.ex_mobil MOB"
								+ " ON MOB.ID_MOBIL = TABELA_TEMP3.ID_MOBIL"
								+ " INNER JOIN siga.ex_documento DOC"
								+ " ON DOC.ID_DOC = MOB.ID_DOC"
								+ " INNER JOIN siga.ex_modelo MD"
								+ " ON MD.ID_MOD = DOC.ID_MOD"
								+ " INNER JOIN corporativo.dp_lotacao LOTA"
								+ " ON LOTA.ID_LOTACAO = DOC.ID_LOTA_CADASTRANTE"
								+ " INNER JOIN corporativo.dp_pessoa PES"
								+ " ON PES.ID_PESSOA = TABELA_TEMP3.ID_CADASTRANTE"
								+ " LEFT OUTER JOIN corporativo.dp_lotacao LOTARESP"
								+ " ON LOTARESP.ID_LOTACAO = TABELA_TEMP3.ID_LOTA_RESP"
								+ queryJoin
								+ " WHERE DOC.ID_ORGAO_USU = :orgao "
								+ " AND DT_FIM_MOV >= :dtini "
								+ " AND DT_FIM_MOV < :dtfim "
								+ queryModelo
								+ queryGroupBy
								);

		query.setParameter("idTpMov1", ExTipoDeMovimentacao.TRANSFERENCIA.getId());
		query.setParameter("idTpMov2", ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA.getId());
		query.setParameter("idTpMov3", ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA.getId());
		query.setParameter("idTpMov4", ExTipoDeMovimentacao.TRANSFERENCIA_EXTERNA.getId());

		if (parametros.get("orgao") != null && !"".equals(parametros.get("orgao"))) {
			query.setParameter("orgao", Long.valueOf((String) parametros.get("orgao")));
		}
		
		if (parametros.get("lotacao") != null && !"".equals(parametros.get("lotacao"))) {
			Query qryLota = ContextoPersistencia.em().createQuery(
					"from DpLotacao lot where lot.idLotacao = "
							+ parametros.get("lotacao"));

			Set<DpLotacao> lotacaoSet = new HashSet<DpLotacao>();
			DpLotacao lotacao = (DpLotacao) qryLota.getResultList().get(0);
			lotacaoSet.add(lotacao);

			query.setParameter("lotacao", lotacao.getIdInicial());
		}

		if (parametros.get("usuario") != null && !"".equals(parametros.get("usuario"))) {
			Query qryPes = ContextoPersistencia.em().createQuery(
					"from DpPessoa pes where pes.idPessoa = "
							+ parametros.get("usuario"));

			Set<DpPessoa> pessoaSet = new HashSet<DpPessoa>();
			DpPessoa pessoa = (DpPessoa) qryPes.getResultList().get(0);
			pessoaSet.add(pessoa);

			query.setParameter("usuario", pessoa.getIdPessoaIni());
		}

//		query.setParameter("orgao", Long.valueOf((String) parametros.get("orgao")));

		Date dtini;
		Date dtfim;
		Date dtfimMaisUm;
		if (parametros.get("idMod") != null && !"".equals(parametros.get("idMod"))) {
			query.setParameter("idMod", Long.valueOf((String) parametros.get("idMod")));
			query.setParameter("idLotaResp", Long.valueOf((String) parametros.get("idLotaResp")));
			dtini = formatter.parse((String) parametros.get("dataVencida"));
			dtfim = formatter.parse((String) parametros.get("dataVencida"));
		} else {
			dtini = formatter.parse((String) parametros.get("dataInicial"));
			dtfim = formatter.parse((String) parametros.get("dataFinal"));
		}
		dtfimMaisUm = new Date( dtfim.getTime() + 86400000L );
		query.setParameter("dtini", dtini);
		query.setParameter("dtfim", dtfimMaisUm);
		
		Iterator it = query.getResultList().iterator();
		return it;
	}
}