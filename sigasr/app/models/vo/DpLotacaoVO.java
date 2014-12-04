package models.vo;

import br.gov.jfrj.siga.dp.DpLotacao;

public class DpLotacaoVO extends AbstractSelecionavel {
	
	public DpLotacaoVO(Long id, String sigla, String descricao) {
		super(id, sigla, descricao);
	}

	public static DpLotacaoVO createFrom(DpLotacao lota) {
		if (lota != null)
			return new DpLotacaoVO(lota.getId(), lota.getSigla(), lota.getDescricao());
		else
			return null;
	}
}
