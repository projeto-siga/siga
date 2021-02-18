package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.Expression;
import com.crivano.jlogic.JLogic;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorTipoInteressadoEnum;
import br.gov.jfrj.siga.dp.CpMarcador;

public class ExEMarcadorDeAtendente implements Expression {

	private CpMarcador marcador;

	public ExEMarcadorDeAtendente(CpMarcador marcador) {
		this.marcador = marcador;
	}

	@Override
	public boolean eval() {
		return marcador.getIdFinalidade().getIdTpInteressado() == CpMarcadorTipoInteressadoEnum.ATENDENTE;
	}

	@Override
	public String explain(boolean result) {
		return JLogic.explain("é marcador de atendente", result);
	}

}