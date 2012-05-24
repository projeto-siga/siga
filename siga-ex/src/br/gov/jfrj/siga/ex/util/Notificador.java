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
package br.gov.jfrj.siga.ex.util;

import java.util.HashSet;
import java.util.List;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;

public class Notificador {

	public static int TIPO_NOTIFICACAO_GRAVACAO = 1;
	public static int TIPO_NOTIFICACAO_CANCELAMENTO = 2;
	public static int TIPO_NOTIFICACAO_EXCLUSAO = 3;

	// private static String servidor = "localhost:8080"; // teste

	private static String servidor = "siga"; // produção
	
	
	/**
	 * Método que notifica as pessoas com perfis vinculados ao documento.
	 * 
	 * @param mov
	 * @param tipoNotificacao
	 *            - Se é uma gravação, cancelamento ou exclusão de movimentação.
	 * @throws AplicacaoException
	 */
	public static void notificarPerfisVinculados(ExMovimentacao mov,
			int tipoNotificacao) throws AplicacaoException {

		StringBuilder conteudo = new StringBuilder(); // armazena o corpo do
		// e-mail
		StringBuilder conteudoHTML = new StringBuilder(); // armazena o corpo
		// do e-mail no formato HTML

		prepararTextoPapeisVinculados(conteudo, conteudoHTML, mov,
				tipoNotificacao);

		HashSet<String> destinatariosEmail = new HashSet<String>(); // lista de
		// destinatários. É um HashSet para não haver duplicidade.

		/*
		 * Para cada movimentação do mobil geral (onde fica as movimentações
		 * vinculações de perfis) verifica se: 1) A movimentação não está
		 * cancelada; 2) Se a movimentação é uma vinculação de perfil; 3) Se há
		 * uma configuração permitindo a notificação por e-mail.
		 * 
		 * Caso TODAS as condições acima sejam verdadeiras, adiciona o e-mail à
		 * lista de destinatários.
		 */
		for (ExMovimentacao m : mov.getExDocumento().getMobilGeral()
				.getExMovimentacaoSet()) {
			if (!m.isCancelada()
					&& m
							.getExTipoMovimentacao()
							.getIdTpMov()
							.equals(
									ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL)) {

				incluirDestinatarioPerfil(mov, destinatariosEmail, m);

			}
		}

		notificarPorEmail(conteudo, conteudoHTML, destinatariosEmail);

	}

	/**
	 * Inclui destinatários na lista baseando-se nas configurações existentes na
	 * tabela EX_CONFIGURACAO
	 * 
	 * @param mov
	 * @param destinatariosEmail
	 * @param m
	 * @throws AplicacaoException
	 */
	private static ExDao dao() {
		return ExDao.getInstance();
	}
	private static void incluirDestinatarioPerfil(ExMovimentacao mov,
			HashSet<String> destinatariosEmail, ExMovimentacao m)
			throws AplicacaoException {
		
		try {
			
			if (m.getSubscritor() != null) {
				if (Ex.getInstance().getConf().podePorConfiguracao(
						mov.getExDocumento().getExFormaDocumento().getExTipoFormaDoc(), 
						m.getExPapel(), m.getSubscritor().getPessoaAtual(),
						mov.getExTipoMovimentacao(),
						CpTipoConfiguracao.TIPO_CONFIG_NOTIFICAR_POR_EMAIL)
						
					/*
					 * Se a movimentação é um cancelamento de uma movimentação que
					 * pode ser notificada, adiciona o e-mail.
					 */	
				    || (mov.getExMovimentacaoRef() != null
							&& Ex.getInstance().getConf().podePorConfiguracao(mov.getExDocumento()
											                                     .getExFormaDocumento()
											                                     .getExTipoFormaDoc(),
							                                                  m.getExPapel(),
							                                                  m.getSubscritor().getPessoaAtual(),
									                                          mov.getExMovimentacaoRef().getExTipoMovimentacao(),
									                                          CpTipoConfiguracao.TIPO_CONFIG_NOTIFICAR_POR_EMAIL))) {
					    destinatariosEmail.add(m.getSubscritor().getPessoaAtual().getEmailPessoa());
					  //  destinatariosEmail.add(m.getSubscritor().getEmailPessoa());
				 }								
			} else {
				if (m.getLotaSubscritor() != null) {
					if (Ex.getInstance().getConf().podePorConfiguracao(
							mov.getExDocumento().getExFormaDocumento().getExTipoFormaDoc(), 
							m.getExPapel(), m.getLotaSubscritor().getLotacaoAtual() ,
							mov.getExTipoMovimentacao(),
							CpTipoConfiguracao.TIPO_CONFIG_NOTIFICAR_POR_EMAIL)
							
					/*
					 * Se a movimentação é um cancelamento de uma movimentação que
					 * pode ser notificada, adiciona o e-mail.
					 */	
				    || (mov.getExMovimentacaoRef() != null
				    		&& Ex.getInstance().getConf().podePorConfiguracao(mov.getExDocumento()
                                                                                 .getExFormaDocumento()
                                                                                 .getExTipoFormaDoc(),
                                                                              m.getExPapel(),
                                                                              m.getLotaSubscritor().getLotacaoAtual(),
                                                                              mov.getExMovimentacaoRef().getExTipoMovimentacao(),
                                                                              CpTipoConfiguracao.TIPO_CONFIG_NOTIFICAR_POR_EMAIL))) {						
						adicionarDestinatariosEmail(mov, destinatariosEmail, m);
					}
					
				}
					
			}		
			
		} catch (Exception e) {
			throw new AplicacaoException(
					"Erro ao enviar email de notificação de movimentação.", 0,
					e);
		}
	}

