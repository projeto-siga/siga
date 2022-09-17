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
package br.gov.jfrj.siga.ldap.sinc.resolvedores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mvel2.MVEL;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpPessoa;

public class RegraCaixaPostal {
	private String homeMDB;
	private String templateLink;
	private String expressao;
	private List<DpPessoa> pessoasAfetadas = new ArrayList<DpPessoa>();
	private List<DpPessoa> pessoasAlvo;
	private boolean carregada = false;
//	private final SincProperties conf = SincProperties.getInstancia();

	public RegraCaixaPostal(List<DpPessoa> pessoasAlvo) {
		this.pessoasAlvo = pessoasAlvo;
	}

	public String getHomeMDB() {
		return homeMDB;
	}

	public void setHomeMDB(String homeMDB) {
		this.homeMDB = homeMDB;
	}

	public String getTemplateLink() {
		return templateLink;
	}

	public void setTemplateLink(String templateLink) {
		this.templateLink = templateLink;
	}

	public String getExpressao() {
		return expressao;
	}

	public void setExpressao(String expressao) {
		this.expressao = expressao;
	}

	public boolean isAfetado(DpPessoa p) throws AplicacaoException {
		if (!this.isCarregada()) {
			carregarRegra();
		}
		return pessoasAfetadas.contains(p);
	}

	public boolean isAfetadoPorIdInicial(Long idInicialPessoa) throws AplicacaoException {
		if (!this.isCarregada()) {
			carregarRegra();
		}
		for (DpPessoa p : pessoasAfetadas) {
			if (p.getIdInicial().equals(idInicialPessoa)) {
				return true;
			}
		}
		return false;
	}

	public boolean isAfetadoPorMatricula(Long matricula) throws AplicacaoException {
		if (!this.isCarregada()) {
			carregarRegra();
		}
		for (DpPessoa p : pessoasAfetadas) {
			if (p.getMatricula().equals(matricula)) {
				return true;
			}
		}
		return false;
	}

	public boolean isCarregada() {
		return carregada;
	}

	public void carregarRegra() throws AplicacaoException {
		Map<String, DpPessoa> pessoa = new HashMap<String, DpPessoa>();
		if (pessoasAlvo != null) {
			for (DpPessoa p : this.pessoasAlvo) {
//				long tInicio = System.currentTimeMillis();
				pessoa.put("pessoa", p);
				try {
					if (MVEL.eval(getExpressao(), pessoa) == null) {
						throw new AplicacaoException("Problema na expressão: " + getExpressao());
					}
					if ((Boolean) MVEL.eval(getExpressao(), pessoa)) {
						pessoasAfetadas.add(p);
					}
				} catch (Exception e) {
					throw new AplicacaoException("Problema na expressão: " + getExpressao());
				}
//				long tFim = System.currentTimeMillis();
//				System.out.println(tFim-tInicio);
			}

		}
		carregada = true;
	}

	public List<DpPessoa> getPessoasAlvo() {
		return pessoasAlvo;
	}

	public void setPessoasAlvo(List<DpPessoa> pessoasAlvo) {
		this.pessoasAlvo = pessoasAlvo;
	}

}
