package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
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
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

	public class RelDocsOrgaoInteressado extends RelatorioTemplate {
 
		public List<String> listColunas;
		public List<List<String>> listLinhas;
		public Long totalDocumentos;
		public Long totalPaginas;
		public Long totalBlobsDoc;
		public Long totalBlobsAnexos;

		public RelDocsOrgaoInteressado (Map parametros) throws DJBuilderException {
			super(parametros);
			if (parametros.get("dataInicial") == null) {
				throw new DJBuilderException("Parâmetro dataInicial não informado!");
			}
			if (parametros.get("dataFinal") == null) {
				throw new DJBuilderException("Parâmetro dataFinal não informado!");
			}
			if (parametros.get("orgaoPesqId") == null) {
				throw new DJBuilderException("Órgão de pesquisa não informado!");
			}
			if (parametros.get("link_siga") == null) {
				throw new DJBuilderException("Parâmetro link_siga não informado!");
			}
			listColunas = new ArrayList<String>();
			listLinhas = new ArrayList<List<String>>();
		}

		@Override
		public AbstractRelatorioBaseBuilder configurarRelatorio()
				throws DJBuilderException, JRException {

			this.setTitle("Relatório de Documentos Por Órgão Interessado");
			this.listColunas.add("Unidade");
			this.listColunas.add("Nome do Documento");
			this.listColunas.add("Número do Documento");
			this.listColunas.add("Cadastrante");
			this.listColunas.add("Responsável pela Assinatura");
			this.listColunas.add("Data da Assinatura");
					
			this.addColuna("Unidade", 10, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Nome do Documento", 30, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Número do Documento", 15, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Cadastrante", 15, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Responsável pela Assinatura", 15, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Data da Assinatura", 15, RelatorioRapido.CENTRO, false);
			return this;

		}

		@Override
		public Collection processarDados() throws Exception {

			List<String> d = new ArrayList<String>();
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
			String queryUsuario = "";
			if (parametros.get("usuario") != null
					&& parametros.get("usuario") != "") {
				queryUsuario = "and mov.cadastrante.idPessoaIni in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :usuario) ";
			}
			
			Query query = HibernateUtil
					.getSessao()
					.createQuery(
						"select "
						+ "mob "
						+ "from ExMobil mob "
						+ "inner join mob.exDocumento doc " 
						+ "where "
						+ "		mob in (select distinct(mob1) from ExMobil mob1 join mob1.exMarcaSet label"
						+ "			left outer join label.dpLotacaoIni.orgaoUsuario orgaoUsu "
						+ "			left outer join label.dpPessoaIni.orgaoUsuario orgaoUsu "
						+ "			where label.cpMarcador.idMarcador = :idMarcador "
						+ "			and (label.dpLotacaoIni.orgaoUsuario.idOrgaoUsu = :orgaoPesqId "
						+ "			or label.dpPessoaIni.orgaoUsuario.idOrgaoUsu = :orgaoPesqId) "
						+ "			and (label.dtIniMarca is null or label.dtIniMarca < sysdate) " 
						+ "			and (label.dtFimMarca is null or label.dtFimMarca > sysdate)) "
						+ "		and doc.dtDoc between :dtini and :dtfim "
						+ queryOrgao
						+ queryLotacao
						+ queryUsuario
						+ "order by doc.lotaCadastrante.siglaLotacao, "
						+ "doc.exModelo.nmMod "
						);

			query.setLong("idMarcador", CpMarcador.MARCADOR_COMO_INTERESSADO);
			query.setLong("orgaoPesqId", Long.valueOf((String) parametros.get("orgaoPesqId")));
			
			if (parametros.get("orgao") != null && parametros.get("orgao") != "") {
				query.setLong("orgao", Long.valueOf((String) parametros.get("orgao")));
			}
			
			if (parametros.get("lotacao") != null && parametros.get("lotacao") != "") {
				Query qryLota = HibernateUtil.getSessao().createQuery(
						"from DpLotacao lot where lot.idLotacao = " + parametros.get("lotacao"));
				Set<DpLotacao> lotacaoSet = new HashSet<DpLotacao>();
				DpLotacao lotacao = (DpLotacao)qryLota.list().get(0);
				lotacaoSet.add(lotacao);
				query.setParameter("idLotacao",	lotacao.getIdInicial());
			}

			if (parametros.get("usuario") != null
					&& parametros.get("usuario") != "") {
				Query qryPes = HibernateUtil.getSessao().createQuery(
						"from DpPessoa pes where pes.idPessoa = "
								+ parametros.get("usuario"));
				Set<DpPessoa> pessoaSet = new HashSet<DpPessoa>();
				DpPessoa pessoa = (DpPessoa) qryPes.list().get(0);
				pessoaSet.add(pessoa);
				query.setParameter("idUsuario", pessoa.getIdPessoaIni());
			}

			Date dtini = formatter.parse((String) parametros.get("dataInicial"));
			query.setDate("dtini", dtini);
			Date dtfim = formatter.parse((String) parametros.get("dataFinal"));
			query.setDate("dtfim", dtfim);
			
			Iterator it = query.list().iterator();

			totalDocumentos = 0L;
			Long lastMobilId = 0L;

			while (it.hasNext()) {
				ExMobil mob = (ExMobil) it.next();
				List<String> listDados = new ArrayList();
				
				if (mob.getId() != lastMobilId) {
					String siglaDoc = "<a href=" + parametros.get("link_siga") 
							+ mob.getSigla() + ">" + mob.getSigla() + "</a>";
					ExDocumento doc = mob.getDoc();
					String lotaDoc = doc.getLotaCadastrante().getSigla();
					String cadastranteDoc = doc.getLotaCadastrante().getSigla() + " / " + doc.getCadastranteString();
					String subscritorDoc = "";
					String dataAssinaturaDoc = "";

					Query qryAssinaturas = HibernateUtil.getSessao().createQuery(
							"select mov "
							+ "from ExMovimentacao mov "
							+ "inner join mov.exMobil mob "
							+ "where mob.idMobil = :idMob "
							+ "		and (mov.exTipoMovimentacao.idTpMov = :idTpMovAssinaCert "
							+ "		or mov.exTipoMovimentacao.idTpMov = :idTpMovAssinaSenha) "
							);
					qryAssinaturas.setLong("idMob", mob.getIdMobil());
					qryAssinaturas.setLong("idTpMovAssinaCert", ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO);
					qryAssinaturas.setLong("idTpMovAssinaSenha", ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA);
								
					if (qryAssinaturas.list().size() != 0) {
						subscritorDoc = doc.getLotaSubscritor().getSigla() + " / " + doc.getSubscritor().getSigla();
						dataAssinaturaDoc = formatter.format(doc.getDtAssinatura());
					}
					
					listDados.add(lotaDoc);
					listDados.add(doc.getNmMod());
					listDados.add(siglaDoc);
					listDados.add(cadastranteDoc);
					listDados.add(subscritorDoc);
					listDados.add(dataAssinaturaDoc);
					listLinhas.add(listDados);
					d.add(lotaDoc);
					d.add(doc.getNmMod());
					d.add(siglaDoc);
					d.add(cadastranteDoc);
					d.add(subscritorDoc);
					d.add(dataAssinaturaDoc);
					
					totalDocumentos = totalDocumentos + 1; 
				}
				lastMobilId = mob.getId();
			}
			if (listLinhas.size() == 0) {
				throw new Exception("Não foram encontrados documentos para os dados informados.");
			}
			
			listComparator comparator = new listComparator();
			Collections.sort(listLinhas, comparator);
			return d;
		}
		
		private class listComparator implements Comparator<List<String>> {
			@Override
			public int compare(List<String> list1, List<String> list2) {
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Calendar cal = new GregorianCalendar(0001, 01, 01);
				Date dt1 = new Date();
				Date dt2 = new Date();
				
				try {
					dt1 = formatter.parse(list1.get(5));
				} catch (ParseException e) {
					dt1 = cal.getTime();
				}
				try {
					dt2 = formatter.parse(list2.get(5));
				} catch (ParseException e) {
					dt2 = cal.getTime();
				}
						
				if (list1.get(0).compareTo(list2.get(0)) > 0) {
					return 1; 
				} else {
					if (list1.get(0).compareTo(list2.get(0)) < 0) {
						return -1; 
					} else {
						if (list1.get(1).compareTo(list2.get(1)) > 0) { 
							return 1;
						} else {
							if (list1.get(1).compareTo(list2.get(1)) < 0) { 
								return -1;
							} else {
								if (dt1.after(dt2)) {
									return 1;
								} else {
									if (dt1.before(dt2)) {
										return -1;
									} else {
										return 0;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	