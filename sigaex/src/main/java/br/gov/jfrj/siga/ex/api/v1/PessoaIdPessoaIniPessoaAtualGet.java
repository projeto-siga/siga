package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IPessoaIdPessoaIniPessoaAtualGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.Pessoa;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.PessoaIdPessoaIniPessoaAtualGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.PessoaIdPessoaIniPessoaAtualGetResponse;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.hibernate.ExDao;

public class PessoaIdPessoaIniPessoaAtualGet implements IPessoaIdPessoaIniPessoaAtualGet {

	@Override
	public String getContext() {
		return "selecionar pessoa atual";
	}

	@Override
	public void run(PessoaIdPessoaIniPessoaAtualGetRequest req, PessoaIdPessoaIniPessoaAtualGetResponse resp)
			throws Exception {
		CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
		SwaggerHelper.buscarEValidarUsuarioLogado();

		try {
			//TODO: ver se precisa de outros parametros listarPessoa
			DpPessoa pessoa = new DpPessoa();
			pessoa.setIdPessoaIni(Long.valueOf(req.idPessoaIni));
			
			DpPessoa pessoaAtual = ExDao.getInstance().obterPessoaAtual(pessoa);
			
			Pessoa pessoaResp = new Pessoa();
			pessoaResp.idPessoa = pessoaAtual.getId().toString();
			pessoaResp.idPessoaIni = pessoaAtual.getIdPessoaIni().toString();
			pessoaResp.nome = pessoaAtual.getNomePessoa();
			pessoaResp.sigla = pessoaAtual.getSiglaCompleta();
			pessoaResp.siglaLotacao = pessoaAtual.getLotacao().getSiglaCompletaFormatada();
			
			resp.pessoaAtual = pessoaResp;

		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}


}
