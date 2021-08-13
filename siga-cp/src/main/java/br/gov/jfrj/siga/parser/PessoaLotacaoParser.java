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
package br.gov.jfrj.siga.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;

public class PessoaLotacaoParser extends SiglaParser {

	public static final String LETTER = "[a-zA-Z]";

	public static final String NUMBER = "[0-9]";

	public static final String CARACTER = "[" + LETTER + "|" + NUMBER + "]";

	public static final String PESSOA = "(" + CARACTER + "{2}+" + ")" + "("
			+ NUMBER + "+" + ")";

	public static final String LOTACAO = "(" + CARACTER + "{2}+" + ")" + "("
			+ "[" + CARACTER + "|" + "/" + "|" + "-" + "]+" + ")";

	public static final String PESSOA_E_OU_LOTACAO = "(" + PESSOA + ")?"
			+ "(?:" + "@" + "(" + LOTACAO + ")" + ")?";

	public static final Pattern pattern = Pattern.compile(PESSOA_E_OU_LOTACAO
			+ "(?:#" + PESSOA_E_OU_LOTACAO + ")?");

	public PessoaLotacaoParser(DpPessoa pessoa, DpLotacao lotacao) {
		super(pessoa, lotacao);
	}

	public PessoaLotacaoParser(String codigo) throws AplicacaoException {
		super(null, null);
		if (codigo == null)
			return;
		Matcher matcher = pattern.matcher(codigo);
		if (!matcher.matches())
			throw new RuntimeException(
					"Erro de sintaxe na definição da lotação para qual o documento deve ser transferido. A sintaxe correta é, por exemplo: @RJSESIE.");
		else {
			String siglaPessoa = matcher.group(3);
			String orgaoPessoa = matcher.group(2);
			String orgaoLotacao = matcher.group(5);
			String siglaLotacao = matcher.group(6);
			if (siglaPessoa != null) {
				try {
					CpOrgaoUsuario orgao = null;
					{
						CpOrgaoUsuario example = new CpOrgaoUsuario();
						example.setSiglaOrgaoUsu(orgaoPessoa);
						orgao = (CpOrgaoUsuario) CpDao.getInstance()
								.consultarPorSigla(example);
					}
					DpPessoaDaoFiltro filtro = new DpPessoaDaoFiltro();
					filtro.setSigla(orgaoPessoa + siglaPessoa);
					filtro.setIdOrgaoUsu(orgao.getIdOrgaoUsu());
					DpPessoa pessoa = (DpPessoa) CpDao.getInstance()
							.consultarPorSigla(filtro);
					this.setPessoa(pessoa);
				} catch (Exception ex) {
				}
			}
			if (siglaLotacao != null) {
				try {
					CpOrgaoUsuario orgao = null;
					{
						CpOrgaoUsuario example = new CpOrgaoUsuario();
						example.setSiglaOrgaoUsu(orgaoLotacao);
						orgao = (CpOrgaoUsuario) CpDao.getInstance()
								.consultarPorSigla(example);
					}
					DpLotacaoDaoFiltro filtro = new DpLotacaoDaoFiltro();
					filtro.setSigla(siglaLotacao);
					filtro.setIdOrgaoUsu(orgao.getIdOrgaoUsu());
					DpLotacao lotacao = (DpLotacao) CpDao.getInstance()
							.consultarPorSigla(filtro);
					this.setLotacao(lotacao);
				} catch (Exception ex) {
				}
			}
		}
	}

	public DpLotacao getLotacaoOuLotacaoPrincipalDaPessoa() {
		if (getLotacao() != null)
			return getLotacao();
		if (getPessoa() != null)
			return getPessoa().getLotacao();
		return null;
	}

	public String getSigla() {
		if (getLotacao() != null)
			return getLotacao().getSigla();
		if (getPessoa() != null)
			return getPessoa().getSigla();
		return null;
	}

	public String getSiglaCompleta() {
		if (getLotacao() != null)
			return getLotacao().getSiglaCompleta();
		if (getPessoa() != null)
			return getPessoa().getSiglaCompleta();
		return null;
	}

	public String getNome() {
		if (getLotacao() != null)
			return getLotacao().getNomeLotacao();
		if (getPessoa() != null)
			return getPessoa().getNomePessoa();
		return null;
	}

}
