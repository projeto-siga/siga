package br.gov.jfrj.siga.cp;

import java.util.Arrays;
import java.util.stream.Stream;

public enum CpTipoConfiguracaoDicionario {	
	
	INCLUIR_COMO_FILHO(30, "Todos os modelos que receberem essa configuração e forem liberados para o seu órgão, unidade, cargo ou usuário"
			+ ", serão disponibilizados como opção para criar um documento filho ao selecionar a opção "
			+ "<img src='/siga/imagens/dicionario-tipos-configuracao/incluir_documento_1.jpg' height='50'>"),
	INCLUIR_DOCUMENTO(41, "Exibe o botão <img src='/siga/imagens/dicionario-tipos-configuracao/incluir_documento_1.jpg' height='50'>"
			+ " após o documento ter sido criado e assinado. Esse botão permitirá criar um documento filho e juntá-lo automaticamente ao documento principal."),
	CRIAR_COMO_NOVO(42, "Todos os modelos que receberem essa configuração e forem liberados para seu órgão, unidade, cargo ou usuário"
			+ ", serão disponibilizados como opção para criar um novo documento"  
			+ ". Ao acessar a opção \"Novo\" no menu de Documentos:<br/><img src='/siga/imagens/dicionario-tipos-configuracao/criar_como_novo_1.jpg' height='250'>");	
	
	private long id;
	private String conteudo;
	
	CpTipoConfiguracaoDicionario(long id, String conteudo) {
		this.id = id;
		this.conteudo = conteudo;
	}
	
	public long getId() {
		return id;
	}
	
	public String getConteudo() {
		return conteudo;
	}
	
	public static CpTipoConfiguracaoDicionario obterDicionario(long idTpConfiguracao) {
		Stream<CpTipoConfiguracaoDicionario> dicionariosStream = Arrays.stream(CpTipoConfiguracaoDicionario.values());		
		return dicionariosStream.filter(d -> d.getId() == idTpConfiguracao).findAny().orElse(null);			
	}
	
}
