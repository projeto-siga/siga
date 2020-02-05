package br.gov.jfrj.siga.ex.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExGraphTramitacao extends ExGraph {

	public ExGraphTramitacao(ExMobil mob) {
		DpLotacao atendenteAnterior = null;
		ExMovimentacao ultMovNaoCanc = mob.getUltimaMovimentacaoNaoCancelada();
		DpLotacao atendenteInicial = null;
		int numTransicao = 0;
		
		List<ExMovimentacao> listMov = new ArrayList<ExMovimentacao>();
		listMov.addAll(mob.getExMovimentacaoSet());
		Collections.sort(listMov);
		
		for (ExMovimentacao mov : listMov) {
			if (!mov.isCancelada() && !mov.isCanceladora()
					&& mov.getLotaResp() != null && !mov.getLotaResp().equivale(atendenteAnterior)) {

				if (atendenteAnterior != null) {
					numTransicao++;
					adicionar(new Transicao(atendenteAnterior.getSiglaCompleta(), mov
							.getLotaResp().getSiglaCompleta())
							.setDirected(true)
							.setLabel(String.valueOf(numTransicao))
							.setTooltip(
									"Transferido em " + mov.getDtRegMovDDMMYY()));
				} else 
					atendenteInicial = mov.getLotaResp();
					if (ultMovNaoCanc != null) {
						adicionar(new Nodo(mov.getLotaResp().getSiglaCompleta())
								.setLabel(mov.getLotaResp().getSigla())
								.setShape(
										mov.getLotaResp().equals(atendenteInicial) ? "oval"
												: "rectangle")
								.setDestacar(
										mov.getLotaResp().equals(
												ultMovNaoCanc.getLotaResp()))
								.setTooltip(mov.getLotaResp().getNomeLotacao()));
						atendenteAnterior = mov.getLotaResp();
					}			
			}
		}
	}
}
