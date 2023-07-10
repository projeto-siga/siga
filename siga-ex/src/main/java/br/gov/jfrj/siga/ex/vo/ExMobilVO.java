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
package br.gov.jfrj.siga.ex.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExParte;
import br.gov.jfrj.siga.ex.logic.ExPodeAnexarArquivo;
import br.gov.jfrj.siga.ex.logic.ExPodeAnotar;
import br.gov.jfrj.siga.ex.logic.ExPodeApensar;
import br.gov.jfrj.siga.ex.logic.ExPodeArquivarCorrente;
import br.gov.jfrj.siga.ex.logic.ExPodeAutuarDocumento;
import br.gov.jfrj.siga.ex.logic.ExPodeAvaliar;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelarCiencia;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelarJuntada;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelarMovimentacao;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelarVia;
import br.gov.jfrj.siga.ex.logic.ExPodeConcluir;
import br.gov.jfrj.siga.ex.logic.ExPodeCopiar;
import br.gov.jfrj.siga.ex.logic.ExPodeDefinirPrazoAssinatura;
import br.gov.jfrj.siga.ex.logic.ExPodeDesapensar;
import br.gov.jfrj.siga.ex.logic.ExPodeDesarquivarCorrente;
import br.gov.jfrj.siga.ex.logic.ExPodeDessobrestar;
import br.gov.jfrj.siga.ex.logic.ExPodeEncerrarVolume;
import br.gov.jfrj.siga.ex.logic.ExPodeExibirBotaoDeArquivarIntermediario;
import br.gov.jfrj.siga.ex.logic.ExPodeExibirBotaoDeArquivarPermanente;
import br.gov.jfrj.siga.ex.logic.ExPodeExibirBotaoDeDesarquivarIntermediario;
import br.gov.jfrj.siga.ex.logic.ExPodeFazerAnotacao;
import br.gov.jfrj.siga.ex.logic.ExPodeFazerCiencia;
import br.gov.jfrj.siga.ex.logic.ExPodeIncluirDocumento;
import br.gov.jfrj.siga.ex.logic.ExPodeIndicarPermanente;
import br.gov.jfrj.siga.ex.logic.ExPodeJuntar;
import br.gov.jfrj.siga.ex.logic.ExPodeMarcar;
import br.gov.jfrj.siga.ex.logic.ExPodeNotificar;
import br.gov.jfrj.siga.ex.logic.ExPodeReceber;
import br.gov.jfrj.siga.ex.logic.ExPodeReclassificar;
import br.gov.jfrj.siga.ex.logic.ExPodeReferenciar;
import br.gov.jfrj.siga.ex.logic.ExPodeRetirarDeEditalDeEliminacao;
import br.gov.jfrj.siga.ex.logic.ExPodeReverterIndicacaoPermanente;
import br.gov.jfrj.siga.ex.logic.ExPodeSobrestar;
import br.gov.jfrj.siga.ex.logic.ExPodeTramitarEmParalelo;
import br.gov.jfrj.siga.ex.logic.ExPodeTransferir;
import br.gov.jfrj.siga.ex.logic.ExPodeVisualizarImpressao;
import br.gov.jfrj.siga.ex.logic.ExTemAnexos;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;

public class ExMobilVO extends ExVO {
	transient Logger log = Logger.getLogger(ExMobilVO.class.getCanonicalName());

	transient ExMobil mob;
	transient List<ExMarca> marcasAtivas = new ArrayList<ExMarca>();
	
	String sigla;
	boolean isGeral;
	List<ExMobilVO> apensos = new ArrayList<ExMobilVO>();
	// List<ExDocumentoVO> filhos = new ArrayList<ExDocumentoVO>();
	List<ExDocumentoVO> expedientesFilhosNaoCancelados = new ArrayList<ExDocumentoVO>();
	List<ExDocumentoVO> processosFilhosNaoCancelados = new ArrayList<ExDocumentoVO>();

	List<ExMovimentacaoVO> anexosNaoAssinados = new ArrayList<ExMovimentacaoVO>();
	List<ExMovimentacaoVO> despachosNaoAssinados = new ArrayList<ExMovimentacaoVO>();
	List<ExDocumentoVO> expedientesFilhosNaoJuntados = new ArrayList<ExDocumentoVO>();
	List<ExMobilVO> expedientesJuntadosNaoAssinados = new ArrayList<ExMobilVO>();
	List<ExMovimentacaoVO> pendenciasDeAnexacao = new ArrayList<ExMovimentacaoVO>();
	List<ExMovimentacaoVO> pendenciasDeColaboracao = new ArrayList<ExMovimentacaoVO>();
	Long pendenciaProximoModelo = null;

