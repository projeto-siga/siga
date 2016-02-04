package br.gov.jfrj.siga.sr.model;

import java.util.Arrays;
import java.util.List;

public enum SrFaixa {		
	ATE_15MIN(1, "Até 15 minutos", 0.25f), ATE_1(2, "Até 1 hora", 1), ATE_2(3, "Até 2 horas", 2), 
	ATE_3(4, "Até 3 horas", 3), ATE_4(5, "Até 4 horas", 4), ATE_8(6, "Até 8 horas", 8), 
	ATE_12(7, "Até 12 horas", 12), ATE_15(8, "Até 15 horas", 15), ACIMA_15(9, "Acima de 15 horas", Float.POSITIVE_INFINITY),
	ATE_16(10, "Até 16 horas", 16), ATE_24(11, "Até 24 horas", 24), ACIMA_24(12, "Acima de 24 horas", Float.POSITIVE_INFINITY),
	ACIMA_4 (13, "Acima de 4 horas", Float.POSITIVE_INFINITY);
	
	private int idFaixa;
	private String descricao;
	private float limiteSuperior;
	
	public static List<SrFaixa> FAIXAS_TRF2 = Arrays.asList(ATE_15MIN,
			ATE_1, ATE_2, ATE_4, ACIMA_4);
	
	public static List<SrFaixa> FAIXAS_JFRJ = Arrays.asList(ATE_1,
			ATE_2, ATE_4, ATE_8, ATE_12, ATE_16, ATE_24, ACIMA_24);
	
	private SrFaixa(int idFaixa, String descricao, float limiteSuperior) {
		this.setIdFaixa(idFaixa);
		this.setDescricao(descricao);
		this.setLimiteSuperior(limiteSuperior);
	}

	public int getIdFaixa() {
		return idFaixa;
	}

	public void setIdFaixa(int idFaixa) {
		this.idFaixa = idFaixa;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public float getLimiteSuperior() {
		return limiteSuperior;
	}

	public void setLimiteSuperior(float limiteSuperior) {
		this.limiteSuperior = limiteSuperior;
	}
}