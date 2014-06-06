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
 *
 */
package br.gov.jfrj.webwork.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExSituacaoConfiguracao;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.ExVia;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.ex.bl.ExConfiguracaoComparator;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.libs.webwork.DpCargoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.libs.webwork.DpLotacaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpPessoaSelecao;

import com.opensymphony.xwork.Action;

public class ExConfiguracaoAction extends ExActionSupport {

	private List<Object[]> itens = new ArrayList<Object[]>();

	private HashSet<ExConfiguracao> publicadores;

	private Long id;

	private Long idOrgaoUsu;
	
	private Long idOrgaoObjeto;

	private DpCargoSelecao cargoSel;

	private DpFuncaoConfiancaSelecao funcaoSel;

	private Long idTpMov;

	private Long idTpDoc;
	
	private Long idTpFormaDoc;

	private Integer tipoPublicador;

	private Integer idFormaDoc;

	private Long idMod;

	private ExClassificacaoSelecao classificacaoSel;

	private Long idVia;

	private Long idNivelAcesso;

	private Long idSituacao;

	private Long idTpConfiguracao;

	private DpPessoaSelecao pessoaSel;

	private DpLotacaoSelecao lotacaoSel;

	// private HashMap<String, List<ExConfiguracao>> configsPorModelo;

