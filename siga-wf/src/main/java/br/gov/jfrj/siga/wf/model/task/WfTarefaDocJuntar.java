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
			String siglaDestino = null;
			
			//TODO: resolver o erro "A via não pode ser juntada ao documento porque ele não pode ser movimentado."
			//TODO: juntar o documento principal no documento informado pelo usuário.
		
			//TODO: RECEBER DOCUMENTO PAI
			//TODO: Diminuir tamanho do campo no front-end
			//Recebe a string digitada pelo usuário no campo texto que receberá o documento
			String text = engine.getHandler().evalTemplate(pi, td.getText());
			
			//TODO: RECEBER DOCUMENTO FILHO
			
			//TODO: VERIFICAR SE PODE JUNTAR antes de tentar executar o procedimento
			// no ExBl.java tem a referencia do código que faz isso
			
			//TODO: esclarecer essa questão:
			//JUNTAR TEM RESPONSÁVEL? QUEM DEVERÁ SER O RESPONSÁVEL APÓS JUNTAR?
			//VAI TER PADRÃO OU O USUÁRIO VAI ESCOLHER?
			
			//TODO: Refatoração - apagar comentários e códigos desnecessários
			
			
		if (!Utils.empty(pi.getPrincipal())) {
			WfResp destino = pi.calcResponsible(td);
			/*
			System.out.println("destino");
			System.out.println(destino);
			*/
			if (destino != null) {
				
				siglaDestino = SiglaParser.makeSigla(destino.getPessoa(), destino.getLotacao());
				
				
			}
			
			
			String documentoPrincipal = pi.getPrincipal();
			// System.out.println("documentoPrincipal");
			// System.out.println(documentoPrincipal);
			/* Saída: OTZZ-MEM-2023/00005-A */
			// String codigoDocumentoViaFilho = documentoPrincipal;
			
			// Responsável é a pessoa que aparece no para
			String codigoDocumentoViaFilho = "OTZZ-MEM-2023/00004-A";
			String codigoDocumentoViaPai = "OTZZ-MEM-2023/00005-A";
			String siglaCadastrante = null;
			
			//adicionada essa linha para tentar prevenir o erro "A via não pode ser juntada ao documento porque ele não pode ser movimentado."
			//não resolveu
			//siglaDestino = null;
			

			Service.getExService().juntar(codigoDocumentoViaFilho, codigoDocumentoViaPai, siglaDestino, siglaCadastrante);
		}
		return new TaskResult(TaskResultKind.DONE, null, null, null, null);
	}
	
}
