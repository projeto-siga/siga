package br.gov.jfrj.siga.wf.vraptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfUtil;

@Controller
public class WfAdminController extends WfController {

	public WfAdminController(HttpServletRequest request, Result result, WfDao dao, SigaObjects so, WfUtil util) {
		super(request, result, dao, so, util);
	}

	public void administrar() {
	}

	@SuppressWarnings("unchecked")
	public void moveToken(Long idToken, Long idNode) throws Exception {
//		assertAcesso(ACESSO_MOVER_TOKEN);
//		Token t = WfContextBuilder.getJbpmContext().getJbpmContext()
//				.getToken(idToken);
//		if (idNode == 0) {
//			t.signal();
//			redirectToHome();
//			return;
//		}
//		List<Node> nodes = t.getProcessInstance().getProcessDefinition()
//				.getNodes();
//		for (Node n : nodes) {
//			if (idNode.equals(n.getId())) {
//				t.setNode(n);
//				redirectToHome();
//				return;
//			}
//		}
//		throw new AplicacaoException("Nenhuma ação realizada");
	}

	public void endProcessInstance(Long idTI, Long idPI, Date dtFim) throws Exception {
//		assertAcesso(ACESSO_ENCERRAR_INSTANCIA_DE_PROCESSO);		
//		
//		if (dtFim == null) {
//			dtFim = new Date();
//		} else {
//			  if (!Data.dataDentroSeculo21(dtFim)){
//				  throw new AplicacaoException("Data fim inválida, deve estar entre o ano 2000 e ano 2100");
//		
//			  }
//		}
//	
//		if (idTI != null) {
//			Wf.getInstance().getBL()
//					.encerrarProcessInstanceDaTarefa(idTI, dtFim);
//			setMensagem("Processo encerrado com sucesso!");
//			redirectToHome();
//			return;
//		}
//
//		if (idPI != null) {
//			Wf.getInstance().getBL().encerrarProcessInstance(idPI, dtFim);
//			setMensagem("Processo encerrado com sucesso!");
//		}
//		redirectToHome();
	}

	public void deleteProcessInstance(Long idPI) throws AplicacaoException, Exception {
//		assertAcesso(ACESSO_EXCLUIR_INSTANCIA_DE_PROCESSO);
//		Wf.getInstance().getBL().excluirProcessInstance(idPI);
//		redirectToHome();
	}
}
