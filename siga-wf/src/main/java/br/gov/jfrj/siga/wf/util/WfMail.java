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
package br.gov.jfrj.siga.wf.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jbpm.context.def.VariableAccess;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.mail.Mail;
import org.jbpm.taskmgmt.def.TaskController;

import br.gov.jfrj.siga.wf.bl.Wf;

/**
 * Classe que representa o serviço de e-mail. Esta classe é definida em
 * siga-wf/src/jbpm.cfg.xml.
 * 
 * @author kpf
 * 
 */
public class WfMail extends Mail {

	private Long tiId;
	private List<VariableAccess> docAccessVariables;
	private Map docVariables;
	Logger logger = Logger.getLogger("br.gov.jfrj.siga.wf.email");

	private static final long serialVersionUID = -1530708539893196841L;

	/**
	 * Retorna o texto do e-mail. Esse método inclui automaticamente rodapé com
	 * informações da tarefa correspondente ao e-mail.
	 */
	@Override
	public String getText() {
		if (Wf.getInstance().getProp().getMailLinkTarefa() == null){
			logger.log(Level.WARNING,"A propriedade mail.link.tarefa não está definida! Os e-mails enviados para o usuário não conterão o link correto para a tarefa");
		}
		if (Wf.getInstance().getProp().getMailLinkTarefa() == null){
			logger.log(Level.WARNING,"A propriedade mail.link.documento não está definida! Os e-mails enviados para o usuário não conterão o link correto para o documento");
		}
		
		String rodape = "\n\n----------\n";
		if (tiId != null) {
			rodape += "Link para a tarefa no SIGA-WF: " + Wf.getInstance().getProp().getMailLinkTarefa()
					+ this.tiId + "\n";
		}

		if (docAccessVariables != null) {
			for (VariableAccess v : docAccessVariables) {
				if (v.getMappedName().startsWith("doc_") && !v.isWritable()
						&& v.isReadable()
						&& docVariables.get(v.getMappedName()) != null) {
					
					rodape += "Link para o "
							+ v.getVariableName()
							+ " no SIGA: " + Wf.getInstance().getProp().getMailLinkDocumento()
							+ docVariables.get(v.getMappedName()) + "\n";
				}

			}
		}
		rodape += "Mensagem gerada automaticamente pelo sistema SIGA.";
		return super.getText() + rodape;
	}


	/**
	 * Ao iniciar a manipulação do e-mail, esse método captura as informações
	 * sobre os documentos do SIGA-DOC que se referem à tarefa que enviou o
	 * e-mail.
	 */
	@Override
	public void execute(ExecutionContext executionContext) {
		if (executionContext.getTaskInstance() != null) {
			tiId = executionContext.getTaskInstance().getId();
			TaskController tc = executionContext.getTaskInstance().getTask()
					.getTaskController();
			if (tc != null) {
				this.docAccessVariables = executionContext.getTaskInstance()
						.getTask().getTaskController().getVariableAccesses();

				this.docVariables = executionContext.getTaskInstance()
						.getContextInstance().getVariables();
			}

		}
		super.execute(executionContext);
	}

	/**
	 * Envia o e-mail.
	 */
	@Override
	public void send() {
		// Para debugar o workflow, o ideal e desabilitar os emails e apenas
		// logá-los no System.out.
//		super.getBccRecipients().add("xxx@xxx.com.br");
		super.send();

		logger.log(Level.INFO,"Email enviado para " + super.getRecipients() + "[" + super.getSubject() + "]");
		logger.log(Level.FINE,"Detalhes do e-mail enviado:"
					+ "\nAssunto: " + getSubject()
					+ "\nDe: " + getFromAddress()
					+ "\nPara: " + getRecipients()
					+ "\nTexto: " + getText());

	}

}
