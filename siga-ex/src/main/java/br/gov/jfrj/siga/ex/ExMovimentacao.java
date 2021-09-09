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
 * Created Mon Nov 14 13:30:45 GMT-03:00 2005.
 */
package br.gov.jfrj.siga.ex;

import static br.gov.jfrj.siga.ex.ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO;
import static java.util.Objects.nonNull;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.auth0.jwt.JWTVerifier;
import com.crivano.jlogic.Expression;
import com.crivano.swaggerservlet.SwaggerUtils;

import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.bluc.service.BlucService;
import br.gov.jfrj.siga.bluc.service.ValidateRequest;
import br.gov.jfrj.siga.bluc.service.ValidateResponse;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelarMarcacao;
import br.gov.jfrj.siga.ex.util.Compactador;
import br.gov.jfrj.siga.ex.util.DatasPublicacaoDJE;
import br.gov.jfrj.siga.ex.util.ProcessadorHtml;
import br.gov.jfrj.siga.ex.util.ProcessadorReferencias;
import br.gov.jfrj.siga.ex.util.PublicacaoDJEBL;

/**
 * A class that represents a row in the 'EX_MOVIMENTACAO' table. This class may
 * be customized as it is never re-generated after being created.
 */

@SuppressWarnings("serial")
@Entity
@BatchSize(size = 500)
@Table(name = "siga.ex_movimentacao")
public class ExMovimentacao extends AbstractExMovimentacao implements
		Serializable, Comparable<ExMovimentacao> {

	/**
	 * Simple constructor of ExMovimentacao instances.
	 */
	public ExMovimentacao() {
	}

	/**
	 * Constructor of ExMovimentacao instances given a simple primary key.
	 * 
	 * @param idMov
	 */
	public ExMovimentacao(final java.lang.Long idMov) {
		super(idMov);
	}

	@Override
	public Long getIdMov() {
		return super.getIdMov();
	}

	public String getConteudoBlobString() throws UnsupportedEncodingException {
		return new String(getConteudoBlobMov2(), "ISO-8859-1");
	}

	public String getConteudoBlobPdfB64() {
		return Base64.getEncoder().encodeToString(getConteudoBlobpdf());
	}

	/* Add customized code below */

	public String getDescrTipoMovimentacao() {
		String s = getExTipoMovimentacao().getSigla();
		if (getCadastrante() == null || getSubscritor() == null)
			return s;
		if (!getSubscritor().getId().equals(getCadastrante().getId())
			&& !SigaMessages.isSigaSP())
			s = s + " de Ordem";
		if (getExMovimentacaoCanceladora() != null)
			s = s + " (Cancelada)";
		return s;
	}

	public byte[] getConteudoBlobMov2() {
		return getConteudoBlobMov();

	}

	public String getLotaPublicacao() {
		Map<String, String> atributosXML = new HashMap<String, String>();
		try {
			String xmlString = this.getConteudoXmlString("boletimadm");
			if (xmlString != null) {
				atributosXML = PublicacaoDJEBL.lerXMLPublicacao(xmlString);
				return atributosXML.get("UNIDADE");
			}
			return PublicacaoDJEBL.obterUnidadeDocumento(this.getExDocumento());
		} catch (Exception e) {
			return "Erro na leitura do arquivo XML (lotação de publicação)";

		}

	}

	public String getDescrPublicacao() {
		Map<String, String> atributosXML = new HashMap<String, String>();
		try {
			String xmlString = this.getConteudoXmlString("boletimadm");
			if (xmlString != null) {
				atributosXML = PublicacaoDJEBL.lerXMLPublicacao(this
						.getConteudoXmlString("boletimadm"));
				return atributosXML.get("DESCREXPEDIENTE");
			}
			return this.getExDocumento().getDescrDocumento();
		} catch (Exception e) {
			return "Erro na leitura do arquivo XML (descrição de publicação)";
		}

	}

	public Long getIdTpMov() {
		return getExTipoMovimentacao().getIdTpMov();
	}

	public void setConteudoBlobMov2(byte[] blob) {
		if (blob != null)
			setConteudoBlobMov(blob);
	}

	/**
	 * Retorna a data da movimentação no formato dd/mm/aa, por exemplo,
	 * 01/02/10.
	 * 
	 * @return Data da movimentação no formato dd/mm/aa, por exemplo, 01/02/10.
	 * 
	 */
	public String getDtMovDDMMYY() {
		if (getDtMov() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			return df.format(getDtMov());
		}
		return "";
	}

	public String getDtMovDDMMYYYY() {
		if (getDtMov() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(getDtMov());
		}
		return "";
	}

	public String getDtMovYYYYMMDD() {
		if (getDtMov() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			return df.format(getDtMov());
		}
		return "";
	}

	/**
	 * Retorna a data de início da movimentação no formato dd/mm/aa, por
	 * exemplo, 01/02/10.
	 * 
	 * @return Data de início da movimentação no formato dd/mm/aa, por exemplo,
	 *         01/02/10.
	 * 
	 */
	public String getDtRegMovDDMMYY() {
		if (getDtIniMov() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			return df.format(getDtIniMov());
		}
		return "";
	}

	/**
	 * Retorna a data de início da movimentação no formato dd/mm/aa HH:MI:SS,
	 * por exemplo, 01/02/10 14:10:00.
	 * 
	 * @return Data de início da movimentação no formato dd/mm/aa HH:MI:SS, por
	 *         exemplo, 01/02/10 14:10:00.
	 * 
	 */
	public String getDtRegMovDDMMYYHHMMSS() {
		if (getDtIniMov() != null) {
			final SimpleDateFormat df = new SimpleDateFormat(
					"dd/MM/yy HH:mm:ss");
			return df.format(getDtIniMov());
		}
		return "";
	}

	/**
	 * Retorna a data de início da movimentação no formato dd/mm/aaaa HH:MI:SS,
	 * por exemplo, 01/02/2010 14:10:00.
	 * 
	 * @return Data de início da movimentação no formato dd/mm/aaaa HH:MI:SS,
	 *         por exemplo, 01/02/2010 14:10:00.
	 * 
	 */
	public String getDtRegMovDDMMYYYYHHMMSS() {
		if (getDtIniMov() != null) {
			final SimpleDateFormat df = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm:ss");
			return df.format(getDtIniMov());
		}
		return "";
	}

	/**
	 * Retorna a data de retorno da movimentação no formato dd/mm/aa, por
	 * exemplo, 01/02/10.
	 * 
	 * @return Data de retorno da movimentação no formato dd/mm/aa, por exemplo,
	 *         01/02/10.
	 * 
	 */
	public String getDtFimMovDDMMYY() {
		if (getDtFimMov() != null) {
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			return df.format(getDtFimMov());
		}
		return "";
	}

	/**
	 * Retorna a data de retorno da movimentação no formato dd/mm/aa HH:MI:SS,
	 * por exemplo, 01/02/10 14:10:00.
	 * 
	 * @return Data de retorno da movimentação no formato dd/mm/aa HH:MI:SS, por
	 *         exemplo, 01/02/10 14:10:00.
	 * 
	 */
	public String getDtFimMovDDMMYYHHMMSS() {
		if (getDtFimMov() != null) {
			final SimpleDateFormat df = new SimpleDateFormat(
					"dd/MM/yy HH:mm:ss");
			return df.format(getDtFimMov());
		}
		return "";
	}

	/**
	 * Retorna a data da movimentação por extenso. no formato "Rio de Janeiro,
	 * 01 de fevereiro de 2010", por exemplo.
	 * 
	 * @return Data da movimentação por extenso. no formato "Rio de Janeiro, 01
	 *         de fevereiro de 2010", por exemplo.
	 */
	public String getDtExtenso() {
        SimpleDateFormat df1 = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy.", new Locale("pt", "BR"));
		try {
			String s = getNmLocalidade();

			DpLotacao lotaBase = null;
			if (getLotaTitular() != null)
				lotaBase = getLotaTitular();
			else if (getLotaSubscritor() != null)
				lotaBase = getLotaSubscritor();
			else if (getLotaCadastrante() != null)
				lotaBase = getLotaCadastrante();

			String parteLocalidade = nonNull(s) ? (s + ", ")
					: (nonNull(lotaBase) && nonNull(lotaBase.getLocalidadeString()))
							? (lotaBase.getLocalidadeString() + ", ")
							: "";

			return parteLocalidade + df1.format(getDtMov()).toLowerCase();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Retorna verdadeiro se a diferença entre a data de disponibilização no DJE
	 * e a data atual for igual a 2 e falso caso contrário.
	 * 
	 * @return Verdadeiro se a diferença entre a data de disponibilização no DJE
	 *         e a data atual for igual a 2 e falso caso contrário.
	 */
	public boolean isARemeterHojeDJE() {
		try {
			DatasPublicacaoDJE d = new DatasPublicacaoDJE(getDtDispPublicacao());
			return d.isDisponibilizacaoDMais2();
		} catch (AplicacaoException ae) {
			return false;
		}
	}

	/**
	 * Retorna a descrição da movimentação.
	 * 
	 * @return Descrição da movimentação.
	 */
	@Override
	public String getDescrMov() {
		// TODO Auto-generated method stub
		return super.getDescrMov();
	}

	/**
	 * Retorna informações da movimentação como Nome do Órgão Externo,
	 * Observação do Órgão, Descrição do Tipo de Movimentação e Descrição da
	 * Movimentação.
	 * 
	 * @return Informações da movimentação como Nome do Órgão Externo,
	 *         Observação do Órgão, Descrição do Tipo de Movimentação e
	 *         Descrição da Movimentação.
	 */
	public String getObs() {
		String s = "";
		if (getOrgaoExterno() != null)
			s = s + getOrgaoExterno().getNmOrgao();

		if (getObsOrgao() != null) {
			if (s.length() > 0)
				s = s + "; ";

			s = s + getObsOrgao().trim();

			final String provObs = getObsOrgao().trim();
			if (!provObs.endsWith(".") && !provObs.endsWith("!")
					&& !provObs.endsWith("?"))
				s = s + ". ";
			else
				s = s + " ";
		}

		if (getExTipoDespacho() != null)
			s = s + getExTipoDespacho().getDescTpDespacho();

		if (getDescrMov() != null) {
			s = s + getDescrMov();
		}

		return s;
	}

	/**
	 * Retorna o número de sequência da via como um inteiro.
	 * 
	 * @return Número de sequência como um inteiro se for uma via e 0 caso
	 *         contrário.
	 */
	public int getNumVia2() {
		return getExMobil().isVia() ? getExMobil().getNumSequencia().intValue()
				: 0;
	}

	/**
	 * Retorna o número de sequência da via como uma String.
	 * 
	 * @return Número de sequência como uma String se for uma via e "" caso
	 *         contrário.
	 */
	public String getNumViaString() {
		if (getNumVia2() == 0)
			return "";
		return String.valueOf(getNumVia2());
	}

	public static Integer tpMovDesempatePosicao(Long idTpMov) {
		final List<Long> tpMovDesempate = Arrays.asList(new Long[] {ExTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA, ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO});

		if (idTpMov == null)
			return Integer.MAX_VALUE;
		
		int i = tpMovDesempate.indexOf(idTpMov);
		if (i == -1)
			return Integer.MAX_VALUE;
		return i;
	}

	public int compareTo(final ExMovimentacao mov) {
		try {
			int i = 0;
			if (this.getDtTimestamp() != null) {
				i = this.getDtTimestamp().compareTo(mov.getDtTimestamp());
			} else if(this.getDtIniMov() != null) {
				i = this.getDtIniMov().compareTo(mov.getDtIniMov());
			}	
			
			if (i != 0)
				return i;
			
			if (getExTipoMovimentacao() != null && mov.getExTipoMovimentacao() != null) {
				i = tpMovDesempatePosicao(getExTipoMovimentacao().getId()).compareTo(
						tpMovDesempatePosicao(mov.getExTipoMovimentacao().getId()));
				if (i != 0)
					return i;
			}
			
			i = getIdMov().compareTo(mov.getIdMov());
			return i;
		} catch (final Exception ex) {
			return 0;
		}
	}

	/**
	 * Retorna o nome do responsável pela movimentação.
	 * 
	 * @return Nome do responsável pela movimentação.
	 */
	public String getRespString() {
		if (getOrgaoExterno() != null)
			return getObs();
		else {
			String strReturn = "";
			if (getLotaResp() != null)
				strReturn = getLotaResp().getDescricao();
			if (getResp() != null)
				strReturn = strReturn + " - " + getResp().getDescricao();
			return strReturn;
		}
	}
	
	/**
	 * Retorna o nome do responsável pela movimentação sem a descricao.
	 * 
	 * @return Nome do responsável pela movimentação sem a descricao.
	 */
	public String getRespSemDescrString() {
		if (getOrgaoExterno() != null) {
			return getOrgaoExterno().getNmOrgao();
		} else {
			String strReturn = "";
			if (getLotaResp() != null)
				strReturn = getLotaResp().getDescricao();
			if (getResp() != null)
				strReturn = strReturn + " - " + getResp().getDescricao();
			return strReturn;
		}
	}

	/**
	 * Retorna Descrição da Movimentação
	 * 
	 * @return Descrição da Movimentação
	 */
	public String getRespDescrString() {
		if (getObsOrgao() != null) {
			return getObsOrgao().trim();
		}
		return "";
	}
	
	/**
	 * Retorna o nome do responsável pela movimentação.
	 * 
	 * @return Nome do responsável pela movimentação.
	 */
	public String getCadastranteString() {
		String strReturn = "";
		if (getLotaResp() != null)
			strReturn = getLotaResp().getDescricao();
		if (getResp() != null)
			strReturn = strReturn + " - " + getResp().getDescricao();
		return strReturn;
	}

	public String getConteudoBlobHtmlB64() {
		return Base64.getEncoder().encodeToString(getConteudoBlobHtml());
	}

	public void setConteudoBlobHtml(final byte[] conteudo) {
		setConteudoBlob("doc.htm", conteudo);
	}

	public void setConteudoBlobPdf(final byte[] conteudo) {
		setConteudoBlob("doc.pdf", conteudo);
	}

	public void setConteudoBlobForm(final byte[] conteudo) {
		setConteudoBlob("doc.form", conteudo);
	}

	public void setConteudoBlobXML(final String nome, final byte[] conteudo) {
		setConteudoBlob(nome + ".xml", conteudo);
	}

	public void setConteudoBlobRTF(final String nome, final byte[] conteudo) {
		setConteudoBlob(nome + ".rtf", conteudo);
	}

	public void setConteudoBlob(final String nome, final byte[] conteudo) {
		final Compactador zip = new Compactador();
		final byte[] arqZip = getConteudoBlobMov2();
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
		setConteudoBlobMov2(conteudoZip);
	}

	public byte[] getConteudoBlobHtml() {
		return getConteudoBlob("doc.htm");
	}

	public String getConteudoBlobHtmlString() {
		if (getConteudoBlobHtml() == null) {
			return null;
		}
		try {
			return new String(getConteudoBlobHtml(), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			return new String(getConteudoBlobHtml());
		}
	}

	public byte[] getConteudoBlobPdfNecessario() {
		if (getConteudoBlobHtml() == null)
			return getConteudoBlobpdf();
		return null;
	}

	/**
	 * Retorna o documento relacionado a movimentação.
	 * 
	 * @return Documento relacionado a movimentação.
	 */
	public ExDocumento getExDocumento() {
		return super.getExMobil().getExDocumento();
	}

	public byte[] getConteudoBlobpdf() {
		try {
			if (getConteudoTpMov().equals("application/zip"))
				return getConteudoBlob("doc.pdf");
			if (getConteudoTpMov().equals("application/pdf"))
				return getConteudoBlobMov2();
		} catch (Exception ex) {
			return null;
		}
		return null;
	}

	public byte[] getConteudoBlobForm() {
		return getConteudoBlob("doc.form");
	}

	public byte[] getConteudoBlobXML() {
		return getConteudoBlobXML("doc.xml");
	}

	public byte[] getConteudoBlobXML(String nome) {
		if (!nome.contains(".xml"))
			nome += ".xml";
		return getConteudoBlob(nome);
	}

	public String getConteudoXmlString(String nome)
			throws UnsupportedEncodingException {

		byte[] xmlByte = this.getConteudoBlobXML(nome);
		if (xmlByte != null)
			return new String(xmlByte, "ISO-8859-1");

		return null;
	}

	public byte[] getConteudoBlobRTF() {
		return getConteudoBlob("doc.rtf");
	}

	public byte[] getConteudoBlob(final String nome) {
		final byte[] conteudoZip = getConteudoBlobMov2();
		byte[] conteudo = null;
		final Compactador zip = new Compactador();
		if (conteudoZip != null) {
			conteudo = zip.descompactarStream(conteudoZip, nome);
		}
		return conteudo;
	}

	public void setConteudoBlobHtmlString(final String s) throws Exception {
		final String sHtml = (new ProcessadorHtml()).canonicalizarHtml(s,
				false, true, false, false, false);
		setConteudoBlob("doc.htm", sHtml.getBytes("ISO-8859-1"));
	}

	/**
	 * Retorna o nome da Função do Subscritor da Movimentação.
	 * 
	 * @return Nome da Função do Subscritor da Movimentação.
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
	 * Retorna o nome do arquivo anexado a movimentação.
	 * 
	 * @return Nome do arquivo anexado a movimentação.
	 */
	public String getNmArqMov() {
		String s = super.getNmArqMov();

		if (s != null) {
			s = s.trim();
			if (s.length() == 0)
				return null;
		}
		return s;
	}

	/**
	 * Retorna o nome do arquivo anexado a movimentação sem extensão.
	 * 
	 * @return Nome do arquivo anexado a movimentação sem extensão.
	 */
	public String getNmArqMovSemExtensao() {
		String s = super.getNmArqMov();

		if (s != null) {
			s = s.trim();
			if (s.length() == 0)
				return null;

			try {
				return s.split("\\.")[0];
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		return s;
	}

	/**
	 * Retorna o nome da lotação do subscritor da movimentação.
	 * 
	 * @return Nome da lotação do subscritor da movimentação.
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
	 * Retorna o nome da localidade da lotação do subscritor da movimentação.
	 * 
	 * @return Nome da localidade da lotação do subscritor da movimentação.
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
	 * Retorna o nome do subscritor da movimentação.
	 * 
	 * @return Nome do subscritor da movimentação.
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
	 * Retorna o código da movimentação de referência da movimentação Atual.
	 * 
	 * @return Código da movimentação de referência da movimentação Atual.
	 */
	public java.lang.String getReferencia() {
		return getExMobil().getCodigoCompacto() + ":" + getIdMov();
		/*
		 * este atributo é utilizado p/ compor nmPdf (abaixo), não retirar o
		 * caracter ":" /* pois este é utilizado no método
		 * ExMovimentacaoAction.recuperarAssinaturaAppletB64()
		 */
	}

	public java.lang.String getNmPdf() {
		return getReferencia() + ".pdf";
	}

	// public String getNumViaToChar() {
	// if (getNumVia2() == 0)
	// return "";
	//
	// return "" + Character.toChars(getNumVia2() + 64)[0];
	// }
	//
	// public String getNumViaDocPaiToChar() {
	// return ""
	// + Character.toChars(getExMobilPai().getNumSequencia()
	// .intValue() + 64)[0];
	// }
	//
	// public String getNumViaDocRefToChar() {
	// return ""
	// + Character.toChars(getExMobilRef().getNumSequencia()
	// .intValue() + 64)[0];
	// }

	@Override
	public String getHtml() {
		return getConteudoBlobHtmlString();
	}

	@Override
	public String getHtmlComReferencias() throws Exception {
		return getConteudoBlobHtmlStringComReferencias();
	}

	private String getConteudoBlobHtmlStringComReferencias() throws Exception {
		String sHtml = getConteudoBlobHtmlString();
		ProcessadorReferencias pr = new ProcessadorReferencias();
		pr.ignorar(this.getExMobil().getExDocumento().getSigla());
		pr.ignorar(this.getExMobil().getSigla());
		sHtml = pr.marcarReferencias(sHtml);
		return sHtml;
	}

	@Override
	public byte[] getPdf() {
		return getConteudoBlobpdf();
	}

	@Override
	public boolean isPdf() {
		return (getNumPaginas() != null && getNumPaginas() > 0)
				|| (getPdf() != null);
	}

	/**
	 * Retorna a data da movimentação.
	 * 
	 * @return Data da movimentação.
	 */
	@Override
	public Date getData() {
		return getDtMov();
	}

	/**
	 * verifica se uma movimentação está cancelada. Uma movimentação está
	 * cancelada quando o seu atributo movimentacaoCanceladora está preenchido
	 * com um código de movimentação de cancelamento.
	 * 
	 * @return Verdadeiro se a movimentação está cancelada e Falso caso
	 *         contrário.
	 */
	public boolean isCancelada() {
		return getExMovimentacaoCanceladora() != null;
	}

	/**
	 * verifica se uma movimentação tem referência a alguma outra.
	 * 
	 * @return Verdadeiro se a movimentação está cancelada e Falso caso
	 *         contrário.
	 */
	public boolean isReferenciando() {
		return getExMovimentacaoRef() != null;
	}

	/**
	 * verifica se uma movimentação é canceladora, ou seja, se é do tipo
	 * Cancelamento de Movimentação.
	 * 
	 * @return Verdadeiro ou Falso.
	 */
	public boolean isCanceladora() {
		return getExTipoMovimentacao() != null
				&& getExTipoMovimentacao()
						.getIdTpMov()
						.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO);
	}

	/**
	 * verifica se uma movimentação de anexação de arquivo está assinada e não
	 * está cancelada. Este tipo de movimentação está assinada quando existe
	 * alguma movimentação de assinatura de movimentação com o seu atributo
	 * movimentacaoReferenciadora igual ao código da movimentação de anexação de
	 * arquivo.
	 * 
	 * @return Verdadeiro se a movimentação está assinada e Falso caso
	 *         contrário.
	 */
	public boolean isAssinada() {
		if (this.isCancelada()
				|| this.getExMobil().getExMovimentacaoSet() == null)
			return false;

		// Usamos getExMovimentacaoSet() em vez de
		// getExMovimentacaoReferenciadoraSet() porque o segundo faz lazy
		// initialization e não recebe as movimentações mais recentes quando se
		// está calculando os marcadores.
		for (ExMovimentacao assinatura : this.getExMobil()
				.getExMovimentacaoSet()) {
			long l = assinatura.getIdTpMov();
			if (l != ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO
					&& l != ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO
					&& l != ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA
					&& l != ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA)
				continue;
			if (assinatura.getExMovimentacaoRef() == null)
				continue;
			if (this.getIdMov().equals(
					assinatura.getExMovimentacaoRef().getIdMov()))
				return true;
		}

		return false;
	}

	/**
	 * Uma movimentação está autenticado quando ela possui pelo menos uma
	 * assinatura com senha.
	 */
	public boolean isAutenticada() {
		if (!this.isCancelada()
				&& this.getExMovimentacaoReferenciadoraSet() != null) {
			for (ExMovimentacao movRef : this
					.getExMovimentacaoReferenciadoraSet()) {
				if (movRef.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO
						|| movRef.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO)
					return true;
			}
		}

		return false;
	}

	/**
	 * Retorna se uma movimentação possui assinaturas com senha.
	 */
	public boolean temAssinaturasComSenha() {
		if (getApenasAssinaturasComSenha() != null
				&& getApenasAssinaturasComSenha().size() > 0)
			return true;

		return false;
	}

	public String getSiglaAssinatura() {
		return getExDocumento().getIdDoc()
				+ "."
				+ getIdMov()
				+ "-"
				+ Math.abs((getExDocumento().getDescrCurta() + getIdMov())
						.hashCode() % 10000);
	}

	public String getSiglaAssinaturaExterna() {
		return getExDocumento().getIdDoc()
				+ "."
				+ getIdMov()
				+ "-"
				+ Math.abs((getExDocumento().getDescrCurta() + getIdMov() + "AssinaturaExterna")
						.hashCode() % 10000);
	}

	public Set<ExMovimentacao> getAssinaturasDigitais() {
		TreeSet<ExMovimentacao> movs = new TreeSet<ExMovimentacao>();
		movs.addAll(getApenasAssinaturas());
		movs.addAll(getApenasConferenciasCopia());
		return movs;
	}

	/**
	 * Retorna uma coleção de movimentações dos tipo:
	 * ASSINATURA_DIGITAL_MOVIMENTACAO.
	 * 
	 * @return Coleção de movimentações de assinaturas digitais.
	 */
	public Set<ExMovimentacao> getApenasAssinaturas() {
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();

		for (ExMovimentacao m : getExMovimentacaoReferenciadoraSet()) {
			if ((m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO || m
					.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA)
					&& m.getExMovimentacaoCanceladora() == null) {
				set.add(m);
			}
		}
		return set;
	}

	/**
	 * Retorna uma coleção de movimentações dos tipo:
	 * ASSINATURA_DIGITAL_MOVIMENTACAO.
	 * 
	 * @return Coleção de movimentações de assinaturas com Token.
	 */
	public Set<ExMovimentacao> getApenasAssinaturasComToken() {
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();

		for (ExMovimentacao m : getExMovimentacaoReferenciadoraSet()) {
			if ((m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO)
					&& m.getExMovimentacaoCanceladora() == null) {
				set.add(m);
			}
		}
		return set;
	}

	/**
	 * Retorna uma coleção de movimentações dos tipo:
	 * ASSINATURA_DIGITAL_MOVIMENTACAO.
	 * 
	 * @return Coleção de movimentações de assinaturas com Senha.
	 */
	public Set<ExMovimentacao> getApenasAssinaturasComSenha() {
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();

		for (ExMovimentacao m : getExMovimentacaoReferenciadoraSet()) {
			if ((m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA)
					&& m.getExMovimentacaoCanceladora() == null) {
				set.add(m);
			}
		}
		return set;
	}

	/**
	 * Retorna uma coleção de movimentações dos tipo
	 * CONFERENCIA_COPIA_DOCUMENTO.
	 * 
	 * @return Coleção de movimentações de conferências de cópia.
	 */
	public Set<ExMovimentacao> getApenasConferenciasCopia() {
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();

		for (ExMovimentacao m : getExMovimentacaoReferenciadoraSet()) {
			if ((m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO || m
					.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA)
					&& m.getExMovimentacaoCanceladora() == null) {
				set.add(m);
			}
		}
		return set;
	}

	/**
	 * Retorna uma coleção de movimentações dos tipo
	 * CONFERENCIA_COPIA_DOCUMENTO.
	 * 
	 * @return Coleção de movimentações de conferências de cópia com token.
	 */
	public Set<ExMovimentacao> getApenasConferenciasCopiaComToken() {
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();

		for (ExMovimentacao m : getExMovimentacaoReferenciadoraSet()) {
			if ((m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO)
					&& m.getExMovimentacaoCanceladora() == null) {
				set.add(m);
			}
		}
		return set;
	}

	/**
	 * Retorna uma coleção de movimentações dos tipo
	 * CONFERENCIA_COPIA_DOCUMENTO.
	 * 
	 * @return Coleção de movimentações de conferências de cópia com senha.
	 */
	public Set<ExMovimentacao> getApenasConferenciasCopiaComSenha() {
		Set<ExMovimentacao> set = new TreeSet<ExMovimentacao>();

		for (ExMovimentacao m : getExMovimentacaoReferenciadoraSet()) {
			if ((m.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA)
					&& m.getExMovimentacaoCanceladora() == null) {
				set.add(m);
			}
		}
		return set;
	}

	public String getAssinantesComTokenString(Date dtMov) {
		return Documento.getAssinantesString(getApenasAssinaturasComToken(),dtMov);
	}

	public String getAssinantesComSenhaString(Date dtMov) {
		return Documento.getAssinantesString(getApenasAssinaturasComSenha(),dtMov);
	}

	public String getConferentesString(Date dtMov) {
		return Documento.getAssinantesString(getApenasConferenciasCopia(),dtMov);
	}

	public String getAssinantesCompleto() {
		String conferentes = getConferentesString(getData());
		String assinantesToken = getAssinantesComTokenString(getData());
		String assinantesSenha = getAssinantesComSenhaString(getData());
		String retorno = "";
		retorno += assinantesToken.length() > 0 ? "Assinado digitalmente por "
				+ assinantesToken + ".\n" : "";
		retorno += assinantesSenha.length() > 0 ? "Assinado com senha por "
				+ assinantesSenha + ".\n" : "";

		retorno += conferentes.length() > 0 ? "Autenticado digitalmente por "
				+ conferentes + ".\n" : "";

		return retorno;
	}

	/**
	 * verifica se uma movimentação está cancelada. Uma movimentação está
	 * cancelada quando o seu atributo movimentacaoCanceladora está preenchido
	 * com um código de movimentação de cancelamento.
	 * 
	 * @return Verdadeiro se a movimentação está cancelada e Falso caso
	 *         contrário.
	 */
	@Override
	public boolean isCancelado() {
		return isCancelada();
	}

	@Override
	public boolean isRascunho() {
		// TODO Auto-generated method stub
		if (getExTipoMovimentacao().getIdTpMov().equals(
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO)
				&& mob().doc().isEletronico() && !isAssinada())
			return true;

		return false;
	}

	@Override
	public boolean isSemEfeito() {
		if (getExDocumento().isSemEfeito()) {
			// Não gera marca de "Sem Efeito em Folha de Desentranhamento"
			if (getExTipoMovimentacao().getId() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA)
				return false;
			else
				return true;
		}

		return false;
	}

	/**
	 * Retorna da lotação do titular da movimentação.
	 * 
	 * @return Lotação do titular da movimentação.
	 */
	@Override
	public DpLotacao getLotacao() {
		return getLotaTitular();
	}

	/**
	 * Retorna uma descrição da movimentação formada pelos campos: Sigla,
	 * Descrição do Tipo de Movimentação e Descrição da Movimentação.
	 * 
	 * @return Uma descrição da movimentação
	 */
	@Override
	public String toString() {
		return (getExMobil() != null ? getExMobil().getSigla() : "")
				+ ": "
				+ (getExTipoMovimentacao() != null ? getExTipoMovimentacao()
						.getDescricao() : "") + ": " + getDescrMov();
	}

	/**
	 * @return Verdadeiro se o tipo de movimentação for CANCELAMENTO_JUNTADA ou
	 *         CANCELAMENTO_DE_MOVIMENTACAO e Falso caso contrário
	 */
	public boolean isInserirDocumentoNoDossieDoMobilRef() {
		return getExTipoMovimentacao().getId() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA
				|| getExTipoMovimentacao().getId() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO;
	}

	/**
	 * @return Data de início da movimentação de referência se o método
	 *         isInserirDocumentoNoDossieDoMobilRef() for verdadeiro ou retorna
	 *         a data de início da movimentação caso contrário.
	 * 
	 */
	public Date getDtIniMovParaInsercaoEmDossie() {
		if (getExTipoMovimentacao() == null)
			return null;
		if (isInserirDocumentoNoDossieDoMobilRef()) {
			return getExMovimentacaoRef().getDtIniMov();
		}
		return getDtIniMov();
	}

	/**
	 * Retorna o Mobil relacionado a movimentação atual.
	 * 
	 * @return Data de início da movimentação de referência se o método
	 *         isInserirDocumentoNoDossieDoMobilRef() for verdadeiro ou retorna
	 *         a data de início da movimentação caso contrário.
	 * 
	 */
	public ExMobil mob() {
		// TODO Auto-generated method stub
		return getExMobil();
	}

	@Override
	public boolean isInternoProduzido() {
		switch (getExTipoMovimentacao().getIdTpMov().intValue()) {
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO:
			return false;
		}
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isUltimaMovimentacao() {
		return getIdMov().equals(
				getExMobil().getUltimaMovimentacao().getIdMov());
	}

	@Override
	public boolean isCodigoParaAssinaturaExterna(String num) {

		int hash = Integer.parseInt(num.substring(num.indexOf("-") + 1));

		for (ExMovimentacao mov : getExDocumento().getExMovimentacaoSet())
			if (Math.abs((getExDocumento().getDescrCurta() + mov.getIdMov() + "AssinaturaExterna")
					.hashCode() % 10000) == hash)
				return true;
		return false;

	}

	@Override
	public String getTipoDescr() {
		switch (getExTipoMovimentacao().getIdTpMov().intValue()) {
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO:
			return "Anexo";
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO:
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA:
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO:
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO_TRANSFERENCIA:
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA:
			return "Despacho";
		}
		return "Outro";
	}
	
	
	public boolean podeCancelar(DpPessoa titular, DpLotacao lotaTitular) {
		if (this.getIdTpMov().equals(TIPO_MOVIMENTACAO_MARCACAO)) {
			Expression exp = new ExPodeCancelarMarcacao(this, titular, lotaTitular);
			return exp.eval();
		}
		return false;
	}

	public boolean isAssinatura() {
		long l = getExTipoMovimentacao().getId();
		switch ((int) l) {
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO:
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA:
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO:
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA:
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA:
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO:
			return true;
		}
		return false;
	}

	public boolean isAssinaturaComSenha() {
		long l = getExTipoMovimentacao().getId();
		switch ((int) l) {
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA:
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA:
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA:
			return true;
		}
		return false;
	}

	public String expliquePodeCancelar(DpPessoa titular, DpLotacao lotaTitular) {
		if (this.getIdTpMov().equals(TIPO_MOVIMENTACAO_MARCACAO)) {
			Expression exp = new ExPodeCancelarMarcacao(this, titular, lotaTitular);
			return AcaoVO.Helper.formatarExplicacao(exp, exp.eval());
		}
		return null;
	}

	public String getAssinaturaValida() {
		if (!isAssinatura())
			throw new AplicacaoException("Não é Assinatura");

		if (isAssinaturaComSenha() && getAuditHash() == null) 
			return "OK";

		try {
			return assertAssinaturaValida();
		} catch (AplicacaoException e) {
			return e.getMessage();
		} catch (Exception e) {
			return "Inválida - " + e.getMessage();
		}
	}

	public String assertAssinaturaValida() throws Exception {
		long l = getExTipoMovimentacao().getId();
		switch ((int) l) {
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA:
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA:
			return assertAssinaturaComSenhaValida(this.getExDocumento().getPdf(), this.getAuditHash(),
					this.getDtIniMov());
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA:
			return assertAssinaturaComSenhaValida(this.getExMovimentacaoRef().getConteudoBlobpdf(), this.getAuditHash(),
					this.getDtIniMov());
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO:
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO:
			return assertAssinaturaDigitalValida(this.getExDocumento().getPdf(), this.getConteudoBlobMov(),
					this.getDtIniMov());
		case (int) ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO:
			return assertAssinaturaDigitalValida(this.getExMovimentacaoRef().getConteudoBlobpdf(),
					this.getConteudoBlobMov(), this.getDtIniMov());
		default:
			throw new AplicacaoException("Não é Assinatura");
		}
	}

	private String assertAssinaturaComSenhaValida(byte[] pdf, String jwt, Date dtMov) throws Exception {
		if (dtMov == null || dtMov.before(Prop.getData("data.validar.assinatura.com.senha")))
			return "OK.";
		
		final JWTVerifier verifier;
		String pwd = Prop.get("carimbo.public.key");
		if (pwd == null)
			throw new AplicacaoException("Inválido (falta propriedade sigaex.carimbo.public.key)");
		
		PublicKey publicKey = null;

		byte[] publicKeyBytes = SwaggerUtils.base64Decode(pwd);

		KeyFactory kf = KeyFactory.getInstance("RSA");
		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
		publicKey = kf.generatePublic(publicKeySpec);
		
		verifier = new JWTVerifier(publicKey);

		Map<String, Object> payload = verifier.verify(jwt);
		String sha256AuditB64 = (String) payload.get("sha256");
		if (sha256AuditB64 == null)
			throw new AplicacaoException("Inválido (sha256 audit)");
		byte[] sha256Audit = SwaggerUtils.base64Decode(sha256AuditB64);
		byte[] sha256Pdf = BlucService.calcSha256(pdf);
		if (!Arrays.equals(sha256Audit, sha256Pdf))
			throw new AplicacaoException("Inválido (sha256 diferente)");
		return "OK (sha256)";
	}

	private String assertAssinaturaDigitalValida(byte[] pdf, byte[] cms, Date dtMov) throws Exception {
		if (dtMov == null || dtMov.before(Prop.getData("data.validar.assinatura.digital")))
			return "OK.";
		
		// Chamar o BluC para validar a assinatura
		//
		BlucService bluc = Service.getBlucService();
		ValidateRequest validatereq = new ValidateRequest();
		validatereq.setEnvelope(bluc.bytearray2b64(cms));
		validatereq.setSha1(bluc.bytearray2b64(bluc.calcSha1(pdf)));
		validatereq.setSha256(bluc.bytearray2b64(bluc.calcSha256(pdf)));
		validatereq.setTime(dtMov);
		validatereq.setCrl("true");
		ValidateResponse validateresp = Ex.getInstance().getBL().assertValid(bluc, validatereq);

		String sNome = validateresp.getCn();

		Service.throwExceptionIfError(sNome);

		String sCPF = validateresp.getCertdetails().get("cpf0");
		Service.throwExceptionIfError(sCPF);

		return "OK (" + validateresp.getPolicy() + " v" + validateresp.getPolicyversion() + ")";
	}
	
	public boolean isResp(DpPessoa titular, DpLotacao lotaTitular) {
		return Utils.equivale(getLotaResp(), lotaTitular)
				|| Utils.equivale(getResp(),titular)
				|| Utils.equivale(getLotaDestinoFinal(),lotaTitular)
				|| Utils.equivale(getDestinoFinal(),titular);
	}
}