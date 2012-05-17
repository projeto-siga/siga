package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
@Table(name="SR_ANDAMENTO")
public class SrAndamento extends GenericModel{
	
	@Id
	@GeneratedValue
	@Column(name = "ID_ACOMPANHAMENTO")
	public long id;
	
	@Column(name="DESCRICAO")
	public String descricao;
	
	@ManyToOne
	@JoinColumn(name = "ID_ARQUIVO")
	public SrArquivo arquivo;
	
	@ManyToOne
	@JoinColumn(name = "ID_ATENDENTE")
	public DpPessoa atendente;
	
	@ManyToOne
	@JoinColumn(name = "ID_LOTA_ATENDENTE")
	public DpLotacao lotaAtendente;

}
