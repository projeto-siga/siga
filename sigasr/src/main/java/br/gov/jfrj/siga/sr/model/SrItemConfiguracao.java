package br.gov.jfrj.siga.sr.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sr.model.vo.PaginaItemConfiguracao;
import br.gov.jfrj.siga.sr.model.vo.SrItemConfiguracaoVO;

import com.google.gson.JsonArray;

@Entity
@Table(name = "sr_item_configuracao", schema = "sigasr")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class SrItemConfiguracao extends HistoricoSuporte implements
		SrSelecionavel, Selecionavel {

	public static final ActiveRecord<SrItemConfiguracao> AR = new ActiveRecord<>(
			SrItemConfiguracao.class);

	private static final long serialVersionUID = 1L;
	// private static final int NETO = 3;

	@SuppressWarnings("unused")
	private static Comparator<SrItemConfiguracao> comparator = new Comparator<SrItemConfiguracao>() {
		@Override
		public int compare(SrItemConfiguracao o1, SrItemConfiguracao o2) {
			if (o1 == null || o2 == null)
				return -1;
			else if (o1.getIdItemConfiguracao().equals(
					o2.getIdItemConfiguracao()))
				return 0;
			else
				return o1.getSiglaItemConfiguracao().compareTo(
						o2.getSiglaItemConfiguracao());
		}
	};

	private static String MASCARA_JAVA = "([0-9]{0,2})\\.?([0-9]{0,2})?\\.?([0-9]{0,2})?\\.?([0-9]{0,2})?";
	// "([0-9][0-9])?([.])?([0-9][0-9])?([.])?([0-9][0-9])";

	@Id
	@SequenceGenerator(sequenceName = "SIGASR" + ".SR_ITEM_CONFIGURACAO_SEQ", name = "srItemSeq")
	@GeneratedValue(generator = "srItemSeq")
	@Column(name = "ID_ITEM_CONFIGURACAO")
	private Long idItemConfiguracao;

	@Column(name = "SIGLA_ITEM_CONFIGURACAO")
	private String siglaItemConfiguracao;

	@Column(name = "DESCR_ITEM_CONFIGURACAO")
	private String descrItemConfiguracao;

	@Column(name = "TITULO_ITEM_CONFIGURACAO")
	private String tituloItemConfiguracao;

	@Lob
	@Column(name = "DESCR_SIMILARIDADE", length = 8192)
	private String descricaoSimilaridade;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "ID_PAI")
	private SrItemConfiguracao pai;

	@OneToMany(targetEntity = SrItemConfiguracao.class, mappedBy = "pai", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private List<SrItemConfiguracao> filhoSet;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	private SrItemConfiguracao itemInicial;

	@OneToMany(targetEntity = SrItemConfiguracao.class, mappedBy = "itemInicial", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@OrderBy("hisDtIni desc")
	private List<SrItemConfiguracao> meuItemHistoricoSet;

	@OneToMany(targetEntity = SrGestorItem.class, mappedBy = "itemConfiguracao", fetch = FetchType.LAZY)
	private List<SrGestorItem> gestorSet;

	@Column(name = "NUM_FATOR_MULTIPLICACAO_GERAL")
	private int numFatorMultiplicacaoGeral = 1;

	@OneToMany(targetEntity = SrFatorMultiplicacao.class, mappedBy = "itemConfiguracao", fetch = FetchType.LAZY)
	private List<SrFatorMultiplicacao> fatorMultiplicacaoSet;

	@Transient
	private List<SrConfiguracao> designacoes;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "sr_configuracao_item", schema = "sigasr", joinColumns = { @JoinColumn(name = "ID_ITEM_CONFIGURACAO") }, inverseJoinColumns = { @JoinColumn(name = "ID_CONFIGURACAO") })
	private List<SrConfiguracao> designacoesSet;

	public SrItemConfiguracao() {
		this(null, null);
	}

	public SrItemConfiguracao(String descricao) {
		this(descricao, null);
	}

	public SrItemConfiguracao(String sigla, String descricao) {
		this.setTituloItemConfiguracao(descricao);
		this.setSiglaItemConfiguracao(sigla);
	}

	public void adicionarDesignacao(SrConfiguracao designacao) throws Exception {
		if (getDesignacoesSet() == null) {
			setDesignacoesSet(new ArrayList<SrConfiguracao>());
		}
		if (podeAdicionar(designacao)) {
			getDesignacoesSet().add(designacao);
			salvarComHistorico();
		}
	}

	private boolean podeAdicionar(SrConfiguracao designacao) {
		for (SrConfiguracao designacaoSalva : getDesignacoesSet()) {
			if (designacaoSalva.getId().equals(designacao.getId())) {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

	@Override
	public Long getId() {
		return getIdItemConfiguracao();
	}

	public String getSigla() {
		return getSiglaItemConfiguracao();
	}

	public String getDescricao() {
		return getTituloItemConfiguracao();
	}
	
	public String getDescricaoCompleta() {
		return getSiglaItemConfiguracao() + " - " + getTituloItemConfiguracao();
	}

	@Override
	public void setId(Long id) {
		this.setIdItemConfiguracao(id);
	}

	public void setDescricao(String descricao) {
		this.setTituloItemConfiguracao(descricao);
	}

	public List<SrItemConfiguracao> getHistoricoItemConfiguracao() {
		if (getItemInicial() != null)
			return getItemInicial().getMeuItemHistoricoSet();
		return null;
	}

	public SrItemConfiguracao getAtual() {
		if (getHisDtFim() == null)
			return this;
		List<SrItemConfiguracao> sols = getHistoricoItemConfiguracao();
		if (sols == null)
			return null;
		return sols.get(0);
	}

	@Override
	public SrItemConfiguracao selecionar(String sigla) throws Exception {
		return selecionar(sigla, null);
	}

	public SrItemConfiguracao selecionar(String sigla,
			List<SrItemConfiguracao> listaBase) throws Exception {
		setSigla(sigla);
		List<SrItemConfiguracao> itens = buscar(listaBase, false);
		if (itens.size() == 0 || itens.size() > 1 || itens.get(0).isGenerico())
			return null;
		return itens.get(0);
	}

	@Override
	public List<SrItemConfiguracao> buscar() throws Exception {
		return buscar(null);
	}

	public List<SrItemConfiguracao> buscar(List<SrItemConfiguracao> listaBase)
			throws Exception {
		return buscar(listaBase, true);
	}

	public List<SrItemConfiguracao> buscar(List<SrItemConfiguracao> listaBase,
			boolean comHierarquia) throws Exception {

		List<SrItemConfiguracao> lista = new ArrayList<SrItemConfiguracao>();
		List<SrItemConfiguracao> listaFinal = new ArrayList<SrItemConfiguracao>();

		if (listaBase == null)
			lista = listar(Boolean.FALSE);
		else
			lista = listaBase;

		if ((getSiglaItemConfiguracao() == null || getSiglaItemConfiguracao()
				.equals(""))
				&& (getTituloItemConfiguracao() == null || getTituloItemConfiguracao()
						.equals("")))
			return lista;

		for (SrItemConfiguracao item : lista) {
			if (getSiglaItemConfiguracao() != null
					&& !getSiglaItemConfiguracao().equals("")
					&& !(item.getSiglaItemConfiguracao().toLowerCase()
							.contains(getSigla())))
				continue;
			if (getTituloItemConfiguracao() != null
					&& !getTituloItemConfiguracao().equals("")) {
				boolean naoAtende = false;
				for (String s : Texto.removeAcento(getTituloItemConfiguracao()).toLowerCase().split("\\s")) {
					if (!Texto.removeAcento(item.getTituloItemConfiguracao()).toLowerCase().contains(s)
						&& !(item.getDescricaoSimilaridade() != null 
							&& Texto.removeAcento(item.getDescricaoSimilaridade()).toLowerCase().contains(s)))
						naoAtende = true;
				}

				if (naoAtende)
					continue;
			}

			if (comHierarquia)
				do {
					if (!listaFinal.contains(item))
						listaFinal.add(item);
					item = item.getPai();
					if (item != null)
						item = item.getAtual();
				} while (item != null);
			else
				listaFinal.add(item);
		}

		Collections.sort(listaFinal, new SrItemConfiguracaoComparator());
		return listaFinal;
	}

	@Override
	public void setSigla(String sigla) {
		if (sigla == null) {
			setSiglaItemConfiguracao("");
			setTituloItemConfiguracao("");
		} else {
			final Pattern p1 = Pattern.compile("^" + MASCARA_JAVA + "$");
			final Matcher m1 = p1.matcher(sigla);
			if (m1.find()) {
				String s = "";
				for (int i = 1; i <= m1.groupCount(); i++) {
					s += m1.group(i);
					s += (i < m1.groupCount() - 1) ? "." : "";
				}
				setSiglaItemConfiguracao(s);
			} else
				setTituloItemConfiguracao(sigla);
		}
	}

	public int getNivel() {
		int camposVazios = 0;
		if (getSigla() == null)
			return 0;
		int pos = getSigla().indexOf(".00", 0);
		while (pos > -1) {
			camposVazios++;
			pos = getSigla().indexOf(".00", pos + 1);
		}

		return 3 - camposVazios;
	}

	public boolean isEspecifico() {
		return getNivel() == 3;
	}

	public boolean isGenerico() {
		return getNivel() == 1;
	}

	public String getSiglaSemZeros() {
		int posFimComparacao = getSigla().indexOf(".00");
		if (posFimComparacao < 0)
			posFimComparacao = getSigla().length() - 1;
		return getSigla().substring(0, posFimComparacao + 1);
	}

	public SrItemConfiguracao getPaiPorSigla() {
		String sigla = getSiglaSemZeros();
		sigla = sigla.substring(0, sigla.length() - 1);
		if (sigla.lastIndexOf(".") == -1)
			return null;
		sigla = sigla.substring(0, sigla.lastIndexOf("."));
		for (int i = 0; i < 3 - (getNivel() - 1); i++) {
			sigla += ".00";
		}
		return SrItemConfiguracao.AR.find(
				"byHisDtFimIsNullAndSiglaItemConfiguracao", sigla).first();
	}

	public boolean isPaiDeOuIgualA(SrItemConfiguracao outroItem) {
		if (outroItem == null || outroItem.getSigla() == null)
			return false;
		if (this.equals(outroItem))
			return true;

		return outroItem.getSigla().startsWith(getSiglaSemZeros());
	}

	public boolean isFilhoDeOuIgualA(SrItemConfiguracao outroItem) {
		return outroItem.isPaiDeOuIgualA(this);
	}

	public static List<SrItemConfiguracao> listar(boolean mostrarDesativados) {
		StringBuffer sb = new StringBuffer();

		if (!mostrarDesativados)
			sb.append(" hisDtFim is null ");
		else {
			sb.append(" idItemConfiguracao in (");
			sb.append(" SELECT max(idItemConfiguracao) as idItemConfiguracao FROM ");
			sb.append(" SrItemConfiguracao GROUP BY hisIdIni) ");
		}
		sb.append(" order by siglaItemConfiguracao ");

		return SrItemConfiguracao.AR.find(sb.toString()).fetch();
	}

	@SuppressWarnings("unchecked")
	public static List<SrItemConfiguracao> listar(
			PaginaItemConfiguracao configuracao) {
		if (configuracao.precisaExecutarCount()) {
			configuracao.setCount(countAtivos(configuracao));
		}

		StringBuilder sb = querySelecionarAtivos("i", configuracao);
		if (configuracao.getOrderBy() != null) {
			sb.append(MessageFormat.format(" order by i.{0} ",
					configuracao.getOrderBy()));
		}
		if (configuracao.getDirecaoOrdenacao() != null) {
			sb.append(configuracao.getDirecaoOrdenacao());
		}

		Query query = AR.em().createQuery(sb.toString());
		query.setFirstResult(configuracao.getFistResult());
		query.setMaxResults(configuracao.getTamanho());

		if (configuracao.possuiParametroConsulta()) {
			query.setParameter("tituloOuCodigo",
					"%" + configuracao.getTituloOuCodigo() + "%");
		}
		return query.getResultList();
	}

	private static Integer countAtivos(PaginaItemConfiguracao pagina) {
		StringBuilder sb = querySelecionarAtivos("count(i)", pagina);
		Query query = AR.em().createQuery(sb.toString());

		if (pagina.possuiParametroConsulta()) {
			query.setParameter("tituloOuCodigo",
					"%" + pagina.getTituloOuCodigo() + "%");
		}
		return ((Long) query.getSingleResult()).intValue();
	}

	private static StringBuilder querySelecionarAtivos(String clause,
			PaginaItemConfiguracao pagina) {
		StringBuilder sb = new StringBuilder();
		sb.append(MessageFormat.format(
				"SELECT {0} FROM SrItemConfiguracao i WHERE hisDtFim is null ",
				clause));

		if (pagina.possuiParametroConsulta()) {
			sb.append(" AND (UPPER(i.tituloItemConfiguracao) LIKE UPPER(:tituloOuCodigo) OR UPPER(i.siglaItemConfiguracao) LIKE UPPER(:tituloOuCodigo)) ");
		}
		sb.append(" AND idItemConfiguracao in (");
		sb.append(" SELECT max(idItemConfiguracao) as idItemConfiguracao FROM ");
		sb.append(" SrItemConfiguracao GROUP BY hisIdIni) ");

		return sb;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	@SuppressWarnings("unused")
	public String getGcTags() {
		int nivel = this.getNivel();
		String tags = "";
		SrItemConfiguracao pai = this.getPai();
		if (pai != null)
			tags += pai.getGcTags();
		return tags + "&tags=@sr-item-" + nivel + "-" + getIdInicial() + ":"
				+ getTituloSlugify();

	}

	@SuppressWarnings("unused")
	public String getGcTagAncora() {
		int nivel = this.getNivel();
		return "&tags=^sr-item-" + nivel + "-" + getIdInicial() + ":"
				+ getTituloSlugify();
	}

	public String getGcTagAbertura() {
		return getGcTagAncora();
	}

	public String getTituloSlugify() {
		return Texto.slugify(getTituloItemConfiguracao(), true, false);
	}

	@Override
	public void salvarComHistorico() throws Exception {
		if (getNivel() > 1) {
			SrItemConfiguracao pai = getPaiPorSigla();
			if (pai == null)
				throw new AplicacaoException("Ainda não há categoria para o item a ser criado.");
			setPai(pai);
		}
		super.salvarComHistorico();

		if (getGestorSet() != null)
			for (SrGestorItem gestor : getGestorSet()) {
				gestor.setItemConfiguracao(this);
				gestor.save();
			}

		if (getFatorMultiplicacaoSet() != null)
			for (SrFatorMultiplicacao fator : getFatorMultiplicacaoSet()) {
				fator.setItemConfiguracao(this);
				fator.save();
			}
	}

	public List<SrItemConfiguracao> getItemETodosDescendentes() {
		List<SrItemConfiguracao> lista = new ArrayList<SrItemConfiguracao>();
		lista.add(this);
		for (SrItemConfiguracao filho : getFilhoSet()) {
			if (filho.getHisDtFim() == null)
				lista.addAll(filho.getItemETodosDescendentes());
		}
		return lista;
	}

	@Override
	public String toString() {
		return getSiglaItemConfiguracao() + " - " + getTituloItemConfiguracao();
	}

	public SrItemConfiguracaoVO toVO() throws Exception {
		return SrItemConfiguracaoVO.createFrom(this);
	}

	/**
	 * Retorna um Json de {@link SrItemConfiguracao}.
	 * 
	 * @throws Exception
	 */
	public String getSrItemConfiguracaoJson() throws Exception {
		return this.toVO().toJson();
	}

	public SrItemConfiguracao getPai() {
		return pai;
	}

	/**
	 * Retorna a lista de {@link SrItemConfiguracao Pai} que este item possui.
	 */
	private List<SrItemConfiguracao> getListaPai() {
		List<SrItemConfiguracao> lista = new ArrayList<SrItemConfiguracao>();
		SrItemConfiguracao itemPai = this.getPai();

		while (itemPai != null) {
			if (!lista.contains(itemPai))
				lista.add(itemPai);

			itemPai = itemPai.getPai();
		}

		return lista;
	}

	/**
	 * Lista as DesignaÃ§Ãµes que sÃ£o vinculadas aos {@link SrItemConfiguracao
	 * Pai} deste item.
	 */
	public List<SrConfiguracao> getDesignacoesPai() {
		List<SrConfiguracao> listasDesignacoesPai = new ArrayList<SrConfiguracao>();

		for (SrItemConfiguracao pai : this.getListaPai()) {
			for (SrConfiguracao confPai : pai.getDesignacoesAtivas()) {
				confPai.setHerdado(true);
				confPai.setUtilizarItemHerdado(true);

				listasDesignacoesPai.add(confPai);
			}
		}

		return listasDesignacoesPai;
	}

	/**
	 * Marca os itens como herdados.
	 */
	public static List<SrConfiguracaoCache> marcarComoHerdadas(
			List<SrConfiguracaoCache> listasDesignacoesPai, SrItemConfiguracao item) {
		Iterator<SrConfiguracaoCache> i = listasDesignacoesPai.iterator();

		while (i.hasNext()) {
			SrConfiguracaoCache conf = i.next();
			boolean encontrou = false;

			conf.herdado = true;
			conf.utilizarItemHerdado = true;

			List<SrConfiguracaoIgnorada> itensIgnorados = SrConfiguracaoIgnorada
					.findByConfiguracao(conf);

			for (SrConfiguracaoIgnorada igItem : itensIgnorados) {
				// Se a configuraÃ§Ã£o for do Item, vai como desmarcado
				if (item.getId().equals(igItem.getItemConfiguracao().getId())) {
					conf.utilizarItemHerdado = false;
				}

				// se a configuraÃ§Ã£o for do Item (histÃ³rico), vai como
				// desmarcado
				else if (item.getHistoricoItemConfiguracao() != null
						&& item.getHistoricoItemConfiguracao().size() > 0) {
					for (SrItemConfiguracao itemHist : item
							.getHistoricoItemConfiguracao()) {
						if (itemHist.getId().equals(
								igItem.getItemConfiguracao().getId())) {
							conf.utilizarItemHerdado = false;
							encontrou = true;
							break;
						}
					}
				}

				else {
					SrItemConfiguracao itemPai = item.getPai();

					while (itemPai != null) {

						// Se for configuraÃ§Ã£o do pai, nÃ£o aparece na tela
						// caso esteja marcada para Ignorar no Pai
						if (itemPai.getId().equals(
								igItem.getItemConfiguracao().getId())) {
							i.remove();
							break;
						} else
							itemPai = itemPai.getPai();
					}
				}

				// Caso tenha encontrado a configuraÃ§Ã£o correta, interrompe o
				// loop
				if (encontrou)
					break;
			}
		}

		return listasDesignacoesPai;
	}

	public Map<String, SrDisponibilidade> buscarDisponibilidadesPorOrgao(
			List<CpOrgaoUsuario> orgaos) {
		return buscarDisponibilidadesPorOrgao(this, orgaos);
	}

	public Map<String, SrDisponibilidade> buscarDisponibilidadesPorOrgao(
			SrItemConfiguracao itemConfiguracao, List<CpOrgaoUsuario> orgaos) {
		if (itemConfiguracao != null) {
			return SrDisponibilidade.buscarTodos(itemConfiguracao, orgaos);
		}
		return new HashMap<String, SrDisponibilidade>();
	}

	public Collection<SrDisponibilidade> encontrarDisponibilidades(
			List<CpOrgaoUsuario> orgaos) {
		Map<String, SrDisponibilidade> disponibilidadesPai = buscarDisponibilidadesPaiPorOrgao(orgaos);
		Map<String, SrDisponibilidade> disponibilidades = buscarDisponibilidadesPorOrgao(orgaos);

		Map<String, SrDisponibilidade> disponibilidadesSelecionadas = selecionarDisponibilidades(
				disponibilidadesPai, disponibilidades, orgaos);
		return preencherDisponibilidadesVazias(disponibilidadesSelecionadas,
				orgaos);
	}

	private Collection<SrDisponibilidade> preencherDisponibilidadesVazias(
			Map<String, SrDisponibilidade> disponibilidadesSelecionadas,
			List<CpOrgaoUsuario> orgaos) {
		for (CpOrgaoUsuario cpOrgaoUsuario : orgaos) {
			if (!disponibilidadesSelecionadas.containsKey(cpOrgaoUsuario
					.getSigla())) {
				disponibilidadesSelecionadas.put(cpOrgaoUsuario.getSigla(),
						new SrDisponibilidade(this, cpOrgaoUsuario));
			}
		}
		return disponibilidadesSelecionadas.values();
	}

	public Map<String, SrDisponibilidade> buscarDisponibilidadesPaiPorOrgao(
			List<CpOrgaoUsuario> orgaos) {
		if (this.getPai() != null) {
			Map<String, SrDisponibilidade> disponibilidadesPai = buscarDisponibilidadesPorOrgao(
					this.getPai(), orgaos);
			Map<String, SrDisponibilidade> disponibilidadesAvo = buscarDisponibilidadesPorOrgao(
					this.getPai().getPai(), orgaos);

			return selecionarDisponibilidades(disponibilidadesAvo,
					disponibilidadesPai, orgaos);
		}
		return new HashMap<String, SrDisponibilidade>();
	}

	private Map<String, SrDisponibilidade> selecionarDisponibilidades(
			Map<String, SrDisponibilidade> disponibilidades,
			Map<String, SrDisponibilidade> disponibilidadesPrioritarias,
			List<CpOrgaoUsuario> orgaos) {
		Map<String, SrDisponibilidade> disponibilidadesPeriorizadas = new HashMap<String, SrDisponibilidade>();
		for (CpOrgaoUsuario orgao : orgaos) {
			SrDisponibilidade disponibilidade = selecionarDisponibilidadePrioritaria(
					disponibilidades, disponibilidadesPrioritarias, orgao);

			if (disponibilidade != null) {
				SrDisponibilidade disponibilidadeSelecionada = disponibilidade
						.pertenceA(this) ? disponibilidade : disponibilidade
						.clonarParaCriarNovo(this);
				disponibilidadesPeriorizadas.put(orgao.getSigla(),
						disponibilidadeSelecionada);
			}
		}
		return disponibilidadesPeriorizadas;
	}

	private SrDisponibilidade selecionarDisponibilidadePrioritaria(
			Map<String, SrDisponibilidade> disponibilidades,
			Map<String, SrDisponibilidade> disponibilidadesPrioritarias,
			CpOrgaoUsuario orgao) {
		SrDisponibilidade disponibilidade = disponibilidadesPrioritarias
				.get(orgao.getSigla());

		/**
		 * Se o item nao tem disponibilidade para aquele orgao, entao retorna a
		 * disponibilidade do pai
		 */
		if (disponibilidade == null) {
			return disponibilidades.get(orgao.getSigla());
		}
		/**
		 * Senao, se a disponibilidade do item eh nenhuma e o item pai possui
		 * disponibilidade, entao utiliza a disponibilidade do pai
		 */
		else if (disponibilidade.isNenhuma()) {
			SrDisponibilidade disponibilidadePai = disponibilidades.get(orgao
					.getSigla());
			if (disponibilidadePai != null) {
				return disponibilidade.clonarParaAtualizar(disponibilidadePai);
			}
		}
		return disponibilidade;
	}

	public JsonArray criarDisponibilidadesJSON(
			SrItemConfiguracao itemConfiguracao, List<CpOrgaoUsuario> orgaos) {
		JsonArray array = new JsonArray();
		for (SrDisponibilidade disponibilidade : itemConfiguracao
				.encontrarDisponibilidades(orgaos)) {
			array.add(disponibilidade.toJsonObject());
		}
		return array;
	}

	public List<SrItemConfiguracao> getFilhoSet() {
		List<SrItemConfiguracao> c = new ArrayList<SrItemConfiguracao>();

		if (this.filhoSet != null)
			c.addAll(filhoSet);

		if (this.getItemInicial() != null
				&& !this.getItemInicial().getId().equals(this.getId()))
			c.addAll(getItemInicial().getFilhoSet());

		return c;
	}

	public Collection<SrConfiguracao> getDesignacoesAtivas() {
		Map<Long, SrConfiguracao> listaCompleta = new HashMap<Long, SrConfiguracao>();
		if (this.getItemInicial() != null)
			for (SrItemConfiguracao itenConf : getHistoricoItemConfiguracao())
				if (itenConf.getDesignacoesSet() != null)
					for (SrConfiguracao d : itenConf.getDesignacoesSet())
						if (d.isAtivo() && d.isDesignacao())
							listaCompleta.put(d.getId(), d);

		return listaCompleta.values();
	}

	public Long getIdItemConfiguracao() {
		return idItemConfiguracao;
	}

	public void setIdItemConfiguracao(Long idItemConfiguracao) {
		this.idItemConfiguracao = idItemConfiguracao;
	}

	public String getSiglaItemConfiguracao() {
		return siglaItemConfiguracao;
	}

	public void setSiglaItemConfiguracao(String siglaItemConfiguracao) {
		this.siglaItemConfiguracao = siglaItemConfiguracao;
	}

	public String getDescrItemConfiguracao() {
		return descrItemConfiguracao;
	}

	public void setDescrItemConfiguracao(String descrItemConfiguracao) {
		this.descrItemConfiguracao = descrItemConfiguracao;
	}

	public String getTituloItemConfiguracao() {
		return tituloItemConfiguracao;
	}

	public void setTituloItemConfiguracao(String tituloItemConfiguracao) {
		this.tituloItemConfiguracao = tituloItemConfiguracao;
	}

	public String getDescricaoSimilaridade() {
		return descricaoSimilaridade;
	}

	public void setDescricaoSimilaridade(String descricaoSimilaridade) {
		this.descricaoSimilaridade = descricaoSimilaridade;
	}

	public void setPai(SrItemConfiguracao pai) {
		this.pai = pai;
	}

	public void setFilhoSet(List<SrItemConfiguracao> filhoSet) {
		this.filhoSet = filhoSet;
	}

	public SrItemConfiguracao getItemInicial() {
		return itemInicial;
	}

	public void setItemInicial(SrItemConfiguracao itemInicial) {
		this.itemInicial = itemInicial;
	}

	public List<SrItemConfiguracao> getMeuItemHistoricoSet() {
		return meuItemHistoricoSet;
	}

	public void setMeuItemHistoricoSet(
			List<SrItemConfiguracao> meuItemHistoricoSet) {
		this.meuItemHistoricoSet = meuItemHistoricoSet;
	}

	public int getNumFatorMultiplicacaoGeral() {
		return numFatorMultiplicacaoGeral;
	}

	public void setNumFatorMultiplicacaoGeral(int numFatorMultiplicacaoGeral) {
		this.numFatorMultiplicacaoGeral = numFatorMultiplicacaoGeral;
	}

	public List<SrGestorItem> getGestorSet() {
		return gestorSet;
	}

	public void setGestorSet(List<SrGestorItem> gestorSet) {
		this.gestorSet = gestorSet;
	}

	public List<SrFatorMultiplicacao> getFatorMultiplicacaoSet() {
		return fatorMultiplicacaoSet;
	}

	public void setFatorMultiplicacaoSet(
			List<SrFatorMultiplicacao> fatorMultiplicacaoSet) {
		this.fatorMultiplicacaoSet = fatorMultiplicacaoSet;
	}

	public List<SrConfiguracao> getDesignacoes() {
		return designacoes;
	}

	public void setDesignacoes(List<SrConfiguracao> designacoes) {
		this.designacoes = designacoes;
	}

	public List<SrConfiguracao> getDesignacoesSet() {
		return designacoesSet;
	}

	public void setDesignacoesSet(List<SrConfiguracao> designacoesSet) {
		this.designacoesSet = designacoesSet;
	}

}
