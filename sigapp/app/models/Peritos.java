/**
 * 
 */
package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

/**
 * @author Herval 11267
 */
@Entity(name="peritos")
@Table(name="peritos" , schema="SIGAPMP")
public class Peritos extends GenericModel {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

@Id()
@Column(name="cpf_perito" , length=50 , nullable=false )
public String cpf_perito;

@Column(name="nome_perito" , length=200 , nullable=true )
public String nome_perito;

 public Peritos( String cpf_perito_construt , String nome_perito_construt ){
	this.cpf_perito = cpf_perito_construt;
	this.nome_perito = nome_perito_construt;
 }
}
