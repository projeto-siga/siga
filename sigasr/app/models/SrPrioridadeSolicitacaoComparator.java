package models;

import java.util.Comparator;

public class SrPrioridadeSolicitacaoComparator implements Comparator<SrPrioridadeSolicitacao> {
	
	private boolean ordemCrescente = false;
	
	public SrPrioridadeSolicitacaoComparator(boolean ordemCrescente){
		this.ordemCrescente = ordemCrescente;
	}
	
	@Override
	public int compare(SrPrioridadeSolicitacao a1, SrPrioridadeSolicitacao a2) {
		return ordemCrescente ? a1.numPosicao.compareTo(a2.numPosicao)
				: a2.numPosicao.compareTo(a1.numPosicao);
	}
}
