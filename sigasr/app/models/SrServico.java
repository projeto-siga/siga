package models;

import java.util.ArrayList;
import java.util.Collections;
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
@Table(name = "SR_SERVICO")
public class SrServico extends GenericModel implements SrSelecionavel {

	@Id
	@GeneratedValue
	@Column(name = "ID_SERVICO")
	public int idServico;

	@Column(name = "SIGLA_SERVICO")
	public String siglaServico;

	@Column(name = "DESCR_SERVICO")
	public String descrServico;

	protected static int NIVEIS = 2;

	public SrServico(String descricao) {
		this(descricao, null);
	}

	public SrServico() {

	}

	public SrServico(String sigla, String descricao) {
		super();
		this.descrServico = descricao;
		this.siglaServico = sigla;
	}

	@Override
	public Integer getId() {
		return this.idServico;
	}

	@Override
	public String getSigla() {
		return this.siglaServico;
	}

	@Override
	public void setSigla(String sigla) {
		final Pattern p1 = Pattern
				.compile("^([0-9][0-9]).?([0-9][0-9])");
		final Matcher m1 = p1.matcher(sigla);
		if (m1.find()) {
			String s = "";
			for (int i = 1; i <= m1.groupCount(); i++) {
				s += m1.group(i);
				s += (i < m1.groupCount()) ? "." : "";
			}
			this.siglaServico = s;
		} else
			this.descrServico = sigla;
	}

	@Override
	public String getDescricao() {
		return descrServico;
	}

	@Override
	public SrSelecionavel selecionar(String sigla) {
		setSigla(sigla);
		List<SrItemConfiguracao> itens;
		if (this.siglaServico != null)
			itens = SrServico.find("bySiglaServico", this.siglaServico).fetch();
		else
			itens = SrServico.find("byDescrServicoLike",
					"%" + this.descrServico + "%").fetch();
		if (itens.size() == 0 || itens.size() > 1)
			return null;
		return itens.get(0);

	}

	@Override
	public List<SrSelecionavel> buscar() {
		String query = "from SrServico where 1=1";
		if (descrServico != null && !descrServico.equals(""))
			query += " and descrServico like '%" + descrServico + "%'";
		if (siglaServico != null && !siglaServico.equals(""))
			query += " and siglaServico like '" + getSiglaServicoSemZeros() + "%'";
		return SrServico.find(query).fetch();
	}

	/*
	 * public SrServico getServicoPai() { if (cacheServicoPai == null) {
	 * String[] partes = siglaServico.split("."); boolean comporCodigoPai =
	 * false; String codigoPai = ""; String ponto = ""; for (int i =
	 * partes.length - 1; i >= 0; i--) { if (comporCodigoPai) codigoPai =
	 * partes[i] + ponto + codigoPai; else codigoPai = "00" + ponto + codigoPai;
	 * if (!partes[i].equals("00")) comporCodigoPai = true; ponto = "."; }
	 * cacheServicoPai = SrServico.find("bySiglaServico", codigoPai) .first(); }
	 * return cacheServicoPai; }
	 * 
	 * public List<SrServico> getAscendencia() { List<SrServico> lista = new
	 * ArrayList<SrServico>(); SrServico pai = getServicoPai(); while (pai !=
	 * null) { lista.add(pai); pai = pai.getServicoPai(); }
	 * Collections.reverse(lista); return (lista); }
	 * 
	 * public List<SrServico> getFilhosImediatos() { if
	 * (!siglaServico.contains("00")) return new ArrayList<SrServico>(); String
	 * prefixoBusca = ""; String sufixoBusca = ""; String[] partes =
	 * siglaServico.split("."); for (int i = 0; i < partes.length - 1; i++) { if
	 * (partes[i].equals("00")) sufixoBusca += ".00"; else prefixoBusca +=
	 * partes[i] + "."; } return SrServico .find(
	 * "select * from SrServico where siglaServico like ? and siglaServico <> ?"
	 * , prefixoBusca, this.siglaServico).fetch(); }
	 * 
	 * public String getCodigoNivel() { String[] partes =
	 * siglaServico.split("."); String codigo = ""; for (int i = 0; i <
	 * partes.length; i++) { if (partes[i].equals("00")) break; codigo += ((i ==
	 * 0) ? "" : ".") + partes[i]; } return codigo; }
	 * 
	 * public String getCodigoUnico() { String[] partes =
	 * siglaServico.split("."); String codigo = ""; for (int i = 0; i <
	 * partes.length; i++) { if (partes[i].equals("00")) break; codigo =
	 * partes[i]; } return codigo; }
	 * 
	 * public String getDescricaoCompleta() { return getCodigoNivel() + " - " +
	 * descrServico; }
	 * 
	 * public void setSigla(String[] codigosNivel) { String codigo = ""; for
	 * (String s : codigosNivel) { codigo += (codigo.length() == 0 ? "" : ".") +
	 * s; } }
	 */
	
	private String getSiglaServicoSemZeros(){
		return siglaServico.replaceAll("\\.00", "");
	}

	public int getNivel() {
		int nZeros = 0;
		try {
			String[] partes = siglaServico.split("\\.");
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

	public SrServico getPorNivelHierarquia(int nivel) {
		String codigo = "";
		if (nivel > getNivel())
			return new SrServico();
		String[] partes = siglaServico.split("\\.");
		String ponto = "";
		for (int i = 0; i < NIVEIS; i++) {
			if (i > nivel - 1)
				codigo += ponto + "00";
			else
				codigo += ponto + partes[i];
			ponto = ".";
		}
		return SrServico.find("bySiglaServico", codigo).first();
	}

	public List<SrServico> getOpcoesPorNivel(int i) {
		if (i > getNivel() + 1)
			return new ArrayList<SrServico>();
		String zeros = "";
		String codigo = "";
		int nZeros = NIVEIS - i;
		String[] partes = new String[] {};
		try {
			partes = siglaServico.split("\\.");
		} catch (NullPointerException npe) {
			//
		}
		for (int n = 0; n < nZeros; n++)
			zeros += ".00";
		for (int n = 0; n < i - 1; n++)
			codigo += partes[n] + ".";
		List<SrServico> lista = SrServico
				.find("from SrServico where siglaServico like ? and siglaServico like ?",
						codigo + "%", "%" + zeros).fetch();
		return lista;

	}

}
