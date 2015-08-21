package br.gov.jfrj.siga.sr.model;

import java.util.Date;

public class SrPendencia {

	private Long id;

	private SrTipoMotivoPendencia motivo;

	private Date dtIni;

	private Date dtFim;

	protected String descricao;

	private SrPendencia(SrPendencia outraPausa) {
		this(outraPausa.dtIni, outraPausa.dtFim, outraPausa.descricao,
				outraPausa.id, outraPausa.motivo);
	}
	
	public SrPendencia(Date dtIni){
		this(dtIni, null, null, null, null);
	}

	public SrPendencia(Date dtIni, Date dtFim, String descr,
			Long id, SrTipoMotivoPendencia motivo) {
		this.dtIni = dtIni;
		this.dtFim = dtFim;
		this.descricao = descr;
		this.id = id;
		this.motivo = motivo;
	}

	public Date getDtIni() {
		return dtIni;
	}

	public SrPendencia setDtIni(Date dtIni) {
		this.dtIni = dtIni;
		return this;
	}

	public Date getDtFim() {
		return dtFim;
	}

	public SrPendencia setDtFim(Date dtFim) {
		this.dtFim = dtFim;
		return this;
	}

	public boolean isInfinita() {
		return dtFim == null;
	}

	public boolean terminouAntesDe(Date dt) {
		return dtFim != null ? dtFim.before(dt) : false;
	}

	public boolean terminouDepoisDe(Date dt) {
		return dtFim != null ? dtFim.after(dt) : false;
	}

	public boolean comecouAntesDe(Date dt) {
		return dtIni != null ? dtIni.before(dt) : false;
	}

	public boolean comecouDepoisDe(Date dt) {
		return dtIni != null ? dtIni.after(dt) : false;
	}
	
	public boolean abrange(Date dt){
		return !terminouAntesDe(dt) && !comecouDepoisDe(dt);
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SrTipoMotivoPendencia getMotivo() {
		return motivo;
	}

	public void setMotivo(SrTipoMotivoPendencia motivo) {
		this.motivo = motivo;
	}

	public SrPendencia copy() {
		return new SrPendencia(this);
	}

}
