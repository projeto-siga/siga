package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Entity
@Table(name = "SR_DISPONIBILIDADE", schema = "SIGASR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SrDisponibilidade extends HistoricoSuporte implements Cloneable {

	private static final long serialVersionUID = 7243562288736225097L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_DISPONIBILIDADE_SEQ", name = "srDisponibilidadeSeq")
	@GeneratedValue(generator = "srDisponibilidadeSeq")
	@Column(name = "ID_DISPONIBILIDADE")
	private Long idDisponibilidade;

	@JoinColumn(name = "ID_ITEM_CONFIGURACAO")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private SrItemConfiguracao itemConfiguracao;

	@JoinColumn(name = "ID_ORGAO_USU")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private CpOrgaoUsuario orgao;
	
	@Enumerated
	@Column(name = "TP_DISPONIBILIDADE")
	private SrTipoDisponibilidade tipo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_HR_INI", nullable = false)
	private Date dataHoraInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_HR_FIM")
	private Date dataHoraTermino;

	@Column(name = "MSG", length = 50)
	private String mensagem;

	@Column(name = "DET_TECNICO", length = 50)
	private String detalhamentoTecnico;
	
	@Transient
	private transient JsonArray disponibilidadesAtualizadas;

	public SrDisponibilidade() {
		this.disponibilidadesAtualizadas = new JsonArray();
	}
	
	public SrDisponibilidade(SrItemConfiguracao itemConfiguracao, CpOrgaoUsuario orgao) {
		this();
		this.tipo = SrTipoDisponibilidade.NENHUM;
		this.itemConfiguracao = itemConfiguracao;
		this.orgao = orgao;
	}
	
	@Override
	public Long getId() {
		return idDisponibilidade;
	}

	@Override
	public void setId(Long id) {
		this.idDisponibilidade = id;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return SincronizavelSuporte.semelhante(this, obj, profundidade);
	}

	public Long getIdDisponibilidade() {
		return idDisponibilidade;
	}

	public void setIdDisponibilidade(Long idDisponibilidade) {
		this.idDisponibilidade = idDisponibilidade;
	}

	public SrItemConfiguracao getItemConfiguracao() {
		return itemConfiguracao;
	}

	public void setItemConfiguracao(SrItemConfiguracao itemConfiguracao) {
		this.itemConfiguracao = itemConfiguracao;
	}

	public CpOrgaoUsuario getOrgao() {
		return orgao;
	}

	public void setOrgao(CpOrgaoUsuario orgao) {
		this.orgao = orgao;
	}

	public SrTipoDisponibilidade getTipo() {
		return tipo;
	}

	public void setTipo(SrTipoDisponibilidade tipo) {
		this.tipo = tipo;
	}

	public Date getDataHoraInicio() {
		return dataHoraInicio;
	}

	public void setDataHoraInicio(Date dataHoraInicio) {
		this.dataHoraInicio = dataHoraInicio;
	}

	public Date getDataHoraTermino() {
		return dataHoraTermino;
	}

	public void setDataHoraTermino(Date dataHoraTermino) {
		this.dataHoraTermino = dataHoraTermino;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getDetalhamentoTecnico() {
		return detalhamentoTecnico;
	}

	public void setDetalhamentoTecnico(String detalhamentoTecnico) {
		this.detalhamentoTecnico = detalhamentoTecnico;
	}
	
	public String getIcone() {
		return this.tipo.getCaminhoIcone();
	}

	public JsonObject toJsonObject() {
		Gson gson = new GsonBuilder()
			.setDateFormat("dd/MM/yyyy HH:mm")
			.create();

		JsonObject object = new JsonObject();
		object.add("siglaOrgao", gson.toJsonTree(orgao.getSiglaOrgaoUsu()));
		object.add("orgaoId", gson.toJsonTree(orgao.getId()));
		object.add("idItemConfiguracao", gson.toJsonTree(itemConfiguracao.getId()));
		object.add("hisIdIniItemConfiguracao", gson.toJsonTree(itemConfiguracao.getHisIdIni()));
		object.add("idDisponibilidade", gson.toJsonTree(idDisponibilidade));
		object.add("hisIdIni", gson.toJsonTree(getHisIdIni()));
		object.add("tipo", gson.toJsonTree(tipo));
		object.add("caminhoIcone", gson.toJsonTree(tipo.getCaminhoIcone()));
		object.add("mensagem", gson.toJsonTree(mensagem));
		object.add("detalhamentoTecnico", gson.toJsonTree(detalhamentoTecnico));
		object.add("dataHoraInicio", gson.toJsonTree(dataHoraInicio));
		object.add("dataHoraTermino", gson.toJsonTree(dataHoraTermino));
		object.add("disponibilidadesAtualizadas", gson.toJsonTree(disponibilidadesAtualizadas));

		return object;
	}
	
	@SuppressWarnings("unchecked")
	public static List<SrDisponibilidade> buscarTodos(SrItemConfiguracao itemConfiguracao, List<CpOrgaoUsuario> orgaos) {
		return em().createQuery("SELECT d FROM SrDisponibilidade d WHERE d.itemConfiguracao.hisIdIni = :hisIdIni AND d.orgao IN (:orgaos) AND d.hisDtFim is null ")
				.setParameter("hisIdIni", itemConfiguracao.getHisIdIni())
				.setParameter("orgaos", orgaos)
				.getResultList();
	}
	
	public static SrDisponibilidade buscarPara(SrItemConfiguracao itemConfiguracao, CpOrgaoUsuario orgao) {
		try {
			return (SrDisponibilidade) em().createQuery("SELECT d FROM SrDisponibilidade d WHERE d.itemConfiguracao.hisIdIni = :hisIdIni AND d.orgao = :orgao AND d.hisDtFim is null ")
						.setParameter("hisIdIni", itemConfiguracao.getHisIdIni())
						.setParameter("orgao", orgao)
						.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}
	
	@Override
	public void salvar() throws Exception {
		super.salvar();
		this.configurarAtualizacaoDisponibilidades(itemConfiguracao, Arrays.asList(orgao));
	}

	private void configurarAtualizacaoDisponibilidades(SrItemConfiguracao itemConfiguracao, List<CpOrgaoUsuario> orgaos) {
		this.disponibilidadesAtualizadas.addAll(itemConfiguracao.criarDisponibilidadesJSON(itemConfiguracao, orgaos));
		
		for (SrItemConfiguracao filho : itemConfiguracao.filhoSet) {
			if(filho.getHisDtFim() == null) {
				configurarAtualizacaoDisponibilidades(filho, orgaos);
			}
		}
	}
	
	public boolean isNenhuma() {
		return SrTipoDisponibilidade.NENHUM.equals(tipo);
	}

	private SrDisponibilidade clonar() {
		try {
			return (SrDisponibilidade) this.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	public SrDisponibilidade atualizarCamposPreenchidos(SrDisponibilidade novaDisponibilidade) {
		this.setTipo(novaDisponibilidade.getTipo());
		this.setDataHoraInicio(novaDisponibilidade.getDataHoraInicio());
		this.setDataHoraTermino(novaDisponibilidade.getDataHoraTermino());
		this.setDetalhamentoTecnico(novaDisponibilidade.getDetalhamentoTecnico());
		this.setMensagem(novaDisponibilidade.getMensagem());
		
		return this;
	}
	
	public void setDataHoraInicioString(String dataInicioString) {
		try {
			this.dataHoraInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dataInicioString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setDataHoraTerminoString(String dataTerminoString) {
		try {
			this.dataHoraTermino = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dataTerminoString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public boolean pertenceA(SrItemConfiguracao srItemConfiguracao) {
		return this.itemConfiguracao.getId().equals(srItemConfiguracao.getId());
	}

	public SrDisponibilidade clonarParaCriarNovo(SrItemConfiguracao filho) {
		SrDisponibilidade clone = clonar();
		clone.setId(null);
		clone.setItemConfiguracao(filho);
		clone.setOrgao(orgao);
		return clone;
	}

	public SrDisponibilidade clonarParaAtualizar(SrDisponibilidade disponibilidadePai) {
		SrDisponibilidade clone = clonar();
		clone.setTipo(disponibilidadePai.getTipo());
		clone.setDataHoraInicio(disponibilidadePai.getDataHoraInicio());
		clone.setDataHoraTermino(disponibilidadePai.getDataHoraTermino());
		clone.setDetalhamentoTecnico(disponibilidadePai.getDetalhamentoTecnico());
		clone.setMensagem(disponibilidadePai.getMensagem());
		
		return clone;
	}
}