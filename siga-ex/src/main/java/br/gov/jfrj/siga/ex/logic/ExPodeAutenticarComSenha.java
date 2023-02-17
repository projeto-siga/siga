package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeAutenticarComSenha extends CompositeExpressionSupport {

	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeAutenticarComSenha(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
		this.doc = doc;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/*
	 * Retorna se é possível autenticar um documento com senha:
	 */
	@Override
	protected Expression create() {
		return And.of(

				new ExEEletronico(doc),
				
				new ExECapturado(doc),

				Not.of(new ExEstaAutenticadoComTokenOuSenha(doc)),

				new ExPodePorConfiguracao(titular, lotaTitular).withExMod(doc.getExModelo())
					.withExFormaDoc(doc.getExFormaDocumento()).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
					.withExTpMov(ExTipoDeMovimentacao.CONFERENCIA_COPIA_COM_SENHA),

				Or.of(

						Not.of(new ExEGovSP()),

						new ExEExternoCapturado(doc),            				
						
						new ExEInternoCapturado(doc)),

				Or.of(

						new ExEExternoCapturado(doc),

						new ExEInternoCapturado(doc),

						new ExEstaAssinadoComSenha(doc)

				)

		);

	}

}