package br.gov.jfrj.siga.sr.model.vo;

import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrPrioridade;

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
	public boolean atributoObrigatorio;

	public int tipoSolicitante;
	public SelecionavelVO orgaoUsuario;
	public SelecionavelVO complexo;
	public SelecionavelVO solicitante;
	public SelecionavelVO atendente;
	public SrPrioridade prioridade;
	public String descPrioridade;
	public boolean ativo;

	public SrConfiguracaoAssociacaoVO(SrConfiguracao configuracao) {
		this.idConfiguracao = configuracao.getIdConfiguracao();
		this.atributoObrigatorio = configuracao.isAtributoObrigatorio();

		this.tipoSolicitante = configuracao.getTipoSolicitante();
		this.orgaoUsuario = SelecionavelVO.createFrom(configuracao.getOrgaoUsuario());
		this.complexo = CpComplexoVO.createFrom(configuracao.getComplexo());
		this.solicitante = SelecionavelVO.createFrom(configuracao.getSolicitante());
		this.atendente = SelecionavelVO.createFrom(configuracao.getAtendente());
		this.prioridade = configuracao.getPrioridade();
		this.descPrioridade = configuracao.getPrioridade() != null ? configuracao.getPrioridade().descPrioridade : "";
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