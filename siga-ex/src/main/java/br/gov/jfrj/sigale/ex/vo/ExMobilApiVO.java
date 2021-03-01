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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExParte;

public class ExMobilApiVO extends ExApiVO {
	String sigla;
	boolean isGeral;
	List<ExMobilApiVO> apensos = new ArrayList<ExMobilApiVO>();
	// List<ExDocumentoApiVO> filhos = new ArrayList<ExDocumentoApiVO>();
	List<ExDocumentoApiVO> expedientesFilhosNaoCancelados = new ArrayList<ExDocumentoApiVO>();
	List<ExDocumentoApiVO> processosFilhosNaoCancelados = new ArrayList<ExDocumentoApiVO>();

	List<ExMovimentacaoApiVO> anexosNaoAssinados = new ArrayList<ExMovimentacaoApiVO>();
	List<ExMovimentacaoApiVO> despachosNaoAssinados = new ArrayList<ExMovimentacaoApiVO>();
	List<ExDocumentoApiVO> expedientesFilhosNaoJuntados = new ArrayList<ExDocumentoApiVO>();
	List<ExMobilApiVO> expedientesJuntadosNaoAssinados = new ArrayList<ExMobilApiVO>();
	List<ExMovimentacaoApiVO> pendenciasDeAnexacao = new ArrayList<ExMovimentacaoApiVO>();
	List<ExMovimentacaoApiVO> pendenciasDeColaboracao = new ArrayList<ExMovimentacaoApiVO>();
	Long pendenciaProximoModelo = null;

	List<ExMovimentacaoApiVO> movs = new ArrayList<ExMovimentacaoApiVO>();
	// Desabilitado temporariamente
	// List<ExMarca> marcasAtivas = new ArrayList<ExMarca>();
	List<DuracaoApiVO> duracoes = new ArrayList<DuracaoApiVO>();
	Long byteCount;
	Integer pagInicial;
	Integer pagFinal;
	String tamanhoDeArquivo;
	boolean ocultar;
	Long id;
	boolean podeTramitar;
	boolean podeAnotar;
	String marcadoresEmHtml;
	String descricaoCompleta;


	public List<ExMovimentacaoApiVO> getMovs() {
		return movs;
	}

	public void setMovs(List<ExMovimentacaoApiVO> movs) {
		this.movs = movs;
	}

	// Desabilitado temporariamente
	// public List<ExMarca> getMarcasAtivas() {
	// return marcasAtivas;
	// }
	//
	// public void setMarcasAtivas(List<ExMarca> marcasAtivas) {
	// this.marcasAtivas = marcasAtivas;
	// }

	public ExMobilApiVO(ExMobil mob, DpPessoa cadastrante, DpPessoa titular, DpLotacao lotaTitular, boolean completo) {
		this(mob, cadastrante, titular, lotaTitular, completo, null, false);
	}

