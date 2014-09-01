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

import org.hibernate.annotations.Type;

import play.db.jpa.JPA;
import br.gov.jfrj.siga.cp.CpComplexo;
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
	@JoinColumn(name = "ID_ACAO")
	public SrAcao acao;

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
	@JoinColumn(name = "ID_EQUIPE_QUALIDADE")
	public DpLotacao equipeQualidade;

	@ManyToOne
	@JoinColumn(name = "ID_PRE_ATENDENTE")
	public DpLotacao preAtendente;

	@ManyToOne
	@JoinColumn(name = "ID_TIPO_ATRIBUTO")
	public SrTipoAtributo tipoAtributo;

	@ManyToOne
	@JoinColumn(name = "ID_PESQUISA")
	public SrPesquisa pesquisaSatisfacao;

	@ManyToOne
	@JoinColumn(name = "ID_LISTA")
	public SrLista listaPrioridade;

	@Column(name = "FG_ATRIBUTO_OBRIGATORIO")
	@Type(type = "yes_no")
	public boolean atributoObrigatorio;

	@Transient
	public SrSubTipoConfiguracao subTipoConfig;

	public SrConfiguracao() {

	}

	public SrConfiguracao(DpLotacao lota, DpPessoa pess, CpComplexo local,
			SrItemConfiguracao item, SrAcao acao, SrLista lista,
			CpTipoConfiguracao tipo, SrSubTipoConfiguracao subTipoConfig) {
		this.setLotacao(lota);
		this.setDpPessoa(pess);
		this.setComplexo(local);
		this.itemConfiguracao = item;
		this.acao = acao;
		this.listaPrioridade = lista;
		this.setCpTipoConfiguracao(tipo);
		this.subTipoConfig = subTipoConfig;
	}

	public String getPesquisaSatisfacaoString() {
		return pesquisaSatisfacao.nomePesquisa;
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
								+ " and conf.hisDtFim is null order by item.siglaItemConfiguracao, conf.orgaoUsuario").getResultList();
	}

	public void salvarComoPermissaoUsoLista() throws Exception {
		setCpTipoConfiguracao(JPA.em().find(CpTipoConfiguracao.class,
				CpTipoConfiguracao.TIPO_CONFIG_SR_PERMISSAO_USO_LISTA));
		salvar();
	}

	public static List<SrConfiguracao> listarPermissoesUsoLista(DpLotacao lota) {
		return JPA
				.em()
				.createQuery(
						"select conf from SrConfiguracao as conf where conf.cpTipoConfiguracao.idTpConfiguracao = "
								+ CpTipoConfiguracao.TIPO_CONFIG_SR_PERMISSAO_USO_LISTA
								+ " and conf.listaPrioridade.lotaCadastrante.idLotacaoIni = "
								+ lota.getLotacaoInicial().getIdLotacao()
								+ " and conf.hisDtFim is null order by conf.orgaoUsuario").getResultList();
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
								+ " and conf.hisDtFim is null order by item.siglaItemConfiguracao, conf.orgaoUsuario").getResultList();
	}

	public static List<List<SrConfiguracao>> listarAssociacoesTipoAtributoDividindoAbertasEFechadas() {

		String query = "select conf from SrConfiguracao as conf left outer join conf.itemConfiguracao as item where conf.cpTipoConfiguracao.idTpConfiguracao = "
				+ CpTipoConfiguracao.TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO
				+ " and conf.hisDtFim is null order by item.siglaItemConfiguracao, conf.orgaoUsuario";

		List<SrConfiguracao> abertas = JPA.em().createQuery(query).getResultList();

		query = query.replace("conf.hisDtFim is null",
				"conf.hisDtFim is not null and conf.hisDtIni = ("
						+ "	select max(hisDtIni) from SrConfiguracao where "
						+ "hisIdIni = conf.hisIdIni)");

		List<SrConfiguracao> fechadas = JPA.em().createQuery(query).getResultList();

		List<List<SrConfiguracao>> retorno = new ArrayList<List<SrConfiguracao>>();
		retorno.add(abertas);
		retorno.add(fechadas);
		return retorno;

	}

	public static SrConfiguracao getConfiguracao(DpPessoa pess,
			CpComplexo local, SrItemConfiguracao item, SrAcao acao,
			long idTipo, SrSubTipoConfiguracao subTipo) throws Exception {

		return getConfiguracao(null, pess, local, item, acao, null, idTipo,
				subTipo);
	}

	public static SrConfiguracao getConfiguracao(DpLotacao lotaTitular,
			DpPessoa pess, long idTipo, SrLista lista) throws Exception {
		return getConfiguracao(lotaTitular, pess, null, null, null, lista,
				idTipo, null);
	}

	public static SrConfiguracao getConfiguracao(DpLotacao lota, DpPessoa pess,
			CpComplexo local, SrItemConfiguracao item, SrAcao acao,
			SrLista lista, long idTipo, SrSubTipoConfiguracao subTipo)
			throws Exception {

		SrConfiguracao conf = new SrConfiguracao(lota, pess, local, item, acao,
				lista, JPA.em().find(CpTipoConfiguracao.class, idTipo), subTipo);

		return SrConfiguracaoBL.get().buscarConfiguracao(conf);
	}

	public static List<SrConfiguracao> getConfiguracoes(DpPessoa pess,
			CpComplexo complexo, SrItemConfiguracao item, SrAcao acao,
			long idTipo, SrSubTipoConfiguracao subTipo) throws Exception {
		return getConfiguracoes(null, pess, complexo, item, acao, idTipo,
				subTipo, new int[] {});
	}

	public static List<SrConfiguracao> getConfiguracoes(DpLotacao lotaTitular,
			DpPessoa pess, long idTipo, int atributoDesconsideradoFiltro[] ) throws Exception {
		return getConfiguracoes(lotaTitular, pess, null, null, null, idTipo,
				null, atributoDesconsideradoFiltro);
	}

	public static List<SrConfiguracao> getConfiguracoes(DpLotacao lota,
			DpPessoa pess, CpComplexo local, SrItemConfiguracao item,
			SrAcao acao, long idTipo, SrSubTipoConfiguracao subTipo,
			int atributoDesconsideradoFiltro[]) throws Exception {
		SrConfiguracao conf = new SrConfiguracao(lota, pess, local, item, acao,
				null, JPA.em().find(CpTipoConfiguracao.class, idTipo), subTipo);
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
