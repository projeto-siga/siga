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
	public boolean atributoObrigatorio;
	public String descrConfiguracao;
	public DpLotacaoVO atendente;
	public DpLotacaoVO posAtendente;
//	public DpLotacaoVO equipeQualidade;
	public DpLotacaoVO preAtendente;
	public CpOrgaoUsuarioVO orgaoUsuario;
	public CpComplexoVO local;
	
	

	public SrConfiguracaoVO(SrConfiguracao configuracao) {
		listaVO = new ArrayList<SrListaVO>();
		listaItemConfiguracaoVO = new ArrayList<SrItemConfiguracaoVO>();
		listaAcaoVO = new ArrayList<SrAcao.SrAcaoVO>();
		listaTipoPermissaoListaVO = new ArrayList<SrTipoPermissaoLista.SrTipoPermissaoListaVO>();
		descrConfiguracao = configuracao.getDescrConfiguracao();
		
		if(configuracao.itemConfiguracaoSet != null)
			for (SrItemConfiguracao item : configuracao.itemConfiguracaoSet) {
				listaItemConfiguracaoVO.add(item.toVO());
			}
		
		if(configuracao.acoesSet != null)
			for (SrAcao item : configuracao.acoesSet) {
				listaAcaoVO.add(item.toVO());
			}

		if(configuracao.tipoPermissaoSet != null)
			for (SrTipoPermissaoLista item : configuracao.tipoPermissaoSet) {
				listaTipoPermissaoListaVO.add(item.toVO());
			}
		
		if(configuracao.atendente != null)
			atendente = DpLotacaoVO.createFrom(configuracao.atendente.getLotacaoAtual());
		
		if(configuracao.posAtendente != null)
			posAtendente = DpLotacaoVO.createFrom(configuracao.posAtendente.getLotacaoAtual());
		
		if(configuracao.preAtendente != null)
			preAtendente = DpLotacaoVO.createFrom(configuracao.preAtendente.getLotacaoAtual());
		
		if(configuracao.getOrgaoUsuario() != null)
			orgaoUsuario = CpOrgaoUsuarioVO.createFrom(configuracao.getOrgaoUsuario());
	
		if(configuracao.getComplexo() != null)
			local = CpComplexoVO.createFrom(configuracao.getComplexo());
	}
	
	public SrConfiguracaoVO(SrConfiguracao configuracao, boolean atributoObrigatorio) {
		this(configuracao);
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