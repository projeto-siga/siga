package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;

public class ExPodeAutenticarDocumento extends CompositeExpressionSupport {

	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/*
	 * Retorna se pode autenticar um documento 
	 * 
	 * @param titular
	 * 
	 * @param lotaTitular
	 * 
	 * @param mob
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	public ExPodeAutenticarDocumento(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
		this.doc = doc;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return Or.of(

				new ExPodeAutenticarComSenha(doc,titular,lotaTitular),
				
				new ExPodeAutenticarComCertificadoDigital(doc,titular,lotaTitular)

		);

	}
}