package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

public class ExEstaMarcadoComMarcador implements Expression {

	private ExMobil mob;
	private CpMarcador marcador;

	public ExEstaMarcadoComMarcador(ExMobil mob, CpMarcador marcador) {
		if (marcador == null)
			throw new RuntimeException("marcador não pode ser nulo");
		if (marcador.getIdInicial() == null)
			throw new RuntimeException("marcador não pode ter id inicial nula");
		if (mob == null)
			throw new RuntimeException("mobil não pode ser nulo");
		this.mob = mob;
		this.marcador = marcador;
	}

	@Override
	public boolean eval() {
		for (ExMovimentacao mov : mob.getMovimentacoesPorTipo(ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO, true)) {
			if (this.marcador.equivale(mov.getMarcador()))
				return true;
		}
		return false;
	}

	@Override
	public String explain(boolean result) {
		return mob.getCodigoCompacto() + " " + (result ? "" : JLogic.NOT) + "está marcado com "
				+ marcador.getDescrMarcador();
	}

}
