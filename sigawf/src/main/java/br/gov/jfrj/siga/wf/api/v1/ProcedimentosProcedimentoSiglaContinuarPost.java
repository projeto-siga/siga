
package br.gov.jfrj.siga.wf.api.v1;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.jfrj.siga.vraptor.Transacional;
import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.IProcedimentosProcedimentoSiglaContinuarPost;
import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.Procedimento;
import br.gov.jfrj.siga.wf.api.v1.apio.ProcedimentoAPIO;
import br.gov.jfrj.siga.wf.bl.WfBL;
import br.gov.jfrj.siga.wf.model.WfProcedimento;

@Transacional
public class ProcedimentosProcedimentoSiglaContinuarPost implements IProcedimentosProcedimentoSiglaContinuarPost {

	@Override
	public void run(Request req, Response resp, WfApiV1Context ctx) throws Exception {
		WfProcedimento procedimento = WfProcedimento.findBySigla(req.procedimentoSigla);

		Map<String, String> paramsAsStrings = new HashMap<>();

		if (req.variaveis != null) {
			if (req.variaveis.startsWith("{")) {
				ObjectMapper objectMapper = new ObjectMapper();
				paramsAsStrings = objectMapper.readValue(req.variaveis, Map.class);
			} else {
				String[] keyAndValues = req.variaveis.split("&");
				for (String keyAndValue : keyAndValues) {
					if (keyAndValue == null || keyAndValue.trim().isEmpty())
						continue;
					int idx = keyAndValue.indexOf("=");
					String key = keyAndValue.substring(0, idx);
					String value = keyAndValue.substring(idx + 1);
					value = URLDecoder.decode(value, StandardCharsets.UTF_8.name());
					paramsAsStrings.put(key, value);
				}
			}
		}

		WfProcedimento piAlterado = WfBL.converterVariaveisEProsseguir(procedimento, paramsAsStrings,
				req.indiceDoDesvio != null ? Integer.parseInt(req.indiceDoDesvio) : null, ctx.getTitular(),
				ctx.getLotaTitular(), ctx.getIdentidadeCadastrante());

		Procedimento r = new ProcedimentoAPIO(piAlterado, false, null, null, null);
		resp.procedimento = r;
	}

	@Override
	public String getContext() {
		return "continuar procedimento";
	}

}
