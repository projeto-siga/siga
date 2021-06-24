package br.gov.jfrj.siga.ex.api.v1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosPost;
import br.gov.jfrj.siga.ex.bl.AcessoConsulta;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.util.NivelDeAcessoUtil;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DocumentosPost implements IDocumentosPost {
	public DocumentosPost() {
		SwaggerUtils.setUploadHandler(new ArquivoUploadHandler());
	}

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		final Ex ex = Ex.getInstance();
		final ExBL exBL = ex.getBL();

		DpPessoa cadastrante = null;
		ExModelo modelo = null;
		ExClassificacao classificacao = null;
		CpOrgao destinatarioOrgaoExterno = null;

		ExDocumento doc;
		if (req.sigla != null && !req.sigla.trim().isEmpty()) {
			ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Salvar");
			if (!Ex.getInstance().getComp().podeEditar(ctx.getTitular(), ctx.getLotaTitular(), mob))
				throw new SwaggerException("Edição do documento " + mob.getSigla() + " não é permitida. ("
						+ ctx.getTitular().getSigla() + "/" + ctx.getLotaTitular().getSiglaCompleta() + ")", 403, null,
						req, resp, null);
			doc = mob.doc();
		} else {
			doc = new ExDocumento();

			if (req.siglamobilpai != null) {
				ExMobil mobPai = ctx.buscarEValidarMobil(req.siglamobilpai, req, resp, "Documento Pai");
				ExDocumento docPai = mobPai.getExDocumento();
				if (docPai.getExMobilPai() != null)
					throw new AplicacaoException("Não foi possível criar o documento pois o documento pai ("
							+ docPai.getSigla() + ") já é documento filho.");

				if (docPai.isPendenteDeAssinatura())
					throw new AplicacaoException("Não foi possível criar o documento pois o documento pai ("
							+ docPai.getSigla() + ") ainda não foi assinado.");

				doc.setExMobilPai(mobPai);
			}

			if (req.siglamobilfilho != null) {
				ExMobil mobFilho = ctx.buscarEValidarMobil(req.siglamobilfilho, req, resp, "Documento Filho");
				doc.setExMobilAutuado(mobFilho);
			}

			ExMobil mob = new ExMobil();
			mob.setExTipoMobil(dao().consultar(ExTipoMobil.TIPO_MOBIL_GERAL, ExTipoMobil.class, false));
			mob.setNumSequencia(1);
			mob.setExMovimentacaoSet(new TreeSet<ExMovimentacao>());
			mob.setExDocumento(doc);
			doc.setExMobilSet(new TreeSet<ExMobil>());
			doc.getExMobilSet().add(mob);
		}

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
				modelo = dao().consultar(Long.valueOf(req.modelo), ExModelo.class, false);
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
			ExTipoDocumento tipoDoc = dao().consultarExTipoDocumento(req.descricaotipodoc);
			if (tipoDoc != null) {
				doc.setExTipoDocumento(tipoDoc);
			} else {
				throw new AplicacaoException("Tipo de documento não existente.");
			}
		} else {
			for (ExTipoDocumento tp : modelo.getExFormaDocumento().getExTipoDocumentoSet()) {
				doc.setExTipoDocumento(dao().consultar(tp.getId(), ExTipoDocumento.class, false));
				break;
			}
			if (doc.getExTipoDocumento() == null)
				doc.setExTipoDocumento(
						dao().consultar(ExTipoDocumento.TIPO_DOCUMENTO_INTERNO, ExTipoDocumento.class, false));
		}

		doc.setOrgaoExterno(null);

		if (doc.getCadastrante() == null) {
			doc.setCadastrante(ctx.getCadastrante());
			doc.setLotaCadastrante(ctx.getLotaTitular());
		}

		if (doc.getLotaCadastrante() == null) {
			doc.setLotaCadastrante(doc.getCadastrante().getLotacao());
		}

		if (req.subscritor != null) {
			DpPessoa subscritor = dao().getPessoaFromSigla(req.subscritor);
			if (subscritor != null) {
				doc.setSubscritor(subscritor);
			} else {
				throw new AplicacaoException("Subscritor não encontrado.");
			}
			doc.setLotaSubscritor(doc.getSubscritor().getLotacao());
		} else {
			if (SigaMessages.isSigaSP() && AcessoConsulta.ehPublicoExterno(ctx.getTitular()) && !doc.isCapturado()) {
				doc.setSubscritor(ctx.getTitular());
				doc.setLotaSubscritor(ctx.getTitular().getLotacao());
			} else {
				doc.setSubscritor(null);
			}
		}

		if (req.titular != null) {
			DpPessoa titular = dao().getPessoaFromSigla(req.titular);
			if (titular != null) {
				doc.setTitular(titular);
			} else {
				throw new AplicacaoException("Titular não encontrado.");
			}
			doc.setLotaTitular(doc.getTitular().getLotacao());
		} else {
			doc.setTitular(doc.getSubscritor());
			doc.setLotaTitular(doc.getLotaSubscritor());
		}

		if (req.pessoadestinatario != null) {
			DpPessoa destinatario = dao().getPessoaFromSigla(req.pessoadestinatario);
			if (destinatario != null) {
				doc.setDestinatario(destinatario);
			} else {
				throw new AplicacaoException("Destinatário não encontrado.");
			}
			doc.setLotaDestinatario(doc.getDestinatario().getLotacao());
			doc.setOrgaoExternoDestinatario(null);
		} else {
			doc.setDestinatario(null);
			if (req.lotadestinatario != null) {
				DpLotacao lotaDestinatario = dao().getLotacaoFromSigla(req.lotadestinatario);
				if (lotaDestinatario != null) {
					doc.setLotaDestinatario(lotaDestinatario);
				} else {
					throw new AplicacaoException("Lotação destinatária não encontrado.");
				}
				doc.setOrgaoExternoDestinatario(null);
			} else {
				doc.setLotaDestinatario(null);
				if (req.orgaoexternodestinatario != null) {
					destinatarioOrgaoExterno = dao().getOrgaoFromSiglaExata(req.orgaoexternodestinatario);
					if (destinatarioOrgaoExterno != null) {
						doc.setOrgaoExternoDestinatario(destinatarioOrgaoExterno);
						doc.setNmOrgaoExterno(req.destinatariocampoextra);
					} else {
						throw new AplicacaoException("Órgão externo destinatário não encontrado.");
					}
				} else {
					doc.setOrgaoExternoDestinatario(null);
				}
			}
		}
		if (doc.getDtRegDoc() == null)
			doc.setDtRegDoc(dao().dt());

		if (req.nivelacesso != null) {
			ExNivelAcesso nivel;
			try {
				nivel = dao().consultarExNidelAcesso(req.nivelacesso);
			} catch (NoResultException e) {
				throw new AplicacaoException("Nível de acesso não encontrado.");
			}
			if (!isNivelAcessoValido(ctx.getTitular(), ctx.getTitular().getLotacao(), doc, nivel))
				throw new AplicacaoException("Nível de acesso não permitido.");
			doc.setExNivelAcesso(nivel);
		} else {
			if (doc.getExModelo() != null && doc.getExModelo().getExNivelAcesso() != null) {
				doc.setExNivelAcesso(doc.getExModelo().getExNivelAcesso());
			} else {

				final ExNivelAcesso nivelDefault = ExNivelAcesso.getNivelAcessoDefault(doc.getExTipoDocumento(),
						doc.getExFormaDocumento(), doc.getExModelo(), doc.getExClassificacao(), ctx.getTitular(),
						ctx.getLotaTitular());

				if (nivelDefault != null) {
					doc.setExNivelAcesso(dao().consultar(nivelDefault, ExNivelAcesso.class, false));
				} else {
					if (Boolean.valueOf(System.getProperty("siga.doc.acesso.limitado"))) {
						doc.setExNivelAcesso(
								dao().consultar(ExNivelAcesso.ID_LIMITADO_AO_ORGAO, ExNivelAcesso.class, false));
					} else {
						doc.setExNivelAcesso(dao().consultar(ExNivelAcesso.ID_PUBLICO, ExNivelAcesso.class, false));
					}

				}
			}
		}

		String camposModelo = "";
		String conteudo = req.entrevista;
		Map<String, String> conteudoMap;

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			if (req.entrevista != null) {
				if (req.entrevista.startsWith("{")) {
					ObjectMapper objectMapper = new ObjectMapper();
					conteudoMap = objectMapper.readValue(conteudo, Map.class);
					for (String key : conteudoMap.keySet()) {
						camposModelo = camposModelo + key + "=" + conteudoMap.get(key) + "&";
					}
				} else {
					String[] keyAndValues = conteudo.split("&");
					for (String keyAndValue : keyAndValues) {
						if (keyAndValue == null || keyAndValue.trim().isEmpty())
							continue;
						int idx = keyAndValue.indexOf("=");
						String key = keyAndValue.substring(0, idx);
						String value = keyAndValue.substring(idx + 1);
						value = URLDecoder.decode(value, StandardCharsets.UTF_8.name());
						camposModelo = camposModelo + key + "=" + URLEncoder.encode(value, "iso-8859-1") + "&";
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

		if (!ex.getConf().podePorConfiguracao(ctx.getTitular(), ctx.getLotaTitular(), doc.getExTipoDocumento(),
				doc.getExFormaDocumento(), doc.getExModelo(), doc.getExClassificacaoAtual(),
				doc.getExNivelAcessoAtual(), ExTipoDeConfiguracao.CRIAR)) {

			if (!ex.getConf().podePorConfiguracao(ctx.getTitular(), ctx.getLotaTitular(), null, null, null,
					doc.getExClassificacao(), null, ExTipoDeConfiguracao.CRIAR)) {
				throw new AplicacaoException("Usuário não possui permissão de criar documento da classificação "
						+ doc.getExClassificacao().getCodificacao());
			}

			throw new AplicacaoException("Operação não permitida");
		}
		doc.setOrgaoUsuario(ctx.getLotaTitular().getOrgaoUsuario());

		// Insere PDF de documento capturado
		//
		if (req.content != null) {
			Integer numBytes = null;
			try {
				final byte[] baArquivo = (byte[]) req.content;
				if (baArquivo == null) {
					throw new AplicacaoException("Arquivo vazio não pode ser anexado.");
				}
				numBytes = baArquivo.length;
				if (numBytes > 10 * 1024 * 1024) {
					throw new AplicacaoException("Não é permitida a anexação de arquivos com mais de 10MB.");
				}
				doc.setConteudoBlobPdf(baArquivo);
				doc.setConteudoBlobHtml(null);
			} catch (IOException e) {
				throw new AplicacaoException("Falha ao manipular aquivo", 1, e);
			}

			Integer numPaginas = doc.getContarNumeroDePaginas();
			if (numPaginas == null || doc.getArquivoComStamp() == null) {
				throw new AplicacaoException(MessageFormat.format(
						"O arquivo {0} está corrompido ou protegido por senha. Favor gera-lo novamente antes de anexar.",
						req.content));
			}
		}

		exBL.gravar(cadastrante, ctx.getTitular(), ctx.getLotaTitular(), doc);

		if (req.titular != null && doc.getTitular() != doc.getSubscritor()) {
			exBL.geraMovimentacaoSubstituicao(doc, ctx.getCadastrante());
		}

		if (doc.getExMobilPai() != null && Ex.getInstance().getComp().podeRestrigirAcesso(cadastrante,
				cadastrante.getLotacao(), doc.getExMobilPai())) {
			exBL.copiarRestringir(doc.getMobilGeral(), doc.getExMobilPai().getDoc().getMobilGeral(), cadastrante,
					ctx.getTitular(), doc.getData());
		}

		if (!doc.isFinalizado()
				&& (doc.getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_CAPTURADO
						|| doc.getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_CAPTURADO)
				&& (exBL.getConf().podePorConfiguracao(ctx.getTitular(), ctx.getLotaTitular(),
						ExTipoDeConfiguracao.FINALIZAR_AUTOMATICAMENTE_CAPTURADOS)))
			exBL.finalizar(cadastrante, ctx.getLotaTitular(), doc);

		try {
			exBL.incluirCosignatariosAutomaticamente(cadastrante, ctx.getLotaTitular(), doc);
		} catch (Exception e) {
			throw new AplicacaoException("Erro ao tentar incluir os cosignatários deste documento", 0, e);
		}

		resp.sigladoc = doc.getSigla();
	}

	private boolean isNivelAcessoValido(DpPessoa titular, DpLotacao lotaTitular, ExDocumento doc, ExNivelAcesso nivel) {
		List<ExNivelAcesso> lst = NivelDeAcessoUtil.getListaNivelAcesso(doc.getExTipoDocumento(),
				doc.getExFormaDocumento(), doc.getExModelo(), doc.getExClassificacao(), titular, lotaTitular);
		for (ExNivelAcesso nv : lst) {
			if (nv.equals(nivel)) {
				return true;
			}
		}
		return false;
	}

	protected ExDao dao() {
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