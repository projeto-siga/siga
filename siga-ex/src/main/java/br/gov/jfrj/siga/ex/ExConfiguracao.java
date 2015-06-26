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

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class ExConfiguracao extends CpConfiguracao {

	private ExTipoMovimentacao exTipoMovimentacao;

	private ExTipoDocumento exTipoDocumento;

	private ExTipoFormaDoc exTipoFormaDoc;

	private ExFormaDocumento exFormaDocumento;

	private ExModelo exModelo;

	private ExClassificacao exClassificacao;

	private ExVia exVia;

	private ExNivelAcesso exNivelAcesso;

	private ExPapel exPapel;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3624557793773660738L;

	public ExConfiguracao() {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isAgendamentoPublicacaoBoletim() {
		return getExTipoMovimentacao() != null 
				&& getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO_BOLETIM;
	}

	public boolean podeAdicionarComoPublicador(DpPessoa titular, DpLotacao lotacaoTitular) {
		return (getDpPessoa() != null 
					&& titular != null 
					&& getDpPessoa().getOrgaoUsuario().getId().equals(titular.getOrgaoUsuario().getId()))
				|| (getLotacao() != null 
						&& lotacaoTitular != null 
						&& getLotacao().getOrgaoUsuario().getId().equals(lotacaoTitular.getOrgaoUsuario().getId()));
	}
}
