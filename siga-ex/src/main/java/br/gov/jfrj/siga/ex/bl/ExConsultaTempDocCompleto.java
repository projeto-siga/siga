package br.gov.jfrj.siga.ex.bl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;

public class ExConsultaTempDocCompleto {

	private static ExConsultaTempDocCompleto INSTANCE;
	private ExBL exBL = Ex.getInstance().getBL();
	private ExDao exDao = ExDao.getInstance();

	public static ExConsultaTempDocCompleto getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ExConsultaTempDocCompleto();
		}
		return INSTANCE;
	}

	public boolean podeExibirArvoreDocsCossigRespAss(DpPessoa cadastrante, DpLotacao lotaCadastrante) {
		return Ex.getInstance().getConf().podePorConfiguracao(cadastrante, lotaCadastrante,
				ExTipoDeConfiguracao.EXIBIR_ARVORE_DOCS_SUBSCRITOR_COSSIGNATARIO);
	}

	public boolean possuiInclusaoCossigRespAss(ExDocumento doc) {
		return !doc.getListaCossigRespAssDiffCadastranteDoc().isEmpty();
	}

	public boolean possuiAssinaturaCossigRespAssHoje(ExDocumento doc) {
		return !doc.getListaCossigRespAssDocHoje().isEmpty() ? Boolean.TRUE : Boolean.FALSE;
	}

	private boolean podeAddMovVinculPapelCossigRespAss(ExDocumento docOrigem, ExDocumento docPai,
			DpPessoa dpPessoaResp) {
		// Valida se for usuario externo
		boolean podeAddMov = dpPessoaResp.isUsuarioExterno() ? Boolean.FALSE : Boolean.TRUE;
		if (podeAddMov) {
			// Valida se pessoa ja possui vinculo de Papel Cossig Resp Ass
			podeAddMov = docPai.possuiVinculPapelRevisorCossigRespAss(dpPessoaResp, docOrigem.getMobilGeral())
					? Boolean.FALSE
					: Boolean.TRUE;
		}
		return podeAddMov;
	}
	
	private List<DpPessoa> obtemListaSomenteCossigs(List<DpPessoa> todosCossigSubscDoc, ExDocumento doc) {
		DpPessoa subscritor = null ;
		for (DpPessoa dpPessoa : todosCossigSubscDoc) {
			subscritor = dpPessoa.equivale(doc.getSubscritor()) ? dpPessoa : null;
		}
		if (subscritor != null)
			todosCossigSubscDoc.remove(subscritor);
		return todosCossigSubscDoc;
	}
	
	private DpPessoa obtemSomenteSusbcritor(List<DpPessoa> todosCossigSubscDoc, ExDocumento doc) {
		DpPessoa subscritor = null ;
		for (DpPessoa dpPessoa : todosCossigSubscDoc) {
			subscritor = dpPessoa.equivale(doc.getSubscritor()) ? dpPessoa : null;
		}
		return subscritor;
	}
	
	public void incluirSomenteCossigsAcessoTempArvoreDocs(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc) {
		if (podeExibirArvoreDocsCossigRespAss(cadastrante, lotaCadastrante) && possuiInclusaoCossigRespAss(doc)) {
			List<DpPessoa> todosCossigSubscDoc = obtemListaSomenteCossigs(doc.getListaCossigRespAssDiffCadastranteDoc(), doc);
			
			List<ExDocumento> listaViasDocs = doc.isFinalizado() ? doc.getTodosOsPaisDasViasCossigRespAss() : Arrays.asList(doc);
			incluirDnmAcessoTempArvoreDocsCossigRespAss(cadastrante, lotaCadastrante, doc,
					listaViasDocs, todosCossigSubscDoc, ExPapel.PAPEL_AUTORIZADO_COSSIG);
		}
	}
	
	public void incluirSomenteSubscritorAcessoTempArvoreDocs(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExDocumento doc) {
		if (podeExibirArvoreDocsCossigRespAss(cadastrante, lotaCadastrante) && possuiInclusaoCossigRespAss(doc)) {
			DpPessoa subscritor = obtemSomenteSusbcritor(doc.getListaCossigRespAssDiffCadastranteDoc(), doc);
			
			List<ExDocumento> listaViasDocs = doc.isFinalizado() ? doc.getTodosOsPaisDasViasCossigRespAss() : Arrays.asList(doc);
			incluirDnmAcessoTempArvoreDocsCossigRespAss(cadastrante, lotaCadastrante, doc,
					listaViasDocs, Arrays.asList(subscritor), ExPapel.PAPEL_AUTORIZADO);
		}
	}

