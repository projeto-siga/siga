package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaMovimentacoesIdExcluirPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaMovimentacoesIdExcluirPost implements IDocumentosSiglaMovimentacoesIdExcluirPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento cuja movimentação será excluída");
		ExMovimentacao mov = ctx.getMov(mob, req.id);

		if (!Ex.getInstance().getComp().podeExcluirAnexo(ctx.getTitular(), ctx.getLotaTitular(), mob, mov)) {
			throw new PresentableUnloggedException(
					"O anexo do documento " + mob.getSigla() + " não pode ser excluído por "
							+ ctx.getTitular().getSiglaCompleta() + "/" + ctx.getLotaTitular().getSiglaCompleta());
		}
		Ex.getInstance().getBL().excluirMovimentacao(mov);

		resp.sigla = mob.doc().getCodigo();
		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Excluir movimentação";
	}

}
