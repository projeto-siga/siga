package br.gov.jfrj.siga.ex.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExMovimentacaoRecebimentoComparator implements Comparator<ExMovimentacao> {
	static public ExMovimentacaoRecebimentoComparator INSTANCE = new ExMovimentacaoRecebimentoComparator();

	public int compare(ExMovimentacao o1, ExMovimentacao o2) {
		try {
			int i = 0;

			// Pelo tipo da movimentação
			if (o1.getExTipoMovimentacao() != null && o2.getExTipoMovimentacao() != null) {
				i = tpMovDesempatePosicao(o1.getExTipoMovimentacao(), o2.getExTipoMovimentacao());
				if (i != 0)
					return i;
			}

			// Pela data
			if (o2.getDtTimestamp() != null) {
				i = o2.getDtTimestamp().compareTo(o1.getDtTimestamp());
			} else if (o2.getDtIniMov() != null) {
				i = o2.getDtIniMov().compareTo(o1.getDtIniMov());
			}
			if (i != 0)
				return i;

			// Pela ID
			i = o2.getIdMov().compareTo(o1.getIdMov());
			return i;
		} catch (final Exception ex) {
			return 0;
		}
	}

	public static Integer tpMovDesempatePosicao(ITipoDeMovimentacao idTpMov, ITipoDeMovimentacao idTpMov2) {
		final List<ITipoDeMovimentacao> tpMovDesempate = Arrays
				.asList(new ITipoDeMovimentacao[] { ExTipoDeMovimentacao.RECEBIMENTO,
						ExTipoDeMovimentacao.TRANSFERENCIA, ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA,
						ExTipoDeMovimentacao.TRAMITE_PARALELO, ExTipoDeMovimentacao.NOTIFICACAO });

		// Trata o caso de alguma parâmetro ser null
		if (idTpMov == null && idTpMov2 == null)
			return 0;
		if (idTpMov == null && idTpMov2 != null)
			return Integer.MAX_VALUE;
		if (idTpMov != null && idTpMov2 == null)
			return Integer.MIN_VALUE;

		int i = tpMovDesempate.indexOf(idTpMov);
		int i2 = tpMovDesempate.indexOf(idTpMov2);

		return i - i2;
	}

}
