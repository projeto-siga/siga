package models;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;

import notifiers.Correio;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.annotations.Where;
import org.joda.time.DateTime;

import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import util.SigaPlayCalendar;
import util.Util;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_SOLICITACAO", schema = "SIGASR")
public class SrSolicitacao extends HistoricoSuporte implements SrSelecionavel {

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_SOLICITACAO_SEQ", name = "srSolicitacaoSeq")
	@GeneratedValue(generator = "srSolicitacaoSeq")
	@Column(name = "ID_SOLICITACAO")
	public Long idSolicitacao;

	@ManyToOne
	@JoinColumn(name = "ID_SOLICITANTE")
	public DpPessoa solicitante;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_SOLICITANTE")
	public DpLotacao lotaSolicitante;

	@ManyToOne
	@JoinColumn(name = "ID_CADASTRANTE")
	public DpPessoa cadastrante;

	@ManyToOne
	@JoinColumn(name = "ID_LOTA_CADASTRANTE")
	public DpLotacao lotaCadastrante;

	@ManyToOne
	@JoinColumn(name = "ID_ORGAO_USU")
	public CpOrgaoUsuario orgaoUsuario;

	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO_PAI")
	public SrSolicitacao solicitacaoPai;

	@Enumerated
	public SrFormaAcompanhamento formaAcompanhamento;

	@ManyToOne
	@JoinColumn(name = "ID_ITEM_CONFIGURACAO")
	public SrItemConfiguracao itemConfiguracao;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_ARQUIVO")
	public SrArquivo arquivo;

	@ManyToOne
	@JoinColumn(name = "ID_SERVICO")
	public SrServico servico;

	@Lob
	@Column(name = "DESCR_SOLICITACAO", length = 8192)
	public String descrSolicitacao;

	@Enumerated
	public SrGravidade gravidade;

	@Enumerated
	public SrTendencia tendencia;

	@Enumerated
	public SrUrgencia urgencia;

	@Column(name = "DT_REG")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtReg;

	@ManyToOne
	@JoinColumn(name = "ID_COMPLEXO")
	public CpComplexo local;

	@Column(name = "TEL_PRINCIPAL")
	public String telPrincipal;

	@Transient
	public boolean fecharAoAbrir;

	@Transient
	public String motivoFechamentoAbertura;

	@Column(name = "NUM_SOLICITACAO")
	public Long numSolicitacao;

	@Column(name = "NUM_SEQUENCIA")
	public Long numSequencia;
	
	@Sort(type = SortType.NATURAL)
	@OneToMany(mappedBy = "solicitacao")
	public SortedSet<SrMovimentacao> movs;

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrSolicitacao solicitacaoInicial;

	@OneToMany(targetEntity = SrSolicitacao.class, mappedBy = "solicitacaoInicial", cascade = CascadeType.PERSIST)
	@OrderBy("hisDtIni desc")
	public List<SrSolicitacao> meuSolicitacaoHistoricoSet;

	@OneToMany(targetEntity = SrAtributo.class, mappedBy = "solicitacao", cascade = CascadeType.PERSIST)
	protected List<SrAtributo> meuAtributoSet;

	@Transient
	public java.util.List<SrMarca> marcas;
	
	@Transient
	public java.util.List<SrLista> listas;

