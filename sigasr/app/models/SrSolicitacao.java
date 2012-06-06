package models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import controllers.SrCalendar;

import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(name = "SR_SOLICITACAO")
public class SrSolicitacao extends GenericModel {

	@Id
	@GeneratedValue
	@Column(name = "ID_SOLICITACAO")
	public long idSolicitacao;

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
	public SrSolicitacao solcitacaoPai;

	@Column(name = "FORMA_ACOMPANHAMENTO")
	public SrFormaAcompanhamento formaAcompanhamento;

	@ManyToOne
	@JoinColumn(name = "ID_ITEM_CONFIGURACAO")
	public SrItemConfiguracao itemConfiguracao;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "ID_ARQUIVO")
	public SrArquivo arquivo;

	@ManyToOne
	@JoinColumn(name = "ID_SERVICO")
	public SrServico servico;

	@Column(name = "DESCRICAO")
	public String descrSolicitacao;

	@Column(name = "GRAVIDADE")
	public SrGravidade gravidade;

	@Column(name = "TENDENCIA")
	public SrTendencia tendencia;

	@Column(name = "URGENCIA")
	public SrUrgencia urgencia;

	@Column(name = "DT_REG")
	@Temporal(TemporalType.TIMESTAMP)
	public Date dtReg;

	public String local;

	@Column(name = "TEL_PRINCIPAL")
	public String telPrincipal;

	@Column(name = "MOTIVO_FECHAMENTO_ABERTURA")
	public String motivoFechamentoAbertura;

	@ManyToMany(cascade = CascadeType.PERSIST)
	public List<SrAtributo> atributoSet;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "solicitacao")
	@Where(clause = "ID_TP_MARCA=2")
	public List<SrMarca> marcaSet;

	@OneToMany(mappedBy = "solicitacao")
	@OrderBy("idAndamento DESC")
	public List<SrAndamento> andamentoSet;

	public SrSolicitacao() {
		andamentoSet = new ArrayList<SrAndamento>();
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
		this.solcitacaoPai = solcitacaoPai;
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
		return "RJ-SR-2012/0000" + idSolicitacao;
	}

	public String getDtRegString() {
		SrCalendar cal = new SrCalendar();
		cal.setTime(dtReg);
		return cal.getTempoTranscorridoString();
	}

	public int getGUT() {
		return gravidade.nivelGravidade * urgencia.nivelUrgencia
				* tendencia.nivelTendencia;
	}

	public SrAndamento getUltimoAndamento() {
		for (SrAndamento andamento : andamentoSet)
			return andamento;
		return null;
	}
	
	public String getSituacao(){
		return getUltimoAndamento().estado.descrEstado;
	}

	public SrAndamento getAndamentoAnterior(SrAndamento ref) {
		boolean retorna = false;
		for (SrAndamento andamento : andamentoSet) {
			if (retorna)
				return andamento;
			if (andamento == ref)
				retorna = true;
		}
		return null;
	}

	public void criar() {

		if (lotaCadastrante == null)
			lotaCadastrante = cadastrante.getLotacao();
		if (solicitante != null && lotaSolicitante == null)
			lotaSolicitante = solicitante.getLotacao();
		else
			lotaSolicitante = lotaCadastrante;

		// substituir isso pelo cascade
		this.save();

		SrAndamento andamento = new SrAndamento();
		andamento.descrAndamento = "Criação da solicitação";
		andamento.cadastrante = cadastrante;
		andamento.lotaCadastrante = lotaCadastrante;
		andamento.estado = SrEstado.ANDAMENTO;

		// Antes de existir configuração pro primeiro antendente...
		DpPessoa eeh = JPA.em().find(DpPessoa.class, 10374L);
		andamento.lotaAtendente = eeh.getLotacao();
		darAndamento(andamento);
	}

	public void darAndamento(SrAndamento andamento) {
		andamento.solicitacao = this;
		andamento.dtReg = new Date();

		DpPessoa eeh = JPA.em().find(DpPessoa.class, 10374L);
		// Enquanto não há login...

		andamento.cadastrante = eeh;
		andamento.lotaCadastrante = eeh.getLotacao();

		if (andamento.atendente != null && andamento.lotaAtendente == null)
			andamento.lotaAtendente = andamento.atendente.getLotacao();
		if (andamento.lotaAtendente == null)
			andamento.lotaAtendente = getUltimoAndamento().lotaAtendente;
		if (andamento.atendente == null && getUltimoAndamento() != null)
			andamento.atendente = getUltimoAndamento().atendente;

		andamento.save();
		andamentoSet.add(andamento);
		// atualizarMarcas();
	}

	public void marcar(Long idMarcador, DpLotacao lotacao, DpPessoa pessoa) {
		CpMarcador marcador = JPA.em().find(CpMarcador.class, idMarcador);
		marcar(marcador, lotacao, pessoa);
	}

	public void marcar(CpMarcador marcador, DpLotacao lotacao, DpPessoa pessoa) {
		SrMarca marca2 = new SrMarca();
		marca2.setCpMarcador(marcador);
		marca2.setDpLotacaoIni(lotacao.getLotacaoInicial());
		marca2.setDpPessoaIni(pessoa.getPessoaInicial());
		marca2.setDtIniMarca(new Date());
		marca2.solicitacao = this;
		JPA.em().persist(marca2);
	}

	public void atualizarMarcas() {
		/*
		 * SrAndamento ultimoAndamento = getUltimoAndamento();
		 * 
		 * if (ultimoAndamento.isTransferenciaParaLotacao())
		 * marcar(CpMarcador.MARCADOR_SOLICITACAO_A_RECEBER,
		 * ultimoAndamento.lotaAtendente, ultimoAndamento.atendente); else if
		 * (ultimoAndamento.isRecebimentoPorLotacao())
		 * marcar(CpMarcador.MARCADOR_SOLICITACAO_EM_ANDAMENTO,
		 * ultimoAndamento.lotaAtendente, ultimoAndamento.atendente);
		 */
	}

	public boolean podeReceber(DpLotacao lota, DpPessoa pess) {
		/*
		 * return ((getUltimoAndamento().isTransferenciaParaPessoa() &&
		 * getUltimoAndamento().atendente.equivale(pess) ||
		 * (getUltimoAndamento() .isTransferenciaParaLotacao() &&
		 * getUltimoAndamento().lotaAtendente .equivale(lota))));
		 */
		return false;
	}

	public boolean podeAlterarAtendente(DpLotacao lota, DpPessoa pess) {

		return ((getUltimoAndamento().atendente != null && getUltimoAndamento().atendente
				.equivale(pess)) || getUltimoAndamento().lotaAtendente
				.equivale(lota));
	}
}
