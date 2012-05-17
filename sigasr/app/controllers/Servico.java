package controllers;

import play.*;
import play.db.jpa.JPA;
import play.mvc.*;

import java.util.*;

import javax.persistence.Query;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

import models.*;

public class Servico extends Controller {

	public static void gravar(long idItemConfiguracao,
			Long idFormaAcompanhamento, String telPrincipal,
			String descrSolicitacao, String local, String motivoFechamento) {
		Long idPessoa = Long.valueOf(params.get("pessoaSolicitanteSel.id"));
		// DpPessoa solicitante = DpPessoa.findById(idPessoa);
		DpPessoa solicitante = JPA.em().find(DpPessoa.class, idPessoa);
		// query.getSingleResult();
		DpLotacao lotaSolicitante = solicitante.lotacao;

		SrFormaAcompanhamento acompanhamento = SrFormaAcompanhamento
				.findById(idFormaAcompanhamento);
		SrItemConfiguracao itemConfig = SrItemConfiguracao
				.findById(idItemConfiguracao);

		SrSolicitacao solicitacao = new SrSolicitacao(solicitante,
				lotaSolicitante, solicitante, lotaSolicitante, null,
				acompanhamento, itemConfig, null, descrSolicitacao,
				telPrincipal, local, motivoFechamento).save();

		listarTudo();
	}

	public static void listarTudo() {
		List<SrSolicitacao> listaSolicitacao = SrSolicitacao.all().fetch();
		render(listaSolicitacao);
	}

}