package br.gov.jfrj.siga.ex.logic;

import java.util.ArrayList;
import java.util.List;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeSerJuntado extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private List<ExMovimentacao> listMovJuntada;

	public ExPodeSerJuntado(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.doc = mob.doc();
		this.titular = titular;
		this.lotaTitular = lotaTitular;

		listMovJuntada = new ArrayList<ExMovimentacao>();
		if (mob.getDoc().getMobilDefaultParaReceberJuntada() != null) {
			listMovJuntada.addAll(mob.getDoc().getMobilDefaultParaReceberJuntada()
					.getMovsNaoCanceladas(ExTipoDeMovimentacao.JUNTADA));
		}
	}

	/**
	 * Retorna se é possível que algum móbil seja juntado a este, segundo as
	 * seguintes regras:
	 * <ul>
	 * <li>Não pode estar cancelado</li>
	 * <li>Volume não pode estar encerrado</li>
	 * <li>Não pode estar em algum arquivo</li>
	 * <li>Não pode estar juntado</li>
	 * <li>Não pode estar em trânsito</li>
	 * <li><i>podeMovimentar()</i> precisa retornar verdadeiro para ele</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	@Override
	protected Expression create() {
		return And.of(

				Not.of(new ExEMobilCancelado(mob)),

				Not.of(new ExEMobilVolumeEncerrado(mob)),

				Not.of(new ExEstaJuntado(mob)),

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				Not.of(new ExEstaArquivado(mob)),

				new ExPodeMovimentar(mob, titular, lotaTitular));
	}
}