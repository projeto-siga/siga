package br.gov.jfrj.siga.base;

public class SigaModal {
	
	public static final String ALERTA = "sigaModalAlerta";
		
	private String titulo;
	private String mensagem;	
	private boolean centralizar;
	
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

}