	@OneToMany(targetEntity = SrMovimentacao.class, mappedBy = "solicitacao", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@OrderBy("dtIniMov DESC")
	protected Set<SrMovimentacao> meuMovimentacaoSet;

	@OneToMany(mappedBy = "solicitacaoPai", cascade = CascadeType.PERSIST)
	@OrderBy("numSequencia asc")
	protected Set<SrSolicitacao> meuSolicitacaoFilhaSet;
	
	@PostLoad
	private void onLoad() {
		marcas = SrMarca.find("solicitacao.id = ?", this.idSolicitacao).fetch();
		listas = SrLista.find("select lista from SrMovimentacao mov where mov.tipoMov = 3 " +
				"and mov.dtFimMov is not null and mov.solicitacao = "
			+ idSolicitacao).fetch(); 
	}
	
	public SrSolicitacao() {

	}

	public SrSolicitacao(DpPessoa solicitante, DpLotacao lotaSolicitante,
			DpPessoa cadastrante, DpLotacao lotaCadastrante,
			SrSolicitacao solcitacaoPai,
			SrFormaAcompanhamento formaAcompanhamento,
			SrItemConfiguracao itemConfiguracao, SrServico servico,
			SrGravidade gravidade, SrUrgencia urgencia, SrTendencia tendencia,
			String dscrSolcitacao, CpComplexo local, String telPrincipal,
			String motivoFechamentoAbertura) {
		super();
		this.solicitante = solicitante;
		this.lotaSolicitante = lotaSolicitante;
		this.cadastrante = cadastrante;
		this.lotaCadastrante = lotaCadastrante;
		this.solicitacaoPai = solcitacaoPai;
		this.formaAcompanhamento = formaAcompanhamento;
		this.itemConfiguracao = itemConfiguracao;
		this.servico = servico;
		this.urgencia = urgencia;
		this.tendencia = tendencia;
		this.gravidade = gravidade;
		this.descrSolicitacao = dscrSolcitacao;
		this.local = local;
		this.telPrincipal = telPrincipal;
		this.motivoFechamentoAbertura = motivoFechamentoAbertura;
	}

	@Override
	public Long getId() {
		return idSolicitacao;
	}

	@Override
	public void setId(Long id) {
		this.idSolicitacao = id;
	}

	@Override
	public String getSigla() {
		return getCodigo();
	}

	@Override
	public void setSigla(String sigla) {
		sigla = sigla.trim().toUpperCase();
		final Pattern p = Pattern
				.compile("^?([A-Z]{2})?-?(SR{1})?-?([0-9]{4})?/?([0-9]{1,5})?(\\.{1})?([0-9]{1,2})?$");
		final Matcher m = p.matcher(sigla);

		if (m.find()) {

			if (m.group(1) != null) {
				try {
					CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
					orgaoUsuario.setSiglaOrgaoUsu(m.group(1));
					orgaoUsuario = (CpOrgaoUsuario) JPA
							.em()
							.createQuery(
									"from CpOrgaoUsuario where siglaOrgaoUsu = '"
											+ m.group(1) + "'")
							.getSingleResult();
					this.orgaoUsuario = orgaoUsuario;
				} catch (final Exception ce) {

				}
			}

			if (m.group(3) != null) {
				Calendar c1 = Calendar.getInstance();
				c1.set(Calendar.YEAR, Integer.valueOf(m.group(3)));
				c1.set(Calendar.DAY_OF_YEAR, 1);
				this.dtReg = c1.getTime();
			} else
				this.dtReg = new Date();

			if (m.group(4) != null)
				numSolicitacao = Long.valueOf(m.group(4));

			if (m.group(6) != null)
				numSequencia = Long.valueOf(m.group(6));
		}

	}

	@Override
	public String getDescricao() {
		if (descrSolicitacao.length() > 40)
			return descrSolicitacao.substring(0, 39) + "...";
		return descrSolicitacao;
	}

	@Override
	public void setDescricao(String descricao) {
		this.descrSolicitacao = descricao;
	}

	@Override
	public SrSelecionavel selecionar(String sigla) throws Exception {
		setSigla(sigla);
		if (orgaoUsuario == null && cadastrante != null)
			orgaoUsuario = cadastrante.getOrgaoUsuario();

		String query = "from SrSolicitacao where hisDtFim is null ";

		if (orgaoUsuario != null) {
			query += " and orgaoUsuario.idOrgaoUsu = "
					+ orgaoUsuario.getIdOrgaoUsu();
		}
		if (dtReg != null) {
			Calendar c1 = Calendar.getInstance();
			c1.setTime(dtReg);
			int year = c1.get(Calendar.YEAR);
			query += " and dtReg between to_date('01/01/" + year
					+ " 00:01', 'dd/mm/yyyy HH24:mi') and to_date('31/12/"
					+ year + " 23:59','dd/mm/yyyy HH24:mi')";
		}
		if (numSequencia != null) {
			query += " and solicitacaoPai.idSolicitacao = (select idSolicitacao from SrSolicitacao where numSolicitacao = "
					+ numSolicitacao + " )";
			query += " and numSequencia = " + numSequencia;
		} else {
			query += " and numSolicitacao = " + numSolicitacao;
		}
		SrSolicitacao sol = (SrSolicitacao) JPA.em().createQuery(query)
				.getSingleResult();

		return sol;
	}

	@Override
	public List<? extends SrSelecionavel> buscar() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCodigo(Long idSolicitacao) {
		SrSolicitacao solicitacao = new SrSolicitacao();
		solicitacao = SrSolicitacao.findById(idSolicitacao);

		if (solicitacao.solicitacaoPai != null)
			return solicitacao.solicitacaoPai.getCodigo() + "."
					+ solicitacao.getNumSequenciaString();

		String numString = solicitacao.numSolicitacao.toString();

		while (numString.length() < 5)
			numString = "0" + numString;

		return solicitacao.orgaoUsuario.getAcronimoOrgaoUsu() + "-SR-"
				+ solicitacao.getAnoEmissao() + "/" + numString;
	}

	public String getCodigo() {

		if (solicitacaoPai != null)
			return solicitacaoPai.getCodigo() + "." + getNumSequenciaString();

		String numString = numSolicitacao.toString();

		while (numString.length() < 5)
			numString = "0" + numString;

		return orgaoUsuario.getAcronimoOrgaoUsu() + "-SR-" + getAnoEmissao()
				+ "/" + numString;
	}

	public String getDtRegString() {
		SigaPlayCalendar cal = new SigaPlayCalendar();
		cal.setTime(dtReg);
		return cal.getTempoTranscorridoString(false);
	}

	public String getAtributosString() {
		String s = "";
		for (SrAtributo att : getAtributoSet()) {
			if (att.valorAtributo != null)
				s += att.tipoAtributo.nomeTipoAtributo + ": "
						+ att.valorAtributo + ". ";
		}
		return s;
	}

	// Edson: Necessário porque não há binder para arquivo
	public void setArquivo(File file) {
		this.arquivo = SrArquivo.newInstance(file);
	}

	public int getGUT() {
		return gravidade.nivelGravidade * urgencia.nivelUrgencia
				* tendencia.nivelTendencia;
	}

	public String getGUTString() {
		return gravidade.descrGravidade + " " + urgencia.descrUrgencia + " "
				+ tendencia.descrTendencia;
	}

	public String getGUTPercent() {
		return (int) ((getGUT() / 125.0) * 100) + "%";
	}

	public String getDtRegDDMMYYYYHHMM() {
		if (dtReg != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return df.format(dtReg);
		}
		return "";
	}

	public Long getProximoNumero() {
		if (orgaoUsuario == null)
			return 0L;
		Long num = find(
				"select max(numSolicitacao)+1 from SrSolicitacao where orgaoUsuario.idOrgaoUsu = "
						+ orgaoUsuario.getIdOrgaoUsu()).first();
		return (num != null) ? num : 1;
	}

	public Long getNumeroProximaFilha() {
		Long num = find(
				"select max(numSequencia)+1 from SrSolicitacao where solicitacaoPai.idSolicitacao = "
						+ idSolicitacao).first();
		return (num != null) ? num : 1;
	}

	public String getAnoEmissao() {
		if (dtReg == null)
			return null;
		return new SimpleDateFormat("yyyy").format(dtReg);
	}

	public String getNumSequenciaString() {
		if (numSequencia == null)
			return null;
		return (numSequencia < 10 ? "0" : "") + numSequencia.toString();
	}

	/*
	 * public List<SrSolicitacao> getHistoricoSolicitacao() { if (getHisIdIni()
	 * == null) return null; return find( "from SrSolicitacao where hisIdIni = "
	 * + getHisIdIni() + " order by idSolicitacao desc").fetch(); }
	 */

	public List<SrSolicitacao> getHistoricoSolicitacao() {
		if (solicitacaoInicial != null)
			return solicitacaoInicial.meuSolicitacaoHistoricoSet;
		return null;
	}

	public SrSolicitacao getSolicitacaoAtual() {
		List<SrSolicitacao> sols = getHistoricoSolicitacao();
		if (sols == null)
			return null;
		return sols.get(0);
	}

	public Set<SrMovimentacao> getMovimentacaoSet() {
		return getMovimentacaoSet(false);
	}

	public List<SrAtributo> getAtributoSet() {
		if (meuAtributoSet == null)
			return new ArrayList<SrAtributo>();
		return meuAtributoSet;
	}

	public Set<SrMovimentacao> getMovimentacaoSetComCancelados() {
		return getMovimentacaoSet(true);
	}

	public Set<SrMovimentacao> getMovimentacaoSet(boolean considerarCancelados) {
		if (solicitacaoInicial == null)
			return null;
		TreeSet<SrMovimentacao> listaCompleta = new TreeSet<SrMovimentacao>(
				new Comparator<SrMovimentacao>() {
					@Override
					public int compare(SrMovimentacao a1, SrMovimentacao a2) {
						return a2.dtIniMov.compareTo(a1.dtIniMov);
					}
				});
		for (SrSolicitacao sol : getHistoricoSolicitacao())
			if (sol.meuMovimentacaoSet != null)
				if (considerarCancelados)
					listaCompleta.addAll(sol.meuMovimentacaoSet);
				else
					for (SrMovimentacao movimentacao : sol.meuMovimentacaoSet)
						if (!movimentacao.isCancelado())
							listaCompleta.add(movimentacao);
		return listaCompleta;
	}
	
	public Set<SrMovimentacao> getMovimentacaoSet(Long tipomov) {
		if (solicitacaoInicial == null)
			return null;
		TreeSet<SrMovimentacao> listaCompleta = new TreeSet<SrMovimentacao>(
				new Comparator<SrMovimentacao>() {
					@Override
					public int compare(SrMovimentacao a1, SrMovimentacao a2) {
						return a2.dtIniMov.compareTo(a1.dtIniMov);
					}
				});
		for (SrSolicitacao sol : getHistoricoSolicitacao())
			if (sol.meuMovimentacaoSet != null)
				for (SrMovimentacao movimentacao : sol.meuMovimentacaoSet)
						if (movimentacao.tipoMov.idTipoMov == tipomov)
							listaCompleta.add(movimentacao);
		return listaCompleta;
	}
	
	public SrMovimentacao getMovimentacaoSolLista(SrSolicitacao solicitacao, SrLista lista) {
		TreeSet<SrMovimentacao> listaCompleta = new TreeSet<SrMovimentacao>(
				new Comparator<SrMovimentacao>() {
					@Override
					public int compare(SrMovimentacao a1, SrMovimentacao a2) {
						return a2.dtIniMov.compareTo(a1.dtIniMov);
					}
				});
		String query = "select mov from SrMovimentacao mov where mov.solicitacao = " + solicitacao.idSolicitacao 
				+ " and mov.lista = " + lista.idLista + " and mov.descrMovimentacao = 'Inclusão em lista' and mov.dtIniMov = (select max(dtIniMov) " 
				+ "from SrMovimentacao where solicitacao = " + solicitacao.idSolicitacao 	+ " and lista = " + lista.idLista 
				+ " and mov.descrMovimentacao = 'Inclusão em lista')";
		SrMovimentacao movIncl = (SrMovimentacao) JPA.em().createQuery(query).getSingleResult();
		return (SrMovimentacao) movIncl;
	}

	public SrMovimentacao getUltimaMovimentacao() {
		if (getMovimentacaoSet() == null)
			return null;
		for (SrMovimentacao movimentacao : getMovimentacaoSet())
			return movimentacao;
		return null;
	}
	
	public DpLotacao getLotaAtendente() {
		return getUltimaMovimentacao().lotaAtendente;
	}

	public DpPessoa getAtendente() {
		return getUltimaMovimentacao().atendente;
	}

	public String getSituacaoString() {
		return getUltimaMovimentacao().getSituacaoString();
	}

	public String getSituacaoStringSemLota() {
		try {
			return getUltimaMovimentacao().getSituacaoStringSemLota();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return getUltimaMovimentacao().getSituacaoStringSemLota();
	}

	public SrEstado getEstado() {
		return getUltimaMovimentacao().estado;
	}

	public boolean temMovimentacao() {
		return getMovimentacaoSetComCancelados() != null
				&& getMovimentacaoSetComCancelados().size() > 0;
	}

	public boolean isFilha() {
		return (this.solicitacaoPai != null);
	}

	public DpLotacao getPreAtendenteDesignado() throws Exception {
		if (solicitante == null)
			return null;
		SrConfiguracao conf = SrConfiguracao.getConfiguracao(solicitante,
				itemConfiguracao, servico,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_PRE_ATENDENTE);
		if (conf != null)
			return conf.preAtendente.getLotacaoAtual();
		return null;
	}

	public DpLotacao getAtendenteDesignado() throws Exception {
		if (solicitante == null)
			return null;
		SrConfiguracao conf = SrConfiguracao.getConfiguracao(solicitante,
				itemConfiguracao, servico,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE);
		if (conf != null)
			return conf.atendente.getLotacaoAtual();
		return null;
	}

	public HashMap<Long, Boolean> getObrigatoriedadeTiposAtributoAssociados()
			throws Exception {
		HashMap<Long, Boolean> map = new HashMap<Long, Boolean>();
		getTiposAtributoAssociados(map);
		return map;
	}

	public List<SrTipoAtributo> getTiposAtributoAssociados() throws Exception {
		return getTiposAtributoAssociados(null);
	}

	private List<SrTipoAtributo> getTiposAtributoAssociados(
			HashMap<Long, Boolean> map) throws Exception {
		List<SrTipoAtributo> listaFinal = new ArrayList<SrTipoAtributo>();

		for (SrConfiguracao conf : SrConfiguracao.getConfiguracoes(solicitante,
				itemConfiguracao, servico,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO,
				null)) {
			SrTipoAtributo tipo = conf.tipoAtributo.getAtual();
			if (!listaFinal.contains(tipo)) {
				listaFinal.add(tipo);
				if (map != null)
					map.put(tipo.idTipoAtributo, conf.atributoObrigatorio);
			}
		}
		return listaFinal;
	}

	public DpLotacao getPosAtendenteDesignado() throws Exception {
		if (solicitante == null)
			return null;

		SrConfiguracao conf = SrConfiguracao.getConfiguracao(solicitante,
				itemConfiguracao, servico,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
				SrSubTipoConfiguracao.DESIGNACAO_POS_ATENDENTE);
		if (conf != null)
			return conf.posAtendente.getLotacaoAtual();
		return null;
	}

	// Edson: poderia também guardar num HashMap transiente e, ao salvar(),
	// mandar criar os atributos, caso se quisesse permitir um
	// solicitacao.getAtributoSet().put...
	public void setAtributoMap(HashMap<Long, String> atributos) {
		meuAtributoSet = new ArrayList<SrAtributo>();
		for (Long idTipoAtt : atributos.keySet()) {
			SrTipoAtributo tipoAtt = SrTipoAtributo.findById(idTipoAtt);
			SrAtributo att = new SrAtributo(tipoAtt, atributos.get(idTipoAtt),
					this);
			meuAtributoSet.add(att);
		}
	}

	public HashMap<Long, String> getAtributoMap() {
		HashMap<Long, String> map = new HashMap<Long, String>();
		if (meuAtributoSet != null)
			for (SrAtributo att : meuAtributoSet) {
				map.put(att.tipoAtributo.idTipoAtributo, att.valorAtributo);
			}
		return map;
	}

	public List<SrSolicitacao> getSolicitacaoFilhaSet() {
		return getSolicitacaoFilhaSet(false);
	}

	private List<SrSolicitacao> getSolicitacaoFilhaSet(
			boolean considerarFinadasHistorico) {
		if (getHisIdIni() == null)
			return null;
		ArrayList<SrSolicitacao> listaCompleta = new ArrayList<SrSolicitacao>();
		for (SrSolicitacao sol : getHistoricoSolicitacao()) {
			if (sol.meuSolicitacaoFilhaSet != null)
				if (considerarFinadasHistorico)
					listaCompleta.addAll(sol.meuSolicitacaoFilhaSet);
				else
					for (SrSolicitacao filha : sol.meuSolicitacaoFilhaSet)
						if (filha.getHisDtFim() == null)
							listaCompleta.add(filha);

		}
		return listaCompleta;
	}

	public boolean isPai() {
		return getSolicitacaoFilhaSet() != null
				&& getSolicitacaoFilhaSet().size() > 0;
	}

	public List<SrMarca> getMarcaSet() {
		if (getHisIdIni() == null)
			return null;
		ArrayList<SrMarca> listaCompleta = new ArrayList<SrMarca>();
		for (SrSolicitacao sol : getHistoricoSolicitacao()) {
			if (sol.marcas != null)
				listaCompleta.addAll(sol.marcas);
		}
		/*if (sol.meuMarcaSet != null)
			listaCompleta.addAll(sol.meuMarcaSet);
		}*/
		return listaCompleta;
	}

	public List<SrEstado> getEstadosSelecionaveis() throws Exception {
		List<SrEstado> listaFinal = new ArrayList<SrEstado>();
		for (SrEstado e : SrEstado.values()) {
			if (e == SrEstado.ANDAMENTO
					&& getEstado() == SrEstado.PRE_ATENDIMENTO
					&& !getLotaAtendente().equivale(getPreAtendenteDesignado()))
				continue;
			if (e == SrEstado.PENDENTE && isEmPreAtendimento())
				continue;
			if (e == SrEstado.PRE_ATENDIMENTO && !isEmPreAtendimento())
				continue;
			if (e == SrEstado.POS_ATENDIMENTO && !isEmPosAtendimento())
				continue;
			listaFinal.add(e);
		}
		return listaFinal;
	}

	public boolean isEditado() {
		return !idSolicitacao.equals(getHisIdIni());
	}

	public boolean isCancelado() {
		return temMovimentacao()
		&& getUltimaMovimentacao().estado == SrEstado.CANCELADO;
		
	}

	public boolean isFechado() {
		return temMovimentacao()
			&& getUltimaMovimentacao().estado == SrEstado.FECHADO;
	}

	public boolean isEmPosAtendimento() {
		return temMovimentacao()
		&& getUltimaMovimentacao().estado == SrEstado.POS_ATENDIMENTO;
	}

	public boolean isEmPreAtendimento() {
		return temMovimentacao()
				&& getUltimaMovimentacao().estado == SrEstado.PRE_ATENDIMENTO;
	}
	
	public boolean isEmAtendimento() {
		return !isEmPreAtendimento() && !isEmPosAtendimento() && !isFechado();
	}

	public boolean temPreAtendenteDesignado() throws Exception {
		return (getPreAtendenteDesignado() != null);
	}

	public boolean temPosAtendenteDesignado() throws Exception {
		return (getPosAtendenteDesignado() != null);
	}

	public boolean temMovPosAtendenteDesignado() throws Exception {
		return (getPosAtendenteDesignado() != null);
	}
	
	public boolean temAtendenteDesignado() throws Exception {
		return (getAtendenteDesignado() != null);
	}

	public boolean estaCom(DpLotacao lota, DpPessoa pess) {
		return temMovimentacao()
		&& ((getUltimaMovimentacao().atendente != null && getUltimaMovimentacao().atendente
				.equivale(pess)) || getUltimaMovimentacao().lotaAtendente
				.equivale(lota));
	}

	public boolean isParteDeArvore() {
		return solicitacaoPai != null
				|| (getSolicitacaoFilhaSet() != null && !getSolicitacaoFilhaSet()
						.isEmpty());
	}

	public SrSolicitacao getPaiDaArvore() {
		SrSolicitacao pai = this;
		while (pai.solicitacaoPai != null) {
			pai = pai.solicitacaoPai;
		}
		return pai;
	}

	public boolean podeCriarFilha(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess) && isEmAtendimento() && !isFilha();
	}

	public boolean podeDesfazerMovimentacao(DpLotacao lota, DpPessoa pess) {
		SrMovimentacao ultimaMovimentacao = getUltimaMovimentacao();
		return ultimaMovimentacao.lotaCadastrante.equivale(lota)
				&& !ultimaMovimentacao.isPrimeiraMovimentacao();
	}

	public boolean podeEditar(DpLotacao lota, DpPessoa pess) {
		return isEmPreAtendimento() && estaCom(lota, pess);
	}

	public boolean podePriorizar(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess) && isEmPreAtendimento();
	}

