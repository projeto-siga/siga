package br.gov.jfrj.siga.ex.logic;

import java.util.ArrayList;
import java.util.List;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpDiferente;
import br.gov.jfrj.siga.cp.logic.CpIgual;
import br.gov.jfrj.siga.cp.logic.CpNaoENulo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeDesfazerRestricaoDeAcesso extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private List<ExMovimentacao> lista;

	public ExPodeDesfazerRestricaoDeAcesso(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.doc = mob.doc();
		this.titular = titular;
		this.lotaTitular = lotaTitular;

		List<ExMovimentacao> lista = new ArrayList<ExMovimentacao>();
		lista.addAll(mob.getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_RESTRINGIR_ACESSO));
	}

	@Override
	protected Expression create() {
		return And.of(
				
				new CpNaoENulo(lista, "restrições de acesso"), 

				new CpDiferente(lista == null ? 0 : lista.size(), "restrições de acesso", 0, "zero"),

				new ExPodeRestringirAcesso(mob, titular, lotaTitular));
	}
}