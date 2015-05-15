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
package br.gov.jfrj.webwork.action;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.stat.Statistics;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPreenchimento;
import br.gov.jfrj.siga.ex.ExSituacaoConfiguracao;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoMobil;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.ExTpDocPublicacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.ex.bl.BIE.HierarquizadorBoletimInterno;
import br.gov.jfrj.siga.ex.util.FuncoesEL;
import br.gov.jfrj.siga.ex.util.GeradorRTF;
import br.gov.jfrj.siga.ex.util.PublicacaoDJEBL;
import br.gov.jfrj.siga.ex.vo.ExDocumentoVO;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.libs.webwork.CpOrgaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpLotacaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpPessoaSelecao;
import br.gov.jfrj.siga.libs.webwork.Selecao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

import com.opensymphony.xwork.Action;

public class ExDocumentoAction extends ExActionSupport {

	/**
         * 
         */
	private static final long serialVersionUID = 2051335434134663817L;

	// private static Log log = LogFactory.getLog(ExDocumentoAction.class);

	private String sigla;

	private String html;

	private String anexarString;

	private boolean exibirCompleto;

	private String anoEmissaoString;

	private File arquivo;

	private LinkedHashSet<ExPreenchimento> preenchSet = null;

	private String preenchParamRedirect;

	private String arquivoContentType;

	private String arquivoFileName;

	/** The value of the exClassificacao association. */
	private ExClassificacaoSelecao classificacaoSel;

	/** The value of the simple conteudoBlobDoc property. */
	private String conteudo;

	/** The value of the simple conteudoTpDoc property. */
	private String conteudoTpDoc;

	private CpOrgaoSelecao cpOrgaoSel;

	private String descrClassifNovo;

	/** The value of the simple descrDocumento property. */
	private String descrDocumento;

	private DpPessoaSelecao destinatarioSel;

	private ExDocumento doc;

	private ExDocumentoSelecao documentoSel;

	private ExMobilSelecao mobilPaiSel;

	private ExMobilSelecao mobilSel;

	/** The value of the simple dtDoc property. */
	private String dtDocString;

	private String dtDocOriginalString;

	/** The value of the simple dtRegDoc property. */
	private String dtRegDoc;

	private Integer eletronico;

	private Integer orgaoUsu;

	private String gravarpreench = new String();

	private String htmlTeste;

	private String htmlTesteFormato;

	private InputStream htmlTesteConvertido;

	private InputStream PdfStreamResult;

	private Long id;

	/** The composite primary key value. */
	private String idDoc;

	/** The value of the exFormaDocumento association. */
	private Integer idFormaDoc;

	private Long idMod;

	/** The value of the exTipoDocumento association. */
	private Long idTpDoc;

	private DpLotacaoSelecao lotacaoDestinatarioSel;

	private ExModelo modelo;

	/** The value of the simple nmArqDoc property. */
	private String nmArqDoc;

	private String nmDestinatario;

	private String nmFuncaoSubscritor;

	private String nmOrgaoExterno;

	/** The value of the simple nmSubscritorExt property. */
	private String nmSubscritorExt;

	private String nomePreenchimento;

	private String numAntigoDoc;

	/** The value of the simple numExpediente property. */
	private String numExpediente;

	/** The value of the simple numExtDoc property. */
	private String numExtDoc;

	private Long idMob;

	private String obsOrgao;

	private CpOrgao orgaoExterno;

	private CpOrgaoSelecao orgaoExternoDestinatarioSel;

	private CpOrgaoSelecao orgaoSel;

	private SortedMap<String, String> paramsEntrevista;

	private Long preenchimento;

	private String preenchRedirect;

	private DpPessoaSelecao subscritorSel;

	private boolean substituicao;

	private Integer tipoDestinatario;

	private DpPessoaSelecao titularSel;

	private DpPessoaSelecao ultMovCadastranteSel;

	private Long ultMovIdEstadoDoc;

	private DpLotacaoSelecao ultMovLotaCadastranteSel;

	private DpLotacaoSelecao ultMovLotaRespSel;

	private DpLotacaoSelecao ultMovLotaSubscritorSel;

	private DpPessoaSelecao ultMovRespSel;

	private String userQuery;

	private DpPessoaSelecao ultMovSubscritorSel;

	private Integer ultMovTipoSubscritor;

	private String podeExibir;

	private List<ExDocumento> results;

	private Long nivelAcesso;

	private boolean eletronicoFixo;

	private List<Serializable> showedResults;

	private String msg;

	private boolean alterouModelo;

	private String desativarDocPai;

	private String desativ;

	private String alerta;

	private ExMobil mob;

	private boolean despachando;

	private boolean criandoAnexo;

	private List<ExFormaDocumento> formasDoc;

	private List<ExModelo> modelos;

	private Long idMobilAutuado;

	private boolean autuando;

	private String descrMov;

	public boolean isCriandoAnexo() {
		return criandoAnexo;
	}

	public void setCriandoAnexo(boolean criandoAnexo) {
		this.criandoAnexo = criandoAnexo;
	}

	public Integer getTamanhoMaximoDescricao() {
		return 4000;
	}

	public boolean getDespachando() {
		return despachando;
	}

	public void setDespachando(boolean despachando) {
		this.despachando = despachando;
	}

	public boolean getAutuando() {
		return autuando;
	}

	public void setAutuando(boolean autuando) {
		this.autuando = autuando;
	}

	public String getAlerta() {
		return alerta;
	}

	public void setAlerta(String alerta) {
		this.alerta = alerta;
	}

	public boolean isMaisDe17Horas() {
		GregorianCalendar agora = new GregorianCalendar();
		agora.setTime(new Date());
		return agora.get(Calendar.HOUR_OF_DAY) >= 17;
	}

	public List<ExTpDocPublicacao> getListaPublicacao() {
		return FuncoesEL.listaPublicacao(getIdMod());
	}

	public List<ExDocumento> getListaDocsAPublicarBoletim() {
		return FuncoesEL.listaDocsAPublicarBoletim(getLotaTitular().getOrgaoUsuario());
	}

	 public List<ExDocumento> getListaDocsAPublicarBoletimPorDocumento() {
         return FuncoesEL.listaDocsAPublicarBoletimPorDocumento(getDoc());
	 }

	public HierarquizadorBoletimInterno getHierarquizadorBIE() {
		return new HierarquizadorBoletimInterno(getLotaTitular()
				.getOrgaoUsuario());
	}

	public boolean isEletronicoFixo() {
		return eletronicoFixo;
	}

	public void setEletronicoFixo(boolean eletronicoFixo) {
		this.eletronicoFixo = eletronicoFixo;
	}

	public Long getNivelAcesso() {
		return nivelAcesso;
	}

	public void setNivelAcesso(Long nivelAcesso) {
		this.nivelAcesso = nivelAcesso;
	}

	public Long getIdMobilAutuado() {
		return idMobilAutuado;
	}

	public void setIdMobilAutuado(Long idMobilAutuado) {
		this.idMobilAutuado = idMobilAutuado;
	}

	public ExDocumentoAction() {

		classificacaoSel = new ExClassificacaoSelecao();
		destinatarioSel = new DpPessoaSelecao();
		documentoSel = new ExDocumentoSelecao();
		mobilSel = new ExMobilSelecao();
		mobilPaiSel = new ExMobilSelecao();
		lotacaoDestinatarioSel = new DpLotacaoSelecao();
		orgaoExternoDestinatarioSel = new CpOrgaoSelecao();
		orgaoSel = new CpOrgaoSelecao();
		subscritorSel = new DpPessoaSelecao();
		titularSel = new DpPessoaSelecao();
		ultMovLotaRespSel = new DpLotacaoSelecao();
		ultMovRespSel = new DpPessoaSelecao();
		ultMovSubscritorSel = new DpPessoaSelecao();
		ultMovCadastranteSel = new DpPessoaSelecao();
		ultMovLotaSubscritorSel = new DpLotacaoSelecao();
		ultMovLotaCadastranteSel = new DpLotacaoSelecao();
		paramsEntrevista = new TreeMap<String, String>();
		cpOrgaoSel = new CpOrgaoSelecao();
		setEletronico(0);
		results = new LinkedList<ExDocumento>();
	}

	public String aAlterarPreenchimento() throws Exception {
		assertAcesso("");
		ExPreenchimento exPreenchimento = new ExPreenchimento();

		dao().iniciarTransacao();
		exPreenchimento.setIdPreenchimento(preenchimento);
		exPreenchimento = dao().consultar(preenchimento, ExPreenchimento.class,
				false);

		exPreenchimento.setPreenchimentoBA(getByteArrayFormPreenchimento());
		dao().gravar(exPreenchimento);
		dao().commitTransacao();

		setPreenchimento(exPreenchimento.getIdPreenchimento());

		String url = getUrlEncodedParameters();
		if (url.indexOf("preenchimento") >= 0) {
			String parte1 = url.substring(0, url.indexOf("preenchimento"));
			String parte2 = url.substring(url.indexOf("&",
					url.indexOf("&preenchimento") + 1) + 1);
			parte2 = parte2 + "&preenchimento=" + getPreenchimento();
			setPreenchRedirect(parte1 + parte2);
		} else
			setPreenchRedirect(getUrlEncodedParameters());

		return aEditar();
	}

