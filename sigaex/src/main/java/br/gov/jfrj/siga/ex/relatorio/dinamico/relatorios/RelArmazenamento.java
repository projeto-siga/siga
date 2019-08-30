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
import br.gov.jfrj.siga.dp.DpPessoa;
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
				queryOrgao = "and doc.orgaoUsuario.idOrgaoUsu = :orgao ";
			}

			String queryLotacao = "";
			if (parametros.get("lotacao") != null
					&& parametros.get("lotacao") != "") {
				queryLotacao = " and doc.lotaCadastrante.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :idLotacao) ";
			}

			String queryUsuario = "";
			if (parametros.get("usuario") != null
					&& parametros.get("usuario") != "") {
				queryUsuario = "and doc.cadastrante.idPessoaIni in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :usuario) ";
			}
			
			Query query = HibernateUtil
					.getSessao()
					.createQuery(
						"select " 
						+ "doc, " 
						+ "mob, "
						+ "length(doc.conteudoBlobDoc) "
						+ "from ExMobil mob "
						+ "		right outer join mob.exDocumento doc " 
						+ "		inner join doc.orgaoUsuario org "
						+ "		left outer join doc.lotaCadastrante lota "
						+ "		left outer join doc.exModelo mod "
						+ "		left outer join doc.cadastrante pes1 "
						+ "where doc.dtRegDoc between :dtini and :dtfim "
						+ "and mob.exTipoMobil.idTipoMobil = 1" 
						+ queryOrgao
						+ queryLotacao
						+ queryUsuario
						+ "order by doc.orgaoUsuario.nmOrgaoUsu, "
						+ "		doc.lotaCadastrante.nomeLotacao, "
						+ "		doc.exModelo.nmMod, "
						+ "		doc.idDoc, "
						+ "		doc.dtRegDoc "
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

			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				ExDocumento doc = (ExDocumento) obj[0];
				ExMobil mob = (ExMobil) obj[1];
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
									long numPag = (long) blob.length / 51200;
									if (numPag < 1) {
										numPag = 1;
									}
									pagsBlobAnexo = pagsBlobAnexo + numPag;
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
//				listDados.add("<a href=" + parametros.get("link_siga") + siglaDoc + ">" + siglaDoc + "</a>" + alert);
				listDados.add(siglaDoc);
				listDados.add(doc.getDtDocDDMMYY()); 
				listDados.add(doc.getDtRegDocDDMMYY());
				listDados.add(doc.getCadastrante().getSigla());
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
			if (listLinhas.size() == 0) {
				throw new AplicacaoException("Não foram encontrados documentos para os dados informados.");
			}
			return d;
		}
	}