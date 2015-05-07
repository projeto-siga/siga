package br.gov.jfrj.siga.ex;
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
 */


import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Entity;

import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.xerces.impl.dv.util.Base64;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.jboss.logging.Logger;

import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.lucene.HtmlBridge;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpResponsavel;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExAcesso;
import br.gov.jfrj.siga.ex.util.AnexoNumeradoComparator;
import br.gov.jfrj.siga.ex.util.Compactador;
import br.gov.jfrj.siga.ex.util.DocumentoFilhoComparator;
import br.gov.jfrj.siga.ex.util.ProcessadorHtml;
import br.gov.jfrj.siga.ex.util.ProcessadorReferencias;
import br.gov.jfrj.siga.ex.util.TipoMobilComparatorInverso;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

/**
 * A class that represents a row in the 'EX_DOCUMENTO' table. This class may be
 * customized as it is never re-generated after being created.
 */
@Entity
@Indexed
public class ExDocumento extends AbstractExDocumento implements Serializable {

	/**
         * 
         */
	private static final long serialVersionUID = -1462217739890785344L;

	private static final Logger log = Logger.getLogger(ExDocumento.class);

	private byte[] cacheConteudoBlobDoc;

	private String descrDocumentoAI;

	/**
	 * Simple constructor of ExDocumento instances.
	 */
	public ExDocumento() {
	}

	@Override
	public Long getIdDoc() {
		// TODO Auto-generated method stub
		return super.getIdDoc();
	}

	/**
	 * Retorna o documento pai, a partir do móbil pai.
	 */
	public ExDocumento getPai() {
		if (getExMobilPai() == null)
			return null;
		return getExMobilPai().getExDocumento();
	}

	/**
	 * Retorna o nível de acesso (não a descrição) atual do documento.
	 */

	// @Override
	public ExNivelAcesso getExNivelAcesso() {
		ExNivelAcesso nivel = getDnmExNivelAcesso();
		if (nivel == null)
			return Ex.getInstance().getBL().atualizarDnmNivelAcesso(this);
		return nivel;
	}

	/**
	 * Retorna o nível de acesso (não a descrição) do documento definido no
	 * momento da criação do documento, desconsiderando as redefinições de
	 * nível.
	 */
	public ExNivelAcesso getExNivelAcessoDoDocumento() {
		return super.getExNivelAcesso();
	}

	/**
	 * Retorna o código do documento.
	 */
	public String getSigla() {
		return getCodigo();
	}

	/**
	 * Retorna a classificação atual do documento.
	 */
	public ExClassificacao getExClassificacaoAtual() {
		ExClassificacao cl = null;
		if (getMobilGeral() != null
				&& getMobilGeral().getUltimaMovimentacaoNaoCancelada() != null)
			cl = getMobilGeral().getUltimaMovimentacaoNaoCancelada()
					.getExClassificacao();
		if (cl != null)
			return cl;
		return super.getExClassificacao();
	}

	/**
	 * Retorna lista com todos os documentos que são filhos do documento atual.
	 */
	public Set<ExDocumento> getTodosDocumentosFilhosSet() {
		Set<ExDocumento> docsFilhos = new HashSet<ExDocumento>();
		for (ExMobil m : getExMobilSet()) {
			docsFilhos.addAll(m.getExDocumentoFilhoSet());
		}
		return docsFilhos;
	}

	/**
	 * Retorna o código do documento.
	 * 
	 * @throws Exception
	 */
	public String getCodigo() {
		if (getExMobilPai() != null && getNumSequencia() != null) {
			String s = getNumSequencia().toString();
			while (s.length() < 2)
				s = "0" + s;

			return getExMobilPai().getSigla() + "." + s;
		}
		if (getAnoEmissao() != null && getNumExpediente() != null) {
			String s = getNumExpediente().toString();
			while (s.length() < 5)
				s = "0" + s;

			if (getOrgaoUsuario() != null) {
				try {
					if (getAnoEmissao() >= SigaExProperties
							.getAnoInicioAcronimoNoCodigoDoDocumento()) {
						return getOrgaoUsuario().getAcronimoOrgaoUsu() + "-"
								+ getExFormaDocumento().getSiglaFormaDoc()
								+ "-" + getAnoEmissao() + "/" + s;
					} else {
						return getOrgaoUsuario().getSiglaOrgaoUsu() + "-"
								+ getExFormaDocumento().getSiglaFormaDoc()
								+ "-" + getAnoEmissao() + "/" + s;
					}
				} catch (Exception ex) {
					throw new Error(ex);
				}
			}
		}
		if (getIdDoc() == null)
			return "NOVO";
		return "TMP-" + getIdDoc();
	}

	/**
	 * Retorna o código do documento de modo resumido. Para resumir, o método
	 * considera o órgão de quem está solicitando o resumo e, opcionalmente, o
	 * documento de referência
	 * 
	 * @throws Exception
	 */
	public String getCodigoResumido(CpOrgaoUsuario orgaoUsu, ExDocumento docRef) {
		String cod = getCodigo();
		if (docRef != null && getNumSequencia() != null) {
			if (docRef.equals(this))
				return "";
			if (getExMobilPai() != null && getExMobilPai().doc().equals(docRef)
					&& isProcesso()) {
				String s = getNumSequencia().toString();
				while (s.length() < 2)
					s = "0" + s;
				return "." + s;
			}
		}
		if (getOrgaoUsuario() != null && orgaoUsu != null
				&& orgaoUsu.equivale(getOrgaoUsuario())) {
			cod = cod
					.replace(getOrgaoUsuario().getAcronimoOrgaoUsu() + "-", "");
			cod = cod.replace(getOrgaoUsuario().getSiglaOrgaoUsu() + "-", "");
		}
		if (getExFormaDocumento() != null) {
			cod = cod.replace(getExFormaDocumento().getSiglaFormaDoc() + "-",
					getExFormaDocumento().getSiglaFormaDoc());
		}
		cod = cod.replace("/0000", "/");
		cod = cod.replace("/000", "/");
		cod = cod.replace("/00", "/");
		cod = cod.replace("/0", "/");
		String anoAtual = String.valueOf(Calendar.getInstance().get(
				Calendar.YEAR));
		if (getAnoEmissao() != null && anoAtual != null
				&& anoAtual.equals(String.valueOf(getAnoEmissao())))
			cod = cod.replace(getAnoEmissao() + "/", "");

		return cod;
	}

	/**
	 * Retorna o código do documento.
	 * 
	 * @throws Exception
	 */
	public static String getCodigo(Long idDoc, String siglaOrgaoUsu,
			String acronimoOrgaoUsu, String siglaFormaDoc, Long anoEmissao,
			Long numExpediente, Integer docNumSequencia, Long idTipoMobil,
			Integer mobilNumSequencia, Long pai_idDoc,
			String pai_siglaOrgaoUsu, String pai_acronimoOrgaoUsu,
			String pai_siglaFormaDoc, Long pai_anoEmissao,
			Long pai_numExpediente, Integer pai_docNumSequencia,
			Long pai_idTipoMobil, Integer pai_mobilNumSequencia) {

		if (pai_siglaOrgaoUsu != null && pai_acronimoOrgaoUsu != null
				&& pai_siglaFormaDoc != null && pai_anoEmissao != null
				&& pai_numExpediente != null && docNumSequencia != null) {
			String s = docNumSequencia.toString();
			while (s.length() < 2)
				s = "0" + s;

			return ExMobil.getSigla(
					getCodigo(pai_idDoc, pai_siglaOrgaoUsu,
							pai_acronimoOrgaoUsu, pai_siglaFormaDoc,
							pai_anoEmissao, pai_numExpediente,
							pai_docNumSequencia, pai_idTipoMobil,
							pai_mobilNumSequencia, null, null, null, null,
							null, null, null, null, null),
					pai_mobilNumSequencia, pai_idTipoMobil)
					+ "." + s;
		}
		if (anoEmissao != null && numExpediente != null) {
			String s = numExpediente.toString();
			while (s.length() < 5)
				s = "0" + s;

			if (siglaOrgaoUsu != null) {
				try {
					Long l_anoEmissao = Long.valueOf(anoEmissao);
					if (l_anoEmissao >= SigaExProperties
							.getAnoInicioAcronimoNoCodigoDoDocumento()) {
						return acronimoOrgaoUsu + "-" + siglaFormaDoc + "-"
								+ anoEmissao + "/" + s;
					} else {
						return siglaOrgaoUsu + "-" + siglaFormaDoc + "-"
								+ anoEmissao + "/" + s;
					}
				} catch (Exception ex) {
					throw new Error(ex);
				}
			}

		}

		if (idDoc == null)
			return "NOVO";

		return "TMP-" + idDoc;
	}

	/**
	 * Retorna o código do documento sem "-" ou "/".
	 */
	public String getCodigoCompacto() {
		String s = getCodigo();
		if (s == null)
			return null;
		return s.replace("-", "").replace("/", "");
	}

	/**
	 * Retorna o código do documento. Se o documento for de origem externa,
	 * adiciona ao código do documento o código externo. Se for interno
	 * importado, adiciona ao código do documento o número antigo.
	 */
	@Field(name = "codigo", store = Store.COMPRESS, index = Index.NO)
	public String getCodigoString() {
		String s = getCodigo();

		if (getNumExtDoc() != null)
			s += " (" + getNumExtDoc() + ")";
		if (getNumAntigoDoc() != null)
			s += " [" + getNumAntigoDoc() + "]";
		return s;
	}

	/**
	 * Retorna o conteúdo (blob) do documento em formato String. Este método
	 * <b>parece</b> estar em desuso.
	 */
	public String getConteudo() {
		if (getConteudoBlobDoc() != null)
			return new String(getConteudoBlobDoc2());
		return "";
	}

