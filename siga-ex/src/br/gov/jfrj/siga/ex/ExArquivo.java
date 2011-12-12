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
package br.gov.jfrj.siga.ex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;

public abstract class ExArquivo {
	private Integer numPaginas;

	/**
	 * Retorna uma lista com o nome de todos os usuários que já assinaram um
	 * documento.
	 * 
	 * @return Uma lista com o nome de todos os usuários que já assinaram um
	 *         documento.
	 * 
	 */
	public ArrayList<String> getAssinantes() {
		Set<ExMovimentacao> set = getAssinaturasDigitais();
		ArrayList<String> als = new ArrayList<String>();

		for (ExMovimentacao movAssinatura : set) {
			String s = movAssinatura.getDescrMov().trim().toUpperCase();
			s = s.split(":")[0];
			s = s.intern();
			if (!als.contains(s)) {
				als.add(s);
			}
		}
		return als;
	}

	public abstract Set<ExMovimentacao> getAssinaturasDigitais();

	/**
	 * Retorna o número de páginas de um arquivo.
	 * 
	 * @return Número de páginas de um arquivo.
	 * 
	 */
	public Integer getContarNumeroDePaginas() {
		try {
			byte[] abPdf = null;
			abPdf = getPdf();
			if (abPdf == null)
				return null;
			return Documento.getNumberOfPages(abPdf);
		} catch (IOException e) {
			return null;
		}
	}

	public abstract Date getData();

	public abstract String getHtml();

	public Long getIdDoc() {
		if (this instanceof ExDocumento) {
			ExDocumento doc = (ExDocumento) this;
			return doc.getIdDoc();
		}
		;

		if (this instanceof ExMovimentacao) {
			ExMovimentacao mov = (ExMovimentacao) this;
			return mov.getIdMov();
		}

		return null;
	}

	public abstract DpLotacao getLotacao();

	/**
	 * Retorna uma mensagem informando quem assinou o documento e o endereço
	 * onde o usuário pode verificar a autenticidade de um documento com base em
	 * um código gerado.
	 * 
	 */
	public String getMensagem() {
		String sMensagem;
		sMensagem = null;
		ArrayList<String> als = getAssinantes();

		if (als.size() > 0) {
			String sSiglaAssinatura = getSiglaAssinatura();
			sMensagem = "Assinado digitalmente por ";
			for (int i = 0; i < als.size(); i++) {
				String nome = als.get(i);
				if (i > 0) {
					if (i == als.size() - 1) {
						sMensagem += " e ";
					} else {
						sMensagem += ", ";
					}
				}
				sMensagem += nome;
			}
			sMensagem += ".\n";
			sMensagem += "Documento Nº: " + sSiglaAssinatura
					+ " - consulta à autenticidade em www.jfrj.jus.br/ex/docs.";
		}
		return sMensagem;
	}

	/**
	 * Caso o método esteja sendo executado em um objeto do tipo documento,
	 * retorna a código do documento. Caso o método esteja sendo executado em um
	 * objeto do tipo movimentação, retorna o nome do arquivo desta
	 * movimentação.
	 * 
	 */
	public String getNome() {
		if (this instanceof ExDocumento) {
			ExDocumento doc = (ExDocumento) this;
			return doc.getCodigo();
		}

		if (this instanceof ExMovimentacao) {
			ExMovimentacao mov = (ExMovimentacao) this;
			return mov.getNmArqMov();
		}
		return null;
	}

	/**
	 * Retorna o número de páginas do documento para exibir no dossiê.
	 * 
	 */
	public int getNumeroDePaginasParaInsercaoEmDossie() {
		if (this instanceof ExMovimentacao) {
			ExMovimentacao mov = (ExMovimentacao) this;
			if (mov.getNumPaginasOri() != null)
				return mov.getNumPaginasOri();
		}
		return getNumPaginas();
	}

	public Integer getNumPaginas() {
		return numPaginas;
	}

	public abstract byte[] getPdf();

	public long getByteCount() {
		byte[] ab = getPdf();
		if (ab == null)
			return 0;
		return ab.length;
	}

	// public byte[] getPdfToHash() throws Exception {
	// byte[] pdf = getPdf();
	// if (pdf == null)
	// return null;
	// return AssinaturaDigital.getHasheableRangeFromPDF(pdf);
	// }
	//
	// public String getPdfToHashB64() throws Exception {
	// return Base64.encode(getPdfToHash());
	// }

	public String getQRCode() {
		if (isAssinadoDigitalmente()) {
			String sQRCode;
			sQRCode = "http://a.teste.com.br/" + getSiglaAssinatura();
			return sQRCode;
		}
		return null;
	}

	/**
	 * Quando o objeto for do tipo documento retorna o código compacto do
	 * documento. Quando o objeto for do tipo movimentação retorna a referência
	 * da movimentação que é o codigo compacto da movimentação mais o id da
	 * movimentação.
	 * 
	 */
	public String getReferencia() {
		if (this instanceof ExDocumento) {
			ExDocumento doc = (ExDocumento) this;
			return doc.getCodigoCompacto();
		}

		if (this instanceof ExMovimentacao) {
			ExMovimentacao mov = (ExMovimentacao) this;
			return mov.getReferencia();
		}
		return null;
	}

	/**
	 * Retorna a referência do objeto mais o extensão ".html".
	 * 
	 */
	public String getReferenciaHtml() {
		if (getHtml() == null)
			return null;
		return getReferencia() + ".html";
	}

	/**
	 * Retorna a referência do objeto mais o extensão ".pdf".
	 * 
	 */
	public String getReferenciaPDF() {
		if (getPdf() == null)
			return null;
		return getReferencia() + ".pdf";
	};

	public Map<String, String> getResumo() {
		return null;
	};

	public abstract String getSiglaAssinatura();

	/**
	 * Verifica se um arquivo foi assinado digitalmente.
	 * 
	 * @return Verdadeiro caso o arquivo tenha sido assinado digitalmente e
	 *         Falso caso o arquivo não tenha sido assinado digitalmente.
	 * 
	 */
	public boolean isAssinadoDigitalmente() {
		return (getAssinaturasDigitais() != null)
				&& (getAssinaturasDigitais().size() > 0);
	}

	public abstract boolean isCancelado();

	public abstract boolean isRascunho();

	public void setNumPaginas(Integer numPaginas) {
		this.numPaginas = numPaginas;
	}
}
