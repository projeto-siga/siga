package br.gov.jfrj.siga.wf.webwork.action;

import java.util.List;

import org.jbpm.graph.def.Node;
import org.jbpm.graph.exe.Token;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;

import com.opensymphony.xwork.Action;

public class WfAdminAction extends WfSigaActionSupport {

	public String moveToken() throws Exception {
		assertAcesso("ADMINISTRAR:Mover token");
		Long idToken = paramLong("idToken");
		Long idNode = paramLong("idNode");
		Token t = WfContextBuilder.getJbpmContext().getJbpmContext()
				.getToken(idToken);
		if (idNode == 0) {
			t.signal();
			return Action.SUCCESS;
		}
		List<Node> nodes = t.getProcessInstance().getProcessDefinition()
				.getNodes();
		for (Node n : nodes) {
			if (idNode.equals(n.getId())) {
				t.setNode(n);
				return Action.SUCCESS;
			}
		}
		throw new AplicacaoException("Nenhuma ação realizada");
	}

	public String endProcessInstance() throws Exception {
		assertAcesso("ADMINISTRAR:Finalizar instancia de processo");
		Wf.getInstance().getBL()
				.encerrarProcessInstance(paramLong("idPI"), paramDate("dtFim"));
		return Action.SUCCESS;
	}

	public String deleteProcessInstance() throws AplicacaoException, Exception {
		assertAcesso("ADMINISTRAR:Excluir instancia de processo");
		Wf.getInstance().getBL().excluirProcessInstance(paramLong("idPI"));
		return Action.SUCCESS;
	}
	
	public String administrar(){
		return Action.SUCCESS;
	}

}
