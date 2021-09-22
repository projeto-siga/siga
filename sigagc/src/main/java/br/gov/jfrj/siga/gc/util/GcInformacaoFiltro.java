package br.gov.jfrj.siga.gc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.gc.model.GcInformacao;
import br.gov.jfrj.siga.gc.model.GcTag;
import br.gov.jfrj.siga.gc.model.GcTipoInformacao;

public class GcInformacaoFiltro {
	public GcTipoInformacao tipo;
	public DpPessoa autor;
	public DpLotacao lotacao;
	public Integer ano;
	public Integer numero;

	public boolean pesquisa = false;
	public String dtCriacaoIni;
	public String dtCriacaoFim;
	public String dtIni;
	public String dtFim;
	public CpMarcador situacao;
	public CpOrgaoUsuario orgaoUsu;
	public String titulo;
	public String conteudo;
	public GcTag tag;
	public DpPessoa responsavel;
	public DpLotacao lotaResponsavel;

	public List<GcInformacao> buscar() throws Exception {

		if (tag != null && tag.getSigla() != null && (tag.getId() == null || tag.getId().equals(0L))) {
			tag = (GcTag) tag.selecionar(tag.getSigla());
		}

		if (autor != null && autor.getId() == null)
			autor = null;

		if (lotacao != null && lotacao.getId() == null)
			lotacao = null;

		if (responsavel != null && responsavel.getId() == null)
			responsavel = null;

		if (lotaResponsavel != null && lotaResponsavel.getId() == null)
			lotaResponsavel = null;

		String query = null;
		final SimpleDateFormat dfUsuario = new SimpleDateFormat("dd/MM/yyyy");
		final SimpleDateFormat dfHibernate = new SimpleDateFormat("yyyy-MM-dd");

		query = "from GcInformacao inf where inf.hisDtIni is not null ";

		if (tag != null && tag.getId() != null && !tag.getId().equals(0L))
			// query =
			// "select inf from models.GcInformacao as inf inner join inf.tags as tag where inf.hisDtFim is null and tag.id = "
			// + tag.id;
			query = "select inf from GcInformacao inf inner join inf.tags tags where tags.titulo = '"
					+ tag.getTitulo() + "'";
		// query += " and inf.tags.id = " + tag.id;

		if (orgaoUsu != null && orgaoUsu.getId() != null)
			query += " and inf.ou = " + orgaoUsu.getId();

		if (autor != null)
			query += " and inf.autor.idPessoaIni = " + autor.getIdInicial();
		if (lotacao != null)
			query += " and inf.lotacao.idLotacaoIni = "
					+ lotacao.getIdInicial();

		if (tipo != null && tipo.getId() != null)
			query += " and inf.tipo = " + tipo.getId();

		if (ano != null)
			query += " and inf.ano = " + Integer.parseInt(ano.toString());

		if (numero != null)
			query += " and inf.numero = " + Integer.parseInt(numero.toString());

		if (conteudo != null && !conteudo.trim().equals("")) {
			for (String s : conteudo.split(" ")) {
				/*
				 * query += " and lower(inf.arq.conteudo) like '%" +
				 * s.toLowerCase() + "%' ";
				 */
				// forma de consultar em uma coluna BLOB através do SQL
				query += " and dbms_lob.instr(inf.arq.conteudo,utl_raw.cast_to_raw('"
						+ s + "'),1,1) > 0";
			}
		}
		if (titulo != null && !titulo.trim().equals("")) {
			for (String t : titulo.split(" "))
				query += " and lower(inf.arq.titulo) like '%" + t.toLowerCase()
						+ "%' ";
		}

		if (dtCriacaoIni != null && !dtCriacaoIni.trim().equals(""))
			try {
				query += " and inf.hisDtIni >= to_date('"
						+ dfHibernate.format(dfUsuario.parse(dtCriacaoIni))
						+ "', 'yyyy-MM-dd') ";
			} catch (ParseException e) {
				//
			}

		if (dtCriacaoFim != null && !dtCriacaoFim.trim().equals(""))
			try {
				query += " and inf.hisDtIni <= to_date('"
						+ dfHibernate.format(dfUsuario.parse(dtCriacaoFim))
						+ " 23:59', 'yyyy-MM-dd HH24:mi') ";
			} catch (ParseException e) {
				//
			}

		String subquery = "";

		Boolean parametro = false;
		if (situacao != null && situacao.getIdMarcador() != null
				&& situacao.getIdMarcador() > 0)
		{
			parametro = true;
			subquery += " and situacao.cpMarcador.idMarcador = "
					+ situacao.getIdMarcador()
					+ " and (situacao.dtFimMarca is null or situacao.dtFimMarca > :dbDatetime)";
		} 
		
		else if (situacao.getIdMarcador() == null) {
			parametro = true;
			subquery += " and situacao.cpMarcador.idMarcador <> 10 "
					+ " and (situacao.dtFimMarca is null or situacao.dtFimMarca > :dbDatetime)";
		}

		if (responsavel != null)
			subquery += " and situacao.dpPessoaIni.idPessoa = "
					+ responsavel.getIdInicial();
		if (lotaResponsavel != null)
			subquery += " and situacao.dpLotacaoIni.idLotacao  = "
					+ lotaResponsavel.getIdInicial();

		if (dtIni != null && !dtIni.trim().equals(""))
			try {
				subquery += " and situacao.dtIniMarca >= to_date('"
						+ dfHibernate.format(dfUsuario.parse(dtIni))
						+ "', 'yyyy-MM-dd') ";
			} catch (ParseException e) {
				//
			}

		if (dtFim != null && !dtFim.trim().equals(""))
			try {
				subquery += " and situacao.dtIniMarca <= to_date('"
						+ dfHibernate.format(dfUsuario.parse(dtFim))
						+ " 23:59', 'yyyy-MM-dd HH24:mi') ";
			} catch (ParseException e) {
				//
			}
		
		if (subquery.length() > 0) {
			subquery = " and exists (from GcMarca situacao where situacao.inf = inf "
					+ subquery + " )";
		}

		List listaRetorno = null;
		if (parametro) {
		    listaRetorno = GcInformacao.AR.em()
				.createQuery(query + subquery + " order by inf.hisDtIni desc")
				.setParameter("dbDatetime", CpDao.getInstance().consultarDataEHoraDoServidor())
				.getResultList();
		}
		else {
			listaRetorno = GcInformacao.AR.em()
					.createQuery(query + subquery + " order by inf.hisDtIni desc")
					.getResultList();
		
		}

		return listaRetorno;
	}

