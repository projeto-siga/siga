package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import com.crivano.swaggerservlet.SwaggerServlet;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@AcessoPublico
public class EspeciesGet implements IExApiV1.IEspeciesGet {
	
	@Override
    public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		resp.especies = getEspecies();

    }
	
	public List<?> getEspecies() throws AplicacaoException {
		List<?> especies = ExDao.getInstance().consultarEspecies();
		return especies;
	}
	
	@Override
    public String getContext() {
        return "verificar assinatura";
    }
}	