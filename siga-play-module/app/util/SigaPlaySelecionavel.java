package util;

import java.util.List;

public interface SigaPlaySelecionavel {
	public Long getId();

	public void setId(Long id);

	public String getSigla();

	public void setSigla(String sigla);

	public String getDescricao();

	public void setDescricao(String descricao);

	public SigaPlaySelecionavel selecionar(String sigla) throws Exception;

	public List<? extends SigaPlaySelecionavel> buscar() throws Exception;
}
