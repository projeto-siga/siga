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

import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_DE_ARQUIVO_AUXILIAR;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANOTACAO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_PERMANENTE;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_COPIA;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESAPENSACAO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO_TRANSFERENCIA;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ELIMINACAO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ENCERRAMENTO_DE_VOLUME;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_GERAR_PROTOCOLO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA_EXTERNO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICACAO_PUBL_BI;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ORDENACAO_ORIGINAL_DOCUMENTO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO_TRANSITORIO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_REFERENCIA;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_REORDENACAO_DOCUMENTO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.hasDespacho;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelarMarcacao;
import br.gov.jfrj.siga.ex.util.ProcessadorReferencias;

public class ExMovimentacaoVO extends ExVO {
	private static final transient String JWT_FIXED_HEADER = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.";
	private static final transient String JWT_FIXED_HEADER_REPLACEMENT = "!";
	
	transient ExMovimentacao mov;
	
	transient ExMobilVO mobVO;
	String classe;
	boolean originadaAqui;
	boolean desabilitada;
	boolean cancelada;
	String descricao;
	long idTpMov;
	String complemento;
	Map<String, ExParteVO> parte = new TreeMap<String, ExParteVO>();
	Date dtIniMov;
	String dtRegMovDDMMYYHHMMSS;
	String dtFimMovDDMMYYHHMMSS;
	String descrTipoMovimentacao;
	long idMov;
	int duracaoSpan;
	int duracaoSpanExibirCompleto;
	String duracao;
	String mimeType;
	String lotaCadastranteSigla;
	String exTipoMovimentacaoSigla;
	String tempoRelativo;
	boolean podeExibirNoSigale;
	private String subscritor;

	public ExMobilVO getMobVO() {
		return mobVO;
	}

	public ExMovimentacaoVO(ExMobilVO mobVO, ExMovimentacao mov, DpPessoa cadastrante, DpPessoa titular,
			DpLotacao lotaTitular, boolean serializavel) {
		originadaAqui = (mov.getExMobil().getId().equals(mobVO.id));
		this.mov = mov;
		this.mobVO = mobVO;
		originadaAqui = (mov.getExMobil().getId().equals(getMobVO().id));
		idTpMov = mov.getExTipoMovimentacao().getIdTpMov();
		dtIniMov = mov.getDtIniMov();

		mimeType = (mov.getConteudoTpMov() == null) ? "" : mov.getConteudoTpMov();


		this.idMov = mov.getIdMov();
		this.dtRegMovDDMMYYHHMMSS = mov.getDtRegMovDDMMYYHHMMSS();
		this.tempoRelativo = Data.calcularTempoRelativo(mov.getDtIniMov());
		this.descrTipoMovimentacao = mov.getDescrTipoMovimentacao();
		this.cancelada = mov.getExMovimentacaoCanceladora() != null;
		this.lotaCadastranteSigla = mov.getLotaCadastrante() != null ? mov.getLotaCadastrante().getSigla() : null;
		this.exTipoMovimentacaoSigla = mov.getExTipoMovimentacao().getSigla();

		if (mov.getLotaCadastrante() != null)
			parte.put("lotaCadastrante", new ExParteVO(mov.getLotaCadastrante()));
		if (mov.getCadastrante() != null)
			parte.put("cadastrante", new ExParteVO(mov.getCadastrante()));
		if (mov.getLotaSubscritor() != null)
			parte.put("lotaSubscritor", new ExParteVO(mov.getLotaSubscritor()));
		if (mov.getSubscritor() != null)
			parte.put("subscritor", new ExParteVO(mov.getSubscritor()));
		if (mov.getLotaResp() != null)
			parte.put("lotaResp", new ExParteVO(mov.getLotaResp()));
		if (mov.getResp() != null)
			parte.put("resp", new ExParteVO(mov.getResp()));

		descricao = mov.getObs();

		if (mov.getIdTpMov().equals(TIPO_MOVIMENTACAO_ANEXACAO))
			descricao = StringUtils.isNotBlank(mov.getDescrMov()) ? mov.getDescrMov() : mov.getNmArqMov();

		if (mov.getIdTpMov().equals(TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA) || mov.getIdTpMov().equals(TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO)) {
			descricao += Ex.getInstance().getBL().extraiPersonalizacaoAssinatura(mov,false);
		}

		addAcoes(mov, cadastrante, titular, lotaTitular);

		calcularClasse(mov);

		desabilitada = (mov.getExMovimentacaoRef() != null && mov.getExMovimentacaoRef().isCancelada() && !mov.getIdTpMov().equals(TIPO_MOVIMENTACAO_MARCACAO) && !mov.getIdTpMov().equals(TIPO_MOVIMENTACAO_RECEBIMENTO))
				|| mov.getExMovimentacaoCanceladora() != null
				|| mov.getIdTpMov().equals(TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO);
		
		if (mov.getIdTpMov().equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL))
			complemento = (mov.getSubscritor() != null ? mov.getSubscritor().getDescricao() : mov.getLotaSubscritor() != null ? mov.getLotaSubscritor().getDescricao() : null) + " - " + mov.getExPapel().getDescPapel() + ". ";
		
