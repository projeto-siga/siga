package br.gov.jfrj.siga.vraptor;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;

@Controller
@Path("app/expediente/configuracao2")
public class ExConfiguracao2Controller extends ExController {
	
	private static final String VERIFICADOR_ACESSO = "FE:Ferramentas;CFG:Configurações";
	
	/**
	 * @deprecated CDI eyes only
	 */
	public ExConfiguracao2Controller() {
		super();
	}
	
	@Inject
	public ExConfiguracao2Controller(HttpServletRequest request, HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);
	}
	
	protected void assertAcesso(String pathServico) throws AplicacaoException {
		super.assertAcesso("DOC:Módulo de Documentos;" + pathServico);
	}
	
	@Get("/listar")
	public void lista() throws Exception {
		//assertAcesso(VERIFICADOR_ACESSO);
				
		result.include("listaTiposConfiguracao", getListaTiposConfiguracao());
		result.include("orgaoUsu", getOrgaosUsu());
	}
	
	@Get("/nova")
	public void cadastro() throws Exception {
		result.include("listaModelos", this.getModelos(null));
		result.include("listaTiposConfiguracao", getListaTiposConfiguracao());		
		result.include("listaNivelAcesso", getListaNivelAcesso());
	}
	
	private List<ExModelo> getModelos(final Long idFormaDoc) {
		ExFormaDocumento forma = null;
		if (idFormaDoc != null && idFormaDoc != 0) {
			forma = dao().consultar(idFormaDoc, ExFormaDocumento.class, false);
		}

		return Ex
				.getInstance()
				.getBL()
				.obterListaModelos(null, forma, false, false, null, null, false, getTitular(),
						getLotaTitular(), false);
	}
	
	@SuppressWarnings("all")
	private Set<CpTipoConfiguracao> getListaTiposConfiguracao() throws Exception {
		TreeSet<CpTipoConfiguracao> s = new TreeSet<CpTipoConfiguracao>(new Comparator() {

			public int compare(Object o1, Object o2) {
				return ((CpTipoConfiguracao) o1).getDscTpConfiguracao()
						.compareTo(((CpTipoConfiguracao) o2).getDscTpConfiguracao());
			}
		});

		s.addAll(dao().listarTiposConfiguracao());

		return s;
	}
	
	private List<ExNivelAcesso> getListaNivelAcesso() throws Exception {
		return dao().listarOrdemNivel();
	}

}
