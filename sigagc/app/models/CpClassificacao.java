package models;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Historico;

@Entity
@Table(name = "CP_CLASSIFICACAO")
public class CpClassificacao extends GenericModel implements Historico {
	@Id
	@GeneratedValue
	@Column(name = "ID_CLASSIFICACAO")
	public long id;

	@Column(name = "CODIGO_CLASSIFICACAO")
	public String codigo;

	@Column(name = "TITULO_CLASSIFICACAO")
	public String titulo;

	@Column(name = "DESC_CLASSIFICACAO")
	public String desc;

	@ManyToOne(optional = false)
	public CpTipoClassificacao tipo;

	@Column(name = "IDE_CLASSIFICACAO")
	public String ide;

	@Column(name = "HIS_ID_INI")
	public Long hisIdIni;

	@Column(name = "HIS_DT_INI")
	public Date hisDtIni;

	@Column(name = "HIS_DT_FIM")
	public Date hisDtFim;

	@Column(name = "HIS_ATIVO")
	public int hisAtivo;

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		if (!(obj instanceof CpClassificacao))
			return false;
		CpClassificacao other = (CpClassificacao) obj;
		if (getIdInicial() != other.getIdInicial())
			return false;
		if (!titulo.equals(other.titulo))
			return false;
		if (!desc.equals(other.desc))
			return false;
		return true;
	}

	@Override
	public boolean equivale(Object other) {
		return this.hisIdIni.equals(((Historico) other).getIdInicial());
	}

	@Override
	public Long getIdInicial() {
		return hisIdIni;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getHisIdIni() {
		return hisIdIni;
	}

	@Override
	public void setHisIdIni(Long hisIdIni) {
		this.hisIdIni = hisIdIni;
	}

	@Override
	public Date getHisDtIni() {
		return hisDtIni;
	}

	@Override
	public void setHisDtIni(Date hisDtIni) {
		this.hisDtIni = hisDtIni;
	}

	@Override
	public Date getHisDtFim() {
		return hisDtFim;
	}

	@Override
	public void setHisDtFim(Date hisDtFim) {
		this.hisDtFim = hisDtFim;
	}

	@Override
	public int getHisAtivo() {
		return hisAtivo;
	}

	@Override
	public void setHisAtivo(int hisAtivo) {
		this.hisAtivo = hisAtivo;
	}
}
