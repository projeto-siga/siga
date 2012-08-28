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
public class SrSolicitacao extends GenericModel implements HistoricoAuditavel {

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

	// Não entendi o porquê de ser necessário o insertable e o updatable. Ocorre
	// erro de ID repetido
	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO_PAI", insertable = false, updatable = false)
	public SrSolicitacao solcitacaoPai;

	@Column(name = "FORMA_ACOMPANHAMENTO")
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

	@Column(name = "FECHAMENTO_ABERTURA")
	@Type(type = "yes_no")
	public boolean fechamentoAbertura;

	@Column(name = "MOTIVO_FECHAMENTO_ABERTURA")
	public String motivoFechamentoAbertura;

	@Column(name = "NUM_SOLICITACAO")
	public Long numSolicitacao;

	@Column(name = "ANO_EMISSAO")
	public Integer anoEmissao;

	@Column(name = "NUM_SEQUENCIA")
	public Long numSequencia;

	@ManyToOne()
	@JoinColumn(name = "ID_SOLICITACAO_PAI")
	public SrSolicitacao solicitacaoPai;

	@ManyToMany(cascade = CascadeType.PERSIST)
	public List<SrAtributo> atributoSet;

	@OneToMany(mappedBy = "solicitacao", cascade = CascadeType.PERSIST)
	// O where abaixo teve de ser explícito porque os id_refs conflitam, e
	// o Hibernate, por estranho que pareça, não consegue retornar os
	// registros corretos
	@Where(clause = "ID_TP_MARCA=2")
	public List<SrMarca> marcaSet;

	@OneToMany(mappedBy = "solicitacao", cascade = CascadeType.PERSIST)
	@OrderBy("idAndamento DESC")
	public List<SrAndamento> andamentoSet;

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

		if (solicitacaoPai != null)
			return solicitacaoPai.getCodigo()
					+ (numSequencia < 10 ? ".0" : ".")
					+ numSequencia.toString();

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
		if (andamentoSet != null)
			for (SrAndamento andamento : andamentoSet)
				return andamento;
		return null;
	}

	public String getSituacao() {
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

	public boolean podeAlterarAtendente(DpLotacao lota, DpPessoa pess) {

		return ((getUltimoAndamento().atendente != null && getUltimoAndamento().atendente
				.equivale(pess)) || getUltimoAndamento().lotaAtendente
				.equivale(lota));
	}

	public boolean houveAndamentos() {
		return getUltimoAndamento() != null;
	}

	public Long buscarProximoNumero() {
		if (orgaoUsuario == null)
			return 0L;
		Long num = find(
				"select max(numSolicitacao)+1 from SrSolicitacao where orgaoUsuario.idOrgaoUsu = "
						+ orgaoUsuario.getIdOrgaoUsu()).first();
		return (num != null) ? num : 1;
	}

	public String getAnoEmissao() {
		if (dtReg == null)
			return null;
		return new SimpleDateFormat("yyyy").format(dtReg);
	}
}
