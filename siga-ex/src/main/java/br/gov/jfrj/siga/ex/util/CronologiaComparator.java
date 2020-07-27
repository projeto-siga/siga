package br.gov.jfrj.siga.ex.util;

import java.util.Comparator;

import br.gov.jfrj.siga.ex.ExMovimentacao;

public class CronologiaComparator implements Comparator<ExMovimentacao> {

	public int compare(ExMovimentacao o1, ExMovimentacao o2) {
		try {
			int i = o2.getDtTimestamp().compareTo(o1.getDtTimestamp());
			return i;
		} catch (final Exception ex) {
			return 0;
		}
	}
}

