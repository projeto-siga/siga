package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.cp.CpTipoMarcadorEnum;
import br.gov.jfrj.siga.dp.CpMarcador;

public class ExEMarcadorDeSistema implements Expression {

	private CpMarcador marcador;

	public ExEMarcadorDeSistema(CpMarcador marcador) {
		this.marcador = marcador;
	}

	@Override
	public boolean eval() {
		return marcador.getIdFinalidade().getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_SISTEMA;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é marcador de sistema", result);
	}

}
