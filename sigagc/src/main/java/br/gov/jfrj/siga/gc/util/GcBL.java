package br.gov.jfrj.siga.gc.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Par;
import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.CpGrupo;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoMarca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.gc.model.GcAcesso;
import br.gov.jfrj.siga.gc.model.GcArquivo;
import br.gov.jfrj.siga.gc.model.GcInformacao;
import br.gov.jfrj.siga.gc.model.GcMarca;
import br.gov.jfrj.siga.gc.model.GcMovimentacao;
import br.gov.jfrj.siga.gc.model.GcPapel;
import br.gov.jfrj.siga.gc.model.GcTag;
import br.gov.jfrj.siga.gc.model.GcTipoMovimentacao;
import br.gov.jfrj.siga.gc.model.GcTipoTag;
import br.gov.jfrj.siga.gc.vraptor.Correio;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@RequestScoped
public class GcBL {
	private static final long TEMPO_NOVIDADE = 7 * 24 * 60 * 60 * 1000L;

	private EntityManager em;
	private SigaObjects so;
	private Date dt;

	/**
	 * @deprecated CDI eyes only
	 */
	public GcBL() {
		super();
		this.so = null;

	}

	@Inject
	public GcBL(SigaObjects so) {
		super();
		this.so = so;
	}

	private String simplificarString(String s) {
		if (s == null)
			return null;
		if (s.trim().length() == 0)
			return null;
		return s.trim();
	}

	private String simplificarHashtag(String s) {
		if (s == null)
			return null;
		if (s.trim().length() == 0)
			return null;
		return s.trim().toLowerCase();
	}

	public GcMovimentacao movimentar(GcInformacao inf, GcArquivo arqDuplicado, long id) throws Exception {
		GcMovimentacao mov = new GcMovimentacao();
		mov.setTipo(GcTipoMovimentacao.AR.findById(id));
		if (mov.getTipo() == null)
			throw new Exception("Não foi possível localizar um tipo de movimentacão com id=" + id);
		mov.setArq(arqDuplicado);
		return movimentar(inf, mov);
	}

	public GcMovimentacao movimentar(GcInformacao inf, long idTipo, DpPessoa pessoa, DpLotacao lotacao,
			String descricao, String titulo, String conteudo, String classificacao, GcMovimentacao movRef,
			Date hisDtIni, byte[] anexo) throws Exception {

		GcMovimentacao mov = new GcMovimentacao();
		mov.setTipo(GcTipoMovimentacao.AR.findById(idTipo));
		if (mov.getTipo() == null)
			throw new Exception("Não foi possível localizar um tipo de movimentacão com id=" + idTipo);
		mov.setPessoaAtendente(pessoa);
		mov.setLotacaoAtendente(lotacao);
		mov.setDescricao(descricao);
		mov.setMovRef(movRef);
		mov.setHisDtIni(hisDtIni);

		titulo = simplificarString(titulo);
		conteudo = simplificarString(conteudo);

		if (conteudo != null && conteudo.startsWith("<")) {
			String canonicalizado = new ProcessadorHtml().canonicalizarHtml(conteudo, false, true, true, true, true);
			conteudo = canonicalizado;
		}

		classificacao = simplificarString(classificacao);
		if (idTipo == GcTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO) {
			GcArquivo arq = new GcArquivo();
			arq.setTitulo(titulo);
			arq.setClassificacao(null);
			arq.setConteudoBinario(anexo, arq.obterMimeType());
			mov.setArq(arq);
		} else {
			if (titulo != null && conteudo != null) {
				GcArquivo arq = new GcArquivo();
				arq.setTitulo(titulo);
				arq.setConteudoTXT(conteudo);
				arq.setClassificacao(classificacao);
				mov.setArq(arq);
			} else if (idTipo == GcTipoMovimentacao.TIPO_MOVIMENTACAO_EDICAO
					|| idTipo == GcTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO) {
				throw new AplicacaoException("Não é permitido salvar uma informação com título ou conteúdo vazios.");
			}
		}

		return movimentar(inf, mov);
	}

	public GcMovimentacao movimentar(GcInformacao inf, GcMovimentacao mov) throws Exception {
		Date dt = dt();
		if (mov.getHisDtIni() == null) {
			mov.setHisDtIni(dt);
		}
		mov.setInf(inf);
		/*
		 * if (mov.isCanceladora()) { for (GcMovimentacao mv : inf.movs) { if (mv.id ==
		 * mov.movRef.id) mv.movCanceladora = mov; } }
		 */

		if (inf.getMovs() == null)
			inf.setMovs(new TreeSet<GcMovimentacao>());
		inf.getMovs().add(mov);
		return mov;
	}

	public void atualizarListaMovimentacoes(GcInformacao inf, GcMovimentacao mov) {
		inf.getMovs().remove(mov);
		inf.getMovs().add(mov);
	}

	public Date dt() {
		if (this.dt == null)
			this.dt = so.dao().dt();
		return this.dt;
	}

