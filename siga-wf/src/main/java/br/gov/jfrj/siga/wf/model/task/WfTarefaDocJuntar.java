package br.gov.jfrj.siga.wf.model.task;

import com.crivano.jflow.Engine;
import com.crivano.jflow.Task;
import com.crivano.jflow.TaskResult;
import com.crivano.jflow.model.enm.TaskResultKind;
import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.parser.SiglaParser;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.util.WfResp;

public class WfTarefaDocJuntar implements Task<WfDefinicaoDeTarefa, WfProcedimento> {	
	/**
	 * Esse método é responsável por Juntar 2 documentos recebidos na tarefa Juntar
	 * Junta o documento principal(Filho) no documento informado pelo usuário(Pai).
	 * O documento pai é fixo (número preenchido na elaboração do diagrama)
	 *
	 * @param td Tarefa.
	 * @param pi Procedimento.
	 * @return TaskResult Resultado da execução da tarefa.
	 */
	@Override
	public TaskResult execute(
			WfDefinicaoDeTarefa td, 
			WfProcedimento pi, 
			Engine engine) throws Exception {
		
		String siglaDestino = "";
		String siglaCadastrante = "";
		String codigoDocumentoPai = "";
		String codigoDocumentoPrincipal = "";
		String codigoDocumentoFilho = "";
		
		codigoDocumentoPai = recebeDocumentoPaiDaEntradaDoUsuario(td, pi, engine);
		codigoDocumentoPrincipal = pi.getPrincipal();	
		codigoDocumentoFilho = codigoDocumentoPrincipal;
		
		if (!Utils.empty(codigoDocumentoPrincipal)) { 
			WfResp responsavel = pi.calcResponsible(td);
			siglaDestino = geraSiglaDoResponsavel(responsavel);
			siglaCadastrante = geraSiglaDoResponsavel(responsavel);
			assert codigoDocumentoPai != null && codigoDocumentoPai != "";
			assert codigoDocumentoFilho != null && codigoDocumentoFilho != "";
			Service.getExService().juntar(codigoDocumentoFilho, 
					codigoDocumentoPai, 
					siglaDestino, 
					siglaCadastrante);
		}
		return new TaskResult(TaskResultKind.DONE, null, null, null, null);
	}
	
	private String geraSiglaDoResponsavel(WfResp destino) {
		String siglaDestino = "";
		if (destino != null) { 
			DpPessoa pessoa = destino.getPessoa();
			DpLotacao lotacao = destino.getLotacao();
			try {
			    siglaDestino = SiglaParser.makeSigla(pessoa, lotacao);
			} catch (NullPointerException npe) {
				siglaDestino = "";
				}
			}
		return siglaDestino;
	}
	
	private String recebeDocumentoPaiDaEntradaDoUsuario(WfDefinicaoDeTarefa td, 
			WfProcedimento pi, 
			Engine engine) {
	    try {
	        return engine.getHandler().evalTemplate(pi, td.getSubject());
	    } catch (NullPointerException npe) {
	        System.err.println("Erro: Um valor nulo foi encontrado.");
	        return null;
	    } catch (Exception e) {
	        System.err.println("Erro ao avaliar o template: " + e.getMessage());
	        return null;
	    }
	}

}
