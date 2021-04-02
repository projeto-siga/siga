package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaJuntarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaJuntarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaJuntarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocumentosSiglaJuntarPost implements IDocumentosSiglaJuntarPost {

	@Override
	public void run(DocumentosSiglaJuntarPostRequest req, DocumentosSiglaJuntarPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ApiContext.assertAcesso("");
				SigaObjects so = ApiContext.getSigaObjects();
		
				DpPessoa cadastrante = so.getCadastrante();
				DpLotacao lotaTitular = cadastrante.getLotacao();
		
				ExMobil mobFilho = SwaggerHelper.buscarEValidarMobil(req.sigla, so, req, resp, "Documento Secundário");
				ExMobil mobPai = SwaggerHelper.buscarEValidarMobil(req.siglapai, so, req, resp, "Documento Principal");
		
				ApiContext.assertAcesso(mobFilho, cadastrante, lotaTitular);
		
				Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();
	
				if (!Ex.getInstance().getComp()
						.podeJuntar(cadastrante, lotaTitular, mobFilho)) {
					throw new AplicacaoException("Não é possível fazer juntada");
				}
		
				Ex.getInstance().getBL().juntarDocumento(cadastrante, cadastrante, lotaTitular, null, mobFilho, mobPai, dt,
						cadastrante, cadastrante, "1");
		
				resp.status = "OK";
			} catch (RegraNegocioException | AplicacaoException e) {
				ctx.rollback(e);
				throw new SwaggerException(e.getMessage(), 400, null, req, resp, null);
			} catch (Exception e) {
				ctx.rollback(e);
				throw e;
			}
		}
	}

	@Override
	public String getContext() {
		return "Juntar documento";
	}
}
