package br.gov.jfrj.siga.cp.model.enm;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpAcoesDeNotificarPorEmail {

	ALTERAR_MINHA_SENHA (1709, "Alterar minha senha" , 1709L), 
	ESQUECI_MINHA_SENHA (1710, "Esqueci minha senha", 1710L),
	RESPONS_ASSINATURA (1711, "Responsável pela assinatura", 1711L),
	CONSSIGNATARIO (1712, "Conssignatario", 1712L),
	SUBSTITUICAO (1713, "Substituição", 1713L),
	DOC_TRAMIT_PARA_MEU_USU (1714, "Tramitar para o meu usuário", 1714L),
	DOC_TRAMIT_PARA_M_UNIDADE (1715, "Tramitar para minha unidade", 1715L),
	TRAMIT_DOC_MARCADOS (1716, "Tramitação de documentos marcados", 1716L),
	ALTERACAO_EMAIL (1717, "Alteração de email", 1717L),
	DOC_MARCADORES (1718, "Documentos de marcadores", 1718L),
	CADASTRO_USUARIO (1719, "Cadastro de novo usuário", 1719L);
	
	private Long idLong;
	private int idInt;
	private String descricao;
	
	private CpAcoesDeNotificarPorEmail(int idInt, String descricao, Long idLong) {
		this.descricao = descricao;
		this.idInt = idInt;
		this.idLong = idLong; 
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public static CpSituacaoDeConfiguracaoEnum getById(Integer id) {
		return IEnumWithId.getEnumFromId(id, CpSituacaoDeConfiguracaoEnum.class);
	}

	public int getIdInt() {
		return idInt;
	}
	
	public Long getIdLong() {
		return idLong;
	}
	
}
