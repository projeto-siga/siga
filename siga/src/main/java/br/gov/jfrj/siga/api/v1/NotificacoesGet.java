package br.gov.jfrj.siga.api.v1;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.INotificacoesGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Notificacao;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.CpArquivoTipoArmazenamentoEnum;
import br.gov.jfrj.siga.cp.arquivo.ArmazenamentoHCP;
import br.gov.jfrj.siga.cp.bl.Cp;

public class NotificacoesGet implements INotificacoesGet {
	
	private Long totalCapacityBytes = 0L;
	private Long usedCapacityBytes  = 0L;
	private Integer softQuotaPercent  = 0;
	private String namespaceName = "Não identificado";
	
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		/* Cria Notificação de Pendência definição PIN para Pessoa Autenticada */
		if (ctx.getIdentidadeCadastrante() != null && ctx.getIdentidadeCadastrante().getPinIdentidade() == null) { /* TODO: Levar verificação para camada de Negócio */
			if (Cp.getInstance().getComp().podeSegundoFatorPin(ctx.getCadastrante(), ctx.getLotaCadastrante())) {
				resp.list.add(criaNotificacaoPendenciaDefinicaoPIN());
			}
			
			
		}
		
		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(ctx.getTitular(),ctx.getLotaTitular(),"SIGA;FE;ARMAZ;ARMAZ_ESTAT")) {
			Notificacao notificacaoArmazenamento = criaNotificacaoArmazenanentoCritico();
			if (notificacaoArmazenamento != null)
				resp.list.add(notificacaoArmazenamento);
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
	
	private Notificacao criaNotificacaoArmazenanentoCritico() throws JSONException {
		
		JSONObject jsonEstatistica = new JSONObject();
		double percentualUsed = 0;
		
		if (CpArquivoTipoArmazenamentoEnum.HCP.equals(CpArquivoTipoArmazenamentoEnum.valueOf(Prop.get("/siga.armazenamento.arquivo.tipo")))) {
			ArmazenamentoHCP a = ArmazenamentoHCP.getInstance();
			jsonEstatistica = a.estatistica().getJSONObject("statistics");
			convertHCPJsonToResult(jsonEstatistica);	
		}

		if(totalCapacityBytes > 0) {
			percentualUsed = ((double) usedCapacityBytes/totalCapacityBytes) * 100;  
		}
		
		if (percentualUsed > softQuotaPercent) {
			Notificacao notificacao = new Notificacao();
			DecimalFormat formato = new DecimalFormat ("#,##0.00", new DecimalFormatSymbols (new Locale ("en", "US"))); 
			
			notificacao.idNotificacao = "2";
			notificacao.titulo = "Armazenamento " + Prop.get("/siga.armazenamento.arquivo.tipo");
			notificacao.icone = "fas fa-hdd";
			notificacao.resumo = "O espaço para armazenamento de arquivos no " + Prop.get("/siga.armazenamento.arquivo.tipo") + " está se esgotando.";
	
			notificacao.conteudo = "O espaço para armazenamento de arquivos no <strong>" + Prop.get("/siga.armazenamento.arquivo.tipo") + "</strong> está se <strong>esgotando</strong>. <br /><br />"
					+ "	<div class='progress'>"
					+ "	  <div class='progress-bar progress-bar-striped progress-bar-animated bg-danger' role='progressbar' style='width: "+formato.format(percentualUsed)+"%' aria-valuenow='"+formato.format(percentualUsed)+"' aria-valuemin='0' aria-valuemax='100'>"
					+ "		 <b>"+formato.format(percentualUsed)+"%</b>"
					+ "	  </div>"
					+ " </div><br />"
					+ "<p class=\"text-center\">Clique <strong><a href='/siga/app/armazenamento/estatistica'>aqui</a></strong> para ir para painel.</p>";
			notificacao.sempreMostrar = false;
			
			return notificacao;
		} else
			return null;
		

	}
	
	private void convertHCPJsonToResult(JSONObject jsonEstatistica) throws JSONException {
		totalCapacityBytes = jsonEstatistica.getLong("totalCapacityBytes");
	    usedCapacityBytes = jsonEstatistica.getLong("usedCapacityBytes");
		softQuotaPercent = jsonEstatistica.getInt("softQuotaPercent");
		namespaceName = jsonEstatistica.getString("namespaceName");
	}

	@Override
	public String getContext() {
		return "obter notificações";
	}
}