	public boolean podeAbrirJaFechando(DpLotacao lota, DpPessoa pess) {
		return false;
	}

	public SrSolicitacao deduzirLocalERamal() {

		if (solicitante == null)
			return this;

		if (lotaSolicitante == null)
			lotaSolicitante = solicitante.getLotacao();

		String queryString = "select sol from SrSolicitacao sol where sol.idSolicitacao = ("
				+ "	select max(idSolicitacao) from SrSolicitacao "
				+ "	where solicitante.idPessoa in ("
				+ "		select idPessoa from DpPessoa "
				+ "		where idPessoaIni = :idInicial" + "	)" + ")";
		TypedQuery<SrSolicitacao> query = JPA.em().createQuery(queryString,
				SrSolicitacao.class);
		query.setParameter("idInicial", solicitante.getIdPessoaIni());
		List<SrSolicitacao> listaProvisoria = query.getResultList();
		SrSolicitacao sol = null;
		if (listaProvisoria != null && listaProvisoria.size() > 0)
			sol = listaProvisoria.get(0);

		if (sol == null && lotaSolicitante != null) {
			queryString = queryString.replace("solicitante.idPessoa",
					"lotaSolicitante.idLotacao");
			queryString = queryString.replace("Pessoa", "Lotacao");
			query = JPA.em().createQuery(queryString, SrSolicitacao.class);
			query.setParameter("idInicial", lotaSolicitante.getIdLotacaoIni());
			listaProvisoria = query.getResultList();
			if (listaProvisoria != null && listaProvisoria.size() > 0)
				sol = listaProvisoria.get(0);
		}

		if (sol != null) {
			telPrincipal = sol.telPrincipal;
			local = sol.local;
		} else {
			telPrincipal = "";
			local = null;
		}

		return this;
	}

