
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
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.CpArquivo;
import br.gov.jfrj.siga.cp.CpArquivoTipoArmazenamentoEnum;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

/**
 * A class that represents a row in the EX_MOVIMENTACAO table. You can customize
 * the behavior of this class by editing the class, {@link ExMovimentacao()}.
 */
@SuppressWarnings("serial")
@MappedSuperclass
@NamedQueries({ @NamedQuery(name = "consultarPorSigla", query = "select mob from ExMobil mob"
		+ "                inner join fetch mob.exDocumento doc" + "                where ("
		+ "                mob.numSequencia=:numSequencia"
		+ "                and mob.exTipoMobil.idTipoMobil=:idTipoMobil"
		+ "                and (:idOrgaoUsu = null or :idOrgaoUsu = 0L or doc.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu)"
		+ "                and doc.idDoc=mob.exDocumento.idDoc" + "                and doc.anoEmissao=:anoEmissao"
		+ "                and doc.exFormaDocumento.idFormaDoc=:idFormaDoc"
		+ "                and doc.numExpediente=:numExpediente)"),
		// Somente os "em andamento" ou "pendentes de assinatura"
		@NamedQuery(name = "consultarParaTransferirEmLote", query = "select mob from ExMobil mob join mob.exMarcaSet mar"
				+ "                where (mar.dpLotacaoIni.idLotacao=:lotaIni"
				+ "                and (mar.cpMarcador.idMarcador=2)"
				+ "                ) order by mar.dtIniMarca desc"),
		@NamedQuery(name = "consultarQuantidadeParaTransferirEmLote", query = "select COUNT(mob) from ExMobil mob join mob.exMarcaSet mar"
				+ "                where (mar.dpLotacaoIni.idLotacao=:lotaIni"
				+ "                and (mar.cpMarcador.idMarcador=2)"
				+ "                ) order by mar.dtIniMarca desc"),
		// Somente os "a receber"
		@NamedQuery(name = "consultarParaReceberEmLote", query = "select mob from ExMobil mob join mob.exMarcaSet mar"
				+ "                where (mar.dpLotacaoIni.idLotacao=:lotaIni"
				+ "                and (mar.cpMarcador.idMarcador=3"
				+ "                or mar.cpMarcador.idMarcador=14)"
				+ "                ) order by mar.dtIniMarca desc"),
		// Somente os "em andamento", "Transferido para Órgão Externo"
		@NamedQuery(name = "consultarParaArquivarCorrenteEmLote", query = "select mob from ExMobil mob join mob.exMarcaSet mar"
				+ "                where (mar.dpLotacaoIni.idLotacao=:lotaIni"
				+ "                and (mar.cpMarcador.idMarcador=2 or mar.cpMarcador.idMarcador=11)"
				+ "                ) order by mar.dtIniMarca desc"),
		// Somente os "a recolher para arquivo intermediário"
		@NamedQuery(name = "consultarParaArquivarIntermediarioEmLote", query = "select mob, mar from ExMobil mob join mob.exMarcaSet mar"
				+ "                where mar.cpMarcador.idMarcador=51              "
				+ "                and mar.dpLotacaoIni.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu"
				+ "                and (mar.dtIniMarca is null or mar.dtIniMarca < :dbDatetime)"
				+ "                and (mar.dtFimMarca is null or mar.dtFimMarca > :dbDatetime)"
				+ "                order by mob.exDocumento.dtDoc asc"),

		@NamedQuery(name = "consultarQuantidadeParaArquivarIntermediarioEmLote", query = "select count(*) from ExMobil mob join mob.exMarcaSet mar"
				+ "                where mar.cpMarcador.idMarcador=51              "
				+ "                and mar.dpLotacaoIni.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu"
				+ "                and (mar.dtIniMarca is null or mar.dtIniMarca < :dbDatetime)"
				+ "                and (mar.dtFimMarca is null or mar.dtFimMarca > :dbDatetime)"),
		// Somente os "a recolher para arquivo permanente"
		@NamedQuery(name = "consultarParaArquivarPermanenteEmLote", query = "select mob, mar from ExMobil mob join mob.exMarcaSet mar"
				+ "                where mar.cpMarcador.idMarcador=50      "
				+ "                and mar.dpLotacaoIni.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu"
				+ "                and (mar.dtIniMarca is null or mar.dtIniMarca < :dbDatetime)"
				+ "                and (mar.dtFimMarca is null or mar.dtFimMarca > :dbDatetime)"
				+ "                order by mob.exDocumento.dtDoc asc"),
		@NamedQuery(name = "consultarQuantidadeParaArquivarPermanenteEmLote", query = "select count(*) from ExMobil mob join mob.exMarcaSet mar"
				+ "                where mar.cpMarcador.idMarcador=50              "
				+ "                and mar.dpLotacaoIni.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu"
				+ "                and (mar.dtIniMarca is null or mar.dtIniMarca < :dbDatetime)"
				+ "                and (mar.dtFimMarca is null or mar.dtFimMarca > :dbDatetime)"),
		// Somente os "a eliminar"
		@NamedQuery(name = "consultarAEliminar", query = "select mob, mar from ExMobil mob join mob.exMarcaSet mar"
				+ "                where (mar.cpMarcador.idMarcador=7)"
				+ "                and mar.dpLotacaoIni.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu"
				+ "                and (:dtIni is null or mob.exDocumento.dtDoc >= :dtIni)"
				+ "                and (:dtFim is null or mob.exDocumento.dtDoc <= :dtFim)"
				+ "                and (mar.dtIniMarca is null or mar.dtIniMarca < :dbDatetime)"
				+ "                and (mar.dtFimMarca is null or mar.dtFimMarca > :dbDatetime)"),
		@NamedQuery(name = "consultarQuantidadeAEliminar", query = "select count(*) from ExMobil mob join mob.exMarcaSet mar"
				+ "                where mar.cpMarcador.idMarcador=7              "
				+ "                and mar.dpLotacaoIni.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu"
				+ "                and (:dtIni is null or mob.exDocumento.dtDoc >= :dtIni)"
				+ "                and (:dtFim is null or mob.exDocumento.dtDoc <= :dtFim)"
				+ "                and (mar.dtIniMarca is null or mar.dtIniMarca < :dbDatetime)"
				+ "                and (mar.dtFimMarca is null or mar.dtFimMarca > :dbDatetime)"),
		// Somente os "em edital de eliminação"
		@NamedQuery(name = "consultarEmEditalEliminacao", query = "select mob, mar" + "                from ExMobil mob"
				+ "                join mob.exMarcaSet mar" + "                join mob.exDocumento doc"
				+ "                where (mar.cpMarcador.idMarcador=52)"
				+ "                and mar.dpLotacaoIni.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu"
				+ "                and (:dtIni is null or doc.dtDoc >= :dtIni)"
				+ "                and (:dtFim is null or doc.dtDoc <= :dtFim)"
				+ "                order by doc.dtDoc desc"),
		// Somente os "em andamento" ou "pendentes de assinatura"
		@NamedQuery(name = "consultarParaAnotarEmLote", query = "select mob from ExMobil mob join mob.exMarcaSet mar"
				+ "                where (mar.dpLotacaoIni.idLotacao=:lotaIni"
				+ "                and (mar.cpMarcador.idMarcador=2 or mar.cpMarcador.idMarcador=15)              "
				+ "                ) order by mar.dtIniMarca desc"),
		@NamedQuery(name = "listarAnexoPendenteAssinatura", query = "select mov from ExMovimentacao mov join mov.exMobil mobil "
				+ "					where mobil in (select distinct(mob) from ExMobil mob join mob.exMarcaSet mar"
				+ "               		where mar.cpMarcador.idMarcador = 30) and (mov.exTipoMovimentacao.idTpMov = 2)"
				+ "					and mov.subscritor.idPessoaIni = :idPessoaIni"
				+ "					order by mov.dtIniMov desc"),
		@NamedQuery(name = "listarDespachoPendenteAssinatura", query = "select mov from ExMovimentacao mov join mov.exMobil mobil "
				+ "					where mobil in (select distinct(mob) from ExMobil mob join mob.exMarcaSet mar"
				+ "               		where mar.cpMarcador.idMarcador = 29) and (mov.exTipoMovimentacao.idTpMov = 5 or mov.exTipoMovimentacao.idTpMov = 6 "
				+ "               			or mov.exTipoMovimentacao.idTpMov = 7 or mov.exTipoMovimentacao.idTpMov = 8 or mov.exTipoMovimentacao.idTpMov = 18)"
				+ "					and mov.subscritor.idPessoaIni = :idPessoaIni"
				+ "					order by mov.dtIniMov desc"),
		// Somente os "em transferencia", "em transferencia eletronica" ou
		// "transferido para orgao externo"
		@NamedQuery(name = "consultarParaViaDeProtocolo", query = "select mob from ExMobil mob join mob.exMarcaSet mar"
				+ "                where (mar.dpLotacaoIni.idLotacao=:lotaIni"
				+ "                and (mar.cpMarcador.idMarcador=23 or mar.cpMarcador.idMarcador=24 or mar.cpMarcador.idMarcador=11)              "
				+ "                ) order by mar.dtIniMarca desc"),
		// Somente os "em andamento" ou "pendentes de assinatura"
		@NamedQuery(name = "consultarMovimentacoesPorLotacaoEntreDatas", query = "select mov from ExMovimentacao mov"
				+ "                inner join mov.exMobil mob" + "                inner join mob.exDocumento doc"
				+ "                inner join mov.lotaResp lot"
				+ "                where (doc.idDoc=mob.exDocumento.idDoc"
				+ "                and mob.idMobil=mov.exMobil.idMobil"
				+ "                and lot.idLotacao=mov.lotaResp.idLotacao"
				+ "                and (lot.idLotacaoIni=:lotaTitular or 0 = :lotaTitular)" + "                )"),
		// Voltar todas as movimentacoes realizadas por uma determinada pessoa
		// em um exato momento. Usado principalmente para gerar segunda-via de
		// protocolos. 
		@NamedQuery(name = "consultarMovimentacoes", query = "from ExMovimentacao mov"
				+ "                where mov.cadastrante.idPessoaIni=:pessoaIni and mov.dtIniMov=to_date(:data, 'DD/MM/YYYY HH24:MI:SS')          "
				+ "                order by mov.dtTimestamp"), 
		@NamedQuery(name = AbstractExMovimentacao.CONSULTAR_TRAMITACOES_POR_MOVIMENTACAO_NAMED_QUERY, query = AbstractExMovimentacao.CONSULTAR_TRAMITACOES_POR_MOVIMENTACAO_QUERY),
		@NamedQuery(name = AbstractExMovimentacao.CONSULTAR_TRAMITACOES_POR_MOVIMENTACAO_DOC_CANCELADO_NAMED_QUERY, query = AbstractExMovimentacao.CONSULTAR_TRAMITACOES_POR_MOVIMENTACAO_DOC_CANCELADO_QUERY),
})
public abstract class AbstractExMovimentacao extends ExArquivo implements Serializable {

