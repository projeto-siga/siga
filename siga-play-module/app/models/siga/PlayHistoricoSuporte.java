package models.siga;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Historico;

@MappedSuperclass
public abstract class PlayHistoricoSuporte extends GenericModel implements
		Historico {

	@Column(name = "HIS_ID_INI")
	private Long hisIdIni;

	@Column(name = "HIS_DT_INI")
	private Date hisDtIni;

	@Column(name = "HIS_DT_FIM")
	private Date hisDtFim;

	@Column(name = "HIS_ATIVO")
	private int hisAtivo;

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long getIdInicial() {
		return hisIdIni;
	}

	@Override
	public boolean equivale(Object other) {
		// TODO Auto-generated method stub
		return false;
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

	public void salvar() throws Exception {
		this.setHisDtIni(new Date());
		this.setHisDtFim(null);
		if (this.getId() == null) {
			this.save();
			this.setHisIdIni(this.getId());
		} else {
			JPA.em().detach(this);
			// Edson: Abaixo, não funcionou findById, por algum motivo
			// relacionado a esse esquema de sobrescrever o ObjetoBase
			Historico oAntigo = (Historico) JPA.em().find(this.getClass(),
					this.getId());
			((PlayHistoricoSuporte) oAntigo).finalizar();
			this.setHisIdIni(oAntigo.getHisIdIni());
			this.setId(null);
		}
		this.save();
	}

	public void finalizar() throws Exception {
		this.setHisDtFim(new Date());
		this.save();
	}
	
	public boolean ativoNaData(Date dt) {
		if (dt == null)
			return this.getHisDtFim() == null;
		if (dt.before(this.getHisDtIni()))
			return false;
		// dt >= DtIni
		if (this.getHisDtFim() == null)
			return true;
		if (dt.before(this.getHisDtFim()))
			return true;
		return false;
	}
}
