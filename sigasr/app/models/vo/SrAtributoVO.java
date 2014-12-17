package models.vo;

import java.util.ArrayList;
import java.util.List;

import models.SrAtributo;
import models.SrConfiguracao;
import models.SrGestorItem;
import models.SrObjetivoAtributo;
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
	public String codigoAtributo;
	public SrTipoAtributo tipoAtributo;
	public SrObjetivoAtributo objetivoAtributo;
	public String descrPreDefinido;
	public Long hisIdIni;
	public List<SrConfiguracaoAssociacaoVO> associacoesVO;

	public SrAtributoVO(Long idAtributo, String nomeAtributo,
			String descrAtributo, String codigoAtributo,
			SrTipoAtributo tipoAtributo, SrObjetivoAtributo objetivoAtributo,
			String descrPreDefinido, Long hisIdIni,
			List<SrConfiguracao> associacoes) {
		this.idAtributo = idAtributo;
		this.nomeAtributo = nomeAtributo;
		this.descrAtributo = descrAtributo;
		this.codigoAtributo = codigoAtributo;
		this.tipoAtributo = tipoAtributo;
		this.objetivoAtributo = objetivoAtributo;
		this.descrPreDefinido = descrPreDefinido;
		this.hisIdIni = hisIdIni;
		this.associacoesVO = new ArrayList<SrConfiguracaoAssociacaoVO>();

		if (associacoes != null)
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
			jsonTree.add("descrTipoAtributo",
					gson.toJsonTree(this.tipoAtributo.descrTipoAtributo));
		}
		if (this.objetivoAtributo != null) {
			jsonTree.add("descrObjetivoAtributo",
					gson.toJsonTree(this.objetivoAtributo.descrObjetivo));
		}
		return jsonTree.toString();
	}

	public static SrAtributoVO createFrom(SrAtributo atributo) {
		if (atributo != null) {
			List<SrConfiguracao> associacoes = SrConfiguracao
					.listarAssociacoesAtributo(atributo, Boolean.FALSE);
			return new SrAtributoVO(atributo.idAtributo, atributo.nomeAtributo,
					atributo.descrAtributo, atributo.codigoAtributo,
					atributo.tipoAtributo, atributo.objetivoAtributo,
					atributo.descrPreDefinido, atributo.getHisIdIni(),
					associacoes);
		}
		return null;
	}
}