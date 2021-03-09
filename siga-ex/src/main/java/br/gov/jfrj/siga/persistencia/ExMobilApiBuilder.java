package br.gov.jfrj.siga.persistencia;

import java.util.Date;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorGrupoEnum;

public class ExMobilApiBuilder {
	private Long idMarcador;
	private CpMarcadorGrupoEnum grupoMarcador;
	private Long idCadastrante;
	private Long idLotaCadastrante;
	private Long idOrgaoUsu;
	private Date dtDocIni;
	private Date dtDocFim;
	private Long qtdMax;
	private Long offset;
	private Long ordenacao;

	public ExMobilApiBuilder() {
	}

	public Long getIdMarcador() {
		return idMarcador;
	}

	public CpMarcadorGrupoEnum getGrupoMarcador() {
		return grupoMarcador;
	}

	public Long getIdCadastrante() {
		return idCadastrante;
	}

	public Long getIdLotaCadastrante() {
		return idLotaCadastrante;
	}

	public Long getIdOrgaoUsu() {
		return idOrgaoUsu;
	}

	public Date getDtDocIni() {
		return dtDocIni;
	}

	public Date getDtDocFim() {
		return dtDocFim;
	}

	public Long getQtdMax() {
		return qtdMax;
	}

	public Long getOffset() {
		return offset;
	}

	public Long getOrdenacao() {
		return ordenacao;
	}

	public ExMobilApiBuilder setMarcador(Long idMarcador) {
		this.idMarcador = idMarcador;
		return this;
	}

	public ExMobilApiBuilder setGrupoMarcador(CpMarcadorGrupoEnum grupoMarcador) {
		this.grupoMarcador = grupoMarcador;
		return this;
	}

	public ExMobilApiBuilder setIdCadastrante(Long idCadastrante) {
		this.idCadastrante = idCadastrante;
		return this;
	}

	public ExMobilApiBuilder setIdLotaCadastrante(Long idLotaCadastrante) {
		this.idLotaCadastrante = idLotaCadastrante;
		return this;
	}

	public ExMobilApiBuilder setIdOrgaoUsu(Long idOrgaoUsu) {
		this.idOrgaoUsu = idOrgaoUsu;
		return this;
	}

	public ExMobilApiBuilder setDtDocIni(Date dtDocIni) {
		this.dtDocIni = dtDocIni;
		return this;
	}

	public ExMobilApiBuilder setDtDocFim(Date dtDocFim) {
		this.dtDocFim = dtDocFim;
		return this;
	}

	public ExMobilApiBuilder setQtdMax(Long qtdMax) {
		this.qtdMax = qtdMax;
		return this;
	}
	
	public ExMobilApiBuilder setOffset(Long offset) {
		this.offset = offset;
		return this;
	}

	public ExMobilApiBuilder setOrdenacao(Long ordenacao) {
		this.ordenacao = ordenacao;
		return this;
	}
}