	public ExConfiguracaoAction() {
		classificacaoSel = new ExClassificacaoSelecao();
		pessoaSel = new DpPessoaSelecao();
		lotacaoSel = new DpLotacaoSelecao();
		cargoSel = new DpCargoSelecao();
		funcaoSel = new DpFuncaoConfiancaSelecao();
		tipoPublicador = 2;
		setPublicadores(new HashSet<ExConfiguracao>());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ExConfiguracao daoCon(long id) {
		return dao().consultar(id, ExConfiguracao.class, false);
	}

	public String aEditar() throws Exception {

		ExConfiguracao config = new ExConfiguracao();

		if (getId() != null) {
			config = daoCon(getId());
		}

		escreverForm(config);

		return Action.SUCCESS;
	}

	private void lerForm(ExConfiguracao c) throws Exception {

		if (getIdOrgaoUsu() != null && getIdOrgaoUsu() != 0) {
			c.setOrgaoUsuario(dao().consultar(getIdOrgaoUsu(),
					CpOrgaoUsuario.class, false));
		} else
			c.setOrgaoUsuario(null);

		if (getIdTpMov() != null && getIdTpMov() != 0) {
			c.setExTipoMovimentacao(dao().consultar(getIdTpMov(),
					ExTipoMovimentacao.class, false));
		} else
			c.setExTipoMovimentacao(null);

		if (getIdTpDoc() != null && getIdTpDoc() != 0) {
			c.setExTipoDocumento(dao().consultar(getIdTpDoc(),
					ExTipoDocumento.class, false));
		} else
			c.setExTipoDocumento(null);
		
		if (getIdMod() != null && getIdMod() != 0) {
			c.setExModelo(dao().consultar(getIdMod(), ExModelo.class, false));
			c.setExFormaDocumento(null);
			c.setExTipoFormaDoc(null);
		} else {
			c.setExModelo(null);
			if (getIdFormaDoc() != null && getIdFormaDoc() != 0) {
				c.setExFormaDocumento(dao().consultar(getIdFormaDoc(),
						ExFormaDocumento.class, false));
				c.setExTipoFormaDoc(null);
			} else {
				c.setExFormaDocumento(null);
				if (getIdTpFormaDoc() != null && getIdTpFormaDoc() != 0){
					c.setExTipoFormaDoc(dao().consultar(getIdTpFormaDoc(), ExTipoFormaDoc.class, false));			
				} else
					c.setExTipoFormaDoc(null);
			}
			
		}			

		if (getIdNivelAcesso() != null && getIdNivelAcesso() != 0) {
			c.setExNivelAcesso(dao().consultar(getIdNivelAcesso(),
					ExNivelAcesso.class, false));
		} else
			c.setExNivelAcesso(null);

		if (getIdSituacao() != null && getIdSituacao() != 0) {
			c.setCpSituacaoConfiguracao(dao().consultar(getIdSituacao(),
					CpSituacaoConfiguracao.class, false));
		} else
			c.setCpSituacaoConfiguracao(null);

		if (getIdTpConfiguracao() != null && getIdTpConfiguracao() != 0) {
			c.setCpTipoConfiguracao(dao().consultar(getIdTpConfiguracao(),
					CpTipoConfiguracao.class, false));
		} else
			c.setCpTipoConfiguracao(null);

		if (getPessoaSel().getId() != null && getPessoaSel().getId() != 0) {
			c.setDpPessoa(daoPes(getPessoaSel().getId()));
		} else
			c.setDpPessoa(null);

		if (getLotacaoSel().getId() != null && getLotacaoSel().getId() != 0) {
			c.setLotacao(daoLot(getLotacaoSel().getId()));
		} else
			c.setLotacao(null);

		if (getCargoSel().getId() != null && getCargoSel().getId() != 0) {
			c.setCargo(dao().consultar(getCargoSel().getId(), DpCargo.class,
					false));
		} else
			c.setCargo(null);

		if (getFuncaoSel().getId() != null && getFuncaoSel().getId() != 0) {
			c.setFuncaoConfianca(dao().consultar(getFuncaoSel().getId(),
					DpFuncaoConfianca.class, false));
		} else
			c.setFuncaoConfianca(null);

		if (getClassificacaoSel().getId() != null
				&& getClassificacaoSel().getId() != 0) {
			c.setExClassificacao(dao().consultar(getClassificacaoSel().getId(),
					ExClassificacao.class, false));
		} else
			c.setExClassificacao(null);
		
		if (getIdOrgaoObjeto() != null && getIdOrgaoObjeto() != 0) {
			c.setOrgaoObjeto(dao().consultar(getIdOrgaoObjeto(),
					CpOrgaoUsuario.class, false));
		} else
			c.setOrgaoObjeto(null);
	}

	private void escreverForm(ExConfiguracao c) throws Exception {
		if (c.getOrgaoUsuario() != null)
			setIdOrgaoUsu(c.getOrgaoUsuario().getIdOrgaoUsu());

		if (c.getExTipoMovimentacao() != null)
			setIdTpMov(c.getExTipoMovimentacao().getIdTpMov());

		if (c.getExTipoDocumento() != null)
			setIdTpDoc(c.getExTipoDocumento().getIdTpDoc());

		if (c.getExTipoFormaDoc() != null)
			setIdTpFormaDoc(c.getExTipoFormaDoc().getIdTipoFormaDoc());

		if (c.getExFormaDocumento() != null)
			setIdFormaDoc(c.getExFormaDocumento().getIdFormaDoc());

		if (c.getExModelo() != null)
			setIdMod(c.getExModelo().getIdMod());

		if (c.getExNivelAcesso() != null)
			setIdNivelAcesso(c.getExNivelAcesso().getIdNivelAcesso());

		if (c.getCpSituacaoConfiguracao() != null)
			setIdSituacao(c.getCpSituacaoConfiguracao().getIdSitConfiguracao());

		if (c.getCpTipoConfiguracao() != null)
			setIdTpConfiguracao(c.getCpTipoConfiguracao().getIdTpConfiguracao());

		if (c.getDpPessoa() != null) {
			getPessoaSel().buscarPorObjeto(c.getDpPessoa());
		}

		if (c.getLotacao() != null) {
			getLotacaoSel().buscarPorObjeto(c.getLotacao());
		}

		if (c.getCargo() != null) {
			getCargoSel().buscarPorObjeto(c.getCargo());
		}

		if (c.getFuncaoConfianca() != null) {
			getFuncaoSel().buscarPorObjeto(c.getFuncaoConfianca());
		}

		if (c.getExClassificacao() != null) {
			getClassificacaoSel().buscarPorObjeto(c.getExClassificacao());
		}
		
		if (c.getOrgaoObjeto() != null)
			setIdOrgaoObjeto(c.getOrgaoObjeto().getIdOrgaoUsu());
	}
	
	public Long getIdTpFormaDoc() {
		return idTpFormaDoc;
	}

	public void setIdTpFormaDoc(Long idTpFormaDoc) {
		this.idTpFormaDoc = idTpFormaDoc;
	}

	public List<ExTipoFormaDoc> getTiposFormaDoc() throws Exception {
		List<ExTipoFormaDoc> lista = new ArrayList<ExTipoFormaDoc>();
		return dao().listarExTiposFormaDoc();
	}

	public List<ExModelo> getModelos() throws Exception {
		ExFormaDocumento forma = null;
		if (getIdFormaDoc() != null && getIdFormaDoc() != 0)
			forma = dao().consultar(this.getIdFormaDoc(),
					ExFormaDocumento.class, false);

		return Ex
				.getInstance()
				.getBL()
				.obterListaModelos(forma, false, "Todos", false, getTitular(),
						getLotaTitular(), false);
	}

	public String aEditarGravar() throws Exception {
		assertAcesso("FE:Ferramentas;CFG:Configurações");
		
		if(getIdTpConfiguracao() == null || getIdTpConfiguracao() == 0)
			throw new AplicacaoException("Tipo de configuracao não informado");
		
		if(getIdSituacao() == null || getIdSituacao() == 0)
			throw new AplicacaoException("Situação de Configuracao não informada");
		
//		if (!Ex.getInstance().getConf().podePorConfiguracao(getTitular(),
//				getLotaTitular(), CpTipoConfiguracao.TIPO_CONFIG_CONFIGURAR)
//				&& !(Ex.getInstance().getConf().podePorConfiguracao(
//						getTitular(), getLotaTitular(),
//						CpTipoConfiguracao.TIPO_CONFIG_DEFINIR_PUBLICADORES)
//						&& param("define_publicadores") != null && param(
//						"define_publicadores").equals("sim"))
//				&& !(Ex.getInstance()
//					   .getConf()
//					   .podePorConfiguracao(
//								getTitular(),
//								getLotaTitular(),
//								CpTipoConfiguracao.TIPO_CONFIG_GERENCIAR_PUBLICACAO_BOLETIM)
//						&& param("gerencia_publicacao") != null && param(
//						"gerencia_publicacao").equals("sim")))
//			throw new AplicacaoException("Operação restrita");

		ExConfiguracao config;
		if (getId() == null)
			config = new ExConfiguracao();
		else
			config = daoCon(getId());
 
		lerForm(config);

		try {
			dao().iniciarTransacao();
			config.setHisDtIni(dao().consultarDataEHoraDoServidor());
			dao().gravarComHistorico(config, getIdentidadeCadastrante());
			dao().commitTransacao();
			// ExConfiguracaoBL.getHashListas().remove(
			// config.getCpTipoConfiguracao().getIdTpConfiguracao());
		} catch (final Exception e) {
			dao().rollbackTransacao();
			throw new AplicacaoException("Erro na gravação", 0, e);
		}

		return Action.SUCCESS;
	}

	public String aExcluir() throws Exception {
		assertAcesso("FE:Ferramentas;CFG:Configurações");

		if (getId() != null) {
			try {
				dao().iniciarTransacao();
				ExConfiguracao config = daoCon(getId());
				config.setHisDtFim(dao().consultarDataEHoraDoServidor());
				dao().gravarComHistorico(config, getIdentidadeCadastrante());
				dao().commitTransacao();
				// ExConfiguracaoBL.getHashListas().remove(
				// config.getCpTipoConfiguracao().getIdTpConfiguracao());
			} catch (final Exception e) {
				dao().rollbackTransacao();
				throw new AplicacaoException("Erro na gravação", 0, e);
			}
		} else
			throw new AplicacaoException("ID não informada");

		return Action.SUCCESS;
	}

	public String aAlterarSituacaoDefault() {
		return "";
	}

	public String aListar() throws Exception {
		assertAcesso("FE:Ferramentas;CFG:Configurações");

	    return Action.SUCCESS;
	}
	
	public String aListarCadastradas() throws Exception {        
		assertAcesso("FE:Ferramentas;CFG:Configurações");
		
		ExConfiguracao config = new ExConfiguracao();	
		
		if (getIdTpConfiguracao() != null && getIdTpConfiguracao() != 0) {
			config.setCpTipoConfiguracao(dao().consultar(getIdTpConfiguracao(),
					CpTipoConfiguracao.class, false));
		} else {			
			getRequest().setAttribute("err", "Tipo de configuração não informado");
			return "ERRO";
		}
			
		if (getIdOrgaoUsu() != null && getIdOrgaoUsu() != 0) {
			config.setOrgaoUsuario(dao().consultar(getIdOrgaoUsu(),
					CpOrgaoUsuario.class, false));
		} else
			config.setOrgaoUsuario(null);
		
		if (getIdTpMov() != null && getIdTpMov() != 0) {
			config.setExTipoMovimentacao(dao().consultar(getIdTpMov(),
					ExTipoMovimentacao.class, false));
		} else
			config.setExTipoMovimentacao(null);
		
		if (getIdMod() != null && getIdMod() != 0) {
			config.setExModelo(dao().consultar(getIdMod(),
					ExModelo.class, false));
		} else
			config.setExTipoMovimentacao(null);
		
												
		List<ExConfiguracao> listConfig = Ex.getInstance().getConf().buscarConfiguracoesVigentes(config);
		 
		Collections.sort(listConfig, new ExConfiguracaoComparator());
		
		this.getRequest().setAttribute("listConfig", listConfig);
		this.getRequest().setAttribute("tpConfiguracao", config.getCpTipoConfiguracao());
				
			
		return Action.SUCCESS;
	}

	public Long getIdTpMov() {
		return idTpMov;
	}

	public String aDefinirPublicadores() throws Exception {
		if (!Ex.getInstance().getConf().podePorConfiguracao(getTitular(),
				getLotaTitular(),
				CpTipoConfiguracao.TIPO_CONFIG_DEFINIR_PUBLICADORES))
			throw new AplicacaoException("Operação restrita");

		TreeSet<CpConfiguracao> listaConfigs = Ex.getInstance().getConf()
				.getListaPorTipo(CpTipoConfiguracao.TIPO_CONFIG_MOVIMENTAR);

		if (listaConfigs != null)
			for (CpConfiguracao config : listaConfigs) {
				if (config instanceof ExConfiguracao
						&& ((ExConfiguracao) config).getExTipoMovimentacao() != null
						&& ((ExConfiguracao) config).getExTipoMovimentacao()
								.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO)
					getPublicadores().add(((ExConfiguracao) config));

			}

		return Action.SUCCESS;
	}

	public String aGerenciarPublicacaoBoletim() throws Exception {
		if (!Ex.getInstance().getConf().podePorConfiguracao(getTitular(),
				getLotaTitular(),
				CpTipoConfiguracao.TIPO_CONFIG_GERENCIAR_PUBLICACAO_BOLETIM))
			throw new AplicacaoException("Operação restrita");

		TreeSet<CpConfiguracao> listaConfigs = Ex.getInstance().getConf()
				.getListaPorTipo(CpTipoConfiguracao.TIPO_CONFIG_MOVIMENTAR);

		if (listaConfigs != null)
			for (CpConfiguracao cfg : listaConfigs) {
				if (!(cfg instanceof ExConfiguracao))
					continue;
				ExConfiguracao config = (ExConfiguracao) cfg;
				if (config.getExTipoMovimentacao() != null
						&& config.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO_BOLETIM) {

					if ((config.getDpPessoa() != null && getTitular() != null && config
							.getDpPessoa().getOrgaoUsuario().getId() == getTitular()
							.getOrgaoUsuario().getId())
							|| (config.getLotacao() != null
									&& getLotaTitular() != null && config
									.getLotacao().getOrgaoUsuario().getId() == getLotaTitular()
									.getOrgaoUsuario().getId()))
						getPublicadores().add(config);
				}
			}

		for (ExConfiguracao c : getPublicadores()) {

			String nomeMod = "";
			if (c.getExModelo() != null) {
				nomeMod = c.getExModelo().getNmMod();
				if (!c.getExModelo().getExFormaDocumento().getDescrFormaDoc()
						.equals(nomeMod))
					nomeMod = c.getExModelo().getExFormaDocumento()
							.getDescrFormaDoc()
							+ " -> " + nomeMod;
			} else {
				if (c.getExFormaDocumento() != null)
					nomeMod = c.getExFormaDocumento().getDescrFormaDoc();
				else
					nomeMod = "[Todos os modelos]";
			}

			Object[] entrada = null;

			for (Object[] obj : itens) {
				if (obj[0].equals(nomeMod))
					entrada = obj;
			}

			if (entrada == null) {
				entrada = new Object[2];
				entrada[0] = nomeMod;
				entrada[1] = new ArrayList<ExConfiguracao>();
				itens.add(entrada);
			}

			((ArrayList<ExConfiguracao>) entrada[1]).add(c);
		}

		/*
		 * 
		 * 
		 * setIdFormaDoc(3);
		 * 
		 * setConfigsPorModelo(new HashMap<String, List<ExConfiguracao>>());
		 * 
		 * for (ExConfiguracao c : getPublicadores()){ ExModelo mod =
		 * c.getExModelo(); if (!getConfigsPorModelo().containsKey(mod))
		 * getConfigsPorModelo().put(mod.getNmMod(), new
		 * ArrayList<ExConfiguracao>()); getConfigsPorModelo().get(mod).add(c);
		 * }
		 */
		return Action.SUCCESS;
	}

	public void setIdTpMov(Long idTpMov) {
		this.idTpMov = idTpMov;
	}

	public Long getIdTpDoc() {
		return idTpDoc;
	}

	public void setIdTpDoc(Long idTpDoc) {
		this.idTpDoc = idTpDoc;
	}

	public Integer getIdFormaDoc() {
		return idFormaDoc;
	}

	public void setIdFormaDoc(Integer idFormaDoc) {
		this.idFormaDoc = idFormaDoc;
	}

	public Long getIdMod() {
		return idMod;
	}

	public void setIdMod(Long idMod) {
		this.idMod = idMod;
	}

	public ExClassificacaoSelecao getClassificacaoSel() {
		return classificacaoSel;
	}

	public void setClassificacaoSel(ExClassificacaoSelecao exClassificacaoSel) {
		this.classificacaoSel = exClassificacaoSel;
	}

	public Long getIdVia() {
		return idVia;
	}

	public void setIdVia(Long idVia) {
		this.idVia = idVia;
	}

	public Long getIdNivelAcesso() {
		return idNivelAcesso;
	}

	public void setIdNivelAcesso(Long idNivelAcesso) {
		this.idNivelAcesso = idNivelAcesso;
	}

	public Long getIdSituacao() {
		return idSituacao;
	}

	public void setIdSituacao(Long idSituacao) {
		this.idSituacao = idSituacao;
	}

	public Long getIdTpConfiguracao() {
		return idTpConfiguracao;
	}

	public void setIdTpConfiguracao(Long idTpConfiguracao) {
		this.idTpConfiguracao = idTpConfiguracao;
	}

	public DpPessoaSelecao getPessoaSel() {
		return pessoaSel;
	}

	public void setPessoaSel(DpPessoaSelecao pessoaSel) {
		this.pessoaSel = pessoaSel;
	}

	public DpLotacaoSelecao getLotacaoSel() {
		return lotacaoSel;
	}

	public void setLotacaoSel(DpLotacaoSelecao lotacaoSel) {
		this.lotacaoSel = lotacaoSel;
	}

	public void setItens(List<Object[]> itens) {
		this.itens = itens;
	}

	public Long getIdOrgaoUsu() {
		return idOrgaoUsu;
	}

	public void setIdOrgaoUsu(Long idOrgaoUsu) {
		this.idOrgaoUsu = idOrgaoUsu;
	}

	public List<ExNivelAcesso> getListaNivelAcesso() throws Exception {

		return dao().listarOrdemNivel();
	}

	@SuppressWarnings("all")
	public Set<ExTipoMovimentacao> getListaTiposMovimentacao() throws Exception {
		TreeSet<ExTipoMovimentacao> s = new TreeSet<ExTipoMovimentacao>(
				new Comparator() {

					public int compare(Object o1, Object o2) {
						return ((ExTipoMovimentacao) o1)
								.getDescrTipoMovimentacao().compareTo(
										((ExTipoMovimentacao) o2)
												.getDescrTipoMovimentacao());
					}

				});

		s.addAll(dao().listarExTiposMovimentacao());

		return s;
	}

	@SuppressWarnings("all")
	public Set<ExModelo> getListaModelos() throws Exception {
		TreeSet<ExModelo> s = new TreeSet<ExModelo>(new Comparator() {

			public int compare(Object o1, Object o2) {
				return ((ExModelo) o1).getNmMod().compareTo(
						((ExModelo) o2).getNmMod());
			}

		});

		s.addAll(dao().listarExModelos());

		return s;
	}

	@SuppressWarnings("all")
	public Set<ExModelo> getListaModelosPorForma() throws Exception {
		if (getIdFormaDoc() != null && getIdFormaDoc() != 0) {
			ExFormaDocumento forma = ExDao.getInstance().consultar(
					getIdFormaDoc(), ExFormaDocumento.class, false);
			return forma.getExModeloSet();
		}
		return getListaModelos();
	}

	@SuppressWarnings("all")
	public Set<ExFormaDocumento> getListaFormas() throws Exception {
		ExBL bl = Ex.getInstance().getBL();
		return bl.obterFormasDocumento(bl.obterListaModelos(null, false, null, false, null, null, false), null, null);
	}

	public List<ExTipoDocumento> getListaTiposDocumento() throws Exception {
		return dao().listarExTiposDocumento();
	}
	
	
	@SuppressWarnings("all")
	public Set<CpSituacaoConfiguracao> getListaSituacao() throws Exception {
		TreeSet<CpSituacaoConfiguracao> s = new TreeSet<CpSituacaoConfiguracao>(
				new Comparator() {

					public int compare(Object o1, Object o2) {
						return ((CpSituacaoConfiguracao) o1)
								.getDscSitConfiguracao().compareTo(
										((CpSituacaoConfiguracao) o2)
												.getDscSitConfiguracao());
					}

				});

		s.addAll(dao().listarSituacoesConfiguracao());

		return s;
	}

	@SuppressWarnings("all")
	public Set<ExSituacaoConfiguracao> getListaSituacaoPodeNaoPode()
			throws Exception {
		HashSet<ExSituacaoConfiguracao> s = new HashSet<ExSituacaoConfiguracao>();

		s.add(ExDao.getInstance().consultar(1L, ExSituacaoConfiguracao.class,
				false));
		s.add(ExDao.getInstance().consultar(2L, ExSituacaoConfiguracao.class,
				false));

		return s;
	}

	@SuppressWarnings("all")
	public Set<CpTipoConfiguracao> getListaTiposConfiguracao() throws Exception {
		TreeSet<CpTipoConfiguracao> s = new TreeSet<CpTipoConfiguracao>(
				new Comparator() {

					public int compare(Object o1, Object o2) {
						return ((CpTipoConfiguracao) o1).getDscTpConfiguracao()
								.compareTo(
										((CpTipoConfiguracao) o2)
												.getDscTpConfiguracao());
					}

				});

		s.addAll(dao().listarTiposConfiguracao());

		return s;
	}

	public Map<Integer, String> getListaTipoPublicador() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, "Matrícula");
		map.put(2, "Órgão Integrado");
		return map;
	}

	public List<ExVia> getListaVias() throws Exception {
		return dao().listarExVias();
	}

	public List<Object[]> getItens() {
		return itens;
	}

	public DpCargoSelecao getCargoSel() {
		return cargoSel;
	}

	public void setCargoSel(DpCargoSelecao dpCargoSel) {
		this.cargoSel = dpCargoSel;
	}

	public DpFuncaoConfiancaSelecao getFuncaoSel() {
		return funcaoSel;
	}

	public void setFuncaoSel(DpFuncaoConfiancaSelecao dpFuncaoSel) {
		this.funcaoSel = dpFuncaoSel;
	}

	public HashSet<ExConfiguracao> getPublicadores() {
		return publicadores;
	}

	public void setPublicadores(HashSet<ExConfiguracao> publicadores) {
		this.publicadores = publicadores;
	}

	public Integer getTipoPublicador() {
		return tipoPublicador;
	}

	public void setTipoPublicador(Integer tipoPublicador) {
		this.tipoPublicador = tipoPublicador;
	}

	public Long getIdOrgaoObjeto() {
		return idOrgaoObjeto;
	}

	public void setIdOrgaoObjeto(Long idOrgaoObjeto) {
		this.idOrgaoObjeto = idOrgaoObjeto;
	}

	/*
	 * public void setConfigsPorModelo(HashMap<String, List<ExConfiguracao>>
	 * configsPorModelo) { this.configsPorModelo = configsPorModelo; }
	 * 
	 * public HashMap<String, List<ExConfiguracao>> getConfigsPorModelo() {
	 * return configsPorModelo; }
	 */
}
