package br.gov.jfrj.siga.wf.model.enm;

import java.util.Date;

import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.enm.CpParamCfg;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.SituacaoDeConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;

public enum WfTipoDeConfiguracao implements ITipoDeConfiguracao {

	INSTANCIAR_PROCEDIMENTO(CpTipoConfiguracao.TIPO_CONFIG_INSTANCIAR_PROCEDIMENTO, "Iniciar Procedimento",
			"Selecione órgão, lotação, pessoa, cargo ou função que tem permissão para iniciar um determinado diagrama, além das indicadas no próprio diagrama.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO,
					WfParamCfg.DEFINICAO_DE_PROCEDIMENTO },
			new Enum[] { WfParamCfg.DEFINICAO_DE_PROCEDIMENTO, CpParamCfg.SITUACAO },
			new SituacaoDeConfiguracao[] { SituacaoDeConfiguracao.PODE, SituacaoDeConfiguracao.NAO_PODE }),
	EDITAR_DEFINICAO_DE_PROCEDIMENTO(CpTipoConfiguracao.TIPO_CONFIG_EDITAR_DEFINICAO_DE_PROCEDIMENTO, "Editar Diagrama",
			"Selecione órgão, lotação, pessoa, cargo ou função que tem permissão para editar um determinado diagrama, além das indicadas no próprio diagrama.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO,
					WfParamCfg.DEFINICAO_DE_PROCEDIMENTO },
			new Enum[] { CpParamCfg.SITUACAO },
			new SituacaoDeConfiguracao[] { SituacaoDeConfiguracao.PODE, SituacaoDeConfiguracao.NAO_PODE });

	private final Long id;
	private final String descr;
	private final String explicacao;
	private final Enum[] params;
	private final Enum[] obrigatorios;
	private final SituacaoDeConfiguracao[] situacoes;

	WfTipoDeConfiguracao(Long id, String descr, String explicacao, Enum[] params, Enum[] obrigatorios,
			SituacaoDeConfiguracao[] situacoes) {
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

	public static WfTipoDeConfiguracao getById(Long id) {
		for (WfTipoDeConfiguracao tp : WfTipoDeConfiguracao.values())
			if (tp.id.equals(id))
				return tp;
		return null;
	}

	public SituacaoDeConfiguracao[] getSituacoes() {
		return situacoes;
	}

	public Enum[] getObrigatorios() {
		return obrigatorios;
	}

	@Override
	public Enum[] getParams() {
		return params;
	}

}
