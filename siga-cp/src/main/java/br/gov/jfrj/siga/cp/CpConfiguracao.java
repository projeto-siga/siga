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
package br.gov.jfrj.siga.cp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

@SuppressWarnings("serial")
@Entity
@Table(name = "corporativo.cp_configuracao")
@Inheritance(strategy = InheritanceType.JOINED)
public class CpConfiguracao extends AbstractCpConfiguracao implements CpConvertableEntity {

	public static final ActiveRecord<CpConfiguracao> AR = new ActiveRecord<>(CpConfiguracao.class);

	public CpConfiguracao() {
	}

	@Transient
	private boolean buscarPorPerfis = false;

	public boolean isBuscarPorPerfis() {
		return buscarPorPerfis;
	}

	public void setBuscarPorPerfis(boolean buscarPorPerfis) {
		this.buscarPorPerfis = buscarPorPerfis;
	}

	public boolean isEspecifica(CpConfiguracao filtro) {
		if (filtro.getDpPessoa() != null)
			return getDpPessoa() != null;
		if (filtro.getLotacao() != null)
			return getLotacao() != null;
		if (filtro.getOrgaoUsuario() != null)
			return getOrgaoUsuario() != null;
		if (filtro.getCpGrupo() != null)
			return getCpGrupo() != null && getCpGrupo().getId().equals(filtro.getCpGrupo().getId());
		return false;
	}

	public Long getId() {
		return getIdConfiguracao();
	}

	public void setId(Long id) {
		setIdConfiguracao(id);
	}

	public boolean semelhante(Assemelhavel obj, int nivel) {
		return SincronizavelSuporte.semelhante(this, obj, nivel);
	}

	/**
	 * 
	 * @return retorna o objeto que é a origem da configuração
	 */
	public Object getOrigem() {
		if (getDpPessoa() != null) {
			return getDpPessoa();
		} else if (getLotacao() != null) {
			return getLotacao();
		} else if (getCpGrupo() != null) {
			return getCpGrupo();
		} else if (getOrgaoUsuario() != null) {
			return getOrgaoUsuario();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @return retorna uma string representativa da origem para exibições curtas
	 */
	public String printOrigemCurta() {
		Object ori = getOrigem();
		if (ori instanceof DpPessoa) {
			DpPessoa pes = (DpPessoa) ori;
			return (pes.getSesbPessoa() + pes.getMatricula());
		} else if (ori instanceof DpLotacao) {
			return ((DpLotacao) ori).getSiglaLotacao();
		} else if (ori instanceof CpGrupo) {
			return ((CpGrupo) ori).getSiglaGrupo();
		} else if (ori instanceof CpOrgaoUsuario) {
			return ((CpOrgaoUsuario) ori).getSiglaOrgaoUsu();
		} else {
			return new String();
		}
	}

	/**
	 * 
	 * @return retorna uma String representativa da origem
	 */
	public String printOrigem() {
		Object ori = getOrigem();
		if (ori instanceof DpPessoa) {
			return ((DpPessoa) ori).getNomePessoa();
		} else if (ori instanceof DpLotacao) {
			return ((DpLotacao) ori).getNomeLotacao();
		} else if (ori instanceof CpGrupo) {
			return ((CpGrupo) ori).getDescricao();
		} else if (ori instanceof CpOrgaoUsuario) {
			return ((CpOrgaoUsuario) ori).getNmOrgaoUsu();
		} else {
			return new String();
		}
	}

	public boolean ativaNaData(Date dt) {
		return super.ativoNaData(dt);
	}

	/**
	 * Retorna a data de fim de vigência no formato dd/mm/aa HH:MM:SS, por exemplo,
	 * 01/02/10 17:52:23.
	 */
	public String getHisDtFimDDMMYY_HHMMSS() {
		if (getHisDtFim() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			return df.format(getHisDtFim());
		}
		return "";
	}

	public String getHisDtIniDDMMYY() {
		if (getHisDtIni() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			return df.format(getHisDtIni());
		}
		return "";
	}

	/**
	 * Retorna a configuração atual no histórico desta configuraçõo
	 * 
	 * @return CpConfiguracao
	 */
	public CpConfiguracao getConfiguracaoAtual() {
		CpConfiguracao ini = getConfiguracaoInicial();
		Set<CpConfiguracao> setConfs = ini.getConfiguracoesPosteriores();
		if (setConfs != null)
			for (CpConfiguracao l : setConfs)
				return l;

		return this;
	}

	@Override
	public String toString() {
		return "id: " + getId() + " ,pessoa: " + (getDpPessoa() != null ? getDpPessoa().getNomePessoa() : "")
				+ " ,lotacao: " + (getLotacao() != null ? getLotacao().getSigla() : "") + " ,situação: "
				+ (getCpSituacaoConfiguracao() != null ? getCpSituacaoConfiguracao().getDescr() : "")
				+ " ,tipo conf: " + (getCpTipoConfiguracao().getDescr());
	}

	public void atualizarObjeto() {
		setLotacao(atual(getLotacao()));
		setCargo(atual(getCargo()));
		setFuncaoConfianca(atual(getFuncaoConfianca()));
		setDpPessoa(atual(getDpPessoa()));
		setCpIdentidade(atual(getCpIdentidade()));
		setLotacaoObjeto(atual(getLotacaoObjeto()));
		setCargoObjeto(atual(getCargoObjeto()));
		setFuncaoConfiancaObjeto(atual(getFuncaoConfiancaObjeto()));
		setPessoaObjeto(atual(getPessoaObjeto()));
		setCpGrupo(atual(getCpGrupo()));
	}

	public void substituirPorObjetoInicial() {
		setLotacao(inicial(getLotacao()));
		setCargo(inicial(getCargo()));
		setFuncaoConfianca(inicial(getFuncaoConfianca()));
		setDpPessoa(inicial(getDpPessoa()));
		setCpIdentidade(inicial(getCpIdentidade()));
		setLotacaoObjeto(inicial(getLotacaoObjeto()));
		setCargoObjeto(inicial(getCargoObjeto()));
		setFuncaoConfiancaObjeto(inicial(getFuncaoConfiancaObjeto()));
		setPessoaObjeto(inicial(getPessoaObjeto()));
		setCpGrupo(inicial(getCpGrupo()));
	}

	public <T extends Historico> T atual(final T antigo) {
		if (antigo == null)
			return null;
		return CpDao.getInstance().obterAtual(antigo);
	}

	public <T extends Historico> T inicial(final T antigo) {
		if (antigo == null)
			return null;
		return CpDao.getInstance().obterInicial(antigo);
	}
	
	public CpConfiguracaoCache converterParaCache() {
		return new CpConfiguracaoCache(this);
	}

}
