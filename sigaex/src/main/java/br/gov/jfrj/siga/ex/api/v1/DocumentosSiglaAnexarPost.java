package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerUtils;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaAnexarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosSiglaAnexarPost implements IDocumentosSiglaAnexarPost {
	public DocumentosSiglaAnexarPost() {
		SwaggerUtils.setUploadHandler(new ArquivoUploadHandler());
	}

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento que Receberá o Anexo");
		if (!Ex.getInstance().getComp().podeAnexarArquivo(ctx.getTitular(), ctx.getLotaTitular(), mob))
			throw new SwaggerException("Anexação no documento " + mob.getSigla() + " não é permitida. ("
					+ ctx.getTitular().getSigla() + "/" + ctx.getLotaTitular().getSiglaCompleta() + ")", 403, null, req,
					resp, null);

		// Insere PDF de documento capturado
		//
		if (req.content == null)
			throw new SwaggerException("PDF deve ser informado", 403, null, req, resp, null);

		Integer numBytes = null;
		final byte[] baArquivo = (byte[]) req.content;
		if (baArquivo == null) {
			throw new AplicacaoException("Arquivo vazio não pode ser anexado.");
		}
		numBytes = baArquivo.length;
		if (numBytes > 10 * 1024 * 1024) {
			throw new AplicacaoException("Não é permitida a anexação de arquivos com mais de 10MB.");
		}

		ExMovimentacao mov = Ex.getInstance().getBL().anexarArquivo(ctx.getCadastrante(), ctx.getLotaCadastrante(), mob,
				null, null, req.descricaodocumento, ctx.getTitular(), ctx.getLotaTitular(), baArquivo,
				"application/pdf", null, null);
		resp.id = mov.getIdMov().toString();
	}

	protected ExDao dao() {
		return ExDao.getInstance();
	}

	@Override
	public String getContext() {
		return "Incluir anexo";
	}

}