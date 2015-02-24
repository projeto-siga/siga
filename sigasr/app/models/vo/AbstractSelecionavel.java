package models.vo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AbstractSelecionavel implements ISelecionavel {
	private Long id;
	private String sigla;
	private String descricao;
	
	public AbstractSelecionavel(Long id, String sigla, String descricao) {
		this.setId(id);
		this.setSigla(sigla);
		this.setDescricao(descricao);
	}
	
	public AbstractSelecionavel(Long id, String descricao) {
		this.setId(id);
		this.setDescricao(descricao);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	/**
	 * Converte o objeto para Json.
	 */
	public String toJson() {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		Gson gson = builder.create();

		return gson.toJson(this);
	}
	
}
