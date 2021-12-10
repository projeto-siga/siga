package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExPodeAutuarDocumento extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeAutuarDocumento(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		super();
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível autuar um expediente, com base nas seguintes
	 * regras:
	 * <ul>
	 * <li>Documento tem de ser expediente</li>
	 * <li>Documento tem de estar assinado</li>
	 * <li>Documento não pode estar sem efeito</li>
	 * <li>Móbil não pode ser geral</li>
	 * <li>Móbil não pode estar em edital de eliminação</li>
	 * <li>Móbil não pode estar juntado</li>
	 * <li>Móbil não pode estar apensado</li>
	 * <li>Móbil não pode estar em trânsito</li>
	 * <li>Móbil não pode estar arquivado permanentemente</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o usuário / móbil</li>
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

				Not.of(new ExEstaPendenteDeAnexacao(mob)),

				Not.of(new ExEstaSemEfeito(mob.doc())),

				Not.of(new ExEstaEmEditalDeEliminacao(mob)),

				Not.of(new ExEstaArquivadoPermanente(mob)),

				Not.of(new ExEstaJuntado(mob)),

				Not.of(new ExEstaApensadoAVolumeDoMesmoProcesso(mob)),

				Not.of(new ExEstaArquivado(mob)),

				Not.of(new ExEstaSobrestado(mob)),

				Not.of(new ExEMobilGeral(mob)),

				new ExEExpediente(mob.doc()),

				Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				new ExPodeMovimentar(mob, titular, lotaTitular),

				new ExPodePorConfiguracao(titular, lotaTitular).withExMod(mob.doc().getExModelo())
						.withExFormaDoc(mob.doc().getExFormaDocumento()).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoDeMovimentacao.AUTUAR));

	}
};
