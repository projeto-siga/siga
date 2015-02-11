package br.gov.jfrj.siga.vraptor;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExTipoDespacho;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.SigaExProperties;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.vo.ExMobilVO;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.libs.webwork.CpOrgaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpLotacaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpPessoaSelecao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.vraptor.builder.BuscaDocumentoBuilder;
import br.gov.jfrj.siga.vraptor.builder.ExMovimentacaoBuilder;

@Resource
public class ExMovimentacaoController extends ExController {

	private static final Logger LOGGER = Logger.getLogger(ExMovimentacaoController.class);

	public ExMovimentacaoController(HttpServletRequest request, HttpServletResponse response, ServletContext context, Result result, SigaObjects so,
			EntityManager em) {
		super(request, response, context, result, ExDao.getInstance(), so, em);

		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}

	private ExDocumento buscarDocumento(final BuscaDocumentoBuilder builder) {
		return buscarDocumento(builder, true);
	}

	private ExDocumento buscarDocumento(final BuscaDocumentoBuilder builder, final boolean verificarAcesso) {
		ExDocumento doc = builder.buscarDocumento(dao());

		if (verificarAcesso && builder.getMob() != null)
			verificaNivelAcesso(builder.getMob());

		return doc;
	}

	@Get("app/expediente/mov/anexar")
	public void anexa(String sigla) throws Exception {
		this.sigla = sigla;
		buscarDocumento(true);

		if (!(mob.isGeral() && mob.doc().isFinalizado()))
			if (!Ex.getInstance().getComp().podeAnexarArquivo(getTitular(), getLotaTitular(), mob))
				throw new AplicacaoException("Arquivo não pode ser anexado");

		ExMobilVO mobilVO = new ExMobilVO(mob, getTitular(), getLotaTitular(), true, ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO, false);

		ExMobilVO mobilCompletoVO = new ExMobilVO(mob, getTitular(), getLotaTitular(), true, null, false);

		result.include("mobilCompletoVO", mobilCompletoVO);
		result.include("mobilVO", mobilVO);

		result.include("sigla", this.sigla);
		result.include("mob", this.mob);
		result.include("subscritorSel", this.subscritorSel);
		result.include("titularSel", this.titularSel);
		result.include("request", getRequest());
	}

