package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import models.SrAtributo;
import models.SrSolicitacao;
import play.db.jpa.JPA;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class SrSolicitacaoFiltro extends SrSolicitacao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean pesquisar = false;

	public String dtIni;

	public String dtFim;

	public CpMarcador situacao;

	public DpPessoa atendente;

	public DpLotacao lotaAtendente;

	public boolean naoDesignados;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<SrSolicitacao> buscar() throws Exception {
		StringBuffer query = new StringBuffer(" from SrSolicitacao sol where ");
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
		}
		
		StringBuffer subqueryAtributo = new StringBuffer();
		if (meuAtributoSet != null && meuAtributoSet.size() > 0) {
			SrAtributo att = meuAtributoSet.get(0);
			subqueryAtributo.append(" and att.tipoAtributo.idTipoAtributo = "
					+ att.tipoAtributo.idTipoAtributo);
			if (att.valorAtributo != null && !att.valorAtributo.equals(""))
				subqueryAtributo.append(" and att.valorAtributo = '"
						+ att.valorAtributo + "' ");
		}

		if (subqueryAtributo.length() > 0) {
			subqueryAtributo.insert(0, " and exists (from SrAtributo att where att.solicitacao.solicitacaoInicial = sol.solicitacaoInicial ");
			subqueryAtributo.append(" )");
		}

		List listaRetorno = JPA
				.em()
				.createQuery(
						query.toString() + subquery.toString() + subqueryAtributo.toString() + " order by sol.idSolicitacao desc")
				.getResultList();

		return listaRetorno;
	}
}
