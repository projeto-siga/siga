package br.gov.jfrj.siga.sr.model;

import java.util.Date;
import java.util.List;


public class SrAtendimentoGeral extends SrEtapaSolicitacao {

	public List<SrAtendimento> atendimentos;

	public SrAtendimentoGeral(SrSolicitacao sol) {
		super(sol);
		setDescricao("Atendimento (total)");
		setInicio(sol.getDtInicioAtendimento());
		if (sol.isFechado())
			setFimEfetivo(sol.getDtEfetivoFechamento());
		else if (sol.isCancelado())
			setFimEfetivo(sol.getDtCancelamento());
		atendimentos = sol.getAtendimentos();
	}	
	
	public List<SrAtendimento> getAtendimentos() {
		return atendimentos;
	}

	public void setAtendimentos(List<SrAtendimento> atendimentos) {
		this.atendimentos = atendimentos;
	}

	@Override
	public Long getDecorridoMillis(Date dtIni, Date dtFim){
		long decorrido = 0L;
		for (SrAtendimento a : atendimentos){
				if (a.terminouAntesDe(dtIni))
					continue;
				if (a.comecouAntesDe(dtIni) && a.terminouAntesDe(dtFim))
					decorrido += a.getDecorridoMillis(dtIni, a.getFimEfetivo());
				else if (a.comecouDepoisDe(dtIni) && a.terminouAntesDe(dtFim))
					decorrido += a.getDecorridoMillis(a.getInicio(), a.getFimEfetivo());
				else if (a.comecouDepoisDe(dtIni) && (a.terminouDepoisDe(dtFim) || !a.isTerminada())){
					decorrido += a.getDecorridoMillis(a.getInicio(), dtFim);
					break;
				}
				else if (a.comecouAntesDe(dtIni) && (a.terminouDepoisDe(dtFim) || !a.isTerminada())){
					decorrido += a.getDecorridoMillis(dtIni, dtFim);
					break;
				}
		}
		return decorrido;
	}

	@Override
	public Date getDataAPartirDe(Date dtBase, Long millisAdiante) {
		Date possivelDtFim = null;
		do {
			possivelDtFim = getAtendimentoAtivoNaData(dtBase).getDataAPartirDe(dtBase, millisAdiante);
			millisAdiante -= getDecorridoMillis(dtBase, possivelDtFim);
			dtBase = possivelDtFim;
		} while (millisAdiante > 0);
		return possivelDtFim;
	}
		
	@Override
	public boolean isAtivoNaData(Date dt) {
		SrAtendimento a = getAtendimentoAtivoNaData(dt);
		return a != null;
	}

	public SrAtendimento getAtendimentoAtivoNaData(Date dt){
		for (SrAtendimento a : atendimentos)
			if (a.abrange(dt))
				return a;
		return null;
	}
	
}
