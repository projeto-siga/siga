package br.gov.jfrj.siga.ex.api.v1;

import java.util.Map;

import org.apache.http.HttpHeaders;

import com.auth0.jwt.JWTVerifier;
import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExProtocolo;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaHtmlGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaHtmlGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaHtmlGet;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.ex.util.ProcessadorHtml;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

@AcessoPublico
public class DocumentosSiglaHtmlGet implements IDocumentosSiglaHtmlGet {
	@Override
	public void run(DocumentosSiglaHtmlGetRequest req, DocumentosSiglaHtmlGetResponse resp) throws Exception {
		try {
			CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
	
	        String jwt = CurrentRequest.get().getRequest().getHeader(HttpHeaders.AUTHORIZATION);		
			if (jwt != null) {
				String n = verifyJwtToken(jwt).get("n").toString();
				ExProtocolo protocolo = ExDao.getInstance().obterProtocoloPorCodigo(n);
				ExDocumento docPai = protocolo.getExDocumento();
						
				ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
				flt.setSigla(req.sigla);
				ExMobil mob = ExDao.getInstance().consultarPorSigla(flt);
				if (mob == null) {
					throw new SwaggerException("Documento não encontrado: " + req.sigla, 404, null, req, resp,
							null);
				}
				ExDocumento doc = mob.doc();
	
				if (!(docPai.getIdDoc() == mob.getExMobilPai().getDoc().getIdDoc()
						&& mob.getPodeExibirNoAcompanhamento())) {
					throw new SwaggerException("Documento não permitido para visualização: " + req.sigla, 403, null, req, resp,
							null);
				}
				resp.html = ProcessadorHtml.bodyOnly(doc.getHtml());
				return;
			} else {
				throw new SwaggerException("O token de acesso não foi enviado." + req.sigla, 401, null, req, resp,
						null);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
		
		/* TODO: Acesso atraves do login no siga */
		
//		SwaggerHelper.buscarEValidarUsuarioLogado();
//		SigaObjects so = SwaggerHelper.getSigaObjects();
//		so.assertAcesso("DOC:Módulo de Documentos;" + "");
//
//		try {
//			DpPessoa cadastrante = so.getCadastrante();
//			DpPessoa titular = cadastrante;
//			DpLotacao lotaTitular = cadastrante.getLotacao();
//
//			ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
//			flt.setSigla(req.sigla);
//			ExMobil mob = ExDao.getInstance().consultarPorSigla(flt);
//			
//			Utils.assertAcesso(mob, titular, lotaTitular);
//			ExDocumento doc = mob.doc();
//			resp.html = doc.getHtml();
//		} catch (Exception e) {
//			e.printStackTrace(System.out);
//			throw e;
//		}
	}

	@Override
	public String getContext() {
		return "obter html do documento";
	}

	private static String getJwtPassword() {
		return Prop.get("/siga.autenticacao.senha");
	}

	private static Map<String, Object> verifyJwtToken(String token) {
		final JWTVerifier verifier = new JWTVerifier(getJwtPassword());
		try {
			Map<String, Object> map = verifier.verify(token);
			return map;
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao verificar token JWT", 0, e);
		}
	}
	
}
