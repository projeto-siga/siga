package models.vo;

import models.SrAcao;
import models.SrAcao.SrAcaoVO;
import models.SrConfiguracao;
import models.SrPrioridade;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.model.Selecionavel;

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
	
	public SrConfiguracaoAssociacaoVO(Long idConfiguracao, 
			SrItemConfiguracaoVO itemConfiguracaoVO, SrAcaoVO acaoVO,
			boolean atributoObrigatorio, int tipoSolicitante, 
			Selecionavel orgaoUsuario, CpComplexo complexo, Selecionavel solicitante, 
			Selecionavel atendente, SrPrioridade prioridade) {
		this.idConfiguracao = idConfiguracao;
		this.itemConfiguracaoUnitario = itemConfiguracaoVO;
		this.acaoUnitaria = acaoVO;
		this.atributoObrigatorio = atributoObrigatorio;
		
		this.tipoSolicitante = tipoSolicitante;
		this.orgaoUsuario = SelecionavelVO.createFrom(orgaoUsuario);
		this.complexo = CpComplexoVO.createFrom(complexo);
		this.solicitante = SelecionavelVO.createFrom(solicitante);
		this.atendente = SelecionavelVO.createFrom(atendente);
		this.prioridade = prioridade;
		this.descPrioridade = prioridade != null ? prioridade.descPrioridade : "";
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