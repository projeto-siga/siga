package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaReceberPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;
import br.gov.jfrj.siga.vraptor.builder.ExMovimentacaoBuilder;

@Transacional
public class DocumentosSiglaReceberPost implements IDocumentosSiglaReceberPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		DpPessoa cadastrante = ctx.getCadastrante();
		DpPessoa titular = ctx.getTitular();
		DpLotacao lotaTitular = cadastrante.getLotacao();

		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento");

		ctx.assertAcesso(mob, cadastrante, lotaTitular);

		final ExMovimentacaoBuilder movBuilder = ExMovimentacaoBuilder.novaInstancia();
		final ExMovimentacao mov = movBuilder.construir(ExDao.getInstance());

		if (!Ex.getInstance().getComp().podeReceber(titular, lotaTitular, mob)) {
			throw new AplicacaoException("Documento não pode ser recebido");
		}

		Ex.getInstance().getBL().receber(cadastrante, titular, lotaTitular, mob, mov.getDtMov());

		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Receber documento";
	}
}
