package models.vo;

import java.util.ArrayList;
import java.util.List;

import models.SrAcao;
import models.SrConfiguracao;
import models.SrItemConfiguracao;
import models.SrTipoPermissaoLista;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Classe que representa um {@link SrConfiguracaoVO VO} da classe
 * {@link SrConfiguracao}.
 * 
 * @author DB1
 */
public class SrConfiguracaoVO {
	public Long idConfiguracao;
	public Long hisIdIni;
	public boolean isHerdado;
	public boolean utilizarItemHerdado;
	public boolean atributoObrigatorio;
	public boolean ativo;
	public String descrConfiguracao;
	
	public List<SrListaVO> listaVO; 
	public List<SrItemConfiguracaoVO> listaItemConfiguracaoVO;
	public List<SrAcao.SrAcaoVO> listaAcaoVO;
	public List<SrTipoPermissaoLista.SrTipoPermissaoListaVO> listaTipoPermissaoListaVO;
	public SelecionavelVO atendente;
	public SelecionavelVO posAtendente;
	public SelecionavelVO equipeQualidade;
	public SelecionavelVO preAtendente;
	public SelecionavelVO orgaoUsuario;
	public SelecionavelVO complexo;
	public SrPesquisaVO pesquisaSatisfacao;
	
	// Solicitante
	public SelecionavelVO dpPessoa;
	public SelecionavelVO lotacao;
	public SelecionavelVO cargo;
	public SelecionavelVO funcaoConfianca;
	public SelecionavelVO grupo;
	public SelecionavelVO solicitante;
	
	public SrConfiguracaoVO(SrConfiguracao configuracao) {
		idConfiguracao = configuracao.getId();
		hisIdIni = configuracao.getHisIdIni();
		isHerdado = configuracao.isHerdado;
		utilizarItemHerdado = configuracao.utilizarItemHerdado;
		ativo = configuracao.isAtivo();
		descrConfiguracao = configuracao.getDescrConfiguracao();
		
		if(configuracao.itemConfiguracaoSet != null) {
			listaItemConfiguracaoVO = new ArrayList<SrItemConfiguracaoVO>();
		
			for (SrItemConfiguracao item : configuracao.itemConfiguracaoSet) {
				listaItemConfiguracaoVO.add(item.toVO());
			}
		}
		
		if(configuracao.acoesSet != null) {
			listaAcaoVO = new ArrayList<SrAcao.SrAcaoVO>();
		
			for (SrAcao item : configuracao.acoesSet) {
				listaAcaoVO.add(item.toVO());
			}
		}

		if(configuracao.tipoPermissaoSet != null) {
			listaTipoPermissaoListaVO = new ArrayList<SrTipoPermissaoLista.SrTipoPermissaoListaVO>();
		
			for (SrTipoPermissaoLista item : configuracao.tipoPermissaoSet) {
				listaTipoPermissaoListaVO.add(item.toVO());
			}
		}
		
		if (configuracao.atendente != null)
			atendente = SelecionavelVO.createFrom(configuracao.atendente.getLotacaoAtual());
		
		if (configuracao.posAtendente != null)
			posAtendente = SelecionavelVO.createFrom(configuracao.posAtendente.getLotacaoAtual());
		
		if (configuracao.preAtendente != null)
			preAtendente = SelecionavelVO.createFrom(configuracao.preAtendente.getLotacaoAtual());
		
		if (configuracao.getOrgaoUsuario() != null)
			orgaoUsuario = SelecionavelVO.createFrom(configuracao.getOrgaoUsuario().getId(), 
					configuracao.getOrgaoUsuario().getDescricao(), 
					configuracao.getOrgaoUsuario().getAcronimoOrgaoUsu());
		
		complexo = CpComplexoVO.createFrom(configuracao.getComplexo());
		equipeQualidade = SelecionavelVO.createFrom(configuracao.equipeQualidade);
		pesquisaSatisfacao = SrPesquisaVO.createFrom(configuracao.pesquisaSatisfacao);
		
		// Dados do Solicitante
		dpPessoa = SelecionavelVO.createFrom(configuracao.getDpPessoa(), configuracao.getTipoSolicitante());
		lotacao = SelecionavelVO.createFrom(configuracao.getLotacao(), configuracao.getTipoSolicitante());
		cargo = SelecionavelVO.createFrom(configuracao.getCargo(), configuracao.getTipoSolicitante());
		funcaoConfianca = SelecionavelVO.createFrom(configuracao.getFuncaoConfianca(), configuracao.getTipoSolicitante());
		grupo = SelecionavelVO.createFrom(configuracao.getCpGrupo(), configuracao.getTipoSolicitante());
		
		solicitante = SelecionavelVO.createFrom(configuracao.getSolicitante());
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
		JsonObject jsonObject = (JsonObject) gson.toJsonTree(this);
		
		if (this.listaItemConfiguracaoVO.size() == 1) {
			jsonObject.add("itemConfiguracaoUnitario", gson.toJsonTree(this.listaItemConfiguracaoVO.get(0)));
		}
		
		if (this.listaAcaoVO.size() == 1) {
			jsonObject.add("acaoUnitaria", gson.toJsonTree(this.listaAcaoVO.get(0)));
		}
		return jsonObject.toString();
	}
}