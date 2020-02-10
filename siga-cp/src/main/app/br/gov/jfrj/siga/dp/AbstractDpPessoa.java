/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * Criado em  21/12/2005
 *
 */
package br.gov.jfrj.siga.dp;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

@MappedSuperclass
@NamedNativeQuery(name = "consultarDataEHoraDoServidor", query = "SELECT sysdate dt FROM dual", resultSetMapping = "scalar")
@NamedQueries({
		@NamedQuery(name = "consultarPorIdDpPessoa", query = "select pes from DpPessoa pes where pes.idPessoaIni = :idPessoa"),
		@NamedQuery(name = "consultarPorIdInicialDpPessoa", query = "select pes from DpPessoa pes where pes.idPessoaIni = :idPessoaIni and pes.dataFimPessoa = null"),
		@NamedQuery(name = "consultarPorSiglaDpPessoa", query = "select pes from DpPessoa pes where pes.matricula = :matricula and pes.sesbPessoa = :sesb and pes.dataFimPessoa = null"),
		@NamedQuery(name = "consultarPessoaAtualPelaInicial", query = "from DpPessoa pes "
				+ "		where pes.dataInicioPessoa = "
				+ "		(select max(p.dataInicioPessoa) from DpPessoa p where p.idPessoaIni = :idPessoaIni)"
				+ "		 and pes.idPessoaIni = :idPessoaIni"),
		@NamedQuery(name = "consultarPorIdInicialDpPessoaInclusiveFechadas", query = "select pes from DpPessoa pes where pes.idPessoaIni = :idPessoaIni"),
		@NamedQuery(name = "consultarPorCpf", query = "from DpPessoa pes where pes.cpfPessoa = :cpfPessoa and pes.dataFimPessoa is null"),
		@NamedQuery(name = "consultarPorEmail", query = "from DpPessoa pes where pes.emailPessoa = :emailPessoa and pes.dataFimPessoa is null"),
		@NamedQuery(name = "consultarPorEmailIgualCpfDiferente", query = "select count(pes) from DpPessoa pes where lower(pes.emailPessoa) = lower(:emailPessoa) and pes.cpfPessoa <> :cpf"),
		@NamedQuery(name = "consultarPorOrgaoUsuDpPessoaInclusiveFechadas", query = "from DpPessoa pes where pes.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu"),
		@NamedQuery(name = "consultarPorFiltroDpPessoa", query = "from DpPessoa pes "
				+ "  where ((upper(pes.nomePessoaAI) like upper('%' || :nome || '%')) or ((pes.sesbPessoa || pes.matricula) like upper('%' || :nome || '%')))"
				+ " and (:cpf = null or :cpf = 0 or pes.cpfPessoa = :cpf) "
				+ "  	and (:idOrgaoUsu = null or :idOrgaoUsu = 0 or pes.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu)"
				+ "	and (:lotacao = null or :lotacao = 0 or pes.lotacao.idLotacao = :lotacao)"
				+ " and (pes.id <> :id or :id = 0)"
				+ " and (:cargo = null or :cargo = 0 or pes.cargo.idCargo = :cargo) "
		      	+ " and (:funcao = null or :funcao = 0 or pes.funcaoConfianca.idFuncao = :funcao) "
		      	+ " and (:email = null or (upper(pes.emailPessoa) like upper('%' || :email || '%')) ) " 
				+ "	and (:situacaoFuncionalPessoa = null or pes.situacaoFuncionalPessoa = :situacaoFuncionalPessoa)"
				+ "   	and pes.dataFimPessoa = null"
				+ "   	order by pes.nomePessoa"),
		@NamedQuery(name = "consultarUsuariosComEnvioDeEmailPendenteFiltrandoPorLotacao", query = "select new br.gov.jfrj.siga.dp.DpPessoaUsuarioDTO(pes.idPessoa, pes.nomePessoa, pes.lotacao.nomeLotacao) from DpPessoa pes "
				+ "	 where pes.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu"
				+ " and pes.lotacao.idLotacao in (:idLotacaoLista)"
				+ " and pes.dataFimPessoa is null"
				+ " and not exists (select ident.dpPessoa.idPessoa from CpIdentidade ident where pes.idPessoa = ident.dpPessoa.idPessoa)"
				+ "   	order by pes.lotacao.nomeLotacao"),
		@NamedQuery(name = "consultarPorFiltroDpPessoaSemIdentidade", query = "from DpPessoa pes "
				+ "  where (upper(pes.nomePessoaAI) like upper('%' || :nome || '%'))"
				+ " and (pes.cpfPessoa = :cpf or :cpf = 0)"
				+ " and pes.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu"
				+ "	and (pes.lotacao.idLotacao = :lotacao or :lotacao = 0)"
				+ " and pes.dataFimPessoa = null"
				+ " and pes.id not in (select pes1.idPessoa from CpIdentidade i inner join i.dpPessoa pes1 where (upper(pes1.nomePessoaAI) like upper('%' || :nome || '%')) "
				+ 			" and (pes1.cpfPessoa = :cpf or :cpf = 0) and pes1.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu and (pes1.lotacao.idLotacao = :lotacao or :lotacao = 0) and pes1.dataFimPessoa = null)"
				+ "   	order by pes.cpfPessoa"),					
		@NamedQuery(name = "consultarQuantidadeDpPessoaSemIdentidade", query = "select count(pes) from DpPessoa pes "
				+ "  where (upper(pes.nomePessoaAI) like upper('%' || :nome || '%'))"
				+ " and (pes.cpfPessoa = :cpf or :cpf = 0)"
				+ " and pes.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu"
				+ "	and (pes.lotacao.idLotacao = :lotacao or :lotacao = 0)"
				+ " and pes.dataFimPessoa = null"
				+ " and pes.id not in (select pes1.idPessoa from CpIdentidade i inner join i.dpPessoa pes1 where (upper(pes1.nomePessoaAI) like upper('%' || :nome || '%')) "
				+ 			" and (pes1.cpfPessoa = :cpf or :cpf = 0) and pes1.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu and (pes1.lotacao.idLotacao = :lotacao or :lotacao = 0) and pes1.dataFimPessoa = null)"
				+ "   	order by pes.nomePessoa"),		
		@NamedQuery(name = "consultarQuantidadeDpPessoa", query = "select count(pes) from DpPessoa pes "
				+ "  where ((upper(pes.nomePessoaAI) like upper('%' || :nome || '%')) or ((pes.sesbPessoa || pes.matricula) like upper('%' || :nome || '%'))) "
				+ "  	and (:idOrgaoUsu = null or :idOrgaoUsu = 0 or pes.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu)"
				+ " and (:cpf = null or :cpf = 0 or pes.cpfPessoa = :cpf) "
				+ " and (:email = null or (upper(pes.emailPessoa) like upper('%' || :email || '%')) ) "
				+ "	and (:lotacao = null or :lotacao = 0 or pes.lotacao.idLotacao = :lotacao)"
				+ " and (:cargo = null or :cargo = 0 or pes.cargo.idCargo = :cargo) "
		      	+ " and (:funcao = null or :funcao = 0 or pes.funcaoConfianca.idFuncao = :funcao) "
				+ " and (pes.id <> :id or :id = 0)"
				+ "	and (:situacaoFuncionalPessoa = null or :situacaoFuncionalPessoa = '' or pes.situacaoFuncionalPessoa = :situacaoFuncionalPessoa)"
				+ "   	and pes.dataFimPessoa = null"
				+ "   	order by pes.nomePessoa"),
		@NamedQuery(name = "consultarPorFiltroDpPessoaInclusiveFechadas", query = "from DpPessoa pes where idPessoa in ("
				+ "	select max(pes.idPessoa)"
				+ "	from DpPessoa pes"
				+ "	where ((upper(pes.nomePessoaAI) like upper('%' || :nome || '%')) or ((pes.sesbPessoa || pes.matricula) like upper('%' || :nome || '%')))"
				+ "  	and (:idOrgaoUsu = null or :idOrgaoUsu = 0 or pes.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu)"
				+ " and (:cpf = null or :cpf = 0 or pes.cpfPessoa like '%' || :cpf || '%') "
				+ "  	and (:lotacao = null or :lotacao = 0 or pes.lotacao.idLotacao = :lotacao)"
				+ " and (:cargo = null or :cargo = 0 or pes.cargo.idCargo = :cargo) "
				+ " and (:funcao = null or :funcao = 0 or pes.funcaoConfianca.idFuncao = :funcao) "
				+ " and (:email = null or (upper(pes.emailPessoa) like upper('%' || :email || '%')) ) " 
				+ "	group by pes.idPessoaIni) order by upper(pes.nomePessoaAI)"),
		@NamedQuery(name = "consultarPessoaComOrgaoFuncaoCargo", query = "from DpPessoa pes"
				+ " left join fetch pes.cargo car "
				+ " left join fetch pes.funcaoConfianca fun "
				+ " left join fetch pes.lotacao lot "
				+ " where pes.idPessoa in ("
				+ "	select max(pes.idPessoa)"
				+ "	from DpPessoa pes"
				+ "	where ((upper(pes.nomePessoaAI) like upper('%' || :nome || '%')) or ((pes.sesbPessoa || pes.matricula) like upper('%' || :nome || '%')))"
				+ "  	and (:idOrgaoUsu = null or :idOrgaoUsu = 0 or pes.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu)"
				+ " and (:cpf = null or :cpf = 0 or pes.cpfPessoa like '%' || :cpf || '%') "
				+ "  	and (:lotacao = null or :lotacao = 0 or pes.lotacao.idLotacao = :lotacao)"
				+ " and (:cargo = null or :cargo = 0 or pes.cargo.idCargo = :cargo) "
				+ " and (:funcao = null or :funcao = 0 or pes.funcaoConfianca.idFuncao = :funcao) "
				+ " and (:email = null or (upper(pes.emailPessoa) like upper('%' || :email || '%'))) "
				+ "	group by pes.idPessoaIni) order by upper(pes.nomePessoaAI)"),
		@NamedQuery(name = "consultarQuantidadeDpPessoaInclusiveFechadas", query = "select count(distinct pes.idPessoaIni)"
				+ "		from DpPessoa pes"
				+ "		where ((upper(pes.nomePessoaAI) like upper('%' || :nome || '%')) or ((pes.sesbPessoa || pes.matricula) like upper('%' || :nome || '%')))"
				+ "  			and (:idOrgaoUsu = null or :idOrgaoUsu = 0 or pes.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu)"
				+ " and (:cpf = null or :cpf = 0 or pes.cpfPessoa like '%' || :cpf || '%') "
				+ "  			and (:lotacao = null or :lotacao = 0 or pes.lotacao.idLotacao = :lotacao)"
				+ " and (:email = null or (upper(pes.emailPessoa) like upper('%' || :email || '%')) ) "
				+ " and (:cargo = null or :cargo = 0 or pes.cargo.idCargo = :cargo) "
	      		+ " and (:funcao = null or :funcao = 0 or pes.funcaoConfianca.idFuncao = :funcao) "), 
		@NamedQuery(name = "consultarPorCpfMatricula", query = "from DpPessoa pes "
				+ "  where pes.cpfPessoa = :cpfPessoa"
				+ "    and pes.matricula = :matricula"
				+ "    and pes.dataFimPessoa is null"),
		@NamedQuery(name = "consultarAtivasNaDataOrgao", query = "from DpPessoa pes "
				+ "  where (:idOrgaoUsu = null or :idOrgaoUsu = 0 or pes.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu)"
				+ "  and  ((pes.dataInicioPessoa < :dt and pes.dataFimPessoa >= :dt )"
				+ "  or (pes.dataInicioPessoa < :dt and pes.dataFimPessoa is null ))"),
		@NamedQuery(name = "consultarPessoasComFuncaoConfianca", query = "from DpPessoa p where p.funcaoConfianca.idFuncao = :idFuncaoConfianca and p.dataFimPessoa is null"),
		@NamedQuery(name = "consultarPessoasComCargo", query = "from DpPessoa p where p.cargo.id = :idCargo and p.dataFimPessoa is null"),
		@NamedQuery(name = "consultarDadosBasicos", query = "select u,  pes from CpIdentidade as u join u.dpPessoa.pessoaInicial pes"
				+ "  where u.nmLoginIdentidade = :nmUsuario"
				+ "   and pes.sesbPessoa = :sesbPessoa"
				+ "   and u.dpPessoa.cpfPessoa = pes.cpfPessoa"
				+ "   and (u.hisDtFim is null)"
				+ "   and (u.dtCancelamentoIdentidade is null)"
				+ "   and (u.dtExpiracaoIdentidade is null or u.dtExpiracaoIdentidade > current_date())"
				+ "   and (pes.dataFimPessoa is null)"
				+ "   and (pes.situacaoFuncionalPessoa in ('1', '2', '31'))")
})
public abstract class AbstractDpPessoa extends DpResponsavel implements
		Serializable {

	@SequenceGenerator(name = "generator", sequenceName = "CORPORATIVO.DP_PESSOA_SEQ")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID_PESSOA", unique = true, nullable = false)
	@Desconsiderar
	private Long idPessoa;

	@Column(name = "ID_PESSOA_INICIAL")
	@Desconsiderar
	private Long idPessoaIni;

	@Column(name = "DATA_FIM_PESSOA", length = 19)
	@Temporal(TemporalType.TIMESTAMP)
	@Desconsiderar
	private Date dataFimPessoa;

	@Column(name = "DATA_INI_PESSOA", length = 19)
	@Temporal(TemporalType.TIMESTAMP)
	@Desconsiderar
	private Date dataInicioPessoa;

	@Column(name = "IDE_PESSOA", length = 256)
	private String idePessoa;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_NASC_PESSOA")
	private Date dataNascimento;

	@Column(name = "NOME_PESSOA", nullable = false, length = 60)
	private String nomePessoa;

	@Column(name = "CPF_PESSOA", nullable = false)
	private Long cpfPessoa;

	@Column(name = "MATRICULA")
	private Long matricula;

	@Column(name = "SESB_PESSOA", length = 2)
	private String sesbPessoa;

	@Column(name = "EMAIL_PESSOA", length = 60)
	private String emailPessoa;

	@Column(name = "SIGLA_PESSOA", length = 10)
	private String siglaPessoa;

	@Column(name = "DSC_PADRAO_REFERENCIA_PESSOA", length = 16)
	private String padraoReferencia;

	@Column(name = "SITUACAO_FUNCIONAL_PESSOA", length = 50)
	private String situacaoFuncionalPessoa;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_INICIO_EXERCICIO_PESSOA")
	private Date dataExercicioPessoa;

	@Column(name = "ATO_NOMEACAO_PESSOA", length = 50)
	private String atoNomeacao;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_NOMEACAO_PESSOA")
	private Date dataNomeacao;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_POSSE_PESSOA")
	private Date dataPosse;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_PUBLICACAO_PESSOA")
	private Date dataPublicacao;

	@Column(name = "GRAU_INSTRUCAO_PESSOA", length = 50)
	private String grauInstrucao;

	@Column(name = "ID_PROVIMENTO")
	private Integer idProvimento;

	@Column(name = "NACIONALIDADE_PESSOA", length = 60)
	private String nacionalidade;

	@Column(name = "NATURALIDADE_PESSOA", length = 60)
	private String naturalidade;

	@Column(name = "FG_IMPRIME_END", length = 1)
	private String imprimeEndereco;

	@Column(name = "SEXO_PESSOA", length = 1)
	private String sexo;

	@Column(name = "TP_SERVIDOR_PESSOA")
	private Integer tipoServidor;

	@Column(name = "TP_SANGUINEO_PESSOA", length = 3)
	private String tipoSanguineo;

	@Column(name = "ENDERECO_PESSOA", length = 100)
	private String endereco;

	@Column(name = "BAIRRO_PESSOA", length = 50)
	private String bairro;

	@Column(name = "CIDADE_PESSOA", length = 30)
	private String cidade;

	@Column(name = "CEP_PESSOA", length = 8)
	private String cep;

	@Column(name = "TELEFONE_PESSOA", length = 30)
	private String telefone;

	@Column(name = "RG_PESSOA", length = 20)
	private String identidade;

	@Column(name = "RG_ORGAO_PESSOA", length = 50)
	private String orgaoIdentidade;

	@Temporal(TemporalType.DATE)
	@Column(name = "RG_DATA_EXPEDICAO_PESSOA")
	private Date dataExpedicaoIdentidade;

	@Column(name = "RG_UF_PESSOA", length = 2)
	private String ufIdentidade;

	@Column(name = "ID_ESTADO_CIVIL")
	private Integer idEstadoCivil;

	@Column(name = "NOME_EXIBICAO")
	@Desconsiderar
	private String nomeExibicao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PESSOA_INICIAL", insertable = false, updatable = false)
	@Desconsiderar
	private DpPessoa pessoaInicial;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTACAO")
	private DpLotacao lotacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CARGO")
	private DpCargo cargo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FUNCAO_CONFIANCA")
	private DpFuncaoConfianca funcaoConfianca;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO_USU")
	@Desconsiderar
	private CpOrgaoUsuario orgaoUsuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_PESSOA")
	private CpTipoPessoa cpTipoPessoa;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pessoaInicial")
	@OrderBy("idPessoa DESC")
	@Desconsiderar
	// private Set<DpPessoa> pessoasPosteriores = new HashSet<DpPessoa>(0);
	private Set<DpPessoa> pessoasPosteriores;

	/**
	 * @return the cpTipoPessoa
	 */
	public CpTipoPessoa getCpTipoPessoa() {
		return cpTipoPessoa;
	}

	/**
	 * @param cpTipoPessoa
	 *            the cpTipoPessoa to set
	 */
	public void setCpTipoPessoa(CpTipoPessoa cpTipoPessoa) {
		this.cpTipoPessoa = cpTipoPessoa;
	}

	public String getNacionalidade() {
		return nacionalidade;
	}

	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public String getNaturalidade() {
		return naturalidade;
	}

	public void setNaturalidade(String naturalidade) {
		this.naturalidade = naturalidade;
	}

	public String getImprimeEndereco() {
		return imprimeEndereco;
	}

	public void setImprimeEndereco(String imprimeEndereco) {
		this.imprimeEndereco = imprimeEndereco;
	}

	public Integer getIdEstadoCivil() {
		return idEstadoCivil;
	}

	public void setIdEstadoCivil(Integer idEstadoCivil) {
		this.idEstadoCivil = idEstadoCivil;
	}

	public Integer getIdProvimento() {
		return idProvimento;
	}

	public void setIdProvimento(Integer idProvimento) {
		this.idProvimento = idProvimento;
	}

	public String getGrauInstrucao() {
		return grauInstrucao;
	}

	public void setGrauInstrucao(String grauInstrucao) {
		this.grauInstrucao = grauInstrucao;
	}

	public String getAtoNomeacao() {
		return atoNomeacao;
	}

	public void setAtoNomeacao(String atoNomeacao) {
		this.atoNomeacao = atoNomeacao;
	}

	public Date getDataExercicioPessoa() {
		return dataExercicioPessoa;
	}

	public void setDataExercicioPessoa(Date dataExercicioPessoa) {
		this.dataExercicioPessoa = dataExercicioPessoa;
	}

	public String getIdePessoa() {
		return idePessoa;
	}

	public void setIdePessoa(String idePessoa) {
		this.idePessoa = idePessoa;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object rhs) {
		if ((rhs == null) || !(rhs instanceof DpPessoa))
			return false;
		final DpPessoa that = (DpPessoa) rhs;

		if ((this.getIdPessoa() == null ? that.getIdPessoa() == null : this
				.getIdPessoa().equals(that.getIdPessoa())))
			return true;
		return false;

	}

	/**
	 * @return Retorna o atributo cargo.
	 */
	public DpCargo getCargo() {
		return cargo;
	}

	/**
	 * @return Retorna o atributo cpfPessoa.
	 */
	public Long getCpfPessoa() {
		return cpfPessoa;
	}

	/**
	 * @return Retorna o atributo dataFimPessoa.
	 */
	public Date getDataFimPessoa() {
		return dataFimPessoa;
	}

	/**
	 * @return Retorna o atributo dataInicioPessoa.
	 */
	public Date getDataInicioPessoa() {
		return dataInicioPessoa;
	}

	/**
	 * @return Retorna o atributo dataNascimento.
	 */
	public Date getDataNascimento() {
		return dataNascimento;
	}

	public String getEmailPessoa() {
		return emailPessoa;
	}

	/**
	 * @return Retorna o atributo funcaoConfianca.
	 */
	public DpFuncaoConfianca getFuncaoConfianca() {
		return funcaoConfianca;
	}

	/**
	 * @return Retorna o atributo idPessoa.
	 */
	public Long getIdPessoa() {
		return idPessoa;
	}

	public Long getIdPessoaIni() {
		return idPessoaIni;
	}

	/**
	 * @return Retorna o atributo lotacao.
	 */
	public DpLotacao getLotacao() {
		return lotacao;
	}

	/**
	 * @return Retorna o atributo matricula.
	 */
	public Long getMatricula() {
		return matricula;
	}

	/**
	 * @return Retorna o atributo nomePessoa.
	 */
	public String getNomePessoa() {
		return nomePessoa;
	}

	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	public String getSesbPessoa() {
		return sesbPessoa;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
		final int idValue = this.getIdPessoa() == null ? 0 : this.getIdPessoa()
				.hashCode();
		result = result * 37 + idValue;

		return result;
	}

	/**
	 * @param cargo
	 *            Atribui a cargo o valor.
	 */
	public void setCargo(final DpCargo cargo) {
		this.cargo = cargo;
	}

	/**
	 * @param cpfPessoa
	 *            Atribui a cpfPessoa o valor.
	 */
	public void setCpfPessoa(final Long cpfPessoa) {
		this.cpfPessoa = cpfPessoa;
	}

	/**
	 * @param dataFimPessoa
	 *            Atribui a dataFimPessoa o valor.
	 */
	public void setDataFimPessoa(final Date dataFimPessoa) {
		this.dataFimPessoa = dataFimPessoa;
	}

	/**
	 * @param dataInicioPessoa
	 *            Atribui a dataInicioPessoa o valor.
	 */
	public void setDataInicioPessoa(final Date dataInicioPessoa) {
		this.dataInicioPessoa = dataInicioPessoa;
	}

	/**
	 * @param dataNascimento
	 *            Atribui a dataNascimento o valor.
	 */
	public void setDataNascimento(final Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public void setEmailPessoa(final String emailPessoa) {
		this.emailPessoa = emailPessoa;
	}

	/**
	 * @param funcaoConfianca
	 *            Atribui a funcaoConfianca o valor.
	 */
	public void setFuncaoConfianca(final DpFuncaoConfianca funcaoConfianca) {
		this.funcaoConfianca = funcaoConfianca;
	}

	/**
	 * @param idPessoa
	 *            Atribui a idPessoa o valor.
	 */
	public void setIdPessoa(final Long idPessoa) {
		this.idPessoa = idPessoa;
	}

	public void setIdPessoaIni(Long idPessoaIni) {
		this.idPessoaIni = idPessoaIni;
	}

	/**
	 * @param lotacao
	 *            Atribui a lotacao o valor.
	 */
	public void setLotacao(final DpLotacao lotacao) {
		this.lotacao = lotacao;
	}

	/**
	 * @param matricula
	 *            Atribui a matricula o valor.
	 */
	public void setMatricula(final Long matricula) {
		this.matricula = matricula;
	}

	/**
	 * @param nomePessoa
	 *            Atribui a nomePessoa o valor.
	 */
	public void setNomePessoa(final String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

	public void setSesbPessoa(final String sesbPessoa) {
		this.sesbPessoa = sesbPessoa;
	}

	public String getPadraoReferencia() {
		return padraoReferencia;
	}

	public void setPadraoReferencia(String padraoReferencia) {
		this.padraoReferencia = padraoReferencia;
	}

	public String getSituacaoFuncionalPessoa() {
		return situacaoFuncionalPessoa;
	}

	public void setSituacaoFuncionalPessoa(String situacaoFuncionalPessoa) {
		this.situacaoFuncionalPessoa = situacaoFuncionalPessoa;
	}

	public DpPessoa getPessoaInicial() {
		return pessoaInicial;
	}

	public void setPessoaInicial(DpPessoa pessoaInicial) {
		this.pessoaInicial = pessoaInicial;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getSiglaPessoa() {
		return siglaPessoa;
	}

	public void setSiglaPessoa(String siglaPessoa) {
		this.siglaPessoa = siglaPessoa;
	}

	/*
	 * public String getSituacaoFuncional() { return situacaoFuncional; }
	 * 
	 * public void setSituacaoFuncional(String situacaoFuncional) {
	 * this.situacaoFuncional = situacaoFuncional; }
	 */

	public Integer getTipoServidor() {
		return tipoServidor;
	}

	public void setTipoServidor(Integer tipoServidor) {
		this.tipoServidor = tipoServidor;
	}

	public String getTipoSanguineo() {
		return tipoSanguineo;
	}

	public void setTipoSanguineo(String tipoSanguineo) {
		this.tipoSanguineo = tipoSanguineo;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getIdentidade() {
		return identidade;
	}

	public void setIdentidade(String identidade) {
		this.identidade = identidade;
	}

	public String getOrgaoIdentidade() {
		return orgaoIdentidade;
	}

	public void setOrgaoIdentidade(String orgaoIdentidade) {
		this.orgaoIdentidade = orgaoIdentidade;
	}

	public Date getDataExpedicaoIdentidade() {
		return dataExpedicaoIdentidade;
	}

	public void setDataExpedicaoIdentidade(Date dataExpedicaoIdentidade) {
		this.dataExpedicaoIdentidade = dataExpedicaoIdentidade;
	}

	public String getUfIdentidade() {
		return ufIdentidade;
	}

	public void setUfIdentidade(String ufIdentidade) {
		this.ufIdentidade = ufIdentidade;
	}

	public Date getDataNomeacao() {
		return dataNomeacao;
	}

	public void setDataNomeacao(Date dataNomeacao) {
		this.dataNomeacao = dataNomeacao;
	}

	public Date getDataPosse() {
		return dataPosse;
	}

	public void setDataPosse(Date dataPosse) {
		this.dataPosse = dataPosse;
	}

	public Date getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public Set<DpPessoa> getPessoasPosteriores() {
		return pessoasPosteriores;
	}

	public void setPessoasPosteriores(Set<DpPessoa> pessoasPosteriores) {
		this.pessoasPosteriores = pessoasPosteriores;
	}

	/**
	 * Define o nome de exibição (apelido ou nome com pronome de tratamento, por
	 * exemplo)
	 * 
	 * @param nomeExibicao
	 */
	public void setNomeExibicao(String nomeExibicao) {
		this.nomeExibicao = nomeExibicao;
	}

	/**
	 * Retorna o nome de exibição (apelido ou nome com pronome de tratamento,
	 * por exemplo)
	 * 
	 * @return
	 */
	public String getNomeExibicao() {
		return nomeExibicao;
	}

}