	private static final String CONSULTAR_TRAMITACOES_POR_MOVIMENTACAO_BEGIN = "SELECT mov FROM ExMovimentacao mov WHERE ";

	private static final String CONSULTAR_TRAMITACOES_POR_MOVIMENTACAO_END = //
			"AND (" //
					+ " (mov.exTipoMovimentacao.idTpMov = " + ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA
					+ ") OR"//
							// Recebimento não exibido! apenas para indicar o instante de recebimento da tramitação.
					+ " (mov.exTipoMovimentacao.idTpMov = " + ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO + ") OR"//
					+ " (mov.exTipoMovimentacao.idTpMov = " + ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA + ") OR"//
					+ " (mov.exTipoMovimentacao.idTpMov = " + ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE
					+ ") OR"//
					+ " (mov.exTipoMovimentacao.idTpMov = "
					+ ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO + ") OR"//
					+ " (mov.exTipoMovimentacao.idTpMov = "
					+ ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE + ") OR"//
					+ " (mov.exTipoMovimentacao.idTpMov = "
					+ ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_INTERMEDIARIO + ") OR"//
					+ " (mov.exTipoMovimentacao.idTpMov = " + ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA
					+ ") OR"//
					+ " (mov.exTipoMovimentacao.idTpMov = "
					+ ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO + ") OR"//
					+ " (mov.exTipoMovimentacao.idTpMov = " + ExTipoMovimentacao.TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO
					+ ")"//
					+ ") " //
					+ "ORDER BY mov.dtTimestamp DESC";

