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
package br.gov.jfrj.siga.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Correio {

	public static void enviar(String destinatario, String assunto,
			String conteudo) throws Exception {
		String[] to = { destinatario };

		//Correio.enviar("SIGA<no-reply@jfrj.jus.br>", to, assunto, conteudo);
		Correio.enviar(ImportarXmlProperties.getString("servidor.usuario.remetente"), to, assunto, conteudo);
	}

	public static void enviar(String remetente, String[] destinatarios,
			String assunto, String conteudo) throws Exception {
		// Cria propriedades a serem usadas na sessão.
		Properties props = new Properties();

		// Define propriedades da sessão.
		props.put("mail.transport.protocol", "smtp");
		//props.put("mail.smtp.host", "mail");
		props.put("mail.smtp.host", ImportarXmlProperties.getString("servidor.smtp"));
		props.put("mail.host", ImportarXmlProperties.getString("servidor.smtp"));
		// Cria sessão. setDebug(true) é interessante pois
		// mostra os passos do envio da mensagem e o
		// recebimento da mensagem do servidor no console.
		Session session = Session.getDefaultInstance(props);
		boolean debug = Boolean.parseBoolean("false");
		session.setDebug(debug);
		// Cria mensagem e seta alguns valores que constituem
		// os seus headers.
		MimeMessage msg = new MimeMessage(session);

		try {
			if (destinatarios.length == 1) {
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(
						destinatarios[0]));

			} else {
				InternetAddress[] endereco = new InternetAddress[destinatarios.length];
				for (int i = 0; i < destinatarios.length; i++) {
					endereco[i] = new InternetAddress(destinatarios[i]);
				}
				msg.setRecipients(Message.RecipientType.TO, endereco);
			}
			msg.setFrom(new InternetAddress(remetente));
			msg.setSubject(assunto, "UTF-8");

			msg.setText(conteudo, "UTF-8");

			// Envia mensagem.
			Transport.send(msg);
		} catch (Exception e) {
			throw e;
		}
	}
	
}
