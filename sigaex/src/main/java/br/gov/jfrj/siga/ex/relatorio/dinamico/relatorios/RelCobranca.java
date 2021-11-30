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

import javax.persistence.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import net.sf.jasperreports.engine.JRException;

	public class RelCobranca extends RelatorioTemplate {
 
		public List<String> listColunas;
		public List<List<String>> listLinhas;
		public Long totalDocumentos;
		public Long totalPaginas;
		public Long totalBlobsDoc;
		public Long totalBlobsAnexos;

		public RelCobranca (Map parametros) throws DJBuilderException {
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
			this.listColunas.add("Unidade");
			this.listColunas.add("Nome do Documento");
			this.listColunas.add("Núm.");
			this.listColunas.add("Qtd Páginas");
			this.listColunas.add("Tamanho Docto.");
			this.listColunas.add("Tamanho Arq. Anexo");
					
			this.addColuna("Unidade", 10, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Nome do Documento", 40, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Núm.", 20, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Qtd Pág.", 10, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Tam.Doc.", 10, RelatorioRapido.ESQUERDA, false);
//			this.addColuna("Tam.Anexo", 10, RelatorioRapido.ESQUERDA, false);
			return this;

		}

		@Override
		public Collection processarDados() throws Exception {

			List<String> d = new ArrayList<String>();
			ExDao dao = ExDao.getInstance();
			ExMobil mob = null;
			String siglaDoc = "";
			
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			String queryOrgao = "";
			if (parametros.get("orgao") != null && parametros.get("orgao") != "") {
				queryOrgao = "and doc.orgaoUsuario.idOrgaoUsu = :orgao ";
			}

			String queryLotacao = "";
			if (parametros.get("lotacao") != null
					&& parametros.get("lotacao") != "") {
				queryLotacao = " and doc.lotaCadastrante.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :idLotacao) ";
			}
			
			Query query = ContextoPersistencia.em().createQuery(
							"select "
							+ "doc.lotaCadastrante.siglaLotacao, "
							+ "doc.exModelo.nmMod, "
							+ "mob.idMobil, "
							+ "doc.numPaginas, "
							+ "length(doc.conteudoBlobDoc) "
//							+ "sum(length(mov.conteudoBlobMov)) "
							+ "from ExMovimentacao mov "
							+ "right join mov.exMobil mob "
							+ "inner join mob.exDocumento doc "
							+ "where mov.dtIniMov between :dtini and :dtfim "
//							+ "where (:dtIni is null or doc.dtDoc >= :dtini) "
//							+ "	and (:dtFim is null or doc.dtDoc <= :dtfim) "
							+ queryOrgao
							+ queryLotacao
							+ "	and mov.exTipoMovimentacao.idTpMov = '1' "
//							+ "		or mov.exTipoMovimentacao.idTpMov = '64') "
							+ "order by doc.lotaCadastrante.siglaLotacao, "
							+ "doc.exModelo.nmMod, "
							+ "mob.idMobil "
							);
			
			if (parametros.get("orgao") != null && parametros.get("orgao") != "") {
				query.setParameter("orgao", Long.valueOf((String) parametros.get("orgao")));
			}
			
			if (parametros.get("lotacao") != null && parametros.get("lotacao") != "") {
				Query qryLota = ContextoPersistencia.em().createQuery(
						"from DpLotacao lot where lot.idLotacao = " + parametros.get("lotacao"));
							
				Set<DpLotacao> lotacaoSet = new HashSet<DpLotacao>();
				DpLotacao lotacao = (DpLotacao)qryLota.getResultList().get(0);
				lotacaoSet.add(lotacao);
				
				query.setParameter("idLotacao",
						lotacao.getIdInicial());
			}

			Date dtini = formatter.parse((String) parametros.get("dataInicial"));
			query.setParameter("dtini", dtini);
			Date dtfim = formatter.parse((String) parametros.get("dataFinal"));
			query.setParameter("dtfim", dtfim);

			Iterator it = query.getResultList().iterator();

			totalDocumentos = 0L;
			totalPaginas = 0L;
			totalBlobsDoc = 0L;
			totalBlobsAnexos = 0L;

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
				Long pags = 0L;
				Long blobsDoc = 0L;
				Long blobsAnexos = 0L;
				
				if (obj[3] != null) {
					pags = Long.valueOf(obj[3].toString());
				}

				if (obj[4] != null) {
					blobsDoc = Long.valueOf(obj[4].toString());
				}
					
//				if (obj[5] != null) {
//					blobsAnexos = Long.valueOf(obj[5].toString());
//				}
					
				List<String> listDados = new ArrayList();
				listDados.add(lotaDoc);
				listDados.add(modeloDoc);
				listDados.add(siglaDoc);
				listDados.add(pags.toString());
				listDados.add(blobsDoc.toString());
				listDados.add(blobsAnexos.toString());
				listLinhas.add(listDados);
				d.add(lotaDoc);
				d.add(modeloDoc);
				d.add(siglaDoc);
				d.add(pags.toString());
				d.add(blobsDoc.toString());
//				d.add(blobsAnexos.toString());
				
				totalDocumentos = totalDocumentos + 1; 
				totalPaginas = totalPaginas + pags; 
				totalBlobsDoc = totalBlobsDoc + blobsDoc; 
				totalBlobsAnexos = totalBlobsAnexos + blobsAnexos; 
			}
			if (d.size() == 0) {
				throw new Exception("Não foram encontrados documentos para os dados informados.");
			}
			
			return d;
		}

	}