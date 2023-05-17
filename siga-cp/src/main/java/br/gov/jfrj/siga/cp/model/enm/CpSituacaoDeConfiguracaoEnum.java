package br.gov.jfrj.siga.cp.model.enm;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpSituacaoDeConfiguracaoEnum implements IEnumWithId {
    OBRIGATORIO(3, "Obrigatório", 8),
    //
	PODE(1, "Pode", 0),
	//
	OPCIONAL(4, "Opcional", 4),
	//
	DEFAULT(5, "Default", 5),
    //
    AUTOMATICO(10, "Automático", 6),
	//
	NAO_DEFAULT(6, "Não Default", 7),
	//
	NAO_PODE(2, "Não Pode", 10),
	//
	PROIBIDO(7, "Proibido", 11),
	//
	SO_LEITURA(8, "Só Leitura", 9),
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
		return this == PODE || this == DEFAULT || this == AUTOMATICO || this == OBRIGATORIO;
	}

    public boolean isDefaultOuObrigatoria() {
        return this == DEFAULT || this == AUTOMATICO || this == OBRIGATORIO;
    }

    public boolean isAutomaticoOuObrigatoria() {
        return this == AUTOMATICO || this == OBRIGATORIO;
    }
}