	public void salvar(DpPessoa cadastrante, DpLotacao lotaCadastrante)
			throws Exception {
		this.cadastrante = cadastrante;
		this.lotaCadastrante = lotaCadastrante;
		salvar();
	}

	public void salvar() throws Exception {

		checarCampos();

		super.salvar();
		
		if (!temMovimentacao()) {
			if (fecharAoAbrir)
				iniciarFechando();
			else
				iniciar();
		if (!isEditado()
				&& formaAcompanhamento != SrFormaAcompanhamento.NUNCA
				&& formaAcompanhamento != SrFormaAcompanhamento.FECHAMENTO)
			notificar();
		}
	}

	public void checarCampos() throws Exception {

		if (cadastrante == null)
			throw new Exception("Cadastrante não pode ser nulo");

		dtReg = new Date();
		
		if (lotaCadastrante == null)
			lotaCadastrante = cadastrante.getLotacao();

		if (solicitante == null)
			solicitante = cadastrante;

		if (lotaSolicitante == null)
			lotaSolicitante = solicitante.getLotacao();

		if (orgaoUsuario == null)
			orgaoUsuario = lotaSolicitante.getOrgaoUsuario();

		if (numSolicitacao == null && solicitacaoPai == null)
			numSolicitacao = getProximoNumero();

		if (gravidade == null)
			gravidade = SrGravidade.NORMAL;

		if (urgencia == null)
			urgencia = SrUrgencia.NORMAL;

		if (tendencia == null)
			tendencia = SrTendencia.PIORA_MEDIO_PRAZO;

		if (!temAtendenteDesignado() && !temPreAtendenteDesignado())
			throw new Exception(
					"Não foi encontrado nenhum atendente designado "
							+ "para esta solicitação. Sugestão: alterar item de "
							+ "configuração e/ou serviço");
	}

