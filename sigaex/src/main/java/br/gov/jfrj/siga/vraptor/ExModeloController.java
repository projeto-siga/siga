package br.gov.jfrj.siga.vraptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.kxml2.io.KXmlSerializer;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
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
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;
import br.gov.jfrj.siga.model.dao.ModeloDao;

@Resource
public class ExModeloController extends ExSelecionavelController {

	private static final String VERIFICADOR_ACESSO = "MOD:Gerenciar modelos";
	private static final String UTF8 = "utf-8";
	private static final Logger LOGGER = Logger.getLogger(ExModeloController.class);

	public ExModeloController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	@Get("app/modelo/listar")
	public void lista(final String script) throws Exception{
		try {
			assertAcesso(VERIFICADOR_ACESSO);
			List<ExModelo> modelos = dao().listarTodosModelosOrdenarPorNome(script);
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
	public void edita(final Long id, final Integer postback) throws UnsupportedEncodingException {
		assertAcesso(VERIFICADOR_ACESSO);
		if (postback == null) {
			ExModelo modelo = buscarModelo(id);

			String tipoModelo = modelo.getConteudoTpBlob();
			if (tipoModelo == null || tipoModelo.trim().length() == 0) {
				tipoModelo = "template-file/jsp";
			}

			final ExClassificacaoSelecao classificacaoSel = new ExClassificacaoSelecao();
			final ExClassificacaoSelecao classificacaoCriacaoViasSel = new ExClassificacaoSelecao();

			classificacaoSel.buscarPorObjeto(modelo.getExClassificacao());
			classificacaoCriacaoViasSel.buscarPorObjeto(modelo.getExClassCriacaoVia());

			final String conteudo = modelo.getConteudoBlobMod() != null ? new String(modelo.getConteudoBlobMod2(), UTF8) : null;
			final Integer forma = modelo.getExFormaDocumento() != null ? modelo.getExFormaDocumento().getIdFormaDoc() : null;
			final Long nivel = modelo.getExNivelAcesso() != null ? modelo.getExNivelAcesso().getIdNivelAcesso() : null;

			result.include("id", id);
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

		}
	}

	@Post("app/modelo/gravar")
	public void editarGravar(final Long id, final String nome, final String tipoModelo, final String conteudo, final ExClassificacaoSelecao classificacaoSel,
			final ExClassificacaoSelecao classificacaoCriacaoViasSel, final String descricao, final Integer forma, final Long nivel, final String arquivo,
			final Integer postback) throws Exception {
		assertAcesso(VERIFICADOR_ACESSO);
		ExModelo modelo = buscarModelo(id);
		if (postback != null) {
			modelo.setNmMod(nome);
			modelo.setExClassificacao(classificacaoSel.buscarObjeto());
			modelo.setExClassCriacaoVia(classificacaoCriacaoViasSel.buscarObjeto());
			modelo.setConteudoTpBlob(tipoModelo);
			modelo.setDescMod(descricao);
			modelo.setNmArqMod(arquivo);
			if (conteudo != null && conteudo.trim().length() > 0) {
				modelo.setConteudoBlobMod2(conteudo.getBytes(UTF8));
			}
			if (forma != null && forma != 0) {
				modelo.setExFormaDocumento(dao().consultar(forma, ExFormaDocumento.class, false));
			}
			if (nivel != null && nivel != 0) {
				modelo.setExNivelAcesso(dao().consultar(nivel, ExNivelAcesso.class, false));
			}
		}

		final ExModelo modAntigo = buscarModeloAntigo(modelo.getIdInicial());
		Ex.getInstance().getBL().gravarModelo(modelo, modAntigo, null, getIdentidadeCadastrante());
		if ("Aplicar".equals(param("submit"))) {
			result.redirectTo("editar?id=" + modelo.getId());
			return;
		}
		result.redirectTo(ExModeloController.class).lista(null);
	}

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
	public void exportar(HttpServletResponse response) {
		final String modelos = "siga-doc-modelos";
		final String modeloGeral = "modelo-geral";
		final String modelo = "modelo";

		try {
			assertAcesso(VERIFICADOR_ACESSO);

			final KXmlSerializer serializer = new KXmlSerializer();
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
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

			final List<ExModelo> l = dao().listarTodosModelosOrdenarPorNome(null);

			for (final ExModelo m : l) {
				serializer.startTag(null, modelo);
				if (m.getExFormaDocumento() != null) {
					serializer.attribute(null, "forma", m.getExFormaDocumento().getDescricao());
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

			final OutputStream out = response.getOutputStream();

			final ByteArrayInputStream bais = new ByteArrayInputStream(os.toByteArray());

			int i;
			while ((i = bais.read()) != -1) {
				out.write(i);
			}
			bais.close();
			out.close();
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
	public DaoFiltroSelecionavel createDaoFiltro() {
		return null;
	}

}