		if (mov.getIdTpMov().equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO))
			subscritor = mov.getSubscritor().getNomePessoa();
				
		if (serializavel) {
			this.mov = null;
		}
		
	}

	/**
	 * @param mov
	 * @param titular
	 * @param lotaTitular
	 * @throws Exception
	 */
	private void addAcoes(ExMovimentacao mov, DpPessoa cadastrante, DpPessoa titular, DpLotacao lotaTitular) {
		if (complemento == null)
			complemento = "";

		if (idTpMov == TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO) {
			descricao = "";
			addAcao(null, "Verificar", "/app/expediente/mov", "assinar_verificar", true, null,
					"&ajax=true&id=" + mov.getIdMov().toString(), null, null, null);
		}

		if (idTpMov == TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO
				|| idTpMov == TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO) {
			descricao = Texto.maiusculasEMinusculas(mov.getObs());
		}

		if (idTpMov == TIPO_MOVIMENTACAO_ANOTACAO) {
			descricao = mov.getObs();
			addAcao(null, "Excluir", "/app/expediente/mov", "excluir",
					Ex.getInstance().getComp().podeExcluirAnotacao(titular, lotaTitular, mov.mob(), mov));
		}

		if (idTpMov == TIPO_MOVIMENTACAO_VINCULACAO_PAPEL) {
			addAcao(null, "Cancelar", "/app/expediente/mov", "cancelar",
					Ex.getInstance().getComp().podeCancelarVinculacaoPapel(titular, lotaTitular, mov.mob(), mov));
		}

		if (idTpMov == TIPO_MOVIMENTACAO_MARCACAO) {
			descricao = mov.getMarcador().getDescrMarcador();
			if (mov.getSubscritor() != null) 
				descricao += ", interessado: " + mov.getSubscritor().getPrimeiroNomeEIniciais();
			if (mov.getLotaSubscritor() != null) 
				descricao += (mov.getSubscritor() == null ? ", lotação interessada: " : "/") + mov.getLotaSubscritor().getSiglaCompleta();
			if (mov.getDtParam1() != null) 
				descricao += ", data de exibição: " + Data.formatDataETempoRelativo(mov.getDtParam1());
			if (mov.getDtParam2() != null) 
				descricao += ", prazo final: " + Data.formatDataETempoRelativo(mov.getDtParam2());
			if (mov.getObs() != null && mov.getObs().trim().length() > 0)
				descricao += ", obs: " + mov.getObs();
			addAcao(AcaoVO.builder().nome("Cancelar").nameSpace("/app/expediente/mov")
					.acao("cancelar_movimentacao_gravar").params("sigla", mov.mob().getCodigoCompacto())
					.params("id", mov.getIdMov().toString()).post(true)
					.exp(new ExPodeCancelarMarcacao(mov, titular, lotaTitular)).build());
		}

		if (idTpMov == TIPO_MOVIMENTACAO_REFERENCIA) {
			addAcao(null, "Cancelar", "/app/expediente/mov", "cancelar",
					Ex.getInstance().getComp().podeCancelarVinculacaoDocumento(titular, lotaTitular, mov.mob(), mov));
		}

		if (idTpMov == TIPO_MOVIMENTACAO_ANEXACAO_DE_ARQUIVO_AUXILIAR) {
			addAcao(getIcon(), mov.getNmArqMov(), "/app/arquivo", "exibir", mov.getNmArqMov() != null, null,
					"&arquivo=" + mov.getReferencia(), null, null, null);
			String pwd = getWebdavPassword();
			if (cadastrante != null && pwd != null && (isWord() || isExcel() || isPresentation()) ) {
				String sApp = "word";
				String sNome = "Word";
				if (isExcel()) {
					sApp = "excel";
					sNome = "Excel";
				}
				if (isPresentation()) {
					sApp = "powerpoint";
					sNome = "PowerPoint";
				}
				String token = mov.mob().getReferencia() + "_" + cadastrante.getSiglaCompleta() + "_"
						+ titular.getSiglaCompleta() + "_" + lotaTitular.getSiglaCompleta();

				token = getWebdavJwtToken(mov, cadastrante, titular, lotaTitular, pwd);

				addAcao(null, "Editar no " + sNome, sApp
						+ ":ofe|u|__scheme__://__serverName__:__serverPort____contextPath__/webdav/" + token,
						mov.getNmArqMov(), true, null, null, null, null, null);
			}

			if (!mov.isCancelada() && !mov.mob().doc().isSemEfeito()) {
				addAcao(null, "Cancelar", "/app/expediente/mov", "cancelar", true);
			}
		}

		if (mov.getNumPaginas() != null || idTpMov == TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO
				|| idTpMov == TIPO_MOVIMENTACAO_ANEXACAO) {
			// A acao pode ser melhorada para mostrar o icone do pdf antes do
			// nome do arquivo.
			// <c:url var='anexo' value='/anexo/${mov.idMov}/${mov.nmArqMov}' />
			// tipo="${mov.conteudoTpMov}" />
			addAcao(null, mov.getNmArqMov(), "/app/arquivo", "exibir", mov.getNmArqMov() != null, null,
					"&popup=true&arquivo=" + mov.getReferenciaPDF(), null, null, null);

			if (idTpMov == TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO) {
				addAcao(null, "Excluir", "/app/expediente/mov", "excluir",
						Ex.getInstance().getComp().podeExcluirCosignatario(titular, lotaTitular, mov.mob(), mov));
			}

			if (idTpMov == TIPO_MOVIMENTACAO_ANEXACAO) {
				if (!mov.isCancelada() && !mov.mob().doc().isSemEfeito() && !mov.mob().isEmTransito(titular, lotaTitular)) {
					addAcao(null, "Excluir", "/app/expediente/mov", "excluir",
							Ex.getInstance().getComp().podeExcluirAnexo(titular, lotaTitular, mov.mob(), mov));
					addAcao(null, "Cancelar", "/app/expediente/mov", "cancelar",
							Ex.getInstance().getComp().podeCancelarAnexo(titular, lotaTitular, mov.mob(), mov));
					addAcao(null, "Assinar/Autenticar", "/app/expediente/mov", "exibir", true, null, "&popup=true",
							null, null, null);

					addAcao("script_key", "Autenticar", "/app/expediente/mov", "autenticar_mov",
							Ex.getInstance().getComp().podeAutenticarMovimentacao(titular, lotaTitular, mov), null,
							"&popup=true&autenticando=true", null, null, null);
				}
			}

			if (hasDespacho(idTpMov)) {
				if (!mov.mob().doc().isSemEfeito())
					addAcao(null, "Cancelar", "/app/expediente/mov", "cancelar",
							Ex.getInstance().getComp().podeCancelarDespacho(titular, lotaTitular, mov.mob(), mov));
			}

			if (idTpMov != TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO
					&& idTpMov != TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO
					&& idTpMov != TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO
					&& idTpMov != TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO && idTpMov != TIPO_MOVIMENTACAO_ANEXACAO
					&& idTpMov != TIPO_MOVIMENTACAO_ANEXACAO_DE_ARQUIVO_AUXILIAR) {
				if (!mov.isCancelada() && !mov.mob().doc().isSemEfeito())

					if ((idTpMov == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO
							|| idTpMov == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO
							|| idTpMov == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO_TRANSFERENCIA
							|| idTpMov == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA
							|| idTpMov == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA
							|| idTpMov == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CIENCIA)
							&& mov.isAssinada()) {

						addAcao("printer", "Ver", "/app/arquivo", "exibir",
								Ex.getInstance().getComp().podeVisualizarImpressao(titular, lotaTitular, mov.mob()),
								null, "&popup=true&arquivo=" + mov.getReferenciaPDF(), null, null, null);

						if (idTpMov != ExTipoMovimentacao.TIPO_MOVIMENTACAO_CIENCIA)
							addAcao("script_key", "Autenticar", "/app/expediente/mov", "autenticar_mov",
									Ex.getInstance().getComp().podeAutenticarMovimentacao(titular, lotaTitular, mov), null,
									"&popup=true&autenticando=true", null, null, null);

					} else if (!(mov.isAssinada() && mov.mob().isEmTransito(titular, lotaTitular))) {
						addAcao(null, "Ver/Assinar", "/app/expediente/mov", "exibir", true, null, "&popup=true", null,
								null, null);
					}
			}

			if (mov.getExMovimentacaoReferenciadoraSet() != null) {
				boolean fAssinaturas = false;
				boolean fConferencias = false;
				String complementoConferencias = "";

				for (ExMovimentacao movRef : mov.getExMovimentacaoReferenciadoraSet()) {
					if (movRef.getIdTpMov() == TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO
							|| movRef.getIdTpMov() == TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA) {
						complemento += (complemento.length() > 0 ? ", " : "")
								+ Texto.maiusculasEMinusculas(movRef.getObs());
						fAssinaturas = true;
					} else if (movRef.getIdTpMov() == TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO
							|| movRef.getIdTpMov() == TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA) {
						complementoConferencias += (complementoConferencias.length() > 0 ? ", " : "")
								+ Texto.maiusculasEMinusculas(movRef.getObs());
						fConferencias = true;
					}

				}
				if (fAssinaturas)
					complemento = " | Assinado por: " + complemento;

				if (fConferencias)
					complemento += " | Autenticado por: " + complementoConferencias;
			}
		}

		if (idTpMov == TIPO_MOVIMENTACAO_COPIA) {
			descricao = null;
			String mensagemPos = null;

			if (!mov.getExMobilRef().getExDocumento().getDescrDocumento()
					.equals(mov.getExMobil().getExDocumento().getDescrDocumento()))
				mensagemPos = " Descrição: " + mov.getExMobilRef().getExDocumento().getDescrDocumento();

			addAcao(null, mov.getExMobilRef().getSigla(), "/app/expediente/doc", "exibir", true, null,
					"sigla=" + mov.getExMobilRef().getSigla(), "Copia do documento: ", mensagemPos, null);
		}

		if (idTpMov == TIPO_MOVIMENTACAO_JUNTADA || idTpMov == TIPO_MOVIMENTACAO_JUNTADA_EXTERNO) {
			descricao = null;
			if (originadaAqui) {
				if (mov.getExMobilRef() != null) {

					String mensagemPos = null;

					if (!mov.getExMobilRef().getExDocumento().getDescrDocumento()
							.equals(mov.getExMobil().getExDocumento().getDescrDocumento()))
						mensagemPos = " Descrição: " + mov.getExMobilRef().getExDocumento().getDescrDocumento();

					addAcao(null, mov.getExMobilRef().getSigla(), "/app/expediente/doc", "exibir", true, null,
							"sigla=" + mov.getExMobilRef().getSigla(), "Juntado ao documento: ", mensagemPos, null);
				} else {
					descricao = "Juntado ao documento: " + mov.getDescrMov();
				}
			} else {

				String mensagemPos = null;

				if (!mov.getExMobil().getExDocumento().getDescrDocumento()
						.equals(mov.getExMobilRef().getExDocumento().getDescrDocumento()))
					mensagemPos = " Descrição: " + mov.getExDocumento().getDescrDocumento();

				addAcao(null, mov.getExMobil().getSigla(), "/app/expediente/doc", "exibir", true, null,
						"sigla=" + mov.getExMobil().getSigla(), "Documento juntado: ", mensagemPos, null);

				if (mov.getExMobil().podeExibirNoAcompanhamento(titular, lotaTitular)) {
						Set<ExMovimentacao> movs = mov.getExMobil().getMovsNaoCanceladas(ExTipoMovimentacao
								.TIPO_MOVIMENTACAO_EXIBIR_NO_ACOMPANHAMENTO_DO_PROTOCOLO);
						if (!movs.isEmpty()) {
							addAcao(null, "Desfazer Disponibilizar no Acompanhamento do Protocolo", "/app/expediente/mov", 
									"desfazer_exibir_no_acompanhamento_do_protocolo",
									true, "Ao clicar em OK o conteúdo deste documento deixará de ficar disponível através do número do " 
											+ "protocolo de acompanhamento. Deseja continuar?", 
									"id=" + movs.iterator().next().getIdMov().toString(), null,
									null, null);
						}
				} else {
					if (mov.getExMobil().isJuntado()
							&& Ex.getInstance().getComp()
								.podeDisponibilizarNoAcompanhamentoDoProtocolo(titular, lotaTitular, mov.getExDocumento())) {
						addAcao(null, "Disponibilizar no Acompanhamento do Protocolo", "/app/expediente/mov", 
								"exibir_no_acompanhamento_do_protocolo", 
								true, "Ao clicar em OK o conteúdo deste documento ficará disponível através do número do "
										+ "protocolo de acompanhamento. Deseja continuar? ",
								"sigla=" + mov.getExMobil().getSigla(), null,
								null, null);
					}
				}
			}
		}

		if (idTpMov == TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA) {
			descricao = null;
			if (originadaAqui) {
				if (mov.getExMobilRef() != null) {

					String mensagemPos = null;

					if (!mov.getExMobilRef().getExDocumento().getDescrDocumento()
							.equals(mov.getExMobil().getExDocumento().getDescrDocumento())) {
						
						String motivo = "";
						if (SigaMessages.isSigaSP() && mov.getDescrMov() != null && mov.getDescrMov().length() > 0) 
							motivo = ". Motivo: " + mov.getDescrMov();
																	
						mensagemPos = " Descrição: " + mov.getExMobilRef().getExDocumento().getDescrDocumento() + motivo;
					}
						
					addAcao(null, mov.getExMobilRef().getSigla(), "/app/expediente/doc", "exibir", true, null,
							"sigla=" + mov.getExMobilRef().getSigla(), "Desentranhado do documento: ", mensagemPos,
							null);
				} else {
					descricao = "Desentranhado do documento: " + mov.getDescrMov();
				}
			} else {

				String mensagemPos = null;

				if (!mov.getExMobil().getExDocumento().getDescrDocumento()
						.equals(mov.getExMobilRef().getExDocumento().getDescrDocumento()))
					mensagemPos = " Descrição: " + mov.getExDocumento().getDescrDocumento();

				addAcao(null, mov.getExMobil().getSigla(), "/app/expediente/doc", "exibir", true, null,
						"sigla=" + mov.getExMobil().getSigla(), "Documento desentranhado: ", mensagemPos, null);
			}
		}

		if (idTpMov == TIPO_MOVIMENTACAO_APENSACAO) {
			descricao = null;
			if (originadaAqui) {
				addAcao(null, mov.getExMobilRef().getSigla(), "/app/expediente/doc", "exibir", true, null,
						"sigla=" + mov.getExMobilRef().getSigla(), "Apensado ao documento: ", null, null);
			} else {
				addAcao(null, mov.getExMobil().getSigla(), "/app/expediente/doc", "exibir", true, null,
						"sigla=" + mov.getExMobil().getSigla(), "Documento apensado: ", null, null);
			}
		}

		if (idTpMov == TIPO_MOVIMENTACAO_DESAPENSACAO) {
			descricao = null;
			if (originadaAqui) {
				addAcao(null, mov.getExMobilRef().getSigla(), "/app/expediente/doc", "exibir", true, null,
						"sigla=" + mov.getExMobilRef().getSigla(), "Desapensado do documento: ", null, null);
			} else {
				addAcao(null, mov.getExMobil().getSigla(), "/app/expediente/doc", "exibir", true, null,
						"sigla=" + mov.getExMobil().getSigla(), "Documento desapensado: ", null, null);
			}
		}

		if (idTpMov == TIPO_MOVIMENTACAO_NOTIFICACAO_PUBL_BI) {
			addAcao(null, mov.getExMobilRef().getSigla(), "/app/expediente/doc", "exibir", true, null,
					"sigla=" + mov.getExMobilRef().getSigla(), "Publicado no Boletim Interno: ",
					" em " + mov.getDtMovDDMMYY(), null);
		}

		if (idTpMov == TIPO_MOVIMENTACAO_REFERENCIA) {
			descricao = null;
			if (originadaAqui) {
				addAcao(null, mov.getExMobilRef().getSigla(), "/app/expediente/doc", "exibir", true, null,
						"sigla=" + mov.getExMobilRef().getSigla(), "Ver também: ",
						" Descrição: " + mov.getExMobilRef().getExDocumento().getDescrDocumento(), null);
			} else {
				addAcao(null, mov.getExMobil().getSigla(), "/app/expediente/doc", "exibir", true, null,
						"sigla=" + mov.getExMobil().getSigla(), "Ver também: ",
						" Descrição: " + mov.getExDocumento().getDescrDocumento(), null);
			}
		}

		if (idTpMov == TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO) {
			descricao = null;
			if (originadaAqui) {
				addAcao(null, mov.getExMobilRef().getSigla(), "/app/expediente/doc", "exibir", true, null,
						"sigla=" + mov.getExMobilRef().getSigla(), "", null, null);
			} else {
				addAcao(null, mov.getExMobil().getSigla(), "/app/expediente/doc", "exibir", true, null,
						"sigla=" + mov.getExMobil().getSigla(), "", null, null);
			}
		}

		if (idTpMov == TIPO_MOVIMENTACAO_ELIMINACAO) {
			descricao = null;
			if (originadaAqui) {
				// Não faz nada, pois o documento eliminado nunca é exibido
			} else {
				descricao = mov.getExMobil().getSigla();
			}
		}
		
		if(idTpMov == TIPO_MOVIMENTACAO_GERAR_PROTOCOLO) {
			if (!mov.isCancelada())
				addAcao(null, "Gerar Protocolo", "/app/expediente/doc", "gerarProtocolo", true,
						null,  "sigla=" + mov.getExMobil().getSigla()
							+ "&popup=true",
						null, null, null);
		}

		if (idTpMov == TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE
				|| idTpMov == TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO
				|| idTpMov == TIPO_MOVIMENTACAO_ARQUIVAMENTO_PERMANENTE) {
			if (!mov.isCancelada())
				addAcao(null, "Protocolo", "/app/expediente/mov", "protocolo_arq_transf", true,
						null, "sigla=" + (mov.getCadastrante() == null ? "null" : mov.getCadastrante().getSigla())
								+ "&dt=" + mov.getDtRegMovDDMMYYYYHHMMSS() + "&popup=true&isTransf=false",
						null, null, null);
		}

		if (idTpMov == TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA
				|| idTpMov == TIPO_MOVIMENTACAO_DESPACHO_INTERNO_TRANSFERENCIA
				|| idTpMov == TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA
				|| idTpMov == TIPO_MOVIMENTACAO_TRANSFERENCIA || idTpMov == TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA
				|| idTpMov == TIPO_MOVIMENTACAO_RECEBIMENTO_TRANSITORIO) {
			String pre = null;
			if (mov.getDtFimMovDDMMYY() != "") {
				pre = "Devolver até " + mov.getDtFimMovDDMMYY() + " | ";
			}
			if (!mov.isCancelada())
				addAcao(null, "Protocolo", "/app/expediente/mov", "protocolo_arq_transf", true,
						null, "sigla=" + (mov.getCadastrante() == null ? "null" : mov.getCadastrante().getSigla())
								+ "&dt=" + mov.getDtRegMovDDMMYYYYHHMMSS() + "&popup=true&isTransf=true",
						pre, null, null);
		}

		if (idTpMov == TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO) {
			addAcao(null, mov.getNmArqMov(), "/app/arquivo", "download", mov.getNmArqMov() != null, null,
					"arquivo=" + mov.getReferenciaZIP(), null, null, null);
		}
		
		if (idTpMov == TIPO_MOVIMENTACAO_REORDENACAO_DOCUMENTO) {
			String detalhe = mov.getExMobil().getDoc().temOrdenacao() ? "Ver última reordenação" : "Ver documento completo";
			String complementoParam = mov.getExMobil().getDoc().temOrdenacao() ? "&exibirReordenacao=true" : "";
			
			addAcao(null, detalhe, "/app/expediente/doc", "exibirProcesso", true, null,
					"sigla=" + mov.getExMobil().getSigla() + complementoParam, "Documento completo reordenado manualmente:", "", null);
		}
		
		if (idTpMov == TIPO_MOVIMENTACAO_ORDENACAO_ORIGINAL_DOCUMENTO) {								
			addAcao(null, "Ver documento completo", "/app/expediente/doc", "exibirProcesso", true, null,
					"sigla=" + mov.getExMobil().getSigla(), "Documento completo reordenado para sua ordem original:", "", null);
		}
		
		if (descricao != null && descricao.equals(mov.getObs())) {
			descricao = ProcessadorReferencias.marcarReferenciasParaDocumentos(descricao, null);
		}
	}

	/**
	 * @param mov
	 */
	private void calcularClasse(ExMovimentacao mov) {
		if (mov.getExMovimentacaoCanceladora() == null) {
			if (originadaAqui) {
				switch (mov.getExTipoMovimentacao().getIdTpMov().intValue()) {
				case (int) TIPO_MOVIMENTACAO_ANEXACAO:
					classe = "anexacao";
					break;
				case (int) TIPO_MOVIMENTACAO_NOTIFICACAO_PUBL_BI:
					classe = "publicacao";
					break;
				case (int) TIPO_MOVIMENTACAO_ANOTACAO:
					classe = "anotacao";
					break;
				}
			}

			switch (mov.getExTipoMovimentacao().getIdTpMov().intValue()) {
			case (int) TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO:
			case (int) TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA:
			case (int) TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA:
			case (int) TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO:
				classe = "assinaturaMov";
				break;

			case (int) TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE:
			case (int) TIPO_MOVIMENTACAO_ARQUIVAMENTO_PERMANENTE:
			case (int) TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO:
				classe = "arquivamento";
				break;
			case (int) TIPO_MOVIMENTACAO_COPIA:
				classe = "copia";
				break;
			case (int) TIPO_MOVIMENTACAO_JUNTADA:
				classe = "juntada";
				break;
			case (int) TIPO_MOVIMENTACAO_JUNTADA_EXTERNO:
				classe = "juntada_externo";
				break;
			case (int) TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA:
				classe = "desentranhamento";
				break;
			case (int) TIPO_MOVIMENTACAO_REFERENCIA:
				classe = "vinculo";
				break;
			case (int) TIPO_MOVIMENTACAO_ENCERRAMENTO_DE_VOLUME:
				classe = "encerramento_volume";
				break;
			case (int) TIPO_MOVIMENTACAO_APENSACAO:
				classe = "apensacao";
				break;
			case (int) TIPO_MOVIMENTACAO_DESAPENSACAO:
				classe = "desapensacao";
				break;
			case (int) TIPO_MOVIMENTACAO_DESPACHO:
			case (int) TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA:
			case (int) TIPO_MOVIMENTACAO_DESPACHO_INTERNO_TRANSFERENCIA:
			case (int) TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA:
				classe = "despacho";
				break;
			case (int) TIPO_MOVIMENTACAO_TRANSFERENCIA:
			case (int) TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA:
			case (int) TIPO_MOVIMENTACAO_RECEBIMENTO_TRANSITORIO:
				classe = "transferencia";
				break;
			case (int) TIPO_MOVIMENTACAO_RECEBIMENTO:
				classe = "recebimento";
				break;
			case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO:
				classe = "criacao";
				break;
			}
		}
	}

	public String getClasse() {
		return classe;
	}

	public String getComplemento() {
		return complemento;
	}

	public String getDescricao() {
		if (descricao != null) {
			descricao = descricao.trim();
			if (descricao.length() == 0)
				return null;
		}
		return descricao;
	}

	public Object getDescrTipoMovimentacao() {
		return descrTipoMovimentacao;
	}

	public String getDisabled() {
		if (desabilitada)
			return "disabled";
		return "";
	}

	public Object getDtRegMovDDMMYYHHMMSS() {
		return dtRegMovDDMMYYHHMMSS;
	}

	public Object getDtRegMovDDMMYY() {
		return dtRegMovDDMMYYHHMMSS.substring(0, 8);
	}

	public Object getDtFimMovDDMMYYHHMMSS() {
		return dtFimMovDDMMYYHHMMSS;
	}

	public long getIdMov() {
		return idMov;
	}

	public long getIdTpMov() {
		return idTpMov;
	}

	public ExMobilVO getMobilVO() {
		// TODO Auto-generated method stub
		return mobVO;
	}

	public ExMovimentacao getMov() {
		return mov;
	}

	public Map<String, ExParteVO> getParte() {
		return parte;
	}

	public boolean isCancelada() {
		return cancelada;
	}

	public boolean isDesabilitada() {
		return desabilitada;
	}

	public boolean isOriginadaAqui() {
		return originadaAqui;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public void setDesabilitada(boolean desabilitada) {
		this.desabilitada = desabilitada;
	}

	public void setMov(ExMovimentacao mov) {
		this.mov = mov;
	}

	public void setOriginadaAqui(boolean originadaAqui) {
		this.originadaAqui = originadaAqui;
	}

	@Override
	public String toString() {
		return getMobilVO().getSigla() + ":" + getIdMov() + " - " + getDescrTipoMovimentacao() + " - " + getDescricao()
				+ "[" + getAcoes() + "] " + getDisabled();
	}

	public int getDuracaoSpan() {
		return duracaoSpan;
	}

	public void setDuracaoSpan(int duracaoSpan) {
		this.duracaoSpan = duracaoSpan;
	}

	public String getDuracao() {
		return duracao;
	}

	public void setDuracao(String duracao) {
		this.duracao = duracao;
	}

	public int getDuracaoSpanExibirCompleto() {
		return duracaoSpanExibirCompleto;
	}

	public void setDuracaoSpanExibirCompleto(int duracaoSpanExibirCompleto) {
		this.duracaoSpanExibirCompleto = duracaoSpanExibirCompleto;
	}

	private String mimeType() {
		if (mov == null || mov.getConteudoTpMov() == null)
			return "";
		return mov.getConteudoTpMov();
	}

	public boolean isImage() {
		return mimeType().startsWith("image/");
	}

	public boolean isPDF() {
		return mimeType().endsWith("/pdf");
	}

	public boolean isWord() {
		return mimeType().endsWith("/msword") || mimeType().endsWith(".wordprocessingml.document");
	}

	public boolean isExcel() {
		return mimeType().endsWith("/vnd.ms-excel") || mimeType().endsWith(".spreadsheetml.sheet");
	}

	public boolean isPresentation() {
		return mimeType().endsWith("/vnd.ms-powerpoint") || mimeType().endsWith(".presentationml.presentation");
	}

	public String getIcon() {
		if (isImage())
			return "image";
		if (isPDF())
			return "page_white_acrobat";
		if (isWord())
			return "page_white_word";
		if (isExcel())
			return "page_white_excel";
		if (isPresentation())
			return "page_white_powerpoint";

		return "page_white";
	}

	public static String getWebdavPassword() {
		String pwd = null;
		try {
			pwd = Prop.get("webdav.senha");
		} catch (Exception e) {
			throw new AplicacaoException("Erro obtendo propriedade siga.ex.webdav.pwd", 0, e);
		}
		return pwd;
	}

	// public static Algorithm getWebdavJwtAlgorithm(String pwd) {
	// Algorithm algorithm;
	// try {
	// algorithm = Algorithm.HMAC256(pwd);
	// } catch (IllegalArgumentException | UnsupportedEncodingException e) {
	// throw new AplicacaoException("Erro criando algoritmo", 0, e);
	// }
	// return algorithm;
	// }

	private static String getWebdavJwtToken(ExMovimentacao mov, DpPessoa cadastrante, DpPessoa titular,
			DpLotacao lotaTitular, String pwd) {
		String token;

		final JWTSigner signer = new JWTSigner(getWebdavPassword());
		final HashMap<String, Object> claims = new HashMap<String, Object>();

		// final long iat = System.currentTimeMillis() / 1000L; // issued at
		// claim
		// final long exp = iat + 48 * 60 * 60L; // token expires in 48 hours
		// claims.put("exp", exp);
		// claims.put("iat", iat);

		// Nato: tive que colocar tudo em uma string só para reduzir o tamanho
		// do JWT, pois o Word tem uma limitação no tamanho máximo da URL.
		claims.put("d", mov.mob().getReferencia() + "|" + cadastrante.getSiglaCompleta() + "|"
				+ titular.getSiglaCompleta() + "|" + lotaTitular.getSiglaCompleta());
		// claims.put("mob", mov.mob().getReferencia());
		// claims.put("cad",cadastrante.getSiglaCompleta());
		// claims.put("tit",titular.getSiglaCompleta());
		// claims.put("lot",lotaTitular.getSiglaCompleta());
		token = signer.sign(claims);

		// Date agora = new Date();
		// Date expiraEm = new Date(agora.getTime() + 1000*60*60*48);
		// Builder jwtBuilder = JWT.create();
		// jwtBuilder.withIssuedAt(agora)
		// .withExpiresAt(expiraEm)
		// .withClaim("mob",mov.mob().getReferencia())
		// .withClaim("cad",cadastrante.getSiglaCompleta())
		// .withClaim("tit",titular.getSiglaCompleta())
		// .withClaim("lot",lotaTitular.getSiglaCompleta());
		//
		// token = jwtBuilder.sign(getWebdavJwtAlgorithm(pwd));

		return token.replace(JWT_FIXED_HEADER, JWT_FIXED_HEADER_REPLACEMENT).replace(".", "~");
	}

	public static Map<String, Object> getWebdavDecodedToken(String token) {
		final JWTVerifier verifier = new JWTVerifier(getWebdavPassword());
		try {
			Map<String, Object> map = verifier
					.verify(token.replace("~", ".").replace(JWT_FIXED_HEADER_REPLACEMENT, JWT_FIXED_HEADER));
			String a[] = map.get("d").toString().split("\\|");
			map.put("mob", a[0]);
			map.put("cad", a[1]);
			map.put("tit", a[2]);
			map.put("lot", a[3]);
			return map;
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao verificar token JWT", 0, e);
		}
		// Algorithm algorithm = getWebdavJwtAlgorithm(getWebdavPassword());
		// JWTVerifier verificador = JWT.require(algorithm).build();
		// DecodedJWT jwt = verificador.verify(token.replace("$", "."));
		// return jwt;
	}

	public String getTempoRelativo() {
		return tempoRelativo;
	}

	public String getSubscritor() {
		return subscritor;
	}

	public void setSubscritor(String subscritor) {
		this.subscritor = subscritor;
	}
}
