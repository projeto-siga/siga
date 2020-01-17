package br.gov.jfrj.siga.wf.util;

import com.crivano.jflow.EngineImpl;

import br.gov.jfrj.siga.wf.WfDefinicaoDeDesvio;
import br.gov.jfrj.siga.wf.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.WfDefinicaoDeVariavel;
import br.gov.jfrj.siga.wf.WfProcedimento;
import br.gov.jfrj.siga.wf.WfResponsavel;
import br.gov.jfrj.siga.wf.WfTipoDeResponsavel;
import br.gov.jfrj.siga.wf.WfTipoDeTarefa;
import br.gov.jfrj.siga.wf.dao.WfDao;

public class WfEngine extends
		EngineImpl<WfDefinicaoDeProcedimento, WfDefinicaoDeTarefa, WfResponsavel, WfTipoDeTarefa, WfTipoDeResponsavel, WfDefinicaoDeVariavel, WfDefinicaoDeDesvio, WfProcedimento, WfHandler, WfDao> {

	public WfEngine(WfDao dao, WfHandler handler) {
		super(dao, handler);
	}

}
