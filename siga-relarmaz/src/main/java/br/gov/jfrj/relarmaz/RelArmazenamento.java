package br.gov.jfrj.relarmaz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import br.gov.jfrj.siga.base.AplicacaoException;

import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

	public class RelArmazenamento {
 
		private static Logger logger = Logger.getLogger(RelArmazenamento.class);
		
		public List<String> listColunas;
		public List<List<String>> listLinhas;
		public Long totalDocumentos;
		public Long totalPaginas;
		private Map<String, String> parametros;

		public RelArmazenamento (Map parm) throws Exception {
			parametros = parm;
			if (parametros.get("dataInicial") == null) {
				throw new AplicacaoException("Parâmetro dataInicial não informado!");
			}
			if (parametros.get("dataFinal") == null) {
				throw new AplicacaoException("Parâmetro dataFinal não informado!");
			}
			listColunas = new ArrayList<String>();
			listLinhas = new ArrayList<List<String>>();
			configurarRelatorio();
		}

		public void configurarRelatorio() throws Exception {

			this.listColunas.add("Sigla Órgão");
			this.listColunas.add("Órgão");
			this.listColunas.add("Sigla Unidade");
			this.listColunas.add("Unidade");
			this.listColunas.add("Qtd. de Documentos + Anexos");
			this.listColunas.add("Soma de Páginas");
		}
		
		public List<List<String>> gerarCsv() throws Exception {
			List<List<String>> listCsv = new ArrayList<List<String>>(); 
			String texto = "";
			String orgaoAnt = listLinhas.get(0).get(0);
			Long totalDocs = 0L;
			Long totalPags = 0L;
			String cabecalho = "";
			for (String col : listColunas) { 
				cabecalho += "\"" + col + "\";";
			}

			List<String> listRows = new ArrayList<String>();
			listRows.add(cabecalho);
			
			// Para cada orgao, gera um array no array contendo o cabecalho, as linhas  
			// com o resultado da query mais uma ultima linha de totais, em formato csv. 
			for (List<String> cols : listLinhas) {
				if (!cols.get(0).equals(orgaoAnt)) {
					listRows.add(orgaoAnt + " Total;;;;" + totalDocs.toString() + ";" + totalPags.toString() + ";");
					listCsv.add(listRows);
					totalDocs = 0L;
					totalPags = 0L;
					listRows = new ArrayList<String>();
					orgaoAnt = cols.get(0);
					listRows.add(cabecalho);
				}
				int index = 0;
				texto = "";
				for (String col : cols) { 
					texto += "\"" + col + "\";";
					if (index == 4) {
						totalDocs += Long.valueOf(col);
					}
					if (index == 5) {
						totalPags += Long.valueOf(col);
					}
					index++;
				}
				listRows.add(texto);
			}
			listRows.add(orgaoAnt + " Total;;;;" + totalDocs.toString() + ";" + totalPags.toString() + ";");
			listCsv.add(listRows);
			return listCsv;
		}
		
		public Collection processarDados() throws Exception {

			List<String> d = new ArrayList<String>();
			
			String qtdBytesPagina = Prop.get("sigaex.relarmaz.qtd.bytes.pagina");
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			boolean detalhar = false;
			String queryDetalhe = "";
			if (parametros.get("detalhar") != null && parametros.get("detalhar") != "") {
				detalhar = true;
				queryDetalhe = " COUNT (TAB1.ID_DOC) TOTAL_DOC, "
						+ " SUM (TAM_DOC) TOTAL_PAG_DOC, "
						+ " SUM (TAM_DOC) TOTAL_TAM_DOC, "
						+ " SUM (TAB1.QTD_ANEXOS) TOTAL_ANEXOS, "
						+ " SUM (PAG_DOC) TOTAL_PAG_ANEXOS, "
						+ " SUM (TAM_ANEXOS) TOTAL_TAM_ANEXOS, ";
			}

			String queryOrgao = "";
			if (parametros.get("orgao") != null && parametros.get("orgao") != "") {
				queryOrgao = " AND DOC.ID_ORGAO_USU = :orgao ";
			}

			String queryLotacao = "";
			if (parametros.get("lotacao") != null
					&& parametros.get("lotacao") != "") {
				queryLotacao = " AND DOC.ID_LOTA_CADASTRANTE IN (SELECT LOT1.ID_LOTACAO FROM CORPORATIVO.DP_LOTACAO LOT1 WHERE LOT1.ID_LOTACAO_INI = :idLotacao) ";
			}

			String queryUsuario = "";
			if (parametros.get("usuario") != null
					&& parametros.get("usuario") != "") {
				queryUsuario = " AND DOC.ID_CADASTRANTE IN (SELECT PES1.ID_PESSOA FROM CORPORATIVO.DP_PESSOA PES1 WHERE PES1.ID_PESSOA_INICIAL = :usuario) ";
			}

			Query query = HibernateUtil
					.getSessao()
					.createSQLQuery(
							"SELECT "  
							+ " ORG.SIGLA_ORGAO_USU ORGAO, " 
							+ " ORG.NM_ORGAO_USU, "
							+ " LOTA.SIGLA_LOTACAO LOTACAO, " 
							+ " LOTA.NOME_LOTACAO, "
							+ queryDetalhe
							+ " (COUNT (TAB1.ID_DOC) + SUM(TAB1.QTD_ANEXOS)) TOTAL_DOC_E_ANEXOS, "
							+ " (SUM(TAB1.PAG_DOC) + SUM(TAB1.PAG_ANEXOS)) TOTAL_PAG_DOC_E_ANEXOS " 
							+ " FROM ( "
							+ " 	SELECT "
							+ " 	DOC.ID_DOC, "
							+ " 	DOC.ID_ORGAO_USU, "
							+ " 	DOC.ID_LOTA_CADASTRANTE, " 
							+ " 	DOC.ID_CADASTRANTE, "
							+ " 	DOC.ID_FORMA_DOC, "
							+ " 	DOC.ID_MOD, "
							+ " 	DOC.DT_DOC, "
							+ " 	DOC.DT_REG_DOC, "
							+ " 	DOC.NUM_PAGINAS PAG_DOC, "
							+ " 	LENGTH(DOC.CONTEUDO_BLOB_DOC) TAM_DOC, " 
							+ " 	COUNT(MOV.ID_MOV) QTD_ANEXOS, " 
							+ " 	COALESCE(SUM( CASE WHEN "
							+ "				MOV.NUM_PAGINAS IS NOT NULL THEN MOV.NUM_PAGINAS "
							+ "			ELSE "
							+ "				CEIL(LENGTH(MOV.CONTEUDO_BLOB_MOV) / " + qtdBytesPagina + ") "
							+ "			END ),0) PAG_ANEXOS, "
							+ " 	COALESCE(SUM( LENGTH(MOV.CONTEUDO_BLOB_MOV) ),0) TAM_ANEXOS " 
							+ " 	FROM SIGA.EX_MOBIL MOB "
							+ " 	INNER JOIN SIGA.EX_DOCUMENTO DOC ON MOB.ID_DOC=DOC.ID_DOC AND MOB.ID_TIPO_MOBIL = 1 " 
							+ " 	FULL JOIN SIGA.EX_MOVIMENTACAO MOV ON MOV.ID_MOBIL=MOB.ID_MOBIL "
							+ "			AND MOV.ID_TP_MOV = :idTpMovAnexo AND MOV.ID_MOV_CANCELADORA IS NULL " 
							+ " 	LEFT JOIN CORPORATIVO.CP_CONTRATO CON ON CON.ID_ORGAO_USU = DOC.ID_ORGAO_USU "
							+ " 	WHERE DOC.DT_REG_DOC >= :dtini AND DOC.DT_REG_DOC < :dtfim "
							+ " 		AND ( CON.DT_CONTRATO IS NULL"
							+ "				OR NOT (EXTRACT(MONTH FROM CON.DT_CONTRATO) = EXTRACT(MONTH FROM SYSDATE)  "	
							+ "					AND DOC.DT_REG_DOC < CON.DT_CONTRATO) ) "
							+ queryOrgao
							+ queryLotacao
							+ queryUsuario
							+ " 		AND DOC.DT_DOC IS NOT NULL " 
							+ " 	GROUP BY "
							+ " 	DOC.ID_DOC, "
							+ " 	DOC.ID_ORGAO_USU, "
							+ " 	DOC.ID_LOTA_CADASTRANTE, "
							+ " 	DOC.ID_CADASTRANTE, "
							+ " 	DOC.ID_FORMA_DOC, "
							+ " 	DOC.ID_MOD, "
							+ " 	DOC.DT_DOC, "
							+ " 	DOC.DT_REG_DOC, "
							+ " 	DOC.ID_CADASTRANTE, "
							+ " 	DOC.NUM_PAGINAS, "
							+ " 	LENGTH(DOC.CONTEUDO_BLOB_DOC) "
							+ " ) TAB1 "
							+ " INNER JOIN CORPORATIVO.CP_ORGAO_USUARIO ORG ON ORG.ID_ORGAO_USU = TAB1.ID_ORGAO_USU "
							+ " LEFT OUTER JOIN CORPORATIVO.DP_LOTACAO LOTA ON LOTA.ID_LOTACAO = TAB1.ID_LOTA_CADASTRANTE "  
							+ " GROUP BY "
							+ " ORG.SIGLA_ORGAO_USU, "
							+ " ORG.NM_ORGAO_USU, "
							+ " LOTA.SIGLA_LOTACAO, " 
							+ " LOTA.NOME_LOTACAO " 
							+ " ORDER BY ORGAO, LOTACAO, TOTAL_DOC_E_ANEXOS DESC, TOTAL_PAG_DOC_E_ANEXOS DESC "
					);
			
			query.setLong("idTpMovAnexo", 64);			

			if (parametros.get("orgao") != null && parametros.get("orgao") != "") {
				query.setLong("orgao", Long.valueOf((String) parametros.get("orgao")));
			}
			
			if (parametros.get("lotacao") != null && parametros.get("lotacao") != "") {
				Query qryLota = HibernateUtil.getSessao().createSQLQuery(
						"SELECT LOT.ID_LOTACAO_INI FROM CORPORATIVO.DP_LOTACAO LOT WHERE ID_LOTACAO = " + parametros.get("lotacao"));
							
				query.setParameter("idLotacao", qryLota.list().get(0));
			}

			if (parametros.get("usuario") != null && parametros.get("usuario") != "") {
				Query qryPes = HibernateUtil.getSessao().createSQLQuery(
						"SELECT PES.ID_PESSOA_INICIAL FROM CORPORATIVO.DP_PESSOA PES WHERE ID_PESSOA = "
								+ parametros.get("usuario"));

				query.setParameter("usuario", qryPes.list().get(0));
			}

			Date dtini = formatter.parse((String) parametros.get("dataInicial"));
			query.setDate("dtini", dtini);
			Date dtfim = formatter.parse((String) parametros.get("dataFinal"));
			Date dtfimMaisUm = new Date( dtfim.getTime() + 86400000L );
			query.setDate("dtfim", dtfimMaisUm);

			Iterator it = query.list().iterator();

			totalDocumentos = 0L;
			totalPaginas = 0L;

			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				Long qtdDocs = 0L;
				Long pagsDocs = 0L;
				Long pagsAnexos = 0L;
				Long qtdAnexos = 0L;
				Long lenBlobDocs = 0L;
				Long lenBlobAnexos = 0L;
				Long totalDocsEAnexos = 0L;
				Long totalPagsDocsEAnexos = 0L;
				List<String> listDados = new ArrayList();
				listDados.add(obj[0].toString()); // sigla orgao
				listDados.add(obj[1].toString()); // nome orgao
				listDados.add(obj[2].toString()); // sigla lotacao 
				listDados.add(obj[3].toString()); // nome lotacao

				if (detalhar) {
					if (obj[4] != null) {
						qtdDocs = Long.valueOf(obj[4].toString());
					}
					if (obj[5] != null) {
						pagsDocs = Long.valueOf(obj[5].toString());
					}
					if (obj[6] != null) {
						lenBlobDocs = Long.valueOf(obj[6].toString());
					}
					if (obj[7] != null) {
						qtdAnexos = Long.valueOf(obj[7].toString());
					}
					if (obj[8] != null) {
						pagsAnexos = Long.valueOf(obj[8].toString());
					}
					if (obj[9] != null) {
						lenBlobAnexos = Long.valueOf(obj[9].toString());
					}
					if (obj[10] != null) {
						totalDocsEAnexos = Long.valueOf(obj[10].toString());
					}
					if (obj[11] != null) {
						totalPagsDocsEAnexos = Long.valueOf(obj[11].toString());
					}
					listDados.add(qtdDocs.toString());
					listDados.add(pagsDocs.toString());
					listDados.add(lenBlobDocs.toString());
					listDados.add(qtdAnexos.toString());
					listDados.add(pagsAnexos.toString());
					listDados.add(lenBlobAnexos.toString());
				} else {
					if (obj[4] != null) {
						totalDocsEAnexos = Long.valueOf(obj[4].toString());
					}
					if (obj[5] != null) {
						totalPagsDocsEAnexos = Long.valueOf(obj[5].toString());
					}
				}
				
				listDados.add(totalDocsEAnexos.toString());
				listDados.add(totalPagsDocsEAnexos.toString());
				totalDocumentos = totalDocsEAnexos + totalDocumentos;
				totalPaginas = totalPagsDocsEAnexos + totalPaginas;
				
				for (String dado : listDados) {
					d.add(dado);
				}
				listLinhas.add(listDados);
			}

			if (listLinhas.size() == 0) {
				throw new AplicacaoException("Não foram encontrados documentos para os dados informados.");
			}
			return d;
		}
		
		public static void main (String[] args) throws Exception {
			
			logger.info("RelArmazenamento - Iniciando...");
			
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String parmDir = "";
			String dtIni;
			String dtFim;
			String parmOrgao = "";
			String parmDtIni = "01/01/1980";
			String parmDtFim = "";
			String parmServidor = "prod";
			if (args.length > 0) {
				for(int i=0; i<args.length; i++) {  
					if (args[i].startsWith("dir=")) {
						parmDir = args[i].replace("dir=", "");
					}
					if (args[i].startsWith("orgao=")) {
						parmOrgao = args[i].replace("orgao=", "");
						if (!parmOrgao.matches("[0-9]*")) {
							logger.error("RelArmazenamento - Parametro orgao deve ser numerico.");
							System.exit(10);
						}
					}
					if (args[i].startsWith("datainicio")) {
						parmDtIni = args[i].replace("datainicio=", "");
						if (!parmDtIni.matches("(\\d{2})[\\/](\\d{2})[\\/](\\d{4})")) {
							logger.error("RelArmazenamento - Parametro dataIni (data inicial) deve ser no formato DD/MM/AAAA.");
							System.exit(11);
						}
					}
					if (args[i].startsWith("datafim")) {
						parmDtFim = args[i].replace("datafim=", "");
						if (!parmDtFim.matches("(\\d{2})[\\/](\\d{2})[\\/](\\d{4})")) {
							logger.error("RelArmazenamento - Parametro datafim (data final) deve ser no formato DD/MM/AAAA.");
							System.exit(12);
						}
					}
					if (args[i].startsWith("servidor")) {
						parmServidor = args[i].replace("servidor=", "");
						if (!(parmServidor.equals("desenv") || parmServidor.equals("homolo") ||  
								parmServidor.equals("treina") || parmServidor.equals("prod") ) ) {
							logger.error("RelArmazenamento - Parametro servidor deve ser 'desenv', 'homolo', 'treina' ou 'prod'.");
							System.exit(13);
						}
					}
				}
			}

			if ( parmDtFim.equals("") ) {
				Calendar cal = Calendar.getInstance();
			    cal.add(Calendar.MONTH, -1);
			    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));					
				parmDtFim = df.format(cal.getTime());
				logger.info("Data final nao informada. Vai assumir o ultimo dia do mes anterior: " + parmDtFim);
			}

			try {
				consistePeriodo(parmDtIni, parmDtFim);
			} catch (Exception e) {
				logger.error("RelArmazenamento - " + e.getMessage());
				System.exit(14);
			}
			dtIni = parmDtIni;
			dtFim = parmDtFim;

			logger.info("RelArmazenamento - Parametros: " + parmServidor + " " + parmOrgao 
            		+ " " + parmDtIni + " " + parmDtFim + " " + parmDir );

            EntityManagerFactory emf = conectaHibernate(parmServidor);
			EntityManager em = emf.createEntityManager();
			ContextoPersistencia.setEntityManager(em);
			em.getTransaction().begin();
			
			final Map<String, String> parametros = new HashMap<String, String>();

			parametros.put("orgao", parmOrgao);
			parametros.put("getAll", "true");
			parametros.put("lotacao", "");
			parametros.put("usuario", "");
			parametros.put("dataInicial", dtIni);
			parametros.put("dataFinal", dtFim); 
	
			final RelArmazenamento rel = new RelArmazenamento(parametros);
			rel.processarDados();
			List<List<String>> listCsv = rel.gerarCsv();
			for (List<String> csvContent : listCsv) {
				try {
					gravaCsv(parmDir, parmDtFim, csvContent);
				} 
		        catch(Exception e){
		        	logger.error(e);
		        }
			}
			em.getTransaction().commit();
			em.close();
			emf.close();
			logger.info("RelArmazenamento - Fim do Processamento."); 
		}

		private static void gravaCsv(String parmDir, String parmDtFim, List<String> csvContent) throws IOException {
			String path = parmDir + "/Documentos Por Orgao - " 
					+ parmDtFim.substring(6,10) + "-" 
					+ parmDtFim.substring(3,5) + "-" 
					+ csvContent.get(1).split(";") [0].replace("\"", "") + "-" 
					+ csvContent.get(1).split(";") [1].replace("\"", "")
					+ ".csv";

			File file = new File(path);

			if (file.exists()) {
				logger.warn("Arquivo já existente, será gravado novamente: " + path + ". ");
			    file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			for (String csvRow : csvContent) {
			    bw.write(csvRow);
			    bw.newLine();
			}
			bw.close();
			logger.info("Arquivo gravado com sucesso: " + path);
		}

		private static EntityManagerFactory conectaHibernate(String parmServidor) {
			Map<String, Comparable> addedOrOverridenProperties = new HashMap<String, Comparable>();

			addedOrOverridenProperties.put("hibernate.connection.url", 
					SigaBaseProperties.getString(parmServidor + ".hibernate.connection.url"));
			addedOrOverridenProperties.put("hibernate.connection.username", 
					SigaBaseProperties.getString(parmServidor + ".hibernate.connection.username"));
			addedOrOverridenProperties.put("hibernate.connection.password", 
					SigaBaseProperties.getString(parmServidor + ".hibernate.connection.password"));
			addedOrOverridenProperties.put("hibernate.connection.driver_class",
					SigaBaseProperties.getString(parmServidor + ".hibernate.connection.driver_class"));
			
			addedOrOverridenProperties.put("hibernate.connection.provider_class",
					"org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider");			
			addedOrOverridenProperties.put("c3p0.min_size", 
					SigaBaseProperties.getString(parmServidor + ".c3p0.min_size"));
			addedOrOverridenProperties.put("c3p0.max_size", 
					SigaBaseProperties.getString(parmServidor + ".c3p0.max_size"));
			addedOrOverridenProperties.put("c3p0.timeout", 
					SigaBaseProperties.getString(parmServidor + ".c3p0.timeout"));
			addedOrOverridenProperties.put("c3p0.max_statements", 
					SigaBaseProperties.getString(parmServidor + ".c3p0.max_statements"));
			
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("relarmaz", addedOrOverridenProperties);			
			return emf;
		}

		private static void consistePeriodo(String dataInicial, String dataFinal)
				throws Exception {
			if (dataInicial == null || dataFinal == null) {
				throw new AplicacaoException(
						"Data inicial ou data final nao informada.");
			}
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			final Date dtIni = df.parse(dataInicial);
			final Date dtFim = df.parse(dataFinal);
			if (dtFim.getTime() - dtIni.getTime() < 0L) {
				throw new AplicacaoException(
						"Data inicial maior que a data final.");
			}		
		}		
	}