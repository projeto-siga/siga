package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.INumeracaoGenericaPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class NumeracaoGenericaPost implements INumeracaoGenericaPost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		try {
			final ExBL exBL = Ex.getInstance().getBL();

			if (req.tiposequencia == null || req.tiposequencia.isEmpty()) {
				throw new AplicacaoException("Tipo de Sequência não informada.");
			}

			if (req.anoemissao == null || req.anoemissao.isEmpty()) {
				throw new AplicacaoException("Ano não informado.");
			}
			
			if (req.zerarinicioano == null || req.zerarinicioano.isEmpty()) {
				throw new AplicacaoException("Flag zerar início ano não informada.");
			}
			
			if (!req.zerarinicioano.trim().equals("0") && !req.zerarinicioano.trim().equals("1") ) {
				throw new AplicacaoException("Flag zerar início inválida: " + req.zerarinicioano + ". Esperado: 0 ou 1");
			}

			Long numrAnoEmissao = null;
			try {
				numrAnoEmissao = Long.parseLong(req.anoemissao);
			} catch (NumberFormatException e) {
				throw new AplicacaoException("Ano de emissão inválido: " + req.anoemissao);
			}
			if (numrAnoEmissao < 1000 || numrAnoEmissao > 9999)
				throw new AplicacaoException("Ano de emissão inválido: " + req.anoemissao);

			Integer numrTipoSequencia = null;
			try {
				numrTipoSequencia = Integer.parseInt(req.tiposequencia);
			} catch (NumberFormatException e) {
				throw new AplicacaoException("Tipo de sequência inválida: " + req.tiposequencia);
			}

			String numrSequencia = "000000" + exBL.obterSequencia(numrAnoEmissao, numrTipoSequencia, req.zerarinicioano);
			numrSequencia = numrSequencia.substring(numrSequencia.length() - 6, numrSequencia.length());
			numrSequencia = numrAnoEmissao + numrSequencia;
			resp.sequenciagenerica = numrSequencia;

			resp.digitoverificador = exBL.calcularDigitoVerificador(numrSequencia);

		} catch (RegraNegocioException | AplicacaoException e) {
			throw new SwaggerException(e.getMessage(), 400, null, req, resp, null);
		}
	}

	protected ExDao dao() {
		return ExDao.getInstance();
	}

	@Override
	public String getContext() {
		return "Gerar Numeracao de Sequencia Generica";
	}

}