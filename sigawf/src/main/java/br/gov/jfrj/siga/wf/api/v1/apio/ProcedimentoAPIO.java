package br.gov.jfrj.siga.wf.api.v1.apio;

import java.util.List;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.jee.SigaLibsEL;
import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.Acao;
import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.Evento;
import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.Procedimento;
import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.Variavel;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfMov;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.model.WfVariavel;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeVariavel;
import br.gov.jfrj.siga.wf.util.WfTarefa;
import br.gov.jfrj.siga.wf.util.WfUtil;

public class ProcedimentoAPIO extends Procedimento {
	public ProcedimentoAPIO(WfProcedimento pi) {
		this(pi, false, null, null, null);
	}

	public ProcedimentoAPIO(WfProcedimento pi, boolean inflar, DpPessoa titular, DpLotacao lotaTitular, WfUtil util) {
		this.id = LongAPIO.of(pi.getId());
		this.sigla = pi.getSigla();
		this.principalTipo = pi.getTipoDePrincipal().name();
		this.principalSigla = pi.getPrincipal();
		this.atendente = PessoaAPIO.of(pi.getEventoPessoa());
		this.lotaAtendente = LotacaoAPIO.of(pi.getEventoLotacao());
		this.prioridadeId = pi.getPrioridade().name();
		this.prioridadeNome = pi.getPrioridade().getDescr();
		if (pi.getCurrentTaskDefinition() != null)
			this.tarefaTitulo = pi.getCurrentTaskDefinition().getTitle();
		this.tarefaDataDeInicio = pi.getDtEvent();
		if (pi.getDtEvent() != null)
			this.tarefaTempoRelativo = SigaLibsEL.esperaSimples(pi.getDtEvent());
		WfDefinicaoDeProcedimento pd = pi.getDefinicaoDeProcedimento();
		this.definicaoDeProcedimentoId = LongAPIO.of(pd.getId());
		this.definicaoDeProcedimentoSigla = pd.getSigla();
		this.definicaoDeProcedimentoNome = pd.getNome();
		this.formulario = pi.isFormulario();

		if (!inflar)
			return;

		try {
			this.vizProcedimento = util.getDot(new WfTarefa(pi));
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.desabilitarFormulario = pi.isDesabilitarFormulario(titular, lotaTitular);

		try {
			this.msgAviso = pi.getMsgAviso(titular, lotaTitular);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<AcaoVO> a = pi.getAcoes(titular, lotaTitular);
		for (AcaoVO avo : a) {
			Acao acao = new Acao();
			acao.nome = avo.getNome();
			acao.icone = avo.getIcone();
			acao.nameSpace = avo.getNameSpace();
			acao.acao = avo.getAcao();
			acao.pode = avo.isPode();
			acao.explicacao = avo.getExplicacao();
			acao.msgConfirmacao = avo.getMsgConfirmacao();
			// acao.params = avo.getParams();
			acao.pre = avo.getPre();
			acao.pos = avo.getPos();
			acao.classe = avo.getClasse();
			acao.modal = avo.getModal();
			acao.post = avo.isPost();
			acoes.add(acao);
		}

		if (pi.getMovimentacoes() != null) {
			for (WfMov mov : pi.getMovimentacoes()) {
				Evento evento = new Evento();
				evento.eventoId = LongAPIO.of(mov.getId());
				evento.eventoHora = mov.getHisDtIni();
				evento.eventoTempoRelativo = mov.getTempoRelativoVO();
				evento.eventoTitulo = mov.getEvento();
				evento.eventoDescr = mov.getDescricaoEvento();
				evento.responsavel = PessoaAPIO.of(mov.getTitular());
				evento.lotaResponsavel = LotacaoAPIO.of(mov.getLotaTitular());
				eventos.add(evento);
			}
		}

		if (pi.getVariaveis() != null) {
			for (WfVariavel v : pi.getVariaveis()) {
				Variavel variavel = new Variavel();
				variavel.variavelIdentificador = v.getNome();
				if (v.getString() != null) {
					variavel.variavelTipo = WfTipoDeVariavel.STRING.name();
					variavel.variavelValorString = v.getString();
				} else if (v.getDate() != null) {
					variavel.variavelTipo = WfTipoDeVariavel.DATE.name();
					variavel.variavelValorDate = v.getDate();
				} else if (v.getBool() != null) {
					variavel.variavelTipo = WfTipoDeVariavel.BOOLEAN.name();
					variavel.variavelValorBoolean = v.getBool();
				} else if (v.getNumber() != null) {
					variavel.variavelTipo = WfTipoDeVariavel.DATE.name();
					variavel.variavelValorNumber = v.getNumber();
				}
				variaveis.add(variavel);
			}
		}

		if (pi.getDefinicaoDeTarefaCorrente() != null) {
			definicaoDeTarefaCorrente = DefinicaoDeTarefaAPIO.of(pi.getDefinicaoDeTarefaCorrente());
		}
	}
}
