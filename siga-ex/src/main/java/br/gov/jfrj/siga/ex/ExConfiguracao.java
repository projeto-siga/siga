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
 * Criado em  12/12/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.ex;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.jfrj.siga.cp.AbstractCpConfiguracao;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

@Entity
@Table(name = "siga.ex_configuracao")
@PrimaryKeyJoinColumn(name = "ID_CONFIGURACAO_EX")
public class ExConfiguracao extends CpConfiguracao {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_MOV")
	private ExTipoMovimentacao exTipoMovimentacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_DOC")
	private ExTipoDocumento exTipoDocumento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TP_FORMA_DOC")
	private ExTipoFormaDoc exTipoFormaDoc;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FORMA_DOC")
	private ExFormaDocumento exFormaDocumento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MOD")
	private ExModelo exModelo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLASSIFICACAO")
	private ExClassificacao exClassificacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_VIA")
	private ExVia exVia;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_NIVEL_ACESSO")
	private ExNivelAcesso exNivelAcesso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PAPEL")
	private ExPapel exPapel;

	public ExConfiguracao() {
	}

	public ExConfiguracao(CpConfiguracao configuracaoBaseParaExConfiguracao) {
		if (configuracaoBaseParaExConfiguracao.isAtivo())
			this.updateAtivo();
		this.setCargo(configuracaoBaseParaExConfiguracao.getCargo());
		this.setComplexo(configuracaoBaseParaExConfiguracao.getComplexo());
		this.setConfiguracaoInicial(configuracaoBaseParaExConfiguracao.getConfiguracaoInicial());
		this.setConfiguracoesPosteriores(configuracaoBaseParaExConfiguracao.getConfiguracoesPosteriores());
		this.setCpGrupo(configuracaoBaseParaExConfiguracao.getCpGrupo());
		this.setCpIdentidade(configuracaoBaseParaExConfiguracao.getCpIdentidade());
		this.setCpServico(configuracaoBaseParaExConfiguracao.getCpServico());
		this.setCpSituacaoConfiguracao(configuracaoBaseParaExConfiguracao.getCpSituacaoConfiguracao());
		this.setCpTipoConfiguracao(configuracaoBaseParaExConfiguracao.getCpTipoConfiguracao());
		this.setCpTipoLotacao(configuracaoBaseParaExConfiguracao.getCpTipoLotacao());
		this.setDpPessoa(configuracaoBaseParaExConfiguracao.getDpPessoa());
		this.setDscFormula(configuracaoBaseParaExConfiguracao.getDscFormula());
		this.setDtFimVigConfiguracao(configuracaoBaseParaExConfiguracao.getDtFimVigConfiguracao());
		this.setDtIniVigConfiguracao(configuracaoBaseParaExConfiguracao.getDtIniVigConfiguracao());
		this.setFuncaoConfianca(configuracaoBaseParaExConfiguracao.getFuncaoConfianca());
		this.setHisAtivo(configuracaoBaseParaExConfiguracao.getHisAtivo());
		this.setHisDtFim(configuracaoBaseParaExConfiguracao.getHisDtFim());
		this.setHisDtIni(configuracaoBaseParaExConfiguracao.getHisDtIni());
		this.setHisIdcFim(configuracaoBaseParaExConfiguracao.getHisIdcFim());
		this.setHisIdcIni(configuracaoBaseParaExConfiguracao.getHisIdcIni());
		this.setHisIdIni(configuracaoBaseParaExConfiguracao.getHisIdIni());
		this.setId(configuracaoBaseParaExConfiguracao.getId());
		this.setIdConfiguracao(configuracaoBaseParaExConfiguracao.getIdConfiguracao());
		this.setLotacao(configuracaoBaseParaExConfiguracao.getLotacao());
		this.setNmEmail(configuracaoBaseParaExConfiguracao.getNmEmail());
		this.setOrgaoObjeto(configuracaoBaseParaExConfiguracao.getOrgaoObjeto());
		this.setOrgaoUsuario(configuracaoBaseParaExConfiguracao.getOrgaoUsuario());
		return;
	}

	public ExTipoMovimentacao getExTipoMovimentacao() {
		return exTipoMovimentacao;
	}

	public void setExTipoMovimentacao(ExTipoMovimentacao exTipoMovimentacao) {
		this.exTipoMovimentacao = exTipoMovimentacao;
	}

	public ExTipoDocumento getExTipoDocumento() {
		return exTipoDocumento;
	}

	public void setExTipoDocumento(ExTipoDocumento exTipoDocumento) {
		this.exTipoDocumento = exTipoDocumento;
	}

	public ExTipoFormaDoc getExTipoFormaDoc() {
		return exTipoFormaDoc;
	}

	public void setExTipoFormaDoc(ExTipoFormaDoc exTipoFormaDoc) {
		this.exTipoFormaDoc = exTipoFormaDoc;
	}

	public ExFormaDocumento getExFormaDocumento() {
		return exFormaDocumento;
	}

	public void setExFormaDocumento(ExFormaDocumento exFormaDocumento) {
		this.exFormaDocumento = exFormaDocumento;
	}

	public ExModelo getExModelo() {
		return exModelo;
	}

	public void setExModelo(ExModelo exModelo) {
		this.exModelo = exModelo;
	}

	public ExClassificacao getExClassificacao() {
		return exClassificacao;
	}

	public void setExClassificacao(ExClassificacao exClassificacao) {
		this.exClassificacao = exClassificacao;
	}

	public ExVia getExVia() {
		return exVia;
	}

	public void setExVia(ExVia exVia) {
		this.exVia = exVia;
	}

	public ExNivelAcesso getExNivelAcesso() {
		return exNivelAcesso;
	}

	public void setExNivelAcesso(ExNivelAcesso exNivelAcesso) {
		this.exNivelAcesso = exNivelAcesso;
	}

	public ExPapel getExPapel() {
		return exPapel;
	}

	public void setExPapel(ExPapel exPapel) {
		this.exPapel = exPapel;
	}

	public boolean isAgendamentoPublicacaoBoletim() {
		return getExTipoMovimentacao() != null
				&& getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO_BOLETIM;
	}

	public boolean podeAdicionarComoPublicador(DpPessoa titular,
			DpLotacao lotacaoTitular) {
		return (getDpPessoa() != null && titular != null && getDpPessoa()
				.getOrgaoUsuario().getId()
				.equals(titular.getOrgaoUsuario().getId()))
				|| (getLotacao() != null && lotacaoTitular != null && getLotacao()
						.getOrgaoUsuario().getId()
						.equals(lotacaoTitular.getOrgaoUsuario().getId()));
	}

	@Override
	public void atualizarObjeto() {
		super.atualizarObjeto();
		setExModelo(atual(getExModelo()));
		setExClassificacao(atual(getExClassificacao()));
		setExVia(atual(getExVia()));
	}

	public void substituirPorObjetoInicial() {
		super.substituirPorObjetoInicial();
		setExModelo(inicial(getExModelo()));
		setExClassificacao(inicial(getExClassificacao()));
		setExVia(inicial(getExVia()));
	}

	@Override
	public CpConfiguracaoCache converterParaCache() {
		return new ExConfiguracaoCache(this);
	}

}
