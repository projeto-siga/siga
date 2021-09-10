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

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

@Entity
@Table(name = "corporativo.cp_grupo")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ID_TP_GRUPO", discriminatorType = DiscriminatorType.INTEGER)
public abstract class CpGrupo extends AbstractCpGrupo implements Selecionavel,
		Comparable<CpGrupo>, CpConvertableEntity {

	public CpGrupo() {
	}

	public int compareTo(CpGrupo o) {
		return getId().compareTo(o.getId());
	}

	public static CpGrupo getInstance(Integer IdTipoGrupo)
			throws AplicacaoException {
		CpGrupo t_grpGrupo;
		switch (IdTipoGrupo) {
		case CpTipoGrupo.TIPO_GRUPO_GRUPO_DE_DISTRIBUICAO:
			t_grpGrupo = new CpGrupoDeEmail();
			break;
		case CpTipoGrupo.TIPO_GRUPO_PERFIL_DE_ACESSO:
			t_grpGrupo = new CpPerfil();
			break;
		case CpTipoGrupo.TIPO_GRUPO_PERFIL_JEE:
			t_grpGrupo = new CpPerfilJEE();
			break;
		default:
			throw new AplicacaoException("Id do tipo do grupo inválido: "
					+ IdTipoGrupo);
		}
		return t_grpGrupo;
	}

	public String getDescricao() {
		return getDscGrupo();
	}

	public Long getId() {
		return getIdGrupo();
	}

	public void setId(Long id) {
		setIdGrupo(id);
	}

	public String getSigla() {
		return getSiglaGrupo();
	}

	public void setSigla(String sigla) {
		setSiglaGrupo(sigla);
	}

	public int getNivel() {
		if (getCpGrupoPai() != null && getCpGrupoPai().getId() != null)
			return getCpGrupoPai().getNivel() + 1;
		return 0;
	}

	public boolean semelhante(Assemelhavel obj, int nivel) {
		return SincronizavelSuporte.semelhante(this, obj, nivel);
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
}
