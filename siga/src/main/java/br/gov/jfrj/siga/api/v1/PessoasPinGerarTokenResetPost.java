package br.gov.jfrj.siga.api.v1;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPessoasPinGerarTokenResetPost;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PessoasPinGerarTokenResetPostRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PessoasPinGerarTokenResetPostResponse;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpToken;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class PessoasPinGerarTokenResetPost implements IPessoasPinGerarTokenResetPost {



	@Override
	public void run(PessoasPinGerarTokenResetPostRequest req, PessoasPinGerarTokenResetPostResponse resp)
			throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
			
			SigaObjects so = ApiContext.getSigaObjects();
			
			CpIdentidade identidadeCadastrante = so.getIdentidadeCadastrante();
			if (identidadeCadastrante.getPinIdentidade() == null) {
				throw new RegraNegocioException("Não é possível gerar token para reset de chave PIN: Não existe chave cadastrada.");
			}
			
			DpPessoa cadastrante = so.getCadastrante();
			Long cpf = cadastrante.getCpfPessoa();
	
			CpToken token =	Cp.getInstance().getBL().gerarTokenResetPin(cpf);/* TODO: Avaliar se o CPF é o melhor para se gerar o Token. Parece ser mais performático para não ter queries adicionais */
			Cp.getInstance().getBL().enviarEmailTokenResetPIN(cadastrante,"Código para redefinição de chave PIN ",token.getToken());
			resp.mensagem = "Token gerado para reset de PIN";
			resp.tokenPin = token.getToken();
			
			
		} catch (RegraNegocioException e) {
			throw new SwaggerException(e.getMessage(), 400, null, req, resp, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
		
		
	}
	
	@Override
	public String getContext() {
		return "gerar Token Reset PIN";
	}
}
