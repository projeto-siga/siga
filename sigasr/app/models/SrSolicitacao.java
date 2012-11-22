package models;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.spy.memcached.protocol.GetCallbackWrapper;
import notifiers.Correio;

import org.hibernate.annotations.Where;

import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import controllers.SrCalendar;

@Entity
@Table(name = "SR_SOLICITACAO")
public class SrSolicitacao extends ObjetoPlayComHistorico {

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

	public String local;

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

	@ManyToMany(cascade = CascadeType.PERSIST)
	protected List<SrAtributo> meuAtributoSet;

	// O where abaixo teve de ser explÃ­cito porque os id_refs conflitam, e
	// o Hibernate, por estranho que pareÃ§a, nÃ£o consegue retornar os
	// registros corretos
	@OneToMany(mappedBy = "solicitacao", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@Where(clause = "ID_TP_MARCA=2")
	protected Set<SrMarca> meuMarcaSet;

	@OneToMany(mappedBy = "solicitacao", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@OrderBy("idAndamento DESC")
	protected Set<SrAndamento> meuAndamentoSet;

	@OneToMany(mappedBy = "solicitacaoPai", cascade = CascadeType.PERSIST)
	protected List<SrSolicitacao> meuSolicitacaoFilhaSet;

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
			String dscrSolcitacao, String local, String telPrincipal,
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

	public String getCodigo() {

		if (solicitacaoPai != null)
			return solicitacaoPai.getCodigo() + "." + getNumSequenciaString();

		String numString = numSolicitacao.toString();

		for (int i = 0; i < 4 - ((int) Math.floor(numSolicitacao / 10)); i++)
			numString = "0" + numString;

		return orgaoUsuario.getSiglaOrgaoUsu() + "-SS-" + getAnoEmissao() + "/"
				+ numString;
	}

	public String getDtRegString() {
		SrCalendar cal = new SrCalendar();
		cal.setTime(dtReg);
		return cal.getTempoTranscorridoString();
	}

	// Necessário porque não há binder para arquivo
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

	public SrSolicitacao getSolicitacaoIniciai() {
		if (getHisIdIni() != null)
			return findById(getHisIdIni());
		return null;
	}

	public List<SrAndamento> getAndamentoSet() {
		return getAndamentoSet(false);
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
						if (!andamento.isCancelado()
								&& !andamento.isCancelador())
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

	public String getSituacao() {
		return getUltimoAndamento().getSituacao();
	}

	public boolean temAndamento() {
		return getAndamentoSetComCancelados() != null
				&& getAndamentoSetComCancelados().size() > 0;
	}

	public boolean temMaisDeUmAndamento() {
		return getAndamentoSetComCancelados() != null
				&& getAndamentoSetComCancelados().size() > 1;

	}

	public DpLotacao getPreAtendenteDesignado() throws Exception {
		if (cachePreAtendenteDesignado == null) {
			SrConfiguracao conf = getConfiguracao(itemConfiguracao, servico,
					CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO);
			if (conf != null)
				cachePreAtendenteDesignado = conf.preAtendente;
		}
		return cachePreAtendenteDesignado;
	}

	public DpLotacao getAtendenteDesignado() throws Exception {
		if (cacheAtendenteDesignado == null) {
			SrConfiguracao conf = getConfiguracao(itemConfiguracao, servico,
					CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO);
			if (conf != null)
				cacheAtendenteDesignado = conf.atendente;
		}
		return cacheAtendenteDesignado;
	}

	public DpLotacao getPosAtendenteDesignado() throws Exception {
		if (cachePosAtendenteDesignado == null) {
			SrConfiguracao conf = getConfiguracao(itemConfiguracao, servico,
					CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO);
			if (conf != null)
				cachePosAtendenteDesignado = conf.posAtendente;
		}
		return cachePosAtendenteDesignado;
	}

	public List<SrAtributo> getAtributoSet() {
		if (getHisIdIni() == null)
			return null;
		ArrayList<SrAtributo> listaCompleta = new ArrayList<SrAtributo>();
		for (SrSolicitacao sol : getHistoricoSolicitacao()) {
			if (sol.meuAtributoSet != null)
				listaCompleta.addAll(sol.meuAtributoSet);
		}
		return listaCompleta;
	}

	public List<SrSolicitacao> getSolicitacaoFilhaSet() {
		if (getHisIdIni() == null)
			return null;
		ArrayList<SrSolicitacao> listaCompleta = new ArrayList<SrSolicitacao>();
		for (SrSolicitacao sol : getHistoricoSolicitacao()) {
			if (sol.meuSolicitacaoFilhaSet != null)
				listaCompleta.addAll(sol.meuSolicitacaoFilhaSet);
		}
		return listaCompleta;
	}

	public List<SrEstado> getEstadosSelecionaveis() {
		List<SrEstado> listaFinal = new ArrayList<SrEstado>();
		for (SrEstado e : SrEstado.values()) {
			if (isEmPreAtendimento() && e == SrEstado.PENDENTE)
				continue;
			if (!isEmPreAtendimento() && e == SrEstado.PRE_ATENDIMENTO)
				continue;
			listaFinal.add(e);
		}
		return listaFinal;
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

	public boolean isFechado() {
		return temAndamento()
				&& getUltimoAndamento().estado == SrEstado.FECHADO;
	}

	public boolean isEmPreAtendimento() {
		return temAndamento()
				&& getUltimoAndamento().estado == SrEstado.PRE_ATENDIMENTO;
	}

	public boolean isEmAtendimento() {
		return !isEmPreAtendimento() && !isFechado();
	}

	public boolean temPreAtendente() throws Exception {
		return (getPreAtendenteDesignado() != null);
	}

	public boolean estaCom(DpLotacao lota, DpPessoa pess) {

		return temAndamento()
				&& ((getUltimoAndamento().atendente != null && getUltimoAndamento().atendente
						.equivale(pess)) || getUltimoAndamento().lotaAtendente
						.equivale(lota));
	}

	public boolean podeCriarFilha(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess) && solicitacaoPai == null
				&& isEmAtendimento();
	}

	public boolean podeDesfazerAndamento(DpLotacao lota, DpPessoa pess) {
		return getUltimoAndamento().lotaCadastrante.equivale(lota)
				&& temMaisDeUmAndamento();
	}

	public boolean podeEditar(DpLotacao lota, DpPessoa pess) {
		return isEmPreAtendimento() && estaCom(lota, pess);
	}

	public boolean podeTrocarAtendente(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess) && !isEmPreAtendimento();
	}

	public boolean podeTrocarSituacao(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess);
	}

	public boolean podePriorizar(DpLotacao lota, DpPessoa pess) {
		return estaCom(lota, pess) && isEmPreAtendimento();
	}

	public boolean podeAbrirJaFechando(DpLotacao lota, DpPessoa pess) {
		return false;
	}

	@Override
	public SrSolicitacao save() {
		try {
			salvar();
		} catch (Exception e) {
			//
		}
		return this;
	}

	public SrSolicitacao salvar(DpPessoa cadastrante, DpLotacao lotaCadastrante)
			throws Exception {
		this.cadastrante = cadastrante;
		this.lotaCadastrante = lotaCadastrante;
		return salvar();
	}

	public SrSolicitacao salvar() throws Exception {

		checarCampos();

		super.salvar();

		if (!temAndamento()) {
			if (fecharAoAbrir)
				iniciarFechando();
			else
				iniciar();
			notificar();
		}
		return this;
	}

	public void checarCampos() throws Exception {

		if (cadastrante == null)
			throw new Exception("Cadastrante nÃ£o pode ser nulo");

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
	}

	public void iniciar() throws Exception {
		if (temPreAtendente())
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

	public void darAndamento(SrEstado estado, String descricao,
			DpLotacao lotaAtendente) throws Exception {

		darAndamento(estado, descricao, null, lotaAtendente, this.cadastrante,
				this.lotaCadastrante);
	}

	public void darAndamento(SrEstado estado, String descricao,
			DpPessoa atendente, DpLotacao lotaAtendente, DpPessoa cadastrante,
			DpLotacao lotaCadastrante) throws Exception {

		new SrAndamento(estado, descricao, atendente, lotaAtendente,
				cadastrante, lotaCadastrante, this).salvar();
	}

	public void notificar() {
		Correio.notificarAbertura(this);
	}

	public void atualizarMarcas() {

		removerMarcas();

		marcar(getUltimoAndamento());

	}

	public void removerMarcas() {
		for (SrMarca marca : getMarcaSet())
			JPA.em().remove(marca);
	}

	public void marcar(SrAndamento andamento) {

		Long marcador;

		if (andamento.estado == SrEstado.FECHADO)
			marcador = CpMarcador.MARCADOR_SOLICITACAO_FECHADO;
		else if (andamento.estado == SrEstado.PENDENTE)
			marcador = CpMarcador.MARCADOR_SOLICITACAO_PENDENTE;
		else if (andamento.estado == SrEstado.CANCELADO)
			marcador = CpMarcador.MARCADOR_SOLICITACAO_CANCELADO;
		else if (andamento.estado == SrEstado.PRE_ATENDIMENTO)
			marcador = CpMarcador.MARCADOR_SOLICITACAO_PRE_ATENDIMENTO;
		else
			marcador = CpMarcador.MARCADOR_SOLICITACAO_A_RECEBER;

		marcar(marcador, andamento.lotaAtendente, andamento.atendente);

	}

	public void marcar(Long marcador, DpLotacao lotacao, DpPessoa pessoa) {

		new SrMarca(marcador, pessoa, lotacao, this).save();
	}

	@Override
	public Long getId() {
		return idSolicitacao;
	}

	@Override
	public void setId(Long id) {
		this.idSolicitacao = id;
	}

}
