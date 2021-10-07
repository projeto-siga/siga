package br.gov.jfrj.siga.wf.model.task;

import com.crivano.jflow.Engine;
import com.crivano.jflow.Task;
import com.crivano.jflow.TaskResult;
import com.crivano.jflow.model.enm.TaskResultKind;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.parser.SiglaParser;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.util.WfResp;

public class WfTarefaDocCriar implements Task<WfDefinicaoDeTarefa, WfProcedimento> {

	@Override
	public TaskResult execute(WfDefinicaoDeTarefa td, WfProcedimento pi, Engine engine) throws Exception {
		WfResp resp = pi.calcResponsible(td);

		String cadastranteStr = SiglaParser.makeSigla(resp.getPessoa(), resp.getLotacao());
		String subscritorStr = null;
		String destinatarioStr = null;
		String destinatarioCampoExtraStr = null;
		String descricaoTipoDeDocumento = null;
		String nomeForma = null;

		if (td.getRefId() == null)
			throw new Exception("modelo deve ser informado");
		String nomeModelo = Long.toString(td.getRefId());

		if (td.getRefId2() == null)
			throw new Exception("preenchimento automático deve ser informado");
		String nomePreenchimento = Long.toString(td.getRefId2());

		String classificacaoStr = null;
		String descricaoStr = null;
		Boolean eletronico = true;
		String nomeNivelDeAcesso = null;
		String conteudo = null;
		String siglaMobilPai = pi.getPrincipal();
		Boolean finalizar = true;

		// Nato: Esse flush é necessário porque o documento precisará dos dados
		// atualizados do workflow para processar o modelo
		ContextoPersistencia.flushTransaction();
		String valor = Service.getExService().criarDocumento(cadastranteStr, subscritorStr, destinatarioStr, destinatarioCampoExtraStr,
				descricaoTipoDeDocumento, nomeForma, nomeModelo, nomePreenchimento, classificacaoStr, descricaoStr,
				eletronico, nomeNivelDeAcesso, conteudo, siglaMobilPai, "PROCEDIMENTO", pi.getSigla(), finalizar);
		
		String identificador = Texto.slugify(pi.getDefinicaoDeTarefaCorrente().getNome(), true, true);
		pi.getVariable().put(identificador, valor);

		return new TaskResult(TaskResultKind.DONE, null, null, null, null);
	}
}
