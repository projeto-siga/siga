package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaMovimentacoesIdAssinarComSenhaPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaMovimentacoesIdAssinarComSenhaPost
		implements IDocumentosSiglaMovimentacoesIdAssinarComSenhaPost {
	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento cujo anexo receberá a assinatura");
		ExMovimentacao mov = ctx.getMov(mob, req.id);

		Ex.getInstance().getBL().assinarMovimentacaoComSenha(ctx.getCadastrante(), ctx.getLotaTitular(), mov, null,
				ctx.getCadastrante().getSiglaCompleta(), null, false, false,
				ExTipoDeMovimentacao.ASSINATURA_MOVIMENTACAO_COM_SENHA);

		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Assinar movimentação com senha";
	}
}
