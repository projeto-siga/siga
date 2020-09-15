package br.gov.jfrj.siga.ex.api.v1;

import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.ISugestaoPost;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.SugestaoPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.SugestaoPostResponse;

public class SugestaoPost implements ISugestaoPost {

	@Override
	public void run(SugestaoPostRequest req, SugestaoPostResponse resp) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("Nome: ");
		sb.append(req.nome);
		sb.append("\nEmail: ");
		sb.append(req.email);
		sb.append("\n\nMensagem: ");
		sb.append(req.mensagem);
		Correio.enviar(SwaggerServlet.getProperty("smtp.sugestao.destinatario"),
				SwaggerServlet.getProperty("smtp.sugestao.assunto"), sb.toString());
	}

	@Override
	public String getContext() {
		return "enviar sugestao";
	}

}
