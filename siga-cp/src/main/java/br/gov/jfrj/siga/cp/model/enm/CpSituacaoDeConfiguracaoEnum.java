package br.gov.jfrj.siga.cp.model.enm;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpSituacaoDeConfiguracaoEnum implements IEnumWithId {
	PODE(1, "Pode", 0),
	//
	NAO_PODE(2, "Não Pode", 9),
	//
	OBRIGATORIO(3, "Obrigatório", 7),
	//
	OPCIONAL(4, "Opcional", 4),
	//
	DEFAULT(5, "Default", 5),
	//
	NAO_DEFAULT(6, "Não Default", 6),
	//
	PROIBIDO(7, "Proibido", 10),
	//
	SO_LEITURA(8, "Só Leitura", 8),
	//
	IGNORAR_CONFIGURACAO_ANTERIOR(9, "Ignorar a configuração anterior", 1);

	private int id;
	private String descr;
	private int restritividade;

	CpSituacaoDeConfiguracaoEnum(int id, String descr, int restritividade) {
		this.id = id;
		this.descr = descr;
		this.restritividade = restritividade;
	}

	public String getDescr() {
		return this.descr;
	}

	public Integer getId() {
		return id;
	}

	public int getRestritividade() {
		return restritividade;
	}

	public static CpSituacaoDeConfiguracaoEnum getById(Integer id) {
		return IEnumWithId.getEnumFromId(id, CpSituacaoDeConfiguracaoEnum.class);
	}

	public boolean isPermissiva() {
		return this == PODE || this == DEFAULT || this == OBRIGATORIO;
	}

	public boolean isDefaultOuObrigatoria() {
		return this == DEFAULT || this == OBRIGATORIO;
	}
}
