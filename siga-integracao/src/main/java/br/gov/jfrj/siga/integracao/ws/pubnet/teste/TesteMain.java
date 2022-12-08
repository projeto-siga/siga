package br.gov.jfrj.siga.integracao.ws.pubnet.teste;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.CancelaMaterialDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.EnviaPublicacaoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.MontaReciboPublicacaoCancelamentoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.MontaReciboPublicacaoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.AuthHeader;
import br.gov.jfrj.siga.integracao.ws.pubnet.service.PubnetCancelamentoService;
import br.gov.jfrj.siga.integracao.ws.pubnet.service.PubnetEnvioService;
import br.gov.jfrj.siga.integracao.ws.pubnet.utils.CertificadoDigitalUtils;

public class TesteMain {
	
	

	public static void main(String[] args) throws Exception {

		System.out.println("Chamando servico...");
		AuthHeader user = new AuthHeader();
		user.setUserName("FernandoH44P2");
		user.setPassword("454545454");
		
		CertificadoDigitalTeste cert = new CertificadoDigitalTeste();

		/************** Consultas ************/
//		PubnetConsultaService consultaService = new PubnetConsultaService();
		
//		TokenDto tok = consultaService.gerarToken("FernandoHP2", "1111111111", "fernando.pascott@ffff.sp.gov.br");
//		System.out.println(tok.getToken());
//		System.exit(0);
//		    - consultaPermissoesPublicante 
//		    Caso j� tenha carregado a lista anteriormente esse item n�o se faz necessario.
//		System.out.println("\n\n consultarPermissaoPublicante");
//		List<PermissaoPublicanteDto> permissaoPublicanteDtos = consultaService.consultarPermissaoPublicante(user);
//		System.out.println(permissaoPublicanteDtos);

//		    - consultaMaterialEnviado
//		    Caso j� tenha carregado a lista anteriormente esse item n�o se faz necess�rio, pode ser utilizado para confirmar que o material que se deseja publicar ainda n�o foi enviado.
//		System.out.println("\n\n consultarMaterialEnviado");
//		List<MaterialEnviadoDto> enviadoDtos = consultaService.consultarMaterialEnviado(user, "2019-01-01",
//				"2022-10-01");
//		System.out.println(enviadoDtos);

//		    - listaJustificativasCancelamento
//		    Caso j� tenha carregado a lista anteriormente esse item n�o se faz necessario.
//		System.out.println("\n\n listarJustificativasCancelamento");
//		List<JustificativasCancelamentoDto> justificativasCancelamentoDtos = consultaService
//				.listarJustificativasCancelamento(user);
//		System.out.println(justificativasCancelamentoDtos);
		
		
		

		/************** Envio ************/
		PubnetEnvioService envioService = new PubnetEnvioService();
//		    - montaReciboPublicacao
//		    Com as referencias obtidas no consultaPermissoesPublicante, solicita-se a cria��o do recibo no formado definido pelo sistema. Sistema retorna texto que dever� ser assinado pelo publicante com certificado ICP-BRASIL
		System.out.println("\n\n montarReciboPublicacao");
		MontaReciboPublicacaoDto reciboPublicacaoDto = envioService.montarReciboPublicacao(user, "3308", "1", "3308",
				"1", "310", CertificadoDigitalUtils.CriarMD5DevolverHex("Texto Aleatório para Publicação"));
		System.out.println(reciboPublicacaoDto);
		
//		    - enviaPublicacao
//		    Monta a solicita��o com as mesmas informa��es que foram passadas para a montagem do recibo, acrescenta a s�ntese que ser� publicada e o recibo assinado pelo publicante.
//		System.out.println("\n\n enviarPublicacao");
		EnviaPublicacaoDto enviaPublicacaoDto = envioService.enviarPublicacao(user, "3308", "1", "3308", "1", "310",
				"Texto Aleatório para Publicação", 
				cert.obterReciboAssinadoHex(reciboPublicacaoDto.getTextoRecibo()), reciboPublicacaoDto.getHashRecibo());
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
		
		System.exit(0);
	}

}
