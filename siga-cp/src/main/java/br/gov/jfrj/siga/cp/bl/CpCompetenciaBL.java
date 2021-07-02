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
package br.gov.jfrj.siga.cp.bl;

import java.lang.reflect.Method;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class CpCompetenciaBL {

	CpConfiguracaoBL configuracaoBL;

	public CpConfiguracaoBL getConfiguracaoBL() {
		return configuracaoBL;
	}

	public void setConfiguracaoBL(CpConfiguracaoBL configuracaoBL) {
		this.configuracaoBL = configuracaoBL;
	}

	/**
	 * Retorna a subsecretaria a que uma lotação pertence. Se a lotação já for
	 * uma subsecretaria (o que é verificado pelo tamanho da sigla), ela mesma é
	 * retornada. Caso contrário, a lotação pai é devolvida. <b>Regra
	 * aparentemente falha</b>
	 * 
	 * @param lota
	 * @return
	 */
	public DpLotacao getSubsecretaria(DpLotacao lota) {
		DpLotacao l = lota;
		if (l == null)
			return null;
		if (l.isSubsecretaria())
			return l;
		while (l.getLotacaoPai() != null) {
			l = l.getLotacaoPai();
			if (l.isSubsecretaria())
				return l;
		}
		return lota;
	}

	public boolean testaCompetencia(final String funcao,
			final DpPessoa titular, final DpLotacao lotaTitular) {
		final Class[] classes = new Class[] { DpPessoa.class, DpLotacao.class };
		Boolean resposta = false;
		try {
			final Method method = CpCompetenciaBL.class.getDeclaredMethod(
					"pode" + funcao.substring(0, 1).toUpperCase()
							+ funcao.substring(1), classes);
			resposta = (Boolean) method.invoke(CpCompetenciaBL.class,
					new Object[] { titular, lotaTitular });
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return resposta.booleanValue();
	}

	public boolean isIdentidadeBloqueada(CpIdentidade cpIdentidade)
			throws AplicacaoException {
		try {
			return !configuracaoBL.podePorConfiguracao(cpIdentidade, CpTipoDeConfiguracao.FAZER_LOGIN);
		} catch (final Exception e) {
			throw new AplicacaoException(
					"Não foi possível verificar se a identidade está bloqueada.",
					0, e);
		}
	}

	public boolean isPessoaBloqueada(DpPessoa pes) throws AplicacaoException {
		try {
			return !configuracaoBL.podePorConfiguracao(pes,
					CpTipoDeConfiguracao.FAZER_LOGIN);
		} catch (final Exception e) {
			throw new AplicacaoException(
					"Não foi possível verificar se a pessoa está bloqueada.",
					0, e);
		}
	}
	
//	public boolean podeMovimentarViaWS(final DpPessoa titular,
//			final DpLotacao lotaTitular) throws Exception {
//		return configuracaoBL.podePorConfiguracao(titular, lotaTitular,
//				CpTipoDeConfiguracao.AUTORIZAR_MOVIMENTACAO_POR_WS);
//	}
	
	public boolean podeSegundoFatorPin(final DpPessoa titular,
			final DpLotacao lotaTitular) throws Exception {
		return configuracaoBL.podePorConfiguracao(titular, lotaTitular,	CpTipoDeConfiguracao.SEGUNDO_FATOR_PIN);
	}
	
}
