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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import br.gov.jfrj.siga.cp.CpToken;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.logic.*;
import org.apache.commons.lang3.StringUtils;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.crivano.jlogic.NOr;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.logic.CpNaoENulo;
import br.gov.jfrj.siga.cp.logic.CpPodeSempre;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.cp.util.CpProcessadorReferencias;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeVinculo;

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
	ITipoDeMovimentacao exTipoMovimentacao;
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
	boolean tipoMovimentacaoArquivamento;

	public ExMobilVO getMobVO() {
		return mobVO;
	}

	public ExMovimentacaoVO(ExMobilVO mobVO, ExMovimentacao mov, DpPessoa cadastrante, DpPessoa titular,
			DpLotacao lotaTitular, boolean serializavel) {
		originadaAqui = (mov.getExMobil().getId().equals(mobVO.id));
		this.mov = mov;
		this.mobVO = mobVO;
		originadaAqui = (mov.getExMobil().getId().equals(getMobVO().id));
		exTipoMovimentacao = mov.getExTipoMovimentacao();
		dtIniMov = mov.getDtIniMov();

		mimeType = (mov.getConteudoTpMov() == null) ? "" : mov.getConteudoTpMov();


		this.idMov = mov.getIdMov();
		this.dtRegMovDDMMYYHHMMSS = mov.getDtRegMovDDMMYYHHMMSS();
		this.tempoRelativo = Data.calcularTempoRelativo(mov.getDtIniMov());
		this.descrTipoMovimentacao = mov.getDescrTipoMovimentacao();
		if (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.REFERENCIA && mov.getTipoDeVinculo() != null)
			this.descrTipoMovimentacao = mov.getTipoDeVinculo().getDescr();
		this.cancelada = mov.getExMovimentacaoCanceladora() != null;
		this.lotaCadastranteSigla = mov.getLotaCadastrante() != null ? mov.getLotaCadastrante().getSigla() : null;
		this.exTipoMovimentacaoSigla = mov.getExTipoMovimentacao().getDescr();

		if (mov.getLotaCadastrante() != null)
			parte.put("lotaCadastrante", new ExParteVO(mov.getLotaCadastrante()));
		if (mov.getCadastrante() != null)
			parte.put("cadastrante", new ExParteVO(mov.getCadastrante()));
		if (mov.getLotaTitular() != null)
			parte.put("lotaTitular", new ExParteVO(mov.getLotaTitular()));
		if (mov.getTitular() != null)
			parte.put("titular", new ExParteVO(mov.getTitular()));
		if (mov.getLotaSubscritor() != null)
			parte.put("lotaSubscritor", new ExParteVO(mov.getLotaSubscritor()));
		if (mov.getSubscritor() != null)
			parte.put("subscritor", new ExParteVO(mov.getSubscritor()));
		if (mov.getLotaResp() != null)
			parte.put("lotaResp", new ExParteVO(mov.getLotaResp()));
		if (mov.getResp() != null)
			parte.put("resp", new ExParteVO(mov.getResp()));

		descricao = mov.getObs();

		if (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.ANEXACAO)
			descricao = StringUtils.isNotBlank(mov.getDescrMov()) ? mov.getDescrMov() : mov.getNmArqMov();

		if (mov.getExTipoMovimentacao() == (ExTipoDeMovimentacao.ASSINATURA_COM_SENHA) || mov.getExTipoMovimentacao() == (ExTipoDeMovimentacao.ASSINATURA_DIGITAL_DOCUMENTO)) {
			descricao += Ex.getInstance().getBL().extraiPersonalizacaoAssinatura(mov,false);
		}

		if(mov.getExMovimentacaoCanceladora() != null && ExTipoDeMovimentacao.hasArquivado(mov.getExTipoMovimentacao()))
			descricao = "";
		
		addAcoes(mov, cadastrante, titular, lotaTitular);

		calcularClasse(mov);

		desabilitada = (mov.getExMovimentacaoRef() != null && mov.getExMovimentacaoRef().isCancelada() && mov.getExTipoMovimentacao() != (ExTipoDeMovimentacao.MARCACAO) && mov.getExTipoMovimentacao() != (ExTipoDeMovimentacao.RECEBIMENTO) && !ExTipoDeMovimentacao.hasArquivado(mov.getExTipoMovimentacao()))
				|| mov.getExMovimentacaoCanceladora() != null
				|| mov.getExTipoMovimentacao() == (ExTipoDeMovimentacao.CANCELAMENTO_DE_MOVIMENTACAO);
		
		if (mov.getExTipoMovimentacao() == (ExTipoDeMovimentacao.VINCULACAO_PAPEL))
			complemento = (mov.getSubscritor() != null ? mov.getSubscritor().getDescricao() : mov.getLotaSubscritor() != null ? mov.getLotaSubscritor().getDescricao() : null) + " - " + mov.getExPapel().getDescPapel() + ". ";
		
		if (mov.getExTipoMovimentacao() == (ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO))
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

		if (exTipoMovimentacao == ExTipoDeMovimentacao.ASSINATURA_DIGITAL_DOCUMENTO) {
			descricao = "";
			addAcao(AcaoVO.builder().nome("Verificar").nameSpace("/app/expediente/mov").acao("assinar_verificar")
					.params("sigla", mov.mob().getCodigoCompacto()).params("id", mov.getIdMov().toString()).params("ajax", "true").exp(new CpPodeSempre()).classe("once").build());
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.ASSINATURA_DIGITAL_MOVIMENTACAO
				|| exTipoMovimentacao == ExTipoDeMovimentacao.ASSINATURA_DIGITAL_MOVIMENTACAO) {
			descricao = Texto.maiusculasEMinusculas(mov.getObs());
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.ANOTACAO) {
			descricao = mov.getObs();
			addAcao(AcaoVO.builder().nome("Excluir").nameSpace("/app/expediente/mov").acao("excluir")
					.params("sigla", mov.mob().getCodigoCompacto()).params("id", mov.getIdMov().toString()).exp(new ExPodeExcluirAnotacao(mov.mob(), mov, titular, lotaTitular)).classe("once").build());
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.VINCULACAO_PAPEL) {
			addAcao(AcaoVO.builder().nome("Cancelar").nameSpace("/app/expediente/mov").acao("cancelar")
					.params("sigla", mov.mob().getCodigoCompacto()).params("id", mov.getIdMov().toString()).exp(new ExPodeCancelarVinculacaoPapel(mov, titular, lotaTitular)).classe("once").build());
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.MARCACAO) {
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
			String descrUrl = "";
			try {
				descrUrl = URLEncoder.encode("Exclusão de marcação: " + mov.getMarcador().getDescrMarcador(), "iso-8859-1");
			} catch (UnsupportedEncodingException e) {
				throw new AplicacaoException("Erro ao converter", 0, e);
			}
			if (!mov.isCancelada())
				addAcao(AcaoVO.builder().nome("Excluir Marcador").nameSpace("/app/expediente/mov")
						.acao("cancelar_movimentacao_gravar").params("sigla", mov.mob().getCodigoCompacto())
						.params("id", mov.getIdMov().toString())
						.params("descrMov", descrUrl).post(true)
						.exp(new ExPodeCancelarMarcacao(mov, titular, lotaTitular)).build());
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.ANEXACAO_DE_ARQUIVO_AUXILIAR) {
			addAcao(AcaoVO.builder().nome(mov.getNmArqMov()).icone(getIcon()).nameSpace("/app/arquivo").acao("exibir").params("sigla", mov.mob().getCodigoCompacto()).params("id", mov.getIdMov().toString())
					.params("arquivo", mov.getReferencia())
					.exp(new CpNaoENulo(mov.getNmArqMov(), "nome do arquivo")).build());
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

				addAcao(AcaoVO.builder().nome("Editar no " + sNome).nameSpace(sApp
						+ ":ofe|u|__scheme__://__serverName__:__serverPort____contextPath__/webdav/" + token).acao(mov.getNmArqMov()).exp(new CpPodeSempre()).build());
			}

			addAcao(AcaoVO.builder().nome("Cancelar").nameSpace("/app/expediente/mov").acao("cancelar").params("sigla", mov.mob().getCodigoCompacto()).params("id", mov.getIdMov().toString())
				.exp(NOr.of(new ExMovimentacaoEstaCancelada(mov), new ExEstaSemEfeito(mov.mob().doc()))).build());
		}

		if (mov.getNumPaginas() != null || exTipoMovimentacao == ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO
				|| exTipoMovimentacao == ExTipoDeMovimentacao.ANEXACAO) {
			// A acao pode ser melhorada para mostrar o icone do pdf antes do
			// nome do arquivo.
			// <c:url var='anexo' value='/anexo/${mov.idMov}/${mov.nmArqMov}' />
			// tipo="${mov.conteudoTpMov}" />
			addAcao(AcaoVO.builder().nome(mov.getNmArqMov()).nameSpace("/app/arquivo").acao("exibir").params("sigla", mov.mob().getCodigoCompacto()).params("id", mov.getIdMov().toString())
					.params("arquivo", mov.getReferenciaPDF()).params("popup", "true")
					.exp(new CpPodeSempre()).build());

			if (exTipoMovimentacao == ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO) {
				addAcao(AcaoVO.builder().nome("Excluir").nameSpace("/app/expediente/mov").acao("excluir").params("sigla", mov.mob().getCodigoCompacto()).params("id", mov.getIdMov().toString())
						.exp(new ExPodeExcluirCossignatario(mov, titular, lotaTitular)).build());
			}

			if (exTipoMovimentacao == ExTipoDeMovimentacao.ANEXACAO) {
				if (!mov.isCancelada() && !mov.mob().doc().isSemEfeito() && !mov.mob().isEmTransito(titular, lotaTitular)) {
					addAcao(AcaoVO.builder().nome("Excluir").nameSpace("/app/expediente/mov").acao("excluir").params("sigla", mov.mob().getCodigoCompacto()).params("id", mov.getIdMov().toString())
							.exp(new ExPodeExcluirAnexo(mov.mob(), mov, titular, lotaTitular)).build());
					
					addAcao(AcaoVO.builder().nome("Cancelar").nameSpace("/app/expediente/mov").acao("cancelar").params("sigla", mov.mob().getCodigoCompacto()).params("id", mov.getIdMov().toString())
							.params("popup", "true")
							.exp(new ExPodeCancelarAnexo(mov.mob(), mov, titular, lotaTitular)).build());
					
					addAcao(AcaoVO.builder().nome("Assinar/Autenticar").nameSpace("/app/expediente/mov").acao("exibir").params("sigla", mov.mob().getCodigoCompacto()).params("id", mov.getIdMov().toString())
							.params("popup", "true")
							.exp(new CpPodeSempre()).build());
					
//					addAcao(AcaoVO.builder().nome("Autenticar").icone("script_key").nameSpace("/app/expediente/mov").acao("autenticar_mov").params("sigla", mov.mob().getCodigoCompacto()).params("id", mov.getIdMov().toString())
//							.params("popup", "true").params("autenticando", "true")
//							.exp(new ExPodeAutenticarMovimentacao(mov, titular, lotaTitular)).build());
				}
			}

			if (ExTipoDeMovimentacao.hasDespacho(exTipoMovimentacao)) {
				if (!mov.mob().doc().isSemEfeito())
					addAcao(AcaoVO.builder().nome("Cancelar").nameSpace("/app/expediente/mov").acao("cancelar").params("sigla", mov.mob().getCodigoCompacto()).params("id", mov.getIdMov().toString())
							.exp(new ExPodeCancelarDespacho(mov, titular, lotaTitular)).build());
			}

			if (exTipoMovimentacao != ExTipoDeMovimentacao.ASSINATURA_DIGITAL_MOVIMENTACAO
					&& exTipoMovimentacao != ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO
					&& exTipoMovimentacao != ExTipoDeMovimentacao.CONFERENCIA_COPIA_DOCUMENTO
					&& exTipoMovimentacao != ExTipoDeMovimentacao.AGENDAMENTO_DE_PUBLICACAO && exTipoMovimentacao != ExTipoDeMovimentacao.ANEXACAO
					&& exTipoMovimentacao != ExTipoDeMovimentacao.ANEXACAO_DE_ARQUIVO_AUXILIAR) {
				if (!mov.isCancelada() && !mov.mob().doc().isSemEfeito())

					if ((exTipoMovimentacao == ExTipoDeMovimentacao.DESPACHO
							|| exTipoMovimentacao == ExTipoDeMovimentacao.DESPACHO_INTERNO
							|| exTipoMovimentacao == ExTipoDeMovimentacao.DESPACHO_INTERNO_TRANSFERENCIA
							|| exTipoMovimentacao == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA
							|| exTipoMovimentacao == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA
							|| exTipoMovimentacao == ExTipoDeMovimentacao.CIENCIA)
							&& mov.isAssinada()) {

						addAcao(AcaoVO.builder().nome("Ver").icone("printer").nameSpace("/app/arquivo").acao("exibir").params("sigla", mov.mob().getCodigoCompacto()).params("id", mov.getIdMov().toString())
								.params("popup", "true").params("arquivo", mov.getReferenciaPDF())
								.exp(new ExPodeVisualizarImpressao(mov.mob(), titular, lotaTitular)).build());

						if (exTipoMovimentacao != ExTipoDeMovimentacao.CIENCIA)
							addAcao(AcaoVO.builder().nome("Autenticar").icone("script_key").nameSpace("/app/expediente/mov").acao("autenticar_mov").params("sigla", mov.mob().getCodigoCompacto()).params("id", mov.getIdMov().toString())
									.params("popup", "true").params("autenticando", "true")
									.exp(new ExPodeAutenticarMovimentacao(mov, titular, lotaTitular)).build());

					} else if (!(mov.isAssinada() && mov.mob().isEmTransito(titular, lotaTitular))) {
						addAcao(AcaoVO.builder().nome(mov.isAssinada() ? "Ver" : "Ver/Assinar").nameSpace("/app/expediente/mov").acao("exibir").params("sigla", mov.mob().getCodigoCompacto()).params("id", mov.getIdMov().toString())
								.params("popup", "true")
								.exp(new CpPodeSempre()).build());
					}
			}

			if (mov.getExMovimentacaoReferenciadoraSet() != null) {
				boolean fAssinaturas = false;
				boolean fConferencias = false;
				String complementoConferencias = "";

				for (ExMovimentacao movRef : mov.getExMovimentacaoReferenciadoraSet()) {
					if (movRef.getExTipoMovimentacao() == ExTipoDeMovimentacao.ASSINATURA_DIGITAL_MOVIMENTACAO
							|| movRef.getExTipoMovimentacao() == ExTipoDeMovimentacao.ASSINATURA_MOVIMENTACAO_COM_SENHA) {
						complemento += (complemento.length() > 0 ? ", " : "")
								+ Texto.maiusculasEMinusculas(movRef.getObs());
						fAssinaturas = true;
					} else if (movRef.getExTipoMovimentacao() == ExTipoDeMovimentacao.CONFERENCIA_COPIA_DOCUMENTO
							|| movRef.getExTipoMovimentacao() == ExTipoDeMovimentacao.CONFERENCIA_COPIA_COM_SENHA) {
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

		if (exTipoMovimentacao == ExTipoDeMovimentacao.COPIA) {
			descricao = null;
			String mensagemPos = null;

			if (!mov.getExMobilRef().getExDocumento().getDescrDocumento()
					.equals(mov.getExMobil().getExDocumento().getDescrDocumento()))
				mensagemPos = " Descrição: " + mov.getExMobilRef().getExDocumento().getDescrDocumento();

			addAcao(AcaoVO.builder().nome(mov.getExMobilRef().getSigla()).nameSpace("/app/expediente/doc").acao("exibir").params("sigla", mov.getExMobilRef().getSigla())
					.exp(new CpPodeSempre()).pre("Copia do documento: ").pos(mensagemPos).build());
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.JUNTADA || exTipoMovimentacao == ExTipoDeMovimentacao.JUNTADA_EXTERNO) {
			descricao = null;
			if (originadaAqui) {
				if (mov.getExMobilRef() != null) {

					String mensagemPos = null;

					if (!mov.getExMobilRef().getExDocumento().getDescrDocumento()
							.equals(mov.getExMobil().getExDocumento().getDescrDocumento()))
						mensagemPos = " Descrição: " + mov.getExMobilRef().getExDocumento().getDescrDocumento();

					addAcao(AcaoVO.builder().nome(mov.getExMobilRef().getSigla()).nameSpace("/app/expediente/doc").acao("exibir").params("sigla", mov.getExMobilRef().getSigla())
							.exp(new CpPodeSempre()).pre("Juntado ao documento: ").pos(mensagemPos).build());
				} else {
					descricao = "Juntado ao documento: " + mov.getDescrMov();
				}
			} else {

				String mensagemPos = null;

				if (!mov.getExMobil().getExDocumento().getDescrDocumento()
						.equals(mov.getExMobilRef().getExDocumento().getDescrDocumento()))
			//		mensagemPos = " Descrição: " + mov.getExDocumento().getDescrDocumento();
					mensagemPos =  mov.getExDocumento().getDescrDocumento();
				
				addAcao(AcaoVO.builder().nome(mov.getExMobil().getSigla()).nameSpace("/app/expediente/doc").acao("exibir").params("sigla", mov.getExMobil().getSigla())
						.exp(new CpPodeSempre()).pos(mensagemPos).build());
						//	.exp(new CpPodeSempre()).pre("Documento juntado: ").pos(mensagemPos).build());

				if (mov.getExMobil().podeExibirNoAcompanhamento(titular, lotaTitular)) {
						Set<ExMovimentacao> movs = mov.getExMobil().getMovsNaoCanceladas(ExTipoDeMovimentacao.EXIBIR_NO_ACOMPANHAMENTO_DO_PROTOCOLO);
						if (!movs.isEmpty()) {
							addAcao(AcaoVO.builder().nome("Desfazer Disponibilizar no Acompanhamento do Protocolo").nameSpace("/app/expediente/mov").acao("desfazer_exibir_no_acompanhamento_do_protocolo")
									.params("id", movs.iterator().next().getIdMov().toString())
									.exp(new CpPodeSempre()).msgConfirmacao("Ao clicar em OK o conteúdo deste documento deixará de ficar disponível através do número do " 
											+ "protocolo de acompanhamento. Deseja continuar?").build());
						}
				} else {
					if (mov.getExMobil().isJuntado() && mov.getExDocumento().getExMobilPai()!= null && mov.getExDocumento().getExMobilPai().isAtendente(titular,lotaTitular)
							&& Ex.getInstance().getComp()
								.pode(ExPodeDisponibilizarNoAcompanhamentoDoProtocolo.class, titular, lotaTitular, mov.getExDocumento())) {
						addAcao(AcaoVO.builder().nome("Disponibilizar no Acompanhamento do Protocolo").nameSpace("/app/expediente/mov").acao("exibir_no_acompanhamento_do_protocolo")
								.params("sigla",mov.getExMobil().getSigla())
								.exp(new CpPodeSempre()).msgConfirmacao(mov.getExMobil().isAcessoRestrito() ? "Atenção: Documento com Restrição de Acesso. \\n" : "" + "YYYYAo clicar em OK o conteúdo deste documento ficará disponível através do número do "
										+ "protocolo de acompanhamento. Deseja continuar?").build());
					}
				}
			}
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA) {
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
					addAcao(AcaoVO.builder().nome(mov.getExMobilRef().getSigla()).nameSpace("/app/expediente/doc").acao("exibir").params("sigla", mov.getExMobilRef().getSigla())
							.exp(new CpPodeSempre()).pre("Desentranhado do documento: ").pos(mensagemPos).build());
				} else {
					descricao = "Desentranhado do documento: " + mov.getDescrMov();
				}
			} else {

				String mensagemPos = null;

				if (!mov.getExMobil().getExDocumento().getDescrDocumento()
						.equals(mov.getExMobilRef().getExDocumento().getDescrDocumento()))
					mensagemPos = " Descrição: " + mov.getExDocumento().getDescrDocumento();

				addAcao(AcaoVO.builder().nome(mov.getExMobil().getSigla()).nameSpace("/app/expediente/doc").acao("exibir").params("sigla", mov.getExMobil().getSigla())
						.exp(new CpPodeSempre()).pre("Documento desentranhado: ").pos(mensagemPos).build());
			}
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.APENSACAO) {
			descricao = null;
			if (originadaAqui) {
				addAcao(AcaoVO.builder().nome(mov.getExMobilRef().getSigla()).nameSpace("/app/expediente/doc").acao("exibir").params("sigla", mov.getExMobilRef().getSigla())
						.exp(new CpPodeSempre()).pre("Apensado ao documento: ").build());
			} else {
				addAcao(AcaoVO.builder().nome(mov.getExMobil().getSigla()).nameSpace("/app/expediente/doc").acao("exibir").params("sigla", mov.getExMobil().getSigla())
						.exp(new CpPodeSempre()).pre("Documento apensado: ").build());
			}
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.DESAPENSACAO) {
			descricao = null;
			if (originadaAqui) {
				addAcao(AcaoVO.builder().nome(mov.getExMobilRef().getSigla()).nameSpace("/app/expediente/doc").acao("exibir").params("sigla", mov.getExMobilRef().getSigla())
						.exp(new CpPodeSempre()).pre("Desapensado do documento: ").build());
			} else {
				addAcao(AcaoVO.builder().nome(mov.getExMobil().getSigla()).nameSpace("/app/expediente/doc").acao("exibir").params("sigla", mov.getExMobil().getSigla())
						.exp(new CpPodeSempre()).pre("Documento desapensado: ").build());
			}
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.NOTIFICACAO_PUBL_BI) {
			addAcao(AcaoVO.builder().nome(mov.getExMobilRef().getSigla()).nameSpace("/app/expediente/doc").acao("exibir").params("sigla", mov.getExMobilRef().getSigla())
					.exp(new CpPodeSempre()).pre("Publicado no Boletim Interno: ").pos(" em " + mov.getDtMovDDMMYY()).build());
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.REFERENCIA) {
			descricao = null;
			ExTipoDeVinculo tipoDeVinculo = mov.getTipoDeVinculo();
			if (tipoDeVinculo == null)
			    tipoDeVinculo = ExTipoDeVinculo.RELACIONAMENTO;
            if (originadaAqui) {
				addAcao(AcaoVO.builder().nome(mov.getExMobilRef().getSigla()).nameSpace("/app/expediente/doc").acao("exibir").params("sigla", mov.getExMobilRef().getSigla())
						.exp(new CpPodeSempre()).pre(tipoDeVinculo.getAcao() + ": ").pos(" Descrição: " + mov.getExMobilRef().getExDocumento().getDescrDocumento()).build());
			} else {
				addAcao(AcaoVO.builder().nome(mov.getExMobil().getSigla()).nameSpace("/app/expediente/doc").acao("exibir").params("sigla", mov.getExMobil().getSigla())
						.exp(new CpPodeSempre()).pre(tipoDeVinculo.getAcaoInversa() + ": ").pos(" Descrição: " + mov.getExMobilRef().getExDocumento().getDescrDocumento()).build());
			}
			addAcao(AcaoVO.builder().nome("Cancelar").nameSpace("/app/expediente/mov").acao("cancelar").params("sigla", mov.mob().getCodigoCompacto()).params("id", mov.getIdMov().toString())
					.exp(new ExPodeCancelarVinculacao(mov, titular, lotaTitular)).build());
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.INCLUSAO_EM_EDITAL_DE_ELIMINACAO) {
			descricao = null;
			if (originadaAqui) {
				addAcao(AcaoVO.builder().nome(mov.getExMobilRef().getSigla()).nameSpace("/app/expediente/doc").acao("exibir").params("sigla", mov.getExMobilRef().getSigla())
						.exp(new CpPodeSempre()).build());
			} else {
				addAcao(AcaoVO.builder().nome(mov.getExMobil().getSigla()).nameSpace("/app/expediente/doc").acao("exibir").params("sigla", mov.getExMobil().getSigla())
						.exp(new CpPodeSempre()).build());
			}
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.ELIMINACAO) {
			descricao = null;
			if (originadaAqui) {
				// Não faz nada, pois o documento eliminado nunca é exibido
			} else {
				descricao = mov.getExMobil().getSigla();
			}
		}
		
		if(exTipoMovimentacao == ExTipoDeMovimentacao.GERAR_PROTOCOLO) {
			if (!mov.isCancelada())
				addAcao(AcaoVO.builder().nome("Gerar Protocolo").nameSpace("/app/expediente/doc").acao("gerarProtocolo").params("sigla", mov.getExMobil().getSigla()).params("popup", "true")
						.exp(new CpPodeSempre()).build());
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.ARQUIVAMENTO_CORRENTE
				|| exTipoMovimentacao == ExTipoDeMovimentacao.ARQUIVAMENTO_INTERMEDIARIO
				|| exTipoMovimentacao == ExTipoDeMovimentacao.ARQUIVAMENTO_PERMANENTE) {
			if (!mov.isCancelada())
				addAcao(AcaoVO.builder().nome("Protocolo").nameSpace("/app/expediente/mov").acao("protocolo_arq_transf").params("sigla", (mov.getCadastrante() == null ? "null" : mov.getCadastrante().getSigla()))
						.params("dtIni", mov.getDtRegMovDDMMYYYYHHMMSS()).params("popup", "true").params("isTransf", "false")
						.exp(new CpPodeSempre()).build());
			if (mov.isCancelada() && mov.getDescrMov() != null) {
				addAcao(AcaoVO.builder().nome("Motivo").nameSpace(null)
						.acao(String.format("javascript:sigaModal.alerta('%s')", mov.getDescrMov()))
						.exp(new CpPodeSempre()).build());
			}
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA
				|| exTipoMovimentacao == ExTipoDeMovimentacao.DESPACHO_INTERNO_TRANSFERENCIA
				|| exTipoMovimentacao == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA
				|| exTipoMovimentacao == ExTipoDeMovimentacao.TRANSFERENCIA || exTipoMovimentacao == ExTipoDeMovimentacao.TRANSFERENCIA_EXTERNA
				|| exTipoMovimentacao == ExTipoDeMovimentacao.RECEBIMENTO_TRANSITORIO) {
			String pre = null;
			if (mov.getDtFimMovDDMMYY() != "") {
				pre = "Devolver até " + mov.getDtFimMovDDMMYY() + " | ";
			}
			if (!mov.isCancelada())
				addAcao(AcaoVO.builder().nome("Protocolo").nameSpace("/app/expediente/mov").acao("protocolo_arq_transf").params("sigla", (mov.getCadastrante() == null ? "null" : mov.getCadastrante().getSigla()))
						.params("dtIni", mov.getDtRegMovDDMMYYYYHHMMSS()).params("popup", "true").params("isTransf", "false").pre(pre)
						.exp(new CpPodeSempre()).build());
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.AGENDAMENTO_DE_PUBLICACAO) {
			addAcao(AcaoVO.builder().nome(mov.getNmArqMov()).nameSpace("/app/arquivo").acao("download").params("arquivo", mov.getReferenciaZIP())
					.params("dt", mov.getDtRegMovDDMMYYYYHHMMSS())
					.exp(new CpNaoENulo(mov.getNmArqMov(), "nome do arquivo")).build());
		}
		
		if (exTipoMovimentacao == ExTipoDeMovimentacao.ENVIAR_PUBLICACAO_DOE || exTipoMovimentacao == ExTipoDeMovimentacao.CANCELAR_PUBLICACAO_DOE) {
			addAcao(AcaoVO.builder().nome("RECIBO").nameSpace("/app/arquivo").acao("download").params("arquivo", mov.getReferenciaZIP())
					.params("dt", mov.getDtRegMovDDMMYYYYHHMMSS())
					.exp(new CpNaoENulo(mov.getNmArqMov(), "nome do arquivo")).build());
		}
		
		if (exTipoMovimentacao == ExTipoDeMovimentacao.REORDENACAO_DOCUMENTO) {
			String detalhe = mov.getExMobil().getDoc().temOrdenacao() ? "Ver última reordenação" : "Ver documento completo";
			String complementoParam = mov.getExMobil().getDoc().temOrdenacao() ? "&exibirReordenacao=true" : "";
			addAcao(AcaoVO.builder().nome(detalhe).nameSpace("/app/expediente/doc").acao("exibirProcesso").params("sigla", mov.getExMobil().getSigla() + complementoParam).pre("Documento completo reordenado manualmente:")
					.exp(new CpPodeSempre()).build());
		}
		
		if (exTipoMovimentacao == ExTipoDeMovimentacao.ORDENACAO_ORIGINAL_DOCUMENTO) {								
			addAcao(AcaoVO.builder().nome("Ver documento completo").nameSpace("/app/expediente/doc").acao("exibirProcesso").params("sigla", mov.getExMobil().getSigla()).pre("Documento completo reordenado para sua ordem original:")
					.exp(new CpPodeSempre()).build());
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.ENVIO_PARA_VISUALIZACAO_EXTERNA) {
			addAcao(AcaoVO.builder().nome(SigaMessages.getMessage("documento.revogar.visualizacaoexterna"))
					.nameSpace("/app/expediente/mov").acao("revogar_visualizacao_externa")
					.params("sigla", mov.mob().getCodigoCompacto())
					.params("idRef", mov.getIdMov().toString())
					.exp(new ExPodeEnviarParaVisualizacaoExterna(mov.mob(), titular, lotaTitular))
					.msgConfirmacao("Confirma a revogação da visualização externa?")
					.build());
		}

		if (exTipoMovimentacao == ExTipoDeMovimentacao.PUBLICACAO_PORTAL_TRANSPARENCIA ||
				exTipoMovimentacao == ExTipoDeMovimentacao.GERAR_LINK_PUBLICO_PROCESSO) {
			
			CpToken cpToken = CpDao.getInstance()
					.obterCpTokenPorTipoIdRef(CpToken.TOKEN_URLPERMANENTE, mov.getExDocumento().getIdDoc());
			String url = Cp.getInstance().getBL()
					.obterURLPermanente(cpToken.getIdTpToken().toString(), cpToken.getToken());
			
			AcaoVO acaoVO = AcaoVO.builder().nome(mov.getExMobil().getSigla())
					.url(url)
					.exp(new CpPodeSempre())
					.build();
			
			addAcao(acaoVO);
		}
		
		if (descricao != null && descricao.equals(mov.getObs())) {
			descricao = CpProcessadorReferencias.marcarReferenciasParaDocumentos(descricao, null);
		}
	}

	/**
	 * @param mov
	 */
	private void calcularClasse(ExMovimentacao mov) {
		ExTipoDeMovimentacao tpMov = (ExTipoDeMovimentacao) mov.getExTipoMovimentacao();
		if (mov.getExMovimentacaoCanceladora() == null) {
			if (originadaAqui) {
				switch (tpMov) {
				case ANEXACAO:
					classe = "anexacao";
					break;
				case NOTIFICACAO_PUBL_BI:
					classe = "publicacao";
					break;
				case ANOTACAO:
					classe = "anotacao";
					break;
				}
			}

			switch (tpMov) {
			case ASSINATURA_DIGITAL_MOVIMENTACAO:
			case ASSINATURA_MOVIMENTACAO_COM_SENHA:
			case CONFERENCIA_COPIA_COM_SENHA:
			case CONFERENCIA_COPIA_DOCUMENTO:
				classe = "assinaturaMov";
				break;

			case ARQUIVAMENTO_CORRENTE:
			case ARQUIVAMENTO_PERMANENTE:
			case ARQUIVAMENTO_INTERMEDIARIO:
				classe = "arquivamento";
				break;
			case COPIA:
				classe = "copia";
				break;
			case JUNTADA:
				classe = "juntada";
				break;
			case JUNTADA_EXTERNO:
				classe = "juntada_externo";
				break;
			case CANCELAMENTO_JUNTADA:
				classe = "desentranhamento";
				break;
			case REFERENCIA:
				classe = "vinculo";
				break;
			case ENCERRAMENTO_DE_VOLUME:
				classe = "encerramento_volume";
				break;
			case APENSACAO:
				classe = "apensacao";
				break;
			case DESAPENSACAO:
				classe = "desapensacao";
				break;
			case DESPACHO:
			case DESPACHO_TRANSFERENCIA:
			case DESPACHO_INTERNO_TRANSFERENCIA:
			case DESPACHO_TRANSFERENCIA_EXTERNA:
				classe = "despacho";
				break;
			case TRANSFERENCIA:
			case TRANSFERENCIA_EXTERNA:
			case RECEBIMENTO_TRANSITORIO:
				classe = "transferencia";
				break;
			case RECEBIMENTO:
				classe = "recebimento";
				break;
			case CRIACAO:
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

	public ITipoDeMovimentacao getExTipoMovimentacao() {
		return exTipoMovimentacao;
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
	
	public boolean isTipoMovimentacaoArquivamento() {
		return ExTipoDeMovimentacao.hasArquivado(exTipoMovimentacao);
	}
}
