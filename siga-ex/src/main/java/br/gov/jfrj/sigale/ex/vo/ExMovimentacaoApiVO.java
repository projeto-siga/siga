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

import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANOTACAO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_PERMANENTE;
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
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_ENCERRAMENTO_DE_VOLUME;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA_EXTERNO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICACAO_PUBL_BI;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO_TRANSITORIO;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_REFERENCIA;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA;
import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.ocpsoft.prettytime.PrettyTime;

import com.auth0.jwt.JWTVerifier;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;

public class ExMovimentacaoApiVO extends ExApiVO {
	private static final String JWT_FIXED_HEADER = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.";
	private static final String JWT_FIXED_HEADER_REPLACEMENT = "!";
	String classe;
	boolean originadaAqui;
	boolean desabilitada;
	boolean cancelada;
	String descricao;
	long idTpMov;
	String complemento;
	Map<String, ExParteApiVO> parte = new TreeMap<String, ExParteApiVO>();
	Date dtIniMov;
	String dtRegMovDDMMYYHHMMSS;
	String dtFimMovDDMMYYHHMMSS;
	String descrTipoMovimentacao;
	long idMov;
	// ExMobilVO mobVO;
	int duracaoSpan;
	int duracaoSpanExibirCompleto;
	String duracao;
	String mimeType;
	String lotaCadastranteSigla;
	String exTipoMovimentacaoSigla;
	String tempoRelativo;

	public ExMovimentacaoApiVO(ExMobilApiVO mobVO, ExMovimentacao mov,
			DpPessoa cadastrante, DpPessoa titular, DpLotacao lotaTitular) {
		originadaAqui = (mov.getExMobil().getId().equals(mobVO.id));
		idTpMov = mov.getExTipoMovimentacao().getIdTpMov();
		dtIniMov = mov.getDtIniMov();

		mimeType = (mov.getConteudoTpMov() == null) ? "" : mov
				.getConteudoTpMov();

		this.idMov = mov.getIdMov();
		this.dtRegMovDDMMYYHHMMSS = mov.getDtRegMovDDMMYYHHMMSS();
		this.tempoRelativo = calcularTempoRelativo(mov.getDtIniMov());
		this.descrTipoMovimentacao = mov.getDescrTipoMovimentacao();
		this.cancelada = mov.getExMovimentacaoCanceladora() != null;
		this.lotaCadastranteSigla = mov.getLotaCadastrante().getSigla();
		this.exTipoMovimentacaoSigla = mov.getExTipoMovimentacao().getSigla();

		if (mov.getLotaCadastrante() != null)
			parte.put("lotaCadastrante",
					new ExParteApiVO(mov.getLotaCadastrante()));
		if (mov.getCadastrante() != null)
			parte.put("cadastrante", new ExParteApiVO(mov.getCadastrante()));
		if (mov.getLotaSubscritor() != null)
			parte.put("lotaSubscritor", new ExParteApiVO(mov.getLotaSubscritor()));
		if (mov.getSubscritor() != null)
			parte.put("subscritor", new ExParteApiVO(mov.getSubscritor()));
		if (mov.getLotaResp() != null)
			parte.put("lotaResp", new ExParteApiVO(mov.getLotaResp()));
		if (mov.getResp() != null)
			parte.put("resp", new ExParteApiVO(mov.getResp()));

		descricao = mov.getObs();

		addAcoes(mov, cadastrante, titular, lotaTitular);

		calcularClasse(mov);

		desabilitada = mov.getExMovimentacaoRef() != null
				&& mov.getExMovimentacaoRef().isCancelada()
				|| mov.getExMovimentacaoCanceladora() != null
				|| mov.getIdTpMov().equals(
						TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO);

	}

