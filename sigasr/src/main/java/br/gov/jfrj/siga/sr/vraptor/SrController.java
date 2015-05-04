package br.gov.jfrj.siga.sr.vraptor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.validator.SrError;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaController;
import br.gov.jfrj.siga.vraptor.SigaObjects;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class SrController extends SigaController {

	protected SrValidator srValidator;

	public SrController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	@Override
	protected void assertAcesso(String pathServico) {
		super.assertAcesso("SR:Módulo de Serviços;" + pathServico);
	}

	public void enviarErroValidacao() {
		result.use(Results.http()).sendError(HttpStatus.SC_BAD_REQUEST, jsonErrors().toString());
	}

	private JsonArray jsonErrors() {
		JsonArray jsonArray = new JsonArray();

		List<SrError> errors = srValidator.getErros();
		for (SrError error : errors) {
			jsonArray.add(new Gson().toJsonTree(error));
		}

		return jsonArray;
	}
}
