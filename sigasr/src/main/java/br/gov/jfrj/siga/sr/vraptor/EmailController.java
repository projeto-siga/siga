package br.gov.jfrj.siga.sr.vraptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.inject.Inject;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.HttpResult;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.notifiers.CorreioHolder;

/**
 * APenas para teste
 * @author DB1
 *
 */
@Deprecated
@Controller
@Path("app/email")
public class EmailController {
	
	private Result result;

	/**
	 * @deprecated CDI eyes only
	 */
	public EmailController() {
		super();
	}
	
	@Inject
	public EmailController(Result result) {
		this.result = result;
	}

	@Path("/todos")
	public void todos() throws Exception {
		notificarAbertura();
		notificarAtendente();
		notificarCancelamentoMovimentacao();
		notificarMovimentacao();
		notificarReplanejamentoMovimentacao();
		pesquisaSatisfacao();
	}
	
	@Path("/notificarAbertura")
	public void notificarAbertura() throws IOException {
		enviar(new ComandoEnviarEmail() {
			@Override
			public void executar() throws Exception {
				CorreioHolder
				.get()
				.notificarAbertura(SrSolicitacao.AR.findById(1858L));
			}
		}, "notificarAbertura");
	}
	
	@Path("/notificarMovimentacao")
	public void notificarMovimentacao() throws Exception {
		enviar(new ComandoEnviarEmail() {
			@Override
			public void executar() throws Exception {
				CorreioHolder
				.get()
				.notificarMovimentacao(SrMovimentacao.AR.findById(4980L));
			}
		}, "notificarMovimentacao");
	}

	@Path("/notificarAtendente")
	public void notificarAtendente() throws Exception {
		enviar(new ComandoEnviarEmail() {
			@Override
			public void executar() throws Exception {
				CorreioHolder
				.get()
				.notificarAtendente(SrMovimentacao.AR.findById(4860L), null);
			}
		}, "notificarAtendente");
	}

	@Path("/notificarReplanejamentoMovimentacao")
	public void notificarReplanejamentoMovimentacao() throws Exception {
		enviar(new ComandoEnviarEmail() {
			@Override
			public void executar() throws Exception {
				CorreioHolder
				.get()
				.notificarReplanejamentoMovimentacao(SrMovimentacao.AR.findById(4860L));
			}
		}, "notificarReplanejamentoMovimentacao");
	}

	@Path("/notificarCancelamentoMovimentacao")
	public void notificarCancelamentoMovimentacao() throws Exception {
		enviar(new ComandoEnviarEmail() {
			@Override
			public void executar() throws Exception {
				CorreioHolder
				.get()
				.notificarCancelamentoMovimentacao(SrMovimentacao.AR.findById(1462L));
			}
		}, "notificarCancelamentoMovimentacao");
	}

	@Path("/pesquisaSatisfacao")
	public void pesquisaSatisfacao() throws Exception {
		enviar(new ComandoEnviarEmail() {
			@Override
			public void executar() throws Exception {
				CorreioHolder
				.get()
				.pesquisaSatisfacao(SrSolicitacao.AR.findById(1858L));
			}
		}, "pesquisaSatisfacao");
	}

	public void enviar(ComandoEnviarEmail command, String nome) throws IOException {
		try {
			command.executar();
			result
				.use(Results.http())
				.body("<br>Email enviado com sucesso: " + nome);
		} catch (Exception e) {
			StringWriter writter = new StringWriter();
			e.printStackTrace(new PrintWriter(writter));
			
			HttpResult httpResult = result.use(Results.http());
			httpResult.sendError(500);
			httpResult.body(writter.toString());
			
			throw new RuntimeException(e);
		}
	}

	interface ComandoEnviarEmail {
		void executar() throws Exception;
	}
}