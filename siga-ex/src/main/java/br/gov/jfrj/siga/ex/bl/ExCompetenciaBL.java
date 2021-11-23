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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.bl.CpCompetenciaBL;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
import br.gov.jfrj.siga.dp.CpMarca;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
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
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.ExVia;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;

public class ExCompetenciaBL extends CpCompetenciaBL {

	public ExConfiguracaoBL getConf() {
		return (ExConfiguracaoBL) super.getConfiguracaoBL();
	}

	public boolean pode(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular) {
		try {
			return clazz.getDeclaredConstructor(DpPessoa.class, DpLotacao.class).newInstance(titular, lotaTitular).eval();
		} catch (Exception e) {
			throw new RuntimeException("Erro executando lógica de negócios", e);
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
	
	public void afirmar(String msg, Class<? extends Expression> clazz, final ExDocumento doc) {
		Expression exp = exp(clazz, doc);
		boolean res = !exp.eval();
		if (!res)
			throw new AplicacaoException(msg + " - " + AcaoVO.Helper.formatarExplicacao(exp, res));
	}



	public Expression exp(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular, final ExDocumento doc) {
		try {
			return clazz.getDeclaredConstructor(ExDocumento.class, DpPessoa.class, DpLotacao.class).newInstance(doc, titular, lotaTitular);
		} catch (Exception e) {
			throw new RuntimeException("Erro executando lógica de negócios", e);
		}
	}

	public boolean pode(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular, final ExDocumento doc) {
		return exp(clazz, titular, lotaTitular, doc).eval();
	}
	
	public void afirmar(String msg, Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular, final ExDocumento doc) {
		Expression exp = exp(clazz, titular, lotaTitular, doc);
		boolean res = !exp.eval();
		if (!res)
			throw new AplicacaoException(msg + " - " + AcaoVO.Helper.formatarExplicacao(exp, res));
	}

	public Expression exp(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob) {
		try {
			return clazz.getDeclaredConstructor(ExMobil.class, DpPessoa.class, DpLotacao.class).newInstance(mob, titular, lotaTitular);
		} catch (Exception e) {
			throw new RuntimeException("Erro executando lógica de negócios", e);
		}
	}

	public boolean pode(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob) {
		return exp(clazz, titular, lotaTitular, mob).eval();
	}

	public void afirmar(String msg, Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob) {
		Expression exp = exp(clazz, titular, lotaTitular, mob);
		boolean res = !exp.eval();
		if (!res)
			throw new AplicacaoException(msg + " - " + AcaoVO.Helper.formatarExplicacao(exp, res));
	}

	public boolean pode(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular, final ExModelo mod) {
		try {
			return clazz.getDeclaredConstructor(ExModelo.class, DpPessoa.class, DpLotacao.class).newInstance(mod, titular, lotaTitular).eval();
		} catch (Exception e) {
			throw new RuntimeException("Erro executando lógica de negócios", e);
		}
	}
	
	public Expression exp(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob, final ExMovimentacao mov) {
		try {
			return clazz.getDeclaredConstructor(ExMobil.class, ExMovimentacao.class, DpPessoa.class, DpLotacao.class).newInstance(mob, mov, titular, lotaTitular);
		} catch (Exception e) {
			throw new RuntimeException("Erro executando lógica de negócios", e);
		}
	}

	public boolean pode(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob, final ExMovimentacao mov) {
		return exp(clazz, titular, lotaTitular, mob, mov).eval();
	}

	public void afirmar(String msg, Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob, final ExMovimentacao mov) {
		Expression exp = exp(clazz, titular, lotaTitular, mob, mov);
		boolean res = !exp.eval();
		if (!res)
			throw new AplicacaoException(msg + " - " + AcaoVO.Helper.formatarExplicacao(exp, res));
	}


	
	public Expression exp(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular, final ExMovimentacao mov) {
		try {
			return clazz.getDeclaredConstructor(ExMovimentacao.class, DpPessoa.class, DpLotacao.class).newInstance(mov, titular, lotaTitular);
		} catch (Exception e) {
			throw new RuntimeException("Erro executando lógica de negócios", e);
		}
	}

	public boolean pode(Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular, final ExMovimentacao mov) {
		return exp(clazz, titular, lotaTitular, mov).eval();
	}

	public void afirmar(String msg, Class<? extends Expression> clazz, final DpPessoa titular, final DpLotacao lotaTitular, final ExMovimentacao mov) {
		Expression exp = exp(clazz, titular, lotaTitular, mov);
		boolean res = !exp.eval();
		if (!res)
			throw new AplicacaoException(msg + " - " + AcaoVO.Helper.formatarExplicacao(exp, res));
	}


	/**
	 * Retorna um configuração existente para a combinação dos dados passados
	 * como parâmetros, caso exista.
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
	private ExConfiguracaoCache preencherFiltroEBuscarConfiguracao(
			DpPessoa titularIniciador, DpLotacao lotaTitularIniciador,
			ITipoDeConfiguracao tipoConfig, long tipoMov, ExTipoDocumento exTipoDocumento,
			ExTipoFormaDoc exTipoFormaDoc, ExFormaDocumento exFormaDocumento,
			ExModelo exModelo, ExClassificacao exClassificacao, ExVia exVia,
			ExNivelAcesso exNivelAcesso, ExPapel exPapel, DpPessoa pessoaObjeto, 
			DpLotacao lotacaoObjeto, CpComplexo complexoObjeto, DpCargo cargoObjeto, 
			DpFuncaoConfianca funcaoConfiancaObjeto, CpOrgaoUsuario orgaoObjeto) {
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
			throw new RuntimeException(
					"Não é permitido buscar uma configuração sem definir seu tipo.");
		if (tipoMov != 0)
			cfgFiltro.setExTipoMovimentacao(CpDao.getInstance().consultar(
					tipoMov, ExTipoMovimentacao.class, false));
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

		ExConfiguracaoCache cfg = (ExConfiguracaoCache) getConfiguracaoBL()
				.buscaConfiguracao(cfgFiltro, new int[] { 0 }, null);

		// Essa linha é necessária porque quando recuperamos um objeto da classe
		// WfConfiguracao do TreeMap estático que os armazena, este objeto está
		// detached, ou seja, não está conectado com a seção atual do hibernate.
		// Portanto, quando vamos acessar alguma propriedade dele que seja do
		// tipo LazyRead, obtemos um erro. O método lock, attacha ele novamente
		// na seção atual.
		
		// Dasabilitado porque estava dando erro de "Illegal attempt to associate a collection with two open sessions"
		//if (cfg != null)
		//	ExDao.getInstance().getSessao().lock(cfg, LockMode.NONE);

		return cfg;
	}

	public boolean podeAnexarArquivoAuxiliar(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {
		
		return getConf().podePorConfiguracao(titular, lotaTitular,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_DE_ARQUIVO_AUXILIAR,
				ExTipoDeConfiguracao.MOVIMENTAR);
	}

	/**
	 * Retorna se é possível cancelar uma movimentação de vinculação de perfil,
	 * passada através do parâmetro mov. As regras são as seguintes: 
	 * <ul>
	 * <li>Vinculação de perfil não pode estar cancelada</li>
	 * <li>Lotação do usuário tem de ser a lotação cadastrante da movimentação</li>
	 * <li>Não pode haver configuração impeditiva. Tipo de configuração:
	 * Cancelar Movimentação</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @param mov
	 * @return
	 * @throws Exception
	 */
	public boolean podeCancelarVinculacaoPapel(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob,
			final ExMovimentacao mov) {

		if (mov.isCancelada())
			return false;
		
		if ((mov.getSubscritor() != null && mov.getSubscritor().equivale(titular))||( mov.getSubscritor()==null && mov.getLotaSubscritor()!=null && mov.getLotaSubscritor().equivale(lotaTitular)))
			return true;

		if ((mov.getCadastrante() != null && mov.getCadastrante().equivale(titular)) || (mov.getCadastrante()==null && mov.getLotaCadastrante() != null && mov.getLotaCadastrante().equivale(lotaTitular)))
			return true;

		return getConf().podePorConfiguracao(titular, lotaTitular,
				mov.getIdTpMov(),
				ExTipoDeConfiguracao.CANCELAR_MOVIMENTACAO);
	}

	/**
	 * Retorna se é possível cancelar uma movimentação de vinculação de perfil,
	 * passada através do parâmetro mov. As regras são as seguintes:
	 * <ul>
	 * <li>Vinculação de perfil não pode estar cancelada</li>
	 * <li>Lotação do usuário tem de ser a lotação cadastrante da movimentação</li>
	 * <li>Não pode haver configuração impeditiva. Tipo de configuração: Cancelar
	 * Movimentação</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @param mov
	 * @return
	 * @throws Exception
	 */
//	public Optional<String> podeCancelarVinculacaoMarca(final DpPessoa titular, final DpLotacao lotaTitular,
//			final ExMobil mob, final ExMovimentacao mov) {
//		if (mov.isCancelada()) {
//			return Optional.of("Marcação já cancelada.");
//		}
//
//		if ((nonNull(mov.getSubscritor()) && mov.getSubscritor().equivale(titular))
//				|| (isNull(mov.getSubscritor()) && nonNull(mov.getLotaSubscritor()) && mov.getLotaSubscritor().equivale(lotaTitular))) {
//			return Optional.empty();
//		}
//
//		if ((nonNull(mov.getCadastrante()) && mov.getCadastrante().equivale(titular))
//				|| (isNull(mov.getCadastrante()) && mov.getLotaCadastrante().equivale(lotaTitular))) {
//			return Optional.empty();
//		}
//
//		if (getConf().podePorConfiguracao(titular, lotaTitular, mov.getIdTpMov(),
//				ExTipoDeConfiguracao.CANCELAR_MOVIMENTACAO)) {
//			return Optional.empty();
//		}
//
//		return Optional.of("Usuário deve ser ou o cadastrante ou o subscritor da movimentação "
//				+ "ou deve estar na mesma unidade desses " //
//				+ "ou deve ter autorização para cancelar marcações.");
//	}

	/**
	 * <b>(Quando é usado este método?)</b> Retorna se é possível cancelar
	 * movimentação do tipo despacho, representada pelo parâmetro mov. São estas
	 * as regras:
	 * <ul>
	 * <li>Despacho não pode estar cancelado</li>
	 * <li>Lotação do usuário tem de ser a lotação cadastrante do despacho</li>
	 * <li>Despacho não pode estar assinado</li>
	 * <li>Não pode haver configuração impeditiva. Tipo de configuração:
	 * Cancelar Movimentação</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @param mov
	 * @return
	 * @throws Exception
	 */
	public boolean podeCancelarDespacho(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob,
			final ExMovimentacao mov) {

		if (mov.isCancelada())
			return false;

		if (!(mov.getLotaCadastrante().equivale(lotaTitular)))
			return false;
		
		if(mov.isUltimaMovimentacao())
			return false;

		for (ExMovimentacao movAssinatura : mov.getExMobil()
				.getExMovimentacaoSet()) {
			if (!movAssinatura.isCancelada()
					&& (movAssinatura.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO 
					     || movAssinatura.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA)
					&& movAssinatura.getExMovimentacaoRef().getIdMov() == mov
							.getIdMov())
				return false;
		}

		return getConf().podePorConfiguracao(titular, lotaTitular,
				mov.getIdTpMov(),
				ExTipoDeConfiguracao.CANCELAR_MOVIMENTACAO);
	}

	/**
	 * Retorna se é possível cancelar ou alterar uma movimentação de definição de prazo de assinatura,
	 * passada através do parâmetro mov. As regras são as seguintes:
	 * <ul>
	 * <li>A movimentação não pode estar cancelada</li>
	 * <li>Se o documento estiver assinado, só o subscritor pode cancelar. Caso contrário, só o cadastrante.</li>
	 * <li>Não pode haver configuração impeditiva. Tipo de configuração:
	 * Cancelar Movimentação</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param subscritor
	 * @param mob
	 * @param mov
	 * @return
	 * @throws Exception
	 */
	public boolean podeCancelarOuAlterarPrazoDeAssinatura(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob,
			final ExMovimentacao mov) {

		if (mov.isCancelada())
			return false;
		
		if (mob.getDoc().isAssinadoPeloSubscritorComTokenOuSenha()) {
			if (mob.getDoc().getSubscritor() != null && !mob.getDoc().getSubscritor().equivale(titular))
				return false;
		} else {
			if (mov.getCadastrante() != null && !mov.getCadastrante().equivale(titular))
				return false;
		}

		return getConf().podePorConfiguracao(titular, lotaTitular,
				mov.getIdTpMov(),
				ExTipoDeConfiguracao.CANCELAR_MOVIMENTACAO);
	}

	/**
	 * Retorna se é possível excluir anotação realizada no móbil, passada pelo
	 * parâmetro mov. As seguintes regras incidem sobre a movimentação a ser
	 * excluída:
	 * <ul>
	 * <li>Não pode estar cancelada</li>
	 * <li>Lotação do usuário tem de ser a do cadastrante ou do subscritor
	 * (responsável) da movimentação</li>
	 * <li>Não pode haver configuração impeditiva. Tipo de configuração: Excluir
	 * Anotação</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @param mov
	 * @return
	 * @throws Exception
	 */
	public boolean podeExcluirAnotacao(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob,
			final ExMovimentacao mov) {

		if (mov.isCancelada())
			return false;
		
		//Verifica se foi a pessoa ou lotação que fez a anotação
		if (!mov.getCadastrante().getIdInicial().equals(titular.getIdInicial())
				&& !mov.getSubscritor().getIdInicial().equals(titular.getIdInicial())
				&& !mov.getLotaCadastrante().getIdInicial().equals(
				lotaTitular.getIdInicial())
				&& !mov.getLotaSubscritor().getIdInicial().equals(
						lotaTitular.getIdInicial()))
			return false;

		return getConf().podePorConfiguracao(titular, lotaTitular,
				ExTipoDeConfiguracao.EXCLUIR_ANOTACAO);
	}

	/**
	 * Retorna se é possível exibir todos os móbil's. Basta o documento estar
	 * finalizado.
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 */
	public boolean podeExibirTodasVias(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {
		return (mob != null && mob.doc().isFinalizado());
	}

	/**
	 * Retorna se é possível fazer anotação no móbil. Basta o móbil não estar
	 * eliminado, não estar em trânsito, não ser geral e não haver configuração
	 * impeditiva, o que significa que, tendo acesso a um documento não
	 * eliminado fora de trânsito, qualquer usuário pode fazer anotação.

	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeFazerAnotacao(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {

		return (!mob.isEmTransitoInterno() && !mob.isEliminado() && !mob
				.isGeral())
				&& getConf().podePorConfiguracao(titular, lotaTitular,
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANOTACAO,
						ExTipoDeConfiguracao.MOVIMENTAR);
	}

	/**
	 * Retorna se é possível vincular perfil ao documento. Basta não estar
	 * eliminado o documento e não haver configuração impeditiva, o que
	 * significa que, tendo acesso a um documento não eliminado, qualquer
	 * usuário pode se cadastrar como interessado.
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeFazerVinculacaoPapel(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {


		if (mob.doc().isCancelado() || mob.doc().isSemEfeito()



				|| mob.isEliminado())
			return false;

		return getConf().podePorConfiguracao(titular, lotaTitular,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL,
				ExTipoDeConfiguracao.MOVIMENTAR);
	}
	
	public boolean podeRestringirDefAcompanhamento(final DpPessoa pessoa, final DpLotacao lotacao, final DpPessoa pessoaObjeto,
			final DpLotacao lotacaoObjeto, final DpCargo cargoObjeto, final DpFuncaoConfianca funcaoConfiancaObjeto, final CpOrgaoUsuario orgaoObjeto) {

		return this.podePorConfiguracao(pessoa,
				lotacao, 0, ExTipoDeConfiguracao.RESTRINGIR_DEF_ACOMPANHAMENTO, pessoaObjeto, 
				lotacaoObjeto, null, cargoObjeto, 
				funcaoConfiancaObjeto, orgaoObjeto);
				
	}


	/**
	 * Retorna se é possível vincular uma marca ao documento. Basta não estar
	 * eliminado o documento e não haver configuração impeditiva, o que
	 * significa que, tendo acesso a um documento não eliminado, qualquer
	 * usuário pode colocar marcas.
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
//	public boolean podeMarcar(final DpPessoa titular,
//			final DpLotacao lotaTitular, final ExMobil mob) {
//		if (mob.doc().isCancelado() || mob.doc().isSemEfeito()
//				|| mob.isEliminado())
//			return false;
//
//		return getConf().podePorConfiguracao(titular, lotaTitular,
//				ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO,
//				ExTipoDeConfiguracao.MOVIMENTAR);
//	}

	/**
	 * Retorna se é possível finalizar o documento ao qual o móbil passado por
	 * parâmetro pertence. São estas as regras:
	 * <ul>
	 * <li>Documento não pode estar finalizado</li>
	 * <li>Se o documento for interno produzido, usuário tem de ser: 1) da
	 * lotação cadastrante do documento, 2) o subscritor do documento ou 3) o
	 * titular do documento. <b>Obs.: por que a origem do documento está sendo
	 * considerada nesse caso?</b></li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeFinalizar(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {

		if (mob.doc().isFinalizado())
			return false;
		if (lotaTitular.isFechada())
			return false;
		if (mob.isPendenteDeAnexacao())
			return false;
		if (mob.isPendenteDeColaboracao())
			return false;
		if (mob.doc().getExTipoDocumento().getIdTpDoc() != 2
				&& mob.doc().getExTipoDocumento().getIdTpDoc() != 3)
			if (!mob.doc().getLotaCadastrante().equivale(lotaTitular)
					&& (mob.doc().getSubscritor() != null && !mob.doc()
							.getSubscritor().equivale(titular))
					&& (mob.doc().getTitular() != null && !mob.doc()
							.getTitular().equivale(titular))
					&& !mob.getExDocumento().temPerfil(titular, lotaTitular, ExPapel.PAPEL_GESTOR)
					&& !mob.getExDocumento().temPerfil(titular, lotaTitular, ExPapel.PAPEL_REVISOR))
				return false;
		return getConf().podePorConfiguracao(titular, lotaTitular,
				ExTipoDeConfiguracao.FINALIZAR);
	}

	/**
	 * Retorna se é possível que o usuário finalize o documento e assine
	 * digitalmente numa única operação. Os requisitos são os mesmos que têm de
	 * ser cumpridos para se poder finalizar
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeFinalizarAssinar(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) throws Exception {
		return podeFinalizar(titular, lotaTitular, mob)
				&& mob.doc().isEletronico();

	}

	/**
	 * Retorna se é possível incluir cossignatário no documento que contém o
	 * móbil passado por parâmetro. O documento tem de atender as seguintes
	 * condições:
	 * <ul>
	 * <li>Não pode estar cancelado</li>
	 * <li>Sendo documento físico, não pode estar finalizado</li>
	 * <li>Sendo documento digital, não pode estar assinado</li>
	 * <li>Lotação do usuário tem de ser a lotação cadastrante do documento</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeIncluirCosignatario(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {

		if (mob.doc().getSubscritor() == null)
			return false;
		if (mob.doc().isCancelado())
			return false;
		if (mob.doc().isEletronico()){
			if (!mob.doc().getAssinaturasEAutenticacoesComTokenOuSenhaERegistros().isEmpty())
				return false;
		} else {
			if (mob.doc().isFinalizado())
				return false;
		}
		if (!mob.doc().isPendenteDeAssinatura())
			return false;
		if (!mob.doc().getLotaCadastrante().equivale(lotaTitular))
			return false;

		return getConf().podePorConfiguracao(titular, lotaTitular,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO,
				ExTipoDeConfiguracao.MOVIMENTAR);
	}

	/**
	 * Retorna se é possível incluir o móbil em edital de eliminação, de acordo
	 * com as condições a seguir:
	 * <ul>
	 * <li>Móbil tem de ser via ou geral de processo</li>
	 * <li>Móbil tem de estar em arquivo corrente ou intermediário</li>
	 * <li>PCTT tem de prever, para o móbil, destinação final Eliminação</li>
	 * <li>Móbil não pode estar arquivado permanentemente</li>
	 * <li>Documento a que o móbil pertence tem de ser digital ou estar na
	 * lotação titular</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeIncluirEmEditalEliminacao(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) throws Exception {

		if (!(mob.isVia() || mob.isGeralDeProcesso())
				|| mob.doc().isSemEfeito() || mob.isEliminado())
			return false;

		ExMobil mobVerif = mob;

		if (mob.isGeralDeProcesso())
			mobVerif = mob.doc().getUltimoVolume();

		return mobVerif != null
				&& (mobVerif.isArquivadoCorrente() || mobVerif
						.isArquivadoIntermediario())
				&& !mobVerif.isArquivadoPermanente()
				&& mobVerif.isDestinacaoEliminacao()
				&& (mob.isAtendente(titular, lotaTitular) || mob
						.doc().isEletronico())
				&& getConf()
						.podePorConfiguracao(
								titular,
								lotaTitular,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO,
								ExTipoDeConfiguracao.MOVIMENTAR);
	}

	/**
	 * Retorna se é possível junta este móbil a outro. Seguem as regras:
	 * <ul>
	 * <li>Móbil não pode estar cancelado</li>
	 * <li>Volume não pode estar encerrado</li>
	 * <li>Móbil não pode estar em trânsito</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o móbil/usuário</li>
	 * <li>Documento tem de estar assinado</li>
	 * <li>Móbil não pode estar juntado <b>(mas pode ser juntado estando
	 * apensado?)</b></li>
	 * <li>Móbil não pode estar em algum arquivo</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeJuntar(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {

		if (!mob.isVia())
			return false;

		if (mob.isPendenteDeAnexacao())
			return false;			
		
		return !mob.isCancelada()
				&& !mob.isVolumeEncerrado()
				&& !mob.isEmTransito(titular, lotaTitular)
				&& podeMovimentar(titular, lotaTitular, mob)

				&& (!mob.doc().isPendenteDeAssinatura() || mob.doc().isInternoCapturado())
				&& !mob.isJuntado()
				&& !mob.isApensado()
				&& !mob.isArquivado()
				&& !mob.isSobrestado()
				&& !mob.doc().isSemEfeito()
				&& !mob.isEmTramiteParalelo()
				&& podePorConfiguracaoParaMovimentacao(titular, lotaTitular, mob, ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA);			
		
		// return true;
	}

	/**
	 * Retorna se é possível apensar este móbil a outro, conforme as regras:
	 * <ul>
	 * <li>Móbil precisa ser via ou volume</li>
	 * <li>Móbil não pode estar cancelado</li>
	 * <li>Móbil não pode estar em trânsito <b>(o que é isEmTransito?)</b></li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o móbil/usuário</li>
	 * <li>Documento tem de estar assinado</li>
	 * <li>Móbil não pode estar juntado</li>
	 * <li>Móbil não pode estar em algum arquivo</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeApensar(DpPessoa titular, DpLotacao lotaTitular,
			ExMobil mob) {

		if (!mob.isVia() && !mob.isVolume())
			return false;
		
		if (mob.isEmTramiteParalelo())
			return false;

		return !mob.isCancelada()
				&& !mob.doc().isSemEfeito()
				&& !mob.isEmTransito(titular, lotaTitular)
				&& podeMovimentar(titular, lotaTitular, mob)
				&& !mob.doc().isPendenteDeAssinatura()
				&& !mob.isApensado()
				&& !mob.isJuntado()
				&& !mob.isArquivado()
				&& !mob.isSobrestado()
				&& getConf().podePorConfiguracao(titular, lotaTitular, titular.getCargo(), titular.getFuncaoConfianca(), mob.doc().getExFormaDocumento(), mob.doc().getExModelo(), 
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO,
						ExTipoDeConfiguracao.MOVIMENTAR);
	}

	/**
	 * Retorna se é possível desapensar este móbil de outro, conforme as
	 * seguintes condições para o móbil:
	 * <ul>
	 * <li>Precisa ser via ou volume</li>
	 * <li>Precisa ter movimentação não cancelada</li>
	 * <li>Precisa estar apensado</li>
	 * <li>Não pode estar em trânsito <b>(o que é isEmTransito?)</b></li>
	 * <li>Não pode estar cancelado</li>
	 * <li>Não pode estar em algum arquivo</li>
	 * <li>Não pode estar juntado <b>(mas pode ser juntado estando
	 * apensado?)</b></li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o móbil/usuário</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeDesapensar(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {

		if (!mob.isVia() && !mob.isVolume())
			return false;

		if(mob.doc().isEletronico() && mob.isApensadoAVolumeDoMesmoProcesso())
			return false;

		ExMobil mobilAVerificarSePodeMovimentar = mob.isApensadoAVolumeDoMesmoProcesso() 
				? mob.doc().getUltimoVolume() : mob;
		
		if (!mob.isApensado() || mob.isEmTransito(titular, lotaTitular) || mob.isCancelada()
				|| mob.isArquivado()
				|| mob.isSobrestado()
				|| !podeMovimentar(titular, lotaTitular, mobilAVerificarSePodeMovimentar)
				|| mob.isJuntado())
			return false;

		return getConf().podePorConfiguracao(titular, lotaTitular,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESAPENSACAO,
				ExTipoDeConfiguracao.MOVIMENTAR);
	}

	/**
	 * Retorna se o usuário tem a permissão básica para movimentar o documento.
	 * Método usado como premissa para várias outras permissões de movimentação.
	 * Regras:
	 * <ul>
	 * <li>Se móbil é geral, <i>podeMovimentar()</i> tem de ser verdadeiro para
	 * algum móbil do mesmo documento</li>
	 * <li>Móbil tem de ser geral, via ou volume</li>
	 * <li>Móbil tem de de ter alguma movimentação não cancelada</li>
	 * <li><b>Móbil não pode estar cancelado nem aberto</b></li>
	 * <li>Usuário tem de ser da lotação atendente definida na última
	 * movimentação não cancelada</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 */
	public boolean podeMovimentar(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {


		if (!podeSerMovimentado(mob))
			return false;

		if (mob.isGeral()) {
			if (mob.doc().isProcesso())
				return podeMovimentar(titular, lotaTitular, mob.doc().getUltimoVolume());
			else {
				for (ExMobil m : mob.doc().getExMobilSet()) {
					if (!m.isGeral() && podeMovimentar(titular, lotaTitular, m))
						return true;
				}
				return false;
			}
		}

		boolean fAtendente = false;
		for (PessoaLotacaoParser atendente : mob.getAtendente()) {
			if ((atendente.getLotacaoOuLotacaoPrincipalDaPessoa() != null
					&& atendente.getLotacaoOuLotacaoPrincipalDaPessoa().equivale(lotaTitular))
					|| (atendente.getPessoa() != null && atendente.getPessoa().equivale(titular))) {
				fAtendente = true;
				break;
			}
		}
		if (!fAtendente)
			return false;

		return getConf().podePorConfiguracao(titular, lotaTitular,
				ExTipoDeConfiguracao.MOVIMENTAR);
	}

	public boolean podeSerMovimentado(final ExMobil mob) {
		if (mob.doc().isSemEfeito())
			return false;

		if (mob.doc().isFinalizado() && mob.isGeral()) {
			if (mob.doc().isProcesso())
				return podeSerMovimentado(mob.doc().getUltimoVolume());
			else {
				for (ExMobil m : mob.doc().getExMobilSet()) {
					if (!m.isGeral() && podeSerMovimentado(m))
						return true;
				}
				return false;
			}
		}
		if (!mob.isVia() && !mob.isVolume())
			return false;

		if (mob.isCancelada() || !mob.doc().isFinalizado())
			return false;
		
		if (mob.isEmTransitoExterno())
			return false;

		return true;
	}
	
	/**
	 * Retorna se o usuário tem é o atendente do documento. Regras:
	 * <ul>
	 * <li>Se móbil é geral, <i>isAtendente()</i> tem de ser verdadeiro para
	 * algum móbil do mesmo documento</li>
	 * <li>Móbil tem de ser geral, via ou volume</li>
	 * <li>Móbil tem de de ter alguma movimentação não cancelada</li>
	 * <li><b>Móbil não pode estar cancelado</b></li>
	 * <li>Usuário tem de ser da lotação atendente definida na última
	 * movimentação não cancelada, ou no documento se ainda não for finalizado.</li>
	 * </ul>
	 */
	public static boolean isAtendente(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) throws Exception {
		if (mob.isGeral()) {
			for (ExMobil m : mob.doc().getExMobilSet()) {
				if (!m.isGeral() && m.isAtendente(titular, lotaTitular))
					return true;
			}
			return false;
		}
		if (!mob.isVia() && !mob.isVolume())
			return false;

		return mob.isAtendente(titular, lotaTitular);
	}

	/**
	 * Retorna se é possível refazer um documento. Têm de ser satisfeitas as
	 * seguintes condições:
	 * <ul>
	 * <li>Documento tem de estar finalizado</li>
	 * <li>Usuário tem de ser o subscritor ou o titular do documento ou ser da
	 * lotação cadastrante do documento</li>
	 * <li>Documento não pode estar assinado, a não ser que seja dos tipos
	 * externo ou interno importado, que são naturalmente considerados
	 * assinados. Porém, se for documento de um desses tipos, não pode haver pdf
	 * anexado <b>(verificar por quê)</b></li>
	 * <li>Documento tem de possuir via não cancelada ou volume não cancelado</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeRefazer(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {
		return (mob.doc().isFinalizado())
				&& !mob.doc().isRecebeuJuntada()
				&& ((mob.doc().getLotaCadastrante().equivale(lotaTitular)
						|| (mob.doc().getSubscritor() != null && mob.doc()
								.getSubscritor().equivale(titular)) || (mob
						.doc().getTitular() != null && mob.doc().getTitular()
						.equivale(titular)))
						&& !mob.doc().isColaborativo()
						&& (mob.doc().isPendenteDeAssinatura() || (mob.doc()
								.getExTipoDocumento().getIdTpDoc() != 1L && !mob
								.doc().hasPDF())) && (mob.doc()
						.getNumUltimaViaNaoCancelada() != 0 || (mob.doc()
						.getUltimoVolume() != null && !mob.doc()
						.getUltimoVolume().isCancelada())))
				&& getConf().podePorConfiguracao(titular, lotaTitular,
						ExTipoDeConfiguracao.REFAZER);
	}

	/**
	 * Retorna se é possível indicar um móbil para guarda permanente. Têm de ser
	 * satisfeitas as seguintes condições:
	 * <ul>
	 * <li>Documento tem de estar assinado</li>
	 * <li>Móbil tem de ser via ou geral de processo</li>
	 * <li>Móbil não pode estar cancelado</li>
	 * <li>Móbil não pode estar em trânsito</li>
	 * <li>Móbil não pode estar juntado</li>
	 * <li>Móbil não pode ter sido já indicado para guarda permanente</li>
	 * <li>Móbil não pode ter sido arquivado permanentemente nem eliminado</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeIndicarPermanente(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {

		if (mob.isPendenteDeAnexacao())
			return false;
		
		return (!mob.doc().isPendenteDeAssinatura()
				&& (mob.isVia() || mob.isGeralDeProcesso())
				&& !mob.isCancelada() && !mob.isEmTransito(titular, lotaTitular)
				&& !mob.isJuntado()
				&& podeMovimentar(titular, lotaTitular, mob)
				&& !mob.isindicadoGuardaPermanente()
				&& !mob.isArquivadoPermanente() && !mob.isEmEditalEliminacao() && getConf()
				.podePorConfiguracao(
						titular,
						lotaTitular,
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_INDICACAO_GUARDA_PERMANENTE,
						ExTipoDeConfiguracao.MOVIMENTAR));
	}

	/**
	 * Retorna se é possível reclassificar um documento. Têm de ser satisfeitas
	 * as seguintes condições:
	 * <ul>
	 * <li>Documento tem de estar assinado</li>
	 * <li>Móbil tem de ser geral</li>
	 * <li>Móbil não pode ter sido eliminado</li>
	 * <li>Móbil não pode estar cancelado</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 ** 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeReclassificar(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {

		return (!mob.doc().isPendenteDeAssinatura() && mob.isGeral() && !mob.isCancelada()
				&& !mob.isEliminado() && getConf().podePorConfiguracao(titular,
				lotaTitular,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECLASSIFICACAO,
				ExTipoDeConfiguracao.MOVIMENTAR));
	}

	/**
	 * Retorna se é possível avaliar um documento. Têm de ser satisfeitas as
	 * seguintes condições:
	 * <ul>
	 * <li>Documento tem de estar assinado</li>
	 * <li>Móbil tem de ser geral</li>
	 * <li>Móbil não pode ter sido eliminado</li>
	 * <li>Móbil não pode estar cancelado</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeAvaliar(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {

		return (!mob.doc().isPendenteDeAssinatura() && mob.isGeral() && !mob.isCancelada()
				&& !mob.isEliminado() && getConf().podePorConfiguracao(titular,
				lotaTitular, ExTipoMovimentacao.TIPO_MOVIMENTACAO_AVALIACAO,
				ExTipoDeConfiguracao.MOVIMENTAR));
	}

	/**
	 * Retorna se é possível reverter a indicação de um móbil para guarda
	 * permanente. Têm de ser satisfeitas as seguintes condições:
	 * <ul>
	 * <li>Móbil tem de estar indicado para guarda permanente</li>
	 * <li>Móbil tem de ser via ou geral de processo</li>
	 * <li>Móbil não pode estar cancelado</li>
	 * <li>Móbil não pode estar em trânsito</li>
	 * <li>Móbil não pode estar juntado</li>
	 * <li>Móbil não pode ter sido arquivado permanentemente nem eliminado</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeReverterIndicacaoPermanente(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {

		return (mob.isindicadoGuardaPermanente()
				&& (mob.isVia() || mob.isGeralDeProcesso()) && !mob.isJuntado()
				&& !mob.isArquivadoPermanente() && !mob.isCancelada()
				&& !mob.isEmTransito(titular, lotaTitular) && getConf()
				.podePorConfiguracao(
						titular,
						lotaTitular,
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_REVERSAO_INDICACAO_GUARDA_PERMANENTE,
						ExTipoDeConfiguracao.MOVIMENTAR));
	}

	/**
	 * Retorna se é possível retirar um móbil de edital de eliminação. Têm de
	 * ser satisfeitas as seguintes condições:
	 * <ul>
	 * <li>Móbil não pode ter sido eliminado</li>
	 * <li>Móbil tem de estar em edital de eliminação</li>
	 * <li>Edital contendo o móbil precisa estar assinado</li>
	 * <li>Pessoa a fazer a retirada tem de ser o subscritor ou titular do
	 * edital</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeRetirarDeEditalEliminacao(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {

		if (mob.isEliminado())
			return false;

		ExMovimentacao movInclusao = mob
				.getUltimaMovimentacaoNaoCancelada(
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO,
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_EDITAL_DE_ELIMINACAO);

		if (movInclusao == null)
			return false;

		ExDocumento edital = movInclusao.getExMobilRef().getExDocumento();

		if (edital.isPendenteDeAssinatura())
			return false;

		if (edital.getSubscritor() == null)
			return lotaTitular.equivale(edital.getLotaCadastrante());
		else
			return titular.equivale(edital.getSubscritor())
					|| titular.equivale(edital.getTitular());

	}

	/**
	 * Retorna se a lotação ou pessoa tem permissão para receber documento
	 * 
	 * @param pessoa
	 * @param lotacao	
	 * @return
	 * @throws Exception
	 */
	public boolean podeReceberPorConfiguracao(final DpPessoa pessoa,
			final DpLotacao lotacao) {
		
		return getConf().podePorConfiguracao(pessoa, lotacao,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO,
				ExTipoDeConfiguracao.MOVIMENTAR);
	}
	
	public boolean podeTramitarPara(final DpPessoa pessoa,
			final DpLotacao lotacao, final DpPessoa pesDest, DpLotacao lotaDest) {
		
		ExTipoMovimentacao exTpMov = ExDao.getInstance().consultar(ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA,
				ExTipoMovimentacao.class, false);
		
		return getConf().podePorConfiguracao(null, null, null, null, null, null, null, null,
				exTpMov, null, null, null, lotacao, pessoa, null, null, ExTipoDeConfiguracao.MOVIMENTAR, pesDest,
				lotaDest, null, null, null, null);
	}
	
	/**
	 * Retorna se é possível receber o móbil. conforme as regras a seguir:
	 * <ul>
	 * <li>Móbil tem de ser via ou volume</li>
	 * <li>Móbil não pode estar cancelado</li>
	 * <li>Móbil não pode estar em algum arquivo</li>
	 * <li>Móbil tem de estar em trânsito</li>
	 * <li>Lotação do usuário tem de ser a do atendente definido na última
	 * movimentação</li>
	 * <li>Se o móbil for eletrônico, não pode estar marcado como Despacho
	 * pendente de assinatura, ou seja, móbil em que tenha havido despacho ou
	 * despacho com transferência não pode ser recebido antes de assinado o
	 * despacho</li>
	 * </ul>
	 * <b>Obs.: Teoricamente, qualquer pessoa pode receber móbil transferido
	 * para órgão externo</b>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeReceber(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {
		if (mob.doc().isEletronico()) 
			return podeReceberEletronico(titular, lotaTitular, mob);
		if (!(mob.isVia() || mob.isVolume()))
			return false;
		final ExMovimentacao exMov = mob.getUltimaMovimentacaoNaoCancelada();

		if (mob.isCancelada() || mob.isApensadoAVolumeDoMesmoProcesso() 
				|| mob.isSobrestado())
			return false;
		else if (!mob.isEmTransitoExterno()) {
			if (!mob.isAtendente(titular, lotaTitular))
				return false;
		}
		return getConf().podePorConfiguracao(titular, lotaTitular,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO,
				ExTipoDeConfiguracao.MOVIMENTAR);
	}

	/**
	 * Retorna se é possível receber o móbil eletronicamente, de acordo com as
	 * regras a seguir, <b>que deveriam ser parecidas com as de podeReceber(),
	 * para não haver incoerência</b>:
	 * <ul>
	 * <li>Móbil tem de ser via ou volume</li>
	 * <li>A última movimentação não cancelada do móbil não pode ser
	 * transferência externa <b>(regra falha, pois pode ser feita anotação)</b></li>
	 * e não pode ser Recebimento <b>(corrige recebimentos duplicados)</b></li>
	 * <li>Móbil não pode estar marcado como "Despacho pendente de assinatura",
	 * ou seja, tendo havido despacho ou despacho com transferência, este
	 * precisa ter sido assinado para haver transferência</li>
	 * <li>Se houver pessoa atendente definida na última movimentação não
	 * cancelada, o usuário tem de ser essa pessoa</li>
	 * <li>Não havendo pessoa atendente definida na última movimentação, apenas
	 * lotação atendente, a lotação do usuário tem de ser essa</li>
	 * <li>Documento tem de ser eletrônico <b>(melhor se fosse verificado no
	 * início)</b></li>
	 * <li>Móbil tem de estar em trãnsito <b>(melhor se fosse verificado no
	 * início)</b></li>
	 * <li>Não pode haver configuração impeditiva para recebimento (não para
	 * recebimento eletrônico)</li>
	 * </ul>
	 * 
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeReceberEletronico(DpPessoa cadastrante,
			DpLotacao lotaCadastrante, final ExMobil mob) {
		if (!(mob.isVia() || mob.isVolume()))
			return false;

		if (!mob.doc().isEletronico())
			return false;
		
		if (mob.isEmTransitoExterno()) {
			if (mob.isAtendente(cadastrante, lotaCadastrante))
				return true;
		}
		
		// Verifica se o despacho já está assinado
		for (CpMarca marca : mob.getExMarcaSet()) {
			if (marca.getCpMarcador().getIdMarcador() == CpMarcadorEnum.DESPACHO_PENDENTE_DE_ASSINATURA.getId())
				return false;
		}
		
		// Verifica se existe recebimento pendente para titular ou lotaTitular
		for (ExMovimentacao tramite : mob.calcularTramitesPendentes().tramitesPendentes) {
			if (tramite.isResp(cadastrante, lotaCadastrante))
				return getConf().podePorConfiguracao(cadastrante, lotaCadastrante,
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO,
						ExTipoDeConfiguracao.MOVIMENTAR);
		}
		return false;
	}
	
	// Deve receber só se o usuário não acabou de fazer um trâmite para sua própria
	// lotação
	public boolean deveReceberEletronico(DpPessoa titular, DpLotacao lotaTitular, final ExMobil mob) {
		if (mob.isEmTransitoExterno())
			return false;
		if (!podeReceberEletronico(titular, lotaTitular, mob))
			return false;
		ExMovimentacao ultMov = mob.getUltimaMovimentacaoNaoCancelada();
		if (ultMov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA
				&& ultMov.getCadastrante().equivale(titular) && ultMov.getLotaResp().equivale(lotaTitular))
			return false;
		return true;
	}

	/**
	 * Retorna se é possível vincular este móbil a outro, conforme as regras:
	 * <ul>
	 * <li>Móbil tem de ser via ou volume</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o móbil/usuário</li>
	 * <li>Móbil não pode estar em trãnsito</li>
	 * <li>Móbil não pode estar juntado</li>
	 * <li>Móbil não pode estar cancelado</li>
	 * <li>Móbil não pode ter sido eliminado</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeReferenciar(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {

		if (!(mob.isVia() || mob.isVolume()))
			return false;

		return !mob.isEmTransito(titular, lotaTitular)
				&& podeMovimentar(titular, lotaTitular, mob)
				&& !mob.isJuntado()
				&& !mob.isEliminado()
				&& !mob.doc().isCancelado()
				&& !mob.doc().isSemEfeito()
				&& getConf().podePorConfiguracao(titular, lotaTitular,
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_REFERENCIA,
						ExTipoDeConfiguracao.MOVIMENTAR);

		// return true;
	}

	/**
	 * Retorna se é possível registrar assinatura manual de documento que contém
	 * o móbil passado por parâmetro. As regras são as seguintes:
	 * <ul>
	 * <li>Móbil tem de ser geral</li>
	 * <li>Não pode ser móbil de processo interno importado</li>
	 * <li>Não pode ser móbil de documento externo</li>
	 * <li>Documento não pode estar cancelado</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro ou usuário tem de ser o
	 * próprio subscritor ou titular do documento</li>
	 * <li>Documento não pode ser eletrônico</li>
	 * <li>Documento tem de estar finalizado</li>
	 * <li>Móbil não pode estar em algum arquivo</li>
	 * <li>Móbil não pode ter sido eliminado</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeRegistrarAssinatura(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {
		if (!mob.isGeral())
			return false;
		if (mob.isArquivado() || mob.isEliminado())
			return false;
		if (mob.getExDocumento().isProcesso()
				&& mob.getExDocumento().getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_FOLHA_DE_ROSTO)
			return false;
		if (mob.doc().getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_FOLHA_DE_ROSTO
				|| mob.doc().isCancelado())
			return false;
		return ((mob.doc().getSubscritor() != null && mob.doc().getSubscritor()
				.equivale(titular))
				|| (mob.doc().getTitular() != null && mob.doc().getTitular()
						.equivale(titular)) || podeMovimentar(titular,
				lotaTitular, mob))
				/*
				 * || (ultMovNaoCancelada .getExEstadoDoc().getIdEstadoDoc() ==
				 * ExEstadoDoc.ESTADO_DOC_EM_ANDAMENTO || ultMovNaoCancelada
				 * .getExEstadoDoc().getIdEstadoDoc() ==
				 * ExEstadoDoc.ESTADO_DOC_PENDENTE_DE_ASSINATURA)
				 */
				&& !mob.doc().isEletronico()

				&& (mob.doc().isFinalizado())
				&& getConf()
						.podePorConfiguracao(
								titular,
								lotaTitular,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_REGISTRO_ASSINATURA_DOCUMENTO,
								ExTipoDeConfiguracao.MOVIMENTAR);
	}

	/**
	 * Retorna se é possível agendar publicação no Boletim. É necessário que não
	 * sejam ainda 17 horas e que <i>podeBotaoAgendarPublicacaoBoletim()</i>
	 * seja verdadeiro para este móbil e usuário.
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeAgendarPublicacaoBoletim(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) throws Exception {

		GregorianCalendar agora = new GregorianCalendar();
		agora.setTime(new Date());
		return podeBotaoAgendarPublicacaoBoletim(titular, lotaTitular, mob)
				&& (agora.get(Calendar.HOUR_OF_DAY) < 17 
					|| podeGerenciarPublicacaoBoletimPorConfiguracao(titular, lotaTitular, mob));
	}
	
	public boolean podeRestrigirAcesso(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob) {
		
		List<ExMovimentacao> listMovJuntada = new ArrayList<ExMovimentacao>();
        if(mob.getDoc().getMobilDefaultParaReceberJuntada() != null) {
            listMovJuntada.addAll(mob.getDoc().getMobilDefaultParaReceberJuntada().getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA));
        }
		
		return (getConf()
				.podePorConfiguracao(
						cadastrante,
						lotaCadastrante,
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_RESTRINGIR_ACESSO,
						ExTipoDeConfiguracao.MOVIMENTAR)) &&
				(!getConf().podePorConfiguracao(
							cadastrante,
							lotaCadastrante,
							mob.doc().getExModelo(),
							ExTipoDeConfiguracao.INCLUIR_DOCUMENTO) || mob.getDoc().getPai()==null) && listMovJuntada.size() == 0;
	}
	
	public boolean podeDesfazerRestricaoAcesso(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob) {
		List<ExMovimentacao> listMovJuntada = new ArrayList<ExMovimentacao>();
        if(mob.getDoc().getMobilDefaultParaReceberJuntada() != null) {
            listMovJuntada.addAll(mob.getDoc().getMobilDefaultParaReceberJuntada().getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA));
        }
		List<ExMovimentacao> lista = new ArrayList<ExMovimentacao>();
		lista.addAll(mob.getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_RESTRINGIR_ACESSO));
		return (!lista.isEmpty() &&
				getConf()
				.podePorConfiguracao(
						cadastrante,
						lotaCadastrante,
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_RESTRINGIR_ACESSO,
						ExTipoDeConfiguracao.MOVIMENTAR)) &&
				(!getConf().podePorConfiguracao(
							cadastrante,
							lotaCadastrante,
							mob.doc().getExModelo(),
							ExTipoDeConfiguracao.INCLUIR_DOCUMENTO) || mob.getDoc().getPai()==null) && listMovJuntada.size() == 0;
	}


	/**
	 * Retorna se é possível exibir a opção para agendar publicação no Boletim.
	 * Seguem as regras:
	 * <ul>
	 * <li>Móbil não pode ser geral</li>
	 * <li>Documento tem de estar finalizado</li>
	 * <li>Documento tem de ser do tipo interno produzido</li>
	 * <li><i>podeGerenciarPublicacaoBoletimPorConfiguracao()</i> ou
	 * <i>podeMovimentar()</i>tem de ser verdadeiro para o usuário</li>
	 * <li>Documento não pode já ter sido publicado em boletim</li>
	 * <li>Publicação no boletim não pode ter sido já agendada para o documento</li>
	 * <li>Documento tem de estar assinado</li>
	 * <li>Documento não pode ter sido eliminado</li>
	 * <li>Móbil não pode estar em algum arquivo</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * 
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeBotaoAgendarPublicacaoBoletim(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {
		if (!mob.isGeral())
			return false;

		if (!mob.doc().isFinalizado())
			return false;
		if (mob.doc().isEliminado())
			return false;
		if (mob.doc().getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_FOLHA_DE_ROSTO)
			return false;
		if (mob.doc().getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_FOLHA_DE_ROSTO)
			return false;
		boolean gerente = podeGerenciarPublicacaoBoletimPorConfiguracao(
				titular, lotaTitular, mob);
		return (podeMovimentar(titular, lotaTitular, mob) || gerente)
				// && !mob.doc().isEletronico()
				&& !mob.doc().isBoletimPublicado()
				&& !mob.doc().isPendenteDeAssinatura()
				&& !mob.doc().isPublicacaoBoletimSolicitada()
				&& !mob.isArquivado()
				&& (getConf()
						.podePorConfiguracao(
								titular,
								lotaTitular,
								mob.doc().getExModelo(),
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO_BOLETIM,
								ExTipoDeConfiguracao.MOVIMENTAR) || gerente);
	}

	/**
	 * Retorna se é possível alterar o nível de accesso do documento. É
	 * necessário apenas que o usuário possa acessar o documento e que não haja
	 * configuração impeditiva. <b>Obs.: Não é verificado se
	 * <i>podeMovimentar()</i></b>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeRedefinirNivelAcesso(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {

		if(mob.doc().isBoletimPublicado() || mob.doc().isDJEPublicado()) {
			if(podeAtenderPedidoPublicacao(titular, lotaTitular, mob) || podeGerenciarPublicacaoBoletimPorConfiguracao(titular, lotaTitular, mob))
				return true;
			
			return false;
		}
		
		/* Não permite redefinir acesso para documentos que foram publicados no portal da transparencia */
		if (mob.getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_PUBLICACAO_PORTAL_TRANSPARENCIA).size() > 0) {
			return false;
		}

		return !mob.isEliminado()
				&& podeAcessarDocumento(titular, lotaTitular, mob)
				&& podeMovimentar(titular, lotaTitular, mob)
				&& getConf()
						.podePorConfiguracao(
								titular,
								lotaTitular,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_REDEFINICAO_NIVEL_ACESSO,
								ExTipoDeConfiguracao.MOVIMENTAR);
	}

	/**
	 * Retorna se é possível que algum móbil seja juntado a este, segundo as
	 * seguintes regras:
	 * <ul>
	 * <li>Não pode estar cancelado</li>
	 * <li>Volume não pode estar encerrado</li>
	 * <li>Não pode estar em algum arquivo</li>
	 * <li>Não pode estar juntado</li>
	 * <li>Não pode estar em trânsito</li>
	 * <li><i>podeMovimentar()</i> precisa retornar verdadeiro para ele</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeSerJuntado(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {

		return !mob.isCancelada() && !mob.isVolumeEncerrado()
				&& !mob.isJuntado()
				&& !mob.isEmTransito(titular, lotaTitular) && !mob.isArquivado()
				&& podeMovimentar(titular, lotaTitular, mob);
	}

	/**
	 * Retorna se é possível a uma lotação, com base em configuração, receber
	 * móbil de documento não assinado. Não é aqui verificado se o móbil está
	 * realmente pendente de assinatura
	 * 
	 * @param pessoa
	 * @param lotacao
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeReceberDocumentoSemAssinatura(final DpPessoa pessoa,
			final DpLotacao lotacao, final ExMobil mob) {
		return getConf().podePorConfiguracao(pessoa, lotacao,
				ExTipoDeConfiguracao.RECEBER_DOC_NAO_ASSINADO);
	}

	/**
	 * Retorna se é possível fazer transferência. As regras são as seguintes
	 * para este móbil: <ul <li>Precisa ser via ou volume (não pode ser geral)</li>
	 * <li>Não pode estar em trânsito</li> <li>Não pode estar juntado.</li> <li>
	 * Não pode estar em arquivo permanente.</li> <li><i>podeMovimentar()</i>
	 * precisa retornar verdadeiro para ele</li> <li>Não pode haver configuração
	 * impeditiva</li> </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeTransferir(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {

		if(!podeSerTransferido(mob))
			return false;
		
		if (mob.isEmTransito(titular, lotaTitular))
			return false;

		return podeMovimentar(titular, lotaTitular, mob)
				&& getConf().podePorConfiguracao(titular, lotaTitular,
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA,
						ExTipoDeConfiguracao.MOVIMENTAR);
	}
	
	public boolean podeSerTransferido(final ExMobil mob) {
		if (mob.isPendenteDeAnexacao())
			return false;

		return (mob.isVia() || mob.isVolume())
				&& !mob.isJuntado()
				&& !mob.isApensadoAVolumeDoMesmoProcesso()
				&& !mob.isArquivado()
				&& (!mob.doc().isPendenteDeAssinatura() || (mob.doc().getExTipoDocumento()
						.getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_FOLHA_DE_ROSTO) || 
						(mob.doc().isProcesso() && mob.doc().getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_FOLHA_DE_ROSTO))
				&& !mob.isEmEditalEliminacao()
				&& !mob.isSobrestado()
				&& !mob.doc().isSemEfeito()
				&& podeSerMovimentado(mob);
	}
	
	public boolean podeTramitarEmParalelo(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {

		if(!podeSerTransferido(mob))
			return false;
		
		if (mob.isEmTransito(titular, lotaTitular))
			return false;

		return podeMovimentar(titular, lotaTitular, mob)
				&& getConf().podePorConfiguracao(titular, lotaTitular,
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRAMITE_PARALELO,
						ExTipoDeConfiguracao.MOVIMENTAR);
	}
	
	public boolean podeNotificar(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {
		
		if (!podeAcessarDocumento(titular, lotaTitular, mob))
			return false;

		if (mob.isPendenteDeAnexacao())
			return false;

		if (!((mob.isVia() || mob.isVolume())
				&& !mob.isJuntado()
				&& !mob.isApensadoAVolumeDoMesmoProcesso()
				&& !mob.isArquivado()
				&& (!mob.doc().isPendenteDeAssinatura() || (mob.doc().getExTipoDocumento()
						.getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_FOLHA_DE_ROSTO) || 
						(mob.doc().isProcesso() && mob.doc().getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_FOLHA_DE_ROSTO))
				&& !mob.isEmEditalEliminacao()
				&& !mob.isSobrestado()
				&& !mob.doc().isSemEfeito()))
			return false;
		
		return getConf().podePorConfiguracao(titular, lotaTitular,
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICACAO,
						ExTipoDeConfiguracao.MOVIMENTAR);
	}
	
	/**
	 * Retorna se é possível fazer transferência imediatamente antes da tela de assinatura. As regras são as seguintes
	 * para este móbil:
	 * <ul>
	 * <li><i>Destinatario esta definido</i>
	 *  <li><i>Destinatario pode receber documento</i>
	 *  <li><i>Se temporário, o documento está na mesma lotação do titular</i> 
	 * <li><i>Se finalizado, podeMovimentar()</i>
	 *  <li>Não pode haver configuração impeditiva</li> </ul>
	 * 
	 * @param destinatario
	 * @param lotaDestinatario 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * 
	 */
	public boolean podeTramitarPosAssinatura(final DpPessoa destinatario, final DpLotacao lotaDestinatario, 
			final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob) {

		if (lotaDestinatario == null && destinatario == null) 
			return false;
		
		if (!podeReceberPorConfiguracao(destinatario, lotaDestinatario))
			return false;
		
		if (!getConf().podePorConfiguracao(titular, lotaTitular,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA,
				ExTipoDeConfiguracao.MOVIMENTAR))
			return false;

		if(!mob.doc().isFinalizado()) { /* documento temporário e não sofreu movimentação. A lotação onde se encontra é a do cadastrante */
			if (mob.doc().getLotaCadastrante() != null && !mob.doc().getLotaCadastrante().equivale(lotaTitular)) 
				return false;			 	
		} else {
			return podeMovimentar(titular, lotaTitular, mob);				 
		}
		
		return true;
		
	}


	/**
	 * Retorna se é possível fazer vinculação deste mobil a outro, conforme as
	 * seguintes regras para <i>este</i> móbil:
	 * <ul>
	 * <li>Precisa ser via ou volume (não pode ser geral)</li>
	 * <li>Não pode estar em trânsito</li>
	 * <li>Não pode estar juntado.</li>
	 * <li><i>podeMovimentar()</i> precisa retornar verdadeiro para ele</li>
	 * </ul>
	 * Não é levada em conta, aqui, a situação do mobil ao qual se pertende
	 * vincular.
	 * 
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeVincular(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) throws Exception {

		if (!(mob.isVia() || mob.isVolume()))
			return false;

		return !mob.isEmTransito(titular, lotaTitular) && podeMovimentar(titular, lotaTitular, mob)
				&& !mob.isJuntado();

		// return true;

	}


	public boolean podeCancelarVinculacaoDocumento(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob,
			final ExMovimentacao mov) {

		if (mov.isCancelada())
			return false;


		if ((mov.getCadastrante() != null && mov.getCadastrante().equivale(
				titular))
				|| (mov.getCadastrante() == null && mov.getLotaCadastrante()
						.equivale(lotaTitular)))
			return true;


		if ((mov.getSubscritor() != null && mov.getSubscritor().equivale(
				titular))
				|| (mov.getSubscritor() == null && mov.getLotaSubscritor()
						.equivale(lotaTitular)))
			return true;


		if ((mov.getLotaSubscritor().equivale(lotaTitular)))
			return true;

		return getConf().podePorConfiguracao(titular, lotaTitular,
				mov.getIdTpMov(),
				ExTipoDeConfiguracao.CANCELAR_MOVIMENTACAO);
	}

	/**
	 * Retorna se é possível visualizar impressão do móbil. Sempre retorna
	 * <i>true</i>, a não ser que o documento esteja finalizado e o mobil em
	 * questão não seja via ou volume. isso impede que se visualize impressão do
	 * mobil geral após a finalização.
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 */
	public boolean podeVisualizarImpressao(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {
		if (!mob.isVia() && !mob.isVolume() && mob.doc().isFinalizado())

			return false;
		return !mob.isEliminado();/*
								 * if ((mob.doc().getConteudo() == null ||
								 * ExCompetenciaBL.viaCancelada(titular,
								 * lotaTitular, doc, numVia))) return false;

								 * return true;
								 */
	}

	/**
	 * Retorna se é possível visualizar impressão do documento em questão e de
	 * todos os filhos, com base na permissão para visualização da impressão de
	 * cada um dos filhos.
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 */
	public boolean podeVisualizarImpressaoCompleta(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {
		Set<ExDocumento> filhos = mob.getExDocumentoFilhoSet();
		return podeVisualizarImpressao(titular, lotaTitular, mob)
				&& filhos != null && filhos.size() > 0;
	}

	/*
	 * public boolean podeAtenderPedidoPublicacaoPorConfiguracao( DpPessoa
	 * titular, DpLotacao lotaTitular, final ExMobil mob) throws Exception { if
	 * (lotaTitular == null) return false; return
	 * getConf().podePorConfiguracao(titular, lotaTitular,
	 * ExTipoDeConfiguracao.ATENDER_PEDIDO_PUBLICACAO); }
	 */

	/**
	 * Retorna se é possível, com base em configuração, utilizar rotina para
	 * redefinição de permissões de publicação no DJE. Não é utilizado o
	 * parãmetro mob. <b>Atenção: Método em desuso.</b>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeDefinirPublicadoresPorConfiguracao(DpPessoa titular,
			DpLotacao lotaTitular, final ExMobil mob) throws Exception {
		if (lotaTitular == null)
			return false;
		return getConf().podePorConfiguracao(titular, lotaTitular,
				ExTipoDeConfiguracao.DEFINIR_PUBLICADORES);

	}

	/**
	 * Retorna se é possível, com base em configuração, utilizar rotina para
	 * redefinição de permissões de publicação no Boletim. Não é utilizado o
	 * parâmetro mob.
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeGerenciarPublicacaoBoletimPorConfiguracao(
			DpPessoa titular, DpLotacao lotaTitular, final ExMobil mob) {
		if (lotaTitular == null)
			return false;
		return getConf().podePorConfiguracao(titular, lotaTitular,
				ExTipoDeConfiguracao.GERENCIAR_PUBLICACAO_BOLETIM);
	}

	/**
	 * Método genérico que recebe função por String e concatena com o método de
	 * checagem de permissão correspondente. Por exemplo, para a função juntar,
	 * é invocado <i>podeJuntar()</i>
	 * 
	 * @param funcao
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 */
	public static boolean testaCompetencia(final String funcao,
			final DpPessoa titular, final DpLotacao lotaTitular,
			final ExMobil mob) {
		final Class[] classes = new Class[] { DpPessoa.class, DpLotacao.class,
				ExMobil.class };
		Boolean resposta = false;
		try {
			final Method method = ExCompetenciaBL.class.getDeclaredMethod(
					"pode" + funcao.substring(0, 1).toUpperCase()
							+ funcao.substring(1), classes);
			resposta = (Boolean) method.invoke(Ex.getInstance().getComp(),
					new Object[] { titular, lotaTitular, mob });
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return resposta.booleanValue();
	}
	/*
	 * Retorna se é possível incluir ciencia do documento a que pertence o
	 * móbil passado por parâmetro, conforme as seguintes condições:
	 * <ul>
	 * <li>Documento não foi tramitado</li>
	 * <li>Documento tem de estar assinado ou autenticado</li>
	 * <li>Documento não pode estar juntado a outro</li>
	 * <li>Usuario não fez ciência ainda</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeFazerCiencia(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {

		if (mob.doc().isExternoCapturado() && mob.getDoc().getAutenticacoesComTokenOuSenha().isEmpty())
				return false;
		
		return (SigaMessages.isSigaSP()
					&& !mob.doc().isPendenteDeAssinatura() 
					&& !mob.isCiente(titular) 
					&& !mob.isEmTransito(titular, lotaTitular) 
					&& !mob.isEliminado() 
					&& !mob.isJuntado()
					&& !mob.isArquivado()
					&& !mob.isVolumeEncerrado()
					&& getConf()
							.podePorConfiguracao(
									titular,
									lotaTitular,
									null,
									null,
									mob.getDoc().getExFormaDocumento(), 
									mob.getDoc().getExModelo(), 
									ExTipoMovimentacao.TIPO_MOVIMENTACAO_CIENCIA,
									ExTipoDeConfiguracao.MOVIMENTAR));
	}
	
	/**
	 * Método genérico que recebe função por String e concatena com o método de
	 * checagem de permissão correspondente. Por exemplo, para a função
	 * excluirAnexo, é invocado <i>podeExcluirAnexo()</i>
	 * 
	 * @param funcao
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @param mov
	 * @return
	 */
	public static boolean testaCompetenciaMov(final String funcao,
			final DpPessoa titular, final DpLotacao lotaTitular,
			final ExMobil mob, final ExMovimentacao mov) {
		final Class[] classes = new Class[] { DpPessoa.class, DpLotacao.class,
				ExMobil.class, ExMovimentacao.class };
		Boolean resposta = false;
		try {
			/*
			 * final Method method = ExCompetenciaBL.class.getDeclaredMethod(
			 * "pode" + funcao.substring(0, 1).toUpperCase() +
			 * funcao.substring(1), classes);
			 */
			ExCompetenciaBL comp = Ex.getInstance().getComp();
			final Method method = comp.getClass().getDeclaredMethod(
					"pode" + funcao.substring(0, 1).toUpperCase()
							+ funcao.substring(1), classes);

			resposta = (Boolean) method.invoke(comp, new Object[] { titular,
					lotaTitular, mob, mov });
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return resposta.booleanValue();
	}


	/**
	 * 
	 */
	public boolean podeDesfazerCancelamentoDocumento(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {


		ExDocumento documento = mob.getDoc();


		if (documento.isEletronico()
				&& documento.isCancelado()
				&& (documento.getLotaCadastrante().equivale(lotaTitular) ||(!documento.isExternoCapturado() &&
						documento.getSubscritor().equivale(titular))))
			return true;


		return false;
	}


	/**
	 * 
	 */
	public boolean podeReiniciarNumeracao(ExDocumento doc) throws Exception {
		if (doc == null || doc.getOrgaoUsuario() == null
				|| doc.getExFormaDocumento() == null)
			return false;

		return getConf().podePorConfiguracao(doc.getOrgaoUsuario(),
				doc.getExFormaDocumento(),
				ExTipoDeConfiguracao.REINICIAR_NUMERACAO_TODO_ANO);
	}
	
	
	public boolean podeReiniciarNumeracao(CpOrgaoUsuario orgaoUsuario, ExFormaDocumento formaDocumento) throws Exception {
		return getConf().podePorConfiguracao(orgaoUsuario,
				formaDocumento,
				ExTipoDeConfiguracao.REINICIAR_NUMERACAO_TODO_ANO);
	}
	

	/**


	 * Retorna se é possível autuar um expediente, com base nas seguintes
	 * regras:
	 * <ul>
	 * <li>Documento tem de ser expediente</li>
	 * <li>Documento tem de estar assinado</li>
	 * <li>Documento não pode estar sem efeito</li>
	 * <li>Móbil não pode ser geral</li>
	 * <li>Móbil não pode estar em edital de eliminação</li>
	 * <li>Móbil não pode estar juntado</li>
	 * <li>Móbil não pode estar apensado</li>
	 * <li>Móbil não pode estar em trânsito</li>
	 * <li>Móbil não pode estar arquivado permanentemente</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o usuário / móbil</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeAutuar(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) {
		
		if (mob.isPendenteDeAnexacao())
			return false;

		if (mob.doc().isSemEfeito())
			return false;

		if (mob.isEmEditalEliminacao() || mob.isArquivadoPermanente())
			return false;

		if (mob.isJuntado())
			return false;

		if (mob.isApensadoAVolumeDoMesmoProcesso())
			return false;
			
		if(mob.isArquivado())
			return false;
		
		if(mob.isSobrestado())
			return false;
			
		final boolean podeMovimentar = podeMovimentar(titular, lotaTitular, mob);

		return (!mob.isGeral() && mob.doc().isExpediente()
				&& !mob.doc().isPendenteDeAssinatura() && !mob.isEmTransito(titular, lotaTitular) && podeMovimentar && getConf().podePorConfiguracao(titular, lotaTitular, ExTipoMovimentacao.TIPO_MOVIMENTACAO_AUTUAR, ExTipoDeConfiguracao.MOVIMENTAR) && getConf().podePorConfiguracao(titular, lotaTitular, titular.getCargo(), titular.getFuncaoConfianca(), mob.doc().getExFormaDocumento(), mob.doc().getExModelo(), ExTipoMovimentacao.TIPO_MOVIMENTACAO_AUTUAR, ExTipoDeConfiguracao.MOVIMENTAR));
	}
	
	public boolean podeAssinarPor(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante) {
		return (getConf()
				.podePorConfiguracao(
						cadastrante,
						lotaCadastrante,
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_POR,
						ExTipoDeConfiguracao.MOVIMENTAR)) ;
	}

	public boolean podeDisponibilizarNoAcompanhamentoDoProtocolo(final DpPessoa titular, final DpLotacao lotaTitular,
			final ExDocumento doc) {
		return getConf()
				.podePorConfiguracao(null, null, null, doc.getExTipoDocumento(), doc.getExFormaDocumento(), 
					doc.getExModelo(), null, null, 
					ExDao.getInstance().consultar(ExTipoMovimentacao.TIPO_MOVIMENTACAO_EXIBIR_NO_ACOMPANHAMENTO_DO_PROTOCOLO,
							ExTipoMovimentacao.class, false), 
					null, null, null, lotaTitular, titular, null,null,
					ExTipoDeConfiguracao.MOVIMENTAR, null, null, null, null, null, null);
	}
		
	public boolean podePublicarPortalTransparencia(final DpPessoa cadastrante,
			final DpLotacao lotacao, final ExMobil mob) {
		
		
		return (mob.doc().isFinalizado() 
				&& !mob.doc().isSemEfeito() 
				&& !mob.doc().isEliminado()
				&& podeMovimentar(cadastrante, lotacao, mob)
				&& !mob.doc().isPendenteDeAssinatura()
				&& (mob.getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_PUBLICACAO_PORTAL_TRANSPARENCIA).size() == 0)
				&& getConf() 
						.podePorConfiguracao(
								cadastrante,
								lotacao,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_PUBLICACAO_PORTAL_TRANSPARENCIA,
								ExTipoDeConfiguracao.MOVIMENTAR)) ;
	}
	
	public boolean podePublicarPortalTransparenciaWS(final DpPessoa cadastrante,
			final DpLotacao lotacao, final ExMobil mob) {
		
		
		return (mob.doc().isFinalizado() 
				&& !mob.doc().isSemEfeito() 
				&& !mob.doc().isEliminado()
				&& !mob.doc().isPendenteDeAssinatura()
				&& (mob.getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_PUBLICACAO_PORTAL_TRANSPARENCIA).size() == 0)
				&& getConf().podePorConfiguracao(cadastrante, lotacao,
						ExTipoDeConfiguracao.AUTORIZAR_MOVIMENTACAO_POR_WS));
	}

	public boolean ehPublicoExterno(DpPessoa titular) {
		// TODO Auto-generated method stub
		return AcessoConsulta.ehPublicoExterno(titular);
	}
	
	private boolean podePorConfiguracaoParaMovimentacao(final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob, long idTpMov) {		
		ExTipoMovimentacao exTpMov = ExDao.getInstance().consultar(idTpMov, ExTipoMovimentacao.class, false);
		
		return getConf().podePorConfiguracao(null, mob.getDoc().getExFormaDocumento().getExTipoFormaDoc(), null, mob.getDoc().getExTipoDocumento(), 
				mob.getDoc().getExFormaDocumento(), mob.getDoc().getExModelo(), null, null, 
				exTpMov, null, null, null, lotaTitular, titular, null, null, 
				ExTipoDeConfiguracao.MOVIMENTAR, null, null, null, null, null, null);
	}
	
	public boolean podeCapturarPDF(final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob, final Long idTipoDoc) {
		Long idTpDoc = (idTipoDoc != null ? idTipoDoc : mob.getDoc().getExTipoDocumento().getId());
		boolean capturado = idTpDoc == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_CAPTURADO
				|| idTpDoc == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_CAPTURADO;
		if (!capturado)
			return false;
		ExDocumento doc = mob.doc();
		if (!doc.isFinalizado())
			return true;
		if (!doc.jaTransferido() && !doc.isAssinadoPorTodosOsSignatariosComTokenOuSenha() && !mob.isJuntado()
				&& !mob.isJuntadoExterno() && !mob.isCancelada() && doc.getAutenticacoesComTokenOuSenha().isEmpty()
				&& capturado && (Ex.getInstance().getConf().podePorConfiguracao(titular, lotaTitular,
						ExTipoDeConfiguracao.TROCAR_PDF_CAPTURADOS))) {
			return true;
		}
		return false;
	}

	public boolean podeCapturarPDF(final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob) {
		return podeCapturarPDF(titular, lotaTitular, mob, null);
	}
	/**
	 * Retorna se é possível definir um prazo para assinatura do documento, conforme as regras:
	 * <ul>
	 * <li>Móbil deve ser geral</li>
	 * <li>Móbil não pode estar cancelado ou sem efeito ou arquivado</li>
	 * <li><i>podeMovimentar()</i> tem de ser verdadeiro para o móbil/usuário</li>
	 * <li>Documento tem de estar pendente de assinatura</li>
	 * <li>Móbil deve estar finalizado</li>
	 * <li>Não pode haver configuração impeditiva</li>
	 * </ul>
	 * 
	 * @param titular
	 * @param lotaTitular
	 * @param mob
	 * @return
	 * @throws Exception
	 */
	public boolean podeDefinirPrazoAssinatura(DpPessoa titular, DpLotacao lotaTitular, 
			ExMobil mob) {

		return mob.isGeral()
				&& !mob.isCancelada()
				&& !mob.doc().isSemEfeito()
				&& !mob.isArquivado()
				&& !mob.isSobrestado()
				&& mob.doc().isFinalizado()
				&& mob.doc().isPendenteDeAssinatura()
				&& getConf().podePorConfiguracao(titular, lotaTitular, titular.getCargo(), titular.getFuncaoConfianca(), mob.doc().getExFormaDocumento(), mob.doc().getExModelo(), 
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_PRAZO_ASSINATURA,
						ExTipoDeConfiguracao.MOVIMENTAR);
	}

}
