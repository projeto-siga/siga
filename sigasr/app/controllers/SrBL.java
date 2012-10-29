package controllers;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import notifiers.Correio;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.mail.HtmlEmail;

import play.db.jpa.JPA;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.bl.CpBL;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import models.SrAndamento;
import models.SrConfiguracao;
import models.SrEstado;
import models.SrMarca;
import models.SrSolicitacao;

public class SrBL extends CpBL{

	public  SrDao dao() {
		return SrDao.getInstance();
	}

	public  SrSolicitacao criar(SrSolicitacao solicitacao,
			DpPessoa cadastrante, DpLotacao lotaCadastrante)
			throws AplicacaoException {
		solicitacao.cadastrante = cadastrante;
		solicitacao.lotaCadastrante = lotaCadastrante;
		return criar(solicitacao);
	}

	public  SrSolicitacao criar(SrSolicitacao solicitacao)
			throws AplicacaoException {

		solicitacao.dtReg = new Date();

		if (solicitacao.lotaCadastrante == null)
			solicitacao.lotaCadastrante = solicitacao.cadastrante.getLotacao();

		if (solicitacao.solicitante == null)
			solicitacao.solicitante = solicitacao.cadastrante;

		if (solicitacao.lotaSolicitante == null)
			solicitacao.lotaSolicitante = solicitacao.solicitante.getLotacao();

		solicitacao.orgaoUsuario = solicitacao.lotaSolicitante
				.getOrgaoUsuario();

		if (solicitacao.solicitacaoPai == null)
			solicitacao.numSolicitacao = solicitacao.buscarProximoNumero();
		else
			solicitacao.numSolicitacao = null;

		solicitacao = dao().salvar(solicitacao, solicitacao.cadastrante);

		SrAndamento andamento = new SrAndamento();

		if (solicitacao.fecharAoAbrir) {
			andamento.estado = SrEstado.FECHADO;
			andamento.descrAndamento = solicitacao.motivoFechamentoAbertura;
		} else {
			andamento.estado = SrEstado.ANDAMENTO;
			andamento.descrAndamento = "Criação da solicitação";
		}

		andamento.solicitacao = solicitacao;
		andamento.cadastrante = solicitacao.cadastrante;
		andamento.lotaCadastrante = solicitacao.lotaCadastrante;

		// Antes de existir configuração pro primeiro antendente...
		DpPessoa eeh = JPA.em().find(DpPessoa.class, 10374L);

		andamento.atendente = eeh;
		andamento.lotaAtendente = eeh.getLotacao();

		darAndamento(andamento);

		notificar(solicitacao);

		return solicitacao;
	}

	public  SrAndamento darAndamento(DpPessoa cadastrante,
			DpLotacao lotaCadastrante, DpPessoa atendente,
			DpLotacao lotaAtendente, String descricao, SrEstado estado) {
		SrAndamento andamento = new SrAndamento();
		andamento.cadastrante = cadastrante;
		andamento.lotaCadastrante = lotaCadastrante;
		andamento.atendente = atendente;
		andamento.lotaAtendente = lotaAtendente;
		andamento.descrAndamento = descricao;
		andamento.estado = estado;
		return darAndamento(andamento);
	}

	public  SrAndamento darAndamento(SrAndamento andamento,
			DpPessoa cadastrante, DpLotacao lotaCadastrante) {

		andamento.cadastrante = cadastrante;
		andamento.lotaCadastrante = lotaCadastrante;
		return darAndamento(andamento);
	}

	public  SrAndamento darAndamento(SrAndamento novoAndamento) {

		novoAndamento.dtReg = new Date();

		if (novoAndamento.atendente != null
				&& novoAndamento.lotaAtendente == null)
			novoAndamento.lotaAtendente = novoAndamento.atendente.getLotacao();

		if (novoAndamento.solicitacao.houveQualquerAndamento()) {

			SrAndamento anterior = novoAndamento.solicitacao
					.getUltimoAndamento();

			if (!novoAndamento.temAtendenteOuLota()) {

				novoAndamento.lotaAtendente = anterior.lotaAtendente;

				novoAndamento.atendente = anterior.atendente;
			}
			if (novoAndamento.estado == null)
				novoAndamento.estado = anterior.estado;

		}

		novoAndamento = dao().salvar(novoAndamento);

		// O refresh é ecessário para o hibernate incluir o novo andamento na
		// coleção de andamentos da solicitação
		//Plim
		//dao().refresh(novoAndamento.solicitacao);
		

		atualizarMarcas(novoAndamento);

		notificar(novoAndamento);

		return novoAndamento;
	}

	public  void notificar(SrSolicitacao sol) {
		Correio.notificarAbertura(sol);
	}

	public  void notificar(SrAndamento andamento) {
		if (!andamento.isCriacao())
			Correio.notificarAndamento(andamento);
	}

	public  SrAndamento desfazerUltimoAndamento(SrSolicitacao sol,
			DpPessoa cadastrante, DpLotacao lotaCadastrante) {

		return desfazerUltimoAndamento(sol.getUltimoAndamento(), cadastrante,
				lotaCadastrante);
	}

	public  SrAndamento desfazerUltimoAndamento(SrAndamento andamento,
			DpPessoa cadastrante, DpLotacao lotaCadastrante) {
		SrAndamento cancelamento = new SrAndamento();
		cancelamento.solicitacao = andamento.solicitacao;
		cancelamento.cadastrante = cadastrante;
		cancelamento.lotaCadastrante = lotaCadastrante;
		cancelamento.descrAndamento = "Cancelando movimentação...";
		darAndamento(cancelamento);

		andamento.andamentoCancelador = cancelamento;
		andamento.save();
		return cancelamento;
	}

	public  void marcar(SrSolicitacao solicitacao, Long idMarcador,
			DpLotacao lotacao, DpPessoa pessoa) {
		CpMarcador marcador = JPA.em().find(CpMarcador.class, idMarcador);
		marcar(solicitacao, marcador, lotacao, pessoa);
	}

	public  void marcar(SrSolicitacao solicitacao, CpMarcador marcador,
			DpLotacao lotacao, DpPessoa pessoa) {
		SrMarca marca2 = new SrMarca();
		marca2.setCpMarcador(marcador);
		marca2.setDpLotacaoIni(lotacao.getLotacaoInicial());
		if (pessoa != null)
			marca2.setDpPessoaIni(pessoa.getPessoaInicial());
		marca2.setDtIniMarca(new Date());
		marca2.solicitacao = solicitacao;
		SrDao.getInstance().salvar(marca2);
	}

	public  void atualizarMarcas(SrAndamento andamento) {

		for (SrSolicitacao sol : andamento.solicitacao
				.getHistoricoSolicitacao())
			if (sol.marcaSet != null)
				for (SrMarca marca : sol.marcaSet)
					JPA.em().remove(marca);

		Long marcador;

		if (andamento.estado == SrEstado.FECHADO)
			marcador = CpMarcador.MARCADOR_SOLICITACAO_FECHADO;
		else if (andamento.estado == SrEstado.PENDENTE)
			marcador = CpMarcador.MARCADOR_SOLICITACAO_PENDENTE;
		else if (andamento.estado == SrEstado.CANCELADO)
			marcador = CpMarcador.MARCADOR_SOLICITACAO_CANCELADO;
		else
			marcador = CpMarcador.MARCADOR_SOLICITACAO_A_RECEBER;

		marcar(andamento.solicitacao.getSolicitacaoAtual(), marcador,
				andamento.lotaAtendente, andamento.atendente);

	}

	public  void atualizarMarcas(SrSolicitacao sol) {
		atualizarMarcas(sol.getUltimoAndamento());
	}

	

}
