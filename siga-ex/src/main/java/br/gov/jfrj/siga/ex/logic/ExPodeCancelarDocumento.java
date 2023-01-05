package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.If;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;

public class ExPodeCancelarDocumento extends CompositeExpressionSupport {

	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível cancelar documento pendente de assinatura, segundo as
	 * regras a seguir:
	 * 
	 * 
	 * <ul>
	 * <li>Móbil tem de ser via ou volume (não pode ser geral)</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o usuário / móbil</li>
	 * *
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public ExPodeCancelarDocumento(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
		this.doc = doc;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(

				Not.of(new ExEstaCancelado(doc)), 
				
				new ExEstaFinalizado(doc),
	
				Or.of(Not.of(new ExEEletronico(doc))),

				If.of(new ExECapturado(doc),

						And.of(Not.of(new ExEDocFilhoJuntadoAoPai(doc)), new ExECadastrante(doc, titular)),
						
						Or.of(new ExESubscritor(doc, titular),new ExECadastrante(doc, lotaTitular))),


				Or.of(new ExECadastrante(doc, titular),

						new ExESubscritor(doc, titular),

						new ExECossignatario(doc, titular),

						new ExELotacaoCadastrante(doc, lotaTitular))
		);
	}
}