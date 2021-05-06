package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaModelosParaIncluirGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaModelosParaIncluirGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaModelosParaIncluirGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.ModeloItem;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocumentosSiglaModelosParaIncluirGet implements IDocumentosSiglaModelosParaIncluirGet {

	@Override
	public String getContext() {
		return "obter lista de modelos";
	}

	@Override
	public void run(DocumentosSiglaModelosParaIncluirGetRequest req, DocumentosSiglaModelosParaIncluirGetResponse resp)
			throws Exception {
		boolean isEditandoAnexo = true;
		boolean isCriandoSubprocesso = false;
		String headerValue = null;
		boolean isAutuando = false;

		try (ApiContext ctx = new ApiContext(false, true)) {
			ApiContext.assertAcesso("");
			ExMobil mobPai = SwaggerHelper.buscarEValidarMobil(req.sigla, ctx.getSigaObjects(), req, resp,
					"Documento Principal");

			List<ExModelo> modelos = Ex.getInstance().getBL().obterListaModelos(null, null, isEditandoAnexo,
					isCriandoSubprocesso, mobPai, headerValue, true, ctx.getTitular(), ctx.getLotaTitular(),
					isAutuando);

			resp.list = new ArrayList<>();
			for (ExModelo m : modelos) {
				ModeloItem mi = new ModeloItem();
				mi.idModelo = m.getId().toString();
				mi.nome = m.getNmMod();
				mi.descr = m.getDescMod();
				resp.list.add(mi);
			}
		}
	}

}
