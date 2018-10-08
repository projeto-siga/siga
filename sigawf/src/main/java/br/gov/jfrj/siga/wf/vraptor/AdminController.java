package br.gov.jfrj.siga.wf.vraptor;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.jbpm.file.def.FileDefinition;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.Token;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.ByteArrayDownload;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Data;
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
		assertAcesso(ACESSO_MOVER_TOKEN);
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
		assertAcesso(ACESSO_ENCERRAR_INSTANCIA_DE_PROCESSO);		
		
		if (dtFim == null) {
			dtFim = new Date();
		} else {
			  if (!Data.dataDentroSeculo21(dtFim)){
				  throw new AplicacaoException("Data fim inválida, deve estar entre o ano 2000 e ano 2100");
		
			  }
		}
	
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
		assertAcesso(ACESSO_EXCLUIR_INSTANCIA_DE_PROCESSO);
		Wf.getInstance().getBL().excluirProcessInstance(idPI);
		redirectToHome();
	}
	
	@Get
	@Path("/app/download/{pdId}")
	public ByteArrayDownload downloadArquivoDeployed(Long pdId) throws IOException {
		ByteArrayOutputStream baos = null;
		ZipOutputStream zos = null;

		try{
			String _proc_img = "processimage.jpg";
			String _proc_def = "processdefinition.xml";
			String _gpd_xml = "gpd.xml";
			
			ProcessDefinition processDefinition = WfDao.getInstance().getProcessDefinition(pdId);
			FileDefinition fileDefinition = processDefinition.getFileDefinition();
			
			baos = new ByteArrayOutputStream();
			zos = new ZipOutputStream(baos);
			
			byte[] arquivo = fileDefinition.getBytes(_proc_img);
			ZipEntry entry = new ZipEntry(_proc_img);
			entry.setSize(arquivo.length);
			zos.putNextEntry(entry);
			zos.write(arquivo);
			zos.closeEntry();
			
			arquivo = fileDefinition.getBytes(_proc_def);
			entry = new ZipEntry(_proc_def);
			entry.setSize(arquivo.length);
			zos.putNextEntry(entry);
			zos.write(arquivo);
			zos.closeEntry();
		
			arquivo = fileDefinition.getBytes(_gpd_xml);
			entry = new ZipEntry(_gpd_xml);
			entry.setSize(arquivo.length);
			zos.putNextEntry(entry);
			zos.write(arquivo);
			zos.closeEntry();

			zos.close();
			
			return new ByteArrayDownload(baos.toByteArray(), "application/zip", "process-definition-" + processDefinition.getName() + "-" + pdId + ".zip");

		}finally{
			baos.close();
			zos.close();
		}
	}

}
