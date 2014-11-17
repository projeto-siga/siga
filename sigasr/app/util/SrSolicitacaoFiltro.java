package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import models.SrAtributo;
import models.SrSolicitacao;
import models.SrTipoAtributo;
import play.db.jpa.JPA;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class SrSolicitacaoFiltro extends SrSolicitacao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String AND = " AND ";

	public boolean pesquisar = false;

	public String dtIni;

	public String dtFim;

	public CpMarcador situacao;

	public DpPessoa atendente;

	public DpLotacao lotaAtendente;

	public boolean naoDesignados;
	
	public boolean apenasFechados;

	public Long idNovoAtributo;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<SrSolicitacao> buscar() throws Exception {
		String query = montarBusca(" from SrSolicitacao sol where ");

		List listaRetorno = JPA
				.em()
				.createQuery( query )
				.getResultList();

		return listaRetorno;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> buscarSimplificado() throws Exception{
		String query = montarBusca("select sol.idSolicitacao, sol.descrSolicitacao, sol.codigo, item.tituloItemConfiguracao"
				+ " from SrSolicitacao sol inner join sol.itemConfiguracao as item where ");
		
		List<Object[]> listaRetorno =  JPA
				.em()
				.createQuery( query )
				.setMaxResults(10)
				.getResultList();
		
		return listaRetorno;
	}
	
	private String montarBusca(String queryString) {
		
		StringBuffer query = new StringBuffer(queryString);
		query.append(" sol.hisDtFim is null ");
		
		if (cadastrante != null)
			query.append(" and sol.cadastrante.idPessoaIni = "
					+ cadastrante.getIdInicial());
		if (lotaCadastrante != null)
			query.append(" and sol.lotaCadastrante.idLotacaoIni = "
					+ lotaCadastrante.getIdInicial());
		if (solicitante != null)
			query.append(" and sol.solicitante.idPessoaIni = "
					+ solicitante.getIdInicial());
		if (itemConfiguracao != null
				&& itemConfiguracao.idItemConfiguracao > 0L)
			query.append(" and sol.itemConfiguracao.itemInicial.idItemConfiguracao = "
					+ itemConfiguracao.itemInicial.idItemConfiguracao);
		if (acao != null && acao.idAcao > 0L)
			query.append(" and sol.acao.acaoInicial.idAcao = "
					+ acao.acaoInicial.idAcao);
		if (urgencia != null && urgencia.nivelUrgencia > 0)
			query.append(" and sol.urgencia = " + urgencia.ordinal());
		if (tendencia != null && tendencia.nivelTendencia > 0)
			query.append(" and sol.tendencia = " + tendencia.ordinal());
		if (gravidade != null && gravidade.nivelGravidade > 0)
			query.append(" and sol.gravidade = " + gravidade.ordinal());
		
		if (descrSolicitacao != null && !descrSolicitacao.trim().equals("")) {
			for (String s : descrSolicitacao.split(" "))
				query.append(" and lower(sol.descrSolicitacao) like '%"
						+ s.toLowerCase() + "%' ");
		}
		
		final SimpleDateFormat dfUsuario = new SimpleDateFormat("dd/MM/yyyy");
		final SimpleDateFormat dfHibernate = new SimpleDateFormat("yyyy-MM-dd");
		
		if (dtIni != null)
			try {
				query.append(" and sol.dtReg >= to_date('"
						+ dfHibernate.format(dfUsuario.parse(dtIni))
						+ "', 'yyyy-MM-dd') ");
			} catch (ParseException e) {
				//
			}
		
		if (dtFim != null)
			try {
				query.append(" and sol.dtReg <= to_date('"
						+ dfHibernate.format(dfUsuario.parse(dtFim))
						+ " 23:59', 'yyyy-MM-dd HH24:mi') ");
			} catch (ParseException e) {
				//
			}
		
		StringBuffer subquery = new StringBuffer();
		
		if (situacao != null && situacao.getIdMarcador() != null
				&& situacao.getIdMarcador() > 0)
			subquery.append(" and situacao.cpMarcador.idMarcador = "
					+ situacao.getIdMarcador());
		if (atendente != null)
			subquery.append("and situacao.dpPessoaIni.idPessoa = "
					+ atendente.getIdInicial());
		else if (lotaAtendente != null) {
			if (naoDesignados)
				subquery.append("and situacao.dpLotacaoIni.idLotacao = "
						+ lotaAtendente.getIdInicial()
						+ " and situacao.dpPessoaIni is null");
			else
				subquery.append("and situacao.dpLotacaoIni.idLotacao = "
						+ lotaAtendente.getIdInicial());
		}
		
		if (subquery.length() > 0) {
			subquery.insert(0, " and exists (from SrMarca situacao where situacao.solicitacao.solicitacaoInicial = sol.solicitacaoInicial ");
			subquery.append(" )");
			query.append(subquery);
		}
		
		StringBuffer subqueryAtributo = new StringBuffer();
		if (meuAtributoSet != null && meuAtributoSet.size() > 0) {
			subqueryAtributo.append(" and (");

			for (SrAtributo att : meuAtributoSet) {
				subqueryAtributo.append("(");
				subqueryAtributo.append(" att.tipoAtributo.idTipoAtributo = " + att.tipoAtributo.idTipoAtributo);
				if (att.valorAtributo != null && !att.valorAtributo.equals("")) {
					subqueryAtributo.append(" and att.valorAtributo = '" + att.valorAtributo + "' ");
				}
				subqueryAtributo.append(")");
				subqueryAtributo.append(AND);
			}
			subqueryAtributo.setLength(subqueryAtributo.length() - AND.length());
			subqueryAtributo.append(" )");
		}
		
		if (subqueryAtributo.length() > 0) {
			subqueryAtributo.insert(0, " and exists (from SrAtributo att where att.solicitacao.solicitacaoInicial = sol.solicitacaoInicial ");
			subqueryAtributo.append(" )");
			query.append(subqueryAtributo);
		}
		
		if (apenasFechados) {
			query.append(" and not exists (from SrMovimentacao where tipoMov in (7,8) and solicitacao = sol.hisIdIni)");
		}
		
		return query.append(" order by sol.idSolicitacao desc").toString();
	}
	
	public List<SrTipoAtributo> getTiposAtributosConsulta() {
		List<SrTipoAtributo> tiposAtributosConsulta = new ArrayList<SrTipoAtributo>();
		
		if (meuAtributoSet != null) {
			for (SrAtributo srAtributo : meuAtributoSet) {
				tiposAtributosConsulta.add(srAtributo.tipoAtributo);
			}
		}
		return tiposAtributosConsulta;
	}
	
	public List<SrTipoAtributo> itensDisponiveis(List<SrTipoAtributo> tiposAtributosDisponiveis, SrTipoAtributo tipoAtributo) {
		ArrayList<SrTipoAtributo> arrayList = new ArrayList<SrTipoAtributo>(tiposAtributosDisponiveis);
		arrayList.add(tipoAtributo);
		return arrayList;
	}
}
