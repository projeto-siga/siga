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
package br.gov.jfrj.siga.cp;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;



@SuppressWarnings("serial")
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Table(name = "corporativo.cp_identidade")
public class CpIdentidade extends AbstractCpIdentidade {

	public static final long pinLength = 8L;
	

	public DpPessoa getPessoaAtual() {
		return CpDao.getInstance().consultarPorIdInicial(
				getDpPessoa().getIdInicial());
	}

	public String getDtExpiracaoDDMMYYYY() {
		if (getDtExpiracaoIdentidade() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(getDtExpiracaoIdentidade());
		}
		return "";
	}

	public String getDtCriacaoDDMMYYYY() {
		if (getDtCriacaoIdentidade() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(getDtCriacaoIdentidade());
		}
		return "";
	}

	public String getDtCancelamentoDDMMYYYY() {
		if (getDtCriacaoIdentidade() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(getDtCancelamentoIdentidade());
		}
		return "";
	}

	public boolean isBloqueada() throws AplicacaoException {
		return Cp.getInstance().getComp().isIdentidadeBloqueada(this);
	}

	public boolean equivale(Object other) {
		if (other == null)
			return false;
		return this.getIdInicial().longValue() == ((CpIdentidade) other)
				.getIdInicial().longValue();
	}

	public void setIdInicial(Long idInicial) {
		setHisIdIni(idInicial);
	}

	public Long getId() {
		return getIdIdentidade();
	}

	public void setId(Long id) {
		setIdIdentidade(id);
	}

	public boolean semelhante(Assemelhavel obj, int nivel) {
		return SincronizavelSuporte.semelhante(this, obj, nivel);
	}

	public boolean ativaNaData(Date dt) {
		return super.ativoNaData(dt);
	}
	
	public boolean isSenhaUsuarioExpirada() {		
		final Integer diasExpiracaoSenha = Prop.getInt("senha.usuario.expiracao.dias");					
		
		if (diasExpiracaoSenha == null) {
			return false;
		}					
		
		LocalDate hoje = CpDao.getInstance().consultarDataEHoraDoServidor().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();		
		LocalDate ultimaTrocaDeSenha = getDtCriacaoIdentidade().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();							
		long diasUltimaTrocaSenha = ChronoUnit.DAYS.between(ultimaTrocaDeSenha, hoje);				
				
		return diasUltimaTrocaSenha >= diasExpiracaoSenha;
	}

	@Override
	public String toString() {
		return "CpIdentidade(" + String.valueOf(getId()) + ")";
	}

	//
	// Solução para não precisar criar HIS_ATIVO em todas as tabelas que herdam
	// de HistoricoSuporte.
	//
	@Column(name = "HIS_ATIVO")
	private Integer hisAtivo;

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
	
	public boolean isPinCadastrado()  {
		return this.getPinIdentidade() != null;
	}
}
