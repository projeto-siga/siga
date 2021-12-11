package br.gov.jfrj.siga.dp;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavel;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

@MappedSuperclass 
@NamedQueries({
@NamedQuery(name = "verificaSeUsuarioJaPossuiConfiguracoesDeNotificacoesPorEmail", query = "select email from DpNotificarPorEmail email where email.dpPessoa.idPessoa = :idPessoa"),
//@NamedQuery(name = "consultarNotificaocaoEmail", query = "select email from DpNotificarPorEmail email where email.dpPessoa.idPessoa = :idPessoa"),
@NamedQuery(name = "consultarQuantidadeNotificarPorEmail", query = "select count(e) from DpNotificarPorEmail e") })
public abstract class AbstractDpNotificarPorEmail extends Objeto implements Serializable, HistoricoAuditavel {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_NOTIFICAR_POR_EMAIL", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "NOME_DA_ACAO", nullable = false, length = 200)  
	private String nomeDaAcao;
	
	@Column(name = "NAO_CONFIGURAVEL", nullable = false)
	private int naoConfiguravel;
	
	@Column(name = "CONFIGURAVEL", nullable = false, length = 200)
	private int configuravel;
	
	@Column(name = "RESTRINGIR", nullable = false, length = 200)
	private int restringir;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DP_PESSOA_ID")
	private DpPessoa dpPessoa;
	
	@Column(name = "CODIGO", nullable = false, length = 200)
	private int codigo;
	
	@Column(name = "CADASTRADO", nullable = false, length = 200)
	private int cadastrado;
	
	@Column(name = "ID_NOTIFICAR_POR_EMAIL_INICIAL")
	@Desconsiderar
	private Long idNotificarPorEmailIni;

	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="HIS_IDC_INI")
	@Desconsiderar 
	private CpIdentidade hisIdcIni;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@Desconsiderar
    @JoinColumn(name="HIS_IDC_FIM")
	private CpIdentidade hisIdcFim;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_INI_NOTIFICAR_POR_EMAIL", length = 19)
	@Desconsiderar
	private Date dataInicioNotificarPorEmail;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_FIM_NOTIFICAR_POR_EMAIL", length = 19)
	private Date dataFimNotificarPorEmail;
	
	@Column(name = "IDE_NOTIFICAR_POR_EMAIL", length = 256)
	private String ideNotificarPorEmail;
	
	public int getCadastrado() {
		return cadastrado;
	}

	public void setCadastrado(int cadastrado) {
		this.cadastrado = cadastrado;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getIdeNotificarPorEmail() {
		return ideNotificarPorEmail;
	}

	public void setIdeNotificarPorEmail(String ideNotificarPorEmail) {
		this.ideNotificarPorEmail = ideNotificarPorEmail;
	}

	public Date getDataFimNotificarPorEmail() {
		return dataFimNotificarPorEmail;
	}

	public void setDataFimNotificarPorEmail(Date dataFimNotificarPorEmail) {
		this.dataFimNotificarPorEmail = dataFimNotificarPorEmail;
	}

	public Date getDataInicioNotificarPorEmail() {
		return dataInicioNotificarPorEmail;
	}

	public void setDataInicioNotificarPorEmail(Date dataInicioNotificarPorEmail) {
		this.dataInicioNotificarPorEmail = dataInicioNotificarPorEmail;
	}

	public Long getIdNotificarPorEmailIni() {
		return idNotificarPorEmailIni;
	}

	public void setIdNotificarPorEmailIni(Long idCargoIni) {
		this.idNotificarPorEmailIni = idCargoIni;
	}

	public int getRestringir() {
		return restringir;
	}

	public void setRestringir(int restringir) {
		this.restringir = restringir;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractDpNotificarPorEmail other = (AbstractDpNotificarPorEmail) obj;
		return Objects.equals(id, other.id);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getNomeDaAcao() {
		return nomeDaAcao;
	}

	public void setNomeDaAcao(String nomeDaAcao) {
		this.nomeDaAcao = nomeDaAcao;
	}

	public int getNaoConfiguravel() {
		return naoConfiguravel;
	}

	public void setNaoConfiguravel(int naoConfiguravel) {
		this.naoConfiguravel = naoConfiguravel;
	}

	public int getConfiguravel() {
		return configuravel;
	}

	public void setConfiguravel(int configuravel) {
		this.configuravel = configuravel;
	}

	public DpPessoa getDpPessoa() {
		return dpPessoa;
	}

	public void setDpPessoa(DpPessoa dpPessoa) {
		this.dpPessoa = dpPessoa;
	}

	public boolean isConfiguravel () {
		if(this.configuravel == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean notConfiguravel () {
		if(this.naoConfiguravel == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean restringir () {
		if(this.restringir == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isCadastrado () {
		if(this.cadastrado == 1) {
			return true;
		} else {
			return false;
		}
	}

}