	public ExMobilApiVO(ExMobil mob, DpPessoa cadastrante, DpPessoa titular, DpLotacao lotaTitular, boolean completo,
			Long tpMov, boolean movAssinada) {
		this.sigla = mob.getSigla();
		this.isGeral = mob.isGeral();
		this.id = mob.getId();

		this.podeTramitar = Ex.getInstance().getComp().podeTransferir(titular, lotaTitular, mob);
		this.podeAnotar = Ex.getInstance().getComp().podeFazerAnotacao(titular, lotaTitular, mob);

		if (!completo || mob.isEliminado())
			return;

		long tempoIni = System.currentTimeMillis();

		Set<ExMarca> marcasAtivas = new TreeSet<>();
		marcasAtivas.addAll(mob.getExMarcaSetAtivas());

		marcadoresEmHtml = getMarcadoresEmHtml(marcasAtivas, titular, lotaTitular);
		descricaoCompleta = mob.getDescricaoCompleta();

		/*
		 * Markenson: O código abaixo foi comentado por questões de desempenho.
		 * Deve ser estudada uma maneira mais eficiente de calcular o tamanho
		 * dos PDFs
		 */

		// byteCount, pagIni e pagFim
		// if (mob.getExDocumento().isEletronico()){

		// byteCount = mob.getByteCount();
		// if (byteCount != null && byteCount > 0)
		// tamanhoDeArquivo = FormataTamanhoDeArquivo
		// .converterEmTexto(byteCount);
		// }

		for (ExMobil m : mob.getApensosExcetoVolumeApensadoAoProximo()) {
			if (m.isEliminado())
				continue;
			apensos.add(new ExMobilApiVO(m, cadastrante, titular, lotaTitular, false));
		}

		tempoIni = System.currentTimeMillis();
		for (ExDocumento d : mob.getExDocumentoFilhoSet()) {
			if (d.isExpediente())
				expedientesFilhosNaoCancelados
						.add(new ExDocumentoApiVO(d, null, cadastrante, titular, lotaTitular, false, false));
			else
				processosFilhosNaoCancelados
						.add(new ExDocumentoApiVO(d, null, cadastrante, titular, lotaTitular, false, false));
		}

		for (ExDocumento doc : mob.getDocsFilhosNaoJuntados())
			expedientesFilhosNaoJuntados
					.add(new ExDocumentoApiVO(doc, null, cadastrante, titular, lotaTitular, false, false));



		tempoIni = System.currentTimeMillis();
		addAcoes(mob, titular, lotaTitular);

		tempoIni = System.currentTimeMillis();

		if (tpMov == null)
			for (ExMovimentacao mov : mob.getCronologiaSet()) {
				if (mov.getExMobil() != mob
						&& mov.getExTipoMovimentacao().getId().equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_COPIA))
					continue;
				movs.add(new ExMovimentacaoApiVO(this, mov, cadastrante, titular, lotaTitular));
			}
		else
			for (ExMovimentacao mov : mob.getMovimentacoesPorTipo(tpMov, false)) {
				if (!movAssinada) {
					if (!mov.isAssinada() && !mov.isCancelada())
						movs.add(new ExMovimentacaoApiVO(this, mov, cadastrante, titular, lotaTitular));
				} else {
					if (mov.isAssinada())
						movs.add(new ExMovimentacaoApiVO(this, mov, cadastrante, titular, lotaTitular));
				}
			}

		// Edson: infelizmente, aí embaixo estamos varrendo de novo as movs.
		// Melhorar?

		if (mob.doc().isEletronico()) {
			for (ExMovimentacao mov : mob.getAnexosNaoAssinados())
				anexosNaoAssinados.add(new ExMovimentacaoApiVO(this, mov, cadastrante, titular, lotaTitular));

			for (ExMobil juntado : mob.getJuntados())
				if (juntado.doc().isPendenteDeAssinatura())
					expedientesJuntadosNaoAssinados
							.add(new ExMobilApiVO(juntado, cadastrante, titular, lotaTitular, false));

			for (ExMovimentacao mov : mob.getDespachosNaoAssinados())
				despachosNaoAssinados.add(new ExMovimentacaoApiVO(this, mov, cadastrante, titular, lotaTitular));
		}

		if (mob.getPendenciasDeAnexacao() != null) {
			for (ExMovimentacao mov : mob.getPendenciasDeAnexacao())
				pendenciasDeAnexacao.add(new ExMovimentacaoApiVO(this, mov, cadastrante, titular, lotaTitular));
		}

		if (mob.getPendenciasDeColaboracao() != null) {
			for (ExMovimentacao mov : mob.getPendenciasDeColaboracao()) {
				ExMovimentacaoApiVO m = new ExMovimentacaoApiVO(this, mov, cadastrante, mov.getSubscritor(),
						mov.getLotaSubscritor());
				m.descricao = ExParte.create(mov.getDescrMov()).getString();
				pendenciasDeColaboracao.add(m);
			}
		}

		if (!mob.getExDocumento().isPendenteDeAssinatura()) {
			if (mob.getExDocumento().getExFormaDocumento().getId() == 107L)
				pendenciaProximoModelo = 110L;
			else if (mob.getExDocumento().getExFormaDocumento().getId() == 110L)
				pendenciaProximoModelo = 111L;
			else if (mob.getExDocumento().getExFormaDocumento().getId() == 111L)
				pendenciaProximoModelo = 112L;
		}

		// Desabilitado temporariamente
		// marcasAtivas.addAll(mob.getExMarcaSetAtivas());

