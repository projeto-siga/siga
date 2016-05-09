package br.gov.jfrj.siga.ex.bl;

import java.util.Comparator;

public class ExAssinavelComparador implements Comparator<ExAssinavelDoc> {

	@Override
	public int compare(ExAssinavelDoc doc1, ExAssinavelDoc doc2) {
		return doc1.getDoc().getSigla().compareTo(doc2.getDoc().getSigla());
	}

}
