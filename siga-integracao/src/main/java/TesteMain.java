import java.util.List;

import br.gov.jfrj.siga.integracao.ws.pubnet.dto.CancelaMaterialDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.EnviaPublicacaoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.JustificativasCancelamentoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.MaterialEnviadoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.MontaReciboPublicacaoCancelamentoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.MontaReciboPublicacaoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.TokenDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.AuthHeader;
import br.gov.jfrj.siga.integracao.ws.pubnet.service.PubnetCancelamentoService;
import br.gov.jfrj.siga.integracao.ws.pubnet.service.PubnetConsultaService;
import br.gov.jfrj.siga.integracao.ws.pubnet.service.PubnetEnvioService;
import br.gov.jfrj.siga.integracao.ws.pubnet.utils.CertificadoDigitalUtils;

public class TesteMain {
	
	

	public static void main(String[] args) throws Exception {

		System.out.println("Chamando servico...");
		AuthHeader user = new AuthHeader();
		user.setUserName("FernandoHP2");
		user.setPassword("bffb2B82E0");
		String passCert = "123456";
//		user.setUserName("WellingtonBdC");
//		user.setPassword("123456");
//		user.setUserName("RICARDOBRT");
//		user.setPassword("d356fE4B8C");
		
		
		
		CertificadoDigitalUtils cert = new CertificadoDigitalUtils();

		/************** Consultas ************/
		PubnetConsultaService consultaService = new PubnetConsultaService();
		
		//TokenDto tok = consultaService.gerarToken("RICARDOBRT", "13432016832", "ricardobrasil@imprensaoficial.com.br");
//		TokenDto tok = consultaService.gerarToken("FernandoHP2", "32417729857", "fernando.pascott@apoioprodesp.sp.gov.br");
//		System.out.println(tok.getToken());//bffb2B82E0
//		System.exit(0);
//		    - consultaPermissoesPublicante 
//		    Caso j� tenha carregado a lista anteriormente esse item n�o se faz necessario.
//		System.out.println("\n\n consultarPermissaoPublicante");
//		List<PermissaoPublicanteDto> permissaoPublicanteDtos = consultaService.consultarPermissaoPublicante(user);
//		System.out.println(permissaoPublicanteDtos);

//		    - consultaMaterialEnviado
//		    Caso j� tenha carregado a lista anteriormente esse item n�o se faz necess�rio, pode ser utilizado para confirmar que o material que se deseja publicar ainda n�o foi enviado.
		System.out.println("\n\n consultarMaterialEnviado");
		List<MaterialEnviadoDto> enviadoDtos = consultaService.consultarMaterialEnviado(user, "2019-01-01",
				"2022-10-01");
		System.out.println(enviadoDtos);

//		    - listaJustificativasCancelamento
//		    Caso j� tenha carregado a lista anteriormente esse item n�o se faz necessario.
		System.out.println("\n\n listarJustificativasCancelamento");
		List<JustificativasCancelamentoDto> justificativasCancelamentoDtos = consultaService
				.listarJustificativasCancelamento(user);
		System.out.println(justificativasCancelamentoDtos);

		/************** Envio ************/
		PubnetEnvioService envioService = new PubnetEnvioService();
//		    - montaReciboPublicacao
//		    Com as referencias obtidas no consultaPermissoesPublicante, solicita-se a cria��o do recibo no formado definido pelo sistema. Sistema retorna texto que dever� ser assinado pelo publicante com certificado ICP-BRASIL
		System.out.println("\n\n montarReciboPublicacao");
		MontaReciboPublicacaoDto reciboPublicacaoDto = envioService.montarReciboPublicacao(user, "3308", "1", "3308",
				"1", "315", CertificadoDigitalUtils.CriarMD5DevolverHex("São Paulo 21 de setembro de 2022. Teste de encode de arquivo texto gerado pelo WS. A aviação é muito importante para o Brasil! O brasilerio precisa ter uma boa saúde. Vai chover ? Uma nota de cinqüenta vale o mesmo que 2 de vinte ? Fim do teste."));
		System.out.println(reciboPublicacaoDto);
		
		;

//		    - enviaPublicacao
//		    Monta a solicita��o com as mesmas informa��es que foram passadas para a montagem do recibo, acrescenta a s�ntese que ser� publicada e o recibo assinado pelo publicante.
		System.out.println("\n\n enviarPublicacao");
		EnviaPublicacaoDto enviaPublicacaoDto = envioService.enviarPublicacao(user, "3308", "1", "3308", "1", "315",
				"São Paulo 21 de setembro de 2022. Teste de encode de arquivo texto gerado pelo WS. A aviação é muito importante para o Brasil! O brasilerio precisa ter uma boa saúde. Vai chover ? Uma nota de cinqüenta vale o mesmo que 2 de vinte ? Fim do teste.", cert.assinarConteudoComCertificado(CertificadoDigitalTeste.obterCertificado(passCert), passCert, reciboPublicacaoDto.getTextoRecibo()), reciboPublicacaoDto.getHashRecibo());
		System.out.println(enviaPublicacaoDto);
		
		System.exit(0);
		/************** Cancelamento ************/
		PubnetCancelamentoService cancelamentoService = new PubnetCancelamentoService();

//		- montaReciboPublicacaoCancelamento
//		Com a referencia obtidas no consultaMaterialEnviado, solicita-se a cria��o do recibo. O retorno dever� ser assinado pelo publicante com certificado ICP-BRASIL
		System.out.println("\n\n montarReciboPublicacaoCancelamento");
		MontaReciboPublicacaoCancelamentoDto montaReciboPublicacaoCancelamentoDto = cancelamentoService
				.montarReciboPublicacaoCancelamento(user, "E2.ZABNJ.18.001.********.TXT");
		System.out.println(montaReciboPublicacaoCancelamentoDto);

//		- cancelaMaterial
//		Monta a solicita��o com as mesmas informa��es que foram passadas para a montagem do recibo, acrescenta o recibo assinado pelo publicante.
		System.out.println("\n\n cancelarMaterial");
		CancelaMaterialDto cancelaMaterialDto = cancelamentoService.cancelarMaterial(user,
				"E2.ZABNJ.18.001.TXT", "justificativa", "recibo", "reciboHash");
		System.out.println(cancelaMaterialDto);
		
		// TODO onde usar o token
		System.out.println("\n\n gerarToken");
		TokenDto token = consultaService.gerarToken("WellingtonBdC", "E2.ZABNJ.18.001.********.TXT",
				"wbcarvalho@sp.gov.br");
		System.out.println(token);

		System.exit(0);
	}

}
