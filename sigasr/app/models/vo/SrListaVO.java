package models.vo;

import models.SrLista;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Classe que representa um V.O. de {@link SrLista}.
 */
public class SrListaVO {

	public Long idLista;
	public String nomeLista;
	public String descrAbrangencia;
	public String descrJustificativa;
	public String descrPriorizacao;

	public SrListaVO(Long idLista, String nomeLista, String descrAbrangencia, String descrJustificativa,
			String descrPriorizacao) {
		this.idLista = idLista;
		this.nomeLista = nomeLista;
		this.descrAbrangencia = descrAbrangencia;
		this.descrJustificativa = descrJustificativa;
		this.descrPriorizacao = descrPriorizacao;
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