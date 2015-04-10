package models;

import java.util.Comparator;

public class SrPrioridadeSolicitacaoComparator implements
		Comparator<SrPrioridadeSolicitacao> {

	private boolean ordemCrescente = false;

	public SrPrioridadeSolicitacaoComparator(boolean ordemCrescente) {
		this.ordemCrescente = ordemCrescente;
	}

	@Override
	public int compare(SrPrioridadeSolicitacao a1, SrPrioridadeSolicitacao a2) {
		if (a1.numPosicao == null && a2.numPosicao == null)
			return 0;
		else if (a1.numPosicao == null && a2.numPosicao != null)
			return 1;
		else if (a1.numPosicao != null && a2.numPosicao == null)
			return -1;
		return ordemCrescente ? a1.numPosicao.compareTo(a2.numPosicao) : a2.numPosicao.compareTo(a1.numPosicao);
	}
}