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
 * Criado em  13/09/2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.vraptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExTemporalidade;
import br.gov.jfrj.siga.ex.ExTipoDestinacao;
import br.gov.jfrj.siga.ex.ExVia;
import br.gov.jfrj.siga.ex.SigaExProperties;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.util.MascaraUtil;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.persistencia.ExClassificacaoDaoFiltro;

@Resource
public class ExClassificacaoController extends SigaSelecionavelControllerSupport<ExClassificacao, ExClassificacaoDaoFiltro> implements IUsaMascara {

	public ExClassificacaoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, ExDao.getInstance(), so, em);
		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();	
	}

	@Override
	public ExClassificacaoDaoFiltro createDaoFiltro() {
/*		final ExClassificacaoDaoFiltro flt = new ExClassificacaoDaoFiltro();

		if (nivelAlterado != null) {
			for (int i = nivelAlterado; i < nivelSelecionado.length - 1; i++) {
				nivelSelecionado[i + 1] = null;
			}

		}

		String codigoSelecionado = null;
		for (int i = nivelSelecionado.length - 1; i >= 0; i--) {
			if (nivelSelecionado[i] != null
					&& !nivelSelecionado[i].equals("-1")) {
				codigoSelecionado = nivelSelecionado[i];
				break;
			}
		}

		if (codigoSelecionado != null) {
			flt.setSigla(codigoSelecionado);
		}

		flt.setDescricao(nome);
		return flt;*/
		return null;
	}

	@Get("app/expediente/classificacao/listar")
	public void lista() throws AplicacaoException, Exception {
		assertAcesso("DOC:Módulo de Documentos;FE:Ferramentas;PC:Plano de Classificação");
		result.include("classificacaoVigente", getClassificacaoVigente());
	}

	public String aEditar(String codificacao, String acao) throws Exception {
		assertAcesso("DOC:Módulo de Documentos;FE:Ferramentas;PC:Plano de Classificação");
		ExClassificacao exClass = null;
		if (codificacao != null) {
			exClass = buscarExClassificacao(codificacao);
		}
		if (exClass == null && !acao.equals("nova_classificacao")) {
			throw new AplicacaoException(
					"A classificação documental não está disponível: "+ codificacao);
		}
		return "success";
	}

	private ExClassificacao buscarExClassificacao(String codificacao) {
		return ExDao.getInstance().consultarExClassificacao(codificacao);
	}

