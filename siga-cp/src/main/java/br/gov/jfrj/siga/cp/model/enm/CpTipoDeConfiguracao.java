package br.gov.jfrj.siga.cp.model.enm;

import br.gov.jfrj.siga.cp.CpTipoConfiguracao;

public enum CpTipoDeConfiguracao {

	INSTANCIAR_PROCEDIMENTO(CpTipoConfiguracao.TIPO_CONFIG_INSTANCIAR_PROCEDIMENTO,
			"Instanciar Procedimento de Workflow",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que tem permissão para iniciar um determinado diagrama, além das indicadas no próprio diagrama.",
			new CpParametroDeConfiguracao[] { CpParametroDeConfiguracao.PESSOA, CpParametroDeConfiguracao.LOTACAO,
					CpParametroDeConfiguracao.CARGO, CpParametroDeConfiguracao.FUNCAO, CpParametroDeConfiguracao.ORGAO,
					CpParametroDeConfiguracao.SITUACAO },
			new CpParametroDeConfiguracao[] { CpParametroDeConfiguracao.SITUACAO },
			new SituacaoDeConfiguracao[] { SituacaoDeConfiguracao.PODE, SituacaoDeConfiguracao.NAO_PODE });
//
//	DESIGNAR_TAREFA(CpTipoConfiguracao.TIPO_CONFIG_DESIGNAR_TAREFA, "Designar Tarefa de Workflow",
//			"Esta configuração deve ser removida.",
//			new CpParametroDeConfiguracao[] { CpParametroDeConfiguracao.PESSOA }, new CpParametroDeConfiguracao[] {},
//			new SituacaoDeConfiguracao[] { SituacaoDeConfiguracao.PODE, SituacaoDeConfiguracao.NAO_PODE });

	private final Long id;
	private final String descr;
	private final String explicacao;
	private final CpParametroDeConfiguracao[] params;
	private final CpParametroDeConfiguracao[] obrigatorios;
	private final SituacaoDeConfiguracao[] situacoes;

	CpTipoDeConfiguracao(Long id, String descr, String explicacao, CpParametroDeConfiguracao[] params,
			CpParametroDeConfiguracao[] obrigatorios, SituacaoDeConfiguracao[] situacoes) {
		this.id = id;
		this.descr = descr;
		this.explicacao = explicacao;
		this.params = params;
		this.obrigatorios = obrigatorios;
		this.situacoes = situacoes;
	}

	public Long getId() {
		return id;
	}

	public String getDescr() {
		return this.descr;
	}

	public String getExplicacao() {
		return this.explicacao;
	}

	public boolean ativo(String param) {
		if (param == null || params == null)
			return false;
		for (CpParametroDeConfiguracao p : params) {
			if (param.equals(p.name()))
				return true;
		}
		return false;
	}

	public String style(String param) {
		return ativo(param) ? "" : "display: none";
	}

	public boolean obrigatorio(String param) {
		if (param == null || obrigatorios == null)
			return false;
		for (CpParametroDeConfiguracao p : obrigatorios) {
			if (param.equals(p.name()))
				return true;
		}
		return false;
	}

	public static CpTipoDeConfiguracao getById(Long id) {
		for (CpTipoDeConfiguracao tp : CpTipoDeConfiguracao.values())
			if (tp.id.equals(id))
				return tp;
		return null;
	}

	public SituacaoDeConfiguracao[] getSituacoes() {
		return situacoes;
	}

	public CpParametroDeConfiguracao[] getObrigatorios() {
		return obrigatorios;
	}
}
