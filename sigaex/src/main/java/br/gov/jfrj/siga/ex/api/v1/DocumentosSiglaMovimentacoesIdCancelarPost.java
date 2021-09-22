package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaMovimentacoesIdCancelarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaMovimentacoesIdCancelarPost implements IDocumentosSiglaMovimentacoesIdCancelarPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExBL bl = Ex.getInstance().getBL();

		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Cancelar Movimentação");

		if (req.id == null || req.id.length() == 0 || "-".equals(req.id)) {
			bl.validarCancelamentoDeUltimaMovimentacao(ctx.getTitular(), ctx.getLotaTitular(), mob);
			bl.cancelarMovimentacao(ctx.getCadastrante(), ctx.getLotaTitular(), mob);
			return;
		}

		ExMovimentacao mov = ctx.getMov(mob, req.id);

		ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

		bl.cancelar(ctx.getTitular(), ctx.getLotaTitular(), mob, mov, null, null, null, null);

		resp.sigla = mob.doc().getCodigo();
		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Arquivar no Corrente";
	}

}
