package br.gov.jfrj.siga.wf.model.task;

import java.util.Map;

import com.crivano.jflow.Engine;
import com.crivano.jflow.TaskResult;
import com.crivano.jflow.model.enm.TaskResultKind;
import com.crivano.jflow.task.TaskForm;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.parser.SiglaParser;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeDesvio;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeVariavel;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeResponsavel;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeTarefa;
import br.gov.jfrj.siga.wf.util.WfResp;

public class WfTarefaDocCriar extends
		TaskForm<WfDefinicaoDeProcedimento, WfDefinicaoDeTarefa, WfResp, WfTipoDeTarefa, WfTipoDeResponsavel, WfDefinicaoDeVariavel, WfDefinicaoDeDesvio, WfProcedimento> {

	@Override
	public String getEvent(WfDefinicaoDeTarefa tarefa, WfProcedimento pi) {
		return getSiglaDoDocumentoCriado(pi) + "|" + pi.getId() + "|" + tarefa.getId();
	}

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

		Boolean finalizar = "FINALIZAR".equals(td.getParam());
		if (finalizar && td.getRefId2() == null)
			throw new Exception("preenchimento automático deve ser informado para que seja possível finalizar a elaboração do documento");

		String nomePreenchimento = null;
		if (td.getRefId2() != null)
			nomePreenchimento = Long.toString(td.getRefId2());

		String classificacaoStr = null;
		String descricaoStr = null;
		Boolean eletronico = true;
		String nomeNivelDeAcesso = null;
		String conteudo = null;
		String siglaMobilPai = getSiglaMobilPai(pi);
		String siglaMobilFilho = getSiglaMobilFilho(pi);

		// Nato: Esse flush é necessário porque o documento precisará dos dados
		// atualizados do workflow para processar o modelo
		ContextoPersistencia.flushTransaction();
		String valor = Service.getExService().criarDocumento(cadastranteStr, subscritorStr, destinatarioStr,
				destinatarioCampoExtraStr, descricaoTipoDeDocumento, nomeForma, nomeModelo, nomePreenchimento,
				classificacaoStr, descricaoStr, eletronico, nomeNivelDeAcesso, conteudo, siglaMobilPai, siglaMobilFilho,
				"PROCEDIMENTO", pi.getSigla(), finalizar);

		pi.getVariable().put(getIdentificadorDaVariavel(pi.getDefinicaoDeTarefaCorrente()), valor);

		return resume(td, pi, null, null, engine);
	}

	public String getSiglaMobilPai(WfProcedimento pi) {
		return pi.getPrincipal();
	}

	public String getSiglaMobilFilho(WfProcedimento pi) {
		return null;
	}

	public boolean isAguardarAssinatura(WfDefinicaoDeTarefa td) {
		return "AGUARDAR_ASSINATURA".equals(td.getParam2());
	}
	
	public boolean isAguardarJuntada(WfDefinicaoDeTarefa td) {
		return "AGUARDAR_JUNTADA".equals(td.getParam2());
	}

	public static String getIdentificadorDaVariavel(WfDefinicaoDeTarefa td) {
		String s = Texto.slugify(td.getNome(), true, true);
		if (!s.startsWith("doc_"))
			s = "doc_" + s;
		return s;
	}

	public static String getSiglaDoDocumentoCriado(WfProcedimento pi) {
		return (String) pi.getVariable().get(getIdentificadorDaVariavel(pi.getDefinicaoDeTarefaCorrente()));
	}

	@Override
	public TaskResult resume(WfDefinicaoDeTarefa td, WfProcedimento pi, Integer detourIndex, Map<String, Object> param,
			Engine<?, ?, ?> engine) throws Exception {
		String siglaDoDocumentoCriado = getSiglaDoDocumentoCriado(pi);
		
		// Caso o documento tenha sido finalizado, atualiza a variável
		if (siglaDoDocumentoCriado.startsWith("TMP")) {
			String siglaAtualDoDocumento = Service.getExService().obterSiglaAtual(siglaDoDocumentoCriado);
			if (!siglaDoDocumentoCriado.equals(siglaAtualDoDocumento)) {
				pi.getVariable().put(getIdentificadorDaVariavel(pi.getDefinicaoDeTarefaCorrente()), siglaAtualDoDocumento);
				siglaDoDocumentoCriado = siglaAtualDoDocumento;
			}
		}
		
		if (isAguardarAssinatura(td) && !Service.getExService().isAssinado(siglaDoDocumentoCriado, null))
			return new TaskResult(TaskResultKind.PAUSE, null, null, getEvent(td, pi), pi.calcResponsible(td));
		if (isAguardarJuntada(td) && !Service.getExService().isJuntado(siglaDoDocumentoCriado, getSiglaMobilPai(pi)))
			return new TaskResult(TaskResultKind.PAUSE, null, null, getEvent(td, pi), pi.calcResponsible(td));
		return new TaskResult(TaskResultKind.DONE, null, null, null, null);
	}

}
