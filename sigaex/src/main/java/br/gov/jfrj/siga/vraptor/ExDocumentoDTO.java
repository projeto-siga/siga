package br.gov.jfrj.siga.vraptor;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.TipoResponsavelEnum;
import br.gov.jfrj.siga.cp.model.CpOrgaoSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPreenchimento;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTpDocPublicacao;
import br.gov.jfrj.siga.ex.util.FuncoesEL;

public class ExDocumentoDTO {
	
	private static final long serialVersionUID = 2051335434134663817L;

	private String sigla;

	private String html;

	private String anexarString;

	private boolean exibirCompleto;

	private String anoEmissaoString;

	private File arquivo;

	private SortedSet<ExPreenchimento> preenchSet = null;

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

	private boolean personalizacao;

	private Integer tipoDestinatario;

	private Integer tipoEmitente;

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

	private boolean criandoAnexo;

	private List<ExFormaDocumento> formasDoc;

	private List<ExModelo> modelos;
	
	private Long idMobilAutuado;

	private boolean autuando;
	
	private boolean criandoSubprocesso;

	private String descrMov;
	
	private String dtPrazoAssinaturaString;
	
	private List<ExTipoDocumento> tiposDocumento;
	
	private List<ExNivelAcesso> listaNivelAcesso;

	public ExDocumentoDTO() {
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
	
	public boolean isCriandoAnexo() {
		return criandoAnexo;
	}

	public void setCriandoAnexo(boolean criandoAnexo) {
		this.criandoAnexo = criandoAnexo;
	}

	public Integer getTamanhoMaximoDescricao() {
		return 4000;
	}

	public boolean getAutuando() {
		return autuando;
	}
	
	public boolean getCriandoSubprocesso() {
		return criandoSubprocesso;
	}

	public void setAutuando(boolean autuando) {
		this.autuando = autuando;
	}

	public void setCriandoSubprocesso(boolean criandoSubprocesso) {
		this.criandoSubprocesso = criandoSubprocesso;
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

	 public List<ExDocumento> getDocsInclusosNoBoletim() {
         return FuncoesEL.consultarDocsInclusosNoBoletim(getDoc());
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
	 * como usamos <file name="arquivo" .../> o content Type do arquivo será
	 * obtido através getter/setter de <file-tag-name>ContentType
	 */
	public String getArquivoContentType() {
		return arquivoContentType;
	}

	/*
	 * como usamos <file name="arquivo" .../> o nome do arquivo será obtido
	 * através getter/setter de <file-tag-name>FileName
	 */
	public String getArquivoFileName() {
		return arquivoFileName;
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
		final SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy",new Locale("pt", "BR"));
		Date minhaData = null;

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

	public String getGravarpreench() {
		return gravarpreench;
	}

	public Long getId() {
		return id;
	}

	public String getIdDoc() {
		return idDoc;
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
		return TipoResponsavelEnum.getLista();
	}

	public Map<Integer, String> getListaTipoEmitente() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, SigaMessages.getMessage("responsavel.externo"));
		map.put(2, SigaMessages.getMessage("responsavel.campo.livre"));
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

	public String getPreenchRedirect() {
		return preenchRedirect;
	}

	public DpPessoaSelecao getSubscritorSel() {
		return subscritorSel;
	}

	public Integer getTipoDestinatario() {
		return tipoDestinatario;
	}

	public Integer getTipoEmitente() {
		return tipoEmitente;
	}

	public String getTipoDocumento() {
		if (getIdTpDoc() == null && doc != null
				&& doc.getExTipoDocumento() != null)
			setIdTpDoc(doc.getExTipoDocumento().getId());
		if (getIdTpDoc() == null || getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO)
			return "interno";
		else if (getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_FOLHA_DE_ROSTO)
			return "antigo";
		else if (getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_FOLHA_DE_ROSTO)
			return "externo";
		else if (getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_CAPTURADO)
			return "interno_capturado";
		else if (getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_CAPTURADO)
			return "externo_capturado";
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
	
	public boolean isPersonalizacao() {
		return personalizacao;
	}

	public void setPersonalizacao(boolean personalizacao) {
		this.personalizacao = personalizacao;
	}

	public ExFormaDocumento getFormaDocPorTipo() {
		if (getFormasDoc().size() == 0)
			throw new AplicacaoException("Não existe nenhuma espécie cadastrada para essa origem.");
		return getFormasDoc().get(0);
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

	public void setIdMod(final Long idMod) {
		this.idMod = idMod;
	}

	public void setIdTpDoc(final Long idTpDoc) {
		this.idTpDoc = idTpDoc;
	}

	public void setLotacaoDestinatarioSel(final DpLotacaoSelecao lotacaoDestinatarioSel) {
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

	public void setTipoEmitente(final Integer tipoEmitente) {
		this.tipoEmitente = tipoEmitente;
	}

	public void setTitularSel(DpPessoaSelecao subscritorTitularSel) {
		this.titularSel = subscritorTitularSel;
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
	
	public void setMob(ExMobil mob) {
		this.mob = mob;
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
    
    public List<ExModelo> getModelos() {
		return modelos;
	}
    
    public void setModelos(List<ExModelo> modelos) {
		this.modelos = modelos;
	}
    
    public List<ExFormaDocumento> getFormasDoc() {
		return formasDoc;
	}
    
    public void setFormasDoc(List<ExFormaDocumento> formasDoc) {
		this.formasDoc = formasDoc;
	}
    
    public void setPreenchSet(SortedSet<ExPreenchimento> preenchSet) {
		this.preenchSet = preenchSet;
	}
    
    public SortedSet<ExPreenchimento> getPreenchimentos() {
		return preenchSet;
	}
    
    
    public Set<ExPreenchimento> getPreenchSet() {
		return preenchSet;
	}
    
    public void setTiposDocumento(List<ExTipoDocumento> tiposDocumento) {
		this.tiposDocumento = tiposDocumento;
	}
    
    public List<ExTipoDocumento> getTiposDocumento() {
		return tiposDocumento;
	}
    
    public void setListaNivelAcesso(List<ExNivelAcesso> listaNivelAcesso) {
		this.listaNivelAcesso = listaNivelAcesso;
	}
    
    public List<ExNivelAcesso> getListaNivelAcesso() {
		return listaNivelAcesso;
	}

	public void setDtPrazoAssinaturaString(final String dtPrazoAssinaturaString) {
		this.dtPrazoAssinaturaString = dtPrazoAssinaturaString;
	}

	public String getDtPrazoAssinatura() {
		return dtPrazoAssinaturaString;
	}

}
