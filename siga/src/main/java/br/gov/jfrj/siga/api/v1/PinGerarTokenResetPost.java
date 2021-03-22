package br.gov.jfrj.siga.api.v1;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPinGerarTokenResetPost;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PinGerarTokenResetPostRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PinGerarTokenResetPostResponse;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpToken;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class PinGerarTokenResetPost implements IPinGerarTokenResetPost {

	@Override
	public void run(PinGerarTokenResetPostRequest req, PinGerarTokenResetPostResponse resp)
			throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				SigaObjects so = ApiContext.getSigaObjects();
				CpIdentidade identidadeCadastrante = so.getIdentidadeCadastrante();
				DpPessoa cadastrante = so.getCadastrante();
				
				if (!Cp.getInstance().getComp().podeSegundoFatorPin(cadastrante, cadastrante.getLotacao()) ) {
					throw new RegraNegocioException("PIN como Segundo Fator de Autenticação: Acesso não permitido a esse recurso.");
				}
				
				if (identidadeCadastrante.getPinIdentidade() == null) {
					throw new RegraNegocioException("Não é possível gerar token para redenifir seu PIN: Não existe PIN cadastrado.");
				}
				
				Long cpf = cadastrante.getCpfPessoa();
				
				CpToken token =	Cp.getInstance().getBL().gerarTokenResetPin(cpf);
				Cp.getInstance().getBL().enviarEmailTokenResetPIN(cadastrante,"Código para redefinição de PIN ",token.getToken());
				resp.mensagem = "Token gerado para reset de PIN";
				
				
			} catch (RegraNegocioException e) {
				ctx.rollback(e);
				throw new SwaggerException(e.getMessage(), 400, null, req, resp, null);
			} catch (Exception e) {
				ctx.rollback(e);
				throw e;
			}
		}
		
	}
	
	@Override
	public String getContext() {
		return "gerar Token Reset PIN";
	}
}
