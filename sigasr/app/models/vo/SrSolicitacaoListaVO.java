package models.vo;

import java.util.ArrayList;
import java.util.List;

import models.SrLista;
import models.SrPrioridadeSolicitacao;
import models.SrSolicitacao;
import util.JsonUtil;
import util.SigaPlayUtil;
import util.SrSolicitacaoFiltro;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SrSolicitacaoListaVO {
	
	private static Long LARGURA_COLUNA_CODIGO = 130L;
	private static Long LARGURA_COLUNA_REMOVER_PRIORIZAR = 55L;
	private static Long LARGURA_COLUNA_PRIORIDADE = 20L;

	public boolean podeOrdenar;
	public boolean podePriorizar;
	public boolean podeRemover;
	public boolean podeFiltrar;
	public boolean podePaginar;
	public List<SrSolicitacaoVO> itens;
	public List<ColunasVO> colunas;
	public List<ColunasVO> colunasDetalhamento;
	public String colunasTabelaJson;
	public String colunasDetalhamentoJson;
	
	public SrSolicitacaoListaVO() {
		this.colunas = new ArrayList<ColunasVO>();
		this.colunasDetalhamento = new ArrayList<ColunasVO>();
		this.itens = new ArrayList<SrSolicitacaoVO>();
	}
	
	public static SrSolicitacaoListaVO fromFiltro(SrSolicitacaoFiltro filtro, boolean telaDeListas, String nome, 
			boolean isPopup, DpLotacao lotaTitular, DpPessoa cadastrante) throws Exception {
		List<SrSolicitacao> solicitacoes = filtro.buscar();
		SrSolicitacaoListaVO solicitacoesVO = new SrSolicitacaoListaVO();
		SrLista lista = null;
		
		if (filtro.idListaPrioridade != null)
			lista = SrLista.findById(filtro.idListaPrioridade);
		
		if (telaDeListas && lista != null) {
			solicitacoesVO.podePriorizar = lista.podePriorizar(lotaTitular, cadastrante);
			solicitacoesVO.podeOrdenar = false;
			solicitacoesVO.podeRemover = lista.podeRemover(lotaTitular, cadastrante);
			solicitacoesVO.podeFiltrar = false;
			solicitacoesVO.podePaginar = false;
		}
		else {
			solicitacoesVO.podeOrdenar = true;
			solicitacoesVO.podeFiltrar = true;
			solicitacoesVO.podePaginar = true;
		}
		
		solicitacoesVO.colunas = solicitacoesVO.gerarColunasSolicitacao(telaDeListas, solicitacoesVO.podeRemover, solicitacoesVO.podePriorizar);
		solicitacoesVO.colunasDetalhamento = solicitacoesVO.gerarColunasDetalhamentoSolicitacao(telaDeListas);
		solicitacoesVO.colunasTabelaJson = createColunasTabelaJson(solicitacoesVO.colunas, telaDeListas);
		solicitacoesVO.colunasDetalhamentoJson = createColunasDetalhamentoJson(solicitacoesVO.colunasDetalhamento);
		
		for (SrSolicitacao sol : solicitacoes) {
			
			if (lista != null) {
				SrPrioridadeSolicitacao prioridade = lista.getSrPrioridadeSolicitacao(sol);
				
				// só adiciona caso exista o vínculo entre a lista e a Solicitação
				if (prioridade != null)
					solicitacoesVO.itens.add(new SrSolicitacaoVO(sol, lista, prioridade, nome, isPopup, lotaTitular, 
							cadastrante, solicitacoesVO.podeRemover, solicitacoesVO.podePriorizar));
			}
			else
				solicitacoesVO.itens.add(new SrSolicitacaoVO(sol, nome, isPopup, lotaTitular, cadastrante));
		}
		
		if (lista != null) 
			SrSolicitacaoVO.ordenarPorPrioridade(solicitacoesVO.itens);
		
		return solicitacoesVO;
	}
	
	public List<ColunasVO> gerarColunasSolicitacao(boolean telaDeListas, boolean podeRemover, boolean podePriorizar) {
		List<ColunasVO> colunasVO = new ArrayList<ColunasVO>();
		
		if (telaDeListas) {
			colunasVO.add(new ColunasVO("#", "prioridadeListaFormatada", "gt-celula-nowrap solicitacao-dados solicitacao-prioridade numero-solicitacao", LARGURA_COLUNA_PRIORIDADE));
			colunasVO.addAll(getColunasEmComum());
			colunasVO.add(new ColunasVO("Lotação", "lotaAtendenteFormatada", "gt-celula-nowrap solicitacao-dados"));
			colunasVO.add(new ColunasVO("Última Movimentação", "ultimaMovimentacaoformatada", "gt-celula-nowrap solicitacao-dados"));
			
			if (podeRemover || podePriorizar)
				colunasVO.add(new ColunasVO("", "botaoRemoverPriorizar", "gt-celula-nowrap solicitacao-dados solicitacao-remover", LARGURA_COLUNA_REMOVER_PRIORIZAR));
		}
		else {
			colunasVO.add(new ColunasVO(SigaPlayUtil.botaoExpandir(), "botaoExpandir", "hide-sort-arrow bt-expandir-tabela gt-celula-nowrap details-control"));
			colunasVO.addAll(getColunasEmComum());
			colunasVO.add(new ColunasVO("Situação", "marcadoresEmHtml", "gt-celula-nowrap solicitacao-dados"));
			colunasVO.add(new ColunasVO("Último Andamento", "ultimaMovimentacaoformatada", "gt-celula-nowrap solicitacao-dados"));
			colunasVO.add(new ColunasVO("Prioridade", "prioridadeFormatada", "gt-celula-nowrap solicitacao-dados"));
		}
		
		return colunasVO;		
	}
	
	private List<ColunasVO> getColunasEmComum() {
		List<ColunasVO> colunasVO = new ArrayList<ColunasVO>();
		colunasVO.add(new ColunasVO("Código", "codigoFormatado", "gt-celula-nowrap solicitacao-codigo", LARGURA_COLUNA_CODIGO));
		colunasVO.add(new ColunasVO("Teor", "teorFormatado", "gt-celula-nowrap solicitacao-dados"));
		colunasVO.add(new ColunasVO("Solicitante", "solicitanteFormatado", "gt-celula-nowrap solicitacao-dados"));
		colunasVO.add(new ColunasVO("Aberto", "dtRegString", "gt-celula-nowrap solicitacao-dados"));
		
		return colunasVO;
	}
	
	public List<ColunasVO> gerarColunasDetalhamentoSolicitacao(boolean telaDeListas) {
		List<ColunasVO> colunasDetalhamento = new ArrayList<ColunasVO>();
		
		colunasDetalhamento.add(new ColunasVO("Teor", "teorFormatado"));
		colunasDetalhamento.add(new ColunasVO("Solicitante", "solicitanteFormatado"));
		colunasDetalhamento.add(new ColunasVO("Prioridade", "prioridadeFormatada"));
		colunasDetalhamento.add(new ColunasVO("Situação", "marcadoresEmHtmlDetalhes"));
		colunasDetalhamento.add(new ColunasVO("Última Movimentação", "ultimaMovimentacaoformatada"));
		
		return colunasDetalhamento;
	}
	
	public String toJson() {
		return JsonUtil.toJson(this).toString();
	}
	
	private static String createColunasTabelaJson(List<ColunasVO> colunas, boolean telaDeListas) {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		List<ColunasVO> colunasResult = null;
		Gson gson = builder.create();
		
		// remove a primeira coluna, que será sempre o detalhamento ou posição na lista		
		if (colunas.size() > 0) {
			if (telaDeListas)
				colunasResult = colunas.subList(1,  colunas.size() -1);
			else
				colunasResult = colunas.subList(1,  colunas.size());
		}
		
		return gson.toJson(colunasResult);
	}
	
	private static String createColunasDetalhamentoJson(List<ColunasVO> colunasDetalhamento) {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		Gson gson = builder.create();
		
		return gson.toJson(colunasDetalhamento);
	}
	
}
