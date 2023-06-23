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
    
    //Nato: incluí isso até que JFES substitua siglas de lotação do tipo "2ª VF-CAC"
    public static final String CARACTERES_INVALIDOS_QUE_DEVEM_SER_REMOVIDOS_EM_BREVE = "[ ª]";

	public static final String CARACTER = "[" + LETTER + "|" + NUMBER + "|" + CARACTERES_INVALIDOS_QUE_DEVEM_SER_REMOVIDOS_EM_BREVE + "]";

	public static final String PESSOA = "(" + CARACTER + "+" + ")" + "("
			+ NUMBER + "+" + ")";

	public static final String LOTACAO = "(" + CARACTER + "+" + ")" + "("
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
			String siglaPessoaCompleta = matcher.group(1);
			String siglaLotacaoCompleta = matcher.group(4);

			if (siglaPessoaCompleta != null) {
				try {

					DpPessoaDaoFiltro filtro = new DpPessoaDaoFiltro();
					filtro.setSigla(siglaPessoaCompleta);
					DpPessoa pessoa = (DpPessoa) CpDao.getInstance().consultarPorSigla(filtro);
					this.setPessoa(pessoa);
				} catch (Exception ex) {
				}
			}
			if (siglaLotacaoCompleta != null) {
				try {
					DpLotacao lotacao = (DpLotacao) CpDao.getInstance().getLotacaoFromSigla(siglaLotacaoCompleta);
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
