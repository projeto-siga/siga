package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.If;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

public class ExPodeIncluirCossignatario extends CompositeExpressionSupport {

	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível incluir cossignatário no documento que contém o
	 * móbil passado por parâmetro. O documento tem de atender as seguintes
	 * condições:
	 * <ul>
	 * <li>Não pode estar cancelado</li>
	 * <li>Sendo documento físico, não pode estar finalizado</li>
	 * <li>Sendo documento digital, não pode estar assinado</li>
	 * <li>Lotação do usuário tem de ser a lotação cadastrante do documento</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public ExPodeIncluirCossignatario(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
		this.doc = doc;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	public ExPodeIncluirCossignatario(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.doc = mob.doc();
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(

				new ExTemSubscritor(doc),

				Not.of(new ExEstaCancelado(doc)),

				If.of(new ExEEletronico(doc),

						Not.of(new ExEstaAssinadoOuAutenticadoComTokenOuSenhaERegistros(doc)),

						Not.of(new ExEstaFinalizado(doc))),

				new ExEstaPendenteDeAssinatura(doc),

				new ExECadastrante(doc, lotaTitular),

				new ExPodeMovimentarPorConfiguracao(ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO,
						titular, lotaTitular));
	}
}
