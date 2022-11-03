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

import org.apache.xerces.dom.ElementNSImpl;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.EnviaPublicacaoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.JustificativasCancelamentoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.MaterialEnviadoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.MensagemErroRetornoPubnetDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.PermissaoPublicanteDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.ProximoSequencialDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.TokenDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.AuthHeader;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.ConsultaMaterialEnviadoResponse.ConsultaMaterialEnviadoResult;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.ConsultaPermissoesPublicanteResponse.ConsultaPermissoesPublicanteResult;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.GeraTokenResponse.GeraTokenResult;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.ListaJustificativasCancelamentoResponse.ListaJustificativasCancelamentoResult;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.PegaProximoSequencialResponse.PegaProximoSequencialResult;
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

	public List<MaterialEnviadoDto> consultarMaterialEnviado(AuthHeader user, String dtInicio, String dtFim) throws Exception {
		List<MaterialEnviadoDto> materialEnviadoDtos = new ArrayList<MaterialEnviadoDto>();
		try {
			ConsultaMaterialEnviadoResult resp = port.consultaMaterialEnviado(user, dtInicio, dtFim);
			JSONObject jsonNode = convertElementParaJsonNode(resp.getAny());
			String json = converterNodeJsonParaStringJson(jsonNode, MaterialEnviadoDto.NOME_NODE_JSON);
			materialEnviadoDtos = Arrays.asList(getObjectMapper().readValue(json, MaterialEnviadoDto[].class));
		} catch (JsonParseException e) {
			e.printStackTrace();
			if (!ENCODING_DEFAULT_XML.equals(ENCODING_UTF_8)) {
				ENCODING_DEFAULT_XML = ENCODING_UTF_8;
				consultarMaterialEnviado(user, dtInicio, dtFim);
			}
		} catch (Exception e) {
			throw new AplicacaoException(e.getMessage());
		}
		return materialEnviadoDtos;
	}

	public List<PermissaoPublicanteDto> consultarPermissaoPublicante(AuthHeader user) throws Exception {
		List<PermissaoPublicanteDto> permissaoPublicanteDtoList = new ArrayList<PermissaoPublicanteDto>();
		try {
			ConsultaPermissoesPublicanteResult resp = port.consultaPermissoesPublicante(user);
			JSONObject jsonNode = convertElementParaJsonNode(resp.getAny());
			String json = converterNodeJsonParaStringJson(jsonNode, PermissaoPublicanteDto.NOME_NODE_JSON);
			System.out.println(json);
			permissaoPublicanteDtoList = Arrays.asList(getObjectMapper().readValue(json, PermissaoPublicanteDto[].class));
		} catch (JsonParseException e) {
			e.printStackTrace();
			if (!ENCODING_DEFAULT_XML.equals(ENCODING_UTF_8)) {
				ENCODING_DEFAULT_XML = ENCODING_UTF_8;
				consultarPermissaoPublicante(user);
			}
		} catch (Exception e) {
			throw new AplicacaoException(e.getMessage());
		}
		return permissaoPublicanteDtoList;
	}

	public List<JustificativasCancelamentoDto> listarJustificativasCancelamento(AuthHeader user) throws Exception {
		List<JustificativasCancelamentoDto> permissaoPublicanteDtoList = new ArrayList<JustificativasCancelamentoDto>();
		try {
			ListaJustificativasCancelamentoResult resp = port.listaJustificativasCancelamento(user);
			JSONObject jsonNode = convertElementParaJsonNode(resp.getAny());
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
			throw new AplicacaoException(e.getMessage());
		}
		return permissaoPublicanteDtoList;
	}
	
	public ProximoSequencialDto obterProximoSequencialPublicacao(AuthHeader user, String retrancaCodigo) throws Exception {
		ProximoSequencialDto proximoSeqDto = new ProximoSequencialDto();
		try {
			PegaProximoSequencialResult resp = port.pegaProximoSequencial(user, retrancaCodigo);
			JSONObject jsonNode = convertElementParaJsonNode(resp.getAny());
			String json = converterNodeJsonParaStringJson(jsonNode, ProximoSequencialDto.NOME_NODE_JSON);
			proximoSeqDto = getObjectMapper().readValue(json, ProximoSequencialDto.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
			if (!ENCODING_DEFAULT_XML.equals(ENCODING_UTF_8)) {
				ENCODING_DEFAULT_XML = ENCODING_UTF_8;
				listarJustificativasCancelamento(user);
			}
		} catch (Exception e) {
			throw new AplicacaoException(e.getMessage());
		}
		return proximoSeqDto;
	}

	public TokenDto gerarToken(String userName, String documento, String email) throws Exception {
		TokenDto tokenDto = new TokenDto();
		try {
			GeraTokenResult resp = port.geraToken(userName, documento, email);
			JSONObject jsonNode = convertElementParaJsonNode(resp.getAny());
			String json = converterNodeJsonParaStringJson(jsonNode, TokenDto.NOME_NODE_JSON);
			tokenDto = getObjectMapper().readValue(json, TokenDto.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
			if (!ENCODING_DEFAULT_XML.equals(ENCODING_UTF_8)) {
				ENCODING_DEFAULT_XML = ENCODING_UTF_8;
				gerarToken(userName, documento, email);
			}
		} catch (Exception e) {
			throw new AplicacaoException(e.getMessage());
		}
		return tokenDto;
	}

	private StringWriter converterDocumentParaXmlString(Document document) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);

		transformer.setOutputProperty(OutputKeys.ENCODING, ENCODING_UTF_8);
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.transform(new DOMSource(document), result);
		return writer;
	}

	private JSONObject converterXMLParaNodeJson(String xmlResponse) throws IOException {
		String xml = xmlResponse;
		JSONObject json = XML.toJSONObject(xml);
		return json;
	}

	protected String converterNodeJsonParaStringJson(JSONObject node, String nomeNodeJson) throws Exception {
		String json = "";
		JSONObject nodeDiffgr = (JSONObject) node.get("diffgr:diffgram");
		if (nodeDiffgr != null) {
			JSONObject nodeLista = getValueJSONObject(nodeDiffgr, "lista");
			if (nodeLista != null) {
				json = JSONObject.valueToString(nodeLista.get(nomeNodeJson));
				System.out.println(json);
			} else {
				JSONObject nodeErr = getValueJSONObject(nodeDiffgr, "erro");
				if(nodeErr != null) {
					json = JSONObject.valueToString(getValueJSONObject(nodeErr, "informacoes"));
					MensagemErroRetornoPubnetDto err = getObjectMapper().readValue(json, MensagemErroRetornoPubnetDto.class);
					System.out.println(json);
					throw new Exception("Erro ao acessar webservice DOE código retorno: " + err.getCodRetorno()
											+ "\nDescrição Erro: " + err.getDescrRetorno());
				} else {
					JSONObject nodeRecibo = getValueJSONObject(nodeDiffgr, "Recibo");
					if(nodeRecibo != null) {
						json = JSONObject.valueToString(getValueJSONObject(nodeRecibo, "Recibo"));
						EnviaPublicacaoDto recibo = getObjectMapper().readValue(json, EnviaPublicacaoDto.class);
						if (recibo != null && !recibo.getCodRetorno().equals("0000")) {
							throw new Exception(recibo.getCodRetorno() + " - " + recibo.getDescricao());
						}
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
	
	private JSONObject getValueJSONObject(JSONObject object,String nomeNode) {
		return !object.isNull(nomeNode) ? (JSONObject) object.get(nomeNode) : null;
	}

	protected JSONObject convertElementParaJsonNode(Object resp) throws TransformerException, IOException {
		ElementNSImpl elementNSImpl = (ElementNSImpl) resp;
		StringWriter writer = converterDocumentParaXmlString(elementNSImpl.getOwnerDocument());
		
		JSONObject jsonNode = converterXMLParaNodeJson(writer.toString());
		System.out.println(jsonNode.toString());
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
