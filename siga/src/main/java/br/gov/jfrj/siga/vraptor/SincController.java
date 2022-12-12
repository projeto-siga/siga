package br.gov.jfrj.siga.vraptor;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.upload.UploadSizeLimit;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.util.SigaCpSinc;
import br.gov.jfrj.siga.cp.util.SigaCpSincLdap;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.matrix.sinc.SigaCpSincExtMatrix;

@Controller
public class SincController extends SigaController {

	/**
	 * @deprecated CDI eyes only
	 */
	public SincController() {
		super();
	}

	@Inject
	public SincController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	// A ideia aqui é usar o curl para enviar um arquivo xml, da forma:
	// curl -F file=@/some/file/on/your/local/disk
	// http://localhost:8080/siga/app/admin/sinc?sigla=TRF2&maxSinc=100&modoLog=true

	@Transacional
	@Post("public/app/admin/sinc")
	@UploadSizeLimit(sizeLimit = 10 * 1024 * 1024, fileSizeLimit = 10 * 1024 * 1024)
	public void sinc(String sigla, int maxSinc, Boolean modoLog, UploadedFile file, String emails) throws Exception {
		try {
			verificarAutorizacao();
			SigaCpSinc sinc = new SigaCpSinc();
			String log = sinc.importxml(sigla, maxSinc, modoLog, file.getFile());

			// result.use(Results.http()).body(log);

			JSONObject json = new JSONObject();
			json.put("sigla", sigla);
			json.put("maxSinc", maxSinc);
			if (modoLog != null)
				json.put("modoLog", modoLog);
			JSONArray arr = new JSONArray();
			for (String s : log.split("\n"))
				arr.put(s);
			json.put("log", arr);

            notificarPorEmail(emails, sigla, log, json);

			String s = json.toString(4);
			result.use(Results.http()).addHeader("Content-Type", "application/json").body(s).setStatusCode(200);

		} catch (Exception ex) {
			jsonError(ex);
		}
	}

    @Post("public/app/admin/sinc-ldap")
    public void sincLdap(String sigla, int maxSinc, Boolean modoLog, Boolean sincSenhas, Boolean exibirAlteracoesCargo,
            String restricoes, String emails) throws Exception {
        if (modoLog == null)
            modoLog = true;
        if (sincSenhas == null)
            sincSenhas = false;
        if (exibirAlteracoesCargo == null)
            exibirAlteracoesCargo = false;

        try {
            verificarAutorizacao();
            SigaCpSincLdap sinc = new SigaCpSincLdap();

            String log = sinc.ldap(sigla, maxSinc, modoLog, sincSenhas, exibirAlteracoesCargo, restricoes);

            // result.use(Results.http()).body(log);

            JSONObject json = new JSONObject();
            json.put("sigla", sigla);
            json.put("maxSinc", maxSinc);
            json.put("modoLog", modoLog);
            JSONArray arr = new JSONArray();
            for (String s : log.split("\n"))
                arr.put(s);
            json.put("log", arr);

            notificarPorEmail(emails, sigla, log, json);

            String s = json.toString(4);
            result.use(Results.http()).addHeader("Content-Type", "application/json").body(s).setStatusCode(200);

        } catch (Exception ex) {
            jsonError(ex);
        }
    }

    @Post("public/app/admin/sinc-matrix")
    public void sincMatrix(String sigla, int maxSinc, Boolean modoLog, String emails) throws Exception {
        if (modoLog == null)
            modoLog = true;

        try {
            verificarAutorizacao();
            SigaCpSincExtMatrix sinc = new SigaCpSincExtMatrix();

            String log = sinc.matrix(sigla, maxSinc, modoLog);
            
            JSONObject json = new JSONObject();
            json.put("sigla", sigla);
            json.put("maxSinc", maxSinc);
            json.put("modoLog", modoLog);
            JSONArray arr = new JSONArray();
            for (String s : log.split("\n"))
                arr.put(s);
            json.put("log", arr);

            notificarPorEmail(emails, sigla, log, json);

            String s = json.toString(4);
            result.use(Results.http()).addHeader("Content-Type", "application/json").body(s).setStatusCode(200);
        } catch (Exception ex) {
            jsonError(ex);
        }
    }

    private void notificarPorEmail(String emails, String sigla, String log, JSONObject json) throws JSONException {
        if (Prop.get("/siga.smtp") == null) {
            json.put("email", "Não enviado já que o parâmetro 'siga.smtp' não foi informado.");
            return;
        }
        if (emails != null && !emails.trim().isEmpty()) {
            json.put("email", "Não enviado já que o parâmetro 'emails' não foi informado.");
            return;
        }

    try {
            Correio.enviar(emails.split(","), "Log de Importação - " + sigla.toUpperCase() + "-> Matrix", log);
            json.put("email", "Enviado para: " + emails);
        } catch (Exception ex) {
            json.put("email", "Erro no envio: " + ex.getMessage());
        }
    }

	private void verificarAutorizacao() throws Exception {
		String pwd = Prop.get("sinc.password");
		if (pwd == null)
			throw new Exception(
					"Para utilizar o webservice de sincronismo é necessário protegê-lo com a propriedade siga.sinc.password");
		if (request.getHeader("Authorization") == null)
			throw new Exception(
					"Para utilizar o webservice de sincronismo é necessário enviar o Authorization HTTP Header");
		if (!pwd.equals(request.getHeader("Authorization")))
			throw new Exception(
					"Para utilizar o webservice de sincronismo é necessário que o Authorization HTTP Header enviado contenha o mesmo valor que a propriedade siga.sinc.password");
	}

}