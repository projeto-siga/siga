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
package br.gov.jfrj.siga.cp;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;

/**
 * A class that represents a row in the CP_ARQUIVO table. You can customize the
 * behavior of this class by editing the class, {@link CpArquivo()}.
 */
@Entity
@Table(name = "CP_ARQUIVO", schema = "CORPORATIVO")
public class CpArquivo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "CORPORATIVO.CP_ARQUIVO_SEQ", name = "CP_ARQUIVO_SEQ")
	@GeneratedValue(generator = "CP_ARQUIVO_SEQ")
	@Column(name = "ID_ARQ")
	private java.lang.Long idArq;

	@ManyToOne
	@JoinColumn(name = "ID_ORGAO_USU")
	private CpOrgaoUsuario orgaoUsuario;

	@Column(name = "CONTEUDO_TP_ARQ", length = 128)
	private java.lang.String conteudoTpArq;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private CpArquivoBlob arquivoBlob;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TP_ARMAZENAMENTO")
	private CpArquivoTipoArmazenamentoEnum tipoArmazenamento;
	
	@Column(name = "CAMINHO")
	private String caminho;
	
	@Column(name = "TAMANHO_ARQ")
	private Integer tamanho = 0;
	
	@Column(name = "HASH_MD5")
	private String hashMD5;
	
	@Transient
	private String hashMD5Original;
	
	@Version
	private Long versao;

	/**
	 * Simple constructor of AbstractExDocumento instances.
	 */
	public CpArquivo() {

	}

	public java.lang.Long getIdArq() {
		return idArq;
	}

	public void setIdArq(java.lang.Long idArq) {
		this.idArq = idArq;
	}

	public java.lang.String getConteudoTpArq() {
		return conteudoTpArq;
	}

	public void setConteudoTpArq(java.lang.String conteudoTpArq) {
		this.conteudoTpArq = conteudoTpArq;
	}

	public CpOrgaoUsuario getOrgaoUsuario() {
		return orgaoUsuario;
	}

	public void setOrgaoUsuario(CpOrgaoUsuario orgaoUsuario) {
		this.orgaoUsuario = orgaoUsuario;
	}

	private CpArquivoBlob getArquivoBlob() {
		return arquivoBlob;
	}

	public void setArquivoBlob(CpArquivoBlob arquivoBlob) {
		this.arquivoBlob = arquivoBlob;
	}

	public byte[] getConteudoBlobArq() {
		if (this.arquivoBlob == null) {
			this.arquivoBlob = new CpArquivoBlob();
			this.arquivoBlob.setArquivo(this);
		}
		return this.arquivoBlob.getConteudoBlobArq();
	}

	public void setConteudoBlobArq(byte[] createBlob) {
		if (this.arquivoBlob == null) {
			this.arquivoBlob = new CpArquivoBlob();
			this.arquivoBlob.setArquivo(this);
		}
		this.arquivoBlob.setConteudoBlobArq(createBlob);
	}

	public CpArquivoTipoArmazenamentoEnum getTipoArmazenamento() {
		return tipoArmazenamento;
	}

	public void setTipoArmazenamento(CpArquivoTipoArmazenamentoEnum tipoArmazenamento) {
		this.tipoArmazenamento = tipoArmazenamento;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	public Integer getTamanho() {
		return tamanho;
	}

	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	public void gerarCaminho(Date data) {
		String extensao;
		
		if(TipoConteudo.ZIP.getMimeType().equals(getConteudoTpArq()))
			extensao = TipoConteudo.ZIP.getExtensao();
		else if(TipoConteudo.PDF.getMimeType().equals(getConteudoTpArq()))
			extensao = TipoConteudo.PDF.getExtensao();
		else if(TipoConteudo.TXT.getMimeType().equals(getConteudoTpArq()))
			extensao = TipoConteudo.TXT.getExtensao();
		else
			extensao = TipoConteudo.ZIP.getExtensao();
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.AM_PM, Calendar.PM);
		if(data!=null)
			c.setTime(data);
		this.caminho = c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DATE)+"/"+c.get(Calendar.HOUR_OF_DAY)+"/"+c.get(Calendar.MINUTE)+"/"+UUID.randomUUID().toString()+"."+extensao;
	}

	public String getHashMD5() {
		return hashMD5;
	}

	public void setHashMD5(String hash) {
		if(this.hashMD5Original == null && this.getIdArq()!=null)
			this.hashMD5Original = this.hashMD5;
		this.hashMD5 = hash;
	}

	public String getHashMD5Original() {
		return hashMD5Original;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}
	
	
}