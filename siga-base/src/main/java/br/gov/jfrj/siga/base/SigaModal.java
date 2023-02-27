package br.gov.jfrj.siga.base;

public class SigaModal {
	
	public static final String ALERTA = "sigaModalAlerta";
	public static final String CONFIRMACAO = "sigaModalConfirmacao";
		
	private String titulo;
	private String mensagem;	
	private boolean centralizar;
	private String linkBotaoDeAcao;
	private String classBotaoDeAcao = "btn-primary";
	private String classBotaoDeFechar = "btn-secondary";
	private boolean inverterBotoes;
	

	public SigaModal(String mensagem) {		
		this.mensagem = mensagem;
	}
	
	public static SigaModal mensagem(String mensagem) {
		return new SigaModal(mensagem);		
	}
	
	public SigaModal titulo(String titulo) {
		this.titulo = titulo;
		return this;
	}
	
	public SigaModal centralizar() {
		this.centralizar = true;
		return this;
	}
			
	public String getTitulo() {		
		return titulo;
	}
	
	public String getMensagem() {
		return mensagem;
	}	
	
	public boolean isCentralizar() {
		return centralizar;
	}
	
	public String getLinkBotaoDeAcao() {
		return linkBotaoDeAcao;
	}

	public SigaModal linkBotaoDeAcao(String linkBotaoDeAcao) {
		this.linkBotaoDeAcao = linkBotaoDeAcao;
		return this;
	}
	
	public String getClassBotaoDeAcao() {
		return classBotaoDeAcao;
	}

	public SigaModal classBotaoDeAcao(String classBotaoDeAcao) {
		this.classBotaoDeAcao = classBotaoDeAcao;
		return this;
	}

	public String getClassBotaoDeFechar() {
		return classBotaoDeFechar;
	}

	public SigaModal classBotaoDeFechar(String classBotaoDeFechar) {
		this.classBotaoDeFechar = classBotaoDeFechar;
		return this;
	}
	
	
	public SigaModal inverterBotoes() {
		this.inverterBotoes = true;
		return this;
	}
	
	public boolean isInverterBotoes() {
		return inverterBotoes;
	}

}