	@Post("app/expediente/mov/anexar_gravar")
	public void anexarGravar(String sigla, DpPessoaSelecao subscritorSel, DpPessoaSelecao titularSel, boolean substituicao, UploadedFile arquivo,
			String dtMovString, String descrMov) throws Exception {

		Date dataMov = parseData(dtMovString);
		mov.setDtMov(dataMov);
		setArquivoContentType(arquivo.getContentType());
		setArquivoFileName(arquivo.getFileName());
		mov.setConteudoTpMov(getArquivoContentType());
		mov.setSubscritor(subscritorSel.getObjeto());
		mov.setTitular(titularSel.getObjeto());
		mov.setNmArqMov(getArquivoFileName());
		mov.setDescrMov(descrMov);
		setDescrMov(descrMov);
		this.substituicao = substituicao;
		this.sigla = sigla;

		buscarDocumento(true);
		lerForm(mov);

		if (arquivo.getFile() == null) {
			throw new AplicacaoException("O arquivo a ser anexado não foi selecionado!");
		}

		byte[] baArquivo = toByteArray(arquivo);
		if (baArquivo == null)
			throw new AplicacaoException("Arquivo vazio não pode ser anexado.");
		if (baArquivo.length > 10 * 1024 * 1024)
			throw new AplicacaoException("Não é permitida a anexação de arquivos com mais de 10MB.");
		mov.setConteudoBlobMov2(baArquivo);

		if (mov.getContarNumeroDePaginas() == null || mov.getArquivoComStamp() == null)
			throw new AplicacaoException("O arquivo " + getArquivoFileName() + " está corrompido. Favor gerá-lo novamente antes de anexar.");
		if (mob.isVolumeEncerrado()) {
			throw new AplicacaoException("Não é possível anexar arquivo em volume encerrado.");
		}

		if (!Ex.getInstance().getComp().podeAnexarArquivo(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Arquivo não pode ser anexado");
		if (!getArquivoContentType().equals("application/pdf"))
			throw new AplicacaoException("Somente é permitido anexar arquivo PDF.");

		// Obtem as pendencias que serão resolvidas
		String aidMov[] = getRequest().getParameterValues("pendencia_de_anexacao");
		Set<ExMovimentacao> pendencias = null;
		if (aidMov != null) {
			pendencias = new TreeSet<ExMovimentacao>();
			for (String s : aidMov) {
				pendencias.add(dao().consultar(Long.parseLong(s), ExMovimentacao.class, false));
			}
		}

		try {
			// Nato: Precisei usar o código abaixo para adaptar o charset do
			// nome do arquivo
			String s1 = new String(mov.getNmArqMov().getBytes(), "utf-8");
			byte[] ab = mov.getNmArqMov().getBytes();
			for (int i = 0; i < ab.length; i++)
				if (ab[i] == -29)
					ab[i] = -61;
			String sNmArqMov = new String(ab, "utf-8");

			Ex.getInstance()
					.getBL()
					.anexarArquivo(getCadastrante(), getLotaTitular(), mob, dataMov, mov.getSubscritor(), sNmArqMov, mov.getTitular(), mov.getLotaTitular(),
							mov.getConteudoBlobMov2(), mov.getConteudoTpMov(), getDescrMov(), pendencias);
		} catch (final Exception e) {
			throw e;
		}

		setDoc(mov.getExDocumento());
		result.redirectTo(MessageFormat.format("anexar?sigla={0}", sigla));
	}

	private Date parseData(String dtMovString) throws ParseException {
		if (dtMovString != null) {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date dataMov = new Date(formatter.parse(dtMovString).getTime());
			return dataMov;
		}
		return null;
	}

	@Get("app/expediente/mov/mostrar_anexos_assinados")
	public void mostrarAnexosAssinados(String sigla) throws Exception {
		this.sigla = sigla;
		buscarDocumento(true);

		ExMobilVO mobilVO = new ExMobilVO(mob, getTitular(), getLotaTitular(), true, ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO, true);

		result.include("mobilVO", mobilVO);
	}

	@Get("app/expediente/mov/desobrestar_gravar")
	public void aDesobrestarGravar(String sigla) throws Exception {
		this.setSigla(sigla);
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance().getComp().podeDesobrestar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Via não pode ser desobrestada");
		try {
			Ex.getInstance().getBL().desobrestar(getCadastrante(), getLotaTitular(), mob, mov.getDtMov(), mov.getSubscritor());
		} catch (final Exception e) {
			throw e;
		}
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Get("app/expediente/mov/sobrestar_gravar")
	public void sobrestarGravar(String sigla) throws Exception {
		this.sigla = sigla;
		this.buscarDocumento(true);
		this.lerForm(mov);

		if (!Ex.getInstance().getComp().podeAcessarDocumento(getTitular(), getLotaTitular(), mob)) {
			throw new AplicacaoException("Acesso permitido a usuários autorizados.");
		}

		if (!Ex.getInstance().getComp().podeSobrestar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Via não pode ser sobrestada");

		try {
			Ex.getInstance().getBL().sobrestar(getCadastrante(), getLotaTitular(), mob, mov.getDtMov(), null, mov.getSubscritor());
		} catch (final Exception e) {
			throw e;
		}
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Get("app/expediente/mov/assinar")
	public void aAssinar(String sigla) throws Exception {
		this.setSigla(sigla);

		buscarDocumento(true);

		boolean fPreviamenteAssinado = doc.isAssinado();

		if (!fPreviamenteAssinado && (doc.getExModelo() != null && ("template/freemarker".equals(doc.getExModelo().getConteudoTpBlob())))) {
			Ex.getInstance().getBL().processarComandosEmTag(doc, "pre_assinatura");
		}

		result.include("sigla", this.getSigla());
		result.include("doc", this.getDoc());
		result.include("titular", this.getTitular());
		result.include("lotaTitular", this.getLotaTitular());

	}

	@Get("app/expediente/mov/redefinir_nivel_acesso")
	public void redefinirNivelAcesso(String sigla) throws Exception {
		this.sigla = sigla;
		this.buscarDocumento(true);
		this.setNivelAcesso(doc.getExNivelAcesso().getIdNivelAcesso());

		result.include("substituicao", substituicao);
		result.include("sigla", sigla);
		result.include("dtMovString", dtMovString);
		result.include("campos", getTitularSel().getId());
		result.include("mob", mob);
		result.include("listaNivelAcesso", getListaNivelAcesso());
		result.include("nivelAcesso", nivelAcesso);

		result.include("subscritorSel", getSubscritorSel());
		result.include("titularSel", getTitularSel());
	}

	@Post("app/expediente/mov/redefinir_nivel_acesso_gravar")
	public void redefinirNivelAcessoGravar(String sigla, DpPessoaSelecao subscritorSel, DpPessoaSelecao titularSel, String dtMovString, boolean substituicao,
			Long nivelAcesso) throws Exception {
		this.sigla = sigla;
		this.subscritorSel = subscritorSel;
		this.titularSel = titularSel;
		this.dtMovString = dtMovString;
		this.substituicao = substituicao;
		this.nivelAcesso = nivelAcesso;

		buscarDocumento(true);
		lerForm(mov);

		ExNivelAcesso exTipoSig = null;

		if (getNivelAcesso() != null) {
			exTipoSig = dao().consultar(getNivelAcesso(), ExNivelAcesso.class, false);
		}

		if (!Ex.getInstance().getComp().podeRedefinirNivelAcesso(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível redefinir o nível de acesso");

		try {
			Ex.getInstance()
					.getBL()
					.redefinirNivelAcesso(getCadastrante(), getLotaTitular(), doc, mov.getDtMov(), mov.getLotaResp(), mov.getResp(), mov.getSubscritor(),
							mov.getTitular(), mov.getNmFuncaoSubscritor(), exTipoSig);
		} catch (final Exception e) {
			throw e;
		}
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Get("/app/expediente/mov/cancelarMovimentacao")
	public void aCancelarUltimaMovimentacao(String sigla) throws Exception {
		setSigla(sigla);
		buscarDocumento(true);

		final ExMovimentacao exUltMovNaoCanc = mob.getUltimaMovimentacaoNaoCancelada();
		final ExMovimentacao exUltMov = mob.getUltimaMovimentacao();

		if (exUltMovNaoCanc.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO
				&& exUltMovNaoCanc.getIdMov() == exUltMov.getIdMov()) {
			if (!Ex.getInstance().getComp().podeCancelarVia(getTitular(), getLotaTitular(), mob))
				throw new AplicacaoException("Não é possível cancelar via");
		} else {
			if (!Ex.getInstance().getComp().podeCancelarMovimentacao(getTitular(), getLotaTitular(), mob))
				throw new AplicacaoException("Não é possível cancelar movimentação");
		}

		try {
			Ex.getInstance().getBL().cancelarMovimentacao(getCadastrante(), getLotaTitular(), mob);
		} catch (final Exception e) {
			throw e;
		}

		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}

	@Get("app/expediente/mov/excluir")
	public void excluir(Long id) throws Exception {
		setId(id);
		buscarDocumento(true);
		lerForm(mov);

		try {
			Ex.getInstance().getBL().excluirMovimentacao(getCadastrante(), getLotaTitular(), mob, getId());
		} catch (final Exception e) {
			throw e;
		}
		result.redirectTo(MessageFormat.format("anexar?sigla={0}", getDoc().getSigla()));
	}

	@Get("app/expediente/mov/exibir")
	public void aExibir(boolean popup, Long id) throws Exception {
		this.setId(id);
		buscarDocumento(true);

		if (getId() == null)
			throw new AplicacaoException("id não foi informada.");

		mov = dao().consultar(getId(), ExMovimentacao.class, false);

		setEnderecoAutenticacao(SigaExProperties.getEnderecoAutenticidadeDocs());

		result.include("id", this.getId());
		result.include("doc", this.getDoc());
		result.include("mov", this.getMov());
		result.include("enderecoAutenticacao", this.getEnderecoAutenticacao());
	}

	@Post("app/expediente/mov/protocolo_transf")
	public void aGerarProtocolo(String movId, Long pessoa, String dt, List<String> itens, String campoData, String campoPara, String campoDe) throws Exception {
		this.setDtMovString(dt);
		this.setMov(mov);
		lerForm(mov);
		final Pattern p = Pattern.compile("chk_([0-9]+)");
		ArrayList al = criarListaDocumentos(itens);
		this.setMov(criarMov(movId));
		Object[] arr = al.toArray();

		Arrays.sort(arr, new Comparator() {
			public int compare(Object obj1, Object obj2) {
				ExDocumento doc1 = (ExDocumento) ((Object[]) obj1)[0];
				ExMovimentacao mov1 = (ExMovimentacao) ((Object[]) obj1)[1];
				ExDocumento doc2 = (ExDocumento) ((Object[]) obj2)[0];
				ExMovimentacao mov2 = (ExMovimentacao) ((Object[]) obj2)[1];

				if (doc1.getAnoEmissao() > doc2.getAnoEmissao())
					return 1;
				else if (doc1.getAnoEmissao() < doc2.getAnoEmissao())
					return -1;
				else if (doc1.getExFormaDocumento().getIdFormaDoc() > doc2.getExFormaDocumento().getIdFormaDoc())
					return 1;
				else if (doc1.getExFormaDocumento().getIdFormaDoc() < doc2.getExFormaDocumento().getIdFormaDoc())
					return -1;
				else if (doc1.getNumExpediente() > doc2.getNumExpediente())
					return 1;
				else if (doc1.getNumExpediente() < doc2.getNumExpediente())
					return -1;
				else if (mov1.getExMobil().getExTipoMobil().getIdTipoMobil() > mov2.getExMobil().getExTipoMobil().getIdTipoMobil())
					return 1;
				else if (mov1.getExMobil().getExTipoMobil().getIdTipoMobil() < mov2.getExMobil().getExTipoMobil().getIdTipoMobil())
					return -1;
				else if (mov1.getExMobil().getNumSequencia() > mov2.getExMobil().getNumSequencia())
					return 1;
				else if (mov1.getExMobil().getNumSequencia() < mov2.getExMobil().getNumSequencia())
					return -1;
				else if (doc1.getIdDoc() > doc2.getIdDoc())
					return 1;
				else if (doc1.getIdDoc() < doc2.getIdDoc())
					return -1;
				else
					return 0;
			}
		});

		al.clear();
		for (int k = 0; k < arr.length; k++)
			al.add(arr[k]);
		setItens(al);

		result.include("campoDe", campoDe);
		result.include("campoPara", campoPara);
		result.include("campoData", campoData);

		result.include("itens", this.getItens());
		result.include("cadastrante", this.getCadastrante());
		result.include("mov", this.getMov());
		result.include("lotaTitular", this.getLotaTitular());
	}

	private ArrayList criarListaDocumentos(List<String> itens) {
		ArrayList listarDocumentos = new ArrayList();

		for (String idMubString : itens) {
			try {
				Long idMob = Long.parseLong(idMubString);
				final ExMobil mob = dao().consultar(idMob, ExMobil.class, false);
				final Object[] ao = { mob.doc(), mob.getUltimaMovimentacaoNaoCancelada() };
				listarDocumentos.add(ao);
			} catch (NumberFormatException nfe) {
				System.out.println(MessageFormat.format("{0} nao pode ser convertido para Long", idMubString));
			}
		}
		return listarDocumentos;
	}

	private ExMovimentacao criarMov(String movIdString) {
		try {
			Long idMov = Long.parseLong(movIdString);
			final ExMovimentacao mov = dao.consultar(idMov, ExMovimentacao.class, false);
		} catch (NumberFormatException nfe) {
			System.out.println(MessageFormat.format("{0} nao pode ser convertido para Long", movIdString));
		}
		return mov;
	}

	@Get("app/expediente/mov/protocolo_unitario")
	public void protocolo(String sigla, Long id) throws Exception {
		setId(id);
		setSigla(sigla);
		buscarDocumento(true);

		mov = dao().consultar(getId(), ExMovimentacao.class, false);

		ArrayList lista = new ArrayList();
		final Object[] ao = { doc, mov };
		lista.add(ao);
		setItens(lista);
		result.include("cadastrante", getCadastrante());
		result.include("mov", getMov());
		result.include("itens", getItens());
		result.include("lotaTitular", getLotaTitular());
	}

	@Get("app/expediente/mov/juntar")
	public void juntar(String sigla) throws Exception {
		this.sigla = sigla;
		this.buscarDocumento(true);

		if (!Ex.getInstance().getComp().podeJuntar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível fazer juntada");

		result.include("sigla", this.getSigla());
		result.include("mob", this.getMob());
		result.include("doc", this.getDoc());
		result.include("substituicao", this.isSubstituicao());
		result.include("dtMovString", this.getDtMovString());
		result.include("campos", this.getTitularSel().getId());
		result.include("idDocumentoPaiExterno", this.getIdDocumentoPaiExterno());

		result.include("titularSel", getTitularSel());
		result.include("documentoRefSel", getDocumentoRefSel());
		result.include("subscritorSel", getSubscritorSel());
	}

	@Post("app/expediente/mov/juntar_gravar")
	public void aJuntarGravar(Integer postback, String sigla, String dtMovString, boolean substituicao, String idDocumentoPaiExterno,
			DpPessoaSelecao subscritorSel, DpPessoaSelecao titularSel, ExMobilSelecao documentoRefSel, String idDocumentoEscolha) throws Exception {

		this.setSigla(sigla);
		this.setDtMovString(dtMovString);
		this.setSubstituicao(substituicao);
		this.setIdDocumentoPaiExterno(idDocumentoPaiExterno);
		this.setSubscritorSel(subscritorSel);
		this.setTitularSel(titularSel);
		this.setDocumentoRefSel(documentoRefSel);
		this.setIdDocumentoEscolha(idDocumentoEscolha);
		this.setPostback(postback);

		if (this.getDocumentoRefSel() == null) {
			this.setDocumentoRefSel(new ExMobilSelecao());
		}

		if (this.getSubscritorSel() == null) {
			this.setSubscritorSel(new DpPessoaSelecao());
		}

		this.buscarDocumento(true);
		this.lerForm(this.getMov());

		if (!Ex.getInstance().getComp().podeJuntar(getTitular(), getLotaTitular(), this.getMob()))
			throw new AplicacaoException("Não é possível fazer juntada");

		// Nato: precisamos rever o codigo abaixo, pois a movimentacao nao pode
		// ser gravada sem hora, minuto e segundo.
		if (mov.getExDocumento().isEletronico()) {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("dd/MM/yyyy");
			setDtRegMov(sdf.format(new Date()).toString());
			mov.setSubscritor(getTitular());
		}

		try {
			// Quando o documento e eletronico, o responsavel pela juntada fica
			// sendo o proprio cadastrante e a data fica sendo a data atual
			if (mov.getExDocumento().isEletronico()) {
				mov.setDtMov(new Date());
				mov.setSubscritor(getCadastrante());
				mov.setTitular(getTitular());
			}

			Ex.getInstance()
					.getBL()
					.juntarDocumento(getCadastrante(), getTitular(), getLotaTitular(), getIdDocumentoPaiExterno(), mob, mov.getExMobilRef(), mov.getDtMov(),
							mov.getSubscritor(), mov.getTitular(), getIdDocumentoEscolha());
		} catch (final Exception e) {
			throw e;
		}
		ExDocumentoController.redirecionarParaExibir(result, this.getSigla());
	}

	@Get("app/expediente/mov/apensar")
	public void apensar(String sigla) throws Exception {
		this.setSigla(sigla);
		this.buscarDocumento(true);

		if (!Ex.getInstance().getComp().podeApensar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível apensar");

		result.include("mob", mob);
		result.include("doc", doc);
		result.include("substituicao", substituicao);
		result.include("sigla", sigla);
		result.include("documentoRefSel", getDocumentoRefSel());
		result.include("titularSel", getTitularSel());
		result.include("subscritorSel", getSubscritorSel());
	}

	@Post("app/expediente/mov/apensar_gravar")
	public void apensarGravar(ExMobilSelecao documentoRefSel, DpPessoaSelecao subscritorSel, DpPessoaSelecao titularSel, String sigla, String dtMovString,
			boolean substituicao) throws Exception {

		this.dtMovString = dtMovString;
		this.sigla = sigla;
		this.substituicao = substituicao;
		this.documentoRefSel = documentoRefSel;
		this.subscritorSel = subscritorSel;
		this.titularSel = titularSel;

		this.buscarDocumento(true);
		this.lerForm(mov);

		if (!Ex.getInstance().getComp().podeApensar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível fazer apensar");

		try {
			// Quando o documento e eletronico, o responsavel pela juntada fica
			// sendo o proprio cadastrante e a data fica sendo a data atual
			if (mov.getExDocumento().isEletronico()) {
				mov.setDtMov(new Date());
				mov.setSubscritor(getCadastrante());
				mov.setTitular(getTitular());
			}

			Ex.getInstance()
					.getBL()
					.apensarDocumento(getCadastrante(), getTitular(), getLotaTitular(), mob, mov.getExMobilRef(), mov.getDtMov(), mov.getSubscritor(),
							mov.getTitular());
		} catch (final Exception e) {
			throw e;
		}
		ExDocumentoController.redirecionarParaExibir(result, mov.getExDocumento().getSigla());
	}

	@Get("/app/expediente/mov/registrar_assinatura")
	public void aRegistrarAssinatura(String sigla) throws Exception {
		this.setSigla(sigla);
		buscarDocumento(true);

		if (doc.getSubscritor() != null) {
			DpPessoaSelecao sub = new DpPessoaSelecao();
			sub.setId(doc.getSubscritor().getId());
			sub.buscar();
			setSubscritorSel(sub);
		}

		if (!Ex.getInstance().getComp().podeRegistrarAssinatura(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível registrar a assinatura");

		setSubstituicao(false);

		result.include("mob", this.getMob());
		result.include("sigla", this.getSigla());
		result.include("dtMovString", this.getDtMovString());
		result.include("subscritorSel", this.getSubscritorSel());
		result.include("substituicao", this.isSubstituicao());
		result.include("titularSel", this.getTitularSel());
	}

	@Post("/app/expediente/mov/registrar_assinatura_gravar")
	public void registrar_assinatura_gravar(int postback, String sigla, String dtMovString, DpPessoaSelecao subscritorSel, boolean substituicao,
			DpPessoaSelecao tilularSel) throws Exception {
		this.setPostback(postback);
		this.setSigla(sigla);
		this.setDtMovString(dtMovString);
		this.setSubscritorSel(subscritorSel);
		this.setSubstituicao(substituicao);
		this.setTitularSel(tilularSel);

		if (this.getSubscritorSel() == null)
			this.setSubscritorSel(new DpPessoaSelecao());

		if (this.getTitularSel() == null)
			this.setTitularSel(new DpPessoaSelecao());

		buscarDocumento(true);
		lerForm(mov);

		if (mov.getSubscritor() == null)
			throw new AplicacaoException("Responsável não informado");

		if (!Ex.getInstance().getComp().podeRegistrarAssinatura(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível registrar a assinatura");

		try {
			setMsg(Ex.getInstance().getBL().RegistrarAssinatura(getCadastrante(), getLotaTitular(), doc, mov.getDtMov(), mov.getSubscritor(), mov.getTitular()));
		} catch (final Exception e) {
			throw e;
		}

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + this.getSigla());

	}

	@Get("/app/expediente/mov/incluir_cosignatario")
	public void incluirCosignatario(String sigla) throws Exception {
		this.setSigla(sigla);
		buscarDocumento(true);

		if (!Ex.getInstance().getComp().podeIncluirCosignatario(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível incluir cossignatário");
		result.include("sigla", sigla);
		result.include("documento", doc);
		result.include("mob", mob);
		result.include("cosignatarioSel", cosignatarioSel);
	}

	@Post("/app/expediente/mov/incluir_cosignatario_gravar")
	public void aIncluirCosignatarioGravar(String sigla, DpPessoaSelecao cosignatarioSel, String funcaoCosignatario, Integer postback) throws Exception {
		this.setPostback(postback);
		this.setFuncaoCosignatario(funcaoCosignatario);
		this.setSigla(sigla);
		this.setCosignatarioSel(cosignatarioSel);
		buscarDocumento(true);
		lerForm(mov);

		if (this.getCosignatarioSel() == null)
			this.setCosignatarioSel(new DpPessoaSelecao());

		mov.setDescrMov(getFuncaoCosignatario());
		if (getCosignatarioSel().getId() != null) {
			mov.setSubscritor(dao().consultar(getCosignatarioSel().getId(), DpPessoa.class, false));
		} else {
			mov.setSubscritor(null);
		}

		if (!Ex.getInstance().getComp().podeIncluirCosignatario(getTitular(), getLotaTitular(), this.mob))
			throw new AplicacaoException("Não é possível incluir cossignatário");

		Ex.getInstance().getBL().incluirCosignatario(getCadastrante(), getLotaTitular(), doc, mov.getDtMov(), mov.getSubscritor(), mov.getDescrMov());

		ExDocumentoController.redirecionarParaExibir(result, mov.getExDocumento().getSigla());

	}

	// Nato: Temos que substituir por uma tela que mostre os itens marcados como
	// "em transito"
	@Get("/app/expediente/mov/receber_lote")
	public void aReceberLote() throws Exception {
		final List<ExMobil> provItens = dao().consultarParaReceberEmLote(getLotaTitular());

		final List<ExMobil> itens = new ArrayList<ExMobil>();

		for (ExMobil m : provItens) {
			if (!m.isApensado() && Ex.getInstance().getComp().podeAcessarDocumento(getTitular(), getLotaTitular(), m)) {
				itens.add(m);
			}
		}

		result.include("itens", itens);
	}

	@Post("/app/expediente/mov/receber_lote_gravar")
	public void aReceberLoteGravar(Integer postback) throws Exception {
		this.setPostback(postback);

		final ExMovimentacaoBuilder builder = ExMovimentacaoBuilder.novaInstancia();
		final ExMovimentacao mov = builder.construir(dao());

		final Pattern p = Pattern.compile("chk_([0-9]+)");

		for (final String s : getPar().keySet()) {
			if (s.startsWith("chk_") && param(s).equals("true")) {
				final Matcher m = p.matcher(s);
				if (!m.find()) {
					throw new AplicacaoException("Não foi possível ler a Id do documento e o número da via.");
				}
				final ExMobil mob = dao().consultar(Long.valueOf(m.group(1)), ExMobil.class, false);

				if (Ex.getInstance().getComp().podeReceber(getTitular(), getLotaTitular(), mob)) {
					Ex.getInstance().getBL().receber(getCadastrante(), getLotaTitular(), mob, mov.getDtMov());
				}
			}
		}

		result.redirectTo("/app/expediente/mov/receber_lote");
	}

	@Get("/app/expediente/mov/arquivar_corrente_lote")
	public void aArquivarCorrenteLote() throws Exception {
		final List<ExMobil> provItens = dao().consultarParaArquivarCorrenteEmLote(getLotaTitular());

		List<ExMobil> itens = new ArrayList<ExMobil>();

		for (ExMobil m : provItens) {
			if (!m.isApensado() && Ex.getInstance().getComp().podeAcessarDocumento(getTitular(), getLotaTitular(), m)) {
				itens.add(m.isVolume() ? m.doc().getMobilGeral() : m);
			}
		}

		result.include("itens", itens);
	}

	@Post("/app/expediente/mov/arquivar_corrente_lote_gravar")
	public void aArquivarCorrenteLoteGravar(Integer postback) throws Exception {
		this.setPostback(postback);

		final ExMovimentacaoBuilder builder = ExMovimentacaoBuilder.novaInstancia();
		final ExMovimentacao mov = builder.construir(dao());

		final Pattern p = Pattern.compile("chk_([0-9]+)");
		final Date dt = dao().dt();

		for (final String s : getPar().keySet()) {
			if (s.startsWith("chk_") && param(s).equals("true")) {
				final Matcher m = p.matcher(s);
				if (!m.find()) {
					throw new AplicacaoException("Não foi possível ler a Id do documento e o número da via.");
				}
				final ExMobil mob = dao().consultar(Long.valueOf(m.group(1)), ExMobil.class, false);

				Ex.getInstance().getBL().arquivarCorrente(getCadastrante(), getLotaTitular(), mob, mov.getDtMov(), dt, mov.getSubscritor(), false);
			}
		}

		result.redirectTo("/app/expediente/mov/arquivar_corrente_lote");
	}

	@Get("/app/expediente/mov/assinar_despacho_lote")
	public void aAssinarDespachoLote() throws Exception {
		final List<ExMovimentacao> itensComoSubscritor = dao().listarDespachoPendenteAssinatura(getTitular());

		final List<ExMovimentacao> itens = new ArrayList<ExMovimentacao>();
		final List<ExMovimentacao> movimentacoesQuePodemSerAssinadasComSenha = new ArrayList<ExMovimentacao>();

		for (ExMovimentacao mov : itensComoSubscritor) {
			if (!mov.isAssinada() && !mov.isCancelada()) {
				itens.add(mov);

				if (Ex.getInstance().getComp().podeAssinarMovimentacaoComSenha(getTitular(), getLotaTitular(), mov)) {
					movimentacoesQuePodemSerAssinadasComSenha.add(mov);
				}
			}

		}

		result.include("itens", itens);
		result.include("movimentacoesQuePodemSerAssinadasComSenha", movimentacoesQuePodemSerAssinadasComSenha);
	}

	@Get("/app/expediente/mov/referenciar")
	public void aReferenciar(String sigla) throws Exception {
		this.setSigla(sigla);
		buscarDocumento(true);
		if (!Ex.getInstance().getComp().podeReferenciar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível fazer vinculação");

		result.include("sigla", this.getSigla());
		result.include("doc", this.getDoc());
		result.include("mob", this.getMob());
		result.include("substituicao", this.isSubstituicao());
		result.include("titularSel", this.getTitularSel());
		result.include("documentoRefSel", this.getDocumentoRefSel());
		result.include("subscritorSel", this.getSubscritorSel());
	}

	@Post("app/expediente/mov/prever")
	public void preve(String sigla) throws Exception {
		this.setSigla(sigla);
		buscarDocumento(true);

		if (getId() != null) {
			mov = daoMov(getId());
			doc = mov.getExDocumento();
		} else {
			mov = new ExMovimentacao();
			lerForm(mov);
			doc = mov.getExDocumento();
		}

		if (param("processar_modelo") != null)
			result.forwardTo(this).processa_modelo();
		else {
			result.include("par", getRequest().getParameterMap());
			result.include("modelo", getModelo());
			result.include("nmArqMod", getModelo().getNmArqMod());
			result.include("mov", mov);
		}
	}

	private void processa_modelo() throws Exception {
		result.include("par", getRequest().getParameterMap());
		result.include("modelo", getModelo());
		result.include("nmArqMod", getModelo().getNmArqMod());
		result.include("mov", mov);
	}

	@Post("/app/expediente/mov/referenciar_gravar")
	public void aReferenciarGravar(String sigla, String dtMovString, boolean substituicao, DpPessoaSelecao titularSel, DpPessoaSelecao subscritorSel,
			ExMobilSelecao documentoRefSel) throws Exception {
		this.setSigla(sigla);
		this.setDtMovString(dtMovString);
		this.setSubstituicao(substituicao);
		this.setTitularSel(titularSel);
		this.setDocumentoRefSel(documentoRefSel);
		this.setSubscritorSel(subscritorSel);

		if (this.getTitularSel() == null)
			this.setTitularSel(new DpPessoaSelecao());

		if (this.getDocumentoRefSel() == null)
			this.setDocumentoRefSel(new ExMobilSelecao());

		if (this.getSubscritorSel() == null)
			this.setSubscritorSel(new DpPessoaSelecao());

		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance().getComp().podeReferenciar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível fazer vinculação");
		if (mov.getExMobilRef() == null)
			throw new AplicacaoException("Não foi selecionado um documento para a vinculação");

		if (mov.getExDocumento().isEletronico()) {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("dd/MM/yyyy");
			setDtRegMov(sdf.format(new Date()).toString());
			mov.setSubscritor(getTitular());
		}

		try {
			Ex.getInstance().getBL()
					.referenciarDocumento(getCadastrante(), getLotaTitular(), mob, mov.getExMobilRef(), mov.getDtMov(), mov.getSubscritor(), mov.getTitular());
		} catch (final Exception e) {
			throw e;
		}

		setDoc(mov.getExDocumento());

		ExDocumentoController.redirecionarParaExibir(result, mov.getExDocumento().getSigla());

	}

	@Get("/app/expediente/mov/transferir")
	public void aTransferir(String sigla) throws Exception {
		this.sigla = sigla;
		buscarDocumento(true);
		lerForm(mov);

		final ExMovimentacao ultMov = mob.getUltimaMovimentacao();
		if (getRequest().getAttribute("postback") == null) {
			if (ultMov.getLotaDestinoFinal() != null) {
				getLotaDestinoFinalSel().buscarPorObjeto(ultMov.getLotaDestinoFinal());
				setTipoDestinoFinal(1); // Orgao interno
				getLotaResponsavelSel().buscarPorObjeto(ultMov.getLotaDestinoFinal());
				setTipoResponsavel(1);
			}
			if (ultMov.getDestinoFinal() != null) {
				getDestinoFinalSel().buscarPorObjeto(ultMov.getDestinoFinal());
				setTipoDestinoFinal(2); // Matricula
				getResponsavelSel().buscarPorObjeto(ultMov.getDestinoFinal());
				setTipoResponsavel(2);
			}
		}

		if (!(Ex.getInstance().getComp().podeTransferir(getTitular(), getLotaTitular(), mob) || Ex.getInstance().getComp()
				.podeDespachar(getTitular(), getLotaTitular(), mob)))
			throw new AplicacaoException("Não é possível fazer despacho nem transferência");

		result.include("doc", this.getDoc());
		result.include("mob", this.getMob());
		result.include("mov", this.getMov());
		result.include("postback", this.getPostback());
		result.include("sigla", this.getSigla());
		result.include("dtMovString", this.getDtMovString());
		result.include("subscritorSel", this.getSubscritorSel());
		result.include("substituicao", this.isSubstituicao());
		result.include("titularSel", this.getTitularSel());
		result.include("nmFuncaoSubscritor", this.getNmFuncaoSubscritor());
		result.include("idTpDespacho", this.getIdTpDespacho());
		result.include("idResp", this.getIdResp());
		result.include("tiposDespacho", this.getTiposDespacho());
		result.include("descrMov", this.getDescrMov());
		result.include("listaTipoResp", this.getListaTipoResp());
		result.include("tipoResponsavel", this.getTipoResponsavel());

		result.include("lotaResponsavelSel", this.getLotaResponsavelSel());
		result.include("responsavelSel", this.getResponsavelSel());
		result.include("cpOrgaoSel", this.getCpOrgaoSel());
		result.include("obsOrgao", this.getObsOrgao());
	}

	@Post("/app/expediente/mov/transferir_gravar")
	public void transferir_gravar(int postback, String sigla, String dtMovString, DpPessoaSelecao subscritorSel, boolean substituicao,
			DpPessoaSelecao titularSel, String nmFuncaoSubscritor, long idTpDespacho, long idResp, List<ExTipoDespacho> tiposDespacho, String descrMov,
			List<Map<Integer, String>> listaTipoResp, int tipoResponsavel, DpLotacaoSelecao lotaResponsavel, DpPessoaSelecao responsavelSel,
			CpOrgaoSelecao cpOrgacaoSel, String obsOrgao, String protocolo) throws Exception {
		this.setId(id);
		this.setSigla(sigla);
		this.setPostback(postback);
		this.setDtMovString(dtMovString);
		this.setSubscritorSel(subscritorSel);
		this.setSubstituicao(substituicao);
		this.setTitularSel(titularSel);
		this.setNmFuncaoSubscritor(nmFuncaoSubscritor);
		this.setIdTpDespacho(idTpDespacho);
		this.setIdResp(idResp);
		this.setDescrMov(descrMov);
		this.setTipoResponsavel(tipoResponsavel);

		if (this.lotaSubscritorSel == null)
			this.lotaSubscritorSel = new DpLotacaoSelecao();

		if (this.subscritorSel == null)
			this.subscritorSel = new DpPessoaSelecao();

		if (this.titularSel == null)
			this.titularSel = new DpPessoaSelecao();

		if (this.responsavelSel == null)
			this.responsavelSel = new DpPessoaSelecao();

		if (this.lotaResponsavelSel == null)
			this.lotaResponsavelSel = new DpLotacaoSelecao();

		buscarDocumento(true);
		lerForm(mov);
		final ExMovimentacao UltMov = mob.getUltimaMovimentacaoNaoCancelada();
		if ((mov.getLotaResp() != null && mov.getResp() == null && UltMov.getLotaResp() != null && UltMov.getResp() == null && UltMov.getLotaResp().equivale(
				mov.getLotaResp()))
				|| (mov.getResp() != null && UltMov.getResp() != null && UltMov.getResp().equivale(mov.getResp())))
			throw new AplicacaoException("Novo responsável não pode ser igual ao atual");

		if (!Ex.getInstance().getComp().podeReceberPorConfiguracao(mov.getResp(), mov.getLotaResp()))
			throw new AplicacaoException("Destinatário não pode receber documentos");

		if (!(Ex.getInstance().getComp()

		.podeTransferir(getTitular(), getLotaTitular(), mob) || Ex.getInstance().getComp().podeDespachar(getTitular(), getLotaTitular(), mob)))
			throw new AplicacaoException("Não é possível fazer despacho nem transferência");

		try {
			Ex.getInstance()
					.getBL()
					.transferir(mov.getOrgaoExterno(), getObsOrgao(), getCadastrante(), getLotaTitular(), mob, mov.getDtMov(), mov.getDtIniMov(),
							mov.getDtFimMov(), mov.getLotaResp(), mov.getResp(), mov.getLotaDestinoFinal(), mov.getDestinoFinal(), mov.getSubscritor(),
							mov.getTitular(), mov.getExTipoDespacho(), false, mov.getDescrMov(), conteudo, mov.getNmFuncaoSubscritor(), false, false);
		} catch (final Exception e) {
			throw e;
		}
		setMov(mob.getUltimaMovimentacao());

		if (protocolo != null && protocolo.equals("mostrar"))
			result.forwardTo(this).transferido();

		result.redirectTo(this).fechar_popup();

	}

	@Get("/app/expediente/mov/fechar_popup")
	public void fechar_popup() throws Exception {
		System.out.println("popup fechado.");
	}

	@Get("/app/expediente/mov/transferido")
	public void transferido() throws Exception {
		result.include("doc", getDoc());
		result.include("mov", getMov());
		result.include("id", getId());
	}

	@Get("app/expediente/mov/encerrar_volume")
	public void encerrarVolumeGravar(String sigla) throws Exception {
		this.sigla = sigla;
		buscarDocumento(true);
		lerForm(mov);

		if (mob.isVolumeEncerrado())
			throw new AplicacaoException("Não é permitido encerrar um volume já encerrado.");
		try {
			Ex.getInstance().getBL()

			.encerrarVolume(getCadastrante(), getLotaTitular(), mob, mov.getDtMov(), mov.getSubscritor(), mov.getTitular(), mov.getNmFuncaoSubscritor(), false);
		} catch (final Exception e) {
			throw e;
		}
		ExDocumentoController.redirecionarParaExibir(result, mov.getExDocumento().getSigla());
	}

	@Get("/app/expediente/mov/anotar")
	public void aAnotar(String sigla) throws Exception {
		this.setSigla(sigla);
		buscarDocumento(true);

		if (!Ex.getInstance().getComp().podeFazerAnotacao(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível fazer anotação");

		result.include("sigla", this.getSigla());
		result.include("dtMovString", this.getDtMovString());
		result.include("mob", this.getMob());
		result.include("mov", this.getMov());
		result.include("doc", this.getDoc());
		result.include("substituicao", this.isSubstituicao());
		result.include("nmFuncaoSubscritor", this.getNmFuncaoSubscritor());
		result.include("descrMov", this.getDescrMov());
		result.include("tipoResponsavel", this.getTipoResponsavel());
		result.include("obsOrgao", this.getObsOrgao());
		result.include("subscritorSel", this.getSubscritorSel());
		result.include("titularSel", this.getTitularSel());

	}

	@Post("/app/expediente/mov/anotar_gravar")
	public void anotar_gravar(Integer postback, String sigla, String dtMovString, DpPessoaSelecao subscritorSel, boolean substituicao,
			DpPessoaSelecao titularSel, String nmFuncaoSubscritor, String descrMov, String obsOrgao, String[] campos) throws Exception {
		this.setPostback(postback);
		this.setSigla(sigla);
		this.setDtMovString(dtMovString);
		this.setSubscritorSel(subscritorSel);
		this.setSubstituicao(substituicao);
		this.setTitularSel(titularSel);
		this.setNmFuncaoSubscritor(nmFuncaoSubscritor);
		this.setDescrMov(descrMov);
		this.setObsOrgao(obsOrgao);

		if (this.subscritorSel == null)
			subscritorSel = new DpPessoaSelecao();

		if (this.titularSel == null)
			titularSel = new DpPessoaSelecao();

		buscarDocumento(true);
		lerForm(mov);

		final ExMovimentacao UltMov = mob.getUltimaMovimentacaoNaoCancelada();

		if (!Ex.getInstance().getComp().podeFazerAnotacao(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível fazer anotação");

		try {
			Ex.getInstance()
					.getBL()
					.anotar(getCadastrante(), getLotaTitular(), mob, mov.getDtMov(), mov.getLotaResp(), mov.getResp(), mov.getSubscritor(), mov.getTitular(),
							mov.getDescrMov(), mov.getNmFuncaoSubscritor());

			result.redirectTo("/app/expediente/doc/exibir?sigla=" + this.getSigla());

		} catch (final Exception e) {
			throw e;
		}

	}

	@Get("/app/expediente/mov/anotar_lote")
	public void aAnotarLote() throws Exception {
		final List<ExMobil> provItens = dao().consultarParaAnotarEmLote(getLotaTitular());

		final List<ExMobil> itens = new ArrayList<ExMobil>();

		for (ExMobil m : provItens) {
			if (!m.isApensado() && Ex.getInstance().getComp().podeAcessarDocumento(getTitular(), getLotaTitular(), m)) {
				itens.add(m);
			}
		}

		result.include("itens", itens);
	}

	@Post("/app/expediente/mov/anotar_lote_gravar")
	public void aAnotarLoteGravar(Integer postback, String dtMovString, DpPessoaSelecao subscritorSel, boolean substituicao, DpPessoaSelecao titularSel,
			String nmFuncaoSubscritor, String descrMov, String obsOrgao, String[] campos) throws Exception {
		this.setPostback(postback);

		ExMovimentacaoBuilder builder = ExMovimentacaoBuilder.novaInstancia();

		builder.setDtMovString(dtMovString).setSubscritorSel(subscritorSel).setSubstituicao(substituicao).setTitularSel(titularSel)
				.setNmFuncaoSubscritor(nmFuncaoSubscritor).setDescrMov(descrMov).setObsOrgao(obsOrgao);

		final ExMovimentacao mov = builder.construir(dao());

		final Pattern p = Pattern.compile("chk_([0-9]+)");

		for (final String s : getPar().keySet()) {
			if (s.startsWith("chk_") && param(s).equals("true")) {
				final Matcher m = p.matcher(s);
				if (!m.find()) {
					throw new AplicacaoException("Não foi possível ler a Id do documento e o número da via.");
				}
				final ExMobil mob = dao().consultar(Long.valueOf(m.group(1)), ExMobil.class, false);

				Ex.getInstance()
						.getBL()
						.anotar(getCadastrante(), getLotaTitular(), mob, mov.getDtMov(), mov.getLotaResp(), mov.getResp(), mov.getSubscritor(),
								mov.getTitular(), mov.getDescrMov(), mov.getNmFuncaoSubscritor());
			}
		}

		result.redirectTo("/app/expediente/mov/anotar_lote");
	}

	@Get("/app/expediente/mov/vincularPapel")
	public void aVincularPapel(String sigla) throws Exception {
		this.setSigla(sigla);
		buscarDocumento(true);

		if (!Ex.getInstance().getComp().podeFazerVinculacaoPapel(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível fazer vinculação de papel");

		result.include("sigla", this.getSigla());
		result.include("mob", this.getMob());
		result.include("dtMovString", this.getDtMovString());
		result.include("listaTipoRespPerfil", this.getListaTipoRespPerfil());
		result.include("responsavelSel", this.getResponsavelSel());
		result.include("lotaResponsavelSel", this.getLotaResponsavelSel());
		result.include("listaExPapel", this.getListaExPapel());
		result.include("tipoResponsavel", this.getTipoResponsavel());

	}

	@Post("/app/expediente/mov/vincularPapel_gravar")
	public void vincularPapel_gravar(int postback, String sigla, String dtMovString, int tipoResponsavel, DpPessoaSelecao responsavelSel,
			DpLotacaoSelecao lotaResponsavelSel, Long idPapel) throws Exception {
		this.setPostback(postback);
		this.setSigla(sigla);
		this.setDtMovString(dtMovString);
		this.setTipoResponsavel(tipoResponsavel);
		this.setResponsavelSel(responsavelSel);
		this.setLotaResponsavelSel(lotaResponsavelSel);
		this.setIdPapel(idPapel);

		if (this.getResponsavelSel() == null || this.getTipoResponsavel() == 2)
			this.setResponsavelSel(new DpPessoaSelecao());

		if (this.getLotaResponsavelSel() == null || this.getTipoResponsavel() == 1)
			this.setLotaResponsavelSel(new DpLotacaoSelecao());

		buscarDocumento(true);
		lerForm(mov);

		if (mov.getResp() == null && mov.getLotaResp() == null)
			throw new AplicacaoException("Não foi informado o responsável ou lotação responsável para a vinculação de papel ");

		if (mov.getResp() != null) {
			mov.setDescrMov(mov.getExPapel().getDescPapel() + ":" + mov.getResp().getDescricaoIniciaisMaiusculas());
		} else {
			if (mov.getLotaResp() != null) {
				mov.setDescrMov(mov.getExPapel().getDescPapel() + ":" + mov.getLotaResp().getDescricaoIniciaisMaiusculas());
			}
		}

		final ExMovimentacao UltMov = mob.getUltimaMovimentacaoNaoCancelada();

		if (!Ex.getInstance().getComp().podeFazerVinculacaoPapel(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível fazer vinculação de papel");

		try {
			Ex.getInstance()
					.getBL()
					.vincularPapel(getCadastrante(), getLotaTitular(), mob, mov.getDtMov(), mov.getLotaResp(), mov.getResp(), mov.getSubscritor(),
							mov.getTitular(), mov.getDescrMov(), mov.getNmFuncaoSubscritor(), mov.getExPapel());
		} catch (final Exception e) {
			throw e;
		}

		result.redirectTo("/app/expediente/doc/exibir?sigla=" + this.getSigla());

	}

	@Get("app/expediente/mov/transferir_lote")
	public void aTransferirLote() throws Exception {
		Iterator<ExMobil> provItens = dao().consultarParaTransferirEmLote(getLotaTitular());
		setItens(new ArrayList<ExMobil>());

		while (provItens.hasNext()) {
			ExMobil m = provItens.next();
			getItens().add(m);
		}

		result.include("listaTipoResp", this.getListaTipoResp());
		result.include("titular", this.getTitular());
		result.include("subscritorSel", this.getSubscritorSel());
		result.include("titularSel", this.getTitularSel());
		result.include("nmFuncaoSubscritor", this.getNmFuncaoSubscritor());
		result.include("lotaResponsavelSel", this.getLotaResponsavelSel());
		result.include("cpOrgaoSel", this.getCpOrgaoSel());
		result.include("responsavelSel", this.getResponsavelSel());
		result.include("tiposDespacho", this.getTiposDespacho());
		result.include("itens", this.getItens());
	}

	@Post("app/expediente/mov/transferir_lote_gravar")
	public void aTransferirLoteGravar(String dtMovString, DpPessoaSelecao subscritorSel, boolean substituicao, DpPessoaSelecao titularSel,
			String nmFuncaoSubscritor, int tipoResponsavel, DpLotacaoSelecao lotaResponsavelSel, DpPessoaSelecao lotaResponsavel, CpOrgaoSelecao cpOrgaoSel,
			String obsOrgao, Long tpdall, String txtall, boolean checkall, String campoDe, String campoPara, String campoData) throws Exception {
		this.setDtMovString(dtMovString);
		this.setSubscritorSel(subscritorSel);
		this.setSubstituicao(substituicao);
		this.setTitularSel(titularSel);
		this.setNmFuncaoSubscritor(nmFuncaoSubscritor);
		this.setTipoResponsavel(tipoResponsavel);
		this.setLotaResponsavelSel(lotaResponsavelSel);
		this.setResponsavelSel(lotaResponsavel);
		this.setCpOrgaoSel(cpOrgaoSel);
		this.setObsOrgao(obsOrgao);
		if (this.getLotaResponsavelSel() == null)
			this.setLotaResponsavelSel(new DpLotacaoSelecao());
		if (this.getResponsavelSel() == null)
			this.setResponsavelSel(new DpPessoaSelecao());
		if (this.getLotaResponsavelSel() == null)
			this.setCpOrgaoSel(new CpOrgaoSelecao());
		lerForm(mov);
		final Pattern p = Pattern.compile("chk_([0-9]+)");
		boolean despaUnico = false;
		final Date dt = dao().dt();
		mov.setDtIniMov(dt);
		ExMobil nmobil = new ExMobil();
		HashMap<ExMobil, AplicacaoException> MapMensagens = new HashMap<ExMobil, AplicacaoException>();
		List<ExMobil> Mobeis = new ArrayList<ExMobil>();
		List<ExMobil> MobilSucesso = new ArrayList<ExMobil>();

		if (mov.getResp() == null && mov.getLotaResp() == null)
			throw new AplicacaoException("Não foi definido o destino da transferência.");
		if (tpdall != null && tpdall != 0)
			despaUnico = true;

		AplicacaoException msgErroNivelAcessoso = null;

		for (final String s : getPar().keySet()) {
			try {
				if (s.startsWith("chk_") && param(s).equals("true")) {
					final Long idTpDespacho;
					if (!despaUnico)
						idTpDespacho = Long.valueOf(param(s.replace("chk_", "tpd_")));
					else
						idTpDespacho = tpdall;

					ExTipoDespacho tpd = null;
					if (idTpDespacho != null && idTpDespacho > 0)
						tpd = dao().consultar(idTpDespacho, ExTipoDespacho.class, false);

					final Matcher m = p.matcher(s);
					if (!m.find())
						throw new AplicacaoException("Não foi possível ler a Id do documento e o número da via.");

					final ExMobil mobil = dao().consultar(Long.valueOf(m.group(1)), ExMobil.class, false);

					if (!Ex.getInstance().getComp().podeAcessarDocumento(getTitular(), getLotaTitular(), mobil)) {
						if (msgErroNivelAcessoso == null)
							msgErroNivelAcessoso = new AplicacaoException("O documento não pode ser transferido por estar inacessível ao usuário.");
						if (!(msgErroNivelAcessoso.equals(null)))
							MapMensagens.put(mobil, msgErroNivelAcessoso);
					} else {
						String txt = "";
						if (!despaUnico)
							txt = param(s.replace("chk_", "txt_"));
						else
							txt = txtall;
						if (txt.equals(""))
							txt = null;

						nmobil = new ExMobil();
						nmobil = mobil;
						Mobeis.add(mobil);

						Ex.getInstance()
								.getBL()
								.transferir(mov.getOrgaoExterno(), getObsOrgao(), getCadastrante(), getLotaTitular(), mobil, mov.getDtMov(), dt,
										mov.getDtFimMov(), mov.getLotaResp(), mov.getResp(), mov.getLotaDestinoFinal(), mov.getDestinoFinal(),
										mov.getSubscritor(), mov.getTitular(), tpd, false, txt, null, mov.getNmFuncaoSubscritor(), false, false);

					}
				}
			} catch (AplicacaoException e) {
				MapMensagens.put(nmobil, e);
			}
		}

		final ArrayList al = new ArrayList();
		final ArrayList check = new ArrayList();
		final ArrayList arrays = new ArrayList();

		if (!(MapMensagens.isEmpty())) {
			for (Iterator<Entry<ExMobil, AplicacaoException>> it = MapMensagens.entrySet().iterator(); it.hasNext();) {
				Entry<ExMobil, AplicacaoException> excep = it.next();
				final Object[] ao = { excep.getKey(), excep.getValue().getMessage() };
				System.out.println("Falha: " + excep.getKey().doc().getSigla());
				System.out.println("Mensagem de erro: " + excep.getValue().getMessage());
				al.add(ao);
			}
		}

		for (Iterator<ExMobil> it = Mobeis.iterator(); it.hasNext();) {
			ExMobil mob = it.next();
			if (!(MapMensagens.containsKey(mob))) {
				MobilSucesso.add(mob);
				System.out.println("Mobil Geral: " + mob.doc().getMobilGeral().isGeral());
				final Object[] ao = { mob.doc(), mob.getUltimaMovimentacaoNaoCancelada() };
				System.out.println("Sucesso sigla: " + mob.doc().getSigla());
				check.add(ao);
			}
		}

		Object[] arr = al.toArray();
		Object[] arr_ = check.toArray();

		al.clear();
		check.clear();

		for (int k = 0; k < arr.length; k++)
			al.add(arr[k]);

		for (int k = 0; k < arr_.length; k++)
			check.add(arr_[k]);

		arrays.add(al);
		arrays.add(check);

		setItens(arrays);

		result.include("cadastrante", this.getCadastrante());
		result.include("mov", this.getMov());
		result.include("itens", this.getItens());
		result.include("lotaTitular", this.getLotaTitular());
	}

	private List<ExNivelAcesso> getListaNivelAcesso() throws Exception {
		ExFormaDocumento exForma = doc.getExFormaDocumento();
		ExClassificacao exClassif = doc.getExClassificacaoAtual();
		ExTipoDocumento exTipo = doc.getExTipoDocumento();
		ExModelo exMod = doc.getExModelo();

		return getListaNivelAcesso(exTipo, exForma, exMod, exClassif);
	}

	public Map<Integer, String> getListaTipoResp() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, "Órgão Integrado");
		map.put(2, "Matrícula");
		map.put(3, "Órgão Externo");
		return map;
	}

	private Map<Integer, String> getListaTipoRespPerfil() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, "Matrícula");
		map.put(2, "Órgão Integrado");
		return map;
	}

	private List<ExTipoDespacho> getTiposDespacho() throws AplicacaoException, Exception {
		final List<ExTipoDespacho> tiposDespacho = new ArrayList<ExTipoDespacho>();
		tiposDespacho.add(new ExTipoDespacho(0L, "[Nenhum]", "S"));
		tiposDespacho.addAll(dao().consultarAtivos());
		tiposDespacho.add(new ExTipoDespacho(-1, "[Outros] (texto curto)", "S"));

		if (mob != null && Ex.getInstance().getComp().podeCriarDocFilho(getTitular(), getLotaTitular(), mob))
			tiposDespacho.add(new ExTipoDespacho(-2, "[Outros] (texto longo)", "S"));

		return tiposDespacho;

	}

	private byte[] toByteArray(final UploadedFile upload) throws IOException {

		final InputStream is = upload.getFile();

		// Get the size of the file
		final long tamanho = upload.getSize();

		// Não podemos criar um array usando o tipo long.
		// é necessário usar o tipo int.
		if (tamanho > Integer.MAX_VALUE)
			throw new IOException("Arquivo muito grande");

		// Create the byte array to hold the data
		final byte[] meuByteArray = new byte[(int) tamanho];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < meuByteArray.length && (numRead = is.read(meuByteArray, offset, meuByteArray.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < meuByteArray.length)
			throw new IOException("Não foi possível ler o arquivo completamente " + upload.getFileName());

		// Close the input stream and return bytes
		is.close();
		return meuByteArray;
	}

	private List<ExPapel> getListaExPapel() {
		return (List<ExPapel>) HibernateUtil.getSessao().createQuery("from ExPapel").list();
	}

	private ExModelo getModelo() {
		return dao().consultarExModelo(null, "Despacho Automático");
	}

}
