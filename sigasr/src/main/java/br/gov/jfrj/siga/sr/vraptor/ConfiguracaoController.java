package br.gov.jfrj.siga.sr.vraptor;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import play.db.jpa.JPA;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.validator.SrError;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class ConfiguracaoController extends SrController {

	public ConfiguracaoController(HttpServletRequest request, Result result,
			CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	public static String desativarDesignacao(Long id, boolean mostrarDesativados) throws Exception {
//		assertAcesso("ADM:Administrar");
		SrConfiguracao designacao = JPA.em().find(SrConfiguracao.class, id);
		designacao.finalizar();

		return designacao.getSrConfiguracaoJson();
	}

	public static String reativarDesignacao(Long id, boolean mostrarDesativados) throws Exception {
//		assertAcesso("ADM:Administrar");
		SrConfiguracao designacao = JPA.em().find(SrConfiguracao.class, id);
		designacao.salvar();

		return designacao.getSrConfiguracaoJson();
	}

	public String gravarDesignacao(SrConfiguracao designacao) throws Exception {
//		assertAcesso("ADM:Administrar");
		validarFormEditarDesignacao(designacao);
		designacao.salvarComoDesignacao();
		designacao.refresh();
		return designacao.getSrConfiguracaoJson();
	}

	private void validarFormEditarDesignacao(SrConfiguracao designacao) throws Exception {
		StringBuffer sb = new StringBuffer();

		if (designacao.getDescrConfiguracao() == null || designacao.getDescrConfiguracao().isEmpty())
			srValidator.addError("designacao.descrConfiguracao", "Descrição não informada");

		for (SrError error: srValidator.getErros()) {
			sb.append(error.getKey() + ";");
		}

		if (srValidator.hasErrors()) {
			throw new Exception(sb.toString());
		}
	}

}
