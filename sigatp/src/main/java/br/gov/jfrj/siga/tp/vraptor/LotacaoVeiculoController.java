package br.gov.jfrj.siga.tp.vraptor;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.tp.model.ItemMenu;
import br.gov.jfrj.siga.tp.model.LotacaoVeiculo;
import br.gov.jfrj.siga.tp.model.Veiculo;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("/app/lotacaoVeiculo/")
public class LotacaoVeiculoController extends TpController {

	public LotacaoVeiculoController(HttpServletRequest request, Result result, CpDao dao,
			Validator validator, SigaObjects so, EntityManager em) {
		super(request, result, dao, validator, so, em);
	}
	
	@Path("/listarPorVeiculo/{idVeiculo}")
	public void listarPorVeiculo(Long idVeiculo) throws Exception {
		Veiculo veiculo = Veiculo.AR.findById(idVeiculo);
		List<LotacaoVeiculo> lotacoesVeiculo = LotacaoVeiculo.buscarTodosPorVeiculo(veiculo);
		MenuMontador.instance(result).recuperarMenuVeiculos(idVeiculo, ItemMenu.LOTACOES);
		
		result.include("veiculo", veiculo);
		result.include("lotacoesVeiculo", lotacoesVeiculo);
	}

}
