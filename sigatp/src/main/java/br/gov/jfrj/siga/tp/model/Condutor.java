package br.gov.jfrj.siga.tp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.tp.util.FormatarDataHora;
import br.gov.jfrj.siga.tp.util.PerguntaSimNao;
import br.gov.jfrj.siga.tp.validation.annotation.Cnh;
import br.gov.jfrj.siga.tp.validation.annotation.Unique;
import br.gov.jfrj.siga.tp.validation.annotation.UpperCase;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;
import br.gov.jfrj.siga.validation.Email;

@SuppressWarnings({"serial", "unchecked"})
@Entity
@Audited
@Table(name = "condutor", schema = "sigatp")
@Unique(message="{condutor.dppessoa.unique}" ,field = "dpPessoa", uniqueColumn="DPPESSOA_ID_PESSOA")
public class Condutor extends TpModel implements ConvertableEntity, Comparable<Condutor> {

	public static final ActiveRecord<Condutor> AR = new ActiveRecord<>(Condutor.class);

	@Id
	@SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "sigatp.hibernate_sequence")
	@GeneratedValue(generator = "hibernate_sequence_generator")
	private Long id;

	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne
	@JoinColumn(name = "ID_ORGAO_USU")
	private CpOrgaoUsuario cpOrgaoUsuario;

	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@OneToOne(targetEntity = DpPessoa.class)
	@NotNull
	private DpPessoa dpPessoa;

	@Enumerated(EnumType.STRING)
	private CategoriaCNH categoriaCNH;

	@Basic(optional = false)
	@NotNull(message = "Data de Vencimento da CNH deve ser preenchida")
	private Calendar dataVencimentoCNH;

	@Basic(optional = false)
	@NotNull(message = "N\u00FAmero da CNH deve ser preenchido")
	@Cnh
	private String numeroCNH;

	@Basic(optional = false)
	@NotNull(message = "Telefone fixo institucional deve ser preenchido")
	private String telefoneInstitucional;

	@OneToMany(mappedBy = "condutor")
	private List<EscalaDeTrabalho> escala;

	@Email(message = "Email invalido")
	private String emailPessoal;

	private String telefonePessoal;

	private String celularPessoal;

	private String endereco;

	private String celularInstitucional;

	private byte[] conteudoimagemblob;

	@UpperCase
	private String observacao;

	@Transient
	private Imagem arquivo;

	@Transient
	private String situacaoImagem;

	public Condutor() {
		this.dpPessoa = new DpPessoa();
		this.id = 0L;
		this.categoriaCNH = CategoriaCNH.D;
		escala = new ArrayList<EscalaDeTrabalho>();
	}

	@Override
	public int compareTo(Condutor o) {
		return this.dpPessoa.getNomePessoa().compareTo(o.dpPessoa.getNomePessoa());
	}

