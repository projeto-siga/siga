package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.NAnd;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeIncluirDocumento extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível utilizar o recurso Criar Anexo, com base nas seguintes
	 * regras:
	 * <ul>
	 * <li>Documento tem de estar finalizado</li>
	 * <li>Documento tem de ser interno produzido</li>
	 * <li>Móbil não pode estar em algum arquivo</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o usuário / móbil</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public ExPodeIncluirDocumento(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.doc = mob.doc();
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {

		return And.of(

				NAnd.of(

						new ExEMobilGeral(mob),

						Not.of(new ExEstaPendenteDeAssinatura(doc))),

				NAnd.of(

						new ExEProcesso(doc),

						new ExEstaArquivadoCorrente(mob)),

				Not.of(new ExEstaArquivado(mob)),

				Not.of(new ExEstaSemEfeito(doc)),

				Not.of(new ExEstaEncerrado(mob)),

				Not.of(new ExEstaSobrestado(mob)),

				Not.of(new ExEstaJuntado(mob)),

				new ExEstaFinalizado(doc),

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				new ExPodeMovimentar(mob, titular, lotaTitular),

				new ExPodePorConfiguracao(titular, lotaTitular).withExMod(mob.doc().getExModelo())
						.withExFormaDoc(mob.doc().getExFormaDocumento())
						.withIdTpConf(ExTipoDeConfiguracao.INCLUIR_DOCUMENTO));
	}
}