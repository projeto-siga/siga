package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeFinalizar extends CompositeExpressionSupport {

	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível finalizar o documento ao qual o móbil passado por
	 * parâmetro pertence. São estas as regras:
	 * <ul>
	 * <li>Documento não pode estar finalizado</li>
	 * <li>Se o documento for interno produzido, usuário tem de ser: 1) da lotação
	 * cadastrante do documento, 2) o subscritor do documento ou 3) o titular do
	 * documento. <b>Obs.: por que a origem do documento está sendo considerada
	 * nesse caso?</b></li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public ExPodeFinalizar(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
		this.doc = doc;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(

				Not.of(new ExEstaFinalizado(doc)),

				Not.of(new ExLotacaoEstaFechada(lotaTitular)),

				Not.of(new ExEstaPendenteDeAnexacao(doc.getMobilGeral())),

				Not.of(new ExEstaPendenteDeColaboracao(doc.getMobilGeral())),

				Or.of(

						new ExEInternoFolhaDeRosto(doc),

						new ExEExterno(doc),

						new ExECadastrante(doc, lotaTitular),

						new ExESubscritor(doc, titular),

						new ExETitular(doc, titular),

						new ExTemPerfil(doc, ExPapel.PAPEL_GESTOR, titular, lotaTitular),

						new ExTemPerfil(doc, ExPapel.PAPEL_REVISOR, titular, lotaTitular)),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.FINALIZAR));
	}
}
