package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

public class ExLotacaoEstaVinculadoPorPerfil implements Expression {

	private ExDocumento doc;
	private DpLotacao lotaTitular;

	public ExLotacaoEstaVinculadoPorPerfil(ExDocumento doc, DpLotacao lotaTitular) {
		this.doc = doc;
		this.lotaTitular = lotaTitular;
	}

	@Override
	public boolean eval() {
		for (ExMovimentacao mov : doc.getMobilGeral().getExMovimentacaoSet()) {
			if (!mov.isCancelada()
					&& mov.getExTipoMovimentacao().getIdTpMov()
							.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL)
					&& mov.getLotaSubscritor().equivale(lotaTitular))
				return true;
		}
		return false;
	}

	@Override
	public String explain(boolean result) {
		return "lotação " + lotaTitular.getSiglaCompleta() + (result ? "" : JLogic.NOT)
				+ " está vinculada por perfil ao documento " + doc.getCodigo();
	}
};
