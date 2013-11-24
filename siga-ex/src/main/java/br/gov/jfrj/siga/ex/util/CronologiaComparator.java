package br.gov.jfrj.siga.ex.util;

import java.util.Comparator;

import br.gov.jfrj.siga.ex.ExMovimentacao;

public class CronologiaComparator implements Comparator<ExMovimentacao> {

	public int compare(ExMovimentacao o1, ExMovimentacao o2) {
		try {
			int i = o2.getDtIniMov().compareTo(o1.getDtIniMov());
			if (i != 0)
				return i;
			i = o2.getIdMov().compareTo(o1.getIdMov());
			return i;
		} catch (final Exception ex) {
			return 0;
		}
	}
}

