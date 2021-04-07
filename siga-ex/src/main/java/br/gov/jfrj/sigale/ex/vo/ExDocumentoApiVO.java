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
package br.gov.jfrj.sigale.ex.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExCompetenciaBL;
import br.gov.jfrj.siga.ex.logic.ExPodeMarcar;
import br.gov.jfrj.siga.ex.util.ExGraphColaboracao;
import br.gov.jfrj.siga.ex.util.ExGraphRelacaoDocs;
import br.gov.jfrj.siga.ex.util.ExGraphTramitacao;

public class ExDocumentoApiVO extends ExApiVO {
	String classe;
	List<ExMobilApiVO> mobs = new ArrayList<ExMobilApiVO>();
	List<ExDocumentoApiVO> documentosPublicados = new ArrayList<ExDocumentoApiVO>();
	ExDocumentoApiVO boletim;
	// Map<ExMobil, Set<ExMarca>> marcasPorMobil = new LinkedHashMap<ExMobil,
	// Set<ExMarca>>();
	String outrosMobsLabel;
	String nomeCompleto;
	String dtDocDDMMYY;
	String subscritorString;
	String originalNumero;
	String originalData;
	String originalOrgao;
	String classificacaoDescricaoCompleta;
	List<String> tags;
	String destinatarioString;
	String descrDocumento;
	String nmNivelAcesso;
	String paiSigla;
	String tipoDocumento;

	String dtFinalizacao;
	String nmArqMod;
	String conteudoBlobHtmlString;
	String sigla;
	String fisicoOuEletronico;
	boolean fDigital;
	Map<ExMovimentacaoApiVO, Boolean> cossignatarios = new HashMap<ExMovimentacaoApiVO, Boolean>();
	String dadosComplementares;
	String forma;
	String modelo;
	String tipoFormaDocumento;
	String cadastranteString;
	String lotaCadastranteString;
	String dotTramitacao;
	String dotRelacaoDocs;
	String dotColaboracao;
	boolean podeAssinar;
	String exTipoDocumentoDescricao;

	// Desabilitado temporariamente
	// private List<Object> listaDeAcessos;

