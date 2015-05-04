package br.gov.jfrj.siga.sr.model;

import java.util.Comparator;

public class SrItemConfiguracaoComparator implements
		Comparator<SrItemConfiguracao> {

	@Override
	public int compare(SrItemConfiguracao o1, SrItemConfiguracao o2) {
		if (o1 != null && o2 != null
				&& o1.idItemConfiguracao == o2.idItemConfiguracao)
			return 0;
		return o1.siglaItemConfiguracao.compareTo(o2.siglaItemConfiguracao);
	}

}
