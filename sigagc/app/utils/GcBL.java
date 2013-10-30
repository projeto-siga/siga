package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Query;

import models.GcArquivo;
import models.GcInformacao;
import models.GcMarca;
import models.GcMovimentacao;
import models.GcTag;
import models.GcTipoMovimentacao;
import models.GcTipoTag;
import play.db.jpa.JPA;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpTipoMarca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class GcBL {
	private static final long TEMPO_NOVIDADE = 7 * 24 * 60 * 60 * 1000L;

	private static String simplificarString(String s) {
		if (s == null)
			return null;
		if (s.trim().length() == 0)
			return null;
		return s.trim();
	}

	public static GcMovimentacao movimentar(GcInformacao inf, long idTipo,
			DpPessoa pessoa, DpLotacao lotacao, String titulo, String conteudo,
			String classificacao, GcMovimentacao movRef, Date hisDtIni,
			byte[] anexo, String mimeType) throws Exception {

		GcMovimentacao mov = new GcMovimentacao();
		mov.tipo = GcTipoMovimentacao.findById(idTipo);
		if (mov.tipo == null)
			throw new Exception(
					"Não foi possível localizar um tipo de movimentacão com id="
							+ idTipo);
		mov.pessoa = pessoa;
		mov.lotacao = lotacao;
		mov.movRef = movRef;
		mov.hisDtIni = hisDtIni;

		titulo = simplificarString(titulo);
		conteudo = simplificarString(conteudo);
		classificacao = simplificarString(classificacao);
		if (idTipo == GcTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO) {
			GcArquivo arq = new GcArquivo();
			arq.titulo = titulo;
			arq.classificacao = null;
			arq.setConteudoBinario(anexo, mimeType);
			mov.arq = arq;
		} else {
			if (titulo != null || conteudo != null || classificacao != null) {
				GcArquivo arq = new GcArquivo();
				arq.titulo = titulo;
				arq.setConteudoTXT(conteudo);
				arq.classificacao = classificacao;
				mov.arq = arq;
			} else {
				if (idTipo == GcTipoMovimentacao.TIPO_MOVIMENTACAO_EDICAO)
					throw new Exception(
							"Não é permitido salvar uma informação com título, conteúdo e classificação vazios.");
			}
		}

		return movimentar(inf, mov);
	}

	public static GcMovimentacao movimentar(GcInformacao inf, GcMovimentacao mov)
			throws Exception {
		Date dt = dt();
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
		return mov;
	}

	public static Date dt() {
		Date dt;
		// try {
		// dt = dao().dt();
		// return dt;
		// } catch (AplicacaoException e) {
		// e.printStackTrace();
		// }
		return new Date();
	}

	public static GcInformacao gravar(GcInformacao inf, CpIdentidade idc)
			throws Exception {
		Date dt = dt();
		// dao().iniciarTransacao();
		// try {

		// Atualiza o campo arq, pois este não pode ser nulo
		if (inf.movs != null) {
			for (GcMovimentacao mov : inf.movs) {
				if (inf.arq == null)
					inf.arq = mov.arq;
			}
		}
		if (inf.hisDtIni == null)
			inf.hisDtIni = dt;
		if (inf.hisIdcIni == null)
			inf.hisIdcIni = idc;
		if (inf.movs != null) {
			for (GcMovimentacao mov : inf.movs) {
				if (mov.arq != null && mov.arq.id == 0) {
					mov.arq.save();
				}
				if (mov.hisIdcIni == null)
					mov.hisIdcIni = idc;
				if (mov.hisDtIni == null)
					mov.hisDtIni = dt;
				if (inf.id == 0)
					inf.save();
				mov.inf = inf;
				mov.save();
			}
		}
		atualizarInformacaoPorMovimentacoes(inf);
		atualizarTags(inf);
		inf.save();
		atualizarMarcas(inf);
		// dao().commitTransacao();
		// } catch (Exception e) {
		// dao().rollbackTransacao();
		// }
		return inf;
	}

	private static void atualizarTags(GcInformacao inf) {
		inf.tags = new TreeSet<GcTag>();
		if (inf.arq.classificacao != null) {
			String a[] = inf.arq.classificacao.split(",");
			inf.tags = buscarTags(a, false);
		}
	}

	public static Set<GcTag> buscarTags(String[] a, boolean fValidos) {
		Set<GcTag> set = new TreeSet<GcTag>();
		for (String s : a) {
			String categoria = null;
			String titulo = null;
			Long tipo = null;
			s = simplificarString(s);
			if (s != null) {
				if (s.startsWith("@") || s.startsWith("^")) {
					if (s.startsWith("@"))
						tipo = GcTipoTag.TIPO_TAG_CLASSIFICACAO;
					else
						tipo = GcTipoTag.TIPO_TAG_ANCORA;
					s = s.substring(1);
					String aa[] = s.split(":");
					if (aa.length > 2) {
						continue;
					} else if (aa.length == 2) {
						categoria = simplificarString(aa[0]);
						titulo = simplificarString(aa[1]);
					} else {
						titulo = simplificarString(aa[0]);
					}
				} else if (s.startsWith("#")) {
					s = s.substring(1);
					titulo = simplificarString(s);
					tipo = GcTipoTag.TIPO_TAG_HASHTAG;
				}
			}
			GcTag tag;
			if (categoria == null)
				tag = GcTag.find(
						"tipo.id = ? and categoria is null and titulo = ?",
						tipo, titulo).first();
			else
				tag = GcTag.find(
						"tipo.id = ? and categoria = ? and titulo = ?", tipo,
						categoria, titulo).first();
			if (tag == null && !fValidos) {
				tag = new GcTag((GcTipoTag) GcTipoTag.findById(tipo),
						categoria, titulo);
			}
			if (tag != null)
				set.add(tag);
		}
		if (set.size() == 0)
			return null;
		return set;
	}

	public static void atualizarInformacaoPorMovimentacoes(GcInformacao inf)
			throws AplicacaoException {
		if (inf.movs == null)
			return;

		ArrayList<GcMovimentacao> movs = new ArrayList<GcMovimentacao>(
				inf.movs.size());
		movs.addAll(inf.movs);
		Collections.reverse(movs);

		for (GcMovimentacao mov : movs) {
			Long t = mov.tipo.id;

			if (mov.isCancelada())
				continue;

			if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO)
				inf.hisDtIni = mov.hisDtIni;

			if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO)
				inf.elaboracaoFim = mov.hisDtIni;

			if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO)
				inf.hisDtFim = mov.hisDtIni;

			if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_EDICAO) {
				if (inf.autor == null)
					inf.autor = mov.pessoa;
				if (inf.lotacao == null)
					inf.lotacao = mov.lotacao;
				inf.arq = mov.arq;
			}
		}
		if (inf.elaboracaoFim != null && inf.ano == null) {
			inf.ano = dt().getYear() + 1900;
			Query qry = JPA
					.em()
					.createQuery(
							"select max(inf.numero) from GcInformacao inf where ano = :ano and ou.idOrgaoUsu = :ouid");
			qry.setParameter("ano", inf.ano);
			qry.setParameter("ouid", inf.ou.getIdOrgaoUsu());
			Integer i = (Integer) qry.getSingleResult();
			inf.numero = (i == null ? 0 : i) + 1;
		}
	}

	public static void atualizarMarcas(GcInformacao inf) {
		SortedSet<GcMarca> setA = new TreeSet<GcMarca>();
		if (inf.marcas != null) {
			// Excluir marcas duplicadas
			for (GcMarca m : inf.marcas) {
				if (setA.contains(m))
					m.delete();
				else
					setA.add(m);
			}
		}
		SortedSet<GcMarca> setB = calcularMarcadores(inf);
		Set<GcMarca> incluir = new TreeSet<GcMarca>();
		Set<GcMarca> excluir = new TreeSet<GcMarca>();
		encaixar(setA, setB, incluir, excluir);
		for (GcMarca i : incluir) {
			if (i.inf.marcas == null) {
				// i.inf.marcas = new TreeSet<GcMarca>();
				i.inf.marcas = new ArrayList<GcMarca>();
			}
			// i.salvar();
			i.inf = inf;
			// dao().salvar(i);
			i.save();
			i.inf.marcas.add(i);
		}
		for (GcMarca e : excluir) {
			if (e.inf.marcas == null) {
				// e.inf.marcas = new TreeSet<GcMarca>();
				e.inf.marcas = new ArrayList<GcMarca>();
			}
			e.inf.marcas.remove(e);
			e.delete();
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
		CpTipoMarca tipoMarca = CpTipoMarca.findById(CpTipoMarca.TIPO_MARCA_SIGA_GC);
		GcMarca mar = new GcMarca();
		mar.setCpTipoMarca(tipoMarca);
		mar.inf = inf;
		mar.setCpMarcador((CpMarcador) CpMarcador.findById(idMarcador));
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

				if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_REVISAO)
					acrescentarMarca(set, inf, CpMarcador.MARCADOR_REVISAR,
							mov.hisDtIni, null, mov.pessoa, mov.lotacao);

				if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICAR)
					acrescentarMarca(set, inf,
							CpMarcador.MARCADOR_TOMAR_CIENCIA, mov.hisDtIni,
							null, mov.pessoa, mov.lotacao);

				if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_INTERESSADO)
					acrescentarMarca(set, inf,
							CpMarcador.MARCADOR_COMO_INTERESSADO, mov.hisDtIni,
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

	private static boolean dtMesmoDia(Date dt1, Date dt2) {
		return dt1.getDate() == dt2.getDate()
				&& dt1.getMonth() == dt2.getMonth()
				&& dt1.getYear() == dt2.getYear();
	}

	public static void logarVisita(GcInformacao informacao, CpIdentidade idc)
			throws Exception {
		Date dt = dt();
		for (GcMovimentacao mov : informacao.movs) {
			if (mov.isCancelada())
				continue;
			if (mov.tipo.id == GcTipoMovimentacao.TIPO_MOVIMENTACAO_VISITA
					&& idc.getDpPessoa().equivale(mov.pessoa)
					&& dtMesmoDia(dt, mov.hisDtIni))
				return;
		}
		GcMovimentacao m = GcBL.movimentar(informacao,
				GcTipoMovimentacao.TIPO_MOVIMENTACAO_VISITA, idc.getDpPessoa()
						.getPessoaAtual(), null, null, null, null, null, null,
				null, null);
		GcBL.gravar(informacao, idc);
	}

	public static void notificado(GcInformacao informacao, CpIdentidade idc)
			throws Exception {
		for (GcMovimentacao mov : informacao.movs) {
			if (mov.isCancelada())
				continue;
			if (mov.tipo.id == GcTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICAR
					&& idc.getDpPessoa().equivale(mov.pessoa)) {
				GcMovimentacao m = GcBL.movimentar(informacao,
						GcTipoMovimentacao.TIPO_MOVIMENTACAO_CIENTE,
						mov.pessoa, null, null, null, null, mov, null, null,
						null);
				mov.movCanceladora = m;
				GcBL.gravar(informacao, idc);
				return;
			}
		}
	}

	public static void interessado(GcInformacao informacao, CpIdentidade idc,
			DpPessoa titular, boolean fInteresse) throws Exception {
		GcMovimentacao movLocalizada = null;
		for (GcMovimentacao mov : informacao.movs) {
			if (mov.isCancelada())
				continue;
			if (mov.tipo.id == GcTipoMovimentacao.TIPO_MOVIMENTACAO_INTERESSADO
					&& titular.equivale(mov.pessoa)) {
				movLocalizada = mov;
				break;
			}
		}
		if (movLocalizada == null && fInteresse) {
			GcMovimentacao m = GcBL.movimentar(informacao,
					GcTipoMovimentacao.TIPO_MOVIMENTACAO_INTERESSADO, titular,
					null, null, null, null, null, null, null, null);
			GcBL.gravar(informacao, idc);
		} else if (movLocalizada != null && !fInteresse) {
			GcMovimentacao m = GcBL
					.movimentar(
							informacao,
							GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO,
							movLocalizada.pessoa, null, null, null, null,
							movLocalizada, null, null, null);
			movLocalizada.movCanceladora = m;
			GcBL.gravar(informacao, idc);
		}
	}

	public static void cancelar(GcInformacao informacao, CpIdentidade idc,
			DpPessoa titular, DpLotacao lotaTitular) throws Exception {
		Date dt = dt();
		for (GcMovimentacao mov : informacao.movs) {
			if (mov.isCancelada())
				continue;
			if (mov.tipo.id == GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO)
				return;
		}
		GcMovimentacao m = GcBL.movimentar(informacao,
				GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO, titular,
				lotaTitular, null, null, null, null, null, null, null);
		GcBL.gravar(informacao, idc);
	}

	public static int compareStrings(String s1, String s2) {
		if (s1 == null) {
			if (s2 != null)
				return -1;
			else
				return 0;
		}
		return s1.compareTo(s2);
	}

	private final static String NON_THIN = "[^iIl1\\.,']";

	private static int textWidth(String str) {
		return (int) (str.length() - str.replaceAll(NON_THIN, "").length() / 2);
	}

	public static String ellipsize(String text, int max) {

		if (textWidth(text) <= max)
			return text;

		// Start by chopping off at the word before max
		// This is an over-approximation due to thin-characters...
		int end = text.lastIndexOf(' ', max - 3);

		// Just one long word. Chop it off.
		if (end == -1)
			return text.substring(0, max - 3) + "...";

		// Step forward as long as textWidth allows.
		int newEnd = end;
		do {
			end = newEnd;
			newEnd = text.indexOf(' ', end + 1);

			// No more spaces.
			if (newEnd == -1)
				newEnd = text.length();

		} while (textWidth(text.substring(0, newEnd) + "...") < max);

		return text.substring(0, end) + "...";
	}

}