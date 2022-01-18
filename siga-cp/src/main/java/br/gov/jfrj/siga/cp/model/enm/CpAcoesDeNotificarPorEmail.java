package br.gov.jfrj.siga.cp.model.enm;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpAcoesDeNotificarPorEmail {
	
	SIGA_CEMAIL ("Integrado de Gestão Administrativa" , "SIGA-CEMAIL-"),
	ALTERAR_MINHA_SENHA ("Alterar minha senha" , "ALTSENHA"), 
	ESQUECI_MINHA_SENHA ("Esqueci minha senha", "ESQSENHA"),
	RESPONS_ASSINATURA ("Responsável pela assinatura", "RESPASSI"),
	CONSSIGNATARIO ("Conssignatário", "CONSSIG"),
	SUBSTITUICAO ("Substituição", "SUB"),
	DOC_TRAMIT_PARA_MEU_USU ("Tramitar para o meu usuário", "DOCTUSU"),
	DOC_TRAMIT_PARA_M_UNIDADE ("Tramitar para minha unidade", "DOCTUN"),
	ALTERACAO_EMAIL ("Alteração de email", "ALTEMAIL"),
	DOC_MARCADORES ("Documentos de marcadores", "DOCMARC"),
	CADASTRO_USUARIO ("Cadastro de novo usuário", "CADUSU");
	
	private String descricao;
	private String sigla;
	
	private CpAcoesDeNotificarPorEmail(String descricao, String sigla) {
		this.descricao = descricao;
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
}
