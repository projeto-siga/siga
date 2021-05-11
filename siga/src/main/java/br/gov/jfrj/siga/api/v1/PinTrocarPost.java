package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPinTrocarPost;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PinTrocarPostRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PinTrocarPostResponse;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class PinTrocarPost implements IPinTrocarPost {

	
	@Override
	public void run(PinTrocarPostRequest req, PinTrocarPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				final String pinAtual = req.pinAtual;
				final String pin = req.pin;
				
				SigaObjects so = ApiContext.getSigaObjects();
				CpIdentidade identidadeCadastrante = so.getIdentidadeCadastrante();
				DpPessoa cadastrante = so.getCadastrante();
				
				if (!Cp.getInstance().getComp().podeSegundoFatorPin(cadastrante, cadastrante.getLotacao()) ) {
					throw new RegraNegocioException("PIN como Segundo Fator de Autenticação: Acesso não permitido a esse recurso.");
				}
				
				if (pinAtual.equals(pin)) {
					throw new RegraNegocioException("Não é possível alterar PIN: PIN atual idêntico ao novo PIN.");
				}
				if (Cp.getInstance().getBL().validaPinIdentidade(pinAtual, identidadeCadastrante)) {
					if (Cp.getInstance().getBL().consisteFormatoPin(pin)) {
						
						List<CpIdentidade> listaIdentidades = new ArrayList<CpIdentidade>();
						listaIdentidades = CpDao.getInstance().consultaIdentidadesPorCpf(cadastrante.getCpfPessoa().toString());	
						
						Cp.getInstance().getBL().definirPinIdentidade(listaIdentidades, pin, identidadeCadastrante);
						Cp.getInstance().getBL().enviarEmailDefinicaoPIN(cadastrante,"Alteração de PIN","Você alterou seu PIN.");
						
						resp.mensagem = "PIN foi alterado.";
						
					}	 
				}
				
	
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
		return "trocar PIN";
	}


	
}
