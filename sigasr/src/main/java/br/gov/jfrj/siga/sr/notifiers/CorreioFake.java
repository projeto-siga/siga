package br.gov.jfrj.siga.sr.notifiers;

import br.com.caelum.vraptor.freemarker.Freemarker;
import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;

//Edson: melhorar as classes Correio e CorreioFake
public class CorreioFake extends Correio{
	
	public CorreioFake(Freemarker freemarker, PathBuilder urlLogic) {
		super(freemarker, urlLogic);
	}

	public void notificarAtendente(SrMovimentacao movimentacao, SrSolicitacao sol) {
		
	}

	public void notificarReplanejamentoMovimentacao(SrMovimentacao movimentacao) {
		
	}

	public void notificarCancelamentoMovimentacao(SrMovimentacao movimentacao) {
		
	}

	public void notificarAbertura(SrSolicitacao solicitacao) {
		
	}

	public void notificarMovimentacao(SrMovimentacao movimentacao) {
		
	}
}