		// Calcula o tempo que o documento ficou em cada uma das lotações por
		// onde ele passou.
		ExMovimentacaoApiVO movVOIni = null;
		ExMovimentacaoApiVO movVOUlt = null;
		int i = 1;
		int duracaoSpan = 0;
		Collections.reverse(movs);
		for (ExMovimentacaoApiVO movVO : movs) {
			if (movVOIni == null) {
				movVOIni = movVO;
			}
			// if (movVO.idTpMov != 14 && !movVO.isCancelada())
			duracaoSpan++;

			if (i == movs.size()
					|| (movVO.idTpMov == ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO && (!movVO.isCancelada()))) {
				if (i == movs.size()) {
					duracaoSpan++;
				}
				if (i == movs.size() || movVOUlt == null)
					movVOUlt = movVO;
				SigaCalendar lIni = new SigaCalendar(movVOIni.dtIniMov.getTime());
				SigaCalendar lFim = new SigaCalendar(movVOUlt.dtIniMov.getTime());

				DuracaoApiVO d = new DuracaoApiVO();
				long l = lFim.getUnixDay() - lIni.getUnixDay();
				if (l == 0) {
					d.setDuracao(lIni.diffCompact(lFim));
					if (d.getDuracao().endsWith("m"))
						d.setDuracao(d.getDuracao() + "in");
				} else {
					d.setDuracao(Long.toString(l) + " dia" + (l == 1L ? "" : "s"));
				}
				// movVOUlt.duracaoSpan = duracaoSpan;
				d.setSpan(duracaoSpan);
				d.setSpanExibirCompleto(duracaoSpan);
				duracoes.add(d);
				movVOIni = movVOUlt;
				duracaoSpan = 0;
			}
			i++;
			movVOUlt = movVO;
		}
		Collections.reverse(movs);
		Collections.reverse(duracoes);

		int j = 0;
		int span = 0;
		for (ExMovimentacaoApiVO movVO : movs) {
			span++;
			if (movVO.idTpMov == 14 || movVO.isCancelada()) {
				duracoes.get(j).setSpan(duracoes.get(j).getSpan() - 1);
			}
			if (span == duracoes.get(j).getSpanExibirCompleto()) {
				j++;
				span = 0;
			}
		}

		// Ocultar movimentações de cancelamento
		j = 0;
		span = 0;
		for (ExMovimentacaoApiVO movVO : movs) {
			if (movVO.idTpMov != 14 && !movVO.isCancelada()) {
				if (span == 0) {
					movVO.duracao = duracoes.get(j).getDuracao();
					movVO.duracaoSpan = duracoes.get(j).getSpan();
					span = duracoes.get(j).getSpan();
					j++;
				}
				span--;
			}
		}

