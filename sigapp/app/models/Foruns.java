package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.OptimisticLock;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Set;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;
/**
 * @author Herval, Edson da Rocha, e Ruben
 */
@Entity(name = "Foruns")
@Table(name = "Foruns")
public class Foruns extends GenericModel {
	@Id()
	@Column(name="cod_forum",length=10, nullable=false)
	public int cod_forum;
	
	@OneToMany(mappedBy="forumFk") // isso não é campo, mas um 'references'.
	public List<Locais> lstLocal;
	
	@OneToMany(mappedBy="forumFk") // isso não é campo, mas um 'references'.
	public List<UsuarioForum> lstUsuarioForum;
	
	@Column(name="descricao_forum" , length=40, nullable=true)
	public String descricao_forum;
		
	public Foruns(int cod_forum_construt, String descricao_construt) {
		this.cod_forum = cod_forum_construt;
		this.descricao_forum = descricao_construt;
	}
}