//	public void incluirDnmAcessoTempArvoreDocsCossigRespAssFluxoTela(final DpPessoa cadastrante,
//			final DpLotacao lotaCadastrante, final ExDocumento doc, List<DpPessoa> todosCossigSubscDoc, long idExPapel) {
//		if (podeExibirArvoreDocsCossigRespAss(cadastrante, lotaCadastrante) && possuiInclusaoCossigRespAss(doc)) {
//			if (doc.isFinalizado()) {
//				incluirDnmAcessoTempArvoreDocsCossigRespAss(cadastrante, lotaCadastrante, doc,
//						doc.getTodosOsPaisDasViasCossigRespAss(), idExPapel);
//			} else {
//				incluirDnmAcessoTempArvoreDocsCossigRespAss(cadastrante, lotaCadastrante, doc, Arrays.asList(doc), idExPapel);
//			}
//		}
//	}

	public void incluirDnmAcessoTempArvoreDocsCossigRespAss(DpPessoa cadastrante, DpLotacao lotaCadastrante,
			ExDocumento docOrigem, List<ExDocumento> listaViasDocPai, List<DpPessoa> listaCossigDoc, long idExPapel) {
		Date dt = ExDao.getInstance().dt();
		// obter lista cossig/resp ass diferente do user cadastrante doc origem
		//List<DpPessoa> listaCossigDoc = docOrigem.getListaCossigRespAssDiffCadastranteDoc();
		ExPapel exPapel = exDao.consultar(idExPapel, ExPapel.class, false);

		for (ExDocumento docPai : listaViasDocPai) {
			for (DpPessoa dpPessoaResp : listaCossigDoc) {
				if (podeAddMovVinculPapelCossigRespAss(docOrigem, docPai, dpPessoaResp)) {
					StringBuffer descrMov = new StringBuffer(
							"Inclusão de Cossignatário ou Responsável pela Assinatura:")
									.append(dpPessoaResp.getDescricaoIniciaisMaiusculas()).append(" - DOC ORIGEM:")
									.append(docOrigem.getCodigo());
					exBL.vincularPapel(docOrigem.getCadastrante(), docOrigem.getLotaCadastrante(),
							docPai.getMobilGeral(), dt, dpPessoaResp.getLotacao(), dpPessoaResp, null, null,
							descrMov.toString(), null, exPapel, docOrigem.getMobilGeral());
				}
			}
		}
	}

	public void removerDnmAcessoTempArvoreDocsCossigRespAssFluxoTela(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final List<ExMovimentacao> movsCossig, final ExDocumento doc) {
		try {
			for (ExMovimentacao mov : movsCossig) {
				if (podeExibirArvoreDocsCossigRespAss(cadastrante, lotaCadastrante)
						&& ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO.equals(mov.getExTipoMovimentacao())) {
					StringBuffer descrMov = new StringBuffer(
							"Remoção de Cossignatário ou Responsável pela Assinatura concluída:");
					// Caso doc finalizado obtem vias do pai, caso contrario obtem doc atual
					List<ExDocumento> listaViasDocPai = doc.isFinalizado() ? doc.getTodosOsPaisDasViasCossigRespAss()
							: Arrays.asList(doc);
					removerDnmAcessoTempArvoreDocsCossigRespAss(cadastrante, doc, listaViasDocPai,
							Arrays.asList(mov.getSubscritor()), descrMov);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Erro ao excluir movimentação.", e);
		}
	}

	public void removerDnmAcessoTempArvoreDocsCossigRespAssDepoisAssinar(DpPessoa cadastrante,
			DpLotacao lotaCadastrante, ExDocumento doc) throws Exception {
		// obter lista de Cossig/Resp ass que assinaram doc hoje
		List<DpPessoa> listaSubscrCancelMovPapel = doc.getListaCossigRespAssDocHoje();
		StringBuffer descrMov = new StringBuffer(
				"Assinatura de Cossignatário ou Responsável pela Assinatura concluída:");
		// obter Vias do doc pai
		List<ExDocumento> listaViasDocPai = doc.getTodosOsPaisDasViasCossigRespAss();
		removerDnmAcessoTempArvoreDocsCossigRespAss(cadastrante, doc, listaViasDocPai, listaSubscrCancelMovPapel,
				descrMov);
	}

	public void removerDnmAcessoTempArvoreDocsCossigRespAssDocTemp(DpPessoa cadastrante, DpLotacao lotaCadastrante,
			ExDocumento doc) throws Exception {
		// obter lista de Cossig/Resp ass que assinaram doc hoje
		List<DpPessoa> listaSubscrCancelMovPapel = doc.getSubscritorECosignatarios();
		StringBuffer descrMov = new StringBuffer(
				"Remoção de Cossignatário ou Responsável pela Assinatura Documento Temporário concluída:");
		removerDnmAcessoTempArvoreDocsCossigRespAss(cadastrante, doc, Arrays.asList(doc), listaSubscrCancelMovPapel,
				descrMov);
	}

	private void removerDnmAcessoTempArvoreDocsCossigRespAss(DpPessoa cadastrante, ExDocumento docOrigem,
			List<ExDocumento> listaViasDocPai, List<DpPessoa> listaSubscritor, StringBuffer descrMov) throws Exception {
		if (!listaSubscritor.isEmpty()) {
			// obter movs de inclusao de Papel cossig ou resp assinatura doc Pai
			List<ExMovimentacao> movsPai = listaViasDocPai.iterator().next()
					.getMovsVinculacaoPapelCossigRespAssinatura();
			// obter movs do pai que possam movs de inclusao de Papel cossig ou resp
			List<ExMovimentacao> movsCossigResp = obterMovsPaiComCossigRespAssDocsFilhos(movsPai,
					Arrays.asList(docOrigem));
			String codDocOrigem = docOrigem.getCodigo();
			// Remover de todas as vias doc pai
			for (ExDocumento docPai : listaViasDocPai) {
				for (DpPessoa subscritor : listaSubscritor) {
					descrMov.append(subscritor.getDescricaoIniciaisMaiusculas()).append(" - DOC ORIGEM:")
							.append(codDocOrigem);
					List<ExMovimentacao> movsPersist = getMovsCossigRespAssPorDocOrigem(movsCossigResp, subscritor,
							docOrigem.getMobilGeral());
					exBL.removerPapel(docPai, movsPersist, ExPapel.PAPEL_AUTORIZADO_COSSIG, cadastrante, descrMov.toString());
				}
			}
		}
	}

	public void tratarFluxoJuntarArvoreDocsCossigRespAss(ExMobil mob, DpPessoa cadastrante, DpLotacao lotaCadastrante)
			throws Exception {
		ExDocumento docAtual = mob.doc();
		// obter Vias do doc pai
		List<ExDocumento> viasDocPai = docAtual.getTodosOsPaisDasViasCossigRespAss();
		// obter movs de inclusao de Papel cossig ou resp assinatura doc Pai
		List<ExMovimentacao> movsCossigResp = docAtual.getMovsVinculacaoPapelCossigRespAssinatura();

		if (!viasDocPai.isEmpty() && movsCossigResp.iterator().hasNext()) {
			Date dt = ExDao.getInstance().dt();
			ExPapel exPapel = exDao.consultar(ExPapel.PAPEL_AUTORIZADO_COSSIG, ExPapel.class, false);
			for (ExMovimentacao movCossig : movsCossigResp) {
				DpPessoa subscritorTemp = movCossig.getSubscritor();
				ExMobil mobRefMov = movCossig.getExMobilRef();
				String codDocOrigemTemp = mobRefMov.getDoc().getCodigo();
				// Cria Descricoes Movs Remocao e Inclusao
				String descrMovRemocao = getDescricaoMovPapelMontada("Juntada", docAtual, subscritorTemp,
						codDocOrigemTemp, "Remoção");
				String descrMovInsercao = getDescricaoMovPapelMontada("Juntada", docAtual, subscritorTemp,
						codDocOrigemTemp, "Inclusão");
				// Movimentos de Cossig/Resp Assinatura Por Doc de Origem
				List<ExMovimentacao> movsPersist = getMovsCossigRespAssPorDocOrigem(Arrays.asList(movCossig),
						subscritorTemp, mobRefMov);
				for (ExDocumento docVia : viasDocPai) {
					// Remove papel do docAtual
					exBL.removerPapel(docAtual, movsPersist, ExPapel.PAPEL_AUTORIZADO_COSSIG, cadastrante,
							descrMovRemocao.toString());
					// Inclui papel das vias do novo docPai
					exBL.vincularPapel(cadastrante, lotaCadastrante, docVia.getMobilGeral(), dt,
							subscritorTemp.getLotacao(), subscritorTemp, null, null, descrMovInsercao.toString(), null,
							exPapel, mobRefMov);
				}
			}
		}
	}

	public void tratarFluxoDesentrDesfJuntadaArvoreDocsCossigRespAss(ExMobil mob, DpPessoa cadastrante,
			DpLotacao lotaCadastrante) throws Exception {
		ExDocumento docAtual = mob.doc();
		List<ExDocumento> viasDocPai = docAtual.getTodosOsPaisDasViasCossigRespAss();

		if (!viasDocPai.isEmpty()) {
			// Movimentacoes de inclusao de Papel cossig ou resp assinatura doc Pai
			List<ExMovimentacao> movsPaiTemp = viasDocPai.iterator().next()
					.getMovsVinculacaoPapelCossigRespAssinatura();
			if (movsPaiTemp.iterator().hasNext()) {
				// Obtem Movs Cossig/Resp Ass validos para Desentranhamento/Desfazer Juntada
				List<ExMovimentacao> movsCossigResp = obterMovsCossigRespAssValidosDesentrDesfJuntada(mob, movsPaiTemp);
				Date dt = ExDao.getInstance().dt();
				ExPapel exPapel = exDao.consultar(ExPapel.PAPEL_AUTORIZADO_COSSIG, ExPapel.class, false);
				for (ExMovimentacao movCossig : movsCossigResp) {
					DpPessoa subscritorTemp = movCossig.getSubscritor();
					ExMobil mobRefMov = movCossig.getExMobilRef();
					String codDocOrigemTemp = mobRefMov.getDoc().getCodigo();
					// Cria Descricoes Movs Remocao e Inclusao
					String descrMovRemocao = getDescricaoMovPapelMontada("Desentranhamento", docAtual, subscritorTemp,
							codDocOrigemTemp, "Remoção");
					String descrMovInsercao = getDescricaoMovPapelMontada("Desentranhamento", docAtual, subscritorTemp,
							codDocOrigemTemp, "Inclusão");
					// Movimentos de Cossig/Resp Assinatura Por Doc de Origem
					List<ExMovimentacao> movsPersist = getMovsCossigRespAssPorDocOrigem(Arrays.asList(movCossig),
							subscritorTemp, mobRefMov);
					for (ExDocumento docVia : viasDocPai) {
						// Remove papel das vias do docPai
						exBL.removerPapel(docVia, movsPersist, ExPapel.PAPEL_AUTORIZADO_COSSIG, cadastrante, descrMovRemocao);
						// Inclui papel das vias do novo docPai
						exBL.vincularPapel(cadastrante, lotaCadastrante, docAtual.getMobilGeral(), dt,
								subscritorTemp.getLotacao(), subscritorTemp, null, null, descrMovInsercao, null,
								exPapel, mobRefMov);
					}
				}
			}
		}
	}

	private List<ExMovimentacao> obterMovsCossigRespAssValidosDesentrDesfJuntada(ExMobil mob,
			List<ExMovimentacao> movsPai) {
		// Achar todos docs filhos do Doc que foi Desentranhado
		List<ExDocumento> listaMobilsFilhos = new ArrayList<>(mob.doc().getTodosDocumentosFilhosSet());
		return obterMovsPaiComCossigRespAssDocsFilhos(movsPai, listaMobilsFilhos);
	}

	private List<ExMovimentacao> obterMovsPaiComCossigRespAssDocsFilhos(List<ExMovimentacao> movsPai,
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

	private List<ExMovimentacao> getMovsCossigRespAssPorDocOrigem(List<ExMovimentacao> movs, DpPessoa subscritor,
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

}
