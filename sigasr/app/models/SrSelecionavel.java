package models;

import java.util.List;

public interface SrSelecionavel {
	public Integer getId();
	
	public void setId(Integer id);

	public String getSigla();

	public void setSigla(String sigla);

	public String getDescricao();
	
	public void setDescricao(String descricao);

	public SrSelecionavel selecionar(String sigla);

	public List<SrSelecionavel> buscar();
}
