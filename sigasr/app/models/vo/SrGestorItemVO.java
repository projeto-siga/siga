package models.vo;

import models.SrGestorItem;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

/**
 * Classe que representa um V.O. de {@link SrGestorItem}.
 */
public class SrGestorItemVO {
	public Long idGestorItem;
	public DpPessoaVO dpPessoaVO;
	public DpLotacaoVO dpLotacaoVO;
	
	public SrGestorItemVO(Long idGestorItem, DpPessoa dpPessoa, DpLotacao dpLotacao) {
		this.idGestorItem = idGestorItem;
		this.dpPessoaVO = DpPessoaVO.createFrom(dpPessoa);
		this.dpLotacaoVO = DpLotacaoVO.createFrom(dpLotacao);
	}
}