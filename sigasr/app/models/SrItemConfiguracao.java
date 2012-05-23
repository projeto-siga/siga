package models;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
@Table(name = "SR_ITEM_CONFIGURACAO")
public class SrItemConfiguracao extends GenericModel implements SrSelecionavel {

	@Id
	@GeneratedValue
	@Column(name = "ID_ITEM_CONFIGURACAO")
	public int idItemConfiguracao;

	@Column(name = "SIGLA_ITEM_CONFIGURACAO")
	public String siglaItemConfiguracao;

	@Column(name = "DESCR_ITEM_CONFIGURACAO")
	public String descrItemConfiguracao;
	
	private static int NIVEIS = 4;

	public SrItemConfiguracao() {

	}

	public SrItemConfiguracao(String sigla, String descricao) {
		super();
		this.descrItemConfiguracao = descricao;
		this.siglaItemConfiguracao = sigla;
	}

	@Override
	public Integer getId() {
		return idItemConfiguracao;
	}

	@Override
	public String getSigla() {
		return siglaItemConfiguracao;
	}

	@Override
	public void setSigla(String sigla) {
		final Pattern p1 = Pattern
				.compile("^([0-9][0-9]).?([0-9][0-9]).?([0-9][0-9]).?([0-9][0-9])");
		final Matcher m1 = p1.matcher(sigla);
		if (m1.find()) {
			String s = "";
			for (int i = 1; i <= m1.groupCount(); i++) {
				s += m1.group(i);
				s += (i < m1.groupCount()) ? "." : "";
			}
			this.siglaItemConfiguracao = s;
		} else
			this.descrItemConfiguracao = sigla;
	}

	@Override
	public String getDescricao() {
		return descrItemConfiguracao;
	}

	@Override
	public SrSelecionavel selecionar(String sigla) {
		setSigla(sigla);
		List<SrItemConfiguracao> itens;
		if (this.siglaItemConfiguracao != null)
			itens = SrItemConfiguracao.find("bySiglaItemConfiguracao", this.siglaItemConfiguracao).fetch();
		else
			itens = SrItemConfiguracao.find("byDescrItemConfiguracaoLike",
					"%" + this.descrItemConfiguracao + "%").fetch();
		if (itens.size() == 0 || itens.size() > 1)
			return null;
		return itens.get(0);

	}

	@Override
	public List<SrSelecionavel> buscar() {
		String query = "from SrItemConfiguracao where 1=1";
		if (descrItemConfiguracao != null && !descrItemConfiguracao.equals(""))
			query += " and descrItemConfiguracao like '%" + descrItemConfiguracao + "%'";
		if (siglaItemConfiguracao != null && !siglaItemConfiguracao.equals(""))
			query += " and siglaItemConfiguracao like '" + getSiglaItemConfiguracaoSemZeros() + "%'";
		return SrItemConfiguracao.find(query).fetch();
	}

