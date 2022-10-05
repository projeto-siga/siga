package br.gov.jfrj.siga.integracao.ws.pubnet.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.CancelaMaterialDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.MontaReciboPublicacaoCancelamentoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.AuthHeader;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.CancelarMaterialResponse.CancelarMaterialResult;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.MontaReciboPublicacaoCancelamentoResponse.MontaReciboPublicacaoCancelamentoResult;

public class PubnetCancelamentoService extends PubnetConsultaService {

	public PubnetCancelamentoService() {
		super();
	}

	public MontaReciboPublicacaoCancelamentoDto montarReciboPublicacaoCancelamento(AuthHeader user,
			String nomeArquivo) throws Exception {
		MontaReciboPublicacaoCancelamentoDto reciboPublicCancelDto = new MontaReciboPublicacaoCancelamentoDto();
		try {
			MontaReciboPublicacaoCancelamentoResult resp = getPort().montaReciboPublicacaoCancelamento(user,
					nomeArquivo);
			JsonNode jsonNode = convertElementParaJsonNode(resp.getAny());
			String json = converterNodeJsonParaStringJson(jsonNode, MontaReciboPublicacaoCancelamentoDto.NOME_NODE_JSON);
			reciboPublicCancelDto = getObjectMapper().readValue(json, MontaReciboPublicacaoCancelamentoDto.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
			if (!ENCODING_DEFAULT_XML.equals(ENCODING_UTF_8)) {
				ENCODING_DEFAULT_XML = ENCODING_UTF_8;
				montarReciboPublicacaoCancelamento(user, nomeArquivo);
			}
		} catch (Exception e) {
			throw new AplicacaoException(e.getMessage());
		}
		return reciboPublicCancelDto;
	}

	public CancelaMaterialDto cancelarMaterial(AuthHeader user, String nomeArquivo,
			String justificativaIdentificador, String recibo, String reciboHash) throws Exception {
		CancelaMaterialDto reciboPublicCancelDto = new CancelaMaterialDto();
		try {
			CancelarMaterialResult resp = getPort().cancelarMaterial(user, nomeArquivo, justificativaIdentificador, recibo,
					reciboHash);
			JsonNode jsonNode = convertElementParaJsonNode(resp.getAny());
			String json = converterNodeJsonParaStringJson(jsonNode, CancelaMaterialDto.NOME_NODE_JSON);
			reciboPublicCancelDto = getObjectMapper().readValue(json, CancelaMaterialDto.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
			if (!ENCODING_DEFAULT_XML.equals(ENCODING_UTF_8)) {
				ENCODING_DEFAULT_XML = ENCODING_UTF_8;
				cancelarMaterial(user, nomeArquivo, justificativaIdentificador, recibo, reciboHash);
			}
		} catch (Exception e) {
			throw new AplicacaoException(e.getMessage());
		}
		return reciboPublicCancelDto;
	}
}
