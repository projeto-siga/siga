package br.gov.jfrj.siga.ex.api.v1;

import java.util.List;
import java.util.stream.Collectors;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.ILotacaoTextoPesquisarGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.LotacaoTextoPesquisarGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.LotacaoTextoPesquisarGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.ResultadoDePesquisa;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.hibernate.ExDao;

public class LotacaoTextoPesquisarGet implements ILotacaoTextoPesquisarGet {

	private ResultadoDePesquisa lotacaoToResultadoPesquisa(DpLotacao p) {
		ResultadoDePesquisa rp = new ResultadoDePesquisa();

		rp.nome = p.getNomeLotacao();
		rp.sigla = p.getSiglaCompleta();
		rp.siglaLotacao = p.getSigla();

		return rp;
	}

	@Override
	public void run(LotacaoTextoPesquisarGetRequest req, LotacaoTextoPesquisarGetResponse resp) throws Exception {
		CurrentRequest.set(
				new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
		SwaggerHelper.buscarEValidarUsuarioLogado();

		final DpLotacaoDaoFiltro flt = new DpLotacaoDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(req.texto));
		// TODO: ver se precisa de outros parametros listarLotacoes
		List<DpLotacao> l = ExDao.getInstance().consultarPorFiltro(flt);

		resp.list = l.stream().map(this::lotacaoToResultadoPesquisa).collect(Collectors.toList());
	}

	@Override
	public String getContext() {
		return "selecionar pessoas";
	}
}
