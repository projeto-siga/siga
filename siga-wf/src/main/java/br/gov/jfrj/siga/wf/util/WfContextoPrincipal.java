package br.gov.jfrj.siga.wf.util;

import java.util.HashMap;
import java.util.Map;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDePrincipal;

public class WfContextoPrincipal {
	WfProcedimento pi;

	WfContextoPrincipal(WfProcedimento pi) {
		this.pi = (WfProcedimento) pi;
	}

	public Map<String, String> getForm() {
		if (pi.getDefinicaoDeProcedimento().getTipoDePrincipal() == WfTipoDePrincipal.DOCUMENTO
				&& pi.getPrincipal() != null) {
			try {
				return Service.getExService().formAcumulativo(pi.getPrincipal());
			} catch (Exception ex) {
			}
		}
		return new HashMap<String, String>();
	}

}
