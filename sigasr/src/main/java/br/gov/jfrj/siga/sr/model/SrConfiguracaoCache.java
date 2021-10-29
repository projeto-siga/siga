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
package br.gov.jfrj.siga.sr.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.converter.LongNonNullConverter;
import br.gov.jfrj.siga.dp.DpLotacao;

@Entity
@Table(name = "sigasr.sr_configuracao")
@PrimaryKeyJoinColumn(name = "ID_CONFIGURACAO_SR")
public class SrConfiguracaoCache extends CpConfiguracaoCache {

	@Convert(converter = LongNonNullConverter.class)
	@Column(name = "ID_ATENDENTE")
	public long atendente;

	@Convert(converter = LongNonNullConverter.class)
	@Column(name = "ID_TIPO_ATRIBUTO")
	public long atributo;

	@Convert(converter = LongNonNullConverter.class)
	@Column(name = "ID_PESQUISA")
	public long pesquisaSatisfacao;

	@Convert(converter = LongNonNullConverter.class)
	@Column(name = "ID_LISTA")
	public long listaPrioridade;

	@Column(name = "PRIORIDADE_LISTA")
	@Enumerated
	public SrPrioridade prioridadeNaLista;

	@Convert(converter = LongNonNullConverter.class)
	@Column(name = "ID_ACORDO")
	public long acordo;

	@Column(name = "FG_ATRIBUTO_OBRIGATORIO")
	@Type(type = "yes_no")
	public boolean atributoObrigatorio;
	
    @ManyToMany(fetch = FetchType.LAZY)
    public @JoinTable(name = "sr_configuracao_item", schema = "sigasr", joinColumns = { @JoinColumn(name = "ID_CONFIGURACAO") }, inverseJoinColumns = { @JoinColumn(name = "ID_ITEM_CONFIGURACAO") }) List<SrItemConfiguracao> itemConfiguracaoSet;
    
//    @Transient
//    private SrAcao acaoFiltro;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sr_configuracao_acao", schema = "sigasr", joinColumns = { @JoinColumn(name = "ID_CONFIGURACAO") }, inverseJoinColumns = { @JoinColumn(name = "ID_ACAO") })
    public List<SrAcao> acoesSet;
    
//    @Enumerated
//    public SrPrioridade prioridade;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sr_configuracao_permissao", joinColumns = @JoinColumn(name = "ID_CONFIGURACAO"), inverseJoinColumns = @JoinColumn(name = "TIPO_PERMISSAO"), schema = "sigasr") 
    public List<SrTipoPermissaoLista> tipoPermissaoSet;

    @Enumerated
    private SrPrioridade prioridade;

    @Transient
    private SrItemConfiguracao itemConfiguracaoFiltro;
    
    @Transient
    private SrAcao acaoFiltro;
    

    @Transient
	public boolean herdado;

    @Transient
	public boolean utilizarItemHerdado;


	public SrConfiguracaoCache() {
	}

	public SrConfiguracaoCache(SrConfiguracao cfg) {
		super(cfg);
		this.atendente = longOrZero(cfg.getAtendente() != null ? cfg.getAtendente().getIdInicial() : null);
		this.atributo = longOrZero(cfg.getAtributo() != null ? cfg.getAtributo().getIdInicial() : null);
		this.pesquisaSatisfacao = longOrZero(cfg.getPesquisaSatisfacao() != null ? cfg.getPesquisaSatisfacao().getIdInicial() : null);
		this.listaPrioridade = longOrZero(cfg.getListaPrioridade() != null ? cfg.getListaPrioridade().getIdInicial() : null);
		this.prioridadeNaLista = cfg.getPrioridadeNaLista();
		this.acordo = longOrZero(cfg.getAcordo() != null ? cfg.getAcordo().getIdInicial() : null);
		this.atributoObrigatorio = cfg.isAtributoObrigatorio();
		this.itemConfiguracaoSet = cfg.getItemConfiguracaoSet();
		this.acoesSet = cfg.getAcoesSet();
		this.acaoFiltro = cfg.getAcaoFiltro() != null ? cfg.getAcaoFiltro() : null;
		this.itemConfiguracaoFiltro = cfg.getItemConfiguracaoFiltro() != null ? cfg.getItemConfiguracaoFiltro() : null;
	}
	
    public int getNivelItemParaComparar() {
        int soma = 0;
        if (itemConfiguracaoSet != null && !itemConfiguracaoSet.isEmpty()) {
            for (SrItemConfiguracao i : itemConfiguracaoSet) {
                SrItemConfiguracao iAtual = i.getAtual();
                if (iAtual != null)
                    soma += i.getNivel();
            }
            return soma / itemConfiguracaoSet.size();
        }
        return 0;
    }

    public int getNivelAcaoParaComparar() {
        int soma = 0;
        if (acoesSet != null && !acoesSet.isEmpty()) {
            for (SrAcao i : acoesSet) {
                SrAcao iAtual = i.getAtual();
                if (iAtual != null)
                    soma += i.getNivel();
            }
            return soma / acoesSet.size();
        }
        return 0;
    }

	public SrPrioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(SrPrioridade prioridade) {
		this.prioridade = prioridade;
	}

	public SrItemConfiguracao getItemConfiguracaoFiltro() {
		return itemConfiguracaoFiltro;
	}

	public void setItemConfiguracaoFiltro(SrItemConfiguracao itemConfiguracaoFiltro) {
		this.itemConfiguracaoFiltro = itemConfiguracaoFiltro;
	}

	public SrAcao getAcaoFiltro() {
		return this.acaoFiltro;
	}

	public void setAcaoFiltro(SrAcao acaoFiltro) {
		this.acaoFiltro = acaoFiltro;
	}
	
	public DpLotacao getLotacaoAtendente() {
		if (this.atendente == 0)
			return null;
		return DpLotacao.AR.findById(this.atendente);
	}
	
	public long getIdConfiguracao() {
		return this.idConfiguracao;
	}
}
