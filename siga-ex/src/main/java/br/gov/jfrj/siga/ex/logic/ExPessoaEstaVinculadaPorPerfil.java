package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

public class ExPessoaEstaVinculadaPorPerfil implements Expression {

	private ExDocumento doc;
	private DpPessoa titular;

	public ExPessoaEstaVinculadaPorPerfil(ExDocumento doc, DpPessoa titular) {
		this.doc = doc;
		this.titular = titular;
	}

	@Override
	public boolean eval() {
		for (ExMovimentacao mov : doc.getMobilGeral().getExMovimentacaoSet()) {
			if (!mov.isCancelada() && mov.getExTipoMovimentacao().getIdTpMov()
					.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL)) {
				if (mov.getSubscritor() != null)
					if (mov.getSubscritor().equivale(titular))
						return true;
			}
		}
		return false;
	}

	@Override
	public String explain(boolean result) {
		return "pessoa " + titular.getSiglaCompleta() + (result ? "" : JLogic.NOT)
				+ " está vinculada por perfil ao documento " + doc.getCodigo();
	}
};
