package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

import java.math.BigDecimal;
import java.sql.Blob;
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

import net.sf.jasperreports.engine.JRException;

import org.hibernate.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.SigaExProperties;
import br.gov.jfrj.siga.ex.util.Compactador;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

	public class RelArmazenamento extends RelatorioTemplate {
 
		public List<String> listColunas;
		public List<List<String>> listLinhas;
		public Long totalDocumentos;
		public Long totalPaginas;
		public Long totalBlobsDoc;
		public Long totalBlobsAnexos;

		public RelArmazenamento (Map parametros) throws DJBuilderException {
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

			this.setTitle("Relatório de Produção Por Órgão");
			this.listColunas.add("Sigla Órgão");
			this.listColunas.add("Órgão");
			this.listColunas.add("Sigla Unidade");
			this.listColunas.add("Unidade");
			this.listColunas.add("Nome do Documento");
			this.listColunas.add("Núm.");
			this.listColunas.add("Data Documento");
			this.listColunas.add("Data TMP");
			this.listColunas.add("Cadastrante");
			this.listColunas.add("Págs. Doc.");
			this.listColunas.add("Tam. Doc.");
			this.listColunas.add("Tam. Doc. (no Oracle)");
			this.listColunas.add("Qtd. Anexos");
			this.listColunas.add("Págs. Anexo");
			this.listColunas.add("Tam. Anexo");
			this.listColunas.add("Tam. Anexo (no Oracle)");
			this.addColuna("Sigla Órgão", 5, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Órgão", 10, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Sigla Unidade", 5, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Unidade", 10, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Nome do Documento", 20, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Núm.", 10, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Data Documento", 4, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Data TMP", 4, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Cadastrante", 4, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Págs. Doc.", 4, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Tam. Doc.", 4, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Tam. Doc. (no Oracle)", 4, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Qtd. Anexos", 4, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Págs. Anexo", 4, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Tam. Anexo", 4, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Tam. Anexo (no Oracle)", 4, RelatorioRapido.ESQUERDA, false);
			return this;
		}

		@Override
		public Collection processarDados() throws Exception {

			List<String> d = new ArrayList<String>();
			
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			ExDao dao = ExDao.getInstance();

			String queryOrgao = "";
			if (parametros.get("orgao") != null && parametros.get("orgao") != "") {
				queryOrgao = " AND DOC.ID_ORGAO_USU = :orgao ";
//				queryOrgao = "and doc.orgaoUsuario.idOrgaoUsu = :orgao ";
			}

			String queryLotacao = "";
			if (parametros.get("lotacao") != null
					&& parametros.get("lotacao") != "") {
//				queryLotacao = " and doc.lotaCadastrante.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :idLotacao) ";
				queryLotacao = " AND DOC.ID_LOTA_CADASTRANTE IN (SELECT L.ID_LOTACAO FROM CORPORATIVO.DP_LOTACAO AS L WHERE L.ID_LOTACAO_INI = :idLotacao) ";
			}

			String queryUsuario = "";
			if (parametros.get("usuario") != null
					&& parametros.get("usuario") != "") {
				queryUsuario = " AND DOC.ID_CADASTRANTE IN (SELECT P.ID_PESSOA FROM CORPORATIVO.DP_PESSOA AS P WHERE P.ID_PESSOA_INI = :usuario) ";
//				queryUsuario = "and doc.cadastrante.idPessoaIni in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :usuario) ";
			}
			
			Query query = HibernateUtil
					.getSessao()
					.createSQLQuery(
						"SELECT " 
							+ "DOC.ID_DOC, "
							+ "MOB.ID_MOBIL, "
							+ "MOV.ID_MOV, "
							+ "ORG.SIGLA_ORGAO_USU, " 
							+ "ORG.NM_ORGAO_USU ORGAO, "
							+ "LOTA.SIGLA_LOTACAO, " 
							+ "LOTA.NOME_LOTACAO LOTACAO, " 
							+ "MOD.NM_MOD MODELO, " 
							+ "DOC.ANO_EMISSAO, "
							+ "DOC.NUM_EXPEDIENTE, "
							+ "DOC.DT_DOC, " 
							+ "DOC.DT_REG_DOC, " 
//							+ "CONVERT (VARCHAR(10), DOC.DT_DOC, 103) AS [DD/MM/YYYY], "
//							+ "CONVERT (VARCHAR(10), DOC.DT_REG_DOC, 103) AS [DD/MM/YYYY], "
							+ "DOC.ID_CADASTRANTE, "
							+ "DOC.NUM_PAGINAS, "
							+ "DOC.CONTEUDO_BLOB_DOC, "
							+ "LENGTH(DOC.CONTEUDO_BLOB_DOC), "
							+ "LENGTH(MOV.CONTEUDO_BLOB_MOV), " 
							+ "MOV.CONTEUDO_BLOB_MOV, " 
							+ "MOV.CONTEUDO_TP_MOV, " 
							+ "FRM.SIGLA_FORMA_DOC " 
						+ "FROM SIGA.EX_MOBIL MOB "
						+ "RIGHT OUTER JOIN SIGA.EX_DOCUMENTO DOC ON mob.ID_DOC=DOC.ID_DOC and mob.id_tipo_mobil = 1 " 
						+ "inner join CORPORATIVO.CP_ORGAO_USUARIO ORG on ORG.ID_ORGAO_USU = DOC.ID_ORGAO_USU "
						+ "LEFT OUTER join CORPORATIVO.DP_LOTACAO LOTA on LOTA.ID_LOTACAO = DOC.ID_LOTA_CADASTRANTE "  
						+ "LEFT OUTER join SIGA.EX_MODELO MOD on MOD.ID_MOD = DOC.ID_MOD "
						+ "LEFT OUTER join SIGA.EX_FORMA_DOCUMENTO FRM on FRM.ID_FORMA_DOC = MOD.ID_FORMA_DOC "
						+ "FULL JOIN SIGA.EX_MOVIMENTACAO MOV ON mov.id_mobil=mob.id_mobil and mov.id_tp_mov = 64 AND mov.id_mov_canceladora IS NULL " 
						+ "WHERE doc.dt_reg_doc > :dtini and doc.dt_reg_doc < :dtfim " 
						+ queryOrgao
						+ queryLotacao
						+ queryUsuario 
//						+ "GROUP BY "
//						+ "ORG.NM_ORGAO_USU, " 
//						+ "LOTA.NOME_LOTACAO, " 
//						+ "MOD.NM_MOD, "
//						+ "doc.id_doc, "
//						+ "DOC.DT_DOC, "
//						+ "doc.dt_reg_doc, "
//						+ "doc.num_expediente "
						+ "ORDER BY ORGAO, LOTACAO, MODELO, DOC.ID_DOC, MOV.ID_MOV "
							);

			if (parametros.get("orgao") != null && parametros.get("orgao") != "") {
				query.setLong("orgao", Long.valueOf((String) parametros.get("orgao")));
			}
			
			if (parametros.get("lotacao") != null && parametros.get("lotacao") != "") {
				Query qryLota = HibernateUtil.getSessao().createQuery(
						"from DpLotacao lot where lot.idLotacao = " + parametros.get("lotacao"));
							
				Set<DpLotacao> lotacaoSet = new HashSet<DpLotacao>();
				DpLotacao lotacao = (DpLotacao)qryLota.list().get(0);
				lotacaoSet.add(lotacao);
				
				query.setParameter("idLotacao",
						lotacao.getIdInicial());
			}

			if (parametros.get("usuario") != null && parametros.get("usuario") != "") {
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

			totalDocumentos = 0L;
			totalPaginas = 0L;
			totalBlobsDoc = 0L;
			totalBlobsAnexos = 0L;
			if (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				
				while (it.hasNext()) {
	//				ExDocumento doc = (ExDocumento) obj[0];
	//				ExMobil mob = (ExMobil) obj[1];
					Long pags = 0L;
					Long pagsBlobDoc = 0L;
					Long pagsBlobAnexo = 0L;
					Long qtdAnexos = 0L;
					Long lenBlobDoc = 0L;
					Long lenBlobDocBanco = 0L;
					Long lenBlobAnexo = 0L;
					Long lenBlobAnexoBanco = 0L;
					String siglaDoc = "";
					String alert = "";
					siglaDoc = obj[0].toString();
					List<String> listDados = new ArrayList();
					listDados.add(obj[3].toString()); // sigla orgao
					listDados.add(obj[4].toString()); // nome orgao
					listDados.add(obj[5].toString()); // sigla lotacao 
					listDados.add(obj[6].toString()); // nome lotacao
					listDados.add(obj[7].toString()); //nome modelo
	//				listDados.add("<a href=" + parametros.get("link_siga") + siglaDoc + ">" + siglaDoc + "</a>" + alert);
					listDados.add(siglaDoc);
					if (obj[10] != null) {
						listDados.add(obj[10].toString()); // Data documento
					} else {
						listDados.add("");
					}
					listDados.add(obj[11].toString()); // Data reg 
					listDados.add(obj[12].toString()); // id cadastrante
//					siglaDoc = ExDocumento.getCodigo(Long.valueOf(obj[0].toString()), obj[4].toString(), obj[3].toString(), obj[19].toString(), 
//							Long.valueOf(obj[8].toString()), Long.valueOf(obj[7].toString()), 0, 1L, 0, 
//							null, null, null, null, 
//							null, null, null, null, null); 
					if (obj[13] != null) {
						pags = Long.valueOf(obj[13].toString());
					}
					byte[] blob = null;
					if (obj[14] != null) {
						blob = br.gov.jfrj.siga.cp.util.Blob.toByteArray((Blob) obj[14]);
						final Compactador zip = new Compactador();
						byte[] blobDescompactado = zip.descompactarStream(blob, "doc.pdf");
						if (blobDescompactado != null) {
							lenBlobDoc = (long) blobDescompactado.length;
						}
					}
					if (obj[15] != null) {
						lenBlobDocBanco = Long.valueOf(obj[15].toString());
					}
					
					if (parametros.get("getAll") == null || parametros.get("getAll") != "true") {
						while (siglaDoc.equals(obj[0].toString())) {
							if (obj[17] != null) {
								qtdAnexos = qtdAnexos + 1;
	//							String conteudoTpMov = obj[18].toString();
								blob = br.gov.jfrj.siga.cp.util.Blob.toByteArray((Blob) obj[17]);
								lenBlobAnexo = lenBlobAnexo + blob.length;
								try {
									pagsBlobAnexo = pagsBlobAnexo + (long) Documento.getNumberOfPages(blob);
									lenBlobAnexoBanco = lenBlobAnexoBanco + blob.length;
								} catch (Exception e) {
									long numPag = (long) blob.length / 51200;
									if (numPag < 1) {
										numPag = 1;
									}
									pagsBlobAnexo = pagsBlobAnexo + numPag;
									lenBlobAnexoBanco = lenBlobAnexoBanco + blob.length;
								}
							}
							if (it.hasNext()) {
								obj = (Object[]) it.next();
							} else {
								continue;
							}
						}
						listDados.add(pags.toString());
						listDados.add(lenBlobDoc.toString());
						listDados.add(lenBlobDocBanco.toString()); 
						listDados.add(qtdAnexos.toString()); 
						listDados.add(pagsBlobAnexo.toString()); 
						listDados.add(lenBlobAnexo.toString()); 
						listDados.add(lenBlobAnexoBanco.toString()); 
						for (String dado : listDados) {
							d.add(dado);
						}
						listLinhas.add(listDados);
						totalDocumentos = totalDocumentos + 1; 
						totalPaginas = totalPaginas + pags; 
						totalBlobsDoc = totalBlobsDoc + lenBlobDoc; 
						totalBlobsAnexos = totalBlobsAnexos + lenBlobAnexo; 
					}
				}
			}

			if (listLinhas.size() == 0) {
				throw new AplicacaoException("Não foram encontrados documentos para os dados informados.");
			}
			return d;
		}
	}