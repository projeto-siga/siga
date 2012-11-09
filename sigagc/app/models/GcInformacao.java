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
import javax.persistence.Table;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import play.db.jpa.GenericModel;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

@Entity
@Table(name = "CP_INFOMACAO")
public class GcInformacao extends GenericModel {
	@Id
	@GeneratedValue
	@Column(name = "ID_INFORMACAO")
	public long id;

	@Column(name = "LOCALIZADOR_INFORMACAO")
	public String localizador;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_TIPO_INFORMACAO")
	public GcTipoInformacao tipo;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_INFORMACAO_PAI")
	public GcInformacao informacaoPai;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_ORGAO_USUARIO")
	public CpOrgaoUsuario ou;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_PESSOA")
	public DpPessoa autor;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_LOTACAO")
	public DpLotacao lotacao;

	@Column(name = "DT_ELABORACAO_FIM")
	public Date elaboracaoFim;

	@Column(name = "TITULO_TEMP")
	public String titulo;

	@Column(name = "CONTEUDO_TEMP1")
	public String conteudo;

	@Sort(type = SortType.NATURAL)
	@OneToMany(mappedBy = "inf")
	public java.util.SortedSet<GcMarca> marcas;

	@OneToMany(mappedBy = "inf")
	public Set<GcMovimentacao> movs;

	@Column(name = "HIS_DT_INI")
	public Date hisDtIni;

	@Column(name = "HIS_DT_FIM")
	public Date hisDtFim;

	@ManyToOne(optional = false)
	@JoinColumn(name = "HIS_IDC_INI")
	public CpIdentidade hisIdcIni;
}
