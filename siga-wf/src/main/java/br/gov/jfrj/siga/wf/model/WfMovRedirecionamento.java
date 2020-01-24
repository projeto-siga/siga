package br.gov.jfrj.siga.wf.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("R")
public class WfMovRedirecionamento extends WfMovTransicao {

}
