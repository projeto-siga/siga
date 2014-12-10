package br.gov.jfrj.siga.gc;

import java.util.List;

public interface ObjetoSelecionavel {
	public Long getId();

	public void setDescricao(String descricao);

	public ObjetoSelecionavel selecionar(String sigla) throws Exception;

	public List<? extends ObjetoSelecionavel> buscar() throws Exception;
}
