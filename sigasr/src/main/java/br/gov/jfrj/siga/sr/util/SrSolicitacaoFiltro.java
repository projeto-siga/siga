package br.gov.jfrj.siga.sr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.inject.Specializes;
import javax.persistence.Query;

import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.sr.model.SrAcordo;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrAtributoSolicitacao;
import br.gov.jfrj.siga.sr.model.SrLista;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import edu.emory.mathcs.backport.java.util.Arrays;

@Specializes
public class SrSolicitacaoFiltro extends SrSolicitacao {

	private static final long serialVersionUID = 1L;
	public static final Long QUALQUER_LISTA_OU_NENHUMA = -1L;
	public static final Long NENHUMA_LISTA = 0L;
	private static final String AND = " AND ";

	private boolean pesquisar = false;

	private String dtIni;

	private String dtFim;

	private CpMarcador situacao;

	private DpPessoa atendente;
	
	//Edson: os dois atributos abaixo não deveriam existir. Eles foram inclusos devido a um erro em que às vezes o 
	//VRaptor confundia "filtro.cadastrante" com o atributo "cadastrante" da sessão, preenchendo automaticamente
	//esse campo no filtro e impedindo a busca
	private DpPessoa cadastranteBusca;
	private DpLotacao lotaCadastranteBusca;

	private SrAcordo acordo;
	
	private SrAtributoSolicitacao atributoSolicitacao;

	private DpLotacao lotaAtendente;

	private Long idListaPrioridade;

	private boolean naoDesignados;

	private Long start;

	private Long length;
	
	private String orderBy;
	
	private SentidoOrdenacao sentidoOrdenacao = SentidoOrdenacao.DESC;
	
	private static List<Long> MARCADORES_ESTADO = Arrays.asList(new Long[]{9L, 42L, 43L, 44L, 45L, 61L});
	
	public enum SentidoOrdenacao{
		ASC, DESC;
	}
	
	public SrSolicitacaoFiltro() {
		super();
	}	

