package br.gov.jfrj.siga.wf.api.v1.apio;

import br.gov.jfrj.siga.jee.SigaLibsEL;
import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.Procedimento;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.util.WfTarefa;

public class ProcedimentoAPIO extends Procedimento {
	public ProcedimentoAPIO(WfTarefa t) {
		WfProcedimento pi = t.getInstanciaDeProcedimento();
		this.id = pi.getId().toString();
		this.sigla = pi.getSigla();
		this.principalTipo = pi.getTipoDePrincipal().name();
		this.principalSigla = pi.getPrincipal();
		this.atendente = PessoaAPIO.of(pi.getEventoPessoa());
		this.lotaAtendente = LotacaoAPIO.of(pi.getEventoLotacao());
		this.prioridadeId = pi.getPrioridade().name();
		this.prioridadeNome = pi.getPrioridade().getDescr();
		this.tarefaTitulo = pi.getCurrentTaskDefinition().getTitle();
		this.tarefaDataDeInicio = pi.getDtEvent();
		this.tarefaTempoTranscorrido = SigaLibsEL.esperaSimples(pi.getDtEvent());
		WfDefinicaoDeProcedimento pd = pi.getDefinicaoDeProcedimento();
		this.definicaoDeProcedimentoId = pd.getId().toString();
		this.definicaoDeProcedimentoSigla = pd.getSigla();
		this.definicaoDeProcedimentoNome = pd.getNome();
	}
}
