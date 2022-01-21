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

public class ExPodePublicar extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodePublicar(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Retorna se é possível publicar o Boletim Interno que possui mob, segundo as
	 * regras:
	 * <ul>
	 * <li>Documento tem de estar finalizado</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o usuário / móbil</li>
	 * <li>Documento tem de estar assinado</li>
	 * <li>Documento não pode estar publicado</li>
	 * <li>Não pode haver configuração impeditiva. Tipo de configuração: Movimentar
	 * / Publicação Boletim</li>
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

				new ExEstaFinalizado(mob.doc()),

				new ExPodeMovimentar(mob, titular, lotaTitular),

				Not.of(new ExEstaPendenteDeAssinatura(mob.doc())),

				Not.of(new ExTemBoletimPublicado(mob.doc())),

				Not.of(new ExEstaSemEfeito(mob.doc())),

				Not.of(new ExEstaEliminado(mob.doc().getMobilGeral())),

				Not.of(new ExEstaPendenteDeAnexacao(mob)),

				new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(ExTipoDeConfiguracao.MOVIMENTAR)
						.withExTpMov(ExTipoDeMovimentacao.PUBLICACAO_BOLETIM)
						.withExFormaDoc(mob.doc().getExFormaDocumento()).withExMod(mob.doc().getExModelo()));
	}
}