	/**
	 * Nome da {@link NamedQuery} usada para a consulta das {@link ExMovimentacao
	 * Movimentações} para o histórico de tramitações de uma {@link ExMobil}
	 * relacionada a um determinado {@link ExDocumento Documento} em ordem
	 * cronológica decrescente ({@link ExMovimentacao#getDtTimestamp()}) a partir da
	 * primeira {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_TRANSFERENCIA
	 * tramitação} dessa {@link ExMobil} . As movimentações retornadas devm ser dos
	 * seguintes {@link ExMovimentacao#getExTipoMovimentacao() Tipos}:
	 * <ul>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_TRANSFERENCIA }
	 * (Tramitação)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_RECEBIMENTO }</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_JUNTADA } (Juntada)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE }
	 * (Arquivamento Corrente)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO }
	 * (Arquivamento Intermediário)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE }
	 * (Desarquivamento)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_DESARQUIVAMENTO_INTERMEDIARIO }
	 * (Desarquivamento Intermediário)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA }</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO }
	 * (Cancelamento de Movimentação)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO }
	 * (Cancelamento)</li>
	 * </ul>
	 * As movimentações do tipo
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_RECEBIMENTO } não serão exibidas.
	 * Elas são apenas usadas para indicar a hora de recebimento da Movimentação de
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_TRANSFERENCIA } imediatamente
	 * anterior.
	 */
	public static final String CONSULTAR_TRAMITACOES_POR_MOVIMENTACAO_NAMED_QUERY = //
			"ExMovimentacao.consultarTramitacoesPorMovimentacao";

