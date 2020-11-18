package br.gov.jfrj.siga.vraptor;

import static br.gov.jfrj.siga.ex.ExMobil.adicionarIndicativoDeMovimentacaoComOrigemPeloBotaoDeRestricaoDeAcesso;
import static br.gov.jfrj.siga.ex.ExMobil.removerIndicativoDeMovimentacaoComOrigemPeloBotaoDeRestricaoDeAcesso;
import static com.google.common.base.Predicates.alwaysFalse;
import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Lists.newArrayList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.xerces.impl.dv.util.Base64;
import org.jboss.logging.Logger;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Contexto;
import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.base.DateUtils;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.SigaModal;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.base.TipoResponsavelEnum;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.CpToken;
import br.gov.jfrj.siga.cp.model.CpOrgaoSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExItemDestinacao;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExSituacaoConfiguracao;
import br.gov.jfrj.siga.ex.ExTipoDespacho;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.ExTopicoDestinacao;
import br.gov.jfrj.siga.ex.ItemDeProtocolo;
import br.gov.jfrj.siga.ex.ItemDeProtocoloComparator;
import br.gov.jfrj.siga.ex.bl.AcessoConsulta;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExAssinavelDoc;
import br.gov.jfrj.siga.ex.util.DatasPublicacaoDJE;
import br.gov.jfrj.siga.ex.util.PublicacaoDJEBL;
import br.gov.jfrj.siga.ex.vo.ExMobilVO;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.builder.BuscaDocumentoBuilder;
import br.gov.jfrj.siga.vraptor.builder.ExMovimentacaoBuilder;

@Controller
public class ExMovimentacaoController extends ExController {

	private static final String OPCAO_MOSTRAR = "mostrar";
	private static final int DEFAULT_TIPO_RESPONSAVEL = 1;
	private static final int DEFAULT_POSTBACK = 1;
	private static final Logger LOGGER = Logger
			.getLogger(ExMovimentacaoController.class);
	
	private static final int MAX_ITENS_PAGINA_TRAMITACAO_LOTE = 50;
	
	/**
	 * @deprecated CDI eyes only
	 */
	public ExMovimentacaoController() {
		super();
	}

	@Inject
	public ExMovimentacaoController(HttpServletRequest request,
			HttpServletResponse response, ServletContext context,
			Result result, SigaObjects so, EntityManager em, Validator validator) {

		super(request, response, context, result, ExDao.getInstance(), so, em);
	}

	private ExDocumento buscarDocumento(final BuscaDocumentoBuilder builder) {
		return buscarDocumento(builder, true);
	}

	private ExDocumento buscarDocumento(final BuscaDocumentoBuilder builder,
			final boolean verificarAcesso) {
		ExDocumento doc = builder.buscarDocumento(dao());
		if (verificarAcesso && builder.getMob() != null) {

			verificaNivelAcesso(builder.getMob());
		}

		return doc;
	}
	
	@Get("app/expediente/mov/anexar")
	public void anexa(final String sigla, final boolean assinandoAnexosGeral) {
		final BuscaDocumentoBuilder documentoBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(documentoBuilder);
		final ExMobil mob = documentoBuilder.getMob();

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia().setMob(mob);

		if (!(mob.isGeral() && mob.doc().isFinalizado())) {
			if (!Ex.getInstance().getComp()
					.podeAnexarArquivo(getTitular(), getLotaTitular(), mob)) {
				throw new AplicacaoException("Arquivo não pode ser anexado");
			}
		}

		final ExMobilVO mobilVO = new ExMobilVO(mob, getCadastrante(), getTitular(),
				getLotaTitular(), true,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO, false);
		final ExMobilVO mobilCompletoVO = new ExMobilVO(mob, getCadastrante(), getTitular(),
				getLotaTitular(), true, null, false);

		result.include("mobilCompletoVO", mobilCompletoVO);
		result.include("mobilVO", mobilVO);
		result.include("sigla", sigla);
		result.include("mob", mob);
		result.include("subscritorSel", movimentacaoBuilder.getSubscritorSel());
		result.include("titularSel", movimentacaoBuilder.getTitularSel());
		result.include("request", getRequest());
		result.include("assinandoAnexosGeral", assinandoAnexosGeral);
	}

	@Get("app/expediente/mov/assinarAnexos")
	public void assinarAnexos(final String sigla, final boolean assinandoAnexosGeral) {
		final BuscaDocumentoBuilder documentoBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(documentoBuilder);
		final ExMobil mob = documentoBuilder.getMob();

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia().setMob(mob);

		final ExMobilVO mobilVO = new ExMobilVO(mob, getCadastrante(), getTitular(),
				getLotaTitular(), true,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO, false);
		final ExMobilVO mobilCompletoVO = new ExMobilVO(mob, getCadastrante(), getTitular(),
				getLotaTitular(), true, null, false);

		result.include("mobilCompletoVO", mobilCompletoVO);
		result.include("mobilVO", mobilVO);
		result.include("sigla", sigla);
		result.include("mob", mob);
//		result.include("subscritorSel", movimentacaoBuilder.getSubscritorSel());
//		result.include("titularSel", movimentacaoBuilder.getTitularSel());
		result.include("request", getRequest());
		result.include("assinandoAnexosGeral", assinandoAnexosGeral);
		result.use(Results.page()).forwardTo(
				"/WEB-INF/page/exMovimentacao/anexa.jsp");
	}

	@Transacional
	@Post("app/expediente/mov/anexar_gravar")
	public void anexarGravar(final String sigla,
			final DpPessoaSelecao subscritorSel,
			final DpPessoaSelecao titularSel, final boolean substituicao,
			final UploadedFile arquivo, final String dtMovString,
			final String descrMov) throws IOException {

		final BuscaDocumentoBuilder documentoBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(documentoBuilder);
		final ExMobil mob = documentoBuilder.getMob();
		
		if (!(mob.isGeral() && mob.doc().isFinalizado())) {
			if (!Ex.getInstance().getComp().podeAnexarArquivo(getTitular(), getLotaTitular(), mob)) {
				throw new AplicacaoException("Arquivo não pode ser anexado");
			}
		}

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia().setMob(documentoBuilder.getMob())
				.setSubstituicao(substituicao).setSubscritorSel(subscritorSel)
				.setTitularSel(titularSel).setDtMovString(dtMovString)
				.setDescrMov(descrMov).setContentType(arquivo.getContentType())
				.setFileName(arquivo.getFileName());

		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());
		mov.setSubscritor(subscritorSel.getObjeto());
		mov.setTitular(titularSel.getObjeto());

		if (arquivo.getFile() == null) {
			throw new AplicacaoException(
					"O arquivo a ser anexado não foi selecionado!");
		}
		
		Integer numBytes = 0;
		try {
			final byte[] baArquivo = toByteArray(arquivo);
			if (baArquivo == null) {
				throw new AplicacaoException(
						"Arquivo vazio não pode ser anexado.");
			}
			numBytes = baArquivo.length;
			if (numBytes > 10 * 1024 * 1024) {
				throw new AplicacaoException("Não é permitida a anexação de arquivos com mais de 10MB.");
			}
			mov.setConteudoBlobMov2(baArquivo);
		} catch (IOException ex) {
			throw new AplicacaoException("Falha ao manipular aquivo", 1, ex);
		}

		Integer numPaginas = mov.getContarNumeroDePaginas();
		if (mov.getContarNumeroDePaginas() == null
			|| mov.getArquivoComStamp() == null) {
			throw new AplicacaoException(
					MessageFormat
							.format("O arquivo {0} está corrompido. Favor gera-lo novamente antes de anexar.",
									arquivo.getFileName()));
		}
		
//		if (numPaginas != null && numBytes != null &&  (numBytes/numPaginas > (1 * 1024 * 1024))) {
//			throw new AplicacaoException("Não é permitida a anexação de arquivos com mais de 1MB por página.");
//		}
		
		if (mob.isVolumeEncerrado()) {
			throw new AplicacaoException(
					"Não é possível anexar arquivo em volume encerrado.");
		}

		if (!Ex.getInstance().getComp()
				.podeAnexarArquivo(getTitular(), getLotaTitular(), mob)) {
			throw new AplicacaoException("Arquivo não pode ser anexado");
		}
		if (!arquivo.getContentType().equals("application/pdf")) {
			throw new AplicacaoException(
					"Somente é permitido anexar arquivo PDF.");
		}

		// Obtem as pendencias que serÃ£o resolvidas
		final String aidMov[] = getRequest().getParameterValues(
				"pendencia_de_anexacao");
		Set<ExMovimentacao> pendencias = null;
		if (aidMov != null) {
			pendencias = new TreeSet<ExMovimentacao>();
			for (String s : aidMov) {
				pendencias.add(dao().consultar(Long.parseLong(s),
						ExMovimentacao.class, false));
			}
		}

		// Nato: Precisei usar o código abaixo para adaptar o charset do
		// nome do arquivo
		try {
			final byte[] ab = mov.getNmArqMov().getBytes();
			for (int i = 0; i < ab.length; i++) {
				if (ab[i] == -29) {
					ab[i] = -61;
				}
			}
			final String sNmArqMov = new String(ab, "utf-8");

			Ex.getInstance()
					.getBL()
					.anexarArquivo(getCadastrante(), getLotaTitular(), mob,
							mov.getDtMov(), mov.getSubscritor(), sNmArqMov,
							mov.getTitular(), mov.getLotaTitular(),
							mov.getConteudoBlobMov2(), mov.getConteudoTpMov(),
							movimentacaoBuilder.getDescrMov(), pendencias);
		} catch (UnsupportedEncodingException ex) {
			LOGGER.error(ex.getMessage(), ex);
		}
		
		if (mob.isVolumeEncerrado()) {
			result.redirectTo(MessageFormat.format("/app/expediente/doc/exibir?sigla={0}&msg=N%26uacute;mero m%26aacute;ximo de p%26aacute;ginas atingido. Volume fechado automaticamente.", sigla));
			return;
		}

