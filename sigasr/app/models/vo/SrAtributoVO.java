package models.vo;

import java.util.ArrayList;
import java.util.List;

import models.SrAtributo;
import models.SrConfiguracao;
import models.SrGestorItem;
import models.SrTipoAtributo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Classe que representa um V.O. de {@link SrGestorItem}.
 */
public class SrAtributoVO {
	public Long idAtributo;
	public String nomeAtributo;
	public String descrAtributo;
	public SrTipoAtributo tipoAtributo;
	public Long hisIdIni;
	public List<SrConfiguracaoVO> associacoesVO;
	
	public SrAtributoVO(Long idAtributo, String nomeAtributo,
			String descrAtributo, SrTipoAtributo tipoAtributo, Long hisIdIni,
			List<SrConfiguracao> associacoes) {
		this.idAtributo = idAtributo;
		this.nomeAtributo = nomeAtributo;
		this.descrAtributo = descrAtributo;
		this.tipoAtributo = tipoAtributo;
		this.hisIdIni = hisIdIni;
		this.associacoesVO = new ArrayList<SrConfiguracaoVO>();
		
		if(associacoes != null)
			for (SrConfiguracao associacao : associacoes) {
				associacoesVO.add(associacao.toVO());
			}
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
	
	public static SrAtributoVO createFrom(SrAtributo atributo) {
		if (atributo != null)
			return new SrAtributoVO(atributo.idAtributo, atributo.nomeAtributo, atributo.descrAtributo, atributo.tipoAtributo, atributo.getHisIdIni(), atributo.associacoes);

		return null;
	}
	
}