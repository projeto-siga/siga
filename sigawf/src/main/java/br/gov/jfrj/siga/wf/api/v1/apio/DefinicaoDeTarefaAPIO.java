package br.gov.jfrj.siga.wf.api.v1.apio;

import br.gov.jfrj.siga.wf.api.v1.IWfApiV1.DefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeDesvio;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeVariavel;

public class DefinicaoDeTarefaAPIO extends DefinicaoDeTarefa {
	public static DefinicaoDeTarefaAPIO of(WfDefinicaoDeTarefa td) {
		if (td == null)
			return null;
		DefinicaoDeTarefaAPIO o = new DefinicaoDeTarefaAPIO();
		o.definicaoDeTarefaId = LongAPIO.of(td.getId());
		o.definicaoDeTarefaOrdem = LongAPIO.of((long) td.getOrdem());
		o.definicaoDeTarefaTipo = EnumAPIO.of(td.getTipoDeTarefa());
		o.definicaoDeTarefaNome = td.getNome();
		o.definicaoDeTarefaAssunto = td.getAssunto();
		o.definicaoDeTarefaConteudo = td.getConteudo();
		o.definicaoDeTarefaSeguinteId = td.getSeguinteIde();
		o.definicaoDeTarefaUltimo = td.isUltimo();
		o.tipoDeResponsavel = EnumAPIO.of(td.getTipoDeResponsavel());
		o.definicaoDeResponsavelId = LongAPIO.of(td.getDefinicaoDeResponsavelId());
		o.responsavel = PessoaAPIO.of(td.getPessoa());
		o.lotaResponsavel = LotacaoAPIO.of(td.getLotacao());
		o.refId = LongAPIO.of(td.getRefId());
		o.refSigla = td.getRefSigla();
		o.refDescr = td.getRefDescr();

		if (td.getDefinicaoDeVariavel() != null && td.getDefinicaoDeVariavel().size() > 0)
			for (WfDefinicaoDeVariavel dv : td.getDefinicaoDeVariavel())
				o.definicoesDeVariaveis.add(DefinicaoDeVariavelAPIO.of(dv));

		if (td.getDefinicaoDeDesvio() != null && td.getDefinicaoDeDesvio().size() > 0)
			for (WfDefinicaoDeDesvio dd : td.getDefinicaoDeDesvio())
				o.definicoesDeDesvios.add(DefinicaoDeDesvioAPIO.of(dd));
		return o;
	}

}
