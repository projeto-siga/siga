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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
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
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.jboss.logging.Logger;

import com.google.common.collect.Lists;

import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.DateUtils;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.cp.CpArquivo;
import br.gov.jfrj.siga.cp.arquivo.ArmazenamentoTemporalidadeEnum;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.cp.util.XjusUtils;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpResponsavel;
import br.gov.jfrj.siga.ex.BIE.ExBoletimDoc;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeAcessarDocumento;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeVinculo;
import br.gov.jfrj.siga.ex.util.AnexoNumeradoComparator;
import br.gov.jfrj.siga.ex.util.Compactador;
import br.gov.jfrj.siga.ex.util.DocumentoFilhoComparator;
import br.gov.jfrj.siga.ex.util.DocumentoUtil;
import br.gov.jfrj.siga.ex.util.ProcessadorHtml;
import br.gov.jfrj.siga.ex.util.ProcessadorReferencias;
import br.gov.jfrj.siga.ex.util.TipoMobilComparatorInverso;
import br.gov.jfrj.siga.ex.vo.AssinanteVO;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.CarimboDeTempo;

/**
 * A class that represents a row in the 'EX_DOCUMENTO' table. This class may be
 * customized as it is never re-generated after being created.
 */
@SuppressWarnings("serial")
@Entity
@BatchSize(size = 500)
@Table(name = "siga.ex_documento")
@DynamicUpdate
public class ExDocumento extends AbstractExDocumento implements Serializable,
		CarimboDeTempo {

	private static final Logger log = Logger.getLogger(ExDocumento.class);

	@Transient
	private List<ExMovimentacao> listaMovimentacaoPorRestricaoAcesso;	
	
	@Transient
	private Boolean podeReordenar;
	
	@Transient
	private boolean podeExibirReordenacao;
	
	@Transient
	private Long idDocPrincipal;
	
	@Transient
	private String numeroSequenciaGenerica;

	@Transient
	private String codigoUnico;
	
    @Transient
    private String digitoVerificadorCodigoUnico;
    
    @Transient
    private boolean atrasandoAtualizacaoDoArquivo = false;
    
    @Transient
    private Map<String, byte[]> mapAtualizacaoDoArquivo = new HashMap<>();
    
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
	 * Retorna o nível de acesso (não a descrição) do documento definido no
	 * momento da criação do documento, desconsiderando as redefinições de
	 * nível.
	 */
	public ExNivelAcesso getExNivelAcesso() {
		return super.getExNivelAcesso();
	}

	/**
	 * Retorna o nível de acesso (não a descrição) atual do documento.
	 * COM Atualização da DnmNivelAcesso
	 */
	public ExNivelAcesso getExNivelAcessoAtual() {
		ExNivelAcesso nivel = getDnmExNivelAcesso();
		if (nivel == null)
			return Ex.getInstance().getBL().atualizarDnmNivelAcesso(this);
		return nivel;
	}
	
	
	/**
	 * Retorna o nível de acesso (não a descrição) atual do documento.
	 * SEM Atualização da DnmNivelAcesso
	 */
	public ExNivelAcesso getExNivelAcessoAnterior() {
		ExNivelAcesso nivel = null;
		if (this.getMobilGeral() != null && this.getMobilGeral().getPenultimaMovimentacaoAlteracaoNivelAcessoNaoCancelada() != null)
			nivel = this.getMobilGeral().getPenultimaMovimentacaoAlteracaoNivelAcessoNaoCancelada().getExNivelAcesso();
		if (nivel == null)
			nivel = this.getExNivelAcesso();
		return nivel;
	}
	
	

	/**
	 * Retorna o nível de acesso (não a descrição) atual do documento.
	 */
	public Date getDataDeRedefinicaoDoNivelDeAcesso() {
		ExMobil m = this.getMobilGeral();
		Date dt = null;
		for (ExMovimentacao mov : m.getExMovimentacaoSet()) {
			if (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.REDEFINICAO_NIVEL_ACESSO) {
				dt = mov.getDtMov();
			}
		}
		return dt;
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
					if (getAnoEmissao() >= Prop.getInt("codigo.acronimo.ano.inicial")) {
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
					if (l_anoEmissao >= Prop.getInt("codigo.acronimo.ano.inicial")) {
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
			return new String(getConteudoBlobDoc());
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
       if (atrasandoAtualizacaoDoArquivo && mapAtualizacaoDoArquivo.containsKey(nome)) {
            return mapAtualizacaoDoArquivo.get(nome);
        }

		final byte[] conteudoZip = getConteudoBlobDoc();
		byte[] conteudo = null;
		final Compactador zip = new Compactador();
		if (conteudoZip != null) {
			conteudo = zip.descompactarStream(conteudoZip, nome);
		}
		return conteudo;
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
	public byte[] getConteudoBlobHtml() {
		return getConteudoBlob("doc.htm");
	}

	/**
	 * Retorna, <b>em formato Base64 (String)</b>, o conteúdo do arquivo
	 * <b>html</b> contido no zip gravado no blob do documento.
	 */
	public String getConteudoBlobHtmlB64() {
		return Base64.getEncoder().encodeToString(getConteudoBlobHtml());
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
	public String getConteudoBlobHtmlStringComReferencias() {
		String sHtml = getConteudoBlobHtmlString();
		ProcessadorReferencias pr = new ProcessadorReferencias();
		pr.ignorar(getSigla());
		sHtml = pr.marcarReferencias(sHtml);

		// Verifica se todos os subscritores assinaram o documento
		try {
			for (DpPessoa subscritor : getSubscritorECosignatarios()) {
				if (isEletronico() && isFinalizado()) {
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
						
						if (!isAssinadoPelaPessoaComTokenOuSenha(subscritor)) {
							sb.append("<span style=\"color:#CD3700;\">");
							sb.append(blocoSubscritor);
							sb.append("</span>");
						} else {
							if (Prop.getBool("assinatura.estampar")) {
								sb.append("<span>- assinado eletronicamente -<br/>");
							} else {
								sb.append("<span>");
							}
							sb.append(blocoSubscritor);
							sb.append("</span>");	
						}
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
		return Base64.getEncoder().encodeToString(getConteudoBlobPdf());
	}

	/**
	 * Retorna um descrição do documento com no máximo 40 caracteres.
	 */
	public java.lang.String getDescrCurta() {
		if (getDescrDocumento() == null)
			return "[sem descrição]";
		if (getDescrDocumento().length() > 40)
			return getDescrDocumento().substring(0, 39) + "...";
		else
			return getDescrDocumento();
	}

	/**
	 * Retorna um descrição do documento com um máximo de caracteres.
	 */
	public java.lang.String getDescrCurta(long qtdCaracteres) {
		if (getDescrDocumento() == null)
			return "[sem descrição]";
		if (getDescrDocumento().length() > qtdCaracteres)
			return getDescrDocumento().substring(0, (int) (qtdCaracteres - 1)) + "...";
		else
			return getDescrDocumento();
	}

	/**
	 * Retorna a descrição completa do documento.
	 */
	@Override
	public String getDescrDocumento() {
		return super.getDescrDocumento();
	}

	/**
	 * Retorna a <b>descrição</b> do nível de acesso do documento definido no
	 * momento da criação do documento, desconsiderando as redefinições de
	 * nível.
	 */
	public String getNivelAcesso() {
		log.debug("[getNivelAcesso] - Obtendo Nivel de Acesso do documento, definido no momento da criação do mesmo");
		String nivel = null;
		ExNivelAcesso nivelAcesso = getExNivelAcesso();

		if (nivelAcesso != null && nivelAcesso.getGrauNivelAcesso() != null) {
			nivel = nivelAcesso.getGrauNivelAcesso().toString();

		} else {
			log.warn("[getNivelAcesso] - O nível de acesso ou o grau do nível de acesso do documento é nulo.");
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

	public String getLotaSubscritorString() {
		if (getLotaTitular() != null)
			return getLotaTitular().getDescricao();
		else if (getOrgaoExterno() != null)
			return getOrgaoExterno().getDescricao();
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
	public String getDtDocDDMMYY() {
		if (getDtDoc() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			return df.format(getDtDoc());
		}
		return "";
	}
	
	
	/**
	 * Retorna a data do documento no formato dd/mm/aa, por exemplo, 01/02/10.
	 */
	public String getDtPrimeiraAssinaturaDDMMYY() {
		if (getDtPrimeiraAssinatura()!= null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			return df.format(getDtPrimeiraAssinatura());
		}
		return "";
	}
	

	/**
	 * Retorna a data original do documento externo no formato dd/mm/aa, por
	 * exemplo, 01/02/2010.
	 */
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
	 * Retorna a data do documento no formato dd/mm/aaaa, por exemplo,
	 * 01/02/2010.
	 */
	public String getDtDocYYYYMMDD() {
		if (getDtDoc() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
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
			if ("GOVSP".equals(Prop.get("/siga.local"))
						&& lotaBase.getLocalidade() != null && lotaBase.getLocalidade().getNmLocalidade() != null) {
				s = lotaBase.getLocalidade().getNmLocalidade();	
			} else {
				s = lotaBase.getLocalidadeString();	
			}
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
		return DocumentoUtil
				.obterDataExtenso(getLocalidadeString(), getDtDoc());
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
		if (a[0].equals("-"))
			return " ";
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
		if (a[1].equals("-"))
			return " ";
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
		if (a[2].equals("-"))
			return " ";
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
	 * Verifica se um documento é composto
	 */
	public Boolean isComposto() {
		return getExFormaDocumento().isComposto();
	}

	/**
	 * Verifica se um documento pode ser indexado conforme as regras
	 * <b>(informar regras)</b>
	 */
	public Boolean isIndexavel() {
		return !isPendenteDeAssinatura() && !isCancelado();
	}
	
	public String getMarcaDagua() {
		String marcaDagua = null;
		
		if( getExModelo() != null &&  getExModelo().getModeloAtual() != null) {
			marcaDagua = getExModelo().getModeloAtual().getMarcaDagua();
		}
			
		return marcaDagua == null ? "" : marcaDagua.trim();
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
		if (a[3].equals("-"))
			return " ";
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
				if (via != null
						&& via.getExTipoDestinacao() != null
						&& via.getExTipoDestinacao().getIdTpDestinacao() != null
						&& via.getExTipoDestinacao().getIdTpDestinacao() == ExTipoDestinacao.TIPO_DESTINACAO_SETOR_COMPETENTE)
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
			if (getExClassificacaoAtual() != null){				
				if (getExClassificacaoAtual().getAtual() != null)
					vias = getExClassificacaoAtual().getAtual().getExViaSet();
				else
					vias = getExClassificacaoAtual().getExViaSet();
			}

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
				&& ((ExVia) vias.toArray()[0]) != null
				&& ((ExVia) vias.toArray()[0]).getExTipoDestinacao() != null
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
	public String getSubscritorString() {
		if (getSubscritor() != null)
			return getSubscritor().getDescricaoIniciaisMaiusculas();
		else if (getOrgaoExterno() != null || getObsOrgao() != null
				|| getNmSubscritorExt() != null) {
			String str = "";
			if (getOrgaoExterno() != null)
				str = getOrgaoExterno().getDescricao();
			if (getObsOrgao() != null) {
				if (str.length() != 0)
					str = str + " - ";
				str = str + getObsOrgao();
			}
			if (getNmSubscritorExt() != null) {
				if (str.length() != 0)
					str = str + " - ";
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
	public String getNmMod() {
		if (getExModelo() != null)
			return getExModelo().getNmMod();
		return null;
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
	 * Verifica se um documento possui controle de colaboração.
	 */
	public boolean isColaborativo() {
		final Set<ExMovimentacao> movs = getMobilGeral().getExMovimentacaoSet();

		for (final ExMovimentacao mov : movs) {
			if ((mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.CONTROLE_DE_COLABORACAO)
					&& mov.getExMovimentacaoCanceladora() == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifica se um documento possui agendamento de publicação no DJE. ()
	 */
	public boolean isPublicacaoAgendada() {
		final Set<ExMovimentacao> movs = getMobilGeral().getExMovimentacaoSet();

		for (final ExMovimentacao mov : movs) {
			if ((mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.AGENDAMENTO_DE_PUBLICACAO)
					&& mov.getExMovimentacaoCanceladora() == null) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isPublicacaoAgendadaEhCadastranteDOE(DpPessoa titular) {
		final Set<ExMovimentacao> movs = getMobilGeral().getMovsNaoCanceladas(ExTipoDeMovimentacao.AGENDAR_PUBLICACAO_DOE, Boolean.TRUE);

		if(movs.isEmpty()) {
			return false;
		} else {
			for (ExMovimentacao exMovimentacao : movs) {
				if(exMovimentacao.getDtFimMov() != null || !exMovimentacao.getCadastrante().equivale(titular)) {
					return true;
				}
			}
			return false;	
		}
	}

	/**
	 * Verifica se um documento possui <b>solicitação</b> de publicação no DJE.
	 */
	public boolean isPublicacaoSolicitada() {
		final Set<ExMovimentacao> movs = getMobilGeral().getExMovimentacaoSet();

		for (final ExMovimentacao mov : movs) {
			if ((mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.PEDIDO_PUBLICACAO)
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
			if ((mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.AGENDAMENTO_DE_PUBLICACAO_BOLETIM)
					&& mov.getExMovimentacaoCanceladora() == null) {
				return true;
			}
		}
		return false;
	}

	public boolean isBoletimPublicado() {
		final Set<ExMovimentacao> movs = getMobilGeral().getExMovimentacaoSet();

		for (final ExMovimentacao mov : movs) {
			if (((mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.PUBLICACAO_BOLETIM || mov
					.getExTipoMovimentacao() == ExTipoDeMovimentacao.NOTIFICACAO_PUBL_BI))
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
			if ((mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.DISPONIBILIZACAO)
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
				+ (isEletronico() ? "" : " (físico)");
	}

	/**
	 * COMPLETAR
	 * 
	 * @return
	 */
    public Map<String, String> getForm() {
        Map<String, String> m = new TreeMap<String, String>();
        final byte[] form = getConteudoBlobForm();
        if (form != null)
            Utils.mapFromUrlEncodedForm(m, form);
        return m;
    }

    public SortedSet<String> getNomesDasVariaveisDaEntrevista() {
        Map<String, String> m = getForm();
        SortedSet<String> set = new TreeSet<>();
        set.addAll(m.keySet());
        return set;
    }

	public Map<String, String> getFormConfidencial(DpPessoa titular, DpLotacao lotaTitular) {
		if (Ex.getInstance()
				.getComp()
				.pode(ExPodeAcessarDocumento.class, titular, lotaTitular,
						getMobilGeral())) {
			return getForm();
		} else {
			return new Hashtable<String, String>();
		}
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
	 * COMPLETAR
	 * 
	 * @return
	 */
	public Set<ExMovimentacao> getExMovimentacaoIndexacaoSet() {
		Set<ExMovimentacao> mSet = new HashSet<ExMovimentacao>();
		for (ExMobil mob : getExMobilSet()) {
			for (ExMovimentacao m : mob.getExMovimentacaoSet()) {
				if (m.getExMovimentacaoCanceladora() == null
						&& (m.getExTipoMovimentacao() == ExTipoDeMovimentacao.ANOTACAO
								|| m.getExTipoMovimentacao() == ExTipoDeMovimentacao.ANEXACAO
								|| m.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA
								|| m.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO || m
								.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA)) {
					mSet.add(m);
				}
			}
		}
		return mSet;
	}

	/**
	 * Retorna conjunto com todas as movimentações do documento.
	 */
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
	private List<ExArquivoNumerado> getArquivosNumerados(ExMobil mob,
			List<ExArquivoNumerado> list, int nivel) {
		
		List<ExArquivoNumerado> listaInicial = list, listaFinal = new ArrayList<>();	
		
		if (mob == null)
			return listaFinal;
		
		boolean podeAtualizarPaginas = true;

		// Incluir o documento principal
		ExArquivoNumerado anDoc = new ExArquivoNumerado();
		anDoc.setArquivo(this);
		anDoc.setMobil(mob);
		anDoc.setNivel(nivel);			
		listaInicial.add(anDoc);					
				
		getAnexosNumerados(mob, listaInicial, nivel + 1, false);						
		
		if (podeReordenar() && podeExibirReordenacao() && temOrdenacao()) {
			boolean houveAlteracaoNaOrdenacao = false;
			podeAtualizarPaginas = false;
			String referenciaHtmlCompletoDocPrincipal = anDoc.getReferenciaHtmlCompleto();	
			String referenciaPDFCompletoDocPrincipal = anDoc.getReferenciaPDFCompleto();
			String ordenacaoDoc[] = this.getOrdenacaoDoc().split(";");			
					
			ordenarDocumentos(ordenacaoDoc, listaInicial, listaFinal, referenciaHtmlCompletoDocPrincipal, referenciaPDFCompletoDocPrincipal);
			
			if (listaInicial.size() > listaFinal.size()) {
				adicionarDocumentosNovosNaOrdenacao(listaInicial, listaFinal);
				houveAlteracaoNaOrdenacao = true;
			}
			
			if (ordenacaoDoc.length > listaFinal.size())
				houveAlteracaoNaOrdenacao = true;		
			
			if (estaNaOrdemOriginal(listaInicial, listaFinal)) 
				limparOrdenacaoDosDocumentos();
			else if (houveAlteracaoNaOrdenacao)
				enviarNovaOrdenacaoDosDocumentos(listaFinal);								
		
		} else {					
			listaFinal = listaInicial;			
		}				
					
		// Numerar as paginas
		if (isNumeracaoUnicaAutomatica() || (SigaMessages.isSigaSP() && mobilPrincipalPossuiJuntadaOuDesentranhamento(mob, listaFinal))) {				
			if (mob.getDnmNumPrimeiraPagina() == null) {
				if (mob.isVolume() && mob.getNumSequencia() > 1) {
					List<ExArquivoNumerado> listVolumeAnterior = mob.doc()
							.getArquivosNumerados(
									mob.doc().getVolume(
											mob.getNumSequencia() - 1));
					int i = listVolumeAnterior.get(
							listVolumeAnterior.size() - 1).getPaginaFinal();
					mob.setDnmNumPrimeiraPagina(i + 1);
					ExDao.getInstance().gravar(mob);
				} else {
					mob.setDnmNumPrimeiraPagina(1);
				}
			}
			int j = mob.getDnmNumPrimeiraPagina();

			// removerDesentranhamentosQueNaoFazemParteDoDossie(list);

			montarPaginas(podeAtualizarPaginas ? listaFinal : listaInicial, j);			
		}

		return listaFinal;
	}
	
	private boolean estaNaOrdemOriginal(List<ExArquivoNumerado> listaInicial, List<ExArquivoNumerado> listaFinal) {
		return listaInicial.equals(listaFinal);
	}
	
	private void montarPaginas(List<ExArquivoNumerado> arquivos, int j) {
		for (ExArquivoNumerado an : arquivos) {
			an.setPaginaInicial(j);
			j += an.getNumeroDePaginasParaInsercaoEmDossie() - 1;
			an.setPaginaFinal(j);
			j++;
		}
	}
	
	private void ordenarDocumentos(String[] ordenacaoDoc, List<ExArquivoNumerado> listaInicial, List<ExArquivoNumerado> listaFinal, String referenciaHtmlCompletoDocPrincipal, String referenciaPDFCompletoDocPrincipal) {
		for(String id : ordenacaoDoc) {
			encontrarArquivoNumerado(Long.valueOf(id), listaInicial, listaFinal, referenciaHtmlCompletoDocPrincipal, referenciaPDFCompletoDocPrincipal);				
		}				
	}
	
	private void encontrarArquivoNumerado(Long id, List<ExArquivoNumerado> listaInicial, List<ExArquivoNumerado> listaFinal, String referenciaHtmlCompletoDocPrincipal, String referenciaPDFCompletoDocPrincipal) {
		for (ExArquivoNumerado arquivo : listaInicial) {					
			if (id.equals(arquivo.getArquivo().getIdDoc())) {
				arquivo.setReferenciaHtmlCompletoDocPrincipal(referenciaHtmlCompletoDocPrincipal);
				arquivo.setReferenciaPDFCompletoDocPrincipal(referenciaPDFCompletoDocPrincipal);
				listaFinal.add(arquivo);					
				break;
			}					
		}			
	}
	
	private void adicionarDocumentosNovosNaOrdenacao(List<ExArquivoNumerado> listaInicial, List<ExArquivoNumerado> listaFinal) {
		for (ExArquivoNumerado arquivo : listaInicial) {
			if (!listaFinal.contains(arquivo)) 
				listaFinal.add(arquivo);			
		}
	}
	
	private void enviarNovaOrdenacaoDosDocumentos(List<ExArquivoNumerado> listaArquivoNumerado) {
		String ordenacao = "";
		for (ExArquivoNumerado arquivoNumerado : listaArquivoNumerado) {				
			if (ordenacao.length() > 0) ordenacao += ";";
			ordenacao += arquivoNumerado.getArquivo().getIdDoc();				
		}
		this.setOrdenacaoDoc(ordenacao);
	}
	
	private void limparOrdenacaoDosDocumentos() {
		this.setOrdenacaoDoc(null);
	}
	
	public boolean mobilPrincipalPossuiJuntadaOuDesentranhamento(ExMobil mobilPrincipal, List<ExArquivoNumerado> arquivosNumerados) {
		if (arquivosNumerados != null) {
			ExMovimentacao movimentacao;
			ITipoDeMovimentacao[] tpIdMovs = { ExTipoDeMovimentacao.JUNTADA,
					ExTipoDeMovimentacao.JUNTADA_EXTERNO,
					ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA };			
			
			for (ExArquivoNumerado arquivoNumerado : arquivosNumerados) {				
				if (arquivoNumerado.getMobil().getId() != mobilPrincipal.getId()) {
					
					movimentacao = arquivoNumerado.getMobil().getMovsNaoCanceladas(tpIdMovs)
							.stream()
							.filter(m -> m.getExMobilRef().getId() == mobilPrincipal.getId())
							.findAny()
							.orElse(null);
												
					if (movimentacao != null) {
						return true;
					}
				}										
			}			
		}		
		
		return false;
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

						if (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA) {

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
		return (getExFormaDocumento().isNumeracaoUnica() || (SigaMessages.isSigaSP() && isExpediente() && !getExDocumentoFilhoSet().isEmpty()))
				&& (getExTipoDocumento().getId() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO || getExTipoDocumento()
						.getId() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_FOLHA_DE_ROSTO)
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
			int nivel, boolean copia) {

		SortedSet<ExMovimentacao> set = new TreeSet<ExMovimentacao>(
				new AnexoNumeradoComparator());

		incluirArquivos(getMobilGeral(), set);
		if (!mob.isGeral())
			incluirArquivos(mob, set);

		// Incluir recursivamente
		for (ExMovimentacao m : set) {
			ExArquivoNumerado an = new ExArquivoNumerado();
			an.setNivel(nivel);
			if (m.getExTipoMovimentacao() == ExTipoDeMovimentacao.JUNTADA) {
				an.setArquivo(m.getExDocumento());
				an.setMobil(m.getExMobil());
				an.setData(m.getData());
				list.add(an);
				m.getExDocumento().getAnexosNumerados(m.getExMobil(), list,
						nivel + 1, copia);
			} else if (m.getExTipoMovimentacao() == ExTipoDeMovimentacao.COPIA) {
				an.setArquivo(m.getExMobilRef().doc());
				an.setMobil(m.getExMobilRef());
				an.setData(m.getData());
				an.setCopia(true);
				list.add(an);
//				m.getExDocumento().getAnexosNumerados(m.getExMobilRef(), list,
//						nivel + 1, true);
			} else if (isDesentranhamentoSP(mob, m)) {				
				continue;				
			} else {			
				an.setArquivo(m);
				an.setMobil(m.getExMobil());
				list.add(an);
			}
		}
				
	}
	
	private boolean isDesentranhamentoSP(ExMobil mobil, ExMovimentacao movimentacao) {
		if (SigaMessages.isSigaSP() &&
				movimentacao.getExTipoMovimentacao() == ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA &&
						movimentacao.getExMobil().getId().equals(mobil.getId())) {

			return this.getIdDocPrincipal() == this.getIdDoc();
			
		}	
		
		return false;
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
				if (!m.isCancelada() && m.isPdf() && m.getExTipoMovimentacao() != ExTipoDeMovimentacao.ANEXACAO_DE_ARQUIVO_AUXILIAR) {
					set.add(m);
				}
			}
		}
		// Incluir copias
		if (mob.getExMovimentacaoSet() != null) {
			for (ExMovimentacao m : mob.getExMovimentacaoSet()) {
				if (!m.isCancelada()) {
					if (m.getExTipoMovimentacao() == ExTipoDeMovimentacao.COPIA) {
						set.add(m);
					}
				}
			}
		}
		// Incluir os documentos juntados
		if (mob.getExMovimentacaoReferenciaSet() != null)
			for (ExMovimentacao m : mob.getExMovimentacaoReferenciaSet()) {
				if (!m.isCancelada()) {
					if (m.getExTipoMovimentacao() == ExTipoDeMovimentacao.JUNTADA) {
						set.add(m);
					} else if (m.getExTipoMovimentacao() == ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA) {
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
	
	public Set<ExMovimentacao> getVinculosPorTipo(ExTipoDeVinculo tipo) {
		if (getMobilGeral() == null)
			return new TreeSet<ExMovimentacao>();
		return getMobilGeral()
				.getMovsNaoCanceladas(ExTipoDeMovimentacao.REFERENCIA,	true).stream().filter(i -> i.getTipoDeVinculo() == tipo).collect(Collectors.toSet());
	}

	/**
	 * Retorna as {@link ExMovimentacao Movimentações} de
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO
	 * Assinaturas Com Token} válidas.
	 * 
	 * @return Movimentações de Assinaturas Com Token
	 */
	public Set<ExMovimentacao> getAssinaturasComToken() {
		if (getMobilGeral() == null)
			return new TreeSet<ExMovimentacao>();
		return getMobilGeral()
				.getMovsNaoCanceladas(
						ExTipoDeMovimentacao.ASSINATURA_DIGITAL_DOCUMENTO);
	}

	/**
	 * Retorna as {@link ExMovimentacao Movimentações} de
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO
	 * Autenticação com token} validas.
	 * 
	 * @return Movimentações de Autenticação com token.
	 */
	public Set<ExMovimentacao> getAutenticacoesComToken() {
		if (getMobilGeral() == null)
			return new TreeSet<ExMovimentacao>();
		return getMobilGeral()
				.getMovsNaoCanceladas(
						ExTipoDeMovimentacao.CONFERENCIA_COPIA_DOCUMENTO,
						true);
	}

	/**
	 * Retorna as {@link ExMovimentacao Movimentações} de
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA Assinaturas
	 * Com Senha} válidas.
	 * 
	 * @return Movimentações de Assinaturas Com Senha
	 */
	public Set<ExMovimentacao> getAssinaturasComSenha() {
		if (getMobilGeral() == null)
			return new TreeSet<ExMovimentacao>();
		return getMobilGeral().getMovsNaoCanceladas(
				ExTipoDeMovimentacao.ASSINATURA_COM_SENHA);
	}
	
	public Set<ExMovimentacao> getAssinaturasPor() {
		if (getMobilGeral() == null)
			return new TreeSet<ExMovimentacao>();
		return getMobilGeral().getMovsNaoCanceladas(
				ExTipoDeMovimentacao.ASSINATURA_POR);
	}


	/**
	 * Retorna as {@link ExMovimentacao Movimentações} de
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA
	 * Autenticação com senha} validas.
	 * 
	 * @return Movimentações de Autenticação com senha.
	 */
	public Set<ExMovimentacao> getAutenticacoesComSenha() {
		if (getMobilGeral() == null)
			return new TreeSet<ExMovimentacao>();
		return getMobilGeral()
				.getMovsNaoCanceladas(
						ExTipoDeMovimentacao.CONFERENCIA_COPIA_COM_SENHA,
						true);
	}

	public Set<ExMovimentacao> getRegistrosDeAssinatura() {
		if (getMobilGeral() == null)
			return new TreeSet<ExMovimentacao>();
		return getMobilGeral()
				.getMovsNaoCanceladas(
						ExTipoDeMovimentacao.REGISTRO_ASSINATURA_DOCUMENTO);
	}

	public Set<ExMovimentacao> getSolicitantesDeAssinatura() {
		if (getMobilGeral() == null)
			return new TreeSet<ExMovimentacao>();
		return getMobilGeral()
				.getMovsNaoCanceladas(
						ExTipoDeMovimentacao.SOLICITACAO_DE_ASSINATURA,
						true);
	}

	/**
	 * Retorna as {@link ExMovimentacao movimentações} de assinatura, seja
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA com senha},
	 * seja com
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO
	 * token}, validas.
	 * 
	 * @return As Movimentações de Assinatura.
	 */
	public Set<ExMovimentacao> getAssinaturasComTokenOuSenha() {
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();
		
		ITipoDeMovimentacao[] idTpMovs = {ExTipoDeMovimentacao.ASSINATURA_DIGITAL_DOCUMENTO, 
				ExTipoDeMovimentacao.ASSINATURA_COM_SENHA};
		
		set.addAll(getMobilGeral().getMovsNaoCanceladas(idTpMovs));
		return set;
	}

	public Set<ExMovimentacao> getAssinaturasComTokenOuSenhaERegistros() {
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();
		set.addAll(getAssinaturasComTokenOuSenha());
		set.addAll(getRegistrosDeAssinatura());
		return set;
	}

	/**
	 * Retorna as {@link ExMovimentacao movimentações} de autenticação, seja
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA com
	 * senha}, seja com
	 * {@link ExTipoMovimentacao#TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO
	 * token}, validas.
	 * 
	 * @return As Movimentações de Autenticação.
	 */
	public Set<ExMovimentacao> getAutenticacoesComTokenOuSenha() {
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();
		set.addAll(getAutenticacoesComSenha());
		set.addAll(getAutenticacoesComToken());
		return set;
	}

	public Set<ExMovimentacao> getAssinaturasEAutenticacoesComTokenOuSenha() {
		if (getMobilGeral() == null)
			return null;
		Set<ExMovimentacao> movs = new TreeSet<ExMovimentacao>();
		movs.addAll(getAssinaturasComTokenOuSenha());
		movs.addAll(getAutenticacoesComTokenOuSenha());
		return movs;
	}

	public Set<ExMovimentacao> getAssinaturasEAutenticacoesComTokenOuSenhaERegistros() {
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();
		set.addAll(getAssinaturasComTokenOuSenha());
		set.addAll(getAutenticacoesComTokenOuSenha());
		set.addAll(getRegistrosDeAssinatura());
		return set;
	}

    public boolean isAssinadoPorTodosOsSignatariosComTokenOuSenha() {
        if (getSubscritor() == null)
            return false;
        for (DpPessoa pess : getSubscritorECosignatarios()) {
            if (!isAssinadoPelaPessoaComTokenOuSenha(pess))
                return false;
        }
        return true;
    }

    public boolean isAutenticadoENaoTemSubscritor() {
        if (getSubscritor() != null)
            return false;
         return getAutenticacoesComTokenOuSenha().size() > 0;
    }

	@Override
	public Set<ExMovimentacao> getAssinaturasDigitais() {
		return getAssinaturasEAutenticacoesComTokenOuSenha();
	}

	public Date getDtAssinatura() {
		Set<ExMovimentacao> assinaturas = getAssinaturasEAutenticacoesComTokenOuSenhaERegistros();
		if (!assinaturas.isEmpty())
			return assinaturas.iterator().next().getDtIniMov();
		return null;
	}

	public String getAssinantesCompleto() {
		String retorno = "";
		String conferentes = Documento
				.getAssinantesString(getAutenticacoesComToken(),getDtDoc());
		String conferentesSenha = Documento
				.getAssinantesString(getAutenticacoesComSenha(),getDtDoc());
		String assinantesToken = Documento
				.getAssinantesString(getAssinaturasComToken(),getDtDoc());
		String assinantesSenha = Documento
				.getAssinantesString(getAssinaturasComSenha(),getDtDoc());
		Set<ExMovimentacao> movsAssinatura = getAssinaturasComToken();
		movsAssinatura.addAll(getAssinaturasComSenha());
		String assinantesPor = Documento
				.getAssinantesPorString(getAssinaturasPor(),getDtDoc(),movsAssinatura);
		
		if(Prop.isGovSP() && assinantesPor != null && !"".equals(assinantesPor)) { 
			assinantesToken = removeAssinadosPor(getAssinaturasComToken());
			assinantesSenha = removeAssinadosPor(getAssinaturasComSenha());
		}
		
		if (assinantesToken.length() > 0)
			retorno = "Assinado digitalmente por " + assinantesToken + ".\n";

		if (assinantesPor.length() > 0) {
			retorno = retorno + assinantesPor +".\n" ;
		}
		
		if (assinantesSenha.length() > 0)
			retorno = retorno + "Assinado com senha por " + assinantesSenha
					+ ".\n";

		if (conferentes.length() > 0)
			retorno += conferentes.length() > 0 ? "Autenticado digitalmente por "
					+ conferentes + ".\n"
					: "";

		if (conferentesSenha.length() > 0)
			retorno += conferentesSenha.length() > 0 ? "Autenticado com senha por "
					+ conferentesSenha + ".\n"
					: "";

		return retorno;
	}
	
	public String getVinculosCompleto() {
		String retorno = "";
		for (ExTipoDeVinculo tipo : Lists.reverse(Lists.newArrayList(ExTipoDeVinculo.values()))) {
			if (tipo != ExTipoDeVinculo.REVOGACAO)
				continue;
			String s = Documento.getVinculosString(getVinculosPorTipo(tipo));
			if (s.length() > 0)
				retorno += tipo.getAcao() + " " + s + ". ";
		}
		return retorno;
	}

	private String removeAssinadosPor(Set<ExMovimentacao> listaAssinantes) {
		// Remove da lista de assinantes (tanto com certificado digital quanto com senha) os que já estão na lista de assinar por 
		Set<ExMovimentacao> listaAssinantesPor = new TreeSet<ExMovimentacao>();
		listaAssinantesPor.addAll(getAssinaturasPor());
		String porAss = "";

		for (ExMovimentacao por : listaAssinantesPor) {
			porAss = por.getDescrMov() != null ? por.getDescrMov().substring(por.getDescrMov().lastIndexOf(":"), por.getDescrMov().length()) : "";
			for (ExMovimentacao ass : listaAssinantes) {
				if(!ass.getCadastrante().getId().equals(ass.getSubscritor().getId()) && (ass.getDescrMov() != null && ass.getDescrMov().indexOf(porAss) != -1)) {
					listaAssinantes.remove(ass);
					break;
				}
			}
		}
		return Documento.getAssinantesString(listaAssinantes,getDtDoc());
	}

	public String getSolicitantesDeAssinaturaCompleto() {
		String retorno = "";
		String revisores = Documento
				.getAssinantesString(getSolicitantesDeAssinatura(),getDtDoc());

		if (revisores.length() > 0)
			retorno = "Revisado por " + revisores + "";
		return retorno;
	}
	
	public boolean isPendenteDeAssinatura() {

		// Edson: não deve estar pendente de assinatura se estiver em
		// elaboração, pois ainda não está
		// pronto para ser assinado. Acertar isso.
		if (!isFinalizado())
			return true;

		if (getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_FOLHA_DE_ROSTO
				|| getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_FOLHA_DE_ROSTO) {
			if (getExMobilSet() != null && getExMobilSet().size() > 1)
				return false;
			else
				return true;
		}

		if (isEletronico()) {
			if (isExternoCapturado()
					&& getAutenticacoesComTokenOuSenha().isEmpty())
				return true;

			// compatibiliza com versoes anteriores do SIGA que permitia
			// transferir
			// documento antes que todos os cossiganatarios assinassem o
			// documento
			Date dtInicioObrigatoriedadeTodosCossigsAssinarem;
			try {
				dtInicioObrigatoriedadeTodosCossigsAssinarem = new SimpleDateFormat(
						"dd/MM/yyyy").parse("09/05/2014");
				if (getDtRegDoc().before(
						dtInicioObrigatoriedadeTodosCossigsAssinarem)
						&& !getAssinaturasComTokenOuSenha().isEmpty())
					return false;
			} catch (ParseException e) {
			}
			if (getSubscritor() != null
					&& !isAssinadoPorTodosOsSignatariosComTokenOuSenha())
				return true;
		} else {
			if (getAssinaturasComTokenOuSenhaERegistros().isEmpty())
				return true;
		}

		return false;
	}

	public boolean isAssinadoPeloSubscritorComTokenOuSenha() {
		return isAssinadoPelaPessoaComTokenOuSenha(getSubscritor());
	}

	public boolean isAssinadoPelaPessoaComTokenOuSenha(DpPessoa subscritor) {
		for (ExMovimentacao assinatura : getAssinaturasComTokenOuSenha()) {
			if (assinatura.getSubscritor().equivale(subscritor) 
					|| (!assinatura.getSubscritor().equivale(assinatura.getCadastrante())
							&& assinatura.getCadastrante().equivale(subscritor)))
				return true;
		}
		return false;
	}

	/**
	 * verifica se um documento ainda está em rascunho, ou seja, se não está
	 * finalizado ou se está finalizado mas é eletrônico.
	 */
	@Override
	public boolean isRascunho() {
		return !isFinalizado() || (isEletronico() && isPendenteDeAssinatura());
	}

	/**
	 * verifica se um documento está sem efeito.
	 */
	@Override
	public boolean isSemEfeito() {
		final Set<ExMovimentacao> movs = getMobilGeral().getExMovimentacaoSet();

		if (movs != null) {
			for (final ExMovimentacao mov : movs) {
				if ((mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.TORNAR_SEM_EFEITO)
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

	public ExMobil getMobilDefaultParaReceberJuntada() {
		if (getExMobilSet() == null)
			return null;
		if (isExpediente())
			return getPrimeiraVia();
		else
			return getUltimoVolume();
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
	 * Retorna o número do último volume (funciona apenas para processo
	 * administrativo).
	 */
	public int getNumUltimoMobil() {
		int maxNumVolume = 0;
		for (final ExMobil mob : getExMobilSet()) {
			if (mob.getNumSequencia() > maxNumVolume) {
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
		return getMobil(i, ExTipoMobil.TIPO_MOBIL_VOLUME);
	}

	/**
	 * Retorna o móbil do documento de acordo com o seu número.
	 */
	private ExMobil getMobil(int i, Long idTipoMobil) {
		for (final ExMobil mob : getExMobilSet()) {
			if (!mob.isGeral()
					&& (idTipoMobil == null || mob.getExTipoMobil()
							.getIdTipoMobil() == idTipoMobil)
					&& mob.getNumSequencia() == i) {
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
		return getMobil(i, ExTipoMobil.TIPO_MOBIL_VIA);
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

	public ExMobil getUltimoVolumeOuGeral() {
		ExMobil ult = getVolume(getNumUltimoVolume());
		if (ult != null)
			return ult;
		return getMobilGeral();
	}

	/**
	 * Retorna o primeiro móbil do documento, seja via ou volume.
	 */
	public ExMobil getPrimeiroMobil() {
		return getMobil(1, null);
	}

	/**
	 * Retorna o último móbil do documento, seja via ou volume.
	 */
	public ExMobil getUltimoMobil() {
		return getMobil(getNumUltimoMobil(), null);
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
		return (getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_FOLHA_DE_ROSTO);
	}

	/**
	 * Verifica se um documento é de origem interna.
	 */
	public boolean isInternoFolhaDeRosto() {
		if (getExTipoDocumento() == null)
			return false;
		return (getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_FOLHA_DE_ROSTO);
	}

	/**
	 * Verifica se um documento é capturado de uma fonte externa.
	 */
	public boolean isInternoCapturado() {
		if (getExTipoDocumento() == null)
			return false;
		return (getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_CAPTURADO ||
				getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_CAPTURADO_FORMATO_LIVRE);
	}

	/**
	 * Verifica se um documento é capturado de uma fonte externa.
	 */
	public boolean isExternoCapturado() {
		if (getExTipoDocumento() == null)
			return false;
		return (getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_CAPTURADO ||
				getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_CAPTURADO_FORMATO_LIVRE);
	}

	public boolean isCapturadoFormatoLivre() {
		if (getExTipoDocumento() == null)
			return false;
		return (getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_CAPTURADO_FORMATO_LIVRE ||
				getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_CAPTURADO_FORMATO_LIVRE);
	}

	/**
	 * Verifica se um documento é capturado.
	 */
	public boolean isCapturado() {
		return isInternoCapturado() || isExternoCapturado();
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
	
	public DpPessoa getSubscritorDiffTitularDoc() {
		if (getSubscritor() != null && !getSubscritor().equivale(getCadastrante())) 
			return getSubscritor();
		return null;
	}
	
	public List<DpPessoa> getListaCossigsSubscritorAssinouDocHoje() {
		List<DpPessoa> listaSubscrCossigFinal = new ArrayList<DpPessoa>();
		List<DpPessoa> listaSubscrCossig =  this.getSubscritorECosignatarios();

		if (!listaSubscrCossig.isEmpty()) {
			Set<ExMovimentacao> listaMovAssinaturas = this.getAssinaturasComTokenOuSenhaERegistros();
			for (ExMovimentacao mov : listaMovAssinaturas) {
				if (DateUtils.isToday(mov.getData())) {
					for (DpPessoa pessoa : listaSubscrCossig) {
						if (mov.getTitular().equivale(pessoa)) {
							listaSubscrCossigFinal.add(pessoa);
						}
					}
				}
			}
		}
		return listaSubscrCossigFinal;
	}

	/**
	 * Retorna uma lista com o todos os cossignatários.
	 */
	public List<DpPessoa> getCosignatarios() {

		List<DpPessoa> cosignatarios = new ArrayList<DpPessoa>();

		if (getMobilGeral() != null
				&& getMobilGeral().getExMovimentacaoSet() != null) {
			for (ExMovimentacao m : getMobilGeral().getExMovimentacaoSet()) {
				if (m.getExTipoMovimentacao() == ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO
						&& m.getExMovimentacaoCanceladora() == null) {
					cosignatarios.add(m.getSubscritor());
				}
			}
		}

		return cosignatarios;
	}

	/**
	 * Retorna se uma pessoa figura ou não como cossignatária no documento.
	 */
	public boolean isCossignatario(DpPessoa quem) {
		for (DpPessoa p : getCosignatarios())
			if (p.equivale(quem))
				return true;
		return false;
	}

	/**
	 * Retorna uma lista com o todos os cossignatários.
	 */
	public List<ExMovimentacao> getMovsCosignatario() {

		List<ExMovimentacao> cosignatarios = new ArrayList<ExMovimentacao>();

		if (getMobilGeral() != null
				&& getMobilGeral().getExMovimentacaoSet() != null) {
			for (ExMovimentacao m : getMobilGeral().getExMovimentacaoSet()) {
				if (m.getExTipoMovimentacao() == ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO
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
				if ((mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO
						|| mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO_INTERNO
						|| mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO_INTERNO_TRANSFERENCIA || mov
						.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA)
						&& !mov.isCancelada())
					subscritoresDesp.add(mov.getSubscritor());
			}
		}

		return subscritoresDesp;
	}

    /**
     * Retorna se determinado documento recebeu juntada.
     */
    public boolean isRecebeuJuntada() {
        return contemMovimentacaoReferenciaEmAlgumMobile(ExTipoDeMovimentacao.JUNTADA);
    }

    /**
     * Retorna se determinado documento recebeu anexação.
     */
    public boolean isRecebeuAnexo() {
        return contemMovimentacaoEmAlgumMobile(ExTipoDeMovimentacao.ANEXACAO);
    }

    private boolean contemMovimentacaoEmAlgumMobile(ExTipoDeMovimentacao tpmov) {
        for (ExMobil mob : getExMobilSet()) {
            if (mob.getExMovimentacaoSet() != null ) {
                for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
                    if ((mov.getExTipoMovimentacao() == tpmov)
                            && !mov.isCancelada())
                        return true;
                }
            }
        }
        return false;
    }

    private boolean contemMovimentacaoReferenciaEmAlgumMobile(ExTipoDeMovimentacao tpmov) {
        for (ExMobil mob : getExMobilSet()) {
            if (mob.getExMovimentacaoReferenciaSet() != null ) {
                for (ExMovimentacao mov : mob.getExMovimentacaoReferenciaSet()) {
                    if ((mov.getExTipoMovimentacao() == tpmov)
                            && !mov.isCancelada())
                        return true;
                }
            }
        }
        return false;
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
		if (isProcesso()) {
			return getMobilGeral().isEliminado();
		}

		boolean eliminado = false;

		for (ExMobil m : getExMobilSet())
			if (!m.isGeral())
				if (m.isEliminado())
					eliminado = true;
				else {
					return false;
				}

		return eliminado;

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
	
    public void atrasarAtualizacaoDoArquivo() {
        atrasandoAtualizacaoDoArquivo = true;
    }
    
    public void atualizarArquivo() {
        atrasandoAtualizacaoDoArquivo = false;
        for (String s : mapAtualizacaoDoArquivo.keySet()) {
            setConteudoBlob(s, mapAtualizacaoDoArquivo.get(s));
        }
        mapAtualizacaoDoArquivo.clear();
    }
    


	public void setConteudoBlob(final String nome, final byte[] conteudo) {
	    if (atrasandoAtualizacaoDoArquivo) {
	        mapAtualizacaoDoArquivo.put(nome, conteudo);
	        return;
	    }
		final Compactador zip = new Compactador();
		final byte[] arqZip = getConteudoBlobDoc();
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
		if (blob != null) {
			setConteudoBlobDoc(blob);
		}
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
		if (sHtml != null)
			setConteudoBlob("doc.htm", sHtml.getBytes("ISO-8859-1"));
		else
			setConteudoBlob("doc.htm", null);
	}

	public void setConteudoBlobPdf(final byte[] conteudo) throws Exception {

		// Atenção, não retirar esse teste nunca, pois ele é quem garante que o
		// pdf não será refeito.
		if (!getAssinaturasComTokenOuSenhaERegistros().isEmpty())
			throw new AplicacaoException(
					"O conteúdo não pode ser alterado pois o documento já está assinado");
		setConteudoBlob("doc.pdf", conteudo);
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
		ExNivelAcesso exNivelAcessoAtual = getExNivelAcessoAtual();
		if (exNivelAcessoAtual != null) {
			return exNivelAcessoAtual.getIdNivelAcesso();
		}
		return null;
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

	public boolean isDnmAcessoMAisAntigoQueODosPais() {
		for (ExDocumento doc : getTodosOsPaisDasVias()) {
			if (doc.getDnmDtAcesso() != null
					&& doc.getDnmDtAcesso().after(this.getDnmDtAcesso()))
				return true;
		}
		return false;
	}

    public Set<ExDocumento> getDocumentoETodosOsPaisDasVias() {
        return getDocumentoETodosOsPaisDasVias(new HashSet<ExDocumento>());
    }

    public Set<ExDocumento> getDocumentoETodosOsPaisDasVias(Set<ExDocumento> docs) {
        if (!docs.contains(this)) {
            docs.add(this);
            docs.addAll(getTodosOsPaisDasVias(docs));
        }
        return docs;
    }

    public List<ExDocumento> getTodosOsPaisDasVias() {
        return getTodosOsPaisDasVias(new HashSet<ExDocumento>());
    }
        
    public List<ExDocumento> getTodosOsPaisDasVias(Set<ExDocumento> docs) {
		List<ExDocumento> pais = new ArrayList<ExDocumento>();
		if (!isExpediente())
			return pais;
		for (ExMobil mob : getExMobilSet()) {
			if (mob.isGeral())
				continue;
			ExMobil pai = mob.getExMobilPai();
			// impede loop infinito ao acessar documentos juntados a ele mesmo
			if (pai != null
					&& pai.getDoc().getIdDoc() != mob.getDoc().getIdDoc() && !docs.contains(pai.doc()))
				pais.addAll(pai.doc().getDocumentoETodosOsPaisDasVias(docs));
		}
		return pais;
	}
	
	public List<ExDocumento> getTodosOsPaisDasViasCossigsSubscritor() {
		List<ExDocumento> pais = new ArrayList<>();
		if (this.getExMobilPai() != null) {
			pais = this.getExMobilPai().getDoc().getTodosOsPaisDasVias();
			if (pais.isEmpty())
				pais.add(this.getExMobilPai().getDoc());
		} else {
			pais = this.getTodosOsPaisDasVias();
			if (pais.isEmpty())
				pais.add(this);
		}
		return pais;
	}
	
	public boolean paiPossuiMovsVinculacaoPapel(long codigoPapel){
		List<ExDocumento> viasDocPai = this.getTodosOsPaisDasViasCossigsSubscritor();
		if (viasDocPai.iterator().hasNext()) {
			List<ExMovimentacao> movs = viasDocPai.iterator().next().getMovsVinculacaoPapelGenerico(codigoPapel);
			for (ExMovimentacao mov : movs) {
				ExMobil docVia = mov.getExMobilRef();
				if(docVia.doc().getCodigo().equals(this.getCodigo()))
					return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	public boolean possuiMovsVinculacaoPapel(long codigoPapel){
		List<ExMovimentacao> movs = this.getMovsVinculacaoPapelGenerico(codigoPapel);
		for (ExMovimentacao mov : movs) {
			ExMobil docVia = mov.getExMobilRef();
			if(docVia.doc().getCodigo().equals(this.getCodigo()))
				return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public List<Object> getListaDeAcessos() {
		if (getDnmAcesso() == null || isDnmAcessoMAisAntigoQueODosPais()) {
			Ex.getInstance().getBL().atualizarDnmAcesso(this);
		}
		if (getExNivelAcessoAtual().getIdNivelAcesso().equals(
				ExNivelAcesso.NIVEL_ACESSO_PUBLICO)
				&& XjusUtils.ACESSO_PUBLICO.equals(getDnmAcesso()))
			return null;
		ExDao dao = ExDao.getInstance();
		List<Object> l = new ArrayList<Object>();
		String a[] = getDnmAcesso().split(",");

		for (String s : a) {
			if (s.equals(XjusUtils.ACESSO_PUBLICO))
				l.add(s);
			else if (s.startsWith("O"))
				l.add(dao.consultar(Long.parseLong(s.substring(1)),
						CpOrgaoUsuario.class, false));
			else if (s.startsWith("L"))
				l.add(dao.consultar(Long.parseLong(s.substring(1)),
						DpLotacao.class, false).getLotacaoAtual());
			else if (s.startsWith("P"))
				l.add(dao.consultar(Long.parseLong(s.substring(1)),
						DpPessoa.class, false).getPessoaAtual());
		}

		return l;
	}

	public String getListaDeAcessosString() {
		String s = "";
		List<Object> l = getListaDeAcessos();

		for (Object o : l) {
			if (s.length() > 0)
				s += ", ";
			if (XjusUtils.ACESSO_PUBLICO.equals(o))
				s += "Público";
			else if (o instanceof CpOrgaoUsuario)
				s += ((CpOrgaoUsuario) o).getSigla();
			else if (o instanceof DpLotacao)
				s += ((DpLotacao) o).getNomeLotacao()+ " - " +((DpLotacao) o).getSiglaCompleta() + "/" + ((DpLotacao) o).getOrgaoUsuario();
			else if (o instanceof DpPessoa)
				s += ((DpPessoa) o).getNomePessoa() + " - " + ((DpPessoa) o).getSiglaCompleta() + "/" + ((DpPessoa) o).getLotacao().getSigla();
			else
				s += o.toString();
		}

		return s;
	}

	public List<DpResponsavel> getResponsaveisPorPapel(ExPapel papel) {
		List<DpResponsavel> lista = new ArrayList<DpResponsavel>();
		List<ExMovimentacao> movs = getMobilGeral().getMovimentacoesPorTipo(
				ExTipoDeMovimentacao.VINCULACAO_PAPEL, true);
		for (ExMovimentacao mov : movs) {
			if (!papel.getIdPapel()
							.equals(mov.getExPapel().getIdPapel()))
				continue;
			if (mov.getSubscritor() != null)
				lista.add(mov.getSubscritor());
			else if (mov.getLotaSubscritor() != null)
				lista.add(mov.getLotaSubscritor());
		}
		return lista.size() == 0 ? null : lista;
	}
	
	public boolean possuiVinculPapelRevisorCossigsSubscritor(DpPessoa dpPessoa, ExMobil mobRefMov, long codigoPapel) {
		List<ExMovimentacao> movs = this.getMobilGeral()
				.getMovimentacoesPorTipo(ExTipoDeMovimentacao.VINCULACAO_PAPEL, Boolean.TRUE);
		for (ExMovimentacao mov : movs) {
			if (mov.getExPapel().getIdPapel().equals(codigoPapel)) { 
				if (dpPessoa != null) {
					if (mov.getSubscritor().equivale(dpPessoa) && mov.getExMobilRef().equals(mobRefMov))
						return Boolean.TRUE;
				} else {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}
	
	public List<ExMovimentacao> getMovsVinculacaoPapelGenerico(long codigoPapel) {
		List<ExMovimentacao> movsReturn = new ArrayList<>();
		ExMobil mobil = this.getMobilGeral();
		if (mobil != null) {
			List<ExMovimentacao> movs = mobil
					.getMovimentacoesPorTipo(ExTipoDeMovimentacao.VINCULACAO_PAPEL, Boolean.TRUE);
			for (ExMovimentacao mov : movs) {
				if (mov.getExPapel().getIdPapel().equals(codigoPapel)) { 
					movsReturn.add(mov);
				}
			}
		}
		return movsReturn;
	}

	@Override
	public boolean isInternoProduzido() {
		if (getExTipoDocumento() == null)
			return false;
		return (getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO);
	}

	public boolean temPerfil(DpPessoa titular, DpLotacao lotaTitular, long papel) {
		for (ExMovimentacao mov : getMobilGeral().getExMovimentacaoSet()) {
			if (!mov.isCancelada()
					&& mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.VINCULACAO_PAPEL
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
					&& mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.VINCULACAO_PAPEL) {
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
		return ExDao.getInstance().consultarDocsInclusosNoBoletim(this);
	}

	public ExDocumento getBoletimEmQueDocFoiPublicado() {
		if (isBoletimPublicado()) {
			ExBoletimDoc boletimDoc = ExDao.getInstance()
					.consultarBoletimEmQueODocumentoEstaIncluso(this);

			if (boletimDoc != null)
				return boletimDoc.getBoletim();
		}

		return null;
	}

	/**
	 * Retorna o conte￺do entre dois textos.
	 */
	public String getConteudoEntreTextos(String textoInicial, String textoFinal) {
		try {
			if (textoInicial == null || textoInicial.isEmpty()
					|| textoFinal == null || textoFinal.isEmpty())
				return "";

			String s = getConteudoBlobHtmlString();

			if (!s.contains(textoInicial) || !s.contains(textoFinal))
				return "";

			return Texto.extrai(s, textoInicial, textoFinal).trim();
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public Date getHisDtAlt() {
		return getDtAltDoc();
	}

	@Override
	public void setHisDtAlt(Date hisDtAlt) {
		setDtAltDoc(hisDtAlt);
	}

	@Override
	public String getTipoDescr() {
		return getExFormaDocumento().getDescricao();
		// switch
		// (getExFormaDocumento().getExTipoFormaDoc().getIdTipoFormaDoc().intValue())
		// {
		// case (int) ExTipoFormaDoc.TIPO_FORMA_DOC_EXPEDIENTE:
		// return "Expediente";
		// }
		// return "Outro";
	}
	
	/**
	 * Verifica se um documento já foi transferido alguma vez
	 */
	public boolean jaTransferido() {
		for (ExMovimentacao mov : getExMovimentacaoSet()) {
			if (!mov.isCancelada()
					&& (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO_INTERNO_TRANSFERENCIA
							|| mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA
							|| mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA
							|| mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.TRANSFERENCIA 
							|| mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.TRAMITE_PARALELO 
							|| mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.NOTIFICACAO
                            || mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.TRANSFERENCIA_EXTERNA
                            
                            // Nato: O desentranhamento é análogo à um recebimento, por isso é contemplado aqui.
                            || mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA))
				return true;
		}

		return false;
	}

	public boolean isAssinaturaSolicitada() {
		ExMovimentacao m = getMovSolicitacaoDeAssinatura();
		return m != null;
	}

	public ExMovimentacao getMovSolicitacaoDeAssinatura() {
		final Set<ExMovimentacao> movs = getMobilGeral().getExMovimentacaoSet();

		if(movs != null)
			for (final ExMovimentacao mov : movs) {
				if (!mov.isCancelada()
						&& mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.SOLICITACAO_DE_ASSINATURA)
					return mov;
			}
		return null;
	}
	
	public boolean isDocFilhoJuntadoAoPai() {
		if (getExMobilPai() != null) {			
			for (ExMobil mob : getExMobilPai().getJuntados()) {					
				if (getIdDoc() == mob.doc().getIdDoc()) {
					return true;
				}									
			}			
		}	
		return false;
	}
	
	public void setListaMovimentacaoPorRestricaoAcesso(List<ExMovimentacao> listaMovs) {
		this.listaMovimentacaoPorRestricaoAcesso = listaMovs;
	}
	
	public List<ExMovimentacao> getListaMovimentacaoPorRestricaoAcesso() {
		return this.listaMovimentacaoPorRestricaoAcesso;
	}			
		
	public boolean podeReordenar() {		
		if (podeReordenar == null) 
			podeReordenar = Prop.getBool("reordenacao.ativo");
		if (podeReordenar == null)
			return false;
			
		return podeReordenar;
	}
	
	public void setPodeExibirReordenacao(boolean podeExibirReordenacao) {
		this.podeExibirReordenacao = podeExibirReordenacao;
	}
	
	public boolean podeExibirReordenacao() {
		return this.podeExibirReordenacao;
	}
	
	public void setIdDocPrincipal(Long idDocPrincipal) {
		this.idDocPrincipal = idDocPrincipal;
	}
	
	public Long getIdDocPrincipal() {
		if (this.idDocPrincipal == null) {
			return this.getIdDoc();
		}
		
		return this.idDocPrincipal;
	}

	/**
	 * Verifica se o documento contém um determinado mobil 
	 */
	public boolean contemMobil(ExMobil mob) {
		for (ExMobil m : getExMobilSet()) {
			if (m.equals(mob))
				return true;
		}
		return false;
	}

	/**
	 * Retorna se o móbil possui acompanhamento de protocolo gerado.
	 * 
	 * @return
	 */
	public boolean temAcompanhamentoDeProtocolo() {
		boolean b = false;
		for (ExMovimentacao movRef : getExMovimentacaoSet()) {
			if (!movRef.isCancelada()
				&& movRef.getExTipoMovimentacao() == ExTipoDeMovimentacao.GERAR_PROTOCOLO)
					b = true;
		}
		return b;
	}

	public List<Long> getIdsDeAssinantes() {
		List<Long> l = new ArrayList<>();
		for (DpPessoa subscritor : getSubscritorECosignatarios()) {
			if (isAssinadoPelaPessoaComTokenOuSenha(subscritor)) 
				l.add(subscritor.getId());
		}
		return l;
	}

	public boolean isDescricaoEspecieDespacho() {
		return this.getExFormaDocumento()
				.getDescricao().contains("Despacho");
	}

	/**
	 * Verifica se o prazo para todos assinarem o documento está vencido.
	 */
	public boolean isPrazoDeAssinaturaVencido() {
		Date dtNow = ExDao.getInstance().consultarDataEHoraDoServidor();
		ExMovimentacao mov = getMovPrazoDeAssinatura();

		if (mov != null && mov.getDtParam1().before(dtNow)) 
			return true;
		return false;
	}

	/**
	 * Obtem a movimentação de definição de prazo de assinatura.
	 */
	public ExMovimentacao getMovPrazoDeAssinatura() {
		final Set<ExMovimentacao> movs = getMobilGeral().getExMovimentacaoSet();

		if(movs != null)
			for (final ExMovimentacao mov : movs) {
				if (!mov.isCancelada()
						&& mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.PRAZO_ASSINATURA)
					return mov;
			}
		return null;
	}
	
	/**
	 * Retorna a data do prazo de assinatura no formato dd/mm/aaaa hh:mm.
	 */
	public Date getDtPrazoDeAssinatura() {
		ExMovimentacao mov = getMovPrazoDeAssinatura();
		if (mov != null) {
			return new Date(mov.getDtParam1().getTime());
		}
		return null;
	}
	/**
	 * Retorna a data do prazo de assinatura no formato dd/mm/aaaa hh:mm.
	 */
	public String getDtPrazoDeAssinaturaDDMMYYYYHHMM() {
		Date dt = getDtPrazoDeAssinatura();
		if (dt != null) {
	        LocalDateTime localDateTime = LocalDateTime.ofInstant(dt.toInstant(), ZoneId.systemDefault());
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			return localDateTime.format(fmt);
		}
		return "";
	}

	public String getNumeroSequenciaGenerica() {
		return numeroSequenciaGenerica;
	}

	public void setNumeroSequenciaGenerica(String numeroSequenciaGenerica) {
		this.numeroSequenciaGenerica = numeroSequenciaGenerica;
	}

	public String getCodigoUnico() {
		return codigoUnico;
	}

	public void setCodigoUnico(String codigoUnico) {
		this.codigoUnico = codigoUnico;
	}

	public String getDigitoVerificadorCodigoUnico() {
		return digitoVerificadorCodigoUnico;
	}

	public void setDigitoVerificadorCodigoUnico(String digitoVerificadorCodigoUnico) {
		this.digitoVerificadorCodigoUnico = digitoVerificadorCodigoUnico;
	}

	public ExRef getRef() {
		return new ExRef(this);
	}

	public String fragmento(String nome) {
		return Texto.extrai(getHtml(), "<!-- fragmento:" + nome + " -->", "<!-- /fragmento:" + nome + " -->");
	}
	
	public List<AssinanteVO> getListaAssinantesOrdenados() {
		List<AssinanteVO> listaOrdenada = new ArrayList<AssinanteVO>();
		ExMovimentacao mov = this.getMobilGeral().getUltimaMovimentacaoNaoCancelada(ExTipoDeMovimentacao.ORDEM_ASSINATURA);
		if(mov != null) {
			String ordem = mov.getDescrMov();

			List<String> listaMatricula = new ArrayList<>();
			listaMatricula.addAll(Arrays.asList(ordem.split(";")));
			for (String matricula : listaMatricula) {
				for (ExMovimentacao movCossig : this.getMobilGeral().getMovimentacoesPorTipo(ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO, true)) {
					if(matricula.equals(movCossig.getSubscritor().getSigla())) {
						listaOrdenada.add(new AssinanteVO(movCossig.getSubscritor(), movCossig.getTitular(), movCossig.getNmFuncao(), movCossig.getNmLotacao(), movCossig.getNmSubscritor()));
					}
				}
				if(matricula.equals(getSubscritor().getSigla())) {
					AssinanteVO pesVO = new AssinanteVO(this.getSubscritor(), this.getTitular(), this.getNmFuncao(), this.getNmLotacao(), this.getNmSubscritor());

					listaOrdenada.add(pesVO);
				}

			}
		} else {
			if (this.getSubscritor() != null && this.getTitular() != null) {
				listaOrdenada.add(new AssinanteVO(this.getSubscritor(), this.getTitular(), this.getNmFuncao(), this.getNmLotacao(), this.getNmSubscritor()));
			}
			
			for (ExMovimentacao movCossig : this.getMobilGeral().getMovimentacoesPorTipo(ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO, true)) {
				listaOrdenada.add(new AssinanteVO(movCossig.getSubscritor(), movCossig.getTitular(), movCossig.getNmFuncao(), movCossig.getNmLotacao(), movCossig.getNmSubscritor()));
			}
		}
		return listaOrdenada;
	}

    public void setConteudoBlobDoc(byte[] createBlob) {
        if(createBlob != null)
            setCpArquivo(CpArquivo.updateConteudo(getCpArquivo(), createBlob, getCodigoCompacto(), isFinalizado() ? ArmazenamentoTemporalidadeEnum.MANTER_POR_30_ANOS : ArmazenamentoTemporalidadeEnum.TEMPORARIO));
    }

}
