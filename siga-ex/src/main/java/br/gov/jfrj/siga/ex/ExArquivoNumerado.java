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

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.model.ContextoPersistencia;

public class ExArquivoNumerado implements Comparable {
	private ExArquivo arquivo;
	private Integer paginaInicial;
	private Integer paginaFinal;
	private ExMobil mobil;
	private Date data;
	private int nivel;
	private boolean copia;	
	private String referenciaHtmlCompletoDocPrincipal;
	private String referenciaPDFCompletoDocPrincipal;

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
            if (doc.getExModelo().isDescricaoAutomatica() || isDescricaoCapturado(doc))
				return doc.getDescrDocumento();
		}
		return getNome();
	}

	public String getNomeOuDescricaoComMovimentacao() {
		if (getArquivo() instanceof ExMovimentacao) {
			ExMovimentacao mov = (ExMovimentacao) getArquivo();
			if (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.CIENCIA)
				return "Ciência (" + getNomeOuDescricao() + ")";
		}
		return getNomeOuDescricao();
	}



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

	public String getReferenciaPDFCompleto() {
		if (!getArquivo().isPdf())
			return null;
		return getReferencia() + ".pdf&completo=1";
	}

	public String getReferenciaPDFCompletoVolumes() {
		if (!getArquivo().isPdf())
			return null;
		return getReferenciaPDFCompleto() + "&volumes=1";
	}

	public String getReferenciaHtml() {
		if ((getArquivo() instanceof ExDocumento && ((ExDocumento)getArquivo()).isCapturado()) ||
				(getArquivo() instanceof ExMovimentacao && getArquivo().getHtml() == null))
			return null;
		return getReferencia() + ".html";
	}
	
	public String getReferenciaHtmlCompleto() {
		return getReferencia() + ".html&completo=1";
	}

	public String getReferenciaHtmlCompletoVolumes() {
		return getReferenciaHtmlCompleto() + "&volumes=1";
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
			else if(SigaMessages.isSigaSP() && !doc.isProcesso() && doc.isFinalizado() 
					&& doc.getMobilDefaultParaReceberJuntada().getMovsNaoCanceladas(ExTipoDeMovimentacao.JUNTADA).isEmpty()
					) {
				return 1;
			}
		}
		return 0;
	}
	
    public String getFolha() {
        if (getPaginaInicial() == null || getPaginaFinal() == null)
            return null;
        if (getPaginaInicial().equals(getPaginaFinal()))
            return "fl. " + getPaginaInicial();
        else
            return "fls. " + getPaginaInicial() + "-" + getPaginaFinal();
    }

	public boolean isCopia() {
		return copia;
	}

	public void setCopia(boolean copia) {
		this.copia = copia;
	}
	
	public String getReferenciaHtmlCompletoDocPrincipal() {
		return referenciaHtmlCompletoDocPrincipal != null ? referenciaHtmlCompletoDocPrincipal : getReferenciaHtmlCompleto();
	}
	
	public String getReferenciaHtmlCompletoDocPrincipalVolumes() {
		return referenciaHtmlCompletoDocPrincipal != null ? referenciaHtmlCompletoDocPrincipal + "&volumes=1" : getReferenciaHtmlCompletoVolumes();
	}
	
	public void setReferenciaHtmlCompletoDocPrincipal(String referenciaHtmlCompletoDocPrincipal) {
		this.referenciaHtmlCompletoDocPrincipal = referenciaHtmlCompletoDocPrincipal;
	}
	
	public String getReferenciaPDFCompletoDocPrincipal() {
		return referenciaPDFCompletoDocPrincipal != null ? referenciaPDFCompletoDocPrincipal : getReferenciaPDFCompleto();
	}
	
	public String getReferenciaPDFCompletoDocPrincipalVolumes() {
		return referenciaPDFCompletoDocPrincipal != null ? referenciaPDFCompletoDocPrincipal + "&volumes=1" : getReferenciaPDFCompletoVolumes();
	}
	
	public void setReferenciaPDFCompletoDocPrincipal(String referenciaPDFCompletoDocPrincipal) {
		this.referenciaPDFCompletoDocPrincipal = referenciaPDFCompletoDocPrincipal;
	}
	
	/**
	 * Desconecta este arquivo do banco de dados, liberando portanto memoria da hibernate session. Necessario pois
	 * quando geramos o PDF completo, precisamos controlar a quantidade total de memoria que sera utilizada.
	 */
	public void evict() {
		if (getArquivo() instanceof ExDocumento) {
			ExDocumento doc = (ExDocumento) getArquivo();
			if (doc.getCpArquivo() != null) {
				if (doc.getCpArquivo().getArquivoBlob() != null) {
					ContextoPersistencia.em().detach(doc.getCpArquivo().getArquivoBlob());
				}
				ContextoPersistencia.em().detach(doc.getCpArquivo());
			}
			ContextoPersistencia.em().detach(doc);
		} else if (getArquivo() instanceof ExMovimentacao) {
			ExMovimentacao mv = (ExMovimentacao) getArquivo();
			if (mv.getCpArquivo() != null) {
				if (mv.getCpArquivo().getArquivoBlob() != null) {
					ContextoPersistencia.em().detach(mv.getCpArquivo().getArquivoBlob());
				}
				ContextoPersistencia.em().detach(mv.getCpArquivo());
			}
			ContextoPersistencia.em().detach(mv);
		}
	}

	private boolean isDescricaoCapturado(ExDocumento doc) {
		return doc.isCapturado() && Prop.getBool("exibir.descricao.capturado");
	}

}
