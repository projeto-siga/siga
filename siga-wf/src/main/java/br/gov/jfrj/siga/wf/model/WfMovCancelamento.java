package br.gov.jfrj.siga.wf.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("C")
public class WfMovCancelamento extends WfMov {

	@Override
	public String getEvento() {
		return "Cancelamento";
	}

	@Override
	public String getDescricaoEvento() {
		return null;
	}

}
