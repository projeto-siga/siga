package br.gov.jfrj.siga.wf.util;

import com.crivano.jflow.EngineImpl;

import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeDesvio;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeVariavel;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeResponsavel;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeTarefa;

public class WfEngine extends
		EngineImpl<WfDefinicaoDeProcedimento, WfDefinicaoDeTarefa, WfResp, WfTipoDeTarefa, WfTipoDeResponsavel, WfDefinicaoDeVariavel, WfDefinicaoDeDesvio, WfProcedimento, WfHandler, WfDao> {

	public WfEngine(WfDao dao, WfHandler handler) {
		super(dao, handler);
	}

}
