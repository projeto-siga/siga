package br.gov.jfrj.siga.sr.model.vo;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sr.model.SrFatorMultiplicacao;

/**
 * Classe que representa um V.O. de {@link SrFatorMultiplicacao}.
 */
public class SrFatorMultiplicacaoVO {
	public Long idFatorMultiplicacao;
	public int numFatorMultiplicacao;
	public SelecionavelVO dpPessoaVO;
	public SelecionavelVO dpLotacaoVO;
	
	public SrFatorMultiplicacaoVO(Long idFatorMultiplicacao, int numFatorMultiplicacao, 
			DpPessoa dpPessoa, DpLotacao dpLotacao) {
		this.idFatorMultiplicacao = idFatorMultiplicacao;
		this.numFatorMultiplicacao = numFatorMultiplicacao;
		this.dpPessoaVO = SelecionavelVO.createFrom(dpPessoa);
		this.dpLotacaoVO = SelecionavelVO.createFrom(dpLotacao);
	}
}