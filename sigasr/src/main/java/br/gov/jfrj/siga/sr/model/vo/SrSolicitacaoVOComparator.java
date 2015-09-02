package br.gov.jfrj.siga.sr.model.vo;

import java.util.Comparator;

public class SrSolicitacaoVOComparator implements Comparator<SrSolicitacaoVO> {

    @Override
    public int compare(SrSolicitacaoVO s1, SrSolicitacaoVO s2) {
        //if (s1.getPrioridadeSolicitacaoVO() != null && s2.getPrioridadeSolicitacaoVO() != null)
        //    return Long.valueOf(s1.getPrioridadeSolicitacaoVO().getNumPosicao()).compareTo(Long.valueOf(s2.getPrioridadeSolicitacaoVO().getNumPosicao()));
        //else
        //    return -1;
    	return 0;
    }

}
