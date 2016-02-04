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
package br.gov.jfrj.siga.wf;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

/**
 * Esta classe representa uma permissão para instaciação de procedimentos e é utilizada na view configurarInstanciacao.jsp 
 * 
 * @author kpf
 * 
 */
public class Permissao {
    private String procedimento;
    private Long id;
    private DpPessoa pessoa;
    private DpLotacao lotacao;
    private Integer tipoResponsavel;
    
    /**
     * Retorna o tipo de responsável que pode instanciar um procedimento.
     * @return 0 - INDEFINIDO (sem definição de permissão)
     * 1 - MATRICULA (permissão para uma pessoa)
     * 2 - LOTACAO (permissão para uma lotação inteira, ou seja, todos da lotação)
     */
	public Integer getTipoResponsavel() {
		return tipoResponsavel;
	}
	
    /**
     * Informa o tipo de responsável que pode instanciar um procedimento.
     * @return 0 - INDEFINIDO (sem definição de permissão)
     * 1 - MATRICULA (permissão para uma pessoa)
     * 2 - LOTACAO (permissão para uma lotação inteira, ou seja, todos da lotação)
     */
	public void setTipoResponsavel(Integer tipoResponsavel) {
		this.tipoResponsavel = tipoResponsavel;
	}
	
	/**
	 * Retorna o procedimento referente à permissão.
	 * 
	 * @return
	 */
	public String getProcedimento() {
		return procedimento;
	}
	
	/**
	 * Informa o procedimento referente à permissão.
	 *  
	 */
	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}
	
	/**
	 * Retorna a pessoa referente à permissão.
	 * @return
	 */
	public DpPessoa getPessoa() {
		return pessoa;
	}
	
	/**
	 * Informa  a pessoa referente à permissão.
	 * @param pessoa
	 */
	public void setPessoa(DpPessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	/**
	 *  Retorna a lotação referente à permissão.
	 * @return
	 */
	public DpLotacao getLotacao() {
		return lotacao;
	}
	
	/**
	 * Informa a lotação referente à permissão.
	 * @param lotacao
	 */
	public void setLotacao(DpLotacao lotacao) {
		this.lotacao = lotacao;
	}

	/**
	 * Retorna o id da permissão.
	 * @return
	 */
	public Long getId() {
		return this.id;
	}
	
	/**
	 * Informa o id da permissão.
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

}
