package models;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;

import net.spy.memcached.protocol.GetCallbackWrapper;
import notifiers.Correio;

import org.hibernate.annotations.Where;

import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import controllers.SrCalendar;
import controllers.Util;

@Entity
@Table(name = "SR_SOLICITACAO")
public class SrSolicitacao extends ObjetoPlayComHistorico implements
		SrSelecionavel {

	@Id
	@GeneratedValue
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

	@ManyToOne()
	@JoinColumn(name = "HIS_ID_INI", insertable = false, updatable = false)
	public SrSolicitacao solicitacaoInicial;

	@OneToMany(targetEntity = SrAtributo.class, mappedBy = "solicitacao", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	protected List<SrAtributo> meuAtributoSet;

	// Edson: O where abaixo teve de ser explÃ­cito porque os id_refs conflitam,
	// e
	// o Hibernate, por estranho que pareÃ§a, nÃ£o consegue retornar os
	// registros corretos
	@OneToMany(mappedBy = "solicitacao", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@Where(clause = "ID_TP_MARCA=2")
	protected Set<SrMarca> meuMarcaSet;

	@OneToMany(targetEntity = SrAndamento.class, mappedBy = "solicitacao", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@OrderBy("idAndamento DESC")
	protected Set<SrAndamento> meuAndamentoSet;

	@OneToMany(mappedBy = "solicitacaoPai", cascade = CascadeType.PERSIST)
	@OrderBy("numSequencia asc")
	protected Set<SrSolicitacao> meuSolicitacaoFilhaSet;

	@Transient
	private DpLotacao cachePreAtendenteDesignado;

	@Transient
	private DpLotacao cacheAtendenteDesignado;

	@Transient
	private DpLotacao cachePosAtendenteDesignado;

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
				.compile("^([A-Za-z0-9]{2})?-?(SR)?-?(?:([0-9]{4})/?)??([0-9]{1,5})?$");
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
			} else this.dtReg = new Date();

			if (m.group(4) != null)
				numSolicitacao = Long.valueOf(m.group(4));
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
		query += " and numSolicitacao = " + numSolicitacao;
		SrSolicitacao sol = (SrSolicitacao) JPA.em().createQuery(query)
				.getSingleResult();
		return sol;
	}

	@Override
	public List<? extends SrSelecionavel> buscar() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCodigo() {

		if (solicitacaoPai != null)
			return solicitacaoPai.getCodigo() + "." + getNumSequenciaString();

		String numString = numSolicitacao.toString();

		for (int i = 0; i < 4 - ((int) Math.floor(numSolicitacao / 10)); i++)
			numString = "0" + numString;

		return orgaoUsuario.getSiglaOrgaoUsu() + "-SR-" + getAnoEmissao() + "/"
				+ numString;
	}

	public String getDtRegString() {
		SrCalendar cal = new SrCalendar();
		cal.setTime(dtReg);
		return cal.getTempoTranscorridoString();
	}

	public String getAtributosString() {
		String s = "";
		for (SrAtributo att : getAtributoSet()) {
			s += att.tipoAtributo.nomeTipoAtributo + ": " + att.valorAtributo
					+ ". ";
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

	public List<SrSolicitacao> getHistoricoSolicitacao() {
		if (getHisIdIni() == null)
			return null;
		return find(
				"from SrSolicitacao where hisIdIni = " + getHisIdIni()
						+ " order by idSolicitacao desc").fetch();
	}

	public SrSolicitacao getSolicitacaoAtual() {
		List<SrSolicitacao> sols = getHistoricoSolicitacao();
		if (sols == null)
			return null;
		return sols.get(0);
	}

	public List<SrAndamento> getAndamentoSet() {
		return getAndamentoSet(false);
	}

	// Edson: futuramente, talvez trazer os do histórico inteiro
	// da solicitação, tal como o getAndamentoSet()
	public List<SrAtributo> getAtributoSet() {
		if (meuAtributoSet == null)
			return new ArrayList<SrAtributo>();
		return meuAtributoSet;
	}

	public List<SrAndamento> getAndamentoSetComCancelados() {
		return getAndamentoSet(true);
	}

	public List<SrAndamento> getAndamentoSet(boolean considerarCancelados) {
		if (getHisIdIni() == null)
			return null;
		ArrayList<SrAndamento> listaCompleta = new ArrayList<SrAndamento>();
		for (SrSolicitacao sol : getHistoricoSolicitacao())
			if (sol.meuAndamentoSet != null)
				if (considerarCancelados)
					listaCompleta.addAll(sol.meuAndamentoSet);
				else
					for (SrAndamento andamento : sol.meuAndamentoSet)
						if (!andamento.isCancelado())
							listaCompleta.add(andamento);

		return listaCompleta;
	}

	public SrAndamento getUltimoAndamento() {
		if (getAndamentoSet() == null)
			return null;
		for (SrAndamento andamento : getAndamentoSet())
			return andamento;
		return null;
	}

	public DpLotacao getLotaAtendente() {
		return getUltimoAndamento().lotaAtendente;
	}

	public DpPessoa getAtendente() {
		return getUltimoAndamento().atendente;
	}

	public String getSituacaoString() {
		return getUltimoAndamento().getSituacaoString();
	}

	public SrEstado getEstado() {
		return getUltimoAndamento().estado;
	}

	public boolean temAndamento() {
		return getAndamentoSetComCancelados() != null
				&& getAndamentoSetComCancelados().size() > 0;
	}

	public DpLotacao getPreAtendenteDesignado() throws Exception {
		if (solicitante == null)
			return null;
		if (cachePreAtendenteDesignado == null) {
			SrConfiguracao conf = Util.getConfiguracao(solicitante,
					itemConfiguracao, servico,
					CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
					SrSubTipoConfiguracao.DESIGNACAO_PRE_ATENDENTE);
			if (conf != null)
				cachePreAtendenteDesignado = conf.preAtendente;
		}
		return cachePreAtendenteDesignado;
	}

	public DpLotacao getAtendenteDesignado() throws Exception {
		if (solicitante == null)
			return null;
		if (cacheAtendenteDesignado == null) {
			SrConfiguracao conf = Util.getConfiguracao(solicitante,
					itemConfiguracao, servico,
					CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
					SrSubTipoConfiguracao.DESIGNACAO_ATENDENTE);
			if (conf != null)
				cacheAtendenteDesignado = conf.atendente;
		}
		return cacheAtendenteDesignado;
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

		for (SrConfiguracao conf : Util.getConfiguracoes(solicitante,
				itemConfiguracao, servico,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO,
				null)) {
			if (conf.tipoAtributo.getHisDtFim() == null
					&& !listaFinal.contains(conf.tipoAtributo)) {
				listaFinal.add(conf.tipoAtributo);
				if (map != null)
					map.put(conf.tipoAtributo.idTipoAtributo,
							conf.atributoObrigatorio);
			}
		}
		return listaFinal;
	}

	public DpLotacao getPosAtendenteDesignado() throws Exception {
		if (solicitante == null)
			return null;
		if (cachePosAtendenteDesignado == null) {
			SrConfiguracao conf = Util.getConfiguracao(solicitante,
					itemConfiguracao, servico,
					CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO,
					SrSubTipoConfiguracao.DESIGNACAO_POS_ATENDENTE);
			if (conf != null)
				cachePosAtendenteDesignado = conf.posAtendente;
		}
		return cachePosAtendenteDesignado;
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
			if (sol.meuMarcaSet != null)
				listaCompleta.addAll(sol.meuMarcaSet);
		}
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
		return idSolicitacao != getHisIdIni();
	}

	public boolean isCancelado() {
		return temAndamento()
				&& getUltimoAndamento().estado == SrEstado.CANCELADO;
	}

	public boolean isFechado() {
		return temAndamento()
				&& getUltimoAndamento().estado == SrEstado.FECHADO;
	}

	public boolean isEmPosAtendimento() {
		return temAndamento()
				&& getUltimoAndamento().estado == SrEstado.POS_ATENDIMENTO;
	}

	public boolean isEmPreAtendimento() {
		return temAndamento()
				&& getUltimoAndamento().estado == SrEstado.PRE_ATENDIMENTO;
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

	public boolean temAtendenteDesignado() throws Exception {
		return (getAtendenteDesignado() != null);
	}

	public boolean estaCom(DpLotacao lota, DpPessoa pess) {

		return temAndamento()
				&& ((getUltimoAndamento().atendente != null && getUltimoAndamento().atendente
						.equivale(pess)) || getUltimoAndamento().lotaAtendente
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
		return estaCom(lota, pess) && isEmAtendimento();
	}

	public boolean podeDesfazerAndamento(DpLotacao lota, DpPessoa pess) {
		SrAndamento ultimoAndamento = getUltimoAndamento();
		return ultimoAndamento.lotaCadastrante.equivale(lota)
				&& !ultimoAndamento.isPrimeiroAndamento();
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

		if (!temAndamento()) {
			if (fecharAoAbrir)
				iniciarFechando();
			else
				iniciar();
			if (!isEditado())
				notificar();
		}
		// return this;
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
			gravidade = SrGravidade.POUCO_GRAVE;

		if (urgencia == null)
			urgencia = SrUrgencia.PODE_ESPERAR;

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
			darAndamento(SrEstado.PRE_ATENDIMENTO,
					"Iniciando pré-atendimento...", getPreAtendenteDesignado());
		else
			darAndamento(SrEstado.ANDAMENTO, "Iniciando atendimento...",
					getAtendenteDesignado());
	}

	public void iniciarFechando() throws Exception {
		darAndamento(SrEstado.FECHADO, motivoFechamentoAbertura,
				getPosAtendenteDesignado());
	}

	// Usado aqui pela própria solicitação
	public void darAndamento(SrEstado estado, String descricao,
			DpLotacao lotaAtendente) throws Exception {

		darAndamento(estado, descricao, null, lotaAtendente, this.cadastrante,
				this.lotaCadastrante);
	}

	public void darAndamento(SrEstado estado, String descricao,
			DpPessoa atendente, DpLotacao lotaAtendente, DpPessoa cadastrante,
			DpLotacao lotaCadastrante) throws Exception {

		darAndamento(new SrAndamento(estado, descricao, atendente,
				lotaAtendente, cadastrante, lotaCadastrante, this));
	}

	// Usado por Application
	public void darAndamento(SrAndamento andamento, DpPessoa cadastrante,
			DpLotacao lotaCadastrante) throws Exception {
		andamento.cadastrante = cadastrante;
		andamento.lotaCadastrante = lotaCadastrante;
		darAndamento(andamento);
	}

	// Todos os darAndamento caem aqui
	public void darAndamento(SrAndamento andamento) throws Exception {
		andamento.salvar();
		// Edson: O refresh é necessário para o hibernate incluir o novo
		// andamento na
		// coleção de andamentos desta solicitação
		refresh();
		atualizarMarcas();
		if (!andamento.isPrimeiroAndamento()
				&& !(formaAcompanhamento == SrFormaAcompanhamento.FECHAMENTO
						&& andamento.estado != SrEstado.FECHADO && andamento.estado != SrEstado.POS_ATENDIMENTO))
			andamento.notificar();
	}

	public void desfazerUltimoAndamento(DpPessoa cadastrante,
			DpLotacao lotaCadastrante) throws Exception {
		SrAndamento andamento = getUltimoAndamento();
		andamento.desfazer(cadastrante, lotaCadastrante);
		refresh();
		atualizarMarcas();
		if (!(formaAcompanhamento == SrFormaAcompanhamento.FECHAMENTO
				&& andamento.estado != SrEstado.FECHADO && andamento.estado != SrEstado.POS_ATENDIMENTO))
			andamento.notificar();
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

	public void atualizarMarcas() throws Exception {

		removerMarcas();

		Long marcador;
		SrAndamento andamento = getUltimoAndamento();

		if (andamento.estado == SrEstado.FECHADO)
			marcador = CpMarcador.MARCADOR_SOLICITACAO_FECHADO;
		else if (andamento.estado == SrEstado.PENDENTE)
			marcador = CpMarcador.MARCADOR_SOLICITACAO_PENDENTE;
		else if (andamento.estado == SrEstado.CANCELADO)
			marcador = CpMarcador.MARCADOR_SOLICITACAO_CANCELADO;
		else if (andamento.estado == SrEstado.PRE_ATENDIMENTO)
			marcador = CpMarcador.MARCADOR_SOLICITACAO_PRE_ATENDIMENTO;
		else if (andamento.estado == SrEstado.POS_ATENDIMENTO)
			marcador = CpMarcador.MARCADOR_SOLICITACAO_POS_ATENDIMENTO;
		else
			marcador = CpMarcador.MARCADOR_SOLICITACAO_EM_ANDAMENTO;
		System.out.println(andamento.lotaAtendente.getNomeLotacao());
		marcar(marcador, andamento.lotaAtendente, andamento.atendente);

		if (!isFechado() && !isCancelado()) {
			marcar(CpMarcador.MARCADOR_SOLICITACAO_COMO_CADASTRANTE,
					lotaCadastrante, cadastrante);
			marcar(CpMarcador.MARCADOR_SOLICITACAO_COMO_SOLICITANTE,
					lotaSolicitante, solicitante);
		}

	}

	public void removerMarcas() {
		for (SrMarca marca : getMarcaSet())
			JPA.em().remove(marca);
	}

	public void marcar(Long marcador, DpLotacao lotacao, DpPessoa pessoa)
			throws Exception {

		new SrMarca(marcador, pessoa, lotacao, this).save();
	}

}
