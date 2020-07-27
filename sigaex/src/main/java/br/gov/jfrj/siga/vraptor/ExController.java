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
package br.gov.jfrj.siga.vraptor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpVisualizacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExEstadoDoc;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExConfiguracaoBL;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.util.ExProcessadorModelo;

public class ExController extends SigaController {
	HttpServletResponse response;
	ServletContext context;

	static {
		if (Ex.getInstance().getBL().getProcessadorModeloJsp() == null) {
			Ex.getInstance().getBL().setProcessadorModeloJsp(new ExProcessadorModelo());
		}
	}

	/**
	 * @deprecated CDI eyes only
	 */
	public ExController() {
		super();
	}

	public ExController(HttpServletRequest request, HttpServletResponse response, ServletContext context, Result result, CpDao dao, SigaObjects so,
			EntityManager em) {
		super(request, result, dao, so, em);
		this.response = response;
		this.context = context;
	}

	protected void verificaNivelAcesso(ExMobil mob) {
		if (!Ex.getInstance().getComp().podeAcessarDocumento(getTitular(), getLotaTitular(), mob)) {
			throw new AplicacaoException("Acesso ao documento " + mob.getSigla() + " permitido somente a usuários autorizados. (" + getTitular().getSigla()
					+ "/" + getLotaTitular().getSiglaCompleta() + ")");
		}
	}

	protected  String getNomeServidor() {
		return getRequest().getServerName();
	}

	protected  String getNomeServidorComPorta() {
		if (getRequest().getServerPort() > 0)
			return getRequest().getServerName() + ":" + getRequest().getServerPort();
		return getRequest().getServerName();
	}

	protected  List<ExNivelAcesso> getListaNivelAcesso(ExTipoDocumento exTpDoc, ExFormaDocumento forma, ExModelo exMod, ExClassificacao classif) {
		List<ExNivelAcesso> listaNiveis = ExDao.getInstance().listarOrdemNivel();
		ArrayList<ExNivelAcesso> niveisFinal = new ArrayList<ExNivelAcesso>();
		Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

		ExConfiguracao config = new ExConfiguracao();
		CpTipoConfiguracao exTpConfig = new CpTipoConfiguracao();
		config.setDpPessoa(getTitular());
		config.setLotacao(getLotaTitular());
		config.setExTipoDocumento(exTpDoc);
		config.setExFormaDocumento(forma);
		config.setExModelo(exMod);
		config.setExClassificacao(classif);

		ExConfiguracao exConfiguracaoMin;
		exTpConfig.setIdTpConfiguracao(CpTipoConfiguracao.TIPO_CONFIG_NIVEL_ACESSO_MINIMO);
		config.setCpTipoConfiguracao(exTpConfig);
		try {
			exConfiguracaoMin = (ExConfiguracao) Ex.getInstance().getConf().buscaConfiguracao(config, new int[] { ExConfiguracaoBL.NIVEL_ACESSO }, dt);
		} catch (Exception e) {
			exConfiguracaoMin = null;
		}

		ExConfiguracao exConfiguracaoMax;
		exTpConfig.setIdTpConfiguracao(CpTipoConfiguracao.TIPO_CONFIG_NIVEL_ACESSO_MAXIMO);
		config.setCpTipoConfiguracao(exTpConfig);
		try {
			exConfiguracaoMax = (ExConfiguracao) Ex.getInstance().getConf().buscaConfiguracao(config, new int[] { ExConfiguracaoBL.NIVEL_ACESSO }, dt);
		} catch (Exception e) {
			exConfiguracaoMax = null;
		}

		if (exConfiguracaoMin != null && exConfiguracaoMax != null && exConfiguracaoMin.getExNivelAcesso() != null
				&& exConfiguracaoMax.getExNivelAcesso() != null) {
			int nivelMinimo = exConfiguracaoMin.getExNivelAcesso().getGrauNivelAcesso();
			int nivelMaximo = exConfiguracaoMax.getExNivelAcesso().getGrauNivelAcesso();

			for (ExNivelAcesso nivelAcesso : listaNiveis) {
				if (nivelAcesso.getGrauNivelAcesso() >= nivelMinimo && nivelAcesso.getGrauNivelAcesso() <= nivelMaximo) {
					niveisFinal.add(nivelAcesso);
				}
			}
		}

		return niveisFinal;
	}

