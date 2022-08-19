package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeAgendarPublicacaoDOE extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public ExPodeAgendarPublicacaoDOE(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.doc = mob.doc();
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(

				Not.of(new ExEstaPendenteDeAssinatura(doc)),

				Not.of(new ExEstaSemEfeito(doc)),

				Not.of(new ExEstaEliminado(mob)),

				Not.of(new ExEstaArquivado(mob)),
				
				Not.of(new ExEstaAgendadaPublicacaoNoDOE(doc, titular)),
	
				new ExPodePorConfiguracao(titular, lotaTitular).withExMod(mob.doc().getExModelo())
					.withExFormaDoc(mob.doc().getExFormaDocumento())
					.withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
					.withExTpMov(ExTipoDeMovimentacao.AGENDAR_PUBLICACAO_DOE));
	}
}