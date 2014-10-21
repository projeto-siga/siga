package models;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_ACAO", schema = "SIGASR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SrAcao extends HistoricoSuporte implements SrSelecionavel {

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_ACAO_SEQ", name = "srAcaoSeq")
	@GeneratedValue(generator = "srAcaoSeq")
	@Column(name = "ID_ACAO")
	public Long idAcao;

	@Column(name = "SIGLA_ACAO")
	public String siglaAcao;

	@Column(name = "DESCR_ACAO")
	public String descrAcao;

	@Column(name = "TITULO_ACAO")
	public String tituloAcao;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrAcao acaoInicial;

	@OneToMany(targetEntity = SrAcao.class, mappedBy = "acaoInicial", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@OrderBy("hisDtIni desc")
	public List<SrAcao> meuAcaoHistoricoSet;

	@ManyToOne()
	@JoinColumn(name = "ID_PAI")
	public SrAcao pai;

	@OneToMany(targetEntity = SrAcao.class, mappedBy = "pai", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	public List<SrAcao> filhoSet;

	public SrAcao() {
		this(null, null);
	}

	public SrAcao(String descricao) {
		this(descricao, null);
	}

	public SrAcao(String sigla, String descricao) {
		this.tituloAcao = descricao;
		this.siglaAcao = sigla;
	}

	@Override
	public Long getId() {
		return this.idAcao;
	}

	public void setId(Long id) {
		idAcao = id;
	}

	@Override
	public String getSigla() {
		return this.siglaAcao;
	}

	@Override
	public String getDescricao() {
		return tituloAcao;
	}

	@Override
	public void setDescricao(String descricao) {
		this.tituloAcao = descricao;
	}

	public List<SrAcao> getHistoricoAcao() {
		if (acaoInicial != null)
			return acaoInicial.meuAcaoHistoricoSet;
		return null;
	}

	public SrAcao getAtual() {
		if (getHisDtFim() == null)
			return this;
		List<SrAcao> sols = getHistoricoAcao();
		if (sols == null)
			return null;
		return sols.get(0);
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	@Override
	public SrAcao selecionar(String sigla) throws Exception {
		return selecionar(sigla, null);
	}

	public SrAcao selecionar(String sigla, List<SrAcao> listaBase)
			throws Exception {
		setSigla(sigla);
		List<SrAcao> itens = buscar(listaBase);
		if (itens.size() == 0 || itens.size() > 1)
			return null;
		return itens.get(0);

	}

	@Override
	public List<SrAcao> buscar() throws Exception {
		return buscar(null);
	}

	public List<SrAcao> buscar(List<SrAcao> listaBase) throws Exception {

		List<SrAcao> lista = new ArrayList<SrAcao>();
		List<SrAcao> listaFinal = new ArrayList<SrAcao>();

		if (listaBase == null)
			lista = listar();
		else
			lista = listaBase;

		if ((siglaAcao == null || siglaAcao.equals(""))
				&& (tituloAcao == null || tituloAcao.equals("")))
			return lista;

		for (SrAcao acao : lista) {
			if (siglaAcao != null && !siglaAcao.equals("")
					&& !(acao.siglaAcao.contains(getSigla())))
				continue;
			if (tituloAcao != null && !tituloAcao.equals("")) {
				boolean naoAtende = false;
				for (String s : tituloAcao.toLowerCase().split("\\s"))
					if (!acao.tituloAcao.toLowerCase().contains(s))
						naoAtende = true;
				if (naoAtende)
					continue;
			}
			listaFinal.add(acao);
		}
		return listaFinal;
	}

	@Override
	public void setSigla(String sigla) {
		if (sigla == null) {
			tituloAcao = "";
			siglaAcao = "";
		} else {
			String padrao = "([0-9][0-9]).?([0-9][0-9])";
			final Pattern p1 = Pattern.compile("^" + padrao);
			final Matcher m1 = p1.matcher(sigla);
			if (m1.find()) {
				String s = "";
				for (int i = 1; i <= m1.groupCount(); i++) {
					s += m1.group(i);
					s += (i < m1.groupCount()) ? "." : "";
				}
				siglaAcao = s;
			} else
				tituloAcao = sigla;
		}
	}

	public int getNivel() {
		int camposVazios = 0;
		int pos = getSigla().indexOf(".00", 0);
		while (pos > -1) {
			camposVazios++;
			pos = getSigla().indexOf(".00", pos + 1);
		}
		return 2 - camposVazios;
	}

	public boolean isEspecifico() {
		return getNivel() == 2;
	}

	public String getSiglaSemZeros() {
		int posFimComparacao = getSigla().indexOf(".00");
		if (posFimComparacao < 0)
			posFimComparacao = getSigla().length() - 1;
		return getSigla().substring(0, posFimComparacao + 1);
	}

	public SrAcao getPaiPorSigla() {
		String sigla = getSiglaSemZeros();
		sigla = sigla.substring(0, sigla.length() - 1);
		if (sigla.lastIndexOf(".") == -1)
			return null;
		sigla = sigla.substring(0, sigla.lastIndexOf("."));
		for (int i = 0; i < 2 - (getNivel() - 1); i++) {
			sigla += ".00";
		}
		return SrAcao.find("byHisDtFimIsNullAndSiglaAcao", sigla).first();
	}

	public boolean isPaiDeOuIgualA(SrAcao outraAcao) {
		if (outraAcao == null || outraAcao.getSigla() == null)
			return false;
		if (this.equals(outraAcao))
			return true;
		int posFimComparacao = getSigla().indexOf(".00");
		if (posFimComparacao < 0)
			posFimComparacao = getSigla().length() - 1;
		return outraAcao.getSigla().startsWith(
				getSigla().substring(0, posFimComparacao + 1));
	}

	public boolean isFilhoDeOuIgualA(SrAcao outroItem) {
		return outroItem.isPaiDeOuIgualA(this);
	}

	public static List<SrAcao> listar() {
		return SrAcao.find("hisDtFim is null order by siglaAcao").fetch();
	}

	public String getGcTags() {
		String tags = "";
		SrAcao pai = this.pai;
		if (pai != null)
			tags += pai.getGcTags();
		return tags + "&tags=@" + getTituloSlugify();
	}

	public String getTituloSlugify() {
		return Texto.slugify(tituloAcao, true, false);
	}

	public void salvar() throws Exception {
		if (getNivel() > 1) {
			pai = getPaiPorSigla();
		}
		super.salvar();
		
		//Edson: comentado o codigo abaixo porque muitos problemas ocorriam. Mas
		//tem de ser corrigido.
		
		//Edson: eh necessario o refresh porque, abaixo, as configuracoes referenciando
		//serao recarregadas do banco, e precisarao reconhecer o novo estado desta acao
		//refresh();

		// Edson: soh apaga o cache de configuracoes se ja existia antes uma
		// instancia do objeto, caso contrario, nao ha configuracao
		// referenciando
		//if (acaoInicial != null)
		//	SrConfiguracao.notificarQueMudou(this);
	}

	public List<SrAcao> getAcaoETodasDescendentes() {
		List<SrAcao> lista = new ArrayList<SrAcao>();
		lista.add(this);
		for (SrAcao filho : filhoSet) {
			if (filho.getHisDtFim() == null)
				lista.addAll(filho.getAcaoETodasDescendentes());
		}
		return lista;
	}
}
