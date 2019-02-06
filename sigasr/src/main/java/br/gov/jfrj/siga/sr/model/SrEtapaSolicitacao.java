package br.gov.jfrj.siga.sr.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sr.util.SrViewUtil;

public class SrEtapaSolicitacao extends SrIntervaloCorrente implements SrParametroAcordoSolicitacao, Comparable<SrEtapaSolicitacao> {

	//Edson: esta propriedade deveria estar em SrParametroAcordoSolicitacao
	private List<SrParametroAcordo> paramsAcordo; 
	
	private List<? extends SrIntervaloCorrente> intervalosCorrentes;
	
	private DpLotacao lotaResponsavel;
	
	private DpPessoa pessoaResponsavel;
	
	private SrPrioridade prioridade;
	
	private SrItemConfiguracao item;
	
	private SrAcao acao;

	private SrParametro parametro;
	
	private SrSolicitacao solicitacao;
	
	private SrTipoMovimentacao tipoMov;

	private SrFaixa faixa;	
	
	
	public SrEtapaSolicitacao(SrParametro etapa) {
		this.parametro = etapa;
		setDescricao(etapa.getTituloEtapa());
	}
	
	//Edson: métodos que deveriam estar em SrParmetroAcordoSolicitaocao
	@Override
	public boolean isAcordoSatisfeito(){
		SrSituacaoAcordo situ = getSituacaoAcordo();
		return situ == SrSituacaoAcordo.OK || situ == SrSituacaoAcordo.NAO_SE_APLICA;
	}
	
	@Override
	public SrSituacaoAcordo getSituacaoAcordo(){
		return getParamAcordo() != null ? getParamAcordo().getSituacaoParaOValor(getValorRealizado()) : SrSituacaoAcordo.NAO_SE_APLICA;
	}

	public SrParametroAcordo getParamAcordo() {
		if (paramsAcordo != null && paramsAcordo.size() > 0)
			return paramsAcordo.get(0);
		return null;
	}
	
	@Override
	public List<SrParametroAcordo> getParamsAcordo(){
		return paramsAcordo;
	}

	@Override
	public void setParamsAcordo(List<SrParametroAcordo> paramsAcordo) {
		this.paramsAcordo = paramsAcordo;
	}
	//Edson: FIM métodos que deveriam estar em SrParmetroAcordoSolicitaocao	
	
	@Override
	public SrValor getValorRealizado() {
		return new SrValor(getDecorridoEmSegundos(), CpUnidadeMedida.SEGUNDO);
	}
	
	public String getDescricaoCompleta(){
		String descr = getDescricao();
		if (lotaResponsavel != null)
			descr += " " + lotaResponsavel.getSiglaCompleta();
		if (getSolicitacao().isFilha())
			descr += " (" + getSolicitacao().getNumSequenciaString() + ")";
		return descr;
	}
	
	@Override
	public Long getDecorridoMillis(boolean isPrevisao){
		long decorrido = 0L;
		for (SrIntervaloCorrente i : intervalosCorrentes){
			if (i.isFuturo())
				break;
			decorrido += i.getDecorridoMillis(false);
		}
		return decorrido;
	}
	
	public SrIntervaloCorrente getIntervaloCorrendoNaData(Date dt){
		for (SrIntervaloCorrente i : intervalosCorrentes)
			if (i.abrange(dt))
				return i;
		return null;
	}

	@Override
	public boolean isAtivo(Date dt) {
		SrIntervaloCorrente a = getIntervaloCorrendoNaData(dt);
		return a != null ? a.isAtivo() : false;
	}

	public boolean isCadastro() {
		return getParametro().equals(SrParametro.CADASTRO);
	} 
	
