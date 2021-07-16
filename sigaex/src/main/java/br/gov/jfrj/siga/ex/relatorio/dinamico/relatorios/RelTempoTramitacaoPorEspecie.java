package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import net.sf.jasperreports.engine.JRException;

	public class RelTempoTramitacaoPorEspecie extends RelatorioTemplate {
 
		public List<String> listColunas;
		public List<List<String>> listLinhas;
		public List<List<String>> listEspecie;
		public Long totalDocumentos;
		private Long totalEspecieDias;
		private Long totalEspecieDocs;

		public RelTempoTramitacaoPorEspecie (Map parametros) throws DJBuilderException {
			super(parametros);
			if (parametros.get("dataInicial") == null) {
				throw new DJBuilderException("Parâmetro dataInicial não informado!");
			}
			if (parametros.get("dataFinal") == null) {
				throw new DJBuilderException("Parâmetro dataFinal não informado!");
			}
			listColunas = new ArrayList<String>();
			listLinhas = new ArrayList<List<String>>();
			listEspecie = new ArrayList<List<String>>();
		}

		@Override
		public AbstractRelatorioBaseBuilder configurarRelatorio()
				throws DJBuilderException, JRException {

			this.listColunas.add("Unidade");
			this.listColunas.add("Nome do Documento");
			this.listColunas.add("No. Documento");
			this.listColunas.add("Tempo Tramitação (dias)");
			this.listColunas.add("Cadastrante");
			this.listColunas.add("Resp. Assinatura / Autenticação");
					
			this.addColuna(this.listColunas.get(0), 15, RelatorioRapido.ESQUERDA, false);
			this.addColuna(this.listColunas.get(1), 30, RelatorioRapido.ESQUERDA, false);
			this.addColuna(this.listColunas.get(2), 20, RelatorioRapido.CENTRO, false);
			this.addColuna(this.listColunas.get(3), 15, RelatorioRapido.CENTRO, false);
			this.addColuna(this.listColunas.get(4), 15, RelatorioRapido.CENTRO, false);
			this.addColuna(this.listColunas.get(5), 15, RelatorioRapido.CENTRO, false);
			return this;

		}

		@Override
		public Collection processarDados() throws Exception {
			
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
			

			List<String> d = new ArrayList<String>();
			List<List<String>> listDocs = new ArrayList<List<String>>();
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			String queryEspecie = "";
			if (parametros.get("especie") != null && !"".equals(parametros.get("especie"))) {
				queryEspecie = "and doc.exFormaDocumento.idFormaDoc = :especie ";
			}

			String queryOrgao = "";
			if (parametros.get("orgao") != null && !"".equals(parametros.get("orgao"))) {
				queryOrgao = "and doc.orgaoUsuario.idOrgaoUsu = :orgao ";
			}

			String queryLotacao = "";
			if (parametros.get("lotacao") != null && !"".equals(parametros.get("lotacao"))) {
				queryLotacao = " and doc.lotaCadastrante.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :idLotacao) ";
			}
			String queryUsuario = "";
			if (parametros.get("usuario") != null && !"".equals(parametros.get("usuario"))) {
				queryUsuario = "and doc.cadastrante.idPessoa in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :usuario) ";
			}
			
			Query query = ContextoPersistencia.em().createQuery(
						"select "
						+ "mob, "
						+ "mov, "
						+ "forma.idFormaDoc, "
						+ "forma.descrFormaDoc, "
						+ "lota.siglaLotacao, "
						+ "lota.nomeLotacao, "
						+ "mod.nmMod, "
						+ "cad.sesbPessoa, "
						+ "cad.matricula, "
						+ "subs.sesbPessoa, "
						+ "subs.matricula, "
						+ "doc.dtFinalizacao "
						+ "from ExMovimentacao mov "
						+ "inner join mov.exMobil mob " 
						+ "inner join mob.exDocumento doc "
						+ "left outer join doc.lotaCadastrante lota "
						+ "left outer join doc.exFormaDocumento forma "
						+ "left outer join doc.exModelo mod "
						+ "left outer join doc.cadastrante cad "
						+ "left outer join doc.subscritor subs "
						+ "where (mov.exTipoMovimentacao.idTpMov = :idTpMov1 "
						+ "		or mov.exTipoMovimentacao.idTpMov = :idTpMov2 "
						+ "		or mov.exTipoMovimentacao.idTpMov = :idTpMov3 "
						+ "		or mov.exTipoMovimentacao.idTpMov = :idTpMov4 "
						+ "		or mov.exTipoMovimentacao.idTpMov = :idTpMov5 "
						+ "		or mov.exTipoMovimentacao.idTpMov = :idTpMov6 "
						+ "		or mov.exTipoMovimentacao.idTpMov = :idTpMov7) "
						+ " 	and mov.exMovimentacaoCanceladora is null "
						+ "		and doc.dtRegDoc >= :dtini and doc.dtRegDoc < :dtfim "
						+ "		and doc.dtFinalizacao is not null "
						+ queryEspecie
						+ queryOrgao
						+ queryLotacao
						+ queryUsuario
						+ "order by forma.descrFormaDoc, "
						+ "lota.siglaLotacao, "
						+ "doc.idDoc, "
						+ "mob.idMobil, "
						+ "mov.idMov "
						);

			query.setParameter("idTpMov1", ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA);
			query.setParameter("idTpMov2", ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA);
			query.setParameter("idTpMov3", ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA);
			query.setParameter("idTpMov4", ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA);
			query.setParameter("idTpMov5", ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE);
			query.setParameter("idTpMov6", ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE);
			query.setParameter("idTpMov7", ExTipoMovimentacao.TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO);			
			
			if (parametros.get("especie") != null && !"".equals(parametros.get("especie"))) {
				query.setParameter("especie", Long.valueOf((String) parametros.get("especie")));
			}
			
			if (parametros.get("orgao") != null && !"".equals(parametros.get("orgao"))) {
				query.setParameter("orgao", Long.valueOf((String) parametros.get("orgao")));
			}
			
			if (parametros.get("lotacao") != null && !"".equals(parametros.get("lotacao"))) {
				Query qryLota = ContextoPersistencia.em().createQuery(
						"from DpLotacao lot where lot.idLotacao = " + parametros.get("lotacao"));
				Set<DpLotacao> lotacaoSet = new HashSet<DpLotacao>();
				DpLotacao lotacao = (DpLotacao)qryLota.getResultList().get(0);
				lotacaoSet.add(lotacao);
				query.setParameter("idLotacao",	lotacao.getIdInicial());
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

			Date dtini = formatter.parse((String) parametros.get("dataInicial"));
			query.setParameter("dtini", dtini);
			Date dtfim = formatter.parse((String) parametros.get("dataFinal"));
			Date dtfimMaisUm = new Date( dtfim.getTime() + 86400000L );
			query.setParameter("dtfim", dtfimMaisUm);
			
			Iterator it = query.getResultList().iterator();

			String idFormaDoc = "";
			String especie = "";
			String lotacao = "";
			String modelo = "";
			String siglaDoc = "";
			String cadastrante = "";
			String subscritor = "";
			Long dtMov1 = 0L;
			Long dtMov2 = 0L;
			int gcCounter = 0;
			Long dtCancel = 0L;
			Object[] obj = null;
			boolean tramitou = false;

			while (it.hasNext()) {
				obj = (Object[]) it.next();
				ExMobil mob = (ExMobil) obj[0];
				ExDocumento doc = (ExDocumento) mob.getDoc();
				if (Ex.getInstance().getBL().exibirQuemTemAcessoDocumentosLimitados(
							 doc,titular, lotaTitular)) {
					Long idDoc1 = doc.getIdDoc();
					Long idMob1 = mob.getId();
					ExMovimentacao mov1 = (ExMovimentacao) obj[1];
					Long qtdDias = 0L;
					Long qtdTram = 0L;
					
					while (it.hasNext()) {
						idFormaDoc = obj[2].toString(); 
						especie = obj[3].toString();
						lotacao = obj[4].toString() + "/" + obj[5].toString(); 
						modelo = obj[6].toString(); 
						siglaDoc = doc.getSigla();
						cadastrante = obj[7].toString() + obj[8].toString(); 
						if (obj[9] != null) {
							subscritor = obj[9].toString() + obj[10].toString(); 
						} else {
							subscritor = obj[7].toString() + obj[8].toString() + " (Autentic.)"; 
						}
	
						obj = (Object[]) it.next();
						mob = (ExMobil) obj[0];
						doc = (ExDocumento) mob.getDoc();
						Long idMob2 = mob.getId();
						Long idDoc2 = doc.getIdDoc();
						ExMovimentacao mov2 = (ExMovimentacao) obj[1];
						dtMov1 = getOnlyDate(mov1.getDtMov());
						dtMov2 = getOnlyDate(mov2.getDtMov());
						if (mov1.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO) {
							dtCancel = getOnlyDate(mov1.getDtMov());
						}
						
						if (idDoc1 == idDoc2) {
							if (idMob1 == idMob2) {
								if (!(mov2.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE 
										&& mov1.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE)
										&& mov1.getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO
										&& ((mov1.getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE 
											&& mov1.getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE)
											|| tramitou)) {
									if (mov1.getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE) { 
										qtdTram += 1L;
									}
									qtdDias += (dtMov2 - dtMov1);
									tramitou = true;
								}
							} else {
								if (mov1.getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE 
										&& mov1.getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO) {
									qtdTram += 1L;
									if (dtCancel == 0) {
										qtdDias += (getOnlyDate(new Date()) - dtMov1);
									} else {
										qtdDias += (dtCancel - dtMov1);
									}
								}
								if (mov2.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE 
									|| mov2.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE) {									
									tramitou = false;
								} else {
									tramitou = true;
								}
							}
						} else {
							if (mov1.getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE 
								&& mov1.getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO) {
								qtdTram += 1L;
								if (dtCancel == 0) {
									qtdDias += (getOnlyDate(new Date()) - dtMov1);
								} else {
									qtdDias += (dtCancel - dtMov1);
								}
							}
							if (qtdTram != 0) {
								addLinha(idFormaDoc, especie, lotacao, modelo, siglaDoc, cadastrante,
										subscritor, qtdDias, listDocs);
							}
							qtdDias = 0L;
							qtdTram = 0L;
							dtCancel = 0L;
							tramitou = false;
						}
						if (gcCounter > 200) {
							gcCounter = 0;
							System.gc();
						} else {
							gcCounter += 1;
						}
						mov1 = mov2;
						idDoc1 = idDoc2;
						idMob1 = idMob2;
					}
					// Grava e totaliza o ultimo documento / especie
					siglaDoc = doc.getSigla();
					if (obj[9] != null) {
						subscritor = obj[9].toString() + obj[10].toString(); 
					} else {
						subscritor = obj[7].toString() + obj[8].toString() + " (Autentic.)"; 
					}
					ExMovimentacao mov2 = (ExMovimentacao) obj[1];
					dtMov2 = getOnlyDate(mov2.getDtMov());
					if (mov2.getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE) {
						qtdTram += 1L;
						if (dtCancel == 0) {
							qtdDias += (getOnlyDate(new Date()) - dtMov2);
						} else {
							qtdDias += (dtCancel - dtMov2);
						}
					}
					if (qtdTram != 0) { 
						addLinha(obj[2].toString(), obj[3].toString(), 
								obj[4].toString() + "/" + obj[5].toString(), 
								obj[6].toString(), siglaDoc, obj[7].toString() + obj[8].toString(),
								subscritor, qtdDias, listDocs);
					}
				}
			}
			if (listDocs.size() == 0) {
				throw new AplicacaoException("Não foram encontrados documentos para os dados informados.");
			}
			geraListEspecie(listDocs);
			listComparator comparator = new listComparator();
			Collections.sort(listLinhas, comparator);
			listEspecieComparator comparatorEspecie = new listEspecieComparator();
			Collections.sort(listEspecie, comparatorEspecie);
			String lotaAnt = "";
			String modeloAnt = "";
			for (List<String> lin : listLinhas) {
				if ((lin.get(0)).equals(lotaAnt)) { 
					lin.set(0, "");
					if (lin.get(1).equals(modeloAnt)) { 
						lin.set(1, "");
					} else {
						modeloAnt = lin.get(1);
					}
				} else {
					lotaAnt = lin.get(0);
					modeloAnt = lin.get(1);
				}
				for (String dado : lin) {
					d.add(dado);
				}
				lin.set(2, "<a href=" + parametros.get("link_siga") 
						+ lin.get(2) + ">" + lin.get(2) + "</a>");
			}
			return d;
		}

		private void geraListEspecie(List<List<String>> listDocs) {
			totalDocumentos = 0L;
			totalEspecieDias = 0L;
			totalEspecieDocs = 0L;
			String lastIdFormaDoc = "";
			String lastEspecie = "";

			for (List<String> lin :listDocs) {
				if (!lin.get(1).equals(lastEspecie)) {
					if (!lastEspecie.equals("")) {
						addEspecie(lastIdFormaDoc, lastEspecie);
					}
					totalEspecieDias = 0L;
					totalEspecieDocs = 0L;
					lastIdFormaDoc = lin.get(0); 
					lastEspecie = lin.get(1);
				}
				totalEspecieDias += Long.valueOf(lin.get(5));
				totalEspecieDocs += 1L;
				totalDocumentos += 1L;
				lin.remove(0);
				lin.remove(0);
				listLinhas.add(lin); 
			}
			addEspecie(lastIdFormaDoc, lastEspecie);
		}

		private void addEspecie(String lastIdFormaDoc, String lastEspecie) {
			List<String> dadosEspecie = new ArrayList(); 
			dadosEspecie.add("<a href='#' class='text-primary' onclick=\"javascript:visualizarRelatorio('" 
					+ parametros.get("link_especie") + "','"  
					+ lastIdFormaDoc + "','" + lastEspecie + "');\">" 
					+ lastEspecie + "</a>");
			dadosEspecie.add(totalEspecieDocs.toString());
			Long media = (long) ((((double) totalEspecieDias / (double) totalEspecieDocs)) + 0.5);
			dadosEspecie.add(media.toString());
			listEspecie.add(dadosEspecie);
		}

		private void addLinha(String idFormaDoc, String especie, String lotacao, String modelo, String siglaDoc,
				String cadastrante, String subscritor, Long qtdDias, List<List<String>> listDocs) {
			List<String> listDados = new ArrayList();
			listDados.add(idFormaDoc); 
			listDados.add(especie); 
			listDados.add(lotacao); 
			listDados.add(modelo); 
			listDados.add(siglaDoc);
			listDados.add(qtdDias.toString()); 
			listDados.add(cadastrante); 
			listDados.add(subscritor); 
			listDocs.add(listDados);
		}

		private static long getOnlyDate(Date dtFull) throws ParseException {
		    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		    return formatter.parse(formatter.format(dtFull)).getTime() / 86400000 ;
		}		
		
		class listComparator implements Comparator<List<String>> {
			@Override
			public int compare(List<String> list1, List<String> list2) {
				if ((list1.get(0) + list1.get(1) + ((Long) (999999999999999L - Long.valueOf(list1.get(3)))).toString())
					.compareTo(list2.get(0) + list2.get(1) + ((Long) (999999999999999L - Long.valueOf(list2.get(3)))).toString()) > 0) 
					return 1; 
				else if ((list1.get(0) + list1.get(1) + ((Long) (999999999999999L - Long.valueOf(list1.get(3)))).toString())
						.compareTo(list2.get(0) + list2.get(1) + ((Long) (999999999999999L - Long.valueOf(list2.get(3)))).toString()) < 0) 
						return -1; 
					else
						return 0;
			}
		}
		
		class listEspecieComparator implements Comparator<List<String>> {
			@Override
			public int compare(List<String> list1, List<String> list2) {
				if (Long.valueOf(list1.get(2)).compareTo(Long.valueOf(list2.get(2))) < 0) 
					return 1; 
				else if (Long.valueOf(list1.get(2)).compareTo(Long.valueOf(list2.get(2))) > 0)
						return -1; 
					else
						return 0;
			}
		}
	}	