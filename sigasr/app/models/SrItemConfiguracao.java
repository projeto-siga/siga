package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_ITEM_CONFIGURACAO", schema = "SIGASR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SrItemConfiguracao extends HistoricoSuporte implements
		SrSelecionavel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Comparator<SrItemConfiguracao> comparator = new Comparator<SrItemConfiguracao>() {
		@Override
		public int compare(SrItemConfiguracao o1, SrItemConfiguracao o2) {
			if (o1 != null && o2 != null
					&& o1.idItemConfiguracao == o2.idItemConfiguracao)
				return 0;
			return o1.siglaItemConfiguracao.compareTo(o2.siglaItemConfiguracao);
		}
	};

	private static String MASCARA_JAVA = "([0-9]{0,2})\\.?([0-9]{0,2})?\\.?([0-9]{0,2})?\\.?([0-9]{0,2})?";
	// "([0-9][0-9])?([.])?([0-9][0-9])?([.])?([0-9][0-9])";

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_ITEM_CONFIGURACAO_SEQ", name = "srItemSeq")
	@GeneratedValue(generator = "srItemSeq")
	@Column(name = "ID_ITEM_CONFIGURACAO")
	public Long idItemConfiguracao;

	@Column(name = "SIGLA_ITEM_CONFIGURACAO")
	public String siglaItemConfiguracao;

	@Column(name = "DESCR_ITEM_CONFIGURACAO")
	public String descrItemConfiguracao;

	@Column(name = "TITULO_ITEM_CONFIGURACAO")
	public String tituloItemConfiguracao;

	@Lob
	@Column(name = "DESCR_SIMILARIDADE", length = 8192)
	public String descricaoSimilaridade;
		
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "ID_PAI")
	public SrItemConfiguracao pai;

	@OneToMany(targetEntity = SrItemConfiguracao.class, mappedBy = "pai", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	public List<SrItemConfiguracao> filhoSet;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrItemConfiguracao itemInicial;

	@OneToMany(targetEntity = SrItemConfiguracao.class, mappedBy = "itemInicial", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@OrderBy("hisDtIni desc")
	public List<SrItemConfiguracao> meuItemHistoricoSet;
   
	@OneToMany(targetEntity = SrGestorItem.class, mappedBy = "itemConfiguracao")
    public Set<SrGestorItem> gestorSet;

	@Column(name = "NUM_FATOR_MULTIPLICACAO_GERAL")
	public int numFatorMultiplicacaoGeral = 1;
	
	@OneToMany(targetEntity = SrFatorMultiplicacao.class, mappedBy = "itemConfiguracao")
	public Set<SrFatorMultiplicacao> fatorMultiplicacaoSet; 
	
	@Transient
	public List<SrConfiguracao> designacoes;
	
	public SrItemConfiguracao() {
		this(null, null);
	}

	public SrItemConfiguracao(String descricao) {
		this(descricao, null);
	}

	public SrItemConfiguracao(String sigla, String descricao) {
		this.tituloItemConfiguracao = descricao;
		this.siglaItemConfiguracao = sigla;
	}

	@Override
	public Long getId() {
		return idItemConfiguracao;
	}

	public String getSigla() {
		return siglaItemConfiguracao;
	}

	public String getDescricao() {
		return tituloItemConfiguracao;
	}

	@Override
	public void setId(Long id) {
		this.idItemConfiguracao = id;
	}

	public void setDescricao(String descricao) {
		this.tituloItemConfiguracao = descricao;
	}

	public List<SrItemConfiguracao> getHistoricoItemConfiguracao() {
		if (itemInicial != null)
			return itemInicial.meuItemHistoricoSet;
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

		if ((siglaItemConfiguracao == null || siglaItemConfiguracao.equals(""))
				&& (tituloItemConfiguracao == null || tituloItemConfiguracao
						.equals("")))
			return lista;

		for (SrItemConfiguracao item : lista) {
			if (siglaItemConfiguracao != null
					&& !siglaItemConfiguracao.equals("")
					&& !(item.siglaItemConfiguracao.toLowerCase()
							.contains(getSigla())))
				continue;
			if (tituloItemConfiguracao != null
					&& !tituloItemConfiguracao.equals("")) {
				boolean naoAtende = false;
				for (String s : tituloItemConfiguracao.toLowerCase().split(
						"\\s")){
					if (!item.tituloItemConfiguracao.toLowerCase().contains(s)
							&& !(item.descricaoSimilaridade != null && item.descricaoSimilaridade
									.toLowerCase().contains(s)))
						naoAtende = true;
				}
				
				if (naoAtende)
					continue;
			}

			if (comHierarquia)
				do {
					if (!listaFinal.contains(item))
						listaFinal.add(item);
					item = item.pai;
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
			siglaItemConfiguracao = "";
			tituloItemConfiguracao = "";
		} else {
			final Pattern p1 = Pattern.compile("^" + MASCARA_JAVA + "$");
			final Matcher m1 = p1.matcher(sigla);
			if (m1.find()) {
				String s = "";
				for (int i = 1; i <= m1.groupCount(); i++) {
					s += m1.group(i);
					s += (i < m1.groupCount() - 1) ? "." : "";
				}
				siglaItemConfiguracao = s;
			} else
				tituloItemConfiguracao = sigla;
		}
	}

	public int getNivel() {
		int camposVazios = 0;
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
		return SrItemConfiguracao.find(
				"byHisDtFimIsNullAndSiglaItemConfiguracao", sigla).first();
	}
	
	/**
	 * Retorna a lista de {@link SrItemConfiguracao Pai} que este item possui.
	 */
	private List<SrItemConfiguracao> getListaPai() {
		List<SrItemConfiguracao> lista = new ArrayList<SrItemConfiguracao>();
		SrItemConfiguracao itemPai = this.pai;
		
		while (itemPai != null) {
			if (!lista.contains(itemPai))
				lista.add(itemPai);
				
			itemPai = itemPai.pai;
		}
		
		return lista;
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
		
		return SrItemConfiguracao.find(sb.toString()).fetch();
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	@SuppressWarnings("unused")
	public String getGcTags() {
		int nivel = this.getNivel();
		String tags = "";
		SrItemConfiguracao pai = this.pai;
		if (pai != null)
			tags += pai.getGcTags();
		return tags + "&tags=@" + getTituloSlugify();
	}

	public String getGcTagAbertura() {
		String s = "^sr:" + getTituloSlugify();
		return s;
	}

    public String getTituloSlugify() {
		return Texto.slugify(tituloItemConfiguracao, true, false);
	}
    
    @Override
    public void salvar() throws Exception {
    	if (getNivel() > 1 && pai == null) {
			pai = getPaiPorSigla();
		}
		super.salvar();
		
        if (gestorSet != null)
            for (SrGestorItem gestor : gestorSet){
                gestor.itemConfiguracao = this;
                gestor.salvar();
            }
        
        if (fatorMultiplicacaoSet != null)
            for (SrFatorMultiplicacao fator : fatorMultiplicacaoSet){
                fator.itemConfiguracao = this;
                fator.salvar();
            }
	}

	public List<SrItemConfiguracao> getItemETodosDescendentes() {
		List<SrItemConfiguracao> lista = new ArrayList<SrItemConfiguracao>();
		lista.add(this);
		for (SrItemConfiguracao filho : filhoSet) {
			if (filho.getHisDtFim() == null)
				lista.addAll(filho.getItemETodosDescendentes());
		}
		return lista;
	}
	
	@Override
	public String toString() {
		return siglaItemConfiguracao + " - " + tituloItemConfiguracao;
	}
		
	/**
	 * Classe que representa um V.O. de {@link SrItemConfiguracao}.
	 */
	public class SrItemConfiguracaoVO {
		
		public Long id;
		public String descricao;
		public String titulo;
		public String sigla;
		public Long hisIdIni;
		public String descricaoSimilaridade;
		
		public SrItemConfiguracaoVO(Long id, String descricao, String titulo, String sigla, Long hisIdIni, String descricaoSimilaridade) {
			this.id = id;
			this.descricao = descricao;
			this.titulo = titulo;
			this.sigla = sigla;
			this.hisIdIni = hisIdIni;
			this.descricaoSimilaridade = descricaoSimilaridade;
		}
	}
	
	public SrItemConfiguracaoVO toVO() {
		return new SrItemConfiguracaoVO(this.idItemConfiguracao, this.descrItemConfiguracao, this.tituloItemConfiguracao, this.siglaItemConfiguracao, this.getHisIdIni(), this.descricaoSimilaridade);
	}
}
