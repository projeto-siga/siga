package br.gov.jfrj.siga.cp.model.enm;

import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpSituacaoDeConfiguracaoEnum implements IEnumWithId {
	PODE((int) CpSituacaoConfiguracao.SITUACAO_PODE, "Pode", 0),
	//
	NAO_PODE((int) CpSituacaoConfiguracao.SITUACAO_NAO_PODE, "Não Pode", 9),
	//
	OBRIGATORIO((int) CpSituacaoConfiguracao.SITUACAO_OBRIGATORIO, "Obrigatório", 7),
	//
	OPCIONAL((int) CpSituacaoConfiguracao.SITUACAO_OPCIONAL, "Opcional", 4),
	//
	DEFAULT((int) CpSituacaoConfiguracao.SITUACAO_DEFAULT, "Default", 5),
	//
	NAO_DEFAULT((int) CpSituacaoConfiguracao.SITUACAO_NAO_DEFAULT, "Não Default", 6),
	//
	PROIBIDO((int) CpSituacaoConfiguracao.SITUACAO_PROIBIDO, "Proibido", 10),
	//
	SO_LEITURA((int) CpSituacaoConfiguracao.SITUACAO_SO_LEITURA, "Só Leitura", 8),
	//
	IGNORAR_CONFIGURACAO_ANTERIOR((int) CpSituacaoConfiguracao.SITUACAO_IGNORAR_CONFIGURACAO_ANTERIOR,
			"Ignorar a configuração anterior", 1);

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
