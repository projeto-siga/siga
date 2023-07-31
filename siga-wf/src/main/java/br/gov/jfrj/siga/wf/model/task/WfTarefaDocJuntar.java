/* 
 * TODO: Desenvolver código do Juntar
 * O código nesse arquivo será executado quando um workflow
 * com uma tarefa do tipo Juntar for executado
*/
	//os métodos juntar e transferir ficam no arquivo:
	//siga-ws/src/main/java/br/gov/jfrj/siga/ex/service/ExService.java
	//Service.getExService().transferir executa o tramitar que era chamado de transferir
	//Service.getExService().transferir(pi.getPrincipal(), siglaDestino, null, true);
	//Service.getExService().juntar(pi.getPrincipal(), codigoDocumentoViaPai, codigoDocumentoViaFilho,
	//siglaDestino, null, true);
	// Executar um WF com uma task JUNTAR vai rodar o método abaixo:

package br.gov.jfrj.siga.wf.model.task;

import com.crivano.jflow.Engine;
import com.crivano.jflow.Task;
import com.crivano.jflow.TaskResult;
import com.crivano.jflow.model.enm.TaskResultKind;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.parser.SiglaParser;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.util.WfResp;

public class WfTarefaDocJuntar implements Task<WfDefinicaoDeTarefa, WfProcedimento> {
	
	@Override
	public TaskResult execute(WfDefinicaoDeTarefa td, WfProcedimento pi, Engine engine) throws Exception {
			//1 - RECEBER DOCUMENTO PAI
			//2 - RECEBER DOCUMENTO FILHO
			
			//3 - VERIFICAR SE PODE JUNTAR
			
			//JUNTAR TEM RESPONSÁVEL?
			//PERGUNTAR QUEM DEVERÁ SER O RESPONSÁVEL APÓS JUNTAR?
			//VAI TER PADRÃO OU O USUÁRIO VAI ESCOLHER?
			//NO FRONT-END - COPIAR A FORMA COMO É FEITO NO TRAMITAR E DEIXAR O USUÁRIO ESCOLHER
			String siglaDestino = null;
			
			System.out.println("td");
			System.out.println(td);
			System.out.println("pi");
			System.out.println(pi);
			System.out.println("engine");
			System.out.println(engine);
			System.out.println("pi.getPrincipal()");
			System.out.println(pi.getPrincipal());
			System.out.println("!Utils.empty(pi.getPrincipal())");
			System.out.println(!Utils.empty(pi.getPrincipal()));
			
		if (!Utils.empty(pi.getPrincipal())) {
			WfResp destino = pi.calcResponsible(td);
			System.out.println("destino");
			System.out.println(destino);
			
			if (destino != null) {
				System.out.println("destino.getPessoa()");
				System.out.println(destino.getPessoa());
				System.out.println("destino.getLotacao()");
				System.out.println(destino.getLotacao());
				
				siglaDestino = SiglaParser.makeSigla(destino.getPessoa(), destino.getLotacao());
				
				System.out.println("siglaDestino");
				System.out.println(siglaDestino);
			}
			
			
			String documentoPrincipal = pi.getPrincipal();
			System.out.println("documentoPrincipal");
			System.out.println(documentoPrincipal);
			/* Saída:
			 * 17:49:28,482 INFO  [stdout] (default task-23) documentoPrincipal
			   17:49:28,502 INFO  [stdout] (default task-23) OTZZ-MEM-2023/00005-A
			 */
			// TODO: A nova tarefa deve juntar o documento principal no documento informado pelo usuário.
			// String codigoDocumentoViaFilho = documentoPrincipal;
			
			
			// Responsável é a pessoa que aparece no para
			String codigoDocumentoViaFilho = "OTZZ-MEM-2023/00004-A";
			String codigoDocumentoViaPai = "OTZZ-MEM-2023/00005-A";
			String siglaCadastrante = null;
			

			Service.getExService().juntar(codigoDocumentoViaFilho, codigoDocumentoViaPai, siglaDestino, siglaCadastrante);
		}
		return new TaskResult(TaskResultKind.DONE, null, null, null, null);
	}
	
}