	/**
	 * @param mov
	 * @param titular
	 * @param lotaTitular
	 * @throws Exception
	 */
	private void addAcoes(ExMovimentacao mov, DpPessoa cadastrante,
			DpPessoa titular, DpLotacao lotaTitular) {
		if (complemento == null)
			complemento = "";

		if (idTpMov == TIPO_MOVIMENTACAO_COPIA) {
			descricao = null;
			String mensagemPos = null;

			if (!mov.getExMobilRef()
					.getExDocumento()
					.getDescrDocumento()
					.equals(mov.getExMobil().getExDocumento()
							.getDescrDocumento()))
				mensagemPos = " Descrição: "
						+ mov.getExMobilRef().getExDocumento()
								.getDescrDocumento();

			addAcao(null, mov.getExMobilRef().getSigla(),
					"/app/expediente/doc", "exibir", true, null, "sigla="
							+ mov.getExMobilRef().getSigla(),
					"Copia do documento: ", mensagemPos, null);
		}

		if (idTpMov == TIPO_MOVIMENTACAO_JUNTADA
				|| idTpMov == TIPO_MOVIMENTACAO_JUNTADA_EXTERNO) {
			descricao = null;
			if (originadaAqui) {
				if (mov.getExMobilRef() != null) {

					String mensagemPos = null;

					if (!mov.getExMobilRef()
							.getExDocumento()
							.getDescrDocumento()
							.equals(mov.getExMobil().getExDocumento()
									.getDescrDocumento()))
						mensagemPos = " Descrição: "
								+ mov.getExMobilRef().getExDocumento()
										.getDescrDocumento();

					addAcao(null, mov.getExMobilRef().getSigla(),
							"/app/expediente/doc", "exibir", true, null,
							"sigla=" + mov.getExMobilRef().getSigla(),
							"Juntado ao documento: ", mensagemPos, null);
				} else {
					descricao = "Juntado ao documento: " + mov.getDescrMov();
				}
			} else {

				String mensagemPos = null;

				if (!mov.getExMobil()
						.getExDocumento()
						.getDescrDocumento()
						.equals(mov.getExMobilRef().getExDocumento()
								.getDescrDocumento()))
					mensagemPos = " Descrição: "
							+ mov.getExDocumento().getDescrDocumento();

				addAcao(null, mov.getExMobil().getSigla(),
						"/app/expediente/doc", "exibir", true, null, "sigla="
								+ mov.getExMobil().getSigla(),
						"Documento juntado: ", mensagemPos, null);
			}
		}

		if (idTpMov == TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA) {
			descricao = null;
			if (originadaAqui) {
				if (mov.getExMobilRef() != null) {

					String mensagemPos = null;

					if (!mov.getExMobilRef()
							.getExDocumento()
							.getDescrDocumento()
							.equals(mov.getExMobil().getExDocumento()
									.getDescrDocumento()))
						mensagemPos = " Descrição: "
								+ mov.getExMobilRef().getExDocumento()
										.getDescrDocumento();

					addAcao(null, mov.getExMobilRef().getSigla(),
							"/app/expediente/doc", "exibir", true, null,
							"sigla=" + mov.getExMobilRef().getSigla(),
							"Desentranhado do documento: ", mensagemPos, null);
				} else {
					descricao = "Desentranhado do documento: "
							+ mov.getDescrMov();
				}
			} else {

				String mensagemPos = null;

				if (!mov.getExMobil()
						.getExDocumento()
						.getDescrDocumento()
						.equals(mov.getExMobilRef().getExDocumento()
								.getDescrDocumento()))
					mensagemPos = " Descrição: "
							+ mov.getExDocumento().getDescrDocumento();

				addAcao(null, mov.getExMobil().getSigla(),
						"/app/expediente/doc", "exibir", true, null, "sigla="
								+ mov.getExMobil().getSigla(),
						"Documento desentranhado: ", mensagemPos, null);
			}
		}

		if (idTpMov == TIPO_MOVIMENTACAO_APENSACAO) {
			descricao = null;
			if (originadaAqui) {
				addAcao(null, mov.getExMobilRef().getSigla(),
						"/app/expediente/doc", "exibir", true, null, "sigla="
								+ mov.getExMobilRef().getSigla(),
						"Apensado ao documento: ", null, null);
			} else {
				addAcao(null, mov.getExMobil().getSigla(),
						"/app/expediente/doc", "exibir", true, null, "sigla="
								+ mov.getExMobil().getSigla(),
						"Documento apensado: ", null, null);
			}
		}

		if (idTpMov == TIPO_MOVIMENTACAO_DESAPENSACAO) {
			descricao = null;
			if (originadaAqui) {
				addAcao(null, mov.getExMobilRef().getSigla(),
						"/app/expediente/doc", "exibir", true, null, "sigla="
								+ mov.getExMobilRef().getSigla(),
						"Desapensado do documento: ", null, null);
			} else {
				addAcao(null, mov.getExMobil().getSigla(),
						"/app/expediente/doc", "exibir", true, null, "sigla="
								+ mov.getExMobil().getSigla(),
						"Documento desapensado: ", null, null);
			}
		}

		if (idTpMov == TIPO_MOVIMENTACAO_NOTIFICACAO_PUBL_BI) {
			addAcao(null, mov.getExMobilRef().getSigla(),
					"/app/expediente/doc", "exibir", true, null, "sigla="
							+ mov.getExMobilRef().getSigla(),
					"Publicado no Boletim Interno: ",
					" em " + mov.getDtMovDDMMYY(), null);
		}

		if (idTpMov == TIPO_MOVIMENTACAO_REFERENCIA) {
			descricao = null;
			if (originadaAqui) {
				addAcao(null, mov.getExMobilRef().getSigla(),
						"/app/expediente/doc", "exibir", true, null, "sigla="
								+ mov.getExMobilRef().getSigla(),
						"Ver também: ", " Descrição: "
								+ mov.getExMobilRef().getExDocumento()
										.getDescrDocumento(), null);
			} else {
				addAcao(null, mov.getExMobil().getSigla(),
						"/app/expediente/doc", "exibir", true, null, "sigla="
								+ mov.getExMobil().getSigla(), "Ver também: ",
						" Descrição: "
								+ mov.getExDocumento().getDescrDocumento(),
						null);
			}
		}

		if (idTpMov == TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO) {
			descricao = null;
			if (originadaAqui) {
				addAcao(null, mov.getExMobilRef().getSigla(),
						"/app/expediente/doc", "exibir", true, null, "sigla="
								+ mov.getExMobilRef().getSigla(), "", null,
						null);
			} else {
				addAcao(null, mov.getExMobil().getSigla(),
						"/app/expediente/doc", "exibir", true, null, "sigla="
								+ mov.getExMobil().getSigla(), "", null, null);
			}
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

	public String getDescricao() {
		if (descricao != null) {
			descricao = descricao.trim();
			if (descricao.length() == 0)
				return null;
		}
		return descricao;
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

	public Map<String, ExParteApiVO> getParte() {
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

	public void setOriginadaAqui(boolean originadaAqui) {
		this.originadaAqui = originadaAqui;
	}

	@Override
	public String toString() {
		return getIdMov() + " - " + descrTipoMovimentacao + " - "
				+ getDescricao() + "[" + getAcoes() + "] " + getDisabled();
	}

	public boolean isImage() {
		return mimeType.startsWith("image/");
	}

	public boolean isPDF() {
		return mimeType.endsWith("/pdf");
	}

	public boolean isWord() {
		return mimeType.endsWith("/msword")
				|| mimeType.endsWith(".wordprocessingml.document");
	}

	public boolean isExcel() {
		return mimeType.endsWith("/vnd.ms-excel")
				|| mimeType.endsWith(".spreadsheetml.sheet");
	}

	public boolean isPresentation() {
		return mimeType.endsWith("/vnd.ms-powerpoint")
				|| mimeType.endsWith(".presentationml.presentation");
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
			pwd = System.getProperty("siga.ex.webdav.pwd");
		} catch (Exception e) {
			throw new AplicacaoException(
					"Erro obtendo propriedade siga.ex.webdav.pwd", 0, e);
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

	public static Map<String, Object> getWebdavDecodedToken(String token) {
		final JWTVerifier verifier = new JWTVerifier(getWebdavPassword());
		try {
			Map<String, Object> map = verifier.verify(token.replace("~", ".")
					.replace(JWT_FIXED_HEADER_REPLACEMENT, JWT_FIXED_HEADER));
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
	
	private String calcularTempoRelativo(Date anterior) {
		PrettyTime p = new PrettyTime(new Date(), new Locale("pt"));

		String tempo = p.format(anterior);
		tempo = tempo.replace(" atrás", "");
		tempo = tempo.replace(" dias", " dias");
		tempo = tempo.replace(" horas", "h");
		tempo = tempo.replace(" minutos", "min");
		tempo = tempo.replace(" segundos", "s");
		tempo = tempo.replace("agora há pouco", "agora");
		return tempo;
	}

}
