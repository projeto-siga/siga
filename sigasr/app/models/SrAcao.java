package models;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_ACAO", schema = "SIGASR")
public class SrAcao extends HistoricoSuporte implements SrSelecionavel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8387408543308440033L;

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
	
	@Column(name = "TIPO_ACAO")
	@Enumerated()
	public SrTipoAcao tipoAcao;
	
	@Column(name = "TIPO_EXECUCAO")
	@Enumerated()
	public SrTipoExecucaoAcao tipoExecucao;
	
	@Column(name = "FORMA_ATENDIMENTO")
	@Enumerated()
	public SrFormaAtendimentoAcao formaAtendimento;

	@OneToMany(targetEntity = SrAcao.class, mappedBy = "acaoInicial", cascade = CascadeType.PERSIST)
	@OrderBy("hisDtIni desc")
	public List<SrAcao> meuAcaoHistoricoSet;

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

	public String getDescricaoCompleta() {
		int nivel = this.getNivel();
		String desc_nivel = null;
		if (nivel == 1) {
			desc_nivel = this.tituloAcao;
		}
		if (nivel == 2) {
			String sigla_raiz = this.getSigla().substring(0, 2) + ".00";
			SrAcao configuracao = SrAcao.find("bySiglaAcaoAndHisDtFimIsNull",
					sigla_raiz).first();
			desc_nivel = configuracao.tituloAcao + " : " + this.tituloAcao;
		}
		return desc_nivel;
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
			lista = listar(Boolean.FALSE);
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
	
	public SrAcao getPai() {
		String sigla = getSiglaSemZeros();
		sigla = sigla.substring(0, sigla.length() - 1);
		if (sigla.lastIndexOf(".") == -1)
			return null;
		sigla = sigla.substring(0, sigla.lastIndexOf("."));
		for (int i = 0; i < 2 - (getNivel() - 1); i++) {
			sigla += ".00";
		}
		return SrAcao.find(
				"byHisDtFimIsNullAndSiglaAcao", sigla).first();
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

	public List<SrAcao> listarAcaoETodasDescendentes() {
		return SrAcao.find("byHisDtFimIsNullAndSiglaAcaoLike",
				getSiglaSemZeros() + "%").fetch();
	}

	public static List<SrAcao> listar(boolean mostrarDesativados) {
		StringBuffer sb = new StringBuffer();
		
		if (!mostrarDesativados)
			sb.append(" hisDtFim is null");
		
		sb.append(" order by siglaAcao ");
		
		return SrAcao.find(sb.toString()).fetch();
	}

	@SuppressWarnings("unused")
	public String getGcTags() {
		int nivel = this.getNivel();
		String tags = "";
		SrAcao pai = getPai();
		if (pai != null)
			tags += pai.getGcTags();
		return tags + "&tags=@" + getTituloSlugify();
	}
	
	public String getTituloSlugify() {
		return Texto.slugify(tituloAcao, true, false);
	}
	
}
