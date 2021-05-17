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

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Selecionavel;

@SuppressWarnings("serial")
@Entity
@Table(name = "corporativo.cp_servico")
@Immutable
//@Cacheable
@Cache(region = CpDao.CACHE_CORPORATIVO, usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class CpServico extends AbstractCpServico implements Selecionavel {

	public static ActiveRecord<CpServico> AR = new ActiveRecord<>(
			CpServico.class);

	// SIGA e seus módulos

	public static final long SERVICO_SIGA = 1;
	public static final long SERVICO_SIGA_EX = 2;
	public static final long SERVICO_SIGA_WF = 3;
	public static final long SERVICO_SIGA_SR = 4;
	
	
	public static final String ACESSO_WEBSERVICE = "SPSEMPAPEL-SIGA-WS";
	

	public CpServico() {

	}

	public boolean equivale(Object other) {
		if (other == null)
			return false;
		if (this.getIdServico() == null || ((CpServico) other).getIdServico() == null)
			return false;
		return this.getIdServico().longValue() == ((CpServico) other)
				.getIdServico().longValue();
	}

	public String getDescricao() {
		return getDscServico();
	}

	public Long getId() {
		return getIdServico();
	}

	public String getSigla() {
		return getSiglaServico();
	}

	public void setSigla(String sigla) {
		setSiglaServico(sigla);
	}

	/**
	 * @return retorna a sigla retirando a parte inicial pertencente ao seviço
	 *         pai.
	 * 
	 */
	public String getSiglaSemPartePai() {
		String siglaSrv = getSigla();
		if (getCpServicoPai() != null) {
			String siglaPai = getCpServicoPai().getSigla();
			int pos = siglaSrv.indexOf(siglaPai);
			int tam = siglaPai.length();
			if (pos >= 0 && tam > 0) {
				siglaSrv = siglaSrv.substring(pos + tam, siglaSrv.length());
			}
			if (siglaSrv.charAt(0) == '-') {
				siglaSrv = siglaSrv.substring(1);
			}
		}
		return siglaSrv;
	}

	/**
	 * @return retorna o nível hierárquico do serviço
	 */
	public int getNivelHierarquico() {
		if (getCpServicoPai() == null) {
			return 0;
		} else {
			return getCpServicoPai().getNivelHierarquico() + 1;
		}
	}
}
