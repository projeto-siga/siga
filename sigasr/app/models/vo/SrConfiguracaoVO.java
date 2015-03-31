package models.vo;

import java.util.ArrayList;
import java.util.List;

import models.SrAcao;
import models.SrConfiguracao;
import models.SrItemConfiguracao;
import models.SrPrioridade;
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
	public SelecionavelVO orgaoUsuario;
	public SelecionavelVO complexo;
	public SrPrioridade prioridade;
	public String descPrioridade;
	
	// Solicitante
	public SelecionavelVO dpPessoa;
	public SelecionavelVO lotacao;
	public SelecionavelVO lotacaoParaInclusaoAutomatica;
	public SelecionavelVO cargo;
	public SelecionavelVO funcaoConfianca;
	public SelecionavelVO cpGrupo;
	public SelecionavelVO solicitante;
	public SrPrioridade prioridadeNaLista;
	public String descPrioridadeNaLista;
	public SelecionavelVO dpPessoaParaInclusaoAutomatica;
	
	public SrConfiguracaoVO(SrConfiguracao configuracao) {
		idConfiguracao = configuracao.getId();
		hisIdIni = configuracao.getHisIdIni();
		isHerdado = configuracao.isHerdado;
		utilizarItemHerdado = configuracao.utilizarItemHerdado;
		ativo = configuracao.isAtivo();
		descrConfiguracao = configuracao.getDescrConfiguracao();
		prioridade = configuracao.prioridade;
		prioridadeNaLista = configuracao.prioridadeNaLista;
		descPrioridade = configuracao.prioridade != null ? configuracao.prioridade.descPrioridade : "";
		descPrioridadeNaLista = configuracao.prioridadeNaLista != null ? configuracao.prioridadeNaLista.descPrioridade : "";
		
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
		
		if (configuracao.getOrgaoUsuario() != null)
			orgaoUsuario = SelecionavelVO.createFrom(configuracao.getOrgaoUsuario().getId(), 
					configuracao.getOrgaoUsuario().getDescricao(), 
					configuracao.getOrgaoUsuario().getAcronimoOrgaoUsu());
		
		complexo = CpComplexoVO.createFrom(configuracao.getComplexo());
		
		// Dados do Solicitante
		dpPessoa = SelecionavelVO.createFrom(configuracao.getDpPessoa(), configuracao.getTipoSolicitante());
		dpPessoaParaInclusaoAutomatica = dpPessoa;
		lotacao = SelecionavelVO.createFrom(configuracao.getLotacao(), configuracao.getTipoSolicitante());
		lotacaoParaInclusaoAutomatica = lotacao;
		cargo = SelecionavelVO.createFrom(configuracao.getCargo(), configuracao.getTipoSolicitante());
		funcaoConfianca = SelecionavelVO.createFrom(configuracao.getFuncaoConfianca(), configuracao.getTipoSolicitante());
		cpGrupo = SelecionavelVO.createFrom(configuracao.getCpGrupo(), configuracao.getTipoSolicitante());
		
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
		return toJsonObject().toString();
	}
	
	public JsonObject toJsonObject() {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		Gson gson = builder.create();
		return (JsonObject) gson.toJsonTree(this);
	}
}