package br.gov.jfrj.siga.wf.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("C")
public class WfMovCancelamento extends WfMov {

}
