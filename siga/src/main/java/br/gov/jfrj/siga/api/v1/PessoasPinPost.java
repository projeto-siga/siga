package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPessoasPinPost;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PessoasPinPostRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PessoasPinPostResponse;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class PessoasPinPost implements IPessoasPinPost {
	@Override
	public void run(PessoasPinPostRequest req, PessoasPinPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
			
			final String pin = req.pin;
			
			if (pin.isEmpty()) {
				throw new RegraNegocioException("Chave PIN não informada.");
			}
			
			if (!isNumeric(pin)) {
				throw new RegraNegocioException("Chave PIN deve conter apenas dígitos númericos (0-9).");
			}
			
			if (pin.length() != CpIdentidade.pinLength) {
				throw new RegraNegocioException("Chave deve ter "+String.valueOf(CpIdentidade.pinLength)+" dígitos numéricos.");
			}	
			
			ApiContext.assertAcesso("");
			SigaObjects so = ApiContext.getSigaObjects();
	
			DpPessoa cadastrante = so.getCadastrante();
			CpIdentidade identidadeCadastrante = so.getIdentidadeCadastrante();
			
			List<CpIdentidade> listaIdentidades = new ArrayList<CpIdentidade>();
			listaIdentidades = CpDao.getInstance().consultaIdentidadesPorCpf(cadastrante.getCpfPessoa().toString());	
			
			Cp.getInstance().getBL().definirPinIdentidade(listaIdentidades, pin, identidadeCadastrante);
			
			resp.mensagem = "Chave PIN definida.";
		} catch (RegraNegocioException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}
	
    private static boolean isNumeric(String str) {
        return str != null && str.matches("[0-9]*");
    }
	
	@Override
	public String getContext() {
		return "cadastrar pin";
	}
}
