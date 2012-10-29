package models;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Historico;

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

	@Column(name = "DT_INI")
	public Date dtIni;
}
