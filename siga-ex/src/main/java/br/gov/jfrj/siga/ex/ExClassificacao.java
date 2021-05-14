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
 * Created Mon Nov 14 13:36:35 GMT-03:00 2005 by MyEclipse Hibernate Tool.
 */
package br.gov.jfrj.siga.ex;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.util.MascaraUtil;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Selecionavel;

/**
 * A class that represents a row in the 'EX_CLASSIFICACAO' table. This class may
 * be customized as it is never re-generated after being created.
 */
@SuppressWarnings("serial")
@Entity
@BatchSize(size = 500)
//@AttributeOverride(name = "hisAtivo", column = @Column(name = "HIS_ATIVO"))
@Table(name = "siga.ex_classificacao")
@Cache(region = ExDao.CACHE_EX, usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class ExClassificacao extends AbstractExClassificacao implements
		Serializable, Selecionavel {
	
	/**
	 * Simple constructor of ExClassificacao instances.
	 */
	public ExClassificacao() {
	}

	public Long getId() {
		return getIdClassificacao();
	}

	private String Zeros(final long l, final int i) {
		String s = Long.toString(l);
		while (s.length() < i)
			s = "0" + s;
		return s;
	}

	/**
	 * Verifica se uma classificação é do tipo intermediária.
	 * 
	 * 
	 * @return Verdadeiro caso a classificação seja do tipo intermediária e
	 *         Falso caso a classificação não seja do tipo intermediária.
	 * 
	 */
	public boolean isIntermediaria() throws AplicacaoException {
		if (getExViaSet() == null) {
			return true;
		}
		if (getExViaSet() != null && getExViaSet().size() == 1) {
			ExVia via = (ExVia) (getExViaSet().toArray()[0]);
			if (via.getCodVia() == null) {
				return true;
			}
		}
		if (getExViaSet() != null && getExViaSet().size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Retorna a sigla de uma classificação.
	 * 
	 */
	public String getSigla() {
		return getCodificacao();
	}

	/**
	 * Informa a sigla de uma classificação.
	 * 
	 * @param sigla
	 * 
	 */
	public void setSigla(final String sigla) {
		setCodificacao(sigla);
	}

	/**
	 * 
	 * 
	 * Retorna a descrição de uma classificação. A descrição de uma
	 * classificação é formada pela descrição do Assunto, da classe e da
	 * subclasse.
	 * 
	 * @param sigla
	 * 
	 */
	public String getDescricao() {
		return ExDao.getInstance().consultarDescricaoExClassificacao(this);
	}

	/**
	 * Retorna a sigla e a descrição simples, ou seja, não trazendo a informação
	 * completa sobre a hierarquia a que a classificação pertence.
	 * 
	 * @param sigla
	 * 
	 */
	public String getDescricaoSimples() {
		return getSigla() + " - " + getDescrClassificacao();
	}

	public String getDescricaoCompleta() {
		return getSigla() + " - " + getDescricao();
	}

	public String getNome() {
		return getDescrClassificacao();
	}

	public Integer getNumVias() {
		return getExViaSet() == null ? 0 : getExViaSet().size();
	}

	public String getDestinacoesFinais() {
		return "";
	}

	public ExClassificacao getClassificacaoAtual() {
		if (this.getHisDtFim() != null)
			return ExDao.getInstance().obterClassificacaoAtual(this);
		return this;
	}

	/**
	 * Verifica se uma classificação está fechada.
	 * 
	 * 
	 * @return Verdadeiro se a classificação está fechado e falso caso
	 *         contrário.
	 * 
	 */
	public boolean isFechada() {
		
		return getHisDtFim() != null;
	}

	public ExClassificacao getAtual() {
		return getClassificacaoAtual();
	}

	public void setId(Long id) {
		setIdClassificacao(id);

	}

	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	public int getNivel() {
		return MascaraUtil.getInstance().calcularNivel(this.getCodificacao());
	}

	@Override
	public String toString() {
		return getCodificacao() + " " + getDescricao();
	}

	//
	// Solução para não precisar criar HIS_ATIVO em todas as tabelas que herdam de HistoricoSuporte.
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