	public GcTipoInformacao getTipo() {
		return tipo;
	}

	public DpPessoa getAutor() {
		return autor;
	}

	public DpLotacao getLotacao() {
		return lotacao;
	}

	public Integer getAno() {
		return ano;
	}

	public Integer getNumero() {
		return numero;
	}

	public boolean isPesquisa() {
		return pesquisa;
	}

	public String getDtCriacaoIni() {
		return dtCriacaoIni;
	}

	public String getDtCriacaoFim() {
		return dtCriacaoFim;
	}

	public String getDtIni() {
		return dtIni;
	}

	public String getDtFim() {
		return dtFim;
	}

	public CpMarcador getSituacao() {
		return situacao;
	}

	public CpOrgaoUsuario getOrgaoUsu() {
		return orgaoUsu;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public GcTag getTag() {
		return tag;
	}

	public DpPessoa getResponsavel() {
		return responsavel;
	}

	public DpLotacao getLotaResponsavel() {
		return lotaResponsavel;
	}

	public void setTipo(GcTipoInformacao tipo) {
		this.tipo = tipo;
	}

	public void setAutor(DpPessoa autor) {
		this.autor = autor;
	}

	public void setLotacao(DpLotacao lotacao) {
		this.lotacao = lotacao;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public void setPesquisa(boolean pesquisa) {
		this.pesquisa = pesquisa;
	}

	public void setDtCriacaoIni(String dtCriacaoIni) {
		this.dtCriacaoIni = dtCriacaoIni;
	}

	public void setDtCriacaoFim(String dtCriacaoFim) {
		this.dtCriacaoFim = dtCriacaoFim;
	}

	public void setDtIni(String dtIni) {
		this.dtIni = dtIni;
	}

	public void setDtFim(String dtFim) {
		this.dtFim = dtFim;
	}

	public void setSituacao(CpMarcador situacao) {
		this.situacao = situacao;
	}

	public void setOrgaoUsu(CpOrgaoUsuario orgaoUsu) {
		this.orgaoUsu = orgaoUsu;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public void setTag(GcTag tag) {
		this.tag = tag;
	}

	public void setResponsavel(DpPessoa responsavel) {
		this.responsavel = responsavel;
	}

	public void setLotaResponsavel(DpLotacao lotaResponsavel) {
		this.lotaResponsavel = lotaResponsavel;
	}
}
