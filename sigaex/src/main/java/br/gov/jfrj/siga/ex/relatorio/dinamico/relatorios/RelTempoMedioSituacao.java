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

import javax.persistence.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import net.sf.jasperreports.engine.JRException;

public class RelTempoMedioSituacao extends RelatorioTemplate {

	public List<String> listColunas;
	public List<List<String>> listModelos;

	public RelTempoMedioSituacao(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("dataInicial") == null) {
			throw new DJBuilderException("Parâmetro dataInicial não informado!");
		}
		if (parametros.get("dataFinal") == null) {
			throw new DJBuilderException("Parâmetro dataFinal não informado!");
		}
		listColunas = new ArrayList<String>();
		listModelos = new ArrayList<List<String>>();
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

		Iterator it = obtemDados(d);
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String lotaAnt = "";
		
		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			String lotaDoc = (String) obj[0];
			String modeloDoc = (String) obj[1];
			if (Ex.getInstance().getBL().getComp().podeExibirQuemTemAcessoAoDocumento(
					 titular, lotaTitular ,ExDao.getInstance().consultarModeloPeloNome(modeloDoc)
							)) {
				List<String> listDados = new ArrayList();
	
				if (!lotaDoc.equals(lotaAnt)) {
					listDados.add(lotaDoc);
					lotaAnt = lotaDoc;
				} else {
					listDados.add("");
				}
				listDados.add(modeloDoc);
				listDados.add(obj[2].toString());
				listDados.add(obj[3].toString());
				listModelos.add(listDados);
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

	private Iterator obtemDados(List<String> d) throws ParseException {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		String queryLotacao = "";
		if (parametros.get("lotacao") != null
				&& !"".equals(parametros.get("lotacao"))) {
			queryLotacao = " AND DOCA.ID_LOTA_CADASTRANTE IN ( SELECT LOTA.ID_LOTACAO FROM CORPORATIVO.DP_LOTACAO LOTA WHERE LOTA.ID_LOTACAO_INI = :lotacao ) ";
		}

		String queryUsuario = "";
		if (parametros.get("usuario") != null
				&& !"".equals(parametros.get("usuario"))) {
			queryUsuario = " AND DOCA.ID_CADASTRANTE IN ( SELECT PES.ID_PESSOA FROM CORPORATIVO.DP_PESSOA PES WHERE PES.ID_PESSOA_INICIAL = :usuario ) ";
		}

		Query query = ContextoPersistencia.em().createNativeQuery(
						"SELECT "
					    + "(LOTA.SIGLA_LOTACAO || ' - ' || LOTA.NOME_LOTACAO) LOTADESCR, "
					    + "MOD.NM_MOD, "
					    + "SITUACAO, "
					    + "ROUND(SUM(MEDIA) / COUNT(MEDIA), 0) MEDIA "  
					    + "FROM "
					    + "    ( SELECT "
					    + "        ID_LOTA_CADASTRANTE, "
					    + "        ID_DOC, "
					    + "        ID_MOD, "
					    + "        SITUACAO, "
					    + "        SUM(DIAS) TOTAL_DIAS, "
					    + "        ROUND(SUM(DIAS) / (COUNT(ID_DOC)), 0) MEDIA "   
					    + "        FROM "
					    + "        ( SELECT "
					    + "            'CAIXA DE ENTRADA' SITUACAO, "
					    + "            ID_DOC, ID_MOD, ID_LOTA_CADASTRANTE, "
					    + "            (TRUNC(DT_INI_MOV2) - TRUNC(DT_INI_MOV1)) DIAS FROM "
					    + "            (SELECT MOV1.ID_DOC, MOV1.ID_MOD, MOV1.ID_LOTA_CADASTRANTE, " 
					    + "                MOV1.ID_TP_MOV TPMOV1, "
					    + "                LEAD (MOV1.ID_TP_MOV, 1) OVER (PARTITION BY MOV1.ID_MOBIL ORDER BY MOV1.ID_MOBIL, MOV1.ID_MOV) TPMOV2, "
					    + "                MOV1.DT_INI_MOV DT_INI_MOV1, "
					    + "                LEAD (MOV1.DT_INI_MOV, 1) OVER (PARTITION BY MOV1.ID_MOBIL ORDER BY MOV1.ID_MOBIL, MOV1.ID_MOV) DT_INI_MOV2 " 
					    + "                FROM "
					    + "                      (SELECT DOCA.ID_DOC, MOVA.ID_MOBIL, MOVA.ID_MOV, MOVA.ID_TP_MOV, MOVA.DT_INI_MOV, DOCA.ID_LOTA_CADASTRANTE, DOCA.ID_CADASTRANTE, DOCA.ID_MOD, MOVA.ID_MOV_CANCELADORA "   
					    + "                       FROM SIGA.EX_MOVIMENTACAO MOVA "
					    + "                       INNER JOIN SIGA.EX_MOBIL MOBA ON MOBA.ID_MOBIL = MOVA.ID_MOBIL "
					    + "                       INNER JOIN SIGA.EX_DOCUMENTO DOCA ON DOCA.ID_DOC = MOBA.ID_DOC "  
					    + "                       WHERE "
						+ "							DOCA.ID_ORGAO_USU = :orgao "
						+ queryLotacao
						+ queryUsuario
					    + "                         AND DOCA.DT_FINALIZACAO >= :dtini AND DOCA.DT_FINALIZACAO <= :dtfim "
					    + "                         AND (MOVA.ID_TP_MOV = :idTpMovTransf "
					    + "							  OR MOVA.ID_TP_MOV = :idTpMovDespTransf "							
					    + "							  OR MOVA.ID_TP_MOV = :idTpMovDespTransfExt "							
					    + "							  OR MOVA.ID_TP_MOV = :idTpMovTransfExt "							
					    + "                           OR MOVA.ID_TP_MOV = :idTpMovRecebimento "
					    + "                           OR MOVA.ID_TP_MOV = :idTpMovTornarSemEfeito "
					    + "                           OR MOVA.ID_TP_MOV = :idTpMovArqCorrente) "
					    + "                         AND MOVA.ID_MOV_CANCELADORA IS NULL "
					    + "                       ORDER BY MOVA.ID_MOBIL, MOVA.ID_MOV "
					    + "                       ) "
					    + "				MOV1 ) "
					    + "            WHERE "
					    + "                ( TPMOV2 = :idTpMovRecebimento "
					    + "						OR TPMOV2 = :idTpMovArqCorrente "
					    + "						OR TPMOV2 = :idTpMovTornarSemEfeito ) "
					    + "                AND ( TPMOV1 = :idTpMovTransf "
					    + "						OR TPMOV1 = :idTpMovDespTransf "
					    + "                		OR TPMOV1 = :idTpMovDespTransfExt "
					    + "						OR TPMOV1 = :idTpMovTransfExt ) "
						+ "		UNION ALL "
					    + "        SELECT "
					    + "            'URGENTE' SITUACAO, "
					    + "            ID_DOC, ID_MOD, ID_LOTA_CADASTRANTE, "
					    + "            (TRUNC(DT_INI_MOV2) - TRUNC(DT_INI_MOV1)) DIAS FROM "
					    + "            (SELECT MOV1.ID_DOC, MOV1.ID_MOD, MOV1.ID_LOTA_CADASTRANTE, " 
					    + "                MOV1.ID_TP_MOV TPMOV1, "
					    + "                LEAD (MOV1.ID_TP_MOV, 1) OVER (PARTITION BY MOV1.ID_MOBIL ORDER BY MOV1.ID_MOBIL, MOV1.ID_MOV) TPMOV2, "
					    + "                MOV1.DT_INI_MOV DT_INI_MOV1, "
					    + "                LEAD (MOV1.DT_INI_MOV, 1) OVER (PARTITION BY MOV1.ID_MOBIL ORDER BY MOV1.ID_MOBIL, MOV1.ID_MOV) DT_INI_MOV2 " 
					    + "                FROM "
					    + "                      (SELECT DOCA.ID_DOC, MOVA.ID_MOBIL, MOVA.ID_MOV, MOVA.ID_TP_MOV, MOVA.DT_INI_MOV, DOCA.ID_LOTA_CADASTRANTE, DOCA.ID_CADASTRANTE, DOCA.ID_MOD, MOVA.ID_MOV_CANCELADORA "   
					    + "                       FROM SIGA.EX_MOVIMENTACAO MOVA "
					    + "                       INNER JOIN SIGA.EX_MOBIL MOBA ON MOBA.ID_MOBIL = MOVA.ID_MOBIL " 
					    + "                       INNER JOIN SIGA.EX_DOCUMENTO DOCA ON DOCA.ID_DOC = MOBA.ID_DOC "  
					    + "                       WHERE "
						+ "						    DOCA.ID_ORGAO_USU = :orgao "
						+ queryLotacao
						+ queryUsuario
					    + "                         AND DOCA.DT_FINALIZACAO >= :dtini AND DOCA.DT_FINALIZACAO <= :dtfim "
					    + "                         AND ( ( MOVA.ID_TP_MOV = :idTpMovMarcacao "
					    + "									AND MOVA.ID_MARCADOR = :idMarcadorUrgente "
					    + "									AND MOVA.ID_MOV_CANCELADORA IS NOT NULL ) "  
					    + "                             OR ( MOVA.ID_TP_MOV = :idTpMovCancelar "
					    + "										AND MOVA.ID_MOV_REF IN "
					    + "                                 	(SELECT ID_MOV FROM SIGA.EX_MOVIMENTACAO WHERE "
					    + "											ID_TP_MOV = :idTpMovMarcacao "
					    + "											AND ID_MARCADOR = :idMarcadorUrgente "
					    + "											AND ID_MOV_CANCELADORA IS NOT NULL) ) ) "
					    + "                            ORDER BY MOVA.ID_MOBIL, MOVA.ID_MOV "
					    + "                        ) MOV1 ) "
					    + "             WHERE "
					    + "                    TPMOV1 = :idTpMovMarcacao "
					    + "                    AND TPMOV2 = :idTpMovCancelar "
					    + "		) "
						+ "		GROUP BY "
						+ "		ID_LOTA_CADASTRANTE, "
						+ "		ID_DOC, "
						+ "		ID_MOD, "
						+ "		SITUACAO "
						+ "	) TAB_AUX "
						+ "INNER JOIN SIGA.EX_MODELO MOD ON MOD.ID_MOD = TAB_AUX.ID_MOD "
						+ "INNER JOIN CORPORATIVO.DP_LOTACAO LOTA ON LOTA.ID_LOTACAO = TAB_AUX.ID_LOTA_CADASTRANTE "          
						+ "GROUP BY "
						+ "    (LOTA.SIGLA_LOTACAO || ' - ' || LOTA.NOME_LOTACAO), "
						+ "    MOD.NM_MOD, "
						+ "    SITUACAO " 
						+ "ORDER BY "
						+ "	   LOTADESCR, "
						+ "    SITUACAO, " 
						+ "    MEDIA DESC " 
					);

		query.setParameter("idTpMovMarcacao", ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO);
		query.setParameter("idMarcadorUrgente", CpMarcadorEnum.URGENTE.getId());
		query.setParameter("idTpMovTransf", ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA);
		query.setParameter("idTpMovDespTransf", ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA);
		query.setParameter("idTpMovDespTransfExt", ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA);
		query.setParameter("idTpMovTransfExt", ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA);
		query.setParameter("idTpMovArqCorrente", ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE);
		query.setParameter("idTpMovRecebimento", ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO);			
		query.setParameter("idTpMovTornarSemEfeito", ExTipoMovimentacao.TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO);			
		query.setParameter("idTpMovCancelar", ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO);			
		
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

		query.setParameter("orgao", Long.valueOf((String) parametros.get("orgao")));

		Date dtini = formatter.parse((String) parametros.get("dataInicial"));
		query.setParameter("dtini", dtini);
		Date dtfim = formatter.parse((String) parametros.get("dataFinal"));
		Date dtfimMaisUm = new Date( dtfim.getTime() + 86400000L );
		query.setParameter("dtfim", dtfimMaisUm);
		
		Iterator it = query.getResultList().iterator();
		return it;
	}
}