package br.gov.jfrj.siga.ex.gsa;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.base.SigaBaseProperties;

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

	@Override
	public void close() throws SecurityException {
		try {
			enviarEmail();
		} catch (Exception e) {
			throw new RuntimeException("Não foi possível enviar o e-mail!");
		}
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
		
		Correio.enviar(
				SigaBaseProperties.getString("servidor.smtp.usuario.remetente"),
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

}
