package br.gov.jfrj.siga.tp.auth.annotation;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
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