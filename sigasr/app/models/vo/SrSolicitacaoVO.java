package models.vo;

import java.util.List;

import edu.emory.mathcs.backport.java.util.Collections;
import models.SrItemConfiguracao;
import models.SrLista;
import models.SrPrioridadeSolicitacao;
import models.SrSolicitacao;
import util.SigaPlayUtil;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class SrSolicitacaoVO {

	private String botaoRemover;
	private String botaoPriorizar;
	
	public Long idSolicitacao;
	public String codigo = "";
	public String codigoFormatado = "";
	public String descricao = "";
	public String teorFormatado = "";
	
	public String solicitanteFormatado = "";
	public String dtUltimaMovimentacaoFormatada = "";
	public String ultimaMovimentacaoformatada = "";
	public String prioridadeFormatada = "";
	public String botaoExpandir = "+";
	public String marcadoresEmHtmlDetalhes = "";
	public String lotaAtendenteFormatada = "";
	public String prioridadeListaFormatada = "";
	public String botaoRemoverPriorizar = "";
	public String dtUltimaMovimentacaoString = "";
	public String nomeSolicitante = "";
	public String descricaoSolicitante = "";
	public String dtRegString = "";
	public SelecionavelVO lotaSolicitante;
	public SelecionavelVO lotaAtendente;
	public String ultimaMovimentacao = "";
	public String marcadoresEmHtml = "";
	
	public SrItemConfiguracaoVO itemConfiguracao;
	public SrPrioridadeSolicitacaoVO prioridadeSolicitacaoVO;
	
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
		
		this.teorFormatado = getTeorFormatado(sol.getItemAtual(), this.descricao);
		this.solicitanteFormatado = getSolicitanteFormatado(this.nomeSolicitante, this.descricaoSolicitante, this.lotaSolicitante);
		this.dtUltimaMovimentacaoFormatada = getDtUltimaMovimentacaoFormatado(this.dtUltimaMovimentacaoString);
		this.ultimaMovimentacaoformatada = SigaPlayUtil.selecionado(this.ultimaMovimentacao, this.ultimaMovimentacao);
		
		this.lotaAtendenteFormatada = this.lotaAtendente != null ? getLotacaoFormatada(this.lotaAtendente) : "";
		this.prioridadeFormatada = sol.prioridade != null ? SigaPlayUtil.selecionado(sol.prioridade.descPrioridade, sol.prioridade.descPrioridade) : "";
	}
	
	public SrSolicitacaoVO(SrSolicitacao sol, SrLista lista, SrPrioridadeSolicitacao prioridadeSolicitacao, boolean podeRemover, boolean podePriorizar) throws Exception {
		this(sol);
		
		this.prioridadeSolicitacaoVO = prioridadeSolicitacao.toVO();
		this.prioridadeListaFormatada = SigaPlayUtil.tagA(String.valueOf(prioridadeSolicitacao.numPosicao));
		this.prioridadeFormatada = prioridadeSolicitacao.prioridade != null ? 
				SigaPlayUtil.selecionado(prioridadeSolicitacao.prioridade.descPrioridade, prioridadeSolicitacao.prioridade.descPrioridade) : "";
			
		if (podeRemover)
			this.botaoRemover = SigaPlayUtil.botaoRemoverSolicitacao(this.idSolicitacao, lista.idLista);
		
		if (podePriorizar)
			this.botaoPriorizar = SigaPlayUtil.botaoPriorizarSolicitacao();
		
		if (podeRemover || podePriorizar)
			this.botaoRemoverPriorizar = this.botaoRemover + this.botaoPriorizar;
	}
	
	public SrSolicitacaoVO(SrSolicitacao sol, SrLista lista, SrPrioridadeSolicitacao prioridadeSolicitacao, String nome, boolean isPopup, DpLotacao lotaTitular, DpPessoa cadastrante, 
			boolean podeRemover, boolean podePriorizar) throws Exception {
		this(sol, lista, prioridadeSolicitacao, podeRemover, podePriorizar);
		this.codigoFormatado = getCodigoFormatado(sol.getId(), sol.getCodigo(), nome, isPopup);
		this.setMarcadoresEmHtml(sol.getMarcadoresEmHtml(cadastrante, lotaTitular));
		this.marcadoresEmHtmlDetalhes = getMarcadoresEmHTMLDetalhes(this.marcadoresEmHtml, this.dtUltimaMovimentacaoString);
	} 
	
	public SrSolicitacaoVO(SrSolicitacao sol, String nome, boolean isPopup, DpLotacao lotaTitular, DpPessoa cadastrante) throws Exception {
		this(sol);
		this.codigoFormatado = getCodigoFormatado(sol.getId(), sol.getCodigo(), nome, isPopup);
		this.setMarcadoresEmHtml(sol.getMarcadoresEmHtml(cadastrante, lotaTitular));
		this.marcadoresEmHtmlDetalhes = getMarcadoresEmHTMLDetalhes(this.marcadoresEmHtml, this.dtUltimaMovimentacaoString);
	} 
	
	private String getCodigoFormatado(Long id, String codigo, String nome, boolean isPopup) {
		StringBuffer sb = new StringBuffer();
		
		if (isPopup) {
			sb.append( "<a href=\"javascript:opener.retorna_solicitacao" );
			sb.append( nome + "('" );
			sb.append( idSolicitacao + "','" + codigo );
			sb.append( "}'," + codigo + "');window.close()\">" );
			sb.append( codigo + "</a>" );
		}
		else {
			sb.append( "<a href=\"/sigasr/solicitacao/exibir/" );
			sb.append( id + "\">" );
			sb.append( codigo + "</a>" );
		}
		
		return sb.toString();
	}
	
	private String getTeorFormatado(SrItemConfiguracao itemConfiguracao, String descricao) {
		StringBuffer sb = new StringBuffer();
		sb.append("<b>");
		sb.append(SigaPlayUtil.descricaoItem(itemConfiguracao));
		sb.append(":</b>&nbsp;");
		sb.append(SigaPlayUtil.selecionado(descricao, descricao));
		
		return sb.toString();
	}
	
	private String getSolicitanteFormatado(String nomeSolicitante, String descricaoSolicitante, SelecionavelVO lotaSolicitante) {
		StringBuffer sb = new StringBuffer();
		sb.append(SigaPlayUtil.selecionado(nomeSolicitante, descricaoSolicitante));
		sb.append("&nbsp;");
		
		if (lotaSolicitante != null)
			sb.append(SigaPlayUtil.selecionado(lotaSolicitante.getSigla() , lotaSolicitante.getDescricao()));
		
		return sb.toString();
	}
	
	private String getLotacaoFormatada(SelecionavelVO lotacao) {
		return new String("<b>" + lotacao.getSigla() + "</b>");
	}
	
	private String getDtUltimaMovimentacaoFormatado(String dtUltimaMovimentacao) {
		return new String("<b>" + SigaPlayUtil.selecionado(dtUltimaMovimentacao, dtUltimaMovimentacao) + "</b>");
	}
	
	private String getMarcadoresEmHTMLDetalhes(String marcadoresEmHTML, String data) {
		StringBuffer sb = new StringBuffer();
		sb.append("<td style=\"padding: 8px 5px 8px 10px !important\"><b>");
		sb.append(marcadoresEmHTML);
		sb.append(" desde</b> ");
		sb.append(data);
		sb.append("</td>");
		
		return sb.toString();
	}
	
	public void setMarcadoresEmHtml(String valor) {
		if (valor != null)
			this.marcadoresEmHtml = valor;
		else
			this.marcadoresEmHtml = "";
	}
	
	public static void ordenarPorPrioridade(List<SrSolicitacaoVO> lista) {
		SrSolicitacaoVOComparator comparator = new SrSolicitacaoVOComparator();
		Collections.sort(lista, comparator);
	}
}
