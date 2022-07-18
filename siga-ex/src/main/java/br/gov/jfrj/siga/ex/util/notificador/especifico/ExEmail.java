package br.gov.jfrj.siga.ex.util.notificador.especifico;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

/**
 * Essa classe tem a responsabilidade de montar e enviar o e-mail.
 * 
 */

public class ExEmail implements ExEnviarEmail, ExMontarEmail {

	public void enviarAoTramitarDocParaUsuario(DpPessoa pessoaDest, DpPessoa titular, String sigla) {
		String assunto = "Documento tramitado para " + pessoaDest.getDescricao();
		String[] destinanarios = { pessoaDest.getEmailPessoaAtual() };
		String conteudoHTML = docTramitadoParaUsuario(pessoaDest, titular, sigla);
		try {
			Correio.enviar(null,destinanarios, assunto, "", conteudoHTML);
		} catch (Exception e) {
			throw new AplicacaoException("Ocorreu um erro durante o envio do email", 0, e);
		}
	}

	@Override
	public void enviarAoDestinatarioExterno(String nomeDestinatario, String emailDestinatario,
											String sigla, String numeroReferencia, String cod, String urlDoc) {

		String assunto = "Código para visualização do documento " + sigla;
		String[] destinanarios = { emailDestinatario };
		String conteudoHTML = docEnviadoParaDestinatarioExterno(nomeDestinatario, emailDestinatario,
				sigla, numeroReferencia, cod, urlDoc);
		try {
			Correio.enviar(null, destinanarios, assunto, "", conteudoHTML);
		} catch (Exception e) {
			throw new AplicacaoException("Ocorreu um erro durante o envio do email", 0, e);
		}
	}

	public void enviarAoResponsavelPelaAssinatura(DpPessoa pessoaDest, DpPessoa titular, String sigla) {
		String assunto = "Responsável pela assinatura: " + pessoaDest.getDescricao();
		String[] destinanarios = { pessoaDest.getEmailPessoaAtual() };
		String conteudoHTML = responsavelPelaAssinatura(pessoaDest, titular, sigla);
		try {
			Correio.enviar(null,destinanarios, assunto, "", conteudoHTML);
		} catch (Exception e) {
			throw new AplicacaoException("Ocorreu um erro durante o envio do email", 0, e);
		}
	}

	public void enviarAoTramitarDocMarcado(DpPessoa pessoaDest, DpPessoa titular, String sigla, String marcador) {
		String assunto = "Documento tramitado para " + pessoaDest.getDescricao();
		String[] destinanarios = { pessoaDest.getEmailPessoaAtual() };
		String conteudoHTML = docMarcadoTramitadoParaUsuario(pessoaDest, titular, sigla, marcador);
		try {
			if (!marcador.equals(""))
			Correio.enviar(null,destinanarios, assunto, "", conteudoHTML);
		} catch (Exception e) {
			throw new AplicacaoException("Ocorreu um erro durante o envio do email", 0, e);
		}
	}

	public void enviarAoTramitarDocParaUsuariosDaUnidade(DpLotacao lotaDest, DpPessoa pessoa, String sigla) {
		String assunto = "Documento tramitado para unidade " + lotaDest.getDescricao();
		String[] destinanarios = { pessoa.getEmailPessoaAtual() };
		String conteudoHTML = docTramitadoParaUnidade(pessoa, lotaDest, sigla);
		try {
			Correio.enviar(null,destinanarios, assunto, "", conteudoHTML);
		} catch (Exception e) {
			throw new AplicacaoException("Ocorreu um erro durante o envio do email", 0, e);
		}
	}

	public void enviarAoCossignatario(DpPessoa pessoaDest, DpPessoa titular, String sigla) {
		String assunto = "Documento tramitado para " + pessoaDest.getDescricao();
		String[] destinanarios = { pessoaDest.getEmailPessoaAtual() };
		String conteudoHTML = incluirCossignatario(pessoaDest, titular, sigla);
		try {
			Correio.enviar(null,destinanarios, assunto, "", conteudoHTML);
		} catch (Exception e) {
			throw new AplicacaoException("Ocorreu um erro durante o envio do email", 0, e);
		}
	}
	
