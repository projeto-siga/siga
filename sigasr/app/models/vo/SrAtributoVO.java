package models.vo;

import java.util.ArrayList;
import java.util.List;

import models.SrAtributo;
import models.SrConfiguracao;
import models.SrGestorItem;
import models.SrTipoAtributo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Classe que representa um V.O. de {@link SrGestorItem}.
 */
public class SrAtributoVO {
	public Long idAtributo;
	public String nomeAtributo;
	public String descrAtributo;
	public SrTipoAtributo tipoAtributo;
	public String descrPreDefinido;
	public Long hisIdIni;
	public List<SrConfiguracaoAssociacaoVO> associacoesVO;
	
	public SrAtributoVO(Long idAtributo, String nomeAtributo,
			String descrAtributo, SrTipoAtributo tipoAtributo, String descrPreDefinido,
			Long hisIdIni, List<SrConfiguracao> associacoes) {
		this.idAtributo = idAtributo;
		this.nomeAtributo = nomeAtributo;
		this.descrAtributo = descrAtributo;
		this.tipoAtributo = tipoAtributo;
		this.descrPreDefinido = descrPreDefinido;
		this.hisIdIni = hisIdIni;
		this.associacoesVO = new ArrayList<SrConfiguracaoAssociacaoVO>();
		
		if(associacoes != null)
			for (SrConfiguracao associacao : associacoes) {
				associacoesVO.add(associacao.toAssociacaoVO());
			}
	}

	/**
	 * Converte o objeto para Json.
	 */
	public String toJson() {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		Gson gson = builder.create();
		
		JsonObject jsonTree = (JsonObject) gson.toJsonTree(this);
		
		if (this.tipoAtributo != null) {
			jsonTree.add("descrTipoAtributo", gson.toJsonTree(this.tipoAtributo.descrTipoAtributo));
		}
		return jsonTree.toString();
	}
	
	public static SrAtributoVO createFrom(SrAtributo atributo) {
		if (atributo != null) {
			List<SrConfiguracao> associacoes = SrConfiguracao.listarAssociacoesAtributo(atributo, Boolean.FALSE);
			return new SrAtributoVO(atributo.idAtributo, atributo.nomeAtributo, atributo.descrAtributo, atributo.tipoAtributo, atributo.descrPreDefinido, atributo.getHisIdIni(), associacoes);
		}
		return null;
	}
}