	/**
	 * Retorna, em formato array de bytes, o conteúdo de um arquivo contido no
	 * zip gravado no blob do documento.
	 * 
	 * @param nome
	 *            Nome do arquivo compactado cujo conteúdo será retornado
	 */
	public byte[] getConteudoBlob(final String nome) {
		final byte[] conteudoZip = getConteudoBlobDoc2();
		byte[] conteudo = null;
		final Compactador zip = new Compactador();
		if (conteudoZip != null) {
			conteudo = zip.descompactarStream(conteudoZip, nome);
		}
		return conteudo;
	}

	/**
	 * Retorna, em formato array de bytes, todo o conteúdo do zip gravado no
	 * blob do documento.
	 */
	public byte[] getConteudoBlobDoc2() {

		if (cacheConteudoBlobDoc == null)
			cacheConteudoBlobDoc = br.gov.jfrj.siga.cp.util.Blob
					.toByteArray(getConteudoBlobDoc());
		return cacheConteudoBlobDoc;

	}

	/**
	 * Retorna, em formato array de bytes, o conteúdo do arquivo de
	 * <b>formulário</b> contido no zip gravado no blob do documento.
	 */
	public byte[] getConteudoBlobForm() {
		return getConteudoBlob("doc.form");
	}

	/**
	 * Retorna, em formato array de bytes, o conteúdo do arquivo de
	 * <b>resumo</b> contido no zip gravado no blob do documento.
	 */
	public byte[] getConteudoBlobResumo() {
		return getConteudoBlob("doc.resumo");
	}

	/**
	 * Retorna, em formato array de bytes, o conteúdo do arquivo <b>html</b>
	 * contido no zip gravado no blob do documento.
	 */
	@Field(name = "conteudoBlobDocHtml")
	@FieldBridge(impl = HtmlBridge.class)
	@Analyzer(impl = BrazilianAnalyzer.class)
	public byte[] getConteudoBlobHtml() {
		return getConteudoBlob("doc.htm");
	}

	/**
	 * Retorna, <b>em formato Base64 (String)</b>, o conteúdo do arquivo
	 * <b>html</b> contido no zip gravado no blob do documento.
	 */
	public String getConteudoBlobHtmlB64() {
		return Base64.encode(getConteudoBlobHtml());
	}

