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
 * Criado em  13/09/2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.webwork.action;

import java.util.List;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.util.MascaraUtil;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.libs.webwork.SigaSelecionavelActionSupport;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.persistencia.ExClassificacaoDaoFiltro;

public class ExClassificacaoAction
		extends
		SigaSelecionavelActionSupport<ExClassificacao, ExClassificacaoDaoFiltro> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6157885790446917185L;

//	private Byte assuntoPrincipal;

	private String assunto;

//	private Byte assuntoSecundario;

	private String atividade;

	private String classe;

	private ExClassificacaoSelecao classificacaoSel;

	private String nome;

	private String subclasse;

	private Boolean ultimoNivel;

	public ExClassificacaoAction() {
		super();
		classificacaoSel = new ExClassificacaoSelecao();
	}

	@Override
	public ExClassificacaoDaoFiltro createDaoFiltro() {
		final ExClassificacaoDaoFiltro flt = new ExClassificacaoDaoFiltro();
		if (assunto != null)
			flt.setAssunto(String.valueOf(assunto));
//		if (assuntoPrincipal != null)
//			flt.setAssuntoPrincipal(String.valueOf(assuntoPrincipal));
//		if (assuntoSecundario != null)
//			flt.setAssuntoSecundario(String.valueOf(assuntoSecundario));
		if (classe != null)
			flt.setClasse(String.valueOf(classe));
		if (subclasse != null)
			flt.setSubclasse(String.valueOf(subclasse));
		if (atividade != null)
			flt.setAtividade(String.valueOf(atividade));
		if (ultimoNivel != null)
			flt.setUltimoNivel(String.valueOf(ultimoNivel));
		else
			flt.setUltimoNivel("false");
		flt.setDescricao(Texto.removeAcentoMaiusculas(nome));
		return flt;
	}

//	public Byte getAssuntoPrincipal() {
//		return assuntoPrincipal;
//	}

//	public Byte getAssuntoSecundario() {
//		return assuntoSecundario;
//	}

	public List<ExClassificacao> getAssuntosPrincipal()
			throws AplicacaoException {
		return ExDao.getInstance().listarAssuntosPrincipal();
	}

	public List<ExClassificacao> getAssuntos() throws AplicacaoException {
		return ExDao.getInstance().listarAssuntos();
	}

//	public List<ExClassificacao> getAssuntosSecundario()
//			throws AplicacaoException {
//		int codAssuntoPrincipal = -1;
//		if (this.getAssuntoPrincipal() != null) {
//			if (!this.getAssuntoPrincipal().equals("")) {
//				codAssuntoPrincipal = new Integer(this.getAssuntoPrincipal());
//			}
//		}
//		return ExDao.getInstance()
//				.listarAssuntosSecundario(codAssuntoPrincipal);
//	}

	public String getAtividade() {
		return atividade;
	}

	/*
	 * public List<ExClassificacao> getClassificacoes() throws CsisException {
	 * ExClassificacaoDao classificacaoDao =
	 * getFabrica().createExClassificacaoDao(); ExClassificacaoDaoFiltro flt =
	 * new ExClassificacaoDaoFiltro(); return
	 * classificacaoDao.consultarPorFiltro(flt); }
	 */

	public List<ExClassificacao> getAtividades() throws AplicacaoException {
		String codClasse = "-1";
		String codSubClasse = "-1";
		boolean ultimoNivel = false;
		if (this.getClasse() != null) {
			if (!this.getClasse().equals("")) {
				codClasse = MascaraUtil.getInstance().getCampoDaMascara(1,getClasse());;
			}
		}
		if (this.getSubclasse() != null) {
			if (!this.getSubclasse().equals("")) {
				codSubClasse = codClasse = MascaraUtil.getInstance().getCampoDaMascara(2,getClasse());;
			}
		}
		if (this.getUltimoNivel() != null) {
			ultimoNivel = this.getUltimoNivel();
		}
		return ExDao.getInstance().listarAtividades(codClasse, codSubClasse, ultimoNivel);

	}

	public String getClasse() {
		return classe;
	}