/*	public String aGravar() throws Exception {
		assertAcesso("DOC:Módulo de Documentos;FE:Ferramentas;PC:Plano de Classificação");
		ExClassificacao exClass;
		dao().iniciarTransacao();

		if (getCodificacao().length() == 0
				|| getDescrClassificacao().length() == 0) {
			throw new AplicacaoException(
					"Preencha o código da classificação e a descrição!");
		}

		if (getAcao().equals("nova_classificacao")) {
			ExClassificacao exClassExistente = buscarExClassificacao(getCodificacao());
			if (exClassExistente != null) {
				throw new AplicacaoException(
						"A classificação documental já existe: "
								+ exClassExistente.getCodificacao());
			} else {
				exClass = new ExClassificacao();
				lerForm(exClass);
				Ex.getInstance()
						.getBL()
						.incluirExClassificacao(exClass,
								getIdentidadeCadastrante());
			}
		} else {
			ExClassificacao exClassAntiga = buscarExClassificacao(getCodificacaoAntiga());
			if (exClassAntiga != null
					&& !exClassAntiga.getCodificacao().equals(getCodificacao())) {
				exClass = new ExClassificacao();
				lerForm(exClass);
				Ex.getInstance()
						.getBL()
						.moverClassificacao(exClass, exClassAntiga,
								getIdentidadeCadastrante());
			} else {
				ExClassificacao exClassNovo = Ex.getInstance().getBL()
						.getCopia(exClassAntiga);
				lerForm(exClassNovo);
				Ex.getInstance()
						.getBL()
						.alterarExClassificacao(exClassNovo, exClassAntiga,
								dao().consultarDataEHoraDoServidor(),
								getIdentidadeCadastrante());
			}

		}

		dao().commitTransacao();

		setMensagem("Classificação salva!");
		return "success";
	}

	public String aExcluir(String codificacao) throws Exception {
		assertAcesso("DOC:Módulo de Documentos;FE:Ferramentas;PC:Plano de Classificação");
		ExClassificacao exClass;
		dao().iniciarTransacao();
		exClass = buscarExClassificacao(codificacao);
		Ex.getInstance().getBL()
				.excluirExClassificacao(exClass, getIdentidadeCadastrante());
		dao().commitTransacao();
		return "success";
	}

	public String aGravarVia() throws Exception {
		assertAcesso("DOC:Módulo de Documentos;FE:Ferramentas;PC:Plano de Classificação");
		verificarParamsVia();
		dao().iniciarTransacao();

		Date dt = dao().consultarDataEHoraDoServidor();

		ExClassificacao exClassAntiga = buscarExClassificacao(getCodificacao());
		if (exClassAntiga == null)
			throw new AplicacaoException("Erro ao obter a classificação");
		ExClassificacao exClassNovo = Ex.getInstance().getBL()
				.getCopia(exClassAntiga);
		dao().gravarComHistorico(exClassNovo, exClassAntiga, dt,
				getIdentidadeCadastrante());

		ExVia exVia = null;
		setExClass(exClassNovo);
		if (getIdVia() == null) {
			// nova via

			exVia = new ExVia();
			lerFormVia(exVia);
			exVia.setCodVia(String
					.valueOf(exClassAntiga.getExViaSet().size() + 1));
			dao().gravarComHistorico(exVia, null, null,
					getIdentidadeCadastrante());
			exClassNovo.getExViaSet().add(exVia);
		} else {
			// alterar via existente
			exVia = dao().consultar(getIdVia(), ExVia.class, false);
			ExVia exViaNova = new ExVia();
			try {
				PropertyUtils.copyProperties(exViaNova, exVia);
				// novo id
				exViaNova.setId(null);

				lerFormVia(exViaNova);

				dao().gravarComHistorico(exViaNova, exVia, dt,
						getIdentidadeCadastrante());

				exClassNovo.getExViaSet().add(exViaNova);
				exClassAntiga.getExViaSet().remove(exVia);

			} catch (Exception e) {
				throw new AplicacaoException(
						"Erro ao copiar as propriedades da via anterior.");
			}

		}
		Ex.getInstance()
				.getBL()
				.copiarReferencias(exClassNovo, exClassAntiga, dt,
						getIdentidadeCadastrante());
		dao().commitTransacao();
		return "success";
	}

	private void verificarParamsVia() throws AplicacaoException {
		if (getIdDestino() == null || getIdDestino() <= 0) {
			throw new AplicacaoException(
					"A destinação da via deve ser definida!");
		}
	}

	public String aExcluirVia() throws Exception {
		assertAcesso("DOC:Módulo de Documentos;FE:Ferramentas;PC:Plano de Classificação");
		dao().iniciarTransacao();

		ExVia exVia = dao().consultar(getIdVia(), ExVia.class, false);
		dao().excluirComHistorico(exVia, null, getIdentidadeCadastrante());

		ExClassificacao exClassAntiga = buscarExClassificacao(getCodificacao());
		if (exClassAntiga == null)
			throw new AplicacaoException("Erro ao obter a classificação");
		ExClassificacao exClassNovo = Ex.getInstance().getBL()
				.getCopia(exClassAntiga);
		Ex.getInstance()
				.getBL()
				.alterarExClassificacao(exClassNovo, exClassAntiga,
						dao().consultarDataEHoraDoServidor(),
						getIdentidadeCadastrante());
		setExClass(exClassNovo);

		dao().commitTransacao();
		return "success";
	}

	private void lerForm(ExClassificacao c) {
		c.setCodificacao(getCodificacao());
		c.setDescrClassificacao(getDescrClassificacao());
		c.setObs(getObs());
	}

	private void lerFormVia(ExVia exVia) {

		ExTipoDestinacao destino = !getIdDestino().equals(-1L) ? dao()
				.consultar(getIdDestino(), ExTipoDestinacao.class, false)
				: null;
		ExTipoDestinacao destFinal = !getIdDestinacaoFinal().equals(-1L) ? dao()
				.consultar(getIdDestinacaoFinal(), ExTipoDestinacao.class,
						false) : null;
		ExTemporalidade tempCorrente = !getIdTemporalidadeArqCorr().equals(-1L) ? dao()
				.consultar(getIdTemporalidadeArqCorr(), ExTemporalidade.class,
						false) : null;
		ExTemporalidade tempInterm = !getIdTemporalidadeArqInterm().equals(-1L) ? dao()
				.consultar(getIdTemporalidadeArqInterm(),
						ExTemporalidade.class, false) : null;

		exVia.setExClassificacao(getExClass());
		exVia.setExTipoDestinacao(destino);
		exVia.setExDestinacaoFinal(destFinal);
		exVia.setTemporalidadeCorrente(tempCorrente);
		exVia.setTemporalidadeIntermediario(tempInterm);
		exVia.setCodVia(getCodigoVia());
		exVia.setObs(getObs());

	}
	
	public List<ExClassificacao> getClassificacoesDoNivel(Integer nivel) {
		List<ExClassificacao> result = new ArrayList<ExClassificacao>();

		// se primeira lista, carrega incondicionalmente
		if (nivel == 0) {
			return ExDao.getInstance().listarExClassificacaoPorNivel(
					MascaraUtil.getInstance().getMscTodosDoNivel(1));
		}

		// se lista do nível anterior está definido, carrega lista baseando-se
		// na anterior
		String nivelListaAnterior = nivelSelecionado[nivel - 1];
		if (nivelListaAnterior != null && !nivelListaAnterior.equals("-1")) {
			return ExDao.getInstance().listarExClassificacaoPorNivel(
					MascaraUtil.getInstance().getMscFilho(nivelListaAnterior,
							false), nivelListaAnterior);
		}

		return result;
	}	

	@Override
	public Selecionavel selecionarPorNome(final ExClassificacaoDaoFiltro flt)
			throws AplicacaoException {

		// Procura por nome
		flt.setDescricao(Texto.removeAcentoMaiusculas(flt.getSigla()));
		flt.setSigla(null);
		final List l = ExDao.getInstance().consultarPorFiltro(flt);
		if (l != null)
			if (l.size() == 1)
				return (ExClassificacao) l.get(0);
		return null;
	}
*/

	@Override
	public Selecionavel selecionarVerificar(Selecionavel sel)
			throws AplicacaoException {
		return (sel != null && ((ExClassificacao) sel).isIntermediaria()) ? null
				: sel;
	}

	public List<ExClassificacao> getClassificacaoVigente() {
		return ExDao.getInstance().consultarExClassificacaoVigente();
	}

	public List<ExTipoDestinacao> getListaExTipoDestinacao() {
		return ExDao.getInstance().listarExTiposDestinacao();
	}

	public List<ExTemporalidade> getListaExTemporalidade() {
		return ExDao.getInstance().listarAtivos(ExTemporalidade.class, "descTemporalidade");
	}

	public String getMascaraEntrada() {
		return MascaraUtil.getInstance().getMascaraEntrada();
	}

	public String getMascaraSaida() {
		return MascaraUtil.getInstance().getMascaraSaida();
	}

	public String getMascaraJavascript() {
		return SigaExProperties.getExClassificacaoMascaraJavascript();
	}

}