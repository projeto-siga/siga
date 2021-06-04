package br.gov.jfrj.siga.tp.auth.annotation;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class DadosAuditoria {
	private String matricula;
	private String motivoLog;
	
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getMotivoLog() {
		return motivoLog;
	}
	public void setMotivoLog(String motivoLog) {
		this.motivoLog = motivoLog;
	}
	
	
}