//	public List<ExClassificacao> getClassesAntigo() throws AplicacaoException {
//		int codAssuntoSecundario = -1;
//		int codAssuntoPrincipal = -1;
//		if (this.getAssuntoPrincipal() != null) {
//			if (!this.getAssuntoPrincipal().equals("")) {
//				codAssuntoPrincipal = new Integer(this.getAssuntoPrincipal());
//			}
//		}
//		if (this.getAssuntoSecundario() != null) {
//			if (!this.getAssuntoSecundario().equals("")) {
//				codAssuntoSecundario = new Integer(this.getAssuntoSecundario());
//			}
//		}
//		return ExDao.getInstance().listarClassesAntigo(codAssuntoPrincipal,
//				codAssuntoSecundario);
//	}

	public List<ExClassificacao> getClasses() throws AplicacaoException {
		String codAssunto = "-1";
		if (this.getAssunto() != null ) {
			if (!this.getAssunto().equals("")) {
				codAssunto = MascaraUtil.getInstance().getCampoDaMascara(0,getAssunto());
			}
		}

		return ExDao.getInstance().listarClasses(codAssunto);
	}

	public ExClassificacaoSelecao getClassificacaoSel() {
		return classificacaoSel;
	}

	public String getNome() {
		return nome;
	}

	public String getSubclasse() {
		return subclasse;
	}

//	public List<ExClassificacao> getSubClassesAntigo() throws AplicacaoException {
//		int codAssuntoSecundario = -1;
//		int codAssuntoPrincipal = -1;
//		int codClasse = -1;
//		if (this.getAssuntoPrincipal() != null) {
//			if (!this.getAssuntoPrincipal().equals("")) {
//				codAssuntoPrincipal = new Integer(this.getAssuntoPrincipal());
//			}
//		}
//		if (this.getAssuntoSecundario() != null) {
//			if (!this.getAssuntoSecundario().equals("")) {
//				codAssuntoSecundario = new Integer(this.getAssuntoSecundario());
//			}
//		}
//		if (this.getClasse() != null) {
//			if (!this.getClasse().equals("")) {
//				codClasse = new Integer(this.getClasse());
//			}
//		}
//		return ExDao.getInstance().listarSubClassesAntigo(codAssuntoPrincipal,
//				codAssuntoSecundario, codClasse);
//	}

	public List<ExClassificacao> getSubClasses() throws AplicacaoException {
		String codAssunto = "-1";
		String codClasse = "-1";
		if (this.getAssunto() != null && !this.getAssunto().equals("-1")) {
			if (!this.getAssunto().equals("")) {
				codAssunto = MascaraUtil.getInstance().getCampoDaMascara(0,getAssunto());
			}
		}
		if (this.getClasse() != null && !this.getClasse().equals("-1")) {
			if (!this.getClasse().equals("")) {
				codClasse = MascaraUtil.getInstance().getCampoDaMascara(1,getClasse());
			}
		}
		return ExDao.getInstance().listarSubClasses(codAssunto, codClasse);
	}

	@Override
	public Selecionavel selecionarPorNome(final ExClassificacaoDaoFiltro flt)
			throws AplicacaoException {

		// Procura por nome
		flt.setDescricao(Texto.removeAcentoMaiusculas(flt.getSigla()));
		flt.setSigla(null);
		final List l = ExDao.getInstance().consultarPorFiltro(flt);
		if (l != null)
			if (l.size() == 1)
				return (ExClassificacao) l.get(0);
		return null;
	}

//	public void setAssuntoPrincipal(final Byte assuntoPrincipal) {
//		this.assuntoPrincipal = assuntoPrincipal;
//	}

//	public void setAssuntoSecundario(final Byte assuntoSecundario) {
//		this.assuntoSecundario = assuntoSecundario;
//	}

	public void setAtividade(final String atividade) {
		this.atividade = atividade;
	}

	public void setClasse(final String classe) {
		this.classe = classe;
	}

	public void setClassificacaoSel(
			final ExClassificacaoSelecao classificacaoSel) {
		this.classificacaoSel = classificacaoSel;
	}

	public void setNome(final String descricao) {
		this.nome = descricao;
	}

	public void setSubclasse(final String subclasse) {
		this.subclasse = subclasse;
	}

	public Boolean getUltimoNivel() {
		return ultimoNivel;
	}

	public void setUltimoNivel(Boolean ultimoNivel) {
		this.ultimoNivel = ultimoNivel;
	}

	@Override
	public Selecionavel selecionarVerificar(Selecionavel sel)
			throws AplicacaoException {
		return (sel != null && ((ExClassificacao) sel).isIntermediaria()) ? null
				: sel;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}
}
