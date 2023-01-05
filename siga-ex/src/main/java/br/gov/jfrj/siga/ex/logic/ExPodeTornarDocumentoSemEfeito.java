package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.If;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeTornarDocumentoSemEfeito extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeTornarDocumentoSemEfeito(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		if (mob.isGeralDeProcesso() && mob.doc().isFinalizado())
			mob = mob.doc().getUltimoVolumeOuGeral();
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível tornar um documento sem efeito, segundo as regras a
	 * seguir:
	 * 
	 * 
	 * <ul>
	 * <li>Móbil tem de ser via ou volume (não pode ser geral)</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o usuário / móbil</li>
	 * <li>Móbil não pode estar juntado</li>
	 * <li>Móbil não pode estar em trânsito</li>
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

				Not.of(new ExEstaSemEfeito(mob.doc())),

				new ExEEletronico(mob.doc()),

				Or.of(new ExECadastrante(mob.doc(), titular),

						new ExESubscritor(mob.doc(), titular),

						new ExECossignatario(mob.doc(), titular),

						new ExELotacaoCadastrante(mob.doc(), lotaTitular)),

				If.of(new ExECapturado(mob.doc()),

						And.of(Not.of(new ExEDocFilhoJuntadoAoPai(mob.doc())),
								Or.of(new ExECadastrante(mob.doc(), titular), new ExESubscritor(mob.doc(), titular)),
								new ExPodePorConfiguracao(titular, lotaTitular)
										.withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
										.withExTpMov(ExTipoDeMovimentacao.TORNAR_SEM_EFEITO)
										.withCargo(titular.getCargo())
										.withDpFuncaoConfianca(titular.getFuncaoConfianca())
										.withExFormaDoc(mob.doc().getExFormaDocumento())
										.withExMod(mob.doc().getExModelo())),

						new ExESubscritor(mob.doc(), titular)

				),

				Not.of(new ExEstaAgendadaPublicacaoNoDiario(mob.doc())),
				Not.of(new ExEstaSolicitadaPublicacaoNoDiario(mob.doc())),
				Not.of(new ExEstaPublicadoNoBoletim(mob.doc())), Not.of(new ExEstaPublicadoNoDiario(mob.doc())),

				new ExPodeMovimentarPorConfiguracao(ExTipoDeMovimentacao.TORNAR_SEM_EFEITO, titular,
						lotaTitular));
	}
}