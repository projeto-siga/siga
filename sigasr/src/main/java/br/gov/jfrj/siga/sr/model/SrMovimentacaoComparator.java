package br.gov.jfrj.siga.sr.model;

import java.util.Comparator;

public class SrMovimentacaoComparator implements Comparator<SrMovimentacao> {
	
	private boolean ordemCrescente = false;
	
	public SrMovimentacaoComparator(boolean ordemCrescente){
		this.ordemCrescente = ordemCrescente;
	}
	
	@Override
	public int compare(SrMovimentacao a1, SrMovimentacao a2) {
		return ordemCrescente ? a1.dtIniMov.compareTo(a2.dtIniMov)
				: a2.dtIniMov.compareTo(a1.dtIniMov);
	}
}
