package models.vo;

import models.SrLista;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Classe que representa um V.O. de {@link SrLista}.
 */
public class SrListaVO {

	public Long idLista;
	public Long hisIdIni;
	public String nomeLista;
	public String nomeLotacao;
	public String descrAbrangencia;
	public String descrJustificativa;
	public String descrPriorizacao;
	public boolean ativo;

	public SrListaVO(SrLista lista) {
		this.idLista = lista.idLista;
		this.hisIdIni = lista.getHisIdIni();
		this.nomeLista = lista.nomeLista;
		this.nomeLotacao = lista.lotaCadastrante != null ? lista.lotaCadastrante.getDescricao() : "";
		this.descrAbrangencia = lista.descrAbrangencia;
		this.descrJustificativa = lista.descrJustificativa;
		this.descrPriorizacao = lista.descrPriorizacao;
		this.ativo = lista.isAtivo();
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