	protected  ExNivelAcesso getNivelAcessoDefault(final ExTipoDocumento exTpDoc, final ExFormaDocumento forma, final ExModelo exMod, final ExClassificacao classif) {
		final Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

		final ExConfiguracao config = new ExConfiguracao();
		final CpTipoConfiguracao exTpConfig = new CpTipoConfiguracao();
		final CpSituacaoConfiguracao exStConfig = new CpSituacaoConfiguracao();
		config.setDpPessoa(getTitular());
		config.setLotacao(getLotaTitular());
		config.setExTipoDocumento(exTpDoc);
		config.setExFormaDocumento(forma);
		config.setExModelo(exMod);
		config.setExClassificacao(classif);
		exTpConfig.setIdTpConfiguracao(CpTipoConfiguracao.TIPO_CONFIG_NIVELACESSO);
		config.setCpTipoConfiguracao(exTpConfig);
		exStConfig.setIdSitConfiguracao(CpSituacaoConfiguracao.SITUACAO_DEFAULT);
		config.setCpSituacaoConfiguracao(exStConfig);
		ExConfiguracao exConfig;

		try {
			exConfig = criarExConfiguracaoPorCpConfiguracao(Ex.getInstance().getConf()
					.buscaConfiguracao(config, new int[] { ExConfiguracaoBL.NIVEL_ACESSO }, dt));
		} catch (Exception e) {
			exConfig = null;
		}

		if (exConfig != null) {
			return exConfig.getExNivelAcesso();
		}

		return null;
	}

	public ExConfiguracao criarExConfiguracaoPorCpConfiguracao(CpConfiguracao configuracaoBaseParaExConfiguracao) {
		ExConfiguracao exConfiguracao = new ExConfiguracao();

		if (configuracaoBaseParaExConfiguracao.isAtivo())
			exConfiguracao.updateAtivo();
		exConfiguracao.setCargo(configuracaoBaseParaExConfiguracao.getCargo());
		exConfiguracao.setComplexo(configuracaoBaseParaExConfiguracao.getComplexo());
		exConfiguracao.setConfiguracaoInicial(configuracaoBaseParaExConfiguracao.getConfiguracaoInicial());
		exConfiguracao.setConfiguracoesPosteriores(configuracaoBaseParaExConfiguracao.getConfiguracoesPosteriores());
		exConfiguracao.setCpGrupo(configuracaoBaseParaExConfiguracao.getCpGrupo());
		exConfiguracao.setCpIdentidade(configuracaoBaseParaExConfiguracao.getCpIdentidade());
		exConfiguracao.setCpServico(configuracaoBaseParaExConfiguracao.getCpServico());
		exConfiguracao.setCpSituacaoConfiguracao(configuracaoBaseParaExConfiguracao.getCpSituacaoConfiguracao());
		exConfiguracao.setCpTipoConfiguracao(configuracaoBaseParaExConfiguracao.getCpTipoConfiguracao());
		exConfiguracao.setCpTipoLotacao(configuracaoBaseParaExConfiguracao.getCpTipoLotacao());
		exConfiguracao.setDpPessoa(configuracaoBaseParaExConfiguracao.getDpPessoa());
		exConfiguracao.setDscFormula(configuracaoBaseParaExConfiguracao.getDscFormula());
		exConfiguracao.setDtFimVigConfiguracao(configuracaoBaseParaExConfiguracao.getDtFimVigConfiguracao());
		exConfiguracao.setDtIniVigConfiguracao(configuracaoBaseParaExConfiguracao.getDtIniVigConfiguracao());
		exConfiguracao.setFuncaoConfianca(configuracaoBaseParaExConfiguracao.getFuncaoConfianca());
		exConfiguracao.setHisAtivo(configuracaoBaseParaExConfiguracao.getHisAtivo());
		exConfiguracao.setHisDtFim(configuracaoBaseParaExConfiguracao.getHisDtFim());
		exConfiguracao.setHisDtIni(configuracaoBaseParaExConfiguracao.getHisDtIni());
		exConfiguracao.setHisIdcFim(configuracaoBaseParaExConfiguracao.getHisIdcFim());
		exConfiguracao.setHisIdcIni(configuracaoBaseParaExConfiguracao.getHisIdcIni());
		exConfiguracao.setHisIdIni(configuracaoBaseParaExConfiguracao.getHisIdIni());
		exConfiguracao.setId(configuracaoBaseParaExConfiguracao.getId());
		exConfiguracao.setIdConfiguracao(configuracaoBaseParaExConfiguracao.getIdConfiguracao());
		exConfiguracao.setLotacao(configuracaoBaseParaExConfiguracao.getLotacao());
		exConfiguracao.setNmEmail(configuracaoBaseParaExConfiguracao.getNmEmail());
		exConfiguracao.setOrgaoObjeto(configuracaoBaseParaExConfiguracao.getOrgaoObjeto());
		exConfiguracao.setOrgaoUsuario(configuracaoBaseParaExConfiguracao.getOrgaoUsuario());
		return exConfiguracao;

	}

