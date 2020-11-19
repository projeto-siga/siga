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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpMarcadorTipoAplicacaoEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoDataEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoExibicaoEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoInteressadoEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoJustificativaEnum;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;

@MappedSuperclass
@NamedNativeQueries({ @NamedNativeQuery(name = "consultarPaginaInicial", query = "SELECT"
		+ " grps.id_marcador, grps.descr_marcador, grps.cont_pessoa, grps.cont_lota from ("
		+ "	SELECT mard.id_marcador, mard.descr_marcador, mard.ord_marcador, "
		+ "	SUM(CASE WHEN marca.id_pessoa_ini = :idPessoaIni THEN 1 ELSE 0 END) cont_pessoa,"
		+ "	SUM(CASE WHEN marca.id_lotacao_ini = :idLotacaoIni THEN 1 ELSE 0 END) cont_lota"
		+ "	FROM corporativo.cp_marca marca"
		+ "	JOIN corporativo.cp_marcador mard on marca.id_marcador = mard.id_marcador"
		+ " WHERE(dt_ini_marca IS NULL OR dt_ini_marca < CURRENT_TIMESTAMP)"
		+ "		AND(dt_fim_marca IS NULL OR dt_fim_marca > CURRENT_TIMESTAMP)"
		+ "		AND((marca.id_pessoa_ini = :idPessoaIni) OR (marca.id_lotacao_ini = :idLotacaoIni))"
		+ "		AND (select id_tipo_forma_doc from siga.ex_forma_documento where id_forma_doc = ("
		+ "				select id_forma_doc from siga.ex_documento where id_doc = ("
		+ "			   		select id_doc from siga.ex_mobil where id_mobil = marca.id_ref ))"
		+ "			   			) = :idTipoForma"
		+ "	   	AND id_tp_marca = 1"
		+ "	GROUP BY mard.id_marcador, mard.descr_marcador, mard.ord_marcador) grps"
		+ "	ORDER BY grps.ord_marcador"),
@NamedNativeQuery(name = "quantidadeDocumentos", query = "SELECT"
		+ "		count(1)"
		+ "	FROM corporativo.cp_marca marca"
		+ "	WHERE(dt_ini_marca IS NULL OR dt_ini_marca < CURRENT_TIMESTAMP)"
		+ "		AND(dt_fim_marca IS NULL OR dt_fim_marca > CURRENT_TIMESTAMP)"
		+ "		AND(id_pessoa_ini = :idPessoaIni)"
		+ "		AND ("
		+ "				select id_tipo_forma_doc from siga.ex_forma_documento where id_forma_doc = ("
		+ "					select id_forma_doc from siga.ex_documento where id_doc = ("
		+ "						select id_doc from siga.ex_mobil where id_mobil = marca.id_ref"
		+ "					)"
		+ "				)"
		+ "			) in (1, 2)"
		+ "	AND id_tp_marca = 1"
		+ "	and id_marcador not in (9,8,10,11,12 ,13,16, 18, 20 , 21, 22, 24 ,26, 32, 62, 63, 64, 7, 50, 51)")})
public abstract class AbstractCpMarcador  extends HistoricoAuditavelSuporte implements Serializable, Historico {

	private static final long serialVersionUID = 6436403895150961831L;

	@Id
	@SequenceGenerator(name = "CP_MARCADOR_LOTACAO_SEQ", sequenceName = "CORPORATIVO.CP_MARCADOR_LOTACAO_SEQ")
	@GeneratedValue(generator = "CP_MARCADOR_LOTACAO_SEQ")
	@Column(name = "ID_MARCADOR", unique = true, nullable = false)
	private Long idMarcador;

	@Column(name = "DESCR_MARCADOR")
	private String descrMarcador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_MARCADOR", nullable = false)
	private CpTipoMarcador cpTipoMarcador;

	@Column(name = "GRUPO_MARCADOR")
	private Integer grupoMarcador;
	
	@Column(name = "ORD_MARCADOR")
	private Integer ordem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTACAO_INI")
	private DpLotacao dpLotacaoIni;

	@Column(name = "DESCR_DETALHADA")
	private String descrDetalhada;

	@Column(name = "COR")
	private String cor;

	@Column(name = "ICONE")
	private String icone;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ID_TP_APLICACAO_MARCADOR")
	private CpMarcadorTipoAplicacaoEnum idTpAplicacao;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ID_TP_DATA_LIMITE")
	private CpMarcadorTipoDataEnum idTpDataLimite;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ID_TP_DATA_PLANEJADA")
	private CpMarcadorTipoDataEnum idTpDataPlanejada;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ID_TP_OPCAO_EXIBICAO")
	private CpMarcadorTipoExibicaoEnum idTpExibicao;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ID_TP_JUSTIFICATIVA")
	private CpMarcadorTipoJustificativaEnum idTpJustificativa;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ID_TP_INTERESSADO")
	private CpMarcadorTipoInteressadoEnum idTpInteressado;

