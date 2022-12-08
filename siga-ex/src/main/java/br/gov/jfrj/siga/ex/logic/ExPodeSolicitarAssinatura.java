package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeSolicitarAssinatura extends CompositeExpressionSupport {

	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/*
	 * Retorna se pode autenticar um documento que só foi assinado com senha.
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
	public ExPodeSolicitarAssinatura(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
		this.doc = doc;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(

				Not.of(new ExEstaAssinadoPorTodosOsSignatariosComTokenOuSenha(doc)),

				Not.of(new ExTemAssinaturaSolicitada(doc)),

				new ExTemLotaSubscritor(doc),

				new ExPodePorConfiguracao(titular, lotaTitular).withExMod(doc.getExModelo())
						.withExFormaDoc(doc.getExFormaDocumento()).withPessoaObjeto(doc.getSubscritor())
						.withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoDeMovimentacao.SOLICITACAO_DE_ASSINATURA)

		);

	}
}