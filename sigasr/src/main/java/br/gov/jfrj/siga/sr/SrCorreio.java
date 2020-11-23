package br.gov.jfrj.siga.sr;

import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;


public class SrCorreio {
	
	private static String remetente = System.getProperty("servidor.smtp.usuario.remetente");

	public static void notificarAbertura(SrSolicitacao sol) throws Exception{
		/*if (sol.solicitante.getEmailPessoa() == null)
			return;
		
		StringBuffer sbHtml = new StringBuffer("<html><body>");
		sbHtml.append("<p>Informamos que foi aberta por "); 
		sbHtml.append(sol.cadastrante.getDescricaoIniciaisMaiusculas()); 
		sbHtml.append("("+sol.lotaCadastrante.getSiglaLotacao()+") "); 
		sbHtml.append("em " + sol.getDtRegDDMMYYYYHHMM()+", "); 
		sbHtml.append("a solicita&ccedil;&atilde;o <b>" + sol.getCodigo() + "</b>, ");
		sbHtml.append("com a seguinte descri&ccedil;&atilde;o: </p>");
		sbHtml.append("<blockquote><p>" + sol.descrSolicitacao + "</p></blockquote>");
		sbHtml.append("<p>Para acessar a solicita&ccedil;&atilde;o, "
				+ "clique <a href=\"@@{Application.exibir(sol.idSolicitacao)}\">aqui</a></p>");
		sbHtml.append("</body></html>");
		Correio.enviar(sol.solicitante.getEmailPessoa(), "Abertura da solicitação " + sol.getCodigo(), sbHtml.toString());
*/
	}

	public static void notificarMovimentacao(SrMovimentacao movimentacao) throws Exception{
		/*SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
		DpPessoa pessoaAtual = sol.solicitante.getPessoaAtual();
		if (pessoaAtual == null || pessoaAtual.getEmailPessoa() == null)
			return;
	
		StringBuffer sbHtml = new StringBuffer("<html><body>");
		sbHtml.append("<p>Informamos que a solicitação <b>" + sol.getCodigo() + " </b>");
		sbHtml.append("recebeu a seguinte movimentacao em " + movimentacao.getDtIniMovDDMMYYYYHHMM()+": </p>");
		sbHtml.append("<blockquote><p>Tipo de movimentação: "+ movimentacao.tipoMov.nome + "</p>");
		sbHtml.append("<p>" +movimentacao.descrMovimentacao + "</p>");
		sbHtml.append("<p>Por "+movimentacao.cadastrante.getDescricaoIniciaisMaiusculas());
		sbHtml.append("("+movimentacao.lotaCadastrante.getSiglaLotacao()+")</p></blockquote>");
		sbHtml.append("<p>Para acessar a solicitação, clique <a href=\"@@{Application.exibir(sol.idSolicitacao)}\">aqui</a>.</p>");
		sbHtml.append("</body></html>");
		
		Correio.enviar(pessoaAtual.getEmailPessoa(), "Movimentação da solicitação " + sol.getCodigo(), sbHtml.toString());*/
	}

	public static void notificarAtendente(SrMovimentacao movimentacao) {
		/*SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
		
		List<String> recipients = new ArrayList<String>();
		DpPessoa atendenteSolPai = sol.solicitacaoPai.getAtendente();
		if (atendenteSolPai != null && atendenteSolPai.getEmailPessoa() != null)
			recipients.add(atendenteSolPai.getEmailPessoa());
		else {
			DpLotacao lotaAtendenteSolPai = sol.solicitacaoPai
					.getLotaAtendente();
			if (lotaAtendenteSolPai != null)
				for (DpPessoa pessoaDaLotacao : lotaAtendenteSolPai
						.getDpPessoaLotadosSet())
					if (pessoaDaLotacao.getDataFim() == null
							&& pessoaDaLotacao.getEmailPessoa() != null)
						recipients.add(pessoaDaLotacao.getEmailPessoa());
		}
		
		if (recipients.size() < 1)
			return;
					
		StringBuffer sb = new StringBuffer(), sbHtml = new StringBuffer();
		
		
		Correio.enviar(remetente, recipients.toArray(), "Movimentação da solicitação " + sol.getCodigo(), sb.toString(), sbHml.toString());
		*/
	}

	public static void notificarReplanejamentoMovimentacao(
			SrMovimentacao movimentacao) {
		/*SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
		List<String> recipients = new ArrayList<String>();
		setSubject("Movimentação da solicitação " + sol.getCodigo());
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		for (SrGestorItem gestor : sol.itemConfiguracao.gestorSet) {
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
		recipients.add(sol.solicitante.getEmailPessoa());
		if (recipients.size() > 0) {
			addRecipient(recipients.toArray());
			send(movimentacao, sol);
		}*/
	}

	public static void notificarCancelamentoMovimentacao(
			SrMovimentacao movimentacao) {
	/*	SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
		DpPessoa solicitanteAtual = sol.solicitante.getPessoaAtual();
		if (solicitanteAtual.getEmailPessoa() == null)
			return;
		setSubject("Movimentação da solicitação " + sol.getCodigo());
		addRecipient(solicitanteAtual.getEmailPessoa());
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		send(movimentacao, sol);*/
	}

	public static void pesquisaSatisfacao(SrSolicitacao sol) throws Exception {
		/*SrSolicitacao solAtual = sol.getSolicitacaoAtual();
		DpPessoa solicitanteAtual = solAtual.solicitante.getPessoaAtual();
		if (solicitanteAtual.getEmailPessoa() == null)
			return;
		setSubject("Pesquisa de Satisfação da solicitação "
				+ solAtual.getCodigo());
		addRecipient(solicitanteAtual.getEmailPessoa());
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		send(sol);*/
	}

}
