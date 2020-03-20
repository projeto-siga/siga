/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * Criado em  13/09/2005
 *
 */
package br.gov.jfrj.siga.vraptor;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.download.InputStreamDownload;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpVisualizacao;
import br.gov.jfrj.siga.dp.DpVisualizacaoAcesso;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExPreenchimento;
import br.gov.jfrj.siga.ex.ExProtocolo;
import br.gov.jfrj.siga.ex.ExSituacaoConfiguracao;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoMobil;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.ex.util.FuncoesEL;
import br.gov.jfrj.siga.ex.vo.ExDocumentoVO;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.Selecao;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.util.ListaHierarquica;
import br.gov.jfrj.siga.util.ListaHierarquicaItem;
import br.gov.jfrj.siga.vraptor.builder.BuscaDocumentoBuilder;

@Controller
public class ExDocumentoController extends ExController {

	private static final String URL_EXIBIR = "/app/expediente/doc/exibir?sigla={0}";
	private static final String URL_EDITAR = "/app/expediente/doc/editar?sigla={0}";

	/**
	 * @deprecated CDI eyes only
	 */
	public ExDocumentoController() {
		super();
	}

	@Inject
	public ExDocumentoController(HttpServletRequest request,
			HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, CpDao.getInstance(), so, em);
	}

	private ExDocumento buscarDocumento(final BuscaDocumentoBuilder builder,
			final boolean verificarAcesso) {
		assertAcesso("");

		ExDocumento doc = builder.buscarDocumento(dao());
		if (verificarAcesso && builder.getMob() != null) {

			verificaNivelAcesso(builder.getMob());
		}

		return doc;
	}

	@Get("app/expediente/doc/atualizar_marcas")
	public void aAtualizarMarcasDoc(final String sigla) {
		assertAcesso("");

		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		final ExDocumento doc = buscarDocumento(builder, false);

		Ex.getInstance().getBL().atualizarMarcas(doc);

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
	}

	@Get("/app/expediente/doc/recalcular_acesso")
	public void aRecalcularAcesso(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder, false);

		Ex.getInstance().getBL().atualizarDnmAcesso(builder.getMob().getDoc());

		result.use(Results.http())
				.body("Acesso: " + builder.getMob().doc().getDnmAcesso()
						+ "\n Alterado: "
						+ builder.getMob().doc().getDnmDtAcesso())
				.setStatusCode(200);
	}

	@Get("app/expediente/doc/cancelar_movimentacoes_replicadas")
	public void aCancelarMovimentacoesReplicadasDoc(final String sigla)
			throws Exception {
		assertAcesso("");

		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		final ExDocumento doc = buscarDocumento(builder, false);

		Set<ExMovimentacao> movsReplicadas = new TreeSet<ExMovimentacao>();
		Set<ExMovimentacao> movs = null;

		for (ExMobil m : doc.getExMobilSet()) {
			movs = m.getExMovimentacaoSet();
			movsReplicadas.addAll(m.getMovimentacoesReplicadas(movs));
		}

		if (!movsReplicadas.isEmpty()) {
			Ex.getInstance().getBL()
					.cancelarMovimentacoesReplicadas(movsReplicadas);
			Ex.getInstance().getBL().atualizarMarcas(doc);
		}

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
	}

	@Get("app/expediente/doc/corrigirPDF")
	public void aCorrigirPDF(final String sigla) {
		assertAcesso("");

		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		final ExDocumento doc = buscarDocumento(builder, false);

		Ex.getInstance().getBL().processar(doc, true, false);

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
	}

	@Get("app/expediente/doc/desfazerCancelamentoDocumento")
	public void aDesfazerCancelamentoDocumento(final String sigla) {
		assertAcesso("");

		final ExDocumentoDTO dto = new ExDocumentoDTO();
		dto.setSigla(sigla);
		buscarDocumento(true, dto);
		if (!Ex.getInstance()
				.getComp()
				.podeDesfazerCancelamentoDocumento(getTitular(),
						getLotaTitular(), dto.getMob())) {
			throw new AplicacaoException(
					"Não é possível desfazer o cancelamento deste documento");
		}

		Ex.getInstance()
				.getBL()
				.DesfazerCancelamentoDocumento(getCadastrante(),
						getLotaTitular(), dto.getDoc());

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
	}

	@Post("app/expediente/doc/alterarpreench")
	public void aAlterarPreenchimento(final ExDocumentoDTO exDocumentoDTO,
			final String[] vars, final String[] campos) throws IOException,
			IllegalAccessException, InvocationTargetException {
		assertAcesso("");

		ModeloDao.iniciarTransacao();
		final ExPreenchimento exPreenchimento = dao()
				.consultar(exDocumentoDTO.getPreenchimento(),
						ExPreenchimento.class, false);

		exPreenchimento.setPreenchimentoBA(getByteArrayFormPreenchimento(vars,
				campos));
		dao().gravar(exPreenchimento);
		ModeloDao.commitTransacao();

		exDocumentoDTO.setPreenchimento(exPreenchimento.getIdPreenchimento());

		final String url = getUrlEncodedParameters();
		if (url.indexOf("preenchimento") >= 0) {
			final String parte1 = url
					.substring(0, url.indexOf("preenchimento"));
			String parte2 = url.substring(url.indexOf("&",
					url.indexOf("&preenchimento") + 1) + 1);
			parte2 = parte2 + "&preenchimento="
					+ exDocumentoDTO.getPreenchimento();
			exDocumentoDTO.setPreenchRedirect(parte1 + parte2);
		} else {
			exDocumentoDTO.setPreenchRedirect(getUrlEncodedParameters());
		}

		result.forwardTo(this).aCarregarPreenchimento(exDocumentoDTO, vars);
	}

	private boolean validar() {
		if (getPar().get("obrigatorios") != null) {
			for (final String valor : getPar().get("obrigatorios")) {
				if (getPar().get(valor) == null
						|| getPar().get(valor)[0].trim().equals("")
						|| getPar().get(valor)[0].trim().equals("Não")
						|| getPar().get(valor)[0].trim().equals("Nao")) {
					return false;
				}
			}
		}
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Post("app/expediente/doc/carregarpreench")
	public void aCarregarPreenchimento(final ExDocumentoDTO exDocumentoDTO,
			final String[] vars) throws IOException, IllegalAccessException,
			InvocationTargetException {
		assertAcesso("");

		setPar(getRequest().getParameterMap());

		// Obtém arrStrBanco[], com os parâmetros vindos do banco
		final ExPreenchimento exPreenchimento = dao()
				.consultar(exDocumentoDTO.getPreenchimento(),
						ExPreenchimento.class, false);
		final String strBanco = new String(exPreenchimento.getPreenchimentoBlob());
		final String arrStrBanco[] = strBanco.split("&");
		String strBancoLimpa = new String();

		final ExDocumentoDTO exDocumentoDTOPreench = exDocumentoDTO;

		// seta os atributos da action com base nos valores do banco, fazendo o
		// decoding da string
		for (final String elem : arrStrBanco) {
			final String[] paramNameAndValue = ((String) elem).split("=");
			final String paramName = paramNameAndValue[0];
			String paramValue = paramNameAndValue[1];
			final String paramValueDecoded = URLDecoder.decode(paramValue,
					"ISO-8859-1");
			final String paramValueEncodedUTF8 = URLEncoder.encode(
					paramValueDecoded, "UTF-8");
			final String dtoParamName = "exDocumentoDTO.".concat(paramName);

			try {
				if (!paramName.contains("Sel.id")) {
					final String mName = "set"
							+ paramName.substring(0, 1).toUpperCase()
							+ paramName.substring(1);
					if (getPar().get(dtoParamName) != null
							|| (dtoParamName.contains("nmOrgaoExterno"))
							|| (dtoParamName.contains("nmDestinatario"))) {
						final Class paramType = exDocumentoDTOPreench
								.getClass().getDeclaredField(paramName)
								.getType();
						final Constructor paramTypeContructor = paramType
								.getConstructor(new Class[] { String.class });
						final Method method = exDocumentoDTOPreench.getClass()
								.getMethod(mName, new Class[] { paramType });
						method.invoke(
								exDocumentoDTOPreench,
								new Object[] { paramTypeContructor
										.newInstance(new Object[] { (paramValueDecoded) }) });
					}
				} else {
					final String mName = "get"
							+ paramName.substring(0, 1).toUpperCase()
							+ paramName.substring(1, paramName.indexOf(".id"));
					if (getPar().get(dtoParamName) != null
							|| (dtoParamName.contains("estinatarioSel.id"))) {
						final Method method = exDocumentoDTOPreench.getClass()
								.getMethod(mName);
						final Selecao sel = (Selecao) method
								.invoke(exDocumentoDTOPreench);
						sel.setId(Long.parseLong(paramValue));
						sel.buscarPorId();
					}
				}

			} catch (NoSuchMethodException nSME) {
			} catch (NoSuchFieldException nSFE) {
			} catch (IllegalAccessException ex) {
			} catch (InstantiationException ex) {
			} catch (NumberFormatException nfe) {
				paramValue = "";
			} catch (InvocationTargetException nfe) {
				paramValue = "";
			} finally {
				strBancoLimpa += "&" + paramName + "=" + paramValueEncodedUTF8;
			}
		}

		// Obtém arrStrURL[], com os parâmetros atuais da edita.jsp
		final String strURL = getUrlEncodedParameters();
		final String arrStrURL[] = strURL.split("&");
		String strURLLimpa = "";

		// limpa a url vinda do browser, tirando o que já consta na string do
		// banco, tirando também os .sigla e .descricao
		exDocumentoDTOPreench.setSigla("");
		if (arrStrURL.length > 0) {
			for (String s : arrStrURL) {
				String arrStrURL2[] = s.split("=");
				if (arrStrURL2.length > 1 && !arrStrURL2[0].contains(".sigla")
						&& !arrStrURL2[0].contains(".descricao")
						&& !strBanco.contains(arrStrURL2[0] + "=")) {
					strURLLimpa = strURLLimpa + s + "&";
				}
			}
		}

		exDocumentoDTOPreench.setPreenchRedirect(strURLLimpa + strBancoLimpa);

		result.forwardTo(this).edita(exDocumentoDTOPreench, null, vars,
				exDocumentoDTO.getMobilPaiSel(),
				exDocumentoDTO.isCriandoAnexo(), exDocumentoDTO.getAutuando(),
				exDocumentoDTO.getIdMobilAutuado(),
				exDocumentoDTO.getCriandoSubprocesso(), null);
	}

	@Get("app/expediente/doc/criar_via")
	public void criarVia(final String sigla) {
		assertAcesso("");

		final ExDocumentoDTO exDocumentoDTO = new ExDocumentoDTO();
		exDocumentoDTO.setSigla(sigla);
		buscarDocumento(true, exDocumentoDTO);

		if (!Ex.getInstance()
				.getComp()
				.podeCriarVia(getTitular(), getLotaTitular(),
						exDocumentoDTO.getMob())) {
			throw new AplicacaoException(
					"Não é possível criar vias neste documento");
		}
		Ex.getInstance()
				.getBL()
				.criarVia(getCadastrante(), getLotaTitular(),
						exDocumentoDTO.getDoc());
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Get("app/expediente/doc/criar_volume")
	public void criarVolume(final String sigla) {
		assertAcesso("");

		final ExDocumentoDTO exDocumentoDTO = new ExDocumentoDTO();
		exDocumentoDTO.setSigla(sigla);
		buscarDocumento(true, exDocumentoDTO);

		if (!Ex.getInstance()
				.getComp()
				.podeCriarVolume(getTitular(), getLotaTitular(),
						exDocumentoDTO.getMob())) {
			throw new AplicacaoException(
					"Não é possível criar volumes neste documento");
		}

		Ex.getInstance()
				.getBL()
				.criarVolume(getCadastrante(), getLotaTitular(),
						exDocumentoDTO.getDoc());
		ExDocumentoController.redirecionarParaExibir(result,
				exDocumentoDTO.getSigla());
	}

	@Post("app/expediente/doc/recarregar")
	public ExDocumentoDTO recarregar(final ExDocumentoDTO exDocumentoDTO,
			final String[] vars, String jsonHierarquiaDeModelos)
			throws IllegalAccessException, InvocationTargetException,
			IOException {
		
		if(exDocumentoDTO != null && exDocumentoDTO
				.getSigla() != null) {
			buscarDocumento(false, exDocumentoDTO);
			if(exDocumentoDTO.getDoc()!= null && exDocumentoDTO.getDoc().isFinalizado() && 
					exDocumentoDTO.getDoc().getExModelo() != null && !exDocumentoDTO.getIdMod().equals(exDocumentoDTO.getDoc().getExModelo().getId())) {
				throw new AplicacaoException("Modelo não pode ser alterado");
			}
		}
		result.forwardTo(this)
				.edita(exDocumentoDTO, null, vars,
						exDocumentoDTO.getMobilPaiSel(),
						exDocumentoDTO.isCriandoAnexo(),
						exDocumentoDTO.getAutuando(),
						exDocumentoDTO.getIdMobilAutuado(),
						exDocumentoDTO.getCriandoSubprocesso(),
						jsonHierarquiaDeModelos);
		return exDocumentoDTO;
	}

	@Post("app/expediente/doc/editar")
	@Get("app/expediente/doc/editar")
	public ExDocumentoDTO edita(ExDocumentoDTO exDocumentoDTO,
			final String sigla, String[] vars,
			final ExMobilSelecao mobilPaiSel, final Boolean criandoAnexo,
			final Boolean autuando, final Long idMobilAutuado,
			final Boolean criandoSubprocesso, String jsonHierarquiaDeModelos)
			throws IOException, IllegalAccessException,
			InvocationTargetException {
		assertAcesso("");

		final boolean isDocNovo = (exDocumentoDTO == null || exDocumentoDTO
				.getSigla() == null);
		
		boolean postback = param("postback") != null;
		if (isDocNovo) {
			if (!postback)
				exDocumentoDTO = new ExDocumentoDTO();
			exDocumentoDTO.setCriandoAnexo(criandoAnexo == null ? false
					: criandoAnexo);
			exDocumentoDTO.setAutuando(autuando == null ? false : autuando);

			if (idMobilAutuado != null)
				exDocumentoDTO.setIdMobilAutuado(idMobilAutuado);

			exDocumentoDTO
					.setCriandoSubprocesso(criandoSubprocesso == null ? false
							: criandoSubprocesso);

			if (mobilPaiSel != null) {
				exDocumentoDTO.setMobilPaiSel(mobilPaiSel);
			}
		}

		if ((sigla != null) && (sigla != "")) {
			exDocumentoDTO.setSigla(sigla);
		}

		buscarDocumentoOuNovo(true, exDocumentoDTO);

		if ((isDocNovo) || (param("exDocumentoDTO.docFilho") != null)) {

			if (exDocumentoDTO.getTipoDestinatario() == null)
				exDocumentoDTO.setTipoDestinatario(2);

			if (exDocumentoDTO.getTipoEmitente() == null)
				exDocumentoDTO.setTipoEmitente(1);

			if (exDocumentoDTO.getIdMod() == null) {
				final List<ExModelo> modelos = getModelos(exDocumentoDTO);

				ExModelo modeloDefault = null;
				for (ExModelo mod : exDocumentoDTO.getModelos()) {
					if ("Memorando".equals(mod.getNmMod())) {
						modeloDefault = mod;
						break;
					}
				}

				if (modeloDefault == null && modelos.size() > 0)
					modeloDefault = modelos.get(0);

				if (modeloDefault == null)
					throw new RuntimeException(
							"Não foi possível carregar um modelo inicial");

				exDocumentoDTO.setIdMod(modeloDefault.getId());

				for (ExTipoDocumento tp : modeloDefault.getExFormaDocumento()
						.getExTipoDocumentoSet()) {
					exDocumentoDTO.setIdTpDoc(tp.getId());
					break;
				}

				// Preencher automaticamente o subscritor quando se tratar de
				// novo documento
				if (exDocumentoDTO.getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO
						&& !postback) {
					DpPessoaSelecao subscritorSel = new DpPessoaSelecao();
					subscritorSel.buscarPorObjeto(getCadastrante());
					exDocumentoDTO.setSubscritorSel(subscritorSel);
				}
			}

			if (exDocumentoDTO.getNivelAcesso() == null) {
				final ExNivelAcesso nivelDefault = getNivelAcessoDefault(exDocumentoDTO);
				if (nivelDefault != null) {
					exDocumentoDTO.setNivelAcesso(nivelDefault
							.getIdNivelAcesso());
				} else {
					exDocumentoDTO.setNivelAcesso(ExNivelAcesso.ID_PUBLICO);
				}
			}
		}

		if (exDocumentoDTO.isCriandoAnexo() && exDocumentoDTO.getId() == null
				&& isDocNovo && !postback) {
			ExModelo despacho = dao().consultarExModelo(null, "Despacho");
			if (despacho == null)
				throw new RuntimeException(
						"Não foi possível carregar um modelo chamado 'Despacho'");
			exDocumentoDTO.setIdMod(despacho.getId());
		}

		if (exDocumentoDTO.getId() == null && exDocumentoDTO.getDoc() != null)
			exDocumentoDTO.setId(exDocumentoDTO.getDoc().getIdDoc());

		if (exDocumentoDTO.getId() == null) {
			if (getLotaTitular().isFechada())
				throw new AplicacaoException(
						"A lotação "
								+ getLotaTitular().getSiglaLotacao()
								+ " foi extinta em "
								+ new SimpleDateFormat("dd/MM/yyyy")
										.format(getLotaTitular()
												.getDataFimLotacao())
								+ ". Não é possível gerar expedientes em lotação extinta.");
			exDocumentoDTO.setDoc(new ExDocumento());
			exDocumentoDTO.getDoc().setOrgaoUsuario(
					getTitular().getOrgaoUsuario());
		} else {
			exDocumentoDTO.setDoc(daoDoc(exDocumentoDTO.getId()));

			if (!Ex.getInstance()
					.getComp()
					.podeEditar(getTitular(), getLotaTitular(),
							exDocumentoDTO.getMob()))
				throw new AplicacaoException(
						"Não é permitido editar documento fechado");

			if (isDocNovo) {
				escreverForm(exDocumentoDTO);
				lerEntrevista(exDocumentoDTO);
			}
		}

		if (exDocumentoDTO.getTipoDocumento() != null
				&& exDocumentoDTO.getTipoDocumento().equals("externo")) {
			exDocumentoDTO.setIdMod(((ExModelo) dao()
					.consultarAtivoPorIdInicial(ExModelo.class, 28L))
					.getIdMod());
		}
		carregarBeans(exDocumentoDTO, mobilPaiSel);

		final Long idSit = Ex
				.getInstance()
				.getConf()
				.buscaSituacao(exDocumentoDTO.getModelo(),
						exDocumentoDTO.getDoc().getExTipoDocumento(),
						getTitular(), getLotaTitular(),
						CpTipoConfiguracao.TIPO_CONFIG_ELETRONICO)
				.getIdSitConfiguracao();

		if (idSit == ExSituacaoConfiguracao.SITUACAO_OBRIGATORIO) {
			exDocumentoDTO.setEletronico(1);
			exDocumentoDTO.setEletronicoFixo(true);
		} else if (idSit == ExSituacaoConfiguracao.SITUACAO_PROIBIDO) {
			exDocumentoDTO.setEletronico(2);
			exDocumentoDTO.setEletronicoFixo(true);
		} else if (idSit == ExSituacaoConfiguracao.SITUACAO_DEFAULT
				&& exDocumentoDTO.isAlterouModelo()) {
			exDocumentoDTO.setEletronico(1);
			exDocumentoDTO.setEletronicoFixo(false);
		} else if (idSit == ExSituacaoConfiguracao.SITUACAO_NAO_DEFAULT
				&& exDocumentoDTO.isAlterouModelo()) {
			exDocumentoDTO.setEletronico(2);
			exDocumentoDTO.setEletronicoFixo(false);
		}

		lerForm(exDocumentoDTO, vars);

		// O (&& classif.getCodAssunto() != null) foi adicionado para permitir
		// que as classificações antigas, ainda não linkadas por equivalência,
		// possam ser usadas

		ExClassificacao classif = exDocumentoDTO.getClassificacaoSel()
				.buscarObjeto();
		if (classif != null && classif.getHisDtFim() != null
				&& classif.getHisDtIni() != null
				&& classif.getCodAssunto() != null) {
			classif = ExDao.getInstance().consultarAtual(classif);
			if (classif != null) {
				exDocumentoDTO.getClassificacaoSel().setId(
						classif.getIdClassificacao());
			} else {
				exDocumentoDTO.getClassificacaoSel().setId(null);
			}
		}

		exDocumentoDTO.getSubscritorSel().buscar();
		exDocumentoDTO.getDestinatarioSel().buscar();
		exDocumentoDTO.getLotacaoDestinatarioSel().buscar();
		exDocumentoDTO.getOrgaoSel().buscar();
		exDocumentoDTO.getOrgaoExternoDestinatarioSel().buscar();
		exDocumentoDTO.getClassificacaoSel().buscar();
		exDocumentoDTO.getMobilPaiSel().buscar();

		registraErroExtEditor();

		// Usado pela extensão editor...
		getPar().put(
				"serverAndPort",
				new String[] { getRequest().getServerName()
						+ (getRequest().getServerPort() > 0 ? ":"
								+ getRequest().getServerPort() : "") });

		// ...inclusive nas operações com preenchimento automático
		if (exDocumentoDTO.getPreenchRedirect() != null
				&& exDocumentoDTO.getPreenchRedirect().length() > 2) {
			exDocumentoDTO.setPreenchRedirect(exDocumentoDTO
					.getPreenchRedirect()
					+ "&serverAndPort="
					+ getPar().get("serverAndPort")[0]);
		}

		exDocumentoDTO.setTiposDocumento(getTiposDocumentoParaCriacao());
		if (exDocumentoDTO.getModelo() != null) {
			Set<ExTipoDocumento> set = exDocumentoDTO.getModelo()
					.getExFormaDocumento().getExTipoDocumentoSet();
			List<ExTipoDocumento> l = new ArrayList<>();
			for (ExTipoDocumento tp : exDocumentoDTO.getTiposDocumento()) {
				if (set.contains(tp))
					l.add(tp);
			}
			exDocumentoDTO.setTiposDocumento(l);
		}
		exDocumentoDTO.setListaNivelAcesso(getListaNivelAcesso(exDocumentoDTO));
		if (exDocumentoDTO.getModelo() != null
				&& exDocumentoDTO.getModelo().getExNivelAcesso() != null) {
			List<ExNivelAcesso> l = new ArrayList<>();
			l.add(exDocumentoDTO.getModelo().getExNivelAcesso());
			exDocumentoDTO.setListaNivelAcesso(l);
		}

		getPreenchimentos(exDocumentoDTO);

		final Map<String, String[]> parFreeMarker = new HashMap<>();
		setPar(getRequest().getParameterMap());
		if (getPar() != null) {
			for (final String key : getPar().keySet()) {
				final String as[] = getPar().get(key);
				final String chave = key.replace("exDocumentoDTO.", "");
				parFreeMarker.put(chave, as);
			}
			for (String p : exDocumentoDTO.getParamsEntrevista().keySet()) {
				if (!parFreeMarker.containsKey(p)) {
					final String as[] = new String[] { exDocumentoDTO
							.getParamsEntrevista().get(p) };
					parFreeMarker.put(p, as);
					// System.out.println("*** " + p + ", "
					// + exDocumentoDTO.getParamsEntrevista().get(p));
				}
			}
		}
		// Usado pela macro de "partes"...
		parFreeMarker.put("sigla_titular", new String[] { getTitular()
				.getSigla() });
		parFreeMarker.put("sigla_lota_titular", new String[] { getLotaTitular()
				.getSiglaCompleta() });

		// result.include("param", exDocumentoDTO.getParamsEntrevista());

		boolean podeEditarData = Ex
				.getInstance()
				.getComp()
				.podeEditarData(getTitular(), getLotaTitular(),
						exDocumentoDTO.getModelo());

		boolean podeEditarDescricao = Ex
				.getInstance()
				.getComp()
				.podeEditarDescricao(getTitular(), getLotaTitular(),
						exDocumentoDTO.getModelo());

		List<String> l = new ArrayList<String>();
		for (String p : exDocumentoDTO.getParamsEntrevista().keySet()) {
			result.include(p, exDocumentoDTO.getParamsEntrevista().get(p));
			l.add(p);
			// System.out.println("*** " + p + ", "
			// + exDocumentoDTO.getParamsEntrevista().get(p));
		}

		boolean modeloEncontrado = false;
		ListaHierarquica lh = null;
		if (jsonHierarquiaDeModelos != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				lh = mapper.readValue(jsonHierarquiaDeModelos,
						ListaHierarquica.class);
				for (ListaHierarquicaItem m : lh.getList()) {
					if (m.getValue() != null
							&& m.getValue().equals(exDocumentoDTO.getIdMod())) {
						m.selected = true;
						modeloEncontrado = true;
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		if (jsonHierarquiaDeModelos == null || !modeloEncontrado) {
			lh = new ListaHierarquica();
			for (ExModelo m : getModelos(exDocumentoDTO)) {
				lh.add(m.getNmMod(),m.getDescMod(), m.getId(),
						m.getId().equals(exDocumentoDTO.getIdMod()));
			}

			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonHierarquiaDeModelos = mapper.writeValueAsString(lh);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		int cModelos = 0;
		for (ListaHierarquicaItem i : lh.getList())
			if (!i.group)
				cModelos++;

		result.include("vars", l);

		result.include("possuiMaisQueUmModelo", cModelos > 1);
		result.include("par", parFreeMarker);
		result.include("cpOrgaoSel", exDocumentoDTO.getCpOrgaoSel());
		result.include("mobilPaiSel", exDocumentoDTO.getMobilPaiSel());
		result.include("subscritorSel", exDocumentoDTO.getSubscritorSel());
		result.include("titularSel", exDocumentoDTO.getTitularSel());
		result.include("destinatarioSel", exDocumentoDTO.getDestinatarioSel());
		result.include("lotacaoDestinatarioSel",
				exDocumentoDTO.getLotacaoDestinatarioSel());
		result.include("orgaoExternoDestinatarioSel",
				exDocumentoDTO.getOrgaoExternoDestinatarioSel());
		result.include("classificacaoSel", exDocumentoDTO.getClassificacaoSel());
		result.include("tipoDestinatario", exDocumentoDTO.getTipoDestinatario());
		result.include("tipoEmitente", exDocumentoDTO.getTipoEmitente());
		result.include("podeEditarData", podeEditarData);
		result.include("podeEditarDescricao", podeEditarDescricao);
		result.include("hierarquiaDeModelos", lh.getList());
		result.include("jsonHierarquiaDeModelos",
				escapeHtml(jsonHierarquiaDeModelos));
		result.include("podeEditarModelo", exDocumentoDTO.getDoc().isFinalizado());

		// Desabilita a proteção contra injeção maldosa de html e js
		this.response.addHeader("X-XSS-Protection", "0");
		return exDocumentoDTO;
	}

	private List<ExTipoDocumento> getTiposDocumentoParaCriacao() {
		List<ExTipoDocumento> l = dao().listarExTiposDocumento();
		return l;
	}

	private void registraErroExtEditor() throws IOException {
		if (param("desconsiderarExtensao") != null
				&& param("desconsiderarExtensao").equals("true")) {

			String nomeArquivo = getRequest().getRemoteHost();
			nomeArquivo = nomeArquivo.replaceAll(":", "_");

			BufferedWriter out = new BufferedWriter(new FileWriter(
					"./siga-ex-ext-editor-erro/" + nomeArquivo));
			out.close();
		}
	}

	@SuppressWarnings("rawtypes")
	private void obterMetodoPorString(final String metodo, final ExDocumento doc)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		if (metodo != null) {
			final Class[] classes = new Class[] { ExDocumento.class };

			Method method;
			try {
				method = Ex.getInstance().getBL().getClass()
						.getDeclaredMethod(metodo, classes);
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
				return;
			}
			method.invoke(Ex.getInstance().getBL(), new Object[] { doc });
		}
	}

	@Get("/app/expediente/doc/excluir")
	public void aExcluirDocMovimentacoes(final String sigla) throws Exception {
		assertAcesso("");

		final ExDocumentoDTO exDocumentoDTO = new ExDocumentoDTO();
		exDocumentoDTO.setSigla(sigla);
		buscarDocumento(true, exDocumentoDTO);
		final ExDocumento doc = exDocumentoDTO.getDoc();

		Ex.getInstance().getBL()
				.excluirDocumento(doc, getTitular(), getLotaTitular(), false);

		result.redirectTo("/");
	}

	@Post("app/expediente/doc/excluirpreench")
	public void aExcluirPreenchimento(final ExDocumentoDTO exDocumentoDTO,
			final String[] vars) throws IllegalAccessException,
			InvocationTargetException, IOException {
		assertAcesso("");

		ModeloDao.iniciarTransacao();
		final ExPreenchimento exemplo = dao()
				.consultar(exDocumentoDTO.getPreenchimento(),
						ExPreenchimento.class, false);
		dao().excluir(exemplo);
		ModeloDao.commitTransacao();
		exDocumentoDTO.setPreenchimento(0L);
		result.forwardTo(this).edita(exDocumentoDTO, null, vars,
				exDocumentoDTO.getMobilPaiSel(),
				exDocumentoDTO.isCriandoAnexo(), exDocumentoDTO.getAutuando(),
				exDocumentoDTO.getIdMobilAutuado(),
				exDocumentoDTO.getCriandoSubprocesso(), null);
	}

	@SuppressWarnings("static-access")
	private void assertAcesso(final ExDocumentoDTO exDocumentoDTO)
			throws Exception {
		if (!Ex.getInstance()
				.getComp()
				.podeAcessarDocumento(getTitular(), getLotaTitular(),
						exDocumentoDTO.getMob())) {
			String msgDestinoDoc = arquivamentoAutomatico(exDocumentoDTO
					.getMob());

			String s = "";
			s += exDocumentoDTO.getMob().doc().getListaDeAcessosString();
			s = "(" + s + ")";
			s = " "
					+ exDocumentoDTO.getMob().doc().getExNivelAcessoAtual()
							.getNmNivelAcesso() + " " + s;

			Map<ExPapel, List<Object>> mapa = exDocumentoDTO.getMob().doc()
					.getPerfis();
			boolean isInteressado = false;

			for (ExPapel exPapel : mapa.keySet()) {
				Iterator<Object> it = mapa.get(exPapel).iterator();

				if ((exPapel != null)
						&& (exPapel.getIdPapel() == exPapel.PAPEL_INTERESSADO)) {
					while (it.hasNext() && !isInteressado) {
						Object item = it.next();
						isInteressado = item.toString().equals(
								getTitular().getSigla()) ? true : false;
					}
				}

			} 
			
			if (exDocumentoDTO.getMob().doc().isSemEfeito() ) {
				if (!exDocumentoDTO.getMob().doc().getCadastrante().equals(getTitular()) &&
				    !exDocumentoDTO.getMob().doc().getSubscritor().equals(getTitular()) && !isInteressado) {
						throw new AplicacaoException("Documento "
								+ exDocumentoDTO.getMob().getSigla()
								+ " cancelado ");
				}
			} else  {
				throw new AplicacaoException("Documento "
						+ exDocumentoDTO.getMob().getSigla()
						+ " inacessível ao usuário " + getTitular().getSigla()
						+ "/" + getLotaTitular().getSiglaCompleta() + "." + s
						+ " " + msgDestinoDoc);
			}
		}
	}

	private String arquivamentoAutomatico(ExMobil mob) throws Exception {

		String msgDestinoDoc = "";
		DpPessoa dest = null;
		DpLotacao lotaDest = null;
		Boolean jaArquivado = false;
		ExMobil mobArq, mobUlt = null; /* mobil a ser arquivado / ultimo mobil */

		if (!mob.doc().isFinalizado()) { /* doc temporário só tem o geral */
			dest = mob.doc().getCadastrante().getPessoaAtual();
			lotaDest = mob.doc().getLotaCadastrante().getLotacaoAtual();
			mobArq = mob; /*
						 * mobil a ser arquivado (excluido) sempre vai ser o
						 * geral
						 */
		} else {
			if (mob.doc().isProcesso()) {
				mobUlt = mob.doc().getUltimoVolume();
				mobArq = mob.doc().getMobilGeral();
			} else {
				if (mob.isGeral()) {
					msgDestinoDoc = "Para arquivar ou cancelar informe a via.";
					return msgDestinoDoc;
				} else {
					mobUlt = mob;
					mobArq = mob;
				}
			}
			/* verificar onde está o documento no momento */
			if (mobUlt.isJuntado())
				mobUlt = mobUlt.getMobilPrincipal();
			if (mobUlt.isApensado())
				mobUlt = mobUlt.getGrandeMestre();

			if (mobUlt.getUltimaMovimentacaoNaoCancelada() != null) {
				if (mobUlt.getUltimaMovimentacaoNaoCancelada().getResp() != null) {
					dest = mobUlt.getUltimaMovimentacaoNaoCancelada().getResp()
							.getPessoaAtual();
				}
				lotaDest = mobUlt.getUltimaMovimentacaoNaoCancelada()
						.getLotaResp().getLotacaoAtual();
			}
		}

		if (mobArq.isArquivado())
			jaArquivado = true;

		if (!jaArquivado && !mob.doc().isCancelado()) {
			if (!dest.ativaNaData(new Date())) {
				if (getLotaTitular().equivale(lotaDest) /*
														 * a pessoa que está
														 * tentando acessar está
														 * na mesma lotação onde
														 * se encontra o doc
														 */
						&& (mob.doc().getExNivelAcessoAtual()
								.getGrauNivelAcesso().intValue() == ExNivelAcesso.NIVEL_ACESSO_SUB_PESSOA
								|| mob.doc().getExNivelAcessoAtual()
										.getGrauNivelAcesso().intValue() == ExNivelAcesso.NIVEL_ACESSO_PESSOAL || mob
								.doc().getExNivelAcessoAtual()
								.getGrauNivelAcesso().intValue() == ExNivelAcesso.NIVEL_ACESSO_PESSOA_SUB)) {

					Boolean alguemPodeAcessar = false;
					Set<DpPessoa> pessoas = lotaDest.getDpPessoaLotadosSet();
					for (DpPessoa pes : pessoas) { /*
													 * verificar se alguem da
													 * lotação pode acessar o
													 * documento
													 */
						if (Ex.getInstance().getComp()
								.podeAcessarDocumento(pes, lotaDest, mob))
							if (pes.getPessoaAtual().ativaNaData(new Date())) {
								alguemPodeAcessar = true;
								break;
							}
					}
					if (!alguemPodeAcessar) { /*
											 * ninguem pode acessar este
											 * documento
											 */
						if (mob.doc().isFinalizado()) {							
							if (!mob.doc().isPendenteDeAssinatura()) { 
								if(mobUlt.isEmTransito()) { /*  em transito */
									Ex.getInstance()
									.getBL()
									.receber(getCadastrante(), getLotaTitular(),
											mobUlt, new Date());
								}
								if (mob.doc().isEletronico()
										&& (mobUlt.temAnexosNaoAssinados() ||  mobUlt.doc().getMobilGeral().temAnexosNaoAssinados())) {
									Ex.getInstance()
									.getBL()
									.excluirAnexosNaoAssinados(getTitular(), getLotaTitular(), mobUlt);															
								}
								if (Ex.getInstance()
										.getComp()
										.podeArquivarCorrente(getTitular(),
												getLotaTitular(), mobArq)) {
									
										Ex.getInstance()
										.getBL()
										.arquivarCorrenteAutomatico(dest, getLotaTitular(), mobArq);
										msgDestinoDoc = "documento sendo arquivado automaticamente";
								} else { /* não pode arquivar */	
									
										msgDestinoDoc = "documento não pode ser arquivado automaticamente";																		
								}								
							} else { /* pendente de assinatura */								

								Ex.getInstance()
										.getBL()
										.cancelarDocumento(getTitular(),
												getLotaTitular(), mob.doc());

								msgDestinoDoc = "documento sendo cancelado automaticamente";

							}

						} else { /* doc temporário */
							Ex.getInstance()
									.getBL()
									.excluirDocumentoAutomatico(mob.doc(),
											dest, getLotaTitular());
							msgDestinoDoc = "documento sendo excluído automaticamente";
						}
					}
				}
			}
		}
		
		ContextoPersistencia.flushTransaction();

		return msgDestinoDoc;

	}

	@Get("app/expediente/doc/exibirAntigo")
	public void aExibirAntigo(final String sigla, final boolean popup,
			final boolean exibirCompleto) throws Exception {
		assertAcesso("");

		final ExDocumentoDTO exDocumentoDTO = new ExDocumentoDTO();

		exDocumentoDTO.setSigla(sigla);
		buscarDocumento(false, exDocumentoDTO);

		assertAcesso(exDocumentoDTO);

		if (Ex.getInstance()
				.getComp()
				.podeReceberEletronico(getTitular(), getLotaTitular(),
						exDocumentoDTO.getMob())) {
			Ex.getInstance()
					.getBL()
					.receber(getCadastrante(), getLotaTitular(),
							exDocumentoDTO.getMob(), new Date());
		}

		final ExDocumentoVO docVO = new ExDocumentoVO(exDocumentoDTO.getDoc(),
				exDocumentoDTO.getMob(), getCadastrante(), getTitular(),
				getLotaTitular(), true, true);

		if (exDocumentoDTO.getMob().isEliminado()) {
			throw new AplicacaoException(
					"Documento "
							+ exDocumentoDTO.getMob().getSigla()
							+ " eliminado, conforme o termo "
							+ exDocumentoDTO
									.getMob()
									.getUltimaMovimentacaoNaoCancelada(
											ExTipoMovimentacao.TIPO_MOVIMENTACAO_ELIMINACAO)
									.getExMobilRef());
		}
		result.include("msg", exDocumentoDTO.getMsg());
		result.include("docVO", docVO);
		result.include("mob", exDocumentoDTO.getMob());
		result.include("exibirCompleto", exibirCompleto);
		result.include("currentTimeMillis", System.currentTimeMillis());
		result.include("popup", popup);
	}
	
	@Get("app/expediente/doc/exibirHistorico")
	public void aExibirHistorico(final String sigla, final boolean popup, final Long idVisualizacao) throws Exception {
		assertAcesso("");

		final ExDocumentoDTO exDocumentoDTO = new ExDocumentoDTO();

		exDocumentoDTO.setSigla(sigla);
		buscarDocumento(false, exDocumentoDTO);

		if(!podeVisualizarDocumento(exDocumentoDTO.getMob(), getTitular(), idVisualizacao)) {
			assertAcesso(exDocumentoDTO);
		}

		if (Ex.getInstance()
				.getComp()
				.podeReceberEletronico(getTitular(), getLotaTitular(),
						exDocumentoDTO.getMob())) {
			Ex.getInstance()
					.getBL()
					.receber(getCadastrante(), getLotaTitular(),
							exDocumentoDTO.getMob(), new Date());
		}

		final ExDocumentoVO docVO = new ExDocumentoVO(exDocumentoDTO.getDoc(),
				exDocumentoDTO.getMob(), getCadastrante(), getTitular(),
				getLotaTitular(), true, true);

		if (exDocumentoDTO.getMob().isEliminado()) {
			throw new AplicacaoException(
					"Documento "
							+ exDocumentoDTO.getMob().getSigla()
							+ " eliminado, conforme o termo "
							+ exDocumentoDTO
									.getMob()
									.getUltimaMovimentacaoNaoCancelada(
											ExTipoMovimentacao.TIPO_MOVIMENTACAO_ELIMINACAO)
									.getExMobilRef());
		}
		result.include("msg", exDocumentoDTO.getMsg());
		result.include("docVO", docVO);
		result.include("mob", exDocumentoDTO.getMob());
		result.include("currentTimeMillis", System.currentTimeMillis());
		result.include("popup", popup);
	}
	
	@Get({ "/app/expediente/doc/visualizar"})
	public void visualiza(final boolean conviteEletronico, final String sigla,
			final ExDocumentoDTO exDocumentoDTO, final Long idmob, final Long idVisualizacao)
			throws Exception {
		assertAcesso("");

		ExDocumentoDTO exDocumentoDto;
		if (exDocumentoDTO == null) {
			exDocumentoDto = new ExDocumentoDTO();
		} else {
			exDocumentoDto = exDocumentoDTO;
		}

		exDocumentoDto.setIdMob(idmob);

		exDocumentoDto.setSigla(sigla);
		buscarDocumento(false, exDocumentoDto);

		if(!podeVisualizarDocumento(exDocumentoDTO.getMob(), getTitular(), idVisualizacao) || !Cp.getInstance().getConf()
				.podePorConfiguracao(getCadastrante(), getCadastrante().getLotacao(), CpTipoConfiguracao.TIPO_CONFIG_DELEGAR_VISUALIZACAO)) {
			assertAcesso(exDocumentoDto);
		}
		
		if(idVisualizacao != null && !idVisualizacao.equals(Long.valueOf(0))) {
			result.include("idVisualizacao", idVisualizacao);
			DpVisualizacaoAcesso acesso = new DpVisualizacaoAcesso();
			DpVisualizacao vis = dao().consultar(idVisualizacao, DpVisualizacao.class, false);
			acesso.setDtAcesso(dao().dt());
			acesso.setIdDoc(exDocumentoDTO.getDoc().getIdDoc());
			acesso.setIdTitular(vis.getTitular().getId());
			acesso.setIdDelegado(getTitular().getId());
			acesso.setIdVisualizacao(vis.getIdVisualizacao());
			dao().gravar(acesso);
		}

		if (Ex.getInstance()
				.getComp()
				.podeReceberEletronico(getTitular(), getLotaTitular(),
						exDocumentoDto.getMob())) {
			Ex.getInstance()
					.getBL()
					.receber(getCadastrante(), getLotaTitular(),
							exDocumentoDto.getMob(), new Date());
		}

		if (exDocumentoDto.getMob() == null
				|| exDocumentoDto.getMob().isGeral()) {
			if (exDocumentoDto.getMob().getDoc().isFinalizado()) {
				if (exDocumentoDto.getDoc().isProcesso()) {
					exDocumentoDto.setMob(exDocumentoDto.getDoc()
							.getUltimoVolume());
				} else {
					exDocumentoDto.setMob(exDocumentoDto.getDoc()
							.getPrimeiraVia());
				}
			}
		}

		final ExDocumentoVO docVO = new ExDocumentoVO(exDocumentoDto.getDoc(),
				exDocumentoDto.getMob(), getCadastrante(), getTitular(),
				getLotaTitular(), true, false);

		docVO.exibe();

		String Sigla = "";
		if (exDocumentoDto.getSigla() != null) {
			Sigla = exDocumentoDto.getSigla().replace("/", "");
		}
		
		docVO.addAcoesVisualizar(docVO, idVisualizacao);

		result.include("docVO", docVO);
		docVO.getMobs().get(0).getAcoesOrdenadasPorNome();
		result.include("sigla", Sigla);
		result.include("id", exDocumentoDto.getId());
		result.include("mob", exDocumentoDto.getMob());
		result.include("lota", this.getLotaTitular());
		result.include("param", exDocumentoDto.getParamsEntrevista());
	}

	@Get({ "/app/expediente/doc/exibir", "/expediente/doc/exibir.action" })
	public void exibe(final boolean conviteEletronico, final String sigla,
			final ExDocumentoDTO exDocumentoDTO, final Long idmob, final Long idVisualizacao)
			throws Exception {
		assertAcesso("");

		ExDocumentoDTO exDocumentoDto;
		if (exDocumentoDTO == null) {
			exDocumentoDto = new ExDocumentoDTO();
		} else {
			exDocumentoDto = exDocumentoDTO;
		}

		exDocumentoDto.setIdMob(idmob);

		exDocumentoDto.setSigla(sigla);
		buscarDocumento(false, exDocumentoDto);

		if(!podeVisualizarDocumento(exDocumentoDto.getMob(), getTitular(), idVisualizacao)) {
			assertAcesso(exDocumentoDto);
		}

		if (Ex.getInstance()
				.getComp()
				.podeReceberEletronico(getTitular(), getLotaTitular(),
						exDocumentoDto.getMob())) {
			Ex.getInstance()
					.getBL()
					.receber(getCadastrante(), getLotaTitular(),
							exDocumentoDto.getMob(), new Date());
		}

		if (exDocumentoDto.getMob() == null
				|| exDocumentoDto.getMob().isGeral()) {
			if (exDocumentoDto.getMob().getDoc().isFinalizado()) {
				if (exDocumentoDto.getDoc().isProcesso()) {
					exDocumentoDto.setMob(exDocumentoDto.getDoc()
							.getUltimoVolume());
				} else {
					exDocumentoDto.setMob(exDocumentoDto.getDoc()
							.getPrimeiraVia());
				}
			}
		}

		final ExDocumentoVO docVO = new ExDocumentoVO(exDocumentoDto.getDoc(),
				exDocumentoDto.getMob(), getCadastrante(), getTitular(),
				getLotaTitular(), true, false);

		docVO.exibe();

		String Sigla = "";
		if (exDocumentoDto.getSigla() != null) {
			Sigla = exDocumentoDto.getSigla().replace("/", "");
		}

		result.include("docVO", docVO);
		result.include("sigla", Sigla);
		result.include("id", exDocumentoDto.getId());
		result.include("mob", exDocumentoDto.getMob());
		result.include("lota", this.getLotaTitular());
		result.include("param", exDocumentoDto.getParamsEntrevista());
		result.include("idVisualizacao", idVisualizacao);
	}

	@Get("app/expediente/doc/exibirProcesso")
	public void exibeProcesso(final String sigla, final boolean podeExibir, Long idVisualizacao)
			throws Exception {
		exibe(false, sigla, null, null, idVisualizacao);
	}

	@Get("/app/expediente/doc/exibirResumoProcesso")
	public void exibeResumoProcesso(final String sigla, final boolean podeExibir)
			throws Exception {
		exibe(false, sigla, null, null, null);
	}

	private void verificaDocumento(final ExDocumento doc) {
		if ((doc.getSubscritor() == null)
				&& !doc.isExternoCapturado()
				&& !doc.isExterno()
				&& ((doc.isProcesso() && doc.isEletronico()) || !doc
						.isProcesso())) {
			throw new AplicacaoException(
					"É necessário definir um subscritor para o documento.");
		}

		if (doc.getDestinatario() == null
				&& doc.getLotaDestinatario() == null
				&& (doc.getNmDestinatario() == null || doc.getNmDestinatario()
						.trim().equals(""))
				&& doc.getOrgaoExternoDestinatario() == null
				&& (doc.getNmOrgaoExterno() == null || doc.getNmOrgaoExterno()
						.trim().equals(""))) {
			final Long idSit = Ex
					.getInstance()
					.getConf()
					.buscaSituacao(doc.getExModelo(), getTitular(),
							getLotaTitular(),
							CpTipoConfiguracao.TIPO_CONFIG_DESTINATARIO)
					.getIdSitConfiguracao();
			if (idSit == ExSituacaoConfiguracao.SITUACAO_OBRIGATORIO) {
				throw new AplicacaoException("Para documentos do modelo "
						+ doc.getExModelo().getNmMod()
						+ ", é necessário definir um destinatário");
			}
		}

		if (doc.getExClassificacao() == null) {
			throw new AplicacaoException(
					"É necessário informar a classificação documental.");
		}

	}

	private void buscarDocumentoOuNovo(final boolean fVerificarAcesso,
			final ExDocumentoDTO exDocumentoDTO) {
		buscarDocumento(fVerificarAcesso, true, exDocumentoDTO);
		if (exDocumentoDTO.getDoc() == null) {
			exDocumentoDTO.setDoc(new ExDocumento());
			exDocumentoDTO.getDoc().setExTipoDocumento(
					dao().consultar(ExTipoDocumento.TIPO_DOCUMENTO_INTERNO,
							ExTipoDocumento.class, false));
			exDocumentoDTO.setMob(new ExMobil());
			exDocumentoDTO.getMob().setExTipoMobil(
					dao().consultar(ExTipoMobil.TIPO_MOBIL_GERAL,
							ExTipoMobil.class, false));
			exDocumentoDTO.getMob().setNumSequencia(1);
			exDocumentoDTO.getMob().setExDocumento(exDocumentoDTO.getDoc());

			exDocumentoDTO.getDoc().setExMobilSet(new TreeSet<ExMobil>());
			exDocumentoDTO.getDoc().getExMobilSet()
					.add(exDocumentoDTO.getMob());
		}
	}

	private void buscarDocumento(final boolean fVerificarAcesso,
			final ExDocumentoDTO exDocumentoDTO) {
		buscarDocumento(fVerificarAcesso, false, exDocumentoDTO);
	}

	private void buscarDocumento(final boolean fVerificarAcesso,
			final boolean fPodeNaoExistir, final ExDocumentoDTO exDocumentoDto) {
		if (exDocumentoDto.getMob() == null
				&& exDocumentoDto.getSigla() != null
				&& exDocumentoDto.getSigla().length() != 0) {
			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(exDocumentoDto.getSigla());
			exDocumentoDto.setMob((ExMobil) dao().consultarPorSigla(filter));
			if (exDocumentoDto.getMob() != null) {
				exDocumentoDto.setDoc(exDocumentoDto.getMob().getExDocumento());
			}
		} else if (exDocumentoDto.getMob() == null
				&& exDocumentoDto.getDocumentoViaSel().getId() != null) {
			exDocumentoDto
					.setIdMob(exDocumentoDto.getDocumentoViaSel().getId());
			exDocumentoDto.setMob(dao().consultar(exDocumentoDto.getIdMob(),
					ExMobil.class, false));
		} else if (exDocumentoDto.getMob() == null
				&& exDocumentoDto.getIdMob() != null
				&& exDocumentoDto.getIdMob() != 0) {
			exDocumentoDto.setMob(dao().consultar(exDocumentoDto.getIdMob(),
					ExMobil.class, false));
		}
		if (exDocumentoDto.getMob() != null) {
			exDocumentoDto.setDoc(exDocumentoDto.getMob().doc());
		}
		if (exDocumentoDto.getDoc() == null) {
			final String id = param("exDocumentoDto.id");
			if (id != null && id.length() != 0) {
				exDocumentoDto.setDoc(daoDoc(Long.parseLong(id)));
			}
		}
		if (exDocumentoDto.getDoc() != null && exDocumentoDto.getMob() == null) {
			exDocumentoDto.setMob(exDocumentoDto.getDoc().getMobilGeral());
		}

		if (!fPodeNaoExistir && exDocumentoDto.getDoc() == null) {
			throw new AplicacaoException("Documento não informado");
		}
		if (fVerificarAcesso && exDocumentoDto.getMob() != null
				&& exDocumentoDto.getMob().getIdMobil() != null) {
			verificaNivelAcesso(exDocumentoDto.getMob());
		}
	}

	@Get("/app/expediente/doc/finalizar")
	public void aFinalizar(final String sigla) {
		final ExDocumentoDTO exDocumentoDto = new ExDocumentoDTO();
		exDocumentoDto.setSigla(sigla);

		buscarDocumento(true, exDocumentoDto);

		verificaDocumento(exDocumentoDto.getDoc());

		if (!Ex.getInstance()
				.getComp()
				.podeFinalizar(getTitular(), getLotaTitular(),
						exDocumentoDto.getMob())) {
			throw new AplicacaoException("Não é possível Finalizar");
		}

		try {
			exDocumentoDto.setMsg(Ex
					.getInstance()
					.getBL()
					.finalizar(getCadastrante(), getLotaTitular(),
							exDocumentoDto.getDoc()));

			if (exDocumentoDto.getDoc().getForm() != null) {
				if (exDocumentoDto.getDoc().getForm().get("acaoFinalizar") != null
						&& exDocumentoDto.getDoc().getForm()
								.get("acaoFinalizar").trim().length() > 0) {
					obterMetodoPorString(
							exDocumentoDto.getDoc().getForm()
									.get("acaoFinalizar"),
							exDocumentoDto.getDoc());
				}
			}

		} catch (final Throwable t) {
			throw new AplicacaoException("Erro ao finalizar documento", 0, t);
		}

		result.redirectTo("exibir?sigla=" + exDocumentoDto.getDoc().getCodigo());
	}

	@Post("/app/expediente/doc/gravar")
	public void gravar(final ExDocumentoDTO exDocumentoDTO,
			final String[] vars, final String[] campos,
			final UploadedFile arquivo, String jsonHierarquiaDeModelos) {
		final Ex ex = Ex.getInstance();
		final ExBL exBL = ex.getBL();

		try {
			buscarDocumentoOuNovo(true, exDocumentoDTO);
			if (exDocumentoDTO.getDoc() == null) {
				exDocumentoDTO.setDoc(new ExDocumento());
			}

			long tempoIni = System.currentTimeMillis();

			setPar(getRequest().getParameterMap());
			if (!validar()) {
				getPar().put("alerta", new String[] { "Sim" });
				exDocumentoDTO.setAlerta("Sim");
				result.forwardTo(this).edita(exDocumentoDTO, null, vars,
						exDocumentoDTO.getMobilPaiSel(),
						exDocumentoDTO.isCriandoAnexo(),
						exDocumentoDTO.getAutuando(),
						exDocumentoDTO.getIdMobilAutuado(),
						exDocumentoDTO.getCriandoSubprocesso(),
						jsonHierarquiaDeModelos);
				return;
			}

			lerForm(exDocumentoDTO, vars);

			if ((exDocumentoDTO.getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_CAPTURADO || exDocumentoDTO
					.getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_CAPTURADO)
					&& exDocumentoDTO.getDoc().getIdDoc() == null
					&& arquivo == null)
				throw new AplicacaoException(
						"Documento capturado não pode ser gravado sem que seja informado o arquivo PDF.");

			if (!ex.getConf().podePorConfiguracao(getTitular(),
					getLotaTitular(),
					exDocumentoDTO.getDoc().getExTipoDocumento(),
					exDocumentoDTO.getDoc().getExFormaDocumento(),
					exDocumentoDTO.getDoc().getExModelo(),
					exDocumentoDTO.getDoc().getExClassificacaoAtual(),
					exDocumentoDTO.getDoc().getExNivelAcessoAtual(),
					CpTipoConfiguracao.TIPO_CONFIG_CRIAR)) {

				if (!ex.getConf().podePorConfiguracao(getTitular(),
						getLotaTitular(), null, null, null,
						exDocumentoDTO.getDoc().getExClassificacao(), null,
						CpTipoConfiguracao.TIPO_CONFIG_CRIAR)) {
					throw new AplicacaoException(
							"Usuário não possui permissão de criar documento da classificação "
									+ exDocumentoDTO.getDoc()
											.getExClassificacao()
											.getCodificacao());
				}

				throw new AplicacaoException("Operação não permitida");
			}

			// System.out.println("monitorando gravacao IDDoc "
			// + exDocumentoDTO.getDoc().getIdDoc() + ", PESSOA "
			// + exDocumentoDTO.getDoc().getCadastrante().getIdPessoa()
			// + ". Terminou verificacao de config PodeCriarModelo: "
			// + (System.currentTimeMillis() - tempoIni));

			tempoIni = System.currentTimeMillis();

			if (exDocumentoDTO.getDoc().getOrgaoUsuario() == null) {
				exDocumentoDTO.getDoc().setOrgaoUsuario(
						getLotaTitular().getOrgaoUsuario());
			}

			if (exDocumentoDTO.isClassificacaoIntermediaria()
					&& (exDocumentoDTO.getDescrClassifNovo() == null || exDocumentoDTO
							.getDescrClassifNovo().trim().length() == 0)) {
				throw new AplicacaoException(
						"Quando a classificação selecionada não traz informação para criação de vias, o "
								+ "sistema exige que, antes de gravar o documento, seja informada uma sugestão de "
								+ "classificação para ser incluída na próxima revisão da tabela de classificações.");
			}

			if (exDocumentoDTO.getDoc().getDescrDocumento() != null
					&& exDocumentoDTO.getDoc().getDescrDocumento().length() > exDocumentoDTO
							.getTamanhoMaximoDescricao()) {
				throw new AplicacaoException(
						"O campo descrição possui mais do que "
								+ exDocumentoDTO.getTamanhoMaximoDescricao()
								+ " caracteres.");
			}

			if (exDocumentoDTO.getDoc().isFinalizado()) {

				final Date dt = dao().dt();
				final Calendar c = Calendar.getInstance();
				c.setTime(dt);

				final Calendar dtDocCalendar = Calendar.getInstance();

				if (exDocumentoDTO.getDoc().getDtDoc() == null) {
					throw new Exception(
							"A data do documento deve ser informada.");
				}

				dtDocCalendar.setTime(exDocumentoDTO.getDoc().getDtDoc());

				if (c.before(dtDocCalendar)) {
					throw new Exception(
							"Não é permitido criar documento com data futura");
				}
				
				// Verifica se a data está entre o ano 2000 e o ano 2100
				if (!Data.dataDentroSeculo21(exDocumentoDTO.getDoc().getDtDoc())) {
					throw new AplicacaoException("Data inválida, deve estar entre o ano 2000 e ano 2100");
				}

				verificaDocumento(exDocumentoDTO.getDoc());
			}

			ExMobil mobilAutuado = null;
			if (exDocumentoDTO.getIdMobilAutuado() != null) {
				mobilAutuado = dao().consultar(
						exDocumentoDTO.getIdMobilAutuado(), ExMobil.class,
						false);
				exDocumentoDTO.getDoc().setExMobilAutuado(mobilAutuado);
			}

			// Insere PDF de documento capturado
			//
			if (arquivo != null) {
				ExDocumento d = exDocumentoDTO.getDoc();

				if (arquivo.getFile() == null) {
					throw new AplicacaoException(
							"O arquivo a ser anexado não foi selecionado!");
				}

				Integer numBytes = null;
				try {
					final byte[] baArquivo = toByteArray(arquivo);
					if (baArquivo == null) {
						throw new AplicacaoException(
								"Arquivo vazio não pode ser anexado.");
					}
					numBytes = baArquivo.length;
					if (numBytes > 10 * 1024 * 1024) {
						throw new AplicacaoException(
								"Não é permitida a anexação de arquivos com mais de 10MB.");
					}
					d.setConteudoBlobPdf(baArquivo);
					d.setConteudoBlobHtml(null);
				} catch (IOException e) {
					throw new AplicacaoException("Falha ao manipular aquivo",
							1, e);
				}

				Integer numPaginas = d.getContarNumeroDePaginas();
				if (numPaginas == null || d.getArquivoComStamp() == null) {
					throw new AplicacaoException(
							MessageFormat
									.format("O arquivo {0} está corrompido ou protegido por senha. Favor gera-lo novamente antes de anexar.",
											arquivo.getFileName()));
				}

				// if (numPaginas != null && numBytes != null &&
				// (numBytes/numPaginas > (1 * 1024 * 1024))) {
				// throw new
				// AplicacaoException("Não é permitida a anexação de arquivos com mais de 1MB por página.");
				// }
			}

			exBL.gravar(getCadastrante(), getTitular(), getLotaTitular(),
					exDocumentoDTO.getDoc());
			
			
			/*
			 * alteracao para adicionar a movimentacao de insercao de substituto
			 */
			/*
			if(exDocumentoDTO.getDoc().getTitular() != temp.getDoc().getTitular() && !isNovo) {
				
				final ExMovimentacao mov = new ExMovimentacao();
				mov.setCadastrante(exDocumentoDTO.getDoc().getCadastrante());
				mov.setLotaCadastrante(exDocumentoDTO.getDoc().getLotaCadastrante());
				mov.setDtMov(dao().dt());
				mov.setDtIniMov(dao().dt());
				mov.setExMobil(exDocumentoDTO.getDoc().getMobilGeral());
				mov.setExTipoMovimentacao(dao().consultar(ExTipoMovimentacao.TIPO_MOVIMENTACAO_SUBSTITUICAO_RESPONSAVEL,ExTipoMovimentacao.class, false));
				mov.setLotaTitular(exDocumentoDTO.getDoc().getLotaTitular());
						
						
				String principal = ContextoPersistencia.getUserPrincipal();
				if (principal != null) {
					CpIdentidade identidade = dao().consultaIdentidadeCadastrante(principal, true);
					mov.setAuditIdentidade(identidade);
				}
				RequestInfo ri = CurrentRequest.get();
				if (ri != null) {
					mov.setAuditIP(HttpRequestUtils.getIpAudit(ri.getRequest()));
				}
				
				dao.gravar(mov);
			}
			*/
			/*
			 * fim da alteracao
			 */
			

			if (!exDocumentoDTO.getDoc().isFinalizado()
					&& (exDocumentoDTO.getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_CAPTURADO || exDocumentoDTO
							.getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_CAPTURADO))
				exBL.finalizar(getCadastrante(), getLotaTitular(),
						exDocumentoDTO.getDoc());

			lerEntrevista(exDocumentoDTO);

			if ("sim".equals(exDocumentoDTO.getDesativarDocPai())) {
				exDocumentoDTO
						.setDesativ("&exDocumentoDTO.desativarDocPai=sim");
			}

			try {
				exBL.incluirCosignatariosAutomaticamente(getCadastrante(),
						getLotaTitular(), exDocumentoDTO.getDoc());
			} catch (Exception e) {
				throw new AplicacaoException(
						"Erro ao tentar incluir os cosignatários deste documento",
						0, e);
			}

		} catch (final Exception e) {
			throw new AplicacaoException("Erro na gravação", 0, e);
		}

		if (param("ajax") != null && param("ajax").equals("true")) {
			final String body = MessageFormat.format("OK_{0}_{1}",
					exDocumentoDTO.getDoc().getSigla(), exDocumentoDTO.getDoc()
							.getDtRegDocDDMMYY());
			result.use(Results.http()).body(body);
		} else {
			final String url = MessageFormat.format(
					"exibir?sigla={0}{1}",
					exDocumentoDTO.getDoc().getSigla(),
					exDocumentoDTO.getDesativ() == null ? "" : exDocumentoDTO
							.getDesativ());
			result.redirectTo(url);
		}
	}

	@Post("app/expediente/doc/gravarpreench")
	public void aGravarPreenchimento(final ExDocumentoDTO exDocumentoDTO,
			final String[] vars, final String[] campos) throws IOException,
			IllegalAccessException, InvocationTargetException {
		assertAcesso("");

		ModeloDao.iniciarTransacao();
		final ExPreenchimento exPreenchimento = new ExPreenchimento();

		final DpLotacao provLota = getLotaTitular();
		final ExModelo provMod = new ExModelo();
		provMod.setIdMod(exDocumentoDTO.getIdMod());

		exPreenchimento.setDpLotacao(provLota);
		exPreenchimento.setExModelo(provMod);
		exPreenchimento.setNomePreenchimento(exDocumentoDTO
				.getNomePreenchimento());

		exPreenchimento.setPreenchimentoBA(getByteArrayFormPreenchimento(vars,
				campos));
		dao().gravar(exPreenchimento);
		ModeloDao.commitTransacao();

		exDocumentoDTO.setPreenchimento(exPreenchimento.getIdPreenchimento());

		result.forwardTo(this).aCarregarPreenchimento(exDocumentoDTO, vars);
	}

	@Post("app/expediente/doc/prever")
	public void preve(final ExDocumentoDTO exDocumentoDTO, final String[] vars)
			throws IllegalAccessException, InvocationTargetException,
			IOException {
		assertAcesso("");

		final boolean isDocNovo = (exDocumentoDTO == null);
		buscarDocumentoOuNovo(true, exDocumentoDTO);
		if (exDocumentoDTO.getDoc() != null) {
			if (isDocNovo) {
				escreverForm(exDocumentoDTO);
			} else {
				lerForm(exDocumentoDTO, vars);
			}
		} else {
			exDocumentoDTO.setDoc(new ExDocumento());
			lerForm(exDocumentoDTO, vars);
		}

		carregarBeans(exDocumentoDTO, exDocumentoDTO.getMobilPaiSel());

		if (exDocumentoDTO.getIdMob() != null) {
			exDocumentoDTO.setModelo(dao().consultar(exDocumentoDTO.getIdMob(),
					ExModelo.class, false));
		}

		if (param("processar_modelo") != null) {
			result.forwardTo(this).processa_modelo(exDocumentoDTO);
		} else {
			result.include("par", getRequest().getParameterMap());
			result.include("modelo", exDocumentoDTO.getModelo());
			result.include("nmArqMod", exDocumentoDTO.getModelo().getNmArqMod());
			result.include("doc", exDocumentoDTO.getDoc());
		}
	}

	public void processa_modelo(final ExDocumentoDTO exDocumentoDTO) {
		result.include("par", getRequest().getParameterMap());
		result.include("modelo", exDocumentoDTO.getModelo());
		result.include("nmArqMod", exDocumentoDTO.getModelo().getNmArqMod());
		result.include("doc", exDocumentoDTO.getDoc());
	}

	@Post("app/expediente/doc/preverPdf")
	public Download aPreverPdf(final ExDocumentoDTO exDocumentoDTO,
			final String[] vars) throws IOException, IllegalAccessException,
			InvocationTargetException {
		assertAcesso("");

		final boolean isDocNovo = (exDocumentoDTO == null);
		buscarDocumentoOuNovo(true, exDocumentoDTO);
		if (exDocumentoDTO.getDoc() != null) {
			if (isDocNovo) {
				escreverForm(exDocumentoDTO);
			} else {
				lerForm(exDocumentoDTO, vars);
			}
		} else {
			exDocumentoDTO.setDoc(new ExDocumento());
			lerForm(exDocumentoDTO, vars);
		}

		carregarBeans(exDocumentoDTO, exDocumentoDTO.getMobilPaiSel());

		if (exDocumentoDTO.getIdMob() != null) {
			exDocumentoDTO.setModelo(dao().consultar(exDocumentoDTO.getIdMob(),
					ExModelo.class, false));
		}

		Ex.getInstance().getBL()
				.processar(exDocumentoDTO.getDoc(), false, false);

		exDocumentoDTO.setPdfStreamResult(new ByteArrayInputStream(
				exDocumentoDTO.getDoc().getConteudoBlobPdf()));

		return new InputStreamDownload(exDocumentoDTO.getPdfStreamResult(),
				"application/pdf", "document.pdf");
	}

	@Get("app/expediente/doc/refazer")
	public void refazer(final String sigla) {
		assertAcesso("");

		final ExDocumentoDTO exDocumentoDTO = new ExDocumentoDTO();
		exDocumentoDTO.setSigla(sigla);
		this.buscarDocumento(true, exDocumentoDTO);

		if (!Ex.getInstance()
				.getComp()
				.podeRefazer(getTitular(), getLotaTitular(),
						exDocumentoDTO.getMob()))
			throw new AplicacaoException("Não é possível refazer o documento");

		exDocumentoDTO.setDoc(Ex
				.getInstance()
				.getBL()
				.refazer(getCadastrante(), getLotaTitular(),
						exDocumentoDTO.getDoc()));
		ExDocumentoController.redirecionarParaExibir(result, exDocumentoDTO
				.getDoc().getSigla());
	}

	@Get("app/expediente/doc/duplicar")
	public void aDuplicar(final boolean conviteEletronico, final String sigla) {
		assertAcesso("");

		final ExDocumentoDTO exDocumentoDto = new ExDocumentoDTO();
		exDocumentoDto.setSigla(sigla);
		buscarDocumento(false, exDocumentoDto);
		if (!Ex.getInstance()
				.getComp()
				.podeDuplicar(getTitular(), getLotaTitular(),
						exDocumentoDto.getMob())) {
			throw new AplicacaoException("Não é possível duplicar o documento");
		}
		exDocumentoDto.setDoc(Ex
				.getInstance()
				.getBL()
				.duplicar(getCadastrante(), getLotaTitular(),
						exDocumentoDto.getDoc()));
		result.redirectTo("exibir?sigla=" + exDocumentoDto.getDoc().getCodigo());
	}

	@Get("/app/expediente/doc/tornarDocumentoSemEfeito")
	public void tornarDocumentoSemEfeito(final String sigla) throws Exception {
		assertAcesso("");

		final ExDocumentoDTO exDocumentoDto = new ExDocumentoDTO();
		exDocumentoDto.setSigla(sigla);
		buscarDocumento(false, exDocumentoDto);

		result.include("sigla", sigla);
		result.include("id", exDocumentoDto.getId());
		result.include("mob", exDocumentoDto.getMob());
		result.include("titularSel", new DpPessoaSelecao());
		result.include("descrMov", exDocumentoDto.getDescrMov());
		result.include("doc", exDocumentoDto.getDoc());
	}
	
	@Get("/app/expediente/doc/cancelarDocumento")
	public void cancelarDocumento(final String sigla) throws Exception {
		assertAcesso("");

		final ExDocumentoDTO exDocumentoDto = new ExDocumentoDTO();
		exDocumentoDto.setSigla(sigla);
		buscarDocumento(false, exDocumentoDto);
		
		Ex.getInstance()
		.getBL()
		.cancelarDocumento(exDocumentoDto.getMob().doc().getTitular(),
				exDocumentoDto.getMob().doc().getLotaTitular(), exDocumentoDto.getMob().doc());		
		
		result.include("sigla", sigla);
		result.include("id", exDocumentoDto.getId());
		result.include("mob", exDocumentoDto.getMob());
		result.include("titularSel", new DpPessoaSelecao());
		result.include("descrMov", exDocumentoDto.getDescrMov());
		result.include("doc", exDocumentoDto.getDoc());
	
	}
	
	@Get("/app/expediente/doc/gerarProtocolo")
	public void gerarProtocolo(final String sigla) {
		assertAcesso("");

		final ExDocumentoDTO exDocumentoDto = new ExDocumentoDTO();
		exDocumentoDto.setSigla(sigla);
		buscarDocumento(false, exDocumentoDto);
		
		final Ex ex = Ex.getInstance();
		final ExBL exBL = ex.getBL();
		
		ExProtocolo prot = new ExProtocolo();
		prot = exBL.obterProtocolo(exDocumentoDto.getDoc());
		
		if(prot == null) {
			try {
				prot = exBL.gerarProtocolo(exDocumentoDto.getDoc(), getCadastrante(), getLotaCadastrante());
			} catch (Exception e) {
				throw new AplicacaoException(
						"Ocorreu um erro ao gerar protocolo.");
			}
		}
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(prot.getData());
		
		String url = SigaBaseProperties.getString("siga.ex."
				+ SigaBaseProperties.getString("siga.ambiente") + ".url") + "/processoautenticar?n=" + prot.getCodigo();
		
		result.include("url", url);
		result.include("ano", c.get(Calendar.YEAR));
		result.include("dataHora", df.format(c.getTime()));
		result.include("protocolo", prot);
		result.include("sigla", sigla);
		result.include("doc", exDocumentoDto.getDoc());
	}

	@Post("/app/expediente/doc/tornarDocumentoSemEfeitoGravar")
	public void tornarDocumentoSemEfeitoGravar(final String sigla,
			final Integer id, final DpPessoaSelecao titularSel,
			final String descrMov) throws Exception {
		assertAcesso("");

		if (descrMov == null || descrMov.trim().length() == 0) {
			throw new AplicacaoException(
					"O preenchimento do campo MOTIVO é obrigatório!");
		}
		final ExDocumentoDTO exDocumentoDto = new ExDocumentoDTO();
		exDocumentoDto.setSigla(sigla);
		buscarDocumento(Boolean.TRUE, exDocumentoDto);

		ExMobil mob = exDocumentoDto.getMob();
		ExDocumento doc = exDocumentoDto.getDoc();

		if (!Ex.getInstance()
				.getComp()
				.podeTornarDocumentoSemEfeito(getTitular(), getLotaTitular(),
						mob))
			throw new AplicacaoException(
					"Não é possível tornar documento sem efeito.");
		try {			
			Ex.getInstance()
					.getBL()
					.TornarDocumentoSemEfeito(getCadastrante(),
							getLotaTitular(), doc, descrMov);
		} catch (final Exception e) {
			throw e;
		}
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	private void carregarBeans(final ExDocumentoDTO exDocumentoDTO,
			final ExMobilSelecao mobilPaiSel) {
		setPar(getRequest().getParameterMap());
		ExMobil mobPai = null;

		// Questões referentes a doc pai-----------------------------
		if (exDocumentoDTO.getDoc().getIdDoc() == null) {
			String req = "nao";
			if (getPar().get("reqexDocumentoDTO.documentoRefSel") != null) {
				req = getPar().get("reqexDocumentoDTO.mobilPaiSel")[0]
						.toString();
			}

			if (getPar().get("reqdocumentoRefSel") != null) {
				req = getPar().get("reqmobilPaiSel")[0].toString();
			}

			if ((mobilPaiSel != null) && (mobilPaiSel.getSigla() != null)) {
				exDocumentoDTO.getMobilPaiSel()
						.setSigla(mobilPaiSel.getSigla());
			}

			exDocumentoDTO.getMobilPaiSel().buscar();
			if ((exDocumentoDTO.getMobilPaiSel().getId() != null)
					|| (req.equals("sim"))) {

				if (exDocumentoDTO.getMobilPaiSel().getId() != null) {
					// Documento Pai
					mobPai = daoMob(exDocumentoDTO.getMobilPaiSel().getId());

					final Long idMod = paramLong("exDocumentoDTO.idMod");
					if (idMod != null && exDocumentoDTO.getCriandoSubprocesso())
						exDocumentoDTO.setIdMod(exDocumentoDTO.getMobilPaiSel()
								.getObjeto().getDoc().getExModelo().getId());

					if (exDocumentoDTO.getClassificacaoSel() != null
							&& exDocumentoDTO.getClassificacaoSel().getId() == null) {
						exDocumentoDTO.getClassificacaoSel().setId(
								mobPai.doc().getExClassificacaoAtual().getId());
					}

					exDocumentoDTO.setDescrDocumento(mobPai.doc()
							.getDescrDocumento());

					exDocumentoDTO.setDesativarDocPai("sim");
				}
			}

			if (exDocumentoDTO.getAutuando()
					&& exDocumentoDTO.getIdMobilAutuado() != null) {
				final ExMobil mobilAutuado = daoMob(exDocumentoDTO
						.getIdMobilAutuado());

				exDocumentoDTO.getDoc().setExMobilAutuado(mobilAutuado);

				exDocumentoDTO.getClassificacaoSel().setId(
						mobilAutuado.getDoc().getExClassificacao().getId());
				exDocumentoDTO.setDescrDocumento(mobilAutuado.getDoc()
						.getDescrDocumento());
			}
		}
		// Fim das questões referentes a doc pai--------------------

		final Long idMod = exDocumentoDTO.getIdMod();
		ExModelo mod = null;
		if (exDocumentoDTO.getIdMod() != null && exDocumentoDTO.getIdMod() != 0) {
			mod = dao().consultar(exDocumentoDTO.getIdMod(), ExModelo.class,
					false);
			if (mod != null) {
				mod = mod.getModeloAtual();
			}
		}

		if (getPreenchimentos(exDocumentoDTO).size() <= 1) {
			exDocumentoDTO.setPreenchimento(0L);
		}

		if (mod != null && exDocumentoDTO.isAlterouModelo()) {
			if (exDocumentoDTO.getIdTpDoc() != null) {
				boolean permitido = false;
				for (ExTipoDocumento tp : mod.getExFormaDocumento()
						.getExTipoDocumentoSet()) {
					if (exDocumentoDTO.getIdTpDoc().equals(tp.getId())) {
						permitido = true;
						break;
					}
				}
				if (!permitido)
					exDocumentoDTO.setIdTpDoc(null);
			}
			if (exDocumentoDTO.getIdTpDoc() == null) {
				for (ExTipoDocumento tp : mod.getExFormaDocumento()
						.getExTipoDocumentoSet()) {
					exDocumentoDTO.setIdTpDoc(tp.getId());
					break;
				}
			}

			boolean naLista = false;
			final Set<ExPreenchimento> preenchimentos = getPreenchimentos(exDocumentoDTO);
			if (preenchimentos != null && preenchimentos.size() > 0) {
				for (final ExPreenchimento exp : preenchimentos) {
					if (exp.getIdPreenchimento().equals(
							exDocumentoDTO.getPreenchimento())) {
						naLista = true;
						break;
					}
				}
				if (!naLista) {
					exDocumentoDTO
							.setPreenchimento(((ExPreenchimento) (preenchimentos
									.toArray())[0]).getIdPreenchimento());
				}
			}

			exDocumentoDTO.setIdMod(mod.getIdMod());
			if ((exDocumentoDTO.getIdMod() != 0)
					&& (exDocumentoDTO.getMobilPaiSel() == null || exDocumentoDTO
							.getMobilPaiSel().getId() == null)
					&& (exDocumentoDTO.getIdMobilAutuado() == null)) {
				if (exDocumentoDTO.getClassificacaoSel() != null)
					exDocumentoDTO.getClassificacaoSel().apagar();
			}
			
			if(exDocumentoDTO.getSubscritorSel() != null && exDocumentoDTO.getSubscritorSel().getId() == null) {
				exDocumentoDTO.getSubscritorSel().buscarPorObjeto(getCadastrante());
			}
		}

		exDocumentoDTO.setModelo(mod);
		if (mod != null && mod.getExClassificacao() != null) {
			exDocumentoDTO.getClassificacaoSel().buscarPorObjeto(
					mod.getExClassificacao());
		}
	}

	private void escreverForm(final ExDocumentoDTO exDocumentoDTO)
			throws IllegalAccessException, InvocationTargetException {
		final ExDocumento doc = exDocumentoDTO.getDoc();

		// Destino , Origem
		final DpLotacao backupLotaTitular = getLotaTitular();
		final DpPessoa backupTitular = getTitular();
		final DpPessoa backupCadastrante = getCadastrante();

		BeanUtils.copyProperties(exDocumentoDTO, doc);

		setTitular(backupTitular);
		setLotaTitular(backupLotaTitular);
		// Orlando: Inclusão da linha, abaixo, para preservar o cadastrante do
		// ambiente.
		setCadastrante(backupCadastrante);

		if (doc.getConteudoBlob("doc.htm") != null) {
			exDocumentoDTO.setConteudo(new String(doc
					.getConteudoBlob("doc.htm")));
		}

		exDocumentoDTO.setIdTpDoc(doc.getExTipoDocumento().getIdTpDoc());
		exDocumentoDTO.setNivelAcesso(doc.getIdExNivelAcesso());

		final ExClassificacao classif = doc.getExClassificacaoAtual();
		if (classif != null && classif.getAtual() != null) {
			exDocumentoDTO.getClassificacaoSel().buscarPorObjeto(
					classif.getAtual());
		}
		exDocumentoDTO.getSubscritorSel().buscarPorObjeto(doc.getSubscritor());
		// form.getDestinatarioSel().buscarPorObjeto(doc.getDestinatario());
		if (doc.getExModelo() != null) {
			final ExModelo modeloAtual = doc.getExModelo().getModeloAtual();
			if (modeloAtual != null) {
				exDocumentoDTO.setIdMod(modeloAtual.getIdMod());
			}
		}

		if (doc.getDestinatario() != null) {
			exDocumentoDTO.getDestinatarioSel().buscarPorObjeto(
					doc.getDestinatario());
			exDocumentoDTO.setTipoDestinatario(1);
		}
		if (doc.getLotaDestinatario() != null) {
			exDocumentoDTO.getLotacaoDestinatarioSel().buscarPorObjeto(
					doc.getLotaDestinatario());
			if (doc.getDestinatario() == null) {
				exDocumentoDTO.setTipoDestinatario(2);
			}
		}

		if (doc.getExMobilPai() != null) {
			exDocumentoDTO.getMobilPaiSel()
					.buscarPorObjeto(doc.getExMobilPai());
		}

		if (doc.getTitular() != null
				&& doc.getSubscritor() != null
				&& !doc.getTitular().getIdPessoa()
						.equals(doc.getSubscritor().getIdPessoa())) {
			exDocumentoDTO.getTitularSel().buscarPorObjeto(doc.getTitular());
			exDocumentoDTO.setSubstituicao(true);
		}

		exDocumentoDTO.setPersonalizacao(doc.getNmFuncaoSubscritor() != null);

		// TODO Verificar se ha realmente a necessidade de setar novamente o
		// nível de acesso do documento
		// tendo em vista que o nível de acesso já foi setado anteriormente
		// neste mesmo método sem que o documento fosse alterado
		exDocumentoDTO.setNivelAcesso(doc.getIdExNivelAcesso());

		if (doc.getOrgaoExternoDestinatario() != null) {
			exDocumentoDTO.getOrgaoExternoDestinatarioSel().buscarPorObjeto(
					doc.getOrgaoExternoDestinatario());
			exDocumentoDTO.setTipoDestinatario(3);
		}
		if (doc.getNmOrgaoExterno() != null
				&& !doc.getNmOrgaoExterno().equals("")) {
			exDocumentoDTO.setTipoDestinatario(3);
		}
		if (doc.getNmDestinatario() != null) {
			exDocumentoDTO.setNmDestinatario(doc.getNmDestinatario());
			exDocumentoDTO.setTipoDestinatario(4);
		}

		if (doc.getOrgaoExterno() != null) {
			exDocumentoDTO.getCpOrgaoSel().buscarPorObjeto(
					doc.getOrgaoExterno());
			exDocumentoDTO.setObsOrgao(null);
			exDocumentoDTO.setTipoEmitente(1);
		}

		if (doc.getObsOrgao() != null) {
			exDocumentoDTO.getCpOrgaoSel().apagar();
			exDocumentoDTO.setObsOrgao(doc.getObsOrgao());
			exDocumentoDTO.setTipoEmitente(2);
		}

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			exDocumentoDTO.setDtDocString(df.format(doc.getDtDoc()));
		} catch (final Exception e) {
		}

		try {
			exDocumentoDTO.setDtDocOriginalString(df.format(doc
					.getDtDocOriginal()));
		} catch (final Exception e) {
		}

		if (doc.getAnoEmissao() != null) {
			exDocumentoDTO.setAnoEmissaoString(doc.getAnoEmissao().toString());
		}

		exDocumentoDTO.setEletronico(doc.isEletronico() ? 1 : 2);
	}

	private byte[] getByteArrayFormPreenchimento(final String[] vars,
			final String[] campos) throws IOException {
		final String[] aVars = vars;
		final String[] aCampos = campos;
		final ArrayList<String> aFinal = new ArrayList<String>();
		if (aVars != null && aVars.length > 0) {
			for (final String str : aVars) {
				aFinal.add(str);
			}
		}
		if (aCampos != null && aCampos.length > 0) {
			for (final String str : aCampos) {
				aFinal.add(str);
			}
		}
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			if (aFinal != null && aFinal.size() > 0) {
				for (final String par : aFinal) {
					String s = "exDocumentoDTO.".concat(par);
					if (param(s) == null) {
						s = par;
					}
					if (param(s) != null && !param(s).trim().equals("")
							&& !s.trim().equals("exDocumentoDTO.preenchimento")
							&& !param(s).matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")) {
						if (baos.size() > 0) {
							baos.write('&');
						}
						baos.write(par.getBytes());
						baos.write('=');

						// Deveria estar gravando como UTF-8
						baos.write(URLEncoder.encode(param(s), "iso-8859-1")
								.getBytes());
					}
				}
			}
			return baos.toByteArray();
		}
	}

	private void lerEntrevista(final ExDocumentoDTO exDocumentoDTO) {
		if (exDocumentoDTO.getDoc().getExModelo() != null) {
			final byte[] form = exDocumentoDTO.getDoc().getConteudoBlob(
					"doc.form");
			if (form != null) {
				final String as[] = new String(form).split("&");
				for (final String s : as) {
					final String param[] = s.split("=");
					try {
						if (param.length == 2) {
							exDocumentoDTO.getParamsEntrevista().put(param[0],
									URLDecoder.decode(param[1], "iso-8859-1"));
						}
					} catch (final UnsupportedEncodingException e) {
					}
					// System.out.println(s);
				}
			}
		}
	}

	private void lerForm(final ExDocumentoDTO exDocumentoDTO,
			final String[] vars) throws IOException {
		ExDocumento doc = exDocumentoDTO.getDoc();

		if (exDocumentoDTO.getAnexar()) {
			doc.setConteudoTpDoc(exDocumentoDTO.getConteudoTpDoc());
			doc.setNmArqDoc(exDocumentoDTO.getNmArqDoc());
		}

		doc.setDescrDocumento(exDocumentoDTO.getDescrDocumento());
		doc.setNmSubscritorExt(exDocumentoDTO.getNmSubscritorExt());
		doc.setNmFuncaoSubscritor(exDocumentoDTO.getNmFuncaoSubscritor());
		doc.setNumExtDoc(exDocumentoDTO.getNumExtDoc());
		doc.setNumAntigoDoc(exDocumentoDTO.getNumAntigoDoc());
		doc.setObsOrgao(exDocumentoDTO.getObsOrgao());
		doc.setEletronico(exDocumentoDTO.getEletronico() == 1 ? true : false);
		doc.setNmOrgaoExterno(exDocumentoDTO.getNmOrgaoExterno());
		doc.setDescrClassifNovo(exDocumentoDTO.getDescrClassifNovo());
		doc.setExNivelAcesso(dao().consultar(exDocumentoDTO.getNivelAcesso(),
				ExNivelAcesso.class, false));
		doc.setExTipoDocumento(dao().consultar(exDocumentoDTO.getIdTpDoc(),
				ExTipoDocumento.class, false));

		doc.setNmDestinatario(exDocumentoDTO.getNmDestinatario());

		doc.setExModelo(null);
		if (exDocumentoDTO.getIdMod() != 0) {
			ExModelo modelo = dao().consultar(exDocumentoDTO.getIdMod(),
					ExModelo.class, false);
			if (modelo != null) {
				doc.setExModelo(modelo.getModeloAtual());
				if (!doc.isFinalizado()) {
					doc.setExFormaDocumento(modelo.getExFormaDocumento());
				}
			}
		}

		if (exDocumentoDTO.getClassificacaoSel().getId() != null
				&& exDocumentoDTO.getClassificacaoSel().getId() != 0) {

			final ExClassificacao classificacao = dao().consultar(
					exDocumentoDTO.getClassificacaoSel().getId(),
					ExClassificacao.class, false);

			if (classificacao != null) {
				ExClassificacao cAtual = classificacao.getAtual();
				doc.setExClassificacao(cAtual);
			} else {
				doc.setExClassificacao(null);
				exDocumentoDTO.getClassificacaoSel().apagar();
			}

		} else {
			doc.setExClassificacao(null);
		}
		if (exDocumentoDTO.getCpOrgaoSel().getId() != null) {
			doc.setOrgaoExterno(dao().consultar(
					exDocumentoDTO.getCpOrgaoSel().getId(), CpOrgao.class,
					false));
		} else {
			doc.setOrgaoExterno(null);
		}

		// Orlando: Alterei o IF abaixo incluindo a instrução
		// "doc.setLotaCadastrante(getLotaTitular());".
		// Esta linha estava "solta",após o IF, e era executada sempre.
		// Fiz esta modificação porque esta linha alterava a lotação do
		// cadastrante, não permitindo que este,
		// ao preencher o campo subscritor com a matrícula de outro usuário,
		// tivesse acesso ao documento.

		if (doc.getCadastrante() == null) {
			doc.setCadastrante(getCadastrante());
			doc.setLotaCadastrante(getLotaTitular());
		}

		if (doc.getLotaCadastrante() == null) {
			doc.setLotaCadastrante(doc.getCadastrante().getLotacao());
		}
		if (exDocumentoDTO.getSubscritorSel().getId() != null) {
			doc.setSubscritor(daoPes(exDocumentoDTO.getSubscritorSel().getId()));
			doc.setLotaSubscritor(doc.getSubscritor().getLotacao());
		} else {
			doc.setSubscritor(null);
		}

		if (exDocumentoDTO.isSubstituicao()) {
			if (exDocumentoDTO.getTitularSel().getId() != null) {
				doc.setTitular(daoPes(exDocumentoDTO.getTitularSel().getId()));
				doc.setLotaTitular(doc.getTitular().getLotacao());
			} else {
				doc.setTitular(doc.getSubscritor());
				doc.setLotaTitular(doc.getLotaSubscritor());
			}
		} else {
			doc.setTitular(doc.getSubscritor());
			doc.setLotaTitular(doc.getLotaSubscritor());
		}

		if (exDocumentoDTO.getDestinatarioSel().getId() != null) {
			doc.setDestinatario(daoPes(exDocumentoDTO.getDestinatarioSel()
					.getId()));
			doc.setLotaDestinatario(daoPes(
					exDocumentoDTO.getDestinatarioSel().getId()).getLotacao());
			doc.setOrgaoExternoDestinatario(null);
		} else {
			doc.setDestinatario(null);
			if (exDocumentoDTO.getLotacaoDestinatarioSel().getId() != null) {
				doc.setLotaDestinatario(daoLot(exDocumentoDTO
						.getLotacaoDestinatarioSel().getId()));
				doc.setOrgaoExternoDestinatario(null);
			} else {
				doc.setLotaDestinatario(null);

				if (exDocumentoDTO.getOrgaoExternoDestinatarioSel().getId() != null) {
					doc.setOrgaoExternoDestinatario(dao().consultar(
							exDocumentoDTO.getOrgaoExternoDestinatarioSel()
									.getId(), CpOrgao.class, false));

				} else {
					doc.setOrgaoExternoDestinatario(null);
				}
			}
		}

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			doc.setDtDoc(df.parse(exDocumentoDTO.getDtDocString()));
		} catch (final ParseException e) {
			doc.setDtDoc(null);
		} catch (final NullPointerException e) {
			doc.setDtDoc(null);
		}
		if (doc.getDtRegDoc() == null)
			doc.setDtRegDoc(dao().dt());

		try {
			doc.setDtDocOriginal(df.parse(exDocumentoDTO
					.getDtDocOriginalString()));
		} catch (final ParseException e) {
			doc.setDtDocOriginal(null);
		} catch (final NullPointerException e) {
			doc.setDtDocOriginal(null);
		}

		if (exDocumentoDTO.getNumExpediente() != null) {
			doc.setNumExpediente(new Long(exDocumentoDTO.getNumExpediente()));
			doc.setAnoEmissao(new Long(exDocumentoDTO.getAnoEmissaoString()));
		}

		if (exDocumentoDTO.getMobilPaiSel().getId() != null) {
			doc.setExMobilPai(dao().consultar(
					exDocumentoDTO.getMobilPaiSel().getId(), ExMobil.class,
					false));
		} else {
			doc.setExMobilPai(null);
		}

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			final String marcacoes[] = { "<!-- INICIO NUMERO -->",
					"<!-- FIM NUMERO -->", "<!-- INICIO NUMERO",
					"FIM NUMERO -->", "<!-- INICIO TITULO", "FIM TITULO -->",
					"<!-- INICIO MIOLO -->", "<!-- FIM MIOLO -->",
					"<!-- INICIO CORPO -->", "<!-- FIM CORPO -->",
					"<!-- INICIO CORPO", "FIM CORPO -->",
					"<!-- INICIO ASSINATURA -->", "<!-- FIM ASSINATURA -->",
					"<!-- INICIO ABERTURA -->", "<!-- FIM ABERTURA -->",
					"<!-- INICIO ABERTURA", "FIM ABERTURA -->",
					"<!-- INICIO FECHO -->", "<!-- FIM FECHO -->" };

			final String as[] = vars;
			if (as != null && as.length > 0) {
				for (final String s : as) {
					if (baos.size() > 0)
						baos.write('&');
					baos.write(s.getBytes());
					baos.write('=');
					if (param(s) != null) {
						String parametro = param(s);
						for (final String m : marcacoes) {
							if (parametro.contains(m))
								parametro = parametro.replaceAll(m, "");
						}
						if (!FuncoesEL.contemTagHTML(parametro)) {
							if (parametro.contains("\"")) {
								parametro = parametro.replace("\"", "&quot;");
								setParam(s, parametro);
							}
						}

						baos.write(URLEncoder.encode(parametro, "iso-8859-1")
								.getBytes());
					}
				}
				doc.setConteudoTpDoc("application/zip");
				doc.setConteudoBlobForm(baos.toByteArray());
			}
		}
	}

	private List<ExModelo> getModelos(final ExDocumentoDTO exDocumentoDTO) {
		if (exDocumentoDTO.getModelos() != null) {
			return exDocumentoDTO.getModelos();
		}

		ExTipoDocumento tipo = null;
		// if (exDocumentoDTO.getIdTpDoc() != null
		// && exDocumentoDTO.getIdTpDoc() != 0) {
		// tipo = dao().consultar(exDocumentoDTO.getIdTpDoc(),
		// ExTipoDocumento.class, false);
		// }
		String headerValue = null;
		if (exDocumentoDTO.getTipoDocumento() != null
				&& exDocumentoDTO.getTipoDocumento().equals("antigo")) {
			headerValue = "Não Informado";
		}

		ExMobil mobPai = null;
		if (exDocumentoDTO.getMobilPaiSel().buscarPorSigla())
			mobPai = exDocumentoDTO.getMobilPaiSel().buscarObjeto();
		
		boolean isEditandoAnexo = false;
		if (exDocumentoDTO.getMob().getExDocumento().getExMobilPai() != null 
				|| exDocumentoDTO.isCriandoAnexo()) {
			isEditandoAnexo = true;
			if(mobPai == null) {
	            mobPai = exDocumentoDTO.getDoc().getExMobilPai();   
	        }
		}
        
		exDocumentoDTO.setModelos(Ex
				.getInstance()
				.getBL()
				.obterListaModelos(tipo, null, isEditandoAnexo,
						exDocumentoDTO.getCriandoSubprocesso(), mobPai,
						headerValue, true, getTitular(), getLotaTitular(),
						exDocumentoDTO.getAutuando()));

		return exDocumentoDTO.getModelos();
	}

	private Set<ExPreenchimento> getPreenchimentos(
			final ExDocumentoDTO exDocumentoDTO) {
		if (exDocumentoDTO.getPreenchSet() != null) {
			return exDocumentoDTO.getPreenchSet();
		}

		exDocumentoDTO.setPreenchSet(new LinkedHashSet<ExPreenchimento>());

		ExPreenchimento preench = new ExPreenchimento();
		if (exDocumentoDTO.getIdMod() != null
				&& exDocumentoDTO.getIdMod() != 0L) {
			preench.setExModelo(dao().consultar(exDocumentoDTO.getIdMod(),
					ExModelo.class, false));
		}

		final List<DpLotacao> lotacaoSet = dao().listarPorIdInicialDpLotacao(getLotaTitular().getIdLotacaoIni());

		exDocumentoDTO.getPreenchSet().add(
				new ExPreenchimento(0, null, " [Em branco] ", null));

		if (exDocumentoDTO.getIdMod() != null && exDocumentoDTO.getIdMod() != 0) {
			for (final DpLotacao lotacao : lotacaoSet) {
				preench.setDpLotacao(lotacao);
				try {
					exDocumentoDTO.getPreenchSet().addAll(
							dao().consultar(preench));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return exDocumentoDTO.getPreenchSet();
	}

	private List<ExNivelAcesso> getListaNivelAcesso(
			final ExDocumentoDTO exDocumentoDTO) {
		ExClassificacao exClassif = new ExClassificacao();
		ExTipoDocumento exTipo = new ExTipoDocumento();
		ExModelo exMod = new ExModelo();

		if (exDocumentoDTO.getIdTpDoc() != null) {
			exTipo = dao().consultar(exDocumentoDTO.getIdTpDoc(),
					ExTipoDocumento.class, false);
		}

		if (exDocumentoDTO.getIdMod() != null && exDocumentoDTO.getIdMod() != 0) {
			exMod = dao().consultar(exDocumentoDTO.getIdMod(), ExModelo.class,
					false);
		}

		if (exDocumentoDTO.getClassificacaoSel().getId() != null) {
			exClassif = dao().consultar(
					exDocumentoDTO.getClassificacaoSel().getId(),
					ExClassificacao.class, false);
		}

		return getListaNivelAcesso(exTipo,
				exMod != null ? exMod.getExFormaDocumento() : null, exMod,
				exClassif);
	}

	private ExNivelAcesso getNivelAcessoDefault(
			final ExDocumentoDTO exDocumentoDTO) {
		ExClassificacao exClassif = new ExClassificacao();
		ExTipoDocumento exTipo = new ExTipoDocumento();
		ExModelo exMod = new ExModelo();

		if (exDocumentoDTO.getIdTpDoc() != null) {
			exTipo = dao().consultar(exDocumentoDTO.getIdTpDoc(),
					ExTipoDocumento.class, false);
		}

		if (exDocumentoDTO.getIdMod() != null) {
			exMod = dao().consultar(exDocumentoDTO.getIdMod(), ExModelo.class,
					false);
		}

		if (exDocumentoDTO.getClassificacaoSel().getId() != null) {
			exClassif = dao().consultar(
					exDocumentoDTO.getClassificacaoSel().getId(),
					ExClassificacao.class, false);
		}

		return getNivelAcessoDefault(exTipo,
				exMod != null ? exMod.getExFormaDocumento() : null, exMod,
				exClassif);
	}

	protected static void redirecionarParaExibir(final Result result,
			final String sigla) {
		result.redirectTo(MessageFormat.format(URL_EXIBIR, sigla));
	}

	protected static void redirecionarParaEditar(final Result result,
			final String sigla) {
		result.redirectTo(MessageFormat.format(URL_EDITAR, sigla));
	}

	@Get("/app/expediente/doc/pdf")
	public void aAcessar(final String sigla) throws Exception {
		assertAcesso("");

		final ExDocumentoDTO dto = new ExDocumentoDTO();
		dto.setSigla(sigla);
		buscarDocumento(false, dto);

		assertAcesso(dto);

		result.redirectTo("/app/expediente/doc/".concat(
				dto.getMob().getCodigoCompacto()).concat(".pdf"));
	}

	@Get("/app/expediente/doc/corrigir_arquivamentos_volume")
	public void aCorrigirArquivamentosVolume(Integer de, Integer ate,
			Boolean efetivar) throws Exception {
		assertAcesso("");

		int idPrimeiroDoc, idUltimoDoc;
		Boolean efetivarDoc = false;
		try {
			idPrimeiroDoc = de;
		} catch (Exception e) {
			idPrimeiroDoc = 1;
		}
		try {
			idUltimoDoc = ate;
		} catch (Exception e) {
			idUltimoDoc = 999999999;
		}
		try {
			if (efetivar == null) {
				efetivarDoc = false;
			} else {
				efetivarDoc = efetivar;
			}
		} catch (Exception e) {
			efetivarDoc = false;
		}
		Ex.getInstance()
				.getBL()
				.corrigirArquivamentosEmVolume(idPrimeiroDoc, idUltimoDoc,
						efetivarDoc);
	}

}
