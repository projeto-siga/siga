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

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExTemporalidade;
import br.gov.jfrj.siga.ex.ExTipoDestinacao;
import br.gov.jfrj.siga.ex.ExVia;
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
	
	private String acao;

	//classificacao
	private String assunto;
	private String atividade;
	private String classe;
	private ExClassificacaoSelecao classificacaoSel;
	private String nome;
	private String subclasse;
	private Boolean ultimoNivel;
	private  String codificacaoAntiga;
	private ExClassificacao exClass;
	private String codificacao;
	private String descrClassificacao;
	private String obs;

	//via
	private String codigoVia;
	private Long idVia;

	private Long idDestino;
	private Long idDestinacaoFinal;
	private Long idTemporalidadeArqCorr;
	private Long idTemporalidadeArqInterm;
	

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

	public String aListar(){
		return SUCCESS;
	}

	public String aEditar(){
		if (getCodificacao()!=null){
			exClass = buscarExClassificacao(getCodificacao());
		}
		return SUCCESS;
	}

	private ExClassificacao buscarExClassificacao(String codificacao) {
		exClass = new ExClassificacao();
		exClass.setSigla(codificacao);
		return ExDao.getInstance().consultarPorSigla(exClass);
	}

	public String aGravar() throws AplicacaoException{
		dao().iniciarTransacao();
		exClass = buscarExClassificacao(getCodificacao());
		if (exClass == null){
			exClass = new ExClassificacao();
		}
		
		if(exClass.getId()==null){
			
			ExClassificacao exClassAntiga = buscarExClassificacao(getCodificacaoAntiga());
			lerForm(exClass);
			
			if(exClassAntiga!=null && !exClassAntiga.getCodificacao().equals(getCodificacao())){
				moverClassificacao(exClass,exClassAntiga);
			}else{
				//novaClassificacao
				dao().gravarComHistorico(exClass,null, dao().consultarDataEHoraDoServidor(), getIdentidadeCadastrante());
			}
			
		}else{
			alterarClassificacaoExistente();
		}
		

		
		setMensagem("Classificação salva!");
		return SUCCESS;
	}

	private void alterarClassificacaoExistente() throws AplicacaoException {
		ExClassificacao exClassNovo = new ExClassificacao();
		try {
			PropertyUtils.copyProperties(exClassNovo, exClass);
			//novo id
			exClassNovo.setId(null);
			//objeto collection deve ser diferente (mas com mesmos elementos), senão ocorre exception
			//HibernateException:Found shared references to a collection
			Set<ExVia> setExVia = new HashSet<ExVia>();
			exClassNovo.setExViaSet(setExVia);
			
			Set<ExModelo> setExModelo = new HashSet<ExModelo>();
			exClassNovo.setExModeloSet(setExModelo);
			
			lerForm(exClassNovo);
			
			dao().gravarComHistorico(exClassNovo, exClass, dao().consultarDataEHoraDoServidor(), getIdentidadeCadastrante());
			
			copiarReferencias(exClassNovo,exClass);

		} catch (Exception e) {
			throw new AplicacaoException(
					"Erro ao copiar as propriedades do modelo anterior.");
		}
		dao().commitTransacao();

		
	}

	private void moverClassificacao(ExClassificacao exClassNova, ExClassificacao exClassAntiga)
			throws AplicacaoException {
		exClassNova.setHisIdIni(exClassAntiga.getHisIdIni());
		dao().gravarComHistorico(exClassNova,exClassAntiga, dao().consultarDataEHoraDoServidor(), getIdentidadeCadastrante());
		copiarReferencias(exClassNova,exClassAntiga);
	}

	private void copiarReferencias(ExClassificacao exClassNova, ExClassificacao exClassAntiga)
			throws AplicacaoException {
		copiarVias(exClassNova, exClassAntiga);
		copiarModelos(exClassNova, exClassAntiga);
	}

	private void copiarModelos(ExClassificacao exClassNovo,
			ExClassificacao exClassAntigo) throws AplicacaoException {
		try{
			for (ExModelo modAntigo: exClassAntigo.getExModeloSet()) {
				ExModelo modNovo = new ExModelo();
				
				PropertyUtils.copyProperties(modNovo, modAntigo);
				modNovo.setIdMod(null);
				modNovo.setExClassificacao(exClassNovo);

				dao().gravarComHistorico(modNovo, modAntigo, null, getIdentidadeCadastrante());
				if(exClassNovo.getExModeloSet()==null){
					exClassNovo.setExModeloSet(new HashSet<ExModelo>());
				}
				exClassNovo.getExModeloSet().add(modNovo);
				
			}
		}catch (Exception e) {
			throw new AplicacaoException("Não foi possível fazer cópia dos modelos!");
		}

	}

	private void copiarVias(ExClassificacao exClassNovo,ExClassificacao exClassAntigo)
			throws AplicacaoException {
		try{
			for (ExVia viaAntiga: exClassAntigo.getExViaSet()) {
				ExVia viaNova = new ExVia();
				
				PropertyUtils.copyProperties(viaNova, viaAntiga);
				viaNova.setId(null);
				viaNova.setExClassificacao(exClassNovo);

				dao().gravarComHistorico(viaNova, viaAntiga, null, getIdentidadeCadastrante());
				if(exClassNovo.getExViaSet()==null){
					exClassNovo.setExViaSet(new HashSet<ExVia>());
				}
				exClassNovo.getExViaSet().add(viaNova);
				
			}
		}catch (Exception e) {
			throw new AplicacaoException("Não foi possível fazer cópia das vias!");
		}
	}
	
	public String aExcluir() throws AplicacaoException{
		dao().iniciarTransacao();
		exClass = buscarExClassificacao(codificacao);
		Date dt = dao().consultarDataEHoraDoServidor();
		for (ExVia exVia : exClass.getExViaSet()) {
			dao().excluirComHistorico(exVia, dt, getIdentidadeCadastrante());
		}
		dao().excluirComHistorico(exClass, dt, getIdentidadeCadastrante());
		dao().commitTransacao();
		return SUCCESS;
	}
	
	public String aGravarVia() throws AplicacaoException{
		verificarParamsVia();
		dao().iniciarTransacao();

		setExClass(buscarExClassificacao(getCodificacao()));
		ExVia exVia = null;
		
		if (getIdVia() ==null){
			//nova via

			exVia = new ExVia();
			lerFormVia(exVia);
			exVia.setCodVia(String.valueOf(getNumeroDeVias()+1));
			dao().gravarComHistorico(exVia,null,null,getIdentidadeCadastrante());
		}else{
			//alterar via existente
			exVia = dao().consultar(getIdVia(),ExVia.class,false);
			ExVia exViaNova = new ExVia();
			try {
				PropertyUtils.copyProperties(exViaNova, exVia);
				//novo id
				exViaNova.setId(null);
				
				lerFormVia(exViaNova);
				
				dao().gravarComHistorico(exViaNova, exVia, dao().consultarDataEHoraDoServidor(), getIdentidadeCadastrante());
				
			} catch (Exception e) {
				throw new AplicacaoException(
						"Erro ao copiar as propriedades da via anterior.");
			}

		}
		dao().commitTransacao();
		return SUCCESS;
	}
	
	private void verificarParamsVia() throws AplicacaoException {
		if (getIdDestino()==null || getIdDestino()<=0){
			throw new AplicacaoException("A destinação da via deve ser definida!");
		}
	}

	public String aExcluirVia() throws AplicacaoException{
		dao().iniciarTransacao();
		
		ExVia exVia = dao().consultar(getIdVia(), ExVia.class, false);
		dao().excluirComHistorico(exVia, null,getIdentidadeCadastrante());
		
		dao().commitTransacao();
		return SUCCESS;
	}

	private void lerForm(ExClassificacao c) {
		c.setCodificacao(getCodificacao());
		c.setDescrClassificacao(getDescrClassificacao());
		c.setObs(getObs());
	}

	private void lerFormVia(ExVia exVia) {
		
		ExTipoDestinacao destino = !getIdDestino().equals(-1L)?dao().consultar(getIdDestino(), ExTipoDestinacao.class, false):null; 
		ExTipoDestinacao destFinal = !getIdDestinacaoFinal().equals(-1L)?dao().consultar(getIdDestinacaoFinal(), ExTipoDestinacao.class, false):null; 
		ExTemporalidade tempCorrente = !getIdTemporalidadeArqCorr().equals(-1L)?dao().consultar(getIdTemporalidadeArqCorr(), ExTemporalidade.class, false):null; 
		ExTemporalidade tempInterm = !getIdTemporalidadeArqInterm().equals(-1L)?dao().consultar(getIdTemporalidadeArqInterm(), ExTemporalidade.class, false):null; 

		exVia.setExClassificacao(getExClass());
		exVia.setExTipoDestinacao(destino);
		exVia.setExDestinacaoFinal(destFinal);
		exVia.setTemporalidadeCorrente(tempCorrente);
		exVia.setTemporalidadeIntermediario(tempInterm);
		exVia.setCodVia(getCodigoVia());
		exVia.setObs(getObs());

	}

	public ExClassificacao getExClass() {
		return exClass;
	}
	
	public void setExClass(ExClassificacao exClass) {
		this.exClass = exClass;
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
	
	public List<ExClassificacao> getClassificacaoVigente(){
		return ExDao.getInstance().consultarExClassificacaoVigente();
	}
	
	public List<ExTipoDestinacao> getListaExTipoDestinacao(){
		return ExDao.getInstance().listarTodos(ExTipoDestinacao.class);
	}
	
	public List<ExTemporalidade> getListaExTemporalidade(){
		return ExDao.getInstance().listarTodos(ExTemporalidade.class);
	}


	public void setCodificacao(String codificacao) {
		this.codificacao = codificacao;
	}

	public String getCodificacao() {
		return codificacao;
	}

	public void setDescrClassificacao(String descrClassificacao) {
		this.descrClassificacao = descrClassificacao;
	}

	public String getDescrClassificacao() {
		return descrClassificacao;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getObs() {
		return obs;
	}

	public void setCodificacaoAntiga(String codificacaoAntiga) {
		this.codificacaoAntiga = codificacaoAntiga;
	}

	public String getCodificacaoAntiga() {
		return codificacaoAntiga;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public String getAcao() {
		return acao;
	}
	
	public int getNumeroDeVias(){
		try{
			return exClass.getExViaSet().size();
		}catch (Exception e) {
			return 0;
		}
		
	}

	public void setIdVia(Long idVia) {
		this.idVia = idVia;
	}

	public Long getIdVia() {
		return idVia;
	}
	
	public Long getIdDestino() {
		return idDestino;
	}

	public void setIdDestino(Long idDestino) {
		this.idDestino = idDestino;
	}

	public Long getIdDestinacaoFinal() {
		return idDestinacaoFinal;
	}

	public void setIdDestinacaoFinal(Long idDestinacaoFinal) {
		this.idDestinacaoFinal = idDestinacaoFinal;
	}

	public void setIdTemporalidadeArqCorr(Long idTemporalidadeArqCorr) {
		this.idTemporalidadeArqCorr = idTemporalidadeArqCorr;
	}

	public Long getIdTemporalidadeArqCorr() {
		return idTemporalidadeArqCorr;
	}

	public void setIdTemporalidadeArqInterm(Long idTemporalidadeArqInterm) {
		this.idTemporalidadeArqInterm = idTemporalidadeArqInterm;
	}

	public Long getIdTemporalidadeArqInterm() {
		return idTemporalidadeArqInterm;
	}

	public void setCodigoVia(String codigoVia) {
		this.codigoVia = codigoVia;
	}

	public String getCodigoVia() {
		return codigoVia;
	}
	
	public String getMascaraEntrada(){
		return MascaraUtil.getInstance().getMascaraEntrada();
	}
	
	public String getMascaraSaida(){
		return MascaraUtil.getInstance().getMascaraSaida();
	}
	
}
