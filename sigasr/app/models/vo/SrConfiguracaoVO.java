package models.vo;

import java.util.ArrayList;
import java.util.List;

import models.SrAcao;
import models.SrConfiguracao;
import models.SrItemConfiguracao;
import models.SrLista;
import models.SrTipoPermissaoLista;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Classe que representa um {@link SrConfiguracaoVO VO} da classe
 * {@link SrConfiguracao}.
 * 
 * @author DB1
 */
public class SrConfiguracaoVO {
	public List<SrListaVO> listaVO; 
	public List<SrItemConfiguracaoVO> listaItemConfiguracaoVO;
	public List<SrAcao.SrAcaoVO> listaAcaoVO;
	public List<SrTipoPermissaoLista.SrTipoPermissaoListaVO> listaTipoPermissaoListaVO;

	public SrConfiguracaoVO(List<SrLista> listaConfiguracaoSet, List<SrItemConfiguracao> itemConfiguracaoSet, List<SrAcao> acoesSet, List<SrTipoPermissaoLista> tipoPermissaoSet) {
		listaVO = new ArrayList<SrListaVO>();
		listaItemConfiguracaoVO = new ArrayList<SrItemConfiguracaoVO>();
		listaAcaoVO = new ArrayList<SrAcao.SrAcaoVO>();
		listaTipoPermissaoListaVO = new ArrayList<SrTipoPermissaoLista.SrTipoPermissaoListaVO>();
		
		if(listaConfiguracaoSet != null)
			for (SrLista item : listaConfiguracaoSet) {
				listaVO.add(item.toVO());
			}
		
		if(itemConfiguracaoSet != null)
			for (SrItemConfiguracao item : itemConfiguracaoSet) {
				listaItemConfiguracaoVO.add(item.toVO());
			}
		
		if(acoesSet != null)
			for (SrAcao item : acoesSet) {
				listaAcaoVO.add(item.toVO());
			}

		if(tipoPermissaoSet != null)
			for (SrTipoPermissaoLista item : tipoPermissaoSet) {
				listaTipoPermissaoListaVO.add(item.toVO());
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