//	public static List<DpPessoa> getPossiveisCondutores(CpOrgaoUsuario cpOrgaoUsuario) {
//		List<LotacaoAtdRequisicao> lotacoesAtdRequisicao = LotacaoAtdRequisicao.find("cpOrgaoUsuario", cpOrgaoUsuario).fetch();
//		List<DpLotacao> lotacoes = new ArrayList<DpLotacao>();
//		for (LotacaoAtdRequisicao lotacaoAtdRequisicao : lotacoesAtdRequisicao) {
//			lotacoes.add(lotacaoAtdRequisicao.dpLotacao);
//		}
//
//		List<DpPessoa> possiveisCondutores = DpPessoa.AR.find("lotacao in (?)", lotacoes.toArray()).fetch();
//		return possiveisCondutores;
//	}

	public String getMatricula() {
		return this.dpPessoa.getMatricula().toString();
	}

	public String getNomePessoaAI() {
		return this.dpPessoa.getNomePessoaAI();
	}

	public boolean getVencimentoCNHExpirado() {
		if (this.dataVencimentoCNH == null) {
			return false;
		}
		if (this.dataVencimentoCNH.compareTo(Calendar.getInstance()) > 0) {
			return false;
		}
		return true;
	}

	public String getNome() {
		return this.dpPessoa.getNomePessoaAI().toString();
	}

	public String getDadosParaExibicao() {
		return getMatricula() + " - " + getNome();
	}

	public static List<Condutor> listarDisponiveis(String dataSaida, Long idMissao, Long idOrgao, PerguntaSimNao inicioRapido) throws Exception {
		List<Condutor> condutores;
		if (inicioRapido == null) {
			inicioRapido = PerguntaSimNao.NAO;
		}
		String dataFormatadaOracle = FormatarDataHora.retorna_DataeHora(dataSaida);

		StringBuilder qrl = new StringBuilder();
		qrl.append("SELECT c FROM Condutor c " + " WHERE " + FormatarDataHora.recuperaFuncaoTrunc() + "(c.dataVencimentoCNH) > " + FormatarDataHora.recuperaFuncaoTrunc() +"(" + dataFormatadaOracle + ")" + "  AND c.cpOrgaoUsuario.id in  " + "(SELECT cp.id FROM CpOrgaoUsuario cp"
				+ " WHERE  cp.id = " + idOrgao + ")" + " AND c.id not in ");
		if (!inicioRapido.equals(PerguntaSimNao.SIM)) {
			qrl.append("(SELECT a.condutor.id FROM Afastamento a" + " WHERE  a.condutor.id = c.id" + " AND   a.dataHoraInicio < " + dataFormatadaOracle + " AND    (a.dataHoraFim = NULL "
					+ " OR    a.dataHoraFim > " + dataFormatadaOracle + "))" + " AND c.id not in");
		}
		qrl.append("(SELECT m.condutor.id FROM Missao m" + " WHERE  m.condutor.id = c.id" + " AND    m.estadoMissao != '" + EstadoMissao.CANCELADA + "'" + " AND    m.estadoMissao != '"
				+ EstadoMissao.FINALIZADA + "'" + " AND    m.estadoMissao != '" + EstadoMissao.PROGRAMADA + "'" + " AND    m.id != " + idMissao + " AND   m.dataHoraSaida < " + dataFormatadaOracle
				+ " AND    (m.dataHoraRetorno = NULL " + " OR    m.dataHoraRetorno > " + dataFormatadaOracle + "))" + " ORDER BY c.dpPessoa.nomePessoa");

		Query qry = AR.em().createQuery(qrl.toString());
		try {
			condutores = ((List<Condutor>) qry.getResultList());
		} catch (NoResultException ignore) {
			condutores = null;
		}

		if (condutores != null && !condutores.isEmpty() && (!inicioRapido.equals(PerguntaSimNao.SIM))) {
			for (Iterator<Condutor> iterator = condutores.iterator(); iterator.hasNext();) {
				Condutor condutor = (Condutor) iterator.next();

				if (condutor.estaDePlantao(dataFormatadaOracle)) {
					continue;
				}

				if (!condutor.estaEscalado(dataSaida)) {
					iterator.remove();
				}
			}
		}
		return condutores;
	}

	public static List<Condutor> listarEscalados(boolean mostrarCanceladosEFinalizados, CpOrgaoUsuario cpOrgaoUsuario) throws Exception {
		return listarEscaladosDoComplexo(mostrarCanceladosEFinalizados, null, cpOrgaoUsuario);
	}

	public static List<Condutor> listarEscaladosDoComplexo(boolean mostrarCanceladosEFinalizados, CpComplexo cpComplexo, CpOrgaoUsuario cpOrgaoUsuario) throws Exception {

		List<Condutor> condutores;

		StringBuffer query = new StringBuffer();

		query.append("SELECT c FROM Condutor c");
		query.append(" WHERE c.id in ");
		query.append("(SELECT m.condutor.id FROM Missao m");
		query.append(" WHERE  m.condutor.id = c.id");
		query.append(" AND    c.dpPessoa.idPessoaIni in (select d.idPessoaIni from DpPessoa d where d.orgaoUsuario.idOrgaoUsu = " + cpOrgaoUsuario.getIdOrgaoUsu() + " AND DATA_FIM_PESSOA IS NULL)");

		if (!mostrarCanceladosEFinalizados) {
			query.append(" AND    m.estadoMissao != '" + EstadoMissao.CANCELADA + "'");
			query.append(" AND    m.estadoMissao != '" + EstadoMissao.FINALIZADA + "'");
		}
		if (cpComplexo != null) {
			query.append(" AND    m.cpComplexo.idComplexo = " + cpComplexo.getIdComplexo());
		}
		query.append(")");

		Query qry = AR.em().createQuery(query.toString());

		try {
			condutores = ((List<Condutor>) qry.getResultList());
			Collections.sort(condutores);
		} catch (NoResultException ex) {
			condutores = null;
		}
		return condutores;
	}

	public boolean estaDePlantao(String dataFormatadaOracle) {
		List<Plantao> plantoes = null;

		String qrl = "SELECT p.condutor.id FROM Plantao p" + " WHERE  p.condutor.id = " + this.id + " AND   (p.dataHoraInicio <= " + dataFormatadaOracle
				+ " AND   ( p.dataHoraFim = NULL OR p.dataHoraFim >= " + dataFormatadaOracle + "))";

		Query qry = AR.em().createQuery(qrl);
		try {
			plantoes = ((List<Plantao>) qry.getResultList());
			if (plantoes != null && !plantoes.isEmpty()) {
				return true;
			} else {
				return false;
			}

		} catch (NoResultException ex) {
			plantoes = null;
		}
		return false;
	}

	public Boolean estaEscalado(String dataMissao) throws Exception {
		Condutor condutor = this;
        
		Map<String, Object> parametros = new HashMap<String,Object>();
		parametros.put("condutor",condutor);
		EscalaDeTrabalho escalaVigente;
		String dataFormatadaOracle = FormatarDataHora.retorna_DataeHora(dataMissao);
		StringBuffer hqlVigentes = new StringBuffer();
		hqlVigentes.append("condutor = :condutor and ");
		hqlVigentes.append("dataVigenciaInicio < ");
		hqlVigentes.append(dataFormatadaOracle);
		hqlVigentes.append(" and ((dataVigenciaFim is null) or (dataVigenciaFim > ");
		hqlVigentes.append(dataFormatadaOracle);
		hqlVigentes.append(")) ");
		hqlVigentes.append("order by dataVigenciaInicio desc ");
		List<EscalaDeTrabalho> escalasDeTrabalho = EscalaDeTrabalho.AR.find(hqlVigentes.toString(), parametros).fetch();
		if (escalasDeTrabalho.isEmpty()) {
			return false;
		}

		escalaVigente = escalasDeTrabalho.get(0);

		if (escalasDeTrabalho.size() > 1) {
			throw new Exception(MessagesBundle.getMessage("condutor.escalasDeTrabalho.exception",escalasDeTrabalho.get(0).getCondutor().dpPessoa.getSigla()));
		}

		return escalaVigente.estaEscaladoNesteDia(dataMissao);
	}

	public static Boolean estaDisponivel(Missao m) throws Exception {
		SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String dataHoraSaidaStr = formatar.format(m.getDataHoraSaida().getTime());
		List<Condutor> condutores = listarDisponiveis(dataHoraSaidaStr, m.getId(), m.getCpOrgaoUsuario().getId(), m.getInicioRapido());

		if (condutores.contains(m.getCondutor())) {
			return true;
		}
		return false;
	}

	public static List<Condutor> listarTodos(CpOrgaoUsuario orgaoUsuario) throws Exception {
		List<Condutor> condutores = Condutor.AR.find("cpOrgaoUsuario", orgaoUsuario).fetch();
		Collections.sort(condutores);
		return condutores;
	}
	
	public static List<Condutor> listarTodos() throws Exception {
		List<Condutor> condutores = Condutor.AR.findAll();
		Collections.sort(condutores);
		return condutores;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Condutor other = (Condutor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static List<Condutor> listarFiltradoPor(CpOrgaoUsuario orgaoUsuario, DpLotacao lotacao) throws Exception {
		Map<String, Object> parametros = new HashMap<String,Object>();
		parametros.put("cpOrgaoUsuario",orgaoUsuario);
		parametros.put("idLotacaoIni",lotacao.getIdInicial());
		List<Condutor> condutores = Condutor.AR.find("cpOrgaoUsuario=:cpOrgaoUsuario and dpPessoa.lotacao.idLotacaoIni = :idLotacaoIni", parametros).fetch();
		Collections.sort(condutores);
		return condutores;
	}

	public static Condutor recuperarLogado(DpPessoa titular, CpOrgaoUsuario orgaoUsuario) {
		Map<String, Object> parametros = new HashMap<String,Object>();
		parametros.put("cpOrgaoUsuario",orgaoUsuario);
		parametros.put("idLotacaoIni",titular.getIdInicial());
		return Condutor.AR.find("dpPessoa.idPessoaIni=:idPessoaIni and cpOrgaoUsuario=:cpOrgaoUsuario", parametros).first();
	}

	public CategoriaCNH[] getCategorias() {
		return CategoriaCNH.values();
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CpOrgaoUsuario getCpOrgaoUsuario() {
		return cpOrgaoUsuario;
	}

	public void setCpOrgaoUsuario(CpOrgaoUsuario cpOrgaoUsuario) {
		this.cpOrgaoUsuario = cpOrgaoUsuario;
	}

	public DpPessoa getDpPessoa() {
		return dpPessoa;
	}

	public void setDpPessoa(DpPessoa dpPessoa) {
		this.dpPessoa = dpPessoa;
	}

	public CategoriaCNH getCategoriaCNH() {
		return categoriaCNH;
	}

	public void setCategoriaCNH(CategoriaCNH categoriaCNH) {
		this.categoriaCNH = categoriaCNH;
	}

	public Calendar getDataVencimentoCNH() {
		return dataVencimentoCNH;
	}

	public void setDataVencimentoCNH(Calendar dataVencimentoCNH) {
		this.dataVencimentoCNH = dataVencimentoCNH;
	}

	public String getNumeroCNH() {
		return numeroCNH;
	}

	public void setNumeroCNH(String numeroCNH) {
		this.numeroCNH = numeroCNH;
	}

	public String getTelefoneInstitucional() {
		return telefoneInstitucional;
	}

	public void setTelefoneInstitucional(String telefoneInstitucional) {
		this.telefoneInstitucional = telefoneInstitucional;
	}

	public List<EscalaDeTrabalho> getEscala() {
		return escala;
	}

	public void setEscala(List<EscalaDeTrabalho> escala) {
		this.escala = escala;
	}

	public String getEmailPessoal() {
		return emailPessoal;
	}

	public void setEmailPessoal(String emailPessoal) {
		this.emailPessoal = emailPessoal;
	}

	public String getTelefonePessoal() {
		return telefonePessoal;
	}

	public void setTelefonePessoal(String telefonePessoal) {
		this.telefonePessoal = telefonePessoal;
	}

	public String getCelularPessoal() {
		return celularPessoal;
	}

	public void setCelularPessoal(String celularPessoal) {
		this.celularPessoal = celularPessoal;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCelularInstitucional() {
		return celularInstitucional;
	}

	public void setCelularInstitucional(String celularInstitucional) {
		this.celularInstitucional = celularInstitucional;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Imagem getArquivo() {
		return arquivo;
	}

	public void setArquivo(Imagem arquivo) {
		this.arquivo = arquivo;
	}

	public String getSituacaoImagem() {
		return situacaoImagem;
	}

	public void setSituacaoImagem(String situacaoImagem) {
		this.situacaoImagem = situacaoImagem;
	}

	public byte[] getConteudoimagemblob() {
		return conteudoimagemblob;
	}

	public void setConteudoimagemblob(byte[] conteudoimagemblob) {
		this.conteudoimagemblob = conteudoimagemblob;
	}

	public String formatDateDDMMYYYY(Calendar cal) {
		return new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
	}
}