	@Column(name = "HIS_ID_INI")
	@Desconsiderar
	private Long hisIdIni;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HIS_DT_FIM", length = 19)
	@Desconsiderar
	private Date hisDtFim;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HIS_DT_INI", length = 19)
	@Desconsiderar
	private Date hisDtIni;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="HIS_IDC_INI")
	private CpIdentidade hisIdcIni;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="HIS_IDC_FIM")
	private CpIdentidade hisIdcFim;

	@Column(name = "HIS_ATIVO")
	private Integer hisAtivo;

	public Long getIdMarcador() {
		return idMarcador;
	}

	public void setIdMarcador(Long idMarcador) {
		this.idMarcador = idMarcador;
	}

	public String getDescrMarcador() {
		return descrMarcador;
	}

	public void setDescrMarcador(String descrMarcador) {
		this.descrMarcador = descrMarcador;
	}

	public CpTipoMarcador getCpTipoMarcador() {
		return cpTipoMarcador;
	}

	public void setCpTipoMarcador(CpTipoMarcador cpTipoMarcador) {
		this.cpTipoMarcador = cpTipoMarcador;
	}

	public Integer getGrupoMarcador() {
		return grupoMarcador;
	}

	public void setGrupoMarcador(Integer grupoMarcador) {
		this.grupoMarcador = grupoMarcador;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public Long getHisIdIni() {
		return hisIdIni;
	}

	public void setHisIdIni(Long hisIdIni) {
		this.hisIdIni = hisIdIni;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public DpLotacao getDpLotacaoIni() {
		return dpLotacaoIni;
	}

	public void setDpLotacaoIni(DpLotacao dpLotacaoIni) {
		this.dpLotacaoIni = dpLotacaoIni;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public String getDescrDetalhada() {
		return descrDetalhada;
	}

	public void setDescrDetalhada(String descrDetalhada) {
		this.descrDetalhada = descrDetalhada;
	}

	public String getIcone() {
		return icone;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}

	public CpMarcadorTipoAplicacaoEnum getIdTpAplicacao() {
		return idTpAplicacao;
	}

	public void setIdTpAplicacao(CpMarcadorTipoAplicacaoEnum idTpAplicacaoMarcador) {
		this.idTpAplicacao = idTpAplicacaoMarcador;
	}

	public CpMarcadorTipoDataEnum getIdTpDataLimite() {
		return idTpDataLimite;
	}

	public void setIdTpDataLimite(CpMarcadorTipoDataEnum idTpDataLimite) {
		this.idTpDataLimite = idTpDataLimite;
	}

	public CpMarcadorTipoDataEnum getIdTpDataPlanejada() {
		return idTpDataPlanejada;
	}

	public void setIdTpDataPlanejada(CpMarcadorTipoDataEnum idTpDataPlanejada) {
		this.idTpDataPlanejada = idTpDataPlanejada;
	}

	public CpMarcadorTipoExibicaoEnum getIdTpExibicao() {
		return idTpExibicao;
	}

	public void setIdTpExibicao(CpMarcadorTipoExibicaoEnum idTpExibicao) {
		this.idTpExibicao = idTpExibicao;
	}

	public CpMarcadorTipoJustificativaEnum getIdTpJustificativa() {
		return idTpJustificativa;
	}

	public void setIdTpJustificativa(CpMarcadorTipoJustificativaEnum idTpJustificativa) {
		this.idTpJustificativa = idTpJustificativa;
	}

	public CpMarcadorTipoInteressadoEnum getIdTpInteressado() {
		return idTpInteressado;
	}

	public void setIdTpInteressado(CpMarcadorTipoInteressadoEnum idTpInteressado) {
		this.idTpInteressado = idTpInteressado;
	}

	public Date getHisDtFim() {
		return hisDtFim;
	}

	public void setHisDtFim(Date hisDtFim) {
		this.hisDtFim = hisDtFim;
	}

	public Date getHisDtIni() {
		return hisDtIni;
	}

	public void setHisDtIni(Date hisDtIni) {
		this.hisDtIni = hisDtIni;
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

	public Integer getHisAtivo() {
		return this.hisAtivo;
	}
	
	public void setHisAtivo(Integer hisAtivo) {
		this.hisAtivo = getHisAtivo();
	}

}