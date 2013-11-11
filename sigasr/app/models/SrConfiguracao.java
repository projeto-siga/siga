package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.siga.PlayConfiguracao;

import org.hibernate.annotations.Type;

import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import util.Util;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

@Entity
@Table(name = "SR_CONFIGURACAO", schema = "SIGASR")
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
	@JoinColumn(name = "ID_ATENDENTE")
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

	/*
	 * public SrItemConfiguracao getItemConfiguracao() { if (itemConfiguracao !=
	 * null) return itemConfiguracao.getAtual(); return null; }
	 * 
	 * public SrServico getServico() { if (servico != null) return
	 * servico.getAtual(); return null; }
	 * 
	 * public SrTipoAtributo getTipoAtributo() { if (tipoAtributo != null)
	 * return tipoAtributo.getAtual(); return null; }
	 * 
	 * public DpLotacao getAtendente() { if (atendente != null) return
	 * atendente.getLotacaoAtual(); return null; }
	 * 
	 * public DpLotacao getPosAtendente() { if (posAtendente != null) return
	 * posAtendente.getLotacaoAtual(); return null; }
	 * 
	 * public DpLotacao getPreAtendente() { if (preAtendente != null) return
	 * preAtendente.getLotacaoAtual(); return null; }
	 */

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
						"select conf from SrConfiguracao as conf left outer join conf.itemConfiguracao as item where conf.cpTipoConfiguracao.idTpConfiguracao = "
								+ CpTipoConfiguracao.TIPO_CONFIG_SR_DESIGNACAO
								+ " and conf.hisDtFim is null order by item.siglaItemConfiguracao, conf.orgaoUsuario",
						SrConfiguracao.class).getResultList();
	}

	public void salvarComoAssociacaoTipoAtributo() throws Exception {
		setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO));
		salvar();
	}
	
	public static List<SrConfiguracao> listarAssociacoesTipoAtributo() {
		return JPA
				.em()
				.createQuery(
						"select conf from SrConfiguracao as conf left outer join conf.itemConfiguracao as item where conf.cpTipoConfiguracao.idTpConfiguracao = "
								+ CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO
								+ " and conf.hisDtFim is null order by item.siglaItemConfiguracao, conf.orgaoUsuario",
						SrConfiguracao.class).getResultList();
	}

	public static List<List<SrConfiguracao>> listarAssociacoesTipoAtributoDividindoAbertasEFechadas() {

		String query = "select conf from SrConfiguracao as conf left outer join conf.itemConfiguracao as item where conf.cpTipoConfiguracao.idTpConfiguracao = "
				+ CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO
				+ " and conf.hisDtFim is null order by item.siglaItemConfiguracao, conf.orgaoUsuario";

		List<SrConfiguracao> abertas = JPA.em()
				.createQuery(query, SrConfiguracao.class).getResultList();

		query = query.replace("conf.hisDtFim is null",
				"conf.hisDtFim is not null and conf.hisDtIni = ("
						+ "	select max(hisDtIni) from SrConfiguracao where "
						+ "hisIdIni = conf.hisIdIni)");

		List<SrConfiguracao> fechadas = JPA.em()
				.createQuery(query, SrConfiguracao.class).getResultList();

		List<List<SrConfiguracao>> retorno = new ArrayList<List<SrConfiguracao>>();
		retorno.add(abertas);
		retorno.add(fechadas);
		return retorno;

	}

	public static SrConfiguracao getConfiguracao(DpPessoa pess,
			SrItemConfiguracao item, SrServico servico, long idTipo,
			SrSubTipoConfiguracao subTipo) throws Exception {

		SrConfiguracao conf = new SrConfiguracao(pess, item, servico, JPA.em()
				.find(CpTipoConfiguracao.class, idTipo), subTipo);

		return SrConfiguracaoBL.get().buscarConfiguracao(conf);
	}

	public static List<SrConfiguracao> getConfiguracoes(DpPessoa pess,
			SrItemConfiguracao item, SrServico servico, long idTipo,
			SrSubTipoConfiguracao subTipo) throws Exception {
		return getConfiguracoes(pess, item, servico, idTipo, subTipo,
				new int[] {});
	}

	public static List<SrConfiguracao> getConfiguracoes(DpPessoa pess,
			SrItemConfiguracao item, SrServico servico, long idTipo,
			SrSubTipoConfiguracao subTipo, int atributoDesconsideradoFiltro[])
			throws Exception {
		SrConfiguracao conf = new SrConfiguracao(pess, item, servico, JPA.em()
				.find(CpTipoConfiguracao.class, idTipo), subTipo);
		return SrConfiguracaoBL.get().listarConfiguracoesAtivasPorFiltro(conf,
				atributoDesconsideradoFiltro);
	}

	@Override
	public Long getId() {
		return getIdConfiguracao();
	}

	@Override
	public void setId(Long id) {
		setIdConfiguracao(id);
	}
}
