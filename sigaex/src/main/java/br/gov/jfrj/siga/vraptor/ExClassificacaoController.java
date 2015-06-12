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
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
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
public class ExClassificacaoController extends SigaSelecionavelControllerSupport<ExClassificacao, ExClassificacaoDaoFiltro> {
	private String[] nivelSelecionado;	
	private Integer nivelAlterado;

	public ExClassificacaoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, ExDao.getInstance(), so, em);
		int totalItens = getTotalDeNiveis();
		nivelSelecionado = new String[totalItens];	
	}
	
	@Override
	public ExClassificacaoDaoFiltro createDaoFiltro() {
		final ExClassificacaoDaoFiltro flt = new ExClassificacaoDaoFiltro();

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

		flt.setDescricao(getNome());
		return flt;
	}

	@Get("app/expediente/classificacao/listar")
	public void lista() {
		assertAcesso("DOC:Módulo de Documentos;FE:Ferramentas;PC:Plano de Classificação");
		result.include("classificacaoVigente", getClassificacaoVigente());
	}
	
	
	@Get("app/classificacao/buscar")
	public void busca(final String sigla, final String postback, final Integer paramoffset, final String nome, 
			final String[] nivelSelecionado, final Integer nivelAlterado, final Boolean discriminarVias) throws Exception {
		setNome(nome);
		this.nivelAlterado = nivelAlterado;
        this.nivelSelecionado = nivelSelecionado != null?nivelSelecionado:this.nivelSelecionado;	
        getP().setOffset(paramoffset);
		aBuscar(sigla, postback);
		final String[] listaNiveis = new String[getTotalDeNiveis()];
		final String[] nomeNivel = new String[getTotalDeNiveis()];
		final List<String> listaNomes = SigaExProperties.getExClassificacaoNomesNiveis();
		final List<ExClassificacao>[] classificacoesDoNivel = new List[getTotalDeNiveis()];
		for (int i = 0; i < listaNiveis.length; i++) {
			listaNiveis[i] = String.valueOf(i);
			nomeNivel[i] = listaNomes.get(i + 1);
			classificacoesDoNivel[i] = getClassificacoesDoNivel(i);			
		}		

		result.include("listaNiveis", listaNiveis);
		result.include("nomeDoNivel", nomeNivel);
		result.include("classificacoesDoNivel", classificacoesDoNivel);
		result.include("nivelSelecionado", nivelSelecionado);
		result.include("discriminarVias", discriminarVias);
		result.include("itens", getItens());
		result.include("tamanho", getTamanho());
		result.include("nome", getNome());
	}	
	
	@Get("app/classificacao/selecionar")
	public void selecionar(String sigla) throws Exception {
		String resultado =  super.aSelecionar(sigla);
		if (resultado == "ajax_retorno"){
			result.include("sel", getSel());
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_retorno.jsp");
		}else{
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_vazio.jsp");
		}
	}

	@Get("app/expediente/classificacao/editar")
	public ExClassificacao edita(ExClassificacao exClassificacao, String codificacao, String acao) throws Exception {
		assertAcesso("DOC:Módulo de Documentos;FE:Ferramentas;PC:Plano de Classificação");
		ExClassificacao exClass = null;
		if (codificacao != null && exClassificacao == null) {
			exClass = buscarExClassificacao(codificacao);
		}else{
			exClass = exClassificacao;
		}
		
		if (exClass == null && !acao.equals("nova_classificacao")) {
			throw new AplicacaoException(
					"A classificação documental não está disponível: "+ codificacao);
		}
		
		result.include("listaExTipoDestinacao", getListaExTipoDestinacao());
		result.include("listaExTemporalidade", getListaExTemporalidade());
		result.include("idTpDestinacao", -1);
		result.include("idTemporalidadeArqCorr", -1);
		result.include("idTemporalidadeArqInterm", -1);
		result.include("idTpDestinacaoFinal", -1);
		result.include("exibirAdicaoDeVia", exibirAdicaoDeVia(codificacao));
		result.include("acao", acao);
		result.include("mascaraEntrada", MascaraUtil.getInstance().getMascaraEntrada());
		result.include("mascaraSaida", MascaraUtil.getInstance().getMascaraSaida());
		result.include("mascaraJavascript", SigaExProperties.getExClassificacaoMascaraJavascript());
		return exClass;
	}
	@Get("app/expediente/classificacao/gravar")
	public void gravar(ExClassificacao exClassificacao, String codificacaoAntiga, String acao) throws Exception {
		assertAcesso("DOC:Módulo de Documentos;FE:Ferramentas;PC:Plano de Classificação");
		
		if (exClassificacao.getCodificacao().length() == 0 || exClassificacao.getDescrClassificacao().length() == 0) {
			throw new AplicacaoException("Preencha o código da classificação e a descrição!");
		}

		if (acao.equals("nova_classificacao")) {
			ExClassificacao exClassExistente = buscarExClassificacao(exClassificacao.getCodificacao());
			if (exClassExistente != null) {
				throw new AplicacaoException("A classificação documental já existe: "+ exClassExistente.getCodificacao());
			}
		}
		
		dao().iniciarTransacao();
		try{

			if (exClassificacao.getCodificacao().length() == 0 || exClassificacao.getDescrClassificacao().length() == 0) {
				throw new AplicacaoException("Preencha o código da classificação e a descrição!");
			}
	
			if (acao.equals("nova_classificacao")) {
				Ex.getInstance().getBL().incluirExClassificacao(exClassificacao, getIdentidadeCadastrante());
			} else {
				ExClassificacao exClassAntiga = buscarExClassificacao(codificacaoAntiga);
				if (exClassAntiga != null && !exClassAntiga.getCodificacao().equals(exClassificacao.getCodificacao())) {
					ExClassificacao exClass = new ExClassificacao();
					exClass.setCodificacao(exClassificacao.getCodificacao());
					exClass.setDescrClassificacao(exClassificacao.getDescrClassificacao());
					exClass.setObs(exClassificacao.getObs());
					Ex.getInstance().getBL().moverClassificacao(exClass, exClassAntiga,getIdentidadeCadastrante());
				} else {
					ExClassificacao exClassNovo = Ex.getInstance().getBL().getCopia(exClassAntiga);
					exClassNovo.setCodificacao(exClassificacao.getCodificacao());
					exClassNovo.setDescrClassificacao(exClassificacao.getDescrClassificacao());
					exClassNovo.setObs(exClassificacao.getObs());
					Ex.getInstance().getBL().alterarExClassificacao(exClassNovo, exClassAntiga,dao().consultarDataEHoraDoServidor(),
							getIdentidadeCadastrante());
					System.out.println(exClassNovo);
					System.out.println(exClassAntiga);
					System.out.println(exClassificacao);
				}
	
			}
	
			dao().commitTransacao();
			setMensagem("Classificação salva!");
			result.redirectTo("editar?codificacao="+exClassificacao.getCodificacao()+"&acao=editar_classificacao");
		} catch (Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Não foi possível gravar classificação no banco de dados."+e.getMessage());			
		}			
	}
	
	@Get("app/expediente/classificacao/excluir")
	public void excluir(String codificacao) throws Exception {
		assertAcesso("DOC:Módulo de Documentos;FE:Ferramentas;PC:Plano de Classificação");
		dao().iniciarTransacao();
		try{		
			ExClassificacao exClass;
			exClass = buscarExClassificacao(codificacao);
			Ex.getInstance().getBL().excluirExClassificacao(exClass, getIdentidadeCadastrante());
			dao().commitTransacao();
			result.redirectTo(this).lista();
		} catch (Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Não foi possível excluir classificação do banco de dados."+e.getMessage());			
		}			
	}
	
	
	@Post("app/expediente/classificacao/gravarVia")
	public void gravarVia(String acao, String codificacao, ExVia via, Long idDestino, Long idTemporalidadeArqCorr,
			Long idTemporalidadeArqInterm, Long idDestinacaoFinal) throws Exception {
		assertAcesso("DOC:Módulo de Documentos;FE:Ferramentas;PC:Plano de Classificação");
		if (idDestino == null || idDestino <= 0) {
			throw new AplicacaoException("A destinação da via deve ser definida!");
		}
		dao().iniciarTransacao();
		try{

			Date dt = dao().consultarDataEHoraDoServidor();
	
			ExClassificacao exClassAntiga = buscarExClassificacao(codificacao);
			if (exClassAntiga == null){
				throw new AplicacaoException("Erro ao obter a classificação");
			}
			ExClassificacao exClassNovo = Ex.getInstance().getBL().getCopia(exClassAntiga);
			dao().gravarComHistorico(exClassNovo, exClassAntiga, dt,getIdentidadeCadastrante());
	
			ExVia exVia = null;
			ExVia exViaGravar = null;
			Date dtHist = null;
			Boolean removerViaAntiga = false;
			if (via.getId() == null) {
				// nova via
	
				exVia = new ExVia();
				exVia.setCodVia(String.valueOf(exClassAntiga.getExViaSet().size() + 1));
				exViaGravar = exVia;
				exVia = null;
				dtHist = null;
			} else {
				// alterar via existente
				exVia = dao().consultar(via.getId(), ExVia.class, false);
				ExVia exViaNova = new ExVia();
				try {
					PropertyUtils.copyProperties(exViaNova, exVia);
					// novo id
					exViaNova.setId(null);
					exViaNova.setCodVia(exVia.getCodVia());
					exViaGravar = exViaNova;
					dtHist = dt;
					removerViaAntiga = true;
	
				} catch (Exception e) {
					throw new AplicacaoException("Erro ao copiar as propriedades da via anterior.");
				}
	
			}
			ExTipoDestinacao destino = !idDestino.equals(-1L) ? dao()
					.consultar(idDestino, ExTipoDestinacao.class, false): null;
					
			ExTipoDestinacao destFinal = !idDestinacaoFinal.equals(-1L) ? dao()
					.consultar(idDestinacaoFinal, ExTipoDestinacao.class,false) : null;
					
			ExTemporalidade tempCorrente = !idTemporalidadeArqCorr.equals(-1L) ? dao()
					.consultar(idTemporalidadeArqCorr, ExTemporalidade.class,false) : null;
					
			ExTemporalidade tempInterm = !idTemporalidadeArqInterm.equals(-1L) ? dao()
					.consultar(idTemporalidadeArqInterm,ExTemporalidade.class, false) : null;
	
			exViaGravar.setExClassificacao(exClassNovo);
			exViaGravar.setExTipoDestinacao(destino);
			exViaGravar.setExDestinacaoFinal(destFinal);
			exViaGravar.setTemporalidadeCorrente(tempCorrente);
			exViaGravar.setTemporalidadeIntermediario(tempInterm);
			exViaGravar.setObs(via.getObs());		
	
			dao().gravarComHistorico(exViaGravar, exVia, dtHist, getIdentidadeCadastrante());
	
			exClassNovo.getExViaSet().add(exViaGravar);		
			
			if (removerViaAntiga) {
				exClassAntiga.getExViaSet().remove(exVia);
			}
			
			Ex.getInstance().getBL().copiarReferencias(exClassNovo, exClassAntiga, dt,getIdentidadeCadastrante());
			dao().commitTransacao();
			//result.redirectTo("editar?codificacao="+codificacao+"&acao="+acao);
			result.forwardTo(this).edita(exClassNovo, codificacao, acao);
		} catch (Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Não foi possível gravar via no banco de dados."+e.getMessage());			
		}				
	}

	@Get("app/expediente/classificacao/excluirVia")
	public void excluirVia(Long idVia, String codificacao, String acao) throws Exception {
		assertAcesso("DOC:Módulo de Documentos;FE:Ferramentas;PC:Plano de Classificação");
		dao().iniciarTransacao();
		try {
			ExVia exVia = dao().consultar(idVia, ExVia.class, false);
			dao().excluirComHistorico(exVia, null, getIdentidadeCadastrante());
	
			ExClassificacao exClassAntiga = buscarExClassificacao(codificacao);
			if (exClassAntiga == null){
				throw new AplicacaoException("Erro ao obter a classificação");
			}
			
			ExClassificacao exClassNovo = Ex.getInstance().getBL().getCopia(exClassAntiga);
			Ex.getInstance().getBL().alterarExClassificacao(exClassNovo, exClassAntiga, dao().consultarDataEHoraDoServidor(),
							getIdentidadeCadastrante());
	
			dao().commitTransacao();
		
		} catch (Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Não foi possível excluir via do banco de dados."+e.getMessage());			
		}		
		result.redirectTo("editar?codificacao="+codificacao+"&acao="+acao);
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

	@Override
	public Selecionavel selecionarVerificar(Selecionavel sel)
			throws AplicacaoException {
		return (sel != null && ((ExClassificacao) sel).isIntermediaria()) ? null
				: sel;
	}

	private List<ExClassificacao> getClassificacaoVigente() {
		return ExDao.getInstance().consultarExClassificacaoVigente();
	}

	private List<ExTipoDestinacao> getListaExTipoDestinacao() {
		return ExDao.getInstance().listarExTiposDestinacao();
	}

	private List<ExTemporalidade> getListaExTemporalidade() {
		return ExDao.getInstance().listarAtivos(ExTemporalidade.class, "descTemporalidade");
	}
	
	private ExClassificacao buscarExClassificacao(String codificacao) {
		return ExDao.getInstance().consultarExClassificacao(codificacao);
	}	
	
	private Integer getTotalDeNiveis() {
		return MascaraUtil.getInstance().getTotalDeNiveisDaMascara();
	}	
	
	private boolean exibirAdicaoDeVia(String codificacao) {
		Integer i = SigaExProperties.getExClassificacaoNivelMinimoDeEnquadramento();
		if (codificacao != null) {
			if (i != null)
				return MascaraUtil.getInstance().calcularNivel(codificacao) >= i;
			else
				return MascaraUtil.getInstance().isUltimoNivel(codificacao);
		}

		return false;
	}	
	
	private List<ExClassificacao> getClassificacoesDoNivel(Integer nivel) {
		List<ExClassificacao> result = new ArrayList<ExClassificacao>();

		// se primeira lista, carrega incondicionalmente
		if (nivel == 0) {
			return ExDao.getInstance().listarExClassificacaoPorNivel(MascaraUtil.getInstance().getMscTodosDoNivel(1));
		}

		// se lista do nível anterior está definido, carrega lista baseando-se
		// na anterior
		if(nivelSelecionado != null && nivelSelecionado.length > (nivel - 1)) {
			String nivelListaAnterior = nivelSelecionado[nivel - 1];
			if (nivelListaAnterior != null && !nivelListaAnterior.equals("-1")) {
				return ExDao.getInstance().listarExClassificacaoPorNivel(
						MascaraUtil.getInstance().getMscFilho(nivelListaAnterior, false), nivelListaAnterior);
			}
		}

		return result;
	}	

}