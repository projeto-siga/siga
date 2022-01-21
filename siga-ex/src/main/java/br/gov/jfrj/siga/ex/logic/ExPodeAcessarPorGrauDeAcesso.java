package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSupport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExNivelAcesso;

public class ExPodeAcessarPorGrauDeAcesso extends CompositeExpressionSupport {

	private ExMobil mob;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeAcessarPorGrauDeAcesso(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		switch (mob.doc().getExNivelAcessoAtual().getGrauNivelAcesso().intValue()) {
		case (int) ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS:
			return new ExPodeAcessarNivel20(mob, titular, lotaTitular);
		case (int) ExNivelAcesso.NIVEL_ACESSO_PUBLICO:
			return new ExPodeAcessarPublico(mob, titular, lotaTitular);
		case (int) ExNivelAcesso.NIVEL_ACESSO_PESSOA_SUB:
			return new ExPodeAcessarNivel30(mob, titular, lotaTitular);
		case (int) ExNivelAcesso.NIVEL_ACESSO_SUB_PESSOA:
			return new ExPodeAcessarNivel40(mob, titular, lotaTitular);
		case (int) ExNivelAcesso.NIVEL_ACESSO_ENTRE_LOTACOES:
			return new ExPodeAcessarNivel60(mob, titular, lotaTitular);
		case (int) ExNivelAcesso.NIVEL_ACESSO_PESSOAL:
			return new ExPodeAcessarNivel100(mob, titular, lotaTitular);
		default:
			throw new RuntimeException("Nível de acesso desconhecido");
		}
	}
}