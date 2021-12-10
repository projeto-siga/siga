package br.gov.jfrj.siga.ex.logic;

import java.util.Set;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.cp.logic.CpDiferente;
import br.gov.jfrj.siga.cp.logic.CpENulo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExPodeVisualizarImpressaoCompleta extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private Set<ExDocumento> filhos;

	public ExPodeVisualizarImpressaoCompleta(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.doc = mob.doc();
		this.titular = titular;
		this.lotaTitular = lotaTitular;

		filhos = mob.getExDocumentoFilhoSet();
	}

	/**
	 * Retorna se é possível visualizar impressão do documento em questão e de todos
	 * os filhos, com base na permissão para visualização da impressão de cada um
	 * dos filhos.
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 */
	@Override
	protected Expression create() {
		return And.of(

				new ExPodeVisualizarImpressao(mob, titular, lotaTitular),

				Not.of(new CpENulo(filhos, "documentos filhos")),

				new CpDiferente(filhos.size(), "quantidade de documentos filhos", 0, "zero"));
	}
}