	@SuppressWarnings("static-access")
	protected  String getDescrDocConfidencial(ExDocumento doc) {
		return Ex.getInstance().getBL().descricaoSePuderAcessar(doc, getTitular(), getLotaTitular());
	}

	protected  List<ExTipoDocumento> getTiposDocumento() throws AplicacaoException {
		return dao().listarExTiposDocumento();
	}

	protected  ExDao dao() {
		return ExDao.getInstance();
	}

	protected  ExDocumento daoDoc(long id) {
		return dao().consultar(id, ExDocumento.class, false);
	}

	protected  ExMovimentacao daoMov(long id) {
		return dao().consultar(id, ExMovimentacao.class, false);
	}

	protected  ExMobil daoMob(long id) {
		return dao().consultar(id, ExMobil.class, false);
	}

	protected  List<ExEstadoDoc> getEstados() throws AplicacaoException {
		return ExDao.getInstance().listarExEstadosDoc();
	}

	protected Map<Integer, String> getListaTipoResp() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, SigaMessages.getMessage("usuario.matricula"));
		map.put(2, "Órgão Integrado");
		return map;
	}

	protected List<String> getListaAnos() {
		final ArrayList<String> lst = new ArrayList<String>();
		// map.add("", "[Vazio]");
		final Calendar cal = Calendar.getInstance();
		for (Integer ano = cal.get(Calendar.YEAR); ano >= 1990; ano--)
			lst.add(ano.toString());
		return lst;
	}

	protected void assertAcesso(String pathServico) throws AplicacaoException {
		super.assertAcesso("DOC:Módulo de Documentos;" + pathServico);
	}

	protected  HttpServletResponse getResponse() {
		return response;
	}

	protected ServletContext getContext() {
		return context;
	}
	
	protected Boolean podeVisualizarDocumento(final ExMobil mob, DpPessoa titular, Long idVisualizacao) throws Exception {
		Boolean retorno = Boolean.FALSE;
		
		if(Cp.getInstance().getConf()
				.podePorConfiguracao(getCadastrante(), getCadastrante().getLotacao(), CpTipoConfiguracao.TIPO_CONFIG_DELEGAR_VISUALIZACAO)) {
			if(idVisualizacao != null && !idVisualizacao.equals(Long.valueOf(0))) {
				DpVisualizacao vis = dao().consultar(idVisualizacao, DpVisualizacao.class, false);
				
				if(vis.getDelegado().equals(titular)) {
					if(Ex.getInstance()
							.getComp()
							.podeAcessarDocumento(vis.getTitular(), vis.getTitular().getLotacao(),
									mob)) {
						retorno = Boolean.TRUE;
					}
				}
			}
		}
		
		return retorno;
	}
}
