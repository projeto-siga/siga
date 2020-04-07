package br.gov.jfrj.siga.wf.util;

import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfProcedimento;

public class WfTarefa implements Comparable<WfTarefa> {
	WfDefinicaoDeTarefa td;

	public WfTarefa(WfDefinicaoDeTarefa td, WfProcedimento pi) {
		super();
		this.td = td;
		this.pi = pi;
	}

	public WfTarefa(WfProcedimento pi) {
		super();
		this.td = pi.getCurrentTaskDefinition();
		this.pi = pi;
	}

	WfProcedimento pi;

	public WfDefinicaoDeTarefa getDefinicaoDeTarefa() {
		return td;
	}

	public void setDefinicaoDeTarefa(WfDefinicaoDeTarefa td) {
		this.td = td;
	}

	public WfProcedimento getInstanciaDeProcedimento() {
		return pi;
	}

	public void setInstanciaDeProcesso(WfProcedimento pi) {
		this.pi = pi;
	}

	@Override
	public int compareTo(WfTarefa o) {
		int i = 0;
		if (o == null)
			return 1;
		if (getInstanciaDeProcedimento().getEventoData() != null
				&& o.getInstanciaDeProcedimento().getEventoData() == null)
			return 1;
		if (getInstanciaDeProcedimento().getEventoData() != null
				&& o.getInstanciaDeProcedimento().getEventoData() != null) {
			i = getInstanciaDeProcedimento().getEventoData().compareTo(o.getInstanciaDeProcedimento().getEventoData());
			if (i != 0)
				return i;
		}
		if (getInstanciaDeProcedimento().getId() != null && o.getInstanciaDeProcedimento().getId() == null)
			return 1;
		if (getInstanciaDeProcedimento().getId() != null && o.getInstanciaDeProcedimento().getId() != null) {
			i = getInstanciaDeProcedimento().getId().compareTo(o.getInstanciaDeProcedimento().getId());
			if (i != 0)
				return i;
		}
		return 0;
	}
}
