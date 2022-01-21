package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaJuntarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeJuntar;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaJuntarPost implements IDocumentosSiglaJuntarPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		DpPessoa cadastrante = ctx.getCadastrante();
		DpPessoa titular = ctx.getTitular();
		DpLotacao lotaTitular = ctx.getLotaTitular();

		ExMobil mobFilho = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento Secundário");
		ExMobil mobPai = ctx.buscarEValidarMobil(req.siglapai, req, resp, "Documento Principal");

		ctx.assertAcesso(mobFilho, titular, lotaTitular);

		Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

		Ex.getInstance().getComp().afirmar("Não é possível fazer juntada", ExPodeJuntar.class, cadastrante, lotaTitular, mobFilho);

		Ex.getInstance().getBL().juntarDocumento(cadastrante, titular, lotaTitular, null, mobFilho, mobPai, dt,
				cadastrante, cadastrante, "1");

		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Juntar documento";
	}
}
