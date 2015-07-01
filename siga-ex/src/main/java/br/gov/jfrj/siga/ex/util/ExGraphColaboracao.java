package br.gov.jfrj.siga.ex.util;

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
	}
}
