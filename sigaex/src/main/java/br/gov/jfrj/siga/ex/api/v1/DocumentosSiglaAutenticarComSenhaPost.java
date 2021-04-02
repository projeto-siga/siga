package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaAutenticarComSenhaPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaAutenticarComSenhaPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaAutenticarComSenhaPost;
import br.gov.jfrj.siga.ex.bl.Ex;

public class DocumentosSiglaAutenticarComSenhaPost extends DocumentosSiglaAssinarAutenticarComSenhaPost
		implements IDocumentosSiglaAutenticarComSenhaPost {
	
	public DocumentosSiglaAutenticarComSenhaPost() {
		super(true);
	}

	@Override
	protected void assertDocumento(DpPessoa titular, DpLotacao lotaTitular, ExMobil mob) throws Exception {
		// Documento é Capturado?
		Long idTipoDoc = mob.getDoc().getExTipoDocumento().getId();
		if (!(idTipoDoc == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_CAPTURADO
				|| idTipoDoc == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_CAPTURADO)) {
			throw new SwaggerException("Documento não é Capturado", 400, null, null, null, null);
		}

		// O documento já foi autenticado?
		if (!mob.getDoc().getAutenticacoesComTokenOuSenha().isEmpty()) {
			throw new SwaggerException("Documento já autenticado anteriormente", 400, null, null, null, null);
		}

		// Usuário pode Autenticar o documento?
		if (!Ex.getInstance().getComp().podeAutenticarComSenha(titular, lotaTitular, mob)) {
			throw new PresentableUnloggedException(
					"O documento " + mob.getSigla() + " não pode ser autenticado com senha por "
							+ titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta());
		}
	}

	@Override
	public void run(DocumentosSiglaAutenticarComSenhaPostRequest req, DocumentosSiglaAutenticarComSenhaPostResponse resp)
			throws Exception {
		try {
			super.executar(req.sigla, (sigla, status) -> {
				resp.sigla = sigla;
				resp.status = status;
			});
		} catch (RegraNegocioException | AplicacaoException e) {
			throw new SwaggerException(e.getMessage(), 400, null, req, resp, null);
		}
	}

	@Override
	public String getContext() {
		return "Registrar autenticação com senha";
	}

}
