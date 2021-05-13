/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.importar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.bouncycastle.util.encoders.Base64;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.util.CpSincPropriedade;

public class CorreioSinc {
	
	private static CpSincPropriedade prop  = new CpSincPropriedade();
	
	private static Logger logger = Logger.getLogger("br.gov.jfrj.siga.base.email");
	
	public static void enviar(final String destinatario, final String assunto,
			final String conteudo) throws Exception {
		final String[] to = { destinatario };

		CorreioSinc.enviar(	prop.emailRemetente(),
				to, assunto, conteudo, null);
	}
	
	public static void enviar(final String[] destinatarios, final String assunto,
			final String conteudo) throws Exception {

		CorreioSinc.enviar(
				prop.emailRemetente(),
				destinatarios, assunto, conteudo, null);
	}
		
	public static void enviar(String remetente,
			final String[] destinatarios, final String assunto,
			final String conteudo, final String conteudoHTML) throws Exception {

		if (remetente == null)
			remetente = prop.emailRemetente();
		
		List<String> listaServidoresEmail = new ArrayList<>();

		// lista indisponivel. Tenta ler apenas 1 servidor definido.
		if (listaServidoresEmail.isEmpty()) {
			listaServidoresEmail = new ArrayList<String>();
			listaServidoresEmail.add(prop.servidorSmtp());
		}

		boolean servidorDisponivel = false;
		String causa = " ";
		for (String servidorEmail : listaServidoresEmail) {
			try {
				enviarParaServidor(servidorEmail, remetente, destinatarios,
						assunto, conteudo, conteudoHTML);
				servidorDisponivel = true;
				break;
			} catch (Exception e) {
					String mensagem = "Servidor de e-mail '" + servidorEmail + "' indisponível: " + e.getMessage();
	
					if (e.getCause() != null) {
						causa =  ", causa: " + e.getCause().getMessage();
						mensagem = mensagem + causa;
				}
					logger.warning(mensagem);
			}
		}

		if (!servidorDisponivel) {
			throw new AplicacaoException(
					"Não foi possível se conectar ao servidor de e-mail!");
		}

	}

