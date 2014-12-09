package models.vo;

import java.util.ArrayList;
import java.util.List;

import models.SrConfiguracao;
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
	public SrListaVO listaInicialVO;
	public List<SrConfiguracaoVO> permissoesVO;

	public SrListaVO(Long idLista, String nomeLista, String descrAbrangencia, String descrJustificativa,
			String descrPriorizacao, SrLista listaInicial, List<SrConfiguracao> permissoes) {
		this.idLista = idLista;
		this.nomeLista = nomeLista;
		this.descrAbrangencia = descrAbrangencia;
		this.descrJustificativa = descrJustificativa;
		this.descrPriorizacao = descrPriorizacao;
		this.listaInicialVO = listaInicial.toVO();
		permissoesVO = new ArrayList<SrConfiguracaoVO>();
		
		if(permissoes != null)
			for (SrConfiguracao conf: permissoes) {
				permissoesVO.add(conf.toVO());
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
}