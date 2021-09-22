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
 * Created Mon Nov 14 13:29:31 GMT-03:00 2005 by MyEclipse Hibernate Tool.
 */
package br.gov.jfrj.siga.ex;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;

/**
 * A class that represents a row in the 'EX_MODELO' table. This class may be
 * customized as it is never re-generated after being created.
 */
@Entity
@BatchSize(size = 500)
@Table(name = "siga.ex_modelo")
public class ExModelo extends AbstractExModelo implements Sincronizavel, Selecionavel {

	/**
	 * Simple constructor of ExModelo instances.
	 */
	public ExModelo() {
	}

	/**
	 * Constructor of ExModelo instances given a simple primary key.
	 * 
	 * @param idMod
	 */
	public ExModelo(final java.lang.Long idMod) {
		super(idMod);
	}

	/* Add customized code below */
	public void setConteudoBlobMod2(final byte[] blob) {
		if (blob != null)
			setConteudoBlobMod(blob);
	}

	public byte[] getConteudoBlobMod2() {
		return getConteudoBlobMod();
	}

	public Long getId() {
		return getIdMod();
	}

	public void setId(Long id) {
		setIdMod(id);
	}

	public boolean isFechado() {
		if (getModeloAtual() == null)
			return true;

		return false;
	}

	public ExModelo getModeloAtual() {
		return ExDao.getInstance().consultarModeloAtual(this);
	}
	
	public ExModelo getModeloPeloId() {
		return ExDao.getInstance().consultar(this.getIdMod(),ExModelo.class, false);
	}

	public boolean isDescricaoAutomatica() {
		if ("template/freemarker".equals(getConteudoTpBlob())) {
			byte[] blob = getConteudoBlobMod2();
			if (blob == null) return false;
			String freemarker = new String(getConteudoBlobMod2(), StandardCharsets.UTF_8);
			if (freemarker.contains("@descricao"))
				return true;
		}
		return false;
	}

	public boolean isClassificacaoAutomatica() {
		if (getExClassificacao() != null)
			return true;

		return false;
	}

	@Override
	public String getIdExterna() {
		return getUuid();
	}

	@Override
	public void setIdExterna(String idExterna) {
		setUuid(idExterna);
	}

	@Override
	public void setIdInicial(Long idInicial) {
		setHisIdIni(idInicial);
	}

	@Override
	public Date getDataInicio() {
		return getHisDtIni();
	}

	@Override
	public void setDataInicio(Date dataInicio) {
		setHisDtIni(dataInicio);
	}

	@Override
	public Date getDataFim() {
		return getHisDtFim();
	}

	@Override
	public void setDataFim(Date dataFim) {
		setHisDtFim(dataFim);
	}

	@Override
	public String getLoteDeImportacao() {
		return null;
	}

	@Override
	public void setLoteDeImportacao(String loteDeImportacao) {

	}

	@Override
	public int getNivelDeDependencia() {
		return 0;
	}

	@Override
	public String getDescricaoExterna() {
		return getNmMod();
	}

	private static final String SUBDIRETORIO = "-subdiretorio-";

	public String getSubdiretorioENome() {
		return Texto
				.slugify(
						(getExFormaDocumento() != null
								&& getExFormaDocumento().getDescrFormaDoc() != null ? "especie-"
								+ getExFormaDocumento().getDescrFormaDoc()
								+ SUBDIRETORIO
								: "")
								+ getNmMod().replace(": ", SUBDIRETORIO), true,
						false).replace(SUBDIRETORIO, "/");
	}

	public String getSubdiretorio() {
		String filename = getSubdiretorioENome();
		if (filename.contains("/"))
			return filename.substring(0, filename.lastIndexOf("/"));
		return null;
	}
	
	@Override
	public String getSigla() {
		return getNmMod();
	}

	@Override
	public void setSigla(String sigla) {
		setNmMod(sigla);
	}

	@Override
	public String getDescricao() {
		return null;
	}
}
