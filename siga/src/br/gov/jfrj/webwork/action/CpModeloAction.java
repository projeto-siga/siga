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
 * Criado em  23/11/2005
 *
 */
package br.gov.jfrj.webwork.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpModelo;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.libs.webwork.SigaActionSupport;

import com.opensymphony.xwork.Action;

public class CpModeloAction extends SigaActionSupport {

	private List<CpModelo> itens;

	private Long id;

	private String conteudo;

	public CpModelo daoMod(long id) {
		return dao().consultar(id, CpModelo.class, false);
	}

	public String aListar() throws Exception {
		assertAcesso("FE:Ferramentas;MODVER:Visualizar modelos");
		setItens(dao().consultaCpModelos());
		return Action.SUCCESS;
	}

	public String aEditarGravar() throws Exception {
		assertAcesso("FE:Ferramentas;MODEDITAR:Editar modelos");

		if (getId() != null) {
			CpModelo mod = daoMod(getId());
			Cp.getInstance().getBL().alterarCpModelo(mod, conteudo,
					getIdentidadeCadastrante());
		} else {
			try {
				dao().iniciarTransacao();
				CpModelo mod = new CpModelo();
				mod.setConteudoBlobString(conteudo);
				if (paramLong("idOrgUsu") != null)
					mod.setCpOrgaoUsuario(dao().consultar(paramLong("idOrgUsu"),
							CpOrgaoUsuario.class, false));
				mod.setHisDtIni(dao().consultarDataEHoraDoServidor());
				dao().gravarComHistorico(mod, getIdentidadeCadastrante());
				dao().commitTransacao();
			} catch (Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException(
						"Não foi possível gravar o modelo.", 9, e);
			}
		}
		return Action.SUCCESS;
	}

	public Long getId() {
		return id;
	}

	public String getConteudo() {
		return conteudo;
	}

	public List<CpModelo> getItens() {
		return itens;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public void setItens(List<CpModelo> itens) {
		this.itens = itens;
	}
}
