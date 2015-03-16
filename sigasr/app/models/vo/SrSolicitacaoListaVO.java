package models.vo;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import models.SrLista;
import models.SrSolicitacao;
import util.SrSolicitacaoFiltro;

public class SrSolicitacaoListaVO {

	public boolean podeOrdenar;
	public boolean podePriorizar;
	public boolean podeRemover;	
	public List<SrSolicitacaoVO> itens;
	public List<ColunasVO> colunas;
	
	public SrSolicitacaoListaVO() {
		this.colunas = new ArrayList<ColunasVO>();
		this.itens = new ArrayList<SrSolicitacaoVO>();
	}
	
	public static SrSolicitacaoListaVO fromFiltro(SrSolicitacaoFiltro filtro, boolean telaDeListas, DpLotacao lotaTitular, DpPessoa cadastrante) throws Exception {
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
		
		for (SrSolicitacao sol : solicitacoes)
			solicitacoesVO.itens.add(new SrSolicitacaoVO(sol, lista, lotaTitular, cadastrante));
		
		return solicitacoesVO;
	}
	
}
