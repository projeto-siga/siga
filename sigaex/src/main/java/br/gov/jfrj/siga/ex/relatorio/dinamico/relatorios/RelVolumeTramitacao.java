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
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

	public class RelVolumeTramitacao extends RelatorioTemplate {

		public List<String> listColunas;
		public List<String> listDados;

		public RelVolumeTramitacao(Map parametros) throws DJBuilderException {
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

			this.setTitle("Documentos Por Volume de Tramitação");
					
			this.addColuna("Tipo de Documento", 20, RelatorioRapido.ESQUERDA, false);
			this.addColuna("Total Tramitados", 40, RelatorioRapido.ESQUERDA, false);
////			" da Lotação " + lotacao.getSigla();
////			" do Usuário " + pessoa.getSigla();
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
									+ "doc.exModelo.nmMod, "
									+ "count(doc.idDoc) "
									+ "from ExMovimentacao mov inner join mov.exMobil mob "
									+ "inner join mob.exDocumento doc "
									+ "where mov.dtIniMov between :dtini and :dtfim "
									+ queryOrgao
									+ queryLotacao
									+ queryUsuario
									+ "and mov.exTipoMovimentacao.idTpMov =  3 "
									+ "group by doc.exModelo.nmMod "
									+ "order by count(doc.idDoc) desc "
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
			query.setMaxResults(5);

			Iterator it = query.list().iterator();
			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				String modeloDoc = (String) obj[0];
				listColunas.add(modeloDoc);
				listDados.add(obj[1].toString());
				d.add(modeloDoc);
				d.add(obj[1].toString());
			}
			if (d.size() == 0) {
				d.add("Não foram encontrados registros para os dados informados.");
				d.add("");
			}
			
			return d;
		}
	}