	/**
	 * Retorna, <b>em formato ISO-8859-1 (String)</b>, o conteúdo do arquivo
	 * <b>html</b> contido no zip gravado no blob do documento.
	 */
	public String getConteudoBlobHtmlString() {
		byte[] ab = getConteudoBlobHtml();
		if (ab == null)
			return null;
		try {
			return new String(ab, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			return new String(ab);
		}
	}

	/**
	 * Retorna o conteúdo do arquivo <b>html</b> contido no zip gravado no blob
	 * do documento, com todas as referencias para outros documentos
	 * substituidas por links html para os devidos documentos.
	 * 
	 * @throws Exception
	 */
	public String getConteudoBlobHtmlStringComReferencias() throws Exception {
		String sHtml = getConteudoBlobHtmlString();
		ProcessadorReferencias pr = new ProcessadorReferencias();
		pr.ignorar(getSigla());
		sHtml = pr.marcarReferencias(sHtml);

		// Verifica se todos os subscritores assinaram o documento
		try {
			for (DpPessoa subscritor : getSubscritorECosignatarios()) {
				if (isEletronico() && isFinalizado()
						&& !jaAssinadoPor(subscritor)) {
					String comentarioInicio = "<!-- INICIO SUBSCRITOR "
							+ subscritor.getId() + " -->";
					String comentarioFim = "<!-- FIM SUBSCRITOR "
							+ subscritor.getId() + " -->";

					if (sHtml.contains(comentarioInicio)
							&& sHtml.contains(comentarioFim)) {
						String blocoSubscritor = sHtml.substring(
								sHtml.indexOf(comentarioInicio)
										+ comentarioInicio.length(),
								sHtml.indexOf(comentarioFim));

						StringBuilder sb = new StringBuilder();
						sb.append("<span style=\"color:#CD3700;\">");
						sb.append(blocoSubscritor);
						sb.append("</span>");

						sHtml = sHtml.replace(blocoSubscritor, sb).toString();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return sHtml;
	}

	/**
	 * Retorna, em formato array de bytes, o conteúdo do arquivo <b>pdf</b>
	 * contido no zip gravado no blob do documento.
	 */
	public byte[] getConteudoBlobPdf() {
		return getConteudoBlob("doc.pdf");
	}

	/**
	 * Retorna, <b>em formato Base64 (String)</b>, o conteúdo do arquivo
	 * <b>pdf</b> contido no zip gravado no blob do documento.
	 */
	public String getConteudoBlobPdfB64() {
		return Base64.encode(getConteudoBlobPdf());
	}

	/**
	 * Retorna um descrição do documento com no máximo 40 caracteres.
	 */
	public java.lang.String getDescrCurta() {
		if (getDescrDocumento() == null)
			return "[sem descri��o]";
		if (getDescrDocumento().length() > 40)
			return getDescrDocumento().substring(0, 39) + "...";
		else
			return getDescrDocumento();
	}

	/**
	 * Retorna a descrição completa do documento.
	 */
	@Field(name = "descrDocumento", store = Store.COMPRESS)
	@Analyzer(impl = BrazilianAnalyzer.class)
	@Override
	public String getDescrDocumento() {
		return super.getDescrDocumento();
	}

	/**
	 * Retorna a descrição completa do documento de modo indiferente à
	 * acentuação.
	 */
	public String getDescrDocumentoAI() {
		return descrDocumentoAI;
	}

	/**
	 * Retorna a <b>descrição</b> do nível de acesso do documento definido no
	 * momento da criação do documento, desconsiderando as redefinições de
	 * nível.
	 */
	@Field(name = "nivelAcesso", store = Store.COMPRESS)
	public String getNivelAcesso() {
		log.debug("[getNivelAcesso] - Obtendo Nivel de Acesso do documento, definido no momento da cria��o do mesmo");
		String nivel = null;
		ExNivelAcesso nivelAcesso = getExNivelAcesso();

		if (nivelAcesso != null && nivelAcesso.getGrauNivelAcesso() != null) {
			nivel = nivelAcesso.getGrauNivelAcesso().toString();

		} else {
			log.warn("[getNivelAcesso] - O nível de acesso ou o grau do n�vel de acesso do documento � nulo.");
		}

		return nivel;
	}

	/**
	 * Retorna a descrição do destinatário de um documento, conforme as
	 * seguintes regras, na seguinte ordem:
	 * <ul>
	 * <li>Se foi definida uma pessoa destinatária, retorna a descrição dela com
	 * iniciais maiúsculas (getDescricaoIniciaisMaiusculas())</li>
	 * <li>Ou então, se for definido um destinatário em campo livre, retorna o
	 * valor digitado.</li>
	 * <li>Ou então, se for definida uma lotação destinatária, retorna a
	 * descrição dela.</li>
	 * <li>Ou então, se for definido um órgão externo destinatário, retorna a
	 * descrição do órgão mais a observação sobre o órgão, se houver, ou apenas
	 * esta última, se não for selecionado órgão mas for definida descrição.</li>
	 * <li></li>
	 * </ul>
	 */
	@Field(name = "destinatarioString", index = Index.NO, store = Store.COMPRESS)
	public String getDestinatarioString() {
		if (getDestinatario() != null)
			return getDestinatario().getDescricaoIniciaisMaiusculas();
		else if (getNmDestinatario() != null)
			return getNmDestinatario();
		else if (getLotaDestinatario() != null)
			return getLotaDestinatario().getDescricao();
		else if (getOrgaoExternoDestinatario() != null)
			if (getNmOrgaoExterno() != null && !getNmOrgaoExterno().equals(""))
				return getOrgaoExternoDestinatario().getDescricao() + ";"
						+ getNmOrgaoExterno();
			else
				return getOrgaoExternoDestinatario().getDescricao();
		else if (getNmOrgaoExterno() != null && !getNmOrgaoExterno().equals(""))
			return getNmOrgaoExterno();
		return null;
	}

	/**
	 * Retorna, em um caracter, o dia que faz parte da data do documento
	 */
	public String getDtD() {
		SimpleDateFormat df1 = new SimpleDateFormat();
		try {
			df1.applyPattern("d");
			return df1.format(getDtDoc());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Retorna a data do documento no formato dd/mm/aa, por exemplo, 01/02/10.
	 */
	@Field(name = "dtDocDDMMYY", index = Index.NO, store = Store.COMPRESS)
	public String getDtDocDDMMYY() {
		if (getDtDoc() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			return df.format(getDtDoc());
		}
		return "";
	}

	/**
	 * Retorna a data original do documento externo no formato dd/mm/aa, por
	 * exemplo, 01/02/2010.
	 */
	@Field(name = "dtDocOriginalDDMMYYYY", index = Index.NO, store = Store.COMPRESS)
	public String getDtDocOriginalDDMMYYYY() {
		if (getDtDocOriginal() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(getDtDocOriginal());
		}
		return "";
	}

	/**
	 * Retorna a data do documento no formato dd/mm/aaaa, por exemplo,
	 * 01/02/2010.
	 */
	public String getDtDocDDMMYYYY() {
		if (getDtDoc() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(getDtDoc());
		}
		return "";
	}

	/**
	 * Retorna a data de finalização do documento no formato dd/mm/aa, por
	 * exemplo, 01/02/10.
	 */
	public String getDtFinalizacaoDDMMYY() {

		if (isFinalizado()) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			return df.format(getDtFinalizacao());
		}
		return "";
	}

	/**
	 * Retorna o nome da localidade (município) onde se encontra a lotação em
	 * que o documento foi produzido, caso não tenha sido digitado valor para a
	 * localidade no campo Função;Lotação;Localidade. A escolha da lotação para
	 * obtenção da localidade obedece à seguinte regra de precedência:
	 * <ul>
	 * <li>Lotação titular;</li>
	 * <li>Lotação subscritor;</li>
	 * <li>Lotação cadastrante;</li>
	 * </ul>
	 */
	public String getLocalidadeString() {
		String s = getNmLocalidade();

		DpLotacao lotaBase = null;
		if (getLotaTitular() != null)
			lotaBase = getLotaTitular();
		else if (getLotaSubscritor() != null)
			lotaBase = getLotaSubscritor();
		else if (getLotaCadastrante() != null)
			lotaBase = getLotaCadastrante();

		if (s == null && lotaBase != null) {
			s = lotaBase.getLocalidadeString();
		}

		return s;
	}

	/**
	 * Retorna o nome da localidade em caixa alta, com base em
	 * getLocalidadeString()
	 */
	public String getLocalidadeStringMaiusculas() {
		return getLocalidadeString().toUpperCase();
	}

	/**
	 * Retorna a data do documento por extenso. no formato "Rio de Janeiro, 01
	 * de fevereiro de 2010", por exemplo.
	 */
	public String getDtExtenso() {
		// Forçando a ficar em pt_BR, antes a data aparecia na linguagem
		// definida no servidor de aplicação (tomcat, jbos, etc.)
		SimpleDateFormat df1 = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy.",
				new Locale("pt", "BR"));
		try {
			// As linhas abaixo foram comentadas porque o formato já está
			// definido na declaração da variável df1.
			//
			// df1.applyPattern("dd/MM/yyyy");
			// df1.applyPattern("dd 'de' MMMM 'de' yyyy.");
			String s = Texto.maiusculasEMinusculas(getLocalidadeString());

			return s + ", " + df1.format(getDtDoc()).toLowerCase();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Retorna a data do documento por extenso <b>sem localidade</b>, no formato
	 * "01 de fevereiro de 2010", por exemplo.
	 */
	public String getDtExtensoSemLocalidade() {
		// Forçando a ficar em pt_BR, antes a data aparecia na linguagem
		// definida no servidor de aplicação (tomcat, jbos, etc.)

		SimpleDateFormat df1 = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy.",
				new Locale("pt", "BR"));
		try {
			// As linhas abaixo foram comentadas porque o formato já está
			// definido na declaração da variável df1.
			//
			// df1.applyPattern("dd/MM/yyyy");
			// df1.applyPattern("dd 'de' MMMM 'de' yyyy.");

			return df1.format(getDtDoc()).toLowerCase();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Retorna a data do documento sem localidade por extenso, em maiúsculas, no
	 * formato "01 DE FEVEREIRO DE 2010", por exemplo.
	 * 
	 */
	public String getDtExtensoMaiusculasSemLocalidade() {
		String data = getDtExtensoSemLocalidade();
		if (data != null)
			return data.toUpperCase();
		return "";
	}

	/**
	 * Retorna o mês que faz parte da data do documento
	 * 
	 */
	public String getDtMMMM() {
		SimpleDateFormat df1 = new SimpleDateFormat();
		try {
			df1.applyPattern("MMMM");
			return df1.format(getDtDoc()).toLowerCase();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Retorna a data de registro do documento no formato dd/mm/aa, por exemplo,
	 * 01/02/10.
	 */
	public String getDtRegDocDDMMYY() {
		if (getDtRegDoc() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			return df.format(getDtRegDoc());
		}
		return "";
	}

	/**
	 * Retorna a data de disponibilização da última movimentação do móbil geral,
	 * no formato dd/MM/yy.<b> Obs.: não corresponde exatamente ao nome do
	 * método.</b>
	 */
	public String getDtDispUltimoAgendamento() {
		Date dt = new Date();
		if (getMobilGeral().getUltimaMovimentacaoNaoCancelada() != null)
			dt = getMobilGeral().getUltimaMovimentacaoNaoCancelada()
					.getDtDispPublicacao();
		if (dt != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			return df.format(dt);
		}
		return "";
	}

	/**
	 * Retorna a data da última movimentação do Mobil Geral, no formato
	 * dd/mm/yy, por exemplo, 12/10/10. <b>Obs.: não está corredpondendo
	 * exatamente ao nome do método.</b>
	 */
	public String getDtUltimaRemessaParaPublicacao() {
		if (getMobilGeral().getUltimaMovimentacaoNaoCancelada() != null)
			return getMobilGeral().getUltimaMovimentacaoNaoCancelada()
					.getDtMovDDMMYY();
		return "";
	}

	/**
	 * Retorna a data de registro do documento no formato dd/mm/aa HH:mm:ss, por
	 * exemplo, 01/02/2010 16:10:00.
	 * 
	 */
	public String getDtRegDocDDMMYYHHMMSS() {
		if (getDtRegDoc() != null) {
			final SimpleDateFormat df = new SimpleDateFormat(
					"dd/MM/yy HH:mm:ss");
			return df.format(getDtRegDoc());
		}
		return "";
	}

	/**
	 * Retorna o ano que faz parte da data do documento no formato aaaa, por
	 * exemplo, 2010.
	 */
	public String getDtYYYY() {
		SimpleDateFormat df1 = new SimpleDateFormat();
		try {
			df1.applyPattern("yyyy");
			return df1.format(getDtDoc());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Retorna o valor digitado para a <b>função</b> no campo
	 * Função;Lotação;Localidade.
	 */
	public java.lang.String getNmFuncao() {
		if (getNmFuncaoSubscritor() == null)
			return null;
		String a[] = getNmFuncaoSubscritor().split(";");
		if (a.length < 1)
			return null;
		if (a[0].length() == 0)
			return null;
		return a[0];
	}

	/**
	 * Retorna valor digitado para a <b>lotação</b> no campo
	 * Função;Lotação;Localidade.
	 */
	public java.lang.String getNmLotacao() {
		if (getNmFuncaoSubscritor() == null)
			return null;
		String a[] = getNmFuncaoSubscritor().split(";");
		if (a.length < 2)
			return null;
		if (a[1].length() == 0)
			return null;
		return a[1];
	}

	/**
	 * Retorna valor digitado para a <b>localidade</b> no campo
	 * Função;Lotação;Localidade.
	 */
	public java.lang.String getNmLocalidade() {
		if (getNmFuncaoSubscritor() == null)
			return null;
		String a[] = getNmFuncaoSubscritor().split(";");
		if (a.length < 3)
			return null;
		if (a[2].length() == 0)
			return null;
		return a[2];
	}

	/**
	 * Verifica se um documento está finalizado, ou seja, se possui
	 * dtFinalizacao definida
	 */
	public boolean isFinalizado() {
		return getDtFinalizacao() != null;
	}

	/**
	 * Verifica se um documento pode ser indexado conforme as regras
	 * <b>(informar regras)</b>
	 */
	public Boolean isIndexavel() {
		return isAssinado() && !isCancelado();
	}

	/**
	 * Retorna o valor digitado para o <b>nome do subscritor</b> no campo
	 * Função;Lotação;Localidade.
	 */
	public java.lang.String getNmSubscritor() {
		if (getNmFuncaoSubscritor() == null)
			return null;
		String a[] = getNmFuncaoSubscritor().split(";");
		if (a.length < 4)
			return null;
		if (a[3].length() == 0)
			return null;
		return a[3];
	}

	/**
	 * Retorna o número da última via do documento.
	 */
	public int getNumUltimaVia() {
		int maxNumVia = 0;
		for (final ExMobil mob : getExMobilSet()) {
			if (mob.isVia() && mob.getNumSequencia() > maxNumVia) {
				maxNumVia = mob.getNumSequencia();
			}
		}
		return maxNumVia;
	}

	/**
	 * Retorna o número da primeira via do documento.
	 */
	@SuppressWarnings("static-access")
	public int getNumPrimeiraVia() {

		int minNumVia = 1;

		
		for (final ExMobil mob : getExMobilSet()) {
			if (mob.isVia()) {
				ExVia via = mob.getViaPCTT();
				if (via !=null && via.getExTipoDestinacao() != null && via.getExTipoDestinacao().getIdTpDestinacao() != null && via.getExTipoDestinacao().getIdTpDestinacao() == ExTipoDestinacao.TIPO_DESTINACAO_SETOR_COMPETENTE)
					minNumVia = mob.getNumSequencia();
			}
		}

		return minNumVia;
	}

	/**
	 * Retorna o número da última via não cancelada.
	 */
	public int getNumUltimaViaNaoCancelada() {
		ExMobil mobUltimaVia = null;
		for (ExMobil mob : getExMobilSet()) {
			if (mob.isVia() && !mob.isCancelada()) {
				mobUltimaVia = mob;
			}
		}

		if (mobUltimaVia == null)
			return 0;

		return mobUltimaVia.getNumSequencia();
	}

	/**
	 * COMPLETAR Retorna o set de Vias do documento de acordo com o modelo e a
	 * classificação do assunto. Se o o modelo possuir uma classificação
	 * específica para a criação de vias esta será utilizada, caso contrário,
	 * será utilizada a classificação do assunto.
	 * 
	 * @return Set<ExVia>
	 */
	public Set<ExVia> getSetVias() {
		Set<ExVia> vias = new HashSet<ExVia>();
		HashSet<ExVia> viasFinal = new HashSet<ExVia>();
		if (getExModelo() != null
				&& getExModelo().getExClassCriacaoVia() != null) {
			vias = getExModelo().getExClassCriacaoVia().getExViaSet();
		} else {
			if (getExClassificacao() != null)
				vias = getExClassificacaoAtual().getExViaSet();
		}

		// Edson: Antes da versão Destinação, quando se alterava uma via, a nova
		// instância
		// continuava apontando para a mesma classificação, pois esta não era
		// alterada junto. Isso não dava problema porque o exViaSet, no
		// exClassificacao.hbm.xml,
		// estava com his_ativo = 1, de modo que o hibernate montava o set só
		// com as vias ativas.
		// Como isso não permitiria saber quais eram as vias de uma
		// classificação num certo
		// momento do histórico, a alteração/criação/exclusão de via passou a
		// gerar outro registro
		// de classificação, e o his_ativo = 1 não está mais lá. Por isso, é
		// preciso limpar o set
		// de vias, garantido que só haja uma instância de cada via:
		HashMap<String, ExVia> viasUmaPorCodigo = new HashMap<String, ExVia>();
		for (ExVia v : vias) {
			ExVia vHash = viasUmaPorCodigo.get(v.getCodVia());
			if (vHash == null || v.getHisDtIni().after(vHash.getHisDtIni()))
				viasUmaPorCodigo.put(v.getCodVia(), v);
		}
		vias = new HashSet<ExVia>(viasUmaPorCodigo.values());

		if (vias != null
				&& vias.size() > 0
				&& ((ExVia) vias.toArray()[0]).getExTipoDestinacao()
						.getFacilitadorDest() != null)
			return vias;

		// Expediente externo ou eletrônico e com Documento Pai tem apenas 1 via
		if (getExTipoDocumento().getIdTpDoc() == 3 || isEletronico()
				|| getExMobilPai() != null) {
			if (vias != null)
				for (ExVia via : vias) {
					if (via.getExTipoDestinacao() != null
							&& via.getExTipoDestinacao()
									.getDescrTipoDestinacao()
									.contains("ompetente"))
						viasFinal.add(via);
				}
			if (viasFinal.size() == 0)
				for (ExVia via : vias) {
					if (via.getCodVia() == null
							|| Integer.parseInt(via.getCodVia()) == 1) {
						viasFinal.add(via);
						break;
					}
				}
		} else
			return vias;

		return viasFinal;
	}

	/**
	 * COMPLETAR Retorna o nome do subscritor.
	 * 
	 * @return Nome do subscritor.
	 */
	@Field(name = "subscritorString", index = Index.NO, store = Store.COMPRESS)
	public String getSubscritorString() {
		if (getSubscritor() != null)
			return getSubscritor().getDescricaoIniciaisMaiusculas();
		else if (getOrgaoExterno() != null || getObsOrgao() != null) {
			String str = "";
			if (getOrgaoExterno() != null)
				str = getOrgaoExterno().getDescricao();
			if (getObsOrgao() != null) {
				if (str.length() != 0)
					str = str + "; ";
				str = str + getObsOrgao();
			}
			if (getNmSubscritorExt() != null) {
				if (str.length() != 0)
					str = str + "; ";
				str = str + getNmSubscritorExt();
			}
			return str;
		} else
			return null;
	}

	/**
	 * COMPLETAR Retorna o nome do cadastrante.
	 * 
	 * @return Nome do cadastrante.
	 */
	@Field(name = "cadastranteString", index = Index.NO, store = Store.COMPRESS)
	public String getCadastranteString() {
		if (getCadastrante() != null)
			return getCadastrante().getDescricaoIniciaisMaiusculas();

		return "";
	}

	/**
	 * Retorna o subscritor do documento, caso exista. Se não, retorna o
	 * cadastrante do documento.
	 */
	public DpPessoa getEditor() {
		return getSubscritor() != null ? getSubscritor() : getCadastrante();
	}

	public Integer getTeste(final Integer i) {
		return i;
	}

	/**
	 * Retorna o nome do modelo do documento
	 */
	@Field(name = "nmMod", store = Store.COMPRESS)
	@Analyzer(impl = BrazilianAnalyzer.class)
	public String getNmMod() {
		if (getExModelo() != null)
			return getExModelo().getNmMod();
		return null;
	}

	/**
	 * Verifica se um documento já está assinado ou, sendo externo ou interno
	 * importado, se está finalizado.
	 */
	public boolean isAssinado() {
		// Interno antigo e externo são considerados como assinados
		if (getExTipoDocumento().getIdTpDoc() != 1L) {
			return getExMobilSet() != null && getExMobilSet().size() > 1;
		}

		/*if(isEletronico() && !isAssinadoEletronicoPorTodosOsSignatarios())
			return false;*/
		
		if(isEletronico() && !isAssinadoEletronicoOuPorSenhaPorTodosOsSignatarios())
			return false;
		
		if(!isEletronico()) {			
			ExMovimentacao mov = getMovAssinatura();
			if (mov == null)
				return false;
		}

		return true;
	}
	
	/**
	 * Verifica se um documento eletrônico possui pelo menos uma assinatura digital
	 */
	public boolean isEletronicoEPossuiPeloMenosUmaAssinaturaDigital() {
		if(!isEletronico())
			return false;
		
		for (ExMovimentacao m : getMobilGeral().getExMovimentacaoSet()) {
			if ((m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Verifica se um documento eletrônico possui pelo menos uma assinatura com Senha
	 */
	public boolean isEletronicoEPossuiPeloMenosUmaAssinaturaComSenha() {
		if(!isEletronico())
			return false;
		
		for (ExMovimentacao m : getMobilGeral().getExMovimentacaoSet()) {
			if ((m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Verifica se um documento já foi assinado pelo Subscritor.
	 */
	public boolean isAssinadoSubscritor() {
		for (ExMovimentacao assinatura : getTodasAsAssinaturas()) {
			if (assinatura.getSubscritor().equivale(getSubscritor()))
				return true;
		}
		return false;
	}

	/**
	 * Verifica se uma pessoa assinou este documento.
	 */
	public boolean jaAssinadoPor(DpPessoa subscritor) {
		for (ExMovimentacao assinatura : getTodasAsAssinaturas()) {
			if (assinatura.getSubscritor().equivale(subscritor))
				return true;
		}
		return false;
	}

	/**
	 * Verifica se um documento está cancelado, o que é verdadeiro quando todas
	 * as vias estão canceladas.
	 */
	@Override
	public boolean isCancelado() {
		// Documento só possível a via geral
		if (getExMobilSet().size() == 1)
			return false;

		for (ExMobil mob : getExMobilSet()) {
			if (mob.getExTipoMobil().getIdTipoMobil() == ExTipoMobil.TIPO_MOBIL_VIA
					|| mob.getExTipoMobil().getIdTipoMobil() == ExTipoMobil.TIPO_MOBIL_VOLUME)
				if (!mob.isCancelada())
					return false;
		}
		return true;
	}

	/**
	 * Verifica se um documento está arquivado, o que é verdadeiro quando todas
	 * as vias estão arquivadas.
	 */
	public boolean isArquivado() {
		// Documento só possível a via geral
		if (getExMobilSet().size() == 1)
			return false;

		for (ExMobil mob : getExMobilSet()) {
			if (mob.getExTipoMobil().getIdTipoMobil() == ExTipoMobil.TIPO_MOBIL_VIA
					|| mob.getExTipoMobil().getIdTipoMobil() == ExTipoMobil.TIPO_MOBIL_VOLUME)
				if (!mob.isArquivado())
					return false;
		}
		return true;
	}

	/**
	 * Retorna a data de assinatura do documento.
	 */
	public Date getDtAssinatura() {
		ExMovimentacao mov = getMovAssinatura();
		if (mov == null)
			return null;
		return mov.getDtIniMov();
	}

	/**
	 * Retorna a primeira movimentação de assinatura ou de registro de
	 * assinatura encontrada.
	 */
	private ExMovimentacao getMovAssinatura() {
		final Set<ExMovimentacao> movs = getMobilGeral().getExMovimentacaoSet();
		if (movs == null || movs.size() == 0)
			return null;

		for (final Object element : movs) {
			final ExMovimentacao movIterate = (ExMovimentacao) element;

			if ((movIterate.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO || movIterate
					.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA || movIterate
					.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_REGISTRO_ASSINATURA_DOCUMENTO)
					&& movIterate.getExMovimentacaoCanceladora() == null) {
				return movIterate;
			}
		}
		return null;
	}

	/**
	 * Verifica se um documento é do tipo eletrônico.
	 */
	public boolean isEletronico() {
		if (getFgEletronico() != null
				&& getFgEletronico().toUpperCase().equals("S"))
			return true;
		else
			return false;
	}
	
	/**
	 * Verifica se um documento é do tipo físico.
	 */
	public boolean isFisico() {
		return !isEletronico();
	}

	/**
	 * Verifica se um documento possui agendamento de publicação no DJE. ()
	 */
	public boolean isPublicacaoAgendada() {
		final Set<ExMovimentacao> movs = getMobilGeral().getExMovimentacaoSet();

		for (final ExMovimentacao mov : movs) {
			if ((mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO)
					&& mov.getExMovimentacaoCanceladora() == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifica se um documento possui <b>solicitação</b> de publicação no DJE.
	 */
	public boolean isPublicacaoSolicitada() {
		final Set<ExMovimentacao> movs = getMobilGeral().getExMovimentacaoSet();

		for (final ExMovimentacao mov : movs) {
			if ((mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_PUBLICACAO)
					&& mov.getExMovimentacaoCanceladora() == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifica se um documento possui solicitação de publicação no Boletim
	 * Interno.
	 */
	public boolean isPublicacaoBoletimSolicitada() {
		final Set<ExMovimentacao> movs = getMobilGeral().getExMovimentacaoSet();

		for (final ExMovimentacao mov : movs) {
			if ((mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO_BOLETIM)
					&& mov.getExMovimentacaoCanceladora() == null) {
				return true;
			}
		}
		return false;
	}

	public boolean isBoletimPublicado() {
		final Set<ExMovimentacao> movs = getMobilGeral().getExMovimentacaoSet();

		for (final ExMovimentacao mov : movs) {
			if (((mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_PUBLICACAO_BOLETIM || mov
					.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_NOTIFICACAO_PUBL_BI))
					&& mov.getExMovimentacaoCanceladora() == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifica se um documento já foi publicado no DJE.
	 */
	public boolean isDJEPublicado() {
		final Set<ExMovimentacao> movs = getMobilGeral().getExMovimentacaoSet();

		for (final ExMovimentacao mov : movs) {
			if ((mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DISPONIBILIZACAO)
					&& mov.getExMovimentacaoCanceladora() == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retorna se uma determinada via está cancelada.
	 * 
	 * @param iVia
	 * @return
	 */
	public boolean isViaCancelada(Integer iVia) {
		for (ExMobil mob : getExMobilSet()) {
			if (mob.isVia() && mob.getNumSequencia().equals(iVia))
				return mob.isCancelada();
		}
		return false;
	}

	/**
	 * Retorna se existe uma via de um determinado número.
	 * 
	 * @param iVia
	 */
	public boolean isViaInexistente(Integer iVia) {
		if (iVia == null || iVia.equals(0))
			return false;
		for (ExMobil mob : getExMobilSet()) {
			if (mob.isVia() && mob.getNumSequencia().equals(iVia))
				return false;
		}
		return true;
	}

	/**
	 * COMPLETAR Retorna a via do documento.
	 * 
	 * @param numVia
	 * 
	 * @return Objeto do tipo via de acordo com o número da via ou null caso não
	 *         exista via com o número informado.
	 */
	public ExVia via(final Short numVia) {
		Short i;

		if (getSetVias() == null)
			return null;
		try {
			for (final ExVia via : getSetVias()) {
				i = Short.parseShort(via.getCodVia());
				if (numVia.equals(i))
					return via;
			}
		} catch (Exception e) {
			return null;
		}

		return null;
	}

	/**
	 * Retorna a descrição da forma do documento. Caso o documento seja
	 * eltrônico, adiciona o texto "digital".
	 */
	public String getDescrFormaDoc() {
		if (getExFormaDocumento() == null)
			return null;
		return getExFormaDocumento().getDescrFormaDoc()
				+ (isEletronico() ? "" : " (f�sico)");
	}

	/**
	 * COMPLETAR
	 * 
	 * @return
	 */
	public Map<String, String> getForm() {
		Hashtable<String, String> m = new Hashtable<String, String>();
		final byte[] form = getConteudoBlob("doc.form");
		if (form != null) {
			final String as[] = new String(form).split("&");
			for (final String s : as) {
				final String param[] = s.split("=");
				try {
					if (param.length == 2)
						m.put(param[0],
								URLDecoder.decode(param[1], "iso-8859-1"));
				} catch (final UnsupportedEncodingException e) {
				}
			}
		}
		return m;
	}

	/**
	 * COMPLETAR
	 */
	public Map<String, String> getResumo() {
		LinkedHashMap<String, String> m = new LinkedHashMap<String, String>();
		final byte[] resumo = getConteudoBlob("doc.resumo");
		if (resumo != null) {
			final String as[] = new String(resumo).split("&");
			for (final String s : as) {
				final String param[] = s.split("=");
				try {
					if (param.length == 2)
						m.put(URLDecoder.decode(param[0], "iso-8859-1"),
								URLDecoder.decode(param[1], "iso-8859-1"));
				} catch (final UnsupportedEncodingException e) {
				}
			}
		}
		return m;
	}

	/**
	 * Retorna o conteúdo do corpo do documento que se encontra entre as tags
	 * <!-- INICIO CORPO --> e <!-- FIM CORPO -->
	 */
	public String getCorpoHtmlString() {
		try {
			String s = getConteudoBlobHtmlString();
			if (s.contains("<!-- INICIO CORPO -->")) {
				return Texto.extraiTudo(s, "<!-- INICIO CORPO -->",
						"<!-- FIM CORPO -->");
			} else {
				String inicioCorpo = "<!-- INICIO CORPO";

				String fimCorpo = "FIM CORPO -->";

				return Texto.extrai(s, inicioCorpo, fimCorpo);
			}

		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * Retorna texto da assinatura do documento que se encontra entre as tags
	 * <!-- INICIO ASSINATURA --> e <!-- FIM ASSINATURA -->.
	 */
	public String getAssinaturaHtmlString() {
		try {
			String s = getConteudoBlobHtmlString();
			return Texto.extrai(s, "<!-- INICIO ASSINATURA -->",
					"<!-- FIM ASSINATURA -->");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * Retorna número do documento que se encontra entre as tags <!-- INICIO
	 * NUMERO --> e <!-- FIM NUMERO -->.
	 */
	public String getNumeroHtmlString() {

		try {

			String s = getConteudoBlobHtmlString();

			String inicioNumero = "<!-- INICIO NUMERO -->";

			String fimNumero = "<!-- FIM NUMERO -->";

			if (!s.contains(inicioNumero)) {

				inicioNumero = "<!-- INICIO NUMERO";

				fimNumero = "FIM NUMERO -->";


	}
		return Texto.extrai(s, inicioNumero, fimNumero);

		} catch (UnsupportedEncodingException e) {

			return null;

		}

	}

	/**
	 * Retorna texto da abertura do documento que se encontra entre as tags <!--
	 * INICIO ABERTURA --> e <!-- FIM ABERTURA -->.
	 */
	public String getAberturaHtmlString() {
		try {
			String s = getConteudoBlobHtmlString();

			String inicioAbertura = "<!-- INICIO ABERTURA -->";
			String fimAbertura = "<!-- FIM ABERTURA -->";

			if (!s.contains(inicioAbertura)) {
				inicioAbertura = "<!-- INICIO ABERTURA";

				fimAbertura = "FIM ABERTURA -->";
			}

			return Texto.extrai(s, inicioAbertura, fimAbertura);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * Retorna texto do fecho do documento que se encontra entre as tags <!--
	 * INICIO FECHO --> e <!-- FIM FECHO -->.
	 */
	public String getFechoHtmlString() {
		try {
			String s = getConteudoBlobHtmlString();
			return Texto.extrai(s, "<!-- INICIO FECHO -->",
					"<!-- FIM FECHO -->");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * COMPLETAR
	 * 
	 * @return
	 */
	@IndexedEmbedded
	public Set<ExMovimentacao> getExMovimentacaoIndexacaoSet() {
		Set<ExMovimentacao> mSet = new HashSet<ExMovimentacao>();
		for (ExMobil mob : getExMobilSet()) {
			for (ExMovimentacao m : mob.getExMovimentacaoSet()) {
				if (m.getExMovimentacaoCanceladora() == null
						&& (m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANOTACAO
								|| m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO
								|| m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA
								|| m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO || m
								.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA)) {
					mSet.add(m);
				}
			}
		}
		return mSet;
	}

	/**
	 * Retorna conjunto com todas as movimentações do documento.
	 */
	@IndexedEmbedded
	public Set<ExMovimentacao> getExMovimentacaoSet() {
		Set<ExMovimentacao> mSet = new HashSet<ExMovimentacao>();
		for (ExMobil mob : getExMobilSet()) {
			for (ExMovimentacao m : mob.getExMovimentacaoSet()) {
				mSet.add(m);
			}
		}
		return mSet;
	}

	/**
	 * Vide getConteudoBlobHtmlString()
	 */
	@Override
	public String getHtml() {
		return getConteudoBlobHtmlString();
	}

	/**
	 * Vide getConteudoBlobHtmlString()
	 * 
	 * @throws Exception
	 */
	@Override
	public String getHtmlComReferencias() throws Exception {
		return getConteudoBlobHtmlStringComReferencias();
	}

	/**
	 * Vide getConteudoBlobHtmlString()
	 */
	@Override
	public byte[] getPdf() {
		return getConteudoBlobPdf();
	}

	@Override
	public boolean isPdf() {
		return true;
	}

	/**
	 * COMPLETAR
	 * 
	 * @param mob
	 * @return
	 */
	public List<ExArquivoNumerado> getArquivosNumerados(ExMobil mob) {
		List<ExArquivoNumerado> list = new ArrayList<ExArquivoNumerado>();
		return getArquivosNumerados(mob, list, 0);
	}

	/**
	 * COMPLETAR
	 * 
	 * @param mob
	 * @param list
	 * @param nivel
	 * @return
	 */
	public List<ExArquivoNumerado> getArquivosNumerados(ExMobil mob,
			List<ExArquivoNumerado> list, int nivel) {

		// Incluir o documento principal
		ExArquivoNumerado anDoc = new ExArquivoNumerado();
		anDoc.setArquivo(this);
		anDoc.setMobil(mob);
		anDoc.setNivel(nivel);
		list.add(anDoc);

		getAnexosNumerados(mob, list, nivel + 1);

		// Numerar as paginas
		if (isNumeracaoUnicaAutomatica()) {
			int i = 0;

			if (mob.isVolume() && mob.getNumSequencia() > 1) {
				List<ExArquivoNumerado> listVolumeAnterior = getArquivosNumerados(mob
						.doc().getVolume(mob.getNumSequencia() - 1));
				i = listVolumeAnterior.get(listVolumeAnterior.size() - 1)
						.getPaginaFinal();
			}

			removerDesentranhamentosQueNaoFazemParteDoDossie(list);

			for (ExArquivoNumerado an : list) {
				i++;
				an.setPaginaInicial(i);
				i += an.getNumeroDePaginasParaInsercaoEmDossie() - 1;
				an.setPaginaFinal(i);
			}
		}

		return list;
	}

	public void removerDesentranhamentosQueNaoFazemParteDoDossie(
			List<ExArquivoNumerado> list) {
		// Verifica se tem movimentação de desentranhamento que não pertence ao
		// documento principal
		if (list != null && list.get(0) != null) {
			ExArquivoNumerado arquivoPrincipal = list.get(0);

			if (arquivoPrincipal.getArquivo() instanceof ExDocumento) {

				List<ExArquivoNumerado> arquivosParaRemover = new ArrayList<ExArquivoNumerado>();

				for (ExArquivoNumerado an : list) {
					if (an.getArquivo() instanceof ExMovimentacao) {
						ExMovimentacao mov = (ExMovimentacao) an.getArquivo();

						if (mov.getExTipoMovimentacao().getId() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA) {

							if (mov.getExMobilRef() != null
									&& !mov.getExMobilRef()
											.getId()
											.equals(arquivoPrincipal.getMobil()
													.getId())) {

								arquivosParaRemover.add(an);
							}
						}
					}
				}

				for (ExArquivoNumerado arquivoParaRemover : arquivosParaRemover) {
					list.remove(arquivoParaRemover);
				}
			}
		}
	}

	/**
	 * COMPLETAR
	 * 
	 * @return
	 */
	public boolean isNumeracaoUnicaAutomatica() {
		// return isEletronico() && getExFormaDocumento().isNumeracaoUnica();
		return (getExFormaDocumento().isNumeracaoUnica())
				&& (getExTipoDocumento().getId() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO || getExTipoDocumento()
						.getId() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_ANTIGO)
				&& isEletronico();
		// return true;
	}

	/**
	 * COMPLETAR A coleção que ordena as movimentações deve respeitar a
	 * cronologia, exceto no caso das movimentações de cancelamento de juntada,
	 * anexação e despacho, que, quando prossuirem certidôes de exclusão, estas
	 * deverão ser inseridas no lugar do documento removido.
	 * 
	 * @param mob
	 * @param list
	 * @param nivel
	 */
	private void getAnexosNumerados(ExMobil mob, List<ExArquivoNumerado> list,
			int nivel) {

		SortedSet<ExMovimentacao> set = new TreeSet<ExMovimentacao>(
				new AnexoNumeradoComparator());

		incluirArquivos(getMobilGeral(), set);
		incluirArquivos(mob, set);

		// Incluir recursivamente
		for (ExMovimentacao m : set) {
			ExArquivoNumerado an = new ExArquivoNumerado();
			an.setNivel(nivel);
			if (m.getExTipoMovimentacao().getId() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA) {
				an.setArquivo(m.getExDocumento());
				an.setMobil(m.getExMobil());
				an.setData(m.getData());
				list.add(an);
				m.getExDocumento().getAnexosNumerados(m.getExMobil(), list,
						nivel + 1);
			} else {
				an.setArquivo(m);
				an.setMobil(m.getExMobil());
				list.add(an);
			}
		}
	}

	/**
	 * COMPLETAR
	 * 
	 * @param mob
	 * @param set
	 */
	private void incluirArquivos(ExMobil mob, SortedSet<ExMovimentacao> set) {
		// Incluir os documentos anexos
		if (mob.getExMovimentacaoSet() != null) {
			for (ExMovimentacao m : mob.getExMovimentacaoSet()) {
				if (!m.isCancelada() && m.isPdf()) {
					set.add(m);
				}
			}
		}
		// Incluir os documentos juntados
		if (mob.getExMovimentacaoReferenciaSet() != null)
			for (ExMovimentacao m : mob.getExMovimentacaoReferenciaSet()) {
				if (!m.isCancelada()) {
					if (m.getExTipoMovimentacao().getId() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA) {
						set.add(m);
					} else if (m.getExTipoMovimentacao().getId() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA) {
						set.remove(m.getExMovimentacaoRef());
						if (m.isPdf())
							set.add(m);
					}
				}
			}
	}

	/**
	 * Vide getDtRegDoc()
	 */
	@Override
	public Date getData() {
		return getDtRegDoc();
	}

	/**
	 * Retorna um código alternativo para o documento, baseado num cálculo feito
	 * sobre o id e a descrição.
	 */
	public String getSiglaAssinatura() {
		return getIdDoc() + "-" + Math.abs(getDescrCurta().hashCode() % 10000);
	}

	/**
	 * Retorna uma lista de movimentações do tipo assinatura digital do
	 * documento.
	 */
	@Override
	public Set<ExMovimentacao> getAssinaturasDigitais() {
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();

		if (getMobilGeral() == null)
			return null;

		if (getMobilGeral().getExMovimentacaoSet() == null)
			return null;

		for (ExMovimentacao m : getMobilGeral().getExMovimentacaoSet()) {
			if ((m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO 
					|| m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA)
					&& m.getExMovimentacaoCanceladora() == null) {
				set.add(m);
			}
		}
		return set;
	}
	
	/**
	 * Retorna uma lista de movimentações do tipo assinatura com senha do
	 * documento.
	 */
	public Set<ExMovimentacao> getApenasAssinaturasComToken() {
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();

		if (getMobilGeral() == null)
			return null;

		if (getMobilGeral().getExMovimentacaoSet() == null)
			return null;

		for (ExMovimentacao m : getMobilGeral().getExMovimentacaoSet()) {
			if ((m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO)
					&& m.getExMovimentacaoCanceladora() == null) {
				set.add(m);
			}
		}
		return set;
	}
	
	/**
	 * Retorna uma lista de movimentações do tipo autenticação de
	 * documento.
	 */
	public Set<ExMovimentacao> getApenasAutenticacoes() {
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();

		if (getMobilGeral() == null)
			return null;

		if (getMobilGeral().getExMovimentacaoSet() == null)
			return null;

		for (ExMovimentacao m : getMobilGeral().getExMovimentacaoSet()) {
			if ((m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO)
					&& m.getExMovimentacaoCanceladora() == null) {
				set.add(m);
			}
		}
		return set;
	}
	
	/**
	 * Retorna uma lista de movimentações do tipo assinatura com senha do
	 * documento.
	 */
	public Set<ExMovimentacao> getApenasAssinaturasComSenha() {
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();

		if (getMobilGeral() == null)
			return null;

		if (getMobilGeral().getExMovimentacaoSet() == null)
			return null;

		for (ExMovimentacao m : getMobilGeral().getExMovimentacaoSet()) {
			if ((m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA)
					&& m.getExMovimentacaoCanceladora() == null) {
				set.add(m);
			}
		}
		return set;
	}
	
	/**
	 * Retorna se um documento possui assinaturas com senha.
	 */
	public boolean temAssinaturasComSenha() {
		if(getApenasAssinaturasComSenha() != null && getApenasAssinaturasComSenha().size() > 0)
			return true;
		
		return false;
	}
	
	/**
	 * Um documento está autenticado quando ele possui pelo menos uma assinatura com senha.
	 */
	public boolean isAutenticado() {
		for (ExMovimentacao m : getMobilGeral().getExMovimentacaoSet()) {
			if (m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO ||
					m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO) {
				
				return true;
			}
		}
	
		return false;
	}

	public String getAssinantesCompleto() {
		String retorno =  "";
		String conferentes = Documento
				.getAssinantesString(getApenasAutenticacoes());
		String assinantesToken = Documento
				.getAssinantesString(getApenasAssinaturasComToken());
		String assinantesSenha = Documento
				.getAssinantesString(getApenasAssinaturasComSenha());
		
		if (assinantesToken.length() > 0)
			retorno = "Assinado digitalmente por " + assinantesToken + ".\n";
		
		if (assinantesSenha.length() > 0)
			retorno = retorno + "Assinado com senha por " + assinantesSenha + ".\n";
		
		if (conferentes.length() > 0)
			retorno += conferentes.length() > 0 ? "Autenticado digitalmente por "
				+ conferentes + ".\n" : "";
		
		return retorno;
	}

	/**
	 * verifica se um documento ainda está em rascunho, ou seja, se não está
	 * finalizado ou se está finalizado mas é eletrônico.
	 */
	@Override
	public boolean isRascunho() {
		return !isFinalizado() || (isEletronico() && !isAssinado());
	}

	/**
	 * verifica se um documento está sem efeito.
	 */
	@Override
	public boolean isSemEfeito() {
		final Set<ExMovimentacao> movs = getMobilGeral().getExMovimentacaoSet();

		if (movs != null) {
			for (final ExMovimentacao mov : movs) {
				if ((mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO)
						&& mov.getExMovimentacaoCanceladora() == null) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Vide getLotaSubscritor()
	 */
	@Override
	public DpLotacao getLotacao() {
		return getLotaSubscritor();
	}

	/**
	 * Retorna a lotação titular ou do subscritor do documento, caso aquela não
	 * exista.
	 */
	public DpLotacao getLotaSubscritorEfetiva() {
		if (getLotaTitular() != null)
			return getLotaTitular();
		return getLotaSubscritor();
	}

	/**
	 * Retorna o Mobil Geral de um documento.
	 */
	public ExMobil getMobilGeral() {
		if (getExMobilSet() == null)
			return null;
		for (ExMobil mob : getExMobilSet()) {
			if (mob.getExTipoMobil().getIdTipoMobil() == ExTipoMobil.TIPO_MOBIL_GERAL)
				return mob;
		}
		return null;
	}

	/**
	 * Retorna uma lista de documentos filhos [de cada móbil] do documento
	 * atual.
	 */
	public java.util.Set<ExDocumento> getExDocumentoFilhoSet() {
		Set<ExDocumento> set = new TreeSet<ExDocumento>(
				new DocumentoFilhoComparator());
		for (ExMobil m : getExMobilSet())
			if (m.getExDocumentoFilhoSet() != null)
				set.addAll(m.getExDocumentoFilhoSet());
		return set;
	}

	/**
	 * Vide getSigla()
	 */
	@Override
	public String toString() {
		return getSigla();
	}

	/**
	 * Retorna o número do último volume (funciona apenas para processo
	 * administrativo).
	 */
	public int getNumUltimoVolume() {
		int maxNumVolume = 0;
		for (final ExMobil mob : getExMobilSet()) {
			if (mob.isVolume() && mob.getNumSequencia() > maxNumVolume) {
				maxNumVolume = mob.getNumSequencia();
			}
		}
		return maxNumVolume;
	}

	/**
	 * Retorna o móbil-volume de um processo administrativo de acordo com o seu
	 * número.
	 */
	public ExMobil getVolume(int i) {
		for (final ExMobil mob : getExMobilSet()) {
			if (mob.isVolume() && mob.getNumSequencia() == i) {
				return mob;
			}
		}
		return null;
	}

	/**
	 * Retorna todos os volumes de um processo administrativo.
	 */
	public Set<ExMobil> getVolumes() {
		if (!isProcesso())
			return new LinkedHashSet<ExMobil>();
		Set<ExMobil> volumes = new LinkedHashSet<ExMobil>();
		for (final ExMobil mobil : getExMobilSet())
			if (mobil.isVolume())
				volumes.add(mobil);
		return volumes;
	}

	/**
	 * Retorna o móbil-via de um expediente de acordo com o seu número.
	 */
	public ExMobil getVia(int i) {
		for (final ExMobil mob : getExMobilSet()) {
			if (mob.isVia() && mob.getNumSequencia() == i) {
				return mob;
			}
		}
		return null;
	}

	/**
	 * Verifica se um documento é do tipo Expediente.
	 */
	public boolean isExpediente() {
		try {
			if (getExModelo() == null)
				return false;
			if (getExModelo().getExFormaDocumento() == null)
				return true;
			return getExModelo().getExFormaDocumento().getExTipoFormaDoc()
					.isExpediente();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Verifica se um documento é do tipo Processo.
	 */
	public boolean isProcesso() {
		try {
			if (getExModelo() == null)
				return false;
			if (getExModelo().getExFormaDocumento() == null)
				return false;
			return getExModelo().getExFormaDocumento().getExTipoFormaDoc()
					.isProcesso();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Retorna o último móbil-volume (funciona apenas para processo
	 * administrativo).
	 */
	public ExMobil getUltimoVolume() {
		return getVolume(getNumUltimoVolume());
	}

	/**
	 * Retorna o último móbil-volume (funciona apenas para processo
	 * administrativo).
	 */
	public ExMobil getUltimaVia() {
		return getVia(getNumUltimaVia());
	}

	/**
	 * Retorna a primeira via do documento.
	 */
	public ExMobil getPrimeiraVia() {
		return getVia(getNumPrimeiraVia());
	}

	/**
	 * Retorna o nome completo de um documento, composto pela descrição da
	 * origem mais o código do documento.
	 */
	public String getNomeCompleto() {
		return "Documento " + getExTipoDocumento().getDescricao() + ":"
				+ getCodigoString();
	}

	/**
	 * Verifica se possui alguma movimentação com arquivo PDF.
	 */
	public boolean hasPDF() {
		for (ExMovimentacao m : getExMovimentacaoSet()) {
			if (!m.isCancelada() && m.getNumPaginas() != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retorna uma lista de Mobils do documento, ordenados de forma decrescente
	 * pelo número de sequência do móbil.
	 */
	public java.util.SortedSet<ExMobil> getExMobilSetInvertido() {
		final TreeSet<ExMobil> mobilInvertido = new TreeSet<ExMobil>(
				new TipoMobilComparatorInverso());
		mobilInvertido.addAll(getExMobilSet());

		return mobilInvertido;
	}

	/**
	 * Verifica se um documento é de origem externa.
	 */
	public boolean isExterno() {
		if (getExTipoDocumento() == null)
			return false;
		return (getExTipoDocumento().getIdTpDoc() == 3);
	}

	/**
	 * Retorna uma lista com o subscritor e todos os cossignatários.
	 */
	public List<DpPessoa> getSubscritorECosignatarios() {
		List<DpPessoa> subscritores = new ArrayList<DpPessoa>();

		if (getSubscritor() != null)
			subscritores.add(getSubscritor());

		subscritores.addAll(getCosignatarios());

		return subscritores;
	}

	/**
	 * Retorna uma lista com o todos os cossignatários.
	 */
	public List<DpPessoa> getCosignatarios() {

		List<DpPessoa> cosignatarios = new ArrayList<DpPessoa>();

		if (getMobilGeral() != null
				&& getMobilGeral().getExMovimentacaoSet() != null) {
			for (ExMovimentacao m : getMobilGeral().getExMovimentacaoSet()) {
				if (m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO
						&& m.getExMovimentacaoCanceladora() == null) {
					cosignatarios.add(m.getSubscritor());
				}
			}
		}

		return cosignatarios;
	}

	/**
	 * Retorna uma lista com o todos os cossignatários.
	 */
	public List<ExMovimentacao> getMovsCosignatario() {

		List<ExMovimentacao> cosignatarios = new ArrayList<ExMovimentacao>();

		if (getMobilGeral() != null
				&& getMobilGeral().getExMovimentacaoSet() != null) {
			for (ExMovimentacao m : getMobilGeral().getExMovimentacaoSet()) {
				if (m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO
						&& m.getExMovimentacaoCanceladora() == null) {
					cosignatarios.add(m);
				}
			}
		}

		return cosignatarios;
	}

	/**
	 * Retorna uma lista com os subscritores de todos os despachos não
	 * cancelados do documento.
	 */
	public List<DpPessoa> getSubscritorDespacho() {
		List<DpPessoa> subscritoresDesp = new ArrayList<DpPessoa>();

		for (ExMobil mob : getExMobilSet()) {
			for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
				if ((mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO
						|| mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO
						|| mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO_TRANSFERENCIA || mov
						.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA)
						&& !mov.isCancelada())
					subscritoresDesp.add(mov.getSubscritor());
			}
		}

		return subscritoresDesp;
	}

	/**
	 * Retorna todas as movimentações de assinatura digital e de registro de
	 * assinatura.
	 */
	public Set<ExMovimentacao> getTodasAsAssinaturas() {
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();

		if (getMobilGeral() == null)
			return null;

		if (getMobilGeral().getExMovimentacaoSet() == null)
			return null;

		for (ExMovimentacao m : getMobilGeral().getExMovimentacaoSet()) {
			if ((m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO || m
					.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA|| m
					.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_REGISTRO_ASSINATURA_DOCUMENTO)
					&& m.getExMovimentacaoCanceladora() == null) {
				set.add(m);
			}
		}
		return set;
	}
	
	/**
	 * Retorna se um documento possui alguma assinatura.
	 */
	public boolean possuiAlgumaAssinatura() {
		if(getTodasAsAssinaturas() != null && getTodasAsAssinaturas().size() > 0)
			return true;
		
		return false;
	}

	/**
	 * Verifica se um documento foi assinado pelo subscritor e por todos os
	 * cosignatários
	 */
	private boolean isAssinadoEletronicoPorTodosOsSignatarios() {
		// Interno antigo e externo são considerados como assinados
		if (getExTipoDocumento().getIdTpDoc() != 1L) {
			return getExMobilSet() != null && getExMobilSet().size() > 1;
		}

		ExMovimentacao mov = getMovAssinatura();
		if (mov == null)
			return false;

		List<DpPessoa> todosQueJaAssinaram = new ArrayList<DpPessoa>();

		for (ExMovimentacao assinatura : getTodasAsAssinaturas()) {
			if (assinatura.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO)
				todosQueJaAssinaram.add(assinatura.getSubscritor());
		}
		
		// compatibiliza com versoes anteriores do SIGA que permitia transferir
		// documento antes que todos os cossiganatarios assinassem o documento
		if (todosQueJaAssinaram.size() > 0 && jaTransferido()){
			return true;
		}

		for (DpPessoa signatario : getSubscritorECosignatarios()) {
			boolean encontrou = false;

			for (DpPessoa jaAssinou : todosQueJaAssinaram) {
				if (jaAssinou.equivale(signatario)) {
					encontrou = true;
					break;
				}
			}

			if (!encontrou)
				return false;
		}

		return true;
	}

	/**
	 * Verifica se um documento foi assinado pelo subscritor e por todos os
	 * cosignatários com Login e Senha ou por token
	 */
	private boolean isAssinadoEletronicoOuPorSenhaPorTodosOsSignatarios() {
		// Interno antigo e externo são considerados como assinados
		if (getExTipoDocumento().getIdTpDoc() != 1L) {
			return getExMobilSet() != null && getExMobilSet().size() > 1;
		}

		ExMovimentacao mov = getMovAssinatura();
		if (mov == null)
			return false;

		List<DpPessoa> todosQueJaAssinaram = new ArrayList<DpPessoa>();

		for (ExMovimentacao assinatura : getTodasAsAssinaturas()) {
			if (assinatura.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO
					|| assinatura.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA)
				todosQueJaAssinaram.add(assinatura.getSubscritor());
		}

		for (DpPessoa signatario : getSubscritorECosignatarios()) {
			boolean encontrou = false;

			for (DpPessoa jaAssinou : todosQueJaAssinaram) {
				if (jaAssinou.equivale(signatario)) {
					encontrou = true;
					break;
				}
			}

			if (!encontrou)
				return false;
		}

		return true;
	}
	
	/**
	 * Verifica se todos os móbiles do documento estão eliminados.
	 */
	public boolean isArquivadoPermanente() {

		if (isProcesso())
			return getMobilGeral().isArquivadoPermanente();

		boolean arqPermanente = false;

		for (ExMobil m : getExMobilSet())
			if (!m.isGeral())
				if (m.isArquivadoPermanente())
					arqPermanente = true;
				else {
					return false;
				}

		return arqPermanente;
	}

	/**
	 * Verifica se todos os móbiles do documento estão eliminados.
	 */
	public boolean isEliminado() {

		// Edson: este método, ainda mais nos lugares em que ele é chamado,
		// estava prejudicando a performance. Ver um jeito melhor de fazer
		return false;
		/*
		 * if (isProcesso()) return getMobilGeral().isEliminado();
		 * 
		 * boolean eliminado = false;
		 * 
		 * for (ExMobil m : getExMobilSet()) if (!m.isGeral()) if
		 * (m.isEliminado()) eliminado = true; else { return false; }
		 * 
		 * return eliminado;
		 */
	}

	/**
	 * Verifica se um documento já foi transferido alguma vez
	 */
	public boolean jaTransferido() {
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (!mov.isCancelada()
					&& (mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO_TRANSFERENCIA
							|| mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA
							|| mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA
							|| mov.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA || mov
							.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA))
				return true;
		}

		return false;
	}

	/**
	 * Verifica se uma pessoa é subscritor ou cosignatário de um documento
	 */
	public boolean isSubscritorOuCosignatario(DpPessoa subscritor) {
		for (DpPessoa signatario : getSubscritorECosignatarios()) {
			if (signatario.equivale(subscritor))
				return true;
		}

		return false;
	}

	public void setConteudoBlob(final String nome, final byte[] conteudo) {
		final Compactador zip = new Compactador();
		final byte[] arqZip = getConteudoBlobDoc2();
		byte[] conteudoZip = null;
		if (arqZip == null || (zip.listarStream(arqZip) == null)) {
			if (conteudo != null) {
				conteudoZip = zip.compactarStream(nome, conteudo);
			} else {
				conteudoZip = null;
			}
		} else {
			if (conteudo != null) {
				conteudoZip = zip.adicionarStream(nome, conteudo, arqZip);
			} else {
				conteudoZip = zip.removerStream(nome, arqZip);
			}
		}
		setConteudoBlobDoc2(conteudoZip);
	}

	public void setConteudoBlobDoc2(byte[] blob) {
		if (blob != null)
			setConteudoBlobDoc(HibernateUtil.getSessao().getLobHelper().createBlob(blob));
		cacheConteudoBlobDoc = blob;
	}

	public void setConteudoBlobForm(final byte[] conteudo) {
		setConteudoBlob("doc.form", conteudo);
	}

	public void setConteudoBlobResumo(final byte[] conteudo) {
		setConteudoBlob("doc.resumo", conteudo);
	}

	public void setConteudoBlobHtml(final byte[] conteudo) {
		setConteudoBlob("doc.htm", conteudo);
	}

	public void setConteudoBlobHtmlString(final String s) throws Exception {
		final String sHtml = (new ProcessadorHtml()).canonicalizarHtml(s,
				false, true, false, false, false);
		setConteudoBlob("doc.htm", sHtml.getBytes("ISO-8859-1"));
	}

	public void setConteudoBlobPdf(final byte[] conteudo) throws Exception {

		// Atenção, não retirar esse teste nunca, pois ele é quem garante que o
		// pdf não será refeito.
		if (isAssinado() || isAssinadoDigitalmente())
			throw new AplicacaoException(
					"O conte�do n�o pode ser alterado pois o documento j� est� assinado");
		setConteudoBlob("doc.pdf", conteudo);
	}

	public void setDescrDocumentoAI(String descrDocumentoAI) {
		this.descrDocumentoAI = descrDocumentoAI;
	}

	public void setEletronico(boolean eletronico) {
		if (eletronico)
			setFgEletronico("S");
		else
			setFgEletronico("N");
	}

	/**
	 * 
	 * @return o id do ExNivelAcesso quando o ExNivelAcesso não for nulo.
	 */
	public Long getIdExNivelAcesso() {
		log.info("Obtendo IdExNivelAcesso...");
		Long idExNivelAcesso = null;
		String nivelAcesso = this.getNivelAcesso();
		if (nivelAcesso != null) {
			idExNivelAcesso = this.getExNivelAcesso().getIdNivelAcesso();
		}
		return idExNivelAcesso;
	}

	public List<DpResponsavel> getResponsaveisPorPapel(
			String papelComoNomeDeVariavel) {
		List<ExPapel> papeis = ExDao.getInstance().listarExPapeis();
		for (ExPapel papel : papeis) {
			if (papel.getComoNomeDeVariavel().equals(papelComoNomeDeVariavel))
				return getResponsaveisPorPapel(papel);
		}
		return null;
	}

	public List<Object> getListaDeAcessos() {
		if (getDnmAcesso() == null)
			return null;
		if (getExNivelAcesso().getIdNivelAcesso().equals(
				ExNivelAcesso.NIVEL_ACESSO_PUBLICO)
				&& ExAcesso.ACESSO_PUBLICO.equals(getDnmAcesso()))
			return null;
		ExDao dao = ExDao.getInstance();
		List<Object> l = new ArrayList<Object>();
		String a[] = getDnmAcesso().split(",");

		for (String s : a) {
			if (s.equals(ExAcesso.ACESSO_PUBLICO))
				l.add(s);
			else if (s.startsWith("O"))
				l.add(dao.consultar(Long.parseLong(s.substring(1)),
						CpOrgaoUsuario.class, false));
			else if (s.startsWith("L"))
				l.add(dao.consultar(Long.parseLong(s.substring(1)),
						DpLotacao.class, false));
			else if (s.startsWith("P"))
				l.add(dao.consultar(Long.parseLong(s.substring(1)),
						DpPessoa.class, false));
		}

		return l;
	}

	public String getListaDeAcessosString() {
		String s = "";
		List<Object> l = getListaDeAcessos();

		for (Object o : l) {
			if (s.length() > 0)
				s += ", ";
			if (ExAcesso.ACESSO_PUBLICO.equals(o))
				s += "P�blico";
			else if (o instanceof CpOrgaoUsuario)
				s += ((CpOrgaoUsuario) o).getSigla();
			else if (o instanceof DpLotacao)
				s += ((DpLotacao) o).getSiglaCompleta();
			else if (o instanceof DpPessoa)
				s += ((DpPessoa) o).getSiglaCompleta();
			else
				s += o.toString();
		}

		return s;
	}

	public List<DpResponsavel> getResponsaveisPorPapel(ExPapel papel) {
		List<DpResponsavel> lista = new ArrayList<DpResponsavel>();
		List<ExMovimentacao> movs = getMobilGeral().getMovimentacoesPorTipo(
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL);
		for (ExMovimentacao mov : movs) {
			if (mov.isCancelada()
					|| !papel.getIdPapel()
							.equals(mov.getExPapel().getIdPapel()))
				continue;
			if (mov.getSubscritor() != null)
				lista.add(mov.getSubscritor());
			else if (mov.getLotaSubscritor() != null)
				lista.add(mov.getLotaSubscritor());
		}
		return lista.size() == 0 ? null : lista;
	}

	@Override
	public boolean isInternoProduzido() {
		return true;
	}

	public boolean temPerfil(DpPessoa titular, DpLotacao lotaTitular, long papel) {
		for (ExMovimentacao mov : getMobilGeral().getExMovimentacaoSet()) {
			if (!mov.isCancelada()
					&& mov.getExTipoMovimentacao()
							.getId()
							.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL)
					&& mov.getExPapel().getIdPapel().equals(papel)
					&& ((mov.getSubscritor() != null && mov.getSubscritor()
							.equivale(titular)) || (mov.getSubscritor() == null && mov
							.getLotaSubscritor().equivale(lotaTitular))))
				return true;
		}

		return false;
	}

	public Map<ExPapel, List<Object>> getPerfis() {
		Map<ExPapel, List<Object>> mapa = new HashMap<ExPapel, List<Object>>();
		for (ExMovimentacao mov : getMobilGeral().getExMovimentacaoSet())
			if (!mov.isCancelada()
					&& mov.getExTipoMovimentacao()
							.getId()
							.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL)) {
				if (mapa.get(mov.getExPapel()) == null)
					mapa.put(mov.getExPapel(), new ArrayList<Object>());
				mapa.get(mov.getExPapel()).add(
						mov.getSubscritor() != null ? mov.getSubscritor() : mov
								.getLotaSubscritor());
			}
		return mapa;
	}

	@Override
	public boolean isCodigoParaAssinaturaExterna(String num) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSiglaAssinaturaExterna() {
		// TODO Auto-generated method stub
		return "";
	}

	public List<ExDocumento> getDocumentosPublicadosNoBoletim() {
		return ExDao.getInstance().consultarPorBoletimParaPublicar(this);
	}

	public ExDocumento getPublicadoNoBoletim() {
		if (isBoletimPublicado()) {
			ExBoletimDoc boletimDoc = ExDao.getInstance()
					.consultarBoletimPorDocumento(this);

			if (boletimDoc != null)
				return boletimDoc.getBoletim();
		}

		return null;
	}
	
	/**
	 * Retorna o conte�do entre dois textos.
	 */
	public String getConteudoEntreTextos(String textoInicial, String textoFinal) {
		try {
			if(textoInicial == null || textoInicial.isEmpty() || textoFinal == null || textoFinal.isEmpty())
				return "";

			String s = getConteudoBlobHtmlString();
			
			if(!s.contains(textoInicial) || !s.contains(textoFinal))
				return "";
			
			return Texto.extrai(s, textoInicial, textoFinal).trim();
		} catch (Exception e) {
			return "";
		} 
	}
	
}