	public GcInformacao gravar(GcInformacao inf, CpIdentidade idc, DpPessoa titular, DpLotacao lotaTitular)
			throws Exception {
		Date dt = dt();

		if (inf.getEdicao() != null)
			inf.setEdicao(GcAcesso.AR.findById(inf.getEdicao().getId()));
		if (inf.getVisualizacao() != null)
			inf.setVisualizacao(GcAcesso.AR.findById(inf.getVisualizacao().getId()));

		// Atualiza o campo arq, pois este não pode ser nulo
		if (inf.getMovs() != null) {
			for (GcMovimentacao mov : inf.getMovs()) {
				if (inf.getArq() == null)
					inf.setArq(mov.getArq());
			}
		}

		if (inf.getHisDtIni() == null)
			inf.setHisDtIni(dt);
		if (inf.getHisIdcIni() == null)
			inf.setHisIdcIni(idc);
		if (inf.getMovs() != null) {
			for (GcMovimentacao mov : inf.getMovs()) {
				if (mov.getArq() != null && mov.getArq().getId() == null)
					mov.getArq().save();
				if (mov.getHisIdcIni() == null)
					mov.setHisIdcIni(idc);
				if (mov.getHisDtIni() == null)
					mov.setHisDtIni(dt);
				if (mov.getPessoaTitular() == null)
					mov.setPessoaTitular(titular);
				if (mov.getLotacaoTitular() == null)
					mov.setLotacaoTitular(lotaTitular);
				if (inf.getId() == null)
					inf.save();
				mov.setInf(inf);
				if (mov.getMovCanceladora() != null) {
					if (mov.getMovCanceladora().getHisIdcIni() == null)
						mov.getMovCanceladora().setHisIdcIni(idc);
					mov.getMovCanceladora().save();
				}
				mov.save();
			}
		}
		// atualizarInformacaoPorMovimentacoes(inf);
		atualizarTags(inf);
		inf.save();
		atualizarMarcas(inf);
		return inf;
	}

	private void atualizarTags(GcInformacao inf) throws Exception {
		inf.setTags(new TreeSet<GcTag>());
		if (inf.getArq().getClassificacao() != null) {
			String a[] = inf.getArq().getClassificacao().split(",");
			inf.setTags(buscarTags(a, false));
		}
	}

