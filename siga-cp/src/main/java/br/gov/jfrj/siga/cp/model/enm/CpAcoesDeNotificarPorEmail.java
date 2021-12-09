package br.gov.jfrj.siga.cp.model.enm;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpAcoesDeNotificarPorEmail implements IEnumWithId{

	ALTERAR_MINHA_SENHA (1709, "Alterar minha senha"), 
	ESQUECI_MINHA_SENHA (1710, "Esqueci minha senha"),
	RESPONS_ASSINATURA (1711, "Responsável pela assinatura"),
	CONSSIGNATARIO (1712, "Conssignatario"),
	SUSBSTITUICAO (1713, "Substituição"),
	TRAMIT_PARA_MEU_USU (1714, "Tramitar para o meu usuário"),
	TRAMIT_PARA_M_UNIDADE (1715, "Tramitar para minha unidade"),
	TRAMIT_DOC_MARCADOS (1716, "Tramitação de documentos marcados"),
	ALTERACAO_EMAIL (1717, "Alteração de email"),
	DOC_MARCADORES (1718, "Documentos de marcadores"),
	CADASTRO_USUARIO (1719, "Cadastro de novo usuário");
	
	private int id;
	private String descricao;
	
	private CpAcoesDeNotificarPorEmail(int id, String descricao) {
		this.descricao = descricao;
		this.id = id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public static CpSituacaoDeConfiguracaoEnum getById(Integer id) {
		return IEnumWithId.getEnumFromId(id, CpSituacaoDeConfiguracaoEnum.class);
	}

	@Override
	public Integer getId() {
		return id;
	}
	
}
