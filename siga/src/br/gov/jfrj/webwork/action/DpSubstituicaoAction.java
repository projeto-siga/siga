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
 * Criado em  23/11/2005
 *
 */
package br.gov.jfrj.webwork.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpPersonalizacao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpSubstituicao;
import br.gov.jfrj.siga.libs.webwork.DpLotacaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpPessoaSelecao;
import br.gov.jfrj.siga.libs.webwork.SigaActionSupport;

import com.opensymphony.xwork.Action;

public class DpSubstituicaoAction extends SigaActionSupport {

	private List itens;
	
	private List itensTitular;

	private Long id;

	private Long idTitular;

	private Integer tipoTitular;

	private Integer tipoSubstituto;

	private Integer idLotaTitular;

	private DpPessoaSelecao titularSel;

	private DpLotacaoSelecao lotaTitularSel;

	private DpPessoaSelecao substitutoSel;

	private DpLotacaoSelecao lotaSubstitutoSel;

	private String dtIniSubst;

	private String dtFimSubst;
	
	private String strBuscarFechadas;

	public Date getDtAtual() {
		return new Date();
	}

	public DpSubstituicaoAction() {
		lotaTitularSel = new DpLotacaoSelecao();
		lotaSubstitutoSel = new DpLotacaoSelecao();
		titularSel = new DpPessoaSelecao();
		substitutoSel = new DpPessoaSelecao();
		tipoTitular = 1;
		tipoSubstituto = 1;
	}

	public DpSubstituicao daoSub(long id) {
		return dao().consultar(id, DpSubstituicao.class, false);
	}

	public String aEditarSubstituto() throws Exception {
		if(Cp.getInstance()
				.getConf()
				.podePorConfiguracao(
						getCadastrante(),
						CpTipoConfiguracao.TIPO_CONFIG_CADASTRAR_QUALQUER_SUBST))
			{
				setStrBuscarFechadas("buscarFechadas=true");
				
			}else
				setStrBuscarFechadas("buscarFechadas=false");
		
		if (getId() != null) {
			DpSubstituicao subst = dao().consultar(getId(),
					DpSubstituicao.class, false);

			if (subst.getTitular() == null) {
				tipoTitular = 2;
				getLotaTitularSel().buscarPorObjeto(subst.getLotaTitular());
			} else {
				tipoTitular = 1;
				getTitularSel().buscarPorObjeto(subst.getTitular());
			}
			if (subst.getSubstituto() == null) {
				tipoSubstituto = 2;
				getLotaSubstitutoSel().buscarPorObjeto(
						subst.getLotaSubstituto());
			} else {
				tipoSubstituto = 1;
				getSubstitutoSel().buscarPorObjeto(subst.getSubstituto());
			}

			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			try {
				setDtFimSubst(df.format(subst.getDtFimSubst()));
			} catch (final Exception e) {
			}

			try {
				setDtIniSubst(df.format(subst.getDtIniSubst()));
			} catch (final Exception e) {
			}

		}
		return Action.SUCCESS;
	}

