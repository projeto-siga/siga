package br.gov.jfrj.siga.wf.vraptor;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.instantiation.Delegation;
import org.jbpm.taskmgmt.def.Swimlane;
import org.jbpm.taskmgmt.def.Task;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;

@Resource
public class EdicaoController extends WfController {

	public EdicaoController(HttpServletRequest request, Result result,
			WfDao dao, SigaObjects so, WfUtil util) {
		super(request, result, dao, so, util);
	}

	@Get
	@Path("/app/edicao/form/{procedimento}")
	public void form(String procedimento) {
		result.include("procedimento", procedimento);
	}

	@Get
	@Path("/app/edicao/processdefinition/{procedimento}")
	public void ler(String procedimento) throws Exception {
		ProcessDefinition pd = WfContextBuilder.getJbpmContext()
				.getJbpmContext().getGraphSession()
				.findLatestProcessDefinition(procedimento);
		byte ab[] = pd.getFileDefinition().getBytes("processdefinition.xml");
		String sXML = new String(ab, "UTF-8");
		result.use(Results.http()).body(sXML);
	}

	@Post
	@Path("/app/edicao/processdefinition/{procedimento}")
	public void gravar(String procedimento, String xml) throws Exception {
		String res = "OK";//doDeployment(xml);
		result.use(Results.http()).body(res);
	}

	@SuppressWarnings("unchecked")
	private String doDeployment(String xml) {
		ProcessDefinition processDefinition = ProcessDefinition
				.parseXmlString(xml);
		WfContextBuilder.getJbpmContext().getJbpmContext()
				.deployProcessDefinition(processDefinition);
		long id = processDefinition.getId();

		String sReturn = "Deployed archive " + processDefinition.getName()
				+ " successfully";

		Delegation d = new Delegation(
				"br.gov.jfrj.siga.wf.util.WfAssignmentHandler");

		for (Swimlane s : ((Collection<Swimlane>) processDefinition
				.getTaskMgmtDefinition().getSwimlanes().values())) {
			if (s.getTasks() != null)
				for (Object t : s.getTasks()) {
					System.out.println(((Task) t).toString());
				}
			if (s.getAssignmentDelegation() == null)
				s.setAssignmentDelegation(d);
		}

		for (Task t : ((Collection<Task>) processDefinition
				.getTaskMgmtDefinition().getTasks().values())) {
			if (t.getSwimlane() == null && t.getAssignmentDelegation() == null)
				t.setAssignmentDelegation(d);
		}

		return sReturn;
	}

}
