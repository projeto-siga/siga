package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSuport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

public class ExPodeCancelarMarcacao extends CompositeExpressionSuport {

//	if (mov.isCancelada()) {
//		return Optional.of("Marcação já cancelada.");
//	}
//
//	if ((nonNull(mov.getSubscritor()) && mov.getSubscritor().equivale(titular))
//			|| (isNull(mov.getSubscritor()) && nonNull(mov.getLotaSubscritor()) && mov.getLotaSubscritor().equivale(lotaTitular))) {
//		return Optional.empty();
//	}
//
//	if ((nonNull(mov.getCadastrante()) && mov.getCadastrante().equivale(titular))
//			|| (isNull(mov.getCadastrante()) && mov.getLotaCadastrante().equivale(lotaTitular))) {
//		return Optional.empty();
//	}
//
//	if (getConf().podePorConfiguracao(titular, lotaTitular, mov.getIdTpMov(),
//			CpTipoDeConfiguracao.CANCELAR_MOVIMENTACAO)) {
//		return Optional.empty();
//	}
//
//	return Optional.of("Usuário deve ser ou o cadastrante ou o subscritor da movimentação "
//			+ "ou deve estar na mesma unidade desses " //
//			+ "ou deve ter autorização para cancelar marcações.");

	private ExMovimentacao mov;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeCancelarMarcacao(ExMovimentacao mov, DpPessoa titular, DpLotacao lotaTitular) {
		this.mov = mov;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(Not.of(new ExMovimentacaoEstaCancelada(mov)),
				Or.of(new ExMovimentacaoESubscritor(mov, titular), new ExMovimentacaoECadastrante(mov, titular),
						And.of(Not.of(new ExMovimentacaoHaSubscritor(mov)),
								new ExMovimentacaoELotaSubscritor(mov, lotaTitular)),
						And.of(Not.of(new ExMovimentacaoHaCadastrante(mov)),
								new ExMovimentacaoELotaCadastrante(mov, lotaTitular)),
						And.of(new ExEMarcadorDeAtendente(mov.getMarcador()),
								new ExEstaResponsavel(mov.mob(), titular, lotaTitular))),
				new ExPodeCancelarMovimentacaoPorConfiguracao(ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO, titular,
						lotaTitular));
	}

	public static void afirmar(ExMovimentacao mov, DpPessoa titular, DpLotacao lotaTitular) {
		ExPodeCancelarMarcacao teste = new ExPodeCancelarMarcacao(mov, titular, lotaTitular);
		if (!teste.eval())
			throw new AplicacaoException(
					"Não é possível cancelar marcação porque " + AcaoVO.Helper.produzirExplicacao(teste, false));
	}

};