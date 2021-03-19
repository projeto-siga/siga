package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.SwaggerException;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaReceberPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaReceberPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaReceberPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.builder.ExMovimentacaoBuilder;

public class DocumentosSiglaReceberPost implements IDocumentosSiglaReceberPost {

	@Override
	public void run(DocumentosSiglaReceberPostRequest req, DocumentosSiglaReceberPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			ApiContext.assertAcesso("");
			SigaObjects so = ApiContext.getSigaObjects();
	
			DpPessoa cadastrante = so.getCadastrante();
			DpPessoa titular = so.getTitular();
			DpLotacao lotaTitular = cadastrante.getLotacao();
	
			ExMobil mob = SwaggerHelper.buscarEValidarMobil(req.sigla, so, req, resp, 
					"Documento");
	
			ApiContext.assertAcesso(mob, cadastrante, lotaTitular);
	
			final ExMovimentacaoBuilder movBuilder = ExMovimentacaoBuilder
					.novaInstancia();
			final ExMovimentacao mov = movBuilder.construir(ExDao.getInstance());

			if (!Ex.getInstance().getComp()
					.podeReceber(titular, lotaTitular, mob)) {
				throw new AplicacaoException("Documento não pode ser recebido");
			}

			Ex.getInstance()
					.getBL()
					.receber(cadastrante, lotaTitular, mob,
							mov.getDtMov());
			
			resp.status = "OK";
		} catch (AplicacaoException | RegraNegocioException | SwaggerException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public String getContext() {
		return "Receber documento";
	}
}
