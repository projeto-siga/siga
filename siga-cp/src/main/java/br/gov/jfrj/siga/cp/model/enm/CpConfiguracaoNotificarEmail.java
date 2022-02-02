package br.gov.jfrj.siga.cp.model.enm;


import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpConfiguracaoNotificarEmail {

	SIGACEMAIL ("Módulo de notificação por email", "SIGA-CEMAIL"),
	DOCMARC ("Notificações de marcadores destinados a minha unidade", SIGACEMAIL.getSigla() + "-" + "DOCMARC"),
	DOCTUN ("Documento foi tramitado para a minha unidade", SIGACEMAIL.getSigla() + "-" + "DOCTUN"),
	DOCTUSU ("Documento foi tramitado para o meu usuário", SIGACEMAIL.getSigla() + "-" + "DOCTUSU"),
	COSSIG ("Fui incluído como cossignatário de um documento", SIGACEMAIL.getSigla() + "-" + "COSSIG"),
	RESPASS ("Fui incluído como responsável pela assinatura", SIGACEMAIL.getSigla() + "-" + "RESPASSI"),
	EMAILALT ("Meu email foi alterado", SIGACEMAIL.getSigla() + "EMAILALT"),
	SUB ("Foi cadastrada uma pessoa ou unidade para me substituir", SIGACEMAIL.getSigla() + "-" + "SUB"),
	ESQSENHA ("Foi usada a opção \"Esqueci minha senha\"", SIGACEMAIL.getSigla() + "-" + "ESQSENHA"),
	ALTSENHA ("Minha senha foi alterada", SIGACEMAIL.getSigla() + "-" + "ALTSENHA");
	
	private String descricao; 
	private String sigla;
	
	private CpConfiguracaoNotificarEmail(String descricao, String sigla) {
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
