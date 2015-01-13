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
package br.gov.jfrj.siga.ex.vo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import br.gov.jfrj.siga.ex.bl.BIE.Nodo;

public class ExAcaoVO {
	String icone;
	String nome;
	String nameSpace;
	String acao;
	boolean pode;
	String msgConfirmacao;
	String pre;
	String pos;
	String classe;
	Map<String, String> params;
	
	public Map<String, String> getParams() {
		return params;
	}
	
	public String getPre() {
		return pre;
	}
	
	public String getPos() {
		return pos;
	}
	
	public String getIcone() {
		return icone;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getNomeNbsp() {
		return getNome().replace(" ", "&nbsp;");
	}
	
	public String getNameSpace() {
		return nameSpace;
	}
	
	public String getAcao() {
		return acao;
	}
	
	public boolean isPode() {
		return pode;
	}
	
	public boolean isPopup() {
		return params.containsKey("popup");
	}
	
	public boolean isAjax() {
		return params.containsKey("ajax");
	}
	
	public String getMsgConfirmacao() {
		return msgConfirmacao;
	}
	
	public String getClasse() {
		return classe;
	}
	
	public void setClasse(String classe) {
		this.classe = classe;
	}
	
	public ExAcaoVO(String icone, String nome, String nameSpace, String acao, boolean pode, String msgConfirmacao,
			TreeMap<String, String> params, String pre, String pos, String classe) {
		super();
		this.icone = icone;
		this.nome = nome;
		this.nameSpace = nameSpace;
		this.acao = acao;
		this.pode = pode;
		this.msgConfirmacao = msgConfirmacao;
		this.params = params;
		this.pre = pre;
		this.pos = pos;
		this.classe = classe;
	}
	
	@Override
	public String toString() {
		return getPre() + " " + getNome() + " " + getPos() + " <" + getNameSpace() + " - " + getAcao() + "?"
				+ getParams() + ">";
	}
	
	public static List<ExAcaoVO> ordena(List<ExAcaoVO> acoes, Comparator<ExAcaoVO> comparator) {
		if (comparator != null)
			Collections.sort(acoes, comparator);
		return acoes;
	}
	
	public String getUrl() {
		String resultUrl = "";
		String valueOfParameterToAdd;
		Map<String, String> parameters = this.params;
		Set<String> parametersNames = this.params.keySet();
		for (String nameOfParameterToAdd : parametersNames) {
			valueOfParameterToAdd = parameters.get(nameOfParameterToAdd);
			resultUrl = (nameOfParameterToAdd + "=" + valueOfParameterToAdd + "&").concat(resultUrl);
		}
		if (!parametersNames.isEmpty())
			resultUrl = ("?").concat(resultUrl);
		resultUrl = (this.nameSpace + "/" + this.acao).concat(resultUrl);
		return resultUrl;
	}
}