	List<ExMovimentacaoVO> movs = new ArrayList<ExMovimentacaoVO>();
	List<DuracaoVO> duracoes = new ArrayList<DuracaoVO>();
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
	
	public List<ExMovimentacaoVO> getMovs() {
		return movs;
	}

	public void setMovs(List<ExMovimentacaoVO> movs) {
		this.movs = movs;
	}

	public List<ExMarca> getMarcasAtivas() {
		return this.marcasAtivas;
	}

	public void setMarcasAtivas(List<ExMarca> marcasAtivas) {
		this.marcasAtivas = marcasAtivas;
	}

	public ExMobil getMob() {
		return mob;
	}

	public void setMob(ExMobil mob) {
		this.mob = mob;
	}

	public ExMobilVO(ExMobil mob, DpPessoa cadastrante, DpPessoa titular, DpLotacao lotaTitular,
			boolean completo, boolean serializavel) {
		this(mob, cadastrante, titular, lotaTitular, completo, null, false, serializavel);
	}

	public ExMobilVO(ExMobil mob, DpPessoa cadastrante, DpPessoa titular, DpLotacao lotaTitular,
			boolean completo, ITipoDeMovimentacao tpMov, boolean movAssinada, boolean serializavel) {
		this.mob = mob;
		this.sigla = mob.getSigla();
		this.isGeral = mob.isGeral();
		this.id = mob.getId();

		this.podeTramitar = Ex.getInstance().getComp().pode(ExPodeTransferir.class, titular, lotaTitular, mob);
		this.podeAnotar = Ex.getInstance().getComp().pode(ExPodeFazerAnotacao.class, titular, lotaTitular, mob);

		if (!completo || mob.isEliminado())
			return;

		long tempoIni = System.currentTimeMillis();

		this.marcasAtivas = new ArrayList<>();
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
			apensos.add(new ExMobilVO(m, cadastrante, titular, lotaTitular, false, serializavel));
		}
		log.debug(mob.getExDocumento().getCodigoString()
				+ ": aExibir - mobil " + mob.getNumSequencia()
				+ " - adicao apensos: "
				+ (System.currentTimeMillis() - tempoIni));

		tempoIni = System.currentTimeMillis();
		for (ExDocumento d : mob.getExDocumentoFilhoSet()) {
			if (d.isExpediente())
				expedientesFilhosNaoCancelados.add(new ExDocumentoVO(d,
						null, cadastrante, titular, lotaTitular, false, false, serializavel, false));
			else
				processosFilhosNaoCancelados.add(new ExDocumentoVO(d, null,
						cadastrante, titular, lotaTitular, false, false, serializavel, false));
		}

		for (ExDocumento doc : mob.getDocsFilhosNaoJuntados())
			expedientesFilhosNaoJuntados.add(new ExDocumentoVO(doc, null,
					cadastrante, titular, lotaTitular, false, false, serializavel, false));

		log.debug(mob.getExDocumento().getCodigoString()
				+ ": aExibir - mobil " + mob.getNumSequencia()
				+ " - adicao filhos: "
				+ (System.currentTimeMillis() - tempoIni));

		tempoIni = System.currentTimeMillis();
		addAcoes(mob, titular, lotaTitular);
		log.debug(mob.getExDocumento().getCodigoString()
						+ ": aExibir - mobil " + mob.getNumSequencia()
						+ " - adicao acoes: "
						+ (System.currentTimeMillis() - tempoIni));

		tempoIni = System.currentTimeMillis();

		if (tpMov == null)
			for (ExMovimentacao mov : mob.getCronologiaSet()) {
				if (mov.getExMobil() != mob && mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.COPIA)
					continue;
				movs.add(new ExMovimentacaoVO(this, mov, cadastrante, titular, lotaTitular, serializavel));
			}
		else
			for (ExMovimentacao mov : mob.getMovimentacoesPorTipo(tpMov, false)) {
				if (!movAssinada) {
					if (!mov.isAssinada() && !mov.isCancelada())
						movs.add(new ExMovimentacaoVO(this, mov, cadastrante, titular,
								lotaTitular, serializavel));
				} else {
					if (mov.isAssinada())
						movs.add(new ExMovimentacaoVO(this, mov, cadastrante, titular,
								lotaTitular, serializavel));
				}
			}

