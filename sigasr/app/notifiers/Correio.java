package notifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import models.SrGestorItem;
import models.SrItemConfiguracao;
import models.SrMovimentacao;
import models.SrSolicitacao;
import models.SrTipoMovimentacao;
import play.mvc.Mailer;

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
		setFrom("Administrador do Siga<sigadocs@jfrj.jus.br>");
		send(movimentacao, sol);
	}
	
	public static void notificarReplanejamentoMovimentacao(SrMovimentacao movimentacao) {
		SrSolicitacao sol = movimentacao.solicitacao.getSolicitacaoAtual();
		setSubject("Movimentação da solicitação " + sol.getCodigo());
		for (SrGestorItem gestor : sol.itemConfiguracao.gestorSet) {
			if(gestor.getDpPessoa() != null && gestor.getDpPessoa().getDataFim() == null)
				if(gestor.getDpPessoa().getEmailPessoa() != null)
					addRecipient(gestor.getDpPessoa().getEmailPessoa());
			
			if(gestor.getDpLotacao() != null)
				for (DpPessoa gestorPessoa : gestor.getDpLotacao().getDpPessoaLotadosSet()) 
					if(gestorPessoa.getDataFim() == null)
						if(gestorPessoa.getEmailPessoa() != null)
							addRecipient(gestorPessoa.getEmailPessoa());
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