	@Override
	public String docTramitadoParaUsuario(DpPessoa destinatario, DpPessoa cadastrante, String siglaDoc) {		 
		String conteudo = "";
		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(ExTemplateEmail.DOCUMENTO_TRAMITADO_PARA_USUARIO.getPath()),StandardCharsets.UTF_8))) {			
			String str;
			while((str = bfr.readLine()) != null) {
				conteudo += str;
			}
			conteudo = conteudo
					.replace("${url}", Prop.get("/siga.base.url"))
					.replace("${logo}", Prop.get("/siga.email.logo"))
					.replace("${titulo}", Prop.get("/siga.email.titulo"))
					.replace("${nomeDestinatario}", destinatario.getNomePessoa())  
					.replace("${siglaDestinatario}", destinatario.getSiglaCompleta())
					.replace("${nomeCadastrante}", cadastrante.getNomePessoa())
					.replace("${siglaCadastrante}", cadastrante.getSigla())
					.replace("${siglaDoc}", siglaDoc);

			return conteudo;

		} catch (IOException e) {
			throw new AplicacaoException("Erro ao montar e-mail para enviar ao usuário " + destinatario.getNomePessoa());
		}	
	}

	@Override
	public String docEnviadoParaDestinatarioExterno(String nomeDestinatario, 
													String emailDestinatario, 
													String siglaDoc,
													String numeroReferencia,
													String cod,
													String urlDoc
	) {
		String conteudo = "";
		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(
				getClass().getResourceAsStream(ExTemplateEmail.DOCUMENTO_ENVIADO_PARA_USUARIO_EXTERNO.getPath()),
						StandardCharsets.UTF_8))) {
			String str;
			while((str = bfr.readLine()) != null) {
				conteudo += str;
			}
			conteudo = conteudo
					.replace("${url}", Prop.get("/siga.base.url"))
					.replace("${logo}", Prop.get("/siga.email.logo"))
					.replace("${titulo}", Prop.get("/siga.email.titulo"))
					.replace("${nomeDestinatario}", nomeDestinatario)
					.replace("${siglaDoc}", siglaDoc)
					.replace("${numeroReferencia}", numeroReferencia)
					.replace("${cod}", cod)
					.replace("${urlDoc}", urlDoc);

			return conteudo;

		} catch (IOException e) {
			throw new AplicacaoException("Erro ao montar e-mail para enviar ao usuário " + emailDestinatario);
		}
	}

	@Override
	public String docMarcadoTramitadoParaUsuario(DpPessoa destinatario, DpPessoa cadastrante, String docSigla, String marcador) {		 
		String conteudo = "";
		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(ExTemplateEmail.DOCUMENTO_COM_MARCAS_TRAMITADO_PARA_USUARIO_OU_UNIDADE.getPath()),StandardCharsets.UTF_8))) {			
			String str;
			while((str = bfr.readLine()) != null) {
				conteudo += str;
			}
			conteudo = conteudo
					.replace("${url}", Prop.get("/siga.base.url"))
					.replace("${logo}", Prop.get("/siga.email.logo"))
					.replace("${titulo}", Prop.get("/siga.email.titulo"))
					.replace("${nomeDestinatario}", destinatario.getNomePessoa())  
					.replace("${siglaDestinatario}", destinatario.getSiglaCompleta())
					.replace("${nomeCadastrante}", cadastrante.getNomePessoa())
					.replace("${siglaCadastrante}", cadastrante.getSigla())
					.replace("${docSigla}", docSigla)
					.replace("${marcador}", marcador); 

			return conteudo;

		} catch (IOException e) {
			throw new AplicacaoException("Erro ao montar e-mail para enviar ao usuário " + destinatario.getNomePessoa());
		}	
	}

	@Override
	public String responsavelPelaAssinatura(DpPessoa destinatario, DpPessoa cadastrante, String siglaDoc) {		 
		String conteudo = "";
		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(ExTemplateEmail.RESPONSAVEL_PELA_ASSINATURA.getPath()),StandardCharsets.UTF_8))) {			
			String str;
			while((str = bfr.readLine()) != null) {
				conteudo += str;
			}
			conteudo = conteudo
					.replace("${url}", Prop.get("/siga.base.url"))
					.replace("${logo}", Prop.get("/siga.email.logo"))
					.replace("${titulo}", Prop.get("/siga.email.titulo"))
					.replace("${nomeDestinatario}", destinatario.getNomePessoa())  
					.replace("${siglaDestinatario}", destinatario.getSiglaCompleta())
					.replace("${nomeCadastrante}", cadastrante.getNomePessoa())
					.replace("${siglaCadastrante}", cadastrante.getSigla())
					.replace("${siglaDoc}", siglaDoc);

			return conteudo;

		} catch (IOException e) {
			throw new AplicacaoException("Erro ao montar e-mail para enviar ao usuário " + destinatario.getNomePessoa());
		}	
	}

	@Override
	public String incluirCossignatario(DpPessoa destinatario, DpPessoa cadastrante, String siglaDoc) {		 
		String conteudo = "";
		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(ExTemplateEmail.INCLUIDO_COMO_COSSIGNATARIO.getPath()),StandardCharsets.UTF_8))) {			
			String str;
			while((str = bfr.readLine()) != null) {
				conteudo += str;
			}
			conteudo = conteudo
					.replace("${url}", Prop.get("/siga.base.url"))
					.replace("${logo}", Prop.get("/siga.email.logo"))
					.replace("${titulo}", Prop.get("/siga.email.titulo"))
					.replace("${nomeDestinatario}", destinatario.getNomePessoa())  
					.replace("${siglaDestinatario}", destinatario.getSiglaCompleta())
					.replace("${nomeCadastrante}", cadastrante.getNomePessoa())
					.replace("${siglaCadastrante}", cadastrante.getSigla()) 
					.replace("${siglaDoc}", siglaDoc);

			return conteudo;

		} catch (IOException e) {
			throw new AplicacaoException("Erro ao montar e-mail para enviar ao usuário " + destinatario.getNomePessoa());
		}	
	}

	@Override
	public String docTramitadoParaUnidade(DpPessoa destinatario, DpLotacao lotacao, String docSigla) {		 
		String conteudo = "";
		try (BufferedReader bfr = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(ExTemplateEmail.DOCUMENTO_TRAMITADO_PARA_UNIDADE.getPath()),StandardCharsets.UTF_8))) {			
			String str;
			while((str = bfr.readLine()) != null) {
				conteudo += str;
			}
			conteudo = conteudo
					.replace("${url}", Prop.get("/siga.base.url"))
					.replace("${logo}", Prop.get("/siga.email.logo"))
					.replace("${titulo}", Prop.get("/siga.email.titulo"))
					.replace("${docSigla}", docSigla)
					.replace("${lotaDesc}", lotacao.getDescricao())
					.replace("${lotaSigla}", lotacao.getSigla());

			return conteudo;

		} catch (IOException e) {
			throw new AplicacaoException("Erro ao montar e-mail para enviar ao usuário " + destinatario.getNomePessoa());
		}	
	}
	
}
