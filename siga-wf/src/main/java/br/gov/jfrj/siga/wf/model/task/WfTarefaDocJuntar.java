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
		Boolean DocumentoPaiSeraONovoPrincipal = false;
		
		//TODO: Também deve ser possível informar se o documento pai deve se tornar ou não o novo Principal do WF.
			//TODO: #1 Alterar front-end, opção doc pai será o novo principal? passar info do front pro back
			//TODO: #2 Alterar TarefaDocJuntar, receber true ou false - receber info no back e salvar na variavel
		
		//if (usuário selecionou sim no campo DocumentoPaiSeraONovoPrincipal){
		//	DocumentoPaiSeraONovoPrincipal = true;
		//}
		// Foram testado: como true funcionou e como false funcionou
		
		//Exemplo de principal no banco de dados: String "OTZZ-MEM-2023/00005-A"
		//Por padrão o documento principal é o selecionado na tela do procedimento, que é o documento filho também
		
		//Por padrão o documento principal é o documento filho
		//Se o usuário marcar documento pai é o novo principal, muda isso
		
		codigoDocumentoPai = recebeDocumentoPaiDaEntradaDoUsuario(td, pi, engine);
		assert codigoDocumentoPai != null && codigoDocumentoPai != "";
		
		codigoDocumentoPrincipal = pi.getPrincipal();
		assert codigoDocumentoPrincipal != null;
		
		codigoDocumentoFilho = codigoDocumentoPrincipal;
		assert codigoDocumentoFilho != null && codigoDocumentoFilho != "";
		
		if (DocumentoPaiSeraONovoPrincipal) {
			pi.setPrincipal(codigoDocumentoPai);
			assert pi.getPrincipal() == codigoDocumentoPai;
		}
			
		
		if (!Utils.empty(codigoDocumentoPrincipal)) { 
			WfResp responsavel = pi.calcResponsible(td);
			siglaDestino = geraSiglaDoResponsavel(responsavel);
			siglaCadastrante = geraSiglaDoResponsavel(responsavel);
			
			
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