		result.redirectTo(MessageFormat.format("anexar?sigla={0}", sigla));
	}

	@Get("app/expediente/mov/mostrar_anexos_assinados")
	public void mostrarAnexosAssinados(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(builder);

		final ExMobilVO mobilVO = new ExMobilVO(builder.getMob(), getCadastrante(), getTitular(),
				getLotaTitular(), true,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO, true);

		result.include("mobilVO", mobilVO);
	}
	
	@Get("app/expediente/mov/anexar_arquivo_auxiliar")
	public void anexarArquivoAuxiliar(final String sigla) {
		final BuscaDocumentoBuilder documentoBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(documentoBuilder);
		ExMobil mob = documentoBuilder.getMob();
		if (mob != null && !mob.isGeral())
			mob = mob.doc().getMobilGeral();

		if(!Ex.getInstance().getComp()
				.podeAnexarArquivoAuxiliar(getTitular(), getLotaTitular(), mob)) {
			throw new AplicacaoException("Arquivo Auxiliar não pode ser anexado");
		}

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia().setMob(mob);

		result.include("sigla", sigla);
		result.include("mob", mob);
		result.include("request", getRequest());
	}

	@Transacional
	@Post("app/expediente/mov/anexar_arquivo_auxiliar_gravar")
	public void anexarArquivoAuxiliarGravar(final String sigla, final UploadedFile arquivo) throws IOException {
		final BuscaDocumentoBuilder documentoBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(documentoBuilder);
		final ExMobil mobOriginal = documentoBuilder.getMob();
		ExMobil mob = mobOriginal;
		if (mob != null && !mob.isGeral())
			mob = mob.doc().getMobilGeral();
		
		if(!Ex.getInstance().getComp()
				.podeAnexarArquivoAuxiliar(getTitular(), getLotaTitular(), mob)) {
			throw new AplicacaoException("Arquivo Auxiliar não pode ser anexado");
		}
		
		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia().setMob(documentoBuilder.getMob()).setContentType(arquivo.getContentType())
				.setFileName(arquivo.getFileName());

		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());
		mov.setSubscritor(getCadastrante());
		mov.setTitular(getCadastrante());

		if (arquivo.getFile() == null) {
			throw new AplicacaoException(
					"O arquivo a ser anexado não foi selecionado!");
		}
		
		String fileExtension = arquivo.getFileName().substring(arquivo.getFileName().lastIndexOf("."));
		
		if (fileExtension.equals(".bat") || fileExtension.equals(".exe") || fileExtension.equals(".sh") || fileExtension.equals(".dll") ) {
			throw new AplicacaoException(
					"Extensão " + fileExtension + " inválida para inclusão do arquivo.");
		}
		
		Integer numBytes = 0;
		try {
			final byte[] baArquivo = toByteArray(arquivo);
			if (baArquivo == null) {
				throw new AplicacaoException(
						"Arquivo vazio não pode ser anexado.");
			}
			numBytes = baArquivo.length;
			if (numBytes > 10 * 1024 * 1024) {
				throw new AplicacaoException("Não é permitida a anexação de arquivos com mais de 10MB.");
			}
			mov.setConteudoBlobMov2(baArquivo);
		} catch (IOException ex) {
			throw new AplicacaoException("Falha ao manipular aquivo", 1, ex);
		}

		// Nato: Precisei usar o código abaixo para adaptar o charset do
		// nome do arquivo
		try {
			final byte[] ab = mov.getNmArqMov().getBytes();
			for (int i = 0; i < ab.length; i++) {
				if (ab[i] == -29) {
					ab[i] = -61;
				}
			}
			final String sNmArqMov = new String(ab, "utf-8");

			Ex.getInstance().getBL()
					.anexarArquivoAuxiliar(getCadastrante(), getLotaTitular(), mob,
							mov.getDtMov(), mov.getSubscritor(), sNmArqMov,
							mov.getTitular(), mov.getLotaTitular(),
							mov.getConteudoBlobMov2(), mov.getConteudoTpMov());
		} catch (UnsupportedEncodingException ex) {
			LOGGER.error(ex.getMessage(), ex);
		}
		ExDocumentoController.redirecionarParaExibir(result, mobOriginal.getSigla());
	}

	@Get("app/expediente/mov/copiar")
	public void aCopiar(final String sigla) {
		final BuscaDocumentoBuilder documentoBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(documentoBuilder);
		final ExMobil mob = documentoBuilder.getMob();

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia().setMob(mob);

		if (!Ex.getInstance().getComp()
				.podeCopiar(getTitular(), getLotaTitular(), mob)) {
			throw new AplicacaoException("Não é permitido incluir cópia");
		}

		final ExMobilVO mobilVO = new ExMobilVO(mob, getCadastrante(), getTitular(),
				getLotaTitular(), true,
				ExTipoMovimentacao.TIPO_MOVIMENTACAO_COPIA, false);
		final ExMobilVO mobilCompletoVO = new ExMobilVO(mob, getCadastrante(), getTitular(),
				getLotaTitular(), true, null, false);

		result.include("sigla", sigla);
		result.include("doc", mob.doc());
		result.include("mob", mob);
		result.include("request", getRequest());
		result.include("titularSel", new DpPessoaSelecao());
		result.include("subscritorSel", new DpPessoaSelecao());
		result.include("documentoRefSel", new ExDocumentoSelecao());
	}
	
	@Transacional
	@Post("app/expediente/mov/copiar_gravar")
	public void copiarGravar(final String sigla,
			final String dtMovString, final boolean substituicao,
			final DpPessoaSelecao titularSel,
			final DpPessoaSelecao subscritorSel,
			final ExMobilSelecao documentoRefSel) {
		final BuscaDocumentoBuilder documentoBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(documentoBuilder);
		final ExMobil mob = documentoBuilder.getMob();
		
		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia();
		movimentacaoBuilder.setDtMovString(dtMovString)
				.setSubstituicao(substituicao).setTitularSel(titularSel)
				.setSubscritorSel(subscritorSel).setMob(mob)
				.setDocumentoRefSel(documentoRefSel);

		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());
		
		if (!Ex.getInstance()
				.getComp()
				.podeAcessarDocumento(getTitular(), getLotaTitular(),
						mov.getExMobilRef())) {
			throw new AplicacaoException("Não é permitido incluir cópia de documento que o usuário não tenha acesso");
		}

		if (!Ex.getInstance()
				.getComp()
				.podeCopiar(getTitular(), getLotaTitular(),
						mob)) {
			throw new AplicacaoException("Não é permitido incluir cópia");
		}
		if (mov.getExMobilRef() == null) {
			throw new AplicacaoException(
					"Não foi selecionado um documento para a vinculação");
		}

		if (mov.getExDocumento().isEletronico()) {
			mov.setSubscritor(getTitular());
		}

		Ex.getInstance()
				.getBL()
				.copiar(getCadastrante(), getLotaTitular(),
						mob, mov.getExMobilRef(), mov.getDtMov(),
						mov.getSubscritor(), mov.getTitular());

		ExDocumentoController.redirecionarParaExibir(result, mov
				.getExDocumento().getSigla());
	}

	@Transacional
	@Get("app/expediente/mov/desobrestar_gravar")
	public void aDesobrestarGravar(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(builder);
		final ExMobil mob = builder.getMob();

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia().setMob(mob);

		final ExMovimentacao movimentacao = movimentacaoBuilder
				.construir(dao());

		if (!Ex.getInstance().getComp()
				.podeDesobrestar(getTitular(), getLotaTitular(), mob)) {
			throw new AplicacaoException("Via não pode ser desobrestada");
		}

		Ex.getInstance()
				.getBL()
				.desobrestar(getCadastrante(), getLotaTitular(), mob,
						movimentacao.getDtMov(), movimentacao.getSubscritor());
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Transacional
	@Get("app/expediente/mov/sobrestar_gravar")
	public void sobrestarGravar(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(builder);

		final ExMovimentacao mov = ExMovimentacaoBuilder.novaInstancia()
				.construir(dao());

		if (!Ex.getInstance()
				.getComp()
				.podeAcessarDocumento(getTitular(), getLotaTitular(),
						builder.getMob())) {
			throw new AplicacaoException(
					"Acesso permitido a usuários autorizados.");
		}

		if (!Ex.getInstance()
				.getComp()
				.podeSobrestar(getTitular(), getLotaTitular(), builder.getMob())) {
			throw new AplicacaoException("Via não pode ser sobrestada");
		}

		Ex.getInstance()
				.getBL()
				.sobrestar(getCadastrante(), getLotaTitular(),
						builder.getMob(), mov.getDtMov(), null,
						mov.getSubscritor());
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}
		
	@Transacional
	@Get("app/expediente/mov/assinar")
	public void aAssinar(String sigla, Boolean autenticando) throws Exception {
		BuscaDocumentoBuilder builder = BuscaDocumentoBuilder.novaInstancia()
				.setSigla(sigla);

		ExDocumento doc = buscarDocumento(builder);
		
		if (Strings.isNullOrEmpty(doc.getDescrDocumento()))
			throw new AplicacaoException(
					"Não é possível assinar o documento pois a descrição está vazia. Edite-o e informe uma descrição.");
		
		if (autenticando == null)
			autenticando = false;
		boolean previamenteAssinado = !doc.isPendenteDeAssinatura();
		boolean assinando = !autenticando;
		
		if (autenticando && !permiteAutenticar(doc)) {				
			throw new AplicacaoException(
					"Não é permitido autenticar o documento, favor rever as configurações para o modelo: "
					+ doc.getExModelo().getDescMod() + ". "
					+ "Tipo de Configuração: Movimentar. "
					+ "Tipo de Movimentação: Autenticação de Documento.");			
		}
					
		/*
		 * 16/01/2020 - recebendo a data da assinatura
		 */
		if(doc.getDtPrimeiraAssinatura() == null)
			doc.setDtPrimeiraAssinatura(dao.dt());

		if (devePreAssinar(doc, previamenteAssinado)) {
			Ex.getInstance().getBL()
					.processarComandosEmTag(doc, "pre_assinatura");
		}
		
		AtivoEFixo afTramite = obterAtivoEFixo(doc.getExModelo(), doc.getExTipoDocumento(), CpTipoConfiguracao.TIPO_CONFIG_TRAMITE_AUTOMATICO);
		
		AtivoEFixo afJuntada = obterAtivoEFixo(doc.getExModelo(), doc.getExTipoDocumento(), CpTipoConfiguracao.TIPO_CONFIG_JUNTADA_AUTOMATICA);
		
		// Habilita ou desabilita o trâmite 
		if (!Ex.getInstance()
				.getComp()
				.podeTramitarPosAssinatura(doc.getDestinatario(), doc.getLotaDestinatario(), getTitular(), getLotaTitular(),doc.getMobilGeral())){
			afTramite.ativo = false;
			afTramite.fixo = true;
		}
		if(Prop.isGovSP()
				&& (doc.getDtFinalizacao() != null && !DateUtils.isToday(doc.getDtFinalizacao()))
				&& doc.getMobilGeral().getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA).isEmpty()
				&& doc.getMobilGeral().getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO).isEmpty()) {
			Ex.getInstance().getBL().gravar(getCadastrante(), getTitular(), getLotaTitular(), doc);
		}
		result.include("sigla", sigla);
		result.include("doc", doc);
		result.include("titular", this.getTitular());
		result.include("lotaTitular", this.getLotaTitular());
		result.include("autenticando", autenticando);
		result.include("assinando", assinando);
		result.include("voltarAtivo", true);
		result.include("juntarAtivo", doc.getPai() != null && afJuntada.ativo ? true : null);
		result.include("juntarFixo", doc.getPai() != null && afJuntada.fixo ? false : null);
		result.include("tramitarAtivo", Prop.isGovSP() ? "" : afTramite.ativo);
		result.include("tramitarFixo", afTramite.fixo);
	}
	
	private boolean permiteAutenticar(ExDocumento doc) {
		return Ex.getInstance().getComp().podeAutenticarDocumento(getTitular(), getLotaTitular(), doc);
	}
	
	public static class AtivoEFixo {
		public boolean ativo;
		public boolean fixo;
	}

	private AtivoEFixo obterAtivoEFixo(ExModelo modelo, ExTipoDocumento tipoDocumento, long tipoConf) {
		final Long idSit = Ex
				.getInstance()
				.getConf()
				.buscaSituacao(modelo,
						tipoDocumento,
						getTitular(), getLotaTitular(),
						tipoConf)
				.getIdSitConfiguracao();

		AtivoEFixo af = new AtivoEFixo();
		
		if (idSit == ExSituacaoConfiguracao.SITUACAO_OBRIGATORIO) {
			af.ativo = true;
			af.fixo = true;
		} else if (idSit == ExSituacaoConfiguracao.SITUACAO_PROIBIDO || idSit == ExSituacaoConfiguracao.SITUACAO_NAO_PODE) {
			af.ativo = false;
			af.fixo = true;
		} else if (idSit == ExSituacaoConfiguracao.SITUACAO_DEFAULT || idSit == ExSituacaoConfiguracao.SITUACAO_PODE) {
			af.ativo = true;
			af.fixo = false;
		} else if (idSit == ExSituacaoConfiguracao.SITUACAO_NAO_DEFAULT) {
			af.ativo = false;
			af.fixo = false;
		}
		return af;
	}
	
	private boolean devePreAssinar(ExDocumento doc, boolean fPreviamenteAssinado) {
		return !fPreviamenteAssinado
				&& (doc.getExModelo() != null && ("template/freemarker"
						.equals(doc.getExModelo().getConteudoTpBlob())));
	}

	@Get("app/expediente/mov/redefinir_nivel_acesso")
	public void redefinirNivelAcesso(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		final ExDocumento doc = buscarDocumento(builder);
		final DpPessoaSelecao subscritorSel = new DpPessoaSelecao();
		final DpPessoaSelecao titularSel = new DpPessoaSelecao();

		result.include("sigla", sigla);
		result.include("mob", builder.getMob());
		result.include("listaNivelAcesso", getListaNivelAcesso(doc));
		result.include("nivelAcesso", doc.getExNivelAcessoAtual().getIdNivelAcesso());
		result.include("subscritorSel", subscritorSel);
		result.include("titularSel", titularSel);
	}
	
	@Get("app/expediente/mov/restringir_acesso")
	public void restringirAcesso(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		final ExDocumento doc = buscarDocumento(builder);
		final DpPessoaSelecao subscritorSel = new DpPessoaSelecao();
		final DpPessoaSelecao titularSel = new DpPessoaSelecao();

		result.include("sigla", sigla);
		result.include("mob", builder.getMob());
		result.include("listaNivelAcesso", getListaNivelAcesso(doc));
		result.include("nivelAcesso", doc.getExNivelAcessoAtual().getIdNivelAcesso());
		result.include("subscritorSel", subscritorSel);
		result.include("titularSel", titularSel);
	}
	
	@Transacional
	@Get("app/expediente/mov/desfazer_restricao_acesso")
	public void desfazerRestringirAcesso(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		final ExDocumento doc = buscarDocumento(builder);
	
		ExNivelAcesso exTipoSig = dao().consultar(ExNivelAcesso.ID_LIMITADO_ENTRE_LOTACOES, ExNivelAcesso.class, false);

		if (!Ex.getInstance()
				.getComp()
				.podeDesfazerRestricaoAcesso(getCadastrante(), getLotaCadastrante(),
						builder.getMob())) {
			throw new AplicacaoException(
					"Não é possível restringir acesso");
		}
		
		Ex.getInstance()
			.getBL()
			.desfazerRestringirAcesso(getCadastrante(), getLotaCadastrante(), doc, null,  exTipoSig);
		
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}
	
	@Transacional
	@Post("app/expediente/mov/restringir_acesso_gravar")
	public void restringirAcessoGravar(final String sigla, final String usu, final Long nivelAcesso) throws Exception {
		String usuarios[] = usu.split(";");
		
		List<DpPessoa> listaSubscritor = new ArrayList<DpPessoa>();
		for (int i = 1; i < usuarios.length; i++) {
			listaSubscritor.add(dao().consultar(Long.valueOf(usuarios[i]), DpPessoa.class, false));
		}
		
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		
		final ExDocumento doc = buscarDocumento(builder);
		
		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia();

		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());

		ExNivelAcesso exTipoSig = null;

		if (nivelAcesso != null) {
			exTipoSig = dao()
					.consultar(nivelAcesso, ExNivelAcesso.class, false);
		}
		
		if (!Ex.getInstance()
				.getComp()
				.podeRestrigirAcesso(getCadastrante(), getLotaCadastrante(),
						builder.getMob())) {
			throw new AplicacaoException(
					"Não é possível restringir acesso");
		}
					
		adicionarIndicativoDeMovimentacaoComOrigemPeloBotaoDeRestricaoDeAcesso();			
		
		Ex.getInstance()
				.getBL()
				.restringirAcesso(getCadastrante(), getLotaTitular(), doc,
						null, mov.getLotaResp(), mov.getResp(),
						listaSubscritor, mov.getTitular(),
						mov.getNmFuncaoSubscritor(), exTipoSig);
		
		removerIndicativoDeMovimentacaoComOrigemPeloBotaoDeRestricaoDeAcesso();

		result.include("msgCabecClass", "alert-warning");
		result.include("mensagemCabec", "Somente os usuários definidos, terão acesso aos documentos. Os usuários que já tiveram acesso ao documento, por tramitações anteriores ou por definição de acompanhamento deixam de ter acesso/visualização ao documento. Inclusive o cadastrante dos documentos, responsáveis pela assinatura e cossignatário");
		result.forwardTo(ExDocumentoController.class).exibe(false, sigla, null, null, null, false);
	}

	@Transacional
	@Post("app/expediente/mov/redefinir_nivel_acesso_gravar")
	public void redefinirNivelAcessoGravar(final String sigla,
			final DpPessoaSelecao subscritorSel,
			final DpPessoaSelecao titularSel, final String dtMovString,
			final boolean substituicao, final Long nivelAcesso) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		final ExDocumento doc = buscarDocumento(builder);

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia();
		movimentacaoBuilder.setSubscritorSel(subscritorSel)
				.setTitularSel(titularSel).setDtMovString(dtMovString)
				.setSubstituicao(substituicao);

		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());

		ExNivelAcesso exTipoSig = null;

		if (nivelAcesso != null) {
			exTipoSig = dao()
					.consultar(nivelAcesso, ExNivelAcesso.class, false);
		}

		if (!Ex.getInstance()
				.getComp()
				.podeRedefinirNivelAcesso(getTitular(), getLotaTitular(),
						builder.getMob())) {
			throw new AplicacaoException(
					"Não é possível redefinir o nível de acesso");
		}

		Ex.getInstance()
				.getBL()
				.redefinirNivelAcesso(getCadastrante(), getLotaTitular(), doc,
						mov.getDtMov(), mov.getLotaResp(), mov.getResp(),
						mov.getSubscritor(), mov.getTitular(),
						mov.getNmFuncaoSubscritor(), exTipoSig);
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Transacional
	@Get("/app/expediente/mov/cancelarMovimentacao")
	public void aCancelarUltimaMovimentacao(final String sigla) {
		final BuscaDocumentoBuilder documentoBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(documentoBuilder);
		final ExMobil mob = documentoBuilder.getMob();

		final ExMovimentacao exUltMovNaoCanc = mob
				.getUltimaMovimentacaoNaoCancelada();
		final ExMovimentacao exUltMov = mob.getUltimaMovimentacao();

		if (exUltMovNaoCanc.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO
				&& exUltMovNaoCanc.getIdMov() == exUltMov.getIdMov()) {
			if (!Ex.getInstance().getComp()
					.podeCancelarVia(getTitular(), getLotaTitular(), mob)) {
				throw new AplicacaoException("Não é possível cancelar via");
			}
		} else {
			if (!Ex.getInstance()
					.getComp()
					.podeCancelarMovimentacao(getTitular(), getLotaTitular(),
							mob)) {
				throw new AplicacaoException(
						"Não é possível cancelar movimentação");
			}
		}

		Ex.getInstance().getBL()
				.cancelarMovimentacao(getCadastrante(), getLotaTitular(), mob);
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Transacional
	@Get("app/expediente/mov/excluir")
	public void excluir(final Long id, boolean continuarTela, String redirectURL) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setId(id);
		buscarDocumento(builder);
		final ExMobil mob = builder.getMob();

		Ex.getInstance()
				.getBL()
				.excluirMovimentacao(getCadastrante(), getLotaTitular(), mob,
						id);

		if (continuarTela) {
			result.redirectTo(MessageFormat.format("anexar?sigla={0}",
					mob.getSigla()));
		} else {
			if (redirectURL != null) {
				result.redirectTo(redirectURL);
			} else {
				ExDocumentoController
				.redirecionarParaExibir(result, mob.getSigla());
			}
		}
	}

	@Get("app/expediente/mov/exibir")
	public void aExibir(final boolean popup, final Long id,
			final boolean autenticando) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setId(id);

		final ExDocumento doc = buscarDocumento(builder);

		if (id == null) {
			throw new AplicacaoException("id não foi informada.");
		}

		final ExMovimentacao mov = dao().consultar(id, ExMovimentacao.class,
				false);

		result.include("id", id);
		result.include("doc", doc);
		result.include("mov", mov);
		result.include("autenticando", autenticando);
		result.include("enderecoAutenticacao", Prop.get("/sigaex.autenticidade.url"));
		result.include("popup", popup);
		result.include("request", getRequest());

	}

	private ArrayList<Object> criarListaDocumentos(List<String> itens) {
		final ArrayList<Object> listarDocumentos = new ArrayList<Object>();

		for (String idMubString : itens) {
			try {
				final Long idMob = Long.parseLong(idMubString);
				final ExMobil mob = dao()
						.consultar(idMob, ExMobil.class, false);
				final Object[] ao = { mob.doc(),
						mob.getUltimaMovimentacaoNaoCancelada() };
				listarDocumentos.add(ao);
			} catch (NumberFormatException nfe) {
				System.out.println(MessageFormat.format(
						"{0} nao pode ser convertido para Long", idMubString));
			}
		}
		return listarDocumentos;
	}

	private ExMovimentacao criarMov(String movIdString) {
		try {
			Long idMov = Long.parseLong(movIdString);
			final ExMovimentacao mov = dao.consultar(idMov,
					ExMovimentacao.class, false);
			return mov;
		} catch (NumberFormatException nfe) {
			System.out.println(MessageFormat.format(
					"{0} nao pode ser convertido para Long", movIdString));
		}
		return null;
	}

	@Get("app/expediente/mov/protocolo_unitario")
	public void protocolo(boolean popup, final String sigla, final Long id) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		final ExDocumento doc = buscarDocumento(builder);
		ExMovimentacao mov = null;

		if (id != null)
			mov = dao().consultar(id, ExMovimentacao.class, false);
		else
			mov = new ExMovimentacao();

		ArrayList<Object> lista = new ArrayList<Object>();
		final Object[] ao = { doc, mov };
		lista.add(ao);
		result.include("cadastrante", mov.getCadastrante());
		result.include("mov", mov);
		result.include("itens", lista);
		result.include("lotaTitular", mov.getLotaTitular());
		result.include("popup", popup);
	}
	
	@Get
	@Path("app/expediente/mov/protocolo_unitario_sp")
	public void protocoloSP(boolean popup, final String sigla, final Long id) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		final ExDocumento doc = buscarDocumento(builder);
		ExMovimentacao mov = null;

		if (id != null)
			mov = dao().consultar(id, ExMovimentacao.class, false);
		else
			mov = new ExMovimentacao();

		ArrayList<Object> lista = new ArrayList<Object>();
		final Object[] ao = { doc, mov };
		lista.add(ao);
		result.include("cadastrante", mov.getCadastrante());
		result.include("mov", mov);
		result.include("itens", lista);
		result.include("lotaTitular", mov.getLotaTitular());
		result.include("popup", popup);
	}

	@Post("/app/expediente/mov/protocolo_arq_transf")
	@Get("/app/expediente/mov/protocolo_arq_transf")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void aGerarProtocoloArqTransf(String sigla, boolean popup,
			boolean isTransf) throws Exception {
		ExMovimentacao mov = null;

		final DpPessoa pes;
		final List<ItemDeProtocolo> al = new ArrayList<ItemDeProtocolo>();
		final DpPessoa oExemplo = new DpPessoa();

		if (sigla == null || sigla.trim() == "") {
			LOGGER.warn("[aGerarProtocoloArq] - A sigla informada é nula ou inválida");
			throw new AplicacaoException(
					"A sigla informada é nula ou inválida.");
		}

		oExemplo.setSigla(sigla);
		pes = CpDao.getInstance().consultarPorSigla(oExemplo);

		if (pes == null) {
			LOGGER.warn("[aGerarProtocoloArq] - Não foi possível localizar DpPessoa com a sigla "
					+ oExemplo.getSigla());
			throw new AplicacaoException(
					"Não foi localizada pessoa com a sigla informada.");
		}

		Date dt = paramDate("dt");
		final List<ExMovimentacao> movs = dao().consultarMovimentacoes(pes, dt);
		for (ExMovimentacao m : movs) {
			if (mov == null)
				mov = m;
			al.add(new ItemDeProtocolo(m));
			
			//Edson: incluindo os mobs apensos do mesmo processo, q não receberam a mov de transferência
			for (ExMobil apenso : m.getExMobil().getDoc().getExMobilSet()){
				if (apenso.isGeral() || apenso.equals(m.getExMobil()))
					continue;
				
				boolean fazParteDaTrilha = false;
				ExMobil mestreDoApenso = apenso;
				while (mestreDoApenso != null && !fazParteDaTrilha){
					mestreDoApenso = mestreDoApenso.getMestre();
					if (mestreDoApenso != null && mestreDoApenso.equals(m.getExMobil()))
						fazParteDaTrilha = true;
				}
				
				if (!fazParteDaTrilha)
					continue;
				
				ItemDeProtocolo i = new ItemDeProtocolo();
				i.setMob(apenso);
				i.setSubscritor(m.getSubscritor());
				i.setLotaSubscritor(m.getLotaSubscritor());
				i.setAtendente(m.getResp());
				i.setLotaAtendente(m.getLotaResp());
				i.setDtDDMMYY(m.getDtMovDDMMYY());
				al.add(i);
			}
		}

		Collections.sort(al, new ItemDeProtocoloComparator());
		
		result.include("itens", al);
		result.include("mov", mov);
		result.include("popup", popup);

		if (isTransf) {
			result.include("campoDe", mov.getCadastrante().getLotacao()
					.getDescricao());
			result.include("campoPara", mov.getRespString());
			result.include("campoData", mov.getDtRegMovDDMMYYHHMMSS());
			result.include("cadastrante", mov.getCadastrante());
			result.include("lotaTitular", mov.getLotaTitular());

			/*result.use(Results.page()).forwardTo(
					"/WEB-INF/page/exMovimentacao/aGerarProtocolo.jsp");*/
		} else
			if(SigaMessages.isSigaSP()) {
				result.redirectTo("/app/expediente/mov/protocolo_unitario_sp?popup="
						+ popup + "&sigla=" + mov.getExMobil().getDoc().getSigla()
						+ "&id=" + mov.getIdMov());
			} else {
				result.redirectTo("/app/expediente/mov/protocolo_unitario?popup="
						+ popup + "&sigla=" + mov.getExMobil().getDoc().getSigla()
						+ "&id=" + mov.getIdMov());
			}
	}

	@Get("app/expediente/mov/juntar")
	public void juntar(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		final ExDocumento doc = buscarDocumento(builder);

		if (!Ex.getInstance().getComp()
				.podeJuntar(getTitular(), getLotaTitular(), builder.getMob())) {
			throw new AplicacaoException("Não é possível fazer juntada");
		}
		
		// Preencher automaticamente o mobil pai quando se tratar de documento filho
		ExMobilSelecao documentoRefSel = new ExMobilSelecao();
		if (doc.getPai() != null) {
			documentoRefSel.buscarPorObjeto(doc.getPai().isExpediente() ? doc.getPai().getPrimeiraVia() : doc.getPai().getUltimoVolume());
		}

		result.include("sigla", sigla);
		result.include("mob", builder.getMob());
		result.include("doc", doc);
		result.include("subscritorSel", new DpPessoaSelecao());
		result.include("documentoRefSel", documentoRefSel);
	}

	@Transacional
	@Post("app/expediente/mov/juntar_gravar")
	public void aJuntarGravar(final Integer postback, final String sigla,
			final String dtMovString, final boolean substituicao,
			final String idDocumentoPaiExterno,
			final DpPessoaSelecao subscritorSel,
			final DpPessoaSelecao titularSel,
			final ExMobilSelecao documentoRefSel,
			final String idDocumentoEscolha) {
		this.setPostback(postback);

		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(builder);

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia().setDtMovString(dtMovString)
				.setSubstituicao(substituicao).setSubscritorSel(subscritorSel)
				.setTitularSel(titularSel).setDocumentoRefSel(documentoRefSel)
				.setMob(builder.getMob());

		if (movimentacaoBuilder.getDocumentoRefSel() == null) {
			movimentacaoBuilder.setDocumentoRefSel(new ExMobilSelecao());
		}

		if (movimentacaoBuilder.getSubscritorSel() == null) {
			movimentacaoBuilder.setSubscritorSel(new DpPessoaSelecao());
		}

		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());
													
		if (!Ex.getInstance()
				.getComp()
				.podeJuntar(getTitular(), getLotaTitular(),
						movimentacaoBuilder.getMob())) {
			throw new AplicacaoException("Não é possível fazer juntada");
		}

		// Nato: precisamos rever o codigo abaixo, pois a movimentacao nao pode
		// ser gravada sem hora, minuto e segundo.
		if (mov.getExDocumento().isEletronico()) {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("dd/MM/yyyy");
			mov.setSubscritor(getTitular());
		}

		// Quando o documento e eletronico, o responsavel pela juntada fica
		// sendo o proprio cadastrante e a data fica sendo a data atual
		if (mov.getExDocumento().isEletronico()) {
			mov.setDtMov(new Date());
			mov.setSubscritor(getCadastrante());
			mov.setTitular(getTitular());
		}
			
		try {
			Ex.getInstance()
				.getBL()
				.juntarDocumento(getCadastrante(), getTitular(),
						getLotaTitular(), idDocumentoPaiExterno,
						movimentacaoBuilder.getMob(), mov.getExMobilRef(),
						mov.getDtMov(), mov.getSubscritor(), mov.getTitular(),
						idDocumentoEscolha);
			
			ExDocumentoController.redirecionarParaExibir(result, sigla);
		} catch (RegraNegocioException e) {
			result.include(SigaModal.ALERTA, SigaModal.mensagem(e.getMessage()));			
			result.forwardTo(this).juntar(sigla);
		}						
	}

	@Get("app/expediente/mov/apensar")
	public void apensar(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		final ExDocumento doc = buscarDocumento(builder);

		if (!Ex.getInstance().getComp()
				.podeApensar(getTitular(), getLotaTitular(), builder.getMob())) {
			throw new AplicacaoException("Não é possível apensar");
		}

		result.include("mob", builder.getMob());
		result.include("doc", doc);
		result.include("sigla", sigla);
		result.include("subscritorSel", new DpPessoaSelecao());
		result.include("titularSel", new DpPessoaSelecao());
		result.include("documentoRefSel", new ExDocumentoSelecao());

	}

	@Transacional
	@Post("app/expediente/mov/apensar_gravar")
	public void apensarGravar(final ExMobilSelecao documentoRefSel,
			final DpPessoaSelecao subscritorSel,
			final DpPessoaSelecao titularSel, final String sigla,
			final String dtMovString, final boolean substituicao) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder);

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia();
		movimentacaoBuilder.setDocumentoRefSel(documentoRefSel)
				.setSubscritorSel(subscritorSel).setTitularSel(titularSel)
				.setDtMovString(dtMovString).setSubstituicao(substituicao)
				.setMob(builder.getMob());

		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());

		if (!Ex.getInstance().getComp()
				.podeApensar(getTitular(), getLotaTitular(), builder.getMob())) {
			throw new AplicacaoException("Não é possível fazer apensar");
		}

		// Quando o documento e eletronico, o responsavel pela juntada fica
		// sendo o proprio cadastrante e a data fica sendo a data atual
		if (mov.getExDocumento().isEletronico()) {
			mov.setDtMov(new Date());
			mov.setSubscritor(getCadastrante());
			mov.setTitular(getTitular());
		}

		Ex.getInstance()
				.getBL()
				.apensarDocumento(getCadastrante(), getTitular(),
						getLotaTitular(), builder.getMob(),
						mov.getExMobilRef(), mov.getDtMov(),
						mov.getSubscritor(), mov.getTitular());
		ExDocumentoController.redirecionarParaExibir(result, mov
				.getExDocumento().getSigla());
	}

	@Get("/app/expediente/mov/registrar_assinatura")
	public void aRegistrarAssinatura(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		final ExDocumento doc = buscarDocumento(builder);

		DpPessoaSelecao sub = null;

		if (doc.getSubscritor() != null) {
			sub = new DpPessoaSelecao();
			sub.setId(doc.getSubscritor().getId());
			sub.buscar();
		}

		if (!Ex.getInstance()
				.getComp()
				.podeRegistrarAssinatura(getTitular(), getLotaTitular(),
						builder.getMob())) {
			throw new AplicacaoException(
					"Não é possível registrar a assinatura");
		}

		result.include("mob", builder.getMob());
		result.include("sigla", sigla);
		result.include("subscritorSel", sub);
		result.include("substituicao", false);
	}

	@Transacional
	@Post("/app/expediente/mov/registrar_assinatura_gravar")
	public void registrar_assinatura_gravar(final int postback,
			final String sigla, final String dtMovString,
			final DpPessoaSelecao subscritorSel, final boolean substituicao,
			final DpPessoaSelecao tilularSel) {
		this.setPostback(postback);

		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		final ExDocumento doc = buscarDocumento(builder);

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia();
		movimentacaoBuilder.setDtMovString(dtMovString)
				.setSubscritorSel(subscritorSel).setSubstituicao(substituicao)
				.setTitularSel(tilularSel);

		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());

		if (mov.getSubscritor() == null) {
			throw new AplicacaoException("Responsável não informado");
		}

		if (!Ex.getInstance()
				.getComp()
				.podeRegistrarAssinatura(getTitular(), getLotaTitular(),
						builder.getMob())) {
			throw new AplicacaoException(
					"Não é possível registrar a assinatura");
		}

		Ex.getInstance()
				.getBL()
				.RegistrarAssinatura(getCadastrante(), getLotaTitular(), doc,
						mov.getDtMov(), mov.getSubscritor(), mov.getTitular());

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
	}

	@Get("/app/expediente/mov/incluir_cosignatario")
	public void incluirCosignatario(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		final ExDocumento doc = buscarDocumento(builder);
		final ExMobil mob = builder.getMob();

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia().setMob(mob);

		if (!Ex.getInstance()
				.getComp()
				.podeIncluirCosignatario(getTitular(), getLotaTitular(),
						builder.getMob())) {
			throw new AplicacaoException("Não é possível incluir cossignatário");
		}

		result.include("sigla", sigla);
		result.include("documento", doc);
		result.include("cosignatarioSel",
				movimentacaoBuilder.getSubscritorSel());
		result.include("mob", builder.getMob());
	}

	@Transacional
	@Post("/app/expediente/mov/incluir_cosignatario_gravar")
	public void aIncluirCosignatarioGravar(final String sigla,
			final DpPessoaSelecao cosignatarioSel,
			final String funcaoCosignatario, final String  unidadeCosignatario, final Integer postback) {
		this.setPostback(postback);

		final BuscaDocumentoBuilder documentoBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		final ExDocumento doc = buscarDocumento(documentoBuilder);

		String funcaoUnidadeCosignatario = funcaoCosignatario;
		// Efetuar validação e concatenar o conteudo se for implantação GOVSP
		if(SigaMessages.isSigaSP() && (funcaoCosignatario != null && !funcaoCosignatario.isEmpty()) && (unidadeCosignatario != null && !unidadeCosignatario.isEmpty())) {
			funcaoUnidadeCosignatario = funcaoUnidadeCosignatario + ";" + unidadeCosignatario; 
		}
		
		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia().setMob(documentoBuilder.getMob())
				.setDescrMov(funcaoUnidadeCosignatario)
				.setSubscritorSel(cosignatarioSel);

		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());

		if (!Ex.getInstance()
				.getComp()
				.podeIncluirCosignatario(getTitular(), getLotaTitular(),
						documentoBuilder.getMob())) {
			throw new AplicacaoException("Não é possível incluir cossignatário");
		}

		Ex.getInstance()
				.getBL()
				.incluirCosignatario(getCadastrante(), getLotaTitular(), doc,
						mov.getDtMov(), mov.getSubscritor(), mov.getDescrMov());

		ExDocumentoController.redirecionarParaExibir(result, mov
				.getExDocumento().getSigla());
	}

	// Nato: Temos que substituir por uma tela que mostre os itens marcados como
	// "em transito"
	@Get("/app/expediente/mov/receber_lote")
	public void aReceberLote() {
		final List<ExMobil> provItens = dao().consultarParaReceberEmLote(
				getLotaTitular());

		final List<ExMobil> itens = new ArrayList<ExMobil>();

		for (ExMobil m : provItens) {
			if (!m.isApensado()
					&& Ex.getInstance()
							.getComp()
							.podeAcessarDocumento(getTitular(),
									getLotaTitular(), m)) {
				itens.add(m);
			}
		}

		result.include("itens", itens);
	}

	@Transacional
	@Post("/app/expediente/mov/receber_lote_gravar")
	public void aReceberLoteGravar(final Integer postback) {
		this.setPostback(postback);

		final ExMovimentacaoBuilder builder = ExMovimentacaoBuilder
				.novaInstancia();
		final ExMovimentacao mov = builder.construir(dao());

		final Pattern p = Pattern.compile("chk_([0-9]+)");

		for (final String s : getPar().keySet()) {
			if (s.startsWith("chk_") && param(s).equals("true")) {
				final Matcher m = p.matcher(s);
				if (!m.find()) {
					throw new AplicacaoException(
							"Não foi possível ler a Id do documento e o número da via.");
				}
				final ExMobil mob = dao().consultar(Long.valueOf(m.group(1)),
						ExMobil.class, false);

				if (Ex.getInstance().getComp()
						.podeReceber(getTitular(), getLotaTitular(), mob)) {
					Ex.getInstance()
							.getBL()
							.receber(getCadastrante(), getLotaTitular(), mob,
									mov.getDtMov());
				}
			}
		}

		result.redirectTo("/app/expediente/mov/receber_lote");
	}

	@Get("/app/expediente/mov/arquivar_corrente_lote")
	public void aArquivarCorrenteLote() {
		final List<ExMobil> provItens = dao()
				.consultarParaArquivarCorrenteEmLote(getLotaTitular());

		List<ExMobil> itens = new ArrayList<ExMobil>();

		for (ExMobil m : provItens) {
			if (!m.isApensado()
					&& Ex.getInstance()
							.getComp()
							.podeAcessarDocumento(getTitular(),
									getLotaTitular(), m)) {
				itens.add(m.isVolume() ? m.doc().getMobilGeral() : m);
			}
		}

		result.include("itens", itens);
	}

	@Transacional
	@Post("/app/expediente/mov/arquivar_corrente_lote_gravar")
	public void aArquivarCorrenteLoteGravar(final Integer postback) {
		this.setPostback(postback);

		final ExMovimentacaoBuilder builder = ExMovimentacaoBuilder
				.novaInstancia();
		final ExMovimentacao mov = builder.construir(dao());

		final Pattern p = Pattern.compile("chk_([0-9]+)");
		final Date dt = dao().dt();

		for (final String s : getPar().keySet()) {
			if (s.startsWith("chk_") && param(s).equals("true")) {
				final Matcher m = p.matcher(s);
				if (!m.find()) {
					throw new AplicacaoException(
							"Não foi possível ler a Id do documento e o número da via.");
				}
				final ExMobil mob = dao().consultar(Long.valueOf(m.group(1)),
						ExMobil.class, false);

				Ex.getInstance()
						.getBL()
						.arquivarCorrente(getCadastrante(), getLotaTitular(),
								mob, mov.getDtMov(), dt, mov.getSubscritor(),
								false);
			}
		}

		result.redirectTo("/app/expediente/mov/arquivar_corrente_lote");
	}

	@Transacional
	@Get("/app/expediente/mov/arquivar_corrente_gravar")
	public void aArquivarCorrenteGravar(final String sigla) {

		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder);

		final ExMovimentacaoBuilder movBuilder = ExMovimentacaoBuilder
				.novaInstancia();
		final ExMovimentacao mov = movBuilder.construir(dao());

		if (!Ex.getInstance()
				.getComp()
				.podeAcessarDocumento(getTitular(), getLotaTitular(),
						builder.getMob())) {
			throw new AplicacaoException(
					"Acesso permitido a usuários autorizados.");
		}

		if (!Ex.getInstance()
				.getComp()
				.podeArquivarCorrente(getTitular(), getLotaTitular(),
						builder.getMob())) {
			throw new AplicacaoException(
					"Via ou processo não pode ser arquivado(a)");
		}

		Ex.getInstance()
				.getBL()
				.arquivarCorrente(getCadastrante(), getLotaTitular(),
						builder.getMob(), mov.getDtMov(), null,
						mov.getSubscritor(), false);

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
	}

	@Transacional
	@Get("/app/expediente/mov/arquivar_permanente_gravar")
	public void aArquivarPermanenteGravar(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder);

		final ExMovimentacaoBuilder movBuilder = ExMovimentacaoBuilder
				.novaInstancia();
		final ExMovimentacao mov = movBuilder.construir(dao());

		if (!Ex.getInstance()
				.getComp()
				.podeArquivarPermanente(getTitular(), getLotaTitular(),
						builder.getMob())) {
			throw new AplicacaoException(
					"Documento não pode ser arquivado. Verifique se ele não se encontra em lotação diferente de "
							+ getLotaTitular().getSigla());
		}

		Ex.getInstance()
				.getBL()
				.arquivarPermanente(getCadastrante(), getLotaTitular(),
						builder.getMob(), mov.getDtMov(), mov.getSubscritor());

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
	}

	@Transacional
	@Get("/app/expediente/mov/reabrir_gravar")
	public void aReabrirGravar(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder);

		final ExMovimentacaoBuilder movBuilder = ExMovimentacaoBuilder
				.novaInstancia();
		final ExMovimentacao mov = movBuilder.construir(dao());

		if (!Ex.getInstance()
				.getComp()
				.podeDesarquivarCorrente(getTitular(), getLotaTitular(),
						builder.getMob())) {
			throw new AplicacaoException("Via não pode ser reaberta");
		}

		Ex.getInstance()
				.getBL()
				.desarquivarCorrente(getCadastrante(), getLotaTitular(),
						builder.getMob(), mov.getDtMov(), mov.getSubscritor());

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
	}

	@Transacional
	@Get("/app/expediente/mov/desarquivar_intermediario_gravar")
	public void aDesarquivarIntermediarioGravar(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder);

		final ExMovimentacaoBuilder movBuilder = ExMovimentacaoBuilder
				.novaInstancia();
		final ExMovimentacao mov = movBuilder.construir(dao());

		if (!Ex.getInstance()
				.getComp()
				.podeDesarquivarIntermediario(getTitular(), getLotaTitular(),
						builder.getMob())) {
			throw new AplicacaoException(
					"Documento não pode ser retirado do arquivo intermediário. Verifique se ele não se encontra em lotação diferente de "
							+ getLotaTitular().getSigla());
		}

		Ex.getInstance()
				.getBL()
				.desarquivarIntermediario(getCadastrante(), getLotaTitular(),
						builder.getMob(), mov.getDtMov(), mov.getSubscritor());

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
	}

	@Get("/app/expediente/mov/assinar_despacho_lote")
	public void aAssinarDespachoLote() {
		final List<ExMovimentacao> itensComoSubscritor = dao()
				.listarDespachoPendenteAssinatura(getTitular());

		final List<ExMovimentacao> itens = new ArrayList<ExMovimentacao>();
		final List<ExMovimentacao> movimentacoesQuePodemSerAssinadasComSenha = new ArrayList<ExMovimentacao>();

		for (ExMovimentacao mov : itensComoSubscritor) {
			if (!mov.isAssinada() && !mov.isCancelada()) {
				itens.add(mov);

				if (Ex.getInstance()
						.getComp()
						.podeAssinarMovimentacaoComSenha(getTitular(),
								getLotaTitular(), mov)) {
					movimentacoesQuePodemSerAssinadasComSenha.add(mov);
				}
			}

		}

		result.include("itens", itens);
		result.include("movimentacoesQuePodemSerAssinadasComSenha",
				movimentacoesQuePodemSerAssinadasComSenha);
	}

	@Transacional
	@Get("/app/expediente/mov/receber")
	public void aReceber(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder);

		final ExMovimentacaoBuilder movBuilder = ExMovimentacaoBuilder
				.novaInstancia();
		final ExMovimentacao mov = movBuilder.construir(dao());

		if (!Ex.getInstance().getComp()
				.podeReceber(getTitular(), getLotaTitular(), builder.getMob())) {
			throw new AplicacaoException("Documento não pode ser recebido");
		}

		Ex.getInstance()
				.getBL()
				.receber(getCadastrante(), getLotaTitular(), builder.getMob(),
						mov.getDtMov());

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
	}
	
	@Transacional
	@Get("/app/expediente/mov/solicitar_assinatura")
	public void aSolicitarAssinatura(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		final ExDocumento doc = buscarDocumento(builder);

		if (!Ex.getInstance()
				.getComp()
				.podeSolicitarAssinatura(getTitular(), getLotaTitular(),
						doc)) {
			throw new AplicacaoException("Não é possível revisar");
		}
		Ex.getInstance()
			.getBL()
			.solicitarAssinatura(getCadastrante(), getLotaTitular(), doc);

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
	}


	@Get("/app/expediente/mov/referenciar")
	public void aReferenciar(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		final ExDocumento doc = buscarDocumento(builder);

		if (!Ex.getInstance()
				.getComp()
				.podeReferenciar(getTitular(), getLotaTitular(),
						builder.getMob())) {
			throw new AplicacaoException("Não é possível fazer vinculação");
		}

		result.include("sigla", sigla);
		result.include("doc", doc);
		result.include("mob", builder.getMob());
		result.include("request", getRequest());
		result.include("titularSel", new DpPessoaSelecao());
		result.include("subscritorSel", new DpPessoaSelecao());
		result.include("documentoRefSel", new ExDocumentoSelecao());
	}

	@Post("app/expediente/mov/prever")
	public void preve(final String sigla, final String dtMovString,
			final DpPessoaSelecao subscritorSel, final boolean substituicao,
			final DpPessoaSelecao titularSel, final String nmFuncaoSubscritor,
			final long idTpDespacho, final long idResp,
			final List<ExTipoDespacho> tiposDespacho, final String descrMov,
			final List<Map<Integer, String>> listaTipoResp,
			final int tipoResponsavel,
			final DpLotacaoSelecao lotaResponsavelSel,
			final DpPessoaSelecao responsavelSel,
			final CpOrgaoSelecao cpOrgaoSel, final String dtDevolucaoMovString,
			final String obsOrgao, final String protocolo) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(builder);

		ExMovimentacao mov;

		if (builder.getId() != null) {
			mov = daoMov(builder.getId());
		} else {

			final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
					.novaInstancia();
			movimentacaoBuilder.setDtMovString(dtMovString)
					.setSubscritorSel(subscritorSel).setMob(builder.getMob())
					.setSubstituicao(substituicao).setTitularSel(titularSel)
					.setNmFuncaoSubscritor(nmFuncaoSubscritor)
					.setIdTpDespacho(idTpDespacho).setDescrMov(descrMov)
					.setLotaResponsavelSel(lotaResponsavelSel)
					.setResponsavelSel(responsavelSel)
					.setDtDevolucaoMovString(dtDevolucaoMovString)
					.setCpOrgaoSel(cpOrgaoSel).setObsOrgao(obsOrgao)
					.setCadastrante(getCadastrante());
			mov = movimentacaoBuilder.construir(dao());
		}

		if (param("processar_modelo") != null) {
			result.forwardTo(this).processaModelo(mov);
		} else {
			ExModelo modeloDespachoAutomatico = getModeloDespachoAutomatico();
			result.include("par", getRequest().getParameterMap());
			result.include("modelo", modeloDespachoAutomatico);
			result.include("nmArqMod", modeloDespachoAutomatico.getNmArqMod());
			result.include("mov", mov);
		}
	}

	private void processaModelo(final ExMovimentacao mov) {
		ExModelo modeloDespachoAutomatico = getModeloDespachoAutomatico();
		result.include("par", getRequest().getParameterMap());
		result.include("modelo", modeloDespachoAutomatico);
		result.include("nmArqMod", modeloDespachoAutomatico.getNmArqMod());
		result.include("mov", mov);
	}

	@Transacional
	@Post("/app/expediente/mov/referenciar_gravar")
	public void aReferenciarGravar(final String sigla,
			final String dtMovString, final boolean substituicao,
			final DpPessoaSelecao titularSel,
			final DpPessoaSelecao subscritorSel,
			final ExMobilSelecao documentoRefSel) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder);

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia();
		movimentacaoBuilder.setDtMovString(dtMovString)
				.setSubstituicao(substituicao).setTitularSel(titularSel)
				.setSubscritorSel(subscritorSel).setMob(builder.getMob())
				.setDocumentoRefSel(documentoRefSel);

		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());

		if (!Ex.getInstance()
				.getComp()
				.podeReferenciar(getTitular(), getLotaTitular(),
						builder.getMob())) {
			throw new AplicacaoException("Não é possível fazer vinculação");
		}
		if (mov.getExMobilRef() == null) {
			throw new AplicacaoException(
					"Não foi selecionado um documento para a vinculação");
		}

		if (mov.getExDocumento().isEletronico()) {
			mov.setSubscritor(getTitular());
		}

		Ex.getInstance()
				.getBL()
				.referenciarDocumento(getCadastrante(), getLotaTitular(),
						builder.getMob(), mov.getExMobilRef(), mov.getDtMov(),
						mov.getSubscritor(), mov.getTitular());

		ExDocumentoController.redirecionarParaExibir(result, mov
				.getExDocumento().getSigla());
	}

	@Post("/app/expediente/mov/transferir")
	@Get("/app/expediente/mov/transferir")
	public void aTransferir(final String sigla, final Long idTpDespacho,
			final Integer tipoResponsavel, final int postback,
			final String dtMovString, final DpPessoaSelecao subscritorSel,
			final boolean substituicao, final DpPessoaSelecao titularSel,
			final String nmFuncaoSubscritor, final long idResp,
			final List<ExTipoDespacho> tiposDespacho, final String descrMov,
			final DpLotacaoSelecao lotaResponsavelSel,
			final DpPessoaSelecao responsavelSel,
			final CpOrgaoSelecao cpOrgaoSel, final String dtDevolucaoMovString,
			final String obsOrgao, final String protocolo) {

		this.setPostback(postback);

		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		final ExDocumento doc = buscarDocumento(builder);
		final DpPessoaSelecao subscritorSelFinal = Optional.fromNullable(
				subscritorSel).or(new DpPessoaSelecao());
		final DpLotacaoSelecao lotaResponsavelSelFinal = Optional.fromNullable(
				lotaResponsavelSel).or(new DpLotacaoSelecao());
		final DpPessoaSelecao responsavelSelFinal = Optional.fromNullable(
				responsavelSel).or(new DpPessoaSelecao());
		final DpPessoaSelecao titularSelFinal = Optional.fromNullable(
				titularSel).or(new DpPessoaSelecao());
		final CpOrgaoSelecao cpOrgaoSelecaoFinal = Optional.fromNullable(
				cpOrgaoSel).or(new CpOrgaoSelecao());
		final ExMovimentacao ultMov = builder.getMob().getUltimaMovimentacao();

		Integer tipoResponsavelFinal = Optional.fromNullable(tipoResponsavel)
				.or(DEFAULT_TIPO_RESPONSAVEL);

		if (getRequest().getAttribute("postback") == null) {
			if (ultMov.getLotaDestinoFinal() != null) {
				lotaResponsavelSelFinal.buscarPorObjeto(ultMov
						.getLotaDestinoFinal());
				tipoResponsavelFinal = 1;
			}
			if (ultMov.getDestinoFinal() != null) {
				responsavelSelFinal.buscarPorObjeto(ultMov.getDestinoFinal());
				tipoResponsavelFinal = 2;
			}
		}

		if (!(Ex.getInstance()
				.getComp()
				.podeTransferir(getTitular(), getLotaTitular(),
						builder.getMob()) || Ex
				.getInstance()
				.getComp()
				.podeDespachar(getTitular(), getLotaTitular(), builder.getMob()))) {
			throw new AplicacaoException(
					"Não é possível fazer despacho nem transferência");
		}

		result.include("ehPublicoExterno", AcessoConsulta.ehPublicoExterno(getTitular()));
		result.include("doc", doc);
		result.include("mob", builder.getMob());
		result.include("postback", this.getPostback());
		result.include("sigla", sigla);
		result.include("tiposDespacho", this.getTiposDespacho(builder.getMob()));
		result.include("listaTipoResp", this.getListaTipoResp());
		result.include("tipoResponsavel", tipoResponsavelFinal);
		result.include("subscritorSel", subscritorSelFinal);
		result.include("titularSel", titularSelFinal);
		result.include("lotaResponsavelSel", lotaResponsavelSelFinal);
		result.include("responsavelSel", responsavelSelFinal);
		result.include("idTpDespacho", idTpDespacho);
		result.include("cpOrgaoSel", cpOrgaoSelecaoFinal);
		result.include("dtMovString", dtMovString);
		result.include("substituicao", substituicao);
		result.include("nmFuncaoSubscritor", nmFuncaoSubscritor);
		result.include("idResp", idResp);
		result.include("descrMov", descrMov);
		result.include("obsOrgao", obsOrgao);
		result.include("protocolo", OPCAO_MOSTRAR.equals(protocolo));
		result.include("dtDevolucaoMovString", dtDevolucaoMovString);
	}

	@Transacional
	@Post("/app/expediente/mov/transferir_gravar")
	public void transferirGravar(final int postback, final String sigla,
			final String dtMovString, final DpPessoaSelecao subscritorSel,
			final boolean substituicao, final DpPessoaSelecao titularSel,
			final String nmFuncaoSubscritor, final long idTpDespacho,
			final long idResp, final List<ExTipoDespacho> tiposDespacho,
			final String descrMov,
			final List<Map<Integer, String>> listaTipoResp,
			final int tipoResponsavel,
			final DpLotacaoSelecao lotaResponsavelSel,
			final DpPessoaSelecao responsavelSel,
			final CpOrgaoSelecao cpOrgaoSel, final String dtDevolucaoMovString,
			final String obsOrgao, final String protocolo) throws Exception {
		this.setPostback(postback);

		if(dtDevolucaoMovString != null && !"".equals(dtDevolucaoMovString.trim())) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        Date dtDevolucao = sdf.parse(dtDevolucaoMovString);		
			
	        if (SigaMessages.isSigaSP()) {
	        	if (!DateUtils.isSameDay(new Date(), dtDevolucao) && dtDevolucao.before(new Date())) {
	        		result.include("msgCabecClass", "alert-danger");
	        		result.include("mensagemCabec", "Data de devolução não pode ser anterior à data de hoje.");
	        		result.forwardTo(this).aTransferir(
	        				sigla, idTpDespacho, tipoResponsavel, postback, dtMovString, subscritorSel, 
	        				substituicao, titularSel, nmFuncaoSubscritor, idResp, tiposDespacho, descrMov, 
	        				lotaResponsavelSel, responsavelSel, cpOrgaoSel, dtDevolucaoMovString, obsOrgao, protocolo);
	        		
	        			return;
	        	}
	        }
		}
			
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder);
		
		if(responsavelSel != null) {
			Boolean podeTramitar = Boolean.FALSE;
			List<ExMovimentacao> listaMov = new ArrayList<ExMovimentacao>();
			listaMov.addAll(builder.getMob().getDoc().getMobilGeral().getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_RESTRINGIR_ACESSO));
		
			for (ExMovimentacao exMovimentacao : listaMov) {
				if(exMovimentacao.getSubscritor().equals(responsavelSel.getObjeto())) {
					podeTramitar = Boolean.TRUE;
					break;
				}
			}
			if(!listaMov.isEmpty() && !podeTramitar) {
				throw new AplicacaoException(
						"Documento não pode ser tramitado para esta pessoa/lotação.");
			}
		}

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia();
		movimentacaoBuilder.setDtMovString(dtMovString)
				.setSubscritorSel(subscritorSel).setMob(builder.getMob())
				.setSubstituicao(substituicao).setTitularSel(titularSel)
				.setNmFuncaoSubscritor(nmFuncaoSubscritor)
				.setIdTpDespacho(idTpDespacho).setDescrMov(descrMov)
				.setLotaResponsavelSel(lotaResponsavelSel)
				.setResponsavelSel(responsavelSel)
				.setDtDevolucaoMovString(dtDevolucaoMovString)
				.setCpOrgaoSel(cpOrgaoSel).setObsOrgao(obsOrgao);

		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());

		final ExMovimentacao UltMov = builder.getMob()
				.getUltimaMovimentacaoNaoCancelada();
		if ((mov.getLotaResp() != null && mov.getResp() == null
				&& UltMov.getLotaResp() != null && UltMov.getResp() == null && UltMov
				.getLotaResp().equivale(mov.getLotaResp()))
				|| (mov.getResp() != null && UltMov.getResp() != null && UltMov
						.getResp().equivale(mov.getResp()))) {
			throw new AplicacaoException(
					"Novo responsável não pode ser igual ao atual");
		}

		if (!Ex.getInstance().getComp()
				.podeReceberPorConfiguracao(mov.getResp(), mov.getLotaResp())) {
			throw new AplicacaoException(
					"Destinatário não pode receber documentos");
		}
		
		if (!Ex.getInstance().getComp()
				.podeTramitarPara(getTitular(), getLotaTitular(), responsavelSel.getObjeto(), lotaResponsavelSel.getObjeto())) {
			throw new AplicacaoException(
					"Documento não pode ser tramitado para o destinário selecionado");
		}
		
		if (mov.getDtFimMov() != null && !Data.dataDentroSeculo21(mov.getDtFimMov()))
			throw new AplicacaoException("Data de devolução inválida, deve estar entre o ano 2000 e ano 2100");	

		
		if(!Ex.getInstance().getConf().podePorConfiguracao(builder.getMob().getExDocumento().getExModelo(),CpTipoConfiguracao.TIPO_CONFIG_TRAMITAR_SEM_CAPTURADO)) {
			Boolean podeTramitar = Boolean.FALSE;
			Set<ExMobil> mobilsJuntados = builder.getMob().getDoc().getMobilDefaultParaReceberJuntada().getJuntados();

			for (ExMobil exMobil : mobilsJuntados) {
				if(exMobil.getDoc().isCapturado()) {
					podeTramitar = Boolean.TRUE;
					break;
				}
			}

			if(!podeTramitar) {
				result.include("msgCabecClass", "alert-danger");
	    		result.include("mensagemCabec", "Para tramitar é necessário incluir um documento do tipo capturado.");
	    		result.forwardTo(this).aTransferir(
	    				sigla, idTpDespacho, tipoResponsavel, postback, dtMovString, subscritorSel, 
	    				substituicao, titularSel, nmFuncaoSubscritor, idResp, tiposDespacho, descrMov, 
	    				lotaResponsavelSel, responsavelSel, cpOrgaoSel, dtDevolucaoMovString, obsOrgao, protocolo);    		
	    			return;
			}
		}
		
		if (!(Ex.getInstance()
				.getComp()
				.podeTransferir(getTitular(), getLotaTitular(),
						builder.getMob()) || Ex
				.getInstance()
				.getComp()
				.podeDespachar(getTitular(), getLotaTitular(), builder.getMob()))) {
			throw new AplicacaoException(
					"Não é possível tramitar");
		}

		Ex.getInstance()
				.getBL()
				.transferir(mov.getOrgaoExterno(), mov.getObsOrgao(),
						getCadastrante(), getLotaTitular(), builder.getMob(),
						mov.getDtMov(), mov.getDtIniMov(), mov.getDtFimMov(),
						mov.getLotaResp(), mov.getResp(),
						mov.getLotaDestinoFinal(), mov.getDestinoFinal(),
						mov.getSubscritor(), mov.getTitular(),
						mov.getExTipoDespacho(), false, mov.getDescrMov(),
						movimentacaoBuilder.getConteudo(),
						mov.getNmFuncaoSubscritor(), false, false);

		if (protocolo != null && protocolo.equals(OPCAO_MOSTRAR)) {
			ExMovimentacao ultimaMovimentacao = builder.getMob()
					.getUltimaMovimentacao();
			
			if (SigaMessages.isSigaSP()) {
				result.redirectTo("/app/expediente/mov/protocolo_unitario_sp?popup=false&sigla=" + sigla
						+ "&id=" + ultimaMovimentacao.getIdMov());
			} else {
				result.redirectTo("/app/expediente/mov/protocolo_unitario?popup=false&sigla=" + sigla
						+ "&id=" + ultimaMovimentacao.getIdMov());
			}
			
			
		} else {
			ExDocumentoController.redirecionarParaExibir(result, builder.getMob().getExDocumento().getSigla());
		}
	}

	@Get
	@Post
	@Path("/app/expediente/mov/fechar_popup")
	public void fecharPopup() {
//		System.out.println("popup fechado.");
	}

	@Get("/app/expediente/mov/transferido")
	public void transferido(String sigla, Long idMov) {
		final BuscaDocumentoBuilder docBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla).setId(idMov);
		final ExDocumento doc = buscarDocumento(docBuilder, false);

		result.include("mov", docBuilder.getMov());
		result.include("doc", doc);
	}

	@Transacional
	@Get("app/expediente/mov/encerrar_volume")
	public void encerrarVolumeGravar(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder);

		final ExMovimentacao mov = ExMovimentacaoBuilder.novaInstancia()
				.construir(dao());

		if (builder.getMob().isVolumeEncerrado()) {
			throw new AplicacaoException(
					"Não é permitido encerrar um volume já encerrado.");
		}

		Ex.getInstance()
				.getBL()
				.encerrarVolume(getCadastrante(), getLotaTitular(), builder.getMob(), mov.getDtMov(), mov.getSubscritor(), mov.getTitular(),
						mov.getNmFuncaoSubscritor(), false);
		ExDocumentoController.redirecionarParaExibir(result, builder.getMob().getExDocumento().getSigla());
	}

	@Get("/app/expediente/mov/anotar")
	public void aAnotar(final String sigla, final String descrMov) {
		final BuscaDocumentoBuilder documentoBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		final ExDocumento documento = buscarDocumento(documentoBuilder);

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia().setMob(documentoBuilder.getMob());

		final ExMovimentacao movimentacao = movimentacaoBuilder
				.construir(dao());

		if (!Ex.getInstance()
				.getComp()
				.podeFazerAnotacao(getTitular(), getLotaTitular(),
						documentoBuilder.getMob())) {
			throw new AplicacaoException("Não é possível fazer anotação");
		}	
		
		String descricaoMov = movimentacaoBuilder.getDescrMov();
		if (descricaoMov == null) {
			descricaoMov = descrMov;
		}
				
		result.include("sigla", sigla);
		result.include("dtMovString", movimentacaoBuilder.getDtMovString());
		result.include("mob", documentoBuilder.getMob());
		result.include("mov", movimentacao);
		result.include("doc", documento);
		result.include("substituicao", movimentacaoBuilder.isSubstituicao());
		result.include("nmFuncaoSubscritor",
				movimentacaoBuilder.getNmFuncaoSubscritor());
		result.include("descrMov", descricaoMov);
		result.include("tipoResponsavel",
				this.processarTipoResponsavel(documentoBuilder.getMob()));
		result.include("obsOrgao", movimentacaoBuilder.getObsOrgao());
		result.include("subscritorSel", movimentacaoBuilder.getSubscritorSel());
		result.include("titularSel", movimentacaoBuilder.getTitularSel());
	}

	@Transacional
	@Post("/app/expediente/mov/anotar_gravar")
	public void anotar_gravar(final Integer postback, final String sigla,
			final String dtMovString, final DpPessoaSelecao subscritorSel,
			final boolean substituicao, final DpPessoaSelecao titularSel,
			final String nmFuncaoSubscritor, final String descrMov,
			final String obsOrgao, final String[] campos) {
		this.setPostback(postback);
		
		String descricaoMov = descrMov;
		if (descricaoMov != null) {
			descricaoMov = Texto.removerEspacosExtra(descricaoMov);
		}

		final ExMovimentacaoBuilder builder = ExMovimentacaoBuilder
				.novaInstancia();

		builder.setDtMovString(dtMovString).setSubscritorSel(subscritorSel)
				.setSubstituicao(substituicao).setTitularSel(titularSel)
				.setNmFuncaoSubscritor(nmFuncaoSubscritor)
				.setDescrMov(descricaoMov).setObsOrgao(obsOrgao);

		final ExMovimentacao mov = builder.construir(dao());

		final BuscaDocumentoBuilder documentoBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(documentoBuilder);

		if (!Ex.getInstance()
				.getComp()
				.podeFazerAnotacao(getTitular(), getLotaTitular(),
						documentoBuilder.getMob())) {
			throw new AplicacaoException("Não é possível fazer anotação");
		}
		
		try {
			Ex.getInstance()
			.getBL()
			.anotar(getCadastrante(), getLotaTitular(),
					documentoBuilder.getMob(), mov.getDtMov(),
					mov.getLotaResp(), mov.getResp(), mov.getSubscritor(),
					mov.getTitular(), mov.getDescrMov(),
					mov.getNmFuncaoSubscritor());		
			
			result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);			
		} catch (RegraNegocioException e) {
			result.include(SigaModal.ALERTA, SigaModal.mensagem(e.getMessage()));
			result.forwardTo(this).aAnotar(sigla, descrMov);			
		}					
	}

	@Get("/app/expediente/mov/anotar_lote")
	public void aAnotarLote() {
		final List<ExMobil> provItens = dao().consultarParaAnotarEmLote(
				getLotaTitular());

		final List<ExMobil> itens = new ArrayList<ExMobil>();

		for (ExMobil m : provItens) {
			if (!m.isApensado()
					&& Ex.getInstance()
							.getComp()
							.podeAcessarDocumento(getTitular(),
									getLotaTitular(), m)) {
				itens.add(m);
			}
		}
		result.include("itens", itens);
		result.include("subscritorSel", new DpPessoaSelecao());
		result.include("titularSel", new DpPessoaSelecao());
	}

	@Transacional
	@Post("/app/expediente/mov/anotar_lote_gravar")
	public void aAnotarLoteGravar(final Integer postback,
			final String dtMovString, final DpPessoaSelecao subscritorSel,
			final boolean substituicao, final DpPessoaSelecao titularSel,
			final String nmFuncaoSubscritor, final String descrMov,
			final String obsOrgao, final String[] campos) {
		this.setPostback(postback);

		ExMovimentacaoBuilder builder = ExMovimentacaoBuilder.novaInstancia();

		builder.setDtMovString(dtMovString).setSubscritorSel(subscritorSel)
				.setSubstituicao(substituicao).setTitularSel(titularSel)
				.setNmFuncaoSubscritor(nmFuncaoSubscritor)
				.setDescrMov(descrMov).setObsOrgao(obsOrgao);

		final ExMovimentacao mov = builder.construir(dao());

		final Pattern p = Pattern.compile("chk_([0-9]+)");

		for (final String s : getPar().keySet()) {
			if (s.startsWith("chk_") && param(s).equals("true")) {
				final Matcher m = p.matcher(s);
				if (!m.find()) {
					throw new AplicacaoException(
							"Não foi possível ler a Id do documento e o número da via.");
				}
				final ExMobil mob = dao().consultar(Long.valueOf(m.group(1)),
						ExMobil.class, false);

				Ex.getInstance()
						.getBL()
						.anotar(getCadastrante(), getLotaTitular(), mob,
								mov.getDtMov(), mov.getLotaResp(),
								mov.getResp(), mov.getSubscritor(),
								mov.getTitular(), mov.getDescrMov(),
								mov.getNmFuncaoSubscritor());
			}
		}

		result.include("dtMovString", dtMovString);
		result.include("subscritorSel", subscritorSel);
		result.include("titularSel", titularSel);
		result.include("nmFuncaoSubscritor", nmFuncaoSubscritor);
		result.include("descrMov", descrMov);
		result.include("obsOrgao", obsOrgao);
		result.include("substituicao", substituicao);
		result.redirectTo("/app/expediente/mov/anotar_lote");
	}

	@Get("/app/expediente/mov/vincularPapel")
	public void aVincularPapel(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder);

		if (!Ex.getInstance()
				.getComp()
				.podeFazerVinculacaoPapel(getTitular(), getLotaTitular(),
						builder.getMob())) {
			throw new AplicacaoException(
					"Não é possível fazer vinculação de papel");
		}

		final List<ExPapel> papeis = this.getListaExPapel();
		
		if (builder.getMob().getDoc().isAssinadoPorTodosOsSignatariosComTokenOuSenha()) {			
			for (Iterator<ExPapel> iter = papeis.listIterator(); iter.hasNext(); ) {
			    ExPapel p = iter.next();
			    if (p.getIdPapel() == ExPapel.PAPEL_REVISOR) {
			        iter.remove();
			    }
			}			
		}
		
		if (SigaMessages.isSigaSP()) {
			for (Iterator<ExPapel> iter = papeis.listIterator(); iter.hasNext(); ) {
			    ExPapel p = iter.next();
			    if (p.getIdPapel() != ExPapel.PAPEL_GESTOR && p.getIdPapel() != ExPapel.PAPEL_INTERESSADO) {
			        iter.remove();
			    }
			}
		}
		
		result.include("sigla", sigla);
		result.include("mob", builder.getMob());
		result.include("listaTipoRespPerfil", this.getListaTipoRespPerfil());
		result.include("listaExPapel", papeis);
		result.include("responsavelSel", new DpPessoaSelecao());
		result.include("lotaResponsavelSel", new DpLotacaoSelecao());
	}

	@Transacional
	@Post("/app/expediente/mov/vincularPapel_gravar")
	public void vincularPapel_gravar(final int postback, final String sigla,
			final String dtMovString, final int tipoResponsavel,
			final DpPessoaSelecao responsavelSel,
			final DpLotacaoSelecao lotaResponsavelSel, final Long idPapel) {
		this.setPostback(postback);

		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder);

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia();
		movimentacaoBuilder.setDtMovString(dtMovString)
				.setResponsavelSel(responsavelSel)
				.setLotaResponsavelSel(lotaResponsavelSel).setIdPapel(idPapel);

		if (responsavelSel == null || tipoResponsavel == 2) {
			movimentacaoBuilder.setResponsavelSel(new DpPessoaSelecao());
		}

		if (lotaResponsavelSel == null || tipoResponsavel == 1) {
			movimentacaoBuilder.setLotaResponsavelSel(new DpLotacaoSelecao());
		}

		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());

		if (mov.getResp() == null && mov.getLotaResp() == null) {
			throw new AplicacaoException(
					"Não foi informado o responsável ou lotação responsável para a vinculação de papel ");
		}

		if (mov.getResp() != null) {
			mov.setDescrMov(mov.getExPapel().getDescPapel() + ":"
					+ mov.getResp().getDescricaoIniciaisMaiusculas());
		} else {
			if (mov.getLotaResp() != null) {
				mov.setDescrMov(mov.getExPapel().getDescPapel() + ":"
						+ mov.getLotaResp().getDescricaoIniciaisMaiusculas());
			}
		}

		if (!Ex.getInstance()
				.getComp()
				.podeFazerVinculacaoPapel(getTitular(), getLotaTitular(),
						builder.getMob())) {
			throw new AplicacaoException(
					"Não é possível fazer vinculação de papel");
		}
		
		if(responsavelSel != null) {
			Boolean podeVincular = Boolean.FALSE;
			List<ExMovimentacao> listaMov = new ArrayList<ExMovimentacao>();
			listaMov.addAll(builder.getMob().getDoc().getMobilGeral().getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_RESTRINGIR_ACESSO));
		
			for (ExMovimentacao exMovimentacao : listaMov) {
				if(exMovimentacao.getSubscritor().equals(responsavelSel.getObjeto())) {
					podeVincular = Boolean.TRUE;
					break;
				}
			}
			if(!listaMov.isEmpty() && !podeVincular) {
				throw new AplicacaoException(
						"Acompanhamento do documento não pode ser cadastrado para o usuário selecionado.");
			}
		}

		Ex.getInstance()
				.getBL()
				.vincularPapel(getCadastrante(), getLotaTitular(),
						builder.getMob(), mov.getDtMov(), mov.getLotaResp(),
						mov.getResp(), mov.getSubscritor(), mov.getTitular(),
						mov.getDescrMov(), mov.getNmFuncaoSubscritor(),
						mov.getExPapel());

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
	}

	@Get("/app/expediente/mov/marcar")
	public void aMarcar(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder.novaInstancia().setSigla(sigla);
		buscarDocumento(builder);

		ExMobil mob = builder.getMob();
		if (!Ex.getInstance().getComp().podeMarcar(getTitular(), getLotaTitular(), mob)) 
			throw new AplicacaoException("Não é possível fazer marcação");

		ExMobil mobilGeral = mob.getDoc().getMobilGeral();
		result.include("sigla", sigla);
		result.include("mob", mob);
		result.include("listaMarcadores", this.getListaMarcadoresGeraisTaxonomiaAdministrada());
		result.include("listaMarcadoresAtivos", this.getListaMarcadoresAtivos(mobilGeral));
		result.include("dataLimite", this.getDataLimiteDemanda(mobilGeral));
	}
	
	@Transacional
	@Get("/app/expediente/mov/recalcular_acesso")
	public void aRecalcularAcesso(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder.novaInstancia().setSigla(sigla);
		buscarDocumento(builder);

		Ex.getInstance().getBL().atualizarDnmAcesso(builder.getMob().getDoc()); 

		result.use(Results.http()).body("Acesso: " + builder.getMob().doc().getDnmAcesso() 
				+ "\n Alterado: " + builder.getMob().doc().getDnmDtAcesso()).setStatusCode(200);
	}

	private Set<CpMarcador> getListaMarcadoresAtivos(ExMobil mob) {
		Set<CpMarcador> set = new HashSet<CpMarcador>();
		for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
			if (mov.getExTipoMovimentacao().getId().equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO) && !mov.isCancelada()) {
				set.add(mov.getMarcador());
			}
		}
		return set;
	}

	private Date getDataLimiteDemanda(ExMobil mob) {
		Date dataLimite = null;
		for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
			if (mov.getExTipoMovimentacao().getId().equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO)
					&& !mov.isCancelada() && mov.getMarcador().isDemandaJudicial()) {
				dataLimite = mov.getDtFimMov();
				break;
			}
		}
		return dataLimite ;
	}

	/**
	 * Retorna a relação das opções de {@link CpMarcador Marcadores} que podem ser
	 * atribuídos a um {@link ExDocumento Documento}. A ordenação é feita aqui ao
	 * invés do método de origem pois
	 * {@link ExDao#listarCpMarcadoresGeraisTaxonomiaAdministrada()} é chamado em
	 * mais de um lugar e além disso nele é retornado a união de 2 queries
	 * diferentes.
	 * 
	 * @return opções de {@link CpMarcador Marcadores} que podem ser atribuídos a um
	 *         {@link ExDocumento Documento} devidamente ordenados de acordo com
	 *         {@link CpMarcador#getOrdem()}
	 */
	private List<CpMarcador> getListaMarcadoresGeraisTaxonomiaAdministrada() {
		List<CpMarcador> marcadores = dao().listarCpMarcadoresGeraisTaxonomiaAdministrada();
		marcadores.sort(CpMarcador.ORDEM_COMPARATOR);

		return marcadores;
	}

	/**
	 * 
	 * @param sigla
	 * @param idMarcador
	 * @param ativo
	 * @throws Exception
	 * @deprecated remover. Funcionalidade substituída por {@link #salvarMarcas(Integer, String, List, List)}
	 */
	@Deprecated
	@Transacional
	@Post("/app/expediente/mov/marcar_gravar")
	public void aMarcarGravar(final String sigla, final Long idMarcador,
			final Boolean ativo) throws Exception {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder);

		if (!Ex.getInstance().getComp()
				.podeMarcar(getTitular(), getLotaTitular(), builder.getMob()))
			throw new AplicacaoException("Não é possível fazer marcação");

		if (idMarcador == null)
			throw new AplicacaoException("Marcador deve ser informado.");

		CpMarcador m = dao().consultar(idMarcador, CpMarcador.class, false);

		Set<CpMarcador> lMarcadoresAtivos = this
				.getListaMarcadoresAtivos(builder.getMob().getDoc().getMobilGeral());

		boolean atual = lMarcadoresAtivos.contains(m);

		if (ativo != atual) {
			final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
					.novaInstancia();
			movimentacaoBuilder.setIdMarcador(idMarcador);
			final ExMovimentacao mov = movimentacaoBuilder.construir(dao());
			Ex.getInstance()
					.getBL()
					.vincularMarcador(getCadastrante(), getLotaTitular(),
							builder.getMob(), mov.getDtMov(),
							mov.getLotaResp(), mov.getResp(),
							mov.getSubscritor(), mov.getTitular(),
							mov.getDescrMov(), mov.getNmFuncaoSubscritor(),
							mov.getMarcador(), ativo, null);
		}
		resultOK();
	}

	private void salvarMarca(BuscaDocumentoBuilder builder, ExMovimentacaoBuilder movimentacaoBuilder, Long idMarcador,
			boolean ativo, String strDataLimite) throws Exception {
		movimentacaoBuilder.setIdMarcador(idMarcador);
		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());

		// A data virá da página no formato "yyyy-MM-dd"
		Date dateLimiteDemanda = (ativo && StringUtils.isNotEmpty(strDataLimite))
				? DateFormatUtils.ISO_DATE_FORMAT.parse(strDataLimite)
				: null;

		Ex.getInstance().getBL().vincularMarcador(getCadastrante(), getLotaTitular(), //
				builder.getMob(), mov.getDtMov(), //
				mov.getLotaResp(), mov.getResp(), //
				mov.getSubscritor(), mov.getTitular(), mov.getDescrMov(), mov.getNmFuncaoSubscritor(),
				mov.getMarcador(), ativo, dateLimiteDemanda);
	}

	@Transacional
	@Post("/app/expediente/mov/salvar_marcas")
	public void salvarMarcas(final Integer postback, final String sigla, List<Long> marcadoresOriginais,
			List<Long> marcadoresSelecionados, final String dataLimite, final String dataLimiteOriginal)
			throws Exception {
		// Será usado para indicar um {@link CpMarcador Marcador} de demanda judicial
		// que continua checado mas mudou de data limite.
		Predicate<Long> mudouDataLimiteDemandaPredicate = Objects.equals(dataLimite, dataLimiteOriginal) ? alwaysFalse()
				: in(CpMarcador.MARCADORES_DEMANDA_JUDICIAL);

		//if null inicializa lista de marcadores
		marcadoresOriginais = marcadoresOriginais == null ? Collections.emptyList() : marcadoresOriginais;
		marcadoresSelecionados = marcadoresSelecionados == null ? Collections.emptyList() : marcadoresSelecionados;
		
		List<Long> idMarcasARemover = isEmpty(marcadoresOriginais) ? Collections.emptyList()
				: newArrayList(filter(marcadoresOriginais,
						or(not(in(marcadoresSelecionados)), mudouDataLimiteDemandaPredicate)));
		List<Long> idMarcasAAdicionar = isEmpty(marcadoresSelecionados) ? Collections.emptyList()
				: newArrayList(filter(marcadoresSelecionados,
						or(not(in(marcadoresOriginais)), mudouDataLimiteDemandaPredicate)));

		if (!idMarcasAAdicionar.isEmpty() || !idMarcasARemover.isEmpty()) {
			final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder.novaInstancia().setSigla(sigla);
			buscarDocumento(builder);

			if (!Ex.getInstance().getComp().podeMarcar(getTitular(), getLotaTitular(), builder.getMob())) {
				throw new AplicacaoException("Não é possível fazer marcação");
			}

			final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder.novaInstancia();

			for (Long idMarcaARemover : idMarcasARemover) {
				salvarMarca(builder, movimentacaoBuilder, idMarcaARemover, false, null);
			}
			for (Long idMarcaAAdicionar : idMarcasAAdicionar) {
				salvarMarca(builder, movimentacaoBuilder, idMarcaAAdicionar, true, dataLimite);
			}
		}

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
	}

	@Get("app/expediente/mov/transferir_lote")
	public void aTransferirLote(Integer paramoffset) {
		Long tamanho = dao().consultarQuantidadeParaTransferirEmLote(getLotaTitular());

		LOGGER.debug("TAMANHO : " + tamanho);

		int offset = Objects.nonNull(paramoffset)
				? ((paramoffset >= tamanho) ? ((paramoffset / MAX_ITENS_PAGINA_TRAMITACAO_LOTE - 1) * MAX_ITENS_PAGINA_TRAMITACAO_LOTE)
						: paramoffset)
				: 0;

		final List<ExMobil> provItens = (tamanho <= MAX_ITENS_PAGINA_TRAMITACAO_LOTE)
				? dao().consultarParaTransferirEmLote(getLotaTitular(), null, null)
				: dao().consultarParaTransferirEmLote(getLotaTitular(), offset, MAX_ITENS_PAGINA_TRAMITACAO_LOTE);

		final DpPessoaSelecao titularSel = new DpPessoaSelecao();
		final DpPessoaSelecao subscritorSel = new DpPessoaSelecao();
		final DpLotacaoSelecao lotaResponsavelSel = new DpLotacaoSelecao();
		final DpPessoaSelecao responsavelSel = new DpPessoaSelecao();
		final CpOrgaoSelecao cpOrgaoSel = new CpOrgaoSelecao();

		result.include("listaTipoResp", this.getListaTipoResp());
		result.include("tiposDespacho", this.getTiposDespacho(null));
		result.include("itens", provItens);
		result.include("titularSel", titularSel);
		result.include("subscritorSel", subscritorSel);
		result.include("lotaResponsavelSel", lotaResponsavelSel);
		result.include("responsavelSel", responsavelSel);
		result.include("cpOrgaoSel", cpOrgaoSel);

		result.include("maxItems", MAX_ITENS_PAGINA_TRAMITACAO_LOTE);
		result.include("tamanho", tamanho);
		result.include("currentPageNumber", (offset / MAX_ITENS_PAGINA_TRAMITACAO_LOTE + 1));
	}

	@Transacional
	@Post("app/expediente/mov/transferir_lote_gravar")
	public void aTransferirLoteGravar(
			final DpLotacaoSelecao lotaResponsavelSel, final CpOrgaoSelecao cpOrgaoSel,
			final String dtDevolucaoMovString, final String obsOrgao, final String protocolo, final Long tpdall,
			final String txtall, final DpPessoaSelecao responsavelSel, final List<Long> documentosSelecionados, Integer paramoffset)
			throws Exception {

		if (dtDevolucaoMovString != null && !"".equals(dtDevolucaoMovString.trim())) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date dtDevolucao = sdf.parse(dtDevolucaoMovString);

			if (SigaMessages.isSigaSP()) {
				if (!DateUtils.isSameDay(new Date(), dtDevolucao) && dtDevolucao.before(new Date())) {
					result.include("msgCabecClass", "alert-danger");
					result.include("mensagemCabec", "Data de devolução não pode ser anterior à data de hoje.");
					result.forwardTo(this).aTransferirLote(paramoffset);
					return;
				}
			}
		}

		final ExMovimentacaoBuilder builder = ExMovimentacaoBuilder.novaInstancia();
		builder//.setDtMovString(dtMovString).setSubscritorSel(subscritorSel).setSubstituicao(substituicao)
				//.setTitularSel(titularSel).setNmFuncaoSubscritor(nmFuncaoSubscritor)
				.setLotaResponsavelSel(lotaResponsavelSel).setCpOrgaoSel(cpOrgaoSel).setObsOrgao(obsOrgao)
				.setDtDevolucaoMovString(dtDevolucaoMovString).setResponsavelSel(responsavelSel);

		final ExMovimentacao mov = builder.construir(dao());

		boolean despaUnico = false;
		final Date dt = dao().dt();
		mov.setDtIniMov(dt);
		ExMobil nmobil = new ExMobil();
		final HashMap<ExMobil, AplicacaoException> MapMensagens = new HashMap<ExMobil, AplicacaoException>();
		final List<ExMobil> mobeis = new ArrayList<ExMobil>();
		final List<ExMobil> mobilSucesso = new ArrayList<ExMobil>();

		if (Objects.isNull(mov.getResp()) && Objects.isNull(mov.getLotaResp())
				&& Objects.isNull(mov.getOrgaoExterno())) {
			throw new AplicacaoException("Não foi definido o destino da transferência.");
		}
		if (tpdall != null && tpdall != 0) {
			despaUnico = true;
		}

		AplicacaoException msgErroNivelAcessoso = null;

		for (Long idDocumento : documentosSelecionados) {
			try {
				final Long idTpDespacho = despaUnico ? tpdall : 0L;
				ExTipoDespacho tpd = dao().consultar(idTpDespacho, ExTipoDespacho.class, false);

				final ExMobil mobil = dao().consultar(idDocumento, ExMobil.class, false);

				if (!Ex.getInstance().getComp().podeAcessarDocumento(getTitular(), getLotaTitular(), mobil)) {
					if (msgErroNivelAcessoso == null) {
						msgErroNivelAcessoso = new AplicacaoException(
								"O documento não pode ser transferido por estar inacessível ao usuário.");
					}
					if (!(msgErroNivelAcessoso == null)) {
						MapMensagens.put(mobil, msgErroNivelAcessoso);
					}
				} else {
					String txt = despaUnico ? (StringUtils.isEmpty(txtall) ? null : txtall) : null;

					nmobil = new ExMobil();
					nmobil = mobil;
					mobeis.add(mobil);

//					LOGGER.debug(idDocumento + ": " + mov + ", " + mobil + ", " + tpd + ", " + txt);

					Ex.getInstance() //
							.getBL() //
							.transferir(mov.getOrgaoExterno(), //
									mov.getObsOrgao(), getCadastrante(), //
									getLotaTitular(), mobil, //
									mov.getDtMov(), dt, mov.getDtFimMov(), //
									mov.getLotaResp(), mov.getResp(), //
									mov.getLotaDestinoFinal(), //
									mov.getDestinoFinal(), //
									mov.getSubscritor(), mov.getTitular(), //
									tpd, false, txt, null, //
									mov.getNmFuncaoSubscritor(), false, //
									false);
				}
			} catch (AplicacaoException e) {
				MapMensagens.put(nmobil, e);
			}

		}

		/*
		 Protocolo não está sendo usado. Na verdade  
		if (protocolo != null && protocolo.equals(OPCAO_MOSTRAR)) {
			ExMovimentacao ultimaMovimentacao = builder.getMob()
					.getUltimaMovimentacao();
			
			if (SigaMessages.isSigaSP()) {
				result.redirectTo("/app/expediente/mov/protocolo_unitario_sp?popup=false"
//						+ "&sigla=" + sigla
						+ "&id=" + ultimaMovimentacao.getIdMov());
			} else {
				result.redirectTo("/app/expediente/mov/protocolo_unitario?popup=false"
//						+ "&sigla=" + sigla
						+ "&id=" + ultimaMovimentacao.getIdMov());
			}
			
			
		} else {
		*/
		final ArrayList<Object> arrays = montarArraysResultadosTransferenciaLote(MapMensagens, mobeis, mobilSucesso);

		result.include("mov", mov);
		result.include("itens", arrays);
		result.include("lotaTitular", mov.getLotaTitular());
