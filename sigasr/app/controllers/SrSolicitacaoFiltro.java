package controllers;

import java.util.Date;
import java.util.List;

import play.db.jpa.JPA;

import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

import models.SrSolicitacao;

public class SrSolicitacaoFiltro extends SrSolicitacao {

	public Integer tpAtendente;

	public Integer tpSolicitante;

	public Integer tpCadastrante;

	public Date dtIni;

	public Date dtFim;

	public CpMarcador situacao;

	public DpPessoa atendente;

	public DpLotacao lotaAtendente;

	public List<SrSolicitacao> buscarPorFiltro() {
		String wheres = "where 1=1 ";

		if (tpCadastrante != null && tpCadastrante == 1 && cadastrante != null)
			wheres += "and sol.cadastrante.id = " + cadastrante.getId();
		else if (tpCadastrante != null && tpCadastrante == 2
				&& lotaCadastrante != null)
			wheres += "and sol.lotaCadastrante.id = " + lotaCadastrante.getId();
		if (tpSolicitante != null && tpSolicitante == 1)
			wheres += "and sol.solicitante.id = " + solicitante.getId();
		else if (tpSolicitante != null && tpSolicitante == 2
				&& lotaSolicitante != null)
			wheres += "and sol.lotaSolicitante.id = " + lotaSolicitante.getId();
		if (itemConfiguracao != null && itemConfiguracao.idItemConfiguracao > 0)
			wheres += "and sol.itemConfiguracao.idItemConfiguracao = "
					+ itemConfiguracao.idItemConfiguracao;
		if (servico != null && servico.idServico > 0)
			wheres += "and sol.servico.idServico = " + servico.idServico;
		if (urgencia != null && urgencia.nivelUrgencia > 0)
			wheres += "and sol.urgencia = "
					+ urgencia.nivelUrgencia;
		if (tendencia != null && tendencia.nivelTendencia > 0)
			wheres += "and sol.tendencia = "
					+ tendencia.nivelTendencia;
		if (gravidade != null && gravidade.nivelGravidade > 0)
			wheres += "and sol.gravidade = "
					+ gravidade.nivelGravidade;

		if (descrSolicitacao != null && !descrSolicitacao.trim().equals("")) {
			for (String s : descrSolicitacao.split(" "))
				wheres += "and lower(sol.descrSolicitacao) like '%"
						+ s.toLowerCase() + "%' ";
		}

		String wheresSituacao = "";

		if (situacao != null && situacao.getIdMarcador() != 0)
			wheresSituacao += " and situacao.cpMarcador.idMarcador = ";
		if (tpAtendente != null && tpAtendente == 1 && atendente != null)
			wheresSituacao += "and situacao.dpPessoaIni.id = ";
		else if (tpAtendente != null && tpAtendente == 2
				&& lotaAtendente != null)
			wheresSituacao += "and situacao.dpLotacaoIni.id = ";

		wheres += wheresSituacao;

		String from = "from SrSolicitacao sol ";

		if (wheresSituacao.length() > 0)
			from += "inner join SrMarca situacao on situacao.solicitacao.idSolicitacao = sol.idSolicitacao";

		List listaRetorno = JPA.em().createQuery(from + wheres).getResultList();

		return listaRetorno;
	}
}
