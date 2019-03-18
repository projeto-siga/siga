package br.gov.jfrj.siga.sr.util;

import java.util.Date;

public class SrSolicitacaoSimplificada{

	private Long id;
	
	private String descricao;
	
	private Date dtReg;
	
	public SrSolicitacaoSimplificada(Long id, String descricao, Date dtReg) {
		super();
		this.id = id;
		this.descricao = descricao;
		this.dtReg = dtReg;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDtReg() {
		return dtReg;
	}
	
	public String getDtRegDDMMYYYY() {
        return SrViewUtil.toDDMMYYYY(getDtReg());
    }

	public void setDtReg(Date dtReg) {
		this.dtReg = dtReg;
	}

	
	
}
