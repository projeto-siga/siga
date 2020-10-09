package br.gov.jfrj.siga.api.v1;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPessoaIdPessoaIniPessoaAtualGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PessoaAtual;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PessoaIdPessoaIniPessoaAtualGetRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PessoaIdPessoaIniPessoaAtualGetResponse;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;

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
			
			DpPessoa pessoaAtual = CpDao.getInstance().obterPessoaAtual(pessoa);
			
			PessoaAtual pessoaResp = new PessoaAtual();
			pessoaResp.idPessoa = pessoaAtual.getId().toString();
			pessoaResp.idPessoaIni = pessoaAtual.getIdPessoaIni().toString();
			pessoaResp.nome = pessoaAtual.getNomePessoa();
			pessoaResp.sigla = pessoaAtual.getSiglaCompleta();
			pessoaResp.siglaLotacao = pessoaAtual.getLotacao().getSiglaCompletaFormatada();
			
			resp.pessoaAtual = pessoaResp;
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}


}