	@SuppressWarnings("unchecked")
	public Long buscarQuantidade(DpPessoa buscador) throws Exception {
		// Edson: foi necessario separar em subquery porque o Oracle nao aceita
		// distinct em coluna CLOB em query contendo join
		StringBuilder query = new StringBuilder("select count(*) ");
		incluirJoinsEWheres(query, buscador);
		try {
			return (Long) ContextoPersistencia.em().createQuery(query.toString())
					.setParameter("dbDatetime", CpDao.getInstance().consultarDataEHoraDoServidor())
					.getSingleResult();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0L;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Object[]> buscarPorFiltro(DpPessoa buscador) throws Exception {
		
		StringBuilder query = new StringBuilder("");
		// Edson: foi necessario separar em subquery porque o Oracle nao aceita
		// distinct em coluna CLOB em query contendo join
		query.append("select sol, situacao, ultMov, marcaPrazo.dtIniMarca as prazo ");
		query.append(idListaPrioridade != null && idListaPrioridade > 0 ? ", l " : " "); 
				
		incluirJoinsEWheres(query, buscador);
		
		query.append(" order by ");
		if (orderBy.equals("dtReg"))
			query.append(" sol.dtReg ");
		else if (orderBy.equals("codigo"))
			query.append(" sol.codigo ");
		else if (orderBy.equals("descrSolicitacao"))
			query.append(" sol.itemConfiguracao.tituloItemConfiguracao ");
		else if (orderBy.equals("solicitante"))
			query.append(" sol.solicitante ");
		else if (orderBy.equals("lotaSolicitante"))
			query.append(" sol.lotaSolicitante.siglaLotacao ");
		else if (orderBy.equals("cadastrante"))
			query.append(" sol.cadastrante ");
		else if (orderBy.equals("lotaTitular"))
			query.append(" sol.lotaTitular.siglaLotacao ");
		else if (orderBy.equals("prioridade"))
			query.append(" sol.prioridade ");
		else if (orderBy.equals("prioridadeTecnica"))
			//query.append(" sol.dnmPrioridadeTecnica ");
			query.append(" ultMov.prioridade ");
		else if (orderBy.equals("posicaoNaLista"))
			query.append(" l.numPosicao ");
		else if (orderBy.equals("situacao"))
			query.append(" situacao.cpMarcador.descrMarcador ");
		else if (orderBy.equals("atendente"))
			query.append(" situacao.dpPessoaIni ");
		else if (orderBy.equals("lotaAtendente"))
			query.append(" situacao.dpLotacaoIni.siglaLotacao ");
		else if (orderBy.equals("dtUltimaMovimentacao"))
			query.append(" ultMov.dtIniMov ");
		else if (orderBy.equals("ultimaMovimentacao"))
			query.append(" ultMov.descrMovimentacao ");
		else if (orderBy.equals("prazo"))
			query.append(" marcaPrazo.dtIniMarca ");
		else {
			query.append(" sol.idSolicitacao ");
		}
		
		query.append(sentidoOrdenacao.name());
		Query jq = ContextoPersistencia.em().createQuery(query.toString());
		jq.setParameter("dbDatetime", CpDao.getInstance().consultarDataEHoraDoServidor());
		jq.setFirstResult(getStart().intValue());
		if (getLength() > 0)
			jq.setMaxResults(getLength().intValue());
		return jq.getResultList();
	}

	private void incluirJoinsEWheres(StringBuilder query, DpPessoa buscador) throws Exception {

		// Edson: A variável situacaoFiltro indica a marca pela qual será feita a busca por pessoa e lotação, pois há casos em que mais 
		// de uma marca está em questão. Por exemplo, quando se busca pelo marcador "Como cadastrante", o que deve ser exibido na coluna 
		//Situação é "Em andamento" (situacao), não "Como cadastrante" (situacaoAux). Porém, a busca deverá ser feita pela marca "Como cadastrante"
		String situacaoFiltro = "situacao";
		if (getSituacao() != null && getSituacao().getIdMarcador() != null
				&& getSituacao().getIdMarcador() > 0 && !MARCADORES_ESTADO.contains(getSituacao().getIdMarcador()))
			situacaoFiltro = "situacaoAux";
		
		query.append(" from SrSolicitacao sol inner join sol.meuMarcaSet situacao ");
		
		if (situacaoFiltro.equals("situacaoAux"))
			query.append(" inner join sol.meuMarcaSet situacaoAux ");
		
		query.append(" left join sol.meuMovimentacaoSet ultMov ");
		
		query.append(" left join sol.meuMarcaSet marcaPrazo with marcaPrazo.cpMarcador.idMarcador = 65 ");
		
		query.append(idListaPrioridade != null && idListaPrioridade > 0 ? " inner join sol.meuPrioridadeSolicitacaoSet l " : "");
		
		query.append(" where sol.hisDtFim is null ");
		
		query.append(" and (marcaPrazo.dpLotacaoIni is null or marcaPrazo.dpLotacaoIni = situacao.dpLotacaoIni) ");
		
		incluirWheresBasicos(query, buscador);
		
		if (situacaoFiltro.equals("situacaoAux") || getSituacao() == null)
			//Edson: Juntado, Em andamento, fechado, pendente, cancelado em elaboração: marcas que não se repetem numa solicitação
			query.append(" and situacao.cpMarcador.idMarcador in (9, 42, 43, 44, 45, 61) ");
		else 
			query.append(" and situacao.cpMarcador.idMarcador = " + getSituacao().getIdMarcador()); 
		
		query.append(" and (situacao.dtIniMarca is null or "
					+ "situacao.dtIniMarca < :dbDatetime) ");
		query.append(" and (situacao.dtFimMarca is null or "
					+ "situacao.dtFimMarca > :dbDatetime) ");
		
		if (situacaoFiltro.equals("situacaoAux")) {
			if (getSituacao() != null)
				query.append(" and situacaoAux.cpMarcador.idMarcador = "
					+ getSituacao().getIdMarcador());
			query.append(" and (situacaoAux.dtIniMarca is null or "
					+ "situacaoAux.dtIniMarca < :dbDatetime) ");
			query.append(" and (situacaoAux.dtFimMarca is null or "
					+ "situacaoAux.dtFimMarca > :dbDatetime) ");
		}
		
		if (Filtros.deveAdicionar(getAtendente())){
			query.append("and " + situacaoFiltro + ".dpPessoaIni.idPessoa = "
					+ getAtendente().getIdInicial());
		} else if (Filtros.deveAdicionar(getLotaAtendente())) {
				query.append("and " + situacaoFiltro + ".dpLotacaoIni.idLotacao = "
						+ getLotaAtendente().getIdInicial());
		}
		
		if (isNaoDesignados())
			query.append(" and " + situacaoFiltro + ".dpPessoaIni is null ");

	}
	
	private void incluirWheresBasicos(StringBuilder query, DpPessoa buscador){
		
		query.append(" and not exists (from SrMovimentacao mov where solicitacao = sol and idMovimentacao > ultMov.idMovimentacao) ");
		
		if (Filtros.deveAdicionar(getCadastranteBusca()))
			query.append(" and sol.cadastrante.idPessoaIni = "
					+ getCadastranteBusca().getIdInicial());
		if (Filtros.deveAdicionar(getLotaCadastranteBusca()))
			query.append(" and sol.lotaTitular.idLotacaoIni = "
					+ getLotaCadastranteBusca().getIdInicial());
		if (Filtros.deveAdicionar(getSolicitante()))
			query.append(" and sol.solicitante.idPessoaIni = "
					+ getSolicitante().getIdInicial());
		if (Filtros.deveAdicionar(getLotaSolicitante()))
			query.append(" and sol.lotaSolicitante.idLotacaoIni = "
					+ getLotaSolicitante().getIdInicial());
		
		if (Filtros.deveAdicionar(getItemConfiguracao()) && getItemConfiguracao().getItemInicial() != null) {
			query.append(" and ultMov.itemConfiguracao.hisIdIni = "
					+ getItemConfiguracao().getItemInicial().getIdItemConfiguracao());
		}
		if (Filtros.deveAdicionar(getAcao())){
			query.append(" and ultMov.acao.hisIdIni = "
					+ getAcao().getAcaoInicial().getIdAcao());
		}
		
		if (getPrioridade() != null && getPrioridade().getIdPrioridade() > 0L){
			query.append(" and ((case when ultMov.idMovimentacao is not null then "
					+ "ultMov.prioridade else sol.prioridade end)) = "
					+  getPrioridade().ordinal());
		}

		if (getIdListaPrioridade() != null
				&& !getIdListaPrioridade().equals(QUALQUER_LISTA_OU_NENHUMA)) {
			if (getIdListaPrioridade().equals(NENHUMA_LISTA)) {
				query.append(" and not exists (from SrPrioridadeSolicitacao prio where prio.solicitacao.hisIdIni = sol.hisIdIni) ");
			} else {
				SrLista lista = SrLista.AR.findById(getIdListaPrioridade());
				query.append(" and l.lista.hisIdIni = "
						+ lista.getListaInicial().getId());
			}
		}

		if (getDescrSolicitacao() != null
				&& !"".equals(getDescrSolicitacao().trim())) {
			for (String s : getDescrSolicitacao().split(" ")) {
				query.append(" and lower(sol.descrSolicitacao) like '%"
						+ s.toLowerCase() + "%' ");
			}
		}

		if ((getLocal() != null) && (getLocal().getIdComplexo() != null) && (!getLocal().getIdComplexo().equals(QUALQUER_LISTA_OU_NENHUMA))) {
			query.append(" and sol.local.idComplexo = " + getLocal().getIdComplexo());
		}

		final SimpleDateFormat dfUsuario = new SimpleDateFormat("dd/MM/yyyy");
		final SimpleDateFormat dfHibernate = new SimpleDateFormat("yyyy-MM-dd");

		if (getDtIni() != null)
			try {
				query.append(" and sol.dtReg >= to_date('"
						+ dfHibernate.format(dfUsuario.parse(getDtIni()))
						+ "', 'yyyy-MM-dd') ");
			} catch (ParseException e) {
				//
			}

		if (getDtFim() != null)
			try {
				query.append(" and sol.dtReg <= to_date('"
						+ dfHibernate.format(dfUsuario.parse(getDtFim()))
						+ " 23:59', 'yyyy-MM-dd HH24:mi') ");
			} catch (ParseException e) {
				//
			}
		
		if (Filtros.deveAdicionar(getAcordo()))
			query.append(" and sol.acordos.hisIdIni = " + getAcordo().getHisIdIni() + " ");		
		
		if (Filtros.deveAdicionar(getAtributoSolicitacao())) {
			query.append("  and sol.hisIdIni in (select distinct atributoDaSolicitacao.solicitacao.hisIdIni "
					+ "from sol.meuAtributoSolicitacaoSet atributoDaSolicitacao where atributoDaSolicitacao.hisDtFim is null "
					+ "and atributoDaSolicitacao.atributo.hisIdIni = " + getAtributoSolicitacao().getAtributo().getHisIdIni());
			
			if (!"".equals(getAtributoSolicitacao().getValorAtributoSolicitacao().trim())) {
				String[] valorPreenchido = getAtributoSolicitacao().getValorAtributoSolicitacao().split(" ");
				for (String valor : valorPreenchido) 
					query.append(" and lower(atributoDaSolicitacao.valorAtributoSolicitacao) like '%" + valor.toLowerCase() + "%'");
			}
			query.append(" ) ");
		}
		
		// BJN usuario externo so ve suas solicitacoes ou as que ele pode atender.
		// me parece um where basico
		if(buscador.isUsuarioExterno()) {
			query.append(" and ( sol.solicitante.idPessoaIni = " + buscador.getIdInicial().toString() + " ");
			query.append(" or ultMov.atendente.idPessoaIni = " + buscador.getLotacao().getIdInicial().toString() + " ) " );
		}
	}

	public boolean isRazoavelmentePreenchido() {
		return getSituacao() != null
				|| getCadastranteBusca() != null
				|| getLotaCadastranteBusca() != null
				|| getSolicitante() != null
				|| getLotaSolicitante() != null
				|| getAtendente() != null
				|| getLotaAtendente() != null
				|| getAcordo() != null
				|| getDtIni() != null
				|| getDtFim() != null
				|| getItemConfiguracao() != null
				|| getAcao() != null
				|| getAcordo() != null
				|| getAtributoSolicitacao() != null
				|| (getIdListaPrioridade() != null && getIdListaPrioridade() >= 0)
				|| (getDescrSolicitacao() != null && !getDescrSolicitacao().trim().equals("")) 
				|| getPrioridade() != null
				|| getLocal() != null;
	}
	
	@SuppressWarnings("unchecked")
	public List<SrSolicitacaoSimplificada> buscarSimplificado(DpPessoa buscador) throws Exception {
		StringBuilder query = new StringBuilder("select sol.hisIdIni, "
				+ " case when sol.descrSolicitacao is null then pai.descrSolicitacao else sol.descrSolicitacao end, "
				+ " sol.dtReg "
				+ " from SrSolicitacao sol left join sol.solicitacaoPai pai"
				+ " left join sol.meuMovimentacaoSet ultMov "
				+ " where sol.rascunho = false and sol.hisDtFim is null ");
		incluirWheresBasicos(query, buscador);
		query.append(" order by sol.idSolicitacao desc ");
		List<Object[]> results = ContextoPersistencia.em().createQuery(query.toString()).setMaxResults(8).getResultList();
		List<SrSolicitacaoSimplificada> sols = new ArrayList<SrSolicitacaoSimplificada>();
		for (Object[] o : results)
			sols.add(new SrSolicitacaoSimplificada((Long)o[0], (String)o[1], (Date)o[2]));
		return sols;
	}

	public boolean isPesquisar() {
		return pesquisar;
	}

	public void setPesquisar(boolean pesquisar) {
		this.pesquisar = pesquisar;
	}

	public String getDtIni() {
		return dtIni;
	}

	public void setDtIni(String dtIni) {
		this.dtIni = dtIni;
	}

	public String getDtFim() {
		return dtFim;
	}

	public void setDtFim(String dtFim) {
		this.dtFim = dtFim;
	}

	public CpMarcador getSituacao() {
		return situacao;
	}

	public void setSituacao(CpMarcador situacao) {
		this.situacao = situacao;
	}

	@Override
	public DpPessoa getAtendente() {
		return atendente;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public SentidoOrdenacao getSentidoOrdenacao() {
		return sentidoOrdenacao;
	}

	public DpPessoa getCadastranteBusca() {
		return cadastranteBusca;
	}

	public void setCadastranteBusca(DpPessoa cadastranteBusca) {
		this.cadastranteBusca = cadastranteBusca;
	}

	public DpLotacao getLotaCadastranteBusca() {
		return lotaCadastranteBusca;
	}

	public void setLotaCadastranteBusca(DpLotacao lotaCadastranteBusca) {
		this.lotaCadastranteBusca = lotaCadastranteBusca;
	}

	public void setSentidoOrdenacao(SentidoOrdenacao sentido) {
		this.sentidoOrdenacao = sentido;
	}

	public void setAtendente(DpPessoa atendente) {
		this.atendente = atendente;
	}

	public SrAcordo getAcordo() {
		return acordo;
	}

	public void setAcordo(SrAcordo acordo) {
		this.acordo = acordo;
	}

	@Override
	public DpLotacao getLotaAtendente() {
		return lotaAtendente;
	}

	public void setLotaAtendente(DpLotacao lotaAtendente) {
		this.lotaAtendente = lotaAtendente;
	}

	public Long getIdListaPrioridade() {
		return idListaPrioridade;
	}

	public void setIdListaPrioridade(Long idListaPrioridade) {
		this.idListaPrioridade = idListaPrioridade;
	}

	public boolean isNaoDesignados() {
		return naoDesignados;
	}

	public void setNaoDesignados(boolean naoDesignados) {
		this.naoDesignados = naoDesignados;
	}
	
	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public SrAtributoSolicitacao getAtributoSolicitacao() {
		return atributoSolicitacao;
	}

	public void setAtributoSolicitacao(SrAtributoSolicitacao atributoSolicitacao) {
		this.atributoSolicitacao = atributoSolicitacao;
	}

	
	
}
