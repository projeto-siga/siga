package br.gov.jfrj.siga.wf.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.BatchSize;

import com.crivano.jflow.model.ProcessDefinition;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.logic.WfPodeEditarDiagrama;
import br.gov.jfrj.siga.wf.logic.WfPodeIniciarDiagrama;
import br.gov.jfrj.siga.wf.model.enm.WfAcessoDeEdicao;
import br.gov.jfrj.siga.wf.model.enm.WfAcessoDeInicializacao;
import br.gov.jfrj.siga.wf.util.SiglaUtils;
import br.gov.jfrj.siga.wf.util.SiglaUtils.SiglaDecodificada;

@Entity
@BatchSize(size = 500)
@Table(name = "sigawf.wf_def_procedimento")
public class WfDefinicaoDeProcedimento extends HistoricoAuditavelSuporte implements Serializable,
		ProcessDefinition<WfDefinicaoDeTarefa>, Selecionavel, Sincronizavel, Comparable<Sincronizavel> {
	public static ActiveRecord<WfDefinicaoDeProcedimento> AR = new ActiveRecord<>(WfDefinicaoDeProcedimento.class);

	@Id
	@GeneratedValue
	@Column(name = "DEFP_ID", unique = true, nullable = false)
	@Desconsiderar
	private java.lang.Long id;

	@NotNull
	@Column(name = "DEFP_NM", length = 256)
	private java.lang.String nome;

	@NotNull
	@Column(name = "DEFP_DS", length = 256)
	private java.lang.String descr;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "definicaoDeProcedimento")
	@OrderBy("ordem ASC")
	@Desconsiderar
	private List<WfDefinicaoDeTarefa> definicaoDeTarefa = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORGU_ID")
	private CpOrgaoUsuario orgaoUsuario;

	@Column(name = "DEFP_ANO")
	private Integer ano;

	@Column(name = "DEFP_NR")
	private Integer numero;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PESS_ID_RESP")
	private DpPessoa responsavel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOTA_ID_RESP")
	private DpLotacao lotaResponsavel;

	@Enumerated(EnumType.STRING)
	@Column(name = "DEFP_TP_EDICAO")
	private WfAcessoDeEdicao acessoDeEdicao;

	@Enumerated(EnumType.STRING)
	@Column(name = "DEFP_TP_INICIO")
	private WfAcessoDeInicializacao acessoDeInicializacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRUP_ID_EDICAO")
	private CpPerfil grupoDeEdicao;

	@Transient
	private java.lang.String hisIde;

	@Transient
	private Long responsavelId;

	@Transient
	private Long lotaResponsavelId;

	//
	// Solução para não precisar criar HIS_ATIVO em todas as tabelas que herdam
	// de HistoricoSuporte.
	//
	@Column(name = "HIS_ATIVO")
	private Integer hisAtivo;

	@Override
	public Integer getHisAtivo() {
		this.hisAtivo = super.getHisAtivo();
		return this.hisAtivo;
	}

	@Override
	public void setHisAtivo(Integer hisAtivo) {
		super.setHisAtivo(hisAtivo);
		this.hisAtivo = getHisAtivo();
	}

	@Override
	public List<WfDefinicaoDeTarefa> getTaskDefinition() {
		return getDefinicaoDeTarefa();
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public java.lang.String getNome() {
		return nome;
	}

	public void setNome(java.lang.String nome) {
		this.nome = nome;
	}

	@Override
	public String getDescricao() {
		return nome;
	}

	public List<WfDefinicaoDeTarefa> getDefinicaoDeTarefa() {
		return definicaoDeTarefa;
	}

	public void setDefinicaoDeTarefa(List<WfDefinicaoDeTarefa> definicaoDeTarefa) {
		this.definicaoDeTarefa = definicaoDeTarefa;
	}

	public java.lang.String getHisIde() {
		return hisIde;
	}

	public void setHisIde(java.lang.String hisIde) {
		this.hisIde = hisIde;
	}

	@Override
	public String getIdExterna() {
		return getHisIde() != null ? getHisIde() : "nova definição de procedimento";
	}

	@Override
	public void setIdExterna(String idExterna) {
		setHisIde(idExterna);
	}

	@Override
	public void setIdInicial(Long idInicial) {
		this.setHisIdIni(idInicial);
	}

	@Override
	public Date getDataInicio() {
		return getHisDtIni();
	}

	@Override
	public void setDataInicio(Date dataInicio) {
		setHisDtIni(dataInicio);
	}

	@Override
	public Date getDataFim() {
		return getHisDtFim();
	}

	@Override
	public void setDataFim(Date dataFim) {
		setHisDtFim(dataFim);
	}

	@Override
	public String getLoteDeImportacao() {
		return null;
	}

	@Override
	public void setLoteDeImportacao(String loteDeImportacao) {
	}

	@Override
	public int getNivelDeDependencia() {
		return 0;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return SincronizavelSuporte.semelhante(this, obj, profundidade);
	}

	@Override
	public String getDescricaoExterna() {
		return getNome();
	}

	@Override
	public int compareTo(Sincronizavel o) {
		if (!this.getClass().equals(o.getClass()))
			return this.getClass().getName().compareTo(o.getClass().getName());
		return this.getIdExterna().compareTo(o.getIdExterna());
	}

	public java.lang.String getDescr() {
		return descr;
	}

	public void setDescr(java.lang.String descr) {
		this.descr = descr;
	}

	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getSigla() {
		return orgaoUsuario.getAcronimoOrgaoUsu() + "-DP-" + ano + "/" + Utils.completarComZeros(numero, 5);
	}

	public String getSiglaCompacta() {
		return getSigla().replace("-", "").replace("/", "");
	}

	public static WfDefinicaoDeProcedimento findBySigla(String sigla) throws NumberFormatException, Exception {
		return findBySigla(sigla, null);
	}

	public static WfDefinicaoDeProcedimento findBySigla(String sigla, CpOrgaoUsuario ouDefault)
			throws NumberFormatException, Exception {
		SiglaDecodificada d = SiglaUtils.parse(sigla, "DP", null);

		WfDefinicaoDeProcedimento info = null;

		if (d.id != null) {
			info = AR.findById(d.id);
		} else if (d.numero != null) {
			info = AR.find("ano = ?1 and numero = ?2 and ou.idOrgaoUsu = ?3", d.ano, d.numero, d.orgaoUsuario.getId())
					.first();
		}

		if (info == null) {
			throw new AplicacaoException("Não foi possível encontrar uma definição de procedimento com o código "
					+ sigla + ". Favor verificá-lo.");
		} else
			return info;
	}

	public static WfDefinicaoDeProcedimento findByNome(String titulo) throws Exception {
		try {
			String[] palavras = titulo.toUpperCase().split(" ");
			StringBuilder query = new StringBuilder("hisDtFim is null");
			for (String palavra : palavras) {
				query.append(" and upper(nome) like '%");
				query.append(palavra);
				query.append("%' ");
			}
			List<WfDefinicaoDeProcedimento> results = AR.find(query.toString()).fetch();
			return results.size() == 1 ? results.get(0) : null;
		} catch (Exception e) {
			return null;
		}
	}

	@PrePersist
	private void onPersist() {
		if (getAno() != null)
			return;
		setAno(WfDao.getInstance().dt().getYear() + 1900);
		Query qry = em().createQuery(
				"select max(numero) from WfDefinicaoDeProcedimento pi where ano = :ano and orgaoUsuario.idOrgaoUsu = :ouid");
		qry.setParameter("ano", getAno());
		qry.setParameter("ouid", getOrgaoUsuario().getId());
		Integer i = (Integer) qry.getSingleResult();
		setNumero((i == null ? 0 : i) + 1);
	}

	@Override
	public void setSigla(String sigla) {
		SiglaDecodificada d = SiglaUtils.parse(sigla, "DP", null);
		this.ano = d.ano;
		this.numero = d.numero;
		this.orgaoUsuario = d.orgaoUsuario;
	}

	public WfAcessoDeEdicao getAcessoDeEdicao() {
		return acessoDeEdicao;
	}

	public void setAcessoDeEdicao(WfAcessoDeEdicao acessoDeEdicao) {
		this.acessoDeEdicao = acessoDeEdicao;
	}

	public CpPerfil getGrupo() {
		return null;
	}

	public void assertAcessoDeEditar(DpPessoa titular, DpLotacao lotaTitular) {
		if (!WfAcessoDeEdicao.acessoPermitido(this, titular, lotaTitular))
			throw new RuntimeException("Edição do diagrama '" + nome + "' não é permitida para "
					+ (titular != null ? titular.getSigla() : null) + "/"
					+ (lotaTitular != null ? lotaTitular.getSigla() : null));
	}

	public List<AcaoVO> getAcoes(DpPessoa titular, DpLotacao lotaTitular) {
		List<AcaoVO> set = new ArrayList<>();

		set.add(AcaoVO.builder().nome("_Editar").icone("pencil").acao("/app/diagrama/editar?id=" + id)
				.exp(new WfPodeEditarDiagrama(this, titular, lotaTitular)).build());

		set.add(AcaoVO.builder().nome("_Iniciar").icone("add").acao("/app/iniciar/" + id)
				.exp(new WfPodeIniciarDiagrama(this, titular, lotaTitular)).build());

		return set;
	}

	public Long getResponsavelId() {
		return responsavelId;
	}

	public void setResponsavelId(Long responsavelId) {
		this.responsavelId = responsavelId;
	}

	public Long getLotaResponsavelId() {
		return lotaResponsavelId;
	}

	public void setLotaResponsavelId(Long lotaResponsavelId) {
		this.lotaResponsavelId = lotaResponsavelId;
	}

	public DpPessoa getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(DpPessoa responsavel) {
		this.responsavel = responsavel;
	}

	public DpLotacao getLotaResponsavel() {
		return lotaResponsavel;
	}

	public void setLotaResponsavel(DpLotacao lotaResponsavel) {
		this.lotaResponsavel = lotaResponsavel;
	}

	public WfAcessoDeInicializacao getAcessoDeInicializacao() {
		return acessoDeInicializacao;
	}

	public void setAcessoDeInicializacao(WfAcessoDeInicializacao acessoDeInicializacao) {
		this.acessoDeInicializacao = acessoDeInicializacao;
	}

}
