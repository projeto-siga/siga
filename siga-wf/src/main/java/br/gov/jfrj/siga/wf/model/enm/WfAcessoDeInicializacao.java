package br.gov.jfrj.siga.wf.model.enm;

import java.util.Date;

import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;

public enum WfAcessoDeInicializacao {

	ACESSO_PUBLICO("Público"),
	//
	ACESSO_ORGAO_USU("Restrito ao Órgão"),
	//
	ACESSO_LOTACAO_E_SUPERIORES("Lotação e Superiores"),
	//
	ACESSO_LOTACAO_E_INFERIORES("Lotação e Inferiores"),
	//
	ACESSO_LOTACAO("Lotação"),
	//
	ACESSO_PESSOAL("Pessoal"),
	//
	ACESSO_LOTACAO_E_GRUPO("Lotação e Grupo");

	private final String descr;

	WfAcessoDeInicializacao(String descr) {
		this.descr = descr;
	}

	public String getDescr() {
		return this.descr;
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

}
