package br.gov.jfrj.siga.api.v1;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPinGerarTokenResetPost;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpToken;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class PinGerarTokenResetPost implements IPinGerarTokenResetPost {

	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		try {
			CpIdentidade identidadeCadastrante = ctx.getIdentidadeCadastrante();
			DpPessoa cadastrante = ctx.getCadastrante();

			if (!Cp.getInstance().getComp().podeSegundoFatorPin(cadastrante, cadastrante.getLotacao())) {
				throw new RegraNegocioException(
						"PIN como Segundo Fator de Autenticação: Acesso não permitido a esse recurso.");
			}

			if (identidadeCadastrante.getPinIdentidade() == null) {
				throw new RegraNegocioException(
						"Não é possível gerar token para redenifir seu PIN: Não existe PIN cadastrado.");
			}

			Long cpf = cadastrante.getCpfPessoa();

			CpToken token = Cp.getInstance().getBL().gerarTokenResetPin(cpf);
			Cp.getInstance().getBL().enviarEmailTokenResetPIN(cadastrante, "Código para redefinição de PIN ",
					token.getToken());
			resp.mensagem = "Token gerado para reset de PIN";

		} catch (RegraNegocioException e) {
			throw new SwaggerException(e.getMessage(), 400, null, req, resp, null);
		}
	}

	@Override
	public String getContext() {
		return "gerar Token Reset PIN";
	}
}
