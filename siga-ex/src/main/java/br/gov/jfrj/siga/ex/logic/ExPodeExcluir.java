package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeExcluir extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível excluir o documento cujo móbil é o representado pelo
	 * parâmetro mob. As regras para o documento são as seguintes:
	 * <ul>
	 * <li>Documento não pode estar finalizado, seja físico ou eletrônico</li>
	 * <li>Lotação do usuário tem de ser a do cadastrante do documento</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param doc
	 * @param numVia
	 * @return
	 * @throws Exception
	 */
	public ExPodeExcluir(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.doc = mob.doc();
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(

				Not.of(new ExEstaFinalizado(doc)),

				new ExECadastrante(doc, lotaTitular),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.EXCLUIR));
	}
}