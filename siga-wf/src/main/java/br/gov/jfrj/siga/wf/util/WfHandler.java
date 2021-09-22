package br.gov.jfrj.siga.wf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mvel2.MVEL;
import org.mvel2.templates.TemplateRuntime;

import com.crivano.jflow.Handler;
import com.crivano.jflow.TaskResult;

import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.bl.WfBL;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.model.WfProcedimento;

public class WfHandler implements Handler<WfProcedimento, WfResp> {

	DpPessoa titular;
	DpLotacao lotaTitular;
	CpIdentidade identidade;
	boolean transicionou = false;

	public WfHandler(DpPessoa titular, DpLotacao lotaTitular, CpIdentidade identidade) {
		this.titular = titular;
		this.lotaTitular = lotaTitular;
		this.identidade = identidade;
	}

	public static Object eval(WfProcedimento pi, String expression) {
		return MVEL.eval(expression, pi.getVariable());
	}

	@Override
	public boolean evalCondition(WfProcedimento pi, String expression) {
		return MVEL.eval(expression, pi.getVariable(), Boolean.class);
	}

	@Override
	public TaskResult evalTask(WfProcedimento pi, String expression) {
		HashMap<String, Object> m = new HashMap<>();
		m.putAll(pi.getVariable());
		m.put("context", new WfContext(pi));
		return MVEL.eval(expression, m, TaskResult.class);
	}

	@Override
	public String evalTemplate(WfProcedimento pi, String template) {
		HashMap<String, Object> m = new HashMap<>();
		m.putAll(pi.getVariable());
		m.put("context", new WfContext(pi));
		m.put("td", pi.getCurrentTaskDefinition());
		m.put("to", pi.calcResponsible(pi.getCurrentTaskDefinition()));
		return (String) TemplateRuntime.eval(template, m);
	}

	@Override
	public void sendEmail(WfResp responsible, String subject, String text) {
		List<String> destinatarios = new ArrayList<>();
		if (responsible.getPessoa() != null) {
			destinatarios.add(responsible.getPessoa().getEmailPessoaAtual());
		} else if (responsible.getLotacao() != null) {
			List<DpPessoa> l = null;
			l = WfDao.getInstance().pessoasPorLotacao(responsible.getLotacao().getId(), false, true);
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

	@Override
	public void afterPause(WfProcedimento pi, TaskResult result) {
		String siglaTitular = null;
		if (titular != null && lotaTitular != null)
			siglaTitular = titular.getSigla() + "@" + lotaTitular.getSiglaCompleta();
		try {
			if (transicionou)
				WfBL.transferirDocumentosVinculados(pi, siglaTitular);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void afterTransition(WfProcedimento pi, Integer de, Integer para) {
		Wf.getInstance().getBL().registrarTransicao(pi, de, para, titular, lotaTitular, identidade);
		transicionou = true;
	}
}
