package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;


import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import util.Util;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

@Entity
@Table(name = "SR_CONFIGURACAO")
@PrimaryKeyJoinColumn(name = "ID_CONFIGURACAO_SR")
public class SrConfiguracao extends CpConfiguracao implements
		ManipuladorHistorico {

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
	@JoinColumn(name = "ID_ATENENTE")
	public DpLotacao atendente;

	@ManyToOne
	@JoinColumn(name = "ID_POS_ATENDENTE")
	public DpLotacao posAtendente;

	@ManyToOne
	@JoinColumn(name = "ID_PRE_ATENDENTE")
	public DpLotacao preAtendente;

	@ManyToOne
	@JoinColumn(name = "ID_TIPO_ATRIBUTO")
	public SrTipoAtributo tipoAtributo;

	@Column(name = "PESQUISA_SATISFACAO")
	@Type(type = "yes_no")
	public boolean pesquisaSatisfacao;

	@Column(name = "FG_ATRIBUTO_OBRIGATORIO")
	@Type(type = "yes_no")
	public boolean atributoObrigatorio;

	@Transient
	public SrSubTipoConfiguracao subTipoConfig;

	public SrConfiguracao() {

	}

	public SrConfiguracao(DpPessoa pess, SrItemConfiguracao item,
			SrServico servico, CpTipoConfiguracao tipo,
			SrSubTipoConfiguracao subTipoConfig) {
		this.setDpPessoa(pess);
		this.itemConfiguracao = item;
		this.servico = servico;
		this.setCpTipoConfiguracao(tipo);
		this.subTipoConfig = subTipoConfig;
	}

	public SrItemConfiguracao getItemConfiguracao() {
		if (itemConfiguracao != null)
			return itemConfiguracao.getAtual();
		return null;
	}

	public SrServico getServico() {
		if (servico != null)
			return servico.getAtual();
		return null;
	}
	
	public SrTipoAtributo getTipoAtributo() {
		if (tipoAtributo != null)
			return tipoAtributo.getAtual();
		return null;
	}

	public DpLotacao getAtendente() {
		if (atendente != null)
			return atendente.getLotacaoAtual();
		return null;
	}

	public DpLotacao getPosAtendente() {
		if (posAtendente != null)
			return posAtendente.getLotacaoAtual();
		return null;
	}

	public DpLotacao getPreAtendente() {
		if (preAtendente != null)
			return preAtendente.getLotacaoAtual();
		return null;
	}

	public String getPesquisaSatisfacaoString() {
		return pesquisaSatisfacao ? "Sim" : "Não";
	}

	public String getAtributoObrigatorioString() {
		return atributoObrigatorio ? "Sim" : "Não";
	}

	public void salvarComoDesignacao() throws Exception {
		setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO));
		salvar();
	}

	public static List<SrConfiguracao> listarDesignacoes() {
		return JPA
				.em()
				.createQuery(
						"from SrConfiguracao where cpTipoConfiguracao.idTpConfiguracao = "
								+ CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO
								+ " and hisDtFim is null", SrConfiguracao.class)
				.getResultList();
	}

	public void salvarComoAssociacaoTipoAtributo() throws Exception {
		setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
		salvar();
	}

	public static List<SrConfiguracao> listarAssociacoesTipoAtributo() {
		long k = CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO;
		return JPA
				.em()
				.createQuery(
						"from SrConfiguracao where cpTipoConfiguracao.idTpConfiguracao = "
								+ CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO
								+ " and hisDtFim is null", SrConfiguracao.class)
				.getResultList();
	}

	@Override
	public void salvar() throws Exception {
		Util.salvar(this);
	}

	@Override
	public void finalizar() throws Exception {
		Util.finalizar(this);
	}
	
	@Override
	public <T extends JPABase> T save() {
		//Edson: Ver no Util o comentário sobre a chamada abaixo
		if (getIdConfiguracao() == null)
			setIdConfiguracao(Util.nextVal("CORPORATIVO.CP_CONFIGURACAO_SEQ"));
		return super.save();
	}
}
