package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaPdfCompletoGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaPdfCompletoGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaPdfCompletoGet;

public class DocumentosSiglaPdfCompletoGet implements IDocumentosSiglaPdfCompletoGet {

	@Override
	public void run(DocumentosSiglaPdfCompletoGetRequest req, DocumentosSiglaPdfCompletoGetResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(false, true)) {
			ctx.assertAcesso("");
	
			DpPessoa cadastrante = ctx.getCadastrante();
			DpPessoa titular = cadastrante;
			DpLotacao lotaTitular = cadastrante.getLotacao();
	
			ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp);
	
			ctx.assertAcesso(mob, titular, lotaTitular);
	
			resp.jwt = DownloadJwtFilenameGet.jwt(cadastrante.getSiglaCompleta(), mob.getCodigoCompacto(), null, null,
					null);
		} catch (AplicacaoException | SwaggerException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}

	@Override
	public String getContext() {
		return "obter documento";
	}

}
