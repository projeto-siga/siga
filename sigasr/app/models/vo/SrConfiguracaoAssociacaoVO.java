package models.vo;

import models.SrAcao;
import models.SrAcao.SrAcaoVO;
import models.SrConfiguracao;
import models.SrPrioridade;

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
	
	public int tipoSolicitante;
	public SelecionavelVO orgaoUsuario;
	public SelecionavelVO complexo;
	public SelecionavelVO solicitante;
	public SelecionavelVO atendente;
	public SrPrioridade prioridade;
	public String descPrioridade;
	public boolean ativo;
	
	public SrConfiguracaoAssociacaoVO(SrConfiguracao configuracao, SrItemConfiguracaoVO itemConfiguracaoVO, SrAcaoVO acaoVO) {
		this.idConfiguracao = configuracao.getIdConfiguracao();
		this.itemConfiguracaoUnitario = itemConfiguracaoVO;
		this.acaoUnitaria = acaoVO;
		this.atributoObrigatorio = configuracao.atributoObrigatorio;
		
		this.tipoSolicitante = configuracao.getTipoSolicitante();
		this.orgaoUsuario = SelecionavelVO.createFrom(configuracao.getOrgaoUsuario());
		this.complexo = CpComplexoVO.createFrom(configuracao.getComplexo());
		this.solicitante = SelecionavelVO.createFrom(configuracao.getSolicitante());
		this.atendente = SelecionavelVO.createFrom(configuracao.atendente);
		this.prioridade = configuracao.prioridade;
		this.descPrioridade = configuracao.prioridade != null ? configuracao.prioridade.descPrioridade : "";
		this.ativo = configuracao.isAtivo();
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