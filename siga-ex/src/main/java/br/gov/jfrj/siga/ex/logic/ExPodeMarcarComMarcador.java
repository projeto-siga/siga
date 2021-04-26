package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.And;
import com.crivano.jlogic.CompositeExpressionSuport;
import com.crivano.jlogic.Expression;
import com.crivano.jlogic.Not;
import com.crivano.jlogic.Or;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;

public class ExPodeMarcarComMarcador extends CompositeExpressionSuport {

	private ExMobil mob;
	private CpMarcador marcador;
	private DpPessoa titular;
	private DpLotacao lotaTitular;

	public ExPodeMarcarComMarcador(ExMobil mob, CpMarcador marcador, DpPessoa titular, DpLotacao lotaTitular) {
		this.mob = mob;
		this.marcador = marcador;
		this.titular = titular;
		this.lotaTitular = lotaTitular;
	}

	@Override
	protected Expression create() {
		return And.of(
				Or.of(Not.of(new ExEMarcadorUnico(marcador)),
						Not.of(new ExEstaMarcadoComMarcadorOuOGeral(mob, marcador))),
				new ExPodeMarcar(mob, titular, lotaTitular));
	}

	public static void afirmar(ExMobil mob, CpMarcador marcador, DpPessoa titular, DpLotacao lotaTitular) {
		ExPodeMarcarComMarcador teste = new ExPodeMarcarComMarcador(mob, marcador, titular, lotaTitular);
		if (!teste.eval())
			throw new AplicacaoException(
					"Não é possível marcar com " + marcador.getDescrMarcador() + " porque " + AcaoVO.Helper.produzirExplicacao(teste, false));
	}

};