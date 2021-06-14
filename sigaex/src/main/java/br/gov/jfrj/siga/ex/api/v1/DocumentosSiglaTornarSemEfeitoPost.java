package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaTornarSemEfeitoPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaTornarSemEfeitoPost implements IDocumentosSiglaTornarSemEfeitoPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Tornar Sem Efeito");

		if (!Ex.getInstance().getComp().podeTornarDocumentoSemEfeito(ctx.getTitular(), ctx.getLotaTitular(), mob)) {
			throw new PresentableUnloggedException(
					"O documento " + mob.getSigla() + " não pode ser tornado sem efeito por "
							+ ctx.getTitular().getSiglaCompleta() + "/" + ctx.getLotaTitular().getSiglaCompleta());
		}

		if (req.motivo == null || req.motivo.isEmpty())
			throw new PresentableUnloggedException("Favor informar o motivo");

		ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

		Ex.getInstance().getBL().tornarDocumentoSemEfeito(ctx.getCadastrante(), ctx.getLotaCadastrante(), mob.doc(),
				req.motivo);

		resp.sigla = mob.doc().getCodigo();
		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Arquivar no Corrente";
	}

}
