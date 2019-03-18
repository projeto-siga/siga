package br.gov.jfrj.siga.sr.model;

import java.util.Date;

public class SrPendencia extends SrIntervalo {

	private Long id;

	private SrTipoMotivoPendencia motivo;
	
	private SrSolicitacao sol;

	private SrPendencia(SrPendencia outraPausa) {
		this(outraPausa.getInicio(), outraPausa.getFim(), outraPausa.getDescricao(),
				outraPausa.id, outraPausa.motivo, outraPausa.getSol());
	}
	
	public SrPendencia(Date dtIni){
		this(dtIni, null, null, null, null, null);
	}

	public SrPendencia(Date dtIni, Date dtFim, String descr,
			Long id, SrTipoMotivoPendencia motivo, SrSolicitacao sol) {
		super(dtIni, dtFim, descr);
		this.id = id;
		this.motivo = motivo;
		this.sol = sol;
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

	public SrSolicitacao getSol() {
		return sol;
	}

	public void setSol(SrSolicitacao sol) {
		this.sol = sol;
	}
	
	@Override
	public String getDescricao() {
		String descricao = getMotivo() != null ? getMotivo().getDescrTipoMotivoPendencia() : "";
		if (super.getDescricao() != null)
			descricao += " | " + super.getDescricao();
		if (getFim() != null)
			descricao += " | Previsão: " + getFimString();
		return descricao + (sol.isFilha() ? " (" + sol.getNumSequenciaString() + ")" : "");
	}

}