		// Edson: infelizmente, aí embaixo estamos varrendo de novo as movs.
		// Melhorar?

		if (mob.doc().isEletronico()) {
			for (ExMovimentacao mov : mob.getAnexosNaoAssinados())
				anexosNaoAssinados.add(new ExMovimentacaoVO(this, mov, cadastrante, titular,
						lotaTitular, serializavel));
			
			for (ExMobil juntado : mob.getJuntados())
				if (juntado.doc().isPendenteDeAssinatura())
					expedientesJuntadosNaoAssinados.add(new ExMobilVO(juntado, cadastrante, titular, lotaTitular, false, serializavel));

			for (ExMovimentacao mov : mob.getDespachosNaoAssinados())
				despachosNaoAssinados.add(new ExMovimentacaoVO(this, mov,
						cadastrante, titular, lotaTitular, serializavel));
		}

		if (mob.getPendenciasDeAnexacao() != null) {
			for (ExMovimentacao mov : mob.getPendenciasDeAnexacao())
				pendenciasDeAnexacao.add(new ExMovimentacaoVO(this, mov,
						cadastrante, titular, lotaTitular, serializavel));
		}

		if (mob.getPendenciasDeColaboracao() != null) {
			for (ExMovimentacao mov : mob.getPendenciasDeColaboracao()) {
				ExMovimentacaoVO m = new ExMovimentacaoVO(this, mov, cadastrante, mov
						.getSubscritor(), mov.getLotaSubscritor(), serializavel);
				m.descricao = ExParte.create(mov.getDescrMov()).getString();
				pendenciasDeColaboracao.add(m);
			}
		}

