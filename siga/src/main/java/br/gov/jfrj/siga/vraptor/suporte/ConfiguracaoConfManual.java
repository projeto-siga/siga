package br.gov.jfrj.siga.vraptor.suporte;

import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class ConfiguracaoConfManual {
	
	private CpDao dao;
	private DpLotacao lotacao;

	public ConfiguracaoConfManual(CpDao dao, DpLotacao lotacao) {
		this.dao = dao;
		this.lotacao = lotacao;
	}

	public Long executar(Long idPessoa, Long idServico) throws Exception {		
		
			DpPessoa pes = dao.consultar(idPessoa, DpPessoa.class, false);
			CpServico svc = dao.consultar(idServico, CpServico.class, false);
			boolean podeSvc = Cp.getInstance().getConf().podePorConfiguracao(pes,lotacao ,svc,CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO);		
			
			if (podeSvc){
				return CpSituacaoConfiguracao.SITUACAO_PODE;
			}else{
				return CpSituacaoConfiguracao.SITUACAO_NAO_PODE; 
			}
		
	}
}