	private static void adicionarDestinatariosEmail(ExMovimentacao mov,
			HashSet<String> destinatariosEmail, ExMovimentacao m)
			throws AplicacaoException, Exception {
		List<String> listaDeEmails =  dao().consultarEmailNotificacao(
				m.getLotaSubscritor().getLotacaoAtual());
		
		if (listaDeEmails.size() > 0) {

			for (String email : listaDeEmails) {

				// Caso exista alguma configuração com email
				// nulo, significa que deve ser enviado para
				// todos da lotação

				if (email == null) {
					for (DpPessoa pes : dao().pessoasPorLotacao(m.getLotaSubscritor().getLotacaoAtual().getIdLotacao(), false)) {
						
						if (Ex.getInstance().getConf().podePorConfiguracao(
								mov.getExDocumento().getExFormaDocumento()
										.getExTipoFormaDoc(), m.getExPapel(), pes,
								mov.getExTipoMovimentacao(),
								CpTipoConfiguracao.TIPO_CONFIG_NOTIFICAR_POR_EMAIL)) 				
						destinatariosEmail.add(pes.getEmailPessoa());
					}
				} else {									
					destinatariosEmail.add(email);
				}
			}
		} else {
			for (DpPessoa pes : dao().pessoasPorLotacao(m.getLotaSubscritor().getLotacaoAtual().getIdLotacao(), false)) {
				if (Ex.getInstance().getConf().podePorConfiguracao(
						mov.getExDocumento().getExFormaDocumento()
								.getExTipoFormaDoc(), m.getExPapel(), pes,
						mov.getExTipoMovimentacao(),
						CpTipoConfiguracao.TIPO_CONFIG_NOTIFICAR_POR_EMAIL)) 
				destinatariosEmail.add(pes.getEmailPessoa());
			}
			
		}
	}

	/**
	 * Notifica os destinatários assincronamente.
	 * 
	 * @param conteudo
	 *            - texto do e-mail
	 * @param conteudoHTML
	 *            - texto do e-mail no formato HTML
	 * @param destinatariosEmail
	 *            - Conjunto de destinatários.
	 */
	private static void notificarPorEmail(StringBuilder conteudo,
			StringBuilder conteudoHTML, HashSet<String> destinatariosEmail) {
		// Se existirem destinatários, envia o e-mail. O e-mail é enviado
		// assincronamente.
		if (destinatariosEmail.size() > 0) {
			CorreioThread t = new CorreioThread();

			t.setDestinatariosEmail(destinatariosEmail);
			t.setConteudo(conteudo);
			t.setConteudoHTML(conteudoHTML);

			t.start();
		}
	}

