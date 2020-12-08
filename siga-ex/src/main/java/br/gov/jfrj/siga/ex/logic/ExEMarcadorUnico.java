package br.gov.jfrj.siga.ex.logic;

import com.crivano.jlogic.CompositeExpressionSuport;
import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.dp.CpMarcador;

public class ExEMarcadorUnico extends CompositeExpressionSuport {

	private CpMarcador marcador;

	public ExEMarcadorUnico(CpMarcador marcador) {
		this.marcador = marcador;
	}

	@Override
	protected Expression create() {
		return new ExEMarcadorDeAtendente(marcador);
	}

};
