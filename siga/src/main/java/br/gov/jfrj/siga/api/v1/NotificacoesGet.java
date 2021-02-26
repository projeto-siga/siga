package br.gov.jfrj.siga.api.v1;



import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.INotificacoesGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Notificacao;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.NotificacoesGetRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.NotificacoesGetResponse;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.vraptor.SigaObjects;



public class NotificacoesGet implements INotificacoesGet {
	@Override
	public void run(NotificacoesGetRequest req, NotificacoesGetResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(false, true)) {
			resp.list = pesquisarNotificacoesPessoa(req, resp);	
		}
	}
	
	private List<Notificacao> pesquisarNotificacoesPessoa (NotificacoesGetRequest req, NotificacoesGetResponse resp) throws Exception {
		try {
			List<Notificacao> resultado = new ArrayList<>();
			
			SigaObjects so = ApiContext.getSigaObjects();
			CpIdentidade identidadeCadastrante = so.getIdentidadeCadastrante();
			DpPessoa cadastrante = so.getCadastrante();
			DpLotacao lotacao = cadastrante.getLotacao();
			
			/* Cria Notificação de Pendência definição PIN para Pessoa Autenticada*/
			if (identidadeCadastrante != null && identidadeCadastrante.getPinIdentidade() == null) {	/*TODO: Levar verificação para camada de Negócio*/			
				if (Cp.getInstance().getComp().podeSegundoFatorPin(cadastrante, lotacao) ) {
					resultado.add(criaNotificacaoPendenciaDefinicaoPIN());
				}
			};
			
			/* TODO: Busca Notificações cadastradas */
			
			return resultado;
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
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
