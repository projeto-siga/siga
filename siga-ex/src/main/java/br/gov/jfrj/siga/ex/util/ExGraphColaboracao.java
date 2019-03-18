package br.gov.jfrj.siga.ex.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		for (ExMovimentacao mov : doc.getMobilGeral().getExMovimentacaoSet()) {
			if (!mov.getExTipoMovimentacao()
					.getIdTpMov()
					.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONTROLE_DE_COLABORACAO))
				continue;
			l.add(mov);
		}
		// Collections.reverse(l);

//		for (Nodo nodo : this.getNodos()) {
//			boolean despreenchido = false;
//			boolean preenchido = false;
//			for (ExMovimentacao mov : l) {
//				ExParte parte = ExParte.create(mov.getDescrMov());
//				if (nodo.getNome().equals(parte.getId())
//				// && nodo.getNome().equals("reserva_passagens")
//				) {
//					if (despreenchido && parte.isPreenchido()) {
//						despreenchido = false;
//						adicionar(new Transicao(parte.getId(), parte.getId())
//								.setDirected(true).setCor("red"));
//					}
//					if (preenchido && !parte.isPreenchido()) {
//						despreenchido = true;
//					}
//					preenchido = parte.isPreenchido();
//				}
//			}
//		}

		// Identifica as Solicitações de Alteração
		//
		Map<String, ExParte> map = new HashMap<>();
		Set<String> setPreenchidos = new HashSet<>();
		int j = 0, k = 0;
		for (int i = 0; i < l.size(); i++) {
			ExMovimentacao mov = l.get(i);

			// olhar a frente para ver se existe alguma Solicitação de Alteração
			// e processá-la antes de atualizar o mapa
			for (; k < l.size() && mov.getDtMov().equals(l.get(k).getDtMov()); k++) {
				ExParte parte = ExParte.create(l.get(k).getDescrMov());
				ExParte anterior = map.get(parte.getId());
				
				// desenhar aresta de retrabalho
				if (parte.isPreenchido() && anterior != null && !anterior.isPreenchido()) {
					if (!setPreenchidos.contains(parte.getId())) {
						setPreenchidos.add(parte.getId()); 
					} else {
						adicionar(new Transicao(parte.getId(), parte.getId())
								.setDirected(true).setConstraint(false)
								.setCor("blue"));
					}
				}

				
				// desenhar aresta de mensagem
				if (parte.getMensagem() != null) {
					// detectar origem
					List<ExParte> origens = new ArrayList<>();
					for (ExParte p : map.values()) {
						if (p.isPendente()) {
							origens.add(p);
							System.out.println("origem: " + p.getString());
						}
					}
					if (origens.size() == 0) {
						adicionar(new Transicao(parte.getId(), parte.getId())
								.setDirected(true).setConstraint(false)
								.setCor("red"));
					} else {
						for (ExParte origem : origens) {
							adicionar(new Transicao(origem.getId(), parte.getId())
									.setDirected(true).setConstraint(false)
									.setCor("red"));
						}
					}
				}
			}

			// atualizar o mapa de situação das partes
			for (; j < l.size() && mov.getDtMov().equals(l.get(j).getDtMov()); j++) {
				ExParte p = ExParte.create(l.get(j).getDescrMov());
				map.put(p.getId(), p);
				System.out.println("mapa: " + p.getString());
			}
		}

	}
}
