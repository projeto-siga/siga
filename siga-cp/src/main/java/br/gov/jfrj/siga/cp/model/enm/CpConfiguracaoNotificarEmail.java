package br.gov.jfrj.siga.cp.model.enm;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpConfiguracaoNotificarEmail {

	DOCMARC ("Notificações de marcadores destinados a minha unidade", "docmarc"),
	DOCTUN ("Documento foi tramitado para a minha unidade", "doctun"),
	DOCTUSU ("Documento foi tramitado para o meu usuário", "doctusu"),
	CONSIG ("Fui incluído como consignatário de um documento", "consig"),
	RESPASS ("Fui incluído como responsável pela assinatura", "respass");
	
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
