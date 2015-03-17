package models.vo;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import models.SrLista;
import models.SrPrioridade;
import models.SrSolicitacao;

public class SrSolicitacaoVO {

	public Long idSolicitacao;
	public String codigo;
	public String descricao;
	public Long prioridadeLista;
	public SrItemConfiguracaoVO itemConfiguracao;
	public String nomeSolicitante;
	public String descricaoSolicitante;
	public String dtRegString;
	public SelecionavelVO lotaSolicitante;
	public SelecionavelVO lotaAtendente;
	public String ultimaMovimentacao;
	public String dtUltimaMovimentacaoString;
	public String marcadoresEmHtml;
	public SrPrioridade prioridade;
	
	
	public SrSolicitacaoVO(SrSolicitacao sol) {
		this.idSolicitacao = sol.getId();
		this.codigo = sol.getCodigo();
		this.descricao = sol.getDescricao();
		this.itemConfiguracao = sol.getItemAtual() != null ? sol.getItemAtual().toVO() : null;
		this.nomeSolicitante = sol.solicitante != null ? sol.solicitante.getNomeAbreviado() : "";
		this.descricaoSolicitante = sol.solicitante != null ? sol.solicitante.getDescricaoIniciaisMaiusculas() : "";
		this.lotaSolicitante = sol.lotaSolicitante != null ? SelecionavelVO.createFrom(sol.lotaSolicitante) : null;
		this.dtRegString = sol.solicitacaoInicial.getDtRegString();
		this.lotaAtendente = sol.getLotaAtendente() != null ? SelecionavelVO.createFrom(sol.getLotaAtendente()) : null;
		this.ultimaMovimentacao = sol.getUltimaMovimentacaoQuePossuaDescricao() != null ? sol.getUltimaMovimentacaoQuePossuaDescricao().descrMovimentacao : "";
		this.dtUltimaMovimentacaoString = sol.getUltimaMovimentacao() != null ? sol.getUltimaMovimentacao().getDtIniMovDDMMYYYYHHMM() : "";
		this.prioridade = sol.prioridade;
	}
	
	public SrSolicitacaoVO(SrSolicitacao sol, SrLista lista) throws Exception {
		this(sol);
		
		if (lista != null)
			this.prioridadeLista = sol.getPrioridadeNaLista(lista);
	}
	
	public SrSolicitacaoVO(SrSolicitacao sol, SrLista lista, DpLotacao lotaTitular, DpPessoa cadastrante) throws Exception {
		this(sol, lista);
		this.marcadoresEmHtml = sol.getMarcadoresEmHtml(cadastrante, lotaTitular);
	}
}
