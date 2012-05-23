package models;

import java.util.List;

public interface SrSelecionavel {
	public Integer getId();

	public String getSigla();

	public void setSigla(String sigla);

	public String getDescricao();

	public SrSelecionavel selecionar(String sigla);

	public List<SrSelecionavel> buscar();
}
