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

public class TesteMain {
	
	

	public static void main(String[] args) throws Exception {

		System.out.println("Chamando servico...");
		AuthHeader user = new AuthHeader();
//		user.setUserName("WellingtonBdC");
//		user.setPassword("123456");
		user.setUserName("RICARDOBRT");
		user.setPassword("d356fE4B8C");
		
		
		
		CertificadoDigitalTeste cert = new CertificadoDigitalTeste();

		/************** Consultas ************/
		PubnetConsultaService consultaService = new PubnetConsultaService();
		
		//TokenDto tok = consultaService.gerarToken("RICARDOBRT", "13432016832", "ricardobrasil@imprensaoficial.com.br");
		//System.out.println(tok.getToken());d356fE4B8C
//		    - consultaPermissoesPublicante 
//		    Caso já tenha carregado a lista anteriormente esse item não se faz necessario.
//		System.out.println("\n\n consultarPermissaoPublicante");
//		List<PermissaoPublicanteDto> permissaoPublicanteDtos = consultaService.consultarPermissaoPublicante(user);
//		System.out.println(permissaoPublicanteDtos);

//		    - consultaMaterialEnviado
//		    Caso já tenha carregado a lista anteriormente esse item não se faz necessário, pode ser utilizado para confirmar que o material que se deseja publicar ainda não foi enviado.
		System.out.println("\n\n consultarMaterialEnviado");
		List<MaterialEnviadoDto> enviadoDtos = consultaService.consultarMaterialEnviado(user, "2019-01-01",
				"2022-10-01");
		System.out.println(enviadoDtos);

//		    - listaJustificativasCancelamento
//		    Caso já tenha carregado a lista anteriormente esse item não se faz necessario.
		System.out.println("\n\n listarJustificativasCancelamento");
		List<JustificativasCancelamentoDto> justificativasCancelamentoDtos = consultaService
				.listarJustificativasCancelamento(user);
		System.out.println(justificativasCancelamentoDtos);

		/************** Envio ************/
		PubnetEnvioService envioService = new PubnetEnvioService();
//		    - montaReciboPublicacao
//		    Com as referencias obtidas no consultaPermissoesPublicante, solicita-se a criação do recibo no formado definido pelo sistema. Sistema retorna texto que deverá ser assinado pelo publicante com certificado ICP-BRASIL
		System.out.println("\n\n montarReciboPublicacao");
		MontaReciboPublicacaoDto reciboPublicacaoDto = envioService.montarReciboPublicacao(user, "3308", "1", "3308",
				"1", "317", "eaff34d51f435f94c410106117313239");
		System.out.println(reciboPublicacaoDto);
		
		;

//		    - enviaPublicacao
//		    Monta a solicitação com as mesmas informações que foram passadas para a montagem do recibo, acrescenta a síntese que será publicada e o recibo assinado pelo publicante.
		System.out.println("\n\n enviarPublicacao");
		EnviaPublicacaoDto enviaPublicacaoDto = envioService.enviarPublicacao(user, "3308", "1", "3308", "1", "317",
				"Usage : wsdl2java-fe", cert.lerCertificado(reciboPublicacaoDto.getTextoRecibo(), true), reciboPublicacaoDto.getHashRecibo());
		System.out.println(enviaPublicacaoDto);
		
		System.exit(0);
		/************** Cancelamento ************/
		PubnetCancelamentoService cancelamentoService = new PubnetCancelamentoService();

//		- montaReciboPublicacaoCancelamento
//		Com a referencia obtidas no consultaMaterialEnviado, solicita-se a criação do recibo. O retorno deverá ser assinado pelo publicante com certificado ICP-BRASIL
		System.out.println("\n\n montarReciboPublicacaoCancelamento");
		MontaReciboPublicacaoCancelamentoDto montaReciboPublicacaoCancelamentoDto = cancelamentoService
				.montarReciboPublicacaoCancelamento(user, "E2.ZABNJ.18.001.********.TXT");
		System.out.println(montaReciboPublicacaoCancelamentoDto);

//		- cancelaMaterial
//		Monta a solicitação com as mesmas informações que foram passadas para a montagem do recibo, acrescenta o recibo assinado pelo publicante.
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
