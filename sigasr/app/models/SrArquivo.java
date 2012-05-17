package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
@Table(name="SR_ARQUIVO")
public class SrArquivo extends GenericModel{
	
	@Id
	@Column(name = "ID_ARQUIVO")
	public long id;
	
	@Lob
	public byte[] blob;
	
	@Column(name="MIME")
	public String mime;
	
	@Column(name="NOME_ARQUIVO")
	public String nomeArquivo;
	
	@Column(name="DESCRICAO")
	public String descricao;

}
