package br.gov.jfrj.siga.integracao.ws.pubnet.service;

import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.EnviaPublicacaoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.MontaReciboPublicacaoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.AuthHeader;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.EnviaPublicacaoResponse.EnviaPublicacaoResult;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.MontaReciboPublicacaoResponse.MontaReciboPublicacaoResult;
import br.gov.jfrj.siga.integracao.ws.pubnet.utils.CertificadoDigitalUtils;

public class PubnetEnvioService extends PubnetConsultaService {

	public PubnetEnvioService() {
		super();
	}

	public MontaReciboPublicacaoDto montarReciboPublicacao(AuthHeader user, String anuncianteIdentificador,
			String cadernoIdentificador, String retrancaCodigo, String tipomaterialIdentificador, String sequencial,
			String textoPublicacao) throws Exception {
		String hashMD5Sintese = CertificadoDigitalUtils.CriarMD5DevolverHex(textoPublicacao);
		return this.montarReciboPublicacaoHash(user, anuncianteIdentificador, cadernoIdentificador, retrancaCodigo,
				tipomaterialIdentificador, sequencial, hashMD5Sintese);
	}

	private MontaReciboPublicacaoDto montarReciboPublicacaoHash(AuthHeader user, String anuncianteIdentificador,
			String cadernoIdentificador, String retrancaCodigo, String tipomaterialIdentificador, String sequencial,
			String hashMD5Sintese) throws Exception {
		MontaReciboPublicacaoDto reciboPublicDto = new MontaReciboPublicacaoDto();
		try {
			MontaReciboPublicacaoResult resp = getPort().montaReciboPublicacao(user, anuncianteIdentificador,
					cadernoIdentificador, retrancaCodigo, tipomaterialIdentificador, sequencial, hashMD5Sintese);
			JSONObject jsonNode = convertElementParaJsonNode(resp.getAny());
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
			throw new AplicacaoException(e.getMessage());
		}
		return reciboPublicDto;
	}

	public EnviaPublicacaoDto enviarPublicacao(AuthHeader userName, String anuncianteIdentificador,
			String cadernoIdentificador, String retrancaCodigo, String tipomaterialIdentificador, String sequencial,
			String sintese, String recibo, String reciboHash)  throws Exception {
		EnviaPublicacaoDto enviaPublicDto = new EnviaPublicacaoDto();
		try {
			if(recibo == null || recibo.equals("")) 
				throw new AplicacaoException("Erro Pubnet: Necessário assinar o recibo para publicação");
			else {
				EnviaPublicacaoResult resp = getPort().enviaPublicacao(userName, anuncianteIdentificador,
						cadernoIdentificador, retrancaCodigo, tipomaterialIdentificador, sequencial, sintese, converterParaUTF8(recibo),
						reciboHash);
				JSONObject jsonNode = convertElementParaJsonNode(resp.getAny());
				String json = converterNodeJsonParaStringJson(jsonNode, EnviaPublicacaoDto.NOME_NODE_JSON);
				enviaPublicDto = getObjectMapper().readValue(json, EnviaPublicacaoDto.class);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
			if (!ENCODING_DEFAULT_XML.equals(ENCODING_UTF_8)) {
				ENCODING_DEFAULT_XML = ENCODING_UTF_8;
				enviarPublicacao(userName, anuncianteIdentificador, cadernoIdentificador, retrancaCodigo,
						tipomaterialIdentificador, sequencial, sintese, recibo, reciboHash);
			}
		} catch (Exception e) {
			throw new AplicacaoException(e.getMessage());
		}
		return enviaPublicDto;
	}
	
	private static String converterParaUTF8(String str) {
		String strConvertido = str != null && !str.equals("") ? new String(str.getBytes(), StandardCharsets.US_ASCII) : str;
		return strConvertido;
	}

}
