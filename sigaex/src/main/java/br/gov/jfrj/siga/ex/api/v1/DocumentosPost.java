package br.gov.jfrj.siga.ex.api.v1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.SwaggerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosPost;
import br.gov.jfrj.siga.ex.bl.AcessoConsulta;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocumentosPost implements IDocumentosPost {
	public DocumentosPost () {
		SwaggerUtils.setUploadHandler(new ArquivoUploadHandler());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(DocumentosPostRequest req,
			DocumentosPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			ApiContext.assertAcesso("");
			
			SigaObjects so = ApiContext.getSigaObjects();

			final Ex ex = Ex.getInstance();
			final ExBL exBL = ex.getBL();

			DpPessoa cadastrante = null;
    		ExModelo modelo = null;
    		ExClassificacao classificacao = null;
    		CpOrgao destinatarioOrgaoExterno = null;    		
    		cadastrante = so.getCadastrante();
    		ExDocumento doc = new ExDocumento();
			if (req.descricaodocumento != null)
				doc.setDescrDocumento(req.descricaodocumento);
			
			if (req.eletronico != null) {
				doc.setEletronico(req.eletronico);
			} else {
				doc.setEletronico(true);
			}
			doc.setExModelo(null);
			if (req.modelo != null) {
				if (StringUtils.isNumeric(req.modelo)) {
		    		modelo = dao().consultar(Long.valueOf(req.modelo),
		    				ExModelo.class, false);
					if (modelo != null) {
						doc.setExModelo(modelo.getModeloAtual());
						doc.setExFormaDocumento(modelo.getExFormaDocumento());
					} else {
		    			throw new AplicacaoException("Não foi possível encontrar um modelo com o id informado.");
					}
				} else {
		    		modelo = (dao()).consultarExModelo(null, req.modelo);
					if (modelo != null) {
						doc.setExModelo(modelo);
						doc.setExFormaDocumento(modelo.getExFormaDocumento());
					} else {
		    			throw new AplicacaoException("Não foi possível encontrar um modelo com o nome informado.");
					}
					
				}
			} else {
    			throw new AplicacaoException("O modelo não foi informado.");
			}
    		
    		if (req.classificacao != null) {
				classificacao = dao().consultarExClassificacao(req.classificacao);
				if (classificacao == null) 
					throw new AplicacaoException("Não foi possível encontrar a classificação informada.");
			} else {
				if (modelo.isClassificacaoAutomatica()) 
					classificacao = modelo.getExClassificacao();
				if (classificacao == null) 
					throw new AplicacaoException("Modelo sem classificação automática, favor informar.");
			}
			ExClassificacao cAtual = classificacao.getAtual();
			doc.setExClassificacao(cAtual);
			
			if (req.descricaotipodoc != null) {
				doc.setExTipoDocumento(dao().consultarExTipoDocumento(req.descricaotipodoc));
			} else {
				for (ExTipoDocumento tp : modelo.getExFormaDocumento()
						.getExTipoDocumentoSet()) {
					doc.setExTipoDocumento(dao().consultar(
							tp.getId(), ExTipoDocumento.class, false));
					break;
				}
				if (doc.getExTipoDocumento() == null) 
					doc.setExTipoDocumento(dao().consultar(
							ExTipoDocumento.TIPO_DOCUMENTO_INTERNO, ExTipoDocumento.class, false));
			}

			doc.setOrgaoExterno(null);

			if (doc.getCadastrante() == null) {
				doc.setCadastrante(so.getCadastrante());
				doc.setLotaCadastrante(so.getLotaTitular());
			}

			if (doc.getLotaCadastrante() == null) {
				doc.setLotaCadastrante(doc.getCadastrante().getLotacao());
			}
    		
    		if (req.subscritor != null) {
	    		doc.setSubscritor(dao().getPessoaFromSigla(req.subscritor));
				doc.setLotaSubscritor(doc.getSubscritor().getLotacao());
			} else {
				if(SigaMessages.isSigaSP() && AcessoConsulta.ehPublicoExterno(so.getTitular()) && !doc.isCapturado()) {
					doc.setSubscritor(so.getTitular());
					doc.setLotaSubscritor(so.getTitular().getLotacao());
				} else {
					doc.setSubscritor(null);
				}
			}

			if (req.titular != null) {
				doc.setTitular(dao().getPessoaFromSigla(req.titular));
				doc.setLotaTitular(doc.getTitular().getLotacao());
			} else {
				doc.setTitular(doc.getSubscritor());
				doc.setLotaTitular(doc.getLotaSubscritor());
			}

			if (req.pessoadestinatario != null) {
				doc.setDestinatario(dao().getPessoaFromSigla(req.pessoadestinatario));
				doc.setLotaDestinatario(doc.getDestinatario().getLotacao());
				doc.setOrgaoExternoDestinatario(null);
			} else {
				doc.setDestinatario(null);
				if (req.lotadestinatario != null) {
					doc.setLotaDestinatario(dao().getLotacaoFromSigla(
							req.lotadestinatario));
					doc.setOrgaoExternoDestinatario(null);
				} else {
					doc.setLotaDestinatario(null);
					if (req.orgaoexternodestinatario != null) {
						destinatarioOrgaoExterno = dao().getOrgaoFromSiglaExata(req.pessoadestinatario);
						if(destinatarioOrgaoExterno != null) {
							doc.setOrgaoExternoDestinatario(destinatarioOrgaoExterno);
							doc.setNmOrgaoExterno(req.destinatariocampoextra);
						}
					} else {
						doc.setOrgaoExternoDestinatario(null);
					}
				}
			}			
			doc.setDtRegDoc(dao().dt());

			if (req.siglamobilpai != null) {
				ExMobilDaoFiltro flt = new ExMobilDaoFiltro();
				flt.setSigla(req.siglamobilpai);
				ExMobil mobPai = (ExMobil) dao().consultarPorSigla(flt);
    			if (mobPai != null) {
    	    		ExDocumento docPai = mobPai.getExDocumento();
    				if(docPai.getExMobilPai() != null)
    					throw new AplicacaoException("Não foi possível criar o documento pois o documento pai (" 
    							+ docPai.getSigla() + ") já é documento filho.");
    				
    				if(docPai.isPendenteDeAssinatura())
    					throw new AplicacaoException("Não foi possível criar o documento pois o documento pai (" 
    							+ docPai.getSigla() + ") ainda não foi assinado.");
    				
    				doc.setExMobilPai(mobPai);
    			}
			} else {
				doc.setExMobilPai(null);
			}
			
			if (req.nivelacesso != null) {
				doc.setExNivelAcesso(dao()
						.consultarExNidelAcesso(
								DocumentosPost.UTF8toISO(req.nivelacesso)));
			} else {
				final ExNivelAcesso nivelDefault =  ExNivelAcesso
						.getNivelAcessoDefault(doc.getExTipoDocumento(), doc.getExFormaDocumento(),
								doc.getExModelo(), doc.getExClassificacao(), 
								so.getTitular(), so.getLotaTitular());
				
				if (nivelDefault != null) {
					doc.setExNivelAcesso(dao().consultar(nivelDefault, 
							ExNivelAcesso.class, false));
				} else {
					doc.setExNivelAcesso(dao().consultar(ExNivelAcesso.ID_PUBLICO, 
							ExNivelAcesso.class, false));
				}
			}

			if (doc.getExModelo() != null
					&& doc.getExModelo().getExNivelAcesso() != null) {
				doc.setExNivelAcesso(doc.getExModelo().getExNivelAcesso());
			}
			
			String camposModelo = "";
			String conteudo = DocumentosPost.UTF8toISO(req.entrevista);
			Map <String, String> conteudoMap;
			
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				if (req.entrevista != null) {
					if (req.entrevista.startsWith("{")) {
						ObjectMapper objectMapper = new ObjectMapper();
						conteudoMap = objectMapper.readValue(conteudo, Map.class);
						for (String key : conteudoMap.keySet()){
							camposModelo = camposModelo + key + "=" 
								+ URLEncoder.encode(conteudoMap.get(key), "iso-8859-1") + "&";
						}
					} else {
					    String[] keyAndValues = conteudo.split("&");
					    for (String keyAndValue : keyAndValues) {
					        int idx = keyAndValue.indexOf("=");
					        String key = keyAndValue.substring(0, idx);
							String value = URLDecoder.decode(keyAndValue.substring(idx + 1), "UTF-8");
							camposModelo = camposModelo + key + "=" +
					        		URLEncoder.encode(value, "iso-8859-1") + "&";
					    }
					}
				} else {
	    			throw new AplicacaoException("O parâmetro entrevista não foi informado.");
				}
				if (camposModelo.length() > 0)
					camposModelo = camposModelo.substring(0, camposModelo.length() - 1);
				baos.write(camposModelo.getBytes());
				doc.setConteudoTpDoc("application/zip");
				doc.setConteudoBlobForm(baos.toByteArray());
			}
			
			if ((doc.getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_CAPTURADO 
					|| doc.getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_CAPTURADO)
					&& req.content == null)
				throw new AplicacaoException(
						"Documento capturado não pode ser gravado sem que seja informado o arquivo PDF.");

			if (!ex.getConf().podePorConfiguracao(so.getTitular(),
					so.getLotaTitular(),
					doc.getExTipoDocumento(),
					doc.getExFormaDocumento(),
					doc.getExModelo(),
					doc.getExClassificacaoAtual(),
					doc.getExNivelAcessoAtual(),
					CpTipoConfiguracao.TIPO_CONFIG_CRIAR)) {

				if (!ex.getConf().podePorConfiguracao(so.getTitular(),
						so.getLotaTitular(), null, null, null,
						doc.getExClassificacao(), null,
						CpTipoConfiguracao.TIPO_CONFIG_CRIAR)) {
					throw new AplicacaoException(
							"Usuário não possui permissão de criar documento da classificação "
									+ doc
											.getExClassificacao()
											.getCodificacao());
				}

				throw new AplicacaoException("Operação não permitida");
			}
			doc.setOrgaoUsuario(so.getLotaTitular().getOrgaoUsuario());

			// Insere PDF de documento capturado
			//
			if (req.content != null) {
				Integer numBytes = null;
				try {
	                final byte[] baArquivo = (byte[]) req.content;
	                if (baArquivo == null) {
						throw new AplicacaoException(
								"Arquivo vazio não pode ser anexado.");
					}
					numBytes = baArquivo.length;
					if (numBytes > 10 * 1024 * 1024) {
						throw new AplicacaoException(
								"Não é permitida a anexação de arquivos com mais de 10MB.");
					}
					doc.setConteudoBlobPdf(baArquivo);
					doc.setConteudoBlobHtml(null);
				} catch (IOException e) {
					throw new AplicacaoException("Falha ao manipular aquivo",
							1, e);
				}

				Integer numPaginas = doc.getContarNumeroDePaginas();
				if (numPaginas == null || doc.getArquivoComStamp() == null) {
					throw new AplicacaoException(
							MessageFormat
									.format("O arquivo {0} está corrompido ou protegido por senha. Favor gera-lo novamente antes de anexar.",
											req.content));
				}
			}

    		ExMobil mob = new ExMobil();
			mob.setExTipoMobil(dao().consultar(ExTipoMobil.TIPO_MOBIL_GERAL,
					ExTipoMobil.class, false));
			mob.setNumSequencia(1);
			mob.setExMovimentacaoSet(new TreeSet<ExMovimentacao>());
			mob.setExDocumento(doc);
			doc.setExMobilSet(new TreeSet<ExMobil>());
			doc.getExMobilSet().add(mob);
			
			exBL.gravar(cadastrante, so.getTitular(), so.getLotaTitular(), doc);
			
			if(req.titular != null && doc.getTitular() != doc.getSubscritor()) {
				exBL.geraMovimentacaoSubstituicao(doc, so.getCadastrante());
			}
			
			if(doc.getExMobilPai() != null && Ex.getInstance().getComp().podeRestrigirAcesso(cadastrante, cadastrante.getLotacao(), doc.getExMobilPai())) {
				exBL.copiarRestringir(doc.getMobilGeral(), doc.getExMobilPai().getDoc().getMobilGeral(), cadastrante, so.getTitular(), doc.getData());
			}

			if (!doc.isFinalizado()
					&& (doc.getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_CAPTURADO || doc.getExTipoDocumento()
							.getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_CAPTURADO) && (exBL.getConf().podePorConfiguracao(so.getTitular(), so.getLotaTitular(), CpTipoConfiguracao.TIPO_CONFIG_FINALIZAR_AUTOMATICAMENTE_CAPTURADOS)))
				exBL.finalizar(cadastrante, so.getLotaTitular(), doc);

			try {
				exBL.incluirCosignatariosAutomaticamente(cadastrante,
						so.getLotaTitular(), doc);
			} catch (Exception e) {
				throw new AplicacaoException(
						"Erro ao tentar incluir os cosignatários deste documento",
						0, e);
			}

    		resp.sigladoc = doc.getSigla();
    		
		} catch (AplicacaoException | SwaggerException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}
	
	protected  ExDao dao() {
		return ExDao.getInstance();
	}
	
	@Override
	public String getContext() {
		return "Incluir documento";
	}

	/**
	 * Converte string em UTF-8 para ISO-8859-1
	 * 
	 * @param str String a ser convertida.
	 * @return String convertida.
	 */
	static String UTF8toISO(String str) {
		Charset utf8charset = Charset.forName("UTF-8");
		Charset iso88591charset = Charset.forName("ISO-8859-1");
		ByteBuffer inputBuffer = ByteBuffer.wrap(str.getBytes());
		CharBuffer data = utf8charset.decode(inputBuffer);
		ByteBuffer outputBuffer = iso88591charset.encode(data);
		byte[] outputData = outputBuffer.array();
		return new String(outputData);
	}

	/**
	 * Verifica se uma string é composta somente por caracteres numéricos.
	 * 
	 * @param str String a verificar se é numérico
	 * @return
	 *         <ul>
	 *         <li><b>true</b> se for totalmente numérico</li>
	 *         <li><b>false</b> se houver qualquer caracter não numérico.</li>
	 *         </ul>
	 */
	static boolean isNumerico(String str) {
		return str.matches("^\\d+$");
	}
}