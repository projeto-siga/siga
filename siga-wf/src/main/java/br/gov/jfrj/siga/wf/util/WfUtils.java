package br.gov.jfrj.siga.wf.util;

import com.crivano.jflow.Engine;
import com.crivano.jflow.EngineImpl;
import com.crivano.jflow.model.ProcessInstance;

import br.gov.jfrj.siga.wf.dao.WfDao;

public class WfUtils {
	// Creates the workflow engine, mocking the dao and the handler. Uses MVEL to
	// evaluate expressions.
	public static Engine buildEngine(final ProcessInstance pi) {
		return new EngineImpl(WfDao.getInstance(), new WfHandler());
	}

}
