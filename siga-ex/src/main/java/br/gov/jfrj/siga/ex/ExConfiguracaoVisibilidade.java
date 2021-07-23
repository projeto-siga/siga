package br.gov.jfrj.siga.ex;

import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;

public enum ExConfiguracaoVisibilidade  {
	
	PODE {
		@Override
		public int obterIdSituacao() {
			return CpSituacaoDeConfiguracaoEnum.PODE.getId();
		}
	},
	NAO_PODE {
		@Override
		public int obterIdSituacao() {
			return CpSituacaoDeConfiguracaoEnum.NAO_PODE.getId();
		}
	};

	public abstract int obterIdSituacao();

}