	private static void enviarParaServidor(final String servidorEmail,
			String remetente, final String[] destinatarios,
			final String assunto, final String conteudo,
			final String conteudoHTML) throws Exception {
		// Cria propriedades a serem usadas na sessão.
		final Properties props = new Properties();
		Set<String> destSet = new HashSet<String>();
		
		
	
		// Define propriedades da sessão.
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", servidorEmail);
		props.put("mail.host", servidorEmail);
		props.put("mail.mime.charset", "UTF-8");
        props.put("mail.smtp.starttls.enable", prop.servidorSmtpStarttlsEnable());

		// Cria sessão. setDebug(true) é interessante pois
		// mostra os passos do envio da mensagem e o
		// recebimento da mensagem do servidor no console.
		Session session = null;
		if (Boolean.valueOf(prop.servidorSmtpAuth())) {
			props.put("mail.smtp.auth", "true");
			final String usuario = prop.servidorSmtpAuthUsuario();
			final String senha = prop.servidorSmtpAuthSenha();
			session = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(usuario, senha);
				}
			});
		} else {
			session = Session.getInstance(props);
		}

		final boolean debug = Boolean.parseBoolean(prop.servidorSmtpDebug());
		// final boolean debug = Boolean.parseBoolean(Mensagens
		// .getString("servidor.smtp.debug"));
		session.setDebug(debug);
		// Cria mensagem e seta alguns valores que constituem
		// os seus headers.
		final Message msg = new MimeMessage(session);

		if (destinatarios.length == 1) {
			if (!destinatarios[0].equals("null") && !destSet.contains(destinatarios[0]))
				destSet.add(destinatarios[0]);
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(
				destinatarios[0]));

		} else {
			for (String s : destinatarios) {
				if (!s.equals("null") && !destSet.contains(s))
					destSet.add(s);
			}

			final InternetAddress[] endereco = new InternetAddress[destSet
					.size()];
			int i = 0;
			for (String email : destSet) {
				if(!email.equals("null")){
					endereco[i] = new InternetAddress(email);
					i++;
				}
			}
			msg.setRecipients(Message.RecipientType.TO, endereco);
		}
		msg.setFrom(new InternetAddress(remetente));
		msg.setSubject(assunto);

		

		if (conteudoHTML == null) {
			// msg.setText(conteudo);
			msg.setSubject(assunto);
			msg.setContent(conteudo, "text/plain;charset=UTF-8");
		} else {
			Multipart mp = new MimeMultipart("alternative");

			// Add text version
			InternetHeaders ihs = new InternetHeaders();
			ihs.addHeader("Content-Type", "text/plain; charset=UTF-8");
			ihs.addHeader("Content-Transfer-Encoding", "base64");
			MimeBodyPart mb1 = new MimeBodyPart(ihs, Base64.encode(conteudo
					.getBytes("utf-8")));
			mp.addBodyPart(mb1);

			// Do the same with the HTML part
			InternetHeaders ihs2 = new InternetHeaders();
			ihs2.addHeader("Content-Type", "text/html; charset=UTF-8");
			ihs2.addHeader("Content-Transfer-Encoding", "base64");
			MimeBodyPart mb2 = new MimeBodyPart(ihs2,
					Base64.encode(conteudoHTML.getBytes("utf-8")));
			mp.addBodyPart(mb2);

			// Set the content for the message and transmit
			msg.setContent(mp);
		}

		// Envia mensagem.
		// Transport.send(msg);

		Transport tr = new br.gov.jfrj.siga.base.SMTPTransport(session,
				null);
		tr.connect(servidorEmail, Integer.valueOf(prop.servidorSmtpPorta()), null, null);
		msg.saveChanges(); // don't forget this
		tr.sendMessage(msg, msg.getAllRecipients());
		tr.close();

		logger.log(Level.INFO,"Email enviado para " + Arrays.asList(destSet).toString() + "[" + assunto + "]");
		logger.log(Level.FINE, "Detalhes do e-mail enviado:"
					+ "\nAssunto: " + assunto
					+ "\nDe: " + remetente
					+ "\nPara: " + Arrays.asList(destinatarios).toString()
					+ "\nTexto: " + (conteudoHTML == null?conteudo:conteudoHTML));
	}

	public static void enviar(String remetente, String[] destinatarios,
			String assunto, String conteudo) throws Exception {
		CorreioSinc.enviar(remetente, destinatarios, assunto, conteudo, null);
	}	
	
	public static String obterHTMLEmailParaUsuarioExternoAssinarDocumento(String uri, String siglaDocumento, String siglaUsuario) {
		StringBuffer sbHtml = new StringBuffer();
		
		sbHtml.append("<html>");
		sbHtml.append("<body>");
		sbHtml.append("	<table>");
		sbHtml.append("		<tbody>");
		sbHtml.append("			<tr>");
		sbHtml.append("				<td style='height: 80px; background-color: #f6f5f6; padding: 10px 20px;'>");
		sbHtml.append("					<img style='padding: 10px 0px; text-align: center;' src='https://www.documentos.spsempapel.sp.gov.br/siga/imagens/logo-sem-papel-cor.png' alt='SP Sem Papel' width='108' height='50' />");		
		sbHtml.append("				</td>");
		sbHtml.append("			</tr>");
		sbHtml.append("			<tr>");
		sbHtml.append("				<td style='background-color: #bbb; padding: 0 20px;'>");
		sbHtml.append("					<h3 style='height: 20px;'>Governo do Estado de S&atilde;o Paulo</h3>");					
		sbHtml.append("				</td>");
		sbHtml.append("			</tr>");
		sbHtml.append("			<tr>");
		sbHtml.append("				<td style='height: 310px; padding: 10px 20px;'>");
		sbHtml.append("					<div>");
		sbHtml.append("						<p style='color: #808080;'>Esse <a style='color: #808080;' href='" + uri +  "' target='_blank'><b>link</b></a> fornece acesso ao documento nº <b>" + siglaDocumento + "</b>, do Programa SP Sem Papel, cujo usuário <b>" + siglaUsuario +  "</b> é interessado.</p>");
		sbHtml.append("						<p style='color: #808080;'>Para visualizar e assinar o documento, acesse o link: <a style='color: #808080;' href='" + uri +  "' target='_blank'><b>" + uri + "</b></a>");
		sbHtml.append("						<p style='color: #808080;'>Atenção: Esse e-mail é de uso restrito ao usuário e entidade para a qual foi endereçado. Se você não é destinatário desta mensagem, você está, por meio desta, notificado que não deverá retransmitir, imprimir, copiar, examinar, distribuir ou utilizar informação contida nesta mensagem.</p>");
		sbHtml.append("					</div>");
		sbHtml.append("				</td>");
		sbHtml.append("			</tr>");
		sbHtml.append("			<tr>");
		sbHtml.append("				<td style='height: 18px; padding: 0 20px; background-color: #eaecee;'>"); 
		sbHtml.append("					<p>");
		sbHtml.append("						<span style='color: #aaa;'><b>Aten&ccedil;&atilde;o:</b> esta &eacute; uma mensagem autom&aacute;tica. Por favor n&atilde;o responda&nbsp;</span>");
		sbHtml.append("					</p>");
		sbHtml.append("				</td>");
		sbHtml.append("			</tr>");
		sbHtml.append("	 	</tbody>");
		sbHtml.append("	</table>");			
		sbHtml.append("</body>");
		sbHtml.append("</html>");
		
		return sbHtml.toString();
	}

	public CpSincPropriedade getProp() {
		return prop;
	}

	public void setProp(CpSincPropriedade prop) {
		this.prop = prop;
	}	

}