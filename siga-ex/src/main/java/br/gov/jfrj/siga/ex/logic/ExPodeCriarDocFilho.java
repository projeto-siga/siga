package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeCriarDocFilho extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeCriarDocFilho(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		if (mob.isGeralDeProcesso() && mob.doc().isFinalizado())
			mob = mob.doc().getUltimoVolumeOuGeral();
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível criar um documento filho do móbil passado como
	 * parâmetro, de acordo com as regras:
	 * <ul>
	 * <li>Documento não pode estar cancelado</li>
	 * <li>Volume não pode estar encerrado, pelo fato de documento filho representar
	 * conteúdo agregado ao móbil</li>
	 * <li>Móbil não pode estar em algum arquivo</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o usuário / móbil</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	@Override
	protected Expression create() {
		return And.of(

				new ExEProcesso(mob.doc()), new ExEMobilGeral(mob), Not.of(new ExTemMobilPai(mob.doc())),

				Not.of(new ExEstaCancelado(mob.doc())), Not.of(new ExEstaSemEfeito(mob.doc())),
				Not.of(new ExEstaEncerrado(mob)), Not.of(new ExEstaArquivado(mob)),

				new ExPodeMovimentar(mob, titular, lotaTitular),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.CRIAR_DOC_FILHO));
	}
}