	static final String CONSULTAR_TRAMITACOES_POR_MOVIMENTACAO_QUERY = //
			CONSULTAR_TRAMITACOES_POR_MOVIMENTACAO_BEGIN //
					+ "mov.exMobil.idMobil = :idMobil " //
					+ "AND mov.dtTimestamp >= (SELECT MIN(tramitacao.dtTimestamp) " //
					+ "FROM ExMovimentacao tramitacao "
					+ "WHERE tramitacao.exMobil.idMobil = :idMobil AND tramitacao.exTipoMovimentacao.idTpMov = "
					+ ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA + ") " //
					+ CONSULTAR_TRAMITACOES_POR_MOVIMENTACAO_END;

	/**
	 * Nome da {@link NamedQuery} usada para a consulta das {@link ExMovimentacao
	 * Movimentações} para o histórico de tramitações do {@link ExDocumento
	 * Documento} Cancelado de uma {@link ExMobil} relacionada a um determinado em
	 * ordem cronológica decrescente ( {@link ExMovimentacao#getDtTimestamp()}) a
	 * partir da primeira {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_TRANSFERENCIA
	 * tramitação} das {@link ExMobil}s do Documento. As movimentações retornadas
	 * devm ser dos seguintes {@link ExMovimentacao#getExTipoMovimentacao() Tipos}:
	 * <ul>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_TRANSFERENCIA }
	 * (Tramitação)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_RECEBIMENTO }</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_JUNTADA } (Juntada)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE }
	 * (Arquivamento Corrente)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO }
	 * (Arquivamento Intermediário)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE }
	 * (Desarquivamento)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_DESARQUIVAMENTO_INTERMEDIARIO }
	 * (Desarquivamento Intermediário)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA }</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO }
	 * (Cancelamento de Movimentação)</li>
	 * <li>{@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO }
	 * (Cancelamento)</li>
	 * </ul>
	 * As movimentações do tipo
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_RECEBIMENTO } não serão exibidas.
	 * Elas são apenas usadas para indicar a hora de recebimento da Movimentação de
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_TRANSFERENCIA } imediatamente
	 * anterior.
	 */
	public static final String CONSULTAR_TRAMITACOES_POR_MOVIMENTACAO_DOC_CANCELADO_NAMED_QUERY = //
			"ExMovimentacao.consultarTramitacoesPorMovimentacaoDocumentoCancelado";

