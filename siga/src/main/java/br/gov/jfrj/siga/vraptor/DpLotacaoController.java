package br.gov.jfrj.siga.vraptor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
import br.gov.jfrj.siga.model.Selecionavel;

@Resource
public class DpLotacaoController extends SigaSelecionavelControllerSupport<DpLotacao, DpLotacaoDaoFiltro> {

	public DpLotacaoController(HttpServletRequest request, Result result,
			CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}
	
	@Get
	@Post
	@Path("app/lotacao/buscar")
	public void busca(String sigla, String postback) throws Exception{
//		setNome(nome);
		super.aBuscar(sigla, postback);
		result.include("param", getRequest().getParameterMap());
		result.include("tamanho", getTamanho());
		result.include("itens", getItens());
		result.include("nmOrgaoUsu", getOrgaosUsu());
//		result.include("nome", getNome());
	}

	@Override
	public DpLotacaoDaoFiltro createDaoFiltro() {
		final DpLotacaoDaoFiltro flt = new DpLotacaoDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(getNome()));
		/*
		 * if (param("postback")==null)
		 * flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario().getIdOrgaoUsu());
		 * else flt.setIdOrgaoUsu(paramInteger("orgaoUsu"));
		 */
		flt.setIdOrgaoUsu(paramLong("orgaoUsu"));
		if (flt.getIdOrgaoUsu() == null) {
			if (getLotaTitular() == null)
				throw new AplicacaoException("Usuário não está logado.");
			else 
			flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario()
					.getIdOrgaoUsu());
		}
		String buscarFechadas = param("buscarFechadas");
		flt.setBuscarFechadas(buscarFechadas != null ? Boolean
				.valueOf(buscarFechadas) : false);

		return flt;
	}

	@Override
	public Selecionavel selecionarPorNome(final DpLotacaoDaoFiltro flt)
			throws AplicacaoException {
		// Procura por nome
		flt.setNome(Texto.removeAcentoMaiusculas(flt.getSigla()));
		flt.setSigla(null);
		final List l = dao().consultarPorFiltro(flt);
		if (l != null)
			if (l.size() == 1)
				return (DpLotacao) l.get(0);
		return null;
	}


}
