package br.gov.jfrj.siga.ex.api.v1;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.codehaus.jettison.json.JSONObject;

import com.auth0.jwt.JWTVerifier;
import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.context.AcessoPublicoEPrivado;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExProtocolo;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaHtmlGet;
import br.gov.jfrj.siga.ex.util.ProcessadorHtml;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

@AcessoPublicoEPrivado
public class DocumentosSiglaHtmlGet implements IDocumentosSiglaHtmlGet {
	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		String jwt = CurrentRequest.get().getRequest().getHeader(HttpHeaders.AUTHORIZATION);
		ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
		flt.setSigla(req.sigla);
		ExMobil mob = ExDao.getInstance().consultarPorSigla(flt);
		if (mob == null) {
			throw new SwaggerException("Documento não encontrado: " + req.sigla, 404, null, req, resp, null);
		}
		if (mob.getDoc().isCapturado()) {
			throw new SwaggerException("Documento capturado, não é possivel ser visualizado em formato HTML.", 403,
					null, req, resp, null);
		}

		Decoder decoder = Base64.getUrlDecoder();
		String[] jwt_split = jwt.split("\\.");
		String jwtBody = new String(decoder.decode(jwt_split[1]));
		JSONObject jwtBodyJson = new JSONObject(jwtBody);

		if (jwtBodyJson.has("n")) {
			String n = verifyJwtToken(jwt).get("n").toString();
			ExProtocolo protocolo = ExDao.getInstance().obterProtocoloPorCodigo(n);
			ExDocumento docPai = protocolo.getExDocumento();
			if (!((docPai.getIdDoc() == mob.getExMobilPai().getDoc().getIdDoc() 
						|| mob.getDoc().isDescricaoEspecieDespacho())
					&& mob.isExibirNoAcompanhamento())) {
				throw new SwaggerException("Documento não permitido para visualização: " + req.sigla, 403, null, req,
						resp, null);
			}
		} else {
			ctx.buscarEValidarUsuarioLogado();
			ctx.assertAcesso(mob, ctx.getCadastrante(), ctx.getCadastrante().getLotacao());
		}
		ExDocumento doc = mob.doc();

		resp.html = ProcessadorHtml.bodyOnly(doc.getHtml());
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
