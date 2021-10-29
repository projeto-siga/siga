package br.gov.jfrj.siga.sr.model.enm;

import br.gov.jfrj.siga.cp.model.enm.CpParamCfg;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;

public enum SrTipoDeConfiguracao implements ITipoDeConfiguracao {

	DESIGNACAO(300, "Designação", "", new CpParamCfg[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO },
			new CpParamCfg[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//
	ASSOCIACAO_TIPO_ATRIBUTO(301, "Associação entre tipo e atributo", "",
			new CpParamCfg[] {
					CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, false),
	//
	PERMISSAO_USO_LISTA(302, "Permissão de uso da lista", "",
			new CpParamCfg[] {
					CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, false),
	//
	DEFINICAO_INCLUSAO_AUTOMATICA(303, "Definição de inclusão automática", "",
			new CpParamCfg[] {
					CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, false),
	//
	ABRANGENCIA_ACORDO(304, "Abrangência de acordo", "",
			new CpParamCfg[] {
					CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR },
			CpSituacaoDeConfiguracaoEnum.PODE, false),
	//
	ASSOCIACAO_PESQUISA(305, "Associação de pesquisa", "",
			new CpParamCfg[] {
					CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR },
			CpSituacaoDeConfiguracaoEnum.PODE, false),
	//
	ESCALONAMENTO_SOL_FILHA(306, "Escalonamento de solicitação filha", "",
			new CpParamCfg[] {
					CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, false);

	private final int id;
	private final String descr;
	private final String explicacao;
	private final CpParamCfg[] params;
	private final CpParamCfg[] obrigatorios;
	private final CpSituacaoDeConfiguracaoEnum[] situacoes;
	private final CpSituacaoDeConfiguracaoEnum situacaoDefault;
	private final boolean editavel;

	SrTipoDeConfiguracao(int id, String descr, String explicacao, CpParamCfg[] params, CpParamCfg[] obrigatorios,
			CpSituacaoDeConfiguracaoEnum[] situacoes, CpSituacaoDeConfiguracaoEnum situacaoDefault, boolean editavel) {
		this.id = id;
		this.descr = descr;
		this.explicacao = explicacao;
		this.params = params;
		this.obrigatorios = obrigatorios;
		this.situacoes = situacoes;
		this.situacaoDefault = situacaoDefault;
		this.editavel = editavel;
	}

	public int getId() {
		return id;
	}

	public String getDescr() {
		return this.descr;
	}

	public String getExplicacao() {
		return this.explicacao;
	}

	public static ITipoDeConfiguracao getById(Integer id) {
		if (id == null)
			return null;
		return CpTipoDeConfiguracao.getById(id);
	}

	public CpSituacaoDeConfiguracaoEnum[] getSituacoes() {
		return situacoes;
	}

	public Enum[] getObrigatorios() {
		return obrigatorios;
	}

	@Override
	public Enum[] getParams() {
		return params;
	}

	@Override
	public CpSituacaoDeConfiguracaoEnum getSituacaoDefault() {
		return situacaoDefault;
	}

	@Override
	public boolean isEditavel() {
		return editavel;
	}
}
