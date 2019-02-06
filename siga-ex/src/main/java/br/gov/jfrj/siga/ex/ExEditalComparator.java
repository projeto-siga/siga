package br.gov.jfrj.siga.ex;

import java.util.Comparator;

public class ExEditalComparator implements Comparator<ExItemDestinacao> {
	
	@Override
	public int compare(ExItemDestinacao c1, ExItemDestinacao c2) {
		return c1.getMob().getDoc().getDtDoc()
				.compareTo(c2.getMob().getDoc().getDtDoc())
				* -1;
	}
}