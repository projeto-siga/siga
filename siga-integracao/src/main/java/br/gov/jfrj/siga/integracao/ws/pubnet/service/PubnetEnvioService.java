package br.gov.jfrj.siga.integracao.ws.pubnet.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;

import br.gov.jfrj.siga.integracao.ws.pubnet.dto.EnviaPublicacaoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.MontaReciboPublicacaoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.AuthHeader;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.EnviaPublicacaoResponse.EnviaPublicacaoResult;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.MontaReciboPublicacaoResponse.MontaReciboPublicacaoResult;

public class PubnetEnvioService extends PubnetConsultaService {

	public PubnetEnvioService() {
		super();
	}

	public MontaReciboPublicacaoDto montarReciboPublicacao(AuthHeader user, String anuncianteIdentificador,
			String cadernoIdentificador, String retrancaCodigo, String tipomaterialIdentificador, String sequencial,
			String hashMD5Sintese) {
		MontaReciboPublicacaoDto reciboPublicDto = new MontaReciboPublicacaoDto();
		try {
			MontaReciboPublicacaoResult resp = getPort().montaReciboPublicacao(user, anuncianteIdentificador,
					cadernoIdentificador, retrancaCodigo, tipomaterialIdentificador, sequencial, hashMD5Sintese);
			JsonNode jsonNode = convertElementParaJsonNode(resp.getAny());
			String json = converterNodeJsonParaStringJson(jsonNode, MontaReciboPublicacaoDto.NOME_NODE_JSON);
			reciboPublicDto = getObjectMapper().readValue(json, MontaReciboPublicacaoDto.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
			if (!ENCODING_DEFAULT_XML.equals(ENCODING_UTF_8)) {
				ENCODING_DEFAULT_XML = ENCODING_UTF_8;
				montarReciboPublicacao(user, anuncianteIdentificador, cadernoIdentificador, retrancaCodigo,
						tipomaterialIdentificador, sequencial, hashMD5Sintese);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reciboPublicDto;
	}

	public EnviaPublicacaoDto enviarPublicacao(AuthHeader userName, String anuncianteIdentificador,
			String cadernoIdentificador, String retrancaCodigo, String tipomaterialIdentificador, String sequencial,
			String sintese, String recibo, String reciboHash) {
		EnviaPublicacaoDto enviaPublicDto = new EnviaPublicacaoDto();
		try {
			EnviaPublicacaoResult resp = getPort().enviaPublicacao(userName, anuncianteIdentificador,
					cadernoIdentificador, retrancaCodigo, tipomaterialIdentificador, sequencial, sintese, recibo,
					reciboHash);
			JsonNode jsonNode = convertElementParaJsonNode(resp.getAny());
			String json = converterNodeJsonParaStringJson(jsonNode, EnviaPublicacaoDto.NOME_NODE_JSON);
			enviaPublicDto = getObjectMapper().readValue(json, EnviaPublicacaoDto.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
			if (!ENCODING_DEFAULT_XML.equals(ENCODING_UTF_8)) {
				ENCODING_DEFAULT_XML = ENCODING_UTF_8;
				enviarPublicacao(userName, anuncianteIdentificador, cadernoIdentificador, retrancaCodigo,
						tipomaterialIdentificador, sequencial, sintese, recibo, reciboHash);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return enviaPublicDto;
	}

}