	static final String CONSULTAR_TRAMITACOES_POR_MOVIMENTACAO_DOC_CANCELADO_QUERY = //
			CONSULTAR_TRAMITACOES_POR_MOVIMENTACAO_BEGIN //
					+ "mov.exMobil.exDocumento = (SELECT mobBase.exDocumento FROM ExMobil mobBase WHERE mobBase.idMobil = :idMobil) "
					+ "AND mov.dtTimestamp >= (SELECT MIN(tramitacao.dtTimestamp) " //
					+ "FROM ExMovimentacao tramitacao "
					+ "WHERE tramitacao.exMobil.exDocumento = mov.exMobil.exDocumento AND tramitacao.exTipoMovimentacao.idTpMov = "
					+ ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA + ") " //
					+ CONSULTAR_TRAMITACOES_POR_MOVIMENTACAO_END;

	@Id
	@SequenceGenerator(sequenceName = "EX_MOVIMENTACAO_SEQ", name = "EX_MOVIMENTACAO_SEQ")
	@GeneratedValue(generator = "EX_MOVIMENTACAO_SEQ")
	@Column(name = "ID_MOV", unique = true, nullable = false)
	private Long idMov;
	
	@Transient
	protected byte[] cacheConteudoBlobMov;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cadastrante")
	private DpPessoa cadastrante;

	@Lob
	@Column(name = "conteudo_blob_mov")
	@Basic(fetch = FetchType.LAZY)
	private byte[] conteudoBlobMov;

	@Column(name = "conteudo_tp_mov", length = 128)
	private String conteudoTpMov;

	@BatchSize(size=1)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exMovimentacaoRef")
	private java.util.Set<ExMovimentacao> exMovimentacaoReferenciadoraSet;

	@Column(name = "descr_mov", length = 500)
	private String descrMov;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_destino_final")
	private DpPessoa destinoFinal;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_fim_mov", length = 19)
	private Date dtFimMov;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_ini_mov", nullable = false, length = 19)
	private Date dtIniMov;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_disp_publicacao", length = 19)
	private Date dtDispPublicacao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_efetiva_disp_publicacao", length = 19)
	private Date dtEfetivaDispPublicacao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_efetiva_publicacao", length = 19)
	private Date dtEfetivaPublicacao;

	@Column(name = "num_trf_publicacao")
	private Long numTRFPublicacao;

	@Column(name = "pag_publicacao", length = 15)
	private String pagPublicacao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_mov", nullable = false, length = 19)
	private Date dtMov;

	@Column(name = "obs_orgao_mov", length = 256)
	private String obsOrgao;

	@Column(name = "nm_funcao_subscritor", length = 128)
	private String nmFuncaoSubscritor;

	// private ExDocumento exDocumento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_nivel_acesso")
	private ExNivelAcesso exNivelAcesso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_orgao")
	private CpOrgao orgaoExterno;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_mob_ref")
	private ExMobil exMobilRef;

	// private ExEstadoDoc exEstadoDoc;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_mov_canceladora")
	private ExMovimentacao exMovimentacaoCanceladora;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_mov_ref")
	private ExMovimentacao exMovimentacaoRef;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tp_despacho")
	private ExTipoDespacho exTipoDespacho;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_classificacao")
	private ExClassificacao exClassificacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tp_mov", nullable = false)
	private ExTipoMovimentacao exTipoMovimentacao;

	// private Long idTpMov;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_lota_cadastrante")
	private DpLotacao lotaCadastrante;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_lota_destino_final")
	private DpLotacao lotaDestinoFinal;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_lota_subscritor")
	private DpLotacao lotaSubscritor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_lota_resp")
	private DpLotacao lotaResp;

	@Column(name = "nm_arq_mov", length = 256)
	private String nmArqMov;

	// private Integer numVia;

	// private Integer numViaDocPai;

	// private Integer numViaDocRef;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_resp")
	private DpPessoa resp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_subscritor")
	private DpPessoa subscritor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_titular")
	private DpPessoa titular;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_lota_titular")
	private DpLotacao lotaTitular;

	@Column(name = "caderno_publicacao_dje", length = 1)
	private String cadernoPublicacaoDje;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_mobil")
	private ExMobil exMobil;

	@Column(name = "num_paginas_ori")
	private Integer numPaginasOri;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_papel")
	private ExPapel exPapel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_marcador")
	private CpMarcador marcador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_identidade_audit")
	private CpIdentidade auditIdentidade;

	@Column(name = "ip_audit", length = 20)
	private String auditIP;