		// Exibir completo
		j = 0;
		span = 0;
		for (ExMovimentacaoApiVO movVO : movs) {
			if (span == 0) {
				movVO.duracao = duracoes.get(j).getDuracao();
				movVO.duracaoSpanExibirCompleto = duracoes.get(j).getSpanExibirCompleto();
				span = duracoes.get(j).getSpanExibirCompleto();
				j++;
			}
			span--;
		}


	}

	/**
	 * @param mob
	 * @param titular
	 * @param lotaTitular
	 * @throws Exception
	 */
	private void addAcoes(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		if (!mob.isGeral()) {
			addAcao("folder", "_Ver Dossiê", "/app/expediente/doc", "exibirProcesso",
					Ex.getInstance().getComp().podeVisualizarImpressao(titular, lotaTitular, mob), null, null, null,
					null, "once");

			addAcao("printer", "Ver _Impressão", "/app/arquivo", "exibir",
					Ex.getInstance().getComp().podeVisualizarImpressao(titular, lotaTitular, mob), null,
					"&popup=true&arquivo=" + mob.getReferenciaPDF(), null, null, null);

			addAcao("page_white_add", "Incluir _Documento", "/app/expediente/doc", "editar",
					Ex.getInstance().getComp().podeIncluirDocumento(titular, lotaTitular, mob), null,
					"criandoAnexo=true&mobilPaiSel.sigla=" + getSigla(), null, null, null);

			if (mob.temAnexos()) {
				addAcao("script_key", "Assinar Anexos " + (mob.isVia() ? "da Via" : "do Volume"), "/app/expediente/mov",
						"assinarAnexos", true, null, "assinandoAnexosGeral=true&sigla=" + getSigla(), null, null, null);
			}
		}
		addAcao("page_white_error", "Desentranhar", "/app/expediente/mov", "cancelar_juntada",
				Ex.getInstance().getComp().podeCancelarJuntada(titular, lotaTitular, mob), null, null, null, null,
				"once");

		addAcao("link_delete", "Desapensar", "/app/expediente/mov", "desapensar",
				Ex.getInstance().getComp().podeDesapensar(titular, lotaTitular, mob), null, null, null, null, "once");

		addAcao("email_open", "Receber", "/app/expediente/mov", "receber",
				Ex.getInstance().getComp().podeReceber(titular, lotaTitular, mob), null, null, null, null, "once");

		addAcao("email_go", "_Tramitar", "/app/expediente/mov", "transferir",
				Ex.getInstance().getComp().podeTransferir(titular, lotaTitular, mob));

		if (mob.isVia() || mob.isVolume()) {
			addAcao("attach", "Ane_xar", "/app/expediente/mov", "anexar",
					Ex.getInstance().getComp().podeAnexarArquivo(titular, lotaTitular, mob));
			addAcao("note_add", "_Anotar", "/app/expediente/mov", "anotar",
					Ex.getInstance().getComp().podeFazerAnotacao(titular, lotaTitular, mob));
			addAcao("page_white_copy", "Incluir _Cópia", "/app/expediente/mov", "copiar",
					Ex.getInstance().getComp().podeCopiar(titular, lotaTitular, mob));
		}

		addAcao("box_add", "Ar_q. Corrente", "/app/expediente/mov", "arquivar_corrente_gravar",
				Ex.getInstance().getComp().podeArquivarCorrente(titular, lotaTitular, mob), null, null, null, null,
				"once");

		addAcao("building_go", "Indicar para Guarda Permanente", "/app/expediente/mov", "indicar_permanente",
				Ex.getInstance().getComp().podeIndicarPermanente(titular, lotaTitular, mob));

		addAcao("buildinge_delete", "Reverter Ind. Guarda Permanente", "/app/expediente/mov",
				"reverter_indicacao_permanente",
				Ex.getInstance().getComp().podeReverterIndicacaoPermanente(titular, lotaTitular, mob));

		addAcao("page_red", "Retirar de Edital de Eliminação", "/app/expediente/mov", "retirar_de_edital_eliminacao",
				Ex.getInstance().getComp().podeRetirarDeEditalEliminacao(titular, lotaTitular, mob));

		addAcao("table_edit", "Reclassificar", "/app/expediente/mov", "reclassificar",
				Ex.getInstance().getComp().podeReclassificar(titular, lotaTitular, mob));

		addAcao("table", "Avaliar", "/app/expediente/mov", "avaliar",
				Ex.getInstance().getComp().podeAvaliar(titular, lotaTitular, mob));

		addAcao("hourglass_add", "So_brestar", "/app/expediente/mov", "sobrestar_gravar",
				Ex.getInstance().getComp().podeSobrestar(titular, lotaTitular, mob), null, null, null, null, "once");

		addAcao("building_add", "Recolher ao Arq. Permanente", "/app/expediente/mov", "arquivar_permanente_gravar",
				Ex.getInstance().getComp().podeBotaoArquivarPermanente(titular, lotaTitular, mob), null, null, null,
				null, "once");

		addAcao("package_add", "Arq. Intermediário", "/app/expediente/mov", "arquivar_intermediario",
				Ex.getInstance().getComp().podeBotaoArquivarIntermediario(titular, lotaTitular, mob), null, null, null,
				null, "once");

		addAcao("box_delete", "Desar_q. Corrente", "/app/expediente/mov", "reabrir_gravar",
				Ex.getInstance().getComp().podeDesarquivarCorrente(titular, lotaTitular, mob), null, null, null, null,
				"once");

		addAcao("package_delete", "Desarq. Intermediário", "/app/expediente/mov", "desarquivar_intermediario_gravar",
				Ex.getInstance().getComp().podeBotaoDesarquivarIntermediario(titular, lotaTitular, mob), null, null,
				null, null, "once");

		addAcao("hourglass_delete", "Deso_brestar", "/app/expediente/mov", "desobrestar_gravar",
				Ex.getInstance().getComp().podeDesobrestar(titular, lotaTitular, mob), null, null, null, null, "once");

		addAcao("page_white_go", "_Juntar", "/app/expediente/mov", "juntar",
				Ex.getInstance().getComp().podeJuntar(titular, lotaTitular, mob));

		addAcao("page_find", "Vi_ncular", "/app/expediente/mov", "referenciar",
				Ex.getInstance().getComp().podeReferenciar(titular, lotaTitular, mob));

		addAcao("link_add", "Apensar", "/app/expediente/mov", "apensar",
				Ex.getInstance().getComp().podeApensar(titular, lotaTitular, mob));

		// Não aparece a opção de Cancelar Movimentação para documentos
		// temporários
		if (mob.getExDocumento().isFinalizado() && mob.getUltimaMovimentacaoNaoCancelada() != null
				&& mob.getUltimaMovimentacaoNaoCancelada().getExTipoMovimentacao()
						.getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO
				&& mob.getUltimaMovimentacaoNaoCancelada().getExTipoMovimentacao()
						.getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONTROLE_DE_COLABORACAO)
			addAcao("arrow_undo", "Desfa_zer " + mob.getDescricaoUltimaMovimentacaoNaoCancelada(),
					"/app/expediente/mov", "cancelarMovimentacao",
					Ex.getInstance().getComp().podeCancelarMovimentacao(titular, lotaTitular, mob),
					"Confirma o cancelamento da última movimentação(" + mob.getDescricaoUltimaMovimentacaoNaoCancelada()
							+ ")?",
					null, null, null, "once"); // popup,
		// exibir+completo,
		// confirmacao

		addAcao("folder_page_white", "Encerrar Volume", "/app/expediente/mov", "encerrar_volume",
				Ex.getInstance().getComp().podeEncerrarVolume(titular, lotaTitular, mob),
				"Confirma o encerramento do volume?", null, null, null, "once");

		addAcao("cancel", "Cancelar Via", "/app/expediente/mov", "cancelarMovimentacao",
				Ex.getInstance().getComp().podeCancelarVia(titular, lotaTitular, mob),
				"Confirma o cancelamento da via?", null, null, null, "once");

		addAcao("page_white_get", "Autuar", "/app/expediente/doc", "editar",
				Ex.getInstance().getComp().podeAutuar(titular, lotaTitular, mob), null,
				"idMobilAutuado=" + mob.getId() + "&autuando=true", null, null, null);
	}

	public String getMarcadoresEmHtml(Set<ExMarca> marcasAtivas, DpPessoa pess, DpLotacao lota) {
		StringBuilder sb = new StringBuilder();

		if (pess != null && lota != null) {
			// Marcacoes para a propria lotacao e para a propria pessoa ou sem
			// informacao de pessoa
			//
			for (ExMarca mar : marcasAtivas) {
				if (mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.EM_TRANSITO.getId()
						&& mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.EM_TRANSITO_ELETRONICO.getId()
						&& mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.DOCUMENTO_ASSINADO_COM_SENHA.getId()
						&& mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.MOVIMENTACAO_ASSINADA_COM_SENHA.getId()
						&& mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.MOVIMENTACAO_CONFERIDA_COM_SENHA.getId()
						&& ((mar.getDpLotacaoIni() != null
								&& lota.getIdInicial().equals(mar.getDpLotacaoIni().getIdInicial()))
								|| mar.getDpLotacaoIni() == null)
						&& (mar.getDpPessoaIni() == null
								|| pess.getIdInicial().equals(mar.getDpPessoaIni().getIdInicial()))) {
					if (sb.length() > 0)
						sb.append(", ");
					sb.append(mar.getCpMarcador().getDescrMarcador());
				}
			}

			// Marcacoes para a propria lotacao e para outra pessoa
			//
			if (sb.length() == 0) {
				for (ExMarca mar : marcasAtivas) {
					if (mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.EM_TRANSITO.getId()
							&& mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.EM_TRANSITO_ELETRONICO.getId()
							&& mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.DOCUMENTO_ASSINADO_COM_SENHA.getId()
							&& mar.getCpMarcador()
									.getIdMarcador() != CpMarcadorEnum.MOVIMENTACAO_ASSINADA_COM_SENHA.getId()
							&& mar.getCpMarcador()
									.getIdMarcador() != CpMarcadorEnum.MOVIMENTACAO_CONFERIDA_COM_SENHA.getId()) {
						if (sb.length() > 0)
							sb.append(", ");
						if ((mar.getDpLotacaoIni() != null
								&& lota.getIdInicial().equals(mar.getDpLotacaoIni().getIdInicial()))
								&& (mar.getDpPessoaIni() != null
										&& !pess.getIdInicial().equals(mar.getDpPessoaIni().getIdInicial()))) {
							sb.append(mar.getCpMarcador().getDescrMarcador());
							sb.append(" [<span title=\"");
							sb.append(mar.getDpPessoaIni().getNomePessoa());
							sb.append("\">");
							sb.append(mar.getDpPessoaIni().getSigla());
							sb.append("</span>]");
						}
					}
				}
			}
		}

		// Marcacoes para qualquer outra pessoa ou lotacao
		//
		if (sb.length() == 0) {
			for (ExMarca mar : marcasAtivas) {
				if (mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.EM_TRANSITO.getId()
						&& mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.EM_TRANSITO_ELETRONICO.getId()
						&& mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.DOCUMENTO_ASSINADO_COM_SENHA.getId()
						&& mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.MOVIMENTACAO_ASSINADA_COM_SENHA.getId()
						&& mar.getCpMarcador()
								.getIdMarcador() != CpMarcadorEnum.MOVIMENTACAO_CONFERIDA_COM_SENHA.getId()) {
					if (sb.length() > 0)
						sb.append(", ");
					sb.append(mar.getCpMarcador().getDescrMarcador());
					if (mar.getDpLotacaoIni() != null || mar.getDpPessoaIni() != null) {
						sb.append(" [");
						if (mar.getDpLotacaoIni() != null) {
							sb.append(mar.getDpLotacaoIni().getLotacaoAtual().getSigla());
						}
						if (mar.getDpPessoaIni() != null) {
							if (mar.getDpLotacaoIni() != null) {
								sb.append(", ");
							}
							sb.append(" [<span title=\"");
							sb.append(mar.getDpPessoaIni().getNomePessoa());
							sb.append("\">");
							sb.append(mar.getDpPessoaIni().getSigla());
							sb.append("</span>]");
						}
						sb.append("]");
					}
				}
			}
		}
		if (sb.length() == 0)
			return null;
		return sb.toString();
	}

	public String getSigla() {
		return sigla;
	}

	public List getApensos() {
		return apensos;
	}

	/*
	 * public List getFilhos() { return filhos; }
	 */

	public List getExpedientesFilhosNaoCancelados() {
		return expedientesFilhosNaoCancelados;
	}

	public List getAnexosNaoAssinados() {
		return anexosNaoAssinados;
	}

	public List getDespachosNaoAssinados() {
		return despachosNaoAssinados;
	}

	public List getExpedientesFilhosNaoJuntados() {
		return expedientesFilhosNaoJuntados;
	}

	public List getProcessosFilhosNaoCancelados() {
		return processosFilhosNaoCancelados;
	}

	public List<ExMobilApiVO> getExpedientesJuntadosNaoAssinados() {
		return expedientesJuntadosNaoAssinados;
	}

	public List<ExMovimentacaoApiVO> getPendenciasDeAnexacao() {
		return pendenciasDeAnexacao;
	}

	public List<ExMovimentacaoApiVO> getPendenciasDeColaboracao() {
		return pendenciasDeColaboracao;
	}

	@Override
	public String toString() {
		return getSigla() + "[" + getAcoes() + "] ";
	}

	public List<DuracaoApiVO> getDuracoes() {
		return duracoes;
	}

	public void setDuracoes(List<DuracaoApiVO> duracoes) {
		this.duracoes = duracoes;
	}

	public Long getByteCount() {
		return byteCount;
	}

	public Integer getPagInicial() {
		return pagInicial;
	}

	public Integer getPagFinal() {
		return pagFinal;
	}

	public String getTamanhoDeArquivo() {
		return tamanhoDeArquivo;
	}

	public Long getPendenciaProximoModelo() {
		return pendenciaProximoModelo;
	}

	public boolean isPendencias() {
		return anexosNaoAssinados.size() > 0 || despachosNaoAssinados.size() > 0
				|| expedientesFilhosNaoJuntados.size() > 0 || expedientesJuntadosNaoAssinados.size() > 0
				|| pendenciasDeAnexacao.size() > 0 || pendenciasDeColaboracao.size() > 0
				|| pendenciaProximoModelo != null;
	}
}