	public void iniciar() throws Exception {
		if (temPreAtendenteDesignado())
			Movimentar(SrEstado.PRE_ATENDIMENTO,"Abertura", 
					getPreAtendenteDesignado(), (SrTipoMovimentacao) SrTipoMovimentacao.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_PRE_ATENDIMENTO),
					getNumSeqMov());
		else
			Movimentar(SrEstado.ANDAMENTO, "Abertura",
					getAtendenteDesignado(), (SrTipoMovimentacao) SrTipoMovimentacao.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO), 
					getNumSeqMov());
	}

	private Long getNumSeqMov() {
		Long numSeqMov = find(
					"select max(numSequencia)+1 from SrMovimentacao where solicitacao.idSolicitacao = "
							+ idSolicitacao).first();
			return (numSeqMov != null) ? numSeqMov : 1;
	}
	
	public void iniciarFechando() throws Exception {
		Movimentar(SrEstado.FECHADO, motivoFechamentoAbertura,
				getPosAtendenteDesignado(), 
				(SrTipoMovimentacao) SrTipoMovimentacao.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO), 
				getNumSeqMov());
	}

	public void desfazerUltimaMovimentacao(DpPessoa cadastrante,
			DpLotacao lotaCadastrante) throws Exception {
		SrMovimentacao movimentacao = getUltimaMovimentacao();
		movimentacao.desfazer(cadastrante, lotaCadastrante);
		refresh();
		atualizarMarcas(movimentacao.solicitacao);
		if (formaAcompanhamento != SrFormaAcompanhamento.NUNCA
				&& !(formaAcompanhamento == SrFormaAcompanhamento.FECHAMENTO
						&& movimentacao.estado != SrEstado.FECHADO && movimentacao.estado != SrEstado.POS_ATENDIMENTO))
			movimentacao.notificar();
	}

	public SrSolicitacao criarFilhaSemSalvar() throws Exception {
		SrSolicitacao filha = new SrSolicitacao();
		Util.copiar(filha, this);
		filha.idSolicitacao = null;
		filha.solicitacaoPai = this;
		filha.numSequencia = getNumeroProximaFilha();
		return filha;
	}

	public void notificar() {
		Correio.notificarAbertura(this);
	}

	public static void atualizarMarcas(SrSolicitacao sol) {
		SortedSet<SrMarca> setA = new TreeSet<SrMarca>();
		if (sol.marcas != null) {
			// Excluir marcas duplicadas
			for (SrMarca m : sol.marcas) {
				if (setA.contains(m))
					m.delete();
				else
					setA.add(m);
			}
		}
		SortedSet<SrMarca> setB = calcularMarcadores(sol);
		Set<SrMarca> incluir = new TreeSet<SrMarca>();
		Set<SrMarca> excluir = new TreeSet<SrMarca>();
		encaixar(setA, setB, incluir, excluir);
		for (SrMarca i : incluir) {
			if (i.solicitacao.marcas == null) {
				i.solicitacao.marcas = new ArrayList<SrMarca>();
			}
			i.solicitacao = sol;
			i.save();
			i.solicitacao.marcas.add(i);
		}
		for (SrMarca e : excluir) {
			if (e.solicitacao.marcas == null) {
					e.solicitacao.marcas = new ArrayList<SrMarca>();
			}
			e.solicitacao.marcas.remove(e);
			e.delete();
		}
	}

	public void removerMarcas() {
		for (SrMarca marca : getMarcaSet())
			JPA.em().remove(marca);
	}

	private static void encaixar(SortedSet<SrMarca> setA,
			SortedSet<SrMarca> setB, Set<SrMarca> incluir, Set<SrMarca> excluir) {
		Iterator<SrMarca> ia = setA.iterator();
		Iterator<SrMarca> ib = setB.iterator();

		SrMarca a = null;
		SrMarca b = null;

		if (ia.hasNext())
			a = ia.next();
		if (ib.hasNext())
			b = ib.next();
		while (a != null || b != null) {
			if ((a == null) || (b != null && a.compareTo(b) > 0)) {
				// Existe em setB, mas nao existe em setA
				incluir.add(b);
				if (ib.hasNext())
					b = ib.next();
				else
					b = null;

			} else if (b == null || (a != null && b.compareTo(a) > 0)) {
				// Existe em setA, mas nao existe em setB
				excluir.add(a);
				if (ia.hasNext())
					a = ia.next();
				else
					a = null;
			} else {

				if (a == null) {
					int i = 0;
				}
				// O registro existe nos dois - atualizar.add(new Par(b, a));
				if (ib.hasNext())
					b = ib.next();
				else
					b = null;
				if (ia.hasNext())
					a = ia.next();
				else
					a = null;
			}
		}
		ib = null;
		ia = null;
	}
	
	private static void acrescentarMarca(SortedSet<SrMarca> set,
			SrSolicitacao sol, Long idMarcador, Date dtIni, Date dtFim,
			DpPessoa pess, DpLotacao lota) {
		SrMarca mar = new SrMarca();
		mar.solicitacao = sol;
		mar.setCpMarcador((CpMarcador) CpMarcador.findById(idMarcador));
		if (pess != null)
			mar.setDpPessoaIni(pess.getPessoaInicial());
		if (lota != null)
			mar.setDpLotacaoIni(lota.getLotacaoInicial());
		mar.setDtIniMarca(dtIni);
		mar.setDtFimMarca(dtFim);
		set.add(mar);
	}
	
	private static SortedSet<SrMarca> calcularMarcadores(SrSolicitacao sol) {
		SortedSet<SrMarca> set = new TreeSet<SrMarca>();

		if (sol.movs != null) {
			for (SrMovimentacao mov : sol.meuMovimentacaoSet) {
				Long t = mov.tipoMov.idTipoMov;
				SrEstado e = mov.estado;
				if (mov.isCancelado())
					continue;
				if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO)
					acrescentarMarca(set, sol, CpMarcador.MARCADOR_SOLICITACAO_EM_ANDAMENTO,
							mov.dtIniMov, null, mov.cadastrante, mov.lotaCadastrante);
				if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO)
					acrescentarMarca(set, sol,
							CpMarcador.MARCADOR_SOLICITACAO_EM_ANDAMENTO, mov.dtIniMov, null, mov.cadastrante, mov.lotaCadastrante);
				if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_PRE_ATENDIMENTO)
					acrescentarMarca(set, sol,
							CpMarcador.MARCADOR_SOLICITACAO_PRE_ATENDIMENTO, mov.dtIniMov, null, mov.cadastrante, mov.lotaCadastrante);
				if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_POS_ATENDIMENTO)
					acrescentarMarca(set, sol,
							CpMarcador.MARCADOR_SOLICITACAO_POS_ATENDIMENTO, mov.dtIniMov, null, mov.cadastrante, mov.lotaCadastrante);
				if (t == SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_INCLUSAO_LISTA)
					acrescentarMarca(set, sol,
							CpMarcador.MARCADOR_SOLICITACAO_EM_ANDAMENTO, mov.dtIniMov, null, mov.cadastrante, mov.lotaCadastrante);
				if (e == SrEstado.ANDAMENTO)
					acrescentarMarca(set, sol,
							CpMarcador.MARCADOR_SOLICITACAO_EM_ANDAMENTO, mov.dtIniMov, null, mov.cadastrante, mov.lotaCadastrante);
				if (e == SrEstado.PENDENTE)
					acrescentarMarca(set, sol,
							CpMarcador.MARCADOR_SOLICITACAO_PENDENTE, mov.dtIniMov, null, mov.cadastrante, mov.lotaCadastrante);
				if (e == SrEstado.FECHADO)
					acrescentarMarca(set, sol,
							CpMarcador.MARCADOR_SOLICITACAO_FECHADO, mov.dtIniMov, null, mov.cadastrante, mov.lotaCadastrante);
				if (e == SrEstado.CANCELADO)
					acrescentarMarca(set, sol,
							CpMarcador.MARCADOR_SOLICITACAO_CANCELADO, mov.dtIniMov, null, mov.cadastrante, mov.lotaCadastrante);
				}
		}
		if (sol.getHisDtFim() != null) { //quando preencho dthisfim da solicitação??? Quando a situação selecionada é Fechado. 
			acrescentarMarca(set, sol,
					CpMarcador.MARCADOR_SOLICITACAO_CANCELADO, sol.dtReg, null, sol.cadastrante, sol.lotaCadastrante);
		} else {
			acrescentarMarca(set, sol,
					CpMarcador.MARCADOR_SOLICITACAO_EM_ANDAMENTO, sol.dtReg, null, sol.cadastrante, sol.lotaCadastrante);
		}
		if (!sol.isFechado() && !sol.isCancelado()) {
		 	acrescentarMarca(set, sol,
					CpMarcador.MARCADOR_SOLICITACAO_COMO_CADASTRANTE, null, null, sol.cadastrante, sol.lotaCadastrante);
			acrescentarMarca(set, sol,
					CpMarcador.MARCADOR_SOLICITACAO_COMO_CADASTRANTE, null, null, sol.solicitante, sol.lotaSolicitante);
		}
	
		if (!sol.isFechado() && !sol.isCancelado()) {
			acrescentarMarca(set, sol,
					CpMarcador.MARCADOR_SOLICITACAO_COMO_CADASTRANTE, null, null, sol.cadastrante, sol.lotaCadastrante);
			acrescentarMarca(set, sol,
					CpMarcador.MARCADOR_SOLICITACAO_COMO_CADASTRANTE, null, null, sol.solicitante, sol.lotaSolicitante);
		}
		return set;
	}
	
	public String getMarcadoresEmHtml(DpPessoa pess, DpLotacao lota) {
		StringBuilder sb = new StringBuilder();
		// Marcacoes para a propria lotacao e para a propria pessoa ou sem informacao de pessoa
		for (SrMarca mar : marcas) {
			if (((mar.getDpLotacaoIni() != null && lota.getIdInicial().equals(
					mar.getDpLotacaoIni().getIdInicial())) || mar
					.getDpLotacaoIni() == null)
					&& (mar.getDpPessoaIni() == null || pess.getIdInicial()
							.equals(mar.getDpPessoaIni().getIdInicial()))) {
				if (sb.length() > 0)
					sb.append(", ");
				sb.append(mar.getCpMarcador().getDescrMarcador());
			}
		}
		// Marcacoes para a propria lotacao e para outra pessoa
		if (sb.length() == 0) {
			for (SrMarca mar : marcas) {
				if (sb.length() > 0)
					sb.append(", ");
				if ((mar.getDpLotacaoIni() != null && lota.getIdInicial()
						.equals(mar.getDpLotacaoIni().getIdInicial()))
						&& (mar.getDpPessoaIni() != null && !pess
								.getIdInicial().equals(
										mar.getDpPessoaIni().getIdInicial()))) {
					sb.append(mar.getCpMarcador().getDescrMarcador());
					sb.append(" [");
					sb.append(mar.getDpPessoaIni().getSigla());
					sb.append("]");
				}
			}
		}
		// Marcacoes para qualquer outra pessoa ou lotacao
		if (sb.length() == 0) {
			for (SrMarca mar : marcas) {
				if (sb.length() > 0)
					sb.append(", ");
				sb.append(mar.getCpMarcador().getDescrMarcador());
				if (mar.getDpLotacaoIni() != null
						|| mar.getDpPessoaIni() != null) {
					sb.append(" [");
					if (mar.getDpLotacaoIni() != null) {
						sb.append(mar.getDpLotacaoIni().getSigla());
					}
					if (mar.getDpPessoaIni() != null) {
						if (mar.getDpLotacaoIni() != null) {
							sb.append(", ");
						}
						sb.append(mar.getDpPessoaIni().getSigla());
					}
					sb.append("]");
				}
			}
		}
		if (sb.length() == 0)
			return null;
		return sb.toString();
	}

	public java.util.List<SrMarca> getMarcas() {
		return marcas;
	}

	public void setMarcas(java.util.List<SrMarca> marcas) {
		this.marcas = marcas;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean considerarcancelados() {
		return false;
	}

	// Usado aqui pela própria solicitação
	public void Movimentar(SrEstado estado, String descricao,
			DpLotacao lotaAtendente, SrTipoMovimentacao idTipoMov, Long numSeqMov) throws Exception {
		Movimentar(estado, descricao, null, lotaAtendente, this.cadastrante,
				this.lotaCadastrante, null, idTipoMov, numSeqMov);
	}
	
	//Usado por Application
	public void Movimentar(SrMovimentacao movimentacao, DpPessoa cadastrante,
			DpLotacao lotaCadastrante, SrTipoMovimentacao idTipoMov, Long numSeqMov ) throws Exception {
		movimentacao.cadastrante = cadastrante;
		movimentacao.lotaCadastrante = lotaCadastrante;
		movimentacao.tipoMov = SrTipoMovimentacao.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO);
		numSeqMov = movimentacao.getnumSequencia();
		//prioridade = movimentacao.solicitacao.listas.set(index, element);
		Movimentar(movimentacao);
	}	
	
	public void Movimentar(SrEstado estado, String descricao,
			DpPessoa atendente, DpLotacao lotaAtendente, DpPessoa cadastrante,
			DpLotacao lotaCadastrante, SrLista lista, SrTipoMovimentacao idTipoMov, Long numSeqMov) throws Exception {
		//modificado com numsequencia
		Long prioridade = null;
		if (lista != null){
			prioridade = lista.setSolicOrd();
		}
		Movimentar(new SrMovimentacao(estado, descricao, atendente,
                 lotaAtendente, cadastrante, lotaCadastrante, this, lista, idTipoMov, numSeqMov, prioridade ));
}
	
	// Todos os métodos Movimentar caem aqui
	public void Movimentar(SrMovimentacao movimentacao) { // throws Exception {
		try {
			movimentacao.salvar();
			refresh();
			atualizarMarcas(movimentacao.solicitacao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!movimentacao.isPrimeiraMovimentacao()
				&& formaAcompanhamento != SrFormaAcompanhamento.NUNCA
				&& !(formaAcompanhamento == SrFormaAcompanhamento.FECHAMENTO
						&& movimentacao.estado != SrEstado.FECHADO && movimentacao.estado != SrEstado.POS_ATENDIMENTO))
			movimentacao.notificar();
	}

	public List<SrLista> getListaDisponivel() {
		ArrayList<SrLista> listaCompleta = new ArrayList<SrLista>();
		for (SrMovimentacao mov : getMovimentacaoSet()) {
			if (mov.lista != null)
				listaCompleta.add(mov.lista);
		}
		SrLista lista = new SrLista();
		ArrayList<SrLista> listaTodos = new ArrayList<SrLista>();
		for (SrLista listas : lista.getListaSet()) {
			if (listas != null)
				listaTodos.add(listas);
		}
		ArrayList<SrLista> listaDisponiveis = new ArrayList<SrLista>(listaTodos);
		listaDisponiveis.removeAll(listaCompleta);
		return listaDisponiveis;
	}

	public List<SrLista> getListaAssociada() {
		ArrayList<SrLista> listaCompleta = new ArrayList<SrLista>();
		for (SrMovimentacao mov : getMovimentacaoSet(false)) {
			if (mov.lista != null)
				listaCompleta.add(mov.lista);
		}
		return listaCompleta;
	}
	
	
	public void desassociarLista(SrSolicitacao solicitacao, SrLista lista) throws Exception {
		SrSolicitacao sol = new SrSolicitacao();
		sol.meuMovimentacaoSet = solicitacao.getMovimentacaoSet();
		SrMovimentacao movIncl = (SrMovimentacao) getMovimentacaoSolLista(solicitacao, lista);
		SrMovimentacao mov = new SrMovimentacao();
		mov.cadastrante = solicitacao.cadastrante;
		mov.lotaCadastrante = solicitacao.lotaCadastrante;
		mov.tipoMov = SrTipoMovimentacao.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_INCLUSAO_LISTA);
		mov.descrMovimentacao = "Cancelamento de Inclusão em Lista";
		mov.lista = null;
		mov.prioridade = getMovimentacaoSolLista(solicitacao, lista).prioridade;
		mov.solicitacao = solicitacao;
		mov.numSequencia = solicitacao.getNumSeqMov();
		mov.idMovRef = movIncl;
		solicitacao.Movimentar(mov);
		movIncl.dtCancelamento = new Date();
		movIncl.prioridade = null;
		movIncl.idmovCanceladora = mov;
		movIncl.save();
		//JPA.em().flush();
		//JPA.em().getTransaction().commit();
		sol.meuMovimentacaoSet.add(mov);
		sol.meuMovimentacaoSet.add(movIncl);
	}

	public String getGcTags() {
		String s = "tags=@servico";
		if (servico != null)
			s += servico.getGcTags();
		if (itemConfiguracao != null)
			s += itemConfiguracao.getGcTags();
		return s;
	}

}
