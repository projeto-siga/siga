package br.gov.jfrj.siga.cp.model.enm;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpAcoesDeNotificarPorEmail {
	
	SIGA_CEMAIL (1709, "Integrado de Gestão Administrativa" , 1709L, "SIGA-CEMAIL-"),
	ALTERAR_MINHA_SENHA (1709, "Alterar minha senha" , 1709L, "ALTSENHA"), 
	ESQUECI_MINHA_SENHA (1710, "Esqueci minha senha", 1710L, "ESQSENHA"),
	RESPONS_ASSINATURA (1711, "Responsável pela assinatura", 1711L, "RESPASSI"),
	CONSSIGNATARIO (1712, "Conssignatário", 1712L, "CONSSIG"),
	SUBSTITUICAO (1713, "Substituição", 1713L, "SUB"),
	DOC_TRAMIT_PARA_MEU_USU (1714, "Tramitar para o meu usuário", 1714L, "DOCTUSU"),
	DOC_TRAMIT_PARA_M_UNIDADE (1715, "Tramitar para minha unidade", 1715L, "DOCTUN"),
	ALTERACAO_EMAIL (1717, "Alteração de email", 1717L, "ALTEMAIL"),
	DOC_MARCADORES (1718, "Documentos de marcadores", 1718L, "DOCMARC"),
	CADASTRO_USUARIO (1719, "Cadastro de novo usuário", 1719L, "CADUSU");
	
	private Long idLong;
	private int idInt;
	private String descricao;
	private String sigla;
	
	private CpAcoesDeNotificarPorEmail(int idInt, String descricao, Long idLong, String sigla) {
		this.descricao = descricao;
		this.idInt = idInt;
		this.idLong = idLong; 
		this.sigla = sigla;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public String getSigla() {
		return sigla;
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
