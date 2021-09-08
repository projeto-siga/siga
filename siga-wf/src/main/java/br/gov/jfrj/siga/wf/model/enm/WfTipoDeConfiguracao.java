package br.gov.jfrj.siga.wf.model.enm;

import java.util.Date;

import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.enm.CpParamCfg;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;

public enum WfTipoDeConfiguracao implements ITipoDeConfiguracao {

	INSTANCIAR_PROCEDIMENTO(100, "Iniciar Procedimento",
			"Selecione órgão, lotação, pessoa, cargo ou função que tem permissão para iniciar um determinado diagrama, além das indicadas no próprio diagrama.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO,
					WfParamCfg.DEFINICAO_DE_PROCEDIMENTO },
			new Enum[] { WfParamCfg.DEFINICAO_DE_PROCEDIMENTO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE, CpSituacaoDeConfiguracaoEnum.NAO_PODE }, CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	EDITAR_DEFINICAO_DE_PROCEDIMENTO(102, "Editar Diagrama",
			"Selecione órgão, lotação, pessoa, cargo ou função que tem permissão para editar um determinado diagrama, além das indicadas no próprio diagrama.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO,
					WfParamCfg.DEFINICAO_DE_PROCEDIMENTO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE, CpSituacaoDeConfiguracaoEnum.NAO_PODE }, CpSituacaoDeConfiguracaoEnum.NAO_PODE, true);

	
	public static final long TIPO_CONFIG_DESIGNAR_TAREFA = 101;

	private final int id;
	private final String descr;
	private final String explicacao;
	private final Enum[] params;
	private final Enum[] obrigatorios;
	private final CpSituacaoDeConfiguracaoEnum[] situacoes;
	private final CpSituacaoDeConfiguracaoEnum situacaoDefault;
	private final boolean editavel;

	WfTipoDeConfiguracao(int id, String descr, String explicacao, Enum[] params, Enum[] obrigatorios,
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

	public static boolean acessoPermitido(WfDefinicaoDeProcedimento pd, DpPessoa titular, DpLotacao lotaTitular) {
		switch (pd.getAcessoDeEdicao()) {
		case ACESSO_PUBLICO:
			return true;
		case ACESSO_ORGAO_USU:
			return pd.getOrgaoUsuario().getIdOrgaoUsu().equals(titular.getOrgaoUsuario().getIdOrgaoUsu())
					|| pd.getOrgaoUsuario().getIdOrgaoUsu().equals(lotaTitular.getOrgaoUsuario().getIdOrgaoUsu());
		case ACESSO_LOTACAO_E_SUPERIORES:
			for (DpLotacao lot = pd.getLotaResponsavel(); lot != null; lot = lot.getLotacaoPai())
				if (lotaTitular.equivale(lot))
					return true;
			return false;
		case ACESSO_LOTACAO_E_INFERIORES:
			for (DpLotacao lot = lotaTitular; lot != null; lot = lot.getLotacaoPai())
				if (pd.getLotaResponsavel().equivale(lot))
					return true;
			return false;
		case ACESSO_LOTACAO:
			return pd.getLotaResponsavel().equivale(lotaTitular);
		case ACESSO_PESSOAL:
			return pd.getResponsavel().equivale(titular);
		case ACESSO_LOTACAO_E_GRUPO:
			if (pd.getLotaResponsavel().equivale(lotaTitular))
				return true;
			try {
				for (CpPerfil perfil : Cp.getInstance().getConf().consultarPerfisPorPessoaELotacao(titular, lotaTitular,
						new Date())) {
					if (perfil.equivale(pd.getGrupo()))
						return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		return false;
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
