package br.gov.jfrj.siga.cp.model.enm;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpServicosNotificacaoPorEmail {

	SIGACEMAIL ("Módulo de notificação por email", "SIGA-CEMAIL", "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email", true),
	DOCMARC ("Notificações de marcadores destinados a minha unidade", SIGACEMAIL.getSigla() + "-" + "DOCMARC", SIGACEMAIL.getChave() + ";" + "DOCMARC:Notificações de marcadores destinados a minha unidade", true),
	DOCTUN ("Documento foi tramitado para a minha unidade", SIGACEMAIL.getSigla() + "-" + "DOCTUN", SIGACEMAIL.getChave() + ";" + "DOCTUN:Documento foi tramitado para a minha unidade", true),
	DOCTUSU ("Documento foi tramitado para o meu usuário", SIGACEMAIL.getSigla() + "-" + "DOCTUSU", SIGACEMAIL.getChave() + ";" + "DOCTUSU:Tramitar para o meu usuário", true),
	COSSIG ("Fui incluído como cossignatário de um documento", SIGACEMAIL.getSigla() + "-" + "COSSIG", SIGACEMAIL.getChave() + ";" + "COSSIG:Fui incluído como cossignatário de um documento", true),
	RESPASS ("Fui incluído como responsável pela assinatura", SIGACEMAIL.getSigla() + "-" + "RESPASS", SIGACEMAIL.getChave() + ";" + "RESPASSI:Fui incluído como responsável pela assinatura", true),
	EMAILALT ("Meu email foi alterado", SIGACEMAIL.getSigla() + "EMAILALT", "", false),
	SUB ("Foi cadastrada uma pessoa ou unidade para me substituir", SIGACEMAIL.getSigla() + "-" + "SUB", "", false),
	ESQSENHA ("Foi usada a opção \"Esqueci minha senha\"", SIGACEMAIL.getSigla() + "-" + "ESQSENHA", "", false),
	ALTSENHA ("Minha senha foi alterada", SIGACEMAIL.getSigla() + "-" + "ALTSENHA", "", false);

	private String descricao; 
	private String sigla;
	private String chave;  
	private Boolean condicao;

	private CpServicosNotificacaoPorEmail(String descricao, String sigla, String chave, Boolean utilizar) {
		this.descricao = descricao;
		this.sigla = sigla;
		this.chave = chave;
		this.condicao = utilizar;
	}

	public void setUtilizar(Boolean utilizar) {
		this.condicao = utilizar;
	}

	public String getDescricao() {  
		return descricao;
	}

	public String getSigla() {  
		return sigla;
	}

	public String getChave() {  
		return chave;
	}

	public Boolean getCondicao() {  
		return condicao;
	}

	public static CpSituacaoDeConfiguracaoEnum getById(Integer id) {
		return IEnumWithId.getEnumFromId(id, CpSituacaoDeConfiguracaoEnum.class);
	}
	
}
