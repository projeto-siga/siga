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
	public boolean ativo;
	public Long idAtributo;
	public String nomeAtributo;
	public String descrAtributo;
	public String codigoAtributo;
	public SrTipoAtributo tipoAtributo;
	public SrObjetivoAtributoVO objetivoAtributo;
	public String descrPreDefinido;
	public Long hisIdIni;
	public List<SrConfiguracaoVO> associacoesVO;
	
	public SrAtributoVO(SrAtributo atributo, List<SrConfiguracao> associacoes) {
		this.ativo = atributo.isAtivo();
		this.idAtributo = atributo.idAtributo;
		this.nomeAtributo = atributo.nomeAtributo;
		this.descrAtributo = atributo.descrAtributo;
		this.codigoAtributo = atributo.codigoAtributo;
		this.tipoAtributo = atributo.tipoAtributo;
		this.objetivoAtributo = SrObjetivoAtributoVO.createFrom(atributo.objetivoAtributo);
		this.descrPreDefinido = atributo.descrPreDefinido;
		this.hisIdIni = atributo.getHisIdIni();
		this.associacoesVO = new ArrayList<SrConfiguracaoVO>();

		if (associacoes != null)
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

		JsonObject jsonTree = (JsonObject) gson.toJsonTree(this);

		if (this.tipoAtributo != null) {
			jsonTree.add("descrTipoAtributo",
					gson.toJsonTree(this.tipoAtributo.descrTipoAtributo));
		}
		
		if (this.objetivoAtributo != null) {
			jsonTree.add("descrObjetivoAtributo",
					gson.toJsonTree(this.objetivoAtributo.getDescricao()));
		}
		
		return jsonTree.toString();
	}

	public static SrAtributoVO createFrom(SrAtributo atributo, boolean listarAssociacoes) {
		if (atributo != null) {
			List<SrConfiguracao> associacoes = new ArrayList<SrConfiguracao>();
			
			if (listarAssociacoes)
				associacoes = SrConfiguracao
						.listarAssociacoesAtributo(atributo, Boolean.FALSE);
			
			return new SrAtributoVO(atributo, associacoes);
		}
		
		return null;
	}
}