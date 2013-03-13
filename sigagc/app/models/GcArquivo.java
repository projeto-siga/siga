package models;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.GenericModel;
import util.SigaPlayCalendar;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

@Entity
@Table(name = "GC_CONTEUDO")
public class GcArquivo extends GenericModel {
	@Id
	@GeneratedValue
	@Column(name = "ID_CONTEUDO")
	public long id;

	@Column(name = "TITULO_TEMP")
	public String titulo;

	@Column(name = "CONTEUDO_TEMP")
	public String conteudo;

	@Column(name = "CLASSIFICACAO_TEMP")
	public String classificacao;
}
