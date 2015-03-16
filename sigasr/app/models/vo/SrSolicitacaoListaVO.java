package models.vo;

import java.util.List;

import models.SrSolicitacao;
import util.SrSolicitacaoFiltro;

public class SrSolicitacaoListaVO {

	private boolean podeOrdenar;
	private boolean podeArrastar;
	private List<SrSolicitacaoVO> itens;
	private List<ColunasVO> colunas;

	public List<SrSolicitacaoVO> getItens() {
		return itens;
	}

	public void setItens(List<SrSolicitacaoVO> itens) {
		this.itens = itens;
	}

	public List<ColunasVO> getColunas() {
		return colunas;
	}

	public void setColunas(List<ColunasVO> colunas) {
		this.colunas = colunas;
	}
	
	public static SrSolicitacaoListaVO fromFiltro(SrSolicitacaoFiltro filtro) throws Exception {
		List<SrSolicitacao> solicitacoes = filtro.buscar();
		
		return null;
	}
	
}
