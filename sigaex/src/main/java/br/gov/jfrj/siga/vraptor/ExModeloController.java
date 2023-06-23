package br.gov.jfrj.siga.vraptor;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.kxml2.io.KXmlSerializer;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.ByteArrayDownload;
import br.com.caelum.vraptor.observer.download.Download;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.CpModelo;
import br.gov.jfrj.siga.cp.model.DpCargoSelecao;
import br.gov.jfrj.siga.cp.model.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.model.enm.CpExtensoesDeArquivoEnum;
import br.gov.jfrj.siga.persistencia.ExModeloDaoFiltro;

import static br.com.caelum.vraptor.view.Results.json;

@Controller
public class ExModeloController extends ExSelecionavelController {

	private static final String SUBDIRETORIO = "-subdiretorio-";
	private static final String VERIFICADOR_ACESSO = "MOD:Gerenciar modelos";
	private static final String UTF8 = "utf-8";
	private static final Logger LOGGER = Logger.getLogger(ExModeloController.class);
	private boolean paraIncluir;
	private boolean paraAutuar;

	/**
	 * @deprecated CDI eyes only
	 */
	public ExModeloController() {
		super();
	}

	@Inject
	public ExModeloController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	@Get
	@Path({ "/app/modelo/buscar-json/{sigla}" })
	public void busca(String sigla) throws Exception {
		aBuscarJson(sigla);
	}

	@Get
	@Path({ "/app/modelo/buscar-json-para-incluir/{sigla}" })
	public void buscaParaIncluir(String sigla) throws Exception {
		this.paraIncluir = true;
		aBuscarJson(sigla);
	}

	@Get
	@Path({ "/app/modelo/buscar-json-para-autuar/{sigla}" })
	public void buscaParaAutuar(String sigla) throws Exception {
		this.paraAutuar = true;
		aBuscarJson(sigla);
	}

