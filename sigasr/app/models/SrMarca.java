package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.gov.jfrj.siga.dp.CpMarca;

@Entity
@DiscriminatorValue("2")
public class SrMarca extends CpMarca{

	@ManyToOne
	@JoinColumn(name = "ID_REF")
	public SrSolicitacao solicitacao;
	
}
