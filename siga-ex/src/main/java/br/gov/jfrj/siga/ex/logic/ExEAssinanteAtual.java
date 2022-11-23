package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExEAssinanteAtual implements Expression {
	ExDocumento doc;
	DpPessoa subscritor;

	public ExEAssinanteAtual(ExDocumento doc, DpPessoa subscritor) {
		this.doc = doc;
		this.subscritor = subscritor;
	}

	@Override
	public boolean eval() {
		return doc != null && !doc.getMobilGeral().getMovimentacoesPorTipo(ExTipoDeMovimentacao.ORDEM_ASSINATURA, Boolean.TRUE).isEmpty() && ehAssinanteAtual();
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é o assinante atual", result);
	}
	
	public Boolean ehAssinanteAtual() {
		Boolean retorno = false;
		ExMovimentacao mov = doc.getMobilGeral().getUltimaMovimentacaoNaoCancelada(ExTipoDeMovimentacao.ORDEM_ASSINATURA);
		
		String[] siglas = mov.getDescrMov().split(";");
		
		if(doc.getAssinaturasEAutenticacoesComTokenOuSenhaERegistros().isEmpty()) {
			//primeiro a assinar
			if(siglas[0].equals(subscritor.getSigla())) {
				retorno = true;
			}
		} else {
			//anterior a ele assinou
			if(!doc.isAssinadoPelaPessoaComTokenOuSenha(subscritor)) {
				String siglaAnterior = null;
				for (int i = 0; i < siglas.length; i++) {
					if(siglas[i].equals(subscritor.getSigla())) {
						siglaAnterior = siglas[i-1];
						break;
					}
				}
				
				if(siglaAnterior != null) {
					DpPessoa pessoaAnt = new CpDao().getPessoaFromSigla(siglaAnterior);
					if(doc.isAssinadoPelaPessoaComTokenOuSenha(pessoaAnt)) {
						retorno = true;
					}
				}
			}
		}
		return retorno;
	}
}