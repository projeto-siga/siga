/**
 * 
 */
package br.gov.jfrj.siga.wf.vraptor;

import br.gov.jfrj.siga.vraptor.CpConfiguracaoBuilder;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.model.WfConfiguracao;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;

public final class WfConfiguracaoBuilder extends CpConfiguracaoBuilder<WfConfiguracaoBuilder, WfConfiguracao, WfDao> {

	public static final Integer ORGAO_INTEGRADO = 2;
	public static final Integer MATRICULA = 1;
	private Long idDefinicaoDeProcedimento;

	WfConfiguracaoBuilder() {
		super(WfConfiguracao.class, WfDao.getInstance());
	}

	public WfConfiguracao construir() {
		WfConfiguracao config = instanciarConfiguracao();
		return construir(config);
	}

	@Override
	public WfConfiguracao construir(WfConfiguracao config) {
		super.construir(config);

		if (idDefinicaoDeProcedimento != null && idDefinicaoDeProcedimento != 0) {
			config.setDefinicaoDeProcedimento(
					WfDao.getInstance().consultar(idDefinicaoDeProcedimento, WfDefinicaoDeProcedimento.class, false));
		} else
			config.setDefinicaoDeProcedimento(null);

		return config;
	}

	public Long getIdDefinicaoDeProcedimento() {
		return idDefinicaoDeProcedimento;
	}

	public WfConfiguracaoBuilder setIdDefinicaoDeProcedimento(Long idDefinicaoDeProcedimento) {
		this.idDefinicaoDeProcedimento = idDefinicaoDeProcedimento;
		return this;
	}

}
