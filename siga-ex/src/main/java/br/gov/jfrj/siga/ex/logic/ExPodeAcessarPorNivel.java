package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.cp.logic.CpENulo;
import br.gov.jfrj.siga.cp.logic.CpNaoENulo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExPodeAcessarPorNivel extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	/**
	 * Retorna se é possível acessar o documento a que pertence o móbil passado por
	 * parâmetro, considerando seu nível de acesso, e também o seguinte:
	 * <ul>
	 * <li>Se há documento pai e usuário pode acessá-lo <i>por nível</i>, retorna
	 * verdadeiro</li>
	 * <li>Se o usuário tem perfil vinculado ao documento, retorna verdadeiro</li>
	 * <li>Nos demais casos, retorna conforme a resposta de
	 * <i>podeAcessarPorNivelN()</i>, dependendo do grau do nível de acesso do
	 * documento</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public ExPodeAcessarPorNivel(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(new CpNaoENulo(mob, mob != null ? mob.getSigla() : "via ou volume"),
				Or.of(new CpENulo(mob.doc().getExNivelAcessoAtual(), "nível de acesso atual"),
						new ExPodeAcessarPorNivel(mob.getExMobilPai(), titular, lotaTitular),
						new ExEstaVinculadaPorPerfil(mob.doc(), titular, lotaTitular),
						new ExESubscritorOuCossignatario(mob.doc(), titular),
						new ExESubscritorDeDespacho(mob.doc(), titular),
						new ExPodeAcessarPorGrauDeAcesso(mob, titular, lotaTitular)));
	}
}