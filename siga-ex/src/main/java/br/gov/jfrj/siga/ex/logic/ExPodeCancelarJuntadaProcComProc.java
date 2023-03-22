package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeCancelarJuntadaProcComProc implements Expression {
	ExMobil mob;

	public ExPodeCancelarJuntadaProcComProc(ExMobil mob) {
		this.mob = mob;
	}

	@Override
	public boolean eval() {
		ExMobil mobPai = mob.getProcessoJuntadoPai();
		ExMovimentacao movJuntadaProcProc = mob.getDoc().getMobilGeral().getUltimaMovimentacaoNaoCancelada(ExTipoDeMovimentacao.JUNTADA);
		ExMovimentacao mov = new ExMovimentacao();
		for(ExMobil mobil : mobPai.getDoc().getExMobilSet()) {
			mov = mobil.getUltimaMovimentacao();
			
			if(mov.getDtTimestamp().after(movJuntadaProcProc.getDtTimestamp())) {
				return false;
			}
			for (ExMovimentacao mov1 : mobil.getExMovimentacaoReferenciaSet()) {
				if(mov1.getDtTimestamp().after(movJuntadaProcProc.getDtTimestamp())) {
					return false;
				}
			} 

		}
		
		return true;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("juntada não pode ser desfeita pois o documento pai sofreu movimentações após a juntada", result);
	}

}
