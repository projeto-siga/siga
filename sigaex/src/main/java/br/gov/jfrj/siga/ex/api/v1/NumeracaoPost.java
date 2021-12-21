package br.gov.jfrj.siga.ex.api.v1;

import javax.persistence.NoResultException;

import com.crivano.swaggerservlet.SwaggerUtils;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.INumeracaoPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class NumeracaoPost implements INumeracaoPost {
	
	public NumeracaoPost() {
		SwaggerUtils.setUploadHandler(new ArquivoUploadHandler());
	}

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		final ExBL exBL = Ex.getInstance().getBL();

		if (req.formadoc == null || req.formadoc.isEmpty()) {
			throw new AplicacaoException("Forma do documento não informada.");
		}

		if (req.anoemissao == null || req.anoemissao.isEmpty()) {
			throw new AplicacaoException("Ano não informado.");
		}

		ExFormaDocumento exFormaDocumento = new ExFormaDocumento();
		exFormaDocumento.setSigla(req.formadoc);
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

		Long idOrgaoUsuario = ctx.getCadastrante().getOrgaoUsuario().getId();

		Integer numrTipoSequencia = null;
		try {
			if(req.tiposequencia != null && !req.tiposequencia.trim().isEmpty())
				numrTipoSequencia = Integer.parseInt(req.tiposequencia);
		} catch (NumberFormatException e) {
			throw new AplicacaoException("Tipo de sequência inválida: " + req.tiposequencia);
		}

		String numeracaoExpediente = exBL.obterNumeracaoExpediente(idOrgaoUsuario, exFormaDocumento.getId(), numrAnoEmissao);
		
		String numrSequencia = null;
		if(numrTipoSequencia != null) {
			numrSequencia = "000000" + exBL.obterSequencia(numrAnoEmissao, numrTipoSequencia, "1");
			numrSequencia = numrSequencia.substring(numrSequencia.length() - 6, numrSequencia.length());
			numrSequencia = numrAnoEmissao + numrSequencia;
			resp.sequenciagenerica = numrSequencia;
		}

		resp.sigladoc = ctx.getCadastrante().getOrgaoUsuario().getSigla() + "-" + exFormaDocumento.getSigla() + "-" + numrAnoEmissao + "/" + numeracaoExpediente;
		
		if(numrSequencia != null)
			resp.digitoverificador = exBL.calcularDigitoVerificador(numrSequencia);
	}

	protected ExDao dao() {
		return ExDao.getInstance();
	}

	@Override
	public String getContext() {
		return "Gerar Numeracao";
	}

}