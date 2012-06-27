package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import play.db.jpa.JPA;

import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

import models.SrSolicitacao;

public class SrSolicitacaoFiltro extends SrSolicitacao {

	public String dtIni;

	public String dtFim;

	public CpMarcador situacao;

	public DpPessoa atendente;

	public DpLotacao lotaAtendente;

	public List<SrSolicitacao> buscar() {
		String wheres = "where 1=1 ";

		if (cadastrante != null)
			wheres += "and sol.cadastrante.idPessoaIni = "
					+ cadastrante.getIdInicial();
		if (lotaCadastrante != null)
			wheres += "and sol.lotaCadastrante.idLotacaoIni = "
					+ lotaCadastrante.getIdInicial();
		if (solicitante != null)
			wheres += "and sol.solicitante.idPessoaIni = "
					+ solicitante.getIdInicial();
		if (lotaSolicitante != null)
			wheres += "and sol.lotaSolicitante.idLotacaoIni = "
					+ lotaSolicitante.getIdInicial();
		if (itemConfiguracao != null && itemConfiguracao.idItemConfiguracao > 0L)
			wheres += "and sol.itemConfiguracao.idItemConfiguracao = "
					+ itemConfiguracao.idItemConfiguracao;
		if (servico != null && servico.idServico > 0L)
			wheres += "and sol.servico.idServico = " + servico.idServico;
		if (urgencia != null && urgencia.nivelUrgencia > 0)
			wheres += "and sol.urgencia = " + (urgencia.nivelUrgencia - 1);
		if (tendencia != null && tendencia.nivelTendencia > 0)
			wheres += "and sol.tendencia = " + (tendencia.nivelTendencia -1);
		if (gravidade != null && gravidade.nivelGravidade > 0)
			wheres += "and sol.gravidade = " + (gravidade.nivelGravidade -1);

		if (descrSolicitacao != null && !descrSolicitacao.trim().equals("")) {
			for (String s : descrSolicitacao.split(" "))
				wheres += "and lower(sol.descrSolicitacao) like '%"
						+ s.toLowerCase() + "%' ";
		}

		final SimpleDateFormat dfUsuario = new SimpleDateFormat("dd/MM/yyyy");
		final SimpleDateFormat dfHibernate = new SimpleDateFormat("yyyy-MM-dd");

		if (dtIni != null)
			try {
				wheres += "and sol.dtReg >= to_date('"
						+ dfHibernate.format(dfUsuario.parse(dtIni))
						+ "', 'yyyy-MM-dd') ";
			} catch (ParseException e) {
				//
			}

		if (dtFim != null)
			try {
				wheres += "and sol.dtReg <= to_date('"
						+ dfHibernate.format(dfUsuario.parse(dtFim))
						+ "', 'yyyy-MM-dd') ";
			} catch (ParseException e) {
				//
			}

		String wheresSituacao = "";

		if (situacao != null && situacao.getIdMarcador() != null
				&& situacao.getIdMarcador() > 0)
			wheresSituacao += " and situacao.cpMarcador.idMarcador = "
					+ situacao.getIdMarcador();
		if (atendente != null)
			wheresSituacao += "and situacao.dpPessoaIni.idPessoa = "
					+ atendente.getIdInicial();
		else if (lotaAtendente != null)
			wheresSituacao += "and situacao.dpLotacaoIni.idLotacao = "
					+ lotaAtendente.getIdInicial();

		wheres += wheresSituacao;

		String from = "select distinct(sol) from SrSolicitacao sol ";

		if (wheresSituacao.length() > 0)
			from += "inner join sol.marcaSet situacao ";

		List listaRetorno = JPA.em().createQuery(from + wheres).getResultList();

		return listaRetorno;
	}
}