	@Column(name = "hash_audit", length = 1024)
	private String auditHash;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_timestamp", insertable=false, updatable=false)
	private Date dtTimestamp;

	@ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_ARQ")
	private CpArquivo cpArquivo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_param1", length = 19)
	private Date dtParam1;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_param2", length = 19)
	private Date dtParam2;

	public void setNumPaginasOri(Integer numPaginasOri) {
		this.numPaginasOri = numPaginasOri;
	}

	public Integer getNumPaginasOri() {
		return numPaginasOri;
	}

	public DpPessoa getTitular() {
		return titular;
	}

	public void setTitular(DpPessoa titular) {
		this.titular = titular;
	}

	public AbstractExMovimentacao() {
	}

	public AbstractExMovimentacao(final Long idMov) {
		setIdMov(idMov);
	}

	public DpPessoa getCadastrante() {
		return cadastrante;
	}

	public String getDescrMov() {
		return descrMov;
	}

	public DpPessoa getDestinoFinal() {
		return destinoFinal;
	}

	public Date getDtFimMov() {
		return dtFimMov;
	}

	public Date getDtIniMov() {
		return dtIniMov;
	}

	public Date getDtMov() {
		return dtMov;
	}

	public ExMovimentacao getExMovimentacaoCanceladora() {
		return exMovimentacaoCanceladora;
	}

	public ExTipoDespacho getExTipoDespacho() {
		return exTipoDespacho;
	}

	public ExClassificacao getExClassificacao() {
		return exClassificacao;
	}

	public ExTipoMovimentacao getExTipoMovimentacao() {
		return exTipoMovimentacao;
	}

	public Long getIdMov() {
		return idMov;
	}

	public DpLotacao getLotaCadastrante() {
		return lotaCadastrante;
	}

	public DpLotacao getLotaResp() {
		return lotaResp;
	}

	public String getNmArqMov() {
		return nmArqMov;
	}

	public DpPessoa getResp() {
		return resp;
	}

	public DpPessoa getSubscritor() {
		return subscritor;
	}

	// public void setIdTpMov(final Long idTpMov) {
	// this.idTpMov = idTpMov;
	// }

	public void setCadastrante(final DpPessoa cadastrante) {
		this.cadastrante = cadastrante;
	}

	public void setDescrMov(final String descrMov) {		
		this.descrMov = descrMov;		
	}	
	
	public String obterDescrMovComPontoFinal() {				
		if (this.descrMov != null && !this.descrMov.isEmpty() && !".".equals(this.descrMov.substring(this.descrMov.length() - 1))) {
			return this.descrMov.trim() + ".";
		} else if (this.descrMov == null) {
			return "";
		} else {			
			return this.descrMov.trim();
		}
	}
	public void setDestinoFinal(final DpPessoa destinoFinal) {
		this.destinoFinal = destinoFinal;
	}

	public void setDtFimMov(final Date dtFimMov) {
		this.dtFimMov = dtFimMov;
	}

	public void setDtIniMov(final Date dtIniMov) {
		this.dtIniMov = dtIniMov;
	}

	public void setDtMov(final Date dtMov) {
		this.dtMov = dtMov;
	}

	public void setExMovimentacaoCanceladora(final ExMovimentacao exMovimentacaoCanceladora) {
		this.exMovimentacaoCanceladora = exMovimentacaoCanceladora;
	}

	public void setExTipoDespacho(final ExTipoDespacho exTipoDespacho) {
		this.exTipoDespacho = exTipoDespacho;
	}

	public void setExClassificacao(ExClassificacao exClassificacao) {
		this.exClassificacao = exClassificacao;
	}

	public void setExTipoMovimentacao(final ExTipoMovimentacao exTipoMovimentacao) {
		this.exTipoMovimentacao = exTipoMovimentacao;
	}

	public void setIdMov(final Long idMov) {
		this.idMov = idMov;
	}

	public void setLotaCadastrante(final DpLotacao lotaCadastrante) {
		this.lotaCadastrante = lotaCadastrante;
	}

	public void setLotaResp(final DpLotacao lotaResp) {
		this.lotaResp = lotaResp;
	}

	public void setNmArqMov(final String nmArqMov) {
		this.nmArqMov = nmArqMov;
	}

