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
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

public class ExPodeMarcar extends CompositeExpressionSuport {

//	if (mob.doc().isCancelado() || mob.doc().isSemEfeito()
//			|| mob.isEliminado())
//		return false;
//
//	return getConf().podePorConfiguracao(titular, lotaTitular,
//			ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO,
//			CpTipoDeConfiguracao.MOVIMENTAR);

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeMarcar(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(Not.of(new ExEstaCancelado(mob.doc())), Not.of(new ExEstaSemEfeito(mob.doc())),
				Not.of(new ExEstaEliminado(mob)),
				Or.of(And.of(Not.of(new ExEstaFinalizado(mob.doc())), new ExEMobilGeral(mob)),
						And.of(new ExEstaFinalizado(mob.doc()), Not.of(new ExEMobilGeral(mob)))),
				new ExPodeMovimentarPorConfiguracao(ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO, titular,
						lotaTitular));
	}

	public static void afirmar(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		ExPodeMarcar teste = new ExPodeMarcar(mob, titular, lotaTitular);
		if (!teste.eval())
			throw new AplicacaoException("Não é possível marcar porque " + AcaoVO.Helper.produzirExplicacao(teste, false));
	}

};