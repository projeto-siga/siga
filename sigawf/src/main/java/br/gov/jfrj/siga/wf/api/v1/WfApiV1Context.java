package br.gov.jfrj.siga.wf.api.v1;

import javax.persistence.EntityManager;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.context.ApiContextSupport;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.dao.WfStarter;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.util.WfUtil;

public class WfApiV1Context extends ApiContextSupport {
	private static final String WF_MÓDULO_DE_WORKFLOW = "WF:Módulo de Workflow;";
	public void atualizarCacheDeConfiguracoes() throws Exception {
		Wf.getInstance().getConf().limparCacheSeNecessario();
	}

	public CpDao inicializarDao() {
		return WfDao.getInstance();
	}

	public EntityManager criarEntityManager() {
		return WfStarter.emf.createEntityManager();
	}

	/**
	 * Verifica se o usuário tem acesso ao serviço
	 * <code>{@value #WF_MÓDULO_DE_WORKFLOW}<code> e ao serviço informado no
	 * parâmetro acesso.
	 * 
	 * @param acesso Caminho do serviço a ser verificado a permissão de acesso
	 * 
	 * @throws Exception Se houver algo de errado.
	 */
	public void assertAcesso(String acesso) throws Exception {
		getSigaObjects().assertAcesso(WF_MÓDULO_DE_WORKFLOW + acesso);
	}

	public void assertAcesso(final WfProcedimento mob, DpPessoa titular, DpLotacao lotaTitular) throws Exception {
		// Nato: Precisaremos restringir o acesso
		if (false)
			throw new AplicacaoException("Procedimento " + mob.getSigla() + " inacessível ao usuário "
					+ titular.getSigla() + "/" + lotaTitular.getSiglaCompleta() + ".");
	}

	public WfUtil getUtil() throws Exception {
		return new WfUtil(getSigaObjects());
	};

}
