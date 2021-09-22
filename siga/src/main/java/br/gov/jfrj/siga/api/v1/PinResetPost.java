package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPinResetPost;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class PinResetPost implements IPinResetPost {

	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		final String tokenPin = req.tokenPin.toUpperCase();
		final String pin = req.pin;

		CpIdentidade identidadeCadastrante = ctx.getIdentidadeCadastrante();

		DpPessoa cadastrante = ctx.getCadastrante();

		if (!Cp.getInstance().getComp().podeSegundoFatorPin(cadastrante, cadastrante.getLotacao()))
			throw new RegraNegocioException(
					"PIN como Segundo Fator de Autenticação: Acesso não permitido a esse recurso.");

		if (Cp.getInstance().getBL().isTokenResetPinValido(cadastrante.getCpfPessoa(), tokenPin)) {
			if (Cp.getInstance().getBL().consisteFormatoPin(pin)) {

				List<CpIdentidade> listaIdentidades = new ArrayList<CpIdentidade>();
				listaIdentidades = CpDao.getInstance().consultaIdentidadesPorCpf(cadastrante.getCpfPessoa().toString());

				Cp.getInstance().getBL().definirPinIdentidade(listaIdentidades, pin, identidadeCadastrante);
				Cp.getInstance().getBL().invalidarTokenUtilizado(cadastrante.getCpfPessoa(), tokenPin);
				Cp.getInstance().getBL().enviarEmailDefinicaoPIN(cadastrante, "Redefinição de PIN",
						"Você redefiniu seu PIN.");
				resp.mensagem = "PIN foi redefinido.";

			}
		} else {
			throw new RegraNegocioException("Token para reset de PIN inválido ou expirado.");
		}
	}

	@Override
	public String getContext() {
		return "reset PIN";
	}

}