	/**
	 * Monta o corpo do e-mail que será recebido pelas pessoas com perfis
	 * vinculados.
	 * 
	 * @param conteudo
	 * @param conteudoHTML
	 * @param mov
	 * @param tipoNotificacao
	 *            - Se é uma gravação, cancelamento ou exclusão de movimentação.
	 */
	private static void prepararTextoPapeisVinculados(StringBuilder conteudo,
			StringBuilder conteudoHTML, ExMovimentacao mov, int tipoNotificacao) {

		// conteúdo texto
		conteudo.append("O documento ");
		conteudo.append(mov.getExMobil().getSigla());
		conteudo.append(", com descrição '");
		conteudo.append(mov.getExDocumento().getDescrDocumento());

		if (tipoNotificacao == TIPO_NOTIFICACAO_GRAVACAO) {

			conteudo.append("', recebeu a movimentação '"
					+ mov.getExTipoMovimentacao().getDescricao() + "'. ");
		}
		if (tipoNotificacao == TIPO_NOTIFICACAO_CANCELAMENTO) {

			conteudo.append("', recebeu a movimentação '"
					+ mov.getExMovimentacaoRef().getDescrTipoMovimentacao()
					+ "'. ");
		}
		if (tipoNotificacao == TIPO_NOTIFICACAO_EXCLUSAO) {

			conteudo.append("',  recebeu movimentação de exclusão de '"
					+ mov.getExTipoMovimentacao().getDescricao() + "'. ");
		}

		if (mov.getCadastrante() != null) {
			conteudo.append("A movimentação foi realizada por '"
					+ mov.getCadastrante().getNomePessoa() + " (Matrícula: "
					+ mov.getCadastrante().getMatricula() + ")'.\n\n");

		}
		conteudo.append("Para visualizar o documento, ");
		conteudo.append("clique no link abaixo:\n\n");
		conteudo.append("http://" + servidor
				+ "/sigaex/expediente/doc/exibir.action?sigla=");
		conteudo.append(mov.getExDocumento().getSigla());

		// conteúdo html
		conteudoHTML.append("<html><body>");

		conteudoHTML.append("<p>O documento <b>");
		conteudoHTML.append(mov.getExMobil().getSigla());
		conteudoHTML.append("</b>, com descrição '<b>");
		conteudoHTML.append(mov.getExDocumento().getDescrDocumento());

		if (tipoNotificacao == TIPO_NOTIFICACAO_GRAVACAO) {
			conteudoHTML.append("</b>', recebeu a movimentação <b>"
					+ mov.getExTipoMovimentacao().getDescricao() + "</b>.");
		}
		if (tipoNotificacao == TIPO_NOTIFICACAO_CANCELAMENTO) {
			conteudoHTML.append("</b>', recebeu a movimentação <b>"
					+ mov.getExMovimentacaoRef().getDescrTipoMovimentacao()
					+ "</b>.");
		}
		if (tipoNotificacao == TIPO_NOTIFICACAO_EXCLUSAO) {
			conteudoHTML
					.append("</b>', recebeu movimentação de exclusão de <b>"
							+ mov.getExTipoMovimentacao().getDescricao()
							+ "</b>.");
		}

		if (mov.getCadastrante() != null) {
			conteudoHTML.append("<br/>A movimentação foi realizada por <b>"
					+ mov.getCadastrante().getNomePessoa() + " (Matrícula: "
					+ mov.getCadastrante().getMatricula() + ")</b></p>");
		}

		if (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA
				|| mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA) {
			if (mov.getResp() != null) {
				conteudoHTML
						.append("<br/>O destinatário da transferência é <b>"
								+ mov.getResp().getNomePessoa()
								+ " (Matrícula: "
								+ mov.getResp().getMatricula() + ") do (a) "
								+ mov.getLotaResp().getNomeLotacao()
								+ " (sigla: " + mov.getLotaResp().getSigla()
								+ ")" + "</b></p>");
			} else {
				conteudoHTML
						.append("<br/>A lotação de destino da transferência é <b>"
								+ mov.getLotaResp().getNomeLotacao()
								+ " (sigla: "
								+ mov.getLotaResp().getSigla()
								+ ")" + "</b></p>");

			}
		}

		conteudoHTML.append("<p>Para visualizar o documento, ");
		conteudoHTML.append("clique <a href=\"");
		conteudoHTML.append("http://" + servidor
				+ "/sigaex/expediente/doc/exibir.action?sigla=");
		conteudoHTML.append(mov.getExDocumento().getSigla());
		conteudoHTML.append("\">aqui</a>.</p></body></html>");
		conteudoHTML.append("</body></html>");

	}

	/**
	 * Classe que representa um thread de envio de e-mail. Há a necessidade do
	 * envio de e-mail ser assíncrono, caso contrário, o usuário sentirá uma
	 * degradação de performance.
	 * 
	 * @author kpf
	 * 
	 */
	static class CorreioThread extends Thread {

		private HashSet<String> destinatariosEmail;
		private StringBuilder conteudo, conteudoHTML;

		@Override
		public void run() {
			try {
				Correio
						.enviar(
								SigaBaseProperties.getString("servidor.smtp.usuario.remetente"),
								destinatariosEmail
										.toArray(new String[destinatariosEmail
												.size()]),
								"Notificação Automática - Movimentação de Documento",
								conteudo.toString(), conteudoHTML.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public HashSet<String> getDestinatariosEmail() {
			return destinatariosEmail;
		}

		public void setDestinatariosEmail(HashSet<String> destinatariosEmail) {
			this.destinatariosEmail = destinatariosEmail;
		}

		public StringBuilder getConteudo() {
			return conteudo;
		}

		public void setConteudo(StringBuilder conteudo) {
			this.conteudo = conteudo;
		}

		public StringBuilder getConteudoHTML() {
			return conteudoHTML;
		}

		public void setConteudoHTML(StringBuilder conteudoHTML) {
			this.conteudoHTML = conteudoHTML;
		}

	}

}
