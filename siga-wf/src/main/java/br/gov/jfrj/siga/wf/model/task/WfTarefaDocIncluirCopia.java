package br.gov.jfrj.siga.wf.model.task;

import com.crivano.jflow.Engine;
import com.crivano.jflow.Task;
import com.crivano.jflow.TaskResult;
import com.crivano.jflow.model.enm.TaskResultKind;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.parser.SiglaParser;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.util.WfResp;

public class WfTarefaDocIncluirCopia implements Task<WfDefinicaoDeTarefa, WfProcedimento> {

	@Override
	public TaskResult execute(WfDefinicaoDeTarefa td, WfProcedimento pi, Engine engine) throws Exception {
		WfResp resp = pi.calcResponsible(td);

		String cadastranteStr = null; // SiglaParser.makeSigla(resp.getPessoa(), resp.getLotacao());

		if (td.getRefSigla() == null)
			throw new Exception("documento filho deve ser informado");
		String siglaMobilFilho = td.getRefSigla();

		String siglaMobilPai = pi.getPrincipal();

		Service.getExService().incluirCopiaDeDocumento(cadastranteStr, siglaMobilPai, siglaMobilFilho);

		return new TaskResult(TaskResultKind.DONE, null, null, null, null);
	}
}