	@Override
	public Date getDataContandoDoInicio(Long millisAdiante) {
		Iterator<? extends SrIntervaloCorrente> it = intervalosCorrentes.iterator();
		SrIntervaloCorrente i = null;
		while (it.hasNext()) {
			i = it.next();
			Date dt = i.getDataContandoDoInicio(millisAdiante);
			if (dt != null)
				return dt;
			millisAdiante -= i.getDecorridoMillis(false);
		}
		return null;
	}

	private Long getRestanteMillis() {
		if (getParamAcordo() != null){
			return getParamAcordo().getValorEmMilissegundos() - getDecorridoMillis(false);
		}
		return null;
	}

	public Long getRestanteEmSegundos() {
		return segundos(getRestanteMillis());
	}
	
	public Date getFimPrevisto() {
		if (getFim() == null && getParamAcordo() != null)
			return getDataContandoDoInicio(getParamAcordo().getValorEmMilissegundos());
		return null;
	}
	
	public String getFimPrevistoString() {
		return SrViewUtil.toDDMMYYYYHHMMSS(getFimPrevisto());
	}

	public List<? extends SrIntervaloCorrente> getIntervalosCorrentes() {
		return intervalosCorrentes;
	}

	public void setIntervalosCorrentes(List<? extends SrIntervaloCorrente> intervalos) {
		this.intervalosCorrentes = intervalos;
	}
	
	public DpLotacao getLotaResponsavel() {
		return lotaResponsavel;
	}

	public void setLotaResponsavel(DpLotacao lotaResponsavel) {
		this.lotaResponsavel = lotaResponsavel;
	}

	public SrParametro getParametro() {
		return parametro;
	}

	public void setParametro(SrParametro etapa) {
		this.parametro = etapa;
	}
	
	public SrPrioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(SrPrioridade prioridade) {
		this.prioridade = prioridade;
	}

	public SrItemConfiguracao getItem() {
		return item;
	}

	public void setItem(SrItemConfiguracao item) {
		this.item = item;
	}

	public SrAcao getAcao() {
		return acao;
	}

	public void setAcao(SrAcao acao) {
		this.acao = acao;
	}

	@Override
	public int compareTo(SrEtapaSolicitacao o) {
		int porData = getInicio().compareTo(o.getInicio()) * -1;
		if (porData != 0)
			return porData;
		return getParametro().compareTo(o.getParametro());
	}

	public SrSolicitacao getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(SrSolicitacao solicitacao) {
		this.solicitacao = solicitacao;
	}

	public DpPessoa getPessoaResponsavel() {
		return pessoaResponsavel;
	}

	public void setPessoaResponsavel(DpPessoa pessoaResponsavel) {
		this.pessoaResponsavel = pessoaResponsavel;
	}

	public SrTipoMovimentacao getTipoMov() {
		return tipoMov;
	}

	public void setTipoMov(SrTipoMovimentacao tipoMov) {
		this.tipoMov = tipoMov;
	}
	
	public SrFaixa getFaixa() {
		return faixa;
	}

	public void setFaixa(SrFaixa faixa) {
		this.faixa = faixa;
	}
	
	public void setFaixa(CpOrgaoUsuario orgaoAtendente) {
		List<SrFaixa> faixas = getFaixasPorOrgao(orgaoAtendente);
		ListIterator<SrFaixa> it = faixas.listIterator();
		float limiteInferior = 0f; 
		SrFaixa faixaAtual = null;
		while (it.hasNext()) {
			if (it.hasPrevious()) 
				limiteInferior = faixas.get(it.previousIndex()).getLimiteSuperior();
			faixaAtual = it.next();
			if (estaEntre(limiteInferior, faixaAtual.getLimiteSuperior())) {
				setFaixa(faixaAtual);
				break;
			}
		}
	}
	
	public List<SrFaixa> getFaixasPorOrgao(CpOrgaoUsuario orgao) {
		if (orgao.getAcronimoOrgaoUsu().equals("JFRJ"))
			return SrFaixa.FAIXAS_JFRJ;
		else
			return SrFaixa.FAIXAS_TRF2;
	}
}
