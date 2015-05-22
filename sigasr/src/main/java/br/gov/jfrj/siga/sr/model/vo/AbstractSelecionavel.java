package br.gov.jfrj.siga.sr.model.vo;

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

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getSigla() {
		return sigla;
	}

	@Override
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Override
	public String getDescricao() {
		return descricao;
	}

	@Override
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