	public String aAnexo() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		return Action.SUCCESS;
	}

	public String aCancelarDocumento() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		try {
			Ex.getInstance().getBL()
					.cancelarDocumento(getCadastrante(), getLotaTitular(), doc);
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;

	}

	// Converte encode de url de iso-8859-1 para utf-8
	private String ConverteEncodeDeUriDeIsoParaUtf(String sUri) {
		// return sUri;
		StringBuilder sb = new StringBuilder();
		String aParametros[] = sUri.split("&");
		for (int i = 0; i < aParametros.length; i++) {
			String aTupla[] = aParametros[i].split("=");
			try {
				sb.append(URLEncoder.encode(
						URLDecoder.decode(aTupla[0], "iso-8859-1"), "utf-8"));
				sb.append("=");
				sb.append(URLEncoder.encode(
						URLDecoder.decode(aTupla[1], "iso-8859-1"), "utf-8"));
			} catch (UnsupportedEncodingException e) {
			}
			if (i < aParametros.length - 1)
				sb.append("&");
		}
		return sb.toString();
	}

	public boolean validar() {
		if (getPar().get("obrigatorios") != null)
			for (String valor : getPar().get("obrigatorios"))
				if (getPar().get(valor) == null
						|| getPar().get(valor)[0].trim().equals("")
						|| getPar().get(valor)[0].trim().equals("Não")
						|| getPar().get(valor)[0].trim().equals("Nao"))
					return false;
		return true;
	}

	public String aCarregarPreenchimento() throws Exception {
		assertAcesso("");
		ExPreenchimento exPreenchimento = new ExPreenchimento();

		// Obtém arrStrBanco[], com os parâmetros vindos do banco
		exPreenchimento = dao().consultar(preenchimento, ExPreenchimento.class,
				false);
		String strBanco = new String(exPreenchimento.getPreenchimentoBA());
		String arrStrBanco[] = strBanco.split("&");
		String strBancoLimpa = new String();

		// seta os atributos da action com base nos valores do banco, fazendo o
		// decoding da string
		for (String elem : arrStrBanco) {
			String[] paramNameAndValue = ((String) elem).split("=");
			String paramName = paramNameAndValue[0];
			String paramValue = paramNameAndValue[1];
			String paramValueDecoded = URLDecoder.decode(paramValue,
					"ISO-8859-1");
			String paramValueEncodedUTF8 = URLEncoder.encode(paramValueDecoded,
					"UTF-8");
			try {
				if (!paramName.contains("Sel.id")) {
					final String mName = "set"
							+ paramName.substring(0, 1).toUpperCase()
							+ paramName.substring(1);
					if (getPar().get(paramName) != null
							|| (paramName.contains("nmOrgaoExterno"))
							|| (paramName.contains("nmDestinatario"))) {
						Class paramType = this.getClass()
								.getDeclaredField(paramName).getType();
						Constructor paramTypeContructor = paramType
								.getConstructor(new Class[] { String.class });
						final Method method = this.getClass().getMethod(mName,
								new Class[] { paramType });
						method.invoke(
								this,
								new Object[] { paramTypeContructor
										.newInstance(new Object[] { (paramValueDecoded) }) });
					}
				} else {
					final String mName = "get"
							+ paramName.substring(0, 1).toUpperCase()
							+ paramName.substring(1, paramName.indexOf(".id"));
					if (getPar().get(paramName) != null
							|| (paramName.contains("estinatarioSel.id"))) {
						final Method method = this.getClass().getMethod(mName);
						Selecao sel = (Selecao) method.invoke(this);
						sel.setId(Long.parseLong(paramValue));
						sel.buscarPorId();
					}
				}

			} catch (NoSuchMethodException nSME) {
			} catch (NoSuchFieldException nSFE) {
			} catch (NumberFormatException nfe) {
				paramValue = "";
			} catch (InvocationTargetException nfe) {
				paramValue = "";
			} finally {
				strBancoLimpa += "&" + paramName + "=" + paramValueEncodedUTF8;
			}
		}

		// Obtém arrStrURL[], com os parâmetros atuais da edita.jsp
		String strURL = getUrlEncodedParameters();
		String arrStrURL[] = strURL.split("&");
		String strURLLimpa = "";

		// limpa a url vinda do browser, tirando o que já consta na string do
		// banco, tirando também os .sigla e .descricao
		if (arrStrURL.length > 0) {
			for (String s : arrStrURL) {
				String arrStrURL2[] = s.split("=");
				if (arrStrURL2.length > 1 && !arrStrURL2[0].contains(".sigla")
						&& !arrStrURL2[0].contains(".descricao")
						&& !strBanco.contains(arrStrURL2[0] + "="))
					strURLLimpa = strURLLimpa + s + "&";
			}
		}

		setPreenchRedirect(strURLLimpa + strBancoLimpa);

		return aEditar();
	}

	public String aCriarVia() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance().getComp()
				.podeCriarVia(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível criar vias neste documento");
		try {
			Ex.getInstance().getBL()
					.criarVia(getCadastrante(), getLotaTitular(), doc);
		} catch (final Exception e) {
			throw e;
		}

		return Action.SUCCESS;
	}

	public String aCriarVolume() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance().getComp()
				.podeCriarVolume(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível criar volumes neste documento");
		try {
			Ex.getInstance().getBL()
					.criarVolume(getCadastrante(), getLotaTitular(), doc);
		} catch (final Exception e) {
			throw e;
		}

		return Action.SUCCESS;
	}

	public String aEditar() throws Exception {
		assertAcesso("");
		buscarDocumentoOuNovo(true);

		if ((getPostback() == null) || (param("docFilho") != null)) {
			tipoDestinatario = 2;
			idFormaDoc = 2;
			idTpDoc = 1L;

			ExNivelAcesso nivelDefault = getNivelAcessoDefault();
			if (nivelDefault != null) {
				nivelAcesso = nivelDefault.getIdNivelAcesso();
			} else
				nivelAcesso = 1L;

			idMod = ((ExModelo) dao().consultarAtivoPorIdInicial(
					ExModelo.class, 26L)).getIdMod();
		}

		if (isCriandoAnexo() && getId() == null && getPostback() == null) {
			idFormaDoc = 60;
			idMod = ((ExModelo) dao().consultarAtivoPorIdInicial(
					ExModelo.class, 507L)).getIdMod();
		}

		if (getDespachando() && getId() == null
				&& (getPostback() == null || getPostback() == 0)) {

			idFormaDoc = 8;

		}

		if (getId() == null && doc != null)
			setId(doc.getIdDoc());

		if (getId() == null) {
			if (getLotaTitular().isFechada())
				throw new AplicacaoException(
						"A lotação "
								+ getLotaTitular().getSiglaLotacao()
								+ " foi extinta em "
								+ new SimpleDateFormat("dd/MM/yyyy")
										.format(getLotaTitular()
												.getDataFimLotacao())
								+ ". Não é possível gerar expedientes em lotação extinta.");
			doc = new ExDocumento();
			doc.setOrgaoUsuario(getTitular().getOrgaoUsuario());
		} else {
			doc = daoDoc(getId());

			if (!Ex.getInstance().getComp()
					.podeEditar(getTitular(), getLotaTitular(), mob))
				throw new AplicacaoException(
						"Não é permitido editar documento fechado");

			if (getPostback() == null) {
				escreverForm();
				lerEntrevista(doc);
			}
		}

		if (getTipoDocumento() != null && getTipoDocumento().equals("externo")) {
			setIdMod(((ExModelo) dao().consultarAtivoPorIdInicial(
					ExModelo.class, 28L)).getIdMod());
		}
		carregarBeans();

		Long idSit = Ex
				.getInstance()
				.getConf()
				.buscaSituacao(getModelo(), doc.getExTipoDocumento(),
						getTitular(), getLotaTitular(),
						CpTipoConfiguracao.TIPO_CONFIG_ELETRONICO)
				.getIdSitConfiguracao();

		if (idSit == ExSituacaoConfiguracao.SITUACAO_OBRIGATORIO) {
			setEletronico(1);
			setEletronicoFixo(true);
		} else if (idSit == ExSituacaoConfiguracao.SITUACAO_PROIBIDO) {
			setEletronico(2);
			setEletronicoFixo(true);
		} else if (idSit == ExSituacaoConfiguracao.SITUACAO_DEFAULT
				&& (getEletronico() == null || getEletronico() == 0)) {
			setEletronico(1);
		} else if (isAlterouModelo()) {
			if (idSit == ExSituacaoConfiguracao.SITUACAO_DEFAULT) {
				setEletronico(1);
			} else {
				setEletronicoFixo(false);
				setEletronico(0);
			}
		}

		lerForm();

		// O (&& classif.getCodAssunto() != null) foi adicionado para permitir
		// que as classificações antigas, ainda não linkadas por equivalência,
		// possam ser usadas
		ExClassificacao classif = getClassificacaoSel().buscarObjeto();
		if (classif != null && classif.getHisDtFim() != null
				&& classif.getHisDtIni() != null
				&& classif.getCodAssunto() != null) {
			classif = ExDao.getInstance().consultarAtual(classif);
			if (classif != null)
				getClassificacaoSel().setId(classif.getIdClassificacao());
			else
				getClassificacaoSel().setId(null);
		}

		getSubscritorSel().buscar();
		getDestinatarioSel().buscar();
		getLotacaoDestinatarioSel().buscar();
		getOrgaoSel().buscar();
		getOrgaoExternoDestinatarioSel().buscar();
		getClassificacaoSel().buscar();
		getMobilPaiSel().buscar();

		if (getRequest().getSession().getAttribute("preenchRedirect") != null) {
			setPreenchRedirect((String) getRequest().getSession().getAttribute(
					"preenchRedirect"));
			getRequest().getSession().removeAttribute("preenchRedirect");
		}

		registraErroExtEditor();

		// Usado pela extensão editor...
		getPar().put(
				"serverAndPort",
				new String[] { getRequest().getServerName()
						+ (getRequest().getServerPort() > 0 ? ":"
								+ getRequest().getServerPort() : "") });

		// ...inclusive nas operações com preenchimento automático
		if (getPreenchRedirect() != null && getPreenchRedirect().length() > 2) {
			setPreenchRedirect(getPreenchRedirect() + "&serverAndPort="
					+ getPar().get("serverAndPort")[0]);
		}

		return Action.SUCCESS;
	}

	public String aExcluir() throws Exception {
		assertAcesso("");
		ExDocumento documento = null;
		final String sId = getRequest().getParameter("id");

		try {
			ExDao.iniciarTransacao();
			documento = daoDoc(Long.valueOf(sId));

			verificaNivelAcesso(doc.getMobilGeral());

			// Testa se existe algum valor preenchido em documento.
			// Se não houver gera ObjectNotFoundException
			final Date d = documento.getDtRegDoc();

			if (documento.isFinalizado())

				throw new AplicacaoException(
						"Documento já foi finalizado e não pode ser excluído");
			if (!Ex.getInstance().getComp()
					.podeExcluir(getTitular(), getLotaTitular(), mob))
				throw new AplicacaoException("Não é possível excluir");

			// Exclui documento da tabela de Boletim Interno
			String funcao = doc.getForm().get("acaoExcluir");
			if (funcao != null) {
				obterMetodoPorString(funcao, doc);
			}

			for (ExMovimentacao movRef : mob.getExMovimentacaoReferenciaSet())
				Ex.getInstance().getBL().excluirMovimentacao(movRef);

			dao().excluir(documento);
			ExDao.commitTransacao();

		} catch (final ObjectNotFoundException e) {
			throw new AplicacaoException(
					"Documento já foi excluído anteriormente");
		} catch (final AplicacaoException e) {
			ExDao.rollbackTransacao();
			throw e;
		} catch (final Exception e) {
			ExDao.rollbackTransacao();
			throw new AplicacaoException("Ocorreu um Erro durante a Operação",
					0, e);
		}

		return Action.SUCCESS;
	}

	private void registraErroExtEditor() {

		try {
			if (param("desconsiderarExtensao") != null
					&& param("desconsiderarExtensao").equals("true")) {

				String nomeArquivo = getRequest().getRemoteHost();
				nomeArquivo = nomeArquivo.replaceAll(":", "_");

				BufferedWriter out = new BufferedWriter(new FileWriter(
						"./siga-ex-ext-editor-erro/" + nomeArquivo));
				out.close();
			}
		} catch (IOException e) {
			int a = 0;
		}
	}

	private void obterMetodoPorString(String metodo, ExDocumento doc)
			throws Exception {
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

	public String aExcluirDocMovimentacoes() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		try {
			ExDao.iniciarTransacao();

			try {
				final Date d = doc.getDtRegDoc();
			} catch (final ObjectNotFoundException e) {
				throw new AplicacaoException(
						"Documento já foi excluído anteriormente", 1, e);
			}

			if (doc.isFinalizado())

				throw new AplicacaoException(
						"Documento já foi finalizado e não pode ser excluído",
						2);
			for (ExMobil m : doc.getExMobilSet()) {
				Set set = m.getExMovimentacaoSet();

				if (!Ex.getInstance().getComp()
						.podeExcluir(getTitular(), getLotaTitular(), m))
					throw new AplicacaoException("Não é possível excluir");

				if (set.size() > 0) {
					final Object[] aMovimentacao = set.toArray();
					for (int i = 0; i < set.size(); i++) {
						final ExMovimentacao movimentacao = (ExMovimentacao) aMovimentacao[i];
						dao().excluir(movimentacao);
					}
				}

				for (ExMarca marc : m.getExMarcaSet())
					dao().excluir(marc);

				set = m.getExMovimentacaoReferenciaSet();
				if (set.size() > 0) {
					final Object[] aMovimentacao = set.toArray();
					for (int i = 0; i < set.size(); i++) {
						final ExMovimentacao movimentacao = (ExMovimentacao) aMovimentacao[i];
						Ex.getInstance()
								.getBL()
								.excluirMovimentacao(getCadastrante(),
										getLotaTitular(),
										movimentacao.getExMobil(),
										movimentacao.getIdMov());
					}
				}

				dao().excluir(m);
			}

			// Exclui documento da tabela de Boletim Interno
			String funcao = doc.getForm().get("acaoExcluir");
			if (funcao != null) {
				obterMetodoPorString(funcao, doc);
			}

			dao().excluir(doc);
			ExDao.commitTransacao();
		} catch (final AplicacaoException e) {
			ExDao.rollbackTransacao();
			throw e;
		} catch (final Exception e) {
			ExDao.rollbackTransacao();
			throw new AplicacaoException("Ocorreu um Erro durante a Operação",
					0, e);
		}

		return Action.SUCCESS;
	}

	public String aExcluirPreenchimento() throws Exception {
		assertAcesso("");
		dao().iniciarTransacao();
		ExPreenchimento exemplo = dao().consultar(preenchimento,
				ExPreenchimento.class, false);
		dao().excluir(exemplo);
		// preenchDao.excluirPorId(preenchimento);
		dao().commitTransacao();
		setPreenchimento(0L);
		return aEditar();
	}

	public String aTestarConexao() {
		return Action.SUCCESS;
	}

	public String aAcessar() throws Exception {
		assertAcesso("");
		buscarDocumento(false);

		assertAcesso();

		return Action.SUCCESS;
	}

	private void assertAcesso() throws Exception {			
		String msgDestinoDoc = "";
		DpPessoa dest;
		DpLotacao lotaDest;
		if(!Ex.getInstance().getComp()
				.podeAcessarDocumento(getTitular(), getLotaTitular(), mob)) {	
			if (!mob.doc().isArquivado() && !mob.isGeral()) {
				if (mob.doc().isFinalizado())
					lotaDest = mob.getUltimaMovimentacaoNaoCancelada().getLotaResp();
				else
					lotaDest = getLotaTitular();				
			
				if (getLotaTitular().equivale(lotaDest)  /* a pessoa que está tentando acessar está na mesma lotação onde se encontra o doc*/ 
						&& (mob.doc().getExNivelAcesso().getGrauNivelAcesso().intValue() == ExNivelAcesso.NIVEL_ACESSO_SUB_PESSOA					
							|| mob.doc().getExNivelAcesso().getGrauNivelAcesso().intValue() == ExNivelAcesso.NIVEL_ACESSO_PESSOAL)) {
				
				
					Boolean alguemPodeAcessar = false;
					Set<DpPessoa> pessoas = lotaDest.getDpPessoaLotadosSet(); 
					for (DpPessoa pes : pessoas) { /* verificar se alguem da lotação pode acessar o documento */
						if(Ex.getInstance().getComp()
								.podeAcessarDocumento(pes, lotaDest, mob))					
							if (pes.getPessoaAtual().ativaNaData(new Date()))  {
								alguemPodeAcessar = true;	
								break;							
							}					
					}			
					if (!alguemPodeAcessar) { /* ninguem pode acessar este documento */
						if (mob.doc().isFinalizado()) {						
							dest = mob.getUltimaMovimentacaoNaoCancelada().getResp().getPessoaAtual(); 
							Ex.getInstance()
								.getBL()
								.arquivarCorrenteAutomatico(dest, getLotaTitular(), mob);	
							msgDestinoDoc = "documento sendo arquivado automaticamente";			 
						
						} else {  /* doc temporário */
							dest = mob.doc().getCadastrante().getPessoaAtual();
							Ex.getInstance().getBL().excluirDocumentoAutomatico(mob.doc(), getCadastrante(),
									getLotaTitular());						
								msgDestinoDoc = "documento sendo excluído automaticamente";					
						}		
					
					}
					
				}
			}	
			
			
			String s = "";
			try { 
				s += mob.doc().getListaDeAcessosString();
				s = "(" + s + ")";
				s = " " + mob.doc().getExNivelAcesso().getNmNivelAcesso() + " " + s;
			} catch (Exception e) {
			}
			throw new AplicacaoException("Documento " + mob.getSigla()
					+ " inacessível ao usuário " + getTitular().getSigla()
					+ "/" + getLotaTitular().getSiglaCompleta() + "." + s + " " + msgDestinoDoc);
		}
	}

	public String aExibirAntigo() throws Exception {
		assertAcesso("");
		buscarDocumento(false);
		
		assertAcesso();

		if (Ex.getInstance().getComp()
				.podeReceberEletronico(getTitular(), getLotaTitular(), mob))
			Ex.getInstance()
					.getBL()
					.receber(getCadastrante(), getLotaTitular(), mob,
							new Date());

		ExDocumentoVO docVO = new ExDocumentoVO(doc, mob, getTitular(),
				getLotaTitular(), true,true);
		super.getRequest().setAttribute("docVO", docVO);

		// logStatistics();

		if (mob.isEliminado())
			throw new AplicacaoException("Documento "
					+ mob.getSigla()
					+ " eliminado, conforme o termo "
					+ mob.getUltimaMovimentacaoNaoCancelada(
							ExTipoMovimentacao.TIPO_MOVIMENTACAO_ELIMINACAO)
							.getExMobilRef());

		return Action.SUCCESS;
	}
	
	
	public String aExibir() throws Exception {
		assertAcesso("");
		buscarDocumento(false);

		assertAcesso();

		if (Ex.getInstance().getComp()
				.podeReceberEletronico(getTitular(), getLotaTitular(), mob))
			Ex.getInstance()
					.getBL()
					.receber(getCadastrante(), getLotaTitular(), mob,
							new Date());
		
		if(mob == null || mob.isGeral()) {
			if(mob.getDoc().isFinalizado()) {
				if(doc.isProcesso())
					mob = doc.getUltimoVolume();
				else
					mob = doc.getPrimeiraVia();
			}
		}

		ExDocumentoVO docVO = new ExDocumentoVO(doc, mob, getTitular(),
				getLotaTitular(), true,false);
		
		
		docVO.exibe();
		
		super.getRequest().setAttribute("docVO", docVO);

		// logStatistics();

		return Action.SUCCESS;
	}

	public String aCorrigirPDF() throws Exception {
		assertAcesso("");
		if (sigla != null) {
			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(sigla);
			mob = (ExMobil) dao().consultarPorSigla(filter);

			Ex.getInstance().getBL()
					.processar(mob.getExDocumento(), true, false, null);
		}
		return Action.SUCCESS;
	}

	private void logStatistics() {
		Statistics stats = ExDao.getInstance().getSessao().getSessionFactory()
				.getStatistics();
		SessionStatistics statsSession = ExDao.getInstance().getSessao()
				.getStatistics();

		double queryCacheHitCount = stats.getQueryCacheHitCount();
		double queryCacheMissCount = stats.getQueryCacheMissCount();
		double queryCacheHitRatio = queryCacheHitCount
				/ (queryCacheHitCount + queryCacheMissCount);

		System.out.println("Query Hit ratio:" + queryCacheHitRatio);

		System.out.println(stats.getQueryExecutionMaxTimeQueryString()
				+ " [time (ms): " + stats.getQueryExecutionMaxTime() + "]");

		System.out
				.println("\n\n\n\n\n\n*****************************************\n\n\n\n\n\n\n");

		for (String query : stats.getQueries()) {
			QueryStatistics qs = stats.getQueryStatistics(query);
			System.out.println(query + " [time (ms): "
					+ qs.getExecutionAvgTime() + ", count: "
					+ qs.getExecutionCount() + "]");
		}

		for (String ent : stats.getEntityNames()) {
			EntityStatistics es = stats.getEntityStatistics(ent);
			System.out.println(ent + " [count: " + es.getFetchCount() + "]");
		}

		System.out
				.println("\n\n\n\n\n\n*****************************************\n\n\n\n\n\n\n");

	}

	private void verificaDocumento() throws AplicacaoException, Exception {
		if ((doc.getSubscritor() == null && doc.getNmSubscritor() == null && doc
				.getNmSubscritorExt() == null)
				&& ((doc.getExFormaDocumento().getExTipoFormaDoc()
						.getIdTipoFormaDoc() == 2 && doc.isEletronico()) || doc
						.getExFormaDocumento().getExTipoFormaDoc()
						.getIdTipoFormaDoc() != 2))
			throw new AplicacaoException(
					"É necessário definir um subscritor para o documento.");

		if (doc.getDestinatario() == null
				&& doc.getLotaDestinatario() == null
				&& (doc.getNmDestinatario() == null || doc.getNmDestinatario()
						.trim().equals(""))
				&& doc.getOrgaoExternoDestinatario() == null
				&& (doc.getNmOrgaoExterno() == null || doc.getNmOrgaoExterno()
						.trim().equals(""))) {
			Long idSit = Ex
					.getInstance()
					.getConf()
					.buscaSituacao(doc.getExModelo(), getTitular(),
							getLotaTitular(),
							CpTipoConfiguracao.TIPO_CONFIG_DESTINATARIO)
					.getIdSitConfiguracao();
			if (idSit == ExSituacaoConfiguracao.SITUACAO_OBRIGATORIO)
				throw new AplicacaoException("Para documentos do modelo "
						+ doc.getExModelo().getNmMod()
						+ ", é necessário definir um destinatário");
		}

		if (doc.getExClassificacao() == null)
			throw new AplicacaoException(
					"É necessário informar a classificação documental.");

	}

	private void buscarDocumentoOuNovo(boolean fVerificarAcesso)
			throws Exception {
		buscarDocumento(fVerificarAcesso, true);
		if (doc == null) {
			doc = new ExDocumento();
			doc.setExTipoDocumento(dao().consultar(
					ExTipoDocumento.TIPO_DOCUMENTO_INTERNO,
					ExTipoDocumento.class, false));
			mob = new ExMobil();
			mob.setExTipoMobil(dao().consultar(ExTipoMobil.TIPO_MOBIL_GERAL,
					ExTipoMobil.class, false));
			mob.setNumSequencia(1);
			mob.setExDocumento(doc);

			doc.setExMobilSet(new TreeSet<ExMobil>());
			doc.getExMobilSet().add(mob);
		}
	}

	private void buscarDocumento(boolean fVerificarAcesso) throws Exception {
		buscarDocumento(fVerificarAcesso, false);
	}

	private void buscarDocumento(boolean fVerificarAcesso,
			boolean fPodeNaoExistir) throws Exception {
		assertAcesso("");
		if (mob == null && sigla != null && sigla.length() != 0) {
			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(sigla);
			mob = (ExMobil) dao().consultarPorSigla(filter);
			// bruno.lacerda@avantiprima.com.br
			if (mob != null) {
				doc = mob.getExDocumento();
				setIdMob(mob.getId());
			}
		} else if (mob == null && getDocumentoViaSel().getId() != null) {
			setIdMob(getDocumentoViaSel().getId());
			mob = dao().consultar(idMob, ExMobil.class, false);
		} else if (mob == null && idMob != null && idMob != 0) {
			mob = dao().consultar(idMob, ExMobil.class, false);
		}
		if (mob != null)
			doc = mob.doc();
		if (doc == null) {
			String id = param("id");
			if (id != null && id.length() != 0) {
				doc = daoDoc(Long.parseLong(id));
			}
		}
		if (doc != null && mob == null)
			mob = doc.getMobilGeral();

		if (!fPodeNaoExistir && doc == null)
			throw new AplicacaoException("Documento não informado");
		if (fVerificarAcesso && mob != null && mob.getIdMobil() != null)
			verificaNivelAcesso(mob);
	}

	public String aFinalizar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		verificaDocumento();

		if (!Ex.getInstance().getComp()
				.podeFinalizar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível Finalizar");

		try {

			setMsg(Ex.getInstance().getBL()
					.finalizar(getCadastrante(), getLotaTitular(), doc, null));

			if (doc.getForm() != null) {
				String funcao = doc.getForm().get("acaoFinalizar");
				if (funcao != null && funcao.trim().length() > 0) {
					obterMetodoPorString(funcao, doc);
				}
			}

		} catch (final Throwable t) {
			throw new AplicacaoException("Erro ao finalizar documento", 0, t);
		}

		return Action.SUCCESS;
	}

	public String aFinalizarAssinar() throws Exception {
		assertAcesso("");

		aFinalizar();

		buscarDocumento(true);

		return Action.SUCCESS;
	}

	public String aGravar() throws Exception {
		assertAcesso("");
		// if (isCancelled(request))
		// return aListar(mapping, actionForm, request, response);

		try {
			buscarDocumentoOuNovo(true);
			if (doc == null)
				doc = new ExDocumento();

			long tempoIni = System.currentTimeMillis();

			if (!validar()) {
		         aEditar();
		         getPar().put("alerta", new String[]{"Sim"});
		         setAlerta("Sim");
		         return "form_incompleto";
			}

			ByteArrayOutputStream baos = null;

			lerForm();

			if (!Ex.getInstance()
					.getConf()
					.podePorConfiguracao(getTitular(), getLotaTitular(),
							doc.getExTipoDocumento(),
							doc.getExFormaDocumento(), doc.getExModelo(),

							doc.getExClassificacaoAtual(),
							doc.getExNivelAcesso(),
							CpTipoConfiguracao.TIPO_CONFIG_CRIAR)) {

				if (!Ex.getInstance()
						.getConf()
						.podePorConfiguracao(getTitular(), getLotaTitular(),
								null, null, null, doc.getExClassificacao(),
								null, CpTipoConfiguracao.TIPO_CONFIG_CRIAR))
					throw new AplicacaoException(
							"Usuário não possui permissão de criar documento da classificação "
									+ doc.getExClassificacao().getCodificacao());

				throw new AplicacaoException("Operação não permitida");
			}

			System.out.println("monitorando gravacao IDDoc " + doc.getIdDoc()
					+ ", PESSOA " + doc.getCadastrante().getIdPessoa()
					+ ". Terminou verificacao de config PodeCriarModelo: "
					+ (System.currentTimeMillis() - tempoIni));
			tempoIni = System.currentTimeMillis();

			if (doc.getOrgaoUsuario() == null){
				doc.setOrgaoUsuario(getLotaTitular().getOrgaoUsuario());
			}

			if (isClassificacaoIntermediaria()
					&& (descrClassifNovo == null || descrClassifNovo.trim()
							.length() == 0))
				throw new AplicacaoException(
						"Quando a classificação selecionada não traz informação para criação de vias, o sistema exige que, antes de gravar o documento, seja informada uma sugestão de classificação para ser incluída na próxima revisão da tabela de classificações.");

			if (doc.getDescrDocumento().length() > getTamanhoMaximoDescricao())
				throw new AplicacaoException(
						"O campo descrição possui mais do que "
								+ getTamanhoMaximoDescricao() + " caracteres.");

			if (doc.isFinalizado()) {

				Date dt = dao().dt();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);

				Calendar dtDocCalendar = Calendar.getInstance();

				if (doc.getDtDoc() == null)
					throw new Exception(
							"A data do documento deve ser informada.");

				dtDocCalendar.setTime(doc.getDtDoc());

				if (c.before(dtDocCalendar))
					throw new Exception(
							"Não é permitido criar documento com data futura");

				verificaDocumento();
			}

			ExMobil mobilAutuado = null;
			if (getIdMobilAutuado() != null) {

				mobilAutuado = dao().consultar(getIdMobilAutuado(),
						ExMobil.class, false);

				doc.setExMobilAutuado(mobilAutuado);
			}

			Ex.getInstance().getBL()
					.gravar(getCadastrante(), getLotaTitular(), doc, null);

			lerEntrevista(doc);

			if (getDesativarDocPai().equals("sim"))
				setDesativ("&desativarDocPai=sim");

			try {

				Ex.getInstance()
						.getBL()
						.incluirCosignatariosAutomaticamente(getCadastrante(),
								getLotaTitular(), doc);

			} catch (Exception e) {

				throw new AplicacaoException(
						"Erro ao tentar incluir os cosignatários deste documento",
						0, e);

			}

		} catch (final Exception e) {
			throw new AplicacaoException("Erro na gravação", 0, e);
		} finally {
		}

		if (param("ajax") != null && param("ajax").equals("true"))
			return "ajax";
		else
			return Action.SUCCESS;
	}

	public String aGravarPreenchimento() throws Exception {
		assertAcesso("");
		dao().iniciarTransacao();
		ExPreenchimento exPreenchimento = new ExPreenchimento();

		DpLotacao provLota = new DpLotacao();
		provLota = getLotaTitular();
		ExModelo provMod = new ExModelo();
		provMod.setIdMod(getIdMod());

		exPreenchimento.setDpLotacao(provLota);
		exPreenchimento.setExModelo(provMod);
		exPreenchimento.setNomePreenchimento(getNomePreenchimento());

		exPreenchimento.setPreenchimentoBA(getByteArrayFormPreenchimento());
		dao().gravar(exPreenchimento);
		dao().commitTransacao();

		setPreenchimento(exPreenchimento.getIdPreenchimento());

		return aEditar();

	}

	public String aPrever() throws Exception {
		assertAcesso("");
		buscarDocumentoOuNovo(true);
		if (doc != null) {
			if (getPostback() == null) {
				escreverForm();
			} else {
				lerForm();
			}
		} else {
			doc = new ExDocumento();
			lerForm();
		}

		carregarBeans();

		if (param("idMod") != null) {
			setModelo(dao()
					.consultar(paramLong("idMod"), ExModelo.class, false));
		}

		if (param("processar_modelo") != null)
			return "processa_modelo";
		return Action.SUCCESS;
	}

	public String aPreverPdf() throws Exception {
		assertAcesso("");
		buscarDocumentoOuNovo(true);
		if (doc != null) {
			if (getPostback() == null) {
				escreverForm();
			} else {
				lerForm();
			}
		} else {
			doc = new ExDocumento();
			lerForm();
		}

		carregarBeans();

		if (param("idMod") != null) {
			setModelo(dao()
					.consultar(paramLong("idMod"), ExModelo.class, false));
		}

		Ex.getInstance().getBL().processar(doc, false, false, null);

		setPdfStreamResult(new ByteArrayInputStream(doc.getConteudoBlobPdf()));

		return Action.SUCCESS;
	}

	public String aRefazer() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance().getComp()
				.podeRefazer(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível refazer o documento");
		try {
			doc = Ex.getInstance().getBL()
					.refazer(getCadastrante(), getLotaTitular(), doc);
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}

	public String aAtualizarMarcasDoc() throws Exception {
		assertAcesso("");

		buscarDocumento(false);
		Ex.getInstance().getBL().atualizarMarcas(getDoc());

		return Action.SUCCESS;
	}

	public String aTestarPdf() throws Exception {
		return Action.SUCCESS;
	}

	// public String aTestarPdfGravar() throws Exception {
	//
	// setHtmlTeste("<html><body> <!-- INICIO PRIMEIRO CABECALHO   FIM PRIMEIRO CABECALHO --> "
	// + "<!-- INICIO CABECALHO   FIM CABECALHO --> "
	// + getHtmlTeste()
	// + "<!-- INICIO PRIMEIRO RODAPE   FIM PRIMEIRO RODAPE --> "
	// + "<!-- INICIO RODAPE   FIM RODAPE --> </body></html>");
	//
	// FOP fop = new FOP();
	//
	// setHtmlTeste((new ProcessadorHtml()).canonicalizarHtml(getHtmlTeste(),
	// true, false, true, false, true));
	//
	// HttpServletRequest req = com.opensymphony.webwork.ServletActionContext
	// .getRequest();
	// setHtmlTeste(getHtmlTeste().replace(
	// "contextpath",
	// "http://" + req.getServerName() + ":" + req.getServerPort()
	// + req.getContextPath()));
	//
	// setHtmlTesteConvertido(new ByteArrayInputStream(fop.converter(
	// getHtmlTeste(),
	// getHtmlTesteFormato().equals("pdf") ? ConversorHtml.PDF
	// : ConversorHtml.RTF)));
	// return Action.SUCCESS;
	// }

	public String aTesteEnvioDJE() throws Exception {
		try {
			ExMovimentacao fakeMov = ExDao.getInstance().consultar(39468L,
					ExMovimentacao.class, false);
			ExDocumento doque = fakeMov.getExDocumento();
			GeradorRTF gerador = new GeradorRTF();
			String nomeArq = doque.getIdDoc().toString();
			fakeMov.setConteudoBlobRTF(nomeArq, gerador.geraRTF(doque));
			fakeMov.setConteudoBlobXML(nomeArq, PublicacaoDJEBL
					.gerarXMLPublicacao(fakeMov, "A", "SESIA",
							"Teste descrição"));
			fakeMov.setNmArqMov(nomeArq + ".zip");

			PublicacaoDJEBL.primeiroEnvio(fakeMov);
			return Action.SUCCESS;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String aCorrigirArquivamentosVolume() throws Exception {
		assertAcesso("");
		int idPrimeiroDoc, idUltimoDoc;
		boolean efetivar;
		try {
			idPrimeiroDoc = Integer.valueOf(param("de"));
		} catch (Exception e) {
			idPrimeiroDoc = 1;
		}
		try {
			idUltimoDoc = Integer.valueOf(param("ate"));
		} catch (Exception e) {
			idUltimoDoc = 999999999;
		}
		try {
			efetivar = Boolean.parseBoolean(param("efetivar"));
		} catch (Exception e) {
			efetivar = false;
		}
		Ex.getInstance()
				.getBL()
				.corrigirArquivamentosEmVolume(idPrimeiroDoc, idUltimoDoc,
						efetivar);
		return Action.SUCCESS;
	}

	public String aTesteExclusaoDJE() throws Exception {
		ExMovimentacao fakeMov = ExDao.getInstance().consultar(37644L,
				ExMovimentacao.class, false);
		PublicacaoDJEBL.cancelarRemessaPublicacao(fakeMov);
		return null;
	}

	public String aMassaTesteDJE2() throws Exception {
		DpPessoa edson = new DpPessoa();
		edson.setSigla("RJ13285");
		edson = CpDao.getInstance().consultar(edson, null).get(0);

		ExDao exDao = ExDao.getInstance();
		StringBuffer appender = new StringBuffer(
				"from ExDocumento doc where (doc.exModelo.hisIdIni in (73, 76) and doc.exModelo.hisAtivo = 1) and doc.dtFechamento between :start and :end");
		Query query = exDao.getSessao().createQuery(appender.toString());
		Calendar cal = new GregorianCalendar();
		cal.set(2008, 07, 14);
		query.setDate("start", cal.getTime());
		cal.set(2008, 07, 18);
		query.setDate("end", cal.getTime());

		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, 2);

		List<ExDocumento> doques = query.list();
		for (ExDocumento ex : doques) { /*
										 * ExDocumentoBL.remeterParaPublicacao(edson
										 * , edson.getLotacao(), ex, null, new
										 * Date(), edson, edson,
										 * edson.getLotacao(), cal .getTime());
										 */
			/*
			 * ExMovimentacao fakeMov = new ExMovimentacao(); GeradorRTF gerador
			 * = new GeradorRTF(); String nomeArq = "I-" + doc.getIdDoc();
			 * fakeMov.setConteudoBlobRTF(nomeArq, gerador.geraRTF(doc));
			 * fakeMov.setConteudoBlobXML(nomeArq, PublicacaoDJEBL
			 * .gerarXMLPublicacao(fakeMov)); fakeMov.setNmArqMov(nomeArq +
			 * ".zip");
			 * 
			 * Long numTRF = PublicacaoDJEBL
			 * .verificaPrimeiroRetornoPublicacao(fakeMov); /* final Compactador
			 * zip = new Compactador(); final byte[] arqZip =
			 * getConteudoBlobMov2(); byte[] conteudoZip = null; conteudoZip =
			 * zip.adicionarStream(nome, conteudo, arqZip);
			 * setConteudoBlobMov2(conteudoZip);
			 */

		}
		return Action.SUCCESS;
	}

	public String aDuplicar() throws Exception {
		assertAcesso("");
		buscarDocumento(false);
		if (!Ex.getInstance().getComp()
				.podeDuplicar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível duplicar o documento");
		try {
			doc = Ex.getInstance().getBL()
					.duplicar(getCadastrante(), getLotaTitular(), doc);
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}

	private void atualizaLotacoesPreenchimentos(DpLotacao lotaTitular)
			throws AplicacaoException {
		if (lotaTitular.getIdLotacao().longValue() != lotaTitular
				.getIdLotacaoIni().longValue()) {
			ExPreenchimento exp = new ExPreenchimento();
			exp.setDpLotacao(daoLot(lotaTitular.getIdLotacaoIni()));
			for (ExPreenchimento exp2 : dao().consultar(exp)) {
				dao().iniciarTransacao();
				exp2.setDpLotacao(lotaTitular);
				dao().gravar(exp2);
				dao().commitTransacao();
			}
		}
	}

	public String aDesfazerCancelamentoDocumento() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		if (!Ex.getInstance()
				.getComp()
				.podeDesfazerCancelamentoDocumento(getTitular(),
						getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível desfazer o cancelamento deste documento");
		try {
			Ex.getInstance()
					.getBL()
					.DesfazerCancelamentoDocumento(getCadastrante(),
							getLotaTitular(), doc);
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}

	public String aTornarDocumentoSemEfeito() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		return Action.SUCCESS;
	}

	public String aTornarDocumentoSemEfeitoGravar() throws Exception {
		assertAcesso("");
		if (getDescrMov() == null || getDescrMov().trim().length() == 0) {
			throw new AplicacaoException(
					"O preenchimento do campo MOTIVO é obrigatório!");
		}
		buscarDocumento(true);
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
							getLotaTitular(), doc, getDescrMov());
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}
	
	public String acriarDocTest() throws Exception {
		try {
			setMensagem(Ex.getInstance()
					.getBL()
					.criarDocTeste());
		} catch (final Exception e) {
			throw e;
		}
		
		return Action.SUCCESS;
	}

	private void carregarBeans() throws Exception {
		ExMobil mobPai = null;

		doc.setExTipoDocumento(dao().consultar(idTpDoc, ExTipoDocumento.class,
				false));

		// Questões referentes a doc pai-----------------------------

		if (doc.getIdDoc() == null) {
			String req = "nao";
			if (getPar().get("reqdocumentoRefSel") != null)
				req = getPar().get("reqmobilPaiSel")[0].toString();

			if (param("mobilPaiSel.sigla") != null)
				mobilPaiSel.setSigla(param("mobilPaiSel.sigla"));
			mobilPaiSel.buscar();
			if ((mobilPaiSel.getId() != null) || (req.equals("sim"))) {

				if (mobilPaiSel.getId() != null) {
					// Documento Pai
					mobPai = daoMob(mobilPaiSel.getId());

					Integer idForma = paramInteger("idForma");
					if (idForma != null)
						setIdFormaDoc(idForma);

					if (getClassificacaoSel() != null
							&& getClassificacaoSel().getId() == null)
						getClassificacaoSel().setId(
								mobPai.doc().getExClassificacaoAtual().getId());

					setDescrDocumento(mobPai.doc().getDescrDocumento());

					setDesativarDocPai("sim");
				}

			}

			if (getAutuando() && getIdMobilAutuado() != null) {
				ExMobil mobilAutuado = daoMob(getIdMobilAutuado());

				doc.setExMobilAutuado(mobilAutuado);

				getClassificacaoSel().setId(
						mobilAutuado.getDoc().getExClassificacao().getId());
				setDescrDocumento(mobilAutuado.getDoc().getDescrDocumento());
			}
		}

		// Fim das questões referentes a doc pai--------------------

		Integer idFormaDoc = getIdFormaDoc();
		if (idFormaDoc != null) {
			if (idFormaDoc == 0) {
				setIdMod(0L);
			} else {

				// Mudou origem? Escolhe um tipo automaticamente--------
				// Vê se usuário alterou campo Origem. Caso sim, seleciona um
				// tipo
				// automaticamente, dentro daquela origem

				final List<ExFormaDocumento> formasDoc = getFormasDocPorTipo();

				ExFormaDocumento forma = dao().consultar(getIdFormaDoc(),
						ExFormaDocumento.class, false);

				if (!formasDoc.contains(forma)) {
					setIdFormaDoc(getFormaDocPorTipo().getIdFormaDoc());
					forma = dao().consultar(getIdFormaDoc(),
							ExFormaDocumento.class, false);
				}

				// Fim -- Mudou origem? Escolhe um tipo automaticamente--------

				if (forma.getExModeloSet().size() == 0) {
					setIdMod(0L);
				}
			}
		}

		ExModelo mod = null;
		if (getIdMod() != null && getIdMod() != 0) {
			mod = dao().consultar(getIdMod(), ExModelo.class, false);
		}
		if (mod != null) {
			mod = mod.getModeloAtual();
		}

		modelos = getModelos();
		if (mod == null || !modelos.contains(mod)) {
			mod = (ExModelo) (modelos.toArray())[0];

			for (ExModelo modeloAtual : modelos) {
				if (modeloAtual.getIdMod() != null
						&& modeloAtual.getIdMod() != 0
						&& modeloAtual.getNmMod().equals(
								modeloAtual.getExFormaDocumento()
										.getDescricao())) {
					mod = modeloAtual;
					break;
				}
			}

			setIdMod(mod.getIdMod());
			if ((getIdMod() != 0) && (mobilPaiSel.getId() == null)
					&& (getIdMobilAutuado() == null))
				getClassificacaoSel().apagar();
		}

		if (getPreenchimentos().size() <= 1) {
			setPreenchimento(0L);
		}

		if (isAlterouModelo() && mobilPaiSel.getId() == null
				&& idMobilAutuado == null)
			getClassificacaoSel().apagar();

		boolean naLista = false;
		final Set<ExPreenchimento> preenchimentos = getPreenchimentos();
		if (preenchimentos != null && preenchimentos.size() > 0) {
			for (ExPreenchimento exp : preenchimentos) {
				if (exp.getIdPreenchimento().equals(getPreenchimento())) {
					naLista = true;
					break;
				}
			}
			if (!naLista)
				setPreenchimento(((ExPreenchimento) (preenchimentos.toArray())[0])
						.getIdPreenchimento());
		}

		modelo = mod;
		if (mod.getExClassificacao() != null
				&& mod.getExClassificacao().getId() != getClassificacaoSel()
						.getId()) {
			getClassificacaoSel().buscarPorObjeto(mod.getExClassificacao());
		}

	}

	/*
	 * public Selecionavel selecionarPorCodigo(ExDocumentoViaDaoFiltro flt)
	 * throws CsisException {
	 * 
	 * Selecionavel sel=null;
	 * 
	 * ExMovimentacaoDao dao = getFabrica().createExMovimentacaoDao();
	 * 
	 * try{ sel = dao.consultarPorSiglaDoc(flt); } catch (SQLException se){
	 * throw new CsisException(se.getMessage()); }
	 * 
	 * if (sel != null) return sel;
	 * 
	 * return null; }
	 */

	private void escreverForm() throws IllegalAccessException,
			NoSuchMethodException, AplicacaoException,
			InvocationTargetException {

		// Destino , Origem
		DpLotacao backupLotaTitular = getLotaTitular();
		DpPessoa backupTitular = getTitular();
		DpPessoa backupCadastrante = getCadastrante();

		BeanUtils.copyProperties(this, doc);

		setTitular(backupTitular);
		setLotaTitular(backupLotaTitular);
		// Orlando: Inclusão da linha, abaixo, para preservar o cadastrante do
		// ambiente.
		setCadastrante(backupCadastrante);

		if (doc.getConteudoBlob("doc.htm") != null)
			setConteudo(new String(doc.getConteudoBlob("doc.htm")));

		setIdTpDoc(doc.getExTipoDocumento().getIdTpDoc());
		setNivelAcesso(doc.getIdExNivelAcesso());

		if (doc.getExFormaDocumento() != null) {
			setIdFormaDoc(doc.getExFormaDocumento().getIdFormaDoc());
		}

		ExClassificacao classif = doc.getExClassificacaoAtual();
		if (classif != null)
			getClassificacaoSel().buscarPorObjeto(classif.getAtual());
		getSubscritorSel().buscarPorObjeto(doc.getSubscritor());
		// form.getDestinatarioSel().buscarPorObjeto(doc.getDestinatario());
		if (doc.getExModelo() != null) {
			ExModelo modeloAtual = doc.getExModelo().getModeloAtual();
			if (modeloAtual != null) {
				setIdMod(modeloAtual.getIdMod());	
			}	
		}
			
		if (doc.getDestinatario() != null) {
			getDestinatarioSel().buscarPorObjeto(doc.getDestinatario());
			setTipoDestinatario(1);
		}
		if (doc.getLotaDestinatario() != null) {
			getLotacaoDestinatarioSel().buscarPorObjeto(
					doc.getLotaDestinatario());
			if (doc.getDestinatario() == null)
				setTipoDestinatario(2);
		}

		if (doc.getExMobilPai() != null) {
			getMobilPaiSel().buscarPorObjeto(doc.getExMobilPai());
		}

		if (doc.getTitular() != null
				&& doc.getSubscritor() != null
				&& !doc.getTitular().getIdPessoa()
						.equals(doc.getSubscritor().getIdPessoa())) {
			getTitularSel().buscarPorObjeto(doc.getTitular());
			setSubstituicao(true);
		}

		// TODO Verificar se ha realmente a necessidade de setar novamente o
		// nível de acesso do documento
		// tendo em vista que o nível de acesso já foi setado anteriormente
		// neste mesmo método sem que o documento fosse alterado
		setNivelAcesso(doc.getIdExNivelAcesso());

		if (doc.getOrgaoExternoDestinatario() != null) {
			getOrgaoExternoDestinatarioSel().buscarPorObjeto(
					doc.getOrgaoExternoDestinatario());
			setTipoDestinatario(3);
		}
		if (doc.getNmOrgaoExterno() != null
				&& !doc.getNmOrgaoExterno().equals("")) {
			setTipoDestinatario(3);
		}
		if (doc.getNmDestinatario() != null) {
			setNmDestinatario(doc.getNmDestinatario());
			setTipoDestinatario(4);
		}

		if (doc.getOrgaoExterno() != null) {
			getCpOrgaoSel().buscarPorObjeto(doc.getOrgaoExterno());
			setIdTpDoc(3L);
		}

		if (doc.getObsOrgao() != null) {
			setObsOrgao(doc.getObsOrgao());
		}

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			setDtDocString(df.format(doc.getDtDoc()));
		} catch (final Exception e) {
		}

		try {
			setDtDocOriginalString(df.format(doc.getDtDocOriginal()));
		} catch (final Exception e) {
		}

		if (doc.getAnoEmissao() != null)
			setAnoEmissaoString(doc.getAnoEmissao().toString());

		setEletronico(doc.isEletronico() ? 1 : 2);

	}

	public Boolean getAnexar() {
		if (anexarString == null)
			return false;
		return anexarString.equals("true");
	}

	public String getAnexarString() {
		return anexarString;
	}

	public String getAnoEmissaoString() {
		return anoEmissaoString;
	}

	public File getArquivo() {
		return arquivo;
	}

	/*
	 * como usamos <ww:file name="arquivo" .../> o content Type do arquivo será
	 * obtido através getter/setter de <file-tag-name>ContentType
	 */
	public String getArquivoContentType() {
		return arquivoContentType;
	}

	/*
	 * como usamos <ww:file name="arquivo" .../> o nome do arquivo será obtido
	 * através getter/setter de <file-tag-name>FileName
	 */
	public String getArquivoFileName() {
		return arquivoFileName;
	}

	private byte[] getByteArrayFormPreenchimento() throws Exception {
		ByteArrayOutputStream baos = null;
		String[] aVars = getPar().get("vars");
		String[] aCampos = getPar().get("campos");
		ArrayList<String> aFinal = new ArrayList<String>();
		if (aVars != null && aVars.length > 0)
			for (String str : aVars) {
				aFinal.add(str);
			}
		if (aCampos != null && aCampos.length > 0)
			for (String str : aCampos) {
				aFinal.add(str);
			}
		if (aFinal != null && aFinal.size() > 0) {
			baos = new ByteArrayOutputStream();
			for (final String s : aFinal) {
				if (param(s) != null && !param(s).trim().equals("")
						&& !s.trim().equals("preenchimento")
						&& !param(s).matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")/*
																		 * s.trim
																		 * (
																		 * ).equals
																		 * (
																		 * "dtDocString"
																		 * )
																		 */) {
					if (baos.size() > 0)
						baos.write('&');
					baos.write(s.getBytes());
					baos.write('=');

					// Deveria estar gravando como UTF-8
					baos.write(URLEncoder.encode(param(s), "iso-8859-1")
							.getBytes());
				}
			}
		}
		return baos.toByteArray();
	}

	public ExClassificacaoSelecao getClassificacaoSel() {
		return classificacaoSel;
	}

	public String getConteudo() {
		return conteudo;
	}

	public String getConteudoTpDoc() {
		return conteudoTpDoc;
	}

	public CpOrgaoSelecao getCpOrgaoSel() {
		return cpOrgaoSel;
	}

	public String getDescrClassifNovo() {
		return descrClassifNovo;
	}

	public String getDescrDocumento() {
		return descrDocumento;
	}

	public DpPessoaSelecao getDestinatarioSel() {
		return destinatarioSel;
	}

	public ExDocumento getDoc() {
		return doc;
	}

	public ExDocumentoSelecao getDocumentoSel() {
		return documentoSel;
	}

	public ExMobilSelecao getDocumentoViaSel() {
		return mobilSel;
	}

	public String getDtDocExtenso() {
		final SimpleDateFormat df1 = new SimpleDateFormat();
		Date minhaData = null;

		df1.applyPattern("dd/MM/yyyy");
		try {
			minhaData = df1.parse(getDtDocString());
		} catch (final ParseException e) {
			return "Ocorreu um erro na conversão da Data";
		}
		df1.applyPattern("'" + "Rio de Janeiro" + ",' dd 'de' MMMM 'de' yyyy.");
		return df1.format(minhaData);
	}

	public String getDtDocString() {
		return dtDocString;
	}

	public String getDtDocOriginalString() {
		return dtDocOriginalString;
	}

	public String getDtRegDoc() {
		return dtRegDoc;
	}

	/*
	 * public String getFgPessoal1() { return fgPessoal1; }
	 */

	/*
	 * public String getFgPessoalStr() { return fgPessoal ? "S" : null; }
	 */

	public List<ExFormaDocumento> getFormasDocumento() throws Exception {
		List<ExFormaDocumento> formasSet = dao().listarExFormasDocumento();
		ArrayList<ExFormaDocumento> formasFinal = new ArrayList<ExFormaDocumento>();
		for (ExFormaDocumento forma : formasSet) {
			if (Ex.getInstance()
					.getConf()
					.podePorConfiguracao(getTitular(), getLotaTitular(), forma,
							CpTipoConfiguracao.TIPO_CONFIG_CRIAR))
				formasFinal.add(forma);
		}
		return formasFinal;

	}

	public String getGravarpreench() {
		return gravarpreench;
	}

	public Long getId() {
		return id;
	}

	public String getIdDoc() {
		return idDoc;
	}

	public Integer getIdFormaDoc() {
		return idFormaDoc;
	}

	public Long getIdMod() {
		return idMod;
	}

	public Long getIdTpDoc() {
		return idTpDoc;
	}

	public List<String> getListaAnos() {
		final ArrayList<String> lst = new ArrayList<String>();
		// map.add("", "[Vazio]");
		final Calendar cal = Calendar.getInstance();
		for (Integer ano = cal.get(Calendar.YEAR); ano >= 1990; ano--)
			lst.add(ano.toString());
		return lst;
	}

	public Map<Integer, String> getListaTipoDest() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, "Matrícula");
		map.put(2, "Órgão Integrado");
		map.put(3, "Órgão Externo");
		map.put(4, "Campo Livre");
		return map;
	}

	public List<ExNivelAcesso> getListaNivelAcesso() throws Exception {
		ExFormaDocumento exForma = new ExFormaDocumento();
		ExClassificacao exClassif = new ExClassificacao();
		ExTipoDocumento exTipo = new ExTipoDocumento();
		ExModelo exMod = new ExModelo();

		if (getIdTpDoc() != null) {
			exTipo = dao()
					.consultar(getIdTpDoc(), ExTipoDocumento.class, false);
		}

		if (getIdFormaDoc() != null) {
			exForma = dao().consultar(getIdFormaDoc(), ExFormaDocumento.class,
					false);
		}

		if (getIdMod() != null && getIdMod() != 0) {
			exMod = dao().consultar(getIdMod(), ExModelo.class, false);
		}

		if (getClassificacaoSel().getId() != null) {
			exClassif = dao().consultar(getClassificacaoSel().getId(),
					ExClassificacao.class, false);
		}

		return getListaNivelAcesso(exTipo, exForma, exMod, exClassif);
	}

	public ExNivelAcesso getNivelAcessoDefault() throws Exception {
		ExFormaDocumento exForma = new ExFormaDocumento();
		ExClassificacao exClassif = new ExClassificacao();
		ExTipoDocumento exTipo = new ExTipoDocumento();
		ExModelo exMod = new ExModelo();

		if (getIdTpDoc() != null) {
			exTipo = dao()
					.consultar(getIdTpDoc(), ExTipoDocumento.class, false);
		}

		if (getIdFormaDoc() != null) {
			exForma = dao().consultar(getIdFormaDoc(), ExFormaDocumento.class,
					false);
		}

		if (getIdMod() != null) {
			exMod = dao().consultar(getIdMod(), ExModelo.class, false);
		}

		if (getClassificacaoSel().getId() != null) {
			exClassif = dao().consultar(getClassificacaoSel().getId(),
					ExClassificacao.class, false);
		}

		return getNivelAcessoDefault(exTipo, exForma, exMod, exClassif);
	}

	public Map<Integer, String> getListaVias() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		for (byte k = 1; k <= 20; k++) {
			final byte[] k2 = { (byte) (k + 64) };
			map.put(new Integer(k), new String(k2));
		}

		return map;
	}

	public DpLotacaoSelecao getLotacaoDestinatarioSel() {
		return lotacaoDestinatarioSel;
	}

	public ExModelo getModelo() {
		return modelo;
	}

	public String getNmArqDoc() {
		return nmArqDoc;
	}

	public String getNmDestinatario() {
		return nmDestinatario;
	}

	public String getNmFuncaoSubscritor() {
		if (nmFuncaoSubscritor != null
				&& nmFuncaoSubscritor.trim().length() == 0)
			return null;
		return nmFuncaoSubscritor;
	}

	public String getNmOrgaoExterno() {
		return nmOrgaoExterno;
	}

	public String getNmSubscritorExt() {
		return nmSubscritorExt;
	}

	public String getNomePreenchimento() {
		return nomePreenchimento;
	}

	public String getNumAntigoDoc() {
		return numAntigoDoc;
	}

	public String getNumExpediente() {
		return numExpediente;
	}

	public String getNumExtDoc() {
		return numExtDoc;
	}

	public String getObsOrgao() {
		return obsOrgao;
	}

	public CpOrgao getOrgaoExterno() {
		return orgaoExterno;
	}

	public CpOrgaoSelecao getOrgaoExternoDestinatarioSel() {
		return orgaoExternoDestinatarioSel;
	}

	public CpOrgaoSelecao getOrgaoSel() {
		return orgaoSel;
	}

	public SortedMap<String, String> getParamsEntrevista() {
		return paramsEntrevista;
	}

	public Long getPreenchimento() {
		return preenchimento;
	}

	public Set<ExPreenchimento> getPreenchimentos() throws AplicacaoException {
		if (preenchSet != null)
			return preenchSet;

		preenchSet = new LinkedHashSet<ExPreenchimento>();
		if (getIdFormaDoc() == null || getIdFormaDoc() == 0)
			return preenchSet;

		ExPreenchimento preench = new ExPreenchimento();
		if (getIdMod() != null && getIdMod() != 0L)
			preench.setExModelo(dao().consultar(getIdMod(), ExModelo.class,
					false));

		DpLotacao lota = new DpLotacao();
		lota.setIdLotacaoIni(getLotaTitular().getIdLotacaoIni());
		List<DpLotacao> lotacaoSet = dao().consultar(lota, null);

		preenchSet.add(new ExPreenchimento(0, null, " [Em branco] ", null));

		if (getIdMod() != null && getIdMod() != 0) {
			for (DpLotacao lotacao : lotacaoSet) {
				preench.setDpLotacao(lotacao);
				preenchSet.addAll(dao().consultar(preench));
			}
		}

		return preenchSet;
	}

	public String getPreenchRedirect() {
		return preenchRedirect;
	}

	public DpPessoaSelecao getSubscritorSel() {
		return subscritorSel;
	}

	public Integer getTipoDestinatario() {
		return tipoDestinatario;
	}

	public String getTipoDocumento() {
		if (getIdTpDoc() == null && doc != null
				&& doc.getExTipoDocumento() != null)
			setIdTpDoc(doc.getExTipoDocumento().getId());
		if (getIdTpDoc() == null || getIdTpDoc() == 1L)
			return "interno";
		else if (getIdTpDoc() == 2L)
			return "antigo";
		else if (getIdTpDoc() == 3L)
			return "externo";

		// if (param("idTpDoc") == null)
		// setParam("idTpDoc", "1");
		return "";
	}

	public DpPessoaSelecao getTitularSel() {
		return titularSel;
	}

	public boolean isClassificacaoIntermediaria() throws AplicacaoException {
		ExClassificacao xc = getClassificacaoSel().buscarObjeto();
		return (xc != null) && xc.isIntermediaria();

	}

	public Integer getEletronico() {
		return eletronico;
	}

	public String getEletronicoString() {
		return (getEletronico() == 1) ? "Documento Eletrônico"
				: "Documento Físico";
	}

	public boolean isSubstituicao() {
		return substituicao;
	}

	private void lerEntrevista(final ExDocumento doc) {
		if (doc.getExModelo() != null) {
			final byte[] form = doc.getConteudoBlob("doc.form");
			if (form != null) {
				final String as[] = new String(form).split("&");
				for (final String s : as) {
					final String param[] = s.split("=");
					try {
						if (param.length == 2)
							paramsEntrevista.put(param[0],
									URLDecoder.decode(param[1], "iso-8859-1"));
						// setParam(param[0], URLDecoder.decode(param[1],
						// "iso-8859-1"));
					} catch (final UnsupportedEncodingException e) {
					}
				}
			}
		}
	}

	private void lerForm() throws IllegalAccessException,
			NoSuchMethodException, AplicacaoException {
		if (getAnexar()) {
			doc.setConteudoTpDoc(getConteudoTpDoc());
			doc.setNmArqDoc(getNmArqDoc());
		}

		// BeanUtils.copyProperties(doc, form);
		// fabrica = DaoFactory.getDAOFactory();
		doc.setDescrDocumento(getDescrDocumento());
		doc.setNmSubscritorExt(getNmSubscritorExt());
		doc.setNmFuncaoSubscritor(getNmFuncaoSubscritor());
		doc.setNumExtDoc(getNumExtDoc());
		doc.setNumAntigoDoc(getNumAntigoDoc());
		doc.setObsOrgao(getObsOrgao());
		doc.setEletronico(getEletronico() == 1 ? true : false);
		doc.setNmOrgaoExterno(getNmOrgaoExterno());
		doc.setDescrClassifNovo(getDescrClassifNovo());

		doc.setExNivelAcesso(dao().consultar(getNivelAcesso(),
				ExNivelAcesso.class, false));

		doc.setExTipoDocumento(dao().consultar(getIdTpDoc(),
				ExTipoDocumento.class, false));

		if (!doc.isFinalizado())
			doc.setExFormaDocumento(dao().consultar(getIdFormaDoc(),
					ExFormaDocumento.class, false));
		doc.setNmDestinatario(getNmDestinatario());
		
		doc.setExModelo(null);
		if (getIdMod() != 0) {
			ExModelo modelo = dao().consultar(getIdMod(), ExModelo.class, false);
			if (modelo != null) 								
				doc.setExModelo(modelo.getModeloAtual());			
		}

		if (getClassificacaoSel().getId() != null
				&& getClassificacaoSel().getId() != 0) {

			ExClassificacao classificacao = dao()

			.consultar(getClassificacaoSel().getId(), ExClassificacao.class,
					false);

			if (classificacao != null && !classificacao.isFechada())
				doc.setExClassificacao(classificacao);
			else {
				doc.setExClassificacao(null);
				getClassificacaoSel().apagar();
			}

		} else
			doc.setExClassificacao(null);
		if (getCpOrgaoSel().getId() != null) {
			doc.setOrgaoExterno(dao().consultar(getCpOrgaoSel().getId(),
					CpOrgao.class, false));
		} else
			doc.setOrgaoExterno(null);

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

		if (doc.getLotaCadastrante() == null)
			doc.setLotaCadastrante(doc.getCadastrante().getLotacao());
		if (getSubscritorSel().getId() != null) {
			doc.setSubscritor(daoPes(getSubscritorSel().getId()));
			doc.setLotaSubscritor(doc.getSubscritor().getLotacao());
		} else {
			doc.setSubscritor(null);
		}

		if (substituicao) {
			if (getTitularSel().getId() != null) {
				doc.setTitular(daoPes(getTitularSel().getId()));
				doc.setLotaTitular(doc.getTitular().getLotacao());
			} else {
				doc.setTitular(doc.getSubscritor());
				doc.setLotaTitular(doc.getLotaSubscritor());
			}
		} else {
			doc.setTitular(doc.getSubscritor());
			doc.setLotaTitular(doc.getLotaSubscritor());
		}

		if (getDestinatarioSel().getId() != null) {
			doc.setDestinatario(daoPes(getDestinatarioSel().getId()));
			doc.setLotaDestinatario(daoPes(getDestinatarioSel().getId())
					.getLotacao());
			doc.setOrgaoExternoDestinatario(null);
		} else {
			doc.setDestinatario(null);
			if (getLotacaoDestinatarioSel().getId() != null) {
				doc.setLotaDestinatario(daoLot(getLotacaoDestinatarioSel()
						.getId()));
				doc.setOrgaoExternoDestinatario(null);
			} else {
				doc.setLotaDestinatario(null);
				if (getOrgaoExternoDestinatarioSel().getId() != null) {
					doc.setOrgaoExternoDestinatario(dao().consultar(
							getOrgaoExternoDestinatarioSel().getId(),
							CpOrgao.class, false));
				} else {
					doc.setOrgaoExternoDestinatario(null);
				}
			}
		}

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			doc.setDtDoc(df.parse(getDtDocString()));
		} catch (final ParseException e) {
			doc.setDtDoc(null);
		} catch (final NullPointerException e) {
			doc.setDtDoc(null);
		}
		if (doc.getDtRegDoc() == null)
			doc.setDtRegDoc(dao().dt());

		try {
			doc.setDtDocOriginal(df.parse(getDtDocOriginalString()));
		} catch (final ParseException e) {
			doc.setDtDocOriginal(null);
		} catch (final NullPointerException e) {
			doc.setDtDocOriginal(null);
		}

		if (getNumExpediente() != null) {
			doc.setNumExpediente(new Long(getNumExpediente()));
			doc.setAnoEmissao(new Long(getAnoEmissaoString()));
		}

		if (getMobilPaiSel().getId() != null) {
			doc.setExMobilPai(dao().consultar(getMobilPaiSel().getId(),
					ExMobil.class, false));
		} else {
			doc.setExMobilPai(null);
		}

		try {
			ByteArrayOutputStream baos;

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

			final String as[] = getPar().get("vars");
			if (as != null) {
				baos = new ByteArrayOutputStream();
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
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ExFormaDocumento getFormaDocPorTipo() throws Exception {
		return getFormasDocPorTipo().get(0);
	}

	public List<ExFormaDocumento> getFormasDocPorTipo() throws Exception {
		if (formasDoc == null) {
			formasDoc = new ArrayList<ExFormaDocumento>();
			ExBL bl = Ex.getInstance().getBL();
			formasDoc.addAll(bl

			.obterFormasDocumento(bl.obterListaModelos(null, getDespachando(),
					null, true, getTitular(), getLotaTitular(), getAutuando()),
					doc.getExTipoDocumento(), null));
		}

		return formasDoc;
	}

	public List<ExModelo> getModelos() throws Exception {
		if (modelos != null)
			return modelos;

		ExFormaDocumento forma = null;
		if (getIdFormaDoc() != null && getIdFormaDoc() != 0)
			forma = dao().consultar(this.getIdFormaDoc(),
					ExFormaDocumento.class, false);

		String headerValue = null;
		if (getTipoDocumento() != null && getTipoDocumento().equals("antigo"))
			headerValue = "Não Informado";

		modelos = Ex

				.getInstance()
				.getBL()
				.obterListaModelos(forma, getDespachando(), headerValue, true,
						getTitular(), getLotaTitular(), getAutuando());
		return modelos;

	}

	public void setAnexar(Boolean anexar) {
		this.anexarString = anexar ? "true" : "false";
	}

	public void setAnexarString(String anexarString) {
		this.anexarString = anexarString;
	}

	public void setAnoEmissaoString(final String anoEmissaoString) {
		this.anoEmissaoString = anoEmissaoString;
	}

	public void setArquivo(File arquivo) {
		this.arquivo = arquivo;
	}

	public void setArquivoContentType(String arquivoContentType) {
		this.arquivoContentType = arquivoContentType;
	}

	public void setArquivoFileName(String arquivoFileName) {
		this.arquivoFileName = arquivoFileName;
	}

	public void setClassificacaoSel(
			final ExClassificacaoSelecao classificacaoSel) {
		this.classificacaoSel = classificacaoSel;
	}

	public void setConteudo(final String conteudo) {
		this.conteudo = conteudo;
	}

	public void setConteudoTpDoc(final String conteudoTpDoc) {
		this.conteudoTpDoc = conteudoTpDoc;
	}

	public void setCpOrgaoSel(final CpOrgaoSelecao cpOrgaoSel) {
		this.cpOrgaoSel = cpOrgaoSel;
	}

	public void setDescrClassifNovo(String descrClassificao) {
		this.descrClassifNovo = descrClassificao;
	}

	public void setDescrDocumento(final String descrDocumento) {
		this.descrDocumento = descrDocumento;
	}

	public void setDestinatarioSel(final DpPessoaSelecao destinatarioSel) {
		this.destinatarioSel = destinatarioSel;
	}

	public void setDoc(final ExDocumento doc) {
		this.doc = doc;
	}

	public void setDocumentoSel(final ExDocumentoSelecao documentoSel) {
		this.documentoSel = documentoSel;
	}

	public void setDocumentoViaSel(final ExMobilSelecao documentoViaSel) {
		this.mobilSel = documentoViaSel;
	}

	public void setDtDocString(final String dtDocString) {
		this.dtDocString = dtDocString;
	}

	public void setDtDocOriginalString(String dtDocOriginalString) {
		this.dtDocOriginalString = dtDocOriginalString;
	}

	public void setDtRegDoc(final String dtRegDoc) {
		this.dtRegDoc = dtRegDoc;
	}

	public void setEletronico(Integer eletronico) {
		this.eletronico = eletronico;
	}

	public void setGravarpreench(String gravarpreench) {
		this.gravarpreench = gravarpreench;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setIdDoc(final String idDoc) {
		this.idDoc = idDoc;
	}

	public void setIdFormaDoc(final Integer idFormaDoc) {
		this.idFormaDoc = idFormaDoc;
	}

	public void setIdMod(final Long idMod) {
		this.idMod = idMod;
	}

	public void setIdTpDoc(final Long idTpDoc) {
		this.idTpDoc = idTpDoc;
	}

	public void setLotacaoDestinatarioSel(
			final DpLotacaoSelecao lotacaoDestinatarioSel) {
		this.lotacaoDestinatarioSel = lotacaoDestinatarioSel;
	}

	public void setModelo(final ExModelo modelo) {
		this.modelo = modelo;
	}

	public void setNmArqDoc(final String nmArqDoc) {
		this.nmArqDoc = nmArqDoc;
	}

	public void setNmDestinatario(final String nmDestinatario) {
		this.nmDestinatario = nmDestinatario;
	}

	public void setNmFuncaoSubscritor(String nmSubscritorFuncao) {
		this.nmFuncaoSubscritor = nmSubscritorFuncao;
	}

	public void setNmOrgaoExterno(String nmOrgaoExterno) {
		this.nmOrgaoExterno = nmOrgaoExterno;
	}

	public void setNmSubscritorExt(final String nmSubscritorExt) {
		this.nmSubscritorExt = nmSubscritorExt;
	}

	public void setNomePreenchimento(String nomePreenchimento) {
		this.nomePreenchimento = nomePreenchimento;
	}

	public void setNumAntigoDoc(String numAntigoDoc) {
		this.numAntigoDoc = numAntigoDoc;
	}

	public void setNumExpediente(final String numExpediente) {
		this.numExpediente = numExpediente;
	}

	public void setNumExtDoc(final String numExtDoc) {
		this.numExtDoc = numExtDoc;
	}

	public void setObsOrgao(final String obsOrgao) {
		this.obsOrgao = obsOrgao;
	}

	public void setOrgaoExterno(final CpOrgao orgaoExterno) {
		this.orgaoExterno = orgaoExterno;
	}

	public void setOrgaoExternoDestinatarioSel(
			final CpOrgaoSelecao orgaoExternoDestinatarioSel) {
		this.orgaoExternoDestinatarioSel = orgaoExternoDestinatarioSel;
	}

	public void setOrgaoSel(final CpOrgaoSelecao orgaoSel) {
		this.orgaoSel = orgaoSel;
	}

	public void setParamsEntrevista(
			final SortedMap<String, String> paramsEntrevista) {
		this.paramsEntrevista = paramsEntrevista;
	}

	public void setPreenchimento(Long preenchimento) {
		this.preenchimento = preenchimento;
	}

	public void setPreenchRedirect(String preenchRedirect) {
		this.preenchRedirect = preenchRedirect;
	}

	public void setSubscritorSel(final DpPessoaSelecao subscritorSel) {
		this.subscritorSel = subscritorSel;
	}

	public void setSubstituicao(boolean substituicao) {
		this.substituicao = substituicao;
	}

	public void setTipoDestinatario(final Integer tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
	}

	public void setTitularSel(DpPessoaSelecao subscritorTitularSel) {
		this.titularSel = subscritorTitularSel;
	}

	// Retorna o conteúdo do arquivo em um array de Byte
	public byte[] toByteArray(final File file) throws IOException {

		final InputStream is = new FileInputStream(file);

		// Get the size of the file
		final long tamanho = file.length();

		// Não podemos criar um array usando o tipo long.
		// é necessário usar o tipo int.
		if (tamanho > Integer.MAX_VALUE)
			throw new IOException("Arquivo muito grande");

		// Create the byte array to hold the data
		final byte[] meuByteArray = new byte[(int) tamanho];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < meuByteArray.length
				&& (numRead = is.read(meuByteArray, offset, meuByteArray.length
						- offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < meuByteArray.length)
			throw new IOException(
					"Não foi possível ler o arquivo completamente "
							+ file.getName());

		// Close the input stream and return bytes
		is.close();
		return meuByteArray;
	}

	public String getPreenchParamRedirect() {
		return preenchParamRedirect;
	}

	public void setPreenchParamRedirect(String preenchParamRedirect) {
		this.preenchParamRedirect = preenchParamRedirect;
	}

	public String getPodeExibir() {
		return podeExibir;
	}

	// Nato: isso, agora, eh a propriedade doc.exMobilSet
	// public ArrayList getListaNumVias() {
	// ArrayList al = new ArrayList();
	// if (via != null && via != 0) {
	// al.add(via);
	// al.add(0);
	// } else {
	// for (int k = 1; k <= getDoc().getNumUltimaVia(); k++)
	// al.add(k);
	// al.add(0);
	// }
	// return al;
	// }

	public ArrayList getListaNumViasAlternativo() {
		ArrayList al = new ArrayList();
		for (int k = 1; k <= getDoc().getNumUltimaVia(); k++)
			al.add(k);
		al.add(0);
		return al;
	}

	public void setPodeExibir(String podeExibir) {
		this.podeExibir = podeExibir;
	}

	public Integer getOrgaoUsu() {
		return orgaoUsu;
	}

	public void setOrgaoUsu(Integer orgaoUsu) {
		this.orgaoUsu = orgaoUsu;
	}

	public boolean isExibirCompleto() {
		return exibirCompleto;
	}

	public void setExibirCompleto(boolean exibirCompleto) {
		this.exibirCompleto = exibirCompleto;
	}

	public String getUserQuery() {
		return userQuery;
	}

	public void setUserQuery(String query) {
		this.userQuery = query;
	}

	public void setResults(List<ExDocumento> results) {
		this.results = results;
	}

	public List<ExDocumento> getResults() {
		return results;
	}

	public List<Serializable> getShowedResults() {
		return showedResults;
	}

	public void setShowedResults(List<Serializable> showedResults) {
		this.showedResults = showedResults;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isAlterouModelo() {
		return alterouModelo;
	}

	public void setAlterouModelo(boolean alterouModelo) {
		this.alterouModelo = alterouModelo;
	}

	public ExMobilSelecao getMobilPaiSel() {
		return mobilPaiSel;
	}

	public void setMobilPaiSel(ExMobilSelecao mobilPaiSel) {
		this.mobilPaiSel = mobilPaiSel;
	}

	public String getDesativarDocPai() {
		return desativarDocPai;
	}

	public void setDesativarDocPai(String desativarDocPai) {
		this.desativarDocPai = desativarDocPai;
	}

	public String getDesativ() {
		return desativ;
	}

	public void setDesativ(String desativ) {
		this.desativ = desativ;
	}

	public Long getIdMob() {
		return idMob;
	}

	public void setIdMob(Long idMob) {
		this.idMob = idMob;
	}

	public ExMobil getMob() {
		return mob;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getHtmlTeste() {
		return htmlTeste;
	}

	public void setHtmlTeste(String htmlTeste) {
		this.htmlTeste = htmlTeste;
	}

	public InputStream getHtmlTesteConvertido() {
		return htmlTesteConvertido;
	}

	public void setHtmlTesteConvertido(InputStream htmlTesteConvertido) {
		this.htmlTesteConvertido = htmlTesteConvertido;
	}

	public String getHtmlTesteFormato() {
		return htmlTesteFormato;
	}

	public void setHtmlTesteFormato(String htmlTesteFormato) {
		this.htmlTesteFormato = htmlTesteFormato;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public InputStream getPdfStreamResult() {
		return PdfStreamResult;
	}

	public void setPdfStreamResult(InputStream pdfStreamResult) {
		PdfStreamResult = pdfStreamResult;
	}

	public String getDescrMov() {
		return descrMov;
	}

	public void setDescrMov(String descrMov) {
		this.descrMov = descrMov;
	}

}
