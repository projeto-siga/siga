package br.gov.jfrj.siga.hibernate.ext;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;


public interface IMontadorQuery {

	public abstract String montaQueryConsultaporFiltro(
			final IExMobilDaoFiltro flt, DpPessoa titular,
			DpLotacao lotaTitular, boolean apenasCount);

	public abstract void setMontadorPrincipal(IMontadorQuery montadorQueryPrincipal);

}