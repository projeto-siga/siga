package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;

public class ExPodeRefazer extends CompositeExpressionSupport {

	private ExMobil mob;
	private ExDocumento doc;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private ExMobil mobVerif;

	public ExPodeRefazer(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.doc = this.mob.doc();
		this.titular = titular;
		this.lotaTitular = lotaTitular;

		this.mobVerif = mob.isApensadoAVolumeDoMesmoProcesso() ? mob.doc().getUltimoVolume() : mob;

	}

	/**
	 * Retorna se é possível refazer um documento. Têm de ser satisfeitas as
	 * seguintes condições:
	 * <ul>
	 * <li>Documento tem de estar finalizado</li>
	 * <li>Usuário tem de ser o subscritor ou o titular do documento ou ser da
	 * lotação cadastrante do documento</li>
	 * <li>Documento não pode estar assinado, a não ser que seja dos tipos externo
	 * ou interno importado, que são naturalmente considerados assinados. Porém, se
	 * for documento de um desses tipos, não pode haver pdf anexado <b>(verificar
	 * por quê)</b></li>
	 * <li>Documento tem de possuir via não cancelada ou volume não cancelado</li>
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

				new ExEstaFinalizado(doc),

				Not.of(new ExTemJuntados(mob)),

				Or.of(

						new ExECadastrante(doc, lotaTitular),

						new ExESubscritor(doc, titular),

						new ExETitular(doc, titular)),

				Not.of(new ExEColaborativo(doc)),

				Or.of(

						new ExEstaPendenteDeAssinatura(doc),

						And.of(

								Not.of(new ExEInternoProduzido(doc)),

								Not.of(new ExTemPDF(doc)))),

				Not.of(new ExEstaCancelado(doc)),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.REFAZER)
						.withExMod(mob.doc().getExModelo()));
	}
}
