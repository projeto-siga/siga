package models.vo;

import models.SrPesquisa;

public class SrPesquisaVO extends AbstractSelecionavel {
	
	public Long hisIdIni;
	public boolean ativo;

	public SrPesquisaVO(Long id, String descricao) {
		super(id, descricao);
	}
	
	public SrPesquisaVO(Long id, String sigla, String descricao) {
		super(id, sigla, descricao);
	}
	
	public SrPesquisaVO(SrPesquisa pesquisa) {
		super(pesquisa.idPesquisa, pesquisa.descrPesquisa);
		this.hisIdIni = pesquisa.getHisIdIni();
		this.ativo = pesquisa.isAtivo();
	}
	
	public static SrPesquisaVO createFrom(SrPesquisa pesquisa) {
		if (pesquisa != null) {
			return new SrPesquisaVO(pesquisa);
		}
		else
			return null;
	}
}
