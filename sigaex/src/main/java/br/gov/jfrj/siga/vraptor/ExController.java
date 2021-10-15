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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.TipoResponsavelEnum;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpVisualizacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExConfiguracaoCache;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExEstadoDoc;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExConfiguracaoBL;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.util.NivelDeAcessoUtil;
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
		return NivelDeAcessoUtil.getListaNivelAcesso(exTpDoc, forma, exMod,
				classif, getTitular(), getLotaTitular());
	}

	protected  ExNivelAcesso getNivelAcessoDefault(final ExTipoDocumento exTpDoc, final ExFormaDocumento forma, final ExModelo exMod, final ExClassificacao classif) {
		final Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

		final ExConfiguracao config = new ExConfiguracao();
		config.setDpPessoa(getTitular());
		config.setLotacao(getLotaTitular());
		config.setExTipoDocumento(exTpDoc);
		config.setExFormaDocumento(forma);
		config.setExModelo(exMod);
		config.setExClassificacao(classif);
		config.setCpTipoConfiguracao(ExTipoDeConfiguracao.NIVEL_DE_ACESSO);
		config.setCpSituacaoConfiguracao(CpSituacaoDeConfiguracaoEnum.DEFAULT);
		ExConfiguracaoCache exConfig;

		try {
			exConfig = (ExConfiguracaoCache) Ex.getInstance().getConf()
					.buscaConfiguracao(config, new int[] { ExConfiguracaoBL.NIVEL_ACESSO }, dt);
		} catch (Exception e) {
			exConfig = null;
		}

		if (exConfig != null) {
			return dao.consultar(exConfig.exNivelAcesso, ExNivelAcesso.class, false);
		}

		return null;
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
		return TipoResponsavelEnum.getListaMatriculaLotacao();
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

	public  HttpServletResponse getResponse() {
		return response;
	}

	protected ServletContext getContext() {
		return context;
	}
	
	protected Boolean podeVisualizarDocumento(final ExMobil mob, DpPessoa titular, Long idVisualizacao) throws Exception {
		return podeVisualizarDocumento(getCadastrante(), titular, idVisualizacao, mob);
	}

	public static boolean podeVisualizarDocumento(DpPessoa cadastrante, DpPessoa titular, Long idVisualizacao, final ExMobil mob) throws Exception {
		boolean retorno = Boolean.FALSE;
		
		if(Cp.getInstance().getConf()
				.podePorConfiguracao(cadastrante, cadastrante.getLotacao(), ExTipoDeConfiguracao.DELEGAR_VISUALIZACAO)) {
			if(idVisualizacao != null && !idVisualizacao.equals(Long.valueOf(0))) {
				DpVisualizacao vis = ExDao.getInstance().consultar(idVisualizacao, DpVisualizacao.class, false);
				
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