package br.gov.jfrj.siga.ex.api.v1;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IModelosIdProcessarEntrevistaPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;

public class ModelosIdProcessarEntrevistaPost implements IModelosIdProcessarEntrevistaPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExModelo mod = ExDao.getInstance().consultar(Long.parseLong(req.id), ExModelo.class, false);
		ExDocumento doc = new ExDocumento();
		doc.setExModelo(mod);

		Map<String, String> formParams = obterFormParams(req.entrevista);

		CpOrgaoUsuario orgaoUsuario = ctx.getCadastrante().getOrgaoUsuario();
		String html = Ex.getInstance().getBL().processarModelo(doc, "entrevista", formParams, orgaoUsuario);
		resp.setContenttype("text/html");
		byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
		resp.setContentlength((long) bytes.length);
		resp.setInputstream(new ByteArrayInputStream(bytes));
	}

	private Map<String, String> obterFormParams(String entrevista)
			throws JsonProcessingException, JsonMappingException, UnsupportedEncodingException {
		String camposModelo = "";
		Map<String, String> formParams = new HashMap<>();
		String conteudo = DocumentosPost.UTF8toISO(entrevista);
		if (entrevista.startsWith("{")) {
			ObjectMapper objectMapper = new ObjectMapper();
			formParams = objectMapper.readValue(conteudo, Map.class);
			for (String key : formParams.keySet()) {
				camposModelo = camposModelo + key + "=" + URLEncoder.encode(formParams.get(key), "iso-8859-1") + "&";
			}
		} else {
			String[] keyAndValues = conteudo.split("&");
			for (String keyAndValue : keyAndValues) {
				int idx = keyAndValue.indexOf("=");
				if (idx != -1) {
					camposModelo = camposModelo + keyAndValue.substring(0, idx) + "="
							+ URLEncoder.encode(keyAndValue.substring(idx + 1), "iso-8859-1") + "&";
					formParams.put(keyAndValue.substring(0, idx),
							URLDecoder.decode(keyAndValue.substring(idx + 1), "UTF-8"));
				}
			}
		}
		return formParams;
	}

	@Override
	public String getContext() {
		return "obter informações sobre um modelo";
	}
}
