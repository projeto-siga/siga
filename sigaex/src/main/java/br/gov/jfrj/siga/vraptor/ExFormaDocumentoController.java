package br.gov.jfrj.siga.vraptor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.base.SigaModal;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;

@Controller
public class ExFormaDocumentoController extends ExController {

	private static final String ACESSO_SIGA_DOC_MOD = "MOD:Gerenciar modelos";

	/**
	 * @deprecated CDI eyes only
	 */
	public ExFormaDocumentoController() {
		super();
	}

	@Inject
	public ExFormaDocumentoController(HttpServletRequest request, HttpServletResponse response, ServletContext context, Result result, SigaObjects so,
			EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	@Get("app/forma/listar")
	public void listarFormas(final String ordenar) {
		assertAcesso(ACESSO_SIGA_DOC_MOD);

		final List<ExFormaDocumento> itens;

		if (ordenar != null && "sigla".equals(ordenar)) {
			itens = dao().listarTodosOrdenarPorSigla();
		} else {
			itens = dao().listarTodosOrdenarPorDescricao();
		}

		result.include("itens", itens);
		result.include("ordenadoPor", ordenar == null ? "descricao" : ordenar);
	}

	@Get("app/forma/editar")
	public void editarForma(final Long id, String descricao, String sigla, Long idTipoFormaDoc, boolean origemExterno,
			boolean origemInternoImportado, boolean origemInternoProduzido, boolean origemInternoCapturado, 
			Integer isComposto, boolean origemExternoCapturado) {
		
		assertAcesso(ACESSO_SIGA_DOC_MOD);

		final ExFormaDocumento forma = id != null ? recuperarForma(id) : new ExFormaDocumento();
		boolean temDocumentoVinculado = false;
		
		if (forma.getIdFormaDoc() != null) {
			descricao = forma.getDescrFormaDoc();
			sigla = forma.getSigla();
			isComposto = forma.getIsComposto();
			
			if (forma.getExTipoFormaDoc() != null) {
				idTipoFormaDoc = forma.getExTipoFormaDoc().getIdTipoFormaDoc();
			}			
						
			temDocumentoVinculado = dao().isExFormaComDocumentoVinculado(forma.getId());
		}

		if (getPostback() == null) {
			result.include("descricao", descricao);
			result.include("sigla", sigla);			
			result.include("idTipoFormaDoc", idTipoFormaDoc);	
			result.include("isComposto", isComposto);

			if (forma.getExTipoDocumentoSet() != null) {
				for (ExTipoDocumento origem : forma.getExTipoDocumentoSet()) {
					if (origem.getId() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_FOLHA_DE_ROSTO) {
						origemExterno = true;
					}
					if (origem.getId() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO) {
						origemInternoProduzido = true;
					}
					if (origem.getId() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_FOLHA_DE_ROSTO) {
						origemInternoImportado = true;
					}
					if (origem.getId() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_CAPTURADO) {
						origemInternoCapturado = true;
					}
					if (origem.getId() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_CAPTURADO) {
						origemExternoCapturado = true;
					}
				}
			}
			
			result.include("origemExterno", origemExterno);
			result.include("origemInternoImportado", origemInternoImportado);
			result.include("origemInternoProduzido", origemInternoProduzido);
			result.include("origemInternoCapturado", origemInternoCapturado);
			result.include("origemExternoCapturado", origemExternoCapturado);			
		}

		result.include("id", id);
		result.include("listaTiposFormaDoc", dao().listarExTiposFormaDoc());		
		result.include("temDocumentoVinculado", temDocumentoVinculado);
	}

	@Get("app/forma/verificar_sigla")
	public void aVerificarSigla(final Long id, final String sigla) {
		ExFormaDocumento formaConsulta = new ExFormaDocumento();

		if (sigla != null && !sigla.isEmpty()) {

			if (!formaConsulta.isSiglaValida(sigla)) {
				setMensagem("Sigla invalida. A sigla deve ser formada por 3 letras.");
			}

			formaConsulta.setSigla(sigla);
			formaConsulta = dao().consultarPorSigla(formaConsulta);

			if (formaConsulta != null) {
				setMensagem("Esta sigla ja esta sendo utilizada");
			}
		}
		
		result.use(Results.page()).forwardTo("/WEB-INF/page/mensagemAjax.jsp");
	}

	@Transacional
	@Post("app/forma/gravar")
	public void gravar(final Integer postback, final Long id, final String descricao, final String sigla, final Long idTipoFormaDoc, final boolean origemExterno,
			final boolean origemInternoImportado, final boolean origemInternoProduzido, final boolean origemInternoCapturado, 
			final Integer isComposto, final boolean origemExternoCapturado) {
		assertAcesso(ACESSO_SIGA_DOC_MOD);
		setPostback(postback);
				
		final ExFormaDocumento forma = id != null ? recuperarForma(id) : new ExFormaDocumento();
		ExFormaDocumento formaCadastrada = new ExFormaDocumento();
		
		if (forma.isEditando()) {
			formaCadastrada = prepararExFormaDocumentoCadastrada(forma);
		}
		
		forma.setDescrFormaDoc(descricao);
		forma.setSigla(sigla);
		forma.setExTipoFormaDoc(dao().consultar(idTipoFormaDoc, ExTipoFormaDoc.class, false));

		if (forma.getExTipoDocumentoSet() == null) {
			forma.setExTipoDocumentoSet(new HashSet<ExTipoDocumento>());
		} else {									
			forma.getExTipoDocumentoSet().clear();					
		}

		forma.setIsComposto(isComposto);

		if (origemInternoProduzido) {
			forma.getExTipoDocumentoSet().add(dao().consultar(ExTipoDocumento.TIPO_DOCUMENTO_INTERNO, ExTipoDocumento.class, false));
		}
		if (origemInternoImportado) {
			forma.getExTipoDocumentoSet().add(dao().consultar(ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_FOLHA_DE_ROSTO, ExTipoDocumento.class, false));
		}
		if (origemExterno) {
			forma.getExTipoDocumentoSet().add(dao().consultar(ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_FOLHA_DE_ROSTO, ExTipoDocumento.class, false));
		}

		if (origemInternoCapturado) {
			forma.getExTipoDocumentoSet().add(dao().consultar(ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_CAPTURADO, ExTipoDocumento.class, false));
		}

		if (origemExternoCapturado) {
			forma.getExTipoDocumentoSet().add(dao().consultar(ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_CAPTURADO, ExTipoDocumento.class, false));
		}

		try {
			Ex.getInstance().getBL().gravarForma(forma, formaCadastrada);
			
			result.include("id", forma.getIdFormaDoc());
			result.include("descricao", descricao);
			result.include("sigla", sigla);
			result.include("idTipoFormaDoc", idTipoFormaDoc);
			result.include("isComposto", isComposto);
			result.include("origemExterno", origemExterno);
			result.include("origemInternoImportado", origemInternoImportado);
			result.include("origemInternoProduzido", origemInternoProduzido);
			result.include("origemInternoCapturado", origemInternoCapturado);
			result.include("origemExternoCapturado", origemExternoCapturado);
			
			result.redirectTo(this).listarFormas(null);
		} catch (RegraNegocioException e) {			
			
			result.include(SigaModal.ALERTA, SigaModal.mensagem(e.getMessage()));
			result.forwardTo(this).editarForma(id, descricao, sigla, idTipoFormaDoc, origemExterno, origemInternoImportado,  origemInternoProduzido
					, origemInternoCapturado, isComposto,  origemExternoCapturado);
		}
	}

	private ExFormaDocumento recuperarForma(final Long id) {
		return dao().consultar(id, ExFormaDocumento.class, false);
	}
	
	private ExFormaDocumento prepararExFormaDocumentoCadastrada(ExFormaDocumento forma) {
		ExFormaDocumento formaCadastrada = new ExFormaDocumento();
		Set<ExTipoDocumento> origensCadastradas = new HashSet<>();
		
		formaCadastrada.setExTipoFormaDoc(new ExTipoFormaDoc());
		formaCadastrada.getExTipoFormaDoc().setIdTipoFormaDoc(forma.getExTipoFormaDoc().getId());			
		formaCadastrada.setSigla(forma.getSigla());
		formaCadastrada.setIsComposto(forma.getIsComposto());
		
		if (forma.getExTipoDocumentoSet() != null) {					
			for (ExTipoDocumento origem : forma.getExTipoDocumentoSet()) {
				origensCadastradas.add(origem);
			}																				
		} 		
		formaCadastrada.setExTipoDocumentoSet(origensCadastradas);
		
		return formaCadastrada;
	}

}