	@Get
	@Path({ "/app/modelo/buscar-json-conteudo-para-comparar/{id}" })
	public void buscaConteudoParaComparar(Long id) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		ExModelo modelo = buscarModelo(id);
		final String conteudo = modelo.getConteudoBlobMod() != null ? new String(
				modelo.getConteudoBlobMod2(), UTF8) : null;
		result.use(json()).from(conteudo,"conteudoModelo").serialize();
	}

	@Get
	@Path({ "/app/modelo/buscar-json-lista-para-comparar/{id}" })
	public void buscaListaParaComparar(Long id) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		ExModelo modelo = buscarModelo(id).getModeloAtual();
		List<ExModelo> listaModeloHistorico =  new ArrayList(Arrays.asList(new Object[][]{{modelo.getId(),"[Versão Atual]"}}));
		listaModeloHistorico.addAll(listarModelosAnteriores(modelo.getIdInicial()));
		result.use(json()).from(listaModeloHistorico,"listaModeloHistorico").serialize();
	}

	@Get("app/modelo/listar")
	public void lista(final String script) throws Exception {
		try {
			assertAcesso(VERIFICADOR_ACESSO);
			List<ExModelo> modelos = dao().listarTodosModelosOrdenarPorNome(null, script);
			result.include("itens", modelos);
			result.include("script", script);
		} catch (AplicacaoException e) {
			throw new AplicacaoException(e.getMessage(), 0, e);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new AplicacaoException(ex.getMessage(), 0, ex);
		}
	}

	@Get("app/modelo/editar")
	public void edita(final Long id,final Integer postback) throws UnsupportedEncodingException {
		assertAcesso(VERIFICADOR_ACESSO);
		if (postback == null) {

			ExModelo modelo =  buscarModelo(id);

			if(id != null) modelo = modelo.getModeloAtual();

			String tipoModelo = modelo.getConteudoTpBlob();
			if (tipoModelo == null || tipoModelo.trim().length() == 0) {
				tipoModelo = "template/freemarker";
			}

			final ExClassificacaoSelecao classificacaoSel = new ExClassificacaoSelecao();
			final ExClassificacaoSelecao classificacaoCriacaoViasSel = new ExClassificacaoSelecao();

			classificacaoSel.buscarPorObjeto(modelo.getExClassificacao());
			classificacaoCriacaoViasSel.buscarPorObjeto(modelo.getExClassCriacaoVia());

			final String conteudo = modelo.getConteudoBlobMod() != null ? new String(modelo.getConteudoBlobMod2(), UTF8)
					: null;
			final Long forma = modelo.getExFormaDocumento() != null ? modelo.getExFormaDocumento().getIdFormaDoc()
					: null;
			final Long nivel = modelo.getExNivelAcesso() != null ? modelo.getExNivelAcesso().getIdNivelAcesso() : null;


			result.include("nome", modelo.getNmMod());
			result.include("classificacaoSel", classificacaoSel);
			result.include("classificacaoCriacaoViasSel", classificacaoCriacaoViasSel);
			result.include("pessoaSel", new DpPessoaSelecao());
			result.include("lotacaoSel", new DpLotacaoSelecao());
			result.include("cargoSel", new DpCargoSelecao());
			result.include("funcaoSel", new DpFuncaoConfiancaSelecao());
			result.include("forma", forma);
			result.include("listaForma", getListaForma());
			result.include("nivel", nivel);
			result.include("listaNivelAcesso", getListaNivelAcesso());
			result.include("tipoModelo", tipoModelo);
			result.include("conteudo", conteudo);
			result.include("descricao", modelo.getDescMod());
			result.include("arquivo", modelo.getNmArqMod());
			result.include("uuid", modelo.getUuid());
			result.include("diretorio", modelo.getNmDiretorio());
			result.include("marcaDagua", modelo.getMarcaDagua());
			result.include("extensoesArquivo", modelo.getExtensoesArquivo());
			result.include("listaExtensoes", CpExtensoesDeArquivoEnum.getList());
			result.include("id", modelo.getId());
			result.include("listaModeloHistorico", listarModelosAnteriores(modelo.getIdInicial()));
		}
	}

	@RequestParamsNotCheck
	@Transacional
	@Post("app/modelo/gravar")
	public void editarGravar(final Long id, final String nome, final String tipoModelo, final String conteudo,
			final ExClassificacaoSelecao classificacaoSel, final ExClassificacaoSelecao classificacaoCriacaoViasSel,
			final String descricao, final Long forma, final Long nivel, final String arquivo, final String diretorio,
			final String uuid, final String marcaDagua, final String[] extensoesArquivo, final Integer postback) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		ExModelo modelo = new ExModelo();
		final ExModelo modAntigo = buscarModeloAntigo(id);
		if (postback != null) {
			modelo.setNmMod(nome);
			modelo.setExClassificacao(classificacaoSel.buscarObjeto());
			modelo.setExClassCriacaoVia(classificacaoCriacaoViasSel.buscarObjeto());
			modelo.setConteudoTpBlob(tipoModelo);
			modelo.setDescMod(descricao);
			modelo.setNmArqMod(arquivo);
			modelo.setNmDiretorio(diretorio);
			modelo.setUuid(uuid);
			modelo.setMarcaDagua(marcaDagua);

			modelo.setIdInicial(modAntigo != null ? modAntigo.getIdInicial() : null);

			if (conteudo != null && conteudo.trim().length() > 0) {
				modelo.setConteudoBlobMod2(conteudo.getBytes(UTF8));
			}
			if (forma != null && forma != 0) {
				modelo.setExFormaDocumento(dao().consultar(forma, ExFormaDocumento.class, false));
				if (extensoesArquivo != null && !modelo.getExFormaDocumento().isCapturadoFormatoLivre()) {
					modelo.setExtensoesArquivo(null);
				} else {
					String extensoesArq = Arrays.toString(extensoesArquivo).replace("[", "")
							.replace("]", "").replace(" ", "");
					if (extensoesArq.length() > 200 || extensoesArquivo.length > 20)
						throw new AplicacaoException("Quantidade de extensões de arquivo maior que 20 "
								+ "ou excedeu o tamanho do campo na tabela de modelos");
					modelo.setExtensoesArquivo(extensoesArq);
				}
			}
			if (nivel != null && nivel != 0) {
				modelo.setExNivelAcesso(dao().consultar(nivel, ExNivelAcesso.class, false));
			}
		}

		Ex.getInstance().getBL().gravarModelo(modelo, modAntigo, null, getIdentidadeCadastrante());
		if ("Ok".equals(param("ok"))) {
			result.redirectTo(ExModeloController.class).lista(null);
		}else if ("Aplicar".equals(param("submit"))) {
			result.redirectTo("editar?id=" + (modelo.getId() != null ? modelo.getId() : id));
		} else {
			result.redirectTo("buscar-json-lista-para-comparar/" + (modelo.getId() != null ? modelo.getId() : id));
		}
	}

	@Transacional
	@Get("app/modelo/desativar")
	public void desativar(final Long id) throws Exception {
		ModeloDao.iniciarTransacao();
		assertAcesso(VERIFICADOR_ACESSO);
		if (id == null) {
			throw new AplicacaoException("ID não informada");
		}
		final ExModelo modelo = dao().consultar(id, ExModelo.class, false);
		dao().excluirComHistorico(modelo, dao().consultarDataEHoraDoServidor(), getIdentidadeCadastrante());
		ModeloDao.commitTransacao();

		result.redirectTo(ExModeloController.class).lista(null);
	}

	@Get("app/modelo/exportar")
	public Download exportar(HttpServletResponse response) throws Exception {
		final String modelo = "modelo";
		Map<String, Integer> mapNomes = new HashMap<>();

		assertAcesso(VERIFICADOR_ACESSO);

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ZipOutputStream zos = new ZipOutputStream(baos)) {

			final List<ExModelo> l = dao().listarTodosModelosOrdenarPorNome(null, null);

			for (final ExModelo m : l) {
				final KXmlSerializer serializer = new KXmlSerializer();
				try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
					serializer.setOutput(os, UTF8);
					serializer.startDocument(null, null);
					serializer.startTag(null, "modelo");
					if (m.getExFormaDocumento() != null) {
						serializer.attribute(null, "especie", m.getExFormaDocumento().getDescricao());
					}
					if (m.getNmMod() != null) {
						serializer.attribute(null, "nome", m.getNmMod());
					}
					if (m.getDescMod() != null) {
						serializer.attribute(null, "descricao", m.getDescMod());
					}
					if (m.getExClassificacao() != null) {
						serializer.attribute(null, "classificacao", m.getExClassificacao().getSigla());
					}
					if (m.getExClassCriacaoVia() != null) {
						serializer.attribute(null, "classCriacaoVia", m.getExClassCriacaoVia().getSigla());
					}
					if (m.getExNivelAcesso() != null) {
						serializer.attribute(null, "nivel", m.getExNivelAcesso().getNmNivelAcesso());
					}
					if (m.getNmArqMod() != null) {
						serializer.attribute(null, "arquivo", m.getNmArqMod());
					}
					if (m.getConteudoTpBlob() != null) {
						serializer.attribute(null, "tipo", m.getConteudoTpBlob());
					}
					if (m.getUuid() == null) {
						m.setUuid(UUID.randomUUID().toString());
						SigaTransacionalInterceptor.upgradeParaTransacional();
						dao().gravar(m);
					}
					if (m.getUuid() != null) {
						serializer.attribute(null, "uuid", m.getUuid());
					}
					if (m.getNmDiretorio() != null) {
						serializer.attribute(null, "diretorio", m.getNmDiretorio());
					}

					final byte[] template = m.getConteudoBlobMod2();
					if (template != null) {
						serializer.flush();
						os.write('\n');
						serializer.cdsect(new String(template, UTF8));
						serializer.flush();
						os.write('\n');
					}
					serializer.endTag(null, modelo);
					serializer.endDocument();
					serializer.flush();
					byte[] arq = os.toByteArray();

					String filename = Texto.slugify(
							(m.getNmDiretorio() != null ? m.getNmDiretorio().replace("/", SUBDIRETORIO) + SUBDIRETORIO
									: ""),
							true, false);
					if (filename == null)
						filename = "";
					filename = filename.replace(SUBDIRETORIO, "/");
					if (filename.length() > 0)
						filename += "/";
					filename += m.getSubdiretorioENome();
					if (mapNomes.containsKey(filename))
						mapNomes.put(filename, mapNomes.get(filename) + 1);
					else
						mapNomes.put(filename, 0);

					ZipEntry entry = new ZipEntry(filename
							+ (mapNomes.get(filename) == 0 ? "" : " (" + mapNomes.get(filename) + ")") + ".mod.xml");
					zos.putNextEntry(entry);
					zos.write(arq);
					zos.closeEntry();
				}
			}
			zos.flush();
			byte[] zipfile = baos.toByteArray();

			return new ByteArrayDownload(zipfile, "application/zip", "siga-doc-modelos.zip", true);
		}
	}

	@Get("app/modelo/exportarxml")
	public Download exportarXml(HttpServletResponse response) {
		final String modelos = "siga-doc-modelos";
		final String modeloGeral = "modelo-geral";
		final String modelo = "modelo";

		assertAcesso(VERIFICADOR_ACESSO);

		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {

			final KXmlSerializer serializer = new KXmlSerializer();
			serializer.setOutput(os, UTF8);
			serializer.startDocument(null, null);

			serializer.startTag(null, modelos);
			serializer.flush();
			os.write('\n');
			os.write('\n');

			final List<CpModelo> lCp = dao().listarModelosOrdenarPorNome(null);

			for (final CpModelo m : lCp) {
				serializer.startTag(null, modeloGeral);
				if (m.getCpOrgaoUsuario() != null) {
					serializer.attribute(null, "orgao", m.getCpOrgaoUsuario().getSigla());
				}
				final String template = m.getConteudoBlobString();
				if (template != null) {
					serializer.flush();
					os.write('\n');
					serializer.cdsect(template);
					serializer.flush();
					os.write('\n');
				}
				serializer.endTag(null, modeloGeral);
				serializer.flush();
				os.write('\n');
				os.write('\n');
			}

			serializer.flush();
			os.write('\n');
			os.write('\n');

			final List<ExModelo> l = dao().listarTodosModelosOrdenarPorNome(null, null);

			for (final ExModelo m : l) {
				serializer.startTag(null, modelo);
				if (m.getExFormaDocumento() != null) {
					serializer.attribute(null, "especie", m.getExFormaDocumento().getDescricao());
				}
				if (m.getNmMod() != null) {
					serializer.attribute(null, "nome", m.getNmMod());
				}
				if (m.getDescMod() != null) {
					serializer.attribute(null, "descricao", m.getDescMod());
				}
				if (m.getExClassificacao() != null) {
					serializer.attribute(null, "classificacao", m.getExClassificacao().getSigla());
				}
				if (m.getExClassCriacaoVia() != null) {
					serializer.attribute(null, "classCriacaoVia", m.getExClassCriacaoVia().getSigla());
				}
				if (m.getExNivelAcesso() != null) {
					serializer.attribute(null, "nivel", m.getExNivelAcesso().getNmNivelAcesso());
				}
				if (m.getNmArqMod() != null) {
					serializer.attribute(null, "arquivo", m.getNmArqMod());
				}
				if (m.getConteudoTpBlob() != null) {
					serializer.attribute(null, "tipo", m.getConteudoTpBlob());
				}
				if (m.getUuid() == null) {
					m.setUuid(UUID.randomUUID().toString());
					SigaTransacionalInterceptor.upgradeParaTransacional();
					dao().gravar(m);
				}
				if (m.getUuid() != null) {
					serializer.attribute(null, "uuid", m.getUuid());
				}
				final byte[] template = m.getConteudoBlobMod2();
				if (template != null) {
					serializer.flush();
					os.write('\n');
					serializer.cdsect(new String(template, UTF8));
					serializer.flush();
					os.write('\n');
				}
				serializer.endTag(null, modelo);
				serializer.flush();
				os.write('\n');
				os.write('\n');
			}

			serializer.endTag(null, modelos);

			serializer.endDocument();

			serializer.flush();

			return new ByteArrayDownload(os.toByteArray(), "application/xml", "dadosrh.xml");

		} catch (Exception e) {
			throw new AplicacaoException(e.getMessage(), 0, e);
		}
	}

	private ExModelo buscarModelo(final Long id) {
		if (id != null) {
			return Ex.getInstance().getBL().getCopia(dao().consultar(id, ExModelo.class, false));
		}
		return new ExModelo();
	}

	private ExModelo buscarModeloAntigo(final Long idInicial) {
		if (idInicial != null) {
			return dao().consultar(idInicial, ExModelo.class, false).getModeloAtual();
		}
		return null;
	}

	private List<ExNivelAcesso> getListaNivelAcesso() {
		return dao().listarOrdemNivel();
	}

	private List<ExFormaDocumento> getListaForma() {
		return dao().listarExFormasDocumento();
	}

	@Override
	protected DaoFiltroSelecionavel createDaoFiltro() {
		ExModeloDaoFiltro flt = new ExModeloDaoFiltro();
		flt.setSigla(getNome());
		return flt;
	}

	@Override
	protected String aBuscar(String sigla, String postback) throws Exception {
		String s = super.aBuscar(sigla, postback);

		if (paraIncluir) {
			List<ExModelo> lExcluir = new ArrayList<>();
			for (ExModelo mod : (List<ExModelo>) getItens()) {
				if (!Ex.getInstance().getConf().podePorConfiguracao(getTitular(), getLotaTitular(), mod,
						ExTipoDeConfiguracao.DESPACHAVEL)) {
					lExcluir.add(mod);
				}
			}
			getItens().removeAll(lExcluir);
		} else if (paraAutuar) {
			List<ExModelo> lExcluir = new ArrayList<>();
			for (ExModelo mod : (List<ExModelo>) getItens()) {
				if (!Ex.getInstance().getConf().podePorConfiguracao(getTitular(), getLotaTitular(), mod,
						ExTipoDeConfiguracao.AUTUAVEL)) {
					lExcluir.add(mod);
				}
			}
			getItens().removeAll(lExcluir);
		}
		return s;
	}

	private List<ExModelo> listarModelosAnteriores(final Long idInicial) {
		return dao().listarModelosAnterioresIdAndHisDtFim(idInicial);
	}
}