	public void setResp(final DpPessoa resp) {
		this.resp = resp;
	}

	public void setSubscritor(final DpPessoa subscritor) {
		this.subscritor = subscritor;
	}

	public DpLotacao getLotaDestinoFinal() {
		return lotaDestinoFinal;
	}

	public void setLotaDestinoFinal(final DpLotacao lotaDestinoFinal) {
		this.lotaDestinoFinal = lotaDestinoFinal;
	}

	public ExMobil getExMobilRef() {
		return exMobilRef;
	}

	public void setExMobilRef(final ExMobil exMobilRef) {
		this.exMobilRef = exMobilRef;
	}

	public CpOrgao getOrgaoExterno() {
		return orgaoExterno;
	}

	public void setOrgaoExterno(final CpOrgao orgaoExterno) {
		this.orgaoExterno = orgaoExterno;
	}

	public String getObsOrgao() {
		return obsOrgao;
	}

	public void setObsOrgao(final String obsOrgao) {
		this.obsOrgao = obsOrgao;
	}

	public ExMovimentacao getExMovimentacaoRef() {
		return exMovimentacaoRef;
	}

	public void setExMovimentacaoRef(ExMovimentacao exMovRef) {
		this.exMovimentacaoRef = exMovRef;
	}

	public java.util.Set<ExMovimentacao> getExMovimentacaoReferenciadoraSet() {
		return exMovimentacaoReferenciadoraSet;
	}

	public void setExMovimentacaoReferenciadoraSet(java.util.Set<ExMovimentacao> exMovimentacaoReferenciadoraSet) {
		this.exMovimentacaoReferenciadoraSet = exMovimentacaoReferenciadoraSet;
	}

	public DpLotacao getLotaSubscritor() {
		return lotaSubscritor;
	}

	public void setLotaSubscritor(DpLotacao lotaSubscritor) {
		this.lotaSubscritor = lotaSubscritor;
	}

	public DpLotacao getLotaTitular() {
		return lotaTitular;
	}

	public void setLotaTitular(DpLotacao lotaTitular) {
		this.lotaTitular = lotaTitular;
	}

	public String getNmFuncaoSubscritor() {
		return nmFuncaoSubscritor;
	}

	public void setNmFuncaoSubscritor(String nmFuncaoSubscritor) {
		this.nmFuncaoSubscritor = nmFuncaoSubscritor;
	}

	public Date getDtDispPublicacao() {
		return dtDispPublicacao;
	}

	public void setDtDispPublicacao(Date dtDispPublicacao) {
		this.dtDispPublicacao = dtDispPublicacao;
	}

	public ExNivelAcesso getExNivelAcesso() {
		return exNivelAcesso;
	}

	public void setExNivelAcesso(ExNivelAcesso exNivelAcesso) {
		this.exNivelAcesso = exNivelAcesso;
	}

	public Date getDtEfetivaDispPublicacao() {
		return dtEfetivaDispPublicacao;
	}

	public void setDtEfetivaDispPublicacao(Date dtEfetivaDispPublicacao) {
		this.dtEfetivaDispPublicacao = dtEfetivaDispPublicacao;
	}

	public Date getDtEfetivaPublicacao() {
		return dtEfetivaPublicacao;
	}

	public void setDtEfetivaPublicacao(Date dtEfetivaPublicacao) {
		this.dtEfetivaPublicacao = dtEfetivaPublicacao;
	}

	public Long getNumTRFPublicacao() {
		return numTRFPublicacao;
	}

	public void setNumTRFPublicacao(Long numTRFPublicacao) {
		this.numTRFPublicacao = numTRFPublicacao;
	}

	public String getPagPublicacao() {
		return pagPublicacao;
	}

	public void setPagPublicacao(String pagPublicacao) {
		this.pagPublicacao = pagPublicacao;
	}

	public String getCadernoPublicacaoDje() {
		return cadernoPublicacaoDje;
	}

	public void setCadernoPublicacaoDje(String cadernoPublicacaoDje) {
		this.cadernoPublicacaoDje = cadernoPublicacaoDje;
	}

	public ExMobil getExMobil() {
		return exMobil;
	}

	public void setExMobil(ExMobil exMobil) {
		this.exMobil = exMobil;
	}

