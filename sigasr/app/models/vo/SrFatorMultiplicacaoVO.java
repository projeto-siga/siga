package models.vo;

import models.SrFatorMultiplicacao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

/**
 * Classe que representa um V.O. de {@link SrFatorMultiplicacao}.
 */
public class SrFatorMultiplicacaoVO {
	public Long idFatorMultiplicacao;
	public int numFatorMultiplicacao;
	public DpPessoaVO dpPessoaVO;
	public DpLotacaoVO dpLotacaoVO;
	
	public SrFatorMultiplicacaoVO(Long idFatorMultiplicacao, int numFatorMultiplicacao, 
			DpPessoa dpPessoa, DpLotacao dpLotacao) {
		this.idFatorMultiplicacao = idFatorMultiplicacao;
		this.numFatorMultiplicacao = numFatorMultiplicacao;
		this.dpPessoaVO = DpPessoaVO.createFrom(dpPessoa);
		this.dpLotacaoVO = DpLotacaoVO.createFrom(dpLotacao);
	}
}