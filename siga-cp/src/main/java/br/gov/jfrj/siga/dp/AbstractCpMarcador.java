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

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

import br.gov.jfrj.siga.cp.CpTipoMarcadorEnum;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavelSuporte;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorCorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorFinalidadeEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorIconeEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorGrupoEnum;
import br.gov.jfrj.siga.model.Historico;

@SuppressWarnings("serial")
@MappedSuperclass
@NamedQueries({ @NamedQuery(name = "quantidadeDocumentos", query = "SELECT count(1)" + "		FROM CpMarca marca"
		+ "	WHERE (marca.dtIniMarca IS NULL OR marca.dtIniMarca < :dbDatetime)"
		+ "		AND (marca.dtFimMarca IS NULL OR marca.dtFimMarca > :dbDatetime)"
		+ "		AND marca.dpPessoaIni.idPessoa = :idPessoaIni" + "     AND marca.cpTipoMarca.idTpMarca = 1 "
		+ "	    AND marca.cpMarcador.idMarcador not in (9,8,10,11,12 ,13,16, 18, 20 , 21, 22, 24 ,26, 32, 62, 63, 64, 7, 50, 51)") })
public abstract class AbstractCpMarcador extends HistoricoAuditavelSuporte implements Serializable, Historico {

	@Id
	@SequenceGenerator(name = "CP_MARCADOR_LOTACAO_SEQ", sequenceName = "CORPORATIVO.CP_MARCADOR_LOTACAO_SEQ")
	@GeneratedValue(generator = "CP_MARCADOR_LOTACAO_SEQ")
	@Column(name = "ID_MARCADOR", unique = true, nullable = false)
	private Long idMarcador;

	@Column(name = "DESCR_MARCADOR")
	private String descrMarcador;

	@Column(name = "GRUPO_MARCADOR")
	private CpMarcadorGrupoEnum idGrupo;

	@Column(name = "ORD_MARCADOR")
	private Integer ordem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LOTACAO_INI")
	private DpLotacao dpLotacaoIni;

	@Column(name = "DESCR_DETALHADA")
	private String descrDetalhada;

	@Column(name = "ID_COR")
	private CpMarcadorCorEnum idCor;

	@Column(name = "ID_ICONE")
	private CpMarcadorIconeEnum idIcone;

	@Column(name = "ID_FINALIDADE_MARCADOR")
	private CpMarcadorFinalidadeEnum idFinalidade;

//	@Column(name = "ID_TP_APLICACAO_MARCADOR")
//	private CpMarcadorTipoAplicacaoEnum idTpAplicacao;
//
//	@Column(name = "ID_TP_DATA_LIMITE")
//	private CpMarcadorTipoDataEnum idTpDataLimite;
//
//	@Column(name = "ID_TP_DATA_PLANEJADA")
//	private CpMarcadorTipoDataEnum idTpDataPlanejada;
//
//	@Column(name = "ID_TP_OPCAO_EXIBICAO")
//	private CpMarcadorTipoExibicaoEnum idTpExibicao;
//
//	@Column(name = "ID_TP_INTERESSADO")
//	private CpMarcadorTipoInteressadoEnum idTpInteressado;
//
//	@Column(name = "ID_TP_TEXTO")
//	private CpMarcadorTipoTextoEnum idTpTexto;
//
	@Column(name = "HIS_ATIVO")
	private Integer hisAtivo;

	@Column(name = "LISTAVEL_PESQUISA_DEFAULT")
	private Integer listavelPesquisaDefault;

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

//	public CpTipoMarcadorEnum getCpTipoMarcador() {
//		return cpTipoMarcador;
//	}
//
//	public void setCpTipoMarcador(CpTipoMarcadorEnum cpTipoMarcador) {
//		this.cpTipoMarcador = cpTipoMarcador;
//	}
//
	public CpMarcadorGrupoEnum getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(CpMarcadorGrupoEnum idGrupo) {
		this.idGrupo = idGrupo;
	}

	public Integer getOrdem() {
		return ordem;
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

	public String getDescrDetalhada() {
		return descrDetalhada;
	}

	public void setDescrDetalhada(String descrDetalhada) {
		this.descrDetalhada = descrDetalhada;
	}

	public CpMarcadorCorEnum getIdCor() {
		return idCor;
	}

	public void setIdCor(CpMarcadorCorEnum idCor) {
		this.idCor = idCor;
	}

	public CpMarcadorIconeEnum getIdIcone() {
		return idIcone;
	}

	public void setIdIcone(CpMarcadorIconeEnum idIcone) {
		this.idIcone = idIcone;
	}

//	public CpMarcadorTipoAplicacaoEnum getIdTpAplicacao() {
//		return idTpAplicacao;
//	}
//
//	public void setIdTpAplicacao(CpMarcadorTipoAplicacaoEnum idTpAplicacaoMarcador) {
//		this.idTpAplicacao = idTpAplicacaoMarcador;
//	}
//
//	public CpMarcadorTipoDataEnum getIdTpDataLimite() {
//		return idTpDataLimite;
//	}
//
//	public void setIdTpDataLimite(CpMarcadorTipoDataEnum idTpDataLimite) {
//		this.idTpDataLimite = idTpDataLimite;
//	}
//
//	public CpMarcadorTipoDataEnum getIdTpDataPlanejada() {
//		return idTpDataPlanejada;
//	}
//
//	public void setIdTpDataPlanejada(CpMarcadorTipoDataEnum idTpDataPlanejada) {
//		this.idTpDataPlanejada = idTpDataPlanejada;
//	}
//
//	public CpMarcadorTipoExibicaoEnum getIdTpExibicao() {
//		return idTpExibicao;
//	}
//
//	public void setIdTpExibicao(CpMarcadorTipoExibicaoEnum idTpExibicao) {
//		this.idTpExibicao = idTpExibicao;
//	}
//
//	public CpMarcadorTipoTextoEnum getIdTpTexto() {
//		return idTpTexto;
//	}
//
//	public void setIdTpTexto(CpMarcadorTipoTextoEnum idTpTexto) {
//		this.idTpTexto = idTpTexto;
//	}
//
//	public CpMarcadorTipoInteressadoEnum getIdTpInteressado() {
//		return idTpInteressado;
//	}
//
//	public void setIdTpInteressado(CpMarcadorTipoInteressadoEnum idTpInteressado) {
//		this.idTpInteressado = idTpInteressado;
//	}

	//
	// Solução para não precisar criar HIS_ATIVO em todas as tabelas que herdam de
	// HistoricoSuporte.
	//
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

	public CpMarcadorFinalidadeEnum getIdFinalidade() {
		return idFinalidade;
	}

	public void setIdFinalidade(CpMarcadorFinalidadeEnum finalidade) {
		this.idFinalidade = finalidade;
	}

	public boolean isListavelPesquisaDefault() {
		return (listavelPesquisaDefault != null && listavelPesquisaDefault == 1? true : false);
	}

	public void setListavelPesquisaDefault(boolean listavelPesquisaDefault) {
		this.listavelPesquisaDefault = (listavelPesquisaDefault? 1 : 0);
	}

}