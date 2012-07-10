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

import br.gov.jfrj.siga.base.FormataTamanhoDeArquivo;
import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;

public class ExMobilVO extends ExVO {
	ExMobil mob;
	String sigla;
	List apensos = new ArrayList<ExMobilVO>();
	List filhos = new ArrayList<ExDocumentoVO>();
	List filhosNaoCancelados = new ArrayList<ExDocumentoVO>();
	List<ExMovimentacaoVO> movs = new ArrayList<ExMovimentacaoVO>();
	List<DuracaoVO> duracoes = new ArrayList<DuracaoVO>();
	Long byteCount;
	Integer pagInicial;
	Integer pagFinal;
	String tamanhoDeArquivo;

	public List<ExMovimentacaoVO> getMovs() {
		return movs;
	}

	public void setMovs(List<ExMovimentacaoVO> movs) {
		this.movs = movs;
	}

	public ExMobil getMob() {
		return mob;
	}

	public void setMob(ExMobil mob) {
		this.mob = mob;
	}

	public ExMobilVO(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular,
			boolean completo) throws Exception {
		this.mob = mob;
		this.sigla = mob.getSigla();

		if (!completo)
			return;

		long tempoIni = System.currentTimeMillis();

		/*
		 * Markenson: O código abaixo foi comentado por questões de desempenho.
		 * Deve ser estudada uma maneira mais eficiente de calcular o tamanho dos PDFs
		 * */
		
		// byteCount, pagIni e pagFim
//		if (mob.getExDocumento().isEletronico()){
			
//			byteCount = mob.getByteCount();
//			if (byteCount != null && byteCount > 0)
//				tamanhoDeArquivo = FormataTamanhoDeArquivo
//						.converterEmTexto(byteCount);
//		}


		for (ExMobil m : mob.getApensosDiretosExcetoVolumeApensadoAoProximo()) {
			apensos.add(new ExMobilVO(m, titular, lotaTitular, false));
		}
		System.out.println(mob.getExDocumento().getCodigoString()
				+ ": aExibir - mobil " + mob.getNumSequencia()
				+ " - adicao apensos: "
				+ (System.currentTimeMillis() - tempoIni));

		tempoIni = System.currentTimeMillis();
		for (ExDocumento d : mob.getExDocumentoFilhoSet()) {
			filhos.add(new ExDocumentoVO(d, null, titular, lotaTitular, false));
			if (d.getDtFechamento() == null || !d.isCancelado())
				filhosNaoCancelados.add(new ExDocumentoVO(d, null, titular,
						lotaTitular, false));
		}
		System.out.println(mob.getExDocumento().getCodigoString()
				+ ": aExibir - mobil " + mob.getNumSequencia()
				+ " - adicao filhos: "
				+ (System.currentTimeMillis() - tempoIni));

		tempoIni = System.currentTimeMillis();
		addAcoes(mob, titular, lotaTitular);
		System.out
				.println(mob.getExDocumento().getCodigoString()
						+ ": aExibir - mobil " + mob.getNumSequencia()
						+ " - adicao acoes: "
						+ (System.currentTimeMillis() - tempoIni));

		tempoIni = System.currentTimeMillis();
		for (ExMovimentacao mov : mob.getCronologiaSet()) {
			movs.add(new ExMovimentacaoVO(this, mov, titular, lotaTitular));
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
					|| (movVO.mov
							.getExTipoMovimentacao()
							.getId()
							.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO) && (!movVO
							.isCancelada()))) {
				if (i == movs.size()) {
					duracaoSpan++;
				}
				if (i == movs.size() || movVOUlt == null)
					movVOUlt = movVO;
				SigaCalendar lIni = new SigaCalendar(movVOIni.mov.getDtIniMov()
						.getTime());
				SigaCalendar lFim = new SigaCalendar(movVOUlt.mov.getDtIniMov()
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
		for (ExMovimentacaoVO movVO : movs) {
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

		System.out.println(mob.getExDocumento().getCodigoString()
				+ ": aExibir - mobil " + mob.getNumSequencia()
				+ " - adicao movs: " + (System.currentTimeMillis() - tempoIni));
	}

	/**
	 * @param mob
	 * @param titular
	 * @param lotaTitular
	 * @throws Exception
	 */
	private void addAcoes(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular)
			throws Exception {
		if (!mob.isGeral()) {
			addAcao("application_side_tree", "Visualizar Dossiê",
					"/expediente/doc",
					"exibirProcesso",
					Ex.getInstance().getComp()
							.podeVisualizarImpressao(titular, lotaTitular, mob));

			addAcao("printer", "Visualizar Impressão",
					"/expediente/doc",
					"pdf",
					Ex.getInstance().getComp()
							.podeVisualizarImpressao(titular, lotaTitular, mob),
					null, "&popup=true", null, null);
		}
		addAcao("link_break", "Desentranhar",
				"/expediente/mov",
				"cancelar_juntada",
				Ex.getInstance().getComp()
						.podeCancelarJuntada(titular, lotaTitular, mob));

		addAcao("link_delete", "Desapensar", "/expediente/mov", "desapensar", Ex.getInstance()
				.getComp().podeDesapensar(titular, lotaTitular, mob));

		addAcao("email_open", "Receber", "/expediente/mov", "receber", Ex.getInstance()
				.getComp().podeReceber(titular, lotaTitular, mob));

		addAcao("email_edit", "Despachar/Transferir",
				"/expediente/mov",
				"transferir",
				Ex.getInstance().getComp()
						.podeDespachar(titular, lotaTitular, mob));

		if (mob.isVia() || mob.isVolume()) {
			addAcao("attach","Anexar Arquivo",
					"/expediente/mov",
					"anexar",
					Ex.getInstance().getComp()
							.podeAnexarArquivo(titular, lotaTitular, mob));
			addAcao("tag_yellow", "Fazer Anotação",
					"/expediente/mov",
					"anotar",
					Ex.getInstance().getComp()
							.podeFazerAnotacao(titular, lotaTitular, mob));
		}

		addAcao("package", "Arq. Corrente",
				"/expediente/mov",
				"arquivar_corrente_gravar",
				Ex.getInstance().getComp()
						.podeArquivarCorrente(titular, lotaTitular, mob));

		addAcao("box","Arq. Permanente", "/expediente/mov",
				"arquivar_permanente_gravar", Ex.getInstance().getComp()
						.podeArquivarPermanente(titular, lotaTitular, mob));

		addAcao("package_go","Desarquivar",
				"/expediente/mov",
				"desarquivar_gravar",
				Ex.getInstance().getComp()
						.podeDesarquivar(titular, lotaTitular, mob));

		addAcao("link","Juntar", "/expediente/mov", "juntar", Ex.getInstance()
				.getComp().podeJuntar(titular, lotaTitular, mob));

		addAcao("page_find","Vincular", "/expediente/mov", "referenciar", Ex.getInstance()
				.getComp().podeReferenciar(titular, lotaTitular, mob));

		addAcao("link_add","Apensar", "/expediente/mov", "apensar", Ex.getInstance()
				.getComp().podeApensar(titular, lotaTitular, mob));

		// Não aparece a opção de Cancelar Movimentação para documentos
		// temporários
		if (mob.getExDocumento().getDtFechamento() != null
				&& mob.getUltimaMovimentacaoNaoCancelada() != null
				&& mob.getUltimaMovimentacaoNaoCancelada()
						.getExTipoMovimentacao().getIdTpMov() != ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO)
			addAcao("arrow_undo", "Desfazer "
					+ mob.getDescricaoUltimaMovimentacaoNaoCancelada(),
					"/expediente/mov",
					"cancelarMovimentacao",
					Ex.getInstance()
							.getComp()
							.podeCancelarMovimentacao(titular, lotaTitular, mob),
					"Confirma o cancelamento da última movimentação("
							+ mob.getDescricaoUltimaMovimentacaoNaoCancelada()
							+ ")?", null, null, null); // popup,
		// exibir+completo,
		// confirmacao

		addAcao("folder_page_white", "Encerrar Volume", "/expediente/mov", "encerrar_gravar", Ex
				.getInstance().getComp()
				.podeEncerrar(titular, lotaTitular, mob),
				"Confirma o encerramento do volume?", null, null, null);

		addAcao("cancel", "Cancelar Via",
				"/expediente/mov",
				"cancelarMovimentacao",
				Ex.getInstance().getComp()
						.podeCancelarVia(titular, lotaTitular, mob),
				"Confirma o cancelamento da via?", null, null, null);
	}

	public String getMarcadoresEmHtml(DpPessoa pess, DpLotacao lota) {
		StringBuilder sb = new StringBuilder();

		// Marcacoes para a propria lotacao e para a propria pessoa ou sem
		// informacao de pessoa
		//
		for (ExMarca mar : getMob().getExMarcaSet()) {
			if (mar.getCpMarcador().getIdMarcador() != CpMarcador.MARCADOR_EM_TRANSITO
					&& mar.getCpMarcador().getIdMarcador() != CpMarcador.MARCADOR_EM_TRANSITO_ELETRONICO
					&& ((mar.getDpLotacaoIni() != null && lota.getIdInicial()
							.equals(mar.getDpLotacaoIni().getIdInicial())) || mar
							.getDpLotacaoIni() == null)
					&& (mar.getDpPessoaIni() == null || pess.getIdInicial()
							.equals(mar.getDpPessoaIni().getIdInicial()))) {
				if (sb.length() > 0)
					sb.append(", ");
				sb.append(mar.getCpMarcador().getDescrMarcador());
			}
		}

		// Marcacoes para a propria lotacao e para outra pessoa
		//
		if (sb.length() == 0) {
			for (ExMarca mar : getMob().getExMarcaSet()) {
				if (mar.getCpMarcador().getIdMarcador() != CpMarcador.MARCADOR_EM_TRANSITO
						&& mar.getCpMarcador().getIdMarcador() != CpMarcador.MARCADOR_EM_TRANSITO_ELETRONICO) {
					if (sb.length() > 0)
						sb.append(", ");
					if ((mar.getDpLotacaoIni() != null && lota.getIdInicial()
							.equals(mar.getDpLotacaoIni().getIdInicial()))
							&& (mar.getDpPessoaIni() != null && !pess
									.getIdInicial()
									.equals(mar.getDpPessoaIni().getIdInicial()))) {
						sb.append(mar.getCpMarcador().getDescrMarcador());
						sb.append(" [");
						sb.append(mar.getDpPessoaIni().getSigla());
						sb.append("]");
					}
				}
			}
		}

		// Marcacoes para qualquer outra pessoa ou lotacao
		//
		if (sb.length() == 0) {
			for (ExMarca mar : getMob().getExMarcaSet()) {
				if (mar.getCpMarcador().getIdMarcador() != CpMarcador.MARCADOR_EM_TRANSITO
						&& mar.getCpMarcador().getIdMarcador() != CpMarcador.MARCADOR_EM_TRANSITO_ELETRONICO) {
					if (sb.length() > 0)
						sb.append(", ");
					sb.append(mar.getCpMarcador().getDescrMarcador());
					if (mar.getDpLotacaoIni() != null
							|| mar.getDpPessoaIni() != null) {
						sb.append(" [");
						if (mar.getDpLotacaoIni() != null) {
							sb.append(mar.getDpLotacaoIni().getSigla());
						}
						if (mar.getDpPessoaIni() != null) {
							if (mar.getDpLotacaoIni() != null) {
								sb.append(", ");
							}
							sb.append(mar.getDpPessoaIni().getSigla());
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

	public String getDescricaoCompletaEMarcadoresEmHtml(DpPessoa pess,
			DpLotacao lota) {
		String m = getMarcadoresEmHtml(pess, lota);
		if (m == null)
			return getMob().getDescricaoCompleta();
		return getMob().getDescricaoCompleta() + " - "
				+ getMarcadoresEmHtml(pess, lota);
	}

	public String getSigla() {
		return sigla;
	}

	public List getApensos() {
		return apensos;
	}

	public List getFilhos() {
		return filhos;
	}

	public List getFilhosNaoCancelados() {
		return filhosNaoCancelados;
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

}
