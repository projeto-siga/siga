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

import javax.persistence.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import net.sf.jasperreports.engine.JRException;

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
			if (parametros.get("orgao") != null && !"".equals(parametros.get("orgao"))) {
				queryOrgao = "and doc.orgaoUsuario.idOrgaoUsu = :orgao ";
			}

			String queryLotacao = "";
			if (parametros.get("lotacao") != null && !"".equals(parametros.get("lotacao"))) {
				queryLotacao = " and doc.lotaCadastrante.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :idLotacao) ";
			}
			String queryUsuario = "";
			if (parametros.get("usuario") != null && !"".equals(parametros.get("usuario"))) {
				queryUsuario = "and mov.cadastrante.idPessoaIni in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :usuario) ";
			}
			
			Query query = ContextoPersistencia.em().createQuery(
						"select "
						+ "doc.idDoc, "
						+ "doc.orgaoUsuario.siglaOrgaoUsu, "
						+ "doc.orgaoUsuario.acronimoOrgaoUsu, "
						+ "doc.exFormaDocumento.siglaFormaDoc, "
						+ "doc.anoEmissao, "
						+ "doc.numExpediente, "
						+ "lota.siglaLotacao, "
						+ "lota.nomeLotacao, "
						+ "mod.nmMod, "
						+ "cad.sesbPessoa, "
						+ "cad.matricula, "
						+ "subs.sesbPessoa, "
						+ "subs.matricula, "
						+ "doc.dtDoc "
						+ "from ExMobil mob "
						+ "inner join mob.exDocumento doc "
						+ "left outer join doc.exModelo mod "
						+ "left outer join doc.lotaCadastrante lota "
						+ "left outer join doc.cadastrante cad "
						+ "left outer join doc.subscritor subs "
						+ "where "
						+ "		mob in (select distinct(mob1) from ExMobil mob1 join mob1.exMarcaSet label"
						+ "			left outer join label.dpLotacaoIni.orgaoUsuario orgaoUsu "
						+ "			left outer join label.dpPessoaIni.orgaoUsuario orgaoUsu "
						+ "			where label.cpMarcador.idMarcador = :idMarcador "
						+ "			and (label.dpLotacaoIni.orgaoUsuario.idOrgaoUsu = :orgaoPesqId "
						+ "			or label.dpPessoaIni.orgaoUsuario.idOrgaoUsu = :orgaoPesqId) "
						+ "			and (label.dtIniMarca is null or label.dtIniMarca < :dbDatetime) " 
						+ "			and (label.dtFimMarca is null or label.dtFimMarca > :dbDatetime)) "
						+ "		and doc.dtDoc >= :dtini and doc.dtDoc < :dtfim "
						+ "		and doc.dtFinalizacao is not null "
						+ queryOrgao
						+ queryLotacao
						+ queryUsuario
						+ "order by "
						+ "lota.siglaLotacao, "
						+ "lota.nomeLotacao, "
						+ "mod.nmMod "
						);

			query.setParameter("idMarcador", CpMarcadorEnum.COMO_INTERESSADO.getId());
			query.setParameter("orgaoPesqId", Long.valueOf((String) parametros.get("orgaoPesqId")));
			
			if (parametros.get("orgao") != null && !"".equals(parametros.get("orgao"))) {
				query.setParameter("orgao", Long.valueOf((String) parametros.get("orgao")));
			}
			
			if (parametros.get("lotacao") != null &&  !"".equals(parametros.get("lotacao"))) {
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
				query.setParameter("idUsuario", pessoa.getIdPessoaIni());
			}

			Date dtini = formatter.parse((String) parametros.get("dataInicial"));
			query.setParameter("dtini", dtini);
			Date dtfim = formatter.parse((String) parametros.get("dataFinal"));
			Date dtfimMaisUm = new Date( dtfim.getTime() + 86400000L );
			query.setParameter("dtfim", dtfimMaisUm);
			
			query.setParameter("dbDatetime", ExDao.getInstance().consultarDataEHoraDoServidor());
			
			Iterator it = query.getResultList().iterator();

			totalDocumentos = 0L;
			Long lastMobilId = 0L;
			Object[] obj = null;
			int gcCounter = 0; 
			String lotacao;
			String modelo;
			String siglaDoc;
			String cadastrante;
			String subscritor;
			String dtAssinatura;

			while (it.hasNext()) {
				obj = (Object[]) it.next();
				lotacao = obj[6].toString() + " / " + obj[7].toString(); 
				modelo = obj[8].toString();
						
				String codigoDoc = ExDocumento.getCodigo(Long.valueOf(obj[0].toString()), obj[1].toString(),
						obj[2].toString(), obj[3].toString(), Long.valueOf(obj[4].toString()),
						Long.valueOf(obj[5].toString()), null, null, null, null, 
						null, null, null, null, null, null, null, null);
				
				siglaDoc = "<a href=" + parametros.get("link_siga") 
						+ codigoDoc + ">" + codigoDoc + "</a>";
				cadastrante = obj[9].toString() + obj[10].toString(); 
				if (obj[11] != null) {
					subscritor = obj[11].toString() + obj[12].toString(); 
				} else {
					subscritor = obj[9].toString() + obj[10].toString() + " (Autentic.)"; 
				}
				SimpleDateFormat formatFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				Date dt = formatFull.parse(obj[13].toString());
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				dtAssinatura = format.format(dt);				

				List<String> listDados = new ArrayList();
				listDados.add(lotacao);
				listDados.add(modelo);
				listDados.add(siglaDoc);
				listDados.add(cadastrante);
				listDados.add(subscritor);
				listDados.add(dtAssinatura);
				listLinhas.add(listDados);
				totalDocumentos = totalDocumentos + 1; 
				if (gcCounter > 200) {
					gcCounter = 0;
					System.gc();
				} else {
					gcCounter += 1;
				}
			}
			if (listLinhas.size() == 0) {
				throw new Exception("Não foram encontrados documentos para os dados informados.");
			}
			
			listComparator comparator = new listComparator();
			Collections.sort(listLinhas, comparator);
			for (List<String> lin : listLinhas) {
				for (String dado : lin) {
					d.add(dado);
				}
			}
			return d;
		}
		
		class listComparator implements Comparator<List<String>> {
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
						
				if ((list1.get(0) + list1.get(1)).compareTo(list2.get(0) + list2.get(1)) > 0) {
					return 1; 
				} else {
					if ((list1.get(0) + list1.get(1)).compareTo(list2.get(0) + list2.get(1)) < 0) {
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
	