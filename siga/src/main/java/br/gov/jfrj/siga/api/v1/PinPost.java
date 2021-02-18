package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPinPost;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PinPostRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PinPostResponse;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class PinPost implements IPinPost {
	@Override
	public void run(PinPostRequest req, PinPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
			
			final String pin = req.pin;
			
			if (Cp.getInstance().getBL().consisteFormatoPin(pin)) {	
				ApiContext.assertAcesso("");
				SigaObjects so = ApiContext.getSigaObjects();
		
				DpPessoa cadastrante = so.getCadastrante();
				CpIdentidade identidadeCadastrante = so.getIdentidadeCadastrante();
				
				if (identidadeCadastrante.getPinIdentidade() != null) {
					throw new RegraNegocioException("Não é possível cadastrar chave PIN: Já existe chave cadastrada.");
				}
				
				List<CpIdentidade> listaIdentidades = new ArrayList<CpIdentidade>();
				listaIdentidades = CpDao.getInstance().consultaIdentidadesPorCpf(cadastrante.getCpfPessoa().toString());	
				
				Cp.getInstance().getBL().definirPinIdentidade(listaIdentidades, pin, identidadeCadastrante);
				Cp.getInstance().getBL().enviarEmailDefinicaoPIN(cadastrante,"Nova Chave PIN","Você definiu uma nova chave PIN.");
				resp.mensagem = "Chave PIN definida.";
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
		return "cadastrar pin";
	}
}
