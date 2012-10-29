package controllers;

import models.SrConfiguracao;
import models.SrSolicitacao;
import br.gov.jfrj.siga.cp.bl.CpCompetenciaBL;
import br.gov.jfrj.siga.dp.DpLotacao;

public class SrCompetenciaBL extends CpCompetenciaBL {

	public SrConfiguracaoBL getConf() {
		return (SrConfiguracaoBL) super.getConfiguracaoBL();
	}
	
	public  DpLotacao obterPreatendente(SrSolicitacao solicitacao) throws Exception{
		SrConfiguracao conf = new SrConfiguracao();
		conf.itemConfiguracao = solicitacao.itemConfiguracao;
		conf.servico = solicitacao.servico;
		return new SrConfiguracaoBL().buscarConfiguracao(conf).preAtendente;
	}

}
