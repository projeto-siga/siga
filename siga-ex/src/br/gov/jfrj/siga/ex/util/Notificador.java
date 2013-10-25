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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jfree.util.Log;

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

	// private static String servidor = "siga"; // produção

	// private static String servidor = "http://"
	// + SigaBaseProperties.getString(SigaBaseProperties
	// .getString("ambiente") + ".servidor.principal");

	private static String servidor = SigaBaseProperties.getString("siga.ex."
					+ SigaBaseProperties.getString("ambiente") + ".url");

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
		
		if(mov.getExMobil().isApensadoAVolumeDoMesmoProcesso())
			return;

		StringBuilder conteudo = new StringBuilder(); // armazena o corpo do
		// e-mail
		StringBuilder conteudoHTML = new StringBuilder(); // armazena o corpo
		// do e-mail no formato HTML

		// lista de destinatários com informações adicionais de perfil e código
		// da movimentação que adicionou o perfil para permitir o cancelamento.
		HashSet<Destinatario> destinatariosEmail = new HashSet<Destinatario>();

		List<Notificacao> notificacoes = new ArrayList<Notificacao>();

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
					&& m.getExTipoMovimentacao()
							.getIdTpMov()
							.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL)) {

				incluirDestinatarioPerfil(mov, destinatariosEmail, m);

			}
		}

		for (Destinatario dest : destinatariosEmail) {
			prepararTextoPapeisVinculados(dest, conteudo, conteudoHTML, mov,
					tipoNotificacao);

			String html = conteudoHTML.toString();
			String txt = conteudo.toString();

			conteudoHTML.delete(0, conteudoHTML.length());
			conteudo.delete(0, conteudo.length());

			Notificacao not = new Notificacao(dest, html, txt);
			notificacoes.add(not);
		}

		notificarPorEmail(notificacoes);

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
			HashSet<Destinatario> destinatariosEmail, ExMovimentacao m)
			throws AplicacaoException {

		try {

			if (m.getSubscritor() != null) {
				if (Ex.getInstance()
						.getConf()
						.podePorConfiguracao(
								mov.getExDocumento().getExFormaDocumento()
										.getExTipoFormaDoc(),
								m.getExPapel(),
								m.getSubscritor().getPessoaAtual(),
								mov.getExTipoMovimentacao(),
								CpTipoConfiguracao.TIPO_CONFIG_NOTIFICAR_POR_EMAIL)

						/*
						 * Se a movimentação é um cancelamento de uma
						 * movimentação que pode ser notificada, adiciona o
						 * e-mail.
						 */
						|| (mov.getExMovimentacaoRef() != null && Ex
								.getInstance()
								.getConf()
								.podePorConfiguracao(
										mov.getExDocumento()
												.getExFormaDocumento()
												.getExTipoFormaDoc(),
										m.getExPapel(),
										m.getSubscritor().getPessoaAtual(),
										mov.getExMovimentacaoRef()
												.getExTipoMovimentacao(),
										CpTipoConfiguracao.TIPO_CONFIG_NOTIFICAR_POR_EMAIL))) {
					destinatariosEmail.add(new Destinatario(m.getSubscritor()
							.getEmailPessoaAtual(), m
							.getSubscritor().getPessoaAtual().getSigla(), m
							.getExPapel().getDescPapel(), m.getIdMov()));
				}
			} else {
				if (m.getLotaSubscritor() != null) {
					if (Ex.getInstance()
							.getConf()
							.podePorConfiguracao(
									mov.getExDocumento().getExFormaDocumento()
											.getExTipoFormaDoc(),
									m.getExPapel(),
									m.getLotaSubscritor().getLotacaoAtual(),
									mov.getExTipoMovimentacao(),
									CpTipoConfiguracao.TIPO_CONFIG_NOTIFICAR_POR_EMAIL)

							/*
							 * Se a movimentação é um cancelamento de uma
							 * movimentação que pode ser notificada, adiciona o
							 * e-mail.
							 */
							|| (mov.getExMovimentacaoRef() != null && Ex
									.getInstance()
									.getConf()
									.podePorConfiguracao(
											mov.getExDocumento()
													.getExFormaDocumento()
													.getExTipoFormaDoc(),
											m.getExPapel(),
											m.getLotaSubscritor()
													.getLotacaoAtual(),
											mov.getExMovimentacaoRef()
													.getExTipoMovimentacao(),
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
			HashSet<Destinatario> destinatariosEmail, ExMovimentacao m)
			throws AplicacaoException, Exception {
		List<String> listaDeEmails = dao().consultarEmailNotificacao(
				m.getLotaSubscritor().getLotacaoAtual());

		String sigla = m.getLotaSubscritor().getLotacaoAtual().getSigla();

		if (listaDeEmails.size() > 0) {

			for (String email : listaDeEmails) {

				// Caso exista alguma configuração com email
				// nulo, significa que deve ser enviado para
				// todos da lotação

				if (email == null) {
					for (DpPessoa pes : dao().pessoasPorLotacao(
							m.getLotaSubscritor().getLotacaoAtual()
									.getIdLotacao(), false, true)) {

						if (Ex.getInstance()
								.getConf()
								.podePorConfiguracao(
										mov.getExDocumento()
												.getExFormaDocumento()
												.getExTipoFormaDoc(),
										m.getExPapel(),
										pes,
										mov.getExTipoMovimentacao(),
										CpTipoConfiguracao.TIPO_CONFIG_NOTIFICAR_POR_EMAIL))
							destinatariosEmail
									.add(new Destinatario(pes
											.getEmailPessoaAtual(), sigla, m
											.getExPapel().getDescPapel(), m
											.getIdMov()));
					}
				} else {
					destinatariosEmail.add(new Destinatario(email, sigla, m
							.getExPapel().getDescPapel(), m.getIdMov()));
				}
			}
		} else {
			for (DpPessoa pes : dao().pessoasPorLotacao(
					m.getLotaSubscritor().getLotacaoAtual().getIdLotacao(),
					false, true)) {
				if (Ex.getInstance()
						.getConf()
						.podePorConfiguracao(
								mov.getExDocumento().getExFormaDocumento()
										.getExTipoFormaDoc(),
								m.getExPapel(),
								pes,
								mov.getExTipoMovimentacao(),
								CpTipoConfiguracao.TIPO_CONFIG_NOTIFICAR_POR_EMAIL))
					destinatariosEmail.add(new Destinatario(pes
							.getEmailPessoaAtual(), sigla, m
							.getExPapel().getDescPapel(), m.getIdMov()));
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
	private static void notificarPorEmail(List<Notificacao> notificacoes) {
		// Se existirem destinatários, envia o e-mail. O e-mail é enviado
		// assincronamente.
		if (notificacoes.size() > 0) {
			CorreioThread t = new CorreioThread(notificacoes);
			t.start();
		}
	}

	/**
	 * Monta o corpo do e-mail que será recebido pelas pessoas com perfis
	 * vinculados.
	 * 
	 * @param dest
	 * 
	 * @param conteudo
	 * @param conteudoHTML
	 * @param mov
	 * @param tipoNotificacao
	 *            - Se é uma gravação, cancelamento ou exclusão de movimentação.
	 */
	private static void prepararTextoPapeisVinculados(Destinatario dest,
			StringBuilder conteudo, StringBuilder conteudoHTML,
			ExMovimentacao mov, int tipoNotificacao) {

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
		conteudo.append(servidor
				+ "/expediente/doc/exibir.action?sigla=");
		conteudo.append(mov.getExDocumento().getSigla());

		conteudo.append("\n\nEste email foi enviado porque ");
		conteudo.append(dest.sigla);
		conteudo.append(" tem o perfil de '");
		conteudo.append(dest.papel);
		conteudo.append("' no documento ");
		conteudo.append(mov.getExDocumento().getSigla());
		conteudo.append(". Caso não deseje mais receber notificações desse documento, clique no link abaixo para se descadastrar:\n\n");
		conteudo.append(servidor + "/expediente/mov/cancelar.action?id=");
		conteudo.append(dest.idMovPapel);

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
		conteudoHTML.append(servidor
				+ "/expediente/doc/exibir.action?sigla=");
		conteudoHTML.append(mov.getExDocumento().getSigla());
		conteudoHTML.append("\">aqui</a>.</p>");

		conteudoHTML.append("<p>Este email foi enviado porque ");
		conteudoHTML.append(dest.sigla);
		conteudoHTML.append(" tem o perfil de '");
		conteudoHTML.append(dest.papel);
		conteudoHTML.append("' no documento ");
		conteudoHTML.append(mov.getExDocumento().getSigla());
		conteudoHTML
				.append(". Caso não deseje mais receber notificações desse documento, clique <a href=\"");
		conteudoHTML.append(servidor
				+ "/expediente/mov/cancelar.action?id=");
		conteudoHTML.append(dest.idMovPapel);
		conteudoHTML.append("\">aqui</a> para descadastrar.</p>");

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

		private List<Notificacao> notificacoes;

		public CorreioThread(List<Notificacao> notificacoes) {
			super();
			this.notificacoes = notificacoes;
		}

		@Override
		public void run() {
			try {
				for (Notificacao not : notificacoes)
					Correio.enviar(
							SigaBaseProperties
									.getString("servidor.smtp.usuario.remetente"),
							new String[] { not.dest.email },
							"Notificação Automática - Movimentação de Documento",
							not.txt, not.html);
			} catch (Exception e) {
				Log.error(e);
			}
		}

	}

	static class Notificacao {
		Destinatario dest;
		String html;
		String txt;

		public Notificacao(Destinatario dest, String html, String txt) {
			super();
			this.dest = dest;
			this.html = html;
			this.txt = txt;
		}
	}

	static class Destinatario implements Comparable<Destinatario> {

		public String email;
		public String sigla;
		public String papel;
		public long idMovPapel;

		public Destinatario(String email, String sigla, String papel,
				long idMovPapel) {
			super();
			this.email = email;
			this.sigla = sigla;
			this.papel = papel;
			this.idMovPapel = idMovPapel;
		}

		public int compareTo(Destinatario o) {
			// TODO Auto-generated method stub
			return email.compareTo(o.email);
		}

	}

}
