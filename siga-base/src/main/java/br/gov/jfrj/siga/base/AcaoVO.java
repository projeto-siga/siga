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
package br.gov.jfrj.siga.base;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class AcaoVO {
	private String icone;
	private String nome;
	private String nameSpace;
	private String acao;
	boolean pode;
	private String msgConfirmacao;
	private String pre;
	private String pos;
	private String classe;
	private String modal;
	private Map<String, String> params;
	private String explicacao;
	private boolean post;

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
		if (params == null)
			return false;
		return params.containsKey("popup");
	}

	public boolean isAjax() {
		if (params == null)
			return false;
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

	public AcaoVO(String icone, String nome, String nameSpace, String acao, boolean pode, String msgConfirmacao,
			TreeMap<String, String> params, String pre, String pos, String classe, String modal) {
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
		this.modal = modal;
	}

	public AcaoVO(String icone, String nome, String nameSpace, String acao, boolean pode, String explicacao,
			String msgConfirmacao, TreeMap<String, String> params, String pre, String pos, String classe,
			String modal) {
		this(icone, nome, nameSpace, acao, pode, msgConfirmacao, params, pre, pos, classe, modal);
		this.explicacao = explicacao;
	}

	public AcaoVO() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return getPre() + " " + getNome() + " " + getPos() + " <" + getNameSpace() + " - " + getAcao() + "?"
				+ getParams() + ">";
	}

	public static List<AcaoVO> ordena(List<AcaoVO> acoes, Comparator<AcaoVO> comparator) {
		if (comparator != null)
			Collections.sort(acoes, comparator);
		return acoes;
	}

	public String getUrl() {
		String resultUrl = "";
		if (this.params != null) {
			String valueOfParameterToAdd;
			Map<String, String> parameters = this.params;
			Set<String> parametersNames = this.params.keySet();
			for (String nameOfParameterToAdd : parametersNames) {
				valueOfParameterToAdd = parameters.get(nameOfParameterToAdd);
				resultUrl = (nameOfParameterToAdd + "=" + valueOfParameterToAdd + "&").concat(resultUrl);
			}
			if (!parametersNames.isEmpty())
				resultUrl = ("?").concat(resultUrl);
		}
		resultUrl = ((this.nameSpace != null ? this.nameSpace + "/" : "") + this.acao).concat(resultUrl);
		return resultUrl;
	}

	public String getModal() {
		return modal;
	}

	public void setModal(String modal) {
		this.modal = modal;
	}

	public String getExplicacao() {
		return explicacao;
	}

	public void setExplicacao(String explicacao) {
		this.explicacao = explicacao;
	}

	public boolean isPost() {
		return post;
	}

	public void setPost(boolean post) {
		this.post = post;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public void setPode(boolean pode) {
		this.pode = pode;
	}

	public void setMsgConfirmacao(String msgConfirmacao) {
		this.msgConfirmacao = msgConfirmacao;
	}

	public void setPre(String pre) {
		this.pre = pre;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

}