	public ExDocumentoApiVO(ExDocumento doc, ExMobil mob, DpPessoa cadastrante, DpPessoa titular, DpLotacao lotaTitular,
			boolean completo, boolean exibirAntigo) {
		this.sigla = doc.getSigla();
		this.descrDocumento = doc.getDescrDocumento();

		this.exTipoDocumentoDescricao = doc.getExTipoDocumento().getDescricao();

		if (!completo)
			return;

		this.nomeCompleto = doc.getNomeCompleto();
		this.dtDocDDMMYY = doc.getDtDocDDMMYY();
		this.subscritorString = doc.getSubscritorString();
		this.cadastranteString = doc.getCadastranteString();
		if (doc.getLotaCadastrante() != null)
			this.lotaCadastranteString = "(" + doc.getLotaCadastrante().getSigla() + ")";
		else
			this.lotaCadastranteString = "";

		if (doc.getExClassificacaoAtual() != null)
			this.classificacaoDescricaoCompleta = doc.getExClassificacaoAtual().getAtual().getDescricaoCompleta();
		this.destinatarioString = doc.getDestinatarioString();
		if (doc.getExNivelAcessoAtual() != null)
			this.nmNivelAcesso = doc.getExNivelAcessoAtual().getNmNivelAcesso();
		// Desabilitado temporariamente
		// this.listaDeAcessos = doc.getListaDeAcessos();
		if (doc.getExMobilPai() != null)
			this.paiSigla = doc.getExMobilPai().getSigla();
		if (doc.getExTipoDocumento() != null)
			switch (doc.getExTipoDocumento().getId().intValue()) {
			case 1:
				this.tipoDocumento = "interno";
				break;
			case 2:
				this.tipoDocumento = "interno_importado";
				break;
			case 3:
				this.tipoDocumento = "externo";
				break;
			}
		if (doc.getExFormaDocumento().getExTipoFormaDoc() != null)
			switch (doc.getExFormaDocumento().getExTipoFormaDoc().getId().intValue()) {
			case 1:
				this.tipoFormaDocumento = "expediente";
				break;
			case 2:
				this.tipoFormaDocumento = "processo_administrativo";
				break;
			}

		this.dtFinalizacao = doc.getDtFinalizacaoDDMMYY();
		if (doc.getExModelo() != null)
			this.nmArqMod = doc.getExModelo().getNmArqMod();

		this.conteudoBlobHtmlString = doc.getConteudoBlobHtmlStringComReferencias();

		if (doc.isEletronico()) {
			this.classe = "header_eletronico";
			this.fisicoOuEletronico = "Documento Eletrônico";
			this.fDigital = true;
		} else {
			this.classe = "header";
			this.fisicoOuEletronico = "Documento Físico";
			this.fDigital = false;
		}

		ExCompetenciaBL comp = Ex.getInstance().getComp();
		for (ExMovimentacao movCossig : doc.getMovsCosignatario()) {
			ExMobilApiVO mobilApiVO = new ExMobilApiVO(doc.getMobilGeral(), titular, titular, lotaTitular, exibirAntigo);
			ExMovimentacaoApiVO movApiVO =  new ExMovimentacaoApiVO(mobilApiVO, movCossig, cadastrante, titular, lotaTitular);
			cossignatarios.put(movApiVO, comp.podeExcluirCosignatario(titular, lotaTitular,	doc.getMobilGeral(), movCossig));
		}
		this.forma = doc.getExFormaDocumento() != null ? doc.getExFormaDocumento().getDescricao() : "";
		this.modelo = doc.getExModelo() != null ? doc.getExModelo().getNmMod() : "";

		if (mob != null) {
			SortedSet<ExMobil> mobsDoc;
			if (doc.isProcesso())
				mobsDoc = doc.getExMobilSetInvertido();
			else
				mobsDoc = doc.getExMobilSet();

			for (ExMobil m : mobsDoc) {
				if (mob.isGeral() || m.isGeral() || mob.getId().equals(m.getId()))
					mobs.add(new ExMobilApiVO(m, cadastrante, titular, lotaTitular, completo));
			}

			addAcoes(doc, titular, lotaTitular, exibirAntigo);

			this.podeAssinar = comp.podeAssinar(titular, lotaTitular, mob) 
					&& comp.podeAssinarComSenha(titular, lotaTitular, mob);
			
			ExGraphTramitacao exGraphTramitacao = new ExGraphTramitacao(mob);
			if (exGraphTramitacao.getNumNodos() > 1)
				this.dotTramitacao = exGraphTramitacao.toString();
			this.dotRelacaoDocs = new ExGraphRelacaoDocs(mob, titular).toString();
			this.dotColaboracao = new ExGraphColaboracao(doc).toString();
		}

		// Desabilitado temporariamente
		// addDadosComplementares();

		tags = new ArrayList<String>();
		if (doc.getExClassificacao() != null) {
			String classificacao = doc.getExClassificacao().getDescricao();
			if (classificacao != null && classificacao.length() != 0) {
				String a[] = classificacao.split(": ");
				for (String s : a) {
					String ss = "@" + Texto.slugify(s, true, true);
					if (!tags.contains(ss)) {
						tags.add(ss);
					}
				}
			}
		}
		if (doc.getExModelo() != null) {
			String ss = "@" + Texto.slugify(doc.getExModelo().getNmMod(), true, true);
			if (!tags.contains(ss)) {
				tags.add(ss);
			}
		}

		// Desabilitado temporariamente
		// if (doc.getDocumentosPublicadosNoBoletim() != null) {
		// for (ExDocumento documentoPublicado : doc
		// .getDocumentosPublicadosNoBoletim()) {
		// documentosPublicados.add(new ExDocumentoApiVO(documentoPublicado));
		// }
		// }
		//
		// ExDocumento bol = doc.getBoletimEmQueDocFoiPublicado();
		// if (bol != null)
		// boletim = new ExDocumentoApiVO(bol);

		this.originalNumero = doc.getNumExtDoc();
		this.originalData = doc.getDtDocOriginalDDMMYYYY();
		this.originalOrgao = doc.getOrgaoExterno() != null ? doc.getOrgaoExterno().getDescricao() : null;

		// Aqui começa o exibe()
		List<Long> movimentacoesPermitidas = new ArrayList<Long>();

		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA_A_DOCUMENTO_EXTERNO);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA_EXTERNO);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_DE_ARQUIVO_AUXILIAR);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANOTACAO);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO_TRANSFERENCIA);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DISPONIBILIZACAO);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO_BOLETIM);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_PUBLICACAO_BOLETIM);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_PUBLICACAO);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ENCERRAMENTO_DE_VOLUME);
		movimentacoesPermitidas.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_COPIA);

		List<Long> marcasGeralPermitidas = new ArrayList<Long>();
		marcasGeralPermitidas.add(CpMarcadorEnum.A_ELIMINAR.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.ARQUIVADO_CORRENTE.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.ARQUIVADO_INTERMEDIARIO.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.ARQUIVADO_PERMANENTE.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.EM_EDITAL_DE_ELIMINACAO.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.PUBLICACAO_SOLICITADA.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.PUBLICADO.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.RECOLHER_PARA_ARQUIVO_PERMANENTE.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.REMETIDO_PARA_PUBLICACAO.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.TRANSFERIR_PARA_ARQUIVO_INTERMEDIARIO.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.PENDENTE_DE_ASSINATURA.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.COMO_SUBSCRITOR.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.REVISAR.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.ANEXO_PENDENTE_DE_ASSINATURA.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.TRANSFERIR_PARA_ARQUIVO_INTERMEDIARIO.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.PENDENTE_DE_ANEXACAO.getId());

		for (ExMobilApiVO mobVO : mobs) {

			// Limpa as Movimentações
			List<ExMovimentacaoApiVO> movimentacoesFinais = new ArrayList<ExMovimentacaoApiVO>();
			List<ExMovimentacaoApiVO> juntadasRevertidas = new ArrayList<ExMovimentacaoApiVO>();

			for (ExMovimentacaoApiVO exMovVO : mobVO.getMovs()) {
				if (!exMovVO.isCancelada() && movimentacoesPermitidas.contains(exMovVO.getIdTpMov())) {
					exMovVO.podeExibirNoSigale = true;
					// Desabilitado temporariamente
					// if (exMovVO.getIdTpMov() ==
					// ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA)
					// {
					// juntadasRevertidas.add(exMovVO.getMov()
					// .getExMovimentacaoRef());
					// // Edson: se não gerou peça, nem mostra o
					// // desentranhamento
					// if (exMovVO.getMov().getConteudoBlobMov() == null)
					// continue;
					// }
					if (!juntadasRevertidas.contains(exMovVO))
						movimentacoesFinais.add(exMovVO);
				} else {
					exMovVO.podeExibirNoSigale = false;
					movimentacoesFinais.add(exMovVO);
				}
			}

			mobVO.setMovs(movimentacoesFinais);
		}

		ExMobilApiVO mobilGeral = null;
		ExMobilApiVO mobilEspecifico = null;

		for (ExMobilApiVO mobilVO : mobs) {
			if (mobilVO.isGeral)
				mobilGeral = mobilVO;
			else
				mobilEspecifico = mobilVO;
		}

		// MarcasPorMobil
