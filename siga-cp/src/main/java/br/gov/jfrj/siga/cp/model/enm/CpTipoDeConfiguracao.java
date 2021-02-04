package br.gov.jfrj.siga.cp.model.enm;

import br.gov.jfrj.siga.cp.CpTipoConfiguracao;

public enum CpTipoDeConfiguracao implements ITipoDeConfiguracao {

//	public static final long TIPO_CONFIG_UTILIZAR_SERVICO = 200;
//	public static final long TIPO_CONFIG_HABILITAR_SERVICO = 201;
//	public static final long TIPO_CONFIG_HABILITAR_SERVICO_DE_DIRETORIO = 202;
//	public static final long TIPO_CONFIG_PERTENCER = 203;
//	public static final long TIPO_CONFIG_FAZER_LOGIN = 204;
//	public static final long TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO = 205;
//	public static final long TIPO_CONFIG_GERENCIAR_GRUPO = 206;

	UTILIZAR_SERVICO(CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO, "Utilizar Serviço",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que tem permissão para utilizar determinado serviço.",
			new CpParamCfg[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO,
					CpParamCfg.CARGO, CpParamCfg.FUNCAO,
					CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new SituacaoDeConfiguracao[] { SituacaoDeConfiguracao.PODE, SituacaoDeConfiguracao.NAO_PODE,
					SituacaoDeConfiguracao.IGNORAR_CONFIGURACAO_ANTERIOR }),
	//
	HABILITAR_SERVICO(CpTipoConfiguracao.TIPO_CONFIG_HABILITAR_SERVICO, "Habilitar Serviço",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que tem permissão para habilitar determinado serviço.",
			new CpParamCfg[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO,
					CpParamCfg.CARGO, CpParamCfg.FUNCAO,
					CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new SituacaoDeConfiguracao[] { SituacaoDeConfiguracao.PODE, SituacaoDeConfiguracao.NAO_PODE,
					SituacaoDeConfiguracao.IGNORAR_CONFIGURACAO_ANTERIOR }),
	//
	HABILITAR_SERVICO_DE_DIRETORIO(CpTipoConfiguracao.TIPO_CONFIG_HABILITAR_SERVICO_DE_DIRETORIO,
			"Habilitar Serviço de Diretório",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que tem permissão para habilitar determinado serviço de diretório.",
			new CpParamCfg[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO,
					CpParamCfg.CARGO, CpParamCfg.FUNCAO,
					CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new SituacaoDeConfiguracao[] { SituacaoDeConfiguracao.PODE, SituacaoDeConfiguracao.NAO_PODE,
					SituacaoDeConfiguracao.IGNORAR_CONFIGURACAO_ANTERIOR }),
	//
	PERTENCER(CpTipoConfiguracao.TIPO_CONFIG_PERTENCER, "Pertencer à Grupo",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que pertencem a determinado grupo.",
			new CpParamCfg[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO,
					CpParamCfg.CARGO, CpParamCfg.FUNCAO,
					CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new SituacaoDeConfiguracao[] { SituacaoDeConfiguracao.PODE, SituacaoDeConfiguracao.NAO_PODE,
					SituacaoDeConfiguracao.IGNORAR_CONFIGURACAO_ANTERIOR }),
	//
	FAZER_LOGIN(CpTipoConfiguracao.TIPO_CONFIG_FAZER_LOGIN, "Fazer Login",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que tem permissão para fazer login.",
			new CpParamCfg[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO,
					CpParamCfg.CARGO, CpParamCfg.FUNCAO,
					CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new SituacaoDeConfiguracao[] { SituacaoDeConfiguracao.PODE, SituacaoDeConfiguracao.NAO_PODE,
					SituacaoDeConfiguracao.IGNORAR_CONFIGURACAO_ANTERIOR }),
	//
	UTILIZAR_SERVICO_OUTRA_LOTACAO(CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO,
			"Utilizar Serviço de Outra Lotação",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que tem permissão para utilizar determinado serviço de outra lotação.",
			new CpParamCfg[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO,
					CpParamCfg.CARGO, CpParamCfg.FUNCAO,
					CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new SituacaoDeConfiguracao[] { SituacaoDeConfiguracao.PODE, SituacaoDeConfiguracao.NAO_PODE,
					SituacaoDeConfiguracao.IGNORAR_CONFIGURACAO_ANTERIOR }),
	//
	GERENCIAR_GRUPO(CpTipoConfiguracao.TIPO_CONFIG_GERENCIAR_GRUPO, "Gerenciar Grupo",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que tem permissão para gerenciar determinado grupo.",
			new CpParamCfg[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO,
					CpParamCfg.CARGO, CpParamCfg.FUNCAO,
					CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new SituacaoDeConfiguracao[] { SituacaoDeConfiguracao.PODE, SituacaoDeConfiguracao.NAO_PODE,
					SituacaoDeConfiguracao.IGNORAR_CONFIGURACAO_ANTERIOR });

	private final Long id;
	private final String descr;
	private final String explicacao;
	private final CpParamCfg[] params;
	private final CpParamCfg[] obrigatorios;
	private final SituacaoDeConfiguracao[] situacoes;

	CpTipoDeConfiguracao(Long id, String descr, String explicacao, CpParamCfg[] params,
			CpParamCfg[] obrigatorios, SituacaoDeConfiguracao[] situacoes) {
		this.id = id;
		this.descr = descr;
		this.explicacao = explicacao;
		this.params = params;
		this.obrigatorios = obrigatorios;
		this.situacoes = situacoes;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getDescr() {
		return this.descr;
	}

	@Override
	public String getExplicacao() {
		return this.explicacao;
	}

	public static ITipoDeConfiguracao getById(Long id) {
		for (CpTipoDeConfiguracao tp : CpTipoDeConfiguracao.values())
			if (tp.id.equals(id))
				return tp;
		return null;
	}

	@Override
	public SituacaoDeConfiguracao[] getSituacoes() {
		return situacoes;
	}

	@Override
	public Enum[] getObrigatorios() {
		return obrigatorios;
	}

	@Override
	public Enum[] getParams() {
		return params;
	}
}
