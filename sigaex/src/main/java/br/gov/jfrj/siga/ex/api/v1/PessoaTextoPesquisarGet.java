package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.List;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IPessoaTextoPesquisarGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.PessoaTextoPesquisarGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.PessoaTextoPesquisarGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.ResultadoDePesquisa;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.hibernate.ExDao;

public class PessoaTextoPesquisarGet implements IPessoaTextoPesquisarGet {

	@Override
	public void run(PessoaTextoPesquisarGetRequest req,
			PessoaTextoPesquisarGetResponse resp) throws Exception {
		CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
		SwaggerHelper.buscarEValidarUsuarioLogado();

		resp.list = new ArrayList<>();
		try {
			final DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
			flt.setNome(Texto.removeAcentoMaiusculas(req.texto));
			//TODO: ver se precisa de outros parametros listarPessoa
			List<DpPessoa> l = ExDao.getInstance().consultarPorFiltro(flt);

			for (DpPessoa p : l) {
				ResultadoDePesquisa rp = new ResultadoDePesquisa();
				rp.nome = p.getNomePessoa();
				rp.sigla = p.getSigla();
				resp.list.add(rp);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public String getContext() {
		return "selecionar pessoas";
	}
}
