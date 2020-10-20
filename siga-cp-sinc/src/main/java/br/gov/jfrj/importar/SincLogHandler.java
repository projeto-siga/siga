package br.gov.jfrj.importar;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.slf4j.event.Level;

import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.util.CpSincPropriedade;

/**
 * Tratador de logs de sincronismo que envia e-mail com relatório das operações
 * @author kpf
 *
 */
public class SincLogHandler extends Handler {

	private StringBuffer sb = new StringBuffer();
	private String[] destinatariosEmail;
	private String assunto = "Log de Importação";
	
	public SincLogHandler(){
		
	}
	
	public SincLogHandler(String[] destinatariosEmail, String Assunto) {
		 this.destinatariosEmail = destinatariosEmail;
	}

	@Override
	public void publish(LogRecord record) {
		sb.append(record.getMessage());
		sb.append("\n");
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	public int close(Logger log) throws SecurityException {
		int statusErro = 0;
		try {
			enviarEmail();
			statusErro = 0;
		} catch (Exception e) {
			statusErro = 1;
			log.warning("Não foi possível enviar e-mail - causa: " + e.getMessage());
		}
		close();
		return statusErro;
	}
	
		
	public void enviarEmail() throws Exception {
		sb.append("Enviando e-mails para: ");
		
		for (String d : destinatariosEmail) {
			sb.append(d);
			sb.append(",\n");
		}
		
		if (destinatariosEmail.length > 1){
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		
		CorreioSinc.enviar(
				new CpSincPropriedade().getString("servidor.smtp.usuario.remetente"),
				getDestinatariosEmail(), getAssunto(), sb.toString(), null);
	}

	public String[] getDestinatariosEmail() {
		return destinatariosEmail;
	}

	public void setDestinatariosEmail(String[] destinatariosEmail) {
		this.destinatariosEmail = destinatariosEmail;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	@Override
	public void close() throws SecurityException {
		// TODO Auto-generated method stub
		
	}

}
