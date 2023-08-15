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
	/*TODO: 
	   []1 - A nova tarefa deve juntar o documento principal no documento informado pelo usuário.
 	   []2 - O documento pai pode ser fixo (número preenchido na elaboração do diagrama) ou
 	   []3 - Obtido através do valor de uma variável váriável do WF (incluindo documentos criados pelo próprio workflow) ou campo constante de algum documento juntado ao principal.
 	   []3 - Também deve ser possível informar se o documento pai deve se tornar ou não o novo Principal do WF. 
	 */
	//TODO: Diminuir tamanho do campo no front-end
	//TODO: Refatoração - apagar comentários e códigos desnecessários
	@Override
	public TaskResult execute(WfDefinicaoDeTarefa td, WfProcedimento pi, Engine engine) throws Exception {
		String siglaDestino = "";
		String siglaCadastrante = "";
		String codigoDocumentoViaFilho = "";
		String codigoDocumentoViaPai = "";
			
		//TODO: RECEBE O DOCUMENTO PAI DIGITADO PELO USUÁRIO
		//String text = engine.getHandler().evalTemplate(pi, td.getText());
		String documento_pai = engine.getHandler().evalTemplate(pi, td.getSubject());
		//TODO: Corrigir erro: text recebeu teste 09-08-23, tem que RECEBER DOCUMENTO FILHO
		/*
		 * No campo Tipo de resolponsável, a opção cadastrante retorna o que deverá sar cadastrado no [siglaCadastrante]
		 * No campo Tipo de resolponsável, a opção lotaçãop do titular o que deverá sar cadastrado no [siglaResponsável]
		 */
		//DpPessoa titular = pi.getTitular();
		//DpLotacao lotacaoDoTitular = pi.getLotaTitular();
		//if (!Utils.empty(pi.getPrincipal())) { //pi.getprincipal = documento principal: OTZZ-MEM-2023/00004-A
		
		String documentoPrincipal = pi.getPrincipal(); //pi.getprincipal = documento principal: OTZZ-MEM-2023/00004-A	
		if (!Utils.empty(documentoPrincipal)) { 
			WfResp destino = pi.calcResponsible(td);
			
			if (destino != null) { 
				//siglaDestino = SiglaParser.makeSigla(destino.getPessoa(), destino.getLotacao());
				DpPessoa pessoa = destino.getPessoa(); // veio null
				DpLotacao lotacao = destino.getLotacao(); // veio null
				//siglaDestino = SiglaParser.makeSigla(pessoa, lotacao); //sigladestino = @ZZLTEST
				try {
				    siglaDestino = SiglaParser.makeSigla(pessoa, lotacao);
				} catch (NullPointerException npe) {
					siglaDestino = "";
					}
				}
			// String codigoDocumentoViaFilho = documentoPrincipal;
			//siglaDestino = "@ZZLTEST";
			siglaCadastrante = siglaDestino;
			codigoDocumentoViaFilho = "OTZZ-MEM-2023/00004-A"; // de onde vem doc filho?
			codigoDocumentoViaPai = documento_pai;
			
			//Esse código deverá receber o documento selecionado pelo usuário em um combobox
			codigoDocumentoViaPai = pi.getPrincipal();
			//codigoDocumentoViaPai = "OTZZ-MEM-2023/00005-A";   // de onde vem doc pai?
			//assert siglaDestino != null;
			/*
			 * codigoDocumentoPai = OTZZ-MEM-2023/00004-A
			 * codigoDocumentoViaFilho = OTZZ-MEM-2023/00005-A
			 * siglaDestino = ZZLTEST
			 * siglaCadastrante = ZZLTEST
			 */
			Service.getExService().juntar(codigoDocumentoViaFilho, codigoDocumentoViaPai, siglaDestino, siglaCadastrante);
		}
		return new TaskResult(TaskResultKind.DONE, null, null, null, null);
	}
}
