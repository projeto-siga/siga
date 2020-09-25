package br.gov.jfrj.siga.ex;

import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;

public enum ExConfiguracaoVisibilidade  {
	
	PODE {
		@Override
		public long obterIdSituacao() {
			return CpSituacaoConfiguracao.SITUACAO_PODE;
		}
	},
	NAO_PODE {
		@Override
		public long obterIdSituacao() {
			return CpSituacaoConfiguracao.SITUACAO_NAO_PODE;
		}
	};

	public abstract long obterIdSituacao();

}

