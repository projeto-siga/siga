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

import java.util.Date;

public class ExArquivoNumerado implements Comparable {
	private ExArquivo arquivo;
	private Integer paginaInicial;
	private Integer paginaFinal;
	private ExMobil mobil;
	private Date data;
	private int nivel;

	public ExArquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(ExArquivo arquivo) {
		this.arquivo = arquivo;
	}

	public Integer getPaginaInicial() {
		return paginaInicial;
	}

	public void setPaginaInicial(Integer paginaInicial) {
		this.paginaInicial = paginaInicial;
	}

	public Date getData() {
		if (data == null)
			return getArquivo().getData();
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int compareTo(Object o) {
		int i;
		i = this.getData().compareTo(((ExArquivoNumerado) o).getData());
		if (i != 0)
			return i;
		return i;
	}

	public Integer getPaginaFinal() {
		return paginaFinal;
	}

	public void setPaginaFinal(Integer paginaFinal) {
		this.paginaFinal = paginaFinal;
	}

	public int getNivel() {
		// TODO Auto-generated method stub
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public String getNome() {
		if (getArquivo() instanceof ExDocumento) {
			return getMobil().getSigla();
		}

		if (getArquivo() instanceof ExMovimentacao) {
			ExMovimentacao mov = (ExMovimentacao) getArquivo();
			if (mov.getNmArqMov() != null)
				return mov.getNmArqMov().replace(".pdf", "");
			else
				return mov.getExMobil().getSigla();
		}
		return null;
	};
	
	public String getNomeOuDescricao() {
		if (getArquivo() instanceof ExDocumento) {
			ExDocumento doc = (ExDocumento) getArquivo();
			if (doc.getExTipoDocumento().getId() == ExTipoDocumento.TIPO_DOCUMENTO_CAPTURADO)
				return doc.getDescrDocumento();
		}
		return getNome();
	};


	public String getReferencia() {
		if (getArquivo() instanceof ExDocumento) {
			return getMobil().getCodigoCompacto();
		}

		if (getArquivo() instanceof ExMovimentacao) {
			ExMovimentacao mov = (ExMovimentacao) getArquivo();
			return mov.getReferencia();
		}
		return null;
	}

	public String getReferenciaPDF() {
		if (!getArquivo().isPdf())
			return null;
		return getReferencia() + ".pdf";
	}

	public String getReferenciaHtml() {
//		if (getArquivo().getHtml() == null)
//			return null;
		return getReferencia() + ".html";
	}

	public ExMobil getMobil() {
		return mobil;
	}

	public void setMobil(ExMobil mobil) {
		this.mobil = mobil;
	}

	public int getNumeroDePaginasParaInsercaoEmDossie() {
		return getArquivo().getNumeroDePaginasParaInsercaoEmDossie();
	}

	public Integer getOmitirNumeracao() {
		if (getArquivo() instanceof ExDocumento) {
			ExDocumento doc = (ExDocumento) getArquivo();
			if (doc.isProcesso())
				return 1;
		}
		return 0;
	}

}
