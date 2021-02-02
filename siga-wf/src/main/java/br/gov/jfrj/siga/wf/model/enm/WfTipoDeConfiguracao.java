package br.gov.jfrj.siga.wf.model.enm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.auth0.jwt.internal.org.bouncycastle.util.Arrays;

import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.enm.SituacaoDeConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;

public enum WfTipoDeConfiguracao {

	INSTANCIAR_PROCEDIMENTO(CpTipoConfiguracao.TIPO_CONFIG_INSTANCIAR_PROCEDIMENTO,
			"Instanciar Procedimento de Workflow",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que tem permissão para iniciar um determinado diagrama, além das indicadas no próprio diagrama.",
			new WfParametroDeConfiguracao[] { WfParametroDeConfiguracao.PESSOA, WfParametroDeConfiguracao.LOTACAO,
					WfParametroDeConfiguracao.CARGO, WfParametroDeConfiguracao.FUNCAO, WfParametroDeConfiguracao.ORGAO,
					WfParametroDeConfiguracao.DEFINICAO_DE_PROCEDIMENTO, WfParametroDeConfiguracao.SITUACAO },
			new WfParametroDeConfiguracao[] { WfParametroDeConfiguracao.DEFINICAO_DE_PROCEDIMENTO,
					WfParametroDeConfiguracao.SITUACAO },
			new SituacaoDeConfiguracao[] { SituacaoDeConfiguracao.PODE, SituacaoDeConfiguracao.NAO_PODE });
//
//	DESIGNAR_TAREFA(CpTipoConfiguracao.TIPO_CONFIG_DESIGNAR_TAREFA, "Designar Tarefa de Workflow",
//			"Esta configuração deve ser removida.",
//			new WfParametroDeConfiguracao[] { WfParametroDeConfiguracao.PESSOA }, new WfParametroDeConfiguracao[] {},
//			new SituacaoDeConfiguracao[] { SituacaoDeConfiguracao.PODE, SituacaoDeConfiguracao.NAO_PODE });

	private final Long id;
	private final String descr;
	private final String explicacao;
	private final WfParametroDeConfiguracao[] params;
	private final WfParametroDeConfiguracao[] obrigatorios;
	private final SituacaoDeConfiguracao[] situacoes;

	WfTipoDeConfiguracao(Long id, String descr, String explicacao, WfParametroDeConfiguracao[] params,
			WfParametroDeConfiguracao[] obrigatorios, SituacaoDeConfiguracao[] situacoes) {
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
		for (WfParametroDeConfiguracao p : params) {
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
		for (WfParametroDeConfiguracao p : obrigatorios) {
			if (param.equals(p.name()))
				return true;
		}
		return false;
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

	public WfParametroDeConfiguracao[] getObrigatorios() {
		return obrigatorios;
	}
}
