package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaSobrestarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeSobrestar;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaSobrestarPost implements IDocumentosSiglaSobrestarPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		DpPessoa cadastrante = ctx.getCadastrante();
		DpPessoa titular = cadastrante;
		DpLotacao lotaCadastrante = cadastrante.getLotacao();
		DpLotacao lotaTitular = ctx.getLotaTitular();

		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Sobrestar");

		Ex.getInstance().getComp().afirmar(
				"O documento " + mob.getSigla() + " não pode ser sobrestado por "
						+ titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta(),
				ExPodeSobrestar.class, titular, lotaTitular, mob);
		
		ctx.assertAcesso(mob, titular, lotaTitular);

		Ex.getInstance().getBL().sobrestar(cadastrante, lotaCadastrante, mob, null, null, titular);

		resp.sigla = mob.doc().getCodigo();
		resp.status = "OK";
	}

	@Override
	public String getContext() {
		return "Arquivar no Corrente";
	}

}
