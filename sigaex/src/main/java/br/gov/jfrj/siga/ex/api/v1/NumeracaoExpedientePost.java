package br.gov.jfrj.siga.ex.api.v1;

import javax.persistence.NoResultException;

import com.crivano.swaggerservlet.SwaggerException;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.INumeracaoExpedientePost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class NumeracaoExpedientePost implements INumeracaoExpedientePost {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		try {
			final ExBL exBL = Ex.getInstance().getBL();

			if (req.formadoc == null || req.formadoc.isEmpty()) {
				throw new AplicacaoException("Forma do documento não informada.");
			}

			if (req.anoemissao == null || req.anoemissao.isEmpty()) {
				throw new AplicacaoException("Ano não informado.");
			}

			ExFormaDocumento exFormaDocumento = new ExFormaDocumento();
			exFormaDocumento.setSigla(req.formadoc.trim().toUpperCase());
			try {
				exFormaDocumento = dao().consultarPorSigla(exFormaDocumento);
			} catch (NoResultException e) {
				throw new AplicacaoException("Não foi encontrada Forma de Documento: " + req.formadoc);
			}

			Long numrAnoEmissao = null;
			try {
				numrAnoEmissao = Long.parseLong(req.anoemissao);
			} catch (NumberFormatException e) {
				throw new AplicacaoException("Ano de emissão inválido: " + req.anoemissao);
			}
			if (numrAnoEmissao < 1000 || numrAnoEmissao > 9999)
				throw new AplicacaoException("Ano de emissão inválido: " + req.anoemissao);

			CpOrgaoUsuario cpOrgaoUsuario = new CpOrgaoUsuario();
			cpOrgaoUsuario.setSigla(req.siglaorgao.trim().toUpperCase());
			try {
				cpOrgaoUsuario = dao().consultarPorSigla(cpOrgaoUsuario);
			} catch (NoResultException e) {
				throw new AplicacaoException("Não foi encontrada Órgão: " + req.siglaorgao);
			}

			if(cpOrgaoUsuario == null)
				throw new AplicacaoException("Não foi encontrado órgão para a sigla: " + req.siglaorgao);
				
			String numeracaoExpediente = "0000" + exBL.obterNumeracaoExpediente(cpOrgaoUsuario.getId(), exFormaDocumento.getId(), numrAnoEmissao);
			numeracaoExpediente = numeracaoExpediente.substring(numeracaoExpediente.length()-5, numeracaoExpediente.length());
			
			resp.sigladoc = cpOrgaoUsuario.getSigla() + "-" + exFormaDocumento.getSigla() + "-" + numrAnoEmissao + "/" + numeracaoExpediente;
		
		} catch (RegraNegocioException | AplicacaoException e) {
			throw new SwaggerException(e.getMessage(), 400, null, req, resp, null);
		}
	}

	protected ExDao dao() {
		return ExDao.getInstance();
	}

	@Override
	public String getContext() {
		return "Gerar Numeracao Expediente";
	}

}