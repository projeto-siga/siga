package br.gov.jfrj.siga.sr.model;

import java.util.Comparator;

public class SrEtapaSolicitacaoComparator implements Comparator<SrEtapaSolicitacao>{

	@Override
	public int compare(SrEtapaSolicitacao o1, SrEtapaSolicitacao o2) {
		if (o1 != null && o2 != null) {
			if (o1.getDecorridoEmSegundos().equals(o2.getDecorridoEmSegundos())) {
				return o1.getInicio().compareTo(o2.getInicio());
			} 
			return o1.getDecorridoEmSegundos().compareTo(o2.getDecorridoEmSegundos());
		}
		return 0;
	}
}
