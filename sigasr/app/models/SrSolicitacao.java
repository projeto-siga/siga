package models;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
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

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import controllers.SrCalendar;
import controllers.SrDao;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavel;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(name = "SR_SOLICITACAO")
public class SrSolicitacao extends EntidadePlay {

	@Id
	@GeneratedValue
	@Column(name = "ID_SOLICITACAO")
	public Long idSolicitacao;

	@Column(name = "HIS_ID_INI")
	private Long hisIdIni;

	@Column(name = "HIS_DT_INI")
	private Date hisDtIni;

	@Column(name = "HIS_DT_FIM")
	private Date hisDtFim;

	@Column(name = "HIS_ATIVO")
	private int hisAtivo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HIS_IDC_INI")
	private CpIdentidade hisIdcIni;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HIS_IDC_FIM")
	private CpIdentidade hisIdcFim;

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

	@Column(name = "DT_INICIO_ATENDIMENTO")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtInicioAtendimento;

	@Column(name = "DT_FECHAMENTO")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtFimAtendimento;

	@ManyToMany(cascade = CascadeType.PERSIST)
	public List<SrAtributo> atributoSet;

	@OneToMany(mappedBy = "solicitacao", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	// O where abaixo teve de ser explícito porque os id_refs conflitam, e
	// o Hibernate, por estranho que pareça, não consegue retornar os
	// registros corretos
	@Where(clause = "ID_TP_MARCA=2")
	public Set<SrMarca> marcaSet;

	@OneToMany(mappedBy = "solicitacao", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@OrderBy("idAndamento DESC")
	public Set<SrAndamento> andamentoSet;

	@OneToMany(mappedBy = "solicitacaoPai", cascade = CascadeType.PERSIST)
	public List<SrSolicitacao> solicitacaoFilhaSet;

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

	public Long getHisIdIni() {
		return hisIdIni;
	}

	public void setHisIdIni(Long hisIdIni) {
		this.hisIdIni = hisIdIni;
	}

	public Date getHisDtIni() {
		return hisDtIni;
	}

	public void setHisDtIni(Date hisDtIni) {
		this.hisDtIni = hisDtIni;
	}

	public Date getHisDtFim() {
		return hisDtFim;
	}

	public void setHisDtFim(Date hisDtFim) {
		this.hisDtFim = hisDtFim;
	}

	public int getHisAtivo() {
		return hisAtivo;
	}

	public void setHisAtivo(int hisAtivo) {
		this.hisAtivo = hisAtivo;
	}

	public CpIdentidade getHisIdcIni() {
		return hisIdcIni;
	}

	public void setHisIdcIni(CpIdentidade hisIdcIni) {
		this.hisIdcIni = hisIdcIni;
	}

	public CpIdentidade getHisIdcFim() {
		return hisIdcFim;
	}

	public void setHisIdcFim(CpIdentidade hisIdcFim) {
		this.hisIdcFim = hisIdcFim;
	}

	@Override
	public Long getIdInicial() {
		return getHisIdIni();
	}

	@Override
	public boolean equivale(Object other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long getId() {
		return idSolicitacao;
	}

	@Override
	public void setId(Long id) {
		idSolicitacao = id;
	}

	// Necessário porque não há binder para arquivo
	public void setArquivo(File file) {
		this.arquivo = SrArquivo.newInstance(file);
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		// TODO Auto-generated method stub
		return false;
	}

	public int getGUT() {
		return gravidade.nivelGravidade * urgencia.nivelUrgencia
				* tendencia.nivelTendencia;
	}

	public String getGUTPercent() {
		return (int) ((getGUT() / 125.0) * 100) + "%";
	}

	public SrAndamento getUltimoAndamento() {
		if (getHistoricoAndamentoSet() == null)
			return null;
		for (SrAndamento andamento : getHistoricoAndamentoSet())
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

	public String getDtRegDDMMYYYYHHMM() {
		if (dtReg != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return df.format(dtReg);
		}
		return "";
	}

	public SrAndamento getAndamentoAnterior(SrAndamento ref) {
		boolean retorna = false;
		for (SrAndamento andamento : getHistoricoAndamentoSet()) {
			if (retorna)
				return andamento;
			if (andamento == ref)
				retorna = true;
		}
		return null;
	}

	public boolean houveQualquerAndamento() {
		return getHistoricoAndamentoSet(true).size() > 0;
	}

	public boolean houveAndamentosSemContarCriacao() {
		return getHistoricoAndamentoSet(true).size() > 1;

	}

	public Long buscarProximoNumero() {
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

	public SrSolicitacao getSolicitacaoIniciai() {
		if (getHisIdIni() != null)
			return findById(getHisIdIni());
		return null;
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

	// Nao pude chamar de getAndamentoSet porque houve conflito com
	// o atributo de mesmo nome
	public List<SrAndamento> getHistoricoAndamentoSet() {
		return getHistoricoAndamentoSet(false);
	}

	public List<SrAndamento> getHistoricoAndamentoSet(
			boolean considerarCancelados) {
		if (getHisIdIni() == null)
			return null;
		ArrayList<SrAndamento> listaCompleta = new ArrayList<SrAndamento>();
		for (SrSolicitacao sol : getHistoricoSolicitacao())
			if (sol.andamentoSet != null)
				if (considerarCancelados)
					listaCompleta.addAll(sol.andamentoSet);
				else
					for (SrAndamento andamento : sol.andamentoSet)
						if (!andamento.isCancelado()
								&& !andamento.isCancelador())
							listaCompleta.add(andamento);

		return listaCompleta;
	}

	public DpLotacao getPreAtendente() throws Exception {

		return getConfiguracao(itemConfiguracao, servico,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO).preAtendente;
	}

	public DpLotacao getPosAtendente() throws Exception {

		return getConfiguracao(itemConfiguracao, servico,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO).posAtendente;
	}

	public boolean isFechado() {
		return dtFimAtendimento != null;
	}

	public boolean isPreAtendido() {
		return dtInicioAtendimento != null;
	}

	public boolean isEmAtendimento() {
		return isPreAtendido() && !isFechado();
	}

	public boolean temPreAtendente() throws Exception {
		return (getPreAtendente() != null);
	}

	public boolean podeMovimentar(DpLotacao lota, DpPessoa pess) {

		return ((getUltimoAndamento().atendente != null && getUltimoAndamento().atendente
				.equivale(pess)) || getUltimoAndamento().lotaAtendente
				.equivale(lota));
	}

	public boolean podeCriarFilha(DpLotacao lota, DpPessoa pess) {
		return podeMovimentar(lota, pess) && solicitacaoPai == null;
	}

	public boolean podeDesfazerAndamento(DpLotacao lota, DpPessoa pess) {
		return getUltimoAndamento().lotaCadastrante.equivale(lota)
				&& houveAndamentosSemContarCriacao();
	}

	public SrSolicitacao salvar(DpPessoa cadastrante, DpLotacao lotaCadastrante)
			throws Exception {
		this.cadastrante = cadastrante;
		this.lotaCadastrante = lotaCadastrante;
		return salvar();
	}

	public void completarCampos() {
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
			numSolicitacao = buscarProximoNumero();
	}

	public void nova() throws Exception {

		completarCampos();

		super.salvar();

		if (fecharAoAbrir)
			finalizarAtendimento();
		else if (temPreAtendente())
			iniciarPreAtendimento();
		else
			iniciarAtendimento();
	}

	public SrSolicitacao salvar() throws Exception {

		if (idSolicitacao == null)
			nova();
		else
			super.salvar();

		return this;
	}

	public void iniciarPreAtendimento() {

	}

	public void finalizarPreAtendimento() {

	}

	public void iniciarAtendimento() {

	}

	public void finalizarAtendimento() throws Exception {
		darAndamento(SrEstado.FECHADO, motivoFechamentoAbertura,
				getPosAtendente());
	}

	public void darAndamento(SrEstado estado, String descricao,
			DpLotacao lotaAtendente) throws Exception {

		darAndamento(estado, descricao, null, lotaAtendente, cadastrante,
				lotaCadastrante);

	}

	public void darAndamento(SrEstado estado, String descricao,
			DpPessoa atendente, DpLotacao lotaAtendente, DpPessoa cadastrante,
			DpLotacao lotaCadastrante) throws Exception {

		new SrAndamento(estado, descricao, atendente,
				lotaAtendente, cadastrante, lotaCadastrante).salvar();

	}

	public void atualizarMarcas() {

		for (SrSolicitacao sol : getHistoricoSolicitacao())
			sol.removerMarcas();

		marcar(getUltimoAndamento());

	}

	public void marcar(SrAndamento andamento) {

		Long marcador;

		if (andamento.estado == SrEstado.FECHADO)
			marcador = CpMarcador.MARCADOR_SOLICITACAO_FECHADO;
		else if (andamento.estado == SrEstado.PENDENTE)
			marcador = CpMarcador.MARCADOR_SOLICITACAO_PENDENTE;
		else if (andamento.estado == SrEstado.CANCELADO)
			marcador = CpMarcador.MARCADOR_SOLICITACAO_CANCELADO;
		else
			marcador = CpMarcador.MARCADOR_SOLICITACAO_A_RECEBER;

		marcar(marcador, andamento.lotaAtendente, andamento.atendente);

	}

	public void marcar(Long marcador, DpLotacao lotacao, DpPessoa pessoa) {

		new SrMarca(marcador, pessoa, lotacao, this).salvar();
	}

	public void removerMarcas() {
		if (marcaSet != null)
			for (SrMarca marca : marcaSet)
				JPA.em().remove(marca);
	}

}
