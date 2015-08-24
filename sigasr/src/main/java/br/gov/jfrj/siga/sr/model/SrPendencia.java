package br.gov.jfrj.siga.sr.model;

import java.util.Date;

public class SrPendencia extends SrIntervalo {

	private Long id;

	private SrTipoMotivoPendencia motivo;

	private SrPendencia(SrPendencia outraPausa) {
		this(outraPausa.getInicio(), outraPausa.getFim(), outraPausa.getDescricao(),
				outraPausa.id, outraPausa.motivo);
	}
	
	public SrPendencia(Date dtIni){
		this(dtIni, null, null, null, null);
	}

	public SrPendencia(Date dtIni, Date dtFim, String descr,
			Long id, SrTipoMotivoPendencia motivo) {
		super(dtIni, dtFim, descr);
		this.id = id;
		this.motivo = motivo;
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