//		result.include("dtMovString", dtMovString);
//		result.include("subscritorSel", subscritorSel);
//		result.include("titularSel", titularSel);
//		result.include("nmFuncaoSubscritor", nmFuncaoSubscritor);
		result.include("lotaResponsavelSel", lotaResponsavelSel);
		result.include("cpOrgaoSel", cpOrgaoSel);
//		result.include("substituicao", substituicao);
		result.include("responsavelSel", responsavelSel);
//		}
	}

	private ArrayList<Object> montarArraysResultadosTransferenciaLote(final HashMap<ExMobil, AplicacaoException> MapMensagens,
			final List<ExMobil> mobeis, final List<ExMobil> mobilSucesso) {
		final ArrayList<Object> al = new ArrayList<Object>();
		final ArrayList<Object> check = new ArrayList<Object>();
		final ArrayList<Object> arrays = new ArrayList<Object>();

		if (!(MapMensagens.isEmpty())) {
			for (Iterator<Entry<ExMobil, AplicacaoException>> it = MapMensagens
					.entrySet().iterator(); it.hasNext();) {
				Entry<ExMobil, AplicacaoException> excep = it.next();
				final Object[] ao = { excep.getKey(), excep.getValue().getMessage() };
//				System.out.println("Falha: " + excep.getKey().doc().getSigla());
//				System.out.println("Mensagem de erro: " + excep.getValue().getMessage());
				al.add(ao);
				throw new AplicacaoException(excep.getValue().getMessage());
			}
		}

		for (Iterator<ExMobil> it = mobeis.iterator(); it.hasNext();) {
			ExMobil mob = it.next();
			if (!(MapMensagens.containsKey(mob))) {
				mobilSucesso.add(mob);
//				System.out.println("Mobil Geral: " + mob.doc().getMobilGeral().isGeral());
				final Object[] ao = { mob.doc(), mob.getUltimaMovimentacaoNaoCancelada() };
//				System.out.println("Sucesso sigla: " + mob.doc().getSigla());
				check.add(ao);
			}
		}

		Object[] arr = al.toArray();
		Object[] arr_ = check.toArray();

		al.clear();
		check.clear();

		for (int k = 0; k < arr.length; k++) {
			al.add(arr[k]);
		}

		for (int k = 0; k < arr_.length; k++) {
			check.add(arr_[k]);
		}

		arrays.add(al);
		arrays.add(check);
		return arrays;
	}

	@Get("app/expediente/mov/arquivar_intermediario_lote")
	public void aArquivarIntermediarioLote(final String paramOffset) {
		int offset;
		try {
			offset = Integer.valueOf(paramOffset);
		} catch (Exception e) {
			offset = 0;
		}
		getP().setOffset(offset);
		final List<ExItemDestinacao> listaProv = dao()
				.consultarParaArquivarIntermediarioEmLote(getLotaTitular(),
						offset);

		final ExTopicoDestinacao digitais = new ExTopicoDestinacao("Digitais",
				true);
		final ExTopicoDestinacao fisicos = new ExTopicoDestinacao("Físicos",
				true);
		final ExTopicoDestinacao indisponiveis = new ExTopicoDestinacao(
				"Não disponíveis", false);

		for (ExItemDestinacao item : listaProv) {
			final boolean pode = Ex
					.getInstance()
					.getComp()
					.podeArquivarIntermediario(getTitular(), getLotaTitular(),
							item.getMob());
			if (pode) {
				if (item.getMob().doc().isEletronico()) {
					digitais.adicionar(item);
				} else {
					fisicos.adicionar(item);
				}
			} else {
				indisponiveis.adicionar(item);
			}
		}

		final List<ExTopicoDestinacao> listaFinal = new ArrayList<ExTopicoDestinacao>();
		listaFinal.add(digitais);
		listaFinal.add(fisicos);
		listaFinal.add(indisponiveis);

		result.include(
				"tamanho",
				dao().consultarQuantidadeParaArquivarIntermediarioEmLote(
						getLotaTitular()));
		result.include("itens", listaFinal);
	}

	@Transacional
	@Get("app/expediente/mov/arquivar_intermediario_lote_gravar")
	public void aArquivarIntermediarioLoteGravar(final Integer postback,
			final Integer paramOffset, final String dtMovString,
			final DpPessoaSelecao subscritorSel, final boolean substituicao,
			final DpPessoaSelecao titularSel, final String nmFuncaoSubscritor,
			final String descrMov, final String[] campos) {
		this.setPostback(postback);
		getP().setOffset(paramOffset);

		final ExMovimentacaoBuilder builder = ExMovimentacaoBuilder
				.novaInstancia();

		builder.setDtMovString(dtMovString).setSubscritorSel(subscritorSel)
				.setSubstituicao(substituicao).setTitularSel(titularSel)
				.setNmFuncaoSubscritor(nmFuncaoSubscritor)
				.setDescrMov(descrMov);

		final ExMovimentacao mov = builder.construir(dao());

		final Pattern p = Pattern.compile("chk_([0-9]+)");

		for (final String s : getPar().keySet()) {
			if (s.startsWith("chk_") && param(s).equals("true")) {
				final Matcher m = p.matcher(s);
				if (!m.find()) {
					throw new AplicacaoException(
							"Não foi possível ler a Id do documento e o número da via.");
				}
				final ExMobil mob = dao().consultar(Long.valueOf(m.group(1)),
						ExMobil.class, false);

				Ex.getInstance()
						.getBL()
						.arquivarIntermediario(getCadastrante(),
								getLotaTitular(), mob, mov.getDtMov(),
								mov.getSubscritor(), mov.getDescrMov());
			}
		}

		result.include("dtMovString", dtMovString);
		result.include("subscritorSel", subscritorSel);
		result.include("titularSel", titularSel);
		result.include("nmFuncaoSubscritor", nmFuncaoSubscritor);
		result.include("descrMov", descrMov);
		result.include("substituicao", substituicao);
		result.redirectTo("/app/expediente/mov/arquivar_intermediario_lote");
	}

	@Get("app/expediente/mov/arquivar_permanente_lote")
	public void aArquivarPermanenteLote(final String paramOffset) {
		int offset;
		try {
			offset = Integer.valueOf(paramOffset);
		} catch (Exception e) {
			offset = 0;
		}
		getP().setOffset(offset);
		final List<ExItemDestinacao> listaProv = dao()
				.consultarParaArquivarPermanenteEmLote(getLotaTitular(), offset);

		final ExTopicoDestinacao digitais = new ExTopicoDestinacao("Digitais",
				true);
		final ExTopicoDestinacao fisicos = new ExTopicoDestinacao("Físicos",
				true);
		final ExTopicoDestinacao indisponiveis = new ExTopicoDestinacao(
				"Não disponíveis", false);

		for (final ExItemDestinacao item : listaProv) {
			boolean pode = Ex
					.getInstance()
					.getComp()
					.podeArquivarPermanente(getTitular(), getLotaTitular(),
							item.getMob());
			if (pode) {
				if (item.getMob().doc().isEletronico()) {
					digitais.adicionar(item);
				} else {
					fisicos.adicionar(item);
				}
			} else {
				indisponiveis.adicionar(item);
			}
		}

		final List<ExTopicoDestinacao> listaFinal = new ArrayList<ExTopicoDestinacao>();
		listaFinal.add(digitais);
		listaFinal.add(fisicos);
		listaFinal.add(indisponiveis);

		result.include(
				"tamanho",
				dao().consultarQuantidadeParaArquivarPermanenteEmLote(
						getLotaTitular()));
		result.include("itens", listaFinal);
	}

	@Transacional
	@Get("app/expediente/mov/arquivar_permanente_lote_gravar")
	public void aArquivarPermanenteLoteGravar(final Integer postback,
			final Integer paramOffset, final String dtMovString,
			final DpPessoaSelecao subscritorSel, final boolean substituicao,
			final DpPessoaSelecao titularSel, final String nmFuncaoSubscritor,
			final String[] campos) {
		this.setPostback(postback);
		getP().setOffset(paramOffset);

		final ExMovimentacaoBuilder builder = ExMovimentacaoBuilder
				.novaInstancia();

		builder.setDtMovString(dtMovString).setSubscritorSel(subscritorSel)
				.setSubstituicao(substituicao).setTitularSel(titularSel)
				.setNmFuncaoSubscritor(nmFuncaoSubscritor);

		final ExMovimentacao mov = builder.construir(dao());

		final Pattern p = Pattern.compile("chk_([0-9]+)");

		String erro = "";

		try {
			for (final String s : getPar().keySet()) {
				if (s.startsWith("chk_") && param(s).equals("true")) {
					final Matcher m = p.matcher(s);
					if (!m.find()) {
						throw new AplicacaoException(
								"Não foi possível ler a Id do documento e o número da via.");
					}

					final ExMobil mob = dao().consultar(
							Long.valueOf(m.group(1)), ExMobil.class, false);

					erro = mob.getSigla();

					Ex.getInstance()
							.getBL()
							.arquivarPermanente(getCadastrante(),
									getLotaTitular(), mob, mov.getDtMov(),
									mov.getSubscritor());
				}
			}
		} catch (final Exception e) {
			throw new AplicacaoException(
					"Ocorreu um erro no arquivamento do documento" + erro
							+ ". ", 0, e);
		}

		result.include("dtMovString", dtMovString);
		result.include("subscritorSel", subscritorSel);
		result.include("titularSel", titularSel);
		result.include("nmFuncaoSubscritor", nmFuncaoSubscritor);
		result.include("substituicao", substituicao);
		result.redirectTo("/app/expediente/mov/arquivar_permanente_lote");
	}

	@Get("/app/expediente/mov/assinar_lote")
	public void assina_lote() throws Exception {
		boolean apenasComSolicitacaoDeAssinatura = !Ex.getInstance().getConf().podePorConfiguracao(getTitular(), CpTipoConfiguracao.TIPO_CONFIG_PODE_ASSINAR_SEM_SOLICITACAO);
		final List<ExDocumento> itensComoSubscritor = dao().listarDocPendenteAssinatura(getTitular(), apenasComSolicitacaoDeAssinatura);
		final List<ExDocumento> itensFinalizados = new ArrayList<ExDocumento>();

		for (final ExDocumento doc : itensComoSubscritor) {

			if (doc.isFinalizado())
				itensFinalizados.add(doc);
		}
		final List<ExDocumento> documentosQuePodemSerAssinadosComSenha = new ArrayList<ExDocumento>();

		for (final ExDocumento exDocumento : itensFinalizados) {
			if (Ex.getInstance()
					.getComp()
					.podeAssinarComSenha(getTitular(), getLotaTitular(),
							exDocumento.getMobilGeral())) {
				documentosQuePodemSerAssinadosComSenha.add(exDocumento);
			}
		}

		result.include("documentosQuePodemSerAssinadosComSenha",
				documentosQuePodemSerAssinadosComSenha);
		result.include("itensSolicitados", itensFinalizados);
		result.include("request", getRequest());
	}

	@Get("/app/expediente/mov/assinar_tudo")
	public void assina_tudo() throws Exception {
		boolean apenasComSolicitacaoDeAssinatura = !Ex.getInstance().getConf().podePorConfiguracao(getTitular(), CpTipoConfiguracao.TIPO_CONFIG_PODE_ASSINAR_SEM_SOLICITACAO);
		List<ExAssinavelDoc> assinaveis = Ex.getInstance().getBL()
				.obterAssinaveis(getTitular(), getLotaTitular(), apenasComSolicitacaoDeAssinatura);

		result.include("assinaveis", assinaveis);
		result.include("request", getRequest());
	}

	@Transacional
	@Post("/app/expediente/mov/assinar_gravar")
	public void aAssinarGravar(final String sigla, final Boolean copia,
			final String atributoAssinavelDataHora, String assinaturaB64,
			final String certificadoB64, final Boolean juntar, final Boolean tramitar, 
			final Boolean exibirNoProtocolo) throws Exception {
		try {

			final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
					.novaInstancia();
			final boolean fApplet = getRequest().getParameter("QTYDATA") != null;
			String b64Applet = null;

			if (fApplet) {
				b64Applet = recuperarAssinaturaAppletB64(builder);
			} else {
				builder.setSigla(sigla);
			}

			buscarDocumento(builder, true);
			final ExMobil mob = builder.getMob();
			final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
					.novaInstancia().setMob(mob);
			final ExMovimentacao mov = movimentacaoBuilder.construir(dao());

			if (b64Applet != null) {
				assinaturaB64 = b64Applet;
			}

			byte[] assinatura = Base64.decode(assinaturaB64);
			Date dt = dao().consultarDataEHoraDoServidor();

			byte[] certificado = Base64.decode(certificadoB64);

			if (certificado != null && certificado.length != 0) {
				dt = new Date(Long.valueOf(atributoAssinavelDataHora));
			} else {
				certificado = null;
			}

			long tpMovAssinatura = ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO;

			if (copia) {
				tpMovAssinatura = ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO;
			}

			result.include(
					"msg",
					Ex.getInstance()
							.getBL()
							.assinarDocumento(getCadastrante(),
									getLotaTitular(), mob.doc(), dt,
									assinatura, certificado, tpMovAssinatura, juntar, tramitar,
									exibirNoProtocolo));
			
		} catch (final Exception e) {
			httpError(e);
		}

		httpOK();
	}

	@Transacional
	@Post("/app/expediente/mov/assinar_senha_gravar")
	public void aAssinarSenhaGravar(String sigla, final Boolean copia, final Boolean juntar, 
			final Boolean tramitar, final Boolean exibirNoProtocolo, String nomeUsuarioSubscritor,
			String senhaUsuarioSubscritor) throws Exception {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		ExDocumento doc = buscarDocumento(builder, true);
		final ExMobil mob = builder.getMob();
		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia().setMob(mob);
		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());

		try {
			Ex.getInstance()
					.getBL()
					.assinarDocumentoComSenha(getCadastrante(),
							getLotaTitular(), doc, mov.getDtMov(),
							nomeUsuarioSubscritor, senhaUsuarioSubscritor, true,
							mov.getTitular(), copia, juntar, tramitar, exibirNoProtocolo);
		} catch (final Exception e) {
			httpError(e);
		}
		
		result.use(Results.page()).forwardTo("/WEB-INF/page/ok.jsp");
	}

	@Transacional
	@Get
	@Post
	@Path("/app/expediente/mov/assinar_mov_login_senha_gravar")
	public void aAssinarMovSenhaGravar(Long id, String sigla,
			String tipoAssinaturaMov, String nomeUsuarioSubscritor,
			String senhaUsuarioSubscritor, Boolean copia) throws Exception {
		long tpMovAssinatura = ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA;
		
		try {

		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setId(id);
		buscarDocumento(builder, true);
		final ExMobil mob = builder.getMob();

		final ExMovimentacao mov = dao().consultar(id, ExMovimentacao.class,
				false);

		if (copia
				|| (tipoAssinaturaMov != null && tipoAssinaturaMov.equals("C")))
			tpMovAssinatura = ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA;

		Ex.getInstance()
				.getBL()
				.assinarMovimentacaoComSenha(getCadastrante(),
						getLotaTitular(), mov, mov.getDtMov(),
						nomeUsuarioSubscritor, senhaUsuarioSubscritor, true,
						tpMovAssinatura);
		} catch (final Exception e) {
			httpError(e);
		}

		result.use(Results.page()).forwardTo("/WEB-INF/page/ok.jsp");
	}

	@Transacional
	@Get({ "/app/expediente/mov/cancelar_pedido_publicacao_boletim",
			"/expediente/mov/cancelar_pedido_publicacao_boletim.action" })
	public void aCancelarPedidoPublicacaoBoletim(final String sigla)
			throws Exception {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder);

		final ExMobil mob = builder.getMob();

		ExMovimentacao movPedidoBI = mob
				.getUltimaMovimentacao(ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO_BOLETIM);

		if (movPedidoBI != null && !movPedidoBI.isCancelada()) {
			Ex.getInstance()
					.getBL()
					.cancelar(getTitular(), getLotaTitular(), builder.getMob(),
							movPedidoBI, null, null, null,
							"Pedido cancelado pela unidade gestora do BI");

			// Verifica se está na base de teste
			String mensagemTeste = Ex.getInstance().getBL().mensagemDeTeste();

			StringBuffer sb = new StringBuffer(
					"Informamos que o pedido de publicação no Boletim Interno do documento "
							+ mob.getExDocumento().getCodigo()
							+ " foi cancelado pela unidade gestora do BI.\n ");

			if (mensagemTeste != null)
				sb.append("\n " + mensagemTeste + "\n");

			StringBuffer sbHtml = new StringBuffer(
					"<html><body><p>Informamos que o pedido de publicação no Boletim Interno do documento "
							+ mob.getExDocumento().getCodigo()
							+ " foi cancelado pela unidade gestora do BI.</p> ");

			if (mensagemTeste != null)
				sbHtml.append("<p><b>" + mensagemTeste + "</b></p>");

			sbHtml.append("</body></html>");

			// Envia email para o servidor que fez o pedido
			ArrayList<String> emailsSolicitantes = new ArrayList<String>();
			emailsSolicitantes.add(movPedidoBI.getCadastrante()
					.getEmailPessoaAtual());

			Correio.enviar(null,
					emailsSolicitantes.toArray(new String[emailsSolicitantes
							.size()]),
					"Cancelamento de pedido de publicação no DJE ("
							+ movPedidoBI.getLotaCadastrante()
									.getSiglaLotacao() + ") ", sb.toString(),
					sbHtml.toString());
		}

		ExDocumentoController.redirecionarParaEditar(result, null);
	}

	@Get("/app/expediente/mov/atender_pedido_publicacao")
	public void aAtenderPedidoPublicacao() throws Exception {
		if (!Ex.getInstance()
				.getConf()
				.podePorConfiguracao(
						getTitular(),
						getLotaTitular(),
						CpTipoConfiguracao.TIPO_CONFIG_ATENDER_PEDIDO_PUBLICACAO))
			throw new AplicacaoException("Operação restrita");

		result.include("itensSolicitados",
				dao().listarSolicitados(getTitular().getOrgaoUsuario()));
	}

	@Transacional
	@Get("/app/expediente/mov/atender_pedido_publicacao_gravar")
	public void aAtenderPedidoPublicacaoGravar() throws Exception {

		final Pattern p = Pattern.compile("chk_([0-9]+)");

		StringBuffer msgDocumentosErro = new StringBuffer();
		int cont = 0;

		ExDocumento doque = new ExDocumento();

		// pra cada doc selecionado na lista e passado pela URL
		for (final String s : getPar().keySet()) {
			if (s.startsWith("chk_") && param(s).equals("true")) {
				final Matcher m = p.matcher(s);
				try {
					// joga exceção se não é uma id válida
					if (!m.find())
						throw new AplicacaoException(
								"Não foi possível ler a Id do documento.");

					doque = daoDoc(Long.valueOf(m.group(1)));

					final ExMovimentacao move = doque
							.getMobilGeral()
							.getUltimaMovimentacao(
									ExTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_PUBLICACAO);

					if (!Ex.getInstance()
							.getComp()
							.podeRemeterParaPublicacaoSolicitada(getTitular(),
									getLotaTitular(), doque.getMobilGeral()))
						throw new AplicacaoException(
								"O documento não está nas condições de ser remetido");

					validarDataGravacao(move, false);

					String stipoMateria = param("tpm_" + m.group(1));
					if (stipoMateria == null)
						stipoMateria = "A";

					// remete, o que pode gerar erros
					Ex.getInstance()
							.getBL()
							.remeterParaPublicacao(getCadastrante(),
									getLotaTitular(), doque.getMobilGeral(),
									dao().dt(), getCadastrante(),
									getCadastrante(), getLotaTitular(),
									move.getDtDispPublicacao(), stipoMateria,
									move.getLotaPublicacao(),
									move.getDescrPublicacao());
				} catch (final Throwable e) {
					cont++;
					msgDocumentosErro.append(cont + ")" + doque.getCodigo()
							+ ": " + e.getMessage() + "                   ");
				}
			}
		}

		if (cont > 0)
			throw new AplicacaoException(
					"Alguns documentos não puderam ser remetidos ->  "
							+ msgDocumentosErro);

		result.redirectTo("/app/expediente/mov/atender_pedido_publicacao");
	}

	@Transacional
	@Get("/app/expediente/mov/atender_pedido_publicacao_cancelar")
	public void aAtenderPedidoPublicacaoCancelar(final String sigla)
			throws Exception {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder);

		final ExMobil mob = builder.getMob();

		if (!Ex.getInstance()
				.getComp()
				.podeAtenderPedidoPublicacao(getTitular(), getLotaTitular(),
						mob))
			throw new AplicacaoException(
					"Usuário não tem permissão de cancelar pedido de publicação no DJE.");

		ExMovimentacao movPedidoDJE = mob
				.getUltimaMovimentacao(ExTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_PUBLICACAO);

		if (movPedidoDJE != null && !movPedidoDJE.isCancelada()) {
			Ex.getInstance()
					.getBL()
					.cancelar(getTitular(), getLotaTitular(), builder.getMob(),
							movPedidoDJE, null, null, null,
							"Pedido cancelado pela unidade gestora do DJE");

			// Verifica se está na base de teste
			String mensagemTeste = Ex.getInstance().getBL().mensagemDeTeste();

			StringBuffer sb = new StringBuffer(
					"Informamos que o pedido de publicação no DJE do documento "
							+ mob.getExDocumento().getCodigo()
							+ " foi cancelado pela unidade gestora do DJE.\n ");

			if (mensagemTeste != null)
				sb.append("\n " + mensagemTeste + "\n");

			StringBuffer sbHtml = new StringBuffer(
					"<html><body><p>Informamos que o pedido de publicação no DJE do documento "
							+ mob.getExDocumento().getCodigo()
							+ " foi cancelado pela unidade gestora do DJE.</p> ");

			if (mensagemTeste != null)
				sbHtml.append("<p><b>" + mensagemTeste + "</b></p>");

			sbHtml.append("</body></html>");

			// Envia email para o servidor que fez o pedido
			ArrayList<String> emailsSolicitantes = new ArrayList<String>();
			emailsSolicitantes.add(movPedidoDJE.getCadastrante()
					.getEmailPessoaAtual());

			Correio.enviar(null,
					emailsSolicitantes.toArray(new String[emailsSolicitantes
							.size()]),
					"Cancelamento de pedido de publicação no DJE ("
							+ movPedidoDJE.getLotaCadastrante()
									.getSiglaLotacao() + ") ", sb.toString(),
					sbHtml.toString());
		}

		result.redirectTo("/app/expediente/mov/atender_pedido_publicacao");
	}

	@Transacional
	@Get("app/expediente/mov/cancelar_juntada")
	public void cancelarJuntada(String sigla) throws Exception {
		BuscaDocumentoBuilder builder = BuscaDocumentoBuilder.novaInstancia()
				.setSigla(sigla);

		buscarDocumento(builder, true);
		ExMobil mob = builder.getMob();
		
		try {
			validarCancelamentoJuntada(mob);

			ExMobil mobilJuntado = mob.getExMobilPai();
			if (mobilJuntado != null && !mobilJuntado.getDoc().isEletronico()) {
				cancelarJuntadaGravar(DEFAULT_POSTBACK, sigla, null, null, null,
						null, Boolean.FALSE);
				return;
			}
			
			result.include("mob", mob);
			result.include("request", getRequest());
			result.include("sigla", sigla);
			result.include("substituicao", Boolean.FALSE);
			result.include("subscritorSel", new DpPessoaSelecao());
			result.include("titularSel", new DpPessoaSelecao());
			result.include("validarCamposObrigatoriosForm", SigaMessages.isSigaSP());		
		} catch (RegraNegocioException e) {
			result.include(SigaModal.ALERTA, SigaModal.mensagem(e.getMessage()));
			result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
		}
	}

	@Transacional
	@Get("/app/expediente/mov/cancelar_anotacao")
	public void aCancelarAnotacao(final Long id, String redirectURL)
			throws Exception {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setId(id);
		buscarDocumento(builder);
		final ExMobil mob = builder.getMob();

		final ExMovimentacao mov = dao().consultar(id, ExMovimentacao.class,
				false);

		if (mov == null 
				|| !mov.getIdTpMov().equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANOTACAO) 
				|| mov.isCancelada()) {
			throw new AplicacaoException("Não existe a anotação a ser cancelada.");
		}
		
		try {
			Ex.getInstance()
			.getBL()
			.cancelar(getTitular(), getLotaTitular(), builder.getMob(),
					mov, null, null, null,
					"Anotação: " + mov.getDescrMov());
		} catch (final Exception e) {
			throw e;
		}

		if (redirectURL != null) {
			result.redirectTo(redirectURL);
		} else {
			ExDocumentoController
			.redirecionarParaExibir(result, mob.getSigla());
		}
	}
	
	@Transacional
	@Get("/app/expediente/mov/cancelar_ciencia")
	public void aCancelarCiencia(String sigla)
			throws Exception {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		ExMovimentacao movCiencia = null;

		buscarDocumento(builder);

		final ExMobil mob = builder.getMob();

		if (!Ex.getInstance()
				.getComp()
				.podeCancelarCiencia(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Usuário não tem permissão de cancelar ciência.");

		Set <ExMovimentacao> setMovCiente = mob.getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CIENCIA);
		ExMovimentacao movAss = mob.getUltimaMovimentacaoNaoCancelada(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA);

		if (setMovCiente != null) {
			for (ExMovimentacao mov : setMovCiente) {
				if (mov.getCadastrante() != null &&  mov.getCadastrante().equivale(getTitular())) {
					movCiencia = mov;
				}
			}
		} else {
			throw new AplicacaoException("Não existe ciência a ser cancelada para este usuário.");
		}
		
		if (movCiencia != null && !movCiencia.isCancelada()) {
			try {
				Ex.getInstance()
						.getBL()
						.cancelar(getTitular(), getLotaTitular(), builder.getMob(),
								movCiencia, null, null, null,
								"Ciência: " + movCiencia.getDescrMov());
				if (movAss != null) {
					Ex.getInstance()
					.getBL()
					.cancelar(getTitular(), getLotaTitular(), builder.getMob(),
							movAss, null, null, null,
							movAss.getDescrTipoMovimentacao() + ": " + movAss.getDescrMov());
				}
			} catch (final Exception e) {
				throw e;
			}
		}

		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}
	
	private void validarCancelamentoJuntada(ExMobil mob) {
		if (!Ex.getInstance().getComp()
				.podeCancelarJuntada(getTitular(), getLotaTitular(), mob))
			throw new RegraNegocioException("Não é possível cancelar juntada. Ação não permitida");
	}

	@Transacional
	@Post("/app/expediente/mov/cancelar_juntada_gravar")
	public void cancelarJuntadaGravar(Integer postback, String sigla,
			String dtMovString, String descrMov, DpPessoaSelecao subscritorSel,
			DpPessoaSelecao titularSel, boolean substituicao) throws Exception {

		this.setPostback(postback);
		
		try {
			if (dtMovString != null && !Data.validaDDMMYYYY(dtMovString)) { 
				throw new RegraNegocioException("Data inválida. Deve estar no formato DD/MM/AAAA e o ano deve estar neste século.");
			}
			
			ExMovimentacao mov = ExMovimentacaoBuilder.novaInstancia()
					.setDtMovString(dtMovString).setSubstituicao(substituicao)
					.setSubscritorSel(subscritorSel).setTitularSel(titularSel)
					.construir(dao());
	
			BuscaDocumentoBuilder builder = BuscaDocumentoBuilder.novaInstancia()
					.setSigla(sigla);
	
			buscarDocumento(builder, true);
	
			validarCancelamentoJuntada(builder.getMob());
		
			Ex.getInstance()
					.getBL()
					.cancelarJuntada(getCadastrante(), getLotaTitular(),
							builder.getMob(), mov.getDtMov(),
							mov.getSubscritor(), mov.getTitular(), descrMov);
			
			ExDocumentoController.redirecionarParaExibir(result, sigla);
		} catch (final RegraNegocioException e) {
			result.include(SigaModal.ALERTA, SigaModal.mensagem(e.getMessage()));			
			result.forwardTo(this).cancelarJuntada(sigla);
		}		
	}

	@Get({"/app/expediente/mov/cancelar", "/expediente/mov/cancelar.action"})
	public void cancelar(Long id) throws Exception {
		String descrMov;
		ExMovimentacao mov = dao().consultar(id, ExMovimentacao.class, false);

		BuscaDocumentoBuilder builder = BuscaDocumentoBuilder.novaInstancia()
				.setId(id);

		ExDocumento doc = buscarDocumento(builder, true);
		validarCancelar(mov, builder.getMob());

		if (SigaMessages.isSigaSP()) {
			descrMov = mov.getDescrTipoMovimentacao();
		} else {
			descrMov = "";
		}
		
		result.include("descrMov", descrMov);
		result.include("mob", builder.getMob());
		result.include("id", id);
		result.include("sigla", doc.getSigla());
		result.include("subscritorSel", new DpPessoaSelecao());
		result.include("titularSel", new DpPessoaSelecao());
		result.include("request", getRequest());
	}
	
	@Transacional
	@Get({"/app/expediente/mov/cancelarPerfil"})
	public void cancelarPerfil(String sigla, Long idPessoa, Long idLotacao) throws Exception {
		
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder.novaInstancia().setSigla(sigla);
		ExDocumento doc = buscarDocumento(builder);
		
		List<ExMovimentacao> lista = new ArrayList<ExMovimentacao>();
		lista.addAll(doc.getMobilGeral().getExMovimentacaoSet());
		
		for (ExMovimentacao mov : lista) {
			if (!mov.isCancelada()
					&& mov.getExTipoMovimentacao()
							.getId()
							.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL)) {
				if((idPessoa != null && mov != null && mov.getSubscritor() != null && mov.getSubscritor().getId().equals(idPessoa)) 
						|| (idLotacao != null && mov != null && mov.getLotaSubscritor() != null && mov.getLotaSubscritor().getId().equals(idLotacao))) {
					Ex.getInstance()
						.getBL()
						.cancelar(getTitular(), getLotaTitular(), builder.getMob(),
								mov, null, null, null,
								mov.getDescrMov());
				} 
			}
		}
		
		result.include("mob", builder.getMob());
		result.include("sigla", doc.getSigla());
		result.include("subscritorSel", new DpPessoaSelecao());
		result.include("titularSel", new DpPessoaSelecao());
		result.include("request", getRequest());
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Transacional
	@Post("/app/expediente/mov/cancelar_movimentacao_gravar")
	public void cancelarMovimentacaoGravar(Integer postback, Long id,
			String sigla, String dtMovString, boolean substituicao,
			String descrMov, DpPessoaSelecao subscritorSel,
			DpPessoaSelecao titularSel) throws Exception {

		ExMovimentacao mov = dao().consultar(id, ExMovimentacao.class, false);

		ExMovimentacaoBuilder movBuilder = ExMovimentacaoBuilder
				.novaInstancia().setDtMovString(dtMovString)
				.setSubstituicao(substituicao).setDescrMov(descrMov)
				.setSubscritorSel(subscritorSel).setTitularSel(titularSel);

		BuscaDocumentoBuilder builder = BuscaDocumentoBuilder.novaInstancia()
				.setSigla(sigla);

		buscarDocumento(builder, true);

		ExMobil mob = builder.getMob();
		ExMovimentacao movForm = movBuilder.construir(dao());

		validarCancelar(mov, mob);

		try {
			Ex.getInstance()
					.getBL()
					.cancelar(getCadastrante(), getLotaTitular(), mob, mov,
							movForm.getDtMov(), movForm.getSubscritor(),
							movForm.getTitular(), descrMov);
		} catch (final Exception e) {
			throw e;
		}
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Get("app/expediente/mov/retirar_de_edital_eliminacao")
	public void retirarDeEditalEliminacao(String sigla) throws Exception {
		BuscaDocumentoBuilder builder = BuscaDocumentoBuilder.novaInstancia()
				.setSigla(sigla);

		buscarDocumento(builder, true);

		validarRetirarEditalEliminacao(builder.getMob());

		result.include("request", getRequest());
		result.include("mob", builder.getMob());
		result.include("sigla", sigla);
		result.include("substituicao", Boolean.FALSE);
		result.include("subscritorSel", new DpPessoaSelecao());
		result.include("titularSel", new DpPessoaSelecao());
	}

	@Transacional
	@Post("/app/expediente/mov/retirar_de_edital_eliminacao_gravar")
	public void retirarDeEditalEliminacaoGravar(Integer postback, String sigla,
			String dtMovString, boolean substituicao, String descrMov,
			DpPessoaSelecao subscritorSel, DpPessoaSelecao titularSel)
			throws Exception {

		BuscaDocumentoBuilder builder = BuscaDocumentoBuilder.novaInstancia()
				.setSigla(sigla);

		buscarDocumento(builder, true);

		ExMovimentacaoBuilder movBuilder = ExMovimentacaoBuilder
				.novaInstancia().setDtMovString(dtMovString)
				.setSubstituicao(substituicao).setDescrMov(descrMov)
				.setSubscritorSel(subscritorSel).setTitularSel(titularSel);

		ExMovimentacao mov = movBuilder.construir(dao());
		ExMobil mob = builder.getMob();

		validarRetirarEditalEliminacao(mob);

		try {
			Ex.getInstance()
					.getBL()
					.retirarDeEditalEliminacao(mob, getCadastrante(),
							getLotaTitular(), mov.getSubscritor(),
							mov.getLotaSubscritor(), mov.getTitular(),
							mov.getLotaTitular(), mov.getDescrMov());
		} catch (final Exception e) {
			throw e;
		}
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Get("/app/expediente/mov/prever_data")
	public void aPreverData() throws Exception {
		try {
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date provDtDispon = format.parse(param("data"));

			DatasPublicacaoDJE DJE = new DatasPublicacaoDJE(provDtDispon);

			String apenas = param("apenasSolicitacao");

			result.include("descrFeriado", DJE.validarDataDeDisponibilizacao((apenas != null) && apenas.equals("true")));
			result.include("dtPrevPubl", format.format(DJE.getDataPublicacao()));
		} catch (Throwable t) {
		}
	}

	private void validarRetirarEditalEliminacao(ExMobil mob) {
		if (!Ex.getInstance()
				.getComp()
				.podeRetirarDeEditalEliminacao(getTitular(), getLotaTitular(),
						mob))
			throw new AplicacaoException(
					"Não é possível retirar o documento de edital de eliminação");
	}

	private void validarCancelar(ExMovimentacao mov, ExMobil mob)
			throws Exception {
		if (mov.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO) {
			if (!Ex.getInstance()
					.getComp()
					.podeCancelarAnexo(getTitular(), getLotaTitular(), mob, mov))
				throw new AplicacaoException("Não é possível cancelar anexo");
		} else if (mov.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_DE_ARQUIVO_AUXILIAR) {
			if (!Ex.getInstance()
					.getComp()
					.podeCancelarArquivoAuxiliar(getTitular(), getLotaTitular(), mob, mov))
				throw new AplicacaoException("Não é possível cancelar arquivo auxiliar");
		} else if (ExTipoMovimentacao.hasDespacho(mov.getIdTpMov())) {
			if (!Ex.getInstance()
					.getComp()
					.podeCancelarDespacho(getTitular(), getLotaTitular(), mob,
							mov))
				throw new AplicacaoException("Não é possível cancelar anexo");

		} else if (mov.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL) {
			if (!Ex.getInstance()
					.getComp()
					.podeCancelarVinculacaoPapel(getTitular(),
							getLotaTitular(), mob, mov))
				throw new AplicacaoException(
						"Não é possível cancelar definição de perfil");

		} else if (mov.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_REFERENCIA) {
			if (!Ex.getInstance()
					.getComp()
					.podeCancelarVinculacaoDocumento(getTitular(),
							getLotaTitular(), mob, mov))
				throw new AplicacaoException(
						"Não é possível cancelar o documento vinculado.");
		} else {
			if (!Ex.getInstance().getComp()
					.podeCancelar(getTitular(), getLotaTitular(), mob, mov))
				throw new AplicacaoException(
						"Não é permitido cancelar esta movimentação.");
		}
	}

	@Get("/app/expediente/mov/arquivar_intermediario")
	public void aArquivarIntermediario(String sigla) {

		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		final ExDocumento doc = buscarDocumento(builder, true);

		final ExMobil mob = builder.getMob();

		if (!Ex.getInstance().getComp()
				.podeArquivarIntermediario(getTitular(), getLotaTitular(), mob)) {
			throw new AplicacaoException(
					"Não é possível fazer arquivamento intermediário. Verifique se o documento não se encontra em lotação diferente de "
							+ getLotaTitular().getSigla());
		}

		result.include("doc", doc);
		result.include("mob", mob);
		result.include("sigla", sigla);
		result.include("substituicao", false);
		result.include("request", getRequest());
		result.include("titularSel", new DpPessoaSelecao());
		result.include("subscritorSel", new DpPessoaSelecao());

		if (doc.isEletronico()) {
			result.redirectTo("arquivar_intermediario_gravar?sigla="
					+ mob.getSigla());
		}
	}

	@Transacional
	@Get
	@Post
	@Path("/app/expediente/mov/arquivar_intermediario_gravar")
	public void aArquivarIntermediarioGravar(final String sigla,
			final Integer postback, final String dtMovString,
			final DpPessoaSelecao subscritorSel,
			final DpPessoaSelecao titularSel, final boolean substituicao,
			final String descrMov) {

		this.setPostback(postback);

		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(builder, true);

		final ExMobil mob = builder.getMob();

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia().setMob(mob).setDescrMov(descrMov)
				.setTitularSel(titularSel).setDtMovString(dtMovString)
				.setSubstituicao(substituicao).setSubscritorSel(subscritorSel);

		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());

		if (!Ex.getInstance().getComp()
				.podeArquivarIntermediario(getTitular(), getLotaTitular(), mob)) {

			throw new AplicacaoException(
					"Não é possível fazer arquivamento intermediário");
		}

		try {
			Ex.getInstance()
					.getBL()
					.arquivarIntermediario(getCadastrante(), getLotaTitular(),
							mob, mov.getDtMov(), mov.getSubscritor(),
							mov.getDescrMov());

			ExDocumentoController.redirecionarParaExibir(result, sigla);

		} catch (final Exception e) {
			throw e;
		}

	}

	@Get("/app/expediente/mov/desapensar")
	public void desapensar(String sigla, String dtMovString) throws Exception {
		BuscaDocumentoBuilder builder = BuscaDocumentoBuilder.novaInstancia()
				.setSigla(sigla);

		ExDocumento doc = buscarDocumento(builder, true);
		ExMobil mob = builder.getMob();

		if (!Ex.getInstance().getComp()
				.podeDesapensar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível desapensar");

		if (doc.isEletronico()) {
			aDesapensarGravar(1, sigla, dtMovString, Boolean.FALSE, null, null);
			return;
		}
		result.include("mob", mob);
		result.include("request", getRequest());
		result.include("sigla", sigla);
		result.include("substituicao", Boolean.FALSE);
		result.include("subscritorSel", new DpPessoaSelecao());
		result.include("titularSel", new DpPessoaSelecao());
	}

	@Transacional
	@Get
	@Post
	@Path("/app/expediente/mov/desapensar_gravar")
	public void aDesapensarGravar(Integer postback, String sigla,
			String dtMovString, boolean substituicao,
			DpPessoaSelecao titularSel, DpPessoaSelecao subscritorSel)
			throws Exception {
		this.setPostback(postback);

		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder, true);

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia();

		movimentacaoBuilder.setSubscritorSel(subscritorSel)
				.setTitularSel(titularSel).setDtMovString(dtMovString)
				.setSubstituicao(substituicao).setMob(builder.getMob());

		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());

		final ExMobil mob = builder.getMob();

		if (!Ex.getInstance().getComp()
				.podeDesapensar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível desapensar");

		try {
			Ex.getInstance()
					.getBL()
					.desapensarDocumento(getCadastrante(), getLotaTitular(),
							mob, mov.getDtMov(), mov.getSubscritor(),
							mov.getTitular());
		} catch (final Exception e) {
			throw e;
		}
		result.include("mob", mob);
		result.include("request", getRequest());
		result.include("sigla", sigla);
		result.include("substituicao", Boolean.FALSE);

		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Get("/app/expediente/mov/reclassificar")
	public void aReclassificar(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		final ExDocumento doc = buscarDocumento(builder, true);
		final ExMobil mob = builder.getMob();

		if (!Ex.getInstance().getComp()
				.podeReclassificar(getTitular(), getLotaTitular(), mob)) {
			throw new AplicacaoException("Não é possível reclassificar");
		}

		result.include("mob", mob);
		result.include("doc", doc);
		result.include("sigla", sigla);
		result.include("tipoResponsavel", 1);
		result.include("substituicao", Boolean.FALSE);
		result.include("titularSel", new DpPessoaSelecao());
		result.include("subscritorSel", new DpPessoaSelecao());
		result.include("classificacaoSel", new ExClassificacaoSelecao());

	}

	@Transacional
	@Post("/app/expediente/mov/reclassificar_gravar")
	public void aReclassificarGravar(final String sigla, final String descrMov,
			final String[] campos, final Integer postback,
			final String dtMovString, final String obsOrgao,
			final boolean substituicao, final DpPessoaSelecao titularSel,
			final DpPessoaSelecao subscritorSel,
			final ExClassificacaoSelecao classificacaoSel) {
		this.setPostback(postback);

		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder, true);
		final ExMobil mob = builder.getMob();

		final ExMovimentacao mov = ExMovimentacaoBuilder.novaInstancia()
				.setDescrMov(descrMov).setDtMovString(dtMovString)
				.setObsOrgao(obsOrgao).setSubstituicao(substituicao)
				.setTitularSel(titularSel).setSubscritorSel(subscritorSel)
				.setClassificacaoSel(classificacaoSel).setMob(mob)
				.construir(dao());

		if (!Ex.getInstance().getComp()
				.podeReclassificar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível reclassificar");

		if (mov.getExDocumento().isEletronico()) {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("dd/MM/yyyy");
			mov.setSubscritor(getTitular());
			result.include("dtRegMov", sdf.format(new Date()).toString());
		}

		try {
			Ex.getInstance()
					.getBL()
					.avaliarReclassificar(getCadastrante(), getLotaTitular(),
							mob, mov.getDtMov(), mov.getSubscritor(),
							mov.getExClassificacao(), mov.getDescrMov(), false);

		} catch (final Exception e) {
			throw e;
		}

		result.include("doc", mov.getExDocumento());
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Transacional
	@Get("/app/expediente/mov/simular_assinatura")
	public void aSimularAssinatura(final String sigla) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		final ExDocumento doc = buscarDocumento(builder, true);

		Ex.getInstance()
				.getBL()
				.simularAssinaturaDocumento(getCadastrante(), getLotaTitular(),
						doc);

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
	}

	@Transacional
	@Get("/app/expediente/mov/simular_assinatura_mov")
	public void aSimularAssinaturaMov(final Long id) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setId(id);
		buscarDocumento(builder, true);

		final ExMobil mob = builder.getMob();
		final ExMovimentacao mov = builder.getMov();

		Ex.getInstance()
				.getBL()
				.simularAssinaturaMovimentacao(
						getCadastrante(),
						getLotaTitular(),
						mov,
						new Date(),
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO);

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + mob.getSigla());
	}

	@Transacional
	@Get("/app/expediente/mov/simular_anexacao")
	public void aSimularAnexacao(final String sigla) throws IOException,
			DocumentException {
		final Document document = new Document();
		byte abPDF[];
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			PdfWriter.getInstance(document, baos);
			document.open();
			document.addTitle("PDF de teste");
			final Paragraph preface = new Paragraph();
			preface.add(new Paragraph("Este é um documento de teste"));
			document.add(preface);
			document.close();
			abPDF = baos.toByteArray();
		}

		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder, true);

		final ExMobil mob = builder.getMob();

		final ExMovimentacaoBuilder movBuilder = ExMovimentacaoBuilder
				.novaInstancia().setMob(mob);
		final ExMovimentacao mov = movBuilder.construir(dao());

		mov.setDtMov(new Date());
		mov.setSubscritor(getTitular());
		mov.setLotaSubscritor(getLotaTitular());
		mov.setTitular(getTitular());
		mov.setLotaTitular(getLotaTitular());
		mov.setCadastrante(getCadastrante());
		mov.setLotaCadastrante(getLotaTitular());

		mov.setNmArqMov("teste.pdf");
		mov.setConteudoTpMov("application/pdf");

		mov.setConteudoBlobMov2(abPDF);

		if (mob.isVolumeEncerrado()) {
			throw new AplicacaoException(
					"Não é possível anexar arquivo em volume encerrado.");
		}

		if (!Ex.getInstance().getComp()
				.podeAnexarArquivo(getTitular(), getLotaTitular(), mob)) {
			throw new AplicacaoException("Arquivo não pode ser anexado");
		}

		// Obtem as pendencias que serão resolvidas
		final String aidMov[] = getRequest().getParameterValues(
				"pendencia_de_anexacao");
		Set<ExMovimentacao> pendencias = null;
		if (aidMov != null) {
			pendencias = new TreeSet<ExMovimentacao>();
			for (final String s : aidMov) {
				pendencias.add(dao().consultar(Long.parseLong(s),
						ExMovimentacao.class, false));
			}
		}

		// Nato: Precisei usar o código abaixo para adaptar o charset do
		// nome do arquivo
		final byte[] ab = mov.getNmArqMov().getBytes();
		for (int i = 0; i < ab.length; i++)
			if (ab[i] == -29) {
				ab[i] = -61;
			}
		String sNmArqMov = new String(ab, "utf-8");

		Ex.getInstance()
				.getBL()
				.anexarArquivo(getCadastrante(), getLotaTitular(), mob,
						mov.getDtMov(), mov.getSubscritor(), sNmArqMov,
						mov.getTitular(), mov.getLotaTitular(),
						mov.getConteudoBlobMov2(), mov.getConteudoTpMov(),
						mov.getDescrMov(), pendencias);

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
	}

	@Get("/app/expediente/mov/avaliar")
	public void aAvaliar(String sigla) {

		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		final ExDocumento doc = buscarDocumento(builder, true);
		final ExMobil mob = builder.getMob();

		if (!Ex.getInstance().getComp()
				.podeAvaliar(getTitular(), getLotaTitular(), mob)) {
			throw new AplicacaoException("Não é possível avaliar");
		}

		result.include("mob", mob);
		result.include("doc", doc);
		result.include("sigla", sigla);
		result.include("tipoResponsavel", 1);
		result.include("substituicao", Boolean.FALSE);
		result.include("titularSel", new DpPessoaSelecao());
		result.include("subscritorSel", new DpPessoaSelecao());
		result.include("classificacaoSel", new ExClassificacaoSelecao());
	}

	@Transacional
	@Post("/app/expediente/mov/avaliar_gravar")
	public void aAvaliarGravar(final String sigla, final String descrMov,
			final String obsOrgao, final String[] campos,
			final Integer postback, final String dtMovString,
			final boolean substituicao, final DpPessoaSelecao titularSel,
			final DpPessoaSelecao subscritorSel,
			final ExClassificacaoSelecao classificacaoSel) {

		this.setPostback(postback);
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder, true);
		final ExMobil mob = builder.getMob();

		final ExMovimentacao mov = ExMovimentacaoBuilder.novaInstancia()
				.setDescrMov(descrMov).setDtMovString(dtMovString)
				.setObsOrgao(obsOrgao).setSubstituicao(substituicao)
				.setTitularSel(titularSel).setSubscritorSel(subscritorSel)
				.setClassificacaoSel(classificacaoSel).setMob(mob)
				.construir(dao());

		if (!Ex.getInstance().getComp()
				.podeAvaliar(getTitular(), getLotaTitular(), mob)) {
			throw new AplicacaoException("Não é possível avaliar");
		}

		if (mov.getExDocumento().isEletronico()) {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("dd/MM/yyyy");
			mov.setSubscritor(getTitular());
			result.include("dtRegMov", sdf.format(new Date()).toString());
		}

		try {
			Ex.getInstance()
					.getBL()
					.avaliarReclassificar(getCadastrante(), getLotaTitular(),
							mob, mov.getDtMov(), mov.getSubscritor(),
							mov.getExClassificacao(), mov.getDescrMov(), true);
		} catch (final Exception e) {
			throw e;
		}

		result.include("doc", mov.getExDocumento());
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Get("/app/expediente/mov/agendar_publicacao")
	public void agendarPublicacao(String sigla, String descrPublicacao,
			String mensagem) throws Exception {
		BuscaDocumentoBuilder builder = BuscaDocumentoBuilder.novaInstancia()
				.setSigla(sigla);

		ExDocumento doc = buscarDocumento(builder, true);
		Boolean podeAtenderPedidoPublicacao = Boolean.FALSE;
		DpLotacaoSelecao lot = new DpLotacaoSelecao();

		if (doc.getExNivelAcesso().getGrauNivelAcesso() != ExNivelAcesso.NIVEL_ACESSO_PUBLICO)
			throw new AplicacaoException(
					"O agendamento de publicação no DJE somente é permitido para documentos com nível de acesso Público.");

		if (!Ex.getInstance()
				.getComp()
				.podeAgendarPublicacao(getTitular(), getLotaTitular(),
						builder.getMob()))
			throw new AplicacaoException(
					"Não foi possível o agendamento de publicação no DJE.");

		if (Ex.getInstance()
				.getConf()
				.podePorConfiguracao(
						getTitular(),
						getLotaTitular(),
						CpTipoConfiguracao.TIPO_CONFIG_ATENDER_PEDIDO_PUBLICACAO)) {

			if (doc.getSubscritor() != null
					&& doc.getSubscritor().getLotacao() != null) {
				lot.setId(doc.getSubscritor().getLotacao().getId());
				lot.buscar();
			}
			podeAtenderPedidoPublicacao = Boolean.TRUE;
		}

		if (!podeAtenderPedidoPublicacao) {
			ListaLotPubl listaLotPubl = getListaLotacaoPublicacao(doc);
			result.include("listaLotPubl", listaLotPubl.getLotacoes());
			result.include("idLotDefault", listaLotPubl.getIdLotDefault());
		}

		result.include("tipoMateria",
				PublicacaoDJEBL.obterSugestaoTipoMateria(doc));
		result.include("cadernoDJEObrigatorio",
				PublicacaoDJEBL.obterObrigatoriedadeTipoCaderno(doc));
		result.include("descrPublicacao",
				descrPublicacao == null ? doc.getDescrDocumento()
						: descrPublicacao);
		result.include("podeAtenderPedidoPubl", podeAtenderPedidoPublicacao);
		result.include("lotaSubscritorSel", lot);
		result.include("mob", builder.getMob());
		result.include("request", getRequest());
		result.include("mensagem", mensagem);
		result.include("tamMaxDescr", 255 - doc.getDescrDocumento().length());
		result.include("request", getRequest());
		result.include("sigla", sigla);
		result.include("proximaDataDisponivelStr",
				DatasPublicacaoDJE.consultarProximaDataDisponivelString());
	}

	@Transacional
	@Post("/app/expediente/mov/agendar_publicacao_gravar")
	public void agendarPublicacaoGravar(Integer postback, String sigla,
			String tipoMateria, String dtDispon, Long idLotPublicacao,
			String descrPublicacao, DpLotacaoSelecao lotaSubscritorSel)
			throws Exception {

		BuscaDocumentoBuilder docBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		Long idPubl = null;
		buscarDocumento(docBuilder, true);

		ExMovimentacao mov = ExMovimentacaoBuilder.novaInstancia()
				.setMob(docBuilder.getMob()).setDtDispon(dtDispon)
				.construir(dao());

		if (idLotPublicacao != null)
			idPubl = idLotPublicacao;
		else {
			if (lotaSubscritorSel.getId() != null)
				idPubl = lotaSubscritorSel.getId();
		}

		String lotPublicacao = dao().consultar(idPubl, DpLotacao.class, false)
				.getSigla();

		if (!Ex.getInstance()
				.getComp()
				.podeAgendarPublicacao(getTitular(), getLotaTitular(),
						docBuilder.getMob()))
			throw new AplicacaoException(
					"Não foi possível o agendamento de publicação no DJE.");

		if (descrPublicacao.length() > 256)
			throw new AplicacaoException(
					"O campo descrição possui mais do que 256 caracteres.");

		validarDataGravacao(mov, false);

		Ex.getInstance()
				.getBL()
				.remeterParaPublicacao(getCadastrante(), getLotaTitular(),
						docBuilder.getMob(), dao().dt(), mov.getSubscritor(),
						mov.getTitular(), getLotaTitular(),
						mov.getDtDispPublicacao(),
						tipoMateria.replaceAll("'", ""), lotPublicacao,
						descrPublicacao);

		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Get("/app/expediente/mov/assinar_verificar")
	public void aAssinarVerificar(Long id, boolean ajax) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setId(id);
		final ExDocumento doc = buscarDocumento(builder, true);

		final ExMovimentacao mov = builder.getMov();

		try {
			final String s = Ex
					.getInstance()
					.getBL()
					.verificarAssinatura(doc.getConteudoBlobPdf(),
							mov.getConteudoBlobMov2(), mov.getConteudoTpMov(),
							mov.getDtIniMov());
			getRequest().setAttribute("assinante", s);

			result.use(Results.page()).forwardTo(
					"/WEB-INF/page/exMovimentacao/assinatura_ok.jsp");
		} catch (final Exception e) {
			getRequest().setAttribute("err", e.getMessage());
			result.use(Results.page()).forwardTo(
					"/WEB-INF/page/exMovimentacao/assinatura_erro.jsp");
		}
	}

	@Get("/app/expediente/mov/assinar_mov_verificar")
	public void aAssinarMovVerificar(Long id, boolean ajax) {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setId(id);
		buscarDocumento(builder, true);

		final ExMovimentacao mov = dao().consultar(id, ExMovimentacao.class,
				false);
		final ExMovimentacao movRef = mov.getExMovimentacaoRef();

		try {
			final String s = Ex
					.getInstance()
					.getBL()
					.verificarAssinatura(movRef.getConteudoBlobpdf(),
							mov.getConteudoBlobMov2(), mov.getConteudoTpMov(),
							mov.getDtIniMov());
			getRequest().setAttribute("assinante", s);

			result.use(Results.page()).forwardTo(
					"/WEB-INF/page/exMovimentacao/assinatura_ok.jsp");
		} catch (final Exception e) {
			getRequest().setAttribute("err", e.getMessage());
			result.use(Results.page()).forwardTo(
					"/WEB-INF/page/exMovimentacao/assinatura_erro.jsp");
		}
	}

	private void validarDataGravacao(ExMovimentacao mov,
			boolean apenasSolicitacao) throws AplicacaoException {
		if (mov.getDtDispPublicacao() == null)
			throw new AplicacaoException(
					"A data desejada para a disponibilização precisa ser informada.");
		
		// Verifica se a data está entre o ano 2000 e o ano 2100
		if (!Data.dataDentroSeculo21(mov.getDtDispPublicacao())) {
				throw new AplicacaoException("Data de disponibilização inválida, deve estar entre o ano 2000 e ano 2100");
		}

		DatasPublicacaoDJE DJE = new DatasPublicacaoDJE(
				mov.getDtDispPublicacao());
		String mensagemValidacao = DJE
				.validarDataDeDisponibilizacao(apenasSolicitacao);
		if (mensagemValidacao != null)
			throw new AplicacaoException(mensagemValidacao);
	}

	private ListaLotPubl getListaLotacaoPublicacao(ExDocumento doc)
			throws Exception {
		validarExisteLotacao(doc);

		Set<DpLotacao> lotacoes = new HashSet<DpLotacao>();
		DpLotacao lotSubscritor, lotCadastrante, lotTitular, lotFiltro;
		String siglaSubscritor, siglaCadastrante, siglaTitular;
		Long idOrgaoUsuario = doc.getOrgaoUsuario().getId();
		Long idOrgaoUsuarioCadastrante = getCadastrante().getOrgaoUsuario()
				.getId();

		siglaSubscritor = PublicacaoDJEBL.obterUnidadeDocumento(doc);
		siglaCadastrante = getCadastrante().getLotacao().getSigla();
		siglaTitular = getLotaTitular().getSigla();
		lotFiltro = new DpLotacao();

		lotFiltro.setOrgaoUsuario(doc.getOrgaoUsuario());
		lotFiltro.setSigla(siglaSubscritor);
		lotSubscritor = dao().consultarPorSigla(lotFiltro);

		lotacoes.add(lotSubscritor);

		if (!siglaSubscritor.equals(siglaCadastrante)
				&& idOrgaoUsuarioCadastrante.equals(idOrgaoUsuario)) {
			lotFiltro.setSigla(siglaCadastrante);
			lotCadastrante = dao().consultarPorSigla(lotFiltro);
			lotacoes.add(lotCadastrante);
		}

		if (!siglaSubscritor.equals(siglaTitular)
				&& !siglaCadastrante.equals(siglaTitular)
				&& ((getTitular().getOrgaoUsuario().getId()
						.equals(idOrgaoUsuario)) || getLotaTitular()
						.getOrgaoUsuario().getId().equals(idOrgaoUsuario))) {
			lotFiltro.setSigla(siglaTitular);
			lotTitular = dao().consultarPorSigla(lotFiltro);
			lotacoes.add(lotTitular);
		}
		return new ListaLotPubl(lotacoes, lotSubscritor.getId());
	}

	private void validarExisteLotacao(ExDocumento doc) {
		if (doc.getTitular() != null) {
			if (doc.getLotaTitular() == null) {
				throw new AplicacaoException(
						"Não foi possível encontrar a lotação do documento");
			}
		} else {
			if (doc.getLotaSubscritor() == null) {
				throw new AplicacaoException(
						"Não foi possível encontrar a lotação do documento");
			}
		}
	}

	private List<ExNivelAcesso> getListaNivelAcesso(final ExDocumento doc) {
		ExFormaDocumento exForma = doc.getExFormaDocumento();
		ExClassificacao exClassif = doc.getExClassificacaoAtual();
		ExTipoDocumento exTipo = doc.getExTipoDocumento();
		ExModelo exMod = doc.getExModelo();

		return getListaNivelAcesso(exTipo, exForma, exMod, exClassif);
	}

	protected Map<Integer, String> getListaTipoResp() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, SigaMessages.getMessage("usuario.lotacao"));
		map.put(2, SigaMessages.getMessage("usuario.matricula"));
		map.put(3, SigaMessages.getMessage("responsavel.externo"));
		return map;
	}

	private Map<Integer, String> getListaTipoRespPerfil() {
		return TipoResponsavelEnum.getListaMatriculaLotacao();
	}

	@SuppressWarnings("unchecked")
	private List<ExTipoDespacho> getTiposDespacho(final ExMobil mob) {
		final List<ExTipoDespacho> tiposDespacho = new ArrayList<ExTipoDespacho>();
		tiposDespacho.add(new ExTipoDespacho(0L, "[Nenhum]", "S"));
		tiposDespacho.addAll(dao().consultarAtivos());
		tiposDespacho
				.add(new ExTipoDespacho(-1, "[Outros] (texto curto)", "S"));

		if (mob != null
				&& Ex.getInstance().getComp()
						.podeCriarDocFilho(getTitular(), getLotaTitular(), mob))
			tiposDespacho.add(new ExTipoDespacho(-2, "[Outros] (texto longo)",
					"S"));

		return tiposDespacho;
	}

	@SuppressWarnings("unchecked")
	private List<ExPapel> getListaExPapel() {
		return (List<ExPapel>) dao.listarTodos(ExPapel.class, null);
	}

	private ExModelo getModeloDespachoAutomatico() {
		return dao().consultarExModelo(null, "Despacho Automático");
	}

	private int processarTipoResponsavel(ExMobil mob) {
		if (mob == null) {
			throw new IllegalArgumentException("Mob nao preenchido!");
		}
		ExMovimentacao ultMov = mob.getUltimaMovimentacao();

		if (ultMov.getLotaDestinoFinal() != null) {
			return 1;
		} else if (ultMov.getDestinoFinal() != null) {
			return 2;
		}
		return -1;
	}

	private String recuperarAssinaturaAppletB64(
			final BuscaDocumentoBuilder builder) throws ServletException,
			AplicacaoException {
		HttpServletRequest request = getRequest();
		// Recupera a quantidade de pacotes enviados
		String QTYDATA = request.getParameter("QTYDATA");
		if (QTYDATA == null)
			throw new ServletException("campo QTYDATA faltando");

		// Recupera o identificador do documento
		String IDDATA = request.getParameter("IDDATA");
		if (IDDATA == null)
			throw new ServletException("campo IDDATA faltando");

		// Recupera o conteudo do pacote
		String ENCDATA = "ENCDATA." + IDDATA;
		String hexEncoded = request.getParameter(ENCDATA).toString();

		// Recupera nome do arquivo
		String ALIAS_NOME = "#arquivo." + IDDATA;
		String ARQUIVO = request.getParameter(ALIAS_NOME);
		if (ARQUIVO == null || ARQUIVO.equals("")) {
			ARQUIVO = "texto.txt";
		}

		// Recupera o Id da movimentacao
		// #arquivo é alimentado com ExMovimentacao.nmPdf. Se existir ":" é uma
		// assinatura de movimentação
		// caso contrário, é uma assinatura de documento

		if (ARQUIVO.contains(":")) {
			String[] partesArq = ARQUIVO.split(":");
			builder.setId(Long.parseLong(partesArq[1]));
		} else
			builder.setSigla(ARQUIVO);

		byte[] decoded = null;
		try {
			decoded = hexStringToByteArray(hexEncoded);
		} catch (Exception e) {
			throw new AplicacaoException(e.getMessage());
		}

		return Base64.encode(decoded);
	}

	private static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	@Transacional
	@Get("/app/expediente/mov/boletim_agendar")
	public void aBoletimAgendar(final String sigla) throws Exception {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		final ExDocumento doc = buscarDocumento(builder, true);
		final ExMobil mob = builder.getMob();

		if (doc.getExNivelAcessoAtual().getGrauNivelAcesso() != ExNivelAcesso.NIVEL_ACESSO_PUBLICO)

			throw new AplicacaoException(
					"A solicitação de publicação no BIE somente é permitida para documentos com nível de acesso Público.");

		if (!Ex.getInstance()
				.getComp()
				.podeAgendarPublicacaoBoletim(getTitular(), getLotaTitular(),
						mob))
			throw new AplicacaoException(
					"A solicitação de publicação no BIE apenas é permitida até as 17:00");

		try {
			Ex.getInstance()
					.getBL()
					.agendarPublicacaoBoletim(getCadastrante(),
							getLotaTitular(), doc);
		} catch (final Exception e) {
			throw e;
		}

		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Get("/app/expediente/mov/boletim_publicar")
	public void publica_boletim(final String sigla) throws Exception {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder, true);
		final ExMobil mob = builder.getMob();

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			result.include("dtPubl", df.format(new Date()));
		} catch (final Exception e) {
		}

		if (!Ex.getInstance().getComp()
				.podePublicar(getTitular(), getLotaTitular(), mob)) {
			throw new AplicacaoException("Publicação não permitida");
		}
		result.include("sigla", sigla);
		result.include("doc", mob.getDoc());
	}

	@Transacional
	@Get("/app/expediente/mov/boletim_publicar_gravar")
	public void aBoletimPublicarGravar(final String sigla, final String dtPubl)
			throws Exception {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(builder, true);

		final ExMovimentacaoBuilder movBuilder = ExMovimentacaoBuilder
				.novaInstancia();
		movBuilder.setMob(builder.getMob());
		movBuilder.setDtPubl(dtPubl);
		final ExMovimentacao mov = movBuilder.construir(dao());

		if (!Ex.getInstance().getComp()
				.podePublicar(getTitular(), getLotaTitular(), builder.getMob())) {
			throw new AplicacaoException("Nao foi possivel fazer a publicacao");
		}

		Ex.getInstance()
				.getBL()
				.publicarBoletim(getCadastrante(), getLotaTitular(),
						mov.getExDocumento(), mov.getDtMov());

		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Get("/app/expediente/mov/autenticar_documento")
	public void aAutenticarDocumento(final String sigla) throws Exception {
		// setAutenticando(true);
		result.forwardTo(this).aAssinar(sigla, true);
	}

	@Get("/app/expediente/mov/pedirPublicacao")
	public void pedirPublicacao(final String sigla,
			final String descrPublicacao, final String mensagem)
			throws Exception {
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		ExDocumento doc = buscarDocumento(builder, true);
		ExMobil mob = builder.getMob();

		DpLotacaoSelecao lot = new DpLotacaoSelecao();

		if (doc.getExNivelAcessoAtual().getGrauNivelAcesso() != ExNivelAcesso.NIVEL_ACESSO_PUBLICO)
			throw new AplicacaoException(
					"A solicitação de publicação no DJE somente é permitida para documentos com nível de acesso Público.");

		if (!Ex.getInstance().getComp()
				.podePedirPublicacao(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Publicação não permitida");

		lot.setId(doc.getSubscritor().getLotacao().getId());
		lot.buscar();
		ListaLotPubl listaLotPubl = getListaLotacaoPublicacao(doc);

		result.include("tipoMateria",
				PublicacaoDJEBL.obterSugestaoTipoMateria(doc));
		result.include("cadernoDJEObrigatorio",
				PublicacaoDJEBL.obterObrigatoriedadeTipoCaderno(doc));
		result.include("descrPublicacao",
				descrPublicacao == null ? doc.getDescrDocumento()
						: descrPublicacao);
		result.include("sigla", sigla);
		result.include("request", getRequest());
		result.include("mob", builder.getMob());
		result.include("doc", builder.getMob().getDoc());
		result.include("listaLotPubl", listaLotPubl.getLotacoes());
		result.include("idLotDefault", listaLotPubl.getIdLotDefault());
		result.include("tamMaxDescr", 255 - doc.getDescrDocumento().length());
		result.include("mensagem", mensagem);
		result.include("proximaDataDisponivelStr",
				DatasPublicacaoDJE.consultarProximaDataDisponivelString());
	}

	@Transacional
	@Post("/app/expediente/mov/pedirPublicacaoGravar")
	public void pedirPublicacaoGravar(String sigla, Integer postback,
			String tipoMateria, String dtDispon, Long idLotPublicacao,
			String descrPublicacao, DpLotacaoSelecao lotaSubscritorSel)
			throws Exception {

		final BuscaDocumentoBuilder docBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		this.setPostback(postback);
		Long idPubl = null;
		buscarDocumento(docBuilder, true);
		final ExMobil mob = docBuilder.getMob();

		final ExMovimentacao mov = ExMovimentacaoBuilder.novaInstancia()
				.setMob(docBuilder.getMob()).setDtDispon(dtDispon)
				.construir(dao());

		if (idLotPublicacao != null)
			idPubl = idLotPublicacao;
		else {
			if (lotaSubscritorSel.getId() != null)
				idPubl = lotaSubscritorSel.getId();
		}

		final String lotPublicacao = dao().consultar(idPubl, DpLotacao.class,
				false).getSigla();

		if (!Ex.getInstance().getComp()
				.podePedirPublicacao(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Publicação não permitida");

		validarDataGravacao(mov, true);

		try {
			Ex.getInstance()
					.getBL()
					.pedirPublicacao(getCadastrante(), getLotaTitular(), mob,
							mov.getDtMov(), mov.getSubscritor(),
							mov.getTitular(), mov.getLotaTitular(),
							mov.getDtDispPublicacao(), tipoMateria,
							lotPublicacao, descrPublicacao);
		} catch (final Exception e) {
			throw e;
		}

		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Get("/app/expediente/mov/indicar_permanente")
	public void indicarPermanente(final String sigla) throws Exception {

		final BuscaDocumentoBuilder docBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(docBuilder, true);
		final ExMobil mob = docBuilder.getMob();

		if (!Ex.getInstance().getComp()
				.podeIndicarPermanente(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível fazer indicação para guarda permanente");
		result.include("mob", mob);
		result.include("sigla", sigla);
		result.include("request", getRequest());
		result.include("subscritorSel", new DpPessoaSelecao());
		result.include("titularSel", new DpPessoaSelecao());
	}

	@Transacional
	@Post("/app/expediente/mov/indicar_permanente_gravar")
	public void indicarPermanenteGravar(final String sigla,
			final String dtMovString, final DpPessoaSelecao subscritorSel,
			final DpPessoaSelecao titularSel, final String descrMov)
			throws Exception {

		final BuscaDocumentoBuilder docBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(docBuilder, true);
		ExMobil mob = docBuilder.getMob();

		final ExMovimentacao mov = ExMovimentacaoBuilder.novaInstancia()
				.setMob(mob).setDtMovString(dtMovString)
				.setSubscritorSel(subscritorSel).setTitularSel(titularSel)
				.setDescrMov(descrMov).construir(dao());

		if (!Ex.getInstance().getComp()
				.podeIndicarPermanente(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível fazer indicação para guarda permanente");

		String dtRegMov = null;
		if (mov.getExDocumento().isEletronico()) {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("dd/MM/yyyy");
			dtRegMov = (sdf.format(new Date()).toString());
			mov.setSubscritor(getTitular());
		}

		try {
			Ex.getInstance()
					.getBL()
					.indicarPermanente(getCadastrante(), getLotaTitular(), mob,
							mov.getDtMov(), mov.getSubscritor(),
							mov.getTitular(), mov.getDescrMov());
		} catch (final Exception e) {
			throw e;
		}

		result.include("dtRegMov", dtRegMov);
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Get("/app/expediente/mov/reverter_indicacao_permanente")
	public void reverterIndicacaoPermanente(final String sigla)
			throws Exception {
		final BuscaDocumentoBuilder docBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(docBuilder, true);
		final ExMobil mob = docBuilder.getMob();

		if (!Ex.getInstance()
				.getComp()
				.podeReverterIndicacaoPermanente(getTitular(),
						getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível reverter indicação para guarda permanente");

		result.include("mob", mob);
		result.include("sigla", sigla);
		result.include("request", getRequest());
		result.include("subscritorSel", new DpPessoaSelecao());
		result.include("titularSel", new DpPessoaSelecao());
	}

	@Transacional
	@Post("/app/expediente/mov/reverter_indicacao_permanente_gravar")
	public void reverterIndicacaoPermanenteGravar(final String sigla,
			final String dtMovString, final DpPessoaSelecao subscritorSel,
			final DpPessoaSelecao titularSel, final String descrMov)
			throws Exception {
		final BuscaDocumentoBuilder docBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		buscarDocumento(docBuilder, true);
		ExMobil mob = docBuilder.getMob();

		final ExMovimentacao mov = ExMovimentacaoBuilder.novaInstancia()
				.setMob(mob).setDtMovString(dtMovString)
				.setSubscritorSel(subscritorSel).setTitularSel(titularSel)
				.setDescrMov(descrMov).construir(dao());

		if (!Ex.getInstance()
				.getComp()
				.podeReverterIndicacaoPermanente(getTitular(),
						getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível reverter indicação para guarda permanente");

		String dtRegMov = null;
		if (mov.getExDocumento().isEletronico()) {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("dd/MM/yyyy");
			dtRegMov = (sdf.format(new Date()).toString());
			mov.setSubscritor(getTitular());
		}

		try {
			Ex.getInstance()
					.getBL()
					.reverterIndicacaoPermanente(getCadastrante(),
							getLotaTitular(), mob, mov.getDtMov(),
							mov.getSubscritor(), mov.getTitular(),
							mov.getDescrMov());
		} catch (final Exception e) {
			throw e;
		}

		result.include("dtRegMov", dtRegMov);
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Transacional
	@Get
	@Post
	@Path("/app/expediente/mov/assinar_mov_gravar")
	public void aAssinarMovGravar(final Long id, final Boolean copia,
			final String atributoAssinavelDataHora, String assinaturaB64,
			final String certificadoB64) throws Exception {
		try {
			final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
					.novaInstancia().setId(id);
			final boolean fApplet = getRequest().getParameter("QTYDATA") != null;
			String b64Applet = null;
			if (fApplet) {
				b64Applet = recuperarAssinaturaAppletB64(builder);
			}
			buscarDocumento(builder, true);
			final ExMobil mob = builder.getMob();
			final ExMovimentacao mov = builder.getMov();
	
			if (b64Applet != null)
				assinaturaB64 = b64Applet;
	
			long tpMovAssinatura = ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO;
			if (copia != null && copia)
				tpMovAssinatura = ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO;
	
			byte[] assinatura = Base64.decode(assinaturaB64);
			Date dt = dao().consultarDataEHoraDoServidor();
	
			byte[] certificado = Base64.decode(certificadoB64);
			if (certificado != null && certificado.length != 0)
				dt = new Date(Long.valueOf(atributoAssinavelDataHora));
			else
				certificado = null;
	
			verificaNivelAcesso(mov.getExMobil());
	
			Ex.getInstance()
					.getBL()
					.assinarMovimentacao(getCadastrante(), getLotaTitular(),
							mov, dt, assinatura, certificado, tpMovAssinatura);
		} catch (final Exception e) {
			httpError(e);
		}

		httpOK();
	}

	protected void httpOK() {
		result.use(Results.http()).body("OK").setStatusCode(200);
	}

	protected void httpError(final Exception e) throws Exception {
 		result.use(Results.http()).body("ERRO - " + e.getMessage())
 				.setStatusCode(500);
		try {
			response.flushBuffer();
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		throw e;
 	}
	
	@Get("/app/expediente/mov/assinado")
	public void assinado(final ExMobil mob) {
		result.include("mob", mob);
	}

	@Transacional
	@Get("/public/app/atualizar_publicacao")
	public void atualizarPublicacao(final String data,
			final String tipoCaderno, final String secao, final String soLerXml)
			throws Exception {
		try{
			String sData = data;
			Date dataBusca = null;
			if (sData != null) {
				dataBusca = new SimpleDateFormat("ddMMyyyy").parse(sData);
			} else dataBusca = new Date();

			String sTipoCaderno = tipoCaderno;
			String sSecao = secao;

			String sSoLerXml = "nao";
			if (soLerXml != null)
				sSoLerXml = soLerXml;

			String xml = PublicacaoDJEBL.segundoRetorno(dataBusca, sTipoCaderno, sSecao,
					sSoLerXml);
			result.use(Results.http()).body(xml).setStatusCode(200);
		} catch(Exception e){
			result.use(Results.http()).body(ExceptionUtils.getStackTrace(e)).setStatusCode(500);
		}
	}
    
	@Get("/app/expediente/mov/ciencia")
	public void aCiencia(final String sigla, final String descrMov) {
		final BuscaDocumentoBuilder documentoBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		final ExDocumento documento = buscarDocumento(documentoBuilder);

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia().setMob(documentoBuilder.getMob());

		final ExMovimentacao movimentacao = movimentacaoBuilder
				.construir(dao());
		
		String descricao = movimentacaoBuilder.getDescrMov() == null ? descrMov : movimentacaoBuilder.getDescrMov();

		try {			
		
			if (!Ex.getInstance()
					.getComp()
					.podeFazerCiencia(getTitular(), getLotaTitular(),
							documentoBuilder.getMob())) {
				throw new RegraNegocioException("Não é possível fazer ciência do documento."			
						+ " Isso pode ocorrer se o documento não estiver apto a receber ciência ou devido a alguma regra para não permitir esta operação");
			}
	
			result.include("sigla", sigla);
			result.include("mob", documentoBuilder.getMob());
			result.include("mov", movimentacao);
			result.include("doc", documento);
			result.include("descrMov", descricao);
		} catch (RegraNegocioException e) {
			result.include(SigaModal.ALERTA, SigaModal.mensagem(e.getMessage()));
			result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
		}
	}

	@Transacional
	@Post("/app/expediente/mov/ciencia_gravar")
	public void ciencia_gravar(final Integer postback, final String sigla, final String descrMov) {					
		this.setPostback(postback);
		
		final ExMovimentacaoBuilder builder = ExMovimentacaoBuilder
				.novaInstancia();

		final ExMovimentacao mov = builder.construir(dao());

		final BuscaDocumentoBuilder documentoBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(documentoBuilder);
		
		mov.setDtMov(dao().dt());
		mov.setSubscritor(getCadastrante());
		mov.setResp(getCadastrante());
		mov.setLotaResp(getLotaTitular());
		mov.setDescrMov(descrMov);
			
		try {	
			Ex.getInstance()
					.getBL()
					.registrarCiencia(getCadastrante(), getLotaTitular(),
							documentoBuilder.getMob(), mov.getDtMov(),
							mov.getLotaResp(), mov.getResp(), mov.getSubscritor(),
							mov.getDescrMov());
	
			result.redirectTo("/app/expediente/doc/exibir?sigla=" + sigla);
		} catch (RegraNegocioException e) {
			result.include(SigaModal.ALERTA, SigaModal.mensagem(e.getMessage()));
			result.forwardTo(this).aCiencia(sigla, descrMov);
		}
	}
	
	private Object getListaMarcadoresTaxonomiaAdministrada() {
		return dao().listarCpMarcadoresTaxonomiaAdministrada();
	}
	
	@Get("/app/expediente/mov/publicacao_transparencia")
	public void aPublicarTransparencia(String sigla, String descrPublicacao,
			String mensagem) throws Exception {
		
		final BuscaDocumentoBuilder documentoBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		final ExDocumento documento = buscarDocumento(documentoBuilder);

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder
				.novaInstancia().setMob(documentoBuilder.getMob());

		final ExMovimentacao movimentacao = movimentacaoBuilder
				.construir(dao());

		List<CpMarcador> marcadores = dao().listarCpMarcadoresTaxonomiaAdministrada();
		Set<CpMarcador> marcadoresAtivo = (Set<CpMarcador>) this.getListaMarcadoresAtivos(documentoBuilder.getMob().getDoc().getMobilGeral());
		if (marcadores != null) {
			marcadores.removeAll(marcadoresAtivo);
		}

		

		result.include("sigla", sigla);
		result.include("mob", documentoBuilder.getMob());
		result.include("mov", movimentacao);
		result.include("doc", documento);
		result.include("descrMov", movimentacaoBuilder.getDescrMov());
		result.include("listaMarcadores", marcadores);
		result.include("listaMarcadoresAtivos", marcadoresAtivo);
	}
	
	
	@Transacional
	@Post("/app/expediente/mov/publicacao_transparencia_gravar")
	public void publicarTransparenciaGravar(final String sigla,
			final Long nivelAcesso) {
		final BuscaDocumentoBuilder documentoBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);
		
		buscarDocumento(documentoBuilder);

		String[] listaMarcadores = request.getParameterValues("lstMarcadores");


		/* Primeiro Passo - Documento para Público */
		CpToken sigaUrlPermanente = new CpToken();
		sigaUrlPermanente = Ex.getInstance().getBL().publicarTransparencia(documentoBuilder.getMob(), getCadastrante(), getLotaCadastrante(),listaMarcadores,false);
	
		String url = Contexto.urlBase(request);
		String caminho = url + "/siga/public/app/sigalink/1/" + sigaUrlPermanente.getToken();
		
		result.include("url", caminho);
		
		
		result.include("msgCabecClass", "alert-info");
		result.include("mensagemCabec", "Documento enviado para publicação. Gerado <a class='alert-link' id='urlPermanente'  href='"+caminho+"' target='_Blank'  data-toggle='tooltip' data-placement='bottom'  data-html='true' title='Ir para endereço <i class=\"fa fa-link\"></i>'>Endereço Permanente</a> para acesso externo ao documento. <script>$(function () {$('[data-toggle=\"tooltip\"]').tooltip();$('#urlPermanente').tooltip('show');});</script> ");

		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Get("/app/expediente/mov/exibir_no_acompanhamento_do_protocolo")
	public void exibirNoAcompanhamentoDoProtocolo(final String sigla) {					
		final BuscaDocumentoBuilder documentoBuilder = BuscaDocumentoBuilder
				.novaInstancia().setSigla(sigla);

		buscarDocumento(documentoBuilder);
		
		String siglaRetorno = sigla;

		if (documentoBuilder.getMob().getDoc().getExMobilPai() != null) {
			siglaRetorno = documentoBuilder.getMob().getDoc().getExMobilPai().getSigla();
		}
			
		try {	
			Ex.getInstance()
					.getBL()
					.exibirNoAcompanhamentoDoProtocolo(getCadastrante(), getLotaTitular(),
							documentoBuilder.getMob(), getTitular());
	
		} catch (RegraNegocioException | AplicacaoException e) {
			result.include(SigaModal.ALERTA, SigaModal.mensagem(e.getMessage()));
			ExDocumentoController.redirecionarParaExibir(result, siglaRetorno);
		}

		ExDocumentoController.redirecionarParaExibir(result, siglaRetorno);
	}
	
	@Get("/app/expediente/mov/desfazer_exibir_no_acompanhamento_do_protocolo")
	public void desfazerExibirNoAcompanhamentoDoProtocolo(final Long id) throws Exception {					
		final BuscaDocumentoBuilder builder = BuscaDocumentoBuilder
				.novaInstancia().setId(id);
		buscarDocumento(builder);
		final ExMobil mob = builder.getMob();
		String siglaRetorno = mob.getSigla();

		if (mob.getDoc().getExMobilPai() != null) {
			siglaRetorno = mob.getDoc().getExMobilPai().getSigla();
		}
		

		final ExMovimentacao mov = dao().consultar(id, ExMovimentacao.class,
				false);

		if (mov == null 
				|| !mov.getIdTpMov().equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_EXIBIR_NO_ACOMPANHAMENTO_DO_PROTOCOLO) 
				|| mov.isCancelada()) {
			throw new AplicacaoException("Não existe a disponibilização no acompanhamento do protocolo a ser cancelada.");
		}
		
		try {
			Ex.getInstance()
			.getBL()
			.cancelar(getTitular(), getLotaTitular(), builder.getMob(),
					mov, null, null, null,
					"Disponibilização no acompanhamento do protocolo");
		} catch (RegraNegocioException | AplicacaoException e) {
			result.include(SigaModal.ALERTA, SigaModal.mensagem("Erro ao desfazer a disponibilização no acompanhamento do protocolo - " 
					+ e.getMessage()));
			ExDocumentoController.redirecionarParaExibir(result, siglaRetorno);
		}

		ExDocumentoController.redirecionarParaExibir(result, siglaRetorno);
	}
	
}