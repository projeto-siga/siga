package br.gov.jfrj.siga.gc;

import java.util.List;

import br.gov.jfrj.siga.model.Selecionavel;

public interface ObjetoSelecionavel extends Selecionavel {
	public Long getId();

	public void setDescricao(String descricao);

	public ObjetoSelecionavel selecionar(String sigla) throws Exception;

	public List<? extends ObjetoSelecionavel> buscar() throws Exception;
}
