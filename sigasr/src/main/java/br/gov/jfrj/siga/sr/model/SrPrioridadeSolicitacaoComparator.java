package br.gov.jfrj.siga.sr.model;

import java.util.Comparator;

public class SrPrioridadeSolicitacaoComparator implements Comparator<SrPrioridadeSolicitacao> {

    private boolean ordemCrescente = false;

    public SrPrioridadeSolicitacaoComparator(boolean ordemCrescente) {
        this.ordemCrescente = ordemCrescente;
    }

    @Override
    public int compare(SrPrioridadeSolicitacao a1, SrPrioridadeSolicitacao a2) {
        if (a1.getNumPosicao() == null && a2.getNumPosicao() == null)
            return 0;
        else if (a1.getNumPosicao() == null && a2.getNumPosicao() != null)
            return 1;
        else if (a1.getNumPosicao() != null && a2.getNumPosicao() == null)
            return -1;
        return ordemCrescente ? a1.getNumPosicao().compareTo(a2.getNumPosicao()) : a2.getNumPosicao().compareTo(a1.getNumPosicao());
    }
}