package br.gov.jfrj.siga.wf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mvel2.MVEL;
import org.mvel2.templates.TemplateRuntime;

import com.crivano.jflow.Handler;
import com.crivano.jflow.TaskResult;
import com.crivano.jflow.model.ProcessInstance;
import com.crivano.jflow.model.Responsible;

import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.dao.WfDao;

public class WfHandler implements Handler {

	@Override
	public void afterPause(ProcessInstance pi, TaskResult result) {
	}

	public static Object eval(ProcessInstance pi, String expression) {
		return MVEL.eval(expression, pi.getVariable());
	}

	@Override
	public boolean evalCondition(ProcessInstance pi, String expression) {
		return MVEL.eval(expression, pi.getVariable(), Boolean.class);
	}

	@Override
	public TaskResult evalTask(ProcessInstance pi, String expression) {
		HashMap<String, Object> m = new HashMap<>();
		m.putAll(pi.getVariable());
		m.put("context", new WfContext(pi));
		return MVEL.eval(expression, m, TaskResult.class);
	}

	@Override
	public String evalTemplate(ProcessInstance pi, String template) {
		HashMap<String, Object> m = new HashMap<>();
		m.putAll(pi.getVariable());
		m.put("context", new WfContext(pi));
		m.put("td", pi.getCurrentTaskDefinition());
		m.put("to", pi.calcResponsible(pi.getCurrentTaskDefinition()));
		return (String) TemplateRuntime.eval(template, m);
	}

	@Override
	public void sendEmail(Responsible responsible, String subject, String text) {
		List<String> destinatarios = new ArrayList<>();
		if (responsible instanceof DpPessoa) {
			destinatarios.add(((DpPessoa) responsible).getEmailPessoaAtual());
		} else if (responsible instanceof DpLotacao) {
			DpLotacao lota = (DpLotacao) responsible;
			List<DpPessoa> l = null;
			l = WfDao.getInstance().pessoasPorLotacao(lota.getId(), false, true);
			for (DpPessoa pessoa : l) {
				destinatarios.add(pessoa.getEmailPessoaAtual());
			}
		}
		try {
			Correio.enviar(destinatarios.toArray(new String[destinatarios.size()]), subject, text);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
