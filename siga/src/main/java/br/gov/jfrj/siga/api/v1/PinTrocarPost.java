package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPinTrocarPost;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class PinTrocarPost implements IPinTrocarPost {

	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		final String pinAtual = req.pinAtual;
		final String pin = req.pin;

		CpIdentidade identidadeCadastrante = ctx.getIdentidadeCadastrante();
		DpPessoa cadastrante = ctx.getCadastrante();

		if (!Cp.getInstance().getComp().podeSegundoFatorPin(cadastrante, cadastrante.getLotacao())) {
			throw new RegraNegocioException(
					"PIN como Segundo Fator de Autenticação: Acesso não permitido a esse recurso.");
		}

		if (pinAtual.equals(pin)) {
			throw new RegraNegocioException("Não é possível alterar PIN: PIN atual idêntico ao novo PIN.");
		}
		if (Cp.getInstance().getBL().validaPinIdentidade(pinAtual, identidadeCadastrante)) {
			if (Cp.getInstance().getBL().consisteFormatoPin(pin)) {

				List<CpIdentidade> listaIdentidades = new ArrayList<CpIdentidade>();
				listaIdentidades = CpDao.getInstance().consultaIdentidadesPorCpf(cadastrante.getCpfPessoa().toString());

				Cp.getInstance().getBL().definirPinIdentidade(listaIdentidades, pin, identidadeCadastrante);
				Cp.getInstance().getBL().enviarEmailDefinicaoPIN(cadastrante, "Alteração de PIN",
						"Você alterou seu PIN.");

				resp.mensagem = "PIN foi alterado.";

			}
		}
	}

	@Override
	public String getContext() {
		return "trocar PIN";
	}

}
