package models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.jfrj.siga.cp.CpConfiguracao;

@Entity
@Table(name = "RI_CONFIGURACAO")
@PrimaryKeyJoinColumn(name = "ID_CONFIGURACAO_RI")
public class RiConfiguracao extends CpConfiguracao
{

	public RiConfiguracao() {

	}

}
