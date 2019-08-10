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

import net.sf.jasperreports.engine.JRException;

	import org.hibernate.Query;

	import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

	public class RelDocumentosProduzidos extends RelatorioTemplate {
 
		public List<String> listColunas;
		public List<String> listDados;
		public Long totalDocumentos;
		public Long totalPaginas;
		public Long totalTramitados;

		public RelDocumentosProduzidos (Map parametros) throws DJBuilderException {
			super(parametros);
			if (parametros.get("dataInicial") == null) {
				throw new DJBuilderException("Parâmetro dataInicial não informado!");
			}
			if (parametros.get("dataFinal") == null) {
				throw new DJBuilderException("Parâmetro dataFinal não informado!");
			}
			listColunas = new ArrayList<String>();
			listDados = new ArrayList<String>();
		}

		@Override
		public AbstractRelatorioBaseBuilder configurarRelatorio()
				throws DJBuilderException, JRException {

			this.setTitle("Indicadores de Produção");
			this.listColunas.add("Unidade");
			this.listColunas.add("Nome do Documento");
			this.listColunas.add("Quantidade");
					
			this.addColuna("Unidade", 20, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Nome do Documento", 40, RelatorioRapido.ESQUERDA, false);
////			" da Lotação " + lotacao.getSigla();
////			" do Usuário " + pessoa.getSigla();
			this.addColuna("Quantidade", 60, RelatorioRapido.ESQUERDA, false);
			return this;

		}

		@Override
		public Collection processarDados() throws Exception {

			List<String> d = new ArrayList<String>();
			
			// Total de documentos criados (por orgao, lotação ou usuário)
			String addSelect = "mov.lotaResp.siglaLotacao, "
							+ "doc.exModelo.descMod, "
							+ "count(distinct doc.idDoc), "
							+ "sum(doc.numPaginas) ";
			String addWhere = "and mov.exTipoMovimentacao.idTpMov =  1 "
							+ "group by mov.lotaResp.siglaLotacao,"
							+ "doc.exModelo.descMod ";
			Iterator it = obtemDados(d, addSelect, addWhere);
			totalPaginas = 0L;
			totalDocumentos = 0L;
			
			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				String lotaDoc = (String) obj[0];
				String modeloDoc = (String) obj[1];
				listDados.add(lotaDoc);
				listDados.add(modeloDoc);
				listDados.add(obj[2].toString());
				d.add(lotaDoc);
				d.add(modeloDoc);
				d.add(obj[2].toString());
				Long docs = Long.valueOf(obj[2].toString());
				Long pags = Long.valueOf(obj[3].toString());
				totalDocumentos = totalDocumentos + docs; 
				totalPaginas = totalPaginas + pags; 
				
			}
			if (d.size() == 0) {
				throw new Exception("Não foram encontrados documentos para os dados informados.");
			}
			
			return d;
		}

		public void processarDadosTramitados() throws Exception {
			List<String> d = new ArrayList<String>();
			
			// Total de documentos tramitadas pelo menos uma vez (por lotação ou usuário)
			String addSelect = "count(distinct doc.idDoc) ";
			String addWhere = "and (mov.exTipoMovimentacao.idTpMov =  3) ";
			Iterator it = obtemDados(d, addSelect, addWhere);

			if (it.hasNext()) {
				totalTramitados = (Long) it.next();
			} else {
				totalTramitados = 0L;
			}
		}
			
		private Iterator obtemDados(List<String> d, String addSelect, String addWhere)
				throws ParseException {

			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			
			String queryOrgao = "";
			if (parametros.get("orgao") != null && parametros.get("orgao") != "") {
				queryOrgao = "and doc.orgaoUsuario.idOrgaoUsu = :orgao ";
			}
			
			String queryLotacao = "";
			if (parametros.get("lotacao") != null && parametros.get("lotacao") != "") {
				queryLotacao = "and mov.lotaCadastrante.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :lotacao) ";
			}
			
			String queryUsuario = "";
			if (parametros.get("usuario") != null && parametros.get("usuario") != "") {
				queryUsuario = "and mov.cadastrante.idPessoaIni in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :usuario) ";
			}
			
			Query query = HibernateUtil
					.getSessao()
					.createQuery(
							"select "
							+ addSelect
							+ "from ExMovimentacao mov inner join mov.exMobil mob "
							+ "inner join mob.exDocumento doc "
							+ "where mov.dtIniMov between :dtini and :dtfim "
							+ queryOrgao
							+ queryLotacao
							+ queryUsuario
							+ addWhere
							);
			
//			if (parametros.get("orgao") != null && parametros.get("orgao") != "") {
//				Query qryLota = HibernateUtil.getSessao().createQuery(
//						"from CpOrgaoUsuario org where org.idOrgaoUsu = " + parametros.get("orgao"));
//							
//				Set<CpOrgaoUsuario> orgaoSet = new HashSet<CpOrgaoUsuario>();
//				CpOrgaoUsuario orgao = (CpOrgaoUsuario)qryLota.list().get(0);
//				orgaoSet.add(orgao);
//				
//				query.setParameter("orgao",
//						orgao.getId());
//			}
//
			if (parametros.get("orgao") != null && parametros.get("orgao") != "") {
				query.setLong("orgao", Long.valueOf((String) parametros.get("orgao")));
			}
			
			if (parametros.get("lotacao") != null && parametros.get("lotacao") != "") {
				Query qryLota = HibernateUtil.getSessao().createQuery(
						"from DpLotacao lot where lot.idLotacao = " + parametros.get("lotacao"));
							
				Set<DpLotacao> lotacaoSet = new HashSet<DpLotacao>();
				DpLotacao lotacao = (DpLotacao)qryLota.list().get(0);
				lotacaoSet.add(lotacao);
				
				query.setParameter("lotacao",
						lotacao.getIdInicial());
			}

			if (parametros.get("usuario") != null && parametros.get("usuario") != "") {
				Query qryPes = HibernateUtil.getSessao().createQuery(
						"from DpPessoa pes where pes.idPessoa = " + parametros.get("usuario"));
							
				Set<DpPessoa> pessoaSet = new HashSet<DpPessoa>();
				DpPessoa pessoa = (DpPessoa)qryPes.list().get(0);
				pessoaSet.add(pessoa);
				
				query.setParameter("usuario",
						pessoa.getIdPessoaIni());
			}

			Date dtini = formatter.parse((String) parametros.get("dataInicial"));
			query.setDate("dtini", dtini);
			Date dtfim = formatter.parse((String) parametros.get("dataFinal"));
			query.setDate("dtfim", dtfim);

			Iterator it = query.list().iterator();
			return it;
		}
		 
	}