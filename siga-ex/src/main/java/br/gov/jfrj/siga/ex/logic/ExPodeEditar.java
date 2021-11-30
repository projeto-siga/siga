package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.If;
import com.crivano.jlogic.NAnd;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeEditar extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível editar um documento, conforme as seguintes regras:
	 * <ul>
	 * <li>Se o documento for físico, não pode estar finalizado</li>
	 * <li>Documento não pode estar cancelado</li>
	 * <li>Se o documento for digital, não pode estar assinado</li>
	 * <li>Usuário tem de ser 1) da lotação cadastrante do documento, 2)subscritor
	 * do documento ou 3) titular do documento</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public ExPodeEditar(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.doc = mob.doc();
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(

				If.of(

						new ExEEletronico(doc),

						Not.of(new ExEstaAssinadoOuAutenticadoComTokenOuSenhaERegistros(doc)),

						Not.of(new ExEstaFinalizado(doc))),

				Not.of(new ExEstaCancelado(doc)),

				Not.of(new ExEstaSemEfeito(doc)),

				NAnd.of(

						new ExECapturado(doc),

						Not.of(new ExEstaPendenteDeAssinatura(doc))),

				Or.of(

						new ExECadastrante(doc, lotaTitular),

						new ExESubscritor(doc, titular),

						new ExETitular(doc, titular),

						new ExTemPerfil(doc, ExPapel.PAPEL_GESTOR, titular, lotaTitular),

						new ExTemPerfil(doc, ExPapel.PAPEL_REVISOR, titular, lotaTitular)),

				new ExPodePorConfiguracao(titular, lotaTitular).withExMod(mob.doc().getExModelo())
						.withExFormaDoc(mob.doc().getExFormaDocumento()).withIdTpConf(ExTipoDeConfiguracao.CRIAR),

				new ExPodePorConfiguracao(titular, lotaTitular).withExMod(mob.doc().getExModelo())
						.withExFormaDoc(mob.doc().getExFormaDocumento()).withIdTpConf(ExTipoDeConfiguracao.EDITAR));
	}
}