package models.siga;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.db.jpa.GenericModel;

import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpTipoMarca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Objeto;

@Entity()
@Table(name = "CP_MARCA", schema = "CORPORATIVO")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ID_TP_MARCA", discriminatorType = DiscriminatorType.INTEGER

)
public abstract class PlayMarca extends GenericModel implements Serializable {

	@Id
	// @SequenceGenerator(name="my_seq", sequenceName="CP_MARCA_SEQ")
	// Desabilitando sequence. Ver comentário em
	// sigasr.controllers.Util.nextVal() @GeneratedValue(generator = "my_seq")
	@Column(name = "ID_MARCA")
	private java.lang.Long idMarca;

	@Column(name = "DT_INI_MARCA")
	@Temporal(TemporalType.DATE)
	private Date dtIniMarca;

	@Column(name = "DT_FIM_MARCA")
	@Temporal(TemporalType.DATE)
	private Date dtFimMarca;

	@ManyToOne
	@JoinColumn(name = "ID_MARCADOR")
	private CpMarcador cpMarcador;

	@ManyToOne
	@JoinColumn(name = "ID_PESSOA_INI")
	private DpPessoa dpPessoaIni;

	@ManyToOne
	@JoinColumn(name = "ID_LOTACAO_INI")
	private DpLotacao dpLotacaoIni;

	@ManyToOne
	@JoinColumn(name = "ID_TP_MARCA", insertable = false, updatable = false)
	private CpTipoMarca cpTipoMarca;

	public CpTipoMarca getCpTipoMarca() {
		return cpTipoMarca;
	}

	public void setCpTipoMarca(CpTipoMarca cpTipoMarca) {
		this.cpTipoMarca = cpTipoMarca;
	}

	public java.lang.Long getIdMarca() {
		return idMarca;
	}

	public void setIdMarca(java.lang.Long idMarca) {
		this.idMarca = idMarca;
	}

	public Date getDtIniMarca() {
		return dtIniMarca;
	}

	public void setDtIniMarca(Date dtIniMarca) {
		this.dtIniMarca = dtIniMarca;
	}

	public Date getDtFimMarca() {
		return dtFimMarca;
	}

	public void setDtFimMarca(Date dtFimMarca) {
		this.dtFimMarca = dtFimMarca;
	}

	public CpMarcador getCpMarcador() {
		return cpMarcador;
	}

	public void setCpMarcador(CpMarcador cpMarcador) {
		this.cpMarcador = cpMarcador;
	}

	public DpPessoa getDpPessoaIni() {
		return dpPessoaIni;
	}

	public void setDpPessoaIni(DpPessoa dpPessoaIni) {
		this.dpPessoaIni = dpPessoaIni;
	}

	public DpLotacao getDpLotacaoIni() {
		return dpLotacaoIni;
	}

	public void setDpLotacaoIni(DpLotacao dpLotacaoIni) {
		this.dpLotacaoIni = dpLotacaoIni;
	}

	public String getDtIniMarcaDDMMYYYY() {
		if (getDtIniMarca() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(getDtIniMarca());
		}
		return "";
	}

}