		// Calcula o tempo que o documento ficou em cada uma das lotações por
		// onde ele passou.
		ExMovimentacaoVO movVOIni = null;
		ExMovimentacaoVO movVOUlt = null;
		int i = 1;
		int duracaoSpan = 0;
		Collections.reverse(movs);
		for (ExMovimentacaoVO movVO : movs) {
			if (movVOIni == null) {
				movVOIni = movVO;
			}
			// if (movVO.idTpMov != 14 && !movVO.isCancelada())
			duracaoSpan++;

			if (i == movs.size()
					|| (movVO.exTipoMovimentacao == ExTipoDeMovimentacao.RECEBIMENTO && !movVO
							.isCancelada())) {
				if (i == movs.size()) {
					duracaoSpan++;
				}
				if (i == movs.size() || movVOUlt == null)
					movVOUlt = movVO;
				SigaCalendar lIni = new SigaCalendar(movVOIni.dtIniMov
						.getTime());
				SigaCalendar lFim = new SigaCalendar(movVOUlt.dtIniMov
						.getTime());

				DuracaoVO d = new DuracaoVO();
				long l = lFim.getUnixDay() - lIni.getUnixDay();
				if (l == 0) {
					d.setDuracao(lIni.diffCompact(lFim));
					if (d.getDuracao().endsWith("m"))
						d.setDuracao(d.getDuracao() + "in");
				} else {
					d.setDuracao(Long.toString(l) + " dia"
							+ (l == 1L ? "" : "s"));
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
		for (ExMovimentacaoVO movVO : movs) {
			span++;
			if (movVO.exTipoMovimentacao == ExTipoDeMovimentacao.CANCELAMENTO_DE_MOVIMENTACAO || movVO.isCancelada()) {
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
		for (ExMovimentacaoVO movVO : movs) {
			if (movVO.exTipoMovimentacao != ExTipoDeMovimentacao.CANCELAMENTO_DE_MOVIMENTACAO  && !movVO.isCancelada()) {
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
		for (ExMovimentacaoVO movVO : movs) {
			if (span == 0) {
				movVO.duracao = duracoes.get(j).getDuracao();
				movVO.duracaoSpanExibirCompleto = duracoes.get(j)
						.getSpanExibirCompleto();
				span = duracoes.get(j).getSpanExibirCompleto();
				j++;
			}
			span--;
		}

		log.debug(mob.getExDocumento().getCodigoString()
				+ ": aExibir - mobil " + mob.getNumSequencia()
				+ " - adicao movs: " + (System.currentTimeMillis() - tempoIni));
		if (serializavel) {
			this.mob = null;
			this.marcasAtivas = null;
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

			addAcao(AcaoVO.builder().nome(SigaMessages.getMessage("documento.ver.dossie"))
					.descr("Exibe um índice de todos os documentos juntados.")
					.icone("folder")
					.nameSpace("/app/expediente/doc")
					.acao("exibirProcesso")
					.params("sigla", mob.getCodigoCompacto())
					.params("nomeAcao", SigaMessages.getMessage("documento.ver.dossie"))
					.exp(new ExPodeVisualizarImpressao(mob, titular, lotaTitular))
					.classe("once")
					.build());

			addAcao(AcaoVO.builder().nome(SigaMessages.getMessage("documento.ver.impressao"))
					.descr("Apresenta a versão em PDF do documento para fins de impressão.")
					.icone(SigaMessages.getMessage("icon.ver.impressao")).nameSpace("/app/arquivo").acao("exibir")
					.params("sigla", mob.getCodigoCompacto()).params("popup", "true")
					.params("arquivo", mob.getReferenciaPDF())
					.params("nomeAcao", SigaMessages.getMessage("documento.ver.impressao"))
					.exp(new ExPodeVisualizarImpressao(mob, titular, lotaTitular)).classe("once").build());

			addAcao(AcaoVO.builder().nome("Incluir _Documento").descr("Cria um novo documento que será posteriormente juntado ao documento corrente.").icone("page_white_add").nameSpace("/app/expediente/doc").acao("editar")
					.params("mobilPaiSel.sigla", mob.getCodigoCompacto()).params("criandoAnexo", "true").exp(new ExPodeIncluirDocumento(mob, titular, lotaTitular)).build());

			addAcao(AcaoVO.builder().nome("Ciência").icone("overlays").nameSpace("/app/expediente/mov").acao("ciencia")
					.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeFazerCiencia(mob, titular, lotaTitular)).classe("once").build());

			addAcao(AcaoVO.builder().nome("Assinar Anexos " + (mob.isVia() ? "da Via" : "do Volume")).descr("Exibe a página de Anexos Pendentes de Assinatura.").icone("script_key").nameSpace("/app/expediente/mov").acao("assinarAnexos")
					.params("sigla", mob.getCodigoCompacto()).params("assinandoAnexosGeral", "true").exp(new ExTemAnexos(mob)).build());
		}

		addAcao(AcaoVO.builder().nome("Desentranhar").descr("Desentranhar este documento, separando ele do documento ao qual foi juntado.").icone("page_white_error").nameSpace("/app/expediente/mov").acao("cancelar_juntada")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeCancelarJuntada(mob, titular, lotaTitular)).classe("once siga-btn-desentranhar").build());
		
		addAcao(AcaoVO.builder().nome("Desapensar").descr("Cancelar o vínculo de apensação existente.").icone("link_delete").nameSpace("/app/expediente/mov").acao("desapensar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeDesapensar(mob, titular, lotaTitular)).classe("once").build());
		
		addAcao(AcaoVO.builder().nome("Receber").descr("Receber o documento, indicando que o trâmite está concluído.").icone("email_open").nameSpace("/app/expediente/mov").acao("receber")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeReceber(mob, titular, lotaTitular)).classe("once").build());
		
		addAcao(AcaoVO.builder().nome("_Tramitar").descr("Enviar o documento para outra lotação ou pessoa.").icone("email_go").nameSpace("/app/expediente/mov").acao("transferir")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeTransferir(mob, titular, lotaTitular)).build());
		
		addAcao(AcaoVO.builder().nome("Tramitar em Paralelo").descr("Enviar o documento simultaneamente para diversas lotações ou pessoas.").icone("email_go").nameSpace("/app/expediente/mov").acao("tramitar_paralelo")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeTramitarEmParalelo(mob, titular, lotaTitular)).build());
		
		addAcao(AcaoVO.builder().nome("Notificar").descr("Notificar determinada lotação ou pessoa sobre a existêcia deste documento.").icone("email_go").nameSpace("/app/expediente/mov").acao("notificar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeNotificar(mob, titular, lotaTitular)).build());
		
		if (!Prop.isGovSP()) {
			addAcao(AcaoVO.builder().nome("_Anotar").descr("Acrescentar uma movimentação de anotação ao documento. As anotações podem ser excluídas a qualquer momento.").icone("note_add").nameSpace("/app/expediente/mov").acao("anotar")
					.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeAnotar(mob, titular, lotaTitular)).build());
		} else {
			addAcao(AcaoVO.builder().nome("_Anotar").icone("note_add")
					.msgConfirmacao("ATENÇÃO: Anotações cadastradas não constituem o documento,"
							+ " são apenas  lembretes ou avisos " 
							+ "	para os usuários com acesso ao documento, podendo ser "
							+ "	excluídas a qualquer tempo.")
					.nameSpace("/app/expediente/mov").acao("anotar")
					.descr("Insere uma pequena observação ao documento. A anotação será exibida nas movimentações do documento, podendo ser excluída a qualquer tempo pela pessoa que a criou.")
					.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeAnotar(mob, titular, lotaTitular))
					.build());
		}

		addAcao(AcaoVO.builder().nome("Definir " + SigaMessages.getMessage("documento.marca")).descr("Marcar o documento com um dos marcadores disponíveis para facilitar a localização futura.").icone("folder_star").modal("definirMarcaModal")
				.exp(new ExPodeMarcar(mob, titular, lotaTitular)).build());
		
		if (mob.isVia() || mob.isVolume()) {
			addAcao(AcaoVO.builder().nome("Ane_xar").descr("Captura um novo PDF e junta ele ao documento.").icone("attach").nameSpace("/app/expediente/mov").acao("anexar")
					.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeAnexarArquivo(mob, titular, lotaTitular)).build());
			
			addAcao(AcaoVO.builder().nome("Incluir _Cópia").descr("Juntar uma cópia de um outro documento.").icone("page_white_copy").nameSpace("/app/expediente/mov").acao("copiar")
					.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeCopiar(mob, titular, lotaTitular)).build());
		}
		
		addAcao(AcaoVO.builder().nome("Ar_q. Corrente").descr("Arquivar este documento no Arquivo Corrente.").icone("box_add").nameSpace("/app/expediente/mov").acao("arquivar_corrente_gravar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeArquivarCorrente(mob, titular, lotaTitular)).classe("once siga-btn-arq-corrente").build());

		addAcao(AcaoVO.builder().nome("Concluir").descr("Indicar que o trâmite paralelo foi concluído ou dar ciência da notificação.").icone("tick").nameSpace("/app/expediente/mov").acao("concluir_gravar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeConcluir(mob, titular, lotaTitular)).post(true).classe("once").build());

		addAcao(AcaoVO.builder().nome("Indicar para Guarda Permanente").descr("Marcar o documento para ser recolhido ao Arquivo Permanente.").icone("building_go").nameSpace("/app/expediente/mov").acao("indicar_permanente")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeIndicarPermanente(mob, titular, lotaTitular)).classe("once").build());

		addAcao(AcaoVO.builder().nome("Reverter Ind. Guarda Permanente").icone("building_delete").nameSpace("/app/expediente/mov").acao("reverter_indicacao_permanente")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeReverterIndicacaoPermanente(mob, titular, lotaTitular)).classe("once").build());

		addAcao(AcaoVO.builder().nome("Retirar de Edital de Eliminação").descr("Remover o documento do Edital de Eliminação.").icone("page_red").nameSpace("/app/expediente/mov").acao("retirar_de_edital_eliminacao")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeRetirarDeEditalDeEliminacao(mob, titular, lotaTitular)).classe("once").build());

		addAcao(AcaoVO.builder().nome("Reclassificar").icone("table_edit").descr("Alterar a classificação documental.").nameSpace("/app/expediente/mov").acao("reclassificar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeReclassificar(mob, titular, lotaTitular)).build());

		addAcao(AcaoVO.builder().nome("Avaliar").descr("Alterar a classificação documental por efeito de uma avaliação.").icone("table").nameSpace("/app/expediente/mov").acao("avaliar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeAvaliar(mob, titular, lotaTitular)).build());

		if (!Prop.isGovSP()) {
			addAcao(AcaoVO.builder().nome("So_brestar").icone("hourglass_add").nameSpace("/app/expediente/mov").acao("sobrestar_gravar")
					.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeSobrestar(mob, titular, lotaTitular)).classe("once").build());
		} else {
			addAcao(AcaoVO.builder().nome("So_brestar").msgConfirmacao("Você deseja SOBRESTAR este documento?").icone("hourglass_add").nameSpace("/app/expediente/mov").acao("sobrestar_gravar")
					.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeSobrestar(mob, titular, lotaTitular)).classe("once").build());
		}
		
		addAcao(AcaoVO.builder().nome("Recolher ao Arq. Permanente").descr("Recolhe o documento ao Arquivo Permanente.").icone("building_add").nameSpace("/app/expediente/mov").acao("arquivar_permanente_gravar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeExibirBotaoDeArquivarPermanente(mob, titular, lotaTitular)).classe("once").build());

		addAcao(AcaoVO.builder().nome("Arq. Intermediário").descr("Transfere o documento para o Arquivo Intermediário.").icone("package_add").nameSpace("/app/expediente/mov").acao("arquivar_intermediario")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeExibirBotaoDeArquivarIntermediario(mob, titular, lotaTitular)).classe("once").build());

		addAcao(AcaoVO.builder().nome("Desar_q. Corrente").descr("Desarquiva o documento do Arquivo Corrente e retoma o trâmite.").icone("box_delete").nameSpace("/app/expediente/mov").acao("reabrir_gravar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeDesarquivarCorrente(mob, titular, lotaTitular)).classe("once").build());

		addAcao(AcaoVO.builder().nome("Desarq. Intermediário").descr("Desarquiva o documento do Arquivo Intermediário.").icone("package_delete").nameSpace("/app/expediente/mov").acao("desarquivar_intermediario_gravar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeExibirBotaoDeDesarquivarIntermediario(mob, titular, lotaTitular)).classe("once").build());

		addAcao(AcaoVO.builder().nome("Desso_brestar").descr("Cancela o estado de sobrestamento do documento, retomando o trâmite.").icone("hourglass_delete").nameSpace("/app/expediente/mov").acao("desobrestar_gravar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeDessobrestar(mob, titular, lotaTitular)).classe("once").build());

		addAcao(AcaoVO.builder().nome("_Juntar").descr("Junta o documento a um documento pai, formando ou complementando um dossiê.").icone("page_white_go").nameSpace("/app/expediente/mov").acao("juntar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeJuntar(mob, titular, lotaTitular)).classe("once").build());

		addAcao(AcaoVO.builder().nome("Apensar").descr("Agrupa este documento a um outro documento para fins de trâmite e aquivamento.").icone("link_add").nameSpace("/app/expediente/mov").acao("apensar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeApensar(mob, titular, lotaTitular)).classe("once").build());

		addAcao(AcaoVO.builder().nome("Atribuir Prazo de Assinatura").icone("date_previous").nameSpace("/app/expediente/mov").acao("definir_prazo_assinatura")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeDefinirPrazoAssinatura(mob, titular, lotaTitular)).classe("once").build());
		
		// Não aparece a opção de Cancelar Movimentação para documentos
		// temporários
		
		ExMovimentacao ultimaMovNaoCancelada = mob.getUltimaMovimentacaoNaoCancelada();
		if (mob.getExDocumento().isFinalizado()	&& ultimaMovNaoCancelada != null) {
			
			//Cria lista de Movimentações que não podem ser canceladas
			List<ITipoDeMovimentacao> listaMovimentacoesNaoCancelavel = new ArrayList<>();
			listaMovimentacoesNaoCancelavel.add(ExTipoDeMovimentacao.CIENCIA);
			listaMovimentacoesNaoCancelavel.add(ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO);
			listaMovimentacoesNaoCancelavel.add(ExTipoDeMovimentacao.CONTROLE_DE_COLABORACAO);
			listaMovimentacoesNaoCancelavel.add(ExTipoDeMovimentacao.RESTRINGIR_ACESSO);
			listaMovimentacoesNaoCancelavel.add(ExTipoDeMovimentacao.REFAZER);
			listaMovimentacoesNaoCancelavel.add(ExTipoDeMovimentacao.ASSINATURA_POR);
			listaMovimentacoesNaoCancelavel.add(ExTipoDeMovimentacao.GERAR_PROTOCOLO);
			listaMovimentacoesNaoCancelavel.add(ExTipoDeMovimentacao.PUBLICACAO_PORTAL_TRANSPARENCIA);
			listaMovimentacoesNaoCancelavel.add(ExTipoDeMovimentacao.ENVIO_SIAFEM);
			listaMovimentacoesNaoCancelavel.add(ExTipoDeMovimentacao.GERAR_LINK_PUBLICO_PROCESSO);
			listaMovimentacoesNaoCancelavel.add(ExTipoDeMovimentacao.ENVIO_PARA_VISUALIZACAO_EXTERNA);
			listaMovimentacoesNaoCancelavel.add(ExTipoDeMovimentacao.ORDEM_ASSINATURA);
			
			if (!listaMovimentacoesNaoCancelavel.contains(ultimaMovNaoCancelada.getExTipoMovimentacao())) {
				addAcao(AcaoVO.builder().nome("Desfa_zer "
						+ mob.getDescricaoUltimaMovimentacaoNaoCancelada()).icone("arrow_undo").nameSpace("/app/expediente/mov").acao("cancelarMovimentacao")
						.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeCancelarMovimentacao(mob, titular, lotaTitular)).msgConfirmacao(SigaMessages.getMessage("documento.confirma.cancelamento") + "("
								+ mob.getDescricaoUltimaMovimentacaoNaoCancelada()
								+ ")?").classe("once").build());
			}
			listaMovimentacoesNaoCancelavel = null;
		}
		ultimaMovNaoCancelada = null;
		
		// exibir+completo,
		// confirmacao

		addAcao(AcaoVO.builder().nome("Encerrar Volume").descr("Fecha o volume corrente do processo.").icone("folder_page_white").nameSpace("/app/expediente/mov").acao("encerrar_volume")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeEncerrarVolume(mob, titular, lotaTitular)).msgConfirmacao("Confirma o encerramento do volume?").classe("once").build());
		
		addAcao(AcaoVO.builder().nome("Cancelar Via").descr("Cancela a via corrente, impedindo futuras movimentações e trâmites.").icone("cancel").nameSpace("/app/expediente/mov").acao("cancelarMovimentacao")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeCancelarVia(mob, titular, lotaTitular)).msgConfirmacao("Confirma o encerramento da via?").classe("once").build());
		
		addAcao(AcaoVO.builder().nome("Autuar").descr("Junta este documento em um novo documento que será criado.").icone("page_white_get").nameSpace("/app/expediente/doc").acao("editar")
				.params("idMobilAutuado", Long.toString(mob.getId())).params("autuando", "true").exp(new ExPodeAutuarDocumento(mob, titular, lotaTitular)).classe("once").build());

		addAcao(AcaoVO.builder().nome("Desfazer Ciência").icone("arrow_undo").nameSpace("/app/expediente/mov").acao("cancelar_ciencia")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeCancelarCiencia(mob, titular, lotaTitular)).classe("once").build());
		
	}

	public String getMarcadoresEmHtml(List<ExMarca> marcasAtivas, DpPessoa pess, DpLotacao lota) {
		StringBuilder sb = new StringBuilder();

		if (pess != null && lota != null) {
			// Marcacoes para a propria lotacao e para a propria pessoa ou sem
			// informacao de pessoa
			//
			for (ExMarca mar : getMarcasAtivas()) {
				if (incluirMarcaEmHtml(mar)
						&& marcaNaoTemLotacaoOuEDeDeterminadaLotacao(mar, lota)
						&& marcaNaoTemPessoaOuEDeDeterminadaPessoa(mar, pess)) {
					if (sb.length() > 0)
						sb.append(", ");
					sb.append(mar.getCpMarcador().getDescrMarcador());
				}
			}

			// Marcacoes para a propria lotacao e para outra pessoa
			//
			if (sb.length() == 0) {
				for (ExMarca mar : getMarcasAtivas()) {
					if (incluirMarcaEmHtml(mar)) {
						if (sb.length() > 0)
							sb.append(", ");
						if (marcaEDeDeterminadaLotacao(mar, lota)
								&& marcaTemPessoaDiferente(mar, pess)) {
							sb.append(mar.getCpMarcador().getDescrMarcador());
							sb.append(" [<span title=\"");
							sb.append(mar.getDpPessoaIni().getNomePessoa());
							sb.append("\">");
							sb.append(mar.getDpPessoaIni().getPessoaAtual().getSigla());
							sb.append("</span>]");
						}
					}
				}
			}
		}

		// Marcacoes para qualquer outra pessoa ou lotacao
		//
		for (ExMarca mar : getMarcasAtivas()) {
			if ((sb.length() == 0 && incluirMarcaEmHtml(mar))
					|| (incluirMarcaEmHtmlDeOutraPessoaELotacao(mar)
							&& !(marcaNaoTemLotacaoOuEDeDeterminadaLotacao(mar, lota)
							&& marcaNaoTemPessoaOuEDeDeterminadaPessoa(mar, pess)))) {
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
						sb.append(mar.getDpPessoaIni().getPessoaAtual().getSigla());
						sb.append("</span>]");
					}
					sb.append("]");
				}
			}
		}
		if (sb.length() == 0)
			return null;
		return sb.toString();
	}

	public boolean marcaTemPessoaDiferente(ExMarca mar, DpPessoa pess) {
		return mar.getDpPessoaIni() != null
				&& !pess.getIdInicial().equals(mar.getDpPessoaIni().getIdInicial());
	}

	public boolean marcaEDeDeterminadaLotacao(ExMarca mar, DpLotacao lota) {
		return mar.getDpLotacaoIni() != null
				&& lota.getIdInicial().equals(mar.getDpLotacaoIni().getIdInicial());
	}

	public boolean marcaNaoTemPessoaOuEDeDeterminadaPessoa(ExMarca mar, DpPessoa pess) {
		return mar.getDpPessoaIni() == null
				|| pess.getIdInicial().equals(mar.getDpPessoaIni().getIdInicial());
	}

	public boolean marcaNaoTemLotacaoOuEDeDeterminadaLotacao(ExMarca mar, DpLotacao lota) {
		return marcaEDeDeterminadaLotacao(mar, lota)
				|| mar.getDpLotacaoIni() == null;
	}

	public boolean incluirMarcaEmHtml(ExMarca mar) {
		return mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.EM_TRANSITO.getId()
			    	&& mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.EM_TRANSITO_ELETRONICO.getId()
				    && mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.DOCUMENTO_ASSINADO_COM_SENHA.getId()
				    && mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.MOVIMENTACAO_ASSINADA_COM_SENHA.getId()
				    && mar.getCpMarcador().getIdMarcador() != CpMarcadorEnum.MOVIMENTACAO_CONFERIDA_COM_SENHA.getId();
	}

	public boolean incluirMarcaEmHtmlDeOutraPessoaELotacao(ExMarca mar) {
		return mar.getCpMarcador().getIdMarcador() == CpMarcadorEnum.EM_ANDAMENTO.getId()
			    	|| mar.getCpMarcador().getIdMarcador() == CpMarcadorEnum.CAIXA_DE_ENTRADA.getId();
	}

	public String getDescricaoCompletaEMarcadoresEmHtml(DpPessoa pess,
			DpLotacao lota) {
		String m = getMarcadoresEmHtml(marcasAtivas, pess, lota);
		if (m == null)
			return getMob().getDescricaoCompleta();
		return getMob().getDescricaoCompleta() + " - " + m;
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
	
	public List<ExMobilVO> getExpedientesJuntadosNaoAssinados() {
		return expedientesJuntadosNaoAssinados;
	}

	public List<ExMovimentacaoVO> getPendenciasDeAnexacao() {
		return pendenciasDeAnexacao;
	}

	public List<ExMovimentacaoVO> getPendenciasDeColaboracao() {
		return pendenciasDeColaboracao;
	}

	@Override
	public String toString() {
		return getSigla() + "[" + getAcoes() + "] ";
	}

	public List<DuracaoVO> getDuracoes() {
		return duracoes;
	}

	public void setDuracoes(List<DuracaoVO> duracoes) {
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
		return anexosNaoAssinados.size() > 0
				|| despachosNaoAssinados.size() > 0
				|| expedientesFilhosNaoJuntados.size() > 0
				|| expedientesJuntadosNaoAssinados.size() > 0
				|| pendenciasDeAnexacao.size() > 0
				|| pendenciasDeColaboracao.size() > 0
				|| pendenciaProximoModelo != null;
	}
}