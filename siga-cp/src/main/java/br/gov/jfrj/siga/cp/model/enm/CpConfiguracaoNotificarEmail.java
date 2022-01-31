package br.gov.jfrj.siga.cp.model.enm;


import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpConfiguracaoNotificarEmail {

	SIGACEMAIL ("Módulo de notificação por email", "SIGA-CEMAIL-"),
	DOCMARC ("Notificações de marcadores destinados a minha unidade", SIGACEMAIL.getSigla() + "DOCMARC"),
	DOCTUN ("Documento foi tramitado para a minha unidade", SIGACEMAIL.getSigla() + "DOCTUN"),
	DOCTUSU ("Documento foi tramitado para o meu usuário", SIGACEMAIL.getSigla() + "DOCTUSU"),
	CONSIG ("Fui incluído como consignatário de um documento", SIGACEMAIL.getSigla() + "CONSIG"),
	RESPASS ("Fui incluído como responsável pela assinatura", SIGACEMAIL.getSigla() + "RESPASSI");
	
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
