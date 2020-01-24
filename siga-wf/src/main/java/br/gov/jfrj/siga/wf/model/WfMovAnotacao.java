package br.gov.jfrj.siga.wf.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@DiscriminatorValue("A")
public class WfMovAnotacao extends WfMov {
}
