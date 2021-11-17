package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.NAnd;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.logic.CpEGovSP;
import br.gov.jfrj.siga.cp.logic.CpNaoENulo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeFazerCiencia extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeFazerCiencia(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		super();
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/*
	 * Retorna se é possível incluir ciencia do documento a que pertence o móbil
	 * passado por parâmetro, conforme as seguintes condições: <ul> <li>Documento
	 * não foi tramitado</li> <li>Documento tem de estar assinado ou
	 * autenticado</li> <li>Documento não pode estar juntado a outro</li>
	 * <li>Usuario não fez ciência ainda</li> <li>Não pode haver configuração
	 * impeditiva</li> </ul>
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
	@Override
	protected Expression create() {
		return And.of(

				NAnd.of(new ExEExternoCapturado(mob.doc()), Not.of(new ExEstaAutenticadoComTokenOuSenha(mob.doc()))),

				new CpEGovSP(),

				Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),

				Not.of(new ExEstaCiente(mob, titular, lotaTitular)),

				Not.of(new ExEstaEmTransito(mob, titular, lotaTitular)),

				Not.of(new ExEstaEliminado(mob)),

				Not.of(new ExEstaJuntado(mob)),

				Not.of(new ExEstaArquivado(mob)),

				Not.of(new ExEstaEncerrado(mob)),

				new ExPodePorConfiguracao(titular, lotaTitular).withExMod(mob.doc().getExModelo())
						.withExFormaDoc(mob.doc().getExFormaDocumento()).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CIENCIA));

	}
};