	public ExPapel getExPapel() {
		return exPapel;
	}

	public void setExPapel(ExPapel exPapel) {
		this.exPapel = exPapel;
	}

	public CpMarcador getMarcador() {
		return marcador;
	}

	public void setMarcador(CpMarcador marcador) {
		this.marcador = marcador;
	}

	public CpIdentidade getAuditIdentidade() {
		return auditIdentidade;
	}

	public void setAuditIdentidade(CpIdentidade auditIdentidade) {
		this.auditIdentidade = auditIdentidade;
	}

	public String getAuditIP() {
		return auditIP;
	}

	public void setAuditIP(String auditIP) {
		this.auditIP = auditIP;
	}

	public String getAuditHash() {
		return auditHash;
	}

	public void setAuditHash(String auditHash) {
		this.auditHash = auditHash;
	}

	public Date getDtTimestamp() {
		return dtTimestamp;
	}

	public void setDtTimestamp(Date dtTimestamp) {
		this.dtTimestamp = dtTimestamp;
	}
	
	public CpArquivo getCpArquivo() {
		return cpArquivo;
	}

	public void setCpArquivo(CpArquivo cpArquivo) {
		this.cpArquivo = cpArquivo;
	}

	public String getConteudoTpMov() {
		if (getCpArquivo() == null || getCpArquivo().getConteudoTpArq() == null)
			return conteudoTpMov;
		return getCpArquivo().getConteudoTpArq();
	}

	public void setConteudoTpMov(final String conteudoTp) {
	    this.conteudoTpMov = conteudoTp;
	    if (orgaoPermiteHcp() && this.conteudoBlobMov == null && !CpArquivoTipoArmazenamentoEnum.BLOB.equals(CpArquivoTipoArmazenamentoEnum.valueOf(Prop.get("/siga.armazenamento.arquivo.tipo")))) {
	    	cpArquivo = CpArquivo.updateConteudoTp(cpArquivo, conteudoTp);
	    }
	}

	public byte[] getConteudoBlobMov() {
		if(cacheConteudoBlobMov != null) {
			return cacheConteudoBlobMov;
		} else if (getCpArquivo() == null) {
			cacheConteudoBlobMov = conteudoBlobMov;
		} else {
			try {
				cacheConteudoBlobMov = getCpArquivo().getConteudo();
			} catch (Exception e) {
				throw new AplicacaoException(e.getMessage());
			}
		}
		return cacheConteudoBlobMov;
	}

	public void setConteudoBlobMov(byte[] createBlob) {
		cacheConteudoBlobMov = createBlob;
		if (this.cpArquivo==null && (this.conteudoBlobMov!=null || CpArquivoTipoArmazenamentoEnum.BLOB.equals(CpArquivoTipoArmazenamentoEnum.valueOf(Prop.get("/siga.armazenamento.arquivo.tipo"))))) {
			this.conteudoBlobMov = createBlob;
		} else if(cacheConteudoBlobMov != null){
			if(orgaoPermiteHcp())
				cpArquivo = CpArquivo.updateConteudo(cpArquivo, cacheConteudoBlobMov);
			else
				this.conteudoBlobMov = createBlob;
		}
		
	}

	public Date getDtParam1() {
		return dtParam1;
	}

	public void setDtParam1(Date dtParam1) {
		this.dtParam1 = dtParam1;
	}

	public Date getDtParam2() {
		return dtParam2;
	}

	public void setDtParam2(Date dtParam2) {
		this.dtParam2 = dtParam2;
	}
	
	private boolean orgaoPermiteHcp() {
		if(exMobil!=null && exMobil.getDoc()!=null && exMobil.getDoc().getCadastrante()!=null && exMobil.getDoc().getCadastrante().getOrgaoUsuario()!=null) {
			List<String> orgaos = Prop.getList("/siga.armazenamento.orgaos");
			CpOrgaoUsuario orgaoUsuario = exMobil.getDoc().getCadastrante().getOrgaoUsuario();
			if(orgaos != null && orgaoUsuario!=null && ("*".equals(orgaos.get(0)) || orgaos.stream().anyMatch(orgao -> orgao.equals(orgaoUsuario.getSigla()))) )
				return true;
		}
		return false;
	}
	
}