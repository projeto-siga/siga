package br.gov.jfrj.siga.wf.webwork.action;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.jbpm.bytes.ByteArray;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.exe.Token;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;

import com.opensymphony.xwork.Action;

public class WfAdminAction extends WfSigaActionSupport {

	Long idTI;
	Long idPI;
	Date dtFim;
	
	public String moveToken() throws Exception {
		assertAcesso("MOVER_TOKEN:Mover token");
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
		assertAcesso("ENCERRAR:Encerrar instancia de processo");
		dtFim=dtFim==null?new Date():dtFim;
		
		if (idTI!=null){
			Wf.getInstance().getBL().encerrarProcessInstanceDaTarefa(idTI, dtFim);
			setMensagem("Processo encerrado com sucesso!");
			return Action.SUCCESS;
		}
		
		if (idPI !=null){
			Wf.getInstance().getBL()
			.encerrarProcessInstance(idPI,dtFim);
			setMensagem("Processo encerrado com sucesso!");
		}
		return Action.SUCCESS;
	}

	public void downloadArquivoDeployed() throws IOException{
		Long id_processimage = null;
		Long id_processdefinition = null;
		Long id_gpd = null;
		ByteArray b = dao().consultar(id_processimage, ByteArray.class, false);
		FileOutputStream fos = new FileOutputStream("processimage.jpg");
		fos.write(b.getBytes());
		fos.close();
		
		b = dao().consultar(id_processdefinition, ByteArray.class, false);
		fos = new FileOutputStream("processdefinition.xml");
		fos.write(b.getBytes());
		fos.close();

		b = dao().consultar(id_gpd, ByteArray.class, false);
		fos = new FileOutputStream("gpd.xml");
		fos.write(b.getBytes());
		fos.close();
		
	}
	
	public String deleteProcessInstance() throws AplicacaoException, Exception {
		assertAcesso("EXCLUIR:Excluir instancia de processo");
		Wf.getInstance().getBL().excluirProcessInstance(paramLong("idPI"));
		return Action.SUCCESS;
	}
	
	public String administrar(){
		return Action.SUCCESS;
	}

	public Long getIdTI() {
		return idTI;
	}

	public void setIdTI(Long idTI) {
		this.idTI = idTI;
	}

	public Long getIdPI() {
		return idPI;
	}

	public void setIdPI(Long idPI) {
		this.idPI = idPI;
	}

	public Date getDtFim() {
		return dtFim;
	}

	public void setDtFim(Date dtFim) {
		this.dtFim = dtFim;
	}
	
	
	
	

}