	public String aEditarSubstitutoGravar() throws Exception {

		DpSubstituicao subst = new DpSubstituicao();
		try {
			dao().iniciarTransacao();
			if (tipoTitular == 1) {
				if (getTitularSel().getId() == null)
					throw new AplicacaoException("Titular não informado");
				subst.setTitular(dao().consultar(getTitularSel().getId(),
						DpPessoa.class, false));
				if (subst.getTitular().getIdPessoa() != getCadastrante()
						.getIdPessoa()
						&& !Cp.getInstance()
								.getConf()
								.podePorConfiguracao(
										getCadastrante(),
										CpTipoConfiguracao.TIPO_CONFIG_CADASTRAR_QUALQUER_SUBST))
					/*
					 * && !getCadastrante().getSigla().equals("RJ13635")) &&
					 * getLotaTitular().getIdLotacaoIni() !=
					 * DpLotacao.ID_LOTACAO_INICIAL_SESAD_BASE_PRODUCAO &&
					 * getLotaTitular().getIdLotacaoIni() !=
					 * DpLotacao.ID_LOTACAO_INICIAL_SEADA_BASE_TESTE &&
					 * getLotaTitular().getIdLotacaoIni() !=
					 * DpLotacao.ID_LOTACAO_INICIAL_SEADA_BASE_PRODUCAO)
					 */
					throw new AplicacaoException(
							"Titular não permitido. Apenas o próprio usuário pode se definir como titular.");
				subst.setLotaTitular(subst.getTitular().getLotacao());
			} else {
				subst.setTitular(null);
				if (getLotaTitularSel().getId() == null)
					throw new AplicacaoException(
							"A lotação titular não foi informada");
				subst.setLotaTitular(dao().consultar(
						getLotaTitularSel().getId(), DpLotacao.class, false));
				if (subst.getLotaTitular().getIdLotacao() != getCadastrante()
						.getIdLotacao()
						&& !Cp.getInstance()
								.getConf()
								.podePorConfiguracao(
										getLotaTitular(),
										CpTipoConfiguracao.TIPO_CONFIG_CADASTRAR_QUALQUER_SUBST))// &&
					// !getCadastrante().getSigla().equals("RJ13635"))
					/*
					 * && getLotaTitular().getIdLotacaoIni() !=
					 * DpLotacao.ID_LOTACAO_INICIAL_SESAD_BASE_PRODUCAO &&
					 * getLotaTitular().getIdLotacaoIni() !=
					 * DpLotacao.ID_LOTACAO_INICIAL_SEADA_BASE_TESTE &&
					 * getLotaTitular().getIdLotacaoIni() !=
					 * DpLotacao.ID_LOTACAO_INICIAL_SEADA_BASE_PRODUCAO
					 */

					throw new AplicacaoException(
							"Lotação titular não permitida. Apenas um usuário da própria lotação pode defini-la como titular.");
			}
			if (tipoSubstituto == 1) {
				if (getSubstitutoSel().getId() == null)
					throw new AplicacaoException("Substituto não informado");
				subst.setSubstituto(daoPes(getSubstitutoSel().getId()));
				subst.setLotaSubstituto(subst.getSubstituto().getLotacao());
			} else {
				subst.setSubstituto(null);
				if (getLotaSubstitutoSel().getId() == null)
					throw new AplicacaoException(
							"A lotação do substituto não foi informada");
				subst.setLotaSubstituto(daoLot(getLotaSubstitutoSel().getId()));
			}
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			try {
				setDtIniSubst(getDtIniSubst());
				subst.setDtIniSubst(df.parse(getDtIniSubst() + " 00:00"));
			} catch (final ParseException e) {
				subst.setDtIniSubst(null);
			} catch (final NullPointerException e) {
				subst.setDtIniSubst(null);
			}
			try {
				subst.setDtFimSubst(df.parse(getDtFimSubst() + " 23:59"));
			} catch (final ParseException e) {
				subst.setDtFimSubst(null);
			} catch (final NullPointerException e) {
				subst.setDtFimSubst(null);
			}

			if (subst.getDtIniSubst() == null)
				subst.setDtIniSubst(new Date());

			subst.setDtIniRegistro(new Date());

			if (getId() != null) {
				DpSubstituicao substAntiga = daoSub(getId());
				substAntiga.setDtFimRegistro(new Date());
				substAntiga = dao().gravar(substAntiga);
				subst.setIdRegistroInicial(substAntiga.getIdRegistroInicial());
			}

			subst = dao().gravar(subst);

			if (subst.getIdRegistroInicial() == null)
				subst.setIdRegistroInicial(subst.getIdSubstituicao());

			subst = dao().gravar(subst);

			dao().commitTransacao();
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
		return Action.SUCCESS;
	}

	public String aExcluirSubstituto() throws Exception {

		if (getId() != null) {
			dao().iniciarTransacao();
			DpSubstituicao dpSub = daoSub(getId());
			dpSub.setDtFimRegistro(new Date());
			dpSub = dao().gravar(dpSub);
			dao().commitTransacao();
		} else
			throw new AplicacaoException("Não foi informada id");
		return Action.SUCCESS;
	}

	public String aListarSubstitutos() throws Exception {

		List<DpSubstituicao> todasSubst = new ArrayList<DpSubstituicao>();
		List<DpSubstituicao> substVigentes = new ArrayList<DpSubstituicao>();
		DpSubstituicao dpSubstituicao = new DpSubstituicao();
		dpSubstituicao.setTitular(getCadastrante());
		dpSubstituicao.setLotaTitular(getCadastrante().getLotacao());
	    todasSubst = dao().consultarOrdemData(dpSubstituicao);	
	    for (DpSubstituicao subst : todasSubst) {
	    	if (subst.getLotaSubstituto() != null && subst.getLotaSubstituto().isFechada())
	    		continue;
	    	if (subst.getSubstituto() != null && (subst.getSubstituto().isFechada() 
	    			|| subst.getSubstituto().isBloqueada()))
	    		continue;
	    	if (subst.isEmVoga()) {
	    		substVigentes.add(subst);	    		
	    	}
	    }		
		setItens(substVigentes);
		
		
		if (!getCadastrante().getId().equals(getTitular().getId())
				|| !getCadastrante().getLotacao().getId().equals(getLotaTitular().getId())) {
			// é uma substituição !
			dpSubstituicao.setTitular(getTitular());
			dpSubstituicao.setLotaTitular(getTitular().getLotacao());
			todasSubst = dao().consultarOrdemData(dpSubstituicao);	
			for (DpSubstituicao subst : todasSubst) {
		    	if (subst.getLotaSubstituto() != null && subst.getLotaSubstituto().isFechada())
		    		continue;
		    	if (subst.getSubstituto() != null && (subst.getSubstituto().isFechada() 
		    			|| subst.getSubstituto().isBloqueada()))
		    		continue;
		    	if (subst.isEmVoga()) {
		    		substVigentes.add(subst);	    		
		    	}
		    }	
			setItensTitular(substVigentes);			
		}
		
		/*
		 * TreeMap tree = new TreeMap(); for (Object dps : getItens()) {
		 * DpSubstituicao trueDps = (DpSubstituicao) dps;
		 * tree.put(trueDps.getDtIniSubst(), trueDps); } setItens(new
		 * ArrayList(tree.values()));
		 */

		return Action.SUCCESS;
	}

	
	public String aSubstituirGravar() throws Exception {
		aFinalizar();
		if (getIdTitular() != null) {
			setTitular(daoPes(getIdTitular()));
			setTitular(dao().consultarPorIdInicial(getTitular().getIdInicial()));
			setLotaTitular(getTitular().getLotacao());
			setLotaTitular(dao().consultarPorIdInicial(DpLotacao.class,
					getTitular().getLotacao().getIdInicial()));
		} else {
			setLotaTitular(daoLot(getIdLotaTitular()));
			setLotaTitular(dao().consultarPorIdInicialInclusiveLotacaoFechada(
					DpLotacao.class, getLotaTitular().getIdInicial()));
		}

		CpPersonalizacao per = dao().consultarPersonalizacao(getCadastrante());
		if (per == null) {
			per = new CpPersonalizacao();
		}
		per.setPessoa(getCadastrante());
		per.setPesSubstituindo(getTitular() != getCadastrante() ? getTitular()
				: null);
		per.setLotaSubstituindo(getLotaTitular() != getCadastrante()
				.getLotacao() ? getLotaTitular() : null);
		try {
			dao().iniciarTransacao();
			dao().gravar(per);
			dao().commitTransacao();
		} catch (Exception e) {
			dao().rollbackTransacao();
		}

		// setSharedContextAttribute("idTitular", getTitular().getIdPessoa());
		// setSharedContextAttribute("idLotaTitular", getLotaTitular()
		// .getIdLotacao());
		// getRequest().getSession().setAttribute("idTitular",
		// getTitular().getIdPessoa());
		// getRequest().getSession().setAttribute("idLotaTitular",
		// getLotaTitular().getIdLotacao());
		return Action.SUCCESS;
	}

	public String aFinalizar() throws Exception {
		CpPersonalizacao per = dao().consultarPersonalizacao(getCadastrante());

		if (per == null) {
			per = new CpPersonalizacao();
			per.setPessoa(getCadastrante());
		}

		per.setPesSubstituindo(null);
		per.setLotaSubstituindo(null);

		try {
			dao().iniciarTransacao();
			dao().gravar(per);
			dao().commitTransacao();
		} catch (Exception e) {
			dao().rollbackTransacao();
		}

		// setSharedContextAttribute("idTitular", null);
		// setSharedContextAttribute("idLotaTitular", null);
		// getRequest().getSession().setAttribute("idTitular", null);
		// getRequest().getSession().setAttribute("idLotaTitular", null);
		return Action.SUCCESS;
	}

//	public String aSimular() throws Exception {
//		Usuario usuarioLogado = daoUsu(getPrincipalProxy().getUserPrincipal()
//				.getName());
//
//		if (!Cp.getInstance().getComp().podeSimularUsuario(
//				usuarioLogado.getPessoa(),
//				usuarioLogado.getPessoa().getLotacao())) {
//			throw new AplicacaoException(
//					"Usuário não possui permissão de simular outro usuáio.");
//		}
//
//		return Action.SUCCESS;
//	}

	// public String aSimularGravar() throws Exception {
	// if (titularSel.getSigla() != null) {
	// Usuario usuarioSimular = daoUsu(titularSel.getSigla());
	// Usuario usuarioLogado = daoUsu(getPrincipalProxy()
	// .getUserPrincipal().getName());
	//
	// CpPersonalizacao per = dao().consultarPersonalizacao(
	// usuarioLogado.getPessoa());
	//
	// if (per == null) {
	// per = new CpPersonalizacao();
	// per.setPessoa(usuarioLogado.getPessoa());
	// }
	//
	// if (usuarioSimular == usuarioLogado)
	// per.setUsuarioSimulando(null);
	// else {
	// Usuario usu = dao().consultaUsuarioCadastrante(
	// usuarioSimular.getNmUsuario());
	// if (usu == null)
	// per.setUsuarioSimulando(null);
	// else
	// per.setUsuarioSimulando(usuarioSimular);
	// }
	//
	// try {
	// dao().iniciarTransacao();
	// dao().gravar(per);
	// dao().commitTransacao();
	// } catch (Exception e) {
	// if (SigaActionSupport.getLog().isErrorEnabled())
	// SigaActionSupport.getLog().error(
	// "Não foi possível simular o usuário: "
	// + usuarioSimular.getNmUsuario());
	// }
	// }
	//
	// return Action.SUCCESS;
	// }

	public String getDtFimSubst() {
		return dtFimSubst;
	}

	public String getDtIniSubst() {
		return dtIniSubst;
	}

	public Long getId() {
		return id;
	}

	public Integer getIdLotaTitular() {
		return idLotaTitular;
	}

	public Long getIdTitular() {
		return idTitular;
	}

	public List getItens() {
		return itens;
	}

	public Map<Integer, String> getListaTipoTitular() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, "Matrícula");
		map.put(2, "Órgão Integrado");
		return map;
	}

	public DpLotacaoSelecao getLotaSubstitutoSel() {
		return lotaSubstitutoSel;
	}

	public DpLotacaoSelecao getLotaTitularSel() {
		return lotaTitularSel;
	}

	public DpPessoaSelecao getSubstitutoSel() {
		return substitutoSel;
	}

	public Integer getTipoSubstituto() {
		return tipoSubstituto;
	}

	public Integer getTipoTitular() {
		return tipoTitular;
	}

	public DpPessoaSelecao getTitularSel() {
		return titularSel;
	}

	public void setDtFimSubst(String dtFimSubst) {
		this.dtFimSubst = dtFimSubst;
	}

	public void setDtIniSubst(String dtIniSubst) {
		this.dtIniSubst = dtIniSubst;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIdLotaTitular(Integer idLotaTitular) {
		this.idLotaTitular = idLotaTitular;
	}

	public void setIdTitular(Long idTitular) {
		this.idTitular = idTitular;
	}

	public void setItens(List itens) {
		this.itens = itens;
	}
	
	public void setItensTitular(List itens) {
		this.itens = itens;
	}

	public void setLotaSubstitutoSel(DpLotacaoSelecao lotaSubstitutoSel) {
		this.lotaSubstitutoSel = lotaSubstitutoSel;
	}

	public void setLotaTitularSel(DpLotacaoSelecao lotaTitularSel) {
		this.lotaTitularSel = lotaTitularSel;
	}

	public void setSubstitutoSel(DpPessoaSelecao substitutoSel) {
		this.substitutoSel = substitutoSel;
	}

	public void setTipoSubstituto(Integer tipoSubstituto) {
		this.tipoSubstituto = tipoSubstituto;
	}

	public void setTipoTitular(Integer tipoTitular) {
		this.tipoTitular = tipoTitular;
	}

	public String getStrBuscarFechadas() {
		return strBuscarFechadas;
	}

	public void setStrBuscarFechadas(String strBuscarFechadas) {
		this.strBuscarFechadas = strBuscarFechadas;
	}

	public void setTitularSel(DpPessoaSelecao titularSel) {
		this.titularSel = titularSel;
	}

}
