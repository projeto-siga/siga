package br.gov.jfrj.siga.ex.util;

import java.util.Comparator;

import br.gov.jfrj.siga.ex.ExMovimentacao;

public class CronologiaComparator implements Comparator<ExMovimentacao> {

	public int compare(ExMovimentacao o1, ExMovimentacao o2) {
		try {
			int i = o2.getDtTimestamp().compareTo(o1.getDtTimestamp());
			if (i != 0)
				return i;
			
			if (o1.getExTipoMovimentacao() != null && o2.getExTipoMovimentacao() != null) {
				i = ExMovimentacao.tpMovDesempatePosicao(o1.getExTipoMovimentacao().getId()).compareTo(
						ExMovimentacao.tpMovDesempatePosicao(o2.getExTipoMovimentacao().getId()));
				if (i != 0)
					return i;
			}
			
			i = o1.getIdMov().compareTo(o2.getIdMov());
			return i;
		} catch (final Exception ex) {
			return 0;
		}
	}
}

