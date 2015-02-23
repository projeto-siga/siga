package models.vo;

import models.SrPesquisa;

public class SrPesquisaVO extends AbstractSelecionavel {

	public SrPesquisaVO(Long id, String descricao) {
		super(id, descricao);
	}
	
	public SrPesquisaVO(Long id, String sigla, String descricao) {
		super(id, sigla, descricao);
	}
	
	public static SrPesquisaVO createFrom(SrPesquisa pesquisa) {
		if (pesquisa != null)
			return new SrPesquisaVO(pesquisa.getId(), pesquisa.descrPesquisa);
		else
			return null;
	}
}
