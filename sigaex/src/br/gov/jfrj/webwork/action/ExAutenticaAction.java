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
package br.gov.jfrj.webwork.action;

import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import nl.captcha.Captcha;
import nl.captcha.noise.StraightLineNoiseProducer;
import br.gov.jfrj.siga.ex.ExArquivo;
import br.gov.jfrj.siga.ex.bl.Ex;

public class ExAutenticaAction extends ExActionSupport {

	private String code;

	private String answer;

	private String n;

	private String sc;

	private byte[] bytes;

	private String fileName;

	private String contentType;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getN() {
		return n;
	}

	public void setN(String n) {
		this.n = n;
	}

	public String getSc() {
		return sc;
	}

	public void setSc(String sc) {
		this.sc = sc;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String exec() throws Exception {
		// assertAcesso("FE:Ferramentas;DESP:Tipos de despacho");

		if (sc != null && sc.trim().length() != 0) {
			Captcha captcha = new Captcha.Builder(150, 75)
					.addNoise(new StraightLineNoiseProducer()).addText()
					.addBackground().gimp().addBorder().build();
			getRequest().getSession().setAttribute(Captcha.NAME, captcha);

			ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
			ImageIO.write(captcha.getImage(), "png", imgOutputStream);
			bytes = imgOutputStream.toByteArray();
			fileName = contentType = "image/png";
			return "autenticado";
		}

		Captcha captcha = (Captcha) getRequest().getSession().getAttribute(
				Captcha.NAME);
		getRequest().getSession().removeAttribute(Captcha.NAME);

		if (captcha == null || n == null || n.trim().length() == 0) {
			return SUCCESS;
		}

		// getRequest().setCharacterEncoding("UTF-8"); // Do this so we can
		// capture non-Latin chars
		if (captcha.isCorrect(getAnswer())) {
			ExArquivo arq = Ex.getInstance().getBL()
					.buscarPorNumeroAssinatura(n);
			fileName = arq.getReferenciaPDF();
			contentType = "application/pdf";
			bytes = Ex.getInstance().getBL().obterPdfPorNumeroAssinatura(n);
			return "autenticado";
		} else {
			setMensagem("Caracteres digitados não conferem com os apresentados na imagem. Por favor, tente novamente.");
		}
		return SUCCESS;
	}
}
