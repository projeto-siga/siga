package br.gov.jfrj.siga.wf.bl;

import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import br.gov.jfrj.siga.base.Par;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.WfMarca;
import br.gov.jfrj.siga.wf.model.WfProcedimento;

public class WfMarcaBL {

	public static void atualizarMarcas(WfProcedimento pi) {
		SortedSet<WfMarca> setA = new TreeSet<WfMarca>();
		setA.addAll(pi.getMarcas());
		SortedSet<WfMarca> setB = calcularMarcadores(pi);
		Set<WfMarca> marcasAIncluir = new TreeSet<WfMarca>();
		Set<WfMarca> marcasAExcluir = new TreeSet<WfMarca>();
		Set<Par<WfMarca, WfMarca>> atualizar = new TreeSet<Par<WfMarca, WfMarca>>();
		encaixar(setA, setB, marcasAIncluir, marcasAExcluir, atualizar);

		for (WfMarca i : marcasAIncluir) {
			i.save();
			pi.getMarcas().add(i);
		}
		for (WfMarca e : marcasAExcluir) {
			pi.getMarcas().remove(e);
			e.delete();
		}
		for (Entry<WfMarca, WfMarca> pair : atualizar) {
			WfMarca a = pair.getKey();
			WfMarca b = pair.getValue();
			a.setDpLotacaoIni(b.getDpLotacaoIni());
			a.setDpPessoaIni(b.getDpPessoaIni());
			a.setDtFimMarca(b.getDtFimMarca());
			a.setDtIniMarca(b.getDtIniMarca());
			a.setProcedimento(b.getProcedimento());
			a.save();
		}
	}

	private static SortedSet<WfMarca> calcularMarcadores(WfProcedimento pi) {
		SortedSet<WfMarca> set = new TreeSet<WfMarca>();

		if (pi.isPausado())
			acrescentarMarca(pi, set, CpMarcadorEnum.EM_ANDAMENTO.getId(), pi.getEventoData(), null, pi.getEventoPessoa(),
					pi.getEventoLotacao());

		return set;
	}

	private static void encaixar(SortedSet<WfMarca> setA, SortedSet<WfMarca> setB, Set<WfMarca> incluir,
			Set<WfMarca> excluir, Set<Par<WfMarca, WfMarca>> atualizar) {
		Iterator<WfMarca> ia = setA.iterator();
		Iterator<WfMarca> ib = setB.iterator();

		WfMarca a = null;
		WfMarca b = null;

		if (ia.hasNext())
			a = ia.next();
		if (ib.hasNext())
			b = ib.next();
		while (a != null || b != null) {
			if ((a == null) || (b != null && a.compareTo(b) > 0)) {
				// Existe em setB, mas nao existe em setA
				incluir.add(b);
				if (ib.hasNext())
					b = ib.next();
				else
					b = null;

			} else if (b == null || (a != null && b.compareTo(a) > 0)) {
				// Existe em setA, mas nao existe em setB
				excluir.add(a);
				if (ia.hasNext())
					a = ia.next();
				else
					a = null;
			} else {

				// O registro existe nos dois
				atualizar.add(new Par<WfMarca, WfMarca>(a, b));
				if (ib.hasNext())
					b = ib.next();
				else
					b = null;
				if (ia.hasNext())
					a = ia.next();
				else
					a = null;
			}
		}
		ib = null;
		ia = null;
	}

	private static void acrescentarMarca(WfProcedimento pi, SortedSet<WfMarca> set, Long idMarcador, Date dtIni,
			Date dtFim, DpPessoa pess, DpLotacao lota) {
		WfMarca mar = new WfMarca();
		// Edson: nao eh necessario ser this.solicitacaoInicial em vez de this
		// porque este metodo soh eh chamado por atualizarMarcas(), que ja se
		// certifica de chamar este metodo apenas para a solicitacao inicial
		mar.setProcedimento(pi);
		mar.setCpMarcador((CpMarcador) CpMarcador.AR.findById(idMarcador));
		if (pess != null)
			mar.setDpPessoaIni(pess.getPessoaInicial());
		if (lota != null)
			mar.setDpLotacaoIni(lota.getLotacaoInicial());
		mar.setDtIniMarca(dtIni);
		mar.setDtFimMarca(dtFim);
		set.add(mar);
	}
}
