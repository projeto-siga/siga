package br.gov.jfrj.siga.api.v1;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.INotificacoesGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Notificacao;
import br.gov.jfrj.siga.cp.bl.Cp;

public class NotificacoesGet implements INotificacoesGet {
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		/* Cria Notificação de Pendência definição PIN para Pessoa Autenticada */
		if (ctx.getIdentidadeCadastrante() != null && ctx.getIdentidadeCadastrante()
				.getPinIdentidade() == null) { /* TODO: Levar verificação para camada de Negócio */
			if (Cp.getInstance().getComp().podeSegundoFatorPin(ctx.getCadastrante(), ctx.getLotaCadastrante())) {
				resp.list.add(criaNotificacaoPendenciaDefinicaoPIN());
			}
		}
	}

	private Notificacao criaNotificacaoPendenciaDefinicaoPIN() {
		Notificacao notificacao = new Notificacao();

		notificacao.idNotificacao = "1";
		notificacao.titulo = "Definição de PIN";
		notificacao.icone = "fa fa-bell";
		notificacao.resumo = "Você ainda não definiu um PIN";

		notificacao.conteudo = "Você ainda não definiu um <strong>PIN</strong> "
				+ "para Assinatura e Autenticação de Documentos com Senha via PIN. <br /><br />"
				+ "Clique <strong><a href='/siga/app/pin/cadastro'>aqui</a></strong> saber mais e definir a chave.";
		notificacao.sempreMostrar = true;
		return notificacao;

	}

	@Override
	public String getContext() {
		return "obter notificações";
	}
}
