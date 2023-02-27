package br.gov.jfrj.siga.ex.logic;

import java.util.ArrayList;
import java.util.List;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpIgual;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeRestringirAcesso extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private List<ExMovimentacao> listMovJuntada;

	public ExPodeRestringirAcesso(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
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

	@Override
	protected Expression create() {
		return And.of(

				new ExPodePorConfiguracao(titular, lotaTitular)
						.withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoDeMovimentacao.RESTRINGIR_ACESSO),
				
				Or.of(
						And.of(
								Not.of(new ExEstaFinalizado(doc)),
								
								new ExECadastrante(doc, lotaTitular)),
						
						new ExPodeMovimentar(mob.getDoc().getMobilDefaultParaReceberJuntada(), titular, lotaTitular)),			
				
				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				Or.of(

						Not.of(new ExPodePorConfiguracao(titular, lotaTitular).withExMod(mob.doc().getExModelo())
								.withIdTpConf(ExTipoDeConfiguracao.INCLUIR_DOCUMENTO)),

						Not.of(new ExTemMobilPai(mob.doc()))),

				new CpIgual(listMovJuntada.size(), "juntadas", 0, "zero"));
	}
}