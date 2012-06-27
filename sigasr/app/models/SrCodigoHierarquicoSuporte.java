package models;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

public abstract class SrCodigoHierarquicoSuporte extends GenericModel implements
		SrSelecionavel {

	private String mascaraZeros;

	private String nomeCampoSigla;

	private String nomeCampoDescricao;

	protected abstract void setSiglaInterno(String sigla);

	protected abstract void setDescricaoInterno(String descr);

	protected SrCodigoHierarquicoSuporte(String mascaraZeros,
			String nomeCampoSigla, String nomeCampoDescricao) {

		this.nomeCampoDescricao = nomeCampoDescricao;
		this.nomeCampoSigla = nomeCampoSigla;
		this.mascaraZeros = mascaraZeros;
	}

	private String getNomeClasse() {
		return getClass().getSimpleName();
	}

	protected String getSiglaSemZerosPonto() {
		// return getSigla().replace(".00", "")+".";
		return obterSiglaHierarquia(1, getNivel(), false, true);
	}

	public int getNiveis() {
		return (mascaraZeros.length() + 1) / 3;
	}

	public int getNivel() {
		if (getSigla() == null)
			return 0;
		int posZeros = getSigla().indexOf(".00");
		if (posZeros < 0)
			return 0;
		return (posZeros + 1) / 3;

	}

	protected String obterSiglaHierarquia(int nivelIni, int nivelFim,
			boolean completarComZeros, boolean pontoNaQuebra) {
		String cod = "";
		if ((nivelIni <= nivelFim) && getSigla() != null
				&& !getSigla().equals(""))
			cod = getSigla().substring((nivelIni - 1) * 3,
					((nivelFim - 1) * 3) + 2);
		String zeros = completarComZeros ? mascaraZeros.substring(
				(nivelFim * 3) - 1, mascaraZeros.length()) : "";
		cod += ((zeros.length() == 0 && pontoNaQuebra) ? "." : "");
		// zeros = ((cod.length() == 0 && pontoNaQuebra) ? "." : "") + zeros;
		return cod + zeros;
	}

	public SrCodigoHierarquicoSuporte obterPorHierarquia(int nivel) {
		if (getSigla() == null || getSigla().equals(""))
			return null;
		if (nivel > getNivel())
			return null;
		String sigla = obterSiglaHierarquia(1, nivel, true, false);
		SrCodigoHierarquicoSuporte item = JPA
				.em()
				.createQuery(
						"from " + getNomeClasse() + " where " + nomeCampoSigla
								+ " = '" + sigla + "'", getClass())
				.getSingleResult();
		return item;
	}

	public List<SrCodigoHierarquicoSuporte> obterOpcoesPorHierarquia(int nivel) {
		String sigla = obterSiglaHierarquia(1, nivel - 1, false, false);
		if (nivel > getNivel() + 1)
			return new ArrayList<SrCodigoHierarquicoSuporte>();
		String zeros = obterSiglaHierarquia(nivel + 1, nivel, true, true);
		String diferenteDaSiglaBase = "";
		if (nivel > 1)
			diferenteDaSiglaBase = " and " + nomeCampoSigla + " != '"
					+ obterSiglaHierarquia(1, nivel - 1, true, false)
					+ "'";
		List<SrCodigoHierarquicoSuporte> itens = (List<SrCodigoHierarquicoSuporte>) JPA
				.em()
				.createQuery(
						"from " + getNomeClasse() + " where " + nomeCampoSigla
								+ " like '" + sigla + "%' and "
								+ nomeCampoSigla + " like '%" + zeros + "' "
								+ diferenteDaSiglaBase, getClass())
				.getResultList();
		return itens;
	}

	@Override
	public SrSelecionavel selecionar(String sigla) {
		setSigla(sigla);
		List<SrCodigoHierarquicoSuporte> itens;
		if (getSigla() != null)
			itens = (List<SrCodigoHierarquicoSuporte>) JPA
					.em()
					.createQuery(
							"from " + getNomeClasse() + " where "
									+ nomeCampoSigla + " = '" + getSigla().toLowerCase()
									+ "'", getClass()).getResultList();
		else
			itens = (List<SrCodigoHierarquicoSuporte>) JPA
					.em()
					.createQuery(
							"from " + getNomeClasse() + " where lower("
									+ nomeCampoDescricao + ") like'%"
									+ getDescricao() + "%'", getClass())
					.getResultList();
		if (itens.size() == 0 || itens.size() > 1)
			return null;
		return itens.get(0);

	}

	@Override
	public List<SrSelecionavel> buscar() {
		String query = "from " + getNomeClasse() + " where 1=1";
		if (getDescricao() != null && !getDescricao().equals(""))
			query += " and lower(" + nomeCampoDescricao + ") like '%" + getDescricao().toLowerCase()
					+ "%'";
		if (getSigla() != null && !getSigla().equals(""))
			query += " and " + nomeCampoSigla + " like '"
					+ getSiglaSemZerosPonto() + "%'";
		return SrServico.find(query).fetch();
	}

	@Override
	public void setSigla(String sigla) {
		String padrao = "";
		for (int i = 1; i <= getNiveis(); i++)
			padrao += ((i > 1) ? ".?" : "") + "([0-9][0-9])";
		final Pattern p1 = Pattern.compile("^" + padrao);
		final Matcher m1 = p1.matcher(sigla);
		if (m1.find()) {
			String s = "";
			for (int i = 1; i <= m1.groupCount(); i++) {
				s += m1.group(i);
				s += (i < m1.groupCount()) ? "." : "";
			}
			setSiglaInterno(s);
		} else
			setDescricaoInterno(sigla);
	}
	
	public List<SrCodigoHierarquicoSuporte> getParte(){
		List<SrCodigoHierarquicoSuporte> lista = new ArrayList<SrCodigoHierarquicoSuporte>();
		for (int i = 1; i<=getNiveis(); i++)
			lista.add(obterPorHierarquia(i));
		return lista;
	}
	
	public void setParte(List<SrCodigoHierarquicoSuporte> lista){
		//Fazer
	}
	
	public List<List<SrCodigoHierarquicoSuporte>> getOpcoes(){
		List<List<SrCodigoHierarquicoSuporte>> lista = new ArrayList<List<SrCodigoHierarquicoSuporte>>();
		for (int i = 1; i<=getNivel(); i++)
			lista.add(obterOpcoesPorHierarquia(i));
		return lista;
	}
	
	public void setOpcoes(List<List<SrCodigoHierarquicoSuporte>> lista){
		//Fazer
	}
	
	public String getSiglaCurta(){
		return obterSiglaHierarquia(getNivel(), getNivel(), false, false);
	}
	
	public String getSiglaEDescricao(){
		return getSiglaCurta() + " - " + getDescricao(); 
	}

}
