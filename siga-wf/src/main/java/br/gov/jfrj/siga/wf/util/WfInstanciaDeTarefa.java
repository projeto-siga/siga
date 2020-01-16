package br.gov.jfrj.siga.wf.util;

import br.gov.jfrj.siga.wf.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.WfInstanciaDeProcedimento;

public class WfInstanciaDeTarefa {
	WfDefinicaoDeTarefa td;

	public WfInstanciaDeTarefa(WfDefinicaoDeTarefa td, WfInstanciaDeProcedimento pi) {
		super();
		this.td = td;
		this.pi = pi;
	}

	WfInstanciaDeProcedimento pi;

	public WfDefinicaoDeTarefa getDefinicaoDeTarefa() {
		return td;
	}

	public void setDefinicaoDeTarefa(WfDefinicaoDeTarefa td) {
		this.td = td;
	}

	public WfInstanciaDeProcedimento getInstanciaDeProcesso() {
		return pi;
	}

	public void setInstanciaDeProcesso(WfInstanciaDeProcedimento pi) {
		this.pi = pi;
	}
}