	public SortedSet<GcTag> buscarTags(String[] a, boolean fValidos) throws Exception {
		SortedSet<GcTag> set = new TreeSet<GcTag>();
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
					titulo = simplificarHashtag(s);
					tipo = GcTipoTag.TIPO_TAG_HASHTAG;
				}
			}
			GcTag tag;
			if (categoria == null)
				tag = GcTag.AR.find("tipo.id = ?1 and categoria is null and titulo = ?2", tipo, titulo).first();
			else
				tag = GcTag.AR.find("tipo.id = ?1 and categoria = ?2 and titulo = ?3", tipo, categoria, titulo).first();
			if (tag == null && !fValidos) {
				tag = new GcTag((GcTipoTag) GcTipoTag.AR.findById(tipo), categoria, titulo);
			}
			if (tag != null) {
				set.add(tag);
			}
		}

		if (set.size() == 0)
			return null;
		return set;
	}

	public void atualizarInformacaoPorMovimentacoes(GcInformacao inf) throws AplicacaoException {

		if (inf.getMovs() == null)
			return;

		ArrayList<GcMovimentacao> movs = new ArrayList<GcMovimentacao>(inf.getMovs().size());

		movs.addAll(inf.getMovs());

		Collections.reverse(movs);

		for (GcMovimentacao mov : movs) {
			Long t = mov.getTipo().getId();

			if (mov.isCancelada())
				continue;

			if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO)
				inf.setHisDtIni(mov.getHisDtIni());

			if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_FECHAMENTO)
				inf.setElaboracaoFim(mov.getHisDtIni());

			if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO)
				inf.setHisDtFim(mov.getHisDtIni());

			if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_EDICAO) {
				if (inf.getAutor() == null)
					// inf.autor = mov.pessoa;
					inf.setAutor(mov.getPessoaTitular());
				if (inf.getLotacao() == null)
					// inf.lotacao = mov.lotacao;
					inf.setLotacao(mov.getLotacaoTitular());
				inf.setArq(mov.getArq());
			}
		}
		if (inf.getElaboracaoFim() != null && inf.getAno() == null) {
			inf.setAno(dt().getYear() + 1900);
			Query qry = em().createQuery(
					"select max(inf.numero) from GcInformacao inf where ano = :ano and ou.idOrgaoUsu = :ouid");
			qry.setParameter("ano", inf.getAno());
			qry.setParameter("ouid", inf.getOu().getIdOrgaoUsu());
			Integer i = (Integer) qry.getSingleResult();
			inf.setNumero((i == null ? 0 : i) + 1);
		}
	}

	public void atualizarMarcas(GcInformacao inf) throws Exception {
		SortedSet<GcMarca> setA = new TreeSet<GcMarca>();
		if (inf.getMarcas() != null) {
			// em().getTransaction().begin();
			// Excluir marcas duplicadas
			// try {
			for (GcMarca m : inf.getMarcas()) {
				if (setA.contains(m))
					m.delete();
				else
					setA.add(m);
			}
			// em().getTransaction().commit();
			//
			// } catch (Exception Ex) {
			// em().getTransaction().rollback();
			// throw Ex;
			// }

		}
		SortedSet<GcMarca> setB = calcularMarcadores(inf);
		Set<GcMarca> incluir = new TreeSet<GcMarca>();
		Set<GcMarca> excluir = new TreeSet<GcMarca>();

		encaixar(setA, setB, incluir, excluir);
		for (GcMarca i : incluir) {
			if (i.getInf().getMarcas() == null) {
				// i.inf.marcas = new TreeSet<GcMarca>();
				i.getInf().setMarcas(new ArrayList<GcMarca>());
			}
			// i.salvar();
			i.setInf(inf);
			// dao().salvar(i);
			i.save();
			i.getInf().getMarcas().add(i);
		}
		// em().getTransaction().begin();
		// try {
		for (GcMarca e : excluir) {
			if (e.getInf().getMarcas() == null) {
				// e.inf.marcas = new TreeSet<GcMarca>();
				e.getInf().setMarcas(new ArrayList<GcMarca>());
			}
			e.getInf().getMarcas().remove(e);
			e.delete();
		}
		// em().getTransaction().commit();

		// } catch (Exception Ex) {
		// em().getTransaction().rollback();
		// throw Ex;
		// }

	}

	/**
	 * Executa algoritmo de comparação entre dois sets e preenche as listas:
	 * inserir, excluir e atualizar.
	 */
	private void encaixar(SortedSet<GcMarca> setA, SortedSet<GcMarca> setB, Set<GcMarca> incluir,
			Set<GcMarca> excluir) {
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

	private void acrescentarMarca(SortedSet<GcMarca> set, GcInformacao inf, Long idMarcador, Date dtIni, Date dtFim,
			DpPessoa pess, DpLotacao lota) throws Exception {
		CpTipoMarca tipoMarca = CpTipoMarca.AR.findById(CpTipoMarca.TIPO_MARCA_SIGA_GC);
		GcMarca mar = new GcMarca();
		mar.setCpTipoMarca(tipoMarca);
		mar.setInf(inf);
		mar.setCpMarcador((CpMarcador) CpMarcador.AR.findById(idMarcador));
		if (pess != null)
			mar.setDpPessoaIni(pess.getPessoaInicial());
		if (lota != null)
			mar.setDpLotacaoIni(lota.getLotacaoInicial());
		mar.setDtIniMarca(dtIni);
		mar.setDtFimMarca(dtFim);
		set.add(mar);
	}

	/**
	 * Calcula quais as marcas cada informação terá com base nas movimentações que
	 * foram feitas na informacao.
	 * 
	 * @param inf
	 */
	private SortedSet<GcMarca> calcularMarcadores(GcInformacao inf) throws Exception {
		SortedSet<GcMarca> set = new TreeSet<GcMarca>();

		if (inf.getHisDtFim() != null) {
			acrescentarMarca(set, inf, CpMarcadorEnum.CANCELADO.getId(), inf.getHisDtFim(), null, inf.getAutor(),
					inf.getLotacao());
		} else {
			if (inf.getElaboracaoFim() == null) {
				acrescentarMarca(set, inf, CpMarcadorEnum.EM_ELABORACAO.getId(), inf.getHisDtIni(), null,
						inf.getAutor(), inf.getLotacao());
			} else {
				acrescentarMarca(set, inf, CpMarcadorEnum.ATIVO.getId(), inf.getElaboracaoFim(), null, inf.getAutor(),
						inf.getLotacao());
				acrescentarMarca(set, inf, CpMarcadorEnum.NOVO.getId(), inf.getElaboracaoFim(),
						new Date(inf.getHisDtIni().getTime() + TEMPO_NOVIDADE), inf.getAutor(), inf.getLotacao());
			}
			if (inf.getMovs() != null) {
				for (GcMovimentacao mov : inf.getMovs()) {
					Long t = mov.getTipo().getId();

					if (mov.isCancelada())
						continue;

					if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_DE_REVISAO)
						acrescentarMarca(set, inf, CpMarcadorEnum.REVISAR.getId(), mov.getHisDtIni(), null,
								mov.getPessoaAtendente(), mov.getLotacaoAtendente());

					if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICAR
							&& (mov.getPessoaAtendente() != null || mov.getLotacaoAtendente() != null)) {

						acrescentarMarca(set, inf, CpMarcadorEnum.TOMAR_CIENCIA.getId(), mov.getHisDtIni(), null,
								mov.getPessoaAtendente(), mov.getLotacaoAtendente());
					}

					List<Par<DpPessoa, DpLotacao>> pessoasELotasDoGrupo = new ArrayList<Par<DpPessoa, DpLotacao>>();
					if (mov.getGrupo() != null) {
						for (CpConfiguracaoCache cfg : Cp.getInstance().getConf()
								.getListaPorTipo(CpTipoDeConfiguracao.PERTENCER)) {
							if (cfg.cpGrupo == mov.getGrupo().getIdInicial() && cfg.hisDtFim == null) {
								DpPessoa pess = null;
								// Edson: evitar LazyException:
								if (cfg.dpPessoa != 0)
									pess = CpDao.getInstance().consultar(cfg.dpPessoa, DpPessoa.class, false);
								DpLotacao lota = null;
								if (cfg.lotacao != 0)
									lota = CpDao.getInstance().consultar(cfg.lotacao, DpLotacao.class, false);
								else if (pess != null)
									lota = pess.getLotacao();
								pessoasELotasDoGrupo.add(new Par<DpPessoa, DpLotacao>(pess, lota));
							}
						}
					}

					if (t == GcTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULAR_PAPEL) {
						Long marcador = 0L;
						switch (mov.getPapel().getId().intValue()) {
						case (int) GcPapel.PAPEL_INTERESSADO:
							marcador = CpMarcadorEnum.COMO_INTERESSADO.getId();
							break;
						case (int) GcPapel.PAPEL_EXECUTOR:
							marcador = CpMarcadorEnum.COMO_EXECUTOR.getId();
							break;
						}
						if (mov.getLotacaoAtendente() != null)
							pessoasELotasDoGrupo.add(
									new Par<DpPessoa, DpLotacao>(mov.getPessoaAtendente(), mov.getLotacaoAtendente()));
						for (Par<DpPessoa, DpLotacao> p : pessoasELotasDoGrupo)
							acrescentarMarca(set, inf, marcador, mov.getHisDtIni(), null, p.getKey(), p.getValue());
					}
				}
			}
		}
		return set;
	}

	private boolean dtMesmoDia(Date dt1, Date dt2) {
		return dt1.getDate() == dt2.getDate() && dt1.getMonth() == dt2.getMonth() && dt1.getYear() == dt2.getYear();
	}

	public void logarVisita(GcInformacao informacao, CpIdentidade idc, DpPessoa titular, DpLotacao lotaTitular)
			throws Exception {
		Date dt = dt();
		for (GcMovimentacao mov : informacao.getMovs()) {
			if (mov.isCancelada())
				continue;
			if (mov.getTipo().getId() == GcTipoMovimentacao.TIPO_MOVIMENTACAO_VISITA
					&& titular.equivale(mov.getPessoaTitular())
					// && idc.getDpPessoa().equivale(mov.pessoa)
					&& dtMesmoDia(dt, mov.getHisDtIni()))
				return;
		}
		GcMovimentacao m = movimentar(informacao, GcTipoMovimentacao.TIPO_MOVIMENTACAO_VISITA, null, null, null, null,
				null, null, null, null, null);
		gravar(informacao, idc, titular, lotaTitular);
	}

	public void notificado(GcInformacao informacao, CpIdentidade idc, DpPessoa titular, DpLotacao lotaTitular,
			GcMovimentacao movNotificacao) throws Exception {
		for (GcMovimentacao movs : informacao.getMovs()) {
			if (movs.isCancelada())
				continue;
			if (movs.getTipo().getId() == movNotificacao.getTipo().getId()) {
				if (titular.equivale(movNotificacao.getPessoaAtendente())) {
					GcMovimentacao m = movimentar(informacao, GcTipoMovimentacao.TIPO_MOVIMENTACAO_CIENTE, null, null,
							null, null, null, null, movNotificacao, null, null);
					movNotificacao.setMovCanceladora(m);
					gravar(informacao, idc, titular, lotaTitular);
				} else if (lotaTitular.equivale(movNotificacao.getLotacaoAtendente())) {
					GcMovimentacao m = movimentar(informacao, GcTipoMovimentacao.TIPO_MOVIMENTACAO_CIENTE, null,
							movNotificacao.getLotacaoAtendente(), null, null, null, null, movNotificacao, null, null);
					movNotificacao.setMovCanceladora(m);
					gravar(informacao, idc, titular, lotaTitular);
					if (m.todaLotacaoCiente(movNotificacao)) {
						movNotificacao.setMovCanceladora(m);
						gravar(informacao, idc, titular, lotaTitular);
					}
				} else {
					// Edson: desenvolver esquema para marcar ciencia de grupo
					// de e-mail,
					// chamando, inclusive, um todoGrupoCiente()
				}
				return;
			}
		}
	}

	public void interessado(GcInformacao informacao, CpIdentidade idc, DpPessoa titular, DpLotacao lotaTitular,
			boolean fInteresse) throws Exception {
		GcMovimentacao movLocalizada = null;
		for (GcMovimentacao mov : informacao.getMovs()) {
			if (!mov.isCancelada() && mov.getTipo().getId() == GcTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULAR_PAPEL
					&& titular.equivale(mov.getPessoaAtendente())
					&& mov.getPapel().getIdPapel() == GcPapel.PAPEL_INTERESSADO) {
				movLocalizada = mov;
				break;
			}
		}
		if (movLocalizada == null && fInteresse) {
			vincularPapel(informacao, idc, titular, lotaTitular, titular, lotaTitular, null,
					GcPapel.AR.findById(GcPapel.PAPEL_INTERESSADO), null);
		} else if (movLocalizada != null && !fInteresse) {
			cancelarMovimentacao(informacao, movLocalizada, idc, titular, lotaTitular);
		}
	}

	public void vincularPapel(GcInformacao informacao, CpIdentidade idc, DpPessoa titular, DpLotacao lotaTitular,
			DpPessoa pessoa, DpLotacao lotacao, CpGrupo grupo, GcPapel papel, Correio correio) throws Exception {
		if (informacao == null || (pessoa == null && lotacao == null && grupo == null))
			throw new AplicacaoException("Não foram informados dados para a definição de perfil");

		if (pessoa != null && lotacao == null && grupo == null)
			lotacao = pessoa.getLotacao();

		if (!(papel.getIdPapel() == GcPapel.PAPEL_INTERESSADO && titular.equivale(pessoa))
				&& !informacao.podeVincularPapel(titular, lotaTitular)) {
			throw new AplicacaoException("Definição de perfil não permitida");
		}

		String descr = papel.getDescPapel() + ": ";
		if (pessoa != null)
			descr += pessoa.getDescricaoCompletaIniciaisMaiusculas();
		else if (lotacao != null)
			descr += lotacao.getDescricaoIniciaisMaiusculas();
		else if (grupo != null)
			descr += grupo.getDscGrupo();

		GcMovimentacao mov = movimentar(informacao, GcTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULAR_PAPEL, pessoa,
				lotacao, descr, null, null, null, null, null, null);
		mov.setPapel(papel);
		mov.setGrupo(grupo);
		gravar(informacao, idc, titular, lotaTitular);
		if (correio != null && (pessoa != null || lotacao != null))
			correio.notificar(informacao, pessoa, lotacao, null);
	}

	public void cancelar(GcInformacao informacao, CpIdentidade idc, DpPessoa titular, DpLotacao lotaTitular)
			throws Exception {
		Date dt = dt();
		for (GcMovimentacao mov : informacao.getMovs()) {
			if (mov.isCancelada())
				continue;
			if (mov.getTipo().getId() == GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO)
				return;
		}
		GcMovimentacao m = movimentar(informacao, GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO, null, null, null,
				null, null, null, null, null, null);
		gravar(informacao, idc, titular, lotaTitular);
	}

	public static int compareStrings(String s1, String s2) {
		if (s1 == null) {
			if (s2 != null)
				return -1;
			else
				return 0;
		} else {
			if (s2 == null)
				return 1;
		}
		return s1.compareTo(s2);
	}

	private final String NON_THIN = "[^iIl1\\.,']";

	private int textWidth(String str) {
		return (int) (str.length() - str.replaceAll(NON_THIN, "").length() / 2);
	}

	public String ellipsize(String text, int max) {

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

	public String atualizarClassificacao(String classificacao, String hashTag) {
		if (classificacao.isEmpty() && hashTag.isEmpty())
			return null;
		else if (classificacao.isEmpty() && !hashTag.isEmpty())
			return hashTag.trim();
		else if (!classificacao.isEmpty() && hashTag.isEmpty())
			return classificacao;
		else
			return classificacao.concat(", ").concat(hashTag).trim();
	}

	public void cancelarMovimentacao(GcInformacao info, GcMovimentacao mov, CpIdentidade idc, DpPessoa titular,
			DpLotacao lotaTitular) throws Exception {
		GcMovimentacao m = movimentar(info, GcTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO, null,
				null, null, null, null, null, mov, null, null);
		// gravar(info, idc, titular, lotaTitular);
		mov.setMovCanceladora(m);
		gravar(info, idc, titular, lotaTitular);
	}

	/**
	 * Metodo que grava arquivos no GcArquivo e atrela esse arquivo a um
	 * conhecimento atraves da movimentacao TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO.
	 * Chamado pela página anexar.html
	 */
	public void gravarArquivoComMovimentacao(GcInformacao info, CpIdentidade idc, DpPessoa titular,
			DpLotacao lotaTitular, String titulo, byte[] file) throws Exception {
		movimentar(info, GcTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO, null, null, null, titulo, null, null,
				null, null, file);
		gravar(info, idc, titular, lotaTitular);
	}

	/**
	 * Metodo que grava imagens no GcArquivo sem associa-las a um conhecimento.
	 * Chamado pela página editar.html
	 */
	public long gravarArquivoSemMovimentacao(byte[] file, String titulo, String contentType) {
		GcArquivo arq = new GcArquivo();
		arq.setTitulo(titulo);
		arq.setClassificacao(null);
		arq.setConteudoBinario(file, contentType);
		if (arq.isImage()) {
			arq.save();
			return arq.getId();
		} else
			return -1; // nao existe arquivo no banco com id negativo,
						// assim esse retorno indica que o arquivo nao foi salvo
						// no banco
	}

	private String acronimoOrgao = null;
	private final int CONTROLE_LINK_HASH_TAG = 2;
	private final String URL_SIGA_DOC = "/sigaex/app/expediente/doc/exibir?sigla=";
	private final String URL_SIGA_SR = "/sigasr/app/solicitacao/exibir/";
	private final String URL_SIGA_GC = "/sigagc/app/exibir?sigla=";

	// public void salvar(Historico o) throws Exception {
	// o.setHisDtIni(new Date());
	// o.setHisDtFim(null);
	// if (o.getId() == null) {
	// ((GenericModel) o).save();
	// o.setHisIdIni(o.getId());
	// } else {
	// JPA.em().detach(o);
	// // Edson: Abaixo, nÃ£o funcionou findById, por algum motivo
	// // relacionado a esse esquema de sobrescrever o ObjetoBase
	// Historico oAntigo = (Historico) JPA.em().find(o.getClass(),
	// o.getId());
	// ((ManipuladorHistorico) oAntigo).finalizar();
	// o.setHisIdIni(oAntigo.getHisIdIni());
	// o.setId(null);
	// }
	// ((GenericModel) o).save();
	// }

	public void finalizar(Historico o) throws Exception {
		o.setHisDtFim(new Date());
		((Objeto) o).save();
	}

	// public GcConfiguracao getConfiguracao(DpPessoa pess,
	// SrItemConfiguracao item, SrServico servico, long idTipo,
	// SrSubTipoConfiguracao subTipo) throws Exception {
	//
	// GcConfiguracao conf = new GcConfiguracao(pess, item, servico, JPA.em()
	// .find(CpTipoConfiguracao.class, idTipo), subTipo);
	//
	// return GcConfiguracaoBL.get().buscarConfiguracao(conf);
	// }
	//
	// public List<GcConfiguracao> getConfiguracoes(DpPessoa pess,
	// SrItemConfiguracao item, SrServico servico, long idTipo,
	// SrSubTipoConfiguracao subTipo) throws Exception {
	// return getConfiguracoes(pess, item, servico, idTipo, subTipo,
	// new int[] {});
	// }
	//
	// public List<GcConfiguracao> getConfiguracoes(DpPessoa pess,
	// SrItemConfiguracao item, SrServico servico, long idTipo,
	// SrSubTipoConfiguracao subTipo, int atributoDesconsideradoFiltro[])
	// throws Exception {
	// GcConfiguracao conf = new GcConfiguracao(pess, item, servico, JPA.em()
	// .find(CpTipoConfiguracao.class, idTipo), subTipo);
	// return GcConfiguracaoBL.get().listarConfiguracoesAtivasPorFiltro(conf,
	// atributoDesconsideradoFiltro);
	// }

	public void copiar(Object dest, Object orig) {
		for (Method getter : orig.getClass().getDeclaredMethods()) {
			try {
				String getterName = getter.getName();
				if (!getterName.startsWith("get"))
					continue;
				if (Collection.class.isAssignableFrom(getter.getReturnType()))
					continue;
				String setterName = getterName.replace("get", "set");
				Object origValue = getter.invoke(orig);
				dest.getClass().getMethod(setterName, getter.getReturnType()).invoke(dest, origValue);
			} catch (NoSuchMethodException nSME) {
				int a = 0;
			} catch (IllegalAccessException iae) {
				int a = 0;
			} catch (IllegalArgumentException iae) {
				int a = 0;
			} catch (InvocationTargetException nfe) {
				int a = 0;
			}

		}
	}

	// Este mÃ©todo Ã© usado por classes para as quais o mapeamento de
	// sequence
	// nÃ£o estÃ¡ funcionando. Isso estÃ¡ ocorrendo porque, quando a
	// opÃ§Ã£o
	// jpa.ddl
	// estÃ¡ setada como validate (em vez de create-drop, por exemplo), o
	// Hibernate tenta conferir erroneamente (JIRA HHH-2508) se uma sequence
	// mapeada estÃ¡ entre as user_sequences, ou seja, entre as sequences do
	// banco cujo owner Ã© sigasr. Como hÃ¡ sequences do usuÃ¡rio
	// Corporativo,
	// nÃ£o
	// do sigasr, a aplicaÃ§Ã£o sigasr nÃ£o inicia por erro de
	// validaÃ§Ã£o do
	// Hibernate. Comentei os mapeamentos de Ã­ndice por anotaÃ§Ã£o (que
	// Ã© o
	// modo
	// de mapear usado pelo sigasr) no Corporativo, pra nÃ£o dar erro de
	// validaÃ§Ã£o. Ver soluÃ§Ã£o melhor o mais
	// rÃ¡pido possÃ­vel. Ainda, como o sigasr precisa usar sequences do
	// Corporativo (em SrMarca e GcConfiguracao) e as anotaÃ§Ãµes nÃ£o
	// estÃ£o
	// presentes, este mÃ©todo Ã© necessÃ¡rio.
	public Long nextVal(String sequence) {
		Long newId;
		return Long.valueOf(
				em().createNativeQuery("select " + sequence + ".nextval from dual").getSingleResult().toString());
	}

	private EntityManager em() {
		return ContextoPersistencia.em();
	}

	/**
	 * Cria um link referenciando automaticamente um documento/serviço/conhecimento
	 * quando é acrescentado o seu código no campo de conteúdo da informação. Ex:
	 * Estou editando um conhecimento, no seu campo texto quero referenciar o
	 * seguinte documento JFRJ-OFI-2013/00003. Quando acrescento esse código do
	 * ofício e mando salvar as alterações do conhecimento é criado um link que leva
	 * direto ao documento referenciado.
	 * 
	 * Além disso, também identifica e cria links para hashTags. Esses hashTags são
	 * inseridos no campo de classificação do conhecimento.
	 * 
	 **/
	public String marcarLinkNoConteudo(GcInformacao informacao, String conteudo) throws Exception {
		if (conteudo == null)
			return null;

		if (acronimoOrgao == null) {
			acronimoOrgao = "";
			List<String> acronimo = CpOrgaoUsuario.AR.find("select acronimoOrgaoUsu from CpOrgaoUsuario").fetch();
			for (String ao : acronimo)
				acronimoOrgao += (acronimoOrgao.isEmpty() ? "" : "|") + ao;
		}
		if (conteudo.startsWith("<")) {
			conteudo = findSiglaHTML(conteudo);
			return findHashTagHTML(informacao, conteudo, null, CONTROLE_LINK_HASH_TAG);
		}
		conteudo = findSigla(conteudo);
		return findHashTag(informacao, conteudo, null, CONTROLE_LINK_HASH_TAG);
	}

	private String findSigla(String conteudo) throws Exception {
		String sigla = null;
		GcInformacao infoReferenciada = null;
		StringBuffer sb = new StringBuffer();

		Pattern padraoSigla = Pattern.compile(
				// reconhece tais tipos de códigos: JFRJ-EOF-2013/01494.01,
				// JFRJ-REQ-2013/03579-A, JFRJ-EOF-2013/01486.01-V01,
				// TRF2-PRO-2013/00001-V01
				"(?i)(?:(?:" + acronimoOrgao
						+ ")-([A-Za-z]{2,3})-[0-9]{4}/[0-9]{5}(?:.[0-9]{2})?(?:-V[0-9]{2})?(?:-[A-Za-z]{1})?)");

		Matcher matcherSigla = padraoSigla.matcher(conteudo);
		while (matcherSigla.find()) {
			// identifica que é um código de um conhecimento, ou serviço ou
			// documento
			if (matcherSigla.group(1) != null) {
				sigla = matcherSigla.group(0).toUpperCase().trim();
				// conhecimento
				if (matcherSigla.group(1).toUpperCase().equals("GC")) {
					infoReferenciada = GcInformacao.findBySigla(sigla);
					matcherSigla.appendReplacement(sb, "[[" + URL_SIGA_GC + URLEncoder.encode(sigla, "UTF-8") + "|"
							+ sigla + " - " + infoReferenciada.getArq().getTitulo() + "]]");
				}
				// serviço
				else if (matcherSigla.group(1).toUpperCase().equals("SR")) {
					matcherSigla.appendReplacement(sb,
							"[[" + URL_SIGA_SR + URLEncoder.encode(sigla, "UTF-8") + "|" + sigla + "]]");
				}
				// documento
				else {
					matcherSigla.appendReplacement(sb,
							"[[" + URL_SIGA_DOC + URLEncoder.encode(sigla, "UTF-8") + "|" + sigla + "]]");
				}
			}
		}
		matcherSigla.appendTail(sb);
		return sb.toString();
	}

	private String getSiglaSRouGCCompacta(String sigla) {
		return sigla.replace("-", "").replace("/", "");
	}

	private String findSiglaHTML(String conteudo) throws Exception {
		String sigla = null;
		GcInformacao infoReferenciada = null;
		StringBuffer sb = new StringBuffer();

		Pattern padraoSigla = Pattern.compile(
				// reconhece tais tipos de códigos: JFRJ-EOF-2013/01494.01,
				// JFRJ-REQ-2013/03579-A, JFRJ-EOF-2013/01486.01-V01,
				// TRF2-PRO-2013/00001-V01
				"(?i)(?:(?:" + acronimoOrgao
						+ ")-([A-Za-z]{2,3})-[0-9]{4}/[0-9]{5}(?:.[0-9]{2})?(?:-V[0-9]{2})?(?:-[A-Za-z]{1})?)");

		Matcher matcherSigla = padraoSigla.matcher(conteudo);
		while (matcherSigla.find()) {
			// identifica que é um código de um conhecimento, ou serviço ou
			// documento
			if (matcherSigla.group(1) != null) {
				sigla = matcherSigla.group(0).toUpperCase().trim();
				// conhecimento
				if (matcherSigla.group(1).toUpperCase().equals("GC")) {
					infoReferenciada = GcInformacao.findBySigla(sigla);
					matcherSigla.appendReplacement(sb,
							"<a href=\"" + URL_SIGA_GC + URLEncoder.encode(getSiglaSRouGCCompacta(sigla), "UTF-8")
									+ "\">" + sigla + " - " + infoReferenciada.getArq().getTitulo() + "</a>");
				}
				// serviço
				else if (matcherSigla.group(1).toUpperCase().equals("SR")) {
					matcherSigla.appendReplacement(sb,
							"<a href=\"" + URL_SIGA_SR + URLEncoder.encode(getSiglaSRouGCCompacta(sigla), "UTF-8")
									+ "\">" + getSiglaSRouGCCompacta(sigla) + "</a>");
				}
				// documento
				else {
					matcherSigla.appendReplacement(sb,
							"<a href=\"" + URL_SIGA_DOC + URLEncoder.encode(sigla, "UTF-8") + "\">" + sigla + "</a>");
				}
			}
		}
		matcherSigla.appendTail(sb);
		return sb.toString();
	}

	/**
	 * Método que encontra uma hashTag. Quando o parâmetro controle é igual a 1, a
	 * classificação é atualizada para poder ser gravada. Quando o controle é igual
	 * a 2, o conteudo é marcado com os links das hashTags encontradas. O conteudo
	 * não é gravado com os links.
	 */
	public String findHashTag(GcInformacao informacao, String conteudo, String classificacao, int controle) {
		StringBuffer sb = new StringBuffer();
		String hashTag = new String();
		Long id = 0L;

		Pattern padraoHashTag = Pattern.compile(
				// reconhece uma hashTag (#)
				"(#(?:[0-9a-fA-F]+[G-Zg-z]|[G-Zg-z])[\\w-]*[\\w])");

		Matcher matcherHashTag = padraoHashTag.matcher(conteudo);
		while (matcherHashTag.find()) {
			if (controle == 1)
				hashTag += (hashTag.isEmpty() ? "" : ", ") + matcherHashTag.group(0);
			else if (controle == 2) {
				for (GcTag t : informacao.getTags()) {
					if (t.getTitulo().equals(matcherHashTag.group(0).substring(1))) {
						id = t.getId();
					}
				}
				matcherHashTag.appendReplacement(sb, "[[/sigagc/app/listar?filtro.pesquisa=true&filtro.tag.id=" + id
						+ "&filtro.tag.sigla=" + matcherHashTag.group(0).substring(1) + "|$0]]");
			}
		}
		if (controle == 1) {
			if (classificacao != null)
				// remove todas as hashTag da classificacao, caso exista.
				// Necessário para manter a classificacao
				// atualizada. Ao final serão inseridas as hashTags que foram
				// acrescentadas/mantidas no conteudo
				classificacao = classificacao.replaceAll("[,\\s]*#[,\\w-]+", "").trim();
			else
				classificacao = "";
			return atualizarClassificacao(classificacao, hashTag);
		} else if (controle == 2) {
			matcherHashTag.appendTail(sb);
			return sb.toString();
		} else
			return null;
	}

	public String escapeHashTag(String conteudo) {
		StringBuffer sb = new StringBuffer();
		// String hashTag = new String();

		Pattern padraoHashTag = Pattern.compile(
				// reconhece uma hashTag (#)
				"(#(?:[0-9a-fA-F]+[G-Zg-z]|[G-Zg-z])[\\w-]*[\\w])");

		Matcher matcherHashTag = padraoHashTag.matcher(conteudo);
		while (matcherHashTag.find()) {
			matcherHashTag.appendReplacement(sb, "{{{" + matcherHashTag.group(0) + "}}}");
		}
		matcherHashTag.appendTail(sb);
		return sb.toString();
	}

	public String findHashTagHTML(GcInformacao informacao, String conteudo, String classificacao, int controle) {
		StringBuffer sb = new StringBuffer();
		String hashTag = new String();
		Long id = 0L;

		Pattern padraoHashTag = Pattern.compile(
				// reconhece uma hashTag (#)
				"(#(?:[0-9a-fA-F]+[G-Zg-z]|[G-Zg-z])[\\w-]*[\\w])");

		Matcher matcherHashTag = padraoHashTag.matcher(conteudo);
		while (matcherHashTag.find()) {
			if (controle == 1)
				hashTag += (hashTag.isEmpty() ? "" : ", ") + matcherHashTag.group(0);
			else if (controle == 2) {
				for (GcTag t : informacao.getTags()) {
					if (t.getTitulo().equals(matcherHashTag.group(0).substring(1))) {
						id = t.getId();
					}
				}
				matcherHashTag.appendReplacement(sb, "<a href=\"/sigagc/app/listar?filtro.pesquisa=true&filtro.tag.id="
						+ id + "&filtro.tag.sigla=" + matcherHashTag.group(0).substring(1) + "\">$0</a>");
			}
		}
		if (controle == 1) {
			if (classificacao != null)
				// remove todas as hashTag da classificacao, caso exista.
				// Necessário para manter a classificacao
				// atualizada. Ao final serão inseridas as hashTags que foram
				// acrescentadas/mantidas no conteudo
				classificacao = classificacao.replaceAll("[,\\s]*#[,\\w-]+", "").trim();
			else
				classificacao = "";
			return atualizarClassificacao(classificacao, hashTag);
		} else if (controle == 2) {
			matcherHashTag.appendTail(sb);
			return sb.toString();
		} else
			return null;
	}

}