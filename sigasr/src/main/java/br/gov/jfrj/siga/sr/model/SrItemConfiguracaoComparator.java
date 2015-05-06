package br.gov.jfrj.siga.sr.model;

import java.util.Comparator;

public class SrItemConfiguracaoComparator implements
		Comparator<SrItemConfiguracao> {

	@Override
	public int compare(SrItemConfiguracao o1, SrItemConfiguracao o2) {
		if (o1 != null && o2 != null
				&& o1.getId() == o2.getId())
			return 0;
		return o1.getSigla().compareTo(o2.getSigla());
	}

}
