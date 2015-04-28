package models.vo;

import java.util.Comparator;

public class SrSolicitacaoVOComparator implements Comparator<SrSolicitacaoVO> {

	@Override
	public int compare(SrSolicitacaoVO s1, SrSolicitacaoVO s2) {
		if (s1.prioridadeSolicitacaoVO != null && s2.prioridadeSolicitacaoVO != null)
			return Long.valueOf(s1.prioridadeSolicitacaoVO.numPosicao).compareTo(
					Long.valueOf(s2.prioridadeSolicitacaoVO.numPosicao));
		else
			return -1;
	}

}
