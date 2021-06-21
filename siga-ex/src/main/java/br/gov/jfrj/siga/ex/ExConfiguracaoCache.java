/*******************************************************************************
] * Copyright (c) 2006 - 2011 SJRJ.
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
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.ex;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.converter.LongNonNullConverter;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.hibernate.ExDao;

@Entity
@Table(name = "siga.ex_configuracao")
@PrimaryKeyJoinColumn(name = "ID_CONFIGURACAO_EX")
public class ExConfiguracaoCache extends CpConfiguracaoCache {
	
	@Convert(converter = LongNonNullConverter.class)
	@Column(name = "ID_TP_MOV")
	public long exTipoMovimentacao;

	@Convert(converter = LongNonNullConverter.class)
	@Column(name = "ID_TP_DOC")
	public long exTipoDocumento;

	@Convert(converter = LongNonNullConverter.class)
	@Column(name = "ID_TP_FORMA_DOC")
	public long exTipoFormaDoc;

	@Convert(converter = LongNonNullConverter.class)
	@Column(name = "ID_FORMA_DOC")
	public long exFormaDocumento;

	@Convert(converter = LongNonNullConverter.class)
	@Column(name = "ID_MOD")
	public long exModelo;

	@Convert(converter = LongNonNullConverter.class)
	@Column(name = "ID_CLASSIFICACAO")
	public long exClassificacao;

	@Convert(converter = LongNonNullConverter.class)
	@Column(name = "ID_VIA")
	public long exVia;

	@Convert(converter = LongNonNullConverter.class)
	@Column(name = "ID_NIVEL_ACESSO")
	public long exNivelAcesso;

	@Convert(converter = LongNonNullConverter.class)
	@Column(name = "ID_PAPEL")
	public long exPapel;

	public ExConfiguracaoCache() {
	}

	public ExConfiguracaoCache(ExConfiguracao cfg) {
		super(cfg);
		this.exTipoMovimentacao = longOrZero(cfg.getExTipoMovimentacao() != null ? cfg.getExTipoMovimentacao().getId() : null);
		this.exTipoDocumento = longOrZero(cfg.getExTipoDocumento() != null ? cfg.getExTipoDocumento().getId() : null);
		this.exTipoFormaDoc = longOrZero(cfg.getExTipoFormaDoc() != null ? cfg.getExTipoFormaDoc().getId() : null);
		this.exFormaDocumento = longOrZero(cfg.getExFormaDocumento() != null ? cfg.getExFormaDocumento().getId() : null);
		this.exModelo = longOrZero(cfg.getExModelo() != null ? cfg.getExModelo().getIdInicial() : null);
		this.exClassificacao = longOrZero(
				cfg.getExClassificacao() != null ? cfg.getExClassificacao().getIdInicial() : null);
		this.exVia = longOrZero(cfg.getExVia() != null ? cfg.getExVia().getIdInicial() : null);
		this.exNivelAcesso = longOrZero(cfg.getExNivelAcesso() != null ? cfg.getExNivelAcesso().getIdNivelAcesso() : null);
		this.exPapel = longOrZero(cfg.getExPapel() != null ? cfg.getExPapel().getIdPapel() : null);
	}

	public boolean podeAdicionarComoPublicador(DpPessoa titular,
			DpLotacao lotacaoTitular) {
		return (dpPessoa != 0 && titular != null && ExDao.getInstance().consultar(dpPessoa, DpPessoa.class, false)
				.getOrgaoUsuario().getId()
				.equals(titular.getOrgaoUsuario().getId()))
				|| (lotacao != 0 && lotacaoTitular != null && ExDao.getInstance().consultar(lotacao, DpLotacao.class, false)
						.getOrgaoUsuario().getId()
						.equals(lotacaoTitular.getOrgaoUsuario().getId()));
	}

}
