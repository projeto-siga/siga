package models.vo;

import util.SigaPlayUtil;
import models.SrItemConfiguracao;
import models.SrLista;
import models.SrPrioridade;
import models.SrSolicitacao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class SrSolicitacaoVO {

	public Long idSolicitacao;
	public String codigo;
	public String codigoFormatado;
	public String descricao;
	public String teorFormatado;
	public SrItemConfiguracaoVO itemConfiguracao;
	public String solicitanteFormatado;
	public String dtUltimaMovimentacaoFormatada;
	public String ultimaMovimentacaoformatada;
	public String prioridadeFormatada;
	public String botaoExpandir = "+";
	public String marcadoresEmHtmlDetalhes;
	
	public String dtUltimaMovimentacaoString;
	public Long prioridadeLista;
	public String nomeSolicitante;
	public String descricaoSolicitante;
	public String dtRegString;
	public SelecionavelVO lotaSolicitante;
	public SelecionavelVO lotaAtendente;
	public String ultimaMovimentacao;
	public String marcadoresEmHtml;
	public SrPrioridade prioridade;
	
	
	public SrSolicitacaoVO(SrSolicitacao sol, String nome, boolean isPopup) {
		this(sol);
		this.codigoFormatado = getCodigoFormatado(sol.getId(), sol.getCodigo(), nome, isPopup);
	}
	
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
		
		this.teorFormatado = getTeorFormatado(sol.getItemAtual(), this.descricao);
		this.solicitanteFormatado = getSolicitanteFormatado(this.nomeSolicitante, this.descricaoSolicitante, this.lotaSolicitante);
		this.dtUltimaMovimentacaoFormatada = getDtUltimaMovimentacaoFormatado(this.dtUltimaMovimentacaoString);
		this.ultimaMovimentacaoformatada = SigaPlayUtil.selecionado(this.ultimaMovimentacao, this.ultimaMovimentacao);
		this.prioridadeFormatada = this.prioridade != null ? SigaPlayUtil.selecionado(this.prioridade.descPrioridade, this.prioridade.descPrioridade) : "";
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
	
	public SrSolicitacaoVO(SrSolicitacao sol, SrLista lista, String nome, boolean isPopup, DpLotacao lotaTitular, DpPessoa cadastrante) throws Exception {
		this(sol, nome, isPopup);
		this.marcadoresEmHtml = sol.getMarcadoresEmHtml(cadastrante, lotaTitular);
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
	
}