	/*
	 * public SrItemConfiguracao getItemConfiguracaoPai() { if (cacheItemConfiguracaoPai == null) {
	 * String[] partes = siglaItemConfiguracao.split("."); boolean comporCodigoPai =
	 * false; String codigoPai = ""; String ponto = ""; for (int i =
	 * partes.length - 1; i >= 0; i--) { if (comporCodigoPai) codigoPai =
	 * partes[i] + ponto + codigoPai; else codigoPai = "00" + ponto + codigoPai;
	 * if (!partes[i].equals("00")) comporCodigoPai = true; ponto = "."; }
	 * cacheItemConfiguracaoPai = SrItemConfiguracao.find("bySiglaItemConfiguracao", codigoPai) .first(); }
	 * return cacheItemConfiguracaoPai; }
	 * 
	 * public List<SrItemConfiguracao> getAscendencia() { List<SrItemConfiguracao> lista = new
	 * ArrayList<SrItemConfiguracao>(); SrItemConfiguracao pai = getItemConfiguracaoPai(); while (pai !=
	 * null) { lista.add(pai); pai = pai.getItemConfiguracaoPai(); }
	 * Collections.reverse(lista); return (lista); }
	 * 
	 * public List<SrItemConfiguracao> getFilhosImediatos() { if
	 * (!siglaItemConfiguracao.contains("00")) return new ArrayList<SrItemConfiguracao>(); String
	 * prefixoBusca = ""; String sufixoBusca = ""; String[] partes =
	 * siglaItemConfiguracao.split("."); for (int i = 0; i < partes.length - 1; i++) { if
	 * (partes[i].equals("00")) sufixoBusca += ".00"; else prefixoBusca +=
	 * partes[i] + "."; } return SrItemConfiguracao .find(
	 * "select * from SrItemConfiguracao where siglaItemConfiguracao like ? and siglaItemConfiguracao <> ?"
	 * , prefixoBusca, this.siglaItemConfiguracao).fetch(); }
	 * 
	 * public String getCodigoNivel() { String[] partes =
	 * siglaItemConfiguracao.split("."); String codigo = ""; for (int i = 0; i <
	 * partes.length; i++) { if (partes[i].equals("00")) break; codigo += ((i ==
	 * 0) ? "" : ".") + partes[i]; } return codigo; }
	 * 
	 * public String getCodigoUnico() { String[] partes =
	 * siglaItemConfiguracao.split("."); String codigo = ""; for (int i = 0; i <
	 * partes.length; i++) { if (partes[i].equals("00")) break; codigo =
	 * partes[i]; } return codigo; }
	 * 
	 * public String getDescricaoCompleta() { return getCodigoNivel() + " - " +
	 * descrItemConfiguracao; }
	 * 
	 * public void setSigla(String[] codigosNivel) { String codigo = ""; for
	 * (String s : codigosNivel) { codigo += (codigo.length() == 0 ? "" : ".") +
	 * s; } }
	 */
	
	private String getSiglaItemConfiguracaoSemZeros(){
		return siglaItemConfiguracao.replaceAll("\\.00", "");
	}

	public int getNivel() {
		int nZeros = 0;
		try {
			String[] partes = siglaItemConfiguracao.split("\\.");
			for (int i = NIVEIS - 1; i >= 0; i--) {
				if (partes[i].equals("00"))
					nZeros++;
				else
					break;
			}
		} catch (NullPointerException npe) {
			return 0;
		}

		return NIVEIS - nZeros;
	}

	public SrItemConfiguracao getPorNivelHierarquia(int nivel) {
		String codigo = "";
		if (nivel > getNivel())
			return new SrItemConfiguracao();
		String[] partes = siglaItemConfiguracao.split("\\.");
		String ponto = "";
		for (int i = 0; i < NIVEIS; i++) {
			if (i > nivel - 1)
				codigo += ponto + "00";
			else
				codigo += ponto + partes[i];
			ponto = ".";
		}
		return SrItemConfiguracao.find("bySiglaItemConfiguracao", codigo).first();
	}

	public List<SrItemConfiguracao> getOpcoesPorNivel(int i) {
		if (i > getNivel() + 1)
			return new ArrayList<SrItemConfiguracao>();
		String zeros = "";
		String codigo = "";
		int nZeros = NIVEIS - i;
		String[] partes = new String[] {};
		try {
			partes = siglaItemConfiguracao.split("\\.");
		} catch (NullPointerException npe) {
			//
		}
		for (int n = 0; n < nZeros; n++)
			zeros += ".00";
		for (int n = 0; n < i - 1; n++)
			codigo += partes[n] + ".";
		List<SrItemConfiguracao> lista = SrItemConfiguracao
				.find("from SrItemConfiguracao where siglaItemConfiguracao like ? and siglaItemConfiguracao like ?",
						codigo + "%", "%" + zeros).fetch();
		return lista;

	}

}
