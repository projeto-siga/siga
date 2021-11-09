package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

public class ExMovMobRefRecebeuMovimentacoesPosteriores implements Expression {
	ExMovimentacao mov;

	public ExMovMobRefRecebeuMovimentacoesPosteriores(ExMovimentacao mov) {
		this.mov = mov;
	}

	// Verifica se o mobil de referência já recebeu outras movimentações depois da
	// movimentação que vai ser cancelada.
	@Override
	public boolean eval() {
		if (mov == null || mov.getExMobilRef() == null || !mov.getExMobilRef().doc().isNumeracaoUnicaAutomatica()
				|| !mov.mob().doc().isEletronico())
			return false;

		for (ExMovimentacao movDoMobilRef : mov.getExMobilRef().getCronologiaSet()) {
			if (movDoMobilRef.getIdMov().equals(mov.getIdMov()))
				break;

			if (!movDoMobilRef.isCancelada()
					&& movDoMobilRef.getExTipoMovimentacao().getId() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_REFERENCIA
					&& movDoMobilRef.getExTipoMovimentacao().getId() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANOTACAO
					&& movDoMobilRef.getDtIniMov().after(mov.getDtIniMov()))
				return true;
		}
		return false;
	}

	@Override
	public String explain(boolean result) {
		return "móbil de referência " + JLogic.explain("possui movimentações posteriores", result);
	}

}
