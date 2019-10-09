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

import net.sf.jasperreports.engine.JRException;

import org.hibernate.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
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
import br.gov.jfrj.siga.model.dao.HibernateUtil;

	public class RelVolumeTramitacaoPorModelo extends RelatorioTemplate {
 
		public List<String> listColunas;
		public List<List<String>> listLinhas;
		public List<List<String>> listModelos;
		public Long totalDocumentos;
		public Long totalTramites;
		public Long totalModeloTramites;
		public String modelo;

		public RelVolumeTramitacaoPorModelo (Map parametros) throws DJBuilderException {
			super(parametros);
			if (parametros.get("dataInicial") == null) {
				throw new DJBuilderException("Parâmetro dataInicial não informado!");
			}
			if (parametros.get("dataFinal") == null) {
				throw new DJBuilderException("Parâmetro dataFinal não informado!");
			}
			listColunas = new ArrayList<String>();
			listLinhas = new ArrayList<List<String>>();
			listModelos = new ArrayList<List<String>>();
		}

		@Override
		public AbstractRelatorioBaseBuilder configurarRelatorio()
				throws DJBuilderException, JRException {

			this.listColunas.add("Unidade");
			this.listColunas.add("Número do Documento");
			this.listColunas.add("Cadastrante");
			this.listColunas.add("Resp. Assinatura");
			this.listColunas.add("Dt Assinatura / Autenticação");
			this.listColunas.add("Tramitações");
					
			this.addColuna(this.listColunas.get(0), 10, RelatorioRapido.ESQUERDA, false);
			this.addColuna(this.listColunas.get(1), 30, RelatorioRapido.ESQUERDA, false);
			this.addColuna(this.listColunas.get(2), 20, RelatorioRapido.CENTRO, false);
			this.addColuna(this.listColunas.get(3), 20, RelatorioRapido.CENTRO, false);
			this.addColuna(this.listColunas.get(4), 20, RelatorioRapido.CENTRO, false);
			this.addColuna(this.listColunas.get(5), 5, RelatorioRapido.CENTRO, false);
			return this;

		}

		public void processarModelos() throws Exception {
			List<String> d = new ArrayList<String>();
			
			String queryOrgao = "";
			if (parametros.get("orgao") != null && parametros.get("orgao") != "") 
				queryOrgao = "and doc.orgaoUsuario.idOrgaoUsu = :orgao ";
			
			String queryLotacao = "";
			if (parametros.get("lotacao") != null
					&& parametros.get("lotacao") != "") 
				queryLotacao = " and doc.lotaCadastrante.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :idLotacao) ";
			
			String queryUsuario = "";
			if (parametros.get("usuario") != null
					&& parametros.get("usuario") != "") 
				queryUsuario = "and doc.cadastrante.idPessoa in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :usuario) ";
			
			Query query = HibernateUtil
					.getSessao()
					.createQuery(
						"select "
						+ "mod.hisIdIni, "
						+ "forma.descrFormaDoc, "
						+ "mod.nmMod, "
						+ "count(distinct doc.idDoc), "
						+ "count(mov.idMov) "
						+ "from ExMovimentacao mov "
						+ "inner join mov.exMobil mob " 
						+ "inner join mob.exDocumento doc "
						+ "left outer join doc.exModelo mod "
						+ "left outer join doc.exFormaDocumento forma "
						+ "where (mov.exTipoMovimentacao.idTpMov = :idTpMov1 "
						+ "		or mov.exTipoMovimentacao.idTpMov = :idTpMov2 "
						+ "		or mov.exTipoMovimentacao.idTpMov = :idTpMov3 "
						+ "		or mov.exTipoMovimentacao.idTpMov = :idTpMov4) "
						+ " 	and mov.exMovimentacaoCanceladora is null "
						+ "		and doc.dtRegDoc >= :dtini and doc.dtRegDoc < :dtfim "
						+ "		and doc.dtFinalizacao is not null "
						+ queryOrgao
						+ queryLotacao
						+ queryUsuario
						+ "group by "
						+ "mod.hisIdIni, "
						+ "forma.descrFormaDoc, "
						+ "mod.nmMod "
						+ "order by mod.nmMod "
						);

			query.setLong("idTpMov1", ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA);
			query.setLong("idTpMov2", ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA);
			query.setLong("idTpMov3", ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA);
			query.setLong("idTpMov4", ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA);

			setQueryParms(query);
			
			Iterator it = query.list().iterator();

			totalTramites = 0L;
			totalDocumentos = 0L;
			String idMod = "";
			String modelo = "";
			Long qtdDocs = 0L;
			Long qtdTram = 0L;
			Object[] obj = null;

			while (it.hasNext()) {
				obj = (Object[]) it.next();
				idMod = obj[0].toString();
				modelo = "<a href='#' class='text-primary' onclick=\"javascript:visualizarRelatorio('" 
						+ parametros.get("link_modelo") + "','"  
						+ idMod + "');\">" 
						+ obj[2].toString() + "</a>";
				qtdDocs = Long.valueOf(obj[3].toString()); 
				if (obj[4].toString() != null)
					qtdTram = Long.valueOf(obj[4].toString());

				List<String> listDados = new ArrayList();
				listDados.add(obj[1].toString()); 
				listDados.add(modelo); 
				listDados.add(qtdTram.toString()); 
				listModelos.add(listDados);
				totalTramites += qtdTram;
				totalDocumentos += qtdDocs;
			}
			if (listModelos.size() == 0) {
				throw new AplicacaoException("Não foram encontrados documentos para os dados informados.");
			}
//			for (List<String> lin : listModelos) {
//				for (String dado : lin) {
//					d.add(dado);
//				}
//			}
//			return d;
		}

		private void setQueryParms(Query query)
				throws ParseException {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			
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
				query.setParameter("usuario", pessoa.getIdPessoaIni());
			}

			Date dtini = formatter.parse((String) parametros.get("dataInicial"));
			query.setDate("dtini", dtini);
			Date dtfim = formatter.parse((String) parametros.get("dataFinal"));
			Date dtfimMaisUm = new Date( dtfim.getTime() + 86400000L );
			query.setDate("dtfim", dtfimMaisUm);
		}

		@Override
		public Collection processarDados() throws Exception {
			List<String> d = new ArrayList<String>();

			String queryModelo = "";
			if (parametros.get("idMod") != null && parametros.get("idMod") != "") 
				queryModelo = "and doc.exModelo.hisIdIni = :idMod ";
			
			String queryOrgao = "";
			if (parametros.get("orgao") != null && parametros.get("orgao") != "") 
				queryOrgao = "and doc.orgaoUsuario.idOrgaoUsu = :orgao ";
			
			String queryLotacao = "";
			if (parametros.get("lotacao") != null
					&& parametros.get("lotacao") != "") 
				queryLotacao = " and doc.lotaCadastrante.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :idLotacao) ";
			
			String queryUsuario = "";
			if (parametros.get("usuario") != null
					&& parametros.get("usuario") != "") 
				queryUsuario = "and doc.cadastrante.idPessoa in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :usuario) ";
			
			Query query = HibernateUtil
					.getSessao()
					.createQuery(
						"select "
						+ "doc.idDoc, "
						+ "doc.orgaoUsuario.siglaOrgaoUsu, "
						+ "doc.orgaoUsuario.acronimoOrgaoUsu, "
						+ "doc.exFormaDocumento.siglaFormaDoc, "
						+ "doc.anoEmissao, "
						+ "doc.numExpediente, "
						+ "lota.siglaLotacao, "
						+ "lota.nomeLotacao, "
						+ "forma.descrFormaDoc, "
						+ "mod.idMod, "
						+ "mod.nmMod, "
						+ "cad.sesbPessoa, "
						+ "cad.matricula, "
						+ "subs.sesbPessoa, "
						+ "subs.matricula, "
						+ "doc.dtDoc, "
						+ "count(mov.idMov) "
						+ "from ExMovimentacao mov "
						+ "inner join mov.exMobil mob " 
						+ "inner join mob.exDocumento doc "
						+ "left outer join doc.exModelo mod "
						+ "left outer join doc.lotaCadastrante lota "
						+ "left outer join doc.exFormaDocumento forma "
						+ "left outer join doc.cadastrante cad "
						+ "left outer join doc.subscritor subs "
						+ "where (mov.exTipoMovimentacao.idTpMov = :idTpMov1 "
						+ "		or mov.exTipoMovimentacao.idTpMov = :idTpMov2 "
						+ "		or mov.exTipoMovimentacao.idTpMov = :idTpMov3 "
						+ "		or mov.exTipoMovimentacao.idTpMov = :idTpMov4) "
						+ " 	and mov.exMovimentacaoCanceladora is null "
						+ "		and doc.dtRegDoc >= :dtini and doc.dtRegDoc < :dtfim "
						+ "		and doc.dtFinalizacao is not null "
						+ queryModelo
						+ queryOrgao
						+ queryLotacao
						+ queryUsuario
						+ "group by "
						+ "doc.idDoc, "
						+ "doc.orgaoUsuario.siglaOrgaoUsu, "
						+ "doc.orgaoUsuario.acronimoOrgaoUsu, "
						+ "doc.exFormaDocumento.siglaFormaDoc, "
						+ "doc.anoEmissao, "
						+ "doc.numExpediente, "
						+ "lota.siglaLotacao, "
						+ "lota.nomeLotacao, "
						+ "forma.descrFormaDoc, "
						+ "mod.idMod, "
						+ "mod.nmMod, "
						+ "cad.sesbPessoa, "
						+ "cad.matricula, "
						+ "subs.sesbPessoa, "
						+ "subs.matricula, "
						+ "doc.dtDoc "
						);

			query.setLong("idTpMov1", ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA);
			query.setLong("idTpMov2", ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA);
			query.setLong("idTpMov3", ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA);
			query.setLong("idTpMov4", ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA);

			if (parametros.get("idMod") != null && parametros.get("idMod") != "") {
				query.setLong("idMod", Long.valueOf((String) parametros.get("idMod")));
			}
			setQueryParms(query);
			
			Iterator it = query.list().iterator();

			totalModeloTramites = 0L;
			String especie = "";
			String lotacao = "";
			String idMod = "";
			String siglaDoc = "";
			String cadastrante = "";
			String subscritor = "";
			String dtAssinatura = "";
			Long qtdTram = 0L;
			int gcCounter = 0;
			Object[] obj = null;

			while (it.hasNext()) {
				obj = (Object[]) it.next();
				lotacao = obj[6].toString() + " / " + obj[7].toString(); 
				especie = obj[8].toString();
				idMod = obj[9].toString();
				modelo = obj[10].toString();
						
				String codigoDoc = ExDocumento.getCodigo(Long.valueOf(obj[0].toString()), obj[1].toString(),
						obj[2].toString(), obj[3].toString(), Long.valueOf(obj[4].toString()),
						Long.valueOf(obj[5].toString()), null, null, null, null, 
						null, null, null, null, null, null, null, null);
				
				siglaDoc = "<a href=" + parametros.get("link_siga") 
						+ codigoDoc + ">" + codigoDoc + "</a>";
				cadastrante = obj[11].toString() + obj[12].toString(); 
				if (obj[13] != null) {
					subscritor = obj[13].toString() + obj[14].toString(); 
				} else {
					subscritor = obj[11].toString() + obj[12].toString() + " (Autentic.)"; 
				}
				SimpleDateFormat formatFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				Date dt = formatFull.parse(obj[15].toString());
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				dtAssinatura = format.format(dt);				
				qtdTram = Long.valueOf(obj[16].toString());

				List<String> listDados = new ArrayList();
				listDados.add(lotacao); 
				listDados.add(siglaDoc);
				listDados.add(cadastrante); 
				listDados.add(subscritor); 
				listDados.add(dtAssinatura); 
				listDados.add(qtdTram.toString()); 
				listLinhas.add(listDados);
				totalModeloTramites += qtdTram;
				if (gcCounter > 200) {
					gcCounter = 0;
					System.gc();
				} else {
					gcCounter += 1;
				}
			}
			if (listLinhas.size() == 0) {
				throw new AplicacaoException("Não foram encontrados documentos para os dados informados.");
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
				if ((list1.get(0) + list1.get(1) + list1.get(2) + list1.get(3) + list1.get(4))
					.compareTo(list2.get(0) + list2.get(1) + list2.get(2) + list2.get(3) + list2.get(4)) > 0) 
					return 1; 
				else if ((list1.get(0) + list1.get(1) + list1.get(2) + list1.get(3) + list1.get(4))
						.compareTo(list2.get(0) + list2.get(1) + list2.get(2) + list2.get(3) + list2.get(4)) < 0) 
						return -1; 
					else
						return 0;
			}
		}
	}
	