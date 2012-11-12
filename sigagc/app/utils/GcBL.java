package utils;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import models.CpClassificacao;
import models.GcInformacao;
import models.GcMarca;
import models.GcMovimentacao;
import models.GcTipoMovimentacao;
import play.db.jpa.GenericModel;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class GcBL {
	private static final long TEMPO_NOVIDADE = 7 * 24 * 60 * 60 * 1000L;

	private static GcDao dao() {
		return GcDao.getInstance();
	}

	public static GcInformacao movimentar(GcInformacao inf, long idTipo,
			DpPessoa pessoa, DpLotacao lotacao, String titulo, String conteudo,
			CpClassificacao classificacao, GcMovimentacao movRef, Date hisDtIni)
			throws Exception {
		GcMovimentacao mov = new GcMovimentacao();
		mov.tipo = GcTipoMovimentacao.findById(idTipo);
		if (mov.tipo == null)
			throw new Exception(
					"Não foi possível localizar um tipo de movimentacão com id="
							+ idTipo);
		mov.pessoa = pessoa;
		mov.lotacao = lotacao;
		mov.titulo = titulo;
		mov.conteudo = conteudo;
		mov.classificacao = classificacao;
		mov.movRef = movRef;
		mov.hisDtIni = hisDtIni;

		return movimentar(inf, mov);
	}

	public static GcInformacao movimentar(GcInformacao inf, GcMovimentacao mov)
			throws Exception {
		Date dt = dao().dt();
		if (mov.hisDtIni == null) {
			mov.hisDtIni = dt;
		}
		mov.inf = inf;
		if (mov.isCanceladora()) {
			for (GcMovimentacao mv : inf.movs) {
				if (mv.id == mov.movRef.id)
					mv.movCanceladora = mov;
			}
		}

		if (inf.movs == null)
			inf.movs = new TreeSet<GcMovimentacao>();
		inf.movs.add(mov);
		return inf;
	}

	public static GcInformacao gravar(GcInformacao inf, CpIdentidade idc)
			throws Exception {
		Date dt = dao().dt();
		// dao().iniciarTransacao();
		// try {
		if (inf.movs != null) {
			for (GcMovimentacao mov : inf.movs) {
				if (mov.hisIdcIni == null)
					mov.hisIdcIni = idc;
				mov.save();
			}
		}
		if (inf.hisDtIni == null) {
			inf.hisDtIni = dt;
		}
		if (inf.hisIdcIni == null)
			inf.hisIdcIni = idc;
		inf.save();
		atualizarMarcas(inf);
		// dao().commitTransacao();
		// } catch (Exception e) {
		// dao().rollbackTransacao();
		// }
		return inf;
	}

	public static void atualizarMarcas(GcInformacao inf) {
		SortedSet<GcMarca> setA = inf.marcas;
		if (setA == null)
			setA = new TreeSet<GcMarca>();
		SortedSet<GcMarca> setB = calcularMarcadores(inf);
		Set<GcMarca> incluir = new TreeSet<GcMarca>();
		Set<GcMarca> excluir = new TreeSet<GcMarca>();
		encaixar(setA, setB, incluir, excluir);
		for (GcMarca i : incluir) {
			if (i.inf.marcas == null) {
				i.inf.marcas = new TreeSet<GcMarca>();
			}
			i.inf.marcas.add(i);
			// i.salvar();
			i.save();
		}
		for (GcMarca e : excluir) {
			if (e.inf.marcas == null) {
				e.inf.marcas = new TreeSet<GcMarca>();
			}
			e.inf.marcas.remove(e);
			dao().excluir(e);
		}
	}

	/**
	 * Executa algoritmo de comparação entre dois sets e preenche as listas:
	 * inserir, excluir e atualizar.
	 */
	private static void encaixar(SortedSet<GcMarca> setA,
			SortedSet<GcMarca> setB, Set<GcMarca> incluir, Set<GcMarca> excluir) {
		Iterator<GcMarca> ia = setA.iterator();
		Iterator<GcMarca> ib = setB.iterator();

		GcMarca a = null;
		GcMarca b = null;

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

				if (a == null) {
					int i = 0;
				}
				// O registro existe nos dois
				// atualizar.add(new Par(b, a));
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

	private static void acrescentarMarca(SortedSet<GcMarca> set,
			GcInformacao inf, Long idMarcador, Date dtIni, Date dtFim,
			DpPessoa pess, DpLotacao lota) {
		GcMarca mar = new GcMarca();
		mar.inf = inf;
		mar.setCpMarcador(dao().consultar(idMarcador, CpMarcador.class, false));
		if (pess != null)
			mar.setDpPessoaIni(pess.getPessoaInicial());
		if (lota != null)
			mar.setDpLotacaoIni(lota.getLotacaoInicial());
		mar.setDtIniMarca(dtIni);
		mar.setDtFimMarca(dtFim);
		set.add(mar);
	}

	/**
	 * Calcula quais as marcas cada informação terá com base nas movimentações
	 * que foram feitas na informacao.
	 * 
	 * @param inf
	 */
	private static SortedSet<GcMarca> calcularMarcadores(GcInformacao inf) {
		SortedSet<GcMarca> set = new TreeSet<GcMarca>();

		if (inf.movs != null) {
			for (GcMovimentacao mov : inf.movs) {
				Long t = mov.tipo.id;

				if (mov.isCancelada())
					continue;

				if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO)
					inf.hisDtIni = mov.hisDtIni;

				if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO)
					inf.elaboracaoFim = mov.hisDtIni;

				if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO)
					inf.hisDtFim = mov.hisDtIni;

				if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_REVISAO)
					acrescentarMarca(set, inf, CpMarcador.MARCADOR_REVISAR,
							mov.hisDtIni, null, mov.pessoa, mov.lotacao);

				if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_CIENCIA)
					acrescentarMarca(set, inf,
							CpMarcador.MARCADOR_TOMAR_CIENCIA, mov.hisDtIni,
							null, mov.pessoa, mov.lotacao);
			}
		}

		if (inf.hisDtFim != null) {
			acrescentarMarca(set, inf, CpMarcador.MARCADOR_CANCELADO,
					inf.hisDtFim, null, inf.autor, inf.lotacao);
		} else if (inf.elaboracaoFim == null) {
			acrescentarMarca(set, inf, CpMarcador.MARCADOR_EM_ELABORACAO,
					inf.hisDtIni, null, inf.autor, inf.lotacao);
		} else {
			acrescentarMarca(set, inf, CpMarcador.MARCADOR_ATIVO, inf.hisDtIni,
					null, inf.autor, inf.lotacao);
			acrescentarMarca(set, inf, CpMarcador.MARCADOR_NOVO, inf.hisDtIni,
					new Date(inf.hisDtIni.getTime() + TEMPO_NOVIDADE),
					inf.autor, inf.lotacao);
		}
		return set;
	}

}
