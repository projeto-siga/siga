package br.gov.jfrj.siga.ex.util.notificador.especifico;

public enum ExTemplateEmail {

	DOCUMENTO_COM_MARCAS_TRAMITADO_PARA_USUARIO_OU_UNIDADE("/templates/email/doc-marcado-tramitado-para-unidade.html"),
	DOCUMENTO_TRAMITADO_PARA_UNIDADE ("/templates/email/doc-tramitado-para-unidade.html"),
	DOCUMENTO_TRAMITADO_PARA_USUARIO("/templates/email/doc-tramitado-para-usuario.html"),
	INCLUIDO_COMO_COSSIGNATARIO ("/templates/email/incluido-como-cossignatario.html"),
	RESPONSAVEL_PELA_ASSINATURA ("/templates/email/usuario-responsavel-pela-assinatura.html"),
	DOCUMENTO_ENVIADO_PARA_USUARIO_EXTERNO("/templates/email/doc-enviado-para-usuario-externo.html");
	
	private String path;
	
	private ExTemplateEmail(String path) {
		this.path = path;
	}
	
	public String getPath () {
		return path;
	}
	
}
