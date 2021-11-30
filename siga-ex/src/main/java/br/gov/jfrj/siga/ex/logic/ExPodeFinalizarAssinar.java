package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;

public class ExPodeFinalizarAssinar extends CompositeExpressionSupport {

	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível que o usuário finalize o documento e assine
	 * digitalmente numa única operação. Os requisitos são os mesmos que têm de ser
	 * cumpridos para se poder finalizar
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public ExPodeFinalizarAssinar(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
		this.doc = doc;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(

				new ExPodeFinalizar(doc, titular, lotaTitular),

				new ExEEletronico(doc));
	}
}
