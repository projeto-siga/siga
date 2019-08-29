package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

import java.math.BigDecimal;
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
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.SigaExProperties;
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
//			this.listColunas.add("Titular");
			this.listColunas.add("Págs. Doc.");
			this.listColunas.add("Tam. Doc.");
			this.listColunas.add("Tam. Doc. (no Oracle)");
			this.listColunas.add("Qtd. Anexos");
			this.listColunas.add("Págs. Anexo");
			this.listColunas.add("Tam. Anexo");
			this.listColunas.add("Tam. Anexo (no Oracle)");
			this.addColuna("Sigla Órgão", 10, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Órgão", 10, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Sigla Unidade", 10, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Unidade", 10, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Nome do Documento", 30, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Núm.", 20, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Data Documento", 5, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Data TMP", 5, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Cadastrante", 5, RelatorioRapido.ESQUERDA, false);
//			this.addColuna("Titular", 5, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Págs. Doc.", 5, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Tam. Doc.", 5, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Tam. Doc. (no Oracle)", 5, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Qtd. Anexos", 5, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Págs. Anexo", 5, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Tam. Anexo", 5, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Tam. Anexo (no Oracle)", 5, RelatorioRapido.ESQUERDA, false);
			return this;

		}

		@Override
		public Collection processarDados() throws Exception {

			List<String> d = new ArrayList<String>();
			
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			ExDao dao = ExDao.getInstance();

			String queryOrgao = "";
			if (parametros.get("orgao") != null && parametros.get("orgao") != "") {
//				queryOrgao = " AND DOC.ID_ORGAO_USU = :orgao ";
				queryOrgao = "and doc.orgaoUsuario.idOrgaoUsu = :orgao ";
			}

			String queryLotacao = "";
			if (parametros.get("lotacao") != null
					&& parametros.get("lotacao") != "") {
//				queryLotacao = " AND DOC.ID_LOTA_CADASTRANTE IN (SELECT L.ID_LOTACAO FROM CORPORATIVO.DP_LOTACAO AS L WHERE L.ID_LOTACAO_INI = :idLotacao) ";
				queryLotacao = " and doc.lotaCadastrante.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :idLotacao) ";
			}
			
			Query query = HibernateUtil
					.getSessao()
//					.createSQLQuery(
//						"SELECT " 
//						+ "ORG.SIGLA_ORGAO_USU, " 
//						+ "ORG.NM_ORGAO_USU, "
//						+ "LOTA.SIGLA_LOTACAO, " 
//						+ "LOTA.NOME_LOTACAO, " 
//						+ "MOD.NM_MOD MODELO, " 
//						+ "DOC.ANO_EMISSAO, "
//						+ "DOC.ID_DOC.NUM_EXPEDIENTE, "
//						+ "CONVERT (varchar(10), DOC.DT_DOC, 103) AS [DD/MM/YYYY], "
//						+ "CONVERT (varchar(10), DOC.DT_REG_DOC, 103) AS [DD/MM/YYYY], "
//						+ "PES1.MATRICULA, "
////						+ "PES2.MATRICULA, "
//						+ "DOC.NUM_PAGINAS, "
//						+ "LENGTH(DOC.CONTEUDO_BLOB_DOC), "
////						+ "LENGTH(MOV.CONTEUDO_BLOB_MOV), " 
//						+ "DOC.ID_DOC, "
//						+ "MOB.ID_MOBIL, "
////						+ "MOV.ID_MOV "
//						+ "FROM SIGA.EX_MOBIL MOB "
//						+ "		RIGHT OUTER JOIN SIGA.EX_DOCUMENTO DOC ON mob.ID_DOC=DOC.ID_DOC and mob.id_tipo_mobil = 1 " 
//						+ "		inner join CORPORATIVO.CP_ORGAO_USUARIO ORG on ORG.ID_ORGAO_USU = DOC.ID_ORGAO_USU "
//						+ "		LEFT OUTER join CORPORATIVO.DP_LOTACAO LOTA on LOTA.ID_LOTACAO = DOC.ID_LOTA_CADASTRANTE "  
//						+ "		LEFT OUTER join SIGA.EX_MODELO MOD on MOD.ID_MOD = DOC.ID_MOD "
//						+ "		LEFT OUTER join CORPORATIVO.DP_PESSOA PES1 on PES1.ID_PESSOA = DOC.ID_CADASTRANTE "
//						+ "		LEFT OUTER join CORPORATIVO.DP_PESSOA PES2 on PES2.ID_PESSOA = DOC.ID_TITULAR "
////						+ "		FULL JOIN SIGA.EX_MOVIMENTACAO MOV ON mov.id_mobil=mob.id_mobil "
////						+ "		and mov.id_tp_mov = 64 AND mov.id_mov_canceladora IS NULL " 
//						+ "WHERE doc.dt_reg_doc between :dtini and :dtfim "
//						+ queryOrgao
//						+ queryLotacao
//						+ "ORDER BY "
//						+ "ORG.NM_ORGAO_USU, "
//						+ "LOTA.NOME_LOTACAO, " 
//						+ "MOD.NM_MOD, " 
//						+ "DOC.ID_DOC, " 
//						+ "DOC.DT_REG_DOC " 
					.createQuery(
						"select " 
						+ "doc, " 
						+ "mob, "
//						+ "mov " 
						+ "length(doc.conteudoBlobDoc) "
//						+ "sum(length(mov.conteudoBlobMov)) "
						+ "from ExMobil mob "
						+ "		right outer join mob.exDocumento doc " 
						+ "		inner join doc.orgaoUsuario org "
						+ "		left outer join doc.lotaCadastrante lota "
						+ "		left outer join doc.exModelo mod "
						+ "		left outer join doc.cadastrante pes1 "
//						+ "		full join mob.exMovimentacao on "
//						+ "			(mov.exTipoMovimentacao.idTpMov = 64 "
//						+ "			and mov.exMovimentacaoCanceladora is null) "
//						+ "		LEFT OUTER join CORPORATIVO.DP_PESSOA PES2 on PES2.ID_PESSOA = DOC.ID_TITULAR "
						+ "where doc.dtRegDoc between :dtini and :dtfim "
						+ "and mob.exTipoMobil.idTipoMobil = 1" 
						+ queryOrgao
						+ queryLotacao
//						+ "		and (mov.exTipoMovimentacao.idTpMov = :idTpMov "
//						+ "				and mov.exMovimentacaoCanceladora is null) "
						+ "order by doc.orgaoUsuario.nmOrgaoUsu, "
						+ "		doc.lotaCadastrante.nomeLotacao, "
						+ "		doc.exModelo.nmMod, "
						+ "		doc.idDoc, "
						+ "		doc.dtRegDoc "
							);
//			query.setLong("idTpMov", ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_DE_ARQUIVO_AUXILIAR);

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

			Date dtini = formatter.parse((String) parametros.get("dataInicial"));
			query.setDate("dtini", dtini);
			Date dtfim = formatter.parse((String) parametros.get("dataFinal"));
			query.setDate("dtfim", dtfim);

			Iterator it = query.list().iterator();

			totalDocumentos = 0L;
			totalPaginas = 0L;
			totalBlobsDoc = 0L;
			totalBlobsAnexos = 0L;

			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				ExDocumento doc = (ExDocumento) obj[0];
				ExMobil mob = (ExMobil) obj[1];
//				ExMovimentacao movAnexo = (ExMovimentacao) obj[2];
//				BigDecimal idMobil = (BigDecimal) obj[11];
//				ExMobil mob = new ExMobil();
//				ExDocumento doc = new ExDocumento();
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
				byte[] blob = null;
				if (parametros.get("getAll") == null || parametros.get("getAll") != "true") {
					try {
						List <ExMovimentacao> movs = mob.getMovimentacoesPorTipo(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_DE_ARQUIVO_AUXILIAR);
						
						for ( ExMovimentacao mov : movs) {
							if (!mov.isCancelada()) {
								blob = mov.getConteudoBlobpdf();
								qtdAnexos = qtdAnexos + 1;
								if (blob != null) {
									lenBlobAnexo = lenBlobAnexo + blob.length;
									pagsBlobAnexo = pagsBlobAnexo + (long) Documento.getNumberOfPages(blob);
									blob = mov.getConteudoBlobMov2();
									lenBlobAnexoBanco = lenBlobAnexoBanco + blob.length;
								} else {
									blob = mov.getConteudoBlobMov2();
									lenBlobAnexo = lenBlobAnexo + blob.length;
									lenBlobAnexoBanco = lenBlobAnexoBanco + blob.length;
									pagsBlobAnexo = pagsBlobAnexo + (long) (blob.length / 51200);
								}
							}
						}
					} catch (Exception e) {
						alert = "<strong> - Doc. sem mobil </strong>";
					}
				}
				siglaDoc = doc.getSigla(); 
				if (doc.getNumPaginas() != null) {
					pags = doc.getNumPaginas().longValue();
				}
				lenBlobDoc = (long) doc.getNumBytes();
				blob = doc.getConteudoBlobPdf();
				if (blob != null) {
					lenBlobDoc = (long) blob.length;
				}
				if (obj[2] != null) {
					lenBlobDocBanco = Long.valueOf(obj[2].toString());
				}
				
				List<String> listDados = new ArrayList();
				listDados.add(doc.getOrgaoUsuario().getSigla());
				listDados.add(doc.getOrgaoUsuario().getNmOrgaoUsu());
				listDados.add(doc.getLotaCadastrante().getSigla());
				listDados.add(doc.getLotaCadastrante().getNomeLotacao());
				listDados.add(doc.getNmMod()); //
				listDados.add("<a href=" + parametros.get("link_siga") + siglaDoc + ">" + siglaDoc + "</a>" + alert);
				listDados.add(doc.getDtDocDDMMYY()); 
				listDados.add(doc.getDtRegDocDDMMYY());
				listDados.add(doc.getCadastrante().getSigla());
				listDados.add(pags.toString());
				listDados.add(lenBlobDoc.toString());
				listDados.add(lenBlobDocBanco.toString()); 
				listDados.add(qtdAnexos.toString()); //QTD ANEXOS
				listDados.add(pagsBlobAnexo.toString()); //PAGS ANEXOS
				listDados.add(lenBlobAnexo.toString()); //TAM BLOB ANEXOS
				listDados.add(lenBlobAnexoBanco.toString()); //TAM BLOB ANEXOS (no banco)
				for (String dado : listDados) {
					d.add(dado);
				}
				listLinhas.add(listDados);
				totalDocumentos = totalDocumentos + 1; 
				totalPaginas = totalPaginas + pags; 
				totalBlobsDoc = totalBlobsDoc + lenBlobDoc; 
				totalBlobsAnexos = totalBlobsAnexos + lenBlobAnexo; 
			}
			if (listLinhas.size() == 0) {
				throw new AplicacaoException("Não foram encontrados documentos para os dados informados.");
			}
			return d;
		}
	}