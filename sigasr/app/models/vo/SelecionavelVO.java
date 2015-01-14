package models.vo;

import br.gov.jfrj.siga.model.Selecionavel;

public class SelecionavelVO extends AbstractSelecionavel {

	public SelecionavelVO(Long id, String descricao) {
		super(id, descricao);
	}
	
	public SelecionavelVO(Long id, String sigla, String descricao) {
		super(id, sigla, descricao);
	}
	
	public static SelecionavelVO createFrom(Selecionavel sel) {
		if (sel != null)
			return new SelecionavelVO(sel.getId(), sel.getSigla(), sel.getDescricao());
		else
			return null;
	}
}
