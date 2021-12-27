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

public class ExPodeEnviarSiafem extends CompositeExpressionSupport {
	
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeEnviarSiafem(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
		this.doc = doc;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}
	
	@Override
	protected Expression create() {
		return And.of(
				
				Or.of(
						
						new ExEstaAssinadoOuAutenticadoComTokenOuSenhaERegistros(doc),
				
						new ExEstaAssinadoComSenha(doc)),
				
				new ExPodeSerMovimentado(doc.getMobilGeral(), titular, lotaTitular),
				
				Not.of(new ExTemMovimentacaoNaoCanceladaDoTipo(doc.getPrimeiraVia(), ExTipoDeMovimentacao.ENVIO_SIAFEM)),
				
				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR).withExTpMov(ExTipoDeMovimentacao.ENVIO_SIAFEM).withExMod(doc.getExModelo()).withAceitarPode(false) );
				
	}

}
