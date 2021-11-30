package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpNaoENulo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeAcessarDocumentoAntigo extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível acessar o documento ao qual pertence o móbil passado
	 * por parâmetro, analisando <i>podeAcessarAberto()</i>,
	 * <i>podeAcessarCancelado()</i> e <i>podeAcessarPorNivel()</i>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public ExPodeAcessarDocumentoAntigo(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return Or.of(
				And.of(new CpNaoENulo(mob.doc().getOrgaoUsuario(), "órgão usuário"),
						new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.ACESSAR)
								.withOrgaoObjeto(mob.doc().getOrgaoUsuario())),
				new ExESubscritorOuCossignatario(mob.doc(), titular),
				new ExEstaVinculadaPorPerfil(mob.doc(), titular, lotaTitular),
				And.of(new ExPodeAcessarAberto(mob, titular, lotaTitular),
						new ExPodeAcessarPorNivel(mob, titular, lotaTitular),
						new ExPodeAcessarCancelado(mob, titular, lotaTitular),
						new ExPodeAcessarSemEfeito(mob, titular, lotaTitular)));
	}
}