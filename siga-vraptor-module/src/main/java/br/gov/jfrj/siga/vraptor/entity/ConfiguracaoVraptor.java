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
package br.gov.jfrj.siga.vraptor.entity;

import java.util.Date;

import javax.persistence.EntityManager;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.Historico;

public class ConfiguracaoVraptor extends CpConfiguracao {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public CpConfiguracao getConfiguracaoAtual() {
		return super.getConfiguracaoAtual();
	}

	public void finalizar() throws Exception {
		try {
			Historico historico = ((Historico) this);
			historico.setHisDtFim(new Date());
			this.save();
		} catch (ClassCastException cce) {
			this.save();
		}
	}

	public void salvar() throws Exception {
		EntityManager em = ContextoPersistencia.em();

		try {
			Historico thisHistorico = (Historico) this;
			thisHistorico.setHisDtIni(new Date());
			thisHistorico.setHisDtFim(null);
			if (thisHistorico.getId() == null) {
				this.save();
				thisHistorico.setHisIdIni(thisHistorico.getId());
			} else {
				em.detach(this);
				// Edson: Na linha abaixo, não funciona findById. Dá
				// UnsupportedOpException.
				// Isso porque ObjetoBase não está anotado com @Entity. No
				// máximo, é @MappedSuperclass
				// Veja
				// https://groups.google.com/forum/?fromgroups=#!topic/play-framework/waYNFtLCH40
				ConfiguracaoVraptor thisAntigo = em.find(this.getClass(), thisHistorico.getId());

				if (((Historico) thisAntigo).getHisDtFim() == null) {
					thisAntigo.finalizar();
				}
				thisHistorico.setHisIdIni(((Historico) thisAntigo).getHisIdIni());
				thisHistorico.setId(null);
			}
			this.save();
			// em.refresh(this);
		} catch (ClassCastException cce) {
			this.save();
		}
	}
}
