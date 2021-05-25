package br.gov.jfrj.siga.ex.api.v1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExProtocolo;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaConsultarProtocoloGet;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;

public class DocumentosSiglaConsultarProtocoloGet implements IDocumentosSiglaConsultarProtocoloGet {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		DpPessoa cadastrante = ctx.getCadastrante();
		DpLotacao lotaCadastrante = cadastrante.getLotacao();
		DpPessoa titular = cadastrante;
		DpLotacao lotaTitular = cadastrante.getLotacao();

		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a consultar protocolo");

		ctx.assertAcesso(mob, titular, lotaTitular);

		final Ex ex = Ex.getInstance();
		final ExBL exBL = ex.getBL();

		ExProtocolo prot = new ExProtocolo();
		prot = exBL.obterProtocolo(mob.getDoc());

		if (prot == null) {
			throw new AplicacaoException("Protocolo ainda não foi gerado para este documento.");
		}
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(prot.getData());
		String servidor = Prop.get("/sigaex.url");
		String caminho = Utils.getBaseUrl(SwaggerServlet.getHttpServletRequest()) + "/public/app/processoautenticar?n="
				+ prot.getCodigo();
		resp.numeroProtocolo = prot.getCodigo();
		resp.linkProtocolo = caminho;
		resp.dataHoraEmissaoProtocolo = df.format(c.getTime());
	}

	@Override
	public String getContext() {
		return "Consultar protocolo do documento";
	}
}
