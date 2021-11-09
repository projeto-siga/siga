package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeCriarSubprocesso extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeCriarSubprocesso(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		if (mob.isGeralDeProcesso() && mob.doc().isFinalizado())
			mob = mob.doc().getUltimoVolume();
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível criar subprocesso, segundo as regras abaixo:
	 * <ul>
	 * <li>Documento tem de ser processo</li>
	 * <li>Móbil não pode ser geral</li>
	 * 
	 * <li>Documento não pode ter um móbil pai</li>
	 * <li>Documento não pode estar cancelado</li>
	 * <li>Usuário tem de ter permissão para acessar o documento que contém o móbil.
	 * <b>É mesmo necessário verificar isso?</b></li>
	 * <li>Não pode haver configuração impeditiva. Tipo de configuração: Criar
	 * Documento Filho</li>
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
				Not.of(new ExEstaArquivado(mob)), Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),

				new ExPodeAcessarDocumento(mob, titular, lotaTitular),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.CRIAR_DOC_FILHO));
	}
}