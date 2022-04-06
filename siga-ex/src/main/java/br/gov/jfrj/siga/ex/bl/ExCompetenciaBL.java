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
package br.gov.jfrj.siga.ex.bl;

import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.bl.CpCompetenciaBL;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExConfiguracaoCache;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.ExVia;
import br.gov.jfrj.siga.ex.logic.ExPodePorConfiguracao;

public class ExCompetenciaBL extends CpCompetenciaBL {

	public ExConfiguracaoBL getConf() {
		return (ExConfiguracaoBL) super.getConfiguracaoBL();
	}

	public Expression exp(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular) {
		try {
			return clazz.getDeclaredConstructor(DpPessoa.class, DpLotacao.class).newInstance(titular, lotaTitular);
		} catch (Exception e) {
			throw new RuntimeException("Erro executando lógica de negócios", e);
		}
	}

	public boolean pode(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular) {
		return exp(clazz, titular, lotaTitular).eval();
	}

	public void afirmar(String msg, Class<? extends Expression> clazz, final DpPessoa titular,
			final DpLotacao lotaTitular) {
		Expression exp = exp(clazz, titular, lotaTitular);
		afirmar(msg, exp, titular, lotaTitular);
	}

	private void afirmar(String msg, Expression exp, final DpPessoa titular, final DpLotacao lotaTitular) {
		boolean res = exp.eval();
		if (!res) {
			String explicacao = "";
			if (new ExPodePorConfiguracao(titular, lotaTitular).withIdTpConf(CpTipoDeConfiguracao.EXIBIR_REGRA_DE_NEGOCIO_EM_MENSAGENS).eval())
				explicacao = " - " + AcaoVO.Helper.formatarExplicacao(exp, res);
			throw new AplicacaoException(msg + explicacao);
		}
	}

	public Expression exp(Class<? extends Expression> clazz, final ExDocumento doc) {
		try {
			return clazz.getDeclaredConstructor(ExDocumento.class).newInstance(doc);
		} catch (Exception e) {
			throw new RuntimeException("Erro executando lógica de negócios", e);
		}
	}

	public boolean pode(Class<? extends Expression> clazz, final ExDocumento doc) {
		return exp(clazz, doc).eval();
	}

	public Expression exp(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular,
			final ExDocumento doc) {
		try {
			return clazz.getDeclaredConstructor(ExDocumento.class, DpPessoa.class, DpLotacao.class).newInstance(doc,
					titular, lotaTitular);
		} catch (Exception e) {
			throw new RuntimeException("Erro executando lógica de negócios", e);
		}
	}

	public Expression exp(Class<? extends Expression> clazz, final DpPessoa titular, final ExDocumento doc) {
		try {
			return clazz.getDeclaredConstructor(ExDocumento.class, DpPessoa.class).newInstance(doc,
					titular);
		} catch (Exception e) {
			throw new RuntimeException("Erro executando lógica de negócios", e);
		}
	}
	
	public boolean pode(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular,
			final ExDocumento doc) {
		return exp(clazz, titular, lotaTitular, doc).eval();
	}

	public void afirmar(String msg, Class<? extends Expression> clazz, final DpPessoa titular,
			final DpLotacao lotaTitular, final ExDocumento doc) {
		Expression exp = exp(clazz, titular, lotaTitular, doc);
		afirmar(msg, exp, titular, lotaTitular);
	}

	public Expression exp(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular,
			final ExMobil mob) {
		try {
			return clazz.getDeclaredConstructor(ExMobil.class, DpPessoa.class, DpLotacao.class).newInstance(mob,
					titular, lotaTitular);
		} catch (Exception e) {
			throw new RuntimeException("Erro executando lógica de negócios", e);
		}
	}

	public boolean pode(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular,
			final ExMobil mob) {
		return exp(clazz, titular, lotaTitular, mob).eval();
	}

