package br.gov.jfrj.siga.tp.util;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@RequestScoped
public class TpBL {

	@Inject
	private EntityManager em;
	private SigaObjects so;

	/**
	 * @deprecated CDI eyes only
	 */
	public TpBL() {
		super();
		this.so = null;

	}

	@Inject
	public TpBL(SigaObjects so) {
		super();
		this.so = so;
	}

	public CpConfiguracao buscaConfiguracaoComplexoPadrao(DpPessoa dpPessoa, CpTipoDeConfiguracao utilizarComplexo) {

		CpConfiguracao t_cfgConfigExemplo = new CpConfiguracao();
		t_cfgConfigExemplo.setLotacao(dpPessoa.getLotacao());
		t_cfgConfigExemplo.setDpPessoa(dpPessoa);
		t_cfgConfigExemplo.setCpTipoConfiguracao(utilizarComplexo);


		CpConfiguracao cpConf = null;
		try {
			CpConfiguracaoCache cache = Cp.getInstance().getConf().buscaConfiguracao(t_cfgConfigExemplo, new int[] {CpConfiguracaoBL.COMPLEXO},
					null);
			cpConf = CpDao.getInstance().consultar(cache.idConfiguracao, CpConfiguracao.class, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cpConf;
	}

	public CpConfiguracao buscaConfiguracaoComplexoAdministrador(DpPessoa titular,
			CpTipoDeConfiguracao utilizarServico, CpServico cpServico) {
		CpConfiguracao t_cfgConfigExemplo = new CpConfiguracao();
		t_cfgConfigExemplo.setLotacao(titular.getLotacao());
		t_cfgConfigExemplo.setDpPessoa(titular);
		t_cfgConfigExemplo.setCpTipoConfiguracao(utilizarServico);
		t_cfgConfigExemplo.setCpServico(cpServico);


		CpConfiguracao cpConf = null;
		try {
			CpConfiguracaoCache cache = Cp.getInstance().getConf().buscaConfiguracao(t_cfgConfigExemplo, new int[] {CpConfiguracaoBL.COMPLEXO},
					null);
			if (cache != null) {
				cpConf = CpDao.getInstance().consultar(cache.idConfiguracao, CpConfiguracao.class, false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cpConf;
	}

}