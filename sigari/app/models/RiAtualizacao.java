package models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name = "RI_ATUALIZACAO")
public class RiAtualizacao extends GenericModel implements Serializable {
	@Id
	@SequenceGenerator(sequenceName = "SIGAGC.ri_hibernate_sequence", name = "riAtualizacaoSeq")
	@GeneratedValue(generator = "riAtualizacaoSeq")
	@Column(name = "ID_ATUALIZACAO")
	public long id;

	@Column(name = "SIGLA_ATUALIZACAO")
	public String sigla; 
	
	@Column(name = "NOME_ATUALIZACAO")
	public String nome;
	
	@Column(name = "URL_ATUALIZACAO")
	public String uri;
	
	@Column(name = "DT_ULTIMA_ATUALIZACAO")
	public Date ultimaAtualizacao;
	
	@Column(name = "DESEMPATE_ATUALIZACAO")
	public Long idDesempate;

	public RiAtualizacao() {
		
	}

}
