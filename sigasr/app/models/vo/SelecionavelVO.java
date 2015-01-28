package models.vo;

import br.gov.jfrj.siga.model.Selecionavel;

public class SelecionavelVO extends AbstractSelecionavel {
	
	public int tipo;

	public SelecionavelVO(Long id, String descricao) {
		super(id, descricao);
	}
	
	public SelecionavelVO(Long id, String sigla, String descricao) {
		super(id, sigla, descricao);
	}
	
	public SelecionavelVO(Long id, String sigla, String descricao, int tipo) {
		super(id, sigla, descricao);
		this.tipo = tipo;
	}
	
	public static SelecionavelVO createFrom(Selecionavel sel, int tipo) {
		if (sel != null)
			return new SelecionavelVO(sel.getId(), sel.getSigla(), sel.getDescricao(), tipo);
		else
			return null;
	}
	
	public static SelecionavelVO createFrom(Selecionavel sel) {
		return SelecionavelVO.createFrom(sel, 0);
	}
	
	public static SelecionavelVO createFrom(Long id, String descricao) {
		return new SelecionavelVO(id, descricao);
	}
	
	public static SelecionavelVO createFrom(Long id, String sigla, String descricao) {
		return new SelecionavelVO(id, descricao, sigla);
	}

}
