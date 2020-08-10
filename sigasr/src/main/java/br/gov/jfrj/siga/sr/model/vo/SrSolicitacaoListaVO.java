package br.gov.jfrj.siga.sr.model.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sr.model.SrLista;
import br.gov.jfrj.siga.sr.model.SrMarca;
import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrPrioridadeSolicitacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.util.JsonUtil;
import br.gov.jfrj.siga.sr.util.SrSolicitacaoFiltro;
import br.gov.jfrj.siga.sr.util.SrSolicitacaoFiltro.SentidoOrdenacao;
import br.gov.jfrj.siga.sr.util.SrViewUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SrSolicitacaoListaVO {

	private static final String ULTIMA_MOVIMENTACAOFORMATADA = "ultimaMovimentacaoformatada";
	private static final String GT_CELULA_NOWRAP_SOLICITACAO_DADOS = "gt-celula-nowrap solicitacao-dados";
	private static Long LARGURA_COLUNA_CODIGO = 130L;
	private static Long LARGURA_COLUNA_REMOVER_PRIORIZAR = 55L;
	private static Long LARGURA_COLUNA_PRIORIDADE = 20L;

	private boolean podeOrdenar;
	private boolean podePriorizar;
	private boolean podeRemover;
	private boolean podeFiltrar;
	private boolean podePaginar;
	private boolean telaDeListas;
	private Long recordsFiltered;
	private List<SrSolicitacaoVO> data;
	private List<ColunaVO> colunas;
	private List<ColunaVO> colunasDetalhamento;
	private String colunasTabelaJson;
    private String colunasDetalhamentoJson;
	
	public SrSolicitacaoListaVO(SrSolicitacaoFiltro filtro,
			boolean telaDeListas, String propriedade, boolean isPopup,
			DpLotacao lotaTitular, DpPessoa cadastrante) throws Exception{

		SrLista lista = null;

		if (filtro.getIdListaPrioridade() != null)
			lista = SrLista.AR.findById(filtro.getIdListaPrioridade());

		//Edson: define propriedades da tabela
		setPodeFiltrar(false);
		if (telaDeListas && lista != null) {
			filtro.setOrderBy("posicaoNaLista");
			filtro.setSentidoOrdenacao(SentidoOrdenacao.ASC);
			setPodePriorizar(lista.podePriorizar(lotaTitular, cadastrante));
			setPodeRemover(lista.podeRemover(lotaTitular, cadastrante));
			setPodePaginar(false);
			setPodeOrdenar(false);
		} else {
			setPodeOrdenar(true);
			setPodePaginar(true);
		}

		//Edson: define as colunas
		ColunaVO posicaoNaLista = new ColunaVO(
				"#",
				"posicaoNaLista",
				"gt-celula-nowrap solicitacao-dados solicitacao-prioridade numero-solicitacao",
				LARGURA_COLUNA_PRIORIDADE);
		ColunaVO botaoExpandir = new ColunaVO(SrViewUtil.botaoExpandir(),
				"botaoExpandir",
				"bt-expandir-tabela gt-celula-nowrap details-control").setOrdenavel(false);
		ColunaVO codigo = new ColunaVO("Código", "codigo",
				"gt-celula-nowrap numero-solicitacao solicitacao-codigo",LARGURA_COLUNA_CODIGO);
		ColunaVO teor = new ColunaVO("Teor", "descrSolicitacao",
				"gt-celula-nowrap solicitacao-dados");
		ColunaVO cad = new ColunaVO("Cadastrante", "cadastrante",
				"gt-celula-nowrap solicitacao-dados", LARGURA_COLUNA_CODIGO).setExibirPorDefault(false);
		ColunaVO lotaCad = new ColunaVO("Lot. Cadastrante", "lotaTitular",
				"gt-celula-nowrap solicitacao-dados", LARGURA_COLUNA_CODIGO).setExibirPorDefault(false);
		ColunaVO solictt = new ColunaVO("Solicitante", "solicitante",
				"gt-celula-nowrap solicitacao-dados", LARGURA_COLUNA_CODIGO);
		ColunaVO lotaSolictt = new ColunaVO("Lot. Solicitante", "lotaSolicitante",
				"gt-celula-nowrap solicitacao-dados", LARGURA_COLUNA_CODIGO);
		ColunaVO dtReg = new ColunaVO("Data Abertura", "dtReg",
				"gt-celula-nowrap solicitacao-dados", LARGURA_COLUNA_CODIGO);
		ColunaVO prioridade = new ColunaVO("Prioridade", "prioridade",
				"gt-celula-nowrap solicitacao-dados", LARGURA_COLUNA_CODIGO).setExibirPorDefault(false);
		ColunaVO prioridadeTecnica = new ColunaVO("Prior. Técnica",
				"prioridadeTecnica",
				"gt-celula-nowrap solicitacao-dados", LARGURA_COLUNA_CODIGO);
		ColunaVO situacao = new ColunaVO("Situação", "situacao",
				"gt-celula-nowrap solicitacao-dados", LARGURA_COLUNA_CODIGO).setExibirPorDefault(false);
		ColunaVO atendente = new ColunaVO("Atendente", "atendente",
				"gt-celula-nowrap solicitacao-dados", LARGURA_COLUNA_CODIGO).setExibirPorDefault(false);
		ColunaVO lotaAtendente = new ColunaVO("Lot. Atendente", "lotaAtendente",
				"gt-celula-nowrap solicitacao-dados", LARGURA_COLUNA_CODIGO).setExibirPorDefault(false);
		ColunaVO ultMov = new ColunaVO("Últ. Movimentação",
				"ultimaMovimentacao",
				"gt-celula-nowrap solicitacao-dados").setExibirPorDefault(false);
		ColunaVO dtUltMov = new ColunaVO("Data Últ. Movim.",
				"dtUltimaMovimentacao",
				"gt-celula-nowrap solicitacao-dados", LARGURA_COLUNA_CODIGO).setExibirPorDefault(false);
		ColunaVO prazo = new ColunaVO("Prazo",
				"prazo",
				"gt-celula-nowrap solicitacao-dados", LARGURA_COLUNA_CODIGO).setExibirPorDefault(false);
		ColunaVO botaoRemoverPriorizar = new ColunaVO(
				"",
				"botaoRemoverPriorizar",
				"gt-celula-nowrap solicitacao-dados solicitacao-remover",
				LARGURA_COLUNA_REMOVER_PRIORIZAR).setOcultavel(false);
		
		this.setColunas(new ArrayList<ColunaVO>());

		if (telaDeListas)
			colunas.add(posicaoNaLista);
		else
			colunas.add(botaoExpandir);	
		
		colunas.add(codigo);
		colunas.add(teor);
		colunas.add(solictt);
		colunas.add(lotaSolictt);
		colunas.add(dtReg);
		colunas.add(cad);
		colunas.add(lotaCad);
		colunas.add(prioridade);
		colunas.add(prioridadeTecnica);
		colunas.add(prazo);
		colunas.add(situacao);
		colunas.add(atendente);
		colunas.add(lotaAtendente);
		colunas.add(dtUltMov);
		colunas.add(ultMov);

		if (telaDeListas){
			//colunas.add(prioridadeNaLista);
			if (podeRemover || podePriorizar)
				colunas.add(botaoRemoverPriorizar);
		}
						
		this.setColunasDetalhamento(new ArrayList<ColunaVO>());
		
		colunasDetalhamento.add(codigo.copy().setExibirPorDefault(false));
		colunasDetalhamento.add(teor);
		colunasDetalhamento.add(solictt.copy().setExibirPorDefault(false));
		colunasDetalhamento.add(lotaSolictt.copy().setExibirPorDefault(false));
		colunasDetalhamento.add(dtReg.copy().setExibirPorDefault(false));
		colunasDetalhamento.add(cad);
		colunasDetalhamento.add(lotaCad.copy());
		colunasDetalhamento.add(prioridade);
		colunasDetalhamento.add(prioridadeTecnica);
		colunasDetalhamento.add(situacao);
		colunasDetalhamento.add(atendente);
		colunasDetalhamento.add(lotaAtendente);
		colunasDetalhamento.add(ultMov.copy().setExibirPorDefault(true).setTitulo("Última Movimentação"));
		colunasDetalhamento.add(dtUltMov.copy().setExibirPorDefault(true).setTitulo("Data da Última Movimentação"));
		
		setColunasTabelaJson();
		setColunasDetalhamentoJson();

		this.setData(new ArrayList<SrSolicitacaoVO>());
		
		//Edson: busca os dados
		if (filtro.isRazoavelmentePreenchido() && filtro.isPesquisar()) {
			setRecordsFiltered(filtro.buscarQuantidade(cadastrante));
			List<Object[]> solicitacoes = filtro.buscarPorFiltro(cadastrante);

			for (Object[] o : solicitacoes) {

				SrSolicitacao sol = (SrSolicitacao) o[0];
				SrMarca m = (SrMarca) o[1];
				SrMovimentacao mov = (SrMovimentacao) o[2];
				Date dt = (Date) o[3];
				SrPrioridadeSolicitacao p = null;
				try{
					p = (SrPrioridadeSolicitacao) o[4];
				} catch(ArrayIndexOutOfBoundsException aioobe){
					//
				}

				getData().add(
						new SrSolicitacaoVO(sol, m, mov, dt, lista, p,
								lotaTitular, cadastrante, propriedade, isPopup,
								isPodeRemover(), isPodePriorizar()));
			}

		} else
			setRecordsFiltered(0L);
	}

	public String toJson() {
		return JsonUtil.toJson(this).toString();
	}

	public boolean isPodeOrdenar() {
		return podeOrdenar;
	}

	public void setPodeOrdenar(boolean podeOrdenar) {
		this.podeOrdenar = podeOrdenar;
	}

	public boolean isPodePriorizar() {
		return podePriorizar;
	}

	public void setPodePriorizar(boolean podePriorizar) {
		this.podePriorizar = podePriorizar;
	}

	public boolean isPodeRemover() {
		return podeRemover;
	}

	public void setPodeRemover(boolean podeRemover) {
		this.podeRemover = podeRemover;
	}

	public boolean isPodeFiltrar() {
		return podeFiltrar;
	}

	public void setPodeFiltrar(boolean podeFiltrar) {
		this.podeFiltrar = podeFiltrar;
	}

	public boolean isPodePaginar() {
		return podePaginar;
	}

	public void setPodePaginar(boolean podePaginar) {
		this.podePaginar = podePaginar;
	}

	public List<SrSolicitacaoVO> getData() {
		return data;
	}

	public void setData(List<SrSolicitacaoVO> itens) {
		this.data = itens;
	}

	public List<ColunaVO> getColunas() {
		return colunas;
	}

	public void setColunas(List<ColunaVO> colunas) {
		this.colunas = colunas;
	}

	public List<ColunaVO> getColunasDetalhamento() {
		return colunasDetalhamento;
	}

	public void setColunasDetalhamento(List<ColunaVO> colunasDetalhamento) {
		this.colunasDetalhamento = colunasDetalhamento;
	}

	public String getColunasJson() {
		return colunasTabelaJson;
	}

	public String getColunasDetalhamentoJson() {
		return colunasDetalhamentoJson;
	}

	public Long getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(Long quantidade) {
		this.recordsFiltered = quantidade;
	}

	public String getColunasTabelaJson() {
		return colunasTabelaJson;
	}

	public void setColunasTabelaJson() {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		List<ColunaVO> colunasResult = null;
		Gson gson = builder.create();

		// remove a primeira coluna, que serÃ¡ sempre o detalhamento ou
		// posiÃ§Ã£o na lista
		if (!colunas.isEmpty()) {
			if (telaDeListas)
				colunasResult = colunas.subList(1, colunas.size() - 1);
			else
				colunasResult = colunas.subList(1, colunas.size());
		}
		this.colunasTabelaJson = gson.toJson(colunasResult);
	}

	public void setColunasDetalhamentoJson() {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		this.colunasDetalhamentoJson = builder.create().toJson(colunasDetalhamento);;
	}

	public boolean isTelaDeListas() {
		return telaDeListas;
	}

	public void setTelaDeListas(boolean telaDeListas) {
		this.telaDeListas = telaDeListas;
	}

}
