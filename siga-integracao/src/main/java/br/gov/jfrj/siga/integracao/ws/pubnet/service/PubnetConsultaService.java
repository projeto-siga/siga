package br.gov.jfrj.siga.integracao.ws.pubnet.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;

import br.gov.jfrj.siga.integracao.ws.pubnet.dto.EnviaPublicacaoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.JustificativasCancelamentoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.MaterialEnviadoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.MensagemErroRetornoPubnetDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.PermissaoPublicanteDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.TokenDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.AuthHeader;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.ConsultaMaterialEnviadoResponse.ConsultaMaterialEnviadoResult;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.ConsultaPermissoesPublicanteResponse.ConsultaPermissoesPublicanteResult;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.GeraTokenResponse.GeraTokenResult;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.ListaJustificativasCancelamentoResponse.ListaJustificativasCancelamentoResult;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.Pubnet;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.PubnetSoap;

public class PubnetConsultaService {

	private Pubnet pubnet;
	private PubnetSoap port;

	protected static String ENCODING_UTF_8 = "UTF-8";
	protected static String ENCODING_US_ASCII = "US-ASCII";
	protected static String ENCODING_DEFAULT_XML = ENCODING_US_ASCII;

	public PubnetConsultaService() {
		pubnet = new Pubnet();
		port = pubnet.getPubnetSoap();
	}

	public List<MaterialEnviadoDto> consultarMaterialEnviado(AuthHeader user, String dtInicio, String dtFim) {
		List<MaterialEnviadoDto> materialEnviadoDtos = new ArrayList<MaterialEnviadoDto>();
		try {
			ConsultaMaterialEnviadoResult resp = port.consultaMaterialEnviado(user, dtInicio, dtFim);
			JsonNode jsonNode = convertElementParaJsonNode(resp.getAny());
			String json = converterNodeJsonParaStringJson(jsonNode, MaterialEnviadoDto.NOME_NODE_JSON);
			materialEnviadoDtos = Arrays.asList(getObjectMapper().readValue(json, MaterialEnviadoDto[].class));
		} catch (JsonParseException e) {
			e.printStackTrace();
			if (!ENCODING_DEFAULT_XML.equals(ENCODING_UTF_8)) {
				ENCODING_DEFAULT_XML = ENCODING_UTF_8;
				consultarMaterialEnviado(user, dtInicio, dtFim);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return materialEnviadoDtos;
	}

	public List<PermissaoPublicanteDto> consultarPermissaoPublicante(AuthHeader user) {
		List<PermissaoPublicanteDto> permissaoPublicanteDtoList = new ArrayList<PermissaoPublicanteDto>();
		try {
			ConsultaPermissoesPublicanteResult resp = port.consultaPermissoesPublicante(user);
			JsonNode jsonNode = convertElementParaJsonNode(resp.getAny());
			String json = converterNodeJsonParaStringJson(jsonNode, PermissaoPublicanteDto.NOME_NODE_JSON);
			permissaoPublicanteDtoList = Arrays.asList(getObjectMapper().readValue(json, PermissaoPublicanteDto[].class));
		} catch (JsonParseException e) {
			e.printStackTrace();
			if (!ENCODING_DEFAULT_XML.equals(ENCODING_UTF_8)) {
				ENCODING_DEFAULT_XML = ENCODING_UTF_8;
				consultarPermissaoPublicante(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return permissaoPublicanteDtoList;
	}

	public List<JustificativasCancelamentoDto> listarJustificativasCancelamento(AuthHeader user) {
		List<JustificativasCancelamentoDto> permissaoPublicanteDtoList = new ArrayList<JustificativasCancelamentoDto>();
		try {
			ListaJustificativasCancelamentoResult resp = port.listaJustificativasCancelamento(user);
			JsonNode jsonNode = convertElementParaJsonNode(resp.getAny());
			String json = converterNodeJsonParaStringJson(jsonNode, JustificativasCancelamentoDto.NOME_NODE_JSON);
			permissaoPublicanteDtoList = Arrays
					.asList(getObjectMapper().readValue(json, JustificativasCancelamentoDto[].class));
		} catch (JsonParseException e) {
			e.printStackTrace();
			if (!ENCODING_DEFAULT_XML.equals(ENCODING_UTF_8)) {
				ENCODING_DEFAULT_XML = ENCODING_UTF_8;
				listarJustificativasCancelamento(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return permissaoPublicanteDtoList;
	}

	public TokenDto gerarToken(String userName, String documento, String email) {
		TokenDto tokenDto = new TokenDto();
		try {
			GeraTokenResult resp = port.geraToken(userName, documento, email);
			JsonNode jsonNode = convertElementParaJsonNode(resp.getAny());
			String json = converterNodeJsonParaStringJson(jsonNode, TokenDto.NOME_NODE_JSON);
			tokenDto = getObjectMapper().readValue(json, TokenDto.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
			if (!ENCODING_DEFAULT_XML.equals(ENCODING_UTF_8)) {
				ENCODING_DEFAULT_XML = ENCODING_UTF_8;
				gerarToken(userName, documento, email);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tokenDto;
	}

	private StringWriter converterDocumentParaXmlString(Document document) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);

		transformer.setOutputProperty(OutputKeys.ENCODING, ENCODING_DEFAULT_XML);
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.transform(new DOMSource(document), result);
		// TODO Remover
		// transformer.transform(new DOMSource(document), new StreamResult(System.out));
		return writer;
	}

	private JsonNode converterXMLParaNodeJson(String xmlResponse) throws IOException {
		String xml = xmlResponse;
		XmlMapper xmlMapper = new XmlMapper();
		JsonNode node = xmlMapper.readTree(xml.getBytes());
		return node;
	}

	protected String converterNodeJsonParaStringJson(JsonNode node, String nomeNodeJson) throws Exception {
		ObjectMapper jsonMapper = new ObjectMapper();
		String json = "";
		JsonNode nodeLista = node.get("lista");
		if (nodeLista != null) {
			json = jsonMapper.writeValueAsString(nodeLista.get(nomeNodeJson));
		} else {
			nodeLista = node.get("erro");
			if(nodeLista != null) {
				json = jsonMapper.writeValueAsString(nodeLista.get("informacoes"));
				MensagemErroRetornoPubnetDto err = new ObjectMapper().readValue(json, MensagemErroRetornoPubnetDto.class);
				System.out.println(json);
				throw new Exception(err.getCodRetorno() + " - " + err.getDescrRetorno());
			} else {
				nodeLista = node.get("Recibo");
				if(nodeLista != null) {
					json = jsonMapper.writeValueAsString(nodeLista.get("Recibo"));
					EnviaPublicacaoDto recibo = new ObjectMapper().readValue(json, EnviaPublicacaoDto.class);
					if (recibo != null && !recibo.getCodRetorno().equals("0000")) {
						throw new Exception(recibo.getCodRetorno() + " - " + recibo.getDescricao());
					}
				}
			}
		}
		// TODO Remover
		System.out.println(json);
		if (json == null || "".equals(json) || "null".equals(json))
			throw new Exception("Json nulo ou vazio");
		return json;
	}

	protected JsonNode convertElementParaJsonNode(Object resp) throws TransformerException, IOException {
		ElementNSImpl elementNSImpl = (ElementNSImpl) resp;
		StringWriter writer = converterDocumentParaXmlString(elementNSImpl.getOwnerDocument());
		JsonNode jsonNode = converterXMLParaNodeJson(writer.toString());
		return jsonNode;
	}

	protected ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, Boolean.TRUE);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	public Pubnet getPubnet() {
		return pubnet;
	}

	public PubnetSoap getPort() {
		return port;
	}
}