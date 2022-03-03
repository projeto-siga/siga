package br.gov.jfrj.siga.ex.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;

public class ExGraphTramitacao extends ExGraph {

	public ExGraphTramitacao(ExMobil mob) {
		int numTransicao = 0;

		List<ExMovimentacao> listMov = new ArrayList<ExMovimentacao>();
		listMov.addAll(mob.getExMovimentacaoSet());
		Collections.sort(listMov);

		// Incluí o cadastrante
		{
			PessoaLotacaoParser cadastrante = new PessoaLotacaoParser(mob.doc().getCadastrante(),
					mob.doc().getLotaCadastrante());

			ExMovimentacao criacao = mob.getUltimaMovimentacao(ExTipoDeMovimentacao.CRIACAO);
			if (criacao != null) 
				cadastrante = new PessoaLotacaoParser(criacao.getCadastrante(), criacao.getLotaCadastrante());

			boolean atendente = mob.isAtendente(cadastrante.getPessoa(), cadastrante.getLotacao());
			boolean notificado = mob.isNotificado(cadastrante.getPessoa(), cadastrante.getLotacao());
			
			adicionar(new Nodo(cadastrante.getSiglaCompleta()).setLabel(cadastrante.getSigla()).setShape("oval")
					.setDestacar(atendente || notificado)
					.setEstilo((notificado && !atendente) ? ESTILO_PONTILHADO : null)
					.setTooltip(cadastrante.getNome()));
		}

		for (ExMovimentacao mov : listMov) {
			if (mov.isCancelada())
				continue;
			ITipoDeMovimentacao t = mov.getExTipoMovimentacao();

			if (t == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA
					|| t == ExTipoDeMovimentacao.TRANSFERENCIA
					|| t == ExTipoDeMovimentacao.TRAMITE_PARALELO
					|| t == ExTipoDeMovimentacao.NOTIFICACAO) {

				PessoaLotacaoParser remetente = new PessoaLotacaoParser(mov.getTitular(), mov.getLotaTitular());
				
				// Quando o trâmite é feito pelo WF, não haverá titular ou lotaTitular na movimentação, então tentaremos obter o remetente pelo recebimento anterior
				if (remetente.getPessoa() == null && remetente.getLotacao() == null && mov.getExMovimentacaoRef() != null)
					remetente = new PessoaLotacaoParser(mov.getExMovimentacaoRef().getResp(), mov.getExMovimentacaoRef().getLotaResp());

				PessoaLotacaoParser destinatario = new PessoaLotacaoParser(mov.getResp(), mov.getLotaResp());

				numTransicao++;
				Transicao transicao = new Transicao(remetente.getSiglaCompleta(), destinatario.getSiglaCompleta())
						.setDirected(true).setLabel(String.valueOf(numTransicao))
						.setTooltip("Tramitado em " + mov.getDtRegMovDDMMYY());
				if (t == ExTipoDeMovimentacao.TRAMITE_PARALELO)
					transicao.setEstilo(ESTILO_TRACEJADO);
				if (t == ExTipoDeMovimentacao.NOTIFICACAO)
					transicao.setEstilo(ESTILO_PONTILHADO);
				adicionar(transicao);

				Nodo nodo = localizarNodoPorNome(destinatario.getSiglaCompleta());
				if (nodo == null || (t != ExTipoDeMovimentacao.NOTIFICACAO
						&& ESTILO_PONTILHADO.equals(nodo.getEstilo()))) {
					boolean atendente = mob.isAtendente(destinatario.getPessoa(), destinatario.getLotacao());
					boolean notificado = mob.isNotificado(destinatario.getPessoa(), destinatario.getLotacao());
					adicionar(new Nodo(destinatario.getSiglaCompleta()).setLabel(destinatario.getSigla())
							.setShape("rectangle").setDestacar(atendente || notificado)
							.setEstilo((t == ExTipoDeMovimentacao.NOTIFICACAO && !atendente)
									? ESTILO_PONTILHADO
									: null)
							.setTooltip(destinatario.getNome()));
				}
			}
		}
	}
}
