package notifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import models.SrGestorItem;
import models.SrMovimentacao;
import models.SrSolicitacao;
import models.SrTipoMovimentacao;
import play.Logger;
import play.mvc.Mailer;
import br.gov.jfrj.siga.dp.DpPessoa;

public class Correio extends Mailer {

	public static void notificarAbertura(SrSolicitacao sol) {
		setSubject("Abertura da solicitação " + sol.getCodigo());
		addRecipient(sol.solicitante.getEmailPessoa());
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		send(sol);
		
	}
	
	public static void notificarMovimentacao(SrMovimentacao movimentacao) {
		SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
		setSubject("Movimentação da solicitação " + sol.getCodigo());
		addRecipient(sol.solicitante.getEmailPessoa());
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		send(movimentacao, sol);
	}
	
	public static void notificarAtendente(SrMovimentacao movimentacao) {
		SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
		setSubject("Movimentação da solicitação " + sol.getCodigo());
		
		DpPessoa atendenteSolPai = sol.solicitacaoPai.getAtendente();
		if (atendenteSolPai != null && atendenteSolPai.getEmailPessoa() != null)
			addRecipient(atendenteSolPai.getEmailPessoa());
		else {
			DpLotacao lotaAtendenteSolPai = sol.solicitacaoPai.getLotaAtendente();
			if (lotaAtendenteSolPai != null)
				for (DpPessoa pessoaDaLotacao : lotaAtendenteSolPai.getDpPessoaLotadosSet())
					if (pessoaDaLotacao.getDataFim() == null 
							&& pessoaDaLotacao.getEmailPessoa() != null)
						addRecipient(pessoaDaLotacao.getEmailPessoa());
		}
		
		try{
			setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
			send(movimentacao, sol);
		} catch(Exception e){
			Logger.error(e, "Nao foi possivel notificar o atendente");
		}
	}
	
	public static void notificarReplanejamentoMovimentacao(SrMovimentacao movimentacao) {
		SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
		setSubject("Movimentação da solicitação " + sol.getCodigo());
		for (SrGestorItem gestor : sol.itemConfiguracao.gestorSet) {
			DpPessoa pessoaGestorAtual = gestor.getDpPessoa().getPessoaAtual();
			if(pessoaGestorAtual != null && pessoaGestorAtual.getDataFim() == null)
				if(pessoaGestorAtual.getEmailPessoa() != null)
					addRecipient(pessoaGestorAtual.getEmailPessoa());
			
			if(gestor.getDpLotacao() != null)
				for (DpPessoa gestorPessoa : gestor.getDpLotacao().getDpPessoaLotadosSet()) 
					if(gestorPessoa.getPessoaAtual().getDataFim() == null)
						if(gestorPessoa.getPessoaAtual().getEmailPessoa() != null)
							addRecipient(gestorPessoa.getPessoaAtual().getEmailPessoa());
		}
		addRecipient(sol.solicitante.getEmailPessoa());
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		send(movimentacao, sol);
	}
	
	public static void notificarCancelamentoMovimentacao(SrMovimentacao movimentacao) {
		SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
		setSubject("Movimentação da solicitação " + sol.getCodigo());
		addRecipient(sol.solicitante.getEmailPessoa());
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		send(movimentacao, sol);
	}
	
	public static void pesquisaSatisfacao(SrSolicitacao sol) throws Exception{
		setSubject("Pesquisa de Satisfação da solicitação " + sol.getCodigo());
		addRecipient(sol.solicitante.getEmailPessoa());
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		send(sol);
	}
}
