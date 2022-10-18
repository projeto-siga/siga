package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import br.gov.jfrj.siga.ex.ExDocumento;

public class ExPodeFazerDownloadFormatoLivre extends CompositeExpressionSupport {

	private ExDocumento doc;

	public ExPodeFazerDownloadFormatoLivre(ExDocumento doc) {
		this.doc = doc;
	}

	/*
	 * Retorna se é possível fazer o download de um arquivo de formato livre
	 * 
	 * @param titular
	 * 
	 * @param lotaTitular
	 * 
	 * @param doc
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	@Override
	protected Expression create() {

		return new ExECapturadoFormatoLivre(doc);

	}

}