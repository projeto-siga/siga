package br.gov.jfrj.siga.wf.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

public class WfTarefa {
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

	public WfProcedimento getInstanciaDeProcesso() {
		return pi;
	}

	public void setInstanciaDeProcesso(WfProcedimento pi) {
		this.pi = pi;
	}
}
