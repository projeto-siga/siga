package br.gov.jfrj.siga.ex.bl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;

public class ExVisualizacaoTempDocCompl {

	private static long PAPEL_AUTORIZ_SUBSCR = ExPapel.PAPEL_AUTORIZADO;
	private static long PAPEL_AUTORIZ_COSSIG = ExPapel.PAPEL_AUTORIZADO_COSSIG;
	
	private static ExVisualizacaoTempDocCompl INSTANCE;
	private ExBL exBL = Ex.getInstance().getBL();
	private ExDao exDao = ExDao.getInstance();
	
	public static ExVisualizacaoTempDocCompl getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ExVisualizacaoTempDocCompl();
		}
		return INSTANCE;
	}
	
	/**
	 * Metodo responsavel por verificar se pode habilitar acesso temporario a arvore completa de documentos 
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @return
	 */
	public boolean podeVisualizarTempDocComplCossigsSubscritor(DpPessoa cadastrante, DpLotacao lotaCadastrante) {
		return Ex.getInstance().getConf().podePorConfiguracao(cadastrante, lotaCadastrante,
												ExTipoDeConfiguracao.VISUALIZAR_TEMP_DOCS_COMPL_SUBSCRITOR_COSSIGNATARIO);
	}
	
	/**
	 * Metodo que verifica se pode exbir o checkbox pode ser exibido nas telas de cossignatarios e alteracao de subscritor
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param doc
	 * @return
	 */
	public boolean podeExibirCheckBoxVisTempDocsComplCossigsSubscritor(DpPessoa cadastrante, DpLotacao lotaCadastrante, ExDocumento doc) {
		boolean podeExibir =  Ex.getInstance().getConf().podePorConfiguracao(cadastrante, lotaCadastrante,
												ExTipoDeConfiguracao.VISUALIZAR_TEMP_DOCS_COMPL_SUBSCRITOR_COSSIGNATARIO);
		if (podeExibir) {
			ExDocumento docPai = getPaiDasViasCossigsSubscritor(doc);
			return (docPai != null && docPai.getIdDoc() != null && !docPai.equals(doc)) ? Boolean.TRUE : Boolean.FALSE;
		}
		return Boolean.FALSE;
	}
	
	public ExDocumento getPaiDasViasCossigsSubscritor(ExDocumento docFilho) {
		if (docFilho != null) {
			List<ExDocumento> viasDocPai = docFilho.getTodosOsPaisDasViasCossigsSubscritor();
			if (viasDocPai.iterator().hasNext()) 
				return viasDocPai.iterator().next();			
		}
		return null;
	}

	public boolean possuiInclusaoCossigsSubscritor(ExDocumento doc) {
		return !doc.getSubscritorECosignatarios().isEmpty();
	}

	public boolean possuiAssinaturaCossigsSubscritorHoje(ExDocumento doc) {
		return !doc.getListaCossigsSubscritorAssinouDocHoje().isEmpty() ? Boolean.TRUE : Boolean.FALSE;
	}
	
	/**
	 * Incluir Cossignatarios e Subscritor para acesso temporario a arvore de documentos completo
	 * Fluxo de finalização do documento
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param doc
	 */
	public void incluirCossigsSubscrVisTempDocsComplFluxoFinalizar (
							final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExDocumento doc) {
		//Flag Boolean False usada para forcar busca de movs PAPEL_AUTORIZADO_COSSIG
		incluirCossigsVisTempDocsCompl(cadastrante, lotaCadastrante, doc, Boolean.FALSE, Boolean.FALSE);
		//Flag Boolean False usada para forcar busca de movs PAPEL_AUTORIZADO
		incluirSubscritorVisTempDocsCompl(cadastrante, lotaCadastrante, doc, Boolean.FALSE, Boolean.FALSE);
	}
	
	/**
	 * Incluir Apenas Cossignatarios para acesso temporario a arvore de documentos completo
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param doc
	 * @param podeIncluirSubscr
	 */
	public void incluirCossigsVisTempDocsCompl(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, 
							final ExDocumento doc, boolean podeIncluirSubscr, boolean fluxoInclusao) {
		if (podeVisualizarTempDocComplCossigsSubscritor(cadastrante, lotaCadastrante))	{
			//obtem todos os cossigs para inclusao de mov
			List<DpPessoa> cossigs = doc.getCosignatarios();
			if (cossigs != null) {
				//Verificacao se pode incluir mov
				if (podeIncluirCossigSubscrVisTempDocsCompl(doc, podeIncluirSubscr, PAPEL_AUTORIZ_COSSIG)) {
					// Valida se for usuario externo
					if (fluxoInclusao)
						validarExistenciaUsuarioExterno(cossigs);
					//obtem vias doc ao qual deve se criar mov
					List<ExDocumento> listaViasDocs = doc.isFinalizado() ? doc.getTodosOsPaisDasViasCossigsSubscritor() : Arrays.asList(doc);
					incluirCossigsSubscritorVisTempDocsCompl(cadastrante, lotaCadastrante, doc,
														listaViasDocs, cossigs, PAPEL_AUTORIZ_COSSIG);
				}
			}
		}
	}
	
	/**
	 * Incluir Apenas Subscritor para acesso temporario a arvore de documentos completo
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param doc
	 * @param podeIncluirSubscr
	 */
	public void incluirSubscritorVisTempDocsCompl(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc, boolean podeIncluirSubscr, boolean fluxoInclusao) {

		if (podeVisualizarTempDocComplCossigsSubscritor(cadastrante, lotaCadastrante)) {
			DpPessoa subscritor = doc.getSubscritorDiffTitularDoc();
			if(subscritor != null) {
				//Verificacao se pode incluir mov
				if (podeIncluirCossigSubscrVisTempDocsCompl(doc, podeIncluirSubscr, PAPEL_AUTORIZ_SUBSCR)) {
					
					// Valida se for usuario externo
					if (fluxoInclusao)
						validarExistenciaUsuarioExterno(Arrays.asList(subscritor));
					//obtem vias doc ao qual deve se criar mov
					List<ExDocumento> listaViasDocs = doc.isFinalizado() ? doc.getTodosOsPaisDasViasCossigsSubscritor() : Arrays.asList(doc);
					
					incluirCossigsSubscritorVisTempDocsCompl(cadastrante, lotaCadastrante, doc, listaViasDocs, Arrays.asList(subscritor), PAPEL_AUTORIZ_SUBSCR);
				}
			}
		}
	}

	private void incluirCossigsSubscritorVisTempDocsCompl(DpPessoa cadastrante, DpLotacao lotaCadastrante,
			ExDocumento docOrigem, List<ExDocumento> listaViasDocPai, List<DpPessoa> listaCossigDoc, long codigoPapel) {
		if (!listasVazias(listaViasDocPai, listaCossigDoc)) {
			Date dt = ExDao.getInstance().dt();
			ExPapel exPapel = exDao.consultar(codigoPapel, ExPapel.class, false);		
			for (ExDocumento docPai : listaViasDocPai) {
				for (DpPessoa dpPessoaCossig : listaCossigDoc) {
					if (podeAddMovVinculPapelCossigsSubscritor(docOrigem, docPai, dpPessoaCossig, codigoPapel)) {
						StringBuffer descrMov = new StringBuffer("Inclusão de ")
										.append(getNomeSubscritorOuCossignatario(codigoPapel) + ":")
										.append(dpPessoaCossig.getDescricaoIniciaisMaiusculas()).append(" - DOC ORIGEM:")
										.append(docOrigem.getCodigo());
						exBL.vincularPapel(cadastrante, lotaCadastrante, docPai.getMobilGeral(), dt, dpPessoaCossig.getLotacao(), 
								dpPessoaCossig, docOrigem.getSubscritor(), docOrigem.getTitular(), descrMov.toString(), null, exPapel, docOrigem.getMobilGeral());
					}
				}
			}
		}
	}
	
	/**
	 * Remover Apenas Cossignatarios para acesso temporario a arvore de documentos completo Fluxo Depois de Assinar
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param movsCossig
	 * @param doc
	 */
	public void removerCossigsVisTempDocsComplFluxoTelaCossignatarios(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final List<ExMovimentacao> movsCossig, final ExDocumento doc) {
		try {
			for (ExMovimentacao mov : movsCossig) {
				if (podeVisualizarTempDocComplCossigsSubscritor(cadastrante, lotaCadastrante)
						&& ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO.equals(mov.getExTipoMovimentacao())) {
					StringBuffer descrMov = new StringBuffer("Remoção de ")
										.append(getNomeSubscritorOuCossignatario(PAPEL_AUTORIZ_COSSIG))
										.append(" concluída:");
					// Caso doc finalizado obtem vias do pai, caso contrario obtem doc atual
					List<ExDocumento> listaViasDocPai = doc.isFinalizado() ? doc.getTodosOsPaisDasViasCossigsSubscritor() : Arrays.asList(doc);
					List<ExMovimentacao> movsCossigResp = getMovsPaiReferenciadosDocsFilho(listaViasDocPai, doc, PAPEL_AUTORIZ_COSSIG);
					if (mov.getSubscritor() != null)
						removerCossigsSubscritorVisTempDocsCompl(
										cadastrante, doc, listaViasDocPai, Arrays.asList(mov.getSubscritor()), movsCossigResp, descrMov);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Erro ao excluir movimentação.", e);
		}
	}

	/**
	 * Remover Subscritor e/Ou Cossignatarios para acesso temporario a arvore de documentos completo Fluxo Depois de Assinar
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param doc
	 * @throws Exception
	 */
	public void removerCossigsSubscritorVisTempDocsComplFluxoDepoisAssinar(DpPessoa cadastrante,
			DpLotacao lotaCadastrante, DpPessoa usuarioDoToken, ExDocumento doc) throws Exception {
		// obter lista de Cossig/Resp ass que assinaram doc hoje
		DpPessoa subscrAssinante = getUsuarioDoTokenPosAssinatura(doc.getListaCossigsSubscritorAssinouDocHoje(), usuarioDoToken);
		if (subscrAssinante != null) {
			List<ExDocumento> listaViasDocPai = doc.getTodosOsPaisDasViasCossigsSubscritor();
			removerCossigsESubscritorVisTempDocsCompl(cadastrante, lotaCadastrante, doc, listaViasDocPai, Arrays.asList(subscrAssinante), getTextoRemocaoTempAssinatura());
		}
	}
	
	private DpPessoa getUsuarioDoTokenPosAssinatura(List<DpPessoa> listaSubscrCancelMovPapel, DpPessoa usuarioDoToken) {
		for (DpPessoa dpPessoa : listaSubscrCancelMovPapel) {
			if (dpPessoa.equivale(usuarioDoToken))
				return dpPessoa;
		}
		return null;
	}
	
	/**
	 * Remover Subscritor e/Ou Cossignatarios para acesso temporario a arvore de documentos completo 
	 * Fluxos Refazer | Cancelar e Excluir Documentos
	 * 
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param doc
	 * @throws Exception
	 */
	public void removerCossigsSubscritorVisTempDocsComplFluxosRefazerCancelarExcluirDoc(DpPessoa cadastrante,
			DpLotacao lotaCadastrante, ExDocumento doc) throws Exception {
		List<DpPessoa> listaSubscrCancelMovPapel = doc.getSubscritorECosignatarios();
		if	(!listaSubscrCancelMovPapel.isEmpty()) {
			removerSubscrCossigsVisTempDocsComplDocAtual(cadastrante, lotaCadastrante, doc, listaSubscrCancelMovPapel);
			removerSubscrCossigsVisTempDocsComplDocPai(cadastrante, lotaCadastrante, doc, listaSubscrCancelMovPapel);
		}
	}

	/**
	 * Remover Subscritor e/Ou Cossignatarios para acesso temporario a arvore de documentos completo Fluxo Finalizar
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param doc
	 * @throws Exception
	 */
	public void removerCossigsSubscrVisTempDocsComplFluxoFinalizar(DpPessoa cadastrante, DpLotacao lotaCadastrante,
			ExDocumento doc) throws Exception {
		List<DpPessoa> listaSubscrCossigs = doc.getCosignatarios();
		listaSubscrCossigs.add(doc.getSubscritor());
		removerSubscrCossigsVisTempDocsComplDocAtual(cadastrante, lotaCadastrante, doc, listaSubscrCossigs);
	}
	
	/**
	 * Remover Apenas Subscritor para acesso temporario a arvore de documentos completo Fluxo Gravar
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param doc
	 * @throws Exception
	 */
	public void removerSubscrVisTempDocsComplFluxoGravar(DpPessoa cadastrante, DpLotacao lotaCadastrante,
			ExDocumento doc) throws Exception {
		List<ExDocumento> listaViasDoc = doc.isFinalizado() ? doc.getTodosOsPaisDasViasCossigsSubscritor() : Arrays.asList(doc);
		List<ExMovimentacao> movsSubscr = getMovsPaiReferenciadosDocsFilho(listaViasDoc, doc, PAPEL_AUTORIZ_SUBSCR);
		if(!listasVazias(movsSubscr)) {
			DpPessoa subscr = movsSubscr.iterator().next().getSubscritor();
			if (subscr != null) {
				if (doc.isFinalizado())
					removerSubscrCossigsVisTempDocsComplDocPai(cadastrante, lotaCadastrante, doc, Arrays.asList(subscr));
				else 
					removerSubscrCossigsVisTempDocsComplDocAtual(cadastrante, lotaCadastrante, doc, Arrays.asList(subscr));
			}
		}
	}
	
	public void tratarFluxoJuntarVisTempDocsCompl(ExMobil mob, DpPessoa cadastrante, DpLotacao lotaCadastrante)
			throws Exception {
		ExDocumento docAtual = mob.doc();
		// obter Vias do doc pai
		List<ExDocumento> viasDocPai = docAtual.getTodosOsPaisDasViasCossigsSubscritor();
		// obter movs de inclusao de Papel cossig ou resp assinatura doc Pai
		List<ExMovimentacao> movsCossigResp = new ArrayList<>();
		movsCossigResp.addAll(docAtual.getMovsVinculacaoPapelGenerico(PAPEL_AUTORIZ_COSSIG));
		movsCossigResp.addAll(docAtual.getMovsVinculacaoPapelGenerico(PAPEL_AUTORIZ_SUBSCR));

		if (!viasDocPai.isEmpty() && movsCossigResp.iterator().hasNext()) {
			Date dt = ExDao.getInstance().dt();
			for (ExMovimentacao movCossig : movsCossigResp) {
				DpPessoa subscritorTemp = movCossig.getSubscritor();
				ExMobil mobRefMov = movCossig.getExMobilRef();
				String codDocOrigemTemp = mobRefMov.getDoc().getCodigo();
				
				ExPapel exPapel = exDao.consultar(movCossig.getExPapel().getIdPapel(), ExPapel.class, false);
				// Cria Descricoes Movs Remocao e Inclusao
				String descrMovRemocao = getDescricaoMovPapelMontada("Juntada", docAtual, subscritorTemp, codDocOrigemTemp, "Remoção");
				String descrMovInsercao = getDescricaoMovPapelMontada("Juntada", docAtual, subscritorTemp, codDocOrigemTemp, "Inclusão");
				// Movimentos de Cossig/Resp Assinatura Por Doc de Origem
				List<ExMovimentacao> movsPersist = getMovsCossigsSubscritorPorDocOrigem(Arrays.asList(movCossig),
						subscritorTemp, mobRefMov);
				for (ExDocumento docVia : viasDocPai) {
					removerIncluirPapelCossigsSubscritor(docAtual, movsPersist, exPapel, cadastrante, 
							descrMovRemocao, lotaCadastrante, docVia.getMobilGeral(), dt, subscritorTemp.getLotacao(), 
							subscritorTemp, descrMovInsercao, mobRefMov);
				}
			}
		}
	}
	
	public void tratarFluxoDesentrDesfJuntadaVisTempDocsCompl(ExMobil mob, DpPessoa cadastrante,
			DpLotacao lotaCadastrante) throws Exception {
		ExDocumento docAtual = mob.doc();
		List<ExDocumento> viasDocPai = docAtual.getTodosOsPaisDasViasCossigsSubscritor();

		if (!viasDocPai.isEmpty()) {
			// Movimentacoes de inclusao de Papel cossig ou resp assinatura doc Pai
			List<ExMovimentacao> movsPaiTemp = new ArrayList<>();
			movsPaiTemp.addAll(viasDocPai.iterator().next().getMovsVinculacaoPapelGenerico(PAPEL_AUTORIZ_COSSIG));
			movsPaiTemp.addAll(viasDocPai.iterator().next().getMovsVinculacaoPapelGenerico(PAPEL_AUTORIZ_SUBSCR));
			
			if (movsPaiTemp.iterator().hasNext()) {
				// Obtem Movs Cossig/Resp Ass validos para Desentranhamento/Desfazer Juntada
				List<ExMovimentacao> movsCossigResp = obterMovsCossigsSubscritorValidosDesentrDesfJuntada(mob, movsPaiTemp);
				Date dt = ExDao.getInstance().dt();
				
				for (ExMovimentacao movCossig : movsCossigResp) {
					DpPessoa subscritorTemp = movCossig.getSubscritor();
					ExMobil mobRefMov = movCossig.getExMobilRef();
					String codDocOrigemTemp = mobRefMov.getDoc().getCodigo();
					
					ExPapel exPapel = exDao.consultar(movCossig.getExPapel().getIdPapel(), ExPapel.class, false);
					
					// Cria Descricoes Movs Remocao e Inclusao
					String descrMovRemocao = getDescricaoMovPapelMontada("Desentranhamento", docAtual, subscritorTemp,
							codDocOrigemTemp, "Remoção");
					String descrMovInsercao = getDescricaoMovPapelMontada("Desentranhamento", docAtual, subscritorTemp,
							codDocOrigemTemp, "Inclusão");
					// Movimentos de Cossig/Resp Assinatura Por Doc de Origem
					List<ExMovimentacao> movsPersist = getMovsCossigsSubscritorPorDocOrigem(Arrays.asList(movCossig),
							subscritorTemp, mobRefMov);
					for (ExDocumento docVia : viasDocPai) {
						removerIncluirPapelCossigsSubscritor(docVia, movsPersist, exPapel, cadastrante, descrMovRemocao, 
								lotaCadastrante, docAtual.getMobilGeral(), dt, subscritorTemp.getLotacao(), subscritorTemp, 
								descrMovInsercao, mobRefMov);
					}
				}
			}
		}
	}
	
	private void removerIncluirPapelCossigsSubscritor(ExDocumento doc, List<ExMovimentacao> movsPersist,
			ExPapel exPapel, DpPessoa cadastrante, String descrMovRemocao, DpLotacao lotaCadastrante, ExMobil mobil,
			Date dt, DpLotacao lotaSubscr, DpPessoa subscritorTemp, String descrMovInsercao, ExMobil mobRefMov)
			throws Exception {
		// Remove papel do docAtual
		exBL.removerPapel(doc, movsPersist, exPapel.getIdPapel(), cadastrante, descrMovRemocao);
		// Inclui papel das vias do novo docPai
		exBL.vincularPapel(cadastrante, lotaCadastrante, mobil, dt, lotaSubscr, subscritorTemp, null, null,
				descrMovInsercao, null, exPapel, mobRefMov);
	}
	
	private void removerSubscrCossigsVisTempDocsComplDocAtual(DpPessoa cadastrante, DpLotacao lotaCadastrante,
			ExDocumento doc, List<DpPessoa> listaSubscrCancelMovPapel) throws Exception {
		List<ExDocumento> listaViasDoc = Arrays.asList(doc);
		removerCossigsESubscritorVisTempDocsCompl(cadastrante, lotaCadastrante, doc, listaViasDoc, listaSubscrCancelMovPapel, getTextoRemocaoTemp());
	}
	
	private void removerSubscrCossigsVisTempDocsComplDocPai(DpPessoa cadastrante, DpLotacao lotaCadastrante,
			ExDocumento doc, List<DpPessoa> listaSubscrCancelMovPapel) throws Exception {
		List<ExDocumento> listaViasDoc = doc.getTodosOsPaisDasViasCossigsSubscritor();
		removerCossigsESubscritorVisTempDocsCompl(cadastrante, lotaCadastrante, doc, listaViasDoc, listaSubscrCancelMovPapel, getTextoRemocaoTemp());
	}
	
	private void removerCossigsESubscritorVisTempDocsCompl(DpPessoa cadastrante,
			DpLotacao lotaCadastrante, ExDocumento doc, List<ExDocumento> listaViasDocPai, List<DpPessoa> listaSubscrCancelMovPapel, StringBuffer descrMov) throws Exception {
		List<ExMovimentacao> movsCossigs = getMovsPaiReferenciadosDocsFilho(listaViasDocPai, doc, PAPEL_AUTORIZ_COSSIG);
		List<ExMovimentacao> movsSubscr = getMovsPaiReferenciadosDocsFilho(listaViasDocPai, doc, PAPEL_AUTORIZ_SUBSCR);
		removerCossigsSubscritorVisTempDocsCompl(cadastrante, doc, listaViasDocPai, listaSubscrCancelMovPapel,
				movsCossigs, descrMov);
		removerCossigsSubscritorVisTempDocsCompl(cadastrante, doc, listaViasDocPai, listaSubscrCancelMovPapel,
				movsSubscr, descrMov);
	}

	private void removerCossigsSubscritorVisTempDocsCompl(DpPessoa cadastrante, ExDocumento docOrigem,
			List<ExDocumento> listaViasDocPai, List<DpPessoa> listaSubscritor, List<ExMovimentacao> movsCossigResp, StringBuffer descrMov) throws Exception {
		if (!listasVazias(listaViasDocPai, listaSubscritor, movsCossigResp)) {
			long codigoPapel = movsCossigResp.iterator().next().getExPapel().getIdPapel();
			String codDocOrigem = docOrigem.getCodigo();
			// Remover de todas as vias doc pai
			for (ExDocumento docPai : listaViasDocPai) {
				for (DpPessoa subscritor : listaSubscritor) {
					String strMov = descrMov.toString() + " " + subscritor.getDescricaoIniciaisMaiusculas() 
									+ " - DOC ORIGEM:" + codDocOrigem;
					List<ExMovimentacao> movsPersist = getMovsCossigsSubscritorPorDocOrigem(movsCossigResp, subscritor,
							docOrigem.getMobilGeral());
					exBL.removerPapel(docPai, movsPersist, codigoPapel, cadastrante, strMov);
				}
			}
		}
	}
	
	private boolean podeIncluirCossigSubscrVisTempDocsCompl(final ExDocumento doc, boolean checkBoxIncluirSubscr, long codigoPapel) {
		//Caso flag false, sera realizada busca de movs doc pai ou doc atual
		if (!checkBoxIncluirSubscr) 			
			checkBoxIncluirSubscr = doc.isFinalizado() ? doc.possuiMovsVinculacaoPapel(codigoPapel) :
					doc.paiPossuiMovsVinculacaoPapel(codigoPapel);
		return checkBoxIncluirSubscr;
	}
	
	private List<ExMovimentacao> getMovsPaiReferenciadosDocsFilho(List<ExDocumento> listaViasDocPai, ExDocumento doc,
			long codigoPapel) {
		if (!listasVazias(listaViasDocPai)) {
			// obter movs de inclusao de Papel cossig ou resp assinatura doc Pai
			List<ExMovimentacao> movsPai = listaViasDocPai.iterator().next().getMovsVinculacaoPapelGenerico(codigoPapel);
			// obter movs do pai que possam movs de inclusao de Papel cossig ou resp
			return obterMovsPaiReferenciadosDocsFilho(movsPai, Arrays.asList(doc));
		}
		return null;
	}

	private List<ExMovimentacao> obterMovsCossigsSubscritorValidosDesentrDesfJuntada(ExMobil mob,
			List<ExMovimentacao> movsPai) {
		// Achar todos docs filhos do Doc que foi Desentranhado
		List<ExDocumento> listaMobilsFilhos = new ArrayList<>(mob.doc().getTodosDocumentosFilhosSet());
		return obterMovsPaiReferenciadosDocsFilho(movsPai, listaMobilsFilhos);
	}

	private List<ExMovimentacao> obterMovsPaiReferenciadosDocsFilho(List<ExMovimentacao> movsPai,
			List<ExDocumento> docsFilhosCossigResp) {
		List<ExMovimentacao> listaMovCossigResp = new ArrayList<>();
		for (ExMovimentacao movPai : movsPai) {
			// Obter Codigo do Doc persistido no campo ExMobilRef
			ExMobil docVia = movPai.getExMobilRef();
			if (docVia != null) {
				for (ExDocumento exDoc : docsFilhosCossigResp) {
					if (docVia.doc().getCodigo().equals(exDoc.getCodigo()))
						listaMovCossigResp.add(movPai);
				}
			}
		}
		return listaMovCossigResp.stream().distinct().collect(Collectors.toList());
	}

	private String getDescricaoMovPapelMontada(String tituloDesc, ExDocumento docAtual, DpPessoa substritor,
			String codDocOrigem, String nomeMov) {
		StringBuffer descrMovInsercao = new StringBuffer(tituloDesc).append(" de Documento:").append(docAtual)
				.append(" - ").append(nomeMov).append(" de Cossignatário ou Responsável pela Assinatura:")
				.append(substritor.getDescricaoIniciaisMaiusculas()).append(" - DOC ORIGEM:").append(codDocOrigem);
		return descrMovInsercao.toString();
	}
	
	private List<ExMovimentacao> getMovsCossigsSubscritorPorDocOrigem(List<ExMovimentacao> movs, DpPessoa subscritor,
			ExMobil mobRefMov) {
		List<ExMovimentacao> movsPessoa = new ArrayList<ExMovimentacao>();
		for (ExMovimentacao mov : movs) {
			if (!mov.isCancelada() && mov.getSubscritor().equivale(subscritor)
					&& mov.getExMobilRef().equals(mobRefMov)) {
				movsPessoa.add(mov);
			}
		}
		return movsPessoa;
	}
	
	private boolean podeAddMovVinculPapelCossigsSubscritor(ExDocumento docOrigem, ExDocumento docPai,
			DpPessoa dpPessoaResp, long codigoPapel) {
		// Valida se for usuario externo
		boolean podeAddMov = dpPessoaResp.isUsuarioExterno() ? Boolean.FALSE : Boolean.TRUE;
		if (podeAddMov) {
			// Valida se pessoa ja possui vinculo de Papel Cossig Resp Ass
			podeAddMov = docPai.possuiVinculPapelRevisorCossigsSubscritor(dpPessoaResp, docOrigem.getMobilGeral(), codigoPapel)
					? Boolean.FALSE
					: Boolean.TRUE;
		}
		return podeAddMov;
	}
	
	private void validarExistenciaUsuarioExterno(List<DpPessoa> listaCossigDoc) {
		for (DpPessoa dpPessoaCossig : listaCossigDoc) {
			if (dpPessoaCossig.isUsuarioExterno())
				throw new RegraNegocioException("Atenção: Usuários Externos não podem acessar Documentos Completos.");
		}
	}

	private boolean listasVazias(List<?> ...listas) {
		for (List<?> list : listas) { 
			if (CollectionUtils.isEmpty(list))
				return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	private static String getNomeSubscritorOuCossignatario(long idPapel) {
		if (idPapel == PAPEL_AUTORIZ_SUBSCR)
			return "Responsável pela Assinatura";
		if (idPapel == PAPEL_AUTORIZ_COSSIG)
			return "Cossignatário";
		return "";
	}
	
	private StringBuffer getTextoRemocaoTemp() {
		return new StringBuffer("Remoção de Cossignatário ou Responsável pela Assinatura Documento Temporário concluída:");
	}
	
	private StringBuffer getTextoRemocaoTempAssinatura() {
		return new StringBuffer("Assinatura de Cossignatário ou Responsável pela Assinatura concluída:");
	}
}
