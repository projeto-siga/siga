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

import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_ITEM_CONFIGURACAO", schema = "SIGASR")
public class SrItemConfiguracao extends HistoricoSuporte implements
		SrSelecionavel {

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
	//"([0-9][0-9])?([.])?([0-9][0-9])?([.])?([0-9][0-9])";
	
	
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
		
	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrItemConfiguracao itemInicial;

	@OneToMany(targetEntity = SrItemConfiguracao.class, mappedBy = "itemInicial", cascade = CascadeType.PERSIST)
	@OrderBy("hisDtIni desc")
	public List<SrItemConfiguracao> meuItemHistoricoSet;
   
	@OneToMany(fetch = FetchType.EAGER, targetEntity = SrGestorItem.class, mappedBy = "itemConfiguracao")
    public Set<SrGestorItem> gestorSet;

	@Column(name = "NUM_FATOR_MULTIPLICACAO_GERAL")
	public int numFatorMultiplicacaoGeral;
	
	@OneToMany(fetch = FetchType.EAGER, targetEntity = SrFatorMultiplicacao.class, mappedBy = "itemConfiguracao")
	public Set<SrFatorMultiplicacao> fatorMultiplicacaoSet; 
	
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

	public String getDescricaoCompleta() {
		String sigla = this.siglaItemConfiguracao;
		int nivel = this.getNivel();
		String desc_nivel = null;
		if (nivel == 1) {
			desc_nivel = this.tituloItemConfiguracao;
		}
		if (nivel == 2) {
			String sigla_nivelpai = this.getSigla().substring(0, 2) + ".00.00";
			SrItemConfiguracao configuracao = SrItemConfiguracao.find(
					"bySiglaItemConfiguracao", sigla_nivelpai).first();
			desc_nivel = configuracao.tituloItemConfiguracao + " : "
					+ this.tituloItemConfiguracao;
		}
		if (nivel == 3) {
			String sigla_nivelpai = this.getSigla().substring(0, 5) + ".00";
			SrItemConfiguracao configuracao = SrItemConfiguracao.find(
					"bySiglaItemConfiguracao", sigla_nivelpai).first();
			String sigla_nivel_anterior = this.getSigla().substring(0, 2)
					+ ".00.00";
			SrItemConfiguracao configuracao_anterior = SrItemConfiguracao.find(
					"bySiglaItemConfiguracao", sigla_nivel_anterior).first();
			desc_nivel = configuracao_anterior.tituloItemConfiguracao + " : "
					+ configuracao.tituloItemConfiguracao + " : "
					+ this.tituloItemConfiguracao;
		}
		return desc_nivel;
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
		List<SrItemConfiguracao> sols = getHistoricoItemConfiguracao();
		if (sols == null)
			return null;
		return sols.get(0);
	}

	@Override
	public SrItemConfiguracao selecionar(String sigla) throws Exception {
		return selecionar(sigla, null);
	}

	public SrItemConfiguracao selecionar(String sigla, List<SrItemConfiguracao> listaBase) throws Exception {
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
						"\\s"))
					if (!item.tituloItemConfiguracao.toLowerCase().contains(s))
						naoAtende = true;
				if (naoAtende)
					continue;
			}

			if (comHierarquia)
				do {
					if (!listaFinal.contains(item))
						listaFinal.add(item);
					item = item.getPai();
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

	public SrItemConfiguracao getPai() {
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

	public List<SrItemConfiguracao> listarItemETodosDescendentes() {
		return SrItemConfiguracao.find(
				"byHisDtFimIsNullAndSiglaItemConfiguracaoLike",
				getSiglaSemZeros() + "%").fetch();
	}

	public static List<SrItemConfiguracao> listar(boolean mostrarDesativados) {
		StringBuffer sb = new StringBuffer();
		
		if (!mostrarDesativados)
			sb.append(" hisDtFim is null");

		sb.append(" order by siglaItemConfiguracao ");
		
		return SrItemConfiguracao.find(sb.toString()).fetch();
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getGcTags() {
		String sigla = this.siglaItemConfiguracao;
		int nivel = this.getNivel();
		String tags = "";
		if (nivel == 1) {
			tags = "&tags=@"
					+ Texto.slugify(this.tituloItemConfiguracao, true, false);
		}
		if (nivel == 2) {
			String sigla_nivelpai = this.getSigla().substring(0, 2) + ".00.00";
			SrItemConfiguracao configuracao = SrItemConfiguracao.find(
					"bySiglaItemConfiguracao", sigla_nivelpai).first();
			tags = "&tags=@"
					+ Texto.slugify(configuracao.tituloItemConfiguracao, true,
							false) + "&tags=@"
					+ Texto.slugify(this.tituloItemConfiguracao, true, false);
		}
		if (nivel == 3) {
			String sigla_nivelpai = this.getSigla().substring(0, 5) + ".00";
			SrItemConfiguracao configuracao = SrItemConfiguracao.find(
					"bySiglaItemConfiguracao", sigla_nivelpai).first();
			String sigla_nivel_anterior = this.getSigla().substring(0, 2)
					+ ".00.00";
			SrItemConfiguracao configuracao_anterior = SrItemConfiguracao.find(
					"bySiglaItemConfiguracao", sigla_nivel_anterior).first();
			tags = "&tags=@"
					+ Texto.slugify(
							configuracao_anterior.tituloItemConfiguracao, true,
							false)
					+ "&tags=@"
					+ Texto.slugify(configuracao.tituloItemConfiguracao, true,
							false) + "&tags=@"
					+ Texto.slugify(this.tituloItemConfiguracao, true, false);
		}
		return tags;
	}
	
	public String getGcTagAbertura(){
			String s = "^sr:"
						+ Texto.slugify(tituloItemConfiguracao,
								true, false);
			return s;
	}


    
    @Override
    public void salvar() throws Exception {
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

}
