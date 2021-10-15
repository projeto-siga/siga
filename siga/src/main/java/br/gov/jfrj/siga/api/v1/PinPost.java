package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPinPost;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class PinPost implements IPinPost {
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		final String pin = req.pin;

		if (Cp.getInstance().getBL().consisteFormatoPin(pin)) {
			DpPessoa cadastrante = ctx.getCadastrante();
			CpIdentidade identidadeCadastrante = ctx.getIdentidadeCadastrante();

			if (!Cp.getInstance().getComp().podeSegundoFatorPin(cadastrante, cadastrante.getLotacao())) {
				throw new RegraNegocioException(
						"PIN como Segundo Fator de Autenticação: Acesso não permitido a esse recurso.");
			}

			if (identidadeCadastrante.getPinIdentidade() != null) {
				throw new RegraNegocioException("Não é possível cadastrar seu PIN: Já existe PIN cadastrado.");
			}

			List<CpIdentidade> listaIdentidades = new ArrayList<CpIdentidade>();
			listaIdentidades = CpDao.getInstance().consultaIdentidadesPorCpf(cadastrante.getCpfPessoa().toString());

			Cp.getInstance().getBL().definirPinIdentidade(listaIdentidades, pin, identidadeCadastrante);
			Cp.getInstance().getBL().enviarEmailDefinicaoPIN(cadastrante, "Novo PIN", "Você definiu um novo PIN.");
			resp.mensagem = "PIN foi definido.";
		}
	}

	@Override
	public String getContext() {
		return "cadastrar PIN";
	}
}
