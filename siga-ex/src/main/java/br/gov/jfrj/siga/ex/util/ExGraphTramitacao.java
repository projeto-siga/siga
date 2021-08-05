package br.gov.jfrj.siga.ex.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;

public class ExGraphTramitacao extends ExGraph {

	public ExGraphTramitacao(ExMobil mob) {
		int numTransicao = 0;

		List<ExMovimentacao> listMov = new ArrayList<ExMovimentacao>();
		listMov.addAll(mob.getExMovimentacaoSet());
		Collections.sort(listMov);

		PessoaLotacaoParser cadastrante = new PessoaLotacaoParser(mob.doc().getCadastrante(),
				mob.doc().getLotaCadastrante());

		// Incluí o cadastrante
		{
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
			long t = mov.getIdTpMov();

			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRAMITE_PARALELO
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICACAO) {

				PessoaLotacaoParser remetente = new PessoaLotacaoParser(mov.getTitular(), mov.getLotaTitular());
				PessoaLotacaoParser destinatario = new PessoaLotacaoParser(mov.getResp(), mov.getLotaResp());

				numTransicao++;
				Transicao transicao = new Transicao(remetente.getSiglaCompleta(), destinatario.getSiglaCompleta())
						.setDirected(true).setLabel(String.valueOf(numTransicao))
						.setTooltip("Tramitado em " + mov.getDtRegMovDDMMYY());
				if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRAMITE_PARALELO)
					transicao.setEstilo(ESTILO_TRACEJADO);
				if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICACAO)
					transicao.setEstilo(ESTILO_PONTILHADO);
				adicionar(transicao);

				Nodo nodo = localizarNodoPorNome(destinatario.getSiglaCompleta());
				if (nodo == null || (t != ExTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICACAO
						&& ESTILO_PONTILHADO.equals(nodo.getEstilo()))) {
					boolean atendente = mob.isAtendente(destinatario.getPessoa(), destinatario.getLotacao());
					boolean notificado = mob.isNotificado(destinatario.getPessoa(), destinatario.getLotacao());
					adicionar(new Nodo(destinatario.getSiglaCompleta()).setLabel(destinatario.getSigla())
							.setShape("rectangle").setDestacar(atendente || notificado)
							.setEstilo((t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICACAO && !atendente)
									? ESTILO_PONTILHADO
									: null)
							.setTooltip(destinatario.getNome()));
				}
			}
		}
	}
}
