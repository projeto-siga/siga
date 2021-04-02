package br.gov.jfrj.siga.ex.api.v1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExProtocolo;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaGerarProtocoloPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaGerarProtocoloPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaGerarProtocoloPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.base.util.Utils;

public class DocumentosSiglaGerarProtocoloPost implements IDocumentosSiglaGerarProtocoloPost {

	@Override
	public void run(DocumentosSiglaGerarProtocoloPostRequest req, DocumentosSiglaGerarProtocoloPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ApiContext.assertAcesso("");
				SigaObjects so = ApiContext.getSigaObjects();
		
				DpPessoa cadastrante = so.getCadastrante();
				DpLotacao lotaCadastrante = cadastrante.getLotacao();
				DpPessoa titular = cadastrante;
				DpLotacao lotaTitular = cadastrante.getLotacao();
		
				ExMobil mob = SwaggerHelper.buscarEValidarMobil(req.sigla, so, req, resp, "Documento a gerar protocolo");
		
				ApiContext.assertAcesso(mob, titular, lotaTitular);
	
				final Ex ex = Ex.getInstance();
				final ExBL exBL = ex.getBL();
				
				ExProtocolo prot = new ExProtocolo();
				prot = exBL.obterProtocolo(mob.getDoc());
				
				if(prot == null) {
					try {
						prot = exBL.gerarProtocolo(mob.getDoc(), cadastrante, lotaCadastrante);
					} catch (Exception e) {
						throw new AplicacaoException("Ocorreu um erro ao gerar protocolo.");
					}
				}
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Calendar c = Calendar.getInstance();
				c.setTime(prot.getData());
				String servidor = Prop.get("/sigaex.url");
				String caminho = Utils.getBaseUrl(SwaggerServlet.getHttpServletRequest()) 
						+ "/public/app/processoautenticar?n=" + prot.getCodigo();
				resp.numeroProtocolo = prot.getCodigo();
				resp.linkProtocolo = caminho;
				resp.dataHoraEmissaoProtocolo = df.format(c.getTime());
			} catch (RegraNegocioException | AplicacaoException e) {
				ctx.rollback(e);
				throw new SwaggerException(e.getMessage(), 400, null, req, resp, null);
			} catch (Exception e) {
				ctx.rollback(e);
				throw e;
			}
		}
	}
	
	@Override
	public String getContext() {
		return "Gerar protocolo do documento";
	}
}
