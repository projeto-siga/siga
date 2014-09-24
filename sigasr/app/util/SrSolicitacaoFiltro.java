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

	public boolean pesquisar = false;

	public String dtIni;

	public String dtFim;

	public CpMarcador situacao;

	public DpPessoa atendente;

	public DpLotacao lotaAtendente;

	public boolean naoDesignados;

	public List<SrSolicitacao> buscar(boolean mostrarDesativados) throws Exception {

		String query = "from SrSolicitacao sol where ";
		
		if (!mostrarDesativados)
			query += " sol.hisDtFim is null ";
		else
			query += " 1 = 1 ";
		

		if (cadastrante != null)
			query += " and sol.cadastrante.idPessoaIni = "
					+ cadastrante.getIdInicial();
		if (lotaCadastrante != null)
			query += " and sol.lotaCadastrante.idLotacaoIni = "
					+ lotaCadastrante.getIdInicial();
		if (solicitante != null)
			query += " and sol.solicitante.idPessoaIni = "
					+ solicitante.getIdInicial();
		if (lotaSolicitante != null)
			query += " and sol.lotaSolicitante.idLotacaoIni = "
					+ lotaSolicitante.getIdInicial();
		if (itemConfiguracao != null
				&& itemConfiguracao.idItemConfiguracao > 0L)
			query += " and sol.itemConfiguracao.idItemConfiguracao = "
					+ itemConfiguracao.idItemConfiguracao;
		if (acao != null && acao.idAcao > 0L)
			query += " and sol.acao.idAcao = " + acao.idAcao;
		if (urgencia != null && urgencia.nivelUrgencia > 0)
			query += " and sol.urgencia = " + urgencia.ordinal();
		if (tendencia != null && tendencia.nivelTendencia > 0)
			query += " and sol.tendencia = " + tendencia.ordinal();
		if (gravidade != null && gravidade.nivelGravidade > 0)
			query += " and sol.gravidade = " + gravidade.ordinal();

		if (descrSolicitacao != null && !descrSolicitacao.trim().equals("")) {
			for (String s : descrSolicitacao.split(" "))
				query += " and lower(sol.descrSolicitacao) like '%"
						+ s.toLowerCase() + "%' ";
		}
				
		final SimpleDateFormat dfUsuario = new SimpleDateFormat("dd/MM/yyyy");
		final SimpleDateFormat dfHibernate = new SimpleDateFormat("yyyy-MM-dd");

		if (dtIni != null)
			try {
				query += " and sol.dtReg >= to_date('"
						+ dfHibernate.format(dfUsuario.parse(dtIni))
						+ "', 'yyyy-MM-dd') ";
			} catch (ParseException e) {
				//
			}

		if (dtFim != null)
			try {
				query += " and sol.dtReg <= to_date('"
						+ dfHibernate.format(dfUsuario.parse(dtFim))
						+ " 23:59', 'yyyy-MM-dd HH24:mi') ";
			} catch (ParseException e) {
				//
			}

		String subquery = "";
		
		if (situacao != null && situacao.getIdMarcador() != null
				&& situacao.getIdMarcador() > 0)
			subquery += " and situacao.cpMarcador.idMarcador = "
					+ situacao.getIdMarcador();
		if (atendente != null)
			subquery += "and situacao.dpPessoaIni.idPessoa = "
					+ atendente.getIdInicial();
		else if (lotaAtendente != null) {
			if (naoDesignados)
				subquery += "and situacao.dpLotacaoIni.idLotacao = "
						+ lotaAtendente.getIdInicial() + " and situacao.dpPessoaIni is null";
			else
				subquery += "and situacao.dpLotacaoIni.idLotacao = "
						+ lotaAtendente.getIdInicial();
		}
		
		if (subquery.length() > 0)
			subquery = " and exists (from SrMarca situacao where situacao.solicitacao.solicitacaoInicial = sol.solicitacaoInicial "
					+ subquery + " )";
		
		String subqueryAtributo = "";
		if (meuAtributoSet != null && meuAtributoSet.size() > 0) {
			SrAtributo att = meuAtributoSet.get(0);
			subqueryAtributo += " and att.tipoAtributo.idTipoAtributo = "
					+ att.tipoAtributo.idTipoAtributo;
			if (att.valorAtributo != null && !att.valorAtributo.equals(""))
				subqueryAtributo += " and att.valorAtributo = '"
						+ att.valorAtributo + "' ";
		}

		if (subqueryAtributo.length() > 0)
			subqueryAtributo = " and exists (from SrAtributo att where att.solicitacao.solicitacaoInicial = sol.solicitacaoInicial "
					+ subqueryAtributo + " )";

		List listaRetorno = JPA
				.em()
				.createQuery(
						query + subquery + subqueryAtributo + " order by sol.idSolicitacao desc")
				.getResultList();

		return listaRetorno;
	}
}
