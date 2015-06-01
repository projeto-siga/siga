package br.gov.jfrj.siga.wf.vraptor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jbpm.bytes.ByteArray;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.exe.Token;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;

@Resource
public class AdminController extends WfController {

	public AdminController(HttpServletRequest request, Result result,
			WfDao dao, SigaObjects so, WfUtil util) {
		super(request, result, dao, so, util);
	}

	public void administrar() {
	}

	@SuppressWarnings("unchecked")
	public void moveToken(Long idToken, Long idNode) throws Exception {
		assertAcesso("MOVER_TOKEN:Mover token");
		Token t = WfContextBuilder.getJbpmContext().getJbpmContext()
				.getToken(idToken);
		if (idNode == 0) {
			t.signal();
			redirectToHome();
			return;
		}
		List<Node> nodes = t.getProcessInstance().getProcessDefinition()
				.getNodes();
		for (Node n : nodes) {
			if (idNode.equals(n.getId())) {
				t.setNode(n);
				redirectToHome();
				return;
			}
		}
		throw new AplicacaoException("Nenhuma ação realizada");
	}

	public void endProcessInstance(Long idTI, Long idPI, Date dtFim) throws Exception {
		assertAcesso("ENCERRAR:Encerrar instancia de processo");
		dtFim = dtFim == null ? new Date() : dtFim;

		if (idTI != null) {
			Wf.getInstance().getBL()
					.encerrarProcessInstanceDaTarefa(idTI, dtFim);
			setMensagem("Processo encerrado com sucesso!");
			redirectToHome();
			return;
		}

		if (idPI != null) {
			Wf.getInstance().getBL().encerrarProcessInstance(idPI, dtFim);
			setMensagem("Processo encerrado com sucesso!");
		}
		redirectToHome();
	}

	public void deleteProcessInstance(Long idPI) throws AplicacaoException, Exception {
		assertAcesso("EXCLUIR:Excluir instancia de processo");
		Wf.getInstance().getBL().excluirProcessInstance(idPI);
		redirectToHome();
	}
	
	private void downloadArquivoDeployed() throws IOException {
		Long id_processimage = null;
		Long id_processdefinition = null;
		Long id_gpd = null;
		ByteArray b = dao.consultar(id_processimage, ByteArray.class, false);
		FileOutputStream fos = new FileOutputStream("processimage.jpg");
		fos.write(b.getBytes());
		fos.close();

		b = dao.consultar(id_processdefinition, ByteArray.class, false);
		fos = new FileOutputStream("processdefinition.xml");
		fos.write(b.getBytes());
		fos.close();

		b = dao.consultar(id_gpd, ByteArray.class, false);
		fos = new FileOutputStream("gpd.xml");
		fos.write(b.getBytes());
		fos.close();

	}

}
