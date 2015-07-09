package br.gov.jfrj.siga.ex.util;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang.ArrayUtils;

import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.ExDependencia;
import br.gov.jfrj.siga.ex.bl.ExParte;

public class ExGraphColaboracao extends ExGraph {

	public ExGraphColaboracao(ExDocumento doc) {
		for (ExMovimentacao mov : doc.getMobilGeral().getExMovimentacaoSet()) {
			if (!mov.isCancelada()
					&& mov.getExTipoMovimentacao()
							.getIdTpMov()
							.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONTROLE_DE_COLABORACAO)) {

				ExParte parte = ExParte.create(mov.getDescrMov());

				adicionar(new Nodo(parte.getId())
						.setLabel(parte.getTitulo().replace("de ", "de "))
						.setShape("rectangle")
						.setCor(parte.isPreenchido() ? "green" : (parte
								.isPendente() ? "red" : "grey"))
						.setTooltip("Sinalizado em " + mov.getDtRegMovDDMMYY()));

				for (ExDependencia d : parte.getDependencias()) {
					adicionar(new Transicao(d.getId(), parte.getId())
							.setDirected(true));
				}
			}
		}

		ArrayList<ExMovimentacao> l = new ArrayList<>();
		l.addAll(doc.getMobilGeral().getExMovimentacaoSet());
		Collections.reverse(l);

		for (Nodo nodo : this.getNodos()) {
			boolean despreenchido = false;
			boolean preenchido = false;
			for (ExMovimentacao mov : l) {
				if (mov.getExTipoMovimentacao()
						.getIdTpMov()
						.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONTROLE_DE_COLABORACAO)) {

					ExParte parte = ExParte.create(mov.getDescrMov());
					if (nodo.getNome().equals(parte.getId())
							//&& nodo.getNome().equals("reserva_passagens")
							) {
						if (despreenchido && parte.isPreenchido()) {
							despreenchido = false;
							adicionar(new Transicao(parte.getId(),
									parte.getId()).setDirected(true).setCor(
									"red"));
						}
						if (preenchido && !parte.isPreenchido()) {
							despreenchido = true;
						}
						preenchido = parte.isPreenchido();
					}
				}
			}
		}
	}
}
