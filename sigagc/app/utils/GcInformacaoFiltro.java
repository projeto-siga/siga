package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import models.GcInformacao;
import models.GcTag;
import models.GcTipoInformacao;
import play.db.jpa.JPA;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;

public class GcInformacaoFiltro extends GcInformacao {

	public String dtIni;

	public String dtFim;

	public CpMarcador situacao;

	public CpOrgaoUsuario orgaoUsu;

	public GcTipoInformacao tipo;

	public String titulo;

	public String conteudo;

	public GcTag tag;

	public List<GcInformacao> buscar() {
		String query = "from GcInformacao inf where inf.hisDtFim is null ";

		if (tag != null)
			//query = "select inf from models.GcInformacao as inf inner join inf.tags as tag where inf.hisDtFim is null and tag.id = " + tag.id;
			query = "select inf from models.GcInformacao as inf inner join inf.tags as tag where inf.hisDtFim is null and tag.titulo = '" + tag.titulo + "'";
			//query += " and inf.tags.id = " + tag.id;
		if (autor != null)
			query += " and inf.autor.idPessoaIni = " + autor.getIdInicial();
		if (lotacao != null)
			query += " and inf.lotacao.idLotacaoIni = "
					+ lotacao.getIdInicial();

		if (conteudo != null && !conteudo.trim().equals("")) {
			for (String s : conteudo.split(" "))
				query += " and lower(inf.arq.conteudo) like '%"
						+ s.toLowerCase() + "%' ";
		}

		final SimpleDateFormat dfUsuario = new SimpleDateFormat("dd/MM/yyyy");
		final SimpleDateFormat dfHibernate = new SimpleDateFormat("yyyy-MM-dd");

		if (dtIni != null)
			try {
				query += " and inf.hisDtIni >= to_date('"
						+ dfHibernate.format(dfUsuario.parse(dtIni))
						+ "', 'yyyy-MM-dd') ";
			} catch (ParseException e) {
				//
			}

		if (dtFim != null)
			try {
				query += " and inf.hisDtIni <= to_date('"
						+ dfHibernate.format(dfUsuario.parse(dtFim))
						+ " 23:59', 'yyyy-MM-dd HH24:mi') ";
			} catch (ParseException e) {
				//
			}

		String subquery = "";

		if (situacao != null && situacao.getIdMarcador() != null
				&& situacao.getIdMarcador() > 0)
			subquery += " and situacao.cpMarcador.idMarcador = "
					+ situacao.getIdMarcador();
		// if (atendente != null)
		// subquery += "and situacao.dpPessoaIni.idPessoa = "
		// + atendente.getIdInicial();
		// else if (lotaAtendente != null)
		// subquery += "and situacao.dpLotacaoIni.idLotacao = "
		// + lotaAtendente.getIdInicial();

		if (subquery.length() > 0)
			subquery = " and exists (from GcMarca situacao where situacao.inf = inf "
					+ subquery + " )";

		List listaRetorno = JPA.em().createQuery(query + subquery)
				.getResultList();

		return listaRetorno;
	}
}
