package models.vo;

import java.util.ArrayList;
import java.util.List;

import models.SrLista;
import models.SrSolicitacao;
import util.JsonUtil;
import util.SigaPlayUtil;
import util.SrSolicitacaoFiltro;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class SrSolicitacaoListaVO {
	
	private static Long LARGURA_COLUNA_CODIGO = 130L;
	private static Long LARGURA_COLUNA_REMOVER = 20L;
	private static Long LARGURA_COLUNA_PRIORIDADE = 20L;

	public boolean podeOrdenar;
	public boolean podePriorizar;
	public boolean podeRemover;	
	public List<SrSolicitacaoVO> itens;
	public List<ColunasVO> colunas;
	public List<ColunasVO> colunasDetalhamento;
	
	public SrSolicitacaoListaVO() {
		this.colunas = new ArrayList<ColunasVO>();
		this.itens = new ArrayList<SrSolicitacaoVO>();
	}
	
	public static SrSolicitacaoListaVO fromFiltro(SrSolicitacaoFiltro filtro, boolean telaDeListas, String nome, 
			boolean isPopup, DpLotacao lotaTitular, DpPessoa cadastrante) throws Exception {
		List<SrSolicitacao> solicitacoes = filtro.buscar();
		SrSolicitacaoListaVO solicitacoesVO = new SrSolicitacaoListaVO();
		SrLista lista = null;
		
		if (telaDeListas && filtro.idListaPrioridade != null) {
			lista = SrLista.findById(filtro.idListaPrioridade);
			solicitacoesVO.podePriorizar = lista.podePriorizar(lotaTitular, cadastrante);
			solicitacoesVO.podeOrdenar = false;
			solicitacoesVO.podeRemover = lista.podeRemover(lotaTitular, cadastrante);
		}
		else
			solicitacoesVO.podeOrdenar = true;
		
		solicitacoesVO.colunas = solicitacoesVO.gerarColunasSolicitacao(telaDeListas, solicitacoesVO.podeRemover);
		solicitacoesVO.colunasDetalhamento = solicitacoesVO.gerarColunasDetalhamentoSolicitacao(telaDeListas);
		
		for (SrSolicitacao sol : solicitacoes)
			solicitacoesVO.itens.add(new SrSolicitacaoVO(sol, lista, nome, isPopup, lotaTitular, cadastrante));
		
		return solicitacoesVO;
	}
	
	public List<ColunasVO> gerarColunasSolicitacao(boolean telaDeListas, boolean podeRemover) {
		List<ColunasVO> colunasVO = new ArrayList<ColunasVO>();
		
		if (telaDeListas) {
			colunasVO.add(new ColunasVO("#", "prioridadeLista", "gt-celula-nowrap", LARGURA_COLUNA_PRIORIDADE));
			colunasVO.add(new ColunasVO("Código", "codigoFormatado", "gt-celula-nowrap solicitacao-codigo", LARGURA_COLUNA_CODIGO));
			colunasVO.add(new ColunasVO("Teor", "teorFormatado", "gt-celula-nowrap solicitacao-dados"));
			colunasVO.add(new ColunasVO("Solicitante", "solicitanteFormatado", "gt-celula-nowrap solicitacao-dados"));
			colunasVO.add(new ColunasVO("Aberto", "dtUltimaMovimentacaoFormatada", "gt-celula-nowrap solicitacao-dados"));
			colunasVO.add(new ColunasVO("Última Movimentação", "ultimaMovimentacaoformatada", "gt-celula-nowrap solicitacao-dados"));
			colunasVO.add(new ColunasVO("Lotação", "lotaAtendenteFormatada", "gt-celula-nowrap solicitacao-dados"));
			
			if (podeRemover)
				colunasVO.add(new ColunasVO("", "botaoRemover", "gt-celula-nowrap solicitacao-dados solicitacao-remover", LARGURA_COLUNA_REMOVER));
		}
		else {
			colunasVO.add(new ColunasVO(SigaPlayUtil.botaoExpandir(), "botaoExpandir", "hide-sort-arrow bt-expandir-tabela gt-celula-nowrap details-control", false,  true, true));
			colunasVO.add(new ColunasVO("Código", "codigoFormatado", "gt-celula-nowrap solicitacao-codigo", LARGURA_COLUNA_CODIGO));
			colunasVO.add(new ColunasVO("Teor", "teorFormatado", "gt-celula-nowrap solicitacao-dados"));
			colunasVO.add(new ColunasVO("Solicitante", "solicitanteFormatado", "gt-celula-nowrap solicitacao-dados"));
			colunasVO.add(new ColunasVO("Aberto", "dtUltimaMovimentacaoFormatada", "gt-celula-nowrap solicitacao-dados"));
			colunasVO.add(new ColunasVO("Situação", "marcadoresEmHtml", "gt-celula-nowrap solicitacao-dados"));
			colunasVO.add(new ColunasVO("Último Andamento", "ultimaMovimentacaoformatada", "gt-celula-nowrap solicitacao-dados"));
			colunasVO.add(new ColunasVO("Prioridade", "prioridadeFormatada", "", false, false, true));
		}
		
		return colunasVO;		
	}
	
	public List<ColunasVO> gerarColunasDetalhamentoSolicitacao(boolean telaDeListas) {
		List<ColunasVO> colunasDetalhamento = new ArrayList<ColunasVO>();
		
		colunasDetalhamento.add(new ColunasVO("Teor", "teorFormatado"));
		colunasDetalhamento.add(new ColunasVO("Solicitante", "solicitanteFormatado"));
		colunasDetalhamento.add(new ColunasVO("Prioridade", "prioridadeFormatada"));
		colunasDetalhamento.add(new ColunasVO("Situação", "marcadoresEmHtmlDetalhes", "", true));
		colunasDetalhamento.add(new ColunasVO("Última Movimentação", "ultimaMovimentacaoformatada"));
		
		return colunasDetalhamento;
	}
	
	public String toJson() {
		return JsonUtil.toJson(this).toString();
	}
	
}
