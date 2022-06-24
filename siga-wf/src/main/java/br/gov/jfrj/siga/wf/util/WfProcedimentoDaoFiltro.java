package br.gov.jfrj.siga.wf.util;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;

public class WfProcedimentoDaoFiltro extends DaoFiltroSelecionavel {
	public CpOrgaoUsuario ouDefault = null;
	public WfDefinicaoDeProcedimento definicaoDeProcedimento;
	public Boolean ativos;
}
