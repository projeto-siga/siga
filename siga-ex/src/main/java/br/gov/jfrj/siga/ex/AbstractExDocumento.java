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
package br.gov.jfrj.siga.ex;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Convert;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringEscapeUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.CpArquivo;
import br.gov.jfrj.siga.cp.CpArquivoTipoArmazenamentoEnum;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.BIE.ExBoletimDoc;
import br.gov.jfrj.siga.ex.converter.ExTipoDePrincipalConverter;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDePrincipal;
import br.gov.jfrj.siga.ex.vo.ExDocumentoVO;

/**
 * A class that represents a row in the EX_DOCUMENTO table. You can customize
 * the behavior of this class by editing the class, {@link ExDocumento()}.
 */
@SuppressWarnings("serial")
@MappedSuperclass
@NamedQueries({
		@NamedQuery(name = "obterProximoNumeroSub", query = "select max(doc.numExpediente)+1"
				+ "			from ExDocumento doc"
				+ "			inner join doc.subscritor sub "
				+ "			inner join doc.exFormaDocumento frm "
				+ "			where doc.anoEmissao = :anoEmissao"
				+ "			and sub.sesbPessoa = :sesb"
				+ "			and frm.idFormaDoc = :idFormaDoc"),
		@NamedQuery(name = "obterProximoNumeroCad", query = "select max(doc.numExpediente)+1 "
				+ "			from ExDocumento doc"
				+ "			inner join doc.cadastrante sub "
				+ "			inner join doc.exFormaDocumento frm "
				+ "			where doc.anoEmissao = :anoEmissao"
				+ "			and sub.sesbPessoa = :sesb"
				+ "			and frm.idFormaDoc = :idFormaDoc"),
		@NamedQuery(name = "obterProximoNumero", query = "select max(doc.numExpediente)+1"
				+ "			from ExDocumento doc"
				+ "			inner join doc.exFormaDocumento frm"
				+ "			inner join doc.orgaoUsuario org"
				+ "			where org.idOrgaoUsu = :idOrgaoUsu"
				+ "			and frm.idFormaDoc = :idFormaDoc"
				+ "			and doc.anoEmissao = :anoEmissao"),
		@NamedQuery(name = "consultarPorSiglaDocumento", query = "from ExDocumento doc"
				+ "		where ("
				+ "		doc.anoEmissao=:anoEmissao"
				+ "		and (:idOrgaoUsu = null or :idOrgaoUsu = 0L or doc.orgaoUsuario.idOrgaoUsuIni = :idOrgaoUsu)"
				+ "		and doc.exFormaDocumento.idFormaDoc=:idFormaDoc"
				+ "		and doc.numExpediente=:numExpediente" + "		)"),
		@NamedQuery(name = "consultarPorModeloEAssinatura", query = "from ExDocumento d where d.idDoc in (select doc.idDoc from ExDocumento as doc join doc.exMobilSet as mob join mob.exMovimentacaoSet as mov"
				+ "			where (mov.exTipoMovimentacao in :enumList)"
				+ "			and mov.exMovimentacaoCanceladora = null"
				+ "			and doc.exModelo.idMod = :idMod"
				+ "			and doc.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu"
				+ "			group by doc.idDoc"
				+ "			having min(mov.dtIniMov) between :dataIni and :dataFim)"),
		@NamedQuery(name = "consultarPorFiltro", query = "select doc, mob, label "
				+ "			from ExMarca label inner join label.exMobil mob inner join label.exMobil.exDocumento doc"
				+ "			"
				+ "			where ("
				+ "				:descrDocumento = null or :descrDocumento = '' or ("
				+ "					doc.exNivelAcesso.grauNivelAcesso < 21 and label.cpMarcador.idMarcador != 1 and label.cpMarcador.idMarcador != 10"
				+ "				) or ( "
				+ "					:lotaTitular!=null and :lotaTitular!=0L and doc.lotaCadastrante in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :lotaTitular)"
				+ "					or (:titular!=null and :titular!=0L and doc.subscritor in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :titular))"
				+ "					or (:titular!=null and :titular!=0L and doc.destinatario in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :titular))"
				+ "					or (:lotaTitular!=null and :lotaTitular!=0L and doc.destinatario = null and doc.lotaDestinatario in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :lotaTitular))"
				+ "					or (:lotaTitular!=null and :lotaTitular!=0L and label.dpLotacaoIni in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :lotaTitular))"
				+ "					or (:titular!=null and :titular!=0L and label.dpPessoaIni.idPessoa in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :titular))"
				+ "				)"
				+ "			)"
				+ "			"
				+ "			and ("
				+ "				("
				+ "					(:ultMovIdEstadoDoc = null or :ultMovIdEstadoDoc = 0L) and not (label.cpMarcador.idMarcador in (3, 14, 25))"
				+ "				) 	or label.cpMarcador.idMarcador = :ultMovIdEstadoDoc"
				+ "			) and (:ultMovRespSelId = null or :ultMovRespSelId = 0L or label.dpPessoaIni.idPessoa = :ultMovRespSelId)"
				+ "			and (:ultMovLotaRespSelId = null or :ultMovLotaRespSelId = 0L or label.dpLotacaoIni.idLotacao = :ultMovLotaRespSelId)"
				+ "			and (:idTipoMobil = null or :idTipoMobil = 0L or mob.exTipoMobil.idTipoMobil = :idTipoMobil)"
				+ "			and (:numSequencia = null or :numSequencia = 0L or mob.numSequencia = :numSequencia)"
				+ "			"
				+ "			and (:idOrgaoUsu = null or :idOrgaoUsu = 0L or doc.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu)"
				+ "			and (:anoEmissao = null or :anoEmissao = 0L or doc.anoEmissao = :anoEmissao)"
				+ "			and (:numExpediente = null or :numExpediente = 0L or doc.numExpediente = :numExpediente)"
				+ "			and (:idTpDoc = null or :idTpDoc = 0L or doc.exTipoDocumento.idTpDoc = :idTpDoc)"
				+ "			and (:idFormaDoc = null or :idFormaDoc = 0L or doc.exFormaDocumento.idFormaDoc = :idFormaDoc)"
				+ "			and (:idMod = null or :idMod = 0L or doc.exModelo.idMod in (select l.idMod from ExModelo as l where l.hisIdIni = :idMod))"

				+ "			and (:idTipoFormaDoc = null or :idTipoFormaDoc = 0L or doc.exFormaDocumento.exTipoFormaDoc.idTipoFormaDoc = :idTipoFormaDoc)"
				+ "			and (:classificacaoSelId = null or :classificacaoSelId = 0L or doc.exClassificacao.idClassificacao = :classificacaoSelId)"
				+ "			and (:descrDocumento = null or :descrDocumento = '' or upper(doc.descrDocumentoAI) like upper('%' || :descrDocumento || '%'))"
				+ "			"
				+ "			and (:dtDoc = null or doc.dtDoc >= :dtDoc)"
				+ "			and (:dtDocFinal = null or doc.dtDoc <= :dtDocFinal)"
				+ "			"
				+ "			and (:numAntigoDoc = null or :numAntigoDoc = '' or upper (doc.numAntigoDoc) like upper('%' || :numAntigoDoc || '%'))	"
				+ "			"
				+ "			and (:destinatarioSelId = null or :destinatarioSelId = 0L or doc.destinatario.idPessoa in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :destinatarioSelId))"
				+ "			and (:lotacaoDestinatarioSelId = null or :lotacaoDestinatarioSelId = 0L or doc.lotaDestinatario.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :lotacaoDestinatarioSelId))"
				+ "			and (:nmDestinatario = null or :nmDestinatario = '' or upper(doc.nmDestinatario) like '%' || :nmDestinatario || '%')"
				+ "			"
				+ "			and (:cadastranteSelId = null or :cadastranteSelId = 0L or doc.cadastrante.idPessoa in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :cadastranteSelId))"
				+ "			and (:lotaCadastranteSelId = null or :lotaCadastranteSelId = 0L or doc.lotaCadastrante.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :lotaCadastranteSelId))"
				+ "			"
				+ "			and (:subscritorSelId = null or :subscritorSelId = 0L or doc.subscritor.idPessoa in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :subscritorSelId))"
				+ "			and (:lotaSubscritorSelId = null or :lotaSubscritorSelId = 0L or doc.lotaSubscritor.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :lotaSubscritorSelId))"
				+ "			and (:nmSubscritorExt = null or :nmSubscritorExt = '' or upper(doc.nmSubscritorExt) like '%' || :nmSubscritorExt || '%')"
				+ "			and (:orgaoExternoSelId = null or :orgaoExternoSelId = null or doc.orgaoExterno.idOrgao = :orgaoExternoSelId)"
				+ "			and (:numExtDoc = null or :numExtDoc = '' or upper(doc.numExtDoc) like upper('%' || :numExtDoc || '%'))"
				+ "			order by doc.dtDoc desc"),
		@NamedQuery(name = "consultarQuantidadePorFiltro", query = "select count(doc) "
				+ "				from ExMarca label inner join label.exMobil mob inner join label.exMobil.exDocumento doc"
				+ "				"
				+ "				where ("
				+ "					:descrDocumento = null or :descrDocumento = '' or ("
				+ "						doc.exNivelAcesso.grauNivelAcesso < 21 and label.cpMarcador.idMarcador != 1 and label.cpMarcador.idMarcador != 10"
				+ "					) or ( "
				+ "						:lotaTitular!=null and :lotaTitular!=0L and doc.lotaCadastrante in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :lotaTitular)"
				+ "						or (:titular!=null and :titular!=0L and doc.subscritor in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :titular))"
				+ "						or (:titular!=null and :titular!=0L and doc.destinatario in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :titular))"
				+ "						or (:lotaTitular!=null and :lotaTitular!=0L and doc.destinatario = null and doc.lotaDestinatario in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :lotaTitular))"
				+ "						or (:lotaTitular!=null and :lotaTitular!=0L and label.dpLotacaoIni in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :lotaTitular))"
				+ "						or (:titular!=null and :titular!=0L and label.dpPessoaIni.idPessoa in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :titular))"
				+ "					)"
				+ "				)"
				+ "				"
				+ "				and ("
				+ "					("
				+ "						(:ultMovIdEstadoDoc = null or :ultMovIdEstadoDoc = 0L) and not (label.cpMarcador.idMarcador in (3, 14, 25))"
				+ "					) 	or label.cpMarcador.idMarcador = :ultMovIdEstadoDoc"
				+ "				) and (:ultMovRespSelId = null or :ultMovRespSelId = 0L or label.dpPessoaIni.idPessoa = :ultMovRespSelId)"
				+ "				and (:ultMovLotaRespSelId = null or :ultMovLotaRespSelId = 0L or label.dpLotacaoIni.idLotacao = :ultMovLotaRespSelId)"
				+ "				and (:idTipoMobil = null or :idTipoMobil = 0L or mob.exTipoMobil.idTipoMobil = :idTipoMobil)"
				+ "				and (:numSequencia = null or :numSequencia = 0 or mob.numSequencia = :numSequencia)"
				+ "				"
				+ "				and (:idOrgaoUsu = null or :idOrgaoUsu = 0L or doc.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu)"
				+ "				and (:anoEmissao = null or :anoEmissao = 0L or doc.anoEmissao = :anoEmissao)"
				+ "				and (:numExpediente = null or :numExpediente = 0L or doc.numExpediente = :numExpediente)"
				+ "				and (:idTpDoc = null or :idTpDoc = 0L or doc.exTipoDocumento.idTpDoc = :idTpDoc)"
				+ "				and (:idTipoFormaDoc = null or :idTipoFormaDoc = 0L or doc.exFormaDocumento.exTipoFormaDoc.idTipoFormaDoc = :idTipoFormaDoc)"
				+ "				and (:idFormaDoc = null or :idFormaDoc = 0L or doc.exFormaDocumento.idFormaDoc = :idFormaDoc)"
				+ "				and (:idMod = null or :idMod = 0L or doc.exModelo.idMod in (select l.idMod from ExModelo as l where l.hisIdIni = :idMod))"
				+ "				and (:classificacaoSelId = null or :classificacaoSelId = 0L or doc.exClassificacao.idClassificacao = :classificacaoSelId)"
				+ "				and (:descrDocumento = null or :descrDocumento = '' or upper(doc.descrDocumentoAI) like upper('%' || :descrDocumento || '%'))"
				+ "				"
				+ "				and (:dtDoc = null or doc.dtDoc >= :dtDoc)"
				+ "				and (:dtDocFinal = null or doc.dtDoc <= :dtDocFinal)"
				+ "				"
				+ "				and (:numAntigoDoc = null or :numAntigoDoc = '' or upper (doc.numAntigoDoc) like upper('%' || :numAntigoDoc || '%'))	"
				+ "				"
				+ "				and (:destinatarioSelId = null or :destinatarioSelId = 0L or doc.destinatario.idPessoa in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :destinatarioSelId))"
				+ "				and (:lotacaoDestinatarioSelId = null or :lotacaoDestinatarioSelId = 0L or doc.lotaDestinatario.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :lotacaoDestinatarioSelId))"
				+ "				and (:nmDestinatario = null or :nmDestinatario = '' or upper(doc.nmDestinatario) like '%' || :nmDestinatario || '%')"
				+ "				"
				+ "				and (:cadastranteSelId = null or :cadastranteSelId = 0L or doc.cadastrante.idPessoa in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :cadastranteSelId))"
				+ "				and (:lotaCadastranteSelId = null or :lotaCadastranteSelId = 0L or doc.lotaCadastrante.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :lotaCadastranteSelId))"
				+ "				"
				+ "				and (:subscritorSelId = null or :subscritorSelId = 0L or doc.subscritor.idPessoa in (select p.idPessoa from DpPessoa as p where p.idPessoaIni = :subscritorSelId))"
				+ "				and (:lotaSubscritorSelId = null or :lotaSubscritorSelId = 0L or doc.lotaSubscritor.idLotacao in (select l.idLotacao from DpLotacao as l where l.idLotacaoIni = :lotaSubscritorSelId))"
				+ "				"
				+ "				and (:nmSubscritorExt = null or :nmSubscritorExt = '' or upper(doc.nmSubscritorExt) like '%' || :nmSubscritorExt || '%')"
				+ "				and (:orgaoExternoSelId = null or :orgaoExternoSelId = null or doc.orgaoExterno.idOrgao = :orgaoExternoSelId)"
				+ "				and (:numExtDoc = null or :numExtDoc = '' or upper(doc.numExtDoc) like upper('%' || :numExtDoc || '%'))"),
		@NamedQuery(name = "listarSolicitados", query = "select doc "
				+ "			from ExMarca label inner join label.exMobil mob inner join label.exMobil.exDocumento doc"
				+ "			where label.cpMarcador.idMarcador = 21"
				+ "			and doc.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu"),
		@NamedQuery(name = "listarDisponibilizados", query = "select doc "
				+ "			from ExMarca label inner join label.exMobil mob inner join label.exMobil.exDocumento doc"
				+ "			where label.cpMarcador.idMarcador = 22"
				+ "			and doc.orgaoUsuario.idOrgaoUsu = :idOrgaoUsu"),
		@NamedQuery(name = "listarDocPendenteAssinatura", query = "select doc "
				+ "			from ExDocumento doc where doc.idDoc in (select distinct(exDocumento.idDoc) from ExMobil mob where mob.idMobil in "
				+ "			(select exMobil.idMobil from ExMarca label where label.cpMarcador.idMarcador = 25 and label.dpPessoaIni.idPessoa = :idPessoaIni)) order by doc.dtDoc desc"),
		@NamedQuery(name = "listarDocPendenteAssinaturaERevisado", query = "select doc "
				+ "			from ExDocumento doc where doc.idDoc in (select distinct(exDocumento.idDoc) from ExMobil mob where mob.idMobil in "
				+ "			(select exMobil.idMobil from ExMarca label where label.cpMarcador.idMarcador = 71 and label.dpPessoaIni.idPessoa = :idPessoaIni)) order by doc.dtDoc desc"),
		@NamedQuery(name = "consultarExDocumentoClassificados", query = "select doc from ExDocumento doc left join fetch doc.exClassificacao"
				+ "		where doc.exClassificacao.codificacao like :mascara"
				+ "		and doc.orgaoUsuario.idOrgaoUsu = :idOrgaoUsuario"
				+ "		and doc.dtFinalizacao is not null"),
		@NamedQuery(name = "consultarExDocumentoClassificadosPorLotacao", query = "select doc from ExDocumento doc left join fetch doc.exClassificacao"
				+ "		where doc.exClassificacao.codificacao like :mascara"
				+ "			and (doc.lotaTitular.id = :idLotacao)"
				+ "			and doc.orgaoUsuario.idOrgaoUsu = :idOrgaoUsuario"
				+ "			and doc.dtFinalizacao is not null"),
		@NamedQuery(name = "consultarDocumentosArquivados", query = "select mob from ExDocumento doc JOIN ExMobil mob on (mob.exDocumento = doc.idDoc )"
				+ " 			   JOIN CpMarca marca on (marca.idRef = mob.idMobil )"
				+ "                where ((marca.dpPessoaIni.idPessoa=:pessoaIni or marca.dpLotacaoIni.idLotacao=:lotaIni)"
				+ "                and (marca.cpMarcador.idMarcador in (:enumList))"
				+ " 			   and mob.exDocumento NOT IN (SELECT mob_cancelado.exDocumento"
				+ " 			   							   	FROM ExMobil mob_cancelado"
				+ " 			   								JOIN CpMarca marca_cancelado on (marca_cancelado.idRef = mob_cancelado.idMobil )"
				+ " 			   								WHERE (mob_cancelado.exDocumento = doc.idDoc)"
				+ " 			   										AND (marca_cancelado.cpMarcador.idMarcador in (:enumListCancelados)) "
				+ "														AND (marca_cancelado.dpPessoaIni.idPessoa=:pessoaIni or marca_cancelado.dpLotacaoIni.idLotacao=:lotaIni))"
				+ "                ) order by marca.dtIniMarca desc"),
		@NamedQuery(name = "consultarDocumentosArquivadosJaTransferido", query = "select mob, emFinal, emAnterior.descrMov from ExDocumento doc JOIN ExMobil mob on (mob.exDocumento = doc.idDoc )"
				+ " 			   JOIN ExMovimentacao emAnterior on (emAnterior.exMobil.idMobil = mob.idMobil) AND emAnterior.exTipoMovimentacao = 9 AND emAnterior.exMovimentacaoCanceladora = mob.ultimaMovimentacaoNaoCancelada"	
				+ " 			   JOIN ExMovimentacao emFinal on (emFinal.exMobil.idMobil = mob.idMobil) AND emFinal.exTipoMovimentacao = 9"
				+ " 			   JOIN CpMarca marca on (marca.idRef = mob.idMobil )"
				+ "                where ("
				+ "((emAnterior.cadastrante.idPessoa = :pessoaIni) OR "
				+ " (emAnterior.lotaCadastrante.idLotacao = :lotaIni)) "
				+ " AND (emFinal.idMov = mob.ultimaMovimentacaoNaoCancelada.idMov)"
				+ "                and (marca.cpMarcador.idMarcador in (:enumList))"
				+ " 			   AND emFinal.exMovimentacaoRef.idMov = emAnterior.idMov"
				+ " 			   AND (emFinal.lotaDestinoFinal.idLotacao <> 0 or emFinal.lotaDestinoFinal IS NOT NULL) "
				+ "                ) order by marca.dtIniMarca desc"),
		@NamedQuery(name = "consultarDocumentosFinalizadosEntreDatas", query = "select doc from ExDocumento doc where "
				+ "					doc.exTipoDocumento.idTpDoc = :idTipoDocumento"
				+ "					and doc.lotaCadastrante.idLotacaoIni = :idLotacaoInicial"
				+ "					and doc.dtFinalizacao is not null"
				+ "					and doc.dtFinalizacao between :dataInicial and :dataFinal"
				+ "				order by  doc.dtFinalizacao") })