//		for (ExMobil cadaMobil : doc.getExMobilSet()) {
//			if (!cadaMobil.isGeral())
//				marcasPorMobil.put(cadaMobil, cadaMobil.getExMarcaSet());
//		}
//
//		if (mobilEspecifico != null && mobilGeral != null) {
//			mobilEspecifico.getAcoes().addAll(mobilGeral.getAcoes());
//			mobilEspecifico.getMovs().addAll(mobilGeral.getMovs());
//			mobilEspecifico.anexosNaoAssinados.addAll(mobilGeral.anexosNaoAssinados);
//			for (ExMarca m : mobilGeral.getMarcasAtivas())
//				if (marcasGeralPermitidas.contains(m.getCpMarcador().getIdMarcador()))
//					mobilEspecifico.getMarcasAtivas().add(m);
//			for (ExMarca m : mobilGeral.getMob().getExMarcaSet())
//				if (marcasGeralPermitidas.contains(m.getCpMarcador().getIdMarcador()))
//					for (ExMobil cadaMobil : marcasPorMobil.keySet())
//						marcasPorMobil.get(cadaMobil).add(m);
//			mobs.remove(mobilGeral);
//		}
//
//		// Edson: mostra lista de vias/volumes só se número de
//		// vias/volumes além do geral for > que 1 ou se o móbil
//		// tiver informações que não aparecem no topo da tela
//		if (doc.getExMobilSet().size() > 2 || mob.temMarcaNaoAtiva())
//			outrosMobsLabel = doc.isProcesso() ? "Volumes" : "Vias";
	}

	/**
	 * @param doc
	 * @param titular
	 * @param lotaTitular
	 * @throws Exception
	 */
	private void addAcoes(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular, boolean exibirAntigo) {
		ExApiVO vo = this;
		for (ExMobilApiVO mobvo : mobs) {
			if (mobvo.isGeral)
				vo = mobvo;
		}

		ExMobil mob = doc.getMobilGeral();

		vo.addAcao("folder_magnify", "_Ver Dossiê", "/app/expediente/doc", "exibirProcesso",
				Ex.getInstance().getComp().podeVisualizarImpressao(titular, lotaTitular, mob));

		vo.addAcao("printer", "Ver _Impressão", "/app/arquivo", "exibir",
				Ex.getInstance().getComp().podeVisualizarImpressao(titular, lotaTitular, mob), null,
				"&popup=true&arquivo=" + doc.getReferenciaPDF(), null, null, null);

		vo.addAcao("lock", "Fina_lizar", "/app/expediente/doc",

				"finalizar", Ex.getInstance().getComp().podeFinalizar(titular, lotaTitular, mob), null, null, null,
				null, "once");

		// addAcao("Finalizar e Assinar", "/expediente/mov",
		// "finalizar_assinar",
		// podeFinalizarAssinar(titular, lotaTitular, mob),
		// "Confirma a finalização do documento?", null, null, null);

		vo.addAcao("pencil", "Edita_r", "/app/expediente/doc", "editar",
				Ex.getInstance().getComp().podeEditar(titular, lotaTitular, mob));

		vo.addAcao("delete", "Excluir", "/app/expediente/doc", "excluir",
				Ex.getInstance().getComp().podeExcluir(titular, lotaTitular, mob), "Confirma a exclusão do documento?",
				null, null, null, "once");

		vo.addAcao("user_add", "Incluir Cossignatário", "/app/expediente/mov", "incluir_cosignatario",
				Ex.getInstance().getComp().podeIncluirCosignatario(titular, lotaTitular, mob), null,
				"sigla=" + doc.getSigla(), null, null, null);

		vo.addAcao("attach", "Ane_xar", "/app/expediente/mov", "anexar",
				Ex.getInstance().getComp().podeAnexarArquivo(titular, lotaTitular, mob));
		vo.addAcao("note_add", "_Anotar", "/app/expediente/mov", "anotar",
				Ex.getInstance().getComp().podeFazerAnotacao(titular, lotaTitular, mob));

		vo.addAcao("folder_user", "Definir Perfil", "/app/expediente/mov", "vincularPapel",
				Ex.getInstance().getComp().podeFazerVinculacaoPapel(titular, lotaTitular, mob));

		vo.addAcao("folder_star", "Definir Marcador", "/app/expediente/mov", "marcar",
				new ExPodeMarcar(mob, titular, lotaTitular).eval());

		vo.addAcao("cd", "Download do Conteúdo", "/expediente/doc", "anexo",
				Ex.getInstance().getComp().podeDownloadConteudo(titular, lotaTitular, mob));

		vo.addAcao("add", "Criar Via", "/app/expediente/doc", "criar_via",
				Ex.getInstance().getComp().podeCriarVia(titular, lotaTitular, mob), null, null, null, null, "once");

		vo.addAcao("add", "Abrir Novo Volume", "/app/expediente/doc", "criar_volume",
				Ex.getInstance().getComp().podeCriarVolume(titular, lotaTitular, mob),
				"Confirma a abertura de um novo volume?", null, null, null, "once");

		vo.addAcao("link_add", "Criar Subprocesso", "/app/expediente/doc", "editar",
				Ex.getInstance().getComp().podeCriarSubprocesso(titular, lotaTitular, mob), null,
				"mobilPaiSel.sigla=" + getSigla() + "&idForma=" + mob.doc().getExFormaDocumento().getIdFormaDoc()
						+ "&criandoSubprocesso=true",
				null, null, null);

		vo.addAcao("script_edit", "Registrar A_ssinatura Manual", "/app/expediente/mov", "registrar_assinatura",
				Ex.getInstance().getComp().podeRegistrarAssinatura(titular, lotaTitular, mob));

		vo.addAcao("script_key", "A_ssinar", "/app/expediente/mov", "assinar",
				Ex.getInstance().getComp().podeAssinar(titular, lotaTitular, mob));

		vo.addAcao("script_key", "A_utenticar", "/app/expediente/mov", "autenticar_documento",
				Ex.getInstance().getComp().podeAutenticarDocumento(titular, lotaTitular, mob.doc()));

		if (doc.isFinalizado() && doc.getNumExpediente() != null) {
			// documentos finalizados
			if (mob.temAnexos()) {
				vo.addAcao("script_key", "Assinar Anexos Gerais", "/app/expediente/mov", "assinarAnexos", true, null,
						"assinandoAnexosGeral=true&sigla=" + getSigla(), null, null, null);
			}
		}

		vo.addAcao("shield", "Redefinir Acesso", "/app/expediente/mov", "redefinir_nivel_acesso",
				Ex.getInstance().getComp().podeRedefinirNivelAcesso(titular, lotaTitular, mob));

		vo.addAcao("book_add", "Solicitar Publicação no Boletim", "/app/expediente/mov", "boletim_agendar",
				Ex.getInstance().getComp().podeBotaoAgendarPublicacaoBoletim(titular, lotaTitular, mob));

		vo.addAcao("book_link", "Registrar Publicação do Boletim", "/app/expediente/mov", "boletim_publicar",
				Ex.getInstance().getComp().podePublicar(titular, lotaTitular, mob), null, null, null, null, "once");

		vo.addAcao("error_go", "Refazer", "/app/expediente/doc", "refazer",
				Ex.getInstance().getComp().podeRefazer(titular, lotaTitular, mob),
				"Esse documento será cancelado e seus dados serão copiados para um novo expediente em elaboração. Prosseguir?",
				null, null, null, "once");

		vo.addAcao("arrow_divide", "Duplicar", "/app/expediente/doc", "duplicar",
				Ex.getInstance().getComp().podeDuplicar(titular, lotaTitular, mob),
				"Esta operação criará um expediente com os mesmos dados do atual. Prosseguir?", null, null, null,
				"once");

		// test="${exibirCompleto != true}" />
		int numUltMobil = doc.getNumUltimoMobil();
		vo.addAcao("eye", "Ver _Mais", "/app/expediente/doc", "exibirAntigo",
				Ex.getInstance().getComp().podeExibirInformacoesCompletas(titular, lotaTitular, mob) && !exibirAntigo,
				numUltMobil < 20 ? ""
						: "Exibir todos os " + numUltMobil
								+ " volumes do processo simultaneamente pode exigir um tempo maior de processamento. Deseja exibi-los?",
				null, null, null, null);

		vo.addAcao("magnifier", "Auditar", "/app/expediente/doc", "exibirAntigo",
				Ex.getInstance().getComp().podeExibirInformacoesCompletas(titular, lotaTitular, mob) && exibirAntigo,
				null, "&exibirCompleto=true", null, null, null);

		vo.addAcao("report_link", "Agendar Publicação no Diário", "/app/expediente/mov", "agendar_publicacao",
				Ex.getInstance().getComp().podeAgendarPublicacao(titular, lotaTitular, mob));

		vo.addAcao("report_add", "Solicitar Publicação no Diário", "/app/expediente/mov", "pedirPublicacao",
				Ex.getInstance().getComp().podePedirPublicacao(titular, lotaTitular, mob));

		// <ww:param name="idFormaDoc">60</ww:param>
		vo.addAcao("arrow_undo", "Desfazer Cancelamento", "/app/expediente/doc", "desfazerCancelamentoDocumento",
				Ex.getInstance().getComp().podeDesfazerCancelamentoDocumento(titular, lotaTitular, mob),
				"Esta operação anulará o cancelamento do documento e tornará o documento novamente editável. Prosseguir?",
				null, null, null, "once");

		vo.addAcao("delete", "Cancelar", "/app/expediente/doc", "tornarDocumentoSemEfeito",
				Ex.getInstance().getComp().podeTornarDocumentoSemEfeito(titular, lotaTitular, mob),
				"Esta operação tornará esse documento sem efeito. Prosseguir?", null, null, null, "once");
	}

	// Desabilitado temporariamente
	// public void addDadosComplementares() {
	// ProcessadorModeloFreemarker p = new ProcessadorModeloFreemarker();
	// Map attrs = new HashMap();
	// attrs.put("nmMod", "macro dadosComplementares");
	// attrs.put("template", "[@dadosComplementares/]");
	// attrs.put("doc", this.getDoc());
	//
	// Desabilitado temporariamente
	// dadosComplementares = p.processarModelo(doc.getOrgaoUsuario(), attrs,
	// null);
	// }

	public String getClasse() {
		return classe;
	}

	public String getClassificacaoDescricaoCompleta() {
		return classificacaoDescricaoCompleta;
	}

	public String getConteudoBlobHtmlString() {
		return conteudoBlobHtmlString;
	}

	public String getDescrDocumento() {
		return descrDocumento;
	}

	public String getDestinatarioString() {
		return destinatarioString;
	}

	public String getDtDocDDMMYY() {
		return dtDocDDMMYY;
	}

	public String getDtFinalizacao() {

		return dtFinalizacao;
	}

	public List<ExMobilApiVO> getMobs() {
		return mobs;
	}

	public String getNmArqMod() {
		return nmArqMod;
	}

	public String getNmNivelAcesso() {
		return nmNivelAcesso;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public String getPaiSigla() {
		return paiSigla;
	}

	public String getSigla() {
		return sigla;
	}

	// Desabilitado temporariamente
	// public String getSiglaCurtaSubProcesso() {
	// if (doc.isProcesso() && doc.getExMobilPai() != null) {
	// try {
	// return sigla.substring(sigla.length() - 3, sigla.length());
	// } catch (Exception e) {
	// return sigla;
	// }
	// }
	//
	// return "";
	//
	// }

	public String getSubscritorString() {
		return subscritorString;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public String getFisicoOuEletronico() {
		return fisicoOuEletronico;
	}

	public boolean isDigital() {
		return fDigital;
	}

	@Override
	public String toString() {
		String s = getSigla() + "[" + getAcoes() + "]";
		for (ExMobilApiVO m : getMobs()) {
			s += "\n" + m.toString();
		}
		return s;
	}

	public String getDadosComplementares() {
		return dadosComplementares;
	}

	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getTipoFormaDocumento() {
		return tipoFormaDocumento;
	}

	public String getCadastranteString() {
		return cadastranteString;
	}

	public String getLotaCadastranteString() {
		return lotaCadastranteString;
	}

	public String getOutrosMobsLabel() {
		return outrosMobsLabel;
	}

	public List<ExDocumentoApiVO> getDocumentosPublicados() {
		return documentosPublicados;
	}

	public ExDocumentoApiVO getBoletim() {
		return boletim;
	}

	// Desabilitado temporariamente
	// public Map<ExMobil, Set<ExMarca>> getMarcasPorMobil() {
	// return marcasPorMobil;
	// }
	//
	// public void setMarcasPorMobil(Map<ExMobil, Set<ExMarca>> marcasPorMobil)
	// {
	// this.marcasPorMobil = marcasPorMobil;
	// }

	public Map<ExMovimentacaoApiVO, Boolean> getCossignatarios() {
		return cossignatarios;
	}

	public void setCossignatarios(Map<ExMovimentacaoApiVO, Boolean> cossignatarios) {
		this.cossignatarios = cossignatarios;
	}

	public String getOriginalNumero() {
		return originalNumero;
	}

	public String getOriginalData() {
		return originalData;
	}

	public String getOriginalOrgao() {
		return originalOrgao;
	}

}