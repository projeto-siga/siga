package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;

@Entity
@Table(name = "SR_CONFIGURACAO")
@PrimaryKeyJoinColumn(name = "ID_CONFIGURACAO_SR")
public class SrConfiguracao extends CpConfiguracao {
	
	@Column(name = "FORMA_ACOMPANHAMENTO")
	public SrFormaAcompanhamento formaAcompanhamento;

	@ManyToOne
	@JoinColumn(name = "ID_ITEM_CONFIGURACAO")
	public SrItemConfiguracao itemConfiguracao;

	@ManyToOne
	@JoinColumn(name = "ID_SERVICO")
	public SrServico servico;

	@Column(name = "GRAVIDADE")
	public SrGravidade gravidade;

	@Column(name = "TENDENCIA")
	public SrTendencia tendencia;

	@Column(name = "URGENCIA")
	public SrUrgencia urgencia;

	@ManyToOne
	@JoinColumn(name = "ID_PRE_ATENDENTE")
	public DpLotacao preAtendente;

	@ManyToOne
	@JoinColumn(name = "ID_ATENENTE")
	public DpLotacao atendente;

	@ManyToOne
	@JoinColumn(name = "ID_POS_ATENDENTE")
	public DpLotacao posAtendente;

	@Column(name = "PESQUISA_SATISFACAO")
	@Type(type = "yes_no")
	public boolean pesquisaSatisfacao;
	
	@Transient
	public SrSubTipoConfiguracao subTipoConfig;

	public SrConfiguracao() {

	}

	public SrConfiguracao(SrItemConfiguracao item, SrServico servico,
			CpTipoConfiguracao tipo, SrSubTipoConfiguracao subTipoConfig) {
		this.itemConfiguracao = item;
		this.servico = servico;
		this.setCpTipoConfiguracao(tipo);
		this.subTipoConfig = subTipoConfig;
	}

	public String getPesquisaSatisfacaoString() {
		return pesquisaSatisfacao ? "Sim" : "Não";
	}

}
