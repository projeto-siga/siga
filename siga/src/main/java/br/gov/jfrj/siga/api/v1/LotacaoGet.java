package br.gov.jfrj.siga.api.v1;

import java.util.List;
import java.util.stream.Collectors;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.ILotacaoGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Lotacao;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.LotacaoGetRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.LotacaoGetResponse;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;

public class LotacaoGet implements ILotacaoGet {

	private Lotacao lotacaoToResultadoPesquisa(DpLotacao lota) {
		Lotacao rp = new Lotacao();

		rp.nome = lota.getNomeLotacao();
		rp.sigla = lota.getSiglaCompleta();
		rp.siglaLotacao = lota.getSigla();

		return rp;
	}

	@Override
	public void run(LotacaoGetRequest req, LotacaoGetResponse resp) throws Exception {
		CurrentRequest.set(
				new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
		SwaggerHelper.buscarEValidarUsuarioLogado();

		final DpLotacaoDaoFiltro flt = new DpLotacaoDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(req.texto));
		// TODO: ver se precisa de outros parametros listarLotacoes
		List<DpLotacao> l = CpDao.getInstance().consultarPorFiltro(flt);

		resp.list = l.stream().map(this::lotacaoToResultadoPesquisa).collect(Collectors.toList());
	}

	@Override
	public String getContext() {
		return "selecionar pessoas";
	}
}
