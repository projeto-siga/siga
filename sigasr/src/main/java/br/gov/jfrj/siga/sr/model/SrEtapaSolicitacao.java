package br.gov.jfrj.siga.sr.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.sr.util.ContadorHorasTrabalhadas;

public abstract class SrEtapaSolicitacao extends SrParametroAcordoSolicitacao {

	private List<SrPendencia> pendencias;
	private ContadorHorasTrabalhadas t;

	protected DpLotacao lotaResponsavel;
	protected DpPessoa responsavel;
	protected Date inicio, fimEfetivo;

	public SrEtapaSolicitacao(SrSolicitacao sol) {
		super(sol);
		this.pendencias = sol.getPendenciasLineares();
		t = new ContadorHorasTrabalhadas(getResponsavel(), getLotaResponsavel());
	}

	public Long getDecorridoMillis(Date dtIni, Date dtFim) {
		Long decorrido = 0L;
		for (SrPendencia p : pendencias) {
			if (p.terminouAntesDe(dtIni))
				continue;
			if (p.comecouDepoisDe(dtFim))
				break;
			if (p.comecouDepoisDe(dtIni))
				decorrido += t.getTempoTrabalhado(dtIni, p.getDtIni());
			if (p.terminouDepoisDe(dtFim) || p.isInfinita())
				return decorrido;
			dtIni = p.getDtFim();
		}
		decorrido += t.getTempoTrabalhado(dtIni, dtFim);
		return decorrido;
	}

	public Long getDecorridoMillis() {
		return getDecorridoMillis(getInicio(), dtOuAgora(getFimEfetivo()));
	}

	public Long getDecorridoEmSegundos() {
		return getDecorridoMillis() / 1000;
	}

	public Date getDataAPartirDe(Date dtBase, Long millisAdiante) {
		Date possivelDtFim = null;
		do {
			possivelDtFim = t.getDataAPartirDe(dtBase, millisAdiante);
			millisAdiante -= getDecorridoMillis(dtBase, possivelDtFim);
			dtBase = possivelDtFim;
		} while (millisAdiante > 0);
		return possivelDtFim;
	}

	public Date getFimPrevisto() {
		if (paramAcordo != null)
			return getDataAPartirDe(getInicio(),
					paramAcordo.getValorEmMilissegundos());
		return null;
	}

	private long getRestanteMillis() {
		Date p = getFimPrevisto();
		return p != null ? p.getTime() - dtOuAgora(getFimEfetivo()).getTime(): null;
	}

	public long getRestanteEmSegundos() {
		return getRestanteMillis() / 1000;
	}

	public SrValor getValorRealizado() {
		return new SrValor(getDecorridoEmSegundos(), CpUnidadeMedida.SEGUNDO);
	}

	public boolean isAtivoNaData(Date dt) {
		if (!abrange(dt))
			return false;
		for (SrPendencia p : pendencias) {
			if (p.abrange(dt) || !t.isTrabalhandoNaData(dt))
				return false;
		}
		return true;
	}

	public boolean isRodando() {
		return isAtivoNaData(new Date());
	}

	public String toStr(Date dt) {
		return dt != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dt)
				: "";
	}

	public Date dtOuAgora(Date dt) {
		return dt != null ? dt : new Date();
	}

	public boolean isTerminada() {
		return getFimEfetivo() != null;
	}

	public boolean terminouAntesDe(Date dt) {
		return getFimEfetivo() != null ? getFimEfetivo().before(dt) : false;
	}

	public boolean terminouDepoisDe(Date dt) {
		return getFimEfetivo() != null ? getFimEfetivo().after(dt) : false;
	}

	public boolean comecouAntesDe(Date dt) {
		return getFimEfetivo() != null ? getFimEfetivo().before(dt) : false;
	}

	public boolean comecouDepoisDe(Date dt) {
		return getFimEfetivo() != null ? getFimEfetivo().after(dt) : false;
	}

	public boolean abrange(Date dt) {
		return !comecouDepoisDe(dt) && !terminouAntesDe(dt);
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFimEfetivo() {
		return fimEfetivo;
	}

	public void setFimEfetivo(Date fimEfetivo) {
		this.fimEfetivo = fimEfetivo;
	}

	public String getInicioString() {
		return toStr(getInicio());
	}

	public String getFimEfetivoString() {
		return toStr(getFimEfetivo());
	}

	public String getFimPrevistoString() {
		return toStr(getFimPrevisto());
	}

	public DpLotacao getLotaResponsavel() {
		return lotaResponsavel;
	}

	public void setLotaResponsavel(DpLotacao lotaResponsavel) {
		this.lotaResponsavel = lotaResponsavel;
	}

	public DpPessoa getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(DpPessoa responsavel) {
		this.responsavel = responsavel;
	}

}