	public void afirmar(String msg, Class<? extends Expression> clazz, final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {
		Expression exp = exp(clazz, titular, lotaTitular, mob);
		afirmar(msg, exp, titular, lotaTitular);
	}

	public boolean pode(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular,
			final ExModelo mod) {
		try {
			return clazz.getDeclaredConstructor(ExModelo.class, DpPessoa.class, DpLotacao.class)
					.newInstance(mod, titular, lotaTitular).eval();
		} catch (Exception e) {
			throw new RuntimeException("Erro executando lógica de negócios", e);
		}
	}

	public Expression exp(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular,
			final ExMobil mob, final ExMovimentacao mov) {
		try {
			return clazz.getDeclaredConstructor(ExMobil.class, ExMovimentacao.class, DpPessoa.class, DpLotacao.class)
					.newInstance(mob, mov, titular, lotaTitular);
		} catch (Exception e) {
			throw new RuntimeException("Erro executando lógica de negócios", e);
		}
	}

	public boolean pode(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular,
			final ExMobil mob, final ExMovimentacao mov) {
		return exp(clazz, titular, lotaTitular, mob, mov).eval();
	}

	public void afirmar(String msg, Class<? extends Expression> clazz, final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob, final ExMovimentacao mov) {
		Expression exp = exp(clazz, titular, lotaTitular, mob, mov);
		afirmar(msg, exp, titular, lotaTitular);
	}

	public Expression exp(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular,
			final ExDocumento doc, final ExMobil mob) {
		try {
			return clazz.getDeclaredConstructor(ExDocumento.class, ExMobil.class, DpPessoa.class, DpLotacao.class).newInstance(doc, mob,
					titular, lotaTitular);
		} catch (Exception e) {
			throw new RuntimeException("Erro executando lógica de negócios", e);
		}
	}

	public boolean pode(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular, 
			final ExDocumento doc, final ExMobil mob) {
		return exp(clazz, titular, lotaTitular, doc, mob).eval();
	}

	public void afirmar(String msg, Class<? extends Expression> clazz, final DpPessoa titular,
			final DpLotacao lotaTitular, final ExDocumento doc, final ExMobil mob) {
		Expression exp = exp(clazz, titular, lotaTitular, doc, mob);
		afirmar(msg, exp, titular, lotaTitular);
	}

	public Expression exp(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular,
			final ExMovimentacao mov) {
		try {
			return clazz.getDeclaredConstructor(ExMovimentacao.class, DpPessoa.class, DpLotacao.class).newInstance(mov,
					titular, lotaTitular);
		} catch (Exception e) {
			throw new RuntimeException("Erro executando lógica de negócios", e);
		}
	}

	public boolean pode(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular,
			final ExMovimentacao mov) {
		return exp(clazz, titular, lotaTitular, mov).eval();
	}

	public void afirmar(String msg, Class<? extends Expression> clazz, final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMovimentacao mov) {
		Expression exp = exp(clazz, titular, lotaTitular, mov);
		afirmar(msg, exp, titular, lotaTitular);
	}

	/**
	 * Retorna um configuração existente para a combinação dos dados passados como
	 * parâmetros, caso exista.
	 * 
	 * @param titularIniciador
	 * @param lotaTitularIniciador
	 * @param tipoConfig
	 * @param procedimento
	 * @param raia
	 * @param tarefa
	 * @return
	 * @throws Exception
	 */
	private ExConfiguracaoCache preencherFiltroEBuscarConfiguracao(DpPessoa titularIniciador,
			DpLotacao lotaTitularIniciador, ITipoDeConfiguracao tipoConfig, ITipoDeMovimentacao tipoMov,
			ExTipoDocumento exTipoDocumento, ExTipoFormaDoc exTipoFormaDoc, ExFormaDocumento exFormaDocumento,
			ExModelo exModelo, ExClassificacao exClassificacao, ExVia exVia, ExNivelAcesso exNivelAcesso,
			ExPapel exPapel, DpPessoa pessoaObjeto, DpLotacao lotacaoObjeto, CpComplexo complexoObjeto,
			DpCargo cargoObjeto, DpFuncaoConfianca funcaoConfiancaObjeto, CpOrgaoUsuario orgaoObjeto) {
		ExConfiguracao cfgFiltro = new ExConfiguracao();

		if (titularIniciador != null) {
			cfgFiltro.setCargo(titularIniciador.getCargo());
			cfgFiltro.setFuncaoConfianca(titularIniciador.getFuncaoConfianca());
		}
		cfgFiltro.setDpPessoa(titularIniciador);
		if (lotaTitularIniciador != null) {
			cfgFiltro.setOrgaoUsuario(lotaTitularIniciador.getOrgaoUsuario());
		}
		cfgFiltro.setLotacao(lotaTitularIniciador);
		cfgFiltro.setCpTipoConfiguracao(tipoConfig);
		if (cfgFiltro.getCpTipoConfiguracao() == null)
			throw new RuntimeException("Não é permitido buscar uma configuração sem definir seu tipo.");
		cfgFiltro.setExTipoMovimentacao(tipoMov);
		cfgFiltro.setExTipoDocumento(exTipoDocumento);
		cfgFiltro.setExTipoFormaDoc(exTipoFormaDoc);
		cfgFiltro.setExFormaDocumento(exFormaDocumento);
		cfgFiltro.setExModelo(exModelo);
		cfgFiltro.setExClassificacao(exClassificacao);
		cfgFiltro.setExVia(exVia);
		cfgFiltro.setExNivelAcesso(exNivelAcesso);
		cfgFiltro.setExPapel(exPapel);
		cfgFiltro.setPessoaObjeto(pessoaObjeto);
		cfgFiltro.setLotacaoObjeto(lotacaoObjeto);
		cfgFiltro.setComplexoObjeto(complexoObjeto);
		cfgFiltro.setCargoObjeto(cargoObjeto);
		cfgFiltro.setOrgaoObjeto(orgaoObjeto);

		ExConfiguracaoCache cfg = (ExConfiguracaoCache) getConfiguracaoBL().buscaConfiguracao(cfgFiltro,
				new int[] { 0 }, null);

		// Essa linha é necessária porque quando recuperamos um objeto da classe
		// WfConfiguracao do TreeMap estático que os armazena, este objeto está
		// detached, ou seja, não está conectado com a seção atual do hibernate.
		// Portanto, quando vamos acessar alguma propriedade dele que seja do
		// tipo LazyRead, obtemos um erro. O método lock, attacha ele novamente
		// na seção atual.

		// Dasabilitado porque estava dando erro de "Illegal attempt to associate a
		// collection with two open sessions"
		// if (cfg != null)
		// ExDao.getInstance().getSessao().lock(cfg, LockMode.NONE);

		return cfg;
	}

	public boolean ehPublicoExterno(DpPessoa titular) {
		// TODO Auto-generated method stub
		return AcessoConsulta.ehPublicoExterno(titular);
	}

	private static final Class<?> findClassByName(String classname, String[] searchPackages) {
		for (int i = 0; i < searchPackages.length; i++) {
			try {
				return Class.forName(searchPackages[i] + "." + classname);
			} catch (ClassNotFoundException e) {
				// not in this package, try another
			}
		}
		throw new RuntimeException("Classe " + classname + " não encontrada em nenhum pacote");
	}

	/**
	 * Método genérico que recebe função por String e concatena com o método de
	 * checagem de permissão correspondente. Por exemplo, para a função juntar, é
	 * invocado <i>podeJuntar()</i>
	 * 
	 * @param funcao
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 */
	public static boolean testaCompetencia(final String funcao, final DpPessoa titular, final DpLotacao lotaTitular,
			final ExMobil mob) {
		final Class[] classes = new Class[] { DpPessoa.class, DpLotacao.class, ExMobil.class };
		Boolean resposta = false;
		Class<? extends Expression> classe = (Class<? extends Expression>) findClassByName(
				"ExPode" + funcao.substring(0, 1).toUpperCase() + funcao.substring(1),
				new String[] { "br.gov.jfrj.siga.ex.logic" });
		try {
			resposta = Ex.getInstance().getComp().pode(classe, titular, lotaTitular, mob);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return resposta.booleanValue();
	}
}

