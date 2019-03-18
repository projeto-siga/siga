package br.gov.jfrj.siga.sr.model;

import java.util.Comparator;

public class SrParametroAcordoComparator implements Comparator<SrParametroAcordo> {
	
	@Override
	public int compare(SrParametroAcordo a1, SrParametroAcordo a2) {
		
		int compare = 0;
		
		compare = a1.getParametro().getId().compareTo(a2.getParametro().getId());
		if (compare != 0)
			return compare;
		
		long segundosA1 = a1.getValorEmSegundos();
		long segundosA2 = a2.getValorEmSegundos();
		
		if (segundosA1 == segundosA2)
			return 0;
		else if (segundosA1 < segundosA2)
			return -1;
		else return 1;
		
		//Edson: melhorar a comparação fazendo o seguinte:
		//- Considerar o sinal. Por exemplo, < é mais restritivo que <=. 
		//- Levar em conta que o o parâmetro pode estar estabelecendo um valor máximo em vez de mínimo.
		//- Suportar os casos em que o parãmetro não é temporal
	}


	
}
