package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPessoasPinTrocarPost;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PessoasPinTrocarPostRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PessoasPinTrocarPostResponse;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class PessoasPinTrocarPost implements IPessoasPinTrocarPost {

	
	@Override
	public void run(PessoasPinTrocarPostRequest req, PessoasPinTrocarPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
			
			final String pinAtual = req.pinAtual;
			final String pin = req.pin;
			
			if (pinAtual.equals(pin)) {
				throw new RegraNegocioException("Não é possível trocar chave PIN: Chave atual idêntica à nova chave.");
			}
			
			SigaObjects so = ApiContext.getSigaObjects();
			CpIdentidade identidadeCadastrante = so.getIdentidadeCadastrante();
			
			 if (Cp.getInstance().getBL().validaPinIdentidade(pinAtual, identidadeCadastrante)) {
				if (Cp.getInstance().getBL().consisteFormatoPin(pin)) {
					DpPessoa cadastrante = so.getCadastrante();
					
					List<CpIdentidade> listaIdentidades = new ArrayList<CpIdentidade>();
					listaIdentidades = CpDao.getInstance().consultaIdentidadesPorCpf(cadastrante.getCpfPessoa().toString());	
					
					Cp.getInstance().getBL().definirPinIdentidade(listaIdentidades, pin, identidadeCadastrante);
					
					resp.mensagem = "Chave PIN foi trocada.";
					
				}	 
			 }
			

		} catch (RegraNegocioException e) {
			throw new SwaggerException(e.getMessage(), 400, null, req, resp, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}
	
	@Override
	public String getContext() {
		return "trocar chave PIN";
	}


	
}