@SqlResultSetMappings(value = {
		@SqlResultSetMapping(name="DocumentosPorCodificacaoClassificacao",
				classes = {
						@ConstructorResult(
								targetClass = ExDocumentoVO.class,
								columns = {
										@ColumnResult(name="idDoc", type = Long.class),
										@ColumnResult(name="sigla", type = String.class),
										@ColumnResult(name="classificacaoSigla", type = String.class),
										@ColumnResult(name="lotaCadastranteString", type = String.class),
										@ColumnResult(name="cadastranteString", type = String.class),
										@ColumnResult(name="descrDocumento", type = String.class)
								}
						)
				}) })
public abstract class AbstractExDocumento extends ExArquivo implements
		Serializable {
	
	/* Limitador para indexação do campo pesquisável da descrição do documento. Length Default 4000. Length indexável: 3150*/
	private static final int LENGTH_DESCR_DOCUMENTO_AI = System.getProperty("sigaex.descricao.documento.ai.length") != null ? Integer.parseInt(System.getProperty("sigaex.descricao.documento.ai.length")) : 4000;

	@Id
	@SequenceGenerator(sequenceName = "EX_DOCUMENTO_SEQ", name = "EX_DOCUMENTO_SEQ")
	@GeneratedValue(generator = "EX_DOCUMENTO_SEQ")
	@Column(name = "ID_DOC")
	private java.lang.Long idDoc;
	
//	@Transient
//	protected byte[] cacheConteudoBlobDoc;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MOB_PAI")
	private ExMobil exMobilPai;

	@Column(name = "ANO_EMISSAO")
	private java.lang.Long anoEmissao;

	@Column(name = "NUM_EXPEDIENTE")
	private java.lang.Long numExpediente;

	@Column(name = "DESCR_DOCUMENTO", length = 4000)
	private java.lang.String descrDocumento;
	
	@Column(name = "DESCR_DOCUMENTO_AI", length = 4000)
	private java.lang.String descrDocumentoAI;

	@Column(name = "DSC_CLASS_DOC", length = 4000)
	private java.lang.String descrClassifNovo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_DOC", length = 19)
	private java.util.Date dtDoc;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_DOC_ORIGINAL", length = 19)
	private java.util.Date dtDocOriginal;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_FINALIZACAO", length = 19)
	private Date dtFinalizacao;
	
	
	/*
	 * 16/01/2020 - Data da primeira assinatura
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_PRIMEIRAASSINATURA", length = 19)
	private Date dtPrimeiraAssinatura;
	
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DT_REG_DOC", nullable = false, length = 19)
	private java.util.Date dtRegDoc;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HIS_DT_ALT", nullable = false, length = 19)
	private java.util.Date dtAltDoc;

	@Column(name = "NM_ARQ_DOC", length = 256)
	private java.lang.String nmArqDoc;

	@Column(name = "NM_DESTINATARIO", length = 256)
	private String nmDestinatario;

	@Column(name = "NM_ORGAO_DESTINATARIO", length = 256)
	private String nmOrgaoExterno;

	@Column(name = "NM_SUBSCRITOR_EXT", length = 256)
	private java.lang.String nmSubscritorExt;

	@Column(name = "NM_FUNCAO_SUBSCRITOR", length = 128)
	private java.lang.String nmFuncaoSubscritor;

	@Column(name = "NUM_EXT_DOC", length = 32)
	private java.lang.String numExtDoc;

	@Column(name = "NUM_ANTIGO_DOC", length = 32)
	private java.lang.String numAntigoDoc;

	@Column(name = "OBS_ORGAO_DOC", length = 256)
	private String obsOrgao;

	@Column(name = "FG_ELETRONICO", nullable = false, length = 1)
	private String fgEletronico;

	@Column(name = "NUM_SEQUENCIA")
	private Integer numSequencia;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DNM_DT_ACESSO", length = 19)
	private Date dnmDtAcesso;

	@Column(name = "DNM_ACESSO", length = 4000)
	private String dnmAcesso;
	
	@Column(name = "ORDENACAO_DOC", length = 200)
	private String ordenacaoDoc;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SUBSCRITOR")
	private DpPessoa subscritor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CADASTRANTE", nullable = false)
	private DpPessoa cadastrante;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TITULAR")
	private DpPessoa titular;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTA_CADASTRANTE", nullable = false)
	private DpLotacao lotaCadastrante;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTA_TITULAR")
	private DpLotacao lotaTitular;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTA_DESTINATARIO")
	private DpLotacao lotaDestinatario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTA_SUBSCRITOR")
	private DpLotacao lotaSubscritor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DESTINATARIO")
	private DpPessoa destinatario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLASSIFICACAO")
	private ExClassificacao exClassificacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FORMA_DOC")
	private ExFormaDocumento exFormaDocumento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MOD")
	private ExModelo exModelo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_DOC", nullable = false)
	private ExTipoDocumento exTipoDocumento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_NIVEL_ACESSO")
	private ExNivelAcesso exNivelAcesso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DNM_ID_NIVEL_ACESSO")
	private ExNivelAcesso dnmExNivelAcesso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO")
	private CpOrgao orgaoExterno;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO_USU")
	private CpOrgaoUsuario orgaoUsuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGAO_DESTINATARIO")
	private CpOrgao orgaoExternoDestinatario;

	@BatchSize(size=1)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exDocumento")
	@Sort(type = SortType.NATURAL)
	private java.util.SortedSet<ExMobil> exMobilSet = new TreeSet<ExMobil>();

	@BatchSize(size=1)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exDocumento")
	private java.util.Set<ExBoletimDoc> exBoletimDocSet;

	@Column(name = "ID_DOC_ANTERIOR")
	private java.lang.Long idDocAnterior;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MOB_AUTUADO")
	private ExMobil exMobilAutuado;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROTOCOLO")
    private ExProtocolo exProtocolo;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_ARQ")
	private CpArquivo cpArquivo;

	
	@ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_ARQ_FMT_LIVRE")
	private CpArquivo cpArquivoFormatoLivre;
	
	@Column(name = "CD_PRINCIPAL")
	private String principal;

	@Convert(converter = ExTipoDePrincipalConverter.class)
	@Column(name = "TP_PRINCIPAL")
	private ExTipoDePrincipal tipoDePrincipal;

	/**
	 * Simple constructor of AbstractExDocumento instances.
	 */
	public AbstractExDocumento() {
	}

	/**
	 * Constructor of AbstractExDocumento instances given a simple primary key.
	 * 
	 * @param idDoc
	 */
	public AbstractExDocumento(final java.lang.Long idDoc) {
		this.setIdDoc(idDoc);
	}

	/**
	 * Compara dois documentos por ID
	 * 
	 */
	@Override
	public boolean equals(final Object rhs) {
		if (rhs == null)
			return false;
		if (!(rhs instanceof ExDocumento))
			return false;
		final ExDocumento that = (ExDocumento) rhs;
		if (this.getIdDoc() == null || that.getIdDoc() == null)
			return false;
		return (this.getIdDoc().equals(that.getIdDoc()));
	}

	/**
	 * Retornao ano de emissão do documento, que compõe o código
	 */
	public java.lang.Long getAnoEmissao() {
		return this.anoEmissao;
	}

	/**
	 * Retorna o cadastrante do documento
	 */
	public DpPessoa getCadastrante() {
		return cadastrante;
	}

	/**
	 * COMPLETAR
	 * 
	 * @return
	 */
	public java.lang.String getDescrClassifNovo() {
		return descrClassifNovo;
	}

	/**
	 * Retorna a descrição do documento
	 */
	public java.lang.String getDescrDocumento() {
		return this.descrDocumento;
	}

	/**
	 * Retorna a pessoa destinatária do documento
	 */
	public DpPessoa getDestinatario() {
		return destinatario;
	}

	/**
	 * Retorna a data do documento
	 */
	public java.util.Date getDtDoc() {
		return this.dtDoc;
	}

	public java.util.Date getDtDocOriginal() {
		return dtDocOriginal;
	}

	/**
	 * Retorna a data de finalização do documento
	 * 
	 * @return
	 */
	public Date getDtFinalizacao() {
		return dtFinalizacao;
	}

	/**
	 * Retorna a data de registro do documento
	 */
	public java.util.Date getDtRegDoc() {
		return this.dtRegDoc;
	}

	/**
	 * Retorna o objeto de relacionamento boletim x documentos relacionado a
	 * este documento
	 * 
	 * @return
	 */
	public java.util.Set<ExBoletimDoc> getExBoletimDocSet() {
		return exBoletimDocSet;
	}

	/**
	 * Retorna a classificação do documento
	 * 
	 * @return
	 */
	public ExClassificacao getExClassificacao() {
		return exClassificacao;
	}

	/**
	 * Retorna o documento usado como base para gerar este, por meio do comando
	 * Duplicar ou Refazer
	 * 
	 * @return
	 */
	public java.lang.Long getIdDocAnterior() {
		return idDocAnterior;
	}

	/**
	 * Retorna o tipo do documento (Ofício, Memorando, etc)
	 * 
	 * @return
	 */
	public ExFormaDocumento getExFormaDocumento() {
		return exFormaDocumento;
	}

	/**
	 * Retorna o móbil do qual o documento é filho
	 * 
	 * @return
	 */
	public ExMobil getExMobilPai() {
		return exMobilPai;
	}

	/**
	 * Retorna o conjunto de móbil's do documento
	 * 
	 * @return
	 */
	public java.util.SortedSet<ExMobil> getExMobilSet() {
		return exMobilSet;
	}

	/**
	 * Retorna o modelo do documento
	 * 
	 * @return
	 */
	public ExModelo getExModelo() {
		return exModelo;
	}

	/**
	 * Retorna o nível de acesso do documento
	 * 
	 * @return
	 */
	public ExNivelAcesso getExNivelAcesso() {
		return exNivelAcesso;
	}

	/**
	 * Retorna a origem do documento
	 */
	public ExTipoDocumento getExTipoDocumento() {
		return this.exTipoDocumento;
	}

	/**
	 * Retorna, no formato String (S/N) se o documento é eletrônico
	 * 
	 * @return
	 */
	public String getFgEletronico() {
		return fgEletronico;
	}

	/**
	 * Retorna o id do documento
	 */
	public java.lang.Long getIdDoc() {
		return idDoc;
	}

	/**
	 * Retorna a lotação cadastrante do documento
	 */
	public DpLotacao getLotaCadastrante() {
		return lotaCadastrante;
	}

	/**
	 * Retorna a lotação destinatária do documento
	 * */
	public DpLotacao getLotaDestinatario() {
		return lotaDestinatario;
	}

	/**
	 * Retorna a lotação subscritora do documento
	 * */
	public DpLotacao getLotaSubscritor() {
		return lotaSubscritor;
	}

	/**
	 * Retorna a lotação titular do documento
	 * 
	 * @return
	 */
	public DpLotacao getLotaTitular() {
		return lotaTitular;
	}

	/**
	 * COMPLETAR Retorna o nome do arquivo do documento
	 */
	public java.lang.String getNmArqDoc() {
		return this.nmArqDoc;
	}

	/**
	 * @return Retorna o nome do destinatário não tabelado digitado.
	 */
	public String getNmDestinatario() {
		return nmDestinatario;
	}

	/**
	 * Retorna o valor completo digitado no campo Função;Lotação;Localidade
	 * 
	 * @return
	 */
	public java.lang.String getNmFuncaoSubscritor() {
		return nmFuncaoSubscritor;
	}

	/**
	 * COMPLETAR Retorna o nome do órgão externo
	 * 
	 * @return
	 */
	public String getNmOrgaoExterno() {
		return nmOrgaoExterno;
	}

	/**
	 * Retorna o nome do subscritor externo digitado
	 */
	public java.lang.String getNmSubscritorExt() {
		return this.nmSubscritorExt;
	}

	/**
	 * Retorna o número antigo digitado
	 * 
	 * @return
	 */
	public java.lang.String getNumAntigoDoc() {
		return numAntigoDoc;
	}

	/**
	 * Retorna o número do expediente (não o código completo)
	 */
	public java.lang.Long getNumExpediente() {
		return this.numExpediente;
	}

	/**
	 * Retorna o número externo digitado.
	 */
	public java.lang.String getNumExtDoc() {
		return this.numExtDoc;
	}

	/**
	 * Retorna o número de sequência do documento. Não é o número do expediente.
	 * No caso de ser um subprocesso, retorna o número que gera o código .01,
	 * .02, etc.
	 * 
	 * @return
	 */
	public Integer getNumSequencia() {
		return numSequencia;
	}

	/**
	 * retorna a observação sobre o órgão externo de origem do documento
	 * 
	 * @return
	 */
	public String getObsOrgao() {
		return obsOrgao;
	}

	/**
	 * Retorna o órgão externo de origem do documento
	 * 
	 * @return
	 */
	public CpOrgao getOrgaoExterno() {
		return orgaoExterno;
	}

	/**
	 * Retorna o órgão externo a que se destina o documento
	 * 
	 * @return
	 */
	public CpOrgao getOrgaoExternoDestinatario() {
		return orgaoExternoDestinatario;
	}

	/**
	 * @return Retorna o órgão usuário em que o documento foi produzido.
	 */
	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	/**
	 * @return Retorna o subscritor do documento.
	 */
	public DpPessoa getSubscritor() {
		return subscritor;
	}

	/**
	 * @return Retorna o titular do documento.
	 */
	public DpPessoa getTitular() {
		return titular;
	}

	/**
	 * Implementation of the hashCode method conforming to the Bloch pattern
	 * with the exception of array properties (these are very unlikely primary
	 * key types).
	 * 
	 * @return int
	 */
	@Override
	public int hashCode() {
		int result = 17;
		final int idDocValue = this.getIdDoc() == null ? 0 : this.getIdDoc()
				.hashCode();
		return result * 37 + idDocValue;
	}

	/**
	 * Set the value of the ANO_EMISSAO column.
	 * 
	 * @param anoEmissao
	 */
	public void setAnoEmissao(final java.lang.Long anoEmissao) {
		this.anoEmissao = anoEmissao;
	}

	/**
	 * @param cadastrante
	 *            Atribui a cadastrante o valor.
	 */
	public void setCadastrante(final DpPessoa cadastrante) {
		this.cadastrante = cadastrante;
	}

	public void setDescrClassifNovo(java.lang.String descrClassifNovo) {
		this.descrClassifNovo = descrClassifNovo;
	}

	/**
	 * Set the value of the DESCR_DOCUMENTO column.
	 * 
	 * @param descrDocumento
	 */
	public void setDescrDocumento(final java.lang.String descrDocumento) {
		this.descrDocumento = StringEscapeUtils.unescapeHtml4(descrDocumento);
		setDescrDocumentoAI(this.descrDocumento);
	}

	/**
	 * @param destinatario
	 *            Atribui a destinatario o valor.
	 */
	public void setDestinatario(final DpPessoa destinatario) {
		this.destinatario = destinatario;
	}

	/**
	 * Set the value of the DT_DOC column.
	 * 
	 * @param dtDoc
	 */
	public void setDtDoc(final java.util.Date dtDoc) {
		this.dtDoc = dtDoc;
	}

	public void setDtDocOriginal(java.util.Date dtDocOriginal) {
		this.dtDocOriginal = dtDocOriginal;
	}

	public void setDtFinalizacao(final Date dtFinalizacao) {
		this.dtFinalizacao = dtFinalizacao;
	}

	/**
	 * Set the value of the DT_REG_DOC column.
	 * 
	 * @param dtRegDoc
	 */
	public void setDtRegDoc(final java.util.Date dtRegDoc) {
		this.dtRegDoc = dtRegDoc;
	}

	public void setExBoletimDocSet(java.util.Set<ExBoletimDoc> exBoletimDocSet) {
		this.exBoletimDocSet = exBoletimDocSet;
	}

	public void setExClassificacao(final ExClassificacao exClassificacao) {
		this.exClassificacao = exClassificacao;
	}

	public void setIdDocAnterior(java.lang.Long idDocAnterior) {
		this.idDocAnterior = idDocAnterior;
	}

	public void setExFormaDocumento(final ExFormaDocumento exFormaDocumento) {
		this.exFormaDocumento = exFormaDocumento;
	}

	public void setExMobilPai(ExMobil exMobilPai) {
		this.exMobilPai = exMobilPai;
	}

	public void setExMobilSet(java.util.SortedSet<ExMobil> exMobilSet) {
		this.exMobilSet = exMobilSet;
	}

	public void setExModelo(final ExModelo exModelo) {
		this.exModelo = exModelo;
	}

	public void setExNivelAcesso(ExNivelAcesso exNivelAcesso) {
		this.exNivelAcesso = exNivelAcesso;
	}

	/**
	 * Set the value of the ID_TP_DOC column.
	 * 
	 * @param exTipoDocumento
	 */
	public void setExTipoDocumento(final ExTipoDocumento exTipoDocumento) {
		this.exTipoDocumento = exTipoDocumento;
	}

	public void setFgEletronico(String fgEletronico) {
		this.fgEletronico = fgEletronico;
	}

	/**
	 * Set the simple primary key value that identifies this object.
	 * 
	 * @param idDoc
	 */
	public void setIdDoc(final java.lang.Long idDoc) {
		this.idDoc = idDoc;
	}

	/**
	 * @param lotaCadastrante
	 *            Atribui a lotaCadastrante o valor.
	 */
	public void setLotaCadastrante(final DpLotacao lotaCadastrante) {
		this.lotaCadastrante = lotaCadastrante;
	}

	/**
	 * @param lotaDestinatario
	 *            Atribui a lotaDestinatario o valor.
	 */
	public void setLotaDestinatario(final DpLotacao lotaDestinatario) {
		this.lotaDestinatario = lotaDestinatario;
	}

	/**
	 * @param lotaSubscritor
	 *            Atribui a lotaSubscritor o valor.
	 */
	public void setLotaSubscritor(final DpLotacao lotaSubscritor) {
		this.lotaSubscritor = lotaSubscritor;
	}

	public void setLotaTitular(DpLotacao lotaTitular) {
		this.lotaTitular = lotaTitular;
	}

	/**
	 * Set the value of the NM_ARQ_DOC column.
	 * 
	 * @param nmArqDoc
	 */
	public void setNmArqDoc(final java.lang.String nmArqDoc) {
		this.nmArqDoc = nmArqDoc;
	}

	/**
	 * @param nomeDestinatario
	 *            Atribui a nomeDestinatario o valor.
	 */
	public void setNmDestinatario(final String nomeDestinatario) {
		this.nmDestinatario = nomeDestinatario;
	}

	public void setNmFuncaoSubscritor(java.lang.String nmSubscritorFuncao) {
		this.nmFuncaoSubscritor = nmSubscritorFuncao;
	}

	public void setNmOrgaoExterno(String nmOrgaoExterno) {
		this.nmOrgaoExterno = nmOrgaoExterno;
	}

	/**
	 * Set the value of the NM_SUBSCRITOR_EXT column.
	 * 
	 * @param nmSubscritorExt
	 */
	public void setNmSubscritorExt(final java.lang.String nmSubscritorExt) {
		this.nmSubscritorExt = nmSubscritorExt;
	}

	public void setNumAntigoDoc(java.lang.String numAntigoDoc) {
		this.numAntigoDoc = numAntigoDoc;
	}

	/**
	 * Set the value of the NUM_EXPEDIENTE column.
	 * 
	 * @param numExpediente
	 */
	public void setNumExpediente(final java.lang.Long numExpediente) {
		this.numExpediente = numExpediente;
	}

	/**
	 * Set the value of the NUM_EXT_DOC column.
	 * 
	 * @param numExtDoc
	 */
	public void setNumExtDoc(final java.lang.String numExtDoc) {
		this.numExtDoc = numExtDoc;
	}

	public void setNumSequencia(Integer numSequencia) {
		this.numSequencia = numSequencia;
	}

	public void setObsOrgao(final String obsOrgao) {
		this.obsOrgao = obsOrgao;
	}

	public void setOrgaoExterno(final CpOrgao orgaoExterno) {
		this.orgaoExterno = orgaoExterno;
	}

	public void setOrgaoExternoDestinatario(final CpOrgao cpOrgao) {
		this.orgaoExternoDestinatario = cpOrgao;
	}

	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
		cpArquivo = CpArquivo.updateOrgaoUsuario(cpArquivo, orgaoUsuario);
	}

	/**
	 * @param subscritor
	 *            Atribui a subscritor o valor.
	 */
	public void setSubscritor(final DpPessoa subscritor) {
		this.subscritor = subscritor;
	}

	public void setTitular(DpPessoa titular) {
		this.titular = titular;
	}

	public ExMobil getExMobilAutuado() {
		return exMobilAutuado;
	}

	public void setExMobilAutuado(ExMobil exMobilAutuado) {
		this.exMobilAutuado = exMobilAutuado;
	}

	public Date getDnmDtAcesso() {
		return dnmDtAcesso;
	}

	public void setDnmDtAcesso(Date dnmDtAcesso) {
		this.dnmDtAcesso = dnmDtAcesso;
	}

	public String getDnmAcesso() {
		return dnmAcesso;
	}

	public void setDnmAcesso(String dnmAcesso) {
		this.dnmAcesso = dnmAcesso;
	}
	
	public String getOrdenacaoDoc() {
		return ordenacaoDoc;
	}

	public void setOrdenacaoDoc(String ordenacaoDoc) {
		this.ordenacaoDoc = ordenacaoDoc;
	}

	public boolean temOrdenacao() {
		return ordenacaoDoc != null;
	}

	public ExNivelAcesso getDnmExNivelAcesso() {
		return dnmExNivelAcesso;
	}

	public void setDnmExNivelAcesso(ExNivelAcesso dnmExNivelAcesso) {
		this.dnmExNivelAcesso = dnmExNivelAcesso;
	}

	public java.util.Date getDtAltDoc() {
		return dtAltDoc;
	}

	public void setDtAltDoc(java.util.Date dtAltDoc) {
		this.dtAltDoc = dtAltDoc;
	}

	public CpArquivo getCpArquivo() {
		return cpArquivo;
	}

	public void setCpArquivo(CpArquivo cpArquivo) {
		this.cpArquivo = cpArquivo;
	}
	
	public java.lang.String getConteudoTpDoc() {
		if (getCpArquivo() == null)
			return null;
		return getCpArquivo().getConteudoTpArq();
	}

	public void setConteudoTpDoc(final java.lang.String conteudoTp) {
		cpArquivo = CpArquivo.updateConteudoTp(cpArquivo, conteudoTp);
	}
	
	public byte[] getConteudoBlobDoc() {
		try {
			return getCpArquivo().getConteudo();
		} catch (Exception e) {
			throw new AplicacaoException("Erro obtendo conteúdo de documento", 0, e);
		}
	}

	public CpArquivo getCpArquivoFormatoLivre() {
		return cpArquivoFormatoLivre;
	}
	
	/**
	 * Grava informações do arquivo de formato livre que já foi feito upload previamente. Estas informações
	 * vem em um token que é passado pelo sistema de upload. É gerado um registro na CpArquivo e associado a CpArquivoFormatoLivre na ExDocumento.
	 * 
	 * @param tokenArquivo Token JWT contendo caminho no storage, nome do arquivo, tamanho e tipo de conteudo
	 */
	public void setConteudoBlobFormatoLivre(String tokenArquivo) throws InvalidKeyException, NoSuchAlgorithmException, 
			IllegalStateException, SignatureException, IOException, JWTVerifyException {
		final JWTVerifier verifier = new JWTVerifier(System.getProperty("siga.jwt.secret"));
		Map<String, Object> lst = verifier.verify(tokenArquivo);
		String nomeArquivo = (String) lst.get("nomeArq");
		String caminho = (String) lst.get("nomeArqS3");
		Long tamanhoArquivo = Long.valueOf((String) lst.get("tamanho"));
		String hashArquivo = (String) lst.get("hash");
		CpArquivo cpArq = getCpArquivoFormatoLivre();
		CpArquivoTipoArmazenamentoEnum tipoArmazenamento = CpArquivoTipoArmazenamentoEnum
				.valueOf(Prop.get("/siga.armazenamento.arquivo.tipo"));
		
		if (!orgaoPermiteHcp() || CpArquivoTipoArmazenamentoEnum.BLOB.equals(tipoArmazenamento) || 
				CpArquivoTipoArmazenamentoEnum.TABELA.equals(tipoArmazenamento)) 
			throw new AplicacaoException("Arquivos de formato livre só são permitidos quando o tipo de armazenamento não é em banco de dados (BLOB ou TABELAS).");
		
		if (nomeArquivo != null) 
			cpArquivoFormatoLivre = CpArquivo.updateFormatoLivre(cpArq, this.orgaoUsuario, caminho, nomeArquivo, tamanhoArquivo, CpArquivoTipoArmazenamentoEnum.HCP, hashArquivo);
	}
	
	private boolean orgaoPermiteHcp() {
		List<String> orgaos = Prop.getList("/siga.armazenamento.orgaos");
		if ("*".equals(orgaos.get(0)))
			return true;
		final String sigla = this.orgaoUsuario!=null?this.orgaoUsuario.getSigla():(this.getCadastrante()!=null?this.getCadastrante().getOrgaoUsuario().getSigla():null);
		if(orgaos != null && (orgaos.stream().anyMatch(siglaFiltro -> siglaFiltro.equals(sigla))) )
			return true;
		return false;
	}
	
	public ExProtocolo getExProtocolo() {
		return exProtocolo;
	}

	public void setExProtocolo(ExProtocolo exProtocolo) {
		this.exProtocolo = exProtocolo;
	}

	public Date getDtPrimeiraAssinatura() {
		return dtPrimeiraAssinatura;
	}

	public void setDtPrimeiraAssinatura(Date dtPrimeiraAssinatura) {
		this.dtPrimeiraAssinatura = dtPrimeiraAssinatura;
	}

	public java.lang.String getDescrDocumentoAI() {
		return descrDocumentoAI;
	}

	public void setDescrDocumentoAI(java.lang.String descrDocumentoAI) {
		if(descrDocumentoAI != null)
			this.descrDocumentoAI = Texto.removeAcentoMaiusculas(descrDocumentoAI).substring(0, descrDocumentoAI.length() < LENGTH_DESCR_DOCUMENTO_AI ? descrDocumentoAI.length() : LENGTH_DESCR_DOCUMENTO_AI );
		else
			this.descrDocumentoAI = descrDocumentoAI;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public ExTipoDePrincipal getTipoDePrincipal() {
		return tipoDePrincipal;
	}

	public void setTipoDePrincipal(ExTipoDePrincipal tipoDePrincipal) {
		this.tipoDePrincipal = tipoDePrincipal;
	}
	
}