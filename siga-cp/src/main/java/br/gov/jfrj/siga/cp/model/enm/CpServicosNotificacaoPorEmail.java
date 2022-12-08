package br.gov.jfrj.siga.cp.model.enm;

import java.util.Arrays;
import java.util.Optional;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpServicosNotificacaoPorEmail {

	SIGACEMAIL ("Módulo de notificação por email", "SIGA-CEMAIL", "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email", true, 
			""),
	DOCMARC ("Notificações de marcadores destinados ao meu usuário ou minha unidade", SIGACEMAIL.getSigla() + "-" + "DOCMARC", SIGACEMAIL.getChave() + ";" + "DOCMARC:Notificações de marcadores destinados a minha unidade", true, 
			"Será enviado um e-mail quando o documento tramitado conter um ou mais marcadores do tipo Geral ou Local."),
	DOCTUN ("Documento foi tramitado para a minha unidade", SIGACEMAIL.getSigla() + "-" + "DOCTUN", SIGACEMAIL.getChave() + ";" + "DOCTUN:Documento foi tramitado para a minha unidade", true, 
			"Será enviado um e-mail para todos os usuários pertencentes a unidade em que o documento foi tramitado."),
	DOCTUSU ("Documento foi tramitado para o meu usuário", SIGACEMAIL.getSigla() + "-" + "DOCTUSU", SIGACEMAIL.getChave() + ";" + "DOCTUSU:Tramitar para o meu usuário", true, 
			"Será enviado um e-mail quando um documento for tramitado para o seu usuário."),
	COSSIG ("Fui incluído como cossignatário de um documento", SIGACEMAIL.getSigla() + "-" + "COSSIG", SIGACEMAIL.getChave() + ";" + "COSSIG:Fui incluído como cossignatário de um documento", true, 
			"Será enviado um e-mail quando o seu usuário for incluído como cossignatário de algum documento assinado."),
	RESPASS ("Fui incluído como responsável pela assinatura", SIGACEMAIL.getSigla() + "-" + "RESPASSI", SIGACEMAIL.getChave() + ";" + "RESPASSI:Fui incluído como responsável pela assinatura", true, 
			"Será enviado um e-mail quando o seu usuário for adicionado como responsável pela assinatura de um documento finalizado."),
	EMAILALT ("Meu email foi alterado", SIGACEMAIL.getSigla() + "EMAILALT", "", false, 
			"Será enviado um e-mail quando seu e-mail for alterado."),
	SUB ("Foi cadastrada uma pessoa ou unidade para me substituir", SIGACEMAIL.getSigla() + "-" + "SUB", "", false, 
			"Será enviado um e-mail quando o seu usuário for cadastrado como substituto de outro usuário ou unidade."),
	ESQSENHA ("Foi usada a opção \"Esqueci minha senha\"", SIGACEMAIL.getSigla() + "-" + "ESQSENHA", "", false, 
			"Será enviado um e-mail quando for usada a função \"Esqueci minha senha\"."),
	ALTSENHA ("Minha senha foi alterada", SIGACEMAIL.getSigla() + "-" + "ALTSENHA", "", false, 
			"Será enviado um e-mail quando sua senha for alterada."); 

	private String descricao; 
	private String sigla;
	private String chave;  
	private Boolean condicao;
	private String explicacao;

	private CpServicosNotificacaoPorEmail(String descricao, String sigla, String chave, 
			Boolean utilizar, String explicacao) {
		this.descricao = descricao;
		this.sigla = sigla;
		this.chave = chave;
		this.condicao = utilizar;
		this.explicacao = explicacao;
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
	
	public String getExplicacao() {  
		return explicacao;
	}

	public static CpSituacaoDeConfiguracaoEnum getById(Integer id) {
		return IEnumWithId.getEnumFromId(id, CpSituacaoDeConfiguracaoEnum.class);
	}
	
	
    public static Optional<CpServicosNotificacaoPorEmail> getBySigla(String sigla) {
        return Arrays.stream(CpServicosNotificacaoPorEmail.values())
            .filter(servico -> servico.sigla.equals(sigla))
            .findFirst();
    }
	
}
