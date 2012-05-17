package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import play.db.jpa.GenericModel;
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
	public DpLotacao orgaoUsuario;

	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO_PAI")
	public SrSolicitacao solcitacaoPai;

	@ManyToOne
	@JoinColumn(name = "ID_FORMA_ACOMPANHAMENTO")
	public SrFormaAcompanhamento formaAcompanhamento;

	@ManyToOne
	@JoinColumn(name = "ID_ITEM_CONFIGURACAO")
	public SrItemConfiguracao itemConfiguracao;

	// @ManyToMany(targetEntity=SrArquivo.class, cascade={ CascadeType.PERSIST }
	// )
	// @JoinTable(name="tblUserPermission",
	// @JoinColumn(name = "ID_ARQUIVO")
	// public SrArquivo arquivo;

	@ManyToOne
	@JoinColumn(name = "ID_SERVICO")
	public SrServico servico;

	@Column(name = "DESCRICAO")
	public String descrSolicitacao;

	@Column(name = "URGENCIA")
	public String urgencia;

	@Column(name = "DT_REG")
	public Date dtReg;

	public String local;

	@Column(name = "TEL_PRINCIPAL")
	public String telPrincipal;

	@Column(name = "MOTIVO_FECHAMENTO_ABERTURA")
	public String motivoFechamentoAbertura;

	@ManyToMany(cascade = CascadeType.PERSIST)
	public List<SrAtributo> atributoSet;

	@ManyToMany(cascade = CascadeType.ALL)
	public List<SrMarca> marcaSet;

	@ManyToMany(cascade = CascadeType.ALL)
	public List<SrAndamento> andamentoSet;

	public SrSolicitacao(DpPessoa solicitante, DpLotacao lotaSolicitante,
			DpPessoa cadastrante, DpLotacao lotaCadastrante,
			SrSolicitacao solcitacaoPai,
			SrFormaAcompanhamento formaAcompanhamento,
			SrItemConfiguracao itemConfiguracao, SrServico servico,
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
		this.descrSolicitacao = dscrSolcitacao;
		this.local = local;
		this.telPrincipal = telPrincipal;
		this.motivoFechamentoAbertura = motivoFechamentoAbertura;
	}

	public String getCodigo() {
		return "RJ-SOL-2012/0000" + idSolicitacao;
	}

}
