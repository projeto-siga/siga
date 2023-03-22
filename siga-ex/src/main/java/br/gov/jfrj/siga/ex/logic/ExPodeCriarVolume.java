package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.NAnd;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpNaoENulo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeCriarVolume extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private ExMobil ultVolume;

	public ExPodeCriarVolume(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;

		ultVolume = mob.doc().getUltimoVolume();
	}

	/**
	 * Retorna se é possível criar volume para o documento que contém o móbil
	 * passado por parâmetro, de acordo com as seguintes condições:
	 * <ul>
	 * <li>Documento tem de ser processo</li>
	 * <li>Processo tem de estar finalizado</li>
	 * <li>Último volume tem de estar encerrado</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 */
	@Override
	protected Expression create() {
		return And.of(

				Not.of(new ExEstaSemEfeito(mob.doc())),

				new ExEProcesso(mob.doc()),

				NAnd.of(

						new CpNaoENulo(ultVolume, "último volume"),

						Or.of(

								new ExEstaEmTransito(ultVolume, titular, lotaTitular),

								new ExEstaSobrestado(ultVolume))),

				Not.of(new ExEstaArquivado(mob)),

				new ExPodeMovimentar(mob, titular, lotaTitular),

				Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),

				new ExEstaFinalizado(mob.doc()),

				new ExEstaEncerrado(ultVolume),
				
				Not.of(new ExEstaJuntadoAOutroProcesso(mob)),

				NAnd.of(

						new ExEEletronico(mob.doc()),

						Or.of(

								new ExTemAnexosNaoAssinados(mob),

								new ExTemDespachosNaoAssinados(mob))));
	}
}
