/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General public License as published by
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
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package br.gov.jfrj.webwork.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.xerces.impl.dv.util.Base64;
import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExEstadoDoc;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExItemDestinacao;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExTipoDespacho;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.ExTopicoDestinacao;
import br.gov.jfrj.siga.ex.SigaExProperties;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.util.DatasPublicacaoDJE;
import br.gov.jfrj.siga.ex.util.PublicacaoDJEBL;
import br.gov.jfrj.siga.ex.vo.ExMobilVO;
import br.gov.jfrj.siga.libs.webwork.CpOrgaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpLotacaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpPessoaSelecao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.persistencia.ExDocumentoDaoFiltro;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.opensymphony.xwork.Action;

public class ExMovimentacaoAction extends ExActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8223202120027622708L;

	private static final Logger log = Logger
			.getLogger(ExMovimentacaoAction.class);

	private ExMobil mob;

	private File arquivo;

	private String assinaturaB64;

	private String certificadoB64;

	private String atributoAssinavelDataHora;

	private Boolean copia;

	private Boolean podeAtenderPedidoPubl;

	private String contentType;

	private Integer tamanho;

	private String sigla;

	private String msg;

	/** The value of the simple conteudoBlobDoc property. */
	private String conteudo;

	/** The value of the simple conteudoTpDoc property. */
	private String conteudoTpDoc;

	private CpOrgaoSelecao cpOrgaoSel;

	/** The value of the simple descrMovimentacao property. */
	private String descrMov;

	private DpPessoaSelecao destinoFinalSel;

	private ExDocumento doc;

	private String idDocumentoPaiExterno;

	private String idDocumentoEscolha;

	private String descrFeriado;

	private ExMobilSelecao documentoRefSel;

	private ExClassificacaoSelecao classificacaoSel;

	private ExDocumentoSelecao documentoSel;

	private ExMobilSelecao documentoViaSel;

	private String dtDispon;

	private String dtPubl;

	/** The value of the simple dtDoc property. */
	private String dtMovString;

	private String dtDevolucaoMovString;

	/** The value of the simple dtRegDoc property. */
	private String dtRegMov;

	private String fileName;

	private Long id;

	/** The composite primary key value. */
	private Long idMob;

	private Long idResp;

	private Long idTpDespacho;

	private List itens;

	private List<ExDocumento> itensSolicitados;
	
	private List<ExDocumento> documentosQuePodemSerAssinadosComSenha;
	
	private List<ExMovimentacao> movimentacoesQuePodemSerAssinadasComSenha;

	private DpLotacaoSelecao lotaDestinoFinalSel;

	private boolean substituicao;

	private DpLotacaoSelecao lotaResponsavelSel;

	private DpLotacaoSelecao lotaSubscritorSel;

	private ExMovimentacao mov = new ExMovimentacao();

	private String dtPrevPubl;

	private Integer numViaDocBusca;

	private String obsOrgao;

	private CpOrgao orgaoExterno;

	private String nmFuncaoSubscritor;

	private Integer postback;

	private String responsavel;

	private String funcaoCosignatario;

	private DpPessoaSelecao responsavelSel;

	private DpPessoaSelecao titularSel;

	private DpPessoaSelecao cosignatarioSel;

	private DpPessoaSelecao subscritorSel;

	private Integer tipoDestinatario;

	private Integer tipoDestinoFinal;

	private Integer tipoResponsavel;

	private String tipoMateria;

	private boolean cadernoDJEObrigatorio;

	private Integer via;

	private Long nivelAcesso;

	private boolean finalizar;

	private String mensagem;

	private String enderecoAutenticacao;

	private Long idPapel;

	private String lotPublicacao;

	private Long idLotPublicacao;

	private String descrPublicacao;

	private Integer tamMaxDescr;

	private Long idLotDefault;
	
	private String nomeUsuarioSubscritor;

	private String senhaUsuarioSubscritor;
	
	private String tipoAssinaturaMov;
	
	private boolean autenticando;
	
	private String proximaDataDisponivelStr;

	public String getAtributoAssinavelDataHora() {
		return atributoAssinavelDataHora;
	}

	public void setAtributoAssinavelDataHora(String atributoAssinavelDataHora) {
		this.atributoAssinavelDataHora = atributoAssinavelDataHora;
	}

	public String getEnderecoAutenticacao() {
		return enderecoAutenticacao;
	}

	public void setEnderecoAutenticacao(String enderecoAutenticacao) {
		this.enderecoAutenticacao = enderecoAutenticacao;
	}

	public String getCertificadoB64() {
		return certificadoB64;
	}

	public void setCertificadoB64(String certificadoB64) {
		this.certificadoB64 = certificadoB64;
	}

	public Long getIdLotPublicacao() {
		return idLotPublicacao;
	}

	public void setIdLotPublicacao(Long idLotPublicacao) {
		this.idLotPublicacao = idLotPublicacao;
	}

	public Long getIdLotDefault() {
		return idLotDefault;
	}

	public void setIdLotDefault(Long idLotDefault) {
		this.idLotDefault = idLotDefault;
	}

	public String getDescrPublicacao() {
		return descrPublicacao;
	}

	public void setDescrPublicacao(String descrPublicacao) {
		this.descrPublicacao = descrPublicacao;
	}

	private String getLotPublicacao() {
		return lotPublicacao;
	}

	private void setLotPublicacao(String lotPublicacao) {
		this.lotPublicacao = lotPublicacao;
	}

	public Integer getTamanho() {
		return tamanho;
	}

	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	public Integer getTamMaxDescr() {
		return 255 - doc.getDescrDocumento().length();
	}

	private boolean assinandoAnexosGeral = false;

	public boolean isAssinandoAnexosGeral() {
		return assinandoAnexosGeral;
	}

	public Long getNivelAcesso() {
		return nivelAcesso;
	}

	public Boolean getPodeAtenderPedidoPubl() {
		return podeAtenderPedidoPubl;
	}

	public void setPodeAtenderPedidoPubl(Boolean podeAtenderPedidoPubl) {
		this.podeAtenderPedidoPubl = podeAtenderPedidoPubl;
	}

	public DpLotacaoSelecao getLotaSubscritorSel() {
		return lotaSubscritorSel;
	}

	public void setLotaSubscritorSel(DpLotacaoSelecao lotaSubscritorSel) {
		this.lotaSubscritorSel = lotaSubscritorSel;
	}

	public void setNivelAcesso(Long nivelAcesso) {
		this.nivelAcesso = nivelAcesso;
	}

	public ExMovimentacaoAction() {
		// arquivo = null;

		subscritorSel = new DpPessoaSelecao();
		documentoViaSel = new ExMobilSelecao();
		documentoRefSel = new ExMobilSelecao();
		classificacaoSel = new ExClassificacaoSelecao();
		cpOrgaoSel = new CpOrgaoSelecao();
		documentoSel = new ExDocumentoSelecao();
		responsavelSel = new DpPessoaSelecao();
		lotaResponsavelSel = new DpLotacaoSelecao();
		cosignatarioSel = new DpPessoaSelecao();

		tipoResponsavel = 1;

		destinoFinalSel = new DpPessoaSelecao();
		lotaDestinoFinalSel = new DpLotacaoSelecao();
		tipoDestinoFinal = 1;
		setSubstituicao(false);
		titularSel = new DpPessoaSelecao();
	}

	public String abCorrigirDataFimMov() throws Exception {
		assertAcesso("");
		return Action.SUCCESS;
	}

	private void buscarDocumentoOuNovo(boolean fVerificarAcesso)
			throws Exception {
		buscarDocumento(fVerificarAcesso, true);
	}

	private void buscarDocumento(boolean fVerificarAcesso) throws Exception {
		buscarDocumento(fVerificarAcesso, false);
	}

	private void buscarDocumento(boolean fVerificarAcesso,
			boolean fPodeNaoExistir) throws Exception {
		assertAcesso("");
		if (id != null) {
			mov = dao().consultar(id, ExMovimentacao.class, false);
			mob = getMov(mov);
		}

		if (mob == null && sigla != null) {
			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(sigla);
			mob = (ExMobil) dao().consultarPorSigla(filter);
			doc = mob.getExDocumento();
			setIdMob(mob.getId());
		} else if (mob == null && getDocumentoViaSel().getId() != null) {
			setIdMob(getDocumentoViaSel().getId());
			mob = dao().consultar(idMob, ExMobil.class, false);
		} else if (mob == null && idMob != null && idMob != 0) {
			mob = dao().consultar(idMob, ExMobil.class, false);
		}
		if (mob != null)
			doc = mob.doc();
		if (doc != null && mob == null)
			mob = doc.getMobilGeral();

		// } else if (paramLong("id") != null) {
		// id = Long.valueOf(paramLong("id"));
		// if (paramInteger("via") != null)
		// via = Integer.valueOf(paramInteger("via"));
		// }
		if (doc == null)
			throw new AplicacaoException("Documento não informado");
		if (fVerificarAcesso && mob != null)
			verificaNivelAcesso(mob);
	}

	private ExMobil getMov(ExMovimentacao movimentacao)
			throws AplicacaoException {
		ExMobil mobil = null;
		if (movimentacao != null) {
			try {
				mobil = movimentacao.getExMobil();
			} catch (Exception e) {
				log.warn("[getMov] - Não foi possível recuperar o mobil da movimentação");
				throw new AplicacaoException(
						"Ocorreu um erro ao recuperar o mobil da movimentação.",
						0, e);
			}
		}

		return mobil;
	}

	public String abCorrigirDataFimMovGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		// ExDocumento doque = new ExDocumento();
		// if (getDocumentoRefSel().getId() != null) {
		// final String provStr = getDocumentoRefSel().getId().toString();
		// doque = daoDoc(new Long(provStr.substring(0, provStr.length() - 3)));
		// }
		//
		// HashMap<Integer, ExMovimentacao> movPorVia = new HashMap<Integer,
		// ExMovimentacao>();

		// Inclui data de fim nas movimentações que não a possuem
		// for (ExMovimentacao move : mob.getExMovimentacaoSet()) {
		//
		// if (movPorVia.containsKey(move.getNumVia())) {
		// movPorVia.get(move.getNumVia()).setDtFimMov(move.getDtIniMov());
		// ExDocumentoBL.bCorrigirDataFimMov(movPorVia.get(move
		// .getNumVia()));
		// }
		// if (move.getDtFimMov() == null)
		// movPorVia.put(move.getNumVia(), move);
		// }

		// movPorVia.clear();
		//
		// boolean stop = false;
		// Nato: Nao precisamos apagar as movimentacoes de criacao duplicadas,
		// pq o perigo agora é a duplicacao de ExMobil.
		// Apaga movimentações de criação excedentes
		// while (!stop) {
		// stop = true;
		// for (ExMovimentacao move : doque.getExMovimentacaoSet()) {
		// if (movPorVia.containsKey(move.getNumVia())
		// && move.getExTipoMovimentacao().getIdTpMov() ==
		// ExTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO) {
		// if (movPorVia.get(move.getNumVia())
		// .getExMovimentacaoCanceladora() == null)
		// ExDocumentoBL.bCorrigirCriacaoDupla(movPorVia.get(move
		// .getNumVia()));
		// stop = false;
		// break;
		// } else if (move.getExTipoMovimentacao().getIdTpMov() ==
		// ExTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO)
		// movPorVia.put(move.getNumVia(), move);
		// }
		// }

		return Action.SUCCESS;
	}

	public String aPreverData() throws Exception {
		assertAcesso("");
		try {
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date provDtDispon = format.parse(param("data"));

			DatasPublicacaoDJE DJE = new DatasPublicacaoDJE(provDtDispon);

			String apenas = param("apenasSolicitacao");

			setDescrFeriado(DJE.validarDataDeDisponibilizacao((apenas != null)
					&& apenas.equals("true")));
			setDtPrevPubl(format.format(DJE.getDataPublicacao()));

			return Action.SUCCESS;
		} catch (Throwable t) {
			return null;
		}
	}

	public String aAtualizarPublicacao() throws Exception {
		String sData = param("data");
		Date data = null;
		if (sData != null) {
			data = new SimpleDateFormat("ddMMyyyy").parse(sData);
		}
		;

		String sTipoCaderno = param("tipoCaderno");
		String sSecao = param("secao");

		String sSoLerXml = "nao";
		if (param("soLerXml") != null)
			sSoLerXml = param("soLerXml");

		PublicacaoDJEBL.segundoRetorno(data, sTipoCaderno, sSecao, sSoLerXml);

		setMensagem(PublicacaoDJEBL.getXmlRetornado());

		return Action.SUCCESS;
	}

	public String aRegistrarDisponibilizacaoPublicacaoGravar() throws Exception {
		assertAcesso("");
		String sNomeDoc = param("documento");
		String sPagina = param("pagina");

		final ExMobilDaoFiltro daoViaFiltro = new ExMobilDaoFiltro();
		daoViaFiltro.setSigla(sNomeDoc);

		final ExMobil docVia;
		docVia = dao().consultarPorSigla(daoViaFiltro);

		Ex.getInstance().getBL()
				.registrarDisponibilizacaoPublicacao(docVia, null, sPagina);

		return Action.SUCCESS;
	}

	public String aPedirPublicacao() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (doc.getExNivelAcesso().getGrauNivelAcesso() != ExNivelAcesso.NIVEL_ACESSO_PUBLICO)

			throw new AplicacaoException(
					"A solicitação de publicação no DJE somente é permitida para documentos com nível de acesso Público.");

		if (!Ex.getInstance().getComp()
				.podePedirPublicacao(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Publicação não permitida");

		setProximaDataDisponivelStr(DatasPublicacaoDJE.consultarProximaDataDisponivelString());
		setTipoMateria(PublicacaoDJEBL.obterSugestaoTipoMateria(doc));
		setCadernoDJEObrigatorio(PublicacaoDJEBL
				.obterObrigatoriedadeTipoCaderno(doc));
		setDescrPublicacao(doc.getDescrDocumento());

		return Action.SUCCESS;
	}

	public String aPedirPublicacaoGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (this.getIdLotPublicacao() != null)
			this.setLotPublicacao(dao().consultar(this.getIdLotPublicacao(),
					DpLotacao.class, false).getSigla());

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
							mov.getDtDispPublicacao(), getTipoMateria(),
							getLotPublicacao(), getDescrPublicacao());
		} catch (final Exception e) {
			throw e;
		}
		// setDoc(mov.getExDocumento());

		return Action.SUCCESS;
	}

	public String aAgendarPublicacao() throws Exception {
		assertAcesso("");

		buscarDocumento(true);

		if (doc.getExNivelAcesso().getGrauNivelAcesso() != ExNivelAcesso.NIVEL_ACESSO_PUBLICO)

			throw new AplicacaoException(
					"O agendamento de publicação no DJE somente é permitido para documentos com nível de acesso Público.");

		if (!Ex.getInstance().getComp()
				.podeAgendarPublicacao(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não foi possível o agendamento de publicação no DJE.");

		setProximaDataDisponivelStr(DatasPublicacaoDJE.consultarProximaDataDisponivelString());
		setTipoMateria(PublicacaoDJEBL.obterSugestaoTipoMateria(doc));
		setCadernoDJEObrigatorio(PublicacaoDJEBL
				.obterObrigatoriedadeTipoCaderno(doc));

		if (getDescrPublicacao() == null)
			setDescrPublicacao(doc.getDescrDocumento());

		if (!Ex.getInstance()
				.getConf()
				.podePorConfiguracao(
						getTitular(),
						getLotaTitular(),
						CpTipoConfiguracao.TIPO_CONFIG_ATENDER_PEDIDO_PUBLICACAO)) {
			this.setPodeAtenderPedidoPubl(false);
		} else {
			DpLotacaoSelecao lot = new DpLotacaoSelecao();
			this.setPodeAtenderPedidoPubl(true);
			lot.setId(doc.getSubscritor().getLotacao().getId());
			lot.buscar();
			setLotaSubscritorSel(lot);

		}

		return Action.SUCCESS;
	}

	public String aAgendarPublicacaoGravar() throws Exception {
		assertAcesso("");
		Long idPubl = null;
		buscarDocumento(true);
		lerForm(mov);

		if (this.getIdLotPublicacao() != null)
			idPubl = this.getIdLotPublicacao();
		else {
			if (getLotaSubscritorSel().getId() != null)
				idPubl = getLotaSubscritorSel().getId();

		}

		this.setLotPublicacao(dao().consultar(idPubl, DpLotacao.class, false)
				.getSigla());

		if (!Ex.getInstance().getComp()
				.podeAgendarPublicacao(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não foi possível o agendamento de publicação no DJE.");

		if (getDescrPublicacao().length() > 256)
			throw new AplicacaoException(
					"O campo descrição possui mais do que 256 caracteres.");

		validarDataGravacao(mov, false);

		// if (getLotPublicacao() == null) {
		// if (getLotaSubscritorSel().getId() != null)
		// this.setLotPublicacao(dao().consultar(getLotaSubscritorSel().getId(),
		// DpLotacao.class, false).getSigla());

		// }

		Ex.getInstance()
				.getBL()
				.remeterParaPublicacao(getCadastrante(), getLotaTitular(), mob,
						dao().dt(), mov.getSubscritor(), mov.getTitular(),
						getLotaTitular(), mov.getDtDispPublicacao(),
						getTipoMateria().replaceAll("'", ""),
						getLotPublicacao(), getDescrPublicacao());
		return Action.SUCCESS;
	}

	private void validarDataGravacao(ExMovimentacao mov,
			boolean apenasSolicitacao) throws AplicacaoException {
		if (mov.getDtDispPublicacao() == null)
			throw new AplicacaoException(
					"A data desejada para a disponibilização precisa ser informada.");

		DatasPublicacaoDJE DJE = new DatasPublicacaoDJE(
				mov.getDtDispPublicacao());

		String mensagemValidacao = DJE
				.validarDataDeDisponibilizacao(apenasSolicitacao);

		if (mensagemValidacao != null)
			throw new AplicacaoException(mensagemValidacao);
	}

	public String aBoletimPublicarGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance().getComp()
				.podePublicar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Nao foi possivel fazer a publicacao");

		Ex.getInstance()
				.getBL()
				.publicarBoletim(getCadastrante(), getLotaTitular(),
						mov.getExDocumento(), mov.getDtMov());

		setDoc(mov.getExDocumento());

		return Action.SUCCESS;
	}

	public String aCancelarPedidoPublicacaoBoletim() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		ExMovimentacao movPedidoBI = mob
				.getUltimaMovimentacao(ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO_BOLETIM);

		if (movPedidoBI != null && !movPedidoBI.isCancelada()) {
			Ex.getInstance()
					.getBL()
					.cancelar(getTitular(), getLotaTitular(), getMob(),
							movPedidoBI, null, null, null,
							"Pedido cancelado pela unidade gestora do BI");

			// Verifica se está na base de teste
			String mensagemTeste = null;
			if (!SigaExProperties.isAmbienteProducao())
				mensagemTeste = SigaExProperties.getString("email.baseTeste");

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

			Correio.enviar(SigaBaseProperties
					.getString("servidor.smtp.usuario.remetente"),
					emailsSolicitantes.toArray(new String[emailsSolicitantes
							.size()]),
					"Cancelamento de pedido de publicação no DJE ("
							+ movPedidoBI.getLotaCadastrante()
									.getSiglaLotacao() + ") ", sb.toString(),
					sbHtml.toString());
		}

		return Action.SUCCESS;
	}

	public String aAtenderPedidoPublicacao() throws Exception {
		assertAcesso("");
		if (!Ex.getInstance()
				.getConf()
				.podePorConfiguracao(
						getTitular(),
						getLotaTitular(),
						CpTipoConfiguracao.TIPO_CONFIG_ATENDER_PEDIDO_PUBLICACAO))
			throw new AplicacaoException("Operação restrita");
		setItensSolicitados(dao().listarSolicitados(
				getTitular().getOrgaoUsuario()));

		return Action.SUCCESS;
	}

	public String aAtenderPedidoPublicacaoGravar() throws Exception {
		assertAcesso("");
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

		return Action.SUCCESS;
	}

	public String aAtenderPedidoPublicacaoCancelar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

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
					.cancelar(getTitular(), getLotaTitular(), getMob(),
							movPedidoDJE, null, null, null,
							"Pedido cancelado pela unidade gestora do DJE");

			// Verifica se está na base de teste
			String mensagemTeste = null;
			if (!SigaExProperties.isAmbienteProducao())
				mensagemTeste = SigaExProperties.getString("email.baseTeste");

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

			Correio.enviar(SigaBaseProperties
					.getString("servidor.smtp.usuario.remetente"),
					emailsSolicitantes.toArray(new String[emailsSolicitantes
							.size()]),
					"Cancelamento de pedido de publicação no DJE ("
							+ movPedidoDJE.getLotaCadastrante()
									.getSiglaLotacao() + ") ", sb.toString(),
					sbHtml.toString());
		}

		return Action.SUCCESS;
	}

	public String aDownloadZipPublicacao() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		for (ExMovimentacao move : doc.getMobilGeral().getExMovimentacaoSet()) {
			if (move.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO
					&& move.getExMovimentacaoCanceladora() == null)
				mov = move;
		}

		return Action.SUCCESS;
	}

	public String aAnexar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!(mob.isGeral() && mob.doc().isFinalizado()))
			if (!Ex.getInstance().getComp()
					.podeAnexarArquivo(getTitular(), getLotaTitular(), mob))
				throw new AplicacaoException("Arquivo não pode ser anexado");

		ExMobilVO mobilVO = new ExMobilVO(mob, getTitular(), getLotaTitular(),
				true, ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO, false);
		this.getRequest().setAttribute("mobilVO", mobilVO);

		ExMobilVO mobilCompletoVO = new ExMobilVO(mob, getTitular(),
				getLotaTitular(), true, null, false);
		this.getRequest().setAttribute("mobilCompletoVO", mobilCompletoVO);
		return Action.SUCCESS;
	}

	public String aAnexarGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		mov.setNmArqMov(getArquivoFileName());
		mov.setConteudoTpMov(getArquivoContentType());

		// bruno.lacerda@avantiprima.com.br
		if (this.arquivo == null) {
			throw new AplicacaoException(
					"O arquivo a ser anexado não foi selecionado!");
		}

		byte[] baArquivo = toByteArray(getArquivo());
		if (baArquivo == null)
			throw new AplicacaoException("Arquivo vazio não pode ser anexado.");
		if (baArquivo.length > 10 * 1024 * 1024)
			throw new AplicacaoException(
					"Não é permitida a anexação de arquivos com mais de 10MB.");
		mov.setConteudoBlobMov2(baArquivo);

		if (mov.getContarNumeroDePaginas() == null
				|| mov.getArquivoComStamp() == null)
			throw new AplicacaoException(
					"O arquivo "
							+ getArquivoFileName()
							+ " está corrompido. Favor gerá-lo novamente antes de anexar.");
		if (mob.isVolumeEncerrado()) {
			throw new AplicacaoException(
					"Não é possível anexar arquivo em volume encerrado.");
		}

		if (!Ex.getInstance().getComp()
				.podeAnexarArquivo(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Arquivo não pode ser anexado");
		if (!getArquivoContentType().equals("application/pdf"))
			throw new AplicacaoException(
					"Somente é permitido anexar arquivo PDF.");

		// Obtem as pendencias que serão resolvidas
		String aidMov[] = getRequest().getParameterValues(
				"pendencia_de_anexacao");
		Set<ExMovimentacao> pendencias = null;
		if (aidMov != null) {
			pendencias = new TreeSet<ExMovimentacao>();
			for (String s : aidMov) {
				pendencias.add(dao().consultar(Long.parseLong(s),
						ExMovimentacao.class, false));
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
					.anexarArquivo(getCadastrante(), getLotaTitular(), mob,
							mov.getDtMov(), mov.getSubscritor(), sNmArqMov,
							mov.getTitular(), mov.getLotaTitular(),
							mov.getConteudoBlobMov2(), mov.getConteudoTpMov(),
							getDescrMov(), pendencias);
		} catch (final Exception e) {
			throw e;
		}

		// request.setAttribute("doc", mov.getExDocumento());
		setDoc(mov.getExDocumento());

		return Action.SUCCESS;
	}

	public String aAssinarAnexosGeral() throws Exception {
		assertAcesso("");
		this.assinandoAnexosGeral = true;

		return aAnexar();

	}

	public String aMostrarAnexosAssinados() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		ExMobilVO mobilVO = new ExMobilVO(mob, getTitular(), getLotaTitular(),
				true, ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO, true);
		this.getRequest().setAttribute("mobilVO", mobilVO);
		return Action.SUCCESS;
	}

	public String aArquivarCorrenteGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance().getComp()
				.podeAcessarDocumento(getTitular(), getLotaTitular(), mob)) {
			throw new AplicacaoException(
					"Acesso permitido a usuários autorizados.");
		}

		if (!Ex.getInstance().getComp()
				.podeArquivarCorrente(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Via ou processo não pode ser arquivado(a)");
		try {
			Ex.getInstance()
					.getBL()
					.arquivarCorrente(getCadastrante(), getLotaTitular(), mob,
							mov.getDtMov(), null, mov.getSubscritor(), false);
		} catch (final Exception e) {
			throw e;
		}

		return Action.SUCCESS;
	}

	public String aArquivarIntermediario() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance().getComp()
				.podeArquivarIntermediario(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível fazer arquivamento intermediário. Verifique se o documento não se encontra em lotação diferente de "
							+ getLotaTitular().getSigla());

		if (doc.isEletronico())
			return "grava_direto";

		return Action.SUCCESS;
	}

	public String aArquivarIntermediarioGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance().getComp()
				.podeArquivarIntermediario(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível fazer arquivamento intermediário");
		try {
			Ex.getInstance()
					.getBL()
					.arquivarIntermediario(getCadastrante(), getLotaTitular(),
							mob, mov.getDtMov(), mov.getSubscritor(),
							mov.getDescrMov());
		} catch (final Exception e) {
			throw e;
		}

		return Action.SUCCESS;
	}

	public String aArquivarPermanenteGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance().getComp()
				.podeArquivarPermanente(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Documento não pode ser arquivado. Verifique se ele não se encontra em lotação diferente de "
							+ getLotaTitular().getSigla());
		try {
			Ex.getInstance()
					.getBL()
					.arquivarPermanente(getCadastrante(), getLotaTitular(),
							mob, mov.getDtMov(), mov.getSubscritor());
		} catch (final Exception e) {
			throw e;
		}

		return Action.SUCCESS;
	}

	public String aReabrirGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance().getComp()
				.podeDesarquivarCorrente(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Via não pode ser reaberta");
		try {
			Ex.getInstance()
					.getBL()
					.desarquivarCorrente(getCadastrante(), getLotaTitular(),
							mob, mov.getDtMov(), mov.getSubscritor());
		} catch (final Exception e) {
			throw e;
		}

		return Action.SUCCESS;
	}

	public String aDesarquivarIntermediarioGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance()
				.getComp()
				.podeDesarquivarIntermediario(getTitular(), getLotaTitular(),
						mob))
			throw new AplicacaoException(
					"Documento não pode ser retirado do arquivo intermediário. Verifique se ele não se encontra em lotação diferente de "
							+ getLotaTitular().getSigla());
		try {
			Ex.getInstance()
					.getBL()
					.desarquivarIntermediario(getCadastrante(),
							getLotaTitular(), mob, mov.getDtMov(),
							mov.getSubscritor());
		} catch (final Exception e) {
			throw e;
		}

		return Action.SUCCESS;
	}

	public String aDesobrestarGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance().getComp()
				.podeDesobrestar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Via não pode ser desobrestada");
		try {
			Ex.getInstance()
					.getBL()
					.desobrestar(getCadastrante(), getLotaTitular(), mob,
							mov.getDtMov(), mov.getSubscritor());
		} catch (final Exception e) {
			throw e;
		}

		return Action.SUCCESS;
	}

	public String aSobrestarGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance().getComp()
				.podeAcessarDocumento(getTitular(), getLotaTitular(), mob)) {
			throw new AplicacaoException(
					"Acesso permitido a usuários autorizados.");
		}

		if (!Ex.getInstance().getComp()
				.podeSobrestar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Via não pode ser sobrestada");
		try {
			Ex.getInstance()
					.getBL()
					.sobrestar(getCadastrante(), getLotaTitular(), mob,
							mov.getDtMov(), null, mov.getSubscritor());
		} catch (final Exception e) {
			throw e;
		}

		return Action.SUCCESS;
	}

	public String aAssinar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		boolean fPreviamenteAssinado = doc.isAssinado();

		if (!fPreviamenteAssinado
				&& (doc.getExModelo() != null && ("template/freemarker"
						.equals(doc.getExModelo().getConteudoTpBlob())))) {
			Ex.getInstance().getBL()
					.processarComandosEmTag(doc, "pre_assinatura");
		}

		return Action.SUCCESS;
	}
	
	public String aAutenticarDocumento() throws Exception {
		assertAcesso("");
		setAutenticando(true);
		return aAssinar();
	}

	public String aConferirCopia() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (getId() == null)
			throw new AplicacaoException("id não foi informada.");

		mov = dao().consultar(getId(), ExMovimentacao.class, false);

		return Action.SUCCESS;
	}

	public String aSimularAssinatura() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		Ex.getInstance()
				.getBL()
				.simularAssinaturaDocumento(getCadastrante(), getLotaTitular(),
						doc);
		return Action.SUCCESS;
	}

	public String aSimularAssinaturaMov() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		Ex.getInstance()
				.getBL()
				.simularAssinaturaMovimentacao(
						getCadastrante(),
						getLotaTitular(),
						mov,
						new Date(),
						ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO);
		return Action.SUCCESS;
	}

	public String aSimularAnexacao() throws Exception {
		assertAcesso("");
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);
		document.open();
		document.addTitle("PDF de teste");
		Paragraph preface = new Paragraph();
		preface.add(new Paragraph("Este é um documento de teste"));
		document.add(preface);
		document.close();

		buscarDocumento(true);
		lerForm(mov);

		mov.setNmArqMov("teste.pdf");
		mov.setConteudoTpMov("application/pdf");

		mov.setConteudoBlobMov2(baos.toByteArray());

		if (mob.isVolumeEncerrado()) {
			throw new AplicacaoException(
					"Não é possível anexar arquivo em volume encerrado.");
		}

		if (!Ex.getInstance().getComp()
				.podeAnexarArquivo(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Arquivo não pode ser anexado");

		// Obtem as pendencias que serão resolvidas
		String aidMov[] = getRequest().getParameterValues(
				"pendencia_de_anexacao");
		Set<ExMovimentacao> pendencias = null;
		if (aidMov != null) {
			pendencias = new TreeSet<ExMovimentacao>();
			for (String s : aidMov) {
				pendencias.add(dao().consultar(Long.parseLong(s),
						ExMovimentacao.class, false));
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
					.anexarArquivo(getCadastrante(), getLotaTitular(), mob,
							mov.getDtMov(), mov.getSubscritor(), sNmArqMov,
							mov.getTitular(), mov.getLotaTitular(),
							mov.getConteudoBlobMov2(), mov.getConteudoTpMov(),
							getDescrMov(), pendencias);
		} catch (final Exception e) {
			throw e;
		}

		// request.setAttribute("doc", mov.getExDocumento());
		setDoc(mov.getExDocumento());

		return Action.SUCCESS;
	}

	public String aAssinarGravar() throws Exception {
		assertAcesso("");
		boolean fApplet = getRequest().getParameter("QTYDATA") != null;
		String b64Applet = null;
		if (fApplet) {
			b64Applet = recuperarAssinaturaAppletB64();
		}
		buscarDocumento(true);
		lerForm(mov);
		if (b64Applet != null)
			setAssinaturaB64(b64Applet);

		byte[] assinatura = Base64.decode(getAssinaturaB64());
		Date dt = dao().consultarDataEHoraDoServidor();

		byte[] certificado = Base64.decode(getCertificadoB64());
		if (certificado != null && certificado.length != 0)
			dt = new Date(Long.valueOf(getAtributoAssinavelDataHora()));
		else
			certificado = null;

		// if ("true".equals(getRequest().getParameter("politica"))) {
		// CdService client = Service.getCdService();

		//
		// assinatura = client.validarECompletarAssinatura(assinatura,
		// doc.getConteudoBlobPdf(), true, mov.getDtMov());
		// }

		try {
			long tpMovAssinatura = ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO;
			
			if(getCopia() || (getTipoAssinaturaMov() != null && getTipoAssinaturaMov().equals("C")))
				tpMovAssinatura = ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO;
			
			setMsg(Ex
					.getInstance()
					.getBL()
					.assinarDocumento(getCadastrante(), getLotaTitular(), doc,
							dt, assinatura, certificado, tpMovAssinatura));
		} catch (final Exception e) {
			if (fApplet) {
				getRequest().setAttribute("err", e.getMessage());
				return "ERRO";
			}

			throw e;
		}

		if (fApplet) {
			return "OK";
		}

		return Action.SUCCESS;
	}
	
	public String aAssinarSenhaGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);
		
		try {
			setMsg(Ex
					.getInstance()
					.getBL()
					.assinarDocumentoComSenha(getCadastrante(), getLotaTitular(),
							doc, mov.getDtMov(), getNomeUsuarioSubscritor(), getSenhaUsuarioSubscritor(),
							mov.getTitular()));
		} catch (final Exception e) {

			throw e;
		}

		return Action.SUCCESS;
	}
	
	public String aAssinarMovSenhaGravar() throws Exception {
		assertAcesso("");
		long tpMovAssinatura = ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA;
		
		buscarDocumento(true);
		

		mov = dao().consultar(getId(), ExMovimentacao.class, false);
		
		if(getCopia() || (getTipoAssinaturaMov() != null && getTipoAssinaturaMov().equals("C")))
			tpMovAssinatura = ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA;
		
		try {
			Ex.getInstance()
					.getBL()
					.assinarMovimentacaoComSenha(getCadastrante(), getLotaTitular(), mov, mov.getDtMov(), 
							getNomeUsuarioSubscritor(), getSenhaUsuarioSubscritor(), tpMovAssinatura);
		} catch (final Exception e) {
			throw e;
		}

		return Action.SUCCESS;
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

	private String recuperarAssinaturaAppletB64() throws ServletException,
			AplicacaoException {
		HttpServletRequest request = getRequest();
		String mensagem = null;

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
			this.setId(Long.parseLong(partesArq[1]));
		} else
			this.setSigla(ARQUIVO);

		// Converte para binario
		Object tools = null;
		byte[] decoded = null;
		try {
			decoded = hexStringToByteArray(hexEncoded);
		} catch (Exception e) {
			throw new AplicacaoException(e.getMessage());
		}

		return Base64.encode(decoded);

		// Instanciar pacote de assinatura
		// PKCS7SignedMessage pacoteAssinatura = (PKCS7SignedMessage)
		// PKCS7Message
		// .getInstance(decoded);

		// // Se a assinatura for desatachada, atachar conteudo
		// if (pacoteAssinatura.isDetachedSignature()) {
		// // Recuperar conteudo
		// String path_conteudo = request.getServletContext().getRealPath(
		// "/documentos");
		//
		// FileInputStream fi = new FileInputStream(path_conteudo + "/"
		// + ARQUIVO);
		// byte[] conteudo = new byte[fi.available()];
		// fi.read(conteudo);
		// fi.close();
		//
		// // Atachar conteudo
		// ByteArrayByteSource conteudoBS = new ByteArrayByteSource(
		// conteudo);
		// pacoteAssinatura.setDetachedContent(conteudoBS);
		// pacoteAssinatura.setDetachedSignature(false);
		// }
		//
		// // Para cada assinante verifica a assinatura.
		// X509CertificateImpl signerCert = null;
		// int signersLen = pacoteAssinatura.getSignerInformations().size();
		// for (int i = 0; i < signersLen; i++) {
		// // Recupera informacoes do assinante
		// SignerInformation sigInfo = pacoteAssinatura
		// .getSignerInformation(i);
		//
		// // Busca o certificado do assinante através do serialNumber e
		// // issuer.
		// BigInteger serialNumber = sigInfo.getSerialNumber();
		// Principal issuer = sigInfo.getIssuer();
		//
		// // Carrega lista de certificados contidas no PKCS#7
		// signerCert = (X509CertificateImpl) pacoteAssinatura
		// .getCertificates().getCertificate(issuer, serialNumber);
		//
		// // Se o certificado não existir, lança erro.
		// if (signerCert == null) {
		// throw new Exception(
		// "O certificado do assinante não foi encontrado");
		// }
		//
		// // Verificar assinaura
		// pacoteAssinatura.verifySignature(sigInfo, signerCert
		// .getPublicKey());
		// }

		// Gravar pacote
		// String path = this.getServletContext().getRealPath("/assinaturas");
		// File file = new File(path, ARQUIVO + ".p7s");
		// OutputStream outFile = new FileOutputStream(file);
		// outFile.write(decoded);
		// outFile.close();
		//
		// mensagem = "OK";
	}

	/*
	 * public String aFinalizarAssinarGravar() throws Exception{ if (getId() ==
	 * null) throw new AplicacaoException("id não foi informada.");
	 * 
	 * doc = daoDoc(getId()); // doc = dao.consultarConteudoBlob(doc);
	 * 
	 * verificaNivelAcesso(doc, numVia);
	 * 
	 * if (!Ex.getInstance().getComp().podeFinalizarAssinar(getTitular(),
	 * getLotaTitular(), doc, numVia)) throw new
	 * AplicacaoException("Operação não permitida");
	 * 
	 * Ex.getInstance().getBL().finalizar(getTitular(), getLotaTitular(), doc);
	 * 
	 * return aAssinarGravar(); }
	 */

	public String aAssinarMov() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		mov = dao().consultar(getId(), ExMovimentacao.class, false);

		return Action.SUCCESS;
	}

	public String aAssinarMovGravar() throws Exception {
		assertAcesso("");
		boolean fApplet = getRequest().getParameter("QTYDATA") != null;
		String b64Applet = null;
		if (fApplet) {
			b64Applet = recuperarAssinaturaAppletB64();
		}
		buscarDocumento(true);
		if (b64Applet != null)
			setAssinaturaB64(b64Applet);

		long tpMovAssinatura = ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO;
		if (getCopia() != null && getCopia())
			tpMovAssinatura = ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO;

		byte[] assinatura = Base64.decode(getAssinaturaB64());
		Date dt = dao().consultarDataEHoraDoServidor();

		byte[] certificado = Base64.decode(getCertificadoB64());
		if (certificado != null && certificado.length != 0)
			dt = new Date(Long.valueOf(getAtributoAssinavelDataHora()));
		else
			certificado = null;

		// String sArquivoPolitica = getRequest().getRealPath("") +
		// File.separator
		// + "policies-ICP-BRASIL" + File.separator + "PA_AD_RB.cer";
		// assinatura = GravarAssinatura.validarECompletarAssinatura(assinatura,
		// mov.getConteudoBlobpdf(), sArquivoPolitica);

		verificaNivelAcesso(mov.getExMobil());

		try {
			Ex.getInstance()
					.getBL()
					.assinarMovimentacao(getCadastrante(), getLotaTitular(),
							mov, dt, assinatura, certificado, tpMovAssinatura);
		} catch (final Exception e) {
			if (fApplet) {
				getRequest().setAttribute("err", e.getMessage());
				return "ERRO";
			}

			throw e;
		}

		if (fApplet) {
			return "OK";
		}

		return Action.SUCCESS;
	}

	public String aFecharPopup() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		return Action.SUCCESS;
	}

	public String aRedefinirNivelAcesso() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		setNivelAcesso(doc.getExNivelAcesso().getIdNivelAcesso());

		return Action.SUCCESS;
	}

	public String aRedefinirNivelAcessoGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		ExNivelAcesso exTipoSig = null;

		if (getNivelAcesso() != null) {
			exTipoSig = dao().consultar(getNivelAcesso(), ExNivelAcesso.class,
					false);
		}

		if (!Ex.getInstance().getComp()
				.podeRedefinirNivelAcesso(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível redefinir o nível de acesso");

		try {
			Ex.getInstance()
					.getBL()
					.redefinirNivelAcesso(getCadastrante(), getLotaTitular(),
							doc, mov.getDtMov(), mov.getLotaResp(),
							mov.getResp(), mov.getSubscritor(),
							mov.getTitular(), mov.getNmFuncaoSubscritor(),
							exTipoSig);
		} catch (final Exception e) {
			throw e;
		}

		setDoc(mov.getExDocumento());
		return Action.SUCCESS;
	}

	public String aAssinarVerificar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		try {
			final String s = Ex
					.getInstance()
					.getBL()
					.verificarAssinatura(doc.getConteudoBlobPdf(),
							mov.getConteudoBlobMov2(), mov.getConteudoTpMov(),
							mov.getDtIniMov());
			getRequest().setAttribute("assinante", s);
			return Action.SUCCESS;
		} catch (final Exception e) {
			getRequest().setAttribute("err", e.getMessage());
			return "verificar_assinatura_erro";
		}
	}

	public String aAssinarMovVerificar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		final ExMovimentacao mov = dao().consultar(getId(),
				ExMovimentacao.class, false);
		final ExMovimentacao movRef = mov.getExMovimentacaoRef();

		try {
			final String s = Ex
					.getInstance()
					.getBL()
					.verificarAssinatura(movRef.getConteudoBlobpdf(),
							mov.getConteudoBlobMov2(), mov.getConteudoTpMov(),
							mov.getDtIniMov());
			getRequest().setAttribute("assinante", s);
			return Action.SUCCESS;
		} catch (final Exception e) {
			getRequest().setAttribute("err", e.getMessage());
			return "verificar_assinatura_erro";
		}
	}

	public String aCancelarJuntada() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance().getComp()
				.podeCancelarJuntada(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível cancelar juntada");

		ExMobil mobilJuntado = mob.getExMobilPai();
		if (mobilJuntado != null && !mobilJuntado.getDoc().isEletronico()) {
			aCancelarJuntadaGravar();
			return Action.SUCCESS;
		}

		return "cancelamento_juntada";
	}

	public String aCancelarJuntadaGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance().getComp()
				.podeCancelarJuntada(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível cancelar juntada");

		try {
			Ex.getInstance()
					.getBL()
					.cancelarJuntada(getCadastrante(), getLotaTitular(), mob,
							mov.getDtMov(), mov.getSubscritor(),
							mov.getTitular(), getDescrMov());
		} catch (final Exception e) {
			throw e;
		}

		setDoc(mov.getExDocumento());
		return Action.SUCCESS;
	}

	public String aCancelarUltimaMovimentacao() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		final ExMovimentacao exUltMovNaoCanc = mob
				.getUltimaMovimentacaoNaoCancelada();
		final ExMovimentacao exUltMov = mob.getUltimaMovimentacao();

		if (exUltMovNaoCanc.getExTipoMovimentacao().getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO
				&& exUltMovNaoCanc.getIdMov() == exUltMov.getIdMov()) {
			if (!Ex.getInstance().getComp()
					.podeCancelarVia(getTitular(), getLotaTitular(), mob))
				throw new AplicacaoException("Não é possível cancelar via");
		} else {
			if (!Ex.getInstance()
					.getComp()
					.podeCancelarMovimentacao(getTitular(), getLotaTitular(),
							mob))
				throw new AplicacaoException(
						"Não é possível cancelar movimentação");
		}

		try {
			Ex.getInstance()
					.getBL()
					.cancelarMovimentacao(getCadastrante(), getLotaTitular(),
							mob);
		} catch (final Exception e) {
			throw e;
		}

		return Action.SUCCESS;
	}

	public String aCancelar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (mov.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO) {
			if (!Ex.getInstance()
					.getComp()
					.podeCancelarAnexo(getTitular(), getLotaTitular(), mob, mov))
				throw new AplicacaoException("Não é possível cancelar anexo");

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

		// if
		// (!Ex.getInstance().getComp().podeCancelarMovimentacao(getTitular(),
		// getLotaTitular(), mob))
		// throw new AplicacaoException("Não é possível cancelar movimentacao");

		// if (doc.isEletronico()) {
		// aCancelarMovimentacaoGravar();
		// return Action.SUCCESS;
		// }

		return Action.SUCCESS;
	}

	public String aCancelarMovimentacaoGravar() throws Exception {
		assertAcesso("");
		ExMovimentacao movForm = new ExMovimentacao();
		buscarDocumento(true);
		lerForm(movForm);

		if (mov.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO) {
			if (!Ex.getInstance()
					.getComp()
					.podeCancelarAnexo(getTitular(), getLotaTitular(), mob, mov))
				throw new AplicacaoException("Não é possível cancelar anexo");
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
					.podeCancelarVinculacaoDocumento(mov.getCadastrante(),
							mov.getLotaCadastrante(), mob, mov))
				throw new AplicacaoException(
						"Não é possível cancelar o documento vinculado.");

		} else {
			if (!Ex.getInstance().getComp()
					.podeCancelar(getTitular(), getLotaTitular(), mob, mov))
				throw new AplicacaoException(
						"Não é permitido cancelar esta movimentação.");
		}

		try {
			Ex.getInstance()
					.getBL()
					.cancelar(getCadastrante(), getLotaTitular(), mob, mov,
							movForm.getDtMov(), movForm.getSubscritor(),
							movForm.getTitular(), getDescrMov());
		} catch (final Exception e) {
			throw e;
		}

		setDoc(mov.getExDocumento());
		return Action.SUCCESS;
	}

	public String aExcluir() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		try {
			Ex.getInstance()
					.getBL()
					.excluirMovimentacao(getCadastrante(), getLotaTitular(),
							mob, getId());
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}

	public String aExibir() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (getId() == null)
			throw new AplicacaoException("id não foi informada.");

		mov = dao().consultar(getId(), ExMovimentacao.class, false);

		setEnderecoAutenticacao(SigaExProperties.getEnderecoAutenticidadeDocs());

		return Action.SUCCESS;
	}

	public String aAutenticarMovimentacao() throws Exception {
		assertAcesso("");
		setAutenticando(true);
		return aExibir();
	}

	public String aGerarProtocolo() throws Exception {
		assertAcesso("");
		// buscarDocumento(true);
		lerForm(mov);

		final Pattern p = Pattern.compile("chk_([0-9]+)");

		final ArrayList al = new ArrayList();

		try {
			for (final String s : getPar().keySet()) {
				if (s.startsWith("chk_") && param(s).equals("true")) {
					final Matcher m = p.matcher(s);
					if (!m.find())
						throw new AplicacaoException(
								"Não foi possível ler a Id do documento e o número da via.");
					final ExMobil mob = dao().consultar(
							Long.valueOf(m.group(1)), ExMobil.class, false);
					final Object[] ao = { mob.doc(),
							mob.getUltimaMovimentacaoNaoCancelada() };
					al.add(ao);
				}
			}
		} catch (final Exception e) {
			throw e;
		}

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
				else if (doc1.getExFormaDocumento().getIdFormaDoc() > doc2
						.getExFormaDocumento().getIdFormaDoc())
					return 1;
				else if (doc1.getExFormaDocumento().getIdFormaDoc() < doc2
						.getExFormaDocumento().getIdFormaDoc())
					return -1;
				else if (doc1.getNumExpediente() > doc2.getNumExpediente())
					return 1;
				else if (doc1.getNumExpediente() < doc2.getNumExpediente())
					return -1;
				else if (mov1.getExMobil().getExTipoMobil().getIdTipoMobil() > mov2
						.getExMobil().getExTipoMobil().getIdTipoMobil())
					return 1;
				else if (mov1.getExMobil().getExTipoMobil().getIdTipoMobil() < mov2
						.getExMobil().getExTipoMobil().getIdTipoMobil())
					return -1;
				else if (mov1.getExMobil().getNumSequencia() > mov2
						.getExMobil().getNumSequencia())
					return 1;
				else if (mov1.getExMobil().getNumSequencia() < mov2
						.getExMobil().getNumSequencia())
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

		return Action.SUCCESS;
	}

	public String aGerarProtocoloUnitario() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		mov = dao().consultar(getId(), ExMovimentacao.class, false);

		ArrayList lista = new ArrayList();
		final Object[] ao = { doc, mov };
		lista.add(ao);
		setItens(lista);
		return Action.SUCCESS;
	}

	public String aGerarProtocoloArq() throws Exception {
		assertAcesso("");
		// buscarDocumento(true);
		mov = null;

		final DpPessoa pes;
		final ArrayList al = new ArrayList();
		final DpPessoa oExemplo = new DpPessoa();

		String siglaPessoa = param("pessoa");

		if (siglaPessoa == null || siglaPessoa.trim() == "") {
			log.warn("[aGerarProtocoloArq] - A sigla informada é nula ou inválida");
			throw new AplicacaoException(
					"A sigla informada é nula ou inválida.");
		}

		oExemplo.setSigla(siglaPessoa);
		pes = CpDao.getInstance().consultarPorSigla(oExemplo);

		if (pes == null) {
			log.warn("[aGerarProtocoloArq] - Não foi possível localizar DpPessoa com a sigla "
					+ oExemplo.getSigla());
			throw new AplicacaoException(
					"Não foi localizada pessoa com a sigla informada.");
		}

		Date dt = paramDate("dt");
		final List<ExMovimentacao> movs = dao().consultarMovimentacoes(pes, dt);
		for (ExMovimentacao m : movs) {
			if (mov == null)
				mov = m;
			final Object[] ao = { m.getExMobil().doc(),
					m.getExMobil().getUltimaMovimentacaoNaoCancelada() };
			al.add(ao);
		}

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
				else if (doc1.getExFormaDocumento().getIdFormaDoc() > doc2
						.getExFormaDocumento().getIdFormaDoc())
					return 1;
				else if (doc1.getExFormaDocumento().getIdFormaDoc() < doc2
						.getExFormaDocumento().getIdFormaDoc())
					return -1;
				else if (doc1.getNumExpediente() > doc2.getNumExpediente())
					return 1;
				else if (doc1.getNumExpediente() < doc2.getNumExpediente())
					return -1;
				else if (mov1.getExMobil().getExTipoMobil().getIdTipoMobil() > mov2
						.getExMobil().getExTipoMobil().getIdTipoMobil())
					return 1;
				else if (mov1.getExMobil().getExTipoMobil().getIdTipoMobil() < mov2
						.getExMobil().getExTipoMobil().getIdTipoMobil())
					return -1;
				else if (mov1.getExMobil().getNumSequencia() > mov2
						.getExMobil().getNumSequencia())
					return 1;
				else if (mov1.getExMobil().getNumSequencia() < mov2
						.getExMobil().getNumSequencia())
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

		return Action.SUCCESS;
	}

	// public String getViaChar() {
	// return "" + (Character.toChars(getNumVia().intValue() + 64))[0];
	// }

	public String aJuntar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance().getComp()
				.podeJuntar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível fazer juntada");

		return Action.SUCCESS;
	}

	public String aJuntarGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance().getComp()
				.podeJuntar(getTitular(), getLotaTitular(), mob))
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
					.juntarDocumento(getCadastrante(), getTitular(),
							getLotaTitular(), getIdDocumentoPaiExterno(), mob,
							mov.getExMobilRef(), mov.getDtMov(),
							mov.getSubscritor(), mov.getTitular(),
							getIdDocumentoEscolha());
		} catch (final Exception e) {
			throw e;
		}

		setDoc(mov.getExDocumento());
		return Action.SUCCESS;
	}

	public String aApensar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance().getComp()
				.podeApensar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível apensar");

		return Action.SUCCESS;
	}

	public String aApensarGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance().getComp()
				.podeApensar(getTitular(), getLotaTitular(), mob))
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
					.apensarDocumento(getCadastrante(), getTitular(),
							getLotaTitular(), mob, mov.getExMobilRef(),
							mov.getDtMov(), mov.getSubscritor(),
							mov.getTitular());
		} catch (final Exception e) {
			throw e;
		}

		setDoc(mov.getExDocumento());
		return Action.SUCCESS;
	}

	public String aDesapensar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance().getComp()
				.podeDesapensar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível desapensar");

		if (doc.isEletronico()) {
			aDesapensarGravar();
			return Action.SUCCESS;
		}

		return "desapensamento";
	}

	public String aDesapensarGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

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
		setDoc(mov.getExDocumento());
		return Action.SUCCESS;
	}

	public String aRegistrarAssinatura() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (doc.getSubscritor() != null) {
			DpPessoaSelecao sub = new DpPessoaSelecao();
			sub.setId(doc.getSubscritor().getId());
			sub.buscar();
			setSubscritorSel(sub);
		}

		if (!Ex.getInstance().getComp()
				.podeRegistrarAssinatura(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível registrar a assinatura");

		setSubstituicao(false);

		return Action.SUCCESS;
	}

	public String aRegistrarAssinaturaGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (mov.getSubscritor() == null)
			throw new AplicacaoException("Responsável não informado");

		if (!Ex.getInstance().getComp()
				.podeRegistrarAssinatura(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível registrar a assinatura");

		try {
			setMsg(Ex
					.getInstance()
					.getBL()
					.RegistrarAssinatura(getCadastrante(), getLotaTitular(),
							doc, mov.getDtMov(), mov.getSubscritor(),
							mov.getTitular()));
		} catch (final Exception e) {
			throw e;
		}

		return Action.SUCCESS;
	}

	public String aIncluirCosignatario() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance().getComp()
				.podeIncluirCosignatario(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível incluir cossignatário");

		return Action.SUCCESS;
	}

	public String aIncluirCosignatarioGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		mov.setDescrMov(getFuncaoCosignatario());
		if (getCosignatarioSel().getId() != null) {
			mov.setSubscritor(dao().consultar(getCosignatarioSel().getId(),
					DpPessoa.class, false));
		} else {
			mov.setSubscritor(null);
		}

		if (!Ex.getInstance().getComp()
				.podeIncluirCosignatario(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível incluir cossignatário");

		try {
			Ex.getInstance()
					.getBL()
					.incluirCosignatario(getCadastrante(), getLotaTitular(),
							doc, mov.getDtMov(), mov.getSubscritor(),
							mov.getDescrMov());
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}

	public String aReceber() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance().getComp()
				.podeReceber(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Documento não pode ser recebido");

		aReceberGravar();
		return "pula";
	}

	public String aReceberGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		// Pelo mesmo motivo acima, comentei isto:
		// DpPessoaDao dao = getFabrica().createDpPessoaDao();
		// mov.setResp(dao.consultar(getIdResp(), false));

		if (!Ex.getInstance().getComp()
				.podeReceber(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Documento não pode ser recebido");
		try {
			Ex.getInstance()
					.getBL()
					.receber(getCadastrante(), getLotaTitular(), mob,
							mov.getDtMov());
		} catch (final Exception e) {
			throw e;
		}

		return Action.SUCCESS;
	}

	public String aReceberPor() throws Exception {
		// buscarDocumento(true);
		//
		// if (!Ex.getInstance().getComp()
		// .podeReceberPor(getTitular(), getLotaTitular(), mob))
		// throw new AplicacaoException("Documento não pode ser recebido");
		//
		// if (!mob.isEmTransito())
		// throw new AplicacaoException(
		// "Documento não disponível para recebimento");
		//
		// aReceberPorGravar();
		return "pula";
	}

	// Nato: dasabilitado pq ninguem usa...
	public String aReceberPorGravar() throws Exception {
		// buscarDocumento(true);
		// lerForm(mov);
		//
		// ExMovimentacao lastMov = mob.getUltimaMovimentacao();
		// mov.setResp(lastMov.getResp());
		// mov.setLotaResp(lastMov.getLotaResp());
		//
		// try {
		// Ex.getInstance().getBL().receberPor( getCadastrante(),
		// getLotaTitular(), mob,
		// mov.getDtMov(), mov.getSubscritor(), mov.getResp(), mov
		// .getLotaResp());
		// /*
		// * Ex.getInstance().getBL().transferir(mov.getOrgaoExterno(),
		// getObsOrgao(),
		// * getCadastrante(), mov.getExDocumento(), mov.getNumVia(),
		// * mov.getDtMov(), mov.getLotaResp(), mov.getResp(), mov
		// * .getLotaDestinoFinal(), mov.getDestinoFinal(), mov
		// * .getSubscritor(), mov.getExTipoDespacho(), false,
		// * mov.getDescrMov(), conteudo, getRequest());
		// */
		// } catch (final Exception e) {
		// throw e;
		// }
		//
		// setDoc(mov.getExDocumento());
		return Action.SUCCESS;
	}

	// Nato: Temos que substituir por uma tela que mostre os itens marcados como
	// "em transito"
	public String aReceberLote() throws Exception {
		assertAcesso("");
		List<ExMobil> provItens = dao().consultarParaReceberEmLote(
				getLotaTitular());

		setItens(new ArrayList<ExMobil>());

		for (ExMobil m : provItens) {
			if (!m.isApensado()
					&& Ex.getInstance()
							.getComp()
							.podeAcessarDocumento(getTitular(),
									getLotaTitular(), m))
				getItens().add(m);
		}

		return Action.SUCCESS;
	}

	public String aReceberLoteGravar() throws Exception {
		assertAcesso("");
		final ExMovimentacao mov = new ExMovimentacao();
		lerForm(mov);

		final Pattern p = Pattern.compile("chk_([0-9]+)");

		try {
			StringBuffer msgErro = new StringBuffer();
			for (final String s : getPar().keySet()) {
				if (s.startsWith("chk_") && param(s).equals("true")) {
					final Matcher m = p.matcher(s);
					if (!m.find())
						throw new AplicacaoException(
								"Não foi possível ler a Id do documento e o número da via.");
					final ExMobil mob = dao().consultar(
							Long.valueOf(m.group(1)), ExMobil.class, false);

					if (!Ex.getInstance().getComp()
							.podeReceber(getTitular(), getLotaTitular(), mob)) {
						if (msgErro == null)
							msgErro = new StringBuffer(
									"Alguns documentos não puderam ser recebidos:  ");
						msgErro.append(doc.getCodigo());
						msgErro.append("  ");
					} else
						Ex.getInstance()
								.getBL()
								.receber(getCadastrante(), getLotaTitular(),
										mob, mov.getDtMov());

				}
			}
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}

	public String aArquivarCorrenteLote() throws Exception {
		assertAcesso("");
		List<ExMobil> provItens = dao().consultarParaArquivarCorrenteEmLote(
				getLotaTitular());

		setItens(new ArrayList<ExMobil>());

		for (ExMobil m : provItens) {
			if (!m.isApensado()
					&& Ex.getInstance()
							.getComp()
							.podeAcessarDocumento(getTitular(),
									getLotaTitular(), m))
				getItens().add(m.isVolume() ? m.doc().getMobilGeral() : m);
		}

		return Action.SUCCESS;
	}

	public String aArquivarCorrenteLoteGravar() throws Exception {
		assertAcesso("");
		final ExMovimentacao mov = new ExMovimentacao();
		lerForm(mov);

		final Pattern p = Pattern.compile("chk_([0-9]+)");

		final Date dt = dao().dt();

		try {
			for (final String s : getPar().keySet()) {
				if (s.startsWith("chk_") && param(s).equals("true")) {
					final Matcher m = p.matcher(s);
					if (!m.find())
						throw new AplicacaoException(
								"Não foi possível ler a Id do documento e o número da via.");
					final ExMobil mob = dao().consultar(
							Long.valueOf(m.group(1)), ExMobil.class, false);

					Ex.getInstance()
							.getBL()
							.arquivarCorrente(getCadastrante(),
									getLotaTitular(), mob,
									mov.getDtMov(), dt, mov.getSubscritor(), false);
				}
			}
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}

	public String aArquivarIntermediarioLote() throws Exception {
		assertAcesso("");
		int offset = 0;
		if (getP().getOffset() != null) {
			offset = getP().getOffset();
		}
		List<ExItemDestinacao> listaProv = dao()
				.consultarParaArquivarIntermediarioEmLote(getLotaTitular(),
						offset);

		ExTopicoDestinacao digitais = new ExTopicoDestinacao("Digitais", true);
		ExTopicoDestinacao fisicos = new ExTopicoDestinacao("Físicos", true);
		ExTopicoDestinacao indisponiveis = new ExTopicoDestinacao(
				"Não disponíveis", false);

		for (ExItemDestinacao item : listaProv) {
			boolean pode = Ex
					.getInstance()
					.getComp()
					.podeArquivarIntermediario(getTitular(), getLotaTitular(),
							item.getMob());
			if (pode) {
				if (item.getMob().doc().isEletronico())
					digitais.adicionar(item);
				else
					fisicos.adicionar(item);
			} else
				indisponiveis.adicionar(item);
		}

		List<ExTopicoDestinacao> listaFinal = new ArrayList<ExTopicoDestinacao>();
		listaFinal.add(digitais);
		listaFinal.add(fisicos);
		listaFinal.add(indisponiveis);

		setTamanho(dao().consultarQuantidadeParaArquivarIntermediarioEmLote(
				getLotaTitular()));
		setItens(listaFinal);

		return Action.SUCCESS;
	}

	public String aArquivarIntermediarioLoteGravar() throws Exception {
		assertAcesso("");
		final ExMovimentacao mov = new ExMovimentacao();
		lerForm(mov);

		final Pattern p = Pattern.compile("chk_([0-9]+)");

		final Date dt = dao().dt();

		try {
			for (final String s : getPar().keySet()) {
				if (s.startsWith("chk_") && param(s).equals("true")) {
					final Matcher m = p.matcher(s);
					if (!m.find())
						throw new AplicacaoException(
								"Não foi possível ler a Id do documento e o número da via.");
					final ExMobil mob = dao().consultar(
							Long.valueOf(m.group(1)), ExMobil.class, false);

					Ex.getInstance()
							.getBL()
							.arquivarIntermediario(getCadastrante(),
									getLotaTitular(), mob, mov.getDtMov(),
									mov.getSubscritor(), mov.getDescrMov());
				}
			}
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}

	public String aArquivarPermanenteLote() throws Exception {
		assertAcesso("");
		int offset = 0;
		if (getP().getOffset() != null) {
			offset = getP().getOffset();
		}
		List<ExItemDestinacao> listaProv = dao()
				.consultarParaArquivarPermanenteEmLote(getLotaTitular(), offset);

		ExTopicoDestinacao digitais = new ExTopicoDestinacao("Digitais", true);
		ExTopicoDestinacao fisicos = new ExTopicoDestinacao("Físicos", true);
		ExTopicoDestinacao indisponiveis = new ExTopicoDestinacao(
				"Não disponíveis", false);

		for (ExItemDestinacao item : listaProv) {
			boolean pode = Ex
					.getInstance()
					.getComp()
					.podeArquivarPermanente(getTitular(), getLotaTitular(),
							item.getMob());
			if (pode) {
				if (item.getMob().doc().isEletronico())
					digitais.adicionar(item);
				else
					fisicos.adicionar(item);
			} else
				indisponiveis.adicionar(item);
		}

		List<ExTopicoDestinacao> listaFinal = new ArrayList<ExTopicoDestinacao>();
		listaFinal.add(digitais);
		listaFinal.add(fisicos);
		listaFinal.add(indisponiveis);

		setTamanho(dao().consultarQuantidadeParaArquivarPermanenteEmLote(
				getLotaTitular()));
		setItens(listaFinal);

		return Action.SUCCESS;
	}

	public String aArquivarPermanenteLoteGravar() throws Exception {
		assertAcesso("");
		final ExMovimentacao mov = new ExMovimentacao();
		lerForm(mov);

		final Pattern p = Pattern.compile("chk_([0-9]+)");

		final Date dt = dao().dt();

		String erro = "";

		try {
			for (final String s : getPar().keySet()) {
				if (s.startsWith("chk_") && param(s).equals("true")) {
					final Matcher m = p.matcher(s);
					if (!m.find())
						throw new AplicacaoException(
								"Não foi possível ler a Id do documento e o número da via.");

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
		return Action.SUCCESS;
	}

	public String aAssinarLote() throws Exception {
		assertAcesso("");
		List<ExDocumento> itensComoSubscritor = dao()
				.listarDocPendenteAssinatura(getTitular());
		List<ExDocumento> itensFinalizados = new ArrayList<ExDocumento>();

		for (ExDocumento doc : itensComoSubscritor) {

			if (doc.isFinalizado())
				itensFinalizados.add(doc);
		}
		setItensSolicitados(itensFinalizados);
		
		setDocumentosQuePodemSerAssinadosComSenha(new ArrayList<ExDocumento>());
		
		for (ExDocumento exDocumento : itensSolicitados) {
			if(Ex.getInstance()
				.getComp().podeAssinarComSenha(getTitular(), getLotaTitular(), exDocumento.getMobilGeral())) {
				getDocumentosQuePodemSerAssinadosComSenha().add(exDocumento);
			}
		}
		
		return Action.SUCCESS;
	}
	
	public String aAssinarDespachoLote() throws Exception {		
		assertAcesso("");
		List<ExMovimentacao> itensComoSubscritor = dao().
					listarDespachoPendenteAssinatura(getTitular());

		setItens(new ArrayList<ExMovimentacao>());
		
		setMovimentacoesQuePodemSerAssinadasComSenha(new ArrayList<ExMovimentacao>());
		for (ExMovimentacao mov : itensComoSubscritor) {
				if(!mov.isAssinada() && !mov.isCancelada()) {
					getItens().add(mov);
					
					if(Ex.getInstance().getComp().podeAssinarMovimentacaoComSenha(getTitular(), getLotaTitular(), mov))
						getMovimentacoesQuePodemSerAssinadasComSenha().add(mov);
				}
				
		}
		return Action.SUCCESS;
	}

	public String aReverterIndicacaoPermanente() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance()
				.getComp()
				.podeReverterIndicacaoPermanente(getTitular(),
						getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível reverter indicação para guarda permanente");

		return Action.SUCCESS;
	}

	public String aReverterIndicacaoPermanenteGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance()
				.getComp()
				.podeReverterIndicacaoPermanente(getTitular(),
						getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível reverter indicação para guarda permanente");

		if (mov.getExDocumento().isEletronico()) {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("dd/MM/yyyy");
			setDtRegMov(sdf.format(new Date()).toString());
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

		setDoc(mov.getExDocumento());
		return Action.SUCCESS;
	}

	public String aRetirarDeEditalEliminacao() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance()
				.getComp()
				.podeRetirarDeEditalEliminacao(getTitular(), getLotaTitular(),
						mob))
			throw new AplicacaoException(
					"Não é possível retirar o documento de edital de eliminação");

		return Action.SUCCESS;
	}

	public String aRetirarDeEditalEliminacaoGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance()
				.getComp()
				.podeRetirarDeEditalEliminacao(getTitular(), getLotaTitular(),
						mob))
			throw new AplicacaoException(
					"Não é possível retirar o documento de edital de eliminação");

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

		setDoc(mov.getExDocumento());
		return Action.SUCCESS;
	}

	public String aIndicarPermanente() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance().getComp()
				.podeIndicarPermanente(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível fazer indicação para guarda permanente");

		return Action.SUCCESS;
	}

	public String aIndicarPermanenteGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance().getComp()
				.podeIndicarPermanente(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível fazer indicação para guarda permanente");

		if (mov.getExDocumento().isEletronico()) {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("dd/MM/yyyy");
			setDtRegMov(sdf.format(new Date()).toString());
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

		setDoc(mov.getExDocumento());
		return Action.SUCCESS;
	}

	public String aAvaliar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance().getComp()
				.podeAvaliar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível avaliar");

		return Action.SUCCESS;
	}

	public String aAvaliarGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance().getComp()
				.podeAvaliar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível avaliar");

		if (mov.getExDocumento().isEletronico()) {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("dd/MM/yyyy");
			setDtRegMov(sdf.format(new Date()).toString());
			mov.setSubscritor(getTitular());
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

		setDoc(mov.getExDocumento());
		return Action.SUCCESS;
	}

	public String aReclassificar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance().getComp()
				.podeReclassificar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível reclassificar");

		return Action.SUCCESS;
	}

	public String aReclassificarGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance().getComp()
				.podeReclassificar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível reclassificar");

		if (mov.getExDocumento().isEletronico()) {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("dd/MM/yyyy");
			setDtRegMov(sdf.format(new Date()).toString());
			mov.setSubscritor(getTitular());
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

		setDoc(mov.getExDocumento());
		return Action.SUCCESS;
	}

	public String aReferenciar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance().getComp()
				.podeReferenciar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível fazer vinculação");

		return Action.SUCCESS;
	}

	public String aPrever() throws Exception {
		assertAcesso("");
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
			return "processa_modelo";
		return Action.SUCCESS;
	}

	public String aReferenciarGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (!Ex.getInstance().getComp()
				.podeReferenciar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível fazer vinculação");
		if (mov.getExMobilRef() == null)
			throw new AplicacaoException(
					"Não foi selecionado um documento para a vinculação");

		if (mov.getExDocumento().isEletronico()) {
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("dd/MM/yyyy");
			setDtRegMov(sdf.format(new Date()).toString());
			mov.setSubscritor(getTitular());
		}

		try {
			Ex.getInstance()
					.getBL()
					.referenciarDocumento(getCadastrante(), getLotaTitular(),
							mob, mov.getExMobilRef(), mov.getDtMov(),
							mov.getSubscritor(), mov.getTitular());
		} catch (final Exception e) {
			throw e;
		}

		setDoc(mov.getExDocumento());
		return Action.SUCCESS;
	}

	public String aTransferir() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		final ExMovimentacao ultMov = mob.getUltimaMovimentacao();
		if (getRequest().getAttribute("postback") == null) {
			if (ultMov.getLotaDestinoFinal() != null) {
				getLotaDestinoFinalSel().buscarPorObjeto(
						ultMov.getLotaDestinoFinal());
				setTipoDestinoFinal(1); // Orgao interno
				getLotaResponsavelSel().buscarPorObjeto(
						ultMov.getLotaDestinoFinal());
				setTipoResponsavel(1);
			}
			if (ultMov.getDestinoFinal() != null) {
				getDestinoFinalSel().buscarPorObjeto(ultMov.getDestinoFinal());
				setTipoDestinoFinal(2); // Matricula
				getResponsavelSel().buscarPorObjeto(ultMov.getDestinoFinal());
				setTipoResponsavel(2);
			}
		}

		if (!(Ex.getInstance().getComp()
				.podeTransferir(getTitular(), getLotaTitular(), mob) || Ex
				.getInstance().getComp()
				.podeDespachar(getTitular(), getLotaTitular(), mob)))
			throw new AplicacaoException(
					"Não é possível fazer despacho nem transferência");
		return Action.SUCCESS;
	}

	public String aTransferirGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		final ExMovimentacao UltMov = mob.getUltimaMovimentacaoNaoCancelada();
		if ((mov.getLotaResp() != null && mov.getResp() == null
				&& UltMov.getLotaResp() != null && UltMov.getResp() == null && UltMov
				.getLotaResp().equivale(mov.getLotaResp()))
				|| (mov.getResp() != null && UltMov.getResp() != null && UltMov
						.getResp().equivale(mov.getResp())))
			throw new AplicacaoException(
					"Novo responsável não pode ser igual ao atual");

		if (!Ex.getInstance().getComp()
				.podeReceberPorConfiguracao(mov.getResp(), mov.getLotaResp()))
			throw new AplicacaoException(
					"Destinatário não pode receber documentos");

		if (!(Ex.getInstance().getComp()

		.podeTransferir(getTitular(), getLotaTitular(), mob) || Ex
				.getInstance().getComp()
				.podeDespachar(getTitular(), getLotaTitular(), mob)))
			throw new AplicacaoException(
					"Não é possível fazer despacho nem transferência");

		try {
			Ex.getInstance()
					.getBL()
					.transferir(mov.getOrgaoExterno(), getObsOrgao(),
							getCadastrante(), getLotaTitular(), mob,
							mov.getDtMov(), mov.getDtIniMov(),
							mov.getDtFimMov(), mov.getLotaResp(),
							mov.getResp(), mov.getLotaDestinoFinal(),
							mov.getDestinoFinal(), mov.getSubscritor(),
							mov.getTitular(), mov.getExTipoDespacho(), false,
							mov.getDescrMov(), conteudo,
							mov.getNmFuncaoSubscritor(), false, false);
		} catch (final Exception e) {
			throw e;
		}

		/*
		 * ArrayList lista = new ArrayList(); final Object[] ao = {
		 * mov.getExDocumento(), UltMov }; lista.add(ao); setItens(lista);
		 */
		setMov(mob.getUltimaMovimentacao());
		if (param("protocolo") != null && param("protocolo").equals("mostrar"))
			return "protocolo";
		return Action.SUCCESS;
	}

	public String aEncerrarVolumeGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);
		lerForm(mov);

		if (mob.isVolumeEncerrado())
			throw new AplicacaoException(
					"Não é permitido encerrar um volume já encerrado.");
		try {
			Ex.getInstance()
					.getBL()

					.encerrarVolume(getCadastrante(), getLotaTitular(), mob,
							mov.getDtMov(), mov.getSubscritor(),
							mov.getTitular(), mov.getNmFuncaoSubscritor(),
							false);
		} catch (final Exception e) {
			throw e;
		}

		/*
		 * ArrayList lista = new ArrayList(); final Object[] ao = {
		 * mov.getExDocumento(), UltMov }; lista.add(ao); setItens(lista);
		 */
		setMov(mob.getUltimaMovimentacao());
		return Action.SUCCESS;
	}

	public String aAnotar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance().getComp()
				.podeFazerAnotacao(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível fazer anotação");

		return Action.SUCCESS;
	}

	public String aAnotarGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		lerForm(mov);

		final ExMovimentacao UltMov = mob.getUltimaMovimentacaoNaoCancelada();

		if (!Ex.getInstance().getComp()
				.podeFazerAnotacao(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível fazer anotação");

		try {
			Ex.getInstance()
					.getBL()
					.anotar(getCadastrante(), getLotaTitular(), mob,
							mov.getDtMov(), mov.getLotaResp(), mov.getResp(),
							mov.getSubscritor(), mov.getTitular(),
							mov.getDescrMov(), mov.getNmFuncaoSubscritor());
		} catch (final Exception e) {
			throw e;
		}

		/*
		 * ArrayList lista = new ArrayList(); final Object[] ao = {
		 * mov.getExDocumento(), UltMov }; lista.add(ao); setItens(lista);
		 */

		return Action.SUCCESS;
	}

	public String aAnotarLote() throws Exception {
		assertAcesso("");
		List<ExMobil> provItens = dao().consultarParaAnotarEmLote(
				getLotaTitular());

		setItens(new ArrayList<ExMobil>());

		for (ExMobil m : provItens) {
			if (!m.isApensado()
					&& Ex.getInstance()
							.getComp()
							.podeAcessarDocumento(getTitular(),
									getLotaTitular(), m))
				getItens().add(m);
		}

		return Action.SUCCESS;
	}

	public String aAnotarLoteGravar() throws Exception {
		assertAcesso("");
		final ExMovimentacao mov = new ExMovimentacao();
		lerForm(mov);

		final Pattern p = Pattern.compile("chk_([0-9]+)");

		final Date dt = dao().dt();

		try {
			for (final String s : getPar().keySet()) {
				if (s.startsWith("chk_") && param(s).equals("true")) {
					final Matcher m = p.matcher(s);
					if (!m.find())
						throw new AplicacaoException(
								"Não foi possível ler a Id do documento e o número da via.");
					final ExMobil mob = dao().consultar(
							Long.valueOf(m.group(1)), ExMobil.class, false);

					Ex.getInstance()
							.getBL()
							.anotar(getCadastrante(), getLotaTitular(), mob,
									mov.getDtMov(), mov.getLotaResp(),
									mov.getResp(), mov.getSubscritor(),
									mov.getTitular(), mov.getDescrMov(),
									mov.getNmFuncaoSubscritor());
				}
			}
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}

	public String aVincularPapel() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (!Ex.getInstance().getComp()
				.podeFazerVinculacaoPapel(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível fazer vinculação de papel");

		return Action.SUCCESS;
	}

	public String aVincularPapelGravar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		lerForm(mov);

		if (mov.getResp() == null && mov.getLotaResp() == null)
			throw new AplicacaoException(
					"Não foi informado o responsável ou lotação responsável para a vinculação de papel ");

		if (mov.getResp() != null) {
			mov.setDescrMov(mov.getExPapel().getDescPapel() + ":"
					+ mov.getResp().getDescricaoIniciaisMaiusculas());
		} else {
			if (mov.getLotaResp() != null) {
				mov.setDescrMov(mov.getExPapel().getDescPapel() + ":"
						+ mov.getLotaResp().getDescricaoIniciaisMaiusculas());
			}
		}

		final ExMovimentacao UltMov = mob.getUltimaMovimentacaoNaoCancelada();

		if (!Ex.getInstance().getComp()
				.podeFazerVinculacaoPapel(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException(
					"Não é possível fazer vinculação de papel");

		try {
			Ex.getInstance()
					.getBL()
					.vincularPapel(getCadastrante(), getLotaTitular(), mob,
							mov.getDtMov(), mov.getLotaResp(), mov.getResp(),
							mov.getSubscritor(), mov.getTitular(),
							mov.getDescrMov(), mov.getNmFuncaoSubscritor(),
							mov.getExPapel());
		} catch (final Exception e) {
			throw e;
		}

		return Action.SUCCESS;
	}

	public String aTransferirLote() throws Exception {
		assertAcesso("");
		Iterator<ExMobil> provItens = dao().consultarParaTransferirEmLote(
				getLotaTitular());
		setItens(new ArrayList<ExMobil>());

		while (provItens.hasNext()) {
			ExMobil m = provItens.next();
			getItens().add(m);
		}

		return Action.SUCCESS;
	}

	public String aTransferirLote_new() throws Exception {
		assertAcesso("");
		final ExDocumentoDaoFiltro flt = new ExDocumentoDaoFiltro();
		DpLotacao lotaFiltro = new DpLotacao();
		lotaFiltro.setIdLotacaoIni(getLotaTitular().getIdLotacaoIni());
		List<DpLotacao> lotacoesEquivalentes = dao()
				.consultar(lotaFiltro, null);

		LinkedList provItens = new LinkedList();
		List provList;

		for (DpLotacao lota : lotacoesEquivalentes) {
			flt.setUltMovLotaRespSelId(lota.getId());
			// flt.setUltMovRespSelId(getCadastrante().getIdPessoa());
			flt.setUltMovIdEstadoDoc(ExEstadoDoc.ESTADO_DOC_EM_ANDAMENTO);

			// setTamanho(docDao.consultarQuantidadePorFiltro(flt));

			provList = dao().consultarPorFiltro(flt);
			if (provList != null)
				provItens.addAll(provList);

			flt.setUltMovIdEstadoDoc(ExEstadoDoc.ESTADO_DOC_PENDENTE_DE_ASSINATURA);

			provList = dao().consultarPorFiltro(flt);
			if (provList != null)
				provItens.addAll(provList);
		}

		setItens(provItens);

		return Action.SUCCESS;
	}

	public String aTransferirLoteGravar() throws Exception {
		assertAcesso("");
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
			throw new AplicacaoException(
					"Não foi definido o destino da transferência.");
		if (param("tpdall") != null && paramLong("tpdall") != 0)
			despaUnico = true;

		AplicacaoException msgErroNivelAcessoso = null;

		for (final String s : getPar().keySet()) {
			try {
				if (s.startsWith("chk_") && param(s).equals("true")) {
					final Long idTpDespacho;
					if (!despaUnico)
						idTpDespacho = Long.valueOf(param(s.replace("chk_",
								"tpd_")));
					else
						idTpDespacho = Long.valueOf(param("tpdall"));

					ExTipoDespacho tpd = null;
					if (idTpDespacho != null && idTpDespacho > 0)
						tpd = dao().consultar(idTpDespacho,
								ExTipoDespacho.class, false);

					final Matcher m = p.matcher(s);
					if (!m.find())
						throw new AplicacaoException(
								"Não foi possível ler a Id do documento e o número da via.");

					final ExMobil mobil = dao().consultar(
							Long.valueOf(m.group(1)), ExMobil.class, false);

					if (!Ex.getInstance()
							.getComp()
							.podeAcessarDocumento(getTitular(),
									getLotaTitular(), mobil)) {
						if (msgErroNivelAcessoso == null)
							msgErroNivelAcessoso = new AplicacaoException(
									"O documento não pode ser transferido por estar inacessível ao usuário.");
						if (!(msgErroNivelAcessoso.equals(null)))
							MapMensagens.put(mobil, msgErroNivelAcessoso);
					} else {
						String txt = "";
						if (!despaUnico)
							txt = param(s.replace("chk_", "txt_"));
						else
							txt = param("txtall");
						if (txt.equals(""))
							txt = null;

						nmobil = new ExMobil();
						nmobil = mobil;
						Mobeis.add(mobil);

						Ex.getInstance()
								.getBL()
								.transferir(mov.getOrgaoExterno(),
										getObsOrgao(), getCadastrante(),
										getLotaTitular(), mobil,
										mov.getDtMov(), dt, mov.getDtFimMov(),
										mov.getLotaResp(), mov.getResp(),
										mov.getLotaDestinoFinal(),
										mov.getDestinoFinal(),
										mov.getSubscritor(), mov.getTitular(),
										tpd, false, txt, null,
										mov.getNmFuncaoSubscritor(), false, false);

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
			for (Iterator<Entry<ExMobil, AplicacaoException>> it = MapMensagens
					.entrySet().iterator(); it.hasNext();) {
				Entry<ExMobil, AplicacaoException> excep = it.next();
				final Object[] ao = { excep.getKey(),
						excep.getValue().getMessage() };
				System.out.println("Falha: " + excep.getKey().doc().getSigla());
				System.out.println("Mensagem de erro: "
						+ excep.getValue().getMessage());
				al.add(ao);
			}
		}

		for (Iterator<ExMobil> it = Mobeis.iterator(); it.hasNext();) {
			ExMobil mob = it.next();
			if (!(MapMensagens.containsKey(mob))) {
				MobilSucesso.add(mob);
				System.out.println("Mobil Geral: "
						+ mob.doc().getMobilGeral().isGeral());
				final Object[] ao = { mob.doc(),
						mob.getUltimaMovimentacaoNaoCancelada() };
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

		return Action.SUCCESS;
	}

	public String aViaProtocolo() throws Exception {
		assertAcesso("");
		List<ExMobil> provItens = dao().consultarParaViaDeProtocolo(
				getLotaTitular());

		setItens(new ArrayList<ExMobil>());

		for (ExMobil m : provItens) {
			if (Ex.getInstance().getComp()
					.podeAcessarDocumento(getTitular(), getLotaTitular(), m))
				getItens().add(m);
		}

		return Action.SUCCESS;
	}

	// public void testaGravacaoRTF() throws Exception {
	// final ExDocumentoDao docDao = getFabrica().createExDocumentoDao();
	// doc = docDao.consultar(getIdDoc(), false);
	// GeradorRTF geradorRTF = new GeradorRTF();
	// geradorRTF.geraRTF(doc);
	// }

	public File getArquivo() {
		return arquivo;
	}

	/*
	 * como usamos <ww:file name="arquivo" .../> o content Type do arquivo será
	 * obtido através getter/setter de <file-tag-name>ContentType
	 */
	public String getArquivoContentType() {
		return contentType;
	}

	/*
	 * como usamos <ww:file name="arquivo" .../> o nome do arquivo será obtido
	 * através getter/setter de <file-tag-name>FileName
	 */
	public String getArquivoFileName() {
		return fileName;
	}

	public String getAssinaturaB64() {
		return assinaturaB64;
	}

	public String getConteudo() {
		if (conteudo == null)
			return null;
		if (conteudo.trim().length() == 0)
			return null;
		return conteudo.trim();
	}

	public String getConteudoTpDoc() {
		return conteudoTpDoc;
	}

	public CpOrgaoSelecao getCpOrgaoSel() {
		return cpOrgaoSel;
	}

	public String getDescrMov() {
		return descrMov;
	}

	public DpPessoaSelecao getDestinoFinalSel() {
		return destinoFinalSel;
	}

	public ExDocumento getDoc() {
		return doc;
	}

	public List getDocsTransito() throws Exception {
		final ExDocumentoDaoFiltro flt = new ExDocumentoDaoFiltro();
		flt.setOrgaoExternoSelId(getCpOrgaoSel().getId());
		flt.setUltMovLotaSubscritorSelId(getTitular().getLotacao().getId());
		flt.setUltMovIdEstadoDoc(ExEstadoDoc.ESTADO_DOC_EM_TRANSITO);
		return dao().consultarPorFiltro(flt);
	}

	public List getDocsTransitoExterno() throws Exception {
		final ExDocumentoDaoFiltro flt = new ExDocumentoDaoFiltro();
		flt.setOrgaoExternoSelId(getCpOrgaoSel().getId());
		flt.setUltMovLotaSubscritorSelId(getTitular().getLotacao().getId());
		flt.setUltMovIdEstadoDoc(ExEstadoDoc.ESTADO_DOC_TRANSFERIDO_A_ORGAO_EXTERNO);
		return dao().consultarPorFiltro(flt);
	}

	public ExMobilSelecao getDocumentoRefSel() {
		return documentoRefSel;
	}

	public ExClassificacaoSelecao getClassificacaoSel() {
		return classificacaoSel;
	}

	public ExDocumentoSelecao getDocumentoSel() {
		return documentoSel;
	}

	public ExMobilSelecao getDocumentoViaSel() {
		return documentoViaSel;
	}

	public String getDtMovString() {
		return dtMovString;
	}

	public String getDtDevolucaoMovString() {
		return dtDevolucaoMovString;
	}

	public String getDtRegMov() {
		return dtRegMov;
	}

	public Long getId() {
		return id;
	}

	public Long getIdResp() {
		return idResp;
	}

	public Long getIdTpDespacho() {
		return idTpDespacho;
	}

	public List getItens() {
		return itens;
	}

	public Map<Long, String> getListaRespRecebimento() {
		final Map<Long, String> hash = new Hashtable<Long, String>();
		final ExMovimentacao mov = mob.getUltimaMovimentacao();
		hash.put(mov.getResp().getId(), mov.getResp().getNomePessoa());
		hash.put(getCadastrante().getId(), getCadastrante().getNomePessoa());
		return hash;
	}

	public List<ExNivelAcesso> getListaNivelAcesso() throws Exception {
		ExFormaDocumento exForma = doc.getExFormaDocumento();
		ExClassificacao exClassif = doc.getExClassificacaoAtual();
		ExTipoDocumento exTipo = doc.getExTipoDocumento();
		ExModelo exMod = doc.getExModelo();

		return getListaNivelAcesso(exTipo, exForma, exMod, exClassif);
	}

	public Map<Integer, String> getListaTipoDestinoFinal() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, "Órgão Integrado");
		map.put(2, "Matrícula");
		return map;
	}

	public Map<Integer, String> getListaTipoResp() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, "Órgão Integrado");
		map.put(2, "Matrícula");
		map.put(3, "Órgão Externo");
		return map;
	}

	public Map<Integer, String> getListaTipoRespPerfil() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, "Matrícula");
		map.put(2, "Órgão Integrado");
		return map;
	}

	public List<ExNivelAcesso> getListaTipoNivelAcesso() throws Exception {
		return getListaNivelAcesso(getDoc().getExTipoDocumento(), getDoc()
				.getExFormaDocumento(), getDoc().getExModelo(), getDoc()

		.getExClassificacaoAtual());
	}

	public DpLotacaoSelecao getLotaDestinoFinalSel() {
		return lotaDestinoFinalSel;
	}

	public DpLotacaoSelecao getLotaResponsavelSel() {
		return lotaResponsavelSel;
	}

	public ExMovimentacao getMov() {
		return mov;
	}

	public Integer getNumViaDocBusca() {
		return numViaDocBusca;
	}

	public String getObsOrgao() {
		return obsOrgao;
	}

	public CpOrgao getOrgaoExterno() {
		return orgaoExterno;
	}

	@Override
	public Integer getPostback() {
		return postback;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public DpPessoaSelecao getResponsavelSel() {
		return responsavelSel;
	}

	public DpPessoaSelecao getSubscritorSel() {
		return subscritorSel;
	}

	public Integer getTipoDestinatario() {
		return tipoDestinatario;
	}

	public Integer getTipoDestinoFinal() {
		return tipoDestinoFinal;
	}

	public Integer getTipoResponsavel() {
		return tipoResponsavel;
	}

	public List<ExTipoDespacho> getTiposDespacho() throws AplicacaoException,
			Exception {
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

	private List<ExTipoDespacho> tiposDespacho2 = null;

	public Integer getVia() {
		return via;
	}

	public boolean isPodeMovimentar(final DpPessoa cadastrante,
			final ExMobil mob) throws Exception {
		return Ex.getInstance().getComp()
				.podeMovimentar(getTitular(), getLotaTitular(), mob);
	}

	private void lerForm(final ExMovimentacao mov) throws Exception {
		assertAcesso("");
		mov.setExMobil(mob);

		mov.setDescrMov(getDescrMov());
		if (idPapel != null) {
			mov.setExPapel(dao().consultar(idPapel, ExPapel.class, false));
		}

		if (getIdTpDespacho() != null) {
			switch (getIdTpDespacho().intValue()) {
			case 0:
				break;
			case -1:
				break;
			case -2:
				break;
			default:
				if (getIdTpDespacho() != null)
					mov.setExTipoDespacho(dao().consultar(
							getIdTpDespacho().longValue(),
							ExTipoDespacho.class, false));
			}
		}

		conteudo = getConteudo();

		if (mov.getCadastrante() == null)
			mov.setCadastrante(getCadastrante());
		mov.setLotaCadastrante(getLotaTitular());
		if (mov.getLotaCadastrante() == null)
			mov.setLotaCadastrante(mov.getCadastrante().getLotacao());

		if (getSubscritorSel().getId() != null) {
			mov.setSubscritor(dao().consultar(getSubscritorSel().getId(),
					DpPessoa.class, false));
		} else {
			mov.setSubscritor(null);
		}

		mov.setNmFuncaoSubscritor(getNmFuncaoSubscritor());

		if (substituicao)
			if (getTitularSel().getId() != null) {
				mov.setTitular(dao().consultar(getTitularSel().getId(),
						DpPessoa.class, false));
			} else {
				mov.setTitular(null);
			}
		else {
			mov.setTitular(mov.getSubscritor());
			mov.setLotaTitular(mov.getLotaSubscritor());
		}
		/*
		 * if (form.getDestinatarioSel().getId() != null) {
		 * mov.setDestinatario(pessoaDao.consultar(form.getDestinatarioSel()
		 * .getId(), false)); } else { mov.setDestinatario(null); }
		 */

		if (getDocumentoRefSel().getId() != null) {
			mov.setExMobilRef(daoMob(getDocumentoRefSel().getId()));
		}

		if (getClassificacaoSel().getId() != null) {
			mov.setExClassificacao(dao()
					.consultar(getClassificacaoSel().getId(),
							ExClassificacao.class, false));
		}

		if (getResponsavelSel().getId() != null) {
			mov.setResp(dao().consultar(getResponsavelSel().getId(),
					DpPessoa.class, false));
		} else {
			mov.setResp(null);
		}

		if (getLotaResponsavelSel().getId() != null) {
			mov.setLotaResp(dao().consultar(getLotaResponsavelSel().getId(),
					DpLotacao.class, false));
		} else {
			if (mov.getResp() != null) {
				mov.setLotaResp(mov.getResp().getLotacao());
			} else {
				mov.setLotaResp(null);
			}
		}

		if (getCpOrgaoSel().getId() != null) {
			mov.setOrgaoExterno(dao().consultar(getCpOrgaoSel().getId(),
					CpOrgao.class, false));
		}

		mov.setObsOrgao(getObsOrgao());

		if (getDestinoFinalSel().getId() != null) {
			mov.setDestinoFinal(dao().consultar(getDestinoFinalSel().getId(),
					DpPessoa.class, false));
		} else {
			mov.setDestinoFinal(null);
		}

		if (getLotaDestinoFinalSel().getId() != null) {
			mov.setLotaDestinoFinal(dao().consultar(
					getLotaDestinoFinalSel().getId(), DpLotacao.class, false));
		} else {
			if (mov.getDestinoFinal() != null) {
				mov.setLotaDestinoFinal(mov.getDestinoFinal().getLotacao());
			} else {
				mov.setLotaDestinoFinal(null);
			}
		}

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			mov.setDtMov(df.parse(getDtMovString()));
		} catch (final ParseException e) {
			mov.setDtMov(null);
		} catch (final NullPointerException e) {
			mov.setDtMov(null);
		}

		final SimpleDateFormat ddf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			mov.setDtFimMov(ddf.parse(getDtDevolucaoMovString()));
		} catch (final ParseException e) {
			mov.setDtFimMov(null);
		} catch (final NullPointerException e) {
			mov.setDtFimMov(null);
		}

		if (getDtPubl() != null) {
			try {
				mov.setDtMov(df.parse(getDtPubl()));
			} catch (final ParseException e) {
				mov.setDtMov(new Date());
			} catch (final NullPointerException e) {
				mov.setDtMov(new Date());
			}
		}

		try {
			mov.setDtDispPublicacao(df.parse(getDtDispon()));
		} catch (final ParseException e) {
			mov.setDtDispPublicacao(null);
		} catch (final NullPointerException e) {
			mov.setDtDispPublicacao(null);
		}

		// Nato: Comentei isso porque acho que não é necessário e custaria um
		// acesso ao Oracle
		// if (mov.getDtIniMov() == null)
		// mov.setDtIniMov(dao().dt());

		if ((mov.getTitular() != null && mov.getSubscritor() == null)
				|| (mov.getLotaTitular() != null && mov.getLotaSubscritor() == null))
			throw new AplicacaoException(
					"Não foi selecionado o substituto para o titular");
	}

	public void setArquivo(final File arquivo) {
		this.arquivo = arquivo;
	}

	public void setArquivoContentType(final String contentType) {
		this.contentType = contentType;
	}

	public void setArquivoFileName(final String fileName) {
		this.fileName = fileName;
	}

	public void setAssinaturaB64(final String assinaturaB64) {
		this.assinaturaB64 = assinaturaB64;
	}

	// public String aTransferir() throws Exception {
	// fabrica = DaoFactory.getDAOFactory();
	// ExMovimentacaoForm form = (ExMovimentacaoForm) actionForm;
	//
	// ExDocumentoDao docDao = fabrica.createExDocumentoDao();
	//
	// if (form.getIdDoc() == null) {
	// String sId = request.getParameter("id");
	// if (sId == null) {
	// throw new CsisException("id não foi informada.");
	// }
	// form.setIdDoc(Long.valueOf(sId));
	// }
	//
	// if (form.getNumVia() == null) {
	// String sVia = request.getParameter("via");
	// form.setNumVia(Short.parseShort(sVia));
	// }
	//
	// form.getSubscritorSel().buscar();
	// form.getResponsavelSel().buscar();
	// form.getLotaResponsavelSel().buscar();
	// form.getDestinoFinalSel().buscar();
	// form.getLotaDestinoFinalSel().buscar();
	//
	// ExDocumento doc = docDao.consultar(form.getIdDoc(), false);
	// request.setAttribute("doc", doc);
	//
	// ExMovimentacao ultMov = Ex.getInstance().getBL().ultimaMovimentacao(doc,
	// form.getNumVia());
	// if (ultMov.getLotaDestinoFinal() != null) {
	// form.getLotaDestinoFinalSel().buscarPorObjeto(ultMov.getLotaDestinoFinal());
	// form.setTipoDestinoFinal("1"); // Orgao interno
	// }
	// if (ultMov.getDestinoFinal() != null) {
	// form.getDestinoFinalSel().buscarPorObjeto(ultMov.getDestinoFinal());
	// form.setTipoDestinoFinal("2"); // Matricula
	// }
	/*
	 * Edson - alterar
	 * 
	 * if (ultMov.getLotaDestinoFinal() != null) {
	 * //form.getLotaDestinoFinalSel(
	 * ).buscarPorObjeto(ultMov.getLotaDestinoFinal());
	 * //form.setTipoDestinoFinal("1"); // Orgao interno
	 * form.getLotaResponsavelSel
	 * ().buscarPorObjeto(ultMov.getLotaDestinoFinal());
	 * form.setTipoResponsavel("1"); // Orgao interno } if
	 * (ultMov.getDestinoFinal() != null) {
	 * //form.getDestinoFinalSel().buscarPorObjeto(ultMov.getDestinoFinal());
	 * //form.setTipoDestinoFinal("2"); // Matricula
	 * form.getResponsavelSel().buscarPorObjeto(ultMov.getDestinoFinal());
	 * form.setTipoResponsavel("2"); // Matricula }
	 */

	public void setConteudo(final String conteudo) {
		this.conteudo = conteudo;
	}

	public void setConteudoTpDoc(final String conteudoTpDoc) {
		this.conteudoTpDoc = conteudoTpDoc;
	}

	public void setCpOrgaoSel(final CpOrgaoSelecao orgaoSel) {
		this.cpOrgaoSel = orgaoSel;
	}

	// public ActionForward aSelecionar(ActionMapping mapping, ActionForm
	// actionForm, HttpServletRequest request,
	// HttpServletResponse response) throws Exception {
	// fabrica = DaoFactory.getDAOFactory();
	//
	// ExMovimentacaoForm form = (ExMovimentacaoForm) actionForm;
	//
	// ExDocumentoDao docDao = fabrica.createExDocumentoDao();
	//
	// DpPessoa pessoa = new DpPessoa();
	// pessoa.setSigla(request.getParameter("sigla").toUpperCase());
	//
	// /*
	// * pessoa = pessoaDao.consultarPorSigla(pessoa);
	// *
	// * if (pessoa == null) {
	// * form.setNome(request.getParameter("sigla").toUpperCase()); List
	// * pessoas = pessoaDao.consultarPorFiltro(form); if (pessoas != null) if
	// * (pessoas.size() == 1) pessoa = (DpPessoa)pessoas.get(0); }
	// */
	// if (pessoa == null)
	// return mapping.findForward("ajax_vazio");
	//
	// request.setAttribute("id", pessoa.getId());
	// request.setAttribute("sigla", pessoa.getSigla());
	// request.setAttribute("descricao", pessoa.getDescricao());
	//
	// return mapping.findForward("ajax_retorno");
	// }
	//
	//

	public void setDescrMov(final String descrMov) {
		this.descrMov = descrMov;
	}

	public void setDestinoFinalSel(final DpPessoaSelecao destinoFinalSel) {
		this.destinoFinalSel = destinoFinalSel;
	}

	public void setDoc(final ExDocumento doc) {
		this.doc = doc;
	}

	public void setDocumentoRefSel(final ExMobilSelecao documentoRefSel) {
		this.documentoRefSel = documentoRefSel;
	}

	public void setClassificacaoSel(
			final ExClassificacaoSelecao classificacaoSel) {
		this.classificacaoSel = classificacaoSel;
	}

	public void setDocumentoSel(final ExDocumentoSelecao documentoSel) {
		this.documentoSel = documentoSel;
	}

	public void setDocumentoViaSel(final ExMobilSelecao documentoViaSel) {
		this.documentoViaSel = documentoViaSel;
	}

	public void setDtMovString(final String dtMovString) {
		this.dtMovString = dtMovString;
	}

	public void setDtDevolucaoMovString(final String dtDevolucaoMovString) {
		this.dtDevolucaoMovString = dtDevolucaoMovString;
	}

	public void setDtRegMov(final String dtRegMov) {
		this.dtRegMov = dtRegMov;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public void setIdResp(final Long idResp) {
		this.idResp = idResp;
	}

	public void setIdTpDespacho(final Long idTpDespacho) {
		this.idTpDespacho = idTpDespacho;
	}

	public void setItens(final List itens) {
		this.itens = itens;
	}

	public void setLotaDestinoFinalSel(
			final DpLotacaoSelecao lotaDestinoFinalSel) {
		this.lotaDestinoFinalSel = lotaDestinoFinalSel;
	}

	public void setLotaResponsavelSel(final DpLotacaoSelecao lotaResponsavelSel) {
		this.lotaResponsavelSel = lotaResponsavelSel;
	}

	public void setMov(final ExMovimentacao mov) {
		this.mov = mov;
	}

	public void setNumViaDocBusca(final Integer numViaDocBusca) {
		this.numViaDocBusca = numViaDocBusca;
	}

	public void setObsOrgao(final String obsOrgao) {
		this.obsOrgao = obsOrgao;
	}

	public void setOrgaoExterno(final CpOrgao orgaoExterno) {
		this.orgaoExterno = orgaoExterno;
	}

	@Override
	public void setPostback(final Integer postback) {
		this.postback = postback;
	}

	public void setResponsavel(final String responsavel) {
		this.responsavel = responsavel;
	}

	public void setResponsavelSel(final DpPessoaSelecao responsavelSel) {
		this.responsavelSel = responsavelSel;
	}

	public void setSubscritorSel(final DpPessoaSelecao subscritorSel) {
		this.subscritorSel = subscritorSel;
	}

	public void setTipoDestinatario(final Integer tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
	}

	public void setTipoDestinoFinal(final Integer tipoDestinoFinal) {
		this.tipoDestinoFinal = tipoDestinoFinal;
	}

	public void setTipoResponsavel(final Integer tipoResponsavel) {
		this.tipoResponsavel = tipoResponsavel;
	}

	public void setVia(final Integer via) {
		this.via = via;
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

	public String getNmArqMod() {
		return "despacho_mov.jsp";
	}

	public String getDespachoTexto() throws UnsupportedEncodingException {
		if (mov != null) {
			if (mov.getExTipoDespacho() != null) {
				return mov.getExTipoDespacho().getDescTpDespacho();
			} else if (mov.getDescrMov() != null) {
				return mov.getDescrMov();
			} else if (conteudo != null) {
				return conteudo;
			}
		}
		return null;
	}

	public String getDespachoHtml() throws UnsupportedEncodingException {
		if (mov != null) {
			if (mov.getConteudoBlobHtmlString() != null) {
				return mov.getConteudoBlobHtmlString();
			}
		}
		return null;
	}

	public DpPessoaSelecao getCosignatarioSel() {
		return cosignatarioSel;
	}

	public void setCosignatarioSel(DpPessoaSelecao cosignatarioSel) {
		this.cosignatarioSel = cosignatarioSel;
	}

	public String getFuncaoCosignatario() {
		return funcaoCosignatario;
	}

	public void setFuncaoCosignatario(String funcaoCosignatario) {
		this.funcaoCosignatario = funcaoCosignatario;
	}

	public boolean isSubstituicao() {
		return substituicao;
	}

	public void setSubstituicao(boolean substituicao) {
		this.substituicao = substituicao;
	}

	public DpPessoaSelecao getTitularSel() {
		return titularSel;
	}

	public void setTitularSel(DpPessoaSelecao titularSel) {
		this.titularSel = titularSel;
	}

	public String getNmFuncaoSubscritor() {
		return nmFuncaoSubscritor;
	}

	public void setNmFuncaoSubscritor(String nmFuncaoSubscritor) {
		this.nmFuncaoSubscritor = nmFuncaoSubscritor;
	}

	public String getIdDocumentoPaiExterno() {
		return idDocumentoPaiExterno;
	}

	public void setIdDocumentoPaiExterno(String idDocumentoPaiExterno) {
		this.idDocumentoPaiExterno = idDocumentoPaiExterno;
	}

	public String getIdDocumentoEscolha() {
		return idDocumentoEscolha;
	}

	public void setIdDocumentoEscolha(String idDocumentoEscolha) {
		this.idDocumentoEscolha = idDocumentoEscolha;
	}

	public String getDtDispon() {
		return dtDispon;
	}

	public void setDtDispon(String dtDispon) {
		this.dtDispon = dtDispon;
	}

	public String aBoletimAgendar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		if (doc.getExNivelAcesso().getGrauNivelAcesso() != ExNivelAcesso.NIVEL_ACESSO_PUBLICO)

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

		return Action.SUCCESS;
	}

	public String aBoletimPublicar() throws Exception {
		assertAcesso("");
		buscarDocumento(true);

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			setDtPubl(df.format(new Date()));
		} catch (final Exception e) {
		}

		if (!Ex.getInstance().getComp()
				.podePublicar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Publicação não permitida");

		return Action.SUCCESS;

	}

	public List<ExDocumento> getItensSolicitados() {
		return itensSolicitados;
	}

	public void setItensSolicitados(List<ExDocumento> itensSolicitados) {
		this.itensSolicitados = itensSolicitados;
	}

	public String getDtPrevPubl() {
		return dtPrevPubl;
	}

	public void setDtPrevPubl(String dtPrevPubl) {
		this.dtPrevPubl = dtPrevPubl;
	}

	public String getDescrFeriado() {
		return descrFeriado;
	}

	public void setDescrFeriado(String descrFeriado) {
		this.descrFeriado = descrFeriado;
	}

	public String getDtPubl() {
		return dtPubl;
	}

	public void setDtPubl(String dtPubl) {
		this.dtPubl = dtPubl;
	}

	public String getTipoMateria() {
		return tipoMateria;
	}

	public void setTipoMateria(String tipoMateria) {
		this.tipoMateria = tipoMateria;
	}

	public boolean isFinalizar() {
		return finalizar;
	}

	public void setFinalizar(boolean finalizar) {

		this.finalizar = finalizar;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public ExMobil getMob() {
		return mob;
	}

	public void setMob(ExMobil mob) {
		this.mob = mob;
	}

	public Long getIdMob() {
		return idMob;
	}

	public void setIdMob(Long idMob) {
		this.idMob = idMob;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public List<ExPapel> getListaExPapel() {
		return (List<ExPapel>) HibernateUtil.getSessao()
				.createQuery("from ExPapel").list();
	}

	public Long getIdPapel() {
		return idPapel;
	}

	public void setIdPapel(Long idPapel) {
		this.idPapel = idPapel;
	}

	public boolean isCadernoDJEObrigatorio() {
		return cadernoDJEObrigatorio;
	}

	public void setCadernoDJEObrigatorio(boolean cadernoDJEObrigatorio) {
		this.cadernoDJEObrigatorio = cadernoDJEObrigatorio;
	}

	public Boolean getCopia() {
		return copia;
	}

	public void setCopia(Boolean copia) {
		this.copia = copia;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ExModelo getModelo() {
		return dao().consultarExModelo(null, "Despacho Automático");
	}

	public Set<DpLotacao> getListaLotPubl() throws Exception {

		Set<DpLotacao> lotacoes = new HashSet<DpLotacao>();
		DpLotacao lotSubscritor, lotCadastrante, lotTitular, lotFiltro;
		String siglaSubscritor, siglaCadastrante, siglaTitular;

		siglaSubscritor = PublicacaoDJEBL.obterUnidadeDocumento(this.doc);
		siglaCadastrante = getCadastrante().getLotacao().getSigla();
		siglaTitular = getLotaTitular().getSigla();
		lotFiltro = new DpLotacao();

		lotFiltro.setOrgaoUsuario(this.doc.getOrgaoUsuario());
		lotFiltro.setSigla(siglaSubscritor);
		lotSubscritor = dao().consultarPorSigla(lotFiltro);

		lotacoes.add(lotSubscritor);

		if (!siglaSubscritor.equals(siglaCadastrante)
				&&

				getCadastrante().getOrgaoUsuario().getId()
						.equals(this.doc.getOrgaoUsuario().getId())) {
			lotFiltro.setSigla(siglaCadastrante);
			lotCadastrante = dao().consultarPorSigla(lotFiltro);
			lotacoes.add(lotCadastrante);
		}

		if (!siglaSubscritor.equals(siglaTitular)
				&& !siglaCadastrante.equals(siglaTitular)
				&& (

				(getTitular().getOrgaoUsuario().getId().equals(this.doc
						.getOrgaoUsuario().getId()))

				|| getLotaTitular().getOrgaoUsuario().getId()
						.equals(this.doc.getOrgaoUsuario().getId()))) {
			lotFiltro.setSigla(siglaTitular);
			lotTitular = dao().consultarPorSigla(lotFiltro);
			lotacoes.add(lotTitular);
		}

		this.setIdLotDefault(lotSubscritor.getId());

		return lotacoes;
	}

	public String getNomeUsuarioSubscritor() {
		return nomeUsuarioSubscritor;
	}

	public void setNomeUsuarioSubscritor(String nomeUsuarioSubscritor) {
		this.nomeUsuarioSubscritor = nomeUsuarioSubscritor;
	}

	public String getSenhaUsuarioSubscritor() {
		return senhaUsuarioSubscritor;
	}

	public void setSenhaUsuarioSubscritor(String senhaUsuarioSubscritor) {
		this.senhaUsuarioSubscritor = senhaUsuarioSubscritor;
	}

	public String getTipoAssinaturaMov() {
		return tipoAssinaturaMov;
	}

	public void setTipoAssinaturaMov(String tipoAssinaturaMov) {
		this.tipoAssinaturaMov = tipoAssinaturaMov;
	}

	public List<ExDocumento> getDocumentosQuePodemSerAssinadosComSenha() {
		return documentosQuePodemSerAssinadosComSenha;
	}

	public void setDocumentosQuePodemSerAssinadosComSenha(
			List<ExDocumento> documentosQuePodemSerAssinadosComSenha) {
		this.documentosQuePodemSerAssinadosComSenha = documentosQuePodemSerAssinadosComSenha;
	}

	public List<ExMovimentacao> getMovimentacoesQuePodemSerAssinadasComSenha() {
		return movimentacoesQuePodemSerAssinadasComSenha;
	}

	public void setMovimentacoesQuePodemSerAssinadasComSenha(
			List<ExMovimentacao> movimentacoesQuePodemSerAssinadasComSenha) {
		this.movimentacoesQuePodemSerAssinadasComSenha = movimentacoesQuePodemSerAssinadasComSenha;
	}

	public boolean isAutenticando() {
		return autenticando;
	}

	public void setAutenticando(boolean autenticando) {
		this.autenticando = autenticando;
	}

	public String getProximaDataDisponivelStr() {
		return proximaDataDisponivelStr;
	}

	public void setProximaDataDisponivelStr(String proximaDataDisponivelStr) {
		this.proximaDataDisponivelStr = proximaDataDisponivelStr;
	}
}
