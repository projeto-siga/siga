package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaJuntarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaJuntarPost implements IDocumentosSiglaJuntarPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		DpPessoa cadastrante = ctx.getCadastrante();
		DpLotacao lotaTitular = cadastrante.getLotacao();

		ExMobil mobFilho = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento Secundário");
		ExMobil mobPai = ctx.buscarEValidarMobil(req.siglapai, req, resp, "Documento Principal");

		ctx.assertAcesso(mobFilho, cadastrante, lotaTitular);

		Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

		if (!Ex.getInstance().getComp().podeJuntar(cadastrante, lotaTitular, mobFilho)) {
			throw new AplicacaoException("Não é possível fazer juntada");
		}

		Ex.getInstance().getBL().juntarDocumento(cadastrante, cadastrante, lotaTitular, null, mobFilho, mobPai, dt,
				cadastrante, cadastrante, "1");

		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Juntar documento";
	}
}
