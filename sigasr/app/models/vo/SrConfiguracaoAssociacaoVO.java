package models.vo;

import models.SrAcao;
import models.SrAcao.SrAcaoVO;
import models.SrConfiguracao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Classe que representa um {@link SrConfiguracaoAssociacaoVO VO} da classe
 * {@link SrConfiguracao}.
 * 
 * @author DB1
 */
public class SrConfiguracaoAssociacaoVO {
	public Long idConfiguracao;
	public SrItemConfiguracaoVO itemConfiguracaoUnitario;
	public SrAcao.SrAcaoVO acaoUnitaria;
	public boolean atributoObrigatorio;

	public SrConfiguracaoAssociacaoVO(Long idConfiguracao, 
			SrItemConfiguracaoVO itemConfiguracaoVO, SrAcaoVO acaoVO,
			boolean atributoObrigatorio) {
		this.idConfiguracao = idConfiguracao;
		this.itemConfiguracaoUnitario = itemConfiguracaoVO;
		this.acaoUnitaria = acaoVO;
		this.atributoObrigatorio = atributoObrigatorio;
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