package br.gov.jfrj.siga.cp.model.enm;

import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;

public enum SituacaoDeConfiguracao {
	PODE(CpSituacaoConfiguracao.SITUACAO_PODE, "Pode"),
	//
	NAO_PODE(CpSituacaoConfiguracao.SITUACAO_NAO_PODE, "Não Pode"),
	//
	OBRIGATORIO(CpSituacaoConfiguracao.SITUACAO_OBRIGATORIO, "Obrigatório"),
	//
	OPCIONAL(CpSituacaoConfiguracao.SITUACAO_OPCIONAL, "Opcional"),
	//
	DEFAULT(CpSituacaoConfiguracao.SITUACAO_DEFAULT, "Default"),
	//
	NAO_DEFAULT(CpSituacaoConfiguracao.SITUACAO_NAO_DEFAULT, "Não Default"),
	//
	PROIBIDO(CpSituacaoConfiguracao.SITUACAO_PROIBIDO, "Proibido"),
	//
	SO_LEITURA(CpSituacaoConfiguracao.SITUACAO_SO_LEITURA, "Só Leitura"),
	//
	IGNORAR_CONFIGURACAO_ANTERIOR(CpSituacaoConfiguracao.SITUACAO_IGNORAR_CONFIGURACAO_ANTERIOR,
			"Ignorar a configuração anterior");

	private Long id;
	String descr;

	SituacaoDeConfiguracao(Long id, String descr) {
		this.id = id;
		this.descr = descr;
	}

	public String getDescr() {
		return this.descr;
	}

	public Long getId() {
		return id;
	}
}
