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
 */
package br.gov.jfrj.siga.vraptor;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.vo.ExDocumentoVO;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

@Controller
public class ExPainelController extends ExController {
	private static final String CORRIGEMOBIL = "CORRIGEMOBIL:Corrige Documento sem Mobil de Via ou Volume";
	
	/**
     * @deprecated CDI eyes only
     */
	public ExPainelController() {
		super();
	}

	@Inject
	public ExPainelController(HttpServletRequest request,
			HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	@Get("app/expediente/painel/exibir")
	public void exibe(final ExMobilSelecao documentoRefSel) throws Exception {
		assertAcesso("");
		boolean postback = param("postback") != null;
		
		if (documentoRefSel.getSigla() == null) {
			if (postback) {
				result.include("mensagemCabec", "Informe o número do documento.");
				result.include("msgCabecClass", "alert-danger");
			}
			return;
		}
		result.include("documentoRefSel", documentoRefSel);
		final ExDocumentoDTO exDocumentoDTO = new ExDocumentoDTO();
		exDocumentoDTO.setSigla(documentoRefSel.getSigla());

		ExDocumentoVO docVO = null;
		try {
			buscarDocumento(false, exDocumentoDTO);
			for (ExDocumento docFilho : exDocumentoDTO.getDoc().getExDocumentoFilhoSet()) {
				if(docFilho.getDescrDocumento() == null) {
					result.include("erroFilhoSemDescricao", true);
					result.include("siglaFilho",docFilho.getSigla());
					return;
				}
			}
			docVO = new ExDocumentoVO(exDocumentoDTO.getDoc(),
					exDocumentoDTO.getMob(), getCadastrante(), getTitular(),
					getLotaTitular(), true, true, false);
			if (exDocumentoDTO.getDoc().isFinalizado() 
					&& exDocumentoDTO.getDoc().getExMobilSet().size() <= 1) {
				result.include("erroSemMobil", true);
			}
			if (exDocumentoDTO.getDoc().getDescrDocumento() == null) {
				result.include("erroSemDescricao", true);
			}
		} catch (AplicacaoException e) {
			result.include("mensagemCabec", "Documento não encontrado." );
			result.include("msgCabecClass", "alert-danger");
			return;
		} catch (Exception e) {
			if (exDocumentoDTO != null 
					&& exDocumentoDTO.getDoc() != null
					&& exDocumentoDTO.getDoc().isFinalizado() 
					&& exDocumentoDTO.getDoc().getExMobilSet().size() <= 1) {
				result.include("erroSemMobil", true);
			}
			result.include("mensagemCabec", "Erro ao tentar obter o documento: " + e);
			result.include("msgCabecClass", "alert-danger");
			return;
		}
		result.include("msg", exDocumentoDTO.getMsg());
		result.include("docVO", docVO);
		result.include("mob", exDocumentoDTO.getMob());
		result.include("currentTimeMillis", System.currentTimeMillis());
	}

	@Transacional
	@Get("app/expediente/painel/corrigeDocSemMobil")
	public void corrigeDocSemMobil(final ExMobilSelecao documentoRefSel) throws Exception {
		assertAcesso(CORRIGEMOBIL);
		
		result.include("postback", true);

		if (documentoRefSel.getSigla() == null) {
			result.include("mensagemCabec", "Informe o número do documento.");
			result.include("msgCabecClass", "alert-warning");
			result.redirectTo(this).exibe(documentoRefSel);
			return;
		}
		final ExDocumentoDTO exDocumentoDTO = new ExDocumentoDTO();
		exDocumentoDTO.setSigla(documentoRefSel.getSigla());
		ExDocumentoVO docVO;
		try {
			buscarDocumento(false, exDocumentoDTO);
			docVO = new ExDocumentoVO(exDocumentoDTO.getDoc(),
					exDocumentoDTO.getMob(), getCadastrante(), getTitular(),
					getLotaTitular(), true, true, false);
		} catch (Exception e) {
		}
		if (exDocumentoDTO != null && exDocumentoDTO.getDoc().isFinalizado() 
				&& exDocumentoDTO.getDoc().getExMobilSet().size() > 1) {
			result.include("mensagemCabec", "Documento já está com mais de um mobil.");
			result.include("msgCabecClass", "alert-danger");
			result.redirectTo(this).exibe(documentoRefSel);
			return;
		}

		try {
			Ex.getInstance()
				.getBL()
				.corrigeDocSemMobil(
						exDocumentoDTO.getDoc());

		} catch (final Throwable t) {
			throw new AplicacaoException("Erro ao tentar corrigir documento sem mobil.", 0, t);
		}
		result.redirectTo(this).exibe(documentoRefSel);
	}

	@Transacional
	@Get("app/expediente/painel/corrigeDocSemDescricao")
	public void corrigeDocSemDescricao(final ExMobilSelecao documentoRefSel) throws Exception {
		assertAcesso(CORRIGEMOBIL);
		
		result.include("postback", true);

		if (documentoRefSel.getSigla() == null) {
			result.include("mensagemCabec", "Informe o número do documento.");
			result.include("msgCabecClass", "alert-warning");
			result.redirectTo(this).exibe(documentoRefSel);
			return;
		}
		final ExDocumentoDTO exDocumentoDTO = new ExDocumentoDTO();
		exDocumentoDTO.setSigla(documentoRefSel.getSigla());
		ExDocumentoVO docVO;
		try {
			buscarDocumento(false, exDocumentoDTO);
			docVO = new ExDocumentoVO(exDocumentoDTO.getDoc(),
					exDocumentoDTO.getMob(), getCadastrante(), getTitular(),
					getLotaTitular(), true, true, false);
		} catch (Exception e) {
		}
		if (exDocumentoDTO.getDoc().getDescrDocumento() != null) {
			result.include("mensagemCabec", "Documento já está com a descrição.");
			result.include("msgCabecClass", "alert-danger");
			result.redirectTo(this).exibe(documentoRefSel);
			return;
		}

		try {
			Ex.getInstance()
				.getBL()
				.corrigeDocSemDescricao(
						exDocumentoDTO.getDoc());

		} catch (final Throwable t) {
			throw new AplicacaoException("Erro ao tentar corrigir descrição do documento", 0, t);
		}
		result.redirectTo(this).exibe(documentoRefSel);
	}

	private void buscarDocumento(final boolean fVerificarAcesso,
			final ExDocumentoDTO exDocumentoDTO) {
		buscarDocumento(fVerificarAcesso, false, exDocumentoDTO);
	}

	private void buscarDocumento(final boolean fVerificarAcesso,
			final boolean fPodeNaoExistir, final ExDocumentoDTO exDocumentoDto) {
		if (exDocumentoDto.getMob() == null
				&& exDocumentoDto.getSigla() != null
				&& exDocumentoDto.getSigla().length() != 0) {
			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(exDocumentoDto.getSigla());
			exDocumentoDto.setMob((ExMobil) dao().consultarPorSigla(filter));
			if (exDocumentoDto.getMob() != null) {
				exDocumentoDto.setDoc(exDocumentoDto.getMob().getExDocumento());
			}
		} else if (exDocumentoDto.getMob() == null
				&& exDocumentoDto.getDocumentoViaSel().getId() != null) {
			exDocumentoDto
					.setIdMob(exDocumentoDto.getDocumentoViaSel().getId());
			exDocumentoDto.setMob(dao().consultar(exDocumentoDto.getIdMob(),
					ExMobil.class, false));
		} else if (exDocumentoDto.getMob() == null
				&& exDocumentoDto.getIdMob() != null
				&& exDocumentoDto.getIdMob() != 0) {
			exDocumentoDto.setMob(dao().consultar(exDocumentoDto.getIdMob(),
					ExMobil.class, false));
		}
		if (exDocumentoDto.getMob() != null) {
			exDocumentoDto.setDoc(exDocumentoDto.getMob().doc());
		}
		if (exDocumentoDto.getDoc() == null) {
			final String id = param("exDocumentoDto.id");
			if (id != null && id.length() != 0) {
				exDocumentoDto.setDoc(daoDoc(Long.parseLong(id)));
			}
		}
		if (exDocumentoDto.getDoc() != null && exDocumentoDto.getMob() == null) {
			exDocumentoDto.setMob(exDocumentoDto.getDoc().getMobilGeral());
		}

		if (!fPodeNaoExistir && exDocumentoDto.getDoc() == null) {
			throw new AplicacaoException("Documento não informado");
		}
		if (fVerificarAcesso && exDocumentoDto.getMob() != null
				&& exDocumentoDto.getMob().getIdMobil() != null) {
			verificaNivelAcesso(exDocumentoDto.getMob());
		}
	}
	
	protected void assertAcesso(final String pathServico) {
		super.assertAcesso("FE:Ferramentas;PAINEL:Painel Administrativo;" + pathServico);
	}
}