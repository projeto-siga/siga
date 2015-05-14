package br.gov.jfrj.siga.sr.notifiers;

import java.util.ArrayList;
import java.util.List;

import play.Logger;
import play.mvc.Mailer;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sr.model.SrGestorItem;
import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;

public class Correio extends Mailer {

	public static void notificarAbertura(SrSolicitacao sol) {
		DpPessoa pessoaAtual = sol.getSolicitante().getPessoaAtual();
		if (pessoaAtual == null || pessoaAtual.getEmailPessoa() == null)
			return;
		
		if (sol.isFilha())
			setSubject("Escalonamento da solicitação " + sol.getSolicitacaoPai().getCodigo());
		else
			setSubject("Abertura da solicitação " + sol.getCodigo());
		
		addRecipient(pessoaAtual.getEmailPessoa());
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		send(sol);

	}

	public static void notificarMovimentacao(SrMovimentacao movimentacao) {
		SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
		DpPessoa pessoaAtual = sol.getSolicitante().getPessoaAtual();
		if (pessoaAtual == null || pessoaAtual.getEmailPessoa() == null)
			return;
		setSubject("Movimentação da solicitação " + sol.getCodigo());
		addRecipient(pessoaAtual.getEmailPessoa());
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		send(movimentacao, sol);
	}

	public static void notificarAtendente(SrMovimentacao movimentacao) {
		SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
		setSubject("Movimentação da solicitação " + sol.getCodigo());
		List<String> recipients = new ArrayList<String>();
		String email = null;
		
		DpPessoa atendenteSolPai = sol.getSolicitacaoPai().getAtendente();
		if (atendenteSolPai != null) {
			email = atendenteSolPai.getPessoaAtual().getEmailPessoa();
			if (email != null)
				recipients.add(email);
		} 
		else {
			DpLotacao lotaAtendenteSolPai = sol.getSolicitacaoPai()
					.getLotaAtendente();
			if (lotaAtendenteSolPai != null)
				for (DpPessoa pessoaDaLotacao : lotaAtendenteSolPai
						.getDpPessoaLotadosSet())
					if (pessoaDaLotacao.getDataFim() == null) {
						email = pessoaDaLotacao.getPessoaAtual().getEmailPessoa();
						if (email != null)
							recipients.add(email);
					}
		}

		if (recipients.size() > 0)
			try {
				addRecipient(recipients.toArray());
				setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
				send(movimentacao, sol);
			} catch (Exception e) {
				Logger.error(e, "Nao foi possivel notificar o atendente");
			}
	}

	public static void notificarReplanejamentoMovimentacao(
			SrMovimentacao movimentacao) {
		SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
		List<String> recipients = new ArrayList<String>();
		setSubject("Movimentação da solicitação " + sol.getCodigo());
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		for (SrGestorItem gestor : sol.getItemConfiguracao().getGestorSet()) {
			DpPessoa pessoaGestorAtual = gestor.getDpPessoa().getPessoaAtual();
			if (pessoaGestorAtual != null
					&& pessoaGestorAtual.getDataFim() == null)
				if (pessoaGestorAtual.getEmailPessoa() != null)
					recipients.add(pessoaGestorAtual.getEmailPessoa());

			if (gestor.getDpLotacao() != null)
				for (DpPessoa gestorPessoa : gestor.getDpLotacao()
						.getDpPessoaLotadosSet())
					if (gestorPessoa.getPessoaAtual().getDataFim() == null)
						if (gestorPessoa.getPessoaAtual().getEmailPessoa() != null)
							recipients.add(gestorPessoa.getPessoaAtual()
									.getEmailPessoa());
		}
		recipients.add(sol.getSolicitante().getEmailPessoa());
		if (recipients.size() > 0) {
			addRecipient(recipients.toArray());
			send(movimentacao, sol);
		}
	}

	public static void notificarCancelamentoMovimentacao(
			SrMovimentacao movimentacao) {
		SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
		DpPessoa solicitanteAtual = sol.getSolicitante().getPessoaAtual();
		if (solicitanteAtual.getEmailPessoa() == null)
			return;
		setSubject("Movimentação da solicitação " + sol.getCodigo());
		addRecipient(solicitanteAtual.getEmailPessoa());
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		send(movimentacao, sol);
	}

	public static void pesquisaSatisfacao(SrSolicitacao sol) throws Exception {
		SrSolicitacao solAtual = sol.getSolicitacaoAtual();
		DpPessoa solicitanteAtual = solAtual.getSolicitante().getPessoaAtual();
		if (solicitanteAtual.getEmailPessoa() == null)
			return;
		setSubject("Pesquisa de Satisfação da solicitação "
				+ solAtual.getCodigo());
		addRecipient(solicitanteAtual.getEmailPessoa());
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		send